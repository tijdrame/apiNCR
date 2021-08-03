package com.boa.api.response;

import java.util.Map;

public class NcrInResponse extends GenericResponse{
    private Map<String, Object> dataNcrIn;

    public NcrInResponse() {
    }

    public NcrInResponse(Map<String,Object> dataNcrIn) {
        this.dataNcrIn = dataNcrIn;
    }

    public Map<String,Object> getDataNcrIn() {
        return this.dataNcrIn;
    }

    public void setDataNcrIn(Map<String,Object> dataNcrIn) {
        this.dataNcrIn = dataNcrIn;
    }

    public NcrInResponse dataNcrIn(Map<String,Object> dataNcrIn) {
        setDataNcrIn(dataNcrIn);
        return this;
    }

    @Override
    public String toString() {
        return "{" +
            " dataNcrIn='" + getDataNcrIn() + "'" +
            "}";
    }
    
}
