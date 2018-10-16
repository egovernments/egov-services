package org.egov.enc.keymanagement;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.egov.enc.config.AppProperties;
import org.egov.enc.models.AsymmetricKey;
import org.egov.enc.models.SymmetricKey;
import org.egov.enc.utils.AESUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Base64;

@Component
public class KeyGenerator {

    private SecureRandom secureRandom;
    private byte[] masterInitialVector;
    private SecretKey masterKey;

    private AppProperties appProperties;

    @Autowired
    public KeyGenerator(AppProperties appProperties) throws NoSuchAlgorithmException, InvalidKeySpecException {
        this.appProperties = appProperties;

        Security.addProvider(new BouncyCastleProvider());
        secureRandom = new SecureRandom();

        initializeMasterKey();
    }

    private void initializeMasterKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String masterPassword = appProperties.getMasterPassword();

        char[] masterSalt = appProperties.getMasterSalt().toCharArray();
        byte[] salt = new byte[8];
        for(int i = 0; i < salt.length; i++) {
            salt[i] = (byte) masterSalt[i];
        }

        char[] masterIV = appProperties.getMasterInitialVector().toCharArray();
        masterInitialVector = new byte[16];
        for(int i = 0; i < masterInitialVector.length; i++) {
            masterInitialVector[i] = (byte) masterIV[i];
        }

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(masterPassword.toCharArray(), salt, 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        masterKey = new SecretKeySpec(tmp.getEncoded(), "AES");
    }


    private byte[] getRandomBytes(int size) {
        byte[] randomBytes = new byte[size];
        secureRandom.nextBytes(randomBytes);
        return randomBytes;
    }

    public ArrayList<SymmetricKey> generateSymmetricKeys(ArrayList<String> tenantIds) throws BadPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException {
        int numberOfKeys = tenantIds.size();
        SecretKey[] keys = new SecretKey[numberOfKeys];
        byte[][] initialVectors = new byte[numberOfKeys][16];
        for(int i = 0; i < numberOfKeys; i++) {
            keys[i] = new SecretKeySpec(getRandomBytes(256/8), "AES");
            initialVectors[i] = getRandomBytes(16);
        }

        ArrayList<SymmetricKey> symmetricKeyArrayList = new ArrayList<SymmetricKey>();

        for(int i = 0; i < keys.length; i++) {
            String keyAsString = encryptWithMasterPassword(Base64.getEncoder().encodeToString(keys[i].getEncoded()));
            String initialVectorAsString = encryptWithMasterPassword(Base64.getEncoder().encodeToString(initialVectors[i]));
            symmetricKeyArrayList.add(new SymmetricKey(i, keyAsString, initialVectorAsString, true, tenantIds.get(i)));
        }
        return symmetricKeyArrayList;
    }


    public ArrayList<AsymmetricKey> generateAsymmetricKeys(ArrayList<String> tenantIds) throws NoSuchAlgorithmException, BadPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException {
        int numberOfKeys = tenantIds.size();
        KeyPair[] keys = new KeyPair[numberOfKeys];
        KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");
        keyGenerator.initialize(1024);
        for(int i = 0; i < numberOfKeys; i++) {
            keys[i] = keyGenerator.generateKeyPair();
        }

        ArrayList<AsymmetricKey> asymmetricKeyArrayList = new ArrayList<AsymmetricKey>();

        for(int i = 0; i < keys.length; i++) {
            String publicKey = encryptWithMasterPassword(Base64.getEncoder().encodeToString(keys[i].getPublic().getEncoded()));
            String privateKey = encryptWithMasterPassword(Base64.getEncoder().encodeToString(keys[i].getPrivate().getEncoded()));
            asymmetricKeyArrayList.add(new AsymmetricKey(i, publicKey, privateKey, true, tenantIds.get(i)));
        }

        return asymmetricKeyArrayList;
    }

    private String encryptWithMasterPassword(String key) throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException {
        byte[] encryptedKey = AESUtil.encrypt(key.getBytes(StandardCharsets.UTF_8), masterKey, masterInitialVector);
        return Base64.getEncoder().encodeToString(encryptedKey);
    }

    private String decryptWithMasterPassword(String encryptedKey) throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException {
        byte[] decryptedKey = AESUtil.decrypt(Base64.getDecoder().decode(encryptedKey), masterKey, masterInitialVector);
        return new String(decryptedKey, StandardCharsets.UTF_8);
    }

}
