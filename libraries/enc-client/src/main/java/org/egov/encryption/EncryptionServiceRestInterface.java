package org.egov.encryption;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egov.encryption.web.contract.EncReqObject;
import org.egov.encryption.web.contract.EncryptionRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@AllArgsConstructor
public class EncryptionServiceRestInterface {


    private String egovEncHost;
    private String egovEncEncryptPath;
    private String egovEncDecryptPath;

    private ObjectMapper mapper;

    public EncryptionServiceRestInterface() {
        mapper = new ObjectMapper(new JsonFactory());
        egovEncHost = "http://localhost:1234";
        egovEncEncryptPath = "/egov-enc-service/crypto/v1/_encrypt";
        egovEncDecryptPath = "/egov-enc-service/crypto/v1/_decrypt";
    }

    Object callEncrypt(String tenantId, String type, Object value) throws IOException {

        EncReqObject encReqObject = new EncReqObject(tenantId, type, value);
        EncryptionRequest encryptionRequest = new EncryptionRequest();
        encryptionRequest.setEncryptionRequests(new ArrayList<>(Collections.singleton(encReqObject)));

        String encryptionRequestString = mapper.valueToTree(encryptionRequest).toString();

        String response =  executeQuery(egovEncHost + egovEncEncryptPath, encryptionRequestString);

        return mapper.readTree(response).get(0);
    }

    Object callDecrypt(Object ciphertext) throws IOException {
        String decryptionRequestString = mapper.valueToTree(ciphertext).toString();
        String response =  executeQuery(egovEncHost + egovEncDecryptPath, decryptionRequestString);

        return mapper.readTree(response);
    }


    private String executeQuery(String urlString, String queryContent) {
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            log.error(e.getMessage());
            return null;
        }
        String response = null;

        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");

            connection.setDoOutput(true);
            OutputStream connectionOutputStream =  connection.getOutputStream();

            connectionOutputStream.write(queryContent.getBytes());

            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer responseStringBuffer = new StringBuffer();
                while ((inputLine = in .readLine()) != null) {
                    responseStringBuffer.append(inputLine);
                }
                in .close();
                response = responseStringBuffer.toString();
            } else {
                log.error("Error in Call To Encryption Service");
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        }


        return response;
    }


}