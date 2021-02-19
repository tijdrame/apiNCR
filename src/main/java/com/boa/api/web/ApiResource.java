package com.boa.api.web;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import com.boa.api.request.InwardRequest;
import com.boa.api.response.InwardResponse;
import com.boa.api.service.ApiService;
import com.boa.api.service.util.ICodeDescResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
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

    @PostMapping("/NewInward")
    public ResponseEntity<InwardResponse> newInward(@RequestBody InwardRequest inwardRequest, HttpServletRequest request) {
        log.debug("REST request to NewInward : [{}]", inwardRequest);
        InwardResponse response = new InwardResponse();
        if (controleParam(inwardRequest.getCreditorAccount()) || controleParam(inwardRequest.getAmount())
                || controleParam(inwardRequest.getCurrency()) || controleParam(inwardRequest.getTransfertDate())
                || controleParam(inwardRequest.getDescription()) || controleParam(inwardRequest.getLanguage())
                || controleParam(inwardRequest.getSameUser()) ) {
            response.setCode(ICodeDescResponse.PARAM_ABSENT_CODE);
            response.setDateResponse(Instant.now());
            response.setDescription(ICodeDescResponse.PARAM_DESCRIPTION);
            return ResponseEntity.badRequest().header("Authorization", request.getHeader("Authorization"))
                    .body(response);
        }
        response = apiService.newInward(inwardRequest, request);
        return ResponseEntity.ok().header("Authorization", request.getHeader("Authorization")).body(response);
    }

    private Boolean controleParam(Object param) {
        Boolean flag = false;
        if (StringUtils.isEmpty(param))
            flag = true;
        return flag;
    }

    
}
