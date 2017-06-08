package org.egov.wcms.util;

import java.util.Calendar;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AckConsumerNoGenerator {

    public String getAckNo() {
        String randomAlphabet = "";
        int randomPIN = 0;
        int lastTwoDigits = 0;
        String ackNo = "";

        lastTwoDigits = Calendar.getInstance().get(Calendar.YEAR) % 100;

        randomAlphabet = RandomStringUtils.randomAlphabetic(2);

        randomPIN = (int) (Math.random() * 9000) + 1000;

        ackNo = lastTwoDigits + String.valueOf(randomAlphabet) + randomPIN;

        return ackNo;

    }

    public String getConsumerNo() {
        String randomAlphabet = "";
        int randomPIN = 0;
        final String consumerAlphabets = "CN";
        String ackNo = "";

        randomAlphabet = RandomStringUtils.randomAlphabetic(2);

        randomPIN = (int) (Math.random() * 9000) + 1000;

        ackNo = consumerAlphabets + String.valueOf(randomAlphabet) + randomPIN;

        return ackNo;

    }

    public String getAckNofromService() {
        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.getForObject("http://egov.org/idgen", String.class);
        return getAckNo();

    }

}
