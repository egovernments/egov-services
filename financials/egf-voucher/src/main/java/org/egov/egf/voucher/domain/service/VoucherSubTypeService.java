package org.egov.egf.voucher.domain.service;

import java.util.List;
import java.util.UUID;

import org.egov.common.constants.Constants;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.voucher.domain.model.VoucherSubType;
import org.egov.egf.voucher.domain.model.VoucherSubTypeSearch;
import org.egov.egf.voucher.domain.repository.VoucherSubTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

@Service
@Transactional(readOnly = true)
public class VoucherSubTypeService {

    private VoucherSubTypeRepository voucherSubTypeRepository;

    private SmartValidator validator;

    @Autowired
    public VoucherSubTypeService(
            VoucherSubTypeRepository voucherSubTypeRepository,
            SmartValidator validator) {
        this.voucherSubTypeRepository = voucherSubTypeRepository;
        this.validator = validator;
    }

    @Transactional
    public List<VoucherSubType> create(List<VoucherSubType> voucherSubTypes,
            BindingResult errors, RequestInfo requestInfo) {

        try {

            voucherSubTypes = fetchRelated(voucherSubTypes);

            validate(voucherSubTypes, Constants.ACTION_CREATE, errors);

            if (errors.hasErrors()) {
                throw new CustomBindException(errors);
            }

            populateVoucherSubTypeIds(voucherSubTypes);

        } catch (CustomBindException e) {

            throw new CustomBindException(errors);
        }

        return voucherSubTypeRepository.save(voucherSubTypes, requestInfo);

    }

    private void populateVoucherSubTypeIds(List<VoucherSubType> voucherSubTypes) {

        for (VoucherSubType vst : voucherSubTypes)
            vst.setId(UUID.randomUUID().toString().replace("-", ""));

    }

    @Transactional
    public List<VoucherSubType> update(List<VoucherSubType> voucherSubTypes, BindingResult errors, RequestInfo requestInfo) {

        try {

            voucherSubTypes = fetchRelated(voucherSubTypes);

            validate(voucherSubTypes, Constants.ACTION_UPDATE, errors);

            if (errors.hasErrors()) {
                throw new CustomBindException(errors);
            }

        } catch (CustomBindException e) {

            throw new CustomBindException(errors);
        }

        return voucherSubTypeRepository.update(voucherSubTypes, requestInfo);

    }

    public List<VoucherSubType> fetchRelated(
            List<VoucherSubType> voucherSubTypes) {
        return voucherSubTypes;
    }

    private BindingResult validate(List<VoucherSubType> voucherSubTypes,
            String method, BindingResult errors) {

        try {

            switch (method) {

            case Constants.ACTION_VIEW:
                break;
            case Constants.ACTION_CREATE:
                Assert.notNull(voucherSubTypes,
                        "voucherSubTypes to create must not be null");
                for (VoucherSubType voucherSubType : voucherSubTypes) {
                    validator.validate(voucherSubType, errors);
                }
                break;
            case Constants.ACTION_UPDATE:
                Assert.notNull(voucherSubTypes,
                        "voucherSubTypes to update must not be null");
                for (VoucherSubType voucherSubType : voucherSubTypes) {
                    validator.validate(voucherSubType, errors);
                }
                break;
            default:

            }

        } catch (IllegalArgumentException e) {
            errors.addError(new ObjectError("Missing data", e.getMessage()));
        }

        return errors;

    }

    @Transactional
    public VoucherSubType save(VoucherSubType voucherSubType) {
        return voucherSubTypeRepository.save(voucherSubType);
    }

    @Transactional
    public VoucherSubType update(VoucherSubType voucherSubType) {
        return voucherSubTypeRepository.update(voucherSubType);
    }

    public Pagination<VoucherSubType> search(VoucherSubTypeSearch voucherSubTypeSearch) {

        Assert.notNull(voucherSubTypeSearch.getTenantId(), "tenantId is mandatory for voucher search");

        return voucherSubTypeRepository.search(voucherSubTypeSearch);
    }

}
