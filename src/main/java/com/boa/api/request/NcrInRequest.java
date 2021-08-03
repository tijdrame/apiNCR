package com.boa.api.request;

public class NcrInRequest {
    private String nooper;

    public NcrInRequest() {
    }

    public NcrInRequest(String nooper) {
        this.nooper = nooper;
    }

    public String getNooper() {
        return this.nooper;
    }

    public void setNooper(String nooper) {
        this.nooper = nooper;
    }

    public NcrInRequest nooper(String nooper) {
        setNooper(nooper);
        return this;
    }

    @Override
    public String toString() {
        return "{" +
            " nooper='" + getNooper() + "'" +
            "}";
    }

}
