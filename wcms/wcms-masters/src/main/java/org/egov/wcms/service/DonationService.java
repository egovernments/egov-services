
package org.egov.wcms.service;

import org.egov.wcms.model.Donation;
import org.egov.wcms.producers.DonationProducer;
import org.egov.wcms.repository.DonationRepository;
import org.egov.wcms.web.contract.DonationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class DonationService {

    public static final Logger logger = LoggerFactory.getLogger(DonationService.class);

    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private DonationProducer donationProducer;

    public DonationRequest create(final DonationRequest donationRequest) {
        return donationRepository.persistDonationDetails(donationRequest);
    }
    
    public Donation createPropertyUsageType(final String topic,final String key,final DonationRequest donationRequest) {
        final ObjectMapper mapper = new ObjectMapper();
        String donationRequestValue = null;
        try {
            logger.info("Donation service::" + donationRequest);
            donationRequestValue  = mapper.writeValueAsString(donationRequest);
            logger.info("Donation Request Value::" + donationRequestValue);
        } catch (final JsonProcessingException e) {
            e.printStackTrace();
        }
        try {
     	   donationProducer.sendMessage(topic,key,donationRequestValue);
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        return donationRequest.getDonation();
    }
    

 }
