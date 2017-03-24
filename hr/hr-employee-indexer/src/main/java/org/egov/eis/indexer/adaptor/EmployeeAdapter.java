package org.egov.eis.indexer.adaptor;


import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.egov.boundary.persistence.entity.Boundary;
import org.egov.core.web.contract.RequestInfo;
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
	
	public EmployeeIndex indexOnCreate(EmployeeRequest employeeRequest) {

		EmployeeIndex employeeIndex = new EmployeeIndex();
	//	System.out.println("employee =" + employeeRequest.getEmployee());
		employeeIndex.setEmployeeDetails(getEmployeeDetails(employeeRequest));
		employeeIndex.setEmployeeAssignment(getEmployeeAssignments(employeeRequest.getEmployee()));
		employeeIndex.setEmployeeEducation(getEmployeeEducation(employeeRequest.getEmployee()));
		employeeIndex.setEmployeeJurisdiction(getEmployeeJurisdiction(employeeRequest));
		employeeIndex.setEmployeeprobation(getEmployeeProbation(employeeRequest.getEmployee()));
		employeeIndex.setEmployeeRegularisation(getEmployeeRegularisation(employeeRequest.getEmployee()));
		employeeIndex.setEmployeeService(getEmployeeService(employeeRequest.getEmployee()));
		employeeIndex.setEmployeeTechnical(getEmployeeTechnical(employeeRequest.getEmployee()));
		employeeIndex.setEmployeeTest(getEmployeeTest(employeeRequest.getEmployee()));


		return employeeIndex;
	}

	private EmployeeDetails getEmployeeDetails(EmployeeRequest employeeRequest) {
		
		EmployeeDetails employeeDetails = new EmployeeDetails();
		Employee employee = employeeRequest.getEmployee();
		RequestInfo requestInfo = employeeRequest.getRequestInfo();
		
		if (employee.getUser() != null) {
			if (employee.getUser().getAadhaarNumber()!= null) {
				employeeDetails.setAadharNumber(employee.getUser().getAadhaarNumber().toString()); // FIXME Long toString()
			}
			employeeDetails.setUserName(employee.getUser().getName());
			employeeDetails.setPwdExpiryDate(employee.getUser().getPwdExpiryDate()); 
			employeeDetails.setMobileNumber(employee.getUser().getMobileNumber());
			employeeDetails.setAlternateNumber(employee.getUser().getAltContactNumber());
			employeeDetails.setEmailId(employee.getUser().getEmailId()); 
			employeeDetails.setEmployeeActive(employee.getUser().getActive());
			employeeDetails.setSalutation(employee.getUser().getSalutation());
			employeeDetails.setEmployeeName(employee.getUser().getUserName());
			if (employee.getUser().getType()!= null) {
				employeeDetails.setUserType(employee.getUser().getType().toString());
			}
			if (employee.getUser().getGender()!= null) {
				employeeDetails.setGender(employee.getUser().getGender().toString());
			}
			employeeDetails.setPanNumber(employee.getUser().getPan());
			if (employee.getUser().getAadhaarNumber()!= null) {
				employeeDetails.setAadharNumber(employee.getUser().getAadhaarNumber().toString()); // FIXME Long toString()
			}
			//employeeDetails.setDateOfBirth(employee.getUserInfo());dateOfBirth; FIXME Date of birth not included
			employeeDetails.setEmployeeCreatedDate(employee.getUser().getCreatedDate());
			if (employee.getUser().getCreatedBy()!= null) {
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
		employeeDetails.setEmployeeId(employee.getId().intValue());
		employeeDetails.setEmployeeCode(employee.getCode());
		employeeDetails.setDateOfAppointment(employee.getDateOfAppointment());
		employeeDetails.setDateOfRetirement(employee.getDateOfRetirement());
		if (employee.getEmployeeStatus()!= null) {
			employeeDetails.setEmployeeStatus(employee.getEmployeeStatus().toString()); 
		}
		if (employee.getEmployeeType()!= null) {
			employeeDetails.setEmployeeType(hrMasterService.getEmployeeType(employee.getEmployeeType(), employee.getTenantId(), requestInfo).getName());
		}
		if (employee.getRecruitmentMode()!= null) {
			employeeDetails.setRecruitmentMode(hrMasterService.getRecruitmentMode(employee.getRecruitmentMode(), employee.getTenantId(), requestInfo).getName());
		}
		if (employee.getRecruitmentType()!= null) {
			employeeDetails.setRecruitmentType(hrMasterService.getRecruitmentType(employee.getEmployeeType(), employee.getTenantId(), requestInfo).getName());
		}
		if (employee.getRecruitmentQuota()!= null) {
			employeeDetails.setRecruitmentQuota(hrMasterService.getRecruitmentQuota(employee.getEmployeeType(), employee.getTenantId(), requestInfo).getName());
		}
		employeeDetails.setRetirementAge(employee.getRetirementAge());
		employeeDetails.setDateOfResignation(employee.getDateOfResignation());
		employeeDetails.setDateOfTermination(employee.getDateOfTermination());
		if (employee.getMotherTongue()!= null) {
			employeeDetails.setMotherTongue(commonMasterService.getLanguage(employee.getMotherTongue(), employee.getTenantId(), requestInfo).getName()); //FIXME commonmaster
		}
		if (employee.getReligion()!= null) {
			employeeDetails.setReligion(commonMasterService.getReligion(employee.getReligion(), employee.getTenantId(), requestInfo).getName());
		}
		if (employee.getCommunity()!= null) {
			employeeDetails.setCommunity(commonMasterService.getCommunity(employee.getCommunity(), employee.getTenantId(), requestInfo).getName());
		}
		if (employee.getCategory()!= null) {
			employeeDetails.setEmployeeCategory(commonMasterService.getCategory(employee.getCategory(), employee.getTenantId(), requestInfo).getName());
		}
		employeeDetails.setPhysicallyDisabled(employee.getPhysicallyDisabled());
		employeeDetails.setMedicalReportProduced(employee.getMedicalReportProduced());
		if (employee.getLanguagesKnown()!= null) { // FIXME Makejust one call
			employeeDetails.setLanguagesKnown(employee.getLanguagesKnown().stream()
					.map(languageId -> commonMasterService.getLanguage(languageId, employee.getTenantId(), requestInfo).getName())
					.collect(Collectors.toList()).toString());
		}
		if (employee.getMaritalStatus()!= null) {
			employeeDetails.setMaritalStatus(employee.getMaritalStatus().toString());
		}
		employeeDetails.setPassportNo(employee.getPassportNo());
		employeeDetails.setGpfNo(employee.getGpfNo());
		if (employee.getBank()!= null) {
			employeeDetails.setBank(employee.getBank().toString());
		}
		if (employee.getBankBranch()!= null) {
			employeeDetails.setBankBranch(employee.getBankBranch().toString());
		}
		employeeDetails.setBankAccount(employee.getBankAccount());
		if (employee.getGroup()!= null) {
			employeeDetails.setEmployeeGroup(employee.getGroup().toString());
		}
		employeeDetails.setPlaceOfBirth(employee.getPlaceOfBirth());


		return employeeDetails;
	}

	private List<EmployeeAssignment> getEmployeeAssignments(Employee employee) {

		List<Assignment> assignments= employee.getAssignments();
		return assignments.stream().map(assignment -> {
			EmployeeAssignment employeeAssignment = new EmployeeAssignment();

			employeeAssignment.setEmployeeId(employee.getId().intValue());
			employeeAssignment.setEmployeeCode(employee.getCode());
			if (assignment.getId()!= null) {
				employeeAssignment.setAssignmentId(assignment.getId().toString());
			}
			if (assignment.getFund()!= null) {
				employeeAssignment.setFund(assignment.getFund().toString());
			}
			if (assignment.getFunction()!= null) {
				employeeAssignment.setFunction(assignment.getFunction().toString());
			}
			if (assignment.getDesignation()!= null) {
				employeeAssignment.setDesignation(assignment.getDesignation().toString());
			}
			if (assignment.getFunctionary()!= null) {
				employeeAssignment.setFunctionary(assignment.getFunctionary().toString());
			}
			if (assignment.getDepartment()!= null) {
				employeeAssignment.setDepartment(assignment.getDepartment().toString());
			}
			if (assignment.getPosition()!= null) {
				employeeAssignment.setPosition(assignment.getPosition().toString());
			}
			if (assignment.getGrade()!= null) {
				employeeAssignment.setGrade(assignment.getGrade().toString());
			}
			employeeAssignment.setTodate(assignment.getToDate());
			employeeAssignment.setFromDate(assignment.getFromDate());
			if (assignment.getIsPrimary()) {
				employeeAssignment.setPrimaryAssignment(assignment.getIsPrimary().toString());
			}
			if (assignment.getHod()!= null) {
				employeeAssignment.setHeadOfDepartmentCode(assignment.getHod().toString());
			}
			employeeAssignment.setAssignmentCreatedDate(assignment.getCreatedDate());
			if (assignment.getCreatedBy()!= null) {
				employeeAssignment.setAssignmentCreatedBy(assignment.getCreatedBy().toString());
			}

			return employeeAssignment;
		}).collect(Collectors.toList());

	}

	private List<EmployeeEducation> getEmployeeEducation(Employee employee) {

		List<EducationalQualification> educations = employee.getEducation();

		return educations.stream().map(education -> {
			EmployeeEducation employeeEducation = new EmployeeEducation();

			employeeEducation.setEmployeeId(employee.getId().intValue());
			employeeEducation.setEmployeeCode(employee.getCode());
			employeeEducation.setQualification(education.getQualification());
			if (education.getYearOfPassing()!= null) {
				employeeEducation.setQualificationYear(education.getYearOfPassing().toString());
			}
			employeeEducation.setQualificationUniversity(education.getUniversity());
			// employeeEducation.setQualificationRemarks(education.);qualificationRemarks; FIXME remarks 
			if (education.getCreatedBy()!= null) {
				employeeEducation.setQualificationCreatedBy(education.getCreatedBy().toString());
			}
			employeeEducation.setQualificationCreatedDate(education.getCreatedDate());

			return employeeEducation;
		}).collect(Collectors.toList());
	}

	private List<EmployeeProbation> getEmployeeProbation(Employee employee) {

		List<Probation> probations = employee.getProbation();

		return probations.stream().map(probation -> {
			EmployeeProbation employeeProbation = new EmployeeProbation();

			employeeProbation.setEmployeeId(employee.getId().intValue());
			employeeProbation.setEmployeeCode(employee.getCode());
			if (probation.getDesignation()!= null) {
				employeeProbation.setProbationDesignation(probation.getDesignation().toString());
			}
			employeeProbation.setProbationDeclareDdate(probation.getDeclaredOn());	
			employeeProbation.setProbationOrderNumber(probation.getOrderNo());
			employeeProbation.setProbationRemarks(probation.getRemarks());
			employeeProbation.setProbationOrderDate(probation.getOrderDate());	
			employeeProbation.setProbationCreatedDate(probation.getCreatedDate());	
			if (probation.getCreatedBy()!= null) {
				employeeProbation.setProbationCreatedBy(probation.getCreatedBy().toString());
			}

			return employeeProbation;
		}).collect(Collectors.toList());
	}

	private List<EmployeeRegularisation> getEmployeeRegularisation(Employee employee){

		List<Regularisation> regularisations = employee.getRegularisation();

		return regularisations.stream().map(regularisation -> {
			EmployeeRegularisation employeeRegularisation = new EmployeeRegularisation();

			employeeRegularisation.setEmployeeId(employee.getId().intValue());
			employeeRegularisation.setEmployeeCode(employee.getCode());
			if (regularisation.getDesignation()!= null) {
				employeeRegularisation.setRegularisationDesignation(regularisation.getDesignation().toString());
			}
			employeeRegularisation.setRegularisationDeclaredDate(regularisation.getDeclaredOn());	
			employeeRegularisation.setRegularisationOrderNumber(regularisation.getOrderNo());
			employeeRegularisation.setRegularisationRemarks(regularisation.getRemarks());
			employeeRegularisation.setRegularisationOrderDate(regularisation.getOrderDate());	
			employeeRegularisation.setRegularisationCreatedDate(regularisation.getCreatedDate());	
			if (regularisation.getCreatedBy()!= null) {
				employeeRegularisation.setRegularisationCreatedBy(regularisation.getCreatedBy().toString());
			}

			return employeeRegularisation;
		}).collect(Collectors.toList());
	}


	private List<EmployeeServiceHistory> getEmployeeService(Employee employee) {

		List<ServiceHistory> services = employee.getServiceHistory();

		return services.stream().map(service -> {
			EmployeeServiceHistory employeeService = new EmployeeServiceHistory();

			employeeService.setEmployeeId(employee.getId().intValue());
			employeeService.setEmployeeCode(employee.getCode());
			//employeeService.setServiceEntry
			employeeService.setServiceFrom(service.getServiceFrom());
			employeeService.setServiceRemarks(service.getRemarks());
			employeeService.setServiceOrderNo(service.getOrderNo());
			if (service.getCreatedBy()!= null) {
				employeeService.setServiceCreatedBy(service.getCreatedBy().toString());
			}
			employeeService.setServiceCreatedDate(service.getCreatedDate());

			return employeeService;
		}).collect(Collectors.toList());
	}

	private List<EmployeeTechnical> getEmployeeTechnical(Employee employee){

		List<TechnicalQualification> technicals = employee.getTechnical();

		return technicals.stream().map(technical -> {
			EmployeeTechnical employeeTechnical = new EmployeeTechnical();

			employeeTechnical.setEmployeeId(employee.getId().intValue());
			employeeTechnical.setEmployeeCode(employee.getCode());
			employeeTechnical.setTechnicalSkill(technical.getSkill());
			if (technical.getYearOfPassing()!= null) {
				employeeTechnical.setTechnicalYear(technical.getYearOfPassing().toString());
			}
			employeeTechnical.setTechnicalGrade(technical.getGrade());
			employeeTechnical.setTechnicalRemarks(technical.getRemarks());
			if (technical.getCreatedBy()!= null) {
				employeeTechnical.setTechnicalCreatedBy(technical.getCreatedBy().toString());
			}
			employeeTechnical.setTechnicalCreatedDate(technical.getCreatedDate());

			return employeeTechnical;
		}).collect(Collectors.toList());

	}

	private List<EmployeeTest> getEmployeeTest(Employee employee) {

		List<DepartmentalTest> tests = employee.getTest();

		return tests.stream().map(test -> {
			EmployeeTest employeeTest = new EmployeeTest();

			employeeTest.setEmployeeId(employee.getId().intValue());
			employeeTest.setEmployeeCode(employee.getCode());
			employeeTest.setTestName(test.getTest());
			if (test.getYearOfPassing()!= null) {
				employeeTest.setTestPassedYear(test.getYearOfPassing().toString());
			}
			employeeTest.setTestRemarks(test.getRemarks());
			if (test.getCreatedBy()!= null) {
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

			employeeJurisdiction.setEmployeeId(employee.getId().intValue());
			employeeJurisdiction.setEmployeeCode(employee.getCode());
			
			employeeJurisdiction.setBoundaryName(boundary.getName());

// FIXME : Boundary type is not available now
/*
			employeeJurisdiction.setBoundaryType(boundary.getBoundaryType().getName());	
*/
			employeeJurisdiction.setJurisdictionCreatedDate(new Date());	
			if (boundary.getCreatedBy()!= null) {
				employeeJurisdiction.setJurisdictionCreatedBy("System"); // FIXME
			}

			return employeeJurisdiction;
		}).collect(Collectors.toList());
	}



}
