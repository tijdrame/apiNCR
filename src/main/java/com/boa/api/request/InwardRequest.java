package com.boa.api.request;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class InwardRequest {
    private String debitorAccount;
    private String creditorAccount;
    private Double amount;
    private String currency;
    private String transfertDate;
    private String description;
    private String language;
    private String sameUser;
    private String isIn;
}
