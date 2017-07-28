package org.egov.collection.notification.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egov.collection.notification.config.PropertiesManager;
import org.egov.collection.notification.domain.model.City;
import org.egov.collection.notification.persistence.repository.BusinessDetailsRepository;
import org.egov.collection.notification.persistence.repository.TenantRepository;
import org.egov.collection.notification.web.contract.*;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private PropertiesManager propertiesManager;

    @Autowired
    private BusinessDetailsRepository businessDetailsRepository;

    @Autowired
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

   public void send(ReceiptRequest receiptRequest) {
       List<Receipt> receipts = receiptRequest.getReceipt();
       String applicantName = receipts.get(0).getPayeeName();
       String emailId = receipts.get(0).getEmailId();
      // String receiptNumber = receipt.getReceiptNumber();
       final City city = tenantRepository.fetchTenantByCode(receiptRequest.getRequestInfo(),receiptRequest.getTenantId());

       //if(StringUtils.isBlank(receiptNumber)) {
           sendEmailNotification(receiptRequest, applicantName,emailId,city);

       //}
   }

    private void sendEmailNotification(ReceiptRequest receiptRequest, String applicantName, String emailId, City city) {

        final EmailRequest emailRequest = EmailRequest.builder().message(getSmsMessage(receiptRequest,applicantName,city))
                .emailId(emailId).build();

        log.info("Collection email details------------" + emailRequest);
        try {
            kafkaTemplate.send(propertiesManager.getEmailNotificationTopic(), propertiesManager.getEmailNotificationTopicKey(),
                    emailRequest);
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getSmsMessage(final ReceiptRequest receiptRequest, final String applicantName, final City city) {
        StringBuilder  emailMessage = new StringBuilder(200);
        List<Receipt> receipts = receiptRequest.getReceipt();
        List<Bill> bills = receipts.get(0).getBill();
        List<BillDetail> billDetails = bills.get(0).getBillDetails();
        emailMessage.append("Dear Sir/Madam,\\n\\nGreetings from ").append(city.getName()).append(".\\n\\n We thank you for making a payment of Rs.");

        String consumerCode = "";
        Long receiptDate = 0l;
        for(BillDetail billDetail : billDetails) {
            BusinessDetailsResponse response = businessDetailsRepository.getBusinessDetails(Arrays.asList(billDetail.getBusinessService()) ,receiptRequest.getTenantId(),receiptRequest.getRequestInfo());
            emailMessage.append(billDetail.getAmountPaid()).append("for ").append(response.getBusinessDetails().get(0).getName());
            consumerCode = billDetail.getConsumerCode();
            receiptDate = billDetail.getReceiptDate();
        }
        emailMessage.append(" against Consumer Code : ").append(consumerCode).append(" on").append(receiptDate).append(".\n\n");
        emailMessage.append("Please click on the URL to View your Payment Receipt: \n\n");
        for(BillDetail billDetail : billDetails) {
            emailMessage.append(propertiesManager.getCollectionServiceHost()).append(propertiesManager.getCollectionServiceUrl())
                    .append("?receiptNumbers=").append(billDetail.getReceiptNumber());
        }

        return emailMessage.toString();

    }

}
