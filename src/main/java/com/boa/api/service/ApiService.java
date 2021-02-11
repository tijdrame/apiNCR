package com.boa.api.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.boa.api.config.ApplicationProperties;
import com.boa.api.domain.ParamEndPoint;
import com.boa.api.domain.Tracking;
import com.boa.api.request.NCRRequest;
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

@Service
@Transactional
public class ApiService {

    private final Logger log = LoggerFactory.getLogger(ApiService.class);
    private final TrackingService trackingService;
    private final UserService userService;
    private final Utils utils;
    private final ParamEndPointService endPointService;
    private final ApplicationProperties applicationProperties;

    public ApiService(TrackingService trackingService, UserService userService, Utils utils,
            ParamEndPointService endPointService, ApplicationProperties applicationProperties) {
        this.trackingService = trackingService;
        this.userService = userService;
        this.utils = utils;
        this.endPointService = endPointService;
        this.applicationProperties = applicationProperties;
    }

    @Scheduled(cron = "*/1800 * * * * ?")
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
            log.info("resp code envoi [{}]", conn.getResponseCode());
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
                if (obj.toString() != null && !obj.isNull("outward")) {
                    NCRRequest ncrRequest = new NCRRequest();

                    String numGen = RandomStringUtils.randomNumeric(15, 15);

                    if (obj.get("outward") instanceof JSONArray) {
                        jsonArray = obj.getJSONArray("outward");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            ncrRequest = constructRequest(jsonArray.getJSONObject(i));
                            if(ncrRequest==null) continue;
                            ncrRequest.setUserName(applicationProperties.getUserName());
                            ncrRequest.setSessionID(applicationProperties.getSessionID());
                            ncrRequest.setUserID(applicationProperties.getUserID());
                            ncrRequest.setPresentingBankRoutNumber(applicationProperties.getPresentingBankRoutNumber());

                            ncrRequest.setBatchNumber(numGen);
                            ncrRequest.setItemSequenceNumber(numGen);
                            ncrRequest.setSerialNumber(numGen.substring(0, 6));

                            String json = mapper.writeValueAsString(ncrRequest);
                            Boolean res = callNcrPay(json);

                            if (res) {
                                // TODO traitement resp positive de ncr
                                jsonStr = new JSONObject().put("nooper", ncrRequest.getNooper()).put("param2", "2222")
                                    .put("param3", "3333").put("param4", "4444").toString();
                            utils.doConnexion(inEndPoint.get().getEndPoints(), jsonStr, "application/json", null, null, false);
                            } else {
                                // TODO resp negative
                            }
                        }
                    } else if (obj.get("outward") instanceof JSONObject) {
                        jsonObject = obj.getJSONObject("outward");
                        ncrRequest = constructRequest(jsonObject);

                        String json = mapper.writeValueAsString(ncrRequest);
                        Boolean res = callNcrPay(json);

                        if (res) {
                            // TODO traitement resp positive de ncr
                            jsonStr = new JSONObject().put("nooper", ncrRequest.getNooper()).put("param2", "2222")
                                    .put("param3", "3333").put("param4", "4444").toString();
                            utils.doConnexion(inEndPoint.get().getEndPoints(), jsonStr, "application/json", null, null, false);
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
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                String ligne = br.readLine();
                while (ligne != null) {
                    result += ligne;
                    ligne = br.readLine();
                }
                log.info("resp envoi error ===== [{}]", result);
                obj = new JSONObject(result);
                obj = new JSONObject(result);
                tracking = createTracking(tracking, ICodeDescResponse.ECHEC_CODE, "ncrProcessing", result,
                        "CRON du " + Instant.now(), "");
                tracking.setResponseTr(result);
            }
        } catch (Exception e) {
            log.error("Exception in ncrPaye [{}]", e.getMessage());
            tracking = createTracking(tracking, ICodeDescResponse.ECHEC_CODE, "ncrProcessing", e.getMessage(),
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
        //tracking.setLoginActeur(userService.getUserWithAuthorities().get().getLogin());
        tracking.setResponseTr(result);
        tracking.setRequestTr(req);
        return tracking;
    }

    private Boolean callNcrPay(String jsonStr) {
        log.info("=============In callNcrPay [{}]", jsonStr);
        // http://10.120.71.11/ACHWebApi/api/ach/NewOutward
        HttpURLConnection conn;
        try {
            conn = utils.doConnexion(applicationProperties.getUrlNewOutward(), jsonStr, "application/json", null, null, true);
            log.info("resp code envoi [{}]", conn.getResponseCode());
            if (conn !=null && conn.getResponseCode() == 200) {
                Tracking tracking = new Tracking();
                tracking.setRequestId("");
                tracking.setCodeResponse("200");
                tracking.setDateResponse(Instant.now());
                tracking.setEndPoint("callNcrPay");
                //tracking.setLoginActeur(userService.getUserWithAuthorities().get().getLogin());
                tracking.setResponseTr("OK");
                tracking.setRequestTr(jsonStr);
                trackingService.save(tracking);
                return true;
            }
        } catch (IOException e) {
            log.error("Erreur sur callNcrPay [{}]", e);
            Tracking tracking = new Tracking();
                tracking.setRequestId("");
                tracking.setCodeResponse("402");
                tracking.setDateResponse(Instant.now());
                tracking.setEndPoint("callNcrPay");
                //tracking.setLoginActeur(userService.getUserWithAuthorities().get().getLogin());
                tracking.setResponseTr("KO");
                tracking.setRequestTr(jsonStr);
                tracking.setResponseTr(e.getMessage());
                trackingService.save(tracking);
        }
        return false;
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
