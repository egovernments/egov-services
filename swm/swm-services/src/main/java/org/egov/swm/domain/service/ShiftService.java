package org.egov.swm.domain.service;

import java.util.Date;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.Shift;
import org.egov.swm.domain.model.ShiftSearch;
import org.egov.swm.domain.model.ShiftType;
import org.egov.swm.domain.repository.ShiftRepository;
import org.egov.swm.web.contract.Department;
import org.egov.swm.web.contract.DesignationResponse;
import org.egov.swm.web.repository.DesignationRepository;
import org.egov.swm.web.repository.IdgenRepository;
import org.egov.swm.web.requests.ShiftRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ShiftService {

    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private ShiftTypeService shiftTypeService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private DesignationRepository designationRepository;

    @Autowired
    private IdgenRepository idgenRepository;

    @Value("${egov.swm.shift.code.idgen.name}")
    private String idGenNameForShiftCodePath;

    @Transactional
    public ShiftRequest create(final ShiftRequest shiftRequest) {

        validate(shiftRequest);

        Long userId = null;

        if (shiftRequest.getRequestInfo() != null
                && shiftRequest.getRequestInfo().getUserInfo() != null
                && null != shiftRequest.getRequestInfo().getUserInfo().getId())
            userId = shiftRequest.getRequestInfo().getUserInfo().getId();

        for (final Shift s : shiftRequest.getShifts()) {

            setAuditDetails(s, userId);

            s.setCode(generateShiftCode(s.getTenantId(), shiftRequest.getRequestInfo()));

        }

        return shiftRepository.save(shiftRequest);

    }

    @Transactional
    public ShiftRequest update(final ShiftRequest shiftRequest) {

        Long userId = null;

        if (shiftRequest.getRequestInfo() != null
                && shiftRequest.getRequestInfo().getUserInfo() != null
                && null != shiftRequest.getRequestInfo().getUserInfo().getId())
            userId = shiftRequest.getRequestInfo().getUserInfo().getId();

        for (final Shift cp : shiftRequest.getShifts()) {

            setAuditDetails(cp, userId);
        }

        validate(shiftRequest);

        return shiftRepository.update(shiftRequest);

    }

    private void validate(final ShiftRequest shiftRequest) {

        ShiftType shiftType;
        Department department;
        DesignationResponse designationResponse;
        for (Shift shift : shiftRequest.getShifts()) {

            if (shift.getShiftType() != null
                    && (shift.getShiftType().getCode() == null || shift.getShiftType().getCode().isEmpty()))
                throw new CustomException("ShiftType",
                        "The field ShiftType Code is Mandatory . It cannot be not be null or empty.Please provide correct value ");

            shiftType = shiftTypeService.getShiftType(shift.getTenantId(),
                    shift.getShiftType().getCode(), shiftRequest.getRequestInfo());
            // Validate ShiftType
            if (shiftType != null && shiftType.getCode() != null)
                shift.setShiftType(shiftType);
            else
                throw new CustomException("ShiftType",
                        "The field ShiftType Code is Mandatory . It cannot be not be null or empty.Please provide correct value ");

            if (shift.getDepartment() != null
                    && (shift.getDepartment().getCode() == null || shift.getDepartment().getCode().isEmpty()))
                throw new CustomException("Department",
                        "The field Department Code is Mandatory . It cannot be not be null or empty.Please provide correct value ");

            department = departmentService.getDepartment(shift.getTenantId(),
                    shift.getDepartment().getCode(), shiftRequest.getRequestInfo());

            // Validate Department
            if (department != null && department.getCode() != null)
                shift.setDepartment(department);
            else {
                throw new CustomException("Department",
                        "The field Department Code is Mandatory . It cannot be not be null or empty.Please provide correct value ");
            }

            if (shift.getDesignation() != null
                    && (shift.getDesignation().getCode() == null || shift.getDesignation().getCode().isEmpty()))
                throw new CustomException("Designation",
                        "The field Designation Code is Mandatory . It cannot be not be null or empty.Please provide correct value ");

            // Validate Designation
            designationResponse = designationRepository.getDesignationByCode(shift.getDesignation().getCode(),
                    shift.getTenantId(),
                    shiftRequest.getRequestInfo());
            if (designationResponse != null && designationResponse.getDesignation() != null
                    && !designationResponse.getDesignation().isEmpty())
                shift.setDesignation(designationResponse.getDesignation().get(0));

        }
    }

    public Pagination<Shift> search(final ShiftSearch shiftSearch) {

        return shiftRepository.search(shiftSearch);
    }

    private String generateShiftCode(final String tenantId, final RequestInfo requestInfo) {

        return idgenRepository.getIdGeneration(tenantId, requestInfo, idGenNameForShiftCodePath);
    }

    private void setAuditDetails(final Shift contract, final Long userId) {

        if (contract.getAuditDetails() == null)
            contract.setAuditDetails(new AuditDetails());

        if (null == contract.getCode() || contract.getCode().isEmpty()) {
            contract.getAuditDetails().setCreatedBy(null != userId ? userId.toString() : null);
            contract.getAuditDetails().setCreatedTime(new Date().getTime());
        }

        contract.getAuditDetails().setLastModifiedBy(null != userId ? userId.toString() : null);
        contract.getAuditDetails().setLastModifiedTime(new Date().getTime());
    }

}