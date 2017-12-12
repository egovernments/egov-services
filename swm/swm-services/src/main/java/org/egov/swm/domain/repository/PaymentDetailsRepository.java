package org.egov.swm.domain.repository;

import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.PaymentDetails;
import org.egov.swm.domain.model.PaymentDetailsSearch;
import org.egov.swm.persistence.queue.repository.PaymentDetailsQueueRepository;
import org.egov.swm.persistence.repository.DocumentJdbcRepository;
import org.egov.swm.persistence.repository.PaymentDetailsJdbcRepository;
import org.egov.swm.web.requests.PaymentDetailsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentDetailsRepository {

    @Autowired
    private PaymentDetailsQueueRepository paymentDetailsQueueRepository;

    @Autowired
    private PaymentDetailsJdbcRepository paymentDetailsJdbcRepository;

    @Autowired
    private DocumentJdbcRepository documentJdbcRepository;

    public PaymentDetailsRequest create(final PaymentDetailsRequest paymentDetailsRequest) {
        return paymentDetailsQueueRepository.save(paymentDetailsRequest);
    }

    public PaymentDetailsRequest update(final PaymentDetailsRequest paymentDetailsRequest) {

        for (final PaymentDetails details : paymentDetailsRequest.getPaymentDetails())
            documentJdbcRepository.delete(details.getTenantId(), details.getCode());
        return paymentDetailsQueueRepository.update(paymentDetailsRequest);
    }

    public Pagination<PaymentDetails> search(final PaymentDetailsSearch paymentDetailsSearch) {
        return paymentDetailsJdbcRepository.search(paymentDetailsSearch);

    }
}
