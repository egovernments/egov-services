package org.egov.domain.service;

import org.egov.persistence.util.DBSequenceGenerator;
import org.egov.persistence.util.SequenceNumberGenerator;
import org.egov.persistence.util.Utils;
import org.egov.persistence.util.repository.TenantRepository;
import org.egov.web.contract.Tenant;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

@Service
public class CrnGeneratorService {

    private SequenceNumberGenerator sequenceNumberGenerator;
    private DBSequenceGenerator dbSequenceGenerator;
    private Utils utils;
    private TenantRepository tenantRepository;

    private final String APP_NUMBER_SEQ_PREFIX = "SEQ_APPLICATION_NUMBER";

    public CrnGeneratorService(SequenceNumberGenerator sequenceNumberGenerator,
                               DBSequenceGenerator dbSequenceGenerator,
                               Utils utils, TenantRepository tenantRepository) {
        this.sequenceNumberGenerator = sequenceNumberGenerator;
        this.dbSequenceGenerator = dbSequenceGenerator;
        this.tenantRepository = tenantRepository;
        this.utils = utils;
    }

    @Transactional
    public String generate(String tenantId) {
        try {
            final String sequenceName = String.format(APP_NUMBER_SEQ_PREFIX);

            Tenant tenant = getTenant(tenantId);
            final String ulbName = tenant.getCity().getCode();
            final String councilName = tenant.getType();
            final String currentYear = utils.currentDateToYearFormat();

            Serializable sequenceNumber;

            try {
                sequenceNumber = sequenceNumberGenerator.getNextSequence(sequenceName);
            } catch (final SQLGrammarException e) {
                sequenceNumber = dbSequenceGenerator.createAndGetNextSequence(sequenceName);
            }

            return String.format("%s/%s/%05d/%s", ulbName, councilName, sequenceNumber, currentYear);
        } catch (final SQLException e) {
            throw new RuntimeException("Error occurred while generating Application Number", e);
        }
    }

    private Tenant getTenant(String tenantId){
        List<Tenant> tenantList = tenantRepository.fetchTenantByCode(tenantId);
        if(!tenantList.isEmpty()){
            return tenantList.get(0);
        }
        return null;
    }
}
