//package org.egov.pg.service.gateways.payu;
//
//import org.egov.pg.models.Transaction;
//import org.egov.pg.service.Gateway;
//import org.egov.pg.utils.Utils;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.util.UriComponents;
//import org.springframework.web.util.UriComponentsBuilder;
//
//import java.net.URI;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.util.Map;
//
//@Service
//public class PayuGateway implements Gateway {
//
//    private static final String GATEWAY_NAME = "PAYU";
//    private static final String MERCHANT_KEY = "";
//    private static final String MERCHANT_SALT = "";
//    private static final String MERCHANT_URL = "test.payu.in";
//    private static final String MERCHANT_PATH = "_payment";
//    private static final String CALLBACK_URL = "";
//
//    @Value("${gateway.payu.active}")
//    private boolean ACTIVE;
//
//    @Override
//    public URI generateRedirectURI(Transaction transaction) {
//
//        String suffix = "||||||||||";
//
//        String builder = MERCHANT_KEY + "|" +
//                transaction.getTxnId() + "|" +
//                transaction.getTxnAmount() + "|" +
//                transaction.getProductInfo() + "|" +
//                transaction.getUser().getName() + "|" +
//                transaction.getUser().getEmailId() + "|" +
//                suffix +
//                MERCHANT_SALT;
//        String hash = hashCal(builder);
//
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("key", MERCHANT_KEY);
//        params.add("txnid", transaction.getTxnId());
//        params.add("amount", Utils.formatAmtAsRupee(transaction.getTxnAmount()));
//        params.add("productinfo", transaction.getProductInfo());
//        params.add("firstname", transaction.getUser().getName());
//        params.add("email", transaction.getUser().getEmailId());
//        params.add("phone", transaction.getUser().getMobileNumber());
//        params.add("surl", CALLBACK_URL);
//        params.add("furl", CALLBACK_URL);
//        params.add("HASH", hash);
//        params.add("service_provider", "payu_paisa");
//
//        UriComponents uriComponents = UriComponentsBuilder.newInstance().scheme("https").host(MERCHANT_URL).path
//                (MERCHANT_PATH).queryParams(params).build().encode();
//
//        return uriComponents.toUri();
//    }
//
//    @Override
//    public Transaction fetchStatus(Transaction currentStatus, Map<String, String> params) {
//        return null;
//    }
//
//    @Override
//    public boolean isActive() {
//        return ACTIVE;
//    }
//
//
//    @Override
//    public String gatewayName() {
//        return GATEWAY_NAME;
//    }
//
//    @Override
//    public String transactionIdKeyInResponse() {
//        return "txnid";
//    }
//
//    private String hashCal(String str) {
//        byte[] hashSequence = str.getBytes();
//        StringBuilder hexString = new StringBuilder();
//        try {
//            MessageDigest algorithm = MessageDigest.getInstance("SHA-512");
//            algorithm.reset();
//            algorithm.update(hashSequence);
//            byte messageDigest[] = algorithm.digest();
//
//
//            for (byte aMessageDigest : messageDigest) {
//                String hex = Integer.toHexString(0xFF & aMessageDigest);
//                if (hex.length() == 1) hexString.append("0");
//                hexString.append(hex);
//            }
//
//        } catch (NoSuchAlgorithmException nsae) {
//        }
//
//        return hexString.toString();
//    }
//
//}
