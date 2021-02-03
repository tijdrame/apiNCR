package com.boa.api.web;

import javax.servlet.http.HttpServletRequest;

import com.boa.api.service.ApiService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
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

    /*@PostMapping("/oAuth")
    public ResponseEntity<OAuthResponse> oAuth(@RequestBody OAuthRequest authRequest, HttpServletRequest request) {
        log.debug("REST request to assetFin : [{}]", authRequest);
        OAuthResponse response = new OAuthResponse();
        if (controleParam(authRequest.getCountry()) || controleParam(authRequest.getLogin())
                || controleParam(authRequest.getPassword()) || controleParam(authRequest.getLangue())) {
            Locale locale = defineLocale(authRequest.getLangue());
            response.setCode(ICodeDescResponse.PARAM_ABSENT_CODE);
            response.setDateResponse(Instant.now());
            response.setDescription(messageSource.getMessage("param.oblig", null, locale));
            return ResponseEntity.badRequest().header("Authorization", request.getHeader("Authorization"))
                    .body(response);
        }
        response = apiService.oAuth(authRequest, request);
        return ResponseEntity.ok().header("Authorization", request.getHeader("Authorization")).body(response);
    }*/

    
}
