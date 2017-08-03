package org.egov.pgr.domain.service.validator;

import org.egov.pgr.domain.exception.PGRMasterException;
import org.egov.pgr.domain.model.ReceivingMode;
import org.egov.pgr.domain.service.ReceivingModeRequestValidator;
import org.egov.pgr.domain.service.ReceivingModeService;
import org.springframework.stereotype.Service;

import java.util.HashMap;


@Service
public class ReceivingModeValidator implements ReceivingModeRequestValidator {

    public static final String NAME = "name";
    public static final String FIELD = "field";
    public static final String CODE = "code";

    private ReceivingModeService receivingModeService;

    public ReceivingModeValidator(ReceivingModeService receivingModeService) {
        this.receivingModeService = receivingModeService;
    }

    @Override
    public boolean canValidate(ReceivingMode receivingMode) {
        return true;
    }

    @Override
    public void validate(ReceivingMode receivingMode) {
        HashMap<String, String> receivingModeHashMap = new HashMap<>();

        if (null == receivingMode.getCode() || receivingMode.getCode() == "") {
            receivingModeHashMap.put(CODE, "PGR.RECEIVING_MODE_CODE_MANDATORY");
            receivingModeHashMap.put(NAME, "receiving mode code not present");
            receivingModeHashMap.put(FIELD, "receivingMode.code");
            throw new PGRMasterException(receivingModeHashMap);
        }

        if (null == receivingMode.getName() || receivingMode.getName() == "") {
            receivingModeHashMap.put(CODE, "PGR.RECEIVING_MODE_NAME_MANDATORY");
            receivingModeHashMap.put(NAME, "receiving mode name not present");
            receivingModeHashMap.put(FIELD, "receivingMode.name");
            throw new PGRMasterException(receivingModeHashMap);
        }

        if (null == receivingMode.getTenantId() || receivingMode.getTenantId() == "") {
            receivingModeHashMap.put(CODE, "PGR.RECEIVING_MODE_TENANTID_MANDATORY");
            receivingModeHashMap.put(NAME, "receiving mode tenantId not present");
            receivingModeHashMap.put(FIELD, "receivingMode.tenantId");
            throw new PGRMasterException(receivingModeHashMap);
        }

        if (null == receivingMode.getDescription() || receivingMode.getDescription() == "") {
            receivingModeHashMap.put(CODE, "PGR.RECEIVING_MODE_DESCRIPTION_MANDATORY");
            receivingModeHashMap.put(NAME, "receiving mode description not present");
            receivingModeHashMap.put(FIELD, "receivingMode.description");
            throw new PGRMasterException(receivingModeHashMap);
        }

        if (!receivingMode.getTenantId().isEmpty() && null != receivingMode.getTenantId() && !(receivingMode.getTenantId().length() > 0 && receivingMode.getTenantId().length() <= 250)) {
            receivingModeHashMap.put(CODE, "PGR.RECEIVING_MODE_TENANT_ID_LENGTH");
            receivingModeHashMap.put(NAME, "receiving mode tenant id length greater than 250 characters");
            receivingModeHashMap.put(FIELD, "receivingMode.tenantId");
            throw new PGRMasterException(receivingModeHashMap);
        }

        if (null != receivingMode.getDescription() && !(receivingMode.getDescription().length() > 0 && receivingMode.getDescription().length() <= 250)) {
            receivingModeHashMap.put(CODE, "PGR.RECEIVING_MODE_DESCRIPTION_LENGTH");
            receivingModeHashMap.put(NAME, "receiving mode description length greater than 250 characters");
            receivingModeHashMap.put(FIELD, "receivingMode.description");
            throw new PGRMasterException(receivingModeHashMap);
        }

        if (receivingModeService.checkReceivingModeTypeByCode(receivingMode.getCode(), receivingMode.getTenantId())) {
            receivingModeHashMap.put(CODE, "PGR.RECEIVING_MODE_CODE_LENGTH");
            receivingModeHashMap.put(NAME, "entered receivingMode code already exist.");
            receivingModeHashMap.put(FIELD, "receivingMode.code");
            throw new PGRMasterException(receivingModeHashMap);
        }

        if (receivingModeService.checkReceivingModeTypeByName(receivingMode.getCode(), receivingMode.getName(), receivingMode.getTenantId())) {
            receivingModeHashMap.put(CODE, "PGR.RECEIVING_MODE_NAME_LENGTH");
            receivingModeHashMap.put(NAME, "entered receivingMode name already exist.");
            receivingModeHashMap.put(FIELD, "receivingMode.name");
            throw new PGRMasterException(receivingModeHashMap);
        }

        if (receivingMode.getChannels().isEmpty() && receivingMode.getChannels() != null) {
            receivingModeHashMap.put(CODE, "PGR.RECEIVING_MODE_CHANNEL_MANDATORY");
            receivingModeHashMap.put(NAME, "receiving mode channels are required.");
            receivingModeHashMap.put(FIELD, "receivingMode.channel");
            throw new PGRMasterException(receivingModeHashMap);
        }

    }
}
