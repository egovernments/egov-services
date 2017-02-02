package org.egov.pgr.contracts.sms;

public class SmsMessage {

    private String mobileNumber;
    private String message;

    public SmsMessage(String mobileNumber, String message) {
        this.mobileNumber = mobileNumber;
        this.message = message;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getMessage() {
        return message;
    }
}
