package org.egov.enc.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.*;

public class SignUtil {

    public SignUtil() { init(); }

    //Initialize Security Provider to BouncyCastleProvider
    public static void init() {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static byte[] hashAndSign(byte[] data, PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(data);
        return signature.sign();
    }


    public static boolean hashAndVerify(byte[] data, byte[] sign, PublicKey publicKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(data);
        return signature.verify(sign);
    }

}
