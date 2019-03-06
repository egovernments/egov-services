package org.egov.encryption.masking;

public class MobileMasking implements Masking {

    @Override
    public String getMaskingTechnique() {
        return "mobile";
    }

    @Override
    public <T> T maskData(T data) {
        String mobilleNumber = (String) data;
        return (T) ("XXXXXX" + mobilleNumber.substring(mobilleNumber.length() - 4));
    }
}
