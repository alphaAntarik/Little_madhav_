package com.etechnie.littlemadhav.models.login_model;



import java.io.Serializable;

public class ResponseLogin implements Serializable {
    private String accessToken;
    private String validFrom;
    private String validTo;



    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }

    public String getValidTo() {
        return validTo;
    }

    public void setValidTo(String validTo) {
        this.validTo = validTo;
    }
}
