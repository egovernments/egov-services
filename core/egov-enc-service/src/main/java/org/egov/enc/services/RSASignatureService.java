package org.egov.enc.services;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.egov.enc.models.AsymmetricKey;
import org.egov.enc.models.Plaintext;
import org.egov.enc.utils.SignUtil;
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

import org.egov.enc.web.models.Signature;

@Service
public class RSASignatureService {

    @Autowired
    private KeyStore keyStore;

    @Autowired
    public RSASignatureService() {
        Security.addProvider(new BouncyCastleProvider());
    }

    public Signature hashAndSign(Plaintext plaintext) throws InvalidKeySpecException, NoSuchAlgorithmException, SignatureException, InvalidKeyException, IllegalBlockSizeException, NoSuchPaddingException, InvalidAlgorithmParameterException, BadPaddingException {
        AsymmetricKey asymmetricKey = keyStore.getAsymmetricKey(plaintext.getTenantId());
        PrivateKey privateKey = keyStore.generatePrivateKey(asymmetricKey);

        byte[] signBytes = SignUtil.hashAndSign(plaintext.getPlaintext().getBytes(StandardCharsets.UTF_8), privateKey);
        String sign = Base64.getEncoder().encodeToString(signBytes);

        Signature signature = new Signature(sign, asymmetricKey.getId());

        return signature;
    }

    public boolean hashandVerify(Plaintext plaintext, Signature signature) throws InvalidKeySpecException, NoSuchAlgorithmException, SignatureException, InvalidKeyException, IllegalBlockSizeException, NoSuchPaddingException, InvalidAlgorithmParameterException, BadPaddingException {
        AsymmetricKey asymmetricKey = keyStore.getAsymmetricKey(signature.getKeyId());
        PublicKey publicKey = keyStore.generatePublicKey(asymmetricKey);

        return SignUtil.hashAndVerify(plaintext.getPlaintext().getBytes(StandardCharsets.UTF_8), Base64.getDecoder().decode(signature.getSignatureValue()), publicKey);
    }

}
