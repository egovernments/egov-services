package org.egov.pgr.model;

public class SmsMessage {

    private String mobileNumber = null;

    private String message = null;

    public SmsMessage(String mobileNumber, String message) {
        this.mobileNumber = mobileNumber;
        this.message = message;
    }
}
