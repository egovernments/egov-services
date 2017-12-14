package org.egov.works.masters.domain.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.works.masters.domain.repository.builder.ContractorQueryBuilder;
import org.egov.works.masters.domain.repository.helper.ContractorHelper;
import org.egov.works.masters.web.contract.Contractor;
import org.egov.works.masters.web.contract.ContractorSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class ContractorRepository {

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private ContractorQueryBuilder contractorQueryBuilder;

    public Contractor getByCode(String code, String tenantId) {
        ContractorSearchCriteria contractorSearchCriteria = new ContractorSearchCriteria();
        List<Contractor> contractors;
        contractorSearchCriteria.setCode(code);
        contractorSearchCriteria.setTenantId(tenantId);
        contractors = getContractorByCriteria(contractorSearchCriteria);
        return contractors.isEmpty() ? null : contractors.get(0);
    }
    
    public Contractor findByID(String contractorId, String tenantId) {
        ContractorSearchCriteria contractorSearchCriteria = new ContractorSearchCriteria();
        List<Contractor> contractors;
        contractorSearchCriteria.setIds(Arrays.asList(contractorId));
        contractorSearchCriteria.setTenantId(tenantId);
        contractors = getContractorByCriteria(contractorSearchCriteria);
        return contractors.isEmpty() ? null : contractors.get(0);
    }

    public List<Contractor> getContractorByCriteria(ContractorSearchCriteria contractorSearchCriteria) {
        Map params = new HashMap();
        String queryStr = contractorQueryBuilder.getSearchQuery(contractorSearchCriteria, params);
        List<ContractorHelper> contractorHelperHelpers = namedParameterJdbcTemplate.query(queryStr, params,
                new BeanPropertyRowMapper(ContractorHelper.class));
        List<Contractor> contractors = new ArrayList<>();

        Contractor contractor;

        for (ContractorHelper contractorHelper : contractorHelperHelpers) {
            contractor = contractorHelper.toDomain();
            contractors.add(contractor);
        }
        return contractors;
    }

}
