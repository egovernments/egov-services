package org.egov.egf.master.domain.repository;

import java.util.HashMap;
import java.util.Map;

import org.egov.common.constants.Constants;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.master.domain.model.Function;
import org.egov.egf.master.domain.model.FunctionSearch;
import org.egov.egf.master.domain.service.FinancialConfigurationService;
import org.egov.egf.master.persistence.entity.FunctionEntity;
import org.egov.egf.master.persistence.queue.MastersQueueRepository;
import org.egov.egf.master.persistence.repository.FunctionJdbcRepository;
import org.egov.egf.master.web.contract.FunctionSearchContract;
import org.egov.egf.master.web.requests.FunctionRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FunctionRepository {

    @Autowired
    private FunctionJdbcRepository functionJdbcRepository;

    @Autowired
    private MastersQueueRepository functionQueueRepository;

    @Autowired
    private FinancialConfigurationService financialConfigurationService;

    @Autowired
    private FunctionESRepository functionESRepository;

    public Function findById(Function function) {
        FunctionEntity entity = functionJdbcRepository.findById(new FunctionEntity().toEntity(function));
        return entity.toDomain();

    }

    @Transactional
    public Function save(Function function) {
        FunctionEntity entity = functionJdbcRepository.create(new FunctionEntity().toEntity(function));
        return entity.toDomain();
    }

    @Transactional
    public Function update(Function function) {
        FunctionEntity entity = functionJdbcRepository.update(new FunctionEntity().toEntity(function));
        return entity.toDomain();
    }

    public void add(FunctionRequest request) {
        Map<String, Object> message = new HashMap<>();

        if (request.getRequestInfo().getAction().equalsIgnoreCase(Constants.ACTION_CREATE)) {
            message.put("function_create", request);
        } else {
            message.put("function_update", request);
        }
        functionQueueRepository.add(message);
    }

    public Pagination<Function> search(FunctionSearch domain) {

        if (!financialConfigurationService.fetchDataFrom().isEmpty()
                && !financialConfigurationService.fetchDataFrom().equalsIgnoreCase("es")) {
            FunctionSearchContract functionSearchContract = new FunctionSearchContract();
            ModelMapper mapper = new ModelMapper();
            mapper.map(domain, functionSearchContract);
            return functionESRepository.search(functionSearchContract);
        } else {
            return functionJdbcRepository.search(domain);
        }

    }

}