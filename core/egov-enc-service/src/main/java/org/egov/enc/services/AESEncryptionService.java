package org.egov.enc.services;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.egov.enc.models.Ciphertext;
import org.egov.enc.models.MethodEnum;
import org.egov.enc.models.Plaintext;
import org.egov.enc.models.SymmetricKey;
import org.egov.enc.utils.AESUtil;
import org.egov.enc.keymanagement.KeyStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

@Service
public class AESEncryptionService {

    private KeyStore keyStore;

    @Autowired
    public AESEncryptionService(KeyStore keyStore) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        this.keyStore = keyStore;
    }

    public Ciphertext encrypt(Plaintext plaintext) throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeySpecException {
        SymmetricKey symmetricKey = keyStore.getSymmetricKey(plaintext.getTenantId());
        SecretKey secretKey = keyStore.generateSecretKey(symmetricKey);

        byte[] initialVectorsBytes = keyStore.generateInitialVector(symmetricKey);

        byte[] cipherBytes = AESUtil.encrypt(plaintext.getPlaintext().getBytes(StandardCharsets.UTF_8), secretKey, initialVectorsBytes);

        Ciphertext ciphertext = new Ciphertext(MethodEnum.AES, symmetricKey.getId(), Base64.getEncoder().encodeToString(cipherBytes));

        return ciphertext;
    }

    public Plaintext decrypt(Ciphertext ciphertext) throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeySpecException {
        SymmetricKey symmetricKey = keyStore.getSymmetricKey(ciphertext.getKeyId());
        SecretKey secretKey = keyStore.generateSecretKey(symmetricKey);

        byte[] initialVectorsBytes = keyStore.generateInitialVector(symmetricKey);

        byte[] plainBytes = AESUtil.decrypt(Base64.getDecoder().decode(ciphertext.getCiphertext()), secretKey, initialVectorsBytes);
        String plain = new String(plainBytes, StandardCharsets.UTF_8);

        Plaintext plaintext = new Plaintext(plain);

        return plaintext;
    }

}
