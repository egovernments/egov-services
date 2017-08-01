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

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
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
       Receipt receipt = receipts.get(0);
       Bill bill = receipt.getBill().get(0);
       String emailId = bill.getPayeeEmail();
       String applicantName = bill.getPayeeName();
       final City city = tenantRepository.fetchTenantByCode(receiptRequest.getRequestInfo(),receiptRequest.getTenantId());
       sendEmailNotification(receiptRequest, applicantName,emailId,city);

   }

    private void sendEmailNotification(ReceiptRequest receiptRequest, String applicantName, String emailId, City city) {

        final EmailRequest emailRequest = EmailRequest.builder().subject("Tax Payment details").body(getSmsMessage(receiptRequest,applicantName,city))
                .email(emailId).build();

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
        emailMessage.append("Dear Sir/Madam,\n\nGreetings from ").append(city.getName()).append(".").append("\n\n").append("We thank you for making a payment of Rs.");


        String consumerCode = "";
        Long receiptDate = 0l;
        for(BillDetail billDetail : billDetails) {
            BusinessDetailsResponse response = businessDetailsRepository.getBusinessDetails(Arrays.asList(billDetail.getBusinessService()) ,receiptRequest.getTenantId(),receiptRequest.getRequestInfo());
            emailMessage.append(billDetail.getAmountPaid()).append(" for ").append(response.getBusinessDetails().get(0).getName());
            consumerCode = billDetail.getConsumerCode();
            receiptDate = billDetail.getReceiptDate();
        }

        Date receiptCreateDate = new Date(receiptDate);
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        emailMessage.append(" against Consumer Code :").append(consumerCode).append(" on ").append(format.format(receiptCreateDate)).append(".").append(System.lineSeparator());
        emailMessage.append("Please click on the URL to View your Payment Receipt: ").append(System.lineSeparator());
        for(BillDetail billDetail : billDetails) {
            emailMessage.append(propertiesManager.getCollectionServiceHost()).append(propertiesManager.getCollectionServiceUrl())
                    .append("?receiptNumbers=").append(billDetail.getReceiptNumber());
        }
        emailMessage.append("\n").append("Regards,").append("\n").append(city.getName());
        return emailMessage.toString();

    }

}
