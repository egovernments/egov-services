package org.egov.collection.notification.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egov.collection.notification.config.PropertiesManager;
import org.egov.collection.notification.domain.model.City;
import org.egov.collection.notification.persistence.repository.BusinessDetailsRepository;
import org.egov.collection.notification.persistence.repository.TenantRepository;
import org.egov.collection.notification.web.contract.*;
import org.egov.common.contract.request.RequestInfo;
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
       final City city = tenantRepository.fetchTenantByCode(receiptRequest.getRequestInfo(),receiptRequest.getTenantId());
       sendEmailNotification(bill, receiptRequest.getRequestInfo(),emailId,city);

   }

    private void sendEmailNotification(Bill bill, RequestInfo requestInfo, String emailId, City city) {

        Date receiptCreateDate = new Date(bill.getBillDetails().get(0).getReceiptDate());
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        final EmailRequest emailRequest = EmailRequest.builder()
                .subject(new StringBuilder().append("Payment Received dated : ").append(format.format(receiptCreateDate)).toString()).body(getSmsMessage(bill, requestInfo, city))
                .email(emailId).build();

        log.info("Collection email details------------" + emailRequest);
        try {
            kafkaTemplate.send(propertiesManager.getEmailNotificationTopic(), propertiesManager.getEmailNotificationTopicKey(),
                    emailRequest);
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getSmsMessage(final Bill bill, final RequestInfo requestInfo, final City city) {
        StringBuilder  emailMessage = new StringBuilder();
        StringBuilder  consumerCodes = new StringBuilder();
        StringBuilder amountPaid = new StringBuilder();
        StringBuilder businessDetails = new StringBuilder();
        StringBuilder urls = new StringBuilder();

        int count = 0;

        for(BillDetail billDetail : bill.getBillDetails()) {
            if(count >= 1 && count != bill.getBillDetails().size()) {
                consumerCodes.append(",");
                amountPaid.append(",");
                businessDetails.append(",");
                urls.append("\n");
            }
            count++;
            consumerCodes.append(billDetail.getConsumerCode());
            amountPaid.append("Rs.").append(billDetail.getAmountPaid()).append("/-");
            BusinessDetailsResponse response = businessDetailsRepository.getBusinessDetails(Arrays.asList(billDetail.getBusinessService()) ,bill.getTenantId(),requestInfo);
            businessDetails.append(response.getBusinessDetails().get(0).getName());
            urls.append(propertiesManager.getCollectionServiceHost()).append(propertiesManager.getCollectionServiceUrl())
                    .append("?receiptNumbers=").append(billDetail.getReceiptNumber()).append("&tenantId=").append(bill.getTenantId());
        }
        emailMessage.append("Dear ").append(bill.getPayeeName()).append(",").append("\n\n").append("We have received payment of ").append(amountPaid).append(" for the consumer codes ")
                .append(consumerCodes).append(" which belong to the service ").append(businessDetails).append(".").append("\n\n");
        emailMessage.append("Please click on the URL for receipt details: ").append(urls).append("\n\n");
        emailMessage.append("Regards,").append("\n").append(city.getName());
        return emailMessage.toString();

    }

}
