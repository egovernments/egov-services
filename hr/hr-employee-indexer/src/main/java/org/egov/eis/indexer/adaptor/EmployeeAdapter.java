package org.egov.eis.indexer.adaptor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.egov.boundary.persistence.entity.Boundary;
import org.egov.core.web.contract.RequestInfo;
import org.egov.core.web.contract.RequestInfoWrapper;
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
import org.egov.eis.indexer.service.BoundaryService;
import org.egov.eis.indexer.service.CommonMasterService;
import org.egov.eis.indexer.service.FinancialsService;
import org.egov.eis.indexer.service.HRMasterService;
import org.egov.eis.model.Assignment;
import org.egov.eis.model.DepartmentalTest;
import org.egov.eis.model.EducationalQualification;
import org.egov.eis.model.Employee;
import org.egov.eis.model.Probation;
import org.egov.eis.model.Regularisation;
import org.egov.eis.model.ServiceHistory;
import org.egov.eis.model.TechnicalQualification;
import org.egov.eis.web.contract.EmployeeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeAdapter {

	@Autowired
	private HRMasterService hrMasterService;

	@Autowired
	private CommonMasterService commonMasterService;

	@Autowired
	private BoundaryService boundaryService;

	@Autowired
	private FinancialsService financialsService;

	public static final Logger LOGGER = LoggerFactory.getLogger(EmployeeAdapter.class);

	// FIXME : User's Name is required for createdBy & modifiedBy fields
	public EmployeeIndex indexOnCreate(EmployeeRequest employeeRequest) {
		EmployeeIndex employeeIndex = new EmployeeIndex();
		employeeIndex.setEmployeeDetails(getEmployeeDetails(employeeRequest));
		employeeIndex.setEmployeeAssignment(getEmployeeAssignments(employeeRequest));
		employeeIndex.setEmployeeEducation(getEmployeeEducation(employeeRequest));
		employeeIndex.setEmployeeJurisdiction(getEmployeeJurisdiction(employeeRequest));
		employeeIndex.setEmployeeProbation(getEmployeeProbation(employeeRequest));
		employeeIndex.setEmployeeRegularisation(getEmployeeRegularisation(employeeRequest));
		employeeIndex.setEmployeeService(getEmployeeService(employeeRequest));
		employeeIndex.setEmployeeTechnical(getEmployeeTechnical(employeeRequest));
		employeeIndex.setEmployeeTest(getEmployeeTest(employeeRequest));

		return employeeIndex;
	}

	private EmployeeDetails getEmployeeDetails(EmployeeRequest employeeRequest) {
		EmployeeDetails employeeDetails = new EmployeeDetails();
		Employee employee = employeeRequest.getEmployee();
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(employeeRequest.getRequestInfo());

		if (employee.getUser() != null) {
			employeeDetails.setAadharNumber(employee.getUser().getAadhaarNumber());
			employeeDetails.setUserName(employee.getUser().getName());
			employeeDetails.setPwdExpiryDate(employee.getUser().getPwdExpiryDate());
			employeeDetails.setMobileNumber(employee.getUser().getMobileNumber());
			employeeDetails.setAlternateNumber(employee.getUser().getAltContactNumber());
			employeeDetails.setEmailId(employee.getUser().getEmailId());
			employeeDetails.setEmployeeActive(employee.getUser().getActive());
			employeeDetails.setSalutation(employee.getUser().getSalutation());
			employeeDetails.setEmployeeName(employee.getUser().getUserName());
			if (employee.getUser().getType() != null) {
				employeeDetails.setUserType(employee.getUser().getType().toString());
			}
			if (employee.getUser().getGender() != null) {
				employeeDetails.setGender(employee.getUser().getGender().toString());
			}
			employeeDetails.setPanNumber(employee.getUser().getPan());

			employeeDetails.setAadharNumber(employee.getUser().getAadhaarNumber());

			// FIXME : dateOfBirth defined as string. Will change to Date once User Service fixes the format.
			// FIXME : TimeZone Difference.
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			try {
				employeeDetails.setDateOfBirth(sdf.parse(employee.getUser().getDob()));
			} catch (ParseException e) {
				LOGGER.error("Following Exception Occurred While Parsing DateOfBirth: " + e.getMessage());
				e.printStackTrace();
			}

			employeeDetails.setEmployeeCreatedDate(employee.getUser().getCreatedDate());
			if (employee.getUser().getCreatedBy() != null) {
				employeeDetails.setEmployeeCreatedBy(employee.getUser().getCreatedBy().toString());
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
		// FIXME populate UserInfo details too
		// ulbName;
		// ulbCode;
		// distName;
		// regName;
		// ulbGrade;
		employeeDetails.setEmployeeId(employee.getId());
		employeeDetails.setEmployeeCode(employee.getCode());
		employeeDetails.setDateOfAppointment(employee.getDateOfAppointment());
		employeeDetails.setDateOfRetirement(employee.getDateOfRetirement());
		employeeDetails.setEmployeeStatus(employee.getEmployeeStatus());
		if (employee.getEmployeeType() != null) {
			employeeDetails.setEmployeeType(hrMasterService
					.getEmployeeType(employee.getEmployeeType(), employee.getTenantId(), requestInfoWrapper).getName());
		}
		if (employee.getRecruitmentMode() != null) {
			employeeDetails.setRecruitmentMode(hrMasterService
					.getRecruitmentMode(employee.getRecruitmentMode(), employee.getTenantId(), requestInfoWrapper)
					.getName());
		}
		if (employee.getRecruitmentType() != null) {
			employeeDetails.setRecruitmentType(hrMasterService
					.getRecruitmentType(employee.getRecruitmentType(), employee.getTenantId(), requestInfoWrapper)
					.getName());
		}
		if (employee.getRecruitmentQuota() != null) {
			employeeDetails.setRecruitmentQuota(hrMasterService
					.getRecruitmentQuota(employee.getRecruitmentQuota(), employee.getTenantId(), requestInfoWrapper)
					.getName());
		}
		if (employee.getGroup() != null) {
			employeeDetails.setEmployeeGroup(hrMasterService
					.getGroup(employee.getGroup(), employee.getTenantId(), requestInfoWrapper).getName());
		}
		employeeDetails.setRetirementAge(employee.getRetirementAge());
		employeeDetails.setDateOfResignation(employee.getDateOfResignation());
		employeeDetails.setDateOfTermination(employee.getDateOfTermination());
		if (employee.getMotherTongue() != null) {
			employeeDetails.setMotherTongue(commonMasterService
					.getLanguage(employee.getMotherTongue(), employee.getTenantId(), requestInfoWrapper).getName());
		}
		if (employee.getReligion() != null) {
			employeeDetails.setReligion(commonMasterService
					.getReligion(employee.getReligion(), employee.getTenantId(), requestInfoWrapper).getName());
		}
		if (employee.getCommunity() != null) {
			employeeDetails.setCommunity(commonMasterService
					.getCommunity(employee.getCommunity(), employee.getTenantId(), requestInfoWrapper).getName());
		}
		if (employee.getCategory() != null) {
			employeeDetails.setEmployeeCategory(commonMasterService
					.getCategory(employee.getCategory(), employee.getTenantId(), requestInfoWrapper).getName());
		}
		employeeDetails.setPhysicallyDisabled(employee.getPhysicallyDisabled());
		employeeDetails.setMedicalReportProduced(employee.getMedicalReportProduced());
		if (employee.getLanguagesKnown() != null) {
			// FIXME Make just one call
			employeeDetails.setLanguagesKnown(employee.getLanguagesKnown().stream()
					.map(languageId -> commonMasterService
							.getLanguage(languageId, employee.getTenantId(), requestInfoWrapper).getName())
					.collect(Collectors.toList()).toString());
		}
		if (employee.getMaritalStatus() != null) {
			employeeDetails.setMaritalStatus(employee.getMaritalStatus().toString());
		}
		employeeDetails.setPassportNo(employee.getPassportNo());
		employeeDetails.setGpfNo(employee.getGpfNo());
		if (employee.getBank() != null) {
			employeeDetails.setBank(financialsService
					.getBank(employee.getBank(), employee.getTenantId(), requestInfoWrapper).getName());
		}
		if (employee.getBankBranch() != null) {
			employeeDetails.setBankBranch(financialsService
					.getBankBranch(employee.getBankBranch(), employee.getTenantId(), requestInfoWrapper).getName());
		}
		employeeDetails.setBankAccount(employee.getBankAccount());
		employeeDetails.setPlaceOfBirth(employee.getPlaceOfBirth());

		return employeeDetails;
	}

	private List<EmployeeAssignment> getEmployeeAssignments(EmployeeRequest employeeRequest) {
		Employee employee = employeeRequest.getEmployee();
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(employeeRequest.getRequestInfo());

		List<Assignment> assignments = employee.getAssignments();
		return assignments.stream().map(assignment -> {
			EmployeeAssignment employeeAssignment = new EmployeeAssignment();

			employeeAssignment.setEmployeeId(employee.getId());
			employeeAssignment.setEmployeeCode(employee.getCode());
			employeeAssignment.setAssignmentId(assignment.getId());
			if (assignment.getFund() != null) {
				employeeAssignment.setFund(financialsService
						.getFund(assignment.getFund(), employee.getTenantId(), requestInfoWrapper).getName());
			}
			if (assignment.getFunction() != null) {
				employeeAssignment.setFunction(financialsService
						.getFunction(assignment.getFunction(), employee.getTenantId(), requestInfoWrapper).getName());
			}
			if (assignment.getDesignation() != null) {
				employeeAssignment.setDesignation(hrMasterService
						.getDesignation(assignment.getDesignation(), employee.getTenantId(), requestInfoWrapper)
						.getName());
			}
			if (assignment.getFunctionary() != null) {
				employeeAssignment.setFunctionary(financialsService
						.getFunctionary(assignment.getFunctionary(), employee.getTenantId(), requestInfoWrapper)
						.getName());
			}
			if (assignment.getDepartment() != null) {
				employeeAssignment.setDepartment(commonMasterService
						.getDepartment(assignment.getDepartment(), employee.getTenantId(), requestInfoWrapper)
						.getName());
			}
			if (assignment.getPosition() != null) {
				employeeAssignment.setPosition(hrMasterService
						.getPosition(assignment.getPosition(), employee.getTenantId(), requestInfoWrapper).getName());
			}
			if (assignment.getGrade() != null) {
				employeeAssignment.setGrade(hrMasterService
						.getGrade(assignment.getGrade(), employee.getTenantId(), requestInfoWrapper).getName());
			}
			employeeAssignment.setTodate(assignment.getToDate());
			employeeAssignment.setFromDate(assignment.getFromDate());
			if (assignment.getIsPrimary()) {
				employeeAssignment.setPrimaryAssignment(assignment.getIsPrimary().toString());
			}
			if (assignment.getHod() != null) {
				employeeAssignment.setHeadOfDepartmentCode(assignment.getHod().toString());
			}
			employeeAssignment.setAssignmentCreatedDate(assignment.getCreatedDate());
			if (assignment.getCreatedBy() != null) {
				employeeAssignment.setAssignmentCreatedBy(assignment.getCreatedBy().toString());
			}

			return employeeAssignment;
		}).collect(Collectors.toList());

	}

	private List<EmployeeEducation> getEmployeeEducation(EmployeeRequest employeeRequest) {
		Employee employee = employeeRequest.getEmployee();
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(employeeRequest.getRequestInfo());

		List<EducationalQualification> educations = employee.getEducation();

		return educations.stream().map(education -> {
			EmployeeEducation employeeEducation = new EmployeeEducation();

			employeeEducation.setQualificationId(education.getId());
			employeeEducation.setEmployeeId(employee.getId());
			employeeEducation.setEmployeeCode(employee.getCode());
			employeeEducation.setQualification(education.getQualification());
			if (education.getYearOfPassing() != null) {
				employeeEducation.setQualificationYear(education.getYearOfPassing().toString());
			}
			employeeEducation.setQualificationUniversity(education.getUniversity());
			// employeeEducation.setQualificationRemarks(education.);qualificationRemarks;
			// FIXME remarks
			if (education.getCreatedBy() != null) {
				employeeEducation.setQualificationCreatedBy(education.getCreatedBy().toString());
			}
			employeeEducation.setQualificationCreatedDate(education.getCreatedDate());

			return employeeEducation;
		}).collect(Collectors.toList());
	}

	private List<EmployeeProbation> getEmployeeProbation(EmployeeRequest employeeRequest) {
		Employee employee = employeeRequest.getEmployee();
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(employeeRequest.getRequestInfo());

		List<Probation> probations = employee.getProbation();

		return probations.stream().map(probation -> {
			EmployeeProbation employeeProbation = new EmployeeProbation();

			employeeProbation.setProbationId(probation.getId());
			employeeProbation.setEmployeeId(employee.getId());
			employeeProbation.setEmployeeCode(employee.getCode());
			if (probation.getDesignation() != null) {
				employeeProbation.setProbationDesignation(hrMasterService
						.getDesignation(probation.getDesignation(), employee.getTenantId(), requestInfoWrapper)
						.getName());
			}
			employeeProbation.setProbationDeclareDdate(probation.getDeclaredOn());
			employeeProbation.setProbationOrderNumber(probation.getOrderNo());
			employeeProbation.setProbationRemarks(probation.getRemarks());
			employeeProbation.setProbationOrderDate(probation.getOrderDate());
			employeeProbation.setProbationCreatedDate(probation.getCreatedDate());
			if (probation.getCreatedBy() != null) {
				employeeProbation.setProbationCreatedBy(probation.getCreatedBy().toString());
			}

			return employeeProbation;
		}).collect(Collectors.toList());
	}

	private List<EmployeeRegularisation> getEmployeeRegularisation(EmployeeRequest employeeRequest) {
		Employee employee = employeeRequest.getEmployee();
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(employeeRequest.getRequestInfo());

		List<Regularisation> regularisations = employee.getRegularisation();

		return regularisations.stream().map(regularisation -> {
			EmployeeRegularisation employeeRegularisation = new EmployeeRegularisation();

			employeeRegularisation.setRegularisationId(regularisation.getId());
			employeeRegularisation.setEmployeeId(employee.getId());
			employeeRegularisation.setEmployeeCode(employee.getCode());
			if (regularisation.getDesignation() != null) {
				employeeRegularisation.setRegularisationDesignation(hrMasterService
						.getDesignation(regularisation.getDesignation(), employee.getTenantId(), requestInfoWrapper)
						.getName());
			}
			employeeRegularisation.setRegularisationDeclaredDate(regularisation.getDeclaredOn());
			employeeRegularisation.setRegularisationOrderNumber(regularisation.getOrderNo());
			employeeRegularisation.setRegularisationRemarks(regularisation.getRemarks());
			employeeRegularisation.setRegularisationOrderDate(regularisation.getOrderDate());
			employeeRegularisation.setRegularisationCreatedDate(regularisation.getCreatedDate());
			if (regularisation.getCreatedBy() != null) {
				employeeRegularisation.setRegularisationCreatedBy(regularisation.getCreatedBy().toString());
			}

			return employeeRegularisation;
		}).collect(Collectors.toList());
	}

	private List<EmployeeServiceHistory> getEmployeeService(EmployeeRequest employeeRequest) {
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
			if (service.getCreatedBy() != null) {
				employeeService.setServiceCreatedBy(service.getCreatedBy().toString());
			}
			employeeService.setServiceCreatedDate(service.getCreatedDate());

			return employeeService;
		}).collect(Collectors.toList());
	}

	private List<EmployeeTechnical> getEmployeeTechnical(EmployeeRequest employeeRequest) {
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
			if (technical.getYearOfPassing() != null) {
				employeeTechnical.setTechnicalYear(technical.getYearOfPassing().toString());
			}
			employeeTechnical.setTechnicalGrade(technical.getGrade());
			employeeTechnical.setTechnicalRemarks(technical.getRemarks());
			if (technical.getCreatedBy() != null) {
				employeeTechnical.setTechnicalCreatedBy(technical.getCreatedBy().toString());
			}
			employeeTechnical.setTechnicalCreatedDate(technical.getCreatedDate());

			return employeeTechnical;
		}).collect(Collectors.toList());

	}

	private List<EmployeeTest> getEmployeeTest(EmployeeRequest employeeRequest) {
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
			if (test.getYearOfPassing() != null) {
				employeeTest.setTestPassedYear(test.getYearOfPassing().toString());
			}
			employeeTest.setTestRemarks(test.getRemarks());
			if (test.getCreatedBy() != null) {
				employeeTest.setTestCreatedBy(test.getCreatedBy().toString());
			}
			employeeTest.setTestCreatedDate(test.getCreatedDate());

			return employeeTest;
		}).collect(Collectors.toList());

	}

	private List<EmployeeJurisdiction> getEmployeeJurisdiction(EmployeeRequest employeeRequest) {
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

			// FIXME : Boundary type is not available now
			/*
			 * employeeJurisdiction.setBoundaryType(boundary.getBoundaryType().
			 * getName());
			 */
			employeeJurisdiction.setJurisdictionCreatedDate(new Date());
			if (boundary.getCreatedBy() != null) {
				employeeJurisdiction.setJurisdictionCreatedBy("System"); // FIXME
			}

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
