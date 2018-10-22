package org.egov.enc.services;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.egov.enc.models.AsymmetricKey;
import org.egov.enc.models.Ciphertext;
import org.egov.enc.models.MethodEnum;
import org.egov.enc.models.Plaintext;
import org.egov.enc.utils.RSAUtil;
import org.egov.enc.keymanagement.KeyStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

@Service
public class RSAEncryptionService implements EncryptionServiceInterface {

    @Autowired
    private KeyStore keyStore;

    public Ciphertext encrypt(Plaintext plaintext) throws InvalidKeySpecException, NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException, InvalidAlgorithmParameterException {
        AsymmetricKey asymmetricKey = keyStore.getAsymmetricKey(plaintext.getTenantId());
        PublicKey publicKey = keyStore.generatePublicKey(asymmetricKey);

        byte[] cipherBytes = RSAUtil.encrypt(plaintext.getPlaintext().getBytes(StandardCharsets.UTF_8), publicKey);

        Ciphertext ciphertext = new Ciphertext(MethodEnum.RSA, asymmetricKey.getId(), Base64.getEncoder().encodeToString(cipherBytes));

        return ciphertext;
    }


    public Plaintext decrypt(Ciphertext ciphertext) throws InvalidKeySpecException, NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException, InvalidAlgorithmParameterException {
        AsymmetricKey asymmetricKey = keyStore.getAsymmetricKey(ciphertext.getKeyId());
        PrivateKey privateKey = keyStore.generatePrivateKey(asymmetricKey);

        byte[] plainBytes = RSAUtil.decrypt(Base64.getDecoder().decode(ciphertext.getCiphertext()), privateKey);
        String plain = new String(plainBytes, StandardCharsets.UTF_8);

        Plaintext plaintext = new Plaintext(plain);

        return plaintext;
    }

}
