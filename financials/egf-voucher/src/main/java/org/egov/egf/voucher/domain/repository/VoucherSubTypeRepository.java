package org.egov.egf.voucher.domain.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.constants.Constants;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.master.web.repository.FinancialConfigurationContractRepository;
import org.egov.egf.voucher.domain.model.VoucherSubType;
import org.egov.egf.voucher.domain.model.VoucherSubTypeSearch;
import org.egov.egf.voucher.persistence.entity.VoucherSubTypeEntity;
import org.egov.egf.voucher.persistence.queue.repository.VoucherSubTypeQueueRepository;
import org.egov.egf.voucher.persistence.repository.VoucherSubTypeJdbcRepository;
import org.egov.egf.voucher.web.contract.VoucherSubTypeContract;
import org.egov.egf.voucher.web.contract.VoucherSubTypeSearchContract;
import org.egov.egf.voucher.web.requests.VoucherSubTypeRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VoucherSubTypeRepository {

    private VoucherSubTypeJdbcRepository voucherSubTypeJdbcRepository;

    private VoucherSubTypeQueueRepository voucherSubTypeQueueRepository;

    private FinancialConfigurationContractRepository financialConfigurationContractRepository;

    private VoucherSubTypeESRepository voucherSubTypeESRepository;

    private String persistThroughKafka;

    @Autowired
    public VoucherSubTypeRepository(
            VoucherSubTypeJdbcRepository voucherSubTypeJdbcRepository,
            FinancialConfigurationContractRepository financialConfigurationContractRepository,
            VoucherSubTypeESRepository voucherSubTypeESRepository,
            VoucherSubTypeQueueRepository voucherSubTypeQueueRepository,
            @Value("${persist.through.kafka}") String persistThroughKafka) {
        this.voucherSubTypeJdbcRepository = voucherSubTypeJdbcRepository;
        this.voucherSubTypeQueueRepository = voucherSubTypeQueueRepository;
        this.financialConfigurationContractRepository = financialConfigurationContractRepository;
        this.voucherSubTypeESRepository = voucherSubTypeESRepository;
        this.persistThroughKafka = persistThroughKafka;

    }

    @Transactional
    public List<VoucherSubType> save(List<VoucherSubType> voucherSubTypes,
            RequestInfo requestInfo) {

        ModelMapper mapper = new ModelMapper();
        VoucherSubTypeContract contract;

        if (persistThroughKafka != null && !persistThroughKafka.isEmpty()
                && persistThroughKafka.equalsIgnoreCase("yes")) {

            VoucherSubTypeRequest request = new VoucherSubTypeRequest();
            request.setRequestInfo(requestInfo);
            request.setVoucherSubTypes(new ArrayList<>());

            for (VoucherSubType f : voucherSubTypes) {

                contract = new VoucherSubTypeContract();
                contract.setCreatedDate(new Date());
                mapper.map(f, contract);
                request.getVoucherSubTypes().add(contract);

            }

            addToQue(request);

            return voucherSubTypes;

        } else {

            List<VoucherSubType> resultList = new ArrayList<VoucherSubType>();

            for (VoucherSubType f : voucherSubTypes) {

                resultList.add(save(f));
            }

            VoucherSubTypeRequest request = new VoucherSubTypeRequest();
            request.setRequestInfo(requestInfo);
            request.setVoucherSubTypes(new ArrayList<>());

            for (VoucherSubType f : resultList) {

                contract = new VoucherSubTypeContract();
                contract.setCreatedDate(new Date());
                mapper.map(f, contract);
                request.getVoucherSubTypes().add(contract);

            }

            addToSearchQueue(request);

            return resultList;
        }

    }

    @Transactional
    public List<VoucherSubType> update(List<VoucherSubType> voucherSubTypes,
            RequestInfo requestInfo) {

        ModelMapper mapper = new ModelMapper();
        VoucherSubTypeContract contract;

        if (persistThroughKafka != null && !persistThroughKafka.isEmpty()
                && persistThroughKafka.equalsIgnoreCase("yes")) {

            VoucherSubTypeRequest request = new VoucherSubTypeRequest();
            request.setRequestInfo(requestInfo);
            request.setVoucherSubTypes(new ArrayList<>());

            for (VoucherSubType f : voucherSubTypes) {

                contract = new VoucherSubTypeContract();
                contract.setCreatedDate(new Date());
                mapper.map(f, contract);
                request.getVoucherSubTypes().add(contract);

            }

            addToQue(request);

            return voucherSubTypes;
        } else {

            List<VoucherSubType> resultList = new ArrayList<VoucherSubType>();

            for (VoucherSubType f : voucherSubTypes) {

                resultList.add(update(f));
            }

            VoucherSubTypeRequest request = new VoucherSubTypeRequest();
            request.setRequestInfo(requestInfo);
            request.setVoucherSubTypes(new ArrayList<>());

            for (VoucherSubType f : resultList) {

                contract = new VoucherSubTypeContract();
                contract.setCreatedDate(new Date());
                mapper.map(f, contract);
                request.getVoucherSubTypes().add(contract);

            }

            addToSearchQueue(request);

            return resultList;
        }

    }

    @Transactional
    public VoucherSubType save(VoucherSubType voucherSubType) {

        VoucherSubType savedVoucherSubType = voucherSubTypeJdbcRepository
                .create(new VoucherSubTypeEntity().toEntity(voucherSubType))
                .toDomain();

        return savedVoucherSubType;

    }

    @Transactional
    public VoucherSubType update(VoucherSubType voucherSubType) {
        VoucherSubTypeEntity entity = voucherSubTypeJdbcRepository
                .update(new VoucherSubTypeEntity().toEntity(voucherSubType));
        return entity.toDomain();
    }

    public void addToQue(VoucherSubTypeRequest request) {

        Map<String, Object> message = new HashMap<>();

        if (request.getRequestInfo().getAction()
                .equalsIgnoreCase(Constants.ACTION_CREATE)) {
            message.put("vouchersubtype_create", request);
        } else {
            message.put("vouchersubtype_update", request);
        }
        voucherSubTypeQueueRepository.addToQue(message);

    }

    public void addToSearchQueue(VoucherSubTypeRequest request) {

        Map<String, Object> message = new HashMap<>();

        message.put("vouchersubtype_persisted", request);

        voucherSubTypeQueueRepository.addToSearchQue(message);
    }

    public Pagination<VoucherSubType> search(VoucherSubTypeSearch domain) {

        if (!financialConfigurationContractRepository.fetchDataFrom().isEmpty()
                && financialConfigurationContractRepository.fetchDataFrom()
                        .equalsIgnoreCase("es")) {
            VoucherSubTypeSearchContract voucherSubTypeSearchContract = new VoucherSubTypeSearchContract();
            ModelMapper mapper = new ModelMapper();
            mapper.map(domain, voucherSubTypeSearchContract);
            return voucherSubTypeESRepository
                    .search(voucherSubTypeSearchContract);
        }

        return voucherSubTypeJdbcRepository.search(domain);

    }
}
