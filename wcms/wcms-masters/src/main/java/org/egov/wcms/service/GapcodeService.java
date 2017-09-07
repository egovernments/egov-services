package org.egov.wcms.service;

import java.util.List;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.wcms.model.CommonDataModel;
import org.egov.wcms.model.Gapcode;
import org.egov.wcms.repository.GapcodeRepository;
import org.egov.wcms.web.contract.GapcodeGetRequest;
import org.egov.wcms.web.contract.GapcodeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GapcodeService {

    @Autowired
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private GapcodeRepository gapcodeRepository;

    @Autowired
    private CodeGeneratorService codeGeneratorService;

    public List<Gapcode> createGapcode(final String topic, final String key,
            final GapcodeRequest gapcodeRequest) {
        for (final Gapcode gapcode : gapcodeRequest.getGapcode())
            gapcode.setId(Long.parseLong(codeGeneratorService.generate(Gapcode.SEQ_GAPCODE)));

        try {
            kafkaTemplate.send(topic, key, gapcodeRequest);
        } catch (final Exception ex) {
            log.error("Exception Encountered : " + ex);
        }
        return gapcodeRequest.getGapcode();
    }

    public GapcodeRequest create(final GapcodeRequest gapcodeRequest) {
        return gapcodeRepository.persist(gapcodeRequest);
    }

    public List<Gapcode> updateGapcode(final String topic, final String key,
            final GapcodeRequest gapcodeRequest) {

        try {
            kafkaTemplate.send(topic, key, gapcodeRequest);
        } catch (final Exception ex) {
            log.error("Exception Encountered : " + ex);
        }
        return gapcodeRequest.getGapcode();
    }

    public GapcodeRequest update(final GapcodeRequest gapcodeRequest) {
        return gapcodeRepository.persistUpdate(gapcodeRequest);
    }

    public List<Gapcode> getGapcodes(final GapcodeGetRequest gapcodeGetRequest) {
        return gapcodeRepository.findForCriteria(gapcodeGetRequest);
    }

    public List<CommonDataModel> getFormulaQuery() {
        return gapcodeRepository.getFormulaQuery();
    }

}
