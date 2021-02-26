package com.boa.api.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.boa.api.config.ApplicationProperties;
import com.boa.api.domain.ParamEndPoint;
import com.boa.api.domain.ParamGeneral;
import com.boa.api.domain.Tracking;
import com.boa.api.request.InwardRequest;
import com.boa.api.request.NCRRequest;
import com.boa.api.response.InwardResponse;
import com.boa.api.response.NCRResponse;
import com.boa.api.service.util.ICodeDescResponse;
import com.boa.api.service.util.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional
public class ApiService {

    private final Logger log = LoggerFactory.getLogger(ApiService.class);
    private final TrackingService trackingService;
    private final UserService userService;
    private final Utils utils;
    private final ParamEndPointService endPointService;
    private final ApplicationProperties applicationProperties;
    private final ParamGeneralService paramGeneralService;

    public ApiService(TrackingService trackingService, UserService userService, Utils utils,
            ParamEndPointService endPointService, ApplicationProperties applicationProperties,
            ParamGeneralService paramGeneralService) {
        this.trackingService = trackingService;
        this.userService = userService;
        this.utils = utils;
        this.endPointService = endPointService;
        this.applicationProperties = applicationProperties;
        this.paramGeneralService = paramGeneralService;
    }

    @Scheduled(cron = "0 0/45 * * * ?")
    // @Scheduled(cron = "0 0 12 * * ?")
    public void ncrProcessing() {
        log.info("Enter in ncrProcessing===[{}]", Instant.now());

        // NCRResponse genericResp = new NCRResponse();
        Tracking tracking = new Tracking();
        tracking.setDateRequest(Instant.now());

        try {
            Optional<ParamEndPoint> endPoint = endPointService.findByCodeParam("ncrPaye");
            if (!endPoint.isPresent()) {
                tracking = createTracking(tracking, ICodeDescResponse.ECHEC_CODE, "ncrPaye", "End point non paramétré",
                        "CRON du " + Instant.now(), "");
                trackingService.save(tracking);
            }
            Optional<ParamEndPoint> inEndPoint = endPointService.findByCodeParam("ncrIn");
            if (!inEndPoint.isPresent()) {
                tracking = createTracking(tracking, ICodeDescResponse.ECHEC_CODE, "ncrIn", "End point non paramétré",
                        "CRON du " + Instant.now(), "");
                trackingService.save(tracking);
            }

            String jsonStr = new JSONObject().put("param1", "1111").put("param2", "2222").put("param3", "3333")
                    .put("param4", "4444").toString();
            HttpURLConnection conn = utils.doConnexion(endPoint.get().getEndPoints(), jsonStr, "application/json", null,
                    null, false);
            BufferedReader br = null;
            JSONObject obj = new JSONObject();
            String result = "";
            log.info("resp code envoi ncrPaye [{}]", conn.getResponseCode());
            if (conn != null && conn.getResponseCode() == 200) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String ligne = br.readLine();
                while (ligne != null) {
                    result += ligne;
                    ligne = br.readLine();
                }
                log.info("ncrPaye result ===== [{}]", result);
                obj = new JSONObject(result);
                JSONArray jsonArray = null;
                JSONObject jsonObject = null;
                ObjectMapper mapper = new ObjectMapper();
                if (obj.toString() != null && !obj.isNull("data") && !obj.getJSONObject("data").isNull("outward")) {
                    NCRRequest ncrRequest = new NCRRequest();

                    String numGen = RandomStringUtils.randomNumeric(15, 15);

                    if (obj.getJSONObject("data").get("outward") instanceof JSONArray) {
                        jsonArray = obj.getJSONObject("data").getJSONArray("outward");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            ncrRequest = constructRequest(jsonArray.getJSONObject(i));
                            if (ncrRequest == null)
                                continue;
                            ncrRequest.setUserName(applicationProperties.getUserName());
                            ncrRequest.setSessionID(applicationProperties.getSessionID());
                            ncrRequest.setUserID(applicationProperties.getUserID());
                            ncrRequest.setPresentingBankRoutNumber(applicationProperties.getPresentingBankRoutNumber());

                            ncrRequest.setBatchNumber(numGen);
                            ncrRequest.setItemSequenceNumber(numGen);
                            ncrRequest.setSerialNumber(numGen.substring(0, 6));

                            // String json = mapper.writeValueAsString(ncrRequest);
                            String json = new JSONObject().put("Branch", ncrRequest.getBranchCode())
                                    .put("UserName", ncrRequest.getUserName())
                                    .put("BatchNumber", ncrRequest.getBatchNumber())
                                    .put("ItemSequenceNumber", ncrRequest.getItemSequenceNumber())
                                    .put("PayorBankRoutNumber", ncrRequest.getPayorBankRoutNumber())
                                    .put("Amount", ncrRequest.getAmount())
                                    .put("AccountNumber", ncrRequest.getAccountNumber())
                                    .put("SerialNumber", ncrRequest.getSerialNumber())
                                    .put("PresentingBankRoutNumber", ncrRequest.getPresentingBankRoutNumber())
                                    .put("PayerName", ncrRequest.getPayeeName())
                                    .put("TransactionDetails", ncrRequest.getTransactionDetails())
                                    .put("DepositorAccountNumber", ncrRequest.getDepositorAccountNumber())
                                    .put("PayeeName", ncrRequest.getPayeeName()).put("UserID", ncrRequest.getUserID())
                                    .put("UserBranch", ncrRequest.getUserBranch())
                                    .put("SessionID", ncrRequest.getSessionID()).toString();
                            log.info("before calling callNcrPay [{}]", json);
                            Boolean res = callNcrPay(json);

                            if (res) {
                                // TODO traitement resp positive de ncr
                                jsonStr = new JSONObject().put("nooper", ncrRequest.getNooper()).put("param2", "2222")
                                        .put("param3", "3333").put("param4", "4444").toString();
                                utils.doConnexion(inEndPoint.get().getEndPoints(), jsonStr, "application/json", null,
                                        null, false);
                            } else {
                                // TODO resp negative
                            }
                        }
                    } else if (obj.getJSONObject("data").get("outward") instanceof JSONObject) {
                        jsonObject = obj.getJSONObject("data").getJSONObject("outward");
                        ncrRequest = constructRequest(jsonObject);
                        if (ncrRequest == null)
                            return;
                        ncrRequest.setUserName(applicationProperties.getUserName());
                        ncrRequest.setSessionID(applicationProperties.getSessionID());
                        ncrRequest.setUserID(applicationProperties.getUserID());
                        ncrRequest.setPresentingBankRoutNumber(applicationProperties.getPresentingBankRoutNumber());

                        ncrRequest.setBatchNumber(numGen);
                        ncrRequest.setItemSequenceNumber(numGen);
                        ncrRequest.setSerialNumber(numGen.substring(0, 6));

                        String json = new JSONObject().put("Branch", ncrRequest.getBranchCode())
                                    .put("UserName", ncrRequest.getUserName())
                                    .put("BatchNumber", ncrRequest.getBatchNumber())
                                    .put("ItemSequenceNumber", ncrRequest.getItemSequenceNumber())
                                    .put("PayorBankRoutNumber", ncrRequest.getPayorBankRoutNumber())
                                    .put("Amount", ncrRequest.getAmount())
                                    .put("AccountNumber", ncrRequest.getAccountNumber())
                                    .put("SerialNumber", ncrRequest.getSerialNumber())
                                    .put("PresentingBankRoutNumber", ncrRequest.getPresentingBankRoutNumber())
                                    .put("PayerName", ncrRequest.getPayeeName())
                                    .put("TransactionDetails", ncrRequest.getTransactionDetails())
                                    .put("DepositorAccountNumber", ncrRequest.getDepositorAccountNumber())
                                    .put("PayeeName", ncrRequest.getPayeeName()).put("UserID", ncrRequest.getUserID())
                                    .put("UserBranch", ncrRequest.getUserBranch())
                                    .put("SessionID", ncrRequest.getSessionID()).toString();
                        log.info("before calling callNcrPay [{}]", json);

                        // json = mapper.writeValueAsString(ncrRequest);
                        Boolean res = callNcrPay(json);

                        if (res) {
                            // TODO traitement resp positive de ncr
                            jsonStr = new JSONObject().put("nooper", ncrRequest.getNooper()).put("param2", "2222")
                                    .put("param3", "3333").put("param4", "4444").toString();
                            utils.doConnexion(inEndPoint.get().getEndPoints(), jsonStr, "application/json", null, null,
                                    false);
                        } else {
                            // TODO resp negative
                        }
                    }

                    tracking = createTracking(tracking, ICodeDescResponse.SUCCES_CODE, "ncrProcessing",
                            "SUCCES response wso2", "CRON du " + Instant.now(), "");
                } else {
                    // cron resp != 200

                    tracking = createTracking(tracking, ICodeDescResponse.ECHEC_CODE, "ncrProcessing",
                            "ECHEC response wso2 <> 200", "CRON du " + Instant.now(), "");
                    tracking.setResponseTr(result);
                }
            } else {
                /*
                 * br = new BufferedReader(new InputStreamReader(conn.getErrorStream())); String
                 * ligne = br.readLine(); while (ligne != null) { result += ligne; ligne =
                 * br.readLine(); } log.info("resp envoi error ===== [{}]", result); obj = new
                 * JSONObject(result); obj = new JSONObject(result);
                 */
                tracking = createTracking(tracking, ICodeDescResponse.ECHEC_CODE, "ncrProcessing",
                        "Connexeion impossible", "CRON du " + Instant.now(), "");
                tracking.setResponseTr(result);
            }
        } catch (Exception e) {
            log.error("Exception in ncrPaye [{}]", e.getMessage());
            tracking = createTracking(tracking, ICodeDescResponse.ECHEC_CODE, "ncrProcessing", "Connexeion impossible",
                    "CRON du " + Instant.now(), "");
        }
        trackingService.save(tracking);
        log.info("End CRON at =====================[{}]", Instant.now());
    }

    public Tracking createTracking(Tracking tracking, String code, String endPoint, String result, String req,
            String reqId) {
        // Tracking tracking = new Tracking();
        tracking.setRequestId(reqId);
        tracking.setCodeResponse(code);
        tracking.setDateResponse(Instant.now());
        tracking.setEndPoint(endPoint);
        tracking.setLoginActeur("x");
        tracking.setResponseTr(result);
        tracking.setRequestTr(req);
        tracking.setDateRequest(Instant.now());
        return tracking;
    }

    private Boolean callNcrPay(String jsonStr) {
        log.info("=============In callNcrPay [{}]", jsonStr);
        // http://10.120.71.11/ACHWebApi/api/ach/NewOutward
        HttpURLConnection conn;
        try {
            conn = utils.doConnexion(applicationProperties.getUrlNewOutward(), jsonStr, "application/json", null, null,
                    true);
            log.info("resp code envoi callNcrPay [{}]", conn.getResponseCode());
            String result ="";
            BufferedReader br = null;
            if (conn != null && conn.getResponseCode() == 200) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String ligne = br.readLine();
                while (ligne != null) {
                    result += ligne;
                    ligne = br.readLine();
                }
                log.info("callNcrPay result ===== [{}]", result);
                Tracking tracking = new Tracking();
                tracking.setRequestId("");
                tracking.setCodeResponse("200");
                tracking.setDateResponse(Instant.now());
                tracking.setEndPoint("callNcrPay");
                tracking.setLoginActeur("x");
                tracking.setResponseTr("OK");
                tracking.setRequestTr(jsonStr);
                trackingService.save(tracking);
                return true;
            }else if(conn!=null){
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                String ligne = br.readLine();
                while (ligne != null) {
                    result += ligne;
                    ligne = br.readLine();
                }
                log.info("callNcrPay result ===== [{}]", result);
                Tracking tracking = new Tracking();
            tracking.setRequestId("");
            tracking.setCodeResponse("402");
            tracking.setDateResponse(Instant.now());
            tracking.setEndPoint("callNcrPay");
            tracking.setLoginActeur("x");
            tracking.setResponseTr("KO");
            tracking.setRequestTr(jsonStr);
            tracking.setResponseTr(result);
            trackingService.save(tracking);
            }
        } catch (IOException e) {
            log.error("Erreur sur callNcrPay [{}]", e);
            Tracking tracking = new Tracking();
            tracking.setRequestId("");
            tracking.setCodeResponse("402");
            tracking.setDateResponse(Instant.now());
            tracking.setEndPoint("callNcrPay");
            tracking.setLoginActeur("x");
            tracking.setResponseTr("KO");
            tracking.setRequestTr(jsonStr);
            tracking.setResponseTr(e.getMessage());
            trackingService.save(tracking);
        }
        return false;
    }

    public InwardResponse newInward(InwardRequest inwardRequest, HttpServletRequest request) {
        log.info("Enter in inwardRequest=== [{}]", inwardRequest);

        InwardResponse genericResp = new InwardResponse();
        Tracking tracking = new Tracking();
        tracking.setDateRequest(Instant.now());

        Optional<ParamEndPoint> endPoint = endPointService.findByCodeParam("newInward");
        if (!endPoint.isPresent()) {
            genericResp.setCode(ICodeDescResponse.ECHEC_CODE);
            genericResp.setDescription(ICodeDescResponse.SERVICE_ABSENT_DESC);
            genericResp.setDateResponse(Instant.now());
            tracking = createTracking(tracking, ICodeDescResponse.ECHEC_CODE, "newInward", genericResp.toString(),
                    inwardRequest.toString(), genericResp.getResponseReference());
            trackingService.save(tracking);
            return genericResp;
        }

        if (StringUtils.isEmpty(inwardRequest.getDebitorAccount())) {
            Optional<ParamGeneral> debitAccount = paramGeneralService.findByCode("debitorAccount");
            if (!debitAccount.isPresent()) {
                genericResp.setCode(ICodeDescResponse.ECHEC_CODE);
                genericResp.setDescription(ICodeDescResponse.DEBIT_ACCOUNT_ABSENT);
                genericResp.setDateResponse(Instant.now());
                tracking = createTracking(tracking, ICodeDescResponse.ECHEC_CODE, "newInward", genericResp.toString(),
                        inwardRequest.toString(), genericResp.getResponseReference());
                trackingService.save(tracking);
                return genericResp;
            }
            inwardRequest.setDebitorAccount(debitAccount.get().getVarString1());
        }

        try {
            String jsonStr = new JSONObject().put("cptDeb", inwardRequest.getDebitorAccount())
                    .put("cptCred", inwardRequest.getCreditorAccount()).put("amount", inwardRequest.getAmount())
                    .put("devise", inwardRequest.getCurrency()).put("dateTrf", inwardRequest.getTransfertDate())
                    .put("motif", inwardRequest.getDescription()).put("language", inwardRequest.getLanguage())
                    .put("xmemeUser", inwardRequest.getSameUser()).toString();
            log.info("Requete newInward wso2 = [{}]", jsonStr);
            HttpURLConnection conn = utils.doConnexion(endPoint.get().getEndPoints(), jsonStr, "application/json", null,
                    null, false);
            BufferedReader br = null;
            JSONObject obj = new JSONObject();
            String result = "";
            log.info("resp code envoi newInward [{}]", (conn != null ? conn.getResponseCode() : ""));
            if (conn != null && conn.getResponseCode() == 200) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String ligne = br.readLine();
                while (ligne != null) {
                    result += ligne;
                    ligne = br.readLine();
                }
                // result = IOUtils.toString(conn.getInputStream(), "UTF-8");
                log.info("newInward result ===== [{}]", result);
                // if(result.contains(";")) result = result.replace(";", " ");
                obj = new JSONObject(result);
                obj = obj.getJSONObject("data");

                if (obj.toString() != null && !obj.isNull("responses")
                        && !obj.getJSONObject("responses").getString("response").contains("-1")) {
                    genericResp.setCode(ICodeDescResponse.SUCCES_CODE);
                    genericResp.setDescription(ICodeDescResponse.SUCCES_DESCRIPTION);
                    genericResp.setDateResponse(Instant.now());
                    obj = obj.getJSONObject("responses");
                    ObjectMapper mapper = new ObjectMapper();
                    Map<String, Object> map = mapper.readValue(obj.toString(), Map.class);
                    genericResp.setDataInward(map);
                    // genericResp.setUserCode(obj.getString("rucode"));
                    tracking = createTracking(tracking, ICodeDescResponse.SUCCES_CODE, request.getRequestURI(),
                            genericResp.toString(), inwardRequest.toString(), genericResp.getResponseReference());
                } else {
                    genericResp.setCode(ICodeDescResponse.ECHEC_CODE);
                    genericResp.setDateResponse(Instant.now());
                    genericResp.setDescription(ICodeDescResponse.ECHEC_DESCRIPTION);
                    ObjectMapper mapper = new ObjectMapper();
                    Map<String, Object> map = mapper.readValue(obj.toString(), Map.class);
                    genericResp.setDataInward(map);
                    tracking = createTracking(tracking, ICodeDescResponse.ECHEC_CODE, request.getRequestURI(),
                            genericResp.toString(), inwardRequest.toString(), genericResp.getResponseReference());
                }
            } else if (conn != null) {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                String ligne = br.readLine();
                while (ligne != null) {
                    result += ligne;
                    ligne = br.readLine();
                }
                log.info("resp envoi error ===== [{}]", result);
                obj = new JSONObject(result);

                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> map = mapper.readValue(result, Map.class);

                genericResp.setDataInward(map);
                genericResp.setCode(ICodeDescResponse.ECHEC_CODE);
                genericResp.setDateResponse(Instant.now());
                genericResp.setDescription(ICodeDescResponse.ECHEC_DESCRIPTION);
                tracking = createTracking(tracking, ICodeDescResponse.ECHEC_CODE, request.getRequestURI(),
                        genericResp.toString(), inwardRequest.toString(), genericResp.getResponseReference());
            } else {
                genericResp.setCode(ICodeDescResponse.ECHEC_CODE);
                genericResp.setDateResponse(Instant.now());
                genericResp.setDescription(ICodeDescResponse.ECHEC_DESCRIPTION);
                tracking = createTracking(tracking, ICodeDescResponse.ECHEC_CODE, request.getRequestURI(),
                        genericResp.toString(), inwardRequest.toString(), genericResp.getResponseReference());
            }
        } catch (Exception e) {
            log.error("Exception in newInward [{}]", e);
            genericResp.setCode(ICodeDescResponse.ECHEC_CODE);
            genericResp.setDateResponse(Instant.now());
            // genericResp.setDescription(ICodeDescResponse.ECHEC_DESCRIPTION + " " +
            // e.getMessage());
            genericResp.setDescription(ICodeDescResponse.ECHEC_DESCRIPTION + e.getMessage());
            tracking = createTracking(tracking, ICodeDescResponse.ECHEC_CODE, request.getRequestURI(),
                    genericResp.getDescription(), inwardRequest.toString(), genericResp.getResponseReference());
        }
        trackingService.save(tracking);
        return genericResp;
    }

    private NCRRequest constructRequest(JSONObject myObj) {
        NCRRequest ncrRequest = new NCRRequest();
        try {
            ncrRequest.branchCode(myObj.getString("AGENCE")).payorBankRoutNumber(myObj.getString("SCODE"))
                    .amount(myObj.getDouble("MNTTOTBE")).accountNumber(myObj.getString("RCOMPTE"))
                    .payerName(myObj.getString("NOMBQBE")).transactionDetails(myObj.getString("MOTIF"))
                    .depositorAccountNumber(myObj.getString("COMPTE")).payeeName(myObj.getString("DORDRED"))
                    .userBranch(myObj.getString("AGENCE")).nooper(myObj.getString("NOOPER"));
        } catch (JSONException e) {
            log.error("Exception in constructRequest [{}]", e);
            return null;
        }
        return ncrRequest;
    }

    /*
     * public static void main(String[] args) { String num =
     * RandomStringUtils.randomNumeric(15, 15); System.out.println("rand =" + num);
     * System.out.println("rand =" + num.substring(0, 6));
     * 
     * NCRRequest req = new NCRRequest(); req.setAccountNumber("xxx01");
     * ObjectMapper mapper = new ObjectMapper(); try { String json =
     * mapper.writeValueAsString(req); System.out.println("res= " + json);
     * System.out.println("date= " + Instant.now()); } catch
     * (JsonProcessingException e) { // TODO Auto-generated catch block
     * e.printStackTrace(); } }
     */

}
