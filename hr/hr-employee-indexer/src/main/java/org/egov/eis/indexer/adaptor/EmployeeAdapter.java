package org.egov.eis.indexer.adaptor;

import lombok.extern.slf4j.Slf4j;
import org.egov.boundary.web.contract.Boundary;
import org.egov.common.contract.request.RequestInfo;
import org.egov.commons.model.*;
import org.egov.egf.persistence.queue.contract.*;
import org.egov.eis.indexer.model.*;
import org.egov.eis.indexer.service.*;
import org.egov.eis.model.*;
import org.egov.eis.web.contract.EmployeeRequest;
import org.egov.eis.web.contract.RequestInfoWrapper;
import org.egov.tenant.web.contract.City;
import org.egov.tenant.web.contract.Tenant;
import org.egov.user.web.contract.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;
import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@Service
public class EmployeeAdapter {

    @Autowired
    private HRMasterService hrMasterService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommonMasterService commonMasterService;

    @Autowired
    private BoundaryService boundaryService;

    @Autowired
    private FinancialsService financialsService;

    @Autowired
    private TenantService tenantService;

    public EmployeeIndex indexOnCreate(EmployeeRequest employeeRequest) {
        EmployeeIndex employeeIndex = new EmployeeIndex();
        employeeIndex.setEmployee(getEmployeeDetails(employeeRequest));
        return employeeIndex;
    }

    private Map<Long, UserRequest> getUsersMap(EmployeeRequest employeeRequest) {
        List<Long> ids = getAllUserIds(employeeRequest);
        List<UserRequest> users = userService.getUser(ids, employeeRequest);
        return users.stream().collect(Collectors.toMap(UserRequest::getId, identity()));
    }

    private List<Long> getAllUserIds(EmployeeRequest employeeRequest) {
        Employee employee = employeeRequest.getEmployee();
        Set<Long> ids = new HashSet<Long>() {{
            add(employee.getId());
            add(employeeRequest.getRequestInfo().getUserInfo().getId());
            addAll(employee.getAssignments().stream().map(Assignment::getCreatedBy).collect(Collectors.toSet()));
            addAll(employee.getAssignments().stream().map(Assignment::getLastModifiedBy).collect(Collectors.toSet()));
            addAll(employee.getServiceHistory().stream().map(ServiceHistory::getCreatedBy).collect(Collectors.toSet()));
            addAll(employee.getServiceHistory().stream().map(ServiceHistory::getLastModifiedBy).collect(Collectors.toSet()));
            addAll(employee.getProbation().stream().map(Probation::getCreatedBy).collect(Collectors.toSet()));
            addAll(employee.getProbation().stream().map(Probation::getLastModifiedBy).collect(Collectors.toSet()));
            addAll(employee.getRegularisation().stream().map(Regularisation::getCreatedBy).collect(Collectors.toSet()));
            addAll(employee.getRegularisation().stream().map(Regularisation::getLastModifiedBy).collect(Collectors.toSet()));
            addAll(employee.getTechnical().stream().map(TechnicalQualification::getCreatedBy).collect(Collectors.toSet()));
            addAll(employee.getTechnical().stream().map(TechnicalQualification::getLastModifiedBy).collect(Collectors.toSet()));
            addAll(employee.getEducation().stream().map(EducationalQualification::getCreatedBy).collect(Collectors.toSet()));
            addAll(employee.getEducation().stream().map(EducationalQualification::getLastModifiedBy).collect(Collectors.toSet()));
            addAll(employee.getTest().stream().map(DepartmentalTest::getCreatedBy).collect(Collectors.toSet()));
            addAll(employee.getTest().stream().map(DepartmentalTest::getLastModifiedBy).collect(Collectors.toSet()));
            addAll(employee.getAprDetails().stream().map(APRDetail::getCreatedBy).collect(Collectors.toSet()));
            addAll(employee.getAprDetails().stream().map(APRDetail::getLastModifiedBy).collect(Collectors.toSet()));
            add(employee.getCreatedBy());
            add(employee.getLastModifiedBy());
        }};

        return new ArrayList<>(ids);
    }

    private EmployeeDetails getEmployeeDetails(EmployeeRequest employeeRequest) {
        Employee employee = employeeRequest.getEmployee();
        RequestInfo requestInfo = employeeRequest.getRequestInfo();
        RequestInfoWrapper requestInfoWrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
        String tenantId = employee.getTenantId();
        User user = employee.getUser();
        Map<Long, UserRequest> usersMap = getUsersMap(employeeRequest);

        // Declaring separately to get languages & check for null values before doing get(0).
        List<Language> languages = getLanguages(Collections.singletonList(employee.getMotherTongue()), tenantId, requestInfoWrapper);
        Language motherTongue = isEmpty(languages) ? null : languages.get(0);

        return EmployeeDetails.builder()
                .id(employee.getId())
                .code(employee.getCode())
                // User Details
                .userName(user.getUserName())
                .salutation(user.getSalutation())
                .name(user.getName())
                .gender(user.getGender())
                .mobileNumber(user.getMobileNumber())
                .emailId(user.getEmailId())
                .altContactNumber(user.getAltContactNumber())
                .pan(user.getPan())
                .aadhaarNumber(user.getAadhaarNumber())
                .permanentAddress(user.getPermanentAddress())
                .permanentCity(user.getPermanentCity())
                .permanentPinCode(user.getPermanentPinCode())
                .correspondenceCity(user.getCorrespondenceCity())
                .correspondencePinCode(user.getCorrespondencePinCode())
                .correspondenceAddress(user.getCorrespondenceAddress())
                .active(user.getActive())
                // FIXME : dateOfBirth defined as string. Will change to Date once User Service fixes the format.
                .dob(user.getDob())
                .pwdExpiryDate(user.getPwdExpiryDate())
                .locale(user.getLocale())
                .type(user.getType())
                .accountLocked(user.getAccountLocked())
                .fatherOrHusbandName(user.getFatherOrHusbandName())
                .bloodGroup(user.getBloodGroup())
                .identificationMark(user.getIdentificationMark())
                .photo(user.getPhoto())
                .signature(user.getSignature())
                // Employee Details
                .dateOfAppointment(employee.getDateOfAppointment())
                .dateOfJoining(employee.getDateOfJoining())
                .dateOfRetirement(employee.getDateOfRetirement())
                .employeeStatus(getEmployeeStatus(employee.getEmployeeStatus(), tenantId, requestInfoWrapper))
                .recruitmentMode(getRecruitmentMode(employee.getRecruitmentMode(), tenantId, requestInfoWrapper))
                .recruitmentType(getRecruitmentType(employee.getRecruitmentType(), tenantId, requestInfoWrapper))
                .recruitmentQuota(getRecruitmentQuota(employee.getRecruitmentQuota(), tenantId, requestInfoWrapper))
                .retirementAge(employee.getRetirementAge())
                .dateOfResignation(employee.getDateOfResignation())
                .dateOfTermination(employee.getDateOfTermination())
                .employeeType(getEmployeeType(employee.getEmployeeType(), tenantId, requestInfoWrapper))
                .motherTongue(motherTongue)
                .religion(getReligion(employee.getReligion(), tenantId, requestInfoWrapper))
                .community(getCommunity(employee.getCommunity(), tenantId, requestInfoWrapper))
                .category(getCategory(employee.getCategory(), tenantId, requestInfoWrapper))
                .physicallyDisabled(employee.getPhysicallyDisabled())
                .medicalReportProduced(employee.getMedicalReportProduced())
                .languagesKnown(getLanguages(employee.getLanguagesKnown(), tenantId, requestInfoWrapper))
                .maritalStatus(employee.getMaritalStatus())
                .passportNo(employee.getPassportNo())
                .gpfNo(employee.getGpfNo())
                .bank(getBank(employee.getBank(), tenantId, requestInfoWrapper))
                .bankBranch(getBankBranch(employee.getBankBranch(), tenantId, requestInfoWrapper))
                .bankAccount(employee.getBankAccount())
                .group(getGroup(employee.getGroup(), tenantId, requestInfoWrapper))
                .placeOfBirth(employee.getPlaceOfBirth())
                .jurisdictions(getJurisdictions(employee, requestInfoWrapper, usersMap))
                .assignments(getAssignments(employee.getAssignments(), requestInfoWrapper, usersMap))
                .serviceHistory(getServiceHistory(employee.getServiceHistory(), requestInfoWrapper, usersMap))
                .probation(getProbation(employee.getProbation(), requestInfoWrapper, usersMap))
                .regularisation(getRegularisation(employee.getRegularisation(), requestInfoWrapper, usersMap))
                .technical(getTechnical(employee.getTechnical(), requestInfoWrapper, usersMap))
                .education(getEducation(employee.getEducation(), requestInfoWrapper, usersMap))
                .test(getTest(employee.getTest(), requestInfoWrapper, usersMap))
                .aprDetails(getAprDetails(employee.getAprDetails(), requestInfoWrapper, usersMap))
                .documents(employee.getDocuments())
                .auditDetails(getAuditDetails(employee.getCreatedBy(), employee.getCreatedDate(),
                        employee.getLastModifiedBy(), employee.getLastModifiedDate(), usersMap))
                .tenantDetails(getTenantDetails(tenantId, requestInfoWrapper))
                .build();
    }

    public void setOldCreatedByAndCreatedDateForUpdate(EmployeeIndex esEmployeeIndex, EmployeeIndex newEmployeeIndex) {
        EmployeeDetails esEmployeeDetails = esEmployeeIndex.getEmployee();
        EmployeeDetails newEmployeeDetails = newEmployeeIndex.getEmployee();

        newEmployeeDetails.getAssignments().forEach((newAssignment) ->
            esEmployeeDetails.getAssignments().forEach((esAssignment) -> {
                if (newAssignment.getId().equals(esAssignment.getId())) {
                    newAssignment.getAuditDetails().setCreatedBy(esAssignment.getAuditDetails().getCreatedBy());
                    newAssignment.getAuditDetails().setCreatedDate(esAssignment.getAuditDetails().getCreatedDate());
                }
            })
        );

        newEmployeeDetails.getEducation().forEach((newEducation) ->
            esEmployeeDetails.getEducation().forEach((esEducation) -> {
                if (newEducation.getId().equals(esEducation.getId())) {
                    newEducation.getAuditDetails().setCreatedBy(esEducation.getAuditDetails().getCreatedBy());
                    newEducation.getAuditDetails().setCreatedDate(esEducation.getAuditDetails().getCreatedDate());
                }
            })
        );

        newEmployeeDetails.getProbation().forEach((newProbation) ->
            esEmployeeDetails.getProbation().forEach((esProbation) -> {
                if (newProbation.getId().equals(esProbation.getId())) {
                    newProbation.getAuditDetails().setCreatedBy(esProbation.getAuditDetails().getCreatedBy());
                    newProbation.getAuditDetails().setCreatedDate(esProbation.getAuditDetails().getCreatedDate());
                }
            })
        );

        newEmployeeDetails.getRegularisation().forEach((newRegularisation) ->
            esEmployeeDetails.getRegularisation().forEach((esRegularisation) -> {
                if (newRegularisation.getId().equals(esRegularisation.getId())) {
                    newRegularisation.getAuditDetails().setCreatedBy(esRegularisation.getAuditDetails().getCreatedBy());
                    newRegularisation.getAuditDetails().setCreatedDate(esRegularisation.getAuditDetails().getCreatedDate());
                }
            })
        );

        newEmployeeDetails.getServiceHistory().forEach((newService) ->
            esEmployeeDetails.getServiceHistory().forEach((esService) -> {
                if (newService.getId().equals(esService.getId())) {
                    newService.getAuditDetails().setCreatedBy(esService.getAuditDetails().getCreatedBy());
                    newService.getAuditDetails().setCreatedDate(esService.getAuditDetails().getCreatedDate());
                }
            })
        );

        newEmployeeDetails.getTechnical().forEach((newTechnical) ->
            esEmployeeDetails.getTechnical().forEach((esTechnical) -> {
                if (newTechnical.getId().equals(esTechnical.getId())) {
                    newTechnical.getAuditDetails().setCreatedBy(esTechnical.getAuditDetails().getCreatedBy());
                    newTechnical.getAuditDetails().setCreatedDate(esTechnical.getAuditDetails().getCreatedDate());
                }
            })
        );

        newEmployeeDetails.getTest().forEach((newTest) ->
            esEmployeeDetails.getTest().forEach((esTest) -> {
                if (newTest.getId().equals(esTest.getId())) {
                    newTest.getAuditDetails().setCreatedBy(esTest.getAuditDetails().getCreatedBy());
                    newTest.getAuditDetails().setCreatedDate(esTest.getAuditDetails().getCreatedDate());
                }
            })
        );
    }

    private List<EmployeeJurisdiction> getJurisdictions(Employee employee, RequestInfoWrapper requestInfoWrapper,
                                                        Map<Long, UserRequest> usersMap) {
        List<Long> jurisdictionIds = employee.getJurisdictions();
        List<Boundary> boundaries = jurisdictionIds.stream().map(jurisdictionId ->
                boundaryService.getBoundary(jurisdictionId, employee.getTenantId())).collect(Collectors.toList());

        return boundaries.stream().map(boundary ->
            EmployeeJurisdiction.builder()
                    .id(boundary.getId())
                    .boundary(boundary)
                    .auditDetails(getAuditDetails(employee.getCreatedBy(), employee.getCreatedDate(), null, null, usersMap))
                    .tenantDetails(getTenantDetails(employee.getTenantId(), requestInfoWrapper))
                    .build()
        ).collect(Collectors.toList());
    }

    private List<EmployeeAssignment> getAssignments(List<Assignment> assignments, RequestInfoWrapper requestInfoWrapper,
                                                    Map<Long, UserRequest> usersMap) {
        return assignments.stream().map(assignment ->
                EmployeeAssignment.builder()
                        .id(assignment.getId())
                        .position(getPosition(assignment.getPosition(), assignment.getTenantId(), requestInfoWrapper))
                        .fund(getFund(assignment.getFund(), assignment.getTenantId(), requestInfoWrapper))
                        .function(getFunction(assignment.getFunction(), assignment.getTenantId(), requestInfoWrapper))
                        .functionary(getFunctionary(assignment.getFunctionary(), assignment.getTenantId(), requestInfoWrapper))
                        .department(getDepartment(assignment.getDepartment(), assignment.getTenantId(), requestInfoWrapper))
                        .designation(getDesignation(assignment.getDesignation(), assignment.getTenantId(), requestInfoWrapper))
                        .hod(getHod(assignment.getHod(), assignment.getTenantId(), requestInfoWrapper))
                        .isPrimary(assignment.getIsPrimary())
                        .fromDate(assignment.getFromDate())
                        .toDate(assignment.getToDate())
                        .grade(getGrade(assignment.getGrade(), assignment.getTenantId(), requestInfoWrapper))
                        .govtOrderNumber(assignment.getGovtOrderNumber())
                        .documents(assignment.getDocuments())
                        .auditDetails(getAuditDetails(assignment.getCreatedBy(), assignment.getCreatedDate(),
                                assignment.getLastModifiedBy(), assignment.getLastModifiedDate(), usersMap))
                        .tenantDetails(getTenantDetails(assignment.getTenantId(), requestInfoWrapper))
                        .build()
        ).collect(Collectors.toList());
    }

    private List<EmployeeServiceHistory> getServiceHistory(List<ServiceHistory> serviceHistories,
                                                           RequestInfoWrapper requestInfoWrapper, Map<Long, UserRequest> usersMap) {
        return serviceHistories.stream().map(serviceHistory ->
                EmployeeServiceHistory.builder()
                        .id(serviceHistory.getId())
                        .serviceInfo(serviceHistory.getServiceInfo())
                        .serviceFrom(serviceHistory.getServiceFrom())
                        .orderNo(serviceHistory.getOrderNo())
                        .remarks(serviceHistory.getRemarks())
                        .documents(serviceHistory.getDocuments())
                        .auditDetails(getAuditDetails(serviceHistory.getCreatedBy(), serviceHistory.getCreatedDate(),
                                serviceHistory.getLastModifiedBy(), serviceHistory.getLastModifiedDate(), usersMap))
                        .tenantDetails(getTenantDetails(serviceHistory.getTenantId(), requestInfoWrapper))
                        .build()
        ).collect(Collectors.toList());
    }

    private List<EmployeeProbation> getProbation(List<Probation> probations, RequestInfoWrapper requestInfoWrapper,
                                                 Map<Long, UserRequest> usersMap) {
        return probations.stream().map(probation ->
                EmployeeProbation.builder()
                        .id(probation.getId())
                        .designation(getDesignation(probation.getDesignation(), probation.getTenantId(), requestInfoWrapper))
                        .declaredOn(probation.getDeclaredOn())
                        .orderNo(probation.getOrderNo())
                        .orderDate(probation.getOrderDate())
                        .remarks(probation.getRemarks())
                        .documents(probation.getDocuments())
                        .auditDetails(getAuditDetails(probation.getCreatedBy(), probation.getCreatedDate(),
                                probation.getLastModifiedBy(), probation.getLastModifiedDate(), usersMap))
                        .tenantDetails(getTenantDetails(probation.getTenantId(), requestInfoWrapper))
                        .build()
        ).collect(Collectors.toList());
    }

    private List<EmployeeRegularisation> getRegularisation(List<Regularisation> regularisations,
                                                           RequestInfoWrapper requestInfoWrapper, Map<Long, UserRequest> usersMap) {
        return regularisations.stream().map(regularisation ->
                EmployeeRegularisation.builder()
                        .id(regularisation.getId())
                        .designation(getDesignation(regularisation.getDesignation(), regularisation.getTenantId(),
                                requestInfoWrapper))
                        .declaredOn(regularisation.getDeclaredOn())
                        .orderNo(regularisation.getOrderNo())
                        .orderDate(regularisation.getOrderDate())
                        .remarks(regularisation.getRemarks())
                        .documents(regularisation.getDocuments())
                        .auditDetails(getAuditDetails(regularisation.getCreatedBy(), regularisation.getCreatedDate(),
                                regularisation.getLastModifiedBy(), regularisation.getLastModifiedDate(), usersMap))
                        .tenantDetails(getTenantDetails(regularisation.getTenantId(), requestInfoWrapper))
                        .build()
        ).collect(Collectors.toList());
    }

    private List<EmployeeTechnical> getTechnical(List<TechnicalQualification> technicals,
                                                 RequestInfoWrapper requestInfoWrapper, Map<Long, UserRequest> usersMap) {
        return technicals.stream().map(technical ->
                EmployeeTechnical.builder()
                        .id(technical.getId())
                        .skill(technical.getSkill())
                        .grade(technical.getGrade())
                        .yearOfPassing(technical.getYearOfPassing())
                        .remarks(technical.getRemarks())
                        .documents(technical.getDocuments())
                        .auditDetails(getAuditDetails(technical.getCreatedBy(), technical.getCreatedDate(),
                                technical.getLastModifiedBy(), technical.getLastModifiedDate(), usersMap))
                        .tenantDetails(getTenantDetails(technical.getTenantId(), requestInfoWrapper))
                        .build()
        ).collect(Collectors.toList());
    }

    private List<EmployeeEducation> getEducation(List<EducationalQualification> educations,
                                                 RequestInfoWrapper requestInfoWrapper, Map<Long, UserRequest> usersMap) {
        return educations.stream().map(education ->
                EmployeeEducation.builder()
                        .id(education.getId())
                        .qualification(education.getQualification())
                        .majorSubject(education.getMajorSubject())
                        .yearOfPassing(education.getYearOfPassing())
                        .university(education.getUniversity())
                        .documents(education.getDocuments())
                        .auditDetails(getAuditDetails(education.getCreatedBy(), education.getCreatedDate(),
                                education.getLastModifiedBy(), education.getLastModifiedDate(), usersMap))
                        .tenantDetails(getTenantDetails(education.getTenantId(), requestInfoWrapper))
                        .build()
        ).collect(Collectors.toList());
    }

    private List<EmployeeTest> getTest(List<DepartmentalTest> tests, RequestInfoWrapper requestInfoWrapper, Map<Long,
            UserRequest> usersMap) {
        return tests.stream().map(test ->
                EmployeeTest.builder()
                        .id(test.getId())
                        .test(test.getTest())
                        .yearOfPassing(test.getYearOfPassing())
                        .remarks(test.getRemarks())
                        .documents(test.getDocuments())
                        .auditDetails(getAuditDetails(test.getCreatedBy(), test.getCreatedDate(),
                                test.getLastModifiedBy(), test.getLastModifiedDate(), usersMap))
                        .tenantDetails(getTenantDetails(test.getTenantId(), requestInfoWrapper))
                        .build()
        ).collect(Collectors.toList());
    }

    private List<EmployeeAPRDetail> getAprDetails(List<APRDetail> aprDetails, RequestInfoWrapper requestInfoWrapper,
                                                  Map<Long, UserRequest> usersMap) {
        return aprDetails.stream().map(aprDetail ->
                EmployeeAPRDetail.builder()
                        .id(aprDetail.getId())
                        .yearOfSubmission(aprDetail.getYearOfSubmission())
                        .detailsSubmitted(aprDetail.getDetailsSubmitted())
                        .dateOfSubmission(aprDetail.getDateOfSubmission())
                        .remarks(aprDetail.getRemarks())
                        .documents(aprDetail.getDocuments())
                        .auditDetails(getAuditDetails(aprDetail.getCreatedBy(), aprDetail.getCreatedDate(),
                                aprDetail.getLastModifiedBy(), aprDetail.getLastModifiedDate(), usersMap))
                        .tenantDetails(getTenantDetails(aprDetail.getTenantId(), requestInfoWrapper))
                        .build()
        ).collect(Collectors.toList());
    }

    private List<EmployeeHODDepartment> getHod(List<HODDepartment> hods, String tenantId, RequestInfoWrapper requestInfoWrapper) {
        return hods.stream().map(hod ->
                EmployeeHODDepartment.builder()
                        .id(hod.getId())
                        .department(getDepartment(hod.getDepartment(), tenantId, requestInfoWrapper))
                        .build()
        ).collect(Collectors.toList());
    }

    private Department getDepartment(Long id, String tenantId, RequestInfoWrapper requestInfoWrapper) {
        return isEmpty(id) ? null : commonMasterService.getDepartment(id, tenantId, requestInfoWrapper);
    }

    private Category getCategory(Long id, String tenantId, RequestInfoWrapper requestInfoWrapper) {
        return isEmpty(id) ? null : commonMasterService.getCategory(id, tenantId, requestInfoWrapper);
    }

    private Community getCommunity(Long id, String tenantId, RequestInfoWrapper requestInfoWrapper) {
        return isEmpty(id) ? null : commonMasterService.getCommunity(id, tenantId, requestInfoWrapper);
    }

    private List<Language> getLanguages(List<Long> ids, String tenantId, RequestInfoWrapper requestInfoWrapper) {
        return isEmpty(ids) ? null : commonMasterService.getLanguages(ids, tenantId, requestInfoWrapper);
    }

    private Religion getReligion(Long id, String tenantId, RequestInfoWrapper requestInfoWrapper) {
        return isEmpty(id) ? null : commonMasterService.getReligion(id, tenantId, requestInfoWrapper);
    }

    private HRStatus getEmployeeStatus(Long id, String tenantId, RequestInfoWrapper requestInfoWrapper) {
        return isEmpty(id) ? null : hrMasterService.getEmployeeStatus(id, tenantId, requestInfoWrapper);
    }

    private Designation getDesignation(Long id, String tenantId, RequestInfoWrapper requestInfoWrapper) {
        return isEmpty(id) ? null : hrMasterService.getDesignation(id, tenantId, requestInfoWrapper);
    }

    private EmployeeType getEmployeeType(Long id, String tenantId, RequestInfoWrapper requestInfoWrapper) {
        return isEmpty(id) ? null : hrMasterService.getEmployeeType(id, tenantId, requestInfoWrapper);
    }

    private Grade getGrade(Long id, String tenantId, RequestInfoWrapper requestInfoWrapper) {
        return isEmpty(id) ? null : hrMasterService.getGrade(id, tenantId, requestInfoWrapper);
    }

    private Group getGroup(Long id, String tenantId, RequestInfoWrapper requestInfoWrapper) {
        return isEmpty(id) ? null : hrMasterService.getGroup(id, tenantId, requestInfoWrapper);
    }

    private Position getPosition(Long id, String tenantId, RequestInfoWrapper requestInfoWrapper) {
        return isEmpty(id) ? null : hrMasterService.getPosition(id, tenantId, requestInfoWrapper);
    }

    private RecruitmentMode getRecruitmentMode(Long id, String tenantId, RequestInfoWrapper requestInfoWrapper) {
        return isEmpty(id) ? null : hrMasterService.getRecruitmentMode(id, tenantId, requestInfoWrapper);
    }

    private RecruitmentType getRecruitmentType(Long id, String tenantId, RequestInfoWrapper requestInfoWrapper) {
        return isEmpty(id) ? null : hrMasterService.getRecruitmentType(id, tenantId, requestInfoWrapper);
    }

    private RecruitmentQuota getRecruitmentQuota(Long id, String tenantId, RequestInfoWrapper requestInfoWrapper) {
        return isEmpty(id) ? null : hrMasterService.getRecruitmentQuota(id, tenantId, requestInfoWrapper);
    }

    private Bank getBank(Long id, String tenantId, RequestInfoWrapper requestInfoWrapper) {
        return isEmpty(id) ? null : financialsService.getBank(id, tenantId, requestInfoWrapper);
    }

    private BankBranch getBankBranch(Long id, String tenantId, RequestInfoWrapper requestInfoWrapper) {
        return isEmpty(id) ? null : financialsService.getBankBranch(id, tenantId, requestInfoWrapper);
    }

    private Fund getFund(Long id, String tenantId, RequestInfoWrapper requestInfoWrapper) {
        return isEmpty(id) ? null : financialsService.getFund(id, tenantId, requestInfoWrapper);
    }

    private Function getFunction(Long id, String tenantId, RequestInfoWrapper requestInfoWrapper) {
        return isEmpty(id) ? null : financialsService.getFunction(id, tenantId, requestInfoWrapper);
    }

    private Functionary getFunctionary(Long id, String tenantId, RequestInfoWrapper requestInfoWrapper) {
        return isEmpty(id) ? null : financialsService.getFunctionary(id, tenantId, requestInfoWrapper);
    }

    private AuditDetails getAuditDetails(Long createdBy, Date createdDate, Long lastModifiedBy, Date lastModifiedDate,
                                         Map<Long, UserRequest> usersMap) {
        UserDetails createdByUser = UserDetails.builder()
                .id(createdBy)
                .name(usersMap.get(createdBy).getName())
                .userName(usersMap.get(createdBy).getUserName())
                .build();
        UserDetails modifiedByUser = isEmpty(lastModifiedBy) ? null : UserDetails.builder()
                .id(lastModifiedBy)
                .name(usersMap.get(lastModifiedBy).getName())
                .userName(usersMap.get(lastModifiedBy).getUserName())
                .build();

        return AuditDetails.builder()
                .createdBy(createdByUser).createdDate(createdDate)
                .lastModifiedBy(modifiedByUser).lastModifiedDate(lastModifiedDate).build();
    }

    private TenantDetails getTenantDetails(String tenantId, RequestInfoWrapper requestInfoWrapper) {
        Tenant tenant = tenantService.getTenant(tenantId, requestInfoWrapper);

        if (!(isEmpty(tenant) || isEmpty(tenant.getCity()))) {
            City city = tenant.getCity();
            return TenantDetails.builder()
                    .tenantId(tenantId)
                    .ulbName(city.getName())
                    .ulbCode(city.getCode())
                    .ulbGrade(city.getUlbGrade())
                    .regName(city.getRegionName())
                    .distName(city.getDistrictName())
                    .build();
        }
        return TenantDetails.builder().tenantId(tenantId).build();
    }

}
