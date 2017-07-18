package org.egov.eis.indexer.adaptor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.egov.boundary.persistence.entity.Boundary;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.eis.indexer.service.*;
import org.egov.eis.web.contract.RequestInfoWrapper;
import org.egov.eis.indexer.model.es.EmployeeAssignment;
import org.egov.eis.indexer.model.es.EmployeeDetails;
import org.egov.eis.indexer.model.es.EmployeeEducation;
import org.egov.eis.indexer.model.es.EmployeeIndex;
import org.egov.eis.indexer.model.es.EmployeeJurisdiction;
import org.egov.eis.indexer.model.es.EmployeeProbation;
import org.egov.eis.indexer.model.es.EmployeeRegularisation;
import org.egov.eis.indexer.model.es.EmployeeServiceHistory;
import org.egov.eis.indexer.model.es.EmployeeTechnical;
import org.egov.eis.indexer.model.es.EmployeeTest;
import org.egov.eis.model.Assignment;
import org.egov.eis.model.DepartmentalTest;
import org.egov.eis.model.EducationalQualification;
import org.egov.eis.model.Employee;
import org.egov.eis.model.Probation;
import org.egov.eis.model.Regularisation;
import org.egov.eis.model.ServiceHistory;
import org.egov.eis.model.TechnicalQualification;
import org.egov.eis.web.contract.EmployeeRequest;
import org.egov.tenant.web.contract.City;
import org.egov.tenant.web.contract.Tenant;
import org.egov.user.web.contract.UserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.springframework.util.ObjectUtils.isEmpty;

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

    public static final Logger LOGGER = LoggerFactory.getLogger(EmployeeAdapter.class);

    // FIXME : User's Name is required for createdBy & modifiedBy fields
    public EmployeeIndex indexOnCreate(EmployeeRequest employeeRequest) {
        EmployeeIndex employeeIndex = new EmployeeIndex();
        Long userId = employeeRequest.getRequestInfo().getUserInfo().getId();

		UserRequest user = userService.getUser(userId, employeeRequest.getRequestInfo());

        employeeIndex.setEmployeeDetails(getEmployeeDetails(employeeRequest, user));
        employeeIndex.setEmployeeAssignment(getEmployeeAssignments(employeeRequest, user));
        employeeIndex.setEmployeeEducation(getEmployeeEducation(employeeRequest, user));
        employeeIndex.setEmployeeJurisdiction(getEmployeeJurisdiction(employeeRequest, user));
        employeeIndex.setEmployeeProbation(getEmployeeProbation(employeeRequest, user));
        employeeIndex.setEmployeeRegularisation(getEmployeeRegularisation(employeeRequest, user));
        employeeIndex.setEmployeeService(getEmployeeService(employeeRequest, user));
        employeeIndex.setEmployeeTechnical(getEmployeeTechnical(employeeRequest, user));
        employeeIndex.setEmployeeTest(getEmployeeTest(employeeRequest, user));

        return employeeIndex;
    }

    private EmployeeDetails getEmployeeDetails(EmployeeRequest employeeRequest, UserRequest user) {
        EmployeeDetails employeeDetails = new EmployeeDetails();
        Employee employee = employeeRequest.getEmployee();
        RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
        requestInfoWrapper.setRequestInfo(employeeRequest.getRequestInfo());

        Tenant tenant = tenantService.getTenant(employee.getTenantId(), requestInfoWrapper);

        if (!(isEmpty(tenant) || isEmpty(tenant.getCity()))) {
            City city = tenant.getCity();
            employeeDetails.setUlbName(city.getName());
            employeeDetails.setUlbCode(city.getCode());
            employeeDetails.setUlbGrade(city.getUlbGrade());
            employeeDetails.setRegName(city.getRegionName());
            employeeDetails.setDistName(city.getDistrictName());
        }

        if (!isEmpty(employee.getUser())) {
            employeeDetails.setAadharNumber(employee.getUser().getAadhaarNumber());
            employeeDetails.setUserName(employee.getUser().getName());
            employeeDetails.setPwdExpiryDate(employee.getUser().getPwdExpiryDate());
            employeeDetails.setMobileNumber(employee.getUser().getMobileNumber());
            employeeDetails.setAlternateNumber(employee.getUser().getAltContactNumber());
            employeeDetails.setEmailId(employee.getUser().getEmailId());
            employeeDetails.setEmployeeActive(employee.getUser().getActive());
            employeeDetails.setSalutation(employee.getUser().getSalutation());
            employeeDetails.setEmployeeName(employee.getUser().getUserName());
            employeeDetails.setUserType(employee.getUser().getType());
            employeeDetails.setGender(employee.getUser().getGender());
            employeeDetails.setPanNumber(employee.getUser().getPan());
            employeeDetails.setAadharNumber(employee.getUser().getAadhaarNumber());

            // FIXME : dateOfBirth defined as string. Will change to Date once User Service fixes the format.
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            try {
                employeeDetails.setDateOfBirth(sdf.parse(employee.getUser().getDob()));
            } catch (ParseException e) {
                LOGGER.error("Following Exception Occurred While Parsing DateOfBirth: " + e.getMessage());
                e.printStackTrace();
            }

            employeeDetails.setPermanentAddress(employee.getUser().getPermanentAddress());
            employeeDetails.setPermanentCity(employee.getUser().getPermanentCity());
            employeeDetails.setPermanentPinCode(employee.getUser().getPermanentPincode());
            employeeDetails.setCorrespondenceCity(employee.getUser().getCorrespondenceCity());
            employeeDetails.setCorrespondencePinCode(employee.getUser().getCorrespondencePincode());
            employeeDetails.setCorrespondenceAddress(employee.getUser().getCorrespondenceAddress());
            employeeDetails.setAccountLocked(employee.getUser().getAccountLocked());
            employeeDetails.setFatherOrHusbandname(employee.getUser().getFatherOrHusbandName());
            employeeDetails.setBloodGroup(employee.getUser().getBloodGroup());
            employeeDetails.setIdentificationMark(employee.getUser().getIdentificationMark());
        }

        employeeDetails.setEmployeeId(employee.getId());
        employeeDetails.setEmployeeCode(employee.getCode());
        employeeDetails.setDateOfAppointment(employee.getDateOfAppointment());
        employeeDetails.setDateOfRetirement(employee.getDateOfRetirement());
        employeeDetails.setEmployeeStatus(employee.getEmployeeStatus());
        employeeDetails.setMaritalStatus(employee.getMaritalStatus());
        employeeDetails.setRetirementAge(employee.getRetirementAge());
        employeeDetails.setDateOfResignation(employee.getDateOfResignation());
        employeeDetails.setDateOfTermination(employee.getDateOfTermination());
        employeeDetails.setBankAccount(employee.getBankAccount());
        employeeDetails.setPlaceOfBirth(employee.getPlaceOfBirth());
        employeeDetails.setPhysicallyDisabled(employee.getPhysicallyDisabled());
        employeeDetails.setMedicalReportProduced(employee.getMedicalReportProduced());
        employeeDetails.setPassportNo(employee.getPassportNo());
        employeeDetails.setGpfNo(employee.getGpfNo());

        if (!isEmpty(employee.getEmployeeType())) {
            employeeDetails.setEmployeeType(hrMasterService
                    .getEmployeeType(employee.getEmployeeType(), employee.getTenantId(), requestInfoWrapper).getName());
        }
        if (!isEmpty(employee.getRecruitmentMode())) {
            employeeDetails.setRecruitmentMode(hrMasterService
                    .getRecruitmentMode(employee.getRecruitmentMode(), employee.getTenantId(), requestInfoWrapper)
                    .getName());
        }
        if (!isEmpty(employee.getRecruitmentType())) {
            employeeDetails.setRecruitmentType(hrMasterService
                    .getRecruitmentType(employee.getRecruitmentType(), employee.getTenantId(), requestInfoWrapper)
                    .getName());
        }
        if (!isEmpty(employee.getRecruitmentQuota())) {
            employeeDetails.setRecruitmentQuota(hrMasterService
                    .getRecruitmentQuota(employee.getRecruitmentQuota(), employee.getTenantId(), requestInfoWrapper)
                    .getName());
        }
        if (!isEmpty(employee.getGroup())) {
            employeeDetails.setEmployeeGroup(hrMasterService
                    .getGroup(employee.getGroup(), employee.getTenantId(), requestInfoWrapper).getName());
        }
        if (!isEmpty(employee.getMotherTongue())) {
            employeeDetails.setMotherTongue(commonMasterService
                    .getLanguages(Arrays.asList(employee.getMotherTongue()), employee.getTenantId(), requestInfoWrapper)
                    .get(0).getName());
        }
        if (!isEmpty(employee.getReligion())) {
            employeeDetails.setReligion(commonMasterService
                    .getReligion(employee.getReligion(), employee.getTenantId(), requestInfoWrapper).getName());
        }
        if (!isEmpty(employee.getCommunity())) {
            employeeDetails.setCommunity(commonMasterService
                    .getCommunity(employee.getCommunity(), employee.getTenantId(), requestInfoWrapper).getName());
        }
        if (!isEmpty(employee.getCategory())) {
            employeeDetails.setEmployeeCategory(commonMasterService
                    .getCategory(employee.getCategory(), employee.getTenantId(), requestInfoWrapper).getName());
        }
        if (!isEmpty(employee.getBank())) {
            employeeDetails.setBank(financialsService
                    .getBank(employee.getBank(), employee.getTenantId(), requestInfoWrapper).getName());
        }
        if (!isEmpty(employee.getBankBranch())) {
            employeeDetails.setBankBranch(financialsService
                    .getBankBranch(employee.getBankBranch(), employee.getTenantId(), requestInfoWrapper).getName());
        }
        if (!isEmpty(employee.getLanguagesKnown())) {
            // FIXME Make just one call
            employeeDetails.setLanguagesKnown(commonMasterService
                    .getLanguages(employee.getLanguagesKnown(), employee.getTenantId(), requestInfoWrapper)
                    .stream().map(language -> language.getName()).collect(Collectors.toList()).toString());
        }

		employeeDetails.setEmployeeCreatedBy(user.getName());
		employeeDetails.setEmployeeCreatedDate(employee.getUser().getCreatedDate());
		employeeDetails.setEmployeeLastModifiedBy(user.getName());
		employeeDetails.setEmployeeLastModifiedDate(employee.getUser().getLastModifiedDate());

        return employeeDetails;
    }

    private List<EmployeeAssignment> getEmployeeAssignments(EmployeeRequest employeeRequest, UserRequest user) {
        Employee employee = employeeRequest.getEmployee();
        RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
        requestInfoWrapper.setRequestInfo(employeeRequest.getRequestInfo());

        List<Assignment> assignments = employee.getAssignments();
        return assignments.stream().map(assignment -> {
            EmployeeAssignment employeeAssignment = new EmployeeAssignment();

            employeeAssignment.setEmployeeId(employee.getId());
            employeeAssignment.setEmployeeCode(employee.getCode());
            employeeAssignment.setAssignmentId(assignment.getId());
            if (!isEmpty(assignment.getFund())) {
                employeeAssignment.setFund(financialsService
                        .getFund(assignment.getFund(), employee.getTenantId(), requestInfoWrapper).getName());
            }
            if (!isEmpty(assignment.getFunction())) {
                employeeAssignment.setFunction(financialsService
                        .getFunction(assignment.getFunction(), employee.getTenantId(), requestInfoWrapper).getName());
            }
            if (!isEmpty(assignment.getDesignation())) {
                employeeAssignment.setDesignation(hrMasterService
                        .getDesignation(assignment.getDesignation(), employee.getTenantId(), requestInfoWrapper)
                        .getName());
            }
            if (!isEmpty(assignment.getFunctionary())) {
                employeeAssignment.setFunctionary(financialsService
                        .getFunctionary(assignment.getFunctionary(), employee.getTenantId(), requestInfoWrapper)
                        .getName());
            }
            if (!isEmpty(assignment.getDepartment())) {
                employeeAssignment.setDepartment(commonMasterService
                        .getDepartment(assignment.getDepartment(), employee.getTenantId(), requestInfoWrapper)
                        .getName());
            }
            if (!isEmpty(assignment.getPosition())) {
                employeeAssignment.setPosition(hrMasterService
                        .getPosition(assignment.getPosition(), employee.getTenantId(), requestInfoWrapper).getName());
            }
            if (!isEmpty(assignment.getGrade())) {
                employeeAssignment.setGrade(hrMasterService
                        .getGrade(assignment.getGrade(), employee.getTenantId(), requestInfoWrapper).getName());
            }
            employeeAssignment.setTodate(assignment.getToDate());
            employeeAssignment.setFromDate(assignment.getFromDate());
            if (assignment.getIsPrimary()) {
                employeeAssignment.setPrimaryAssignment(assignment.getIsPrimary().toString());
            }
            if (!isEmpty(assignment.getHod())) {
                employeeAssignment.setHeadOfDepartmentCode(assignment.getHod().toString());
            }
			employeeAssignment.setAssignmentCreatedBy(user.getName());
			employeeAssignment.setAssignmentCreatedDate(assignment.getCreatedDate());
			employeeAssignment.setAssignmentLastModifiedBy(user.getName());
			employeeAssignment.setAssignmentLastModifiedDate(assignment.getLastModifiedDate());

            return employeeAssignment;
        }).collect(Collectors.toList());
    }

    private List<EmployeeEducation> getEmployeeEducation(EmployeeRequest employeeRequest, UserRequest user) {
        Employee employee = employeeRequest.getEmployee();

        List<EducationalQualification> educations = employee.getEducation();

        return educations.stream().map(education -> {
            EmployeeEducation employeeEducation = new EmployeeEducation();

            employeeEducation.setQualificationId(education.getId());
            employeeEducation.setEmployeeId(employee.getId());
            employeeEducation.setEmployeeCode(employee.getCode());
            employeeEducation.setQualification(education.getQualification());
			employeeEducation.setQualificationUniversity(education.getUniversity());
			if (!isEmpty(education.getYearOfPassing())) {
				employeeEducation.setQualificationYear(education.getYearOfPassing().toString());
			}
			// FIXME remarks
			// employeeEducation.setQualificationRemarks(education.getQualificationRemarks());
            employeeEducation.setQualificationCreatedBy(user.getName());
            employeeEducation.setQualificationCreatedDate(education.getCreatedDate());
            employeeEducation.setQualificationLastModifiedBy(user.getName());
            employeeEducation.setQualificationLastModifiedDate(education.getCreatedDate());

            return employeeEducation;
        }).collect(Collectors.toList());
    }

    private List<EmployeeProbation> getEmployeeProbation(EmployeeRequest employeeRequest, UserRequest user) {
        Employee employee = employeeRequest.getEmployee();
        RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
        requestInfoWrapper.setRequestInfo(employeeRequest.getRequestInfo());

        List<Probation> probations = employee.getProbation();

        return probations.stream().map(probation -> {
            EmployeeProbation employeeProbation = new EmployeeProbation();

            employeeProbation.setProbationId(probation.getId());
            employeeProbation.setEmployeeId(employee.getId());
            employeeProbation.setEmployeeCode(employee.getCode());
            if (!isEmpty(probation.getDesignation())) {
                employeeProbation.setProbationDesignation(hrMasterService
                        .getDesignation(probation.getDesignation(), employee.getTenantId(), requestInfoWrapper)
                        .getName());
            }
            employeeProbation.setProbationDeclareDdate(probation.getDeclaredOn());
            employeeProbation.setProbationOrderNumber(probation.getOrderNo());
            employeeProbation.setProbationRemarks(probation.getRemarks());
            employeeProbation.setProbationOrderDate(probation.getOrderDate());
			employeeProbation.setProbationCreatedBy(user.getName());
			employeeProbation.setProbationCreatedDate(probation.getCreatedDate());
			employeeProbation.setProbationLastModifiedBy(user.getName());
			employeeProbation.setProbationLastModifiedDate(probation.getLastModifiedDate());

            return employeeProbation;
        }).collect(Collectors.toList());
    }

    private List<EmployeeRegularisation> getEmployeeRegularisation(EmployeeRequest employeeRequest, UserRequest user) {
        Employee employee = employeeRequest.getEmployee();
        RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
        requestInfoWrapper.setRequestInfo(employeeRequest.getRequestInfo());

        List<Regularisation> regularisations = employee.getRegularisation();

        return regularisations.stream().map(regularisation -> {
            EmployeeRegularisation employeeRegularisation = new EmployeeRegularisation();

            employeeRegularisation.setRegularisationId(regularisation.getId());
            employeeRegularisation.setEmployeeId(employee.getId());
            employeeRegularisation.setEmployeeCode(employee.getCode());
            if (!isEmpty(regularisation.getDesignation())) {
                employeeRegularisation.setRegularisationDesignation(hrMasterService
                        .getDesignation(regularisation.getDesignation(), employee.getTenantId(), requestInfoWrapper)
                        .getName());
            }
            employeeRegularisation.setRegularisationDeclaredDate(regularisation.getDeclaredOn());
            employeeRegularisation.setRegularisationOrderNumber(regularisation.getOrderNo());
            employeeRegularisation.setRegularisationRemarks(regularisation.getRemarks());
            employeeRegularisation.setRegularisationOrderDate(regularisation.getOrderDate());
			employeeRegularisation.setRegularisationCreatedBy(user.getName());
			employeeRegularisation.setRegularisationCreatedDate(regularisation.getCreatedDate());
			employeeRegularisation.setRegularisationLastModifiedBy(user.getName());
			employeeRegularisation.setRegularisationLastModifiedDate(regularisation.getLastModifiedDate());

            return employeeRegularisation;
        }).collect(Collectors.toList());
    }

    private List<EmployeeServiceHistory> getEmployeeService(EmployeeRequest employeeRequest, UserRequest user) {
        Employee employee = employeeRequest.getEmployee();
        RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
        requestInfoWrapper.setRequestInfo(employeeRequest.getRequestInfo());

        List<ServiceHistory> services = employee.getServiceHistory();

        return services.stream().map(service -> {
            EmployeeServiceHistory employeeService = new EmployeeServiceHistory();

            employeeService.setServiceId(service.getId());
            employeeService.setEmployeeId(employee.getId());
            employeeService.setEmployeeCode(employee.getCode());
            // employeeService.setServiceEntry
            employeeService.setServiceFrom(service.getServiceFrom());
            employeeService.setServiceRemarks(service.getRemarks());
            employeeService.setServiceOrderNo(service.getOrderNo());
			employeeService.setServiceCreatedBy(user.getName());
			employeeService.setServiceCreatedDate(service.getCreatedDate());
			employeeService.setServiceLastModifiedBy(user.getName());
			employeeService.setServiceLastModifiedDate(service.getLastModifiedDate());

            return employeeService;
        }).collect(Collectors.toList());
    }

    private List<EmployeeTechnical> getEmployeeTechnical(EmployeeRequest employeeRequest, UserRequest user) {
        Employee employee = employeeRequest.getEmployee();
        RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
        requestInfoWrapper.setRequestInfo(employeeRequest.getRequestInfo());

        List<TechnicalQualification> technicals = employee.getTechnical();

        return technicals.stream().map(technical -> {
            EmployeeTechnical employeeTechnical = new EmployeeTechnical();

            employeeTechnical.setTechnicalId(technical.getId());
            employeeTechnical.setEmployeeId(employee.getId());
            employeeTechnical.setEmployeeCode(employee.getCode());
            employeeTechnical.setTechnicalSkill(technical.getSkill());
            if (!isEmpty(technical.getYearOfPassing())) {
                employeeTechnical.setTechnicalYear(technical.getYearOfPassing().toString());
            }
            employeeTechnical.setTechnicalGrade(technical.getGrade());
            employeeTechnical.setTechnicalRemarks(technical.getRemarks());
			employeeTechnical.setTechnicalCreatedBy(user.getName());
			employeeTechnical.setTechnicalCreatedDate(technical.getCreatedDate());
			employeeTechnical.setTechnicalLastModifiedBy(user.getName());
			employeeTechnical.setTechnicalLastModifiedDate(technical.getLastModifiedDate());

            return employeeTechnical;
        }).collect(Collectors.toList());
    }

    private List<EmployeeTest> getEmployeeTest(EmployeeRequest employeeRequest, UserRequest user) {
        Employee employee = employeeRequest.getEmployee();
        RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
        requestInfoWrapper.setRequestInfo(employeeRequest.getRequestInfo());

        List<DepartmentalTest> tests = employee.getTest();

        return tests.stream().map(test -> {
            EmployeeTest employeeTest = new EmployeeTest();

            employeeTest.setTestId(test.getId());
            employeeTest.setEmployeeId(employee.getId());
            employeeTest.setEmployeeCode(employee.getCode());
            employeeTest.setTestName(test.getTest());
            if (!isEmpty(test.getYearOfPassing())) {
                employeeTest.setTestPassedYear(test.getYearOfPassing().toString());
            }
            employeeTest.setTestRemarks(test.getRemarks());
			employeeTest.setTestCreatedBy(user.getName());
			employeeTest.setTestCreatedDate(test.getCreatedDate());
			employeeTest.setTestLastModifiedBy(user.getName());
			employeeTest.setTestLastModifiedDate(test.getLastModifiedDate());

            return employeeTest;
        }).collect(Collectors.toList());
    }

    private List<EmployeeJurisdiction> getEmployeeJurisdiction(EmployeeRequest employeeRequest, UserRequest user) {
        Employee employee = employeeRequest.getEmployee();
        RequestInfo requestInfo = employeeRequest.getRequestInfo();

        String jurisdictionIdsInCsv = String.join(",", employee.getJurisdictions().stream()
                .map(jurisdictionId -> jurisdictionId.toString()).collect(Collectors.toList()));
        List<Boundary> boundaries = boundaryService.getBoundary(jurisdictionIdsInCsv, requestInfo);

        return boundaries.stream().map(boundary -> {
            EmployeeJurisdiction employeeJurisdiction = new EmployeeJurisdiction();

            employeeJurisdiction.setEmployeeId(employee.getId());
            employeeJurisdiction.setEmployeeCode(employee.getCode());
            employeeJurisdiction.setBoundaryName(boundary.getName());
			employeeJurisdiction.setBoundaryType(boundary.getBoundaryType().getName());

			// FIXME : Jurisdiction is a list of Ids. So, this populating data
			employeeJurisdiction.setJurisdictionCreatedBy(user.getName());
			employeeJurisdiction.setJurisdictionCreatedDate(new Date());
			employeeJurisdiction.setJurisdictionLastModifiedBy(user.getName());
			employeeJurisdiction.setJurisdictionLastModifiedDate(new Date());

            return employeeJurisdiction;
        }).collect(Collectors.toList());
    }

    public void setOldCreatedByAndCreatedDateForEditEmployee(EmployeeIndex esEmployeeIndex,
                                                             EmployeeIndex newEmployeeIndex) {
        newEmployeeIndex.getEmployeeAssignment().forEach((newAssignment) -> {
            esEmployeeIndex.getEmployeeAssignment().forEach((esAssignment) -> {
                if (newAssignment.getAssignmentId().equals(esAssignment.getAssignmentId())) {
                    newAssignment.setAssignmentCreatedBy(esAssignment.getAssignmentCreatedBy());
                    newAssignment.setAssignmentCreatedDate(esAssignment.getAssignmentCreatedDate());
                }
            });
        });

        newEmployeeIndex.getEmployeeEducation().forEach((newEducation) -> {
            esEmployeeIndex.getEmployeeEducation().forEach((esEducation) -> {
                if (newEducation.getQualificationId().equals(esEducation.getQualificationId())) {
                    newEducation.setQualificationCreatedBy(esEducation.getQualificationCreatedBy());
                    newEducation.setQualificationCreatedDate(esEducation.getQualificationCreatedDate());
                }
            });
        });

        newEmployeeIndex.getEmployeeProbation().forEach((newProbation) -> {
            esEmployeeIndex.getEmployeeProbation().forEach((esProbation) -> {
                if (newProbation.getProbationId().equals(esProbation.getProbationId())) {
                    newProbation.setProbationCreatedBy(esProbation.getProbationCreatedBy());
                    newProbation.setProbationCreatedDate(esProbation.getProbationCreatedDate());
                }
            });
        });

        newEmployeeIndex.getEmployeeRegularisation().forEach((newRegularisation) -> {
            esEmployeeIndex.getEmployeeRegularisation().forEach((esRegularisation) -> {
                if (newRegularisation.getRegularisationId().equals(esRegularisation.getRegularisationId())) {
                    newRegularisation.setRegularisationCreatedBy(esRegularisation.getRegularisationCreatedBy());
                    newRegularisation.setRegularisationCreatedDate(esRegularisation.getRegularisationCreatedDate());
                }
            });
        });

        newEmployeeIndex.getEmployeeService().forEach((newService) -> {
            esEmployeeIndex.getEmployeeService().forEach((esService) -> {
                if (newService.getServiceId().equals(esService.getServiceId())) {
                    newService.setServiceCreatedBy(esService.getServiceCreatedBy());
                    newService.setServiceCreatedDate(esService.getServiceCreatedDate());
                }
            });
        });

        newEmployeeIndex.getEmployeeTechnical().forEach((newTechnical) -> {
            esEmployeeIndex.getEmployeeTechnical().forEach((esTechnical) -> {
                if (newTechnical.getTechnicalId().equals(esTechnical.getTechnicalId())) {
                    newTechnical.setTechnicalCreatedBy(esTechnical.getTechnicalCreatedBy());
                    newTechnical.setTechnicalCreatedDate(esTechnical.getTechnicalCreatedDate());
                }
            });
        });

        newEmployeeIndex.getEmployeeTest().forEach((newTest) -> {
            esEmployeeIndex.getEmployeeTest().forEach((esTest) -> {
                if (newTest.getTestId().equals(esTest.getTestId())) {
                    newTest.setTestCreatedBy(esTest.getTestCreatedBy());
                    newTest.setTestCreatedDate(esTest.getTestCreatedDate());
                }
            });
        });
    }
}
