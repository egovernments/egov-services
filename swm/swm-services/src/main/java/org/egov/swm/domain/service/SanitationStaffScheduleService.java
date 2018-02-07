package org.egov.swm.domain.service;

import java.util.Date;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.SanitationStaffSchedule;
import org.egov.swm.domain.model.SanitationStaffScheduleSearch;
import org.egov.swm.domain.model.SanitationStaffTarget;
import org.egov.swm.domain.model.SanitationStaffTargetSearch;
import org.egov.swm.domain.model.Shift;
import org.egov.swm.domain.model.ShiftSearch;
import org.egov.swm.domain.repository.SanitationStaffScheduleRepository;
import org.egov.swm.web.repository.IdgenRepository;
import org.egov.swm.web.requests.SanitationStaffScheduleRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SanitationStaffScheduleService {

    @Autowired
    private SanitationStaffScheduleRepository sanitationStaffScheduleRepository;

    @Autowired
    private IdgenRepository idgenRepository;

    @Autowired
    private SanitationStaffTargetService sanitationStaffTargetService;

    @Autowired
    private ShiftService shiftService;

    @Value("${egov.swm.sanitation.staff.schedule.transaction.no.idgen.name}")
    private String idGenNameForTransactionNumPath;

    @Transactional
    public SanitationStaffScheduleRequest create(final SanitationStaffScheduleRequest sanitationStaffScheduleRequest) {

        validate(sanitationStaffScheduleRequest);

        Long userId = null;

        if (sanitationStaffScheduleRequest.getRequestInfo() != null
                && sanitationStaffScheduleRequest.getRequestInfo().getUserInfo() != null
                && null != sanitationStaffScheduleRequest.getRequestInfo().getUserInfo().getId())
            userId = sanitationStaffScheduleRequest.getRequestInfo().getUserInfo().getId();

        for (final SanitationStaffSchedule v : sanitationStaffScheduleRequest.getSanitationStaffSchedules()) {

            setAuditDetails(v, userId);

            v.setTransactionNo(
                    generateTransactionNumber(v.getTenantId(), sanitationStaffScheduleRequest.getRequestInfo()));

        }

        return sanitationStaffScheduleRepository.save(sanitationStaffScheduleRequest);

    }

    @Transactional
    public SanitationStaffScheduleRequest update(final SanitationStaffScheduleRequest sanitationStaffScheduleRequest) {

        Long userId = null;

        if (sanitationStaffScheduleRequest.getRequestInfo() != null
                && sanitationStaffScheduleRequest.getRequestInfo().getUserInfo() != null
                && null != sanitationStaffScheduleRequest.getRequestInfo().getUserInfo().getId())
            userId = sanitationStaffScheduleRequest.getRequestInfo().getUserInfo().getId();

        for (final SanitationStaffSchedule v : sanitationStaffScheduleRequest.getSanitationStaffSchedules())
            setAuditDetails(v, userId);

        validate(sanitationStaffScheduleRequest);

        return sanitationStaffScheduleRepository.update(sanitationStaffScheduleRequest);

    }

    private void validate(final SanitationStaffScheduleRequest sanitationStaffScheduleRequest) {

        final SanitationStaffTargetSearch sanitationStaffTargetSearch = new SanitationStaffTargetSearch();
        Pagination<SanitationStaffTarget> sanitationStaffTargets;
        ShiftSearch shiftSearch = new ShiftSearch();
        Pagination<Shift> shifts;
        for (final SanitationStaffSchedule sanitationStaffSchedule : sanitationStaffScheduleRequest
                .getSanitationStaffSchedules()) {

            // Validate Shift

            if (sanitationStaffSchedule.getShift() != null && (sanitationStaffSchedule.getShift().getCode() == null
                    || sanitationStaffSchedule.getShift().getCode().isEmpty()))
                throw new CustomException("Shift",
                        "The field Shift Code is Mandatory . It cannot be not be null or empty.Please provide correct value ");

            shiftSearch.setTenantId(sanitationStaffSchedule.getTenantId());
            shiftSearch.setCode(sanitationStaffSchedule.getShift().getCode());
            shifts = shiftService.search(shiftSearch);
            if (shifts != null && shifts.getPagedData() != null && !shifts.getPagedData().isEmpty())
                sanitationStaffSchedule.setShift(shifts.getPagedData().get(0));
            else
                throw new CustomException("Shift", "Given Shift is invalid: "
                        + sanitationStaffSchedule.getShift().getCode());

            // Validate Sanitation Staff Target

            if (sanitationStaffSchedule.getSanitationStaffTarget() != null
                    && (sanitationStaffSchedule.getSanitationStaffTarget().getTargetNo() == null
                            || sanitationStaffSchedule.getSanitationStaffTarget().getTargetNo().isEmpty()))
                throw new CustomException("SanitationStaffTarget", "Given SanitationStaffTarget is invalid: "
                        + sanitationStaffSchedule.getSanitationStaffTarget().getTargetNo());
            if (sanitationStaffSchedule.getSanitationStaffTarget() != null) {

                sanitationStaffTargetSearch.setTenantId(sanitationStaffSchedule.getTenantId());
                sanitationStaffTargetSearch
                        .setTargetNo(sanitationStaffSchedule.getSanitationStaffTarget().getTargetNo());
                sanitationStaffTargets = sanitationStaffTargetService.search(sanitationStaffTargetSearch);

                if (sanitationStaffTargets != null && sanitationStaffTargets.getPagedData() != null
                        && !sanitationStaffTargets.getPagedData().isEmpty())
                    sanitationStaffSchedule.setSanitationStaffTarget(sanitationStaffTargets.getPagedData().get(0));
                else
                    throw new CustomException("SanitationStaffTarget", "Given SanitationStaffTarget is invalid: "
                            + sanitationStaffSchedule.getSanitationStaffTarget().getTargetNo());

            }

        }
    }

    private String generateTransactionNumber(final String tenantId, final RequestInfo requestInfo) {

        return idgenRepository.getIdGeneration(tenantId, requestInfo, idGenNameForTransactionNumPath);
    }

    public Pagination<SanitationStaffSchedule> search(final SanitationStaffScheduleSearch sanitationStaffScheduleSearch) {

        return sanitationStaffScheduleRepository.search(sanitationStaffScheduleSearch);
    }

    private void setAuditDetails(final SanitationStaffSchedule contract, final Long userId) {

        if (contract.getAuditDetails() == null)
            contract.setAuditDetails(new AuditDetails());

        if (null == contract.getTransactionNo() || contract.getTransactionNo().isEmpty()) {
            contract.getAuditDetails().setCreatedBy(null != userId ? userId.toString() : null);
            contract.getAuditDetails().setCreatedTime(new Date().getTime());
        }

        contract.getAuditDetails().setLastModifiedBy(null != userId ? userId.toString() : null);
        contract.getAuditDetails().setLastModifiedTime(new Date().getTime());
    }

}