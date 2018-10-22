package org.egov.enc.keymanagement;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.egov.enc.config.AppProperties;
import org.egov.enc.models.AsymmetricKey;
import org.egov.enc.models.SymmetricKey;
import org.egov.enc.repository.KeyRepository;
import org.egov.enc.utils.AESUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;



/*
    KeyStore fetches keys from database.
    All Keys will be stored inside the HashMaps.
    Keys can be extracted from these maps based on Key_ID.
    Active Key for a given Tenant can be got by providing Tenant_ID.
*/


@Component
public class KeyStore implements ApplicationRunner {

    @Autowired
    private AppProperties appProperties;
    @Autowired
    private KeyRepository keyRepository;

    private ArrayList<String> tenantIds;

    private static ArrayList<SymmetricKey> symmetricKeys;
    private static ArrayList<AsymmetricKey> asymmetricKeys;

    private static HashMap<Integer, SymmetricKey> symmetricKeyHashMap;
    private static HashMap<Integer, AsymmetricKey> asymmetricKeyHashMap;

    private static HashMap<String, Integer> activeSymmetricKeys;
    private static HashMap<String, Integer> activeAsymmetricKeys;

    private SecretKey masterKey;
    private byte[] masterInitialVector;


    @Autowired
    public KeyStore()  {
        Security.addProvider(new BouncyCastleProvider());
    }

    //Master Key will be used to decrypt the keys read from the database
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

    //Reset and Initialize all the keys and HashMaps from the database
    public void refreshKeys() throws BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeySpecException {
        tenantIds = (ArrayList<String>) keyRepository.fetchDistinctTenantIds();

        symmetricKeys = (ArrayList<SymmetricKey>) this.keyRepository.fetchSymmetricKeys();
        asymmetricKeys = (ArrayList<AsymmetricKey>) this.keyRepository.fetchAsymmtericKeys();

        decryptAllKeys();

        symmetricKeyHashMap = new HashMap<>();
        asymmetricKeyHashMap = new HashMap<>();

        initializeKeys();

        activeSymmetricKeys = new HashMap<>();
        activeAsymmetricKeys = new HashMap<>();

        initializeActiveKeys();
    }


    //Create HashMap to store keys indexed with keyId
    private void initializeKeys() {
        for(SymmetricKey symmetricKey : symmetricKeys) {
            symmetricKeyHashMap.put(symmetricKey.getId(), symmetricKey);
        }
        for(AsymmetricKey asymmetricKey : asymmetricKeys) {
            asymmetricKeyHashMap.put(asymmetricKey.getId(), asymmetricKey);
        }
    }

    //Create HashMap to store active keys indexed with tenantId
    private void initializeActiveKeys() {

        for(String tenant : tenantIds) {
            for(SymmetricKey symmetricKey : symmetricKeys) {
                if(symmetricKey.getTenantId().equalsIgnoreCase(tenant) && symmetricKey.isActive()) {
                    activeSymmetricKeys.put(tenant, symmetricKey.getId());
                    break;
                }
            }

            for(AsymmetricKey asymmetricKey : asymmetricKeys) {
                if(asymmetricKey.getTenantId().equalsIgnoreCase(tenant) && asymmetricKey.isActive()) {
                    activeAsymmetricKeys.put(tenant, asymmetricKey.getId());
                    break;
                }
            }
        }
    }

    //Get currently active symmetric key for given tenanId
    public SymmetricKey getSymmetricKey(String tenantId) {
        return getSymmetricKey(activeSymmetricKeys.get(tenantId));
    }

    //Get currently active asymmetric key for given tenanId
    public AsymmetricKey getAsymmetricKey(String tenantId) {
        return getAsymmetricKey(activeAsymmetricKeys.get(tenantId));
    }

    //Get symmetric key based on given keyId
    public SymmetricKey getSymmetricKey(int keyId) {
        return symmetricKeyHashMap.get(keyId);
    }

    //Get asymmetric key based on given keyId
    public AsymmetricKey getAsymmetricKey(int keyId) {
        return asymmetricKeyHashMap.get(keyId);
    }

    //Generate Secret Key to be used by AES from custom object SymmetricKey
    public SecretKey generateSecretKey(SymmetricKey symmetricKey) {
        String encodedKey = symmetricKey.getSecretKey();
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        return new SecretKeySpec(decodedKey, "AES");
    }

    //Generate PublicKey to be used by RSA from custom object AsymmetricKey
    public PublicKey generatePublicKey(AsymmetricKey asymmetricKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String encodedPublicKey = asymmetricKey.getPublicKey();
        byte[] decodedPublicKey = Base64.getDecoder().decode(encodedPublicKey);

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedPublicKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    //Generate PrivateKey to be used by RSA from custom object AsymmetricKey
    public PrivateKey generatePrivateKey(AsymmetricKey asymmetricKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String encodedPrivateKey = asymmetricKey.getPrivateKey();
        byte[] decodedPrivateKey = Base64.getDecoder().decode(encodedPrivateKey);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedPrivateKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    //Generate Initial Vecctor to be used by AES from custom object SymmetricKey
    public byte[] generateInitialVector(SymmetricKey symmetricKey) {
        return Base64.getDecoder().decode(symmetricKey.getInitialVector());
    }

    //Decrypt the key read from the database with the use of master password
    private String decryptWithMasterPassword(String encryptedKey) throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException {
        byte[] decryptedKey = AESUtil.decrypt(Base64.getDecoder().decode(encryptedKey), masterKey, masterInitialVector);
        return new String(decryptedKey, StandardCharsets.UTF_8);
    }

    //Decrypt all keys
    private void decryptAllKeys() throws BadPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException {
        for(int i = 0; i < symmetricKeys.size(); i++) {
            symmetricKeys.get(i).setSecretKey(decryptWithMasterPassword(symmetricKeys.get(i).getSecretKey()));
            symmetricKeys.get(i).setInitialVector(decryptWithMasterPassword(symmetricKeys.get(i).getInitialVector()));
        }
        for(int i = 0; i < asymmetricKeys.size(); i++) {
            asymmetricKeys.get(i).setPublicKey(decryptWithMasterPassword(asymmetricKeys.get(i).getPublicKey()));
            asymmetricKeys.get(i).setPrivateKey(decryptWithMasterPassword(asymmetricKeys.get(i).getPrivateKey()));
        }
    }


    //Initialize keys after application has finished loading
    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        initializeMasterKey();
        refreshKeys();
    }
}
