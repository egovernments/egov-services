package org.egov.enc.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

public class AESUtil {

    public AESUtil(byte[] initialVector) {
        init();
    }

    //Initialize Security Provider to BouncyCastleProvider
    public static void init() { Security.addProvider(new BouncyCastleProvider()); }

    public static byte[] encrypt(byte[] plaintext, SecretKey secretKey, byte[] initialVector) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(initialVector));
        return cipher.doFinal(plaintext);
    }

    public static byte[] decrypt(byte[] ciphertext, SecretKey secretKey, byte[] initialVector) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(initialVector));
        return cipher.doFinal(ciphertext);
    }

}
