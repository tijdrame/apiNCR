package com.boa.api.web;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import com.boa.api.request.InwardRequest;
import com.boa.api.request.NcrInRequest;
import com.boa.api.response.InwardResponse;
import com.boa.api.response.NcrInResponse;
import com.boa.api.service.ApiService;
import com.boa.api.service.util.ICodeDescResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiResource {

    private final Logger log = LoggerFactory.getLogger(ApiResource.class);

    private final ApiService apiService;

    public ApiResource(ApiService apiService) {
        this.apiService = apiService;
    }

    /*@GetMapping ("/testCron")
    public String testCron(){
        apiService.ncrProcessing();
        return "ok";
    }*/

    @PostMapping("/NewInward")
    public ResponseEntity<InwardResponse> newInward(@RequestBody InwardRequest inwardRequest, HttpServletRequest request) {
        log.debug("REST request to NewInward : [{}]", inwardRequest);
        InwardResponse response = new InwardResponse();
        if (controleParam(inwardRequest.getCreditorAccount()) || controleParam(inwardRequest.getAmount())
                || controleParam(inwardRequest.getCurrency()) || controleParam(inwardRequest.getTransfertDate())
                || controleParam(inwardRequest.getDescription()) || controleParam(inwardRequest.getLanguage())
                || controleParam(inwardRequest.getSameUser()) || controleParam(inwardRequest.getIsIn()) ) {
            response.setCode(ICodeDescResponse.PARAM_ABSENT_CODE);
            response.setDateResponse(Instant.now());
            response.setDescription(ICodeDescResponse.PARAM_DESCRIPTION);
            return ResponseEntity.badRequest().header("Authorization", request.getHeader("Authorization"))
                    .body(response);
        }
        if(!inwardRequest.getCurrency().equalsIgnoreCase("ghs")){
            response.setCode(ICodeDescResponse.PARAM_ABSENT_CODE);
            response.setDateResponse(Instant.now());
            response.setDescription(ICodeDescResponse.DEVISE_ATTENDU_GSH);
            return ResponseEntity.badRequest().header("Authorization", request.getHeader("Authorization"))
                    .body(response);
        }
        response = apiService.newInward(inwardRequest, request);

        return ResponseEntity.ok().header("Authorization", request.getHeader("Authorization")).body(response);
    }

    @PostMapping("/ncrIn")
    public ResponseEntity<NcrInResponse> ncrIn(@RequestBody NcrInRequest ncrRequest, HttpServletRequest request) {
        log.debug("REST request to NewInward : [{}]", ncrRequest);
        NcrInResponse response = new NcrInResponse();
        if (controleParam(ncrRequest.getNooper()) ) {
            response.setCode(ICodeDescResponse.PARAM_ABSENT_CODE);
            response.setDateResponse(Instant.now());
            response.setDescription(ICodeDescResponse.PARAM_DESCRIPTION);
            return ResponseEntity.badRequest().header("Authorization", request.getHeader("Authorization"))
                    .body(response);
        }
        response = apiService.ncrIn(ncrRequest, request);

        return ResponseEntity.ok().header("Authorization", request.getHeader("Authorization")).body(response);
    }

    private Boolean controleParam(Object param) {
        Boolean flag = false;
        if (StringUtils.isEmpty(param))
            flag = true;
        return flag;
    }

    
}
