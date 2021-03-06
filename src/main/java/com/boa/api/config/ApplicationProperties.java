package com.boa.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Api NCR.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
    private String userName;
    private String presentingBankRoutNumber;
    private String userID;
    private String sessionID;
    private String urlNewOutward;
    private String userAuth;
    private String password;
    private Integer timeOut;

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPresentingBankRoutNumber() {
        return this.presentingBankRoutNumber;
    }

    public void setPresentingBankRoutNumber(String presentingBankRoutNumber) {
        this.presentingBankRoutNumber = presentingBankRoutNumber;
    }

    public String getUserID() {
        return this.userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getSessionID() {
        return this.sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getUrlNewOutward() {
        return this.urlNewOutward;
    }

    public void setUrlNewOutward(String urlNewOutward) {
        this.urlNewOutward = urlNewOutward;
    }

    public String getUserAuth() {
        return this.userAuth;
    }

    public void setUserAuth(String userAuth) {
        this.userAuth = userAuth;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getTimeOut() {
        return this.timeOut;
    }

    public void setTimeOut(Integer timeOut) {
        this.timeOut = timeOut;
    }

}
