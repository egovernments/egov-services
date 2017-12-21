package org.egov.egf.bill.persistence.queue.repository;

import org.egov.egf.bill.domain.model.BillDetail;
import org.egov.egf.bill.domain.model.BillRegister;
import org.egov.egf.bill.persistence.repository.BillChecklistJdbcRepository;
import org.egov.egf.bill.persistence.repository.BillDetailJdbcRepository;
import org.egov.egf.bill.persistence.repository.BillPayeeDetailJdbcRepository;
import org.egov.egf.bill.web.requests.BillRegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class BillRegisterQueueRepository {

    @Autowired
    private BillDetailJdbcRepository billDetailJdbcRepository;

    @Autowired
    private BillPayeeDetailJdbcRepository billPayeeDetailJdbcRepository;

    @Autowired
    private BillChecklistJdbcRepository billChecklistJdbcRepository;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${egov.egf.bill.billregister.save.topic}")
    private String saveTopic;

    @Value("${egov.egf.bill.billregister.update.topic}")
    private String updateTopic;

    @Value("${egov.egf.bill.billregister.indexer.topic}")
    private String indexerTopic;

    public BillRegisterRequest save(final BillRegisterRequest billRegisterRequest) {

        kafkaTemplate.send(saveTopic, billRegisterRequest);

        kafkaTemplate.send(indexerTopic, billRegisterRequest);

        return billRegisterRequest;

    }

    public BillRegisterRequest update(final BillRegisterRequest billRegisterRequest) {

        for (final BillRegister br : billRegisterRequest.getBillRegisters()) {

            billDetailJdbcRepository.delete(br.getTenantId(), br.getBillNumber());

            billChecklistJdbcRepository.delete(br.getTenantId(), br.getBillNumber());

            if (br.getBillDetails() != null && !br.getBillDetails().isEmpty()) {

                for (BillDetail bd : br.getBillDetails()) {

                    billPayeeDetailJdbcRepository.delete(bd.getTenantId(), bd.getId());
                }
            }
        }

        kafkaTemplate.send(updateTopic, billRegisterRequest);

        kafkaTemplate.send(indexerTopic, billRegisterRequest);

        return billRegisterRequest;

    }
}
