package org.egov.enc.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Ciphertext {

    private String method;

    private int keyId;

    private String ciphertext;

    public Ciphertext(String ciphertext) {
        String[] cipherArray = ciphertext.split("\\|");
        method = cipherArray[0];
        keyId = Integer.parseInt(cipherArray[1]);
        this.ciphertext = cipherArray[2];
    }

    @Override
    public String toString() {
        return method + "|" + String.valueOf(keyId) + "|" + ciphertext;
    }

}
