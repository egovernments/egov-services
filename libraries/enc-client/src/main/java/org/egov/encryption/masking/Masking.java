package org.egov.encryption.masking;

public abstract class Masking {

    public String maskingTechnique;

    public abstract String maskData(String data);

}
