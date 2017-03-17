package org.egov.web.indexer.adaptor;


import java.util.List;
import java.util.stream.Collectors;

import org.egov.eis.index.model.EmployeeAssignment;
import org.egov.eis.index.model.EmployeeDetails;
import org.egov.eis.index.model.EmployeeEducation;
import org.egov.eis.index.model.EmployeeIndex;
import org.egov.eis.index.model.EmployeeProbation;
import org.egov.eis.index.model.EmployeeRegularisation;
import org.egov.eis.index.model.EmployeeServiceHistory;
import org.egov.eis.index.model.EmployeeTechnical;
import org.egov.eis.index.model.EmployeeTest;
import org.egov.eis.model.Assignment;
import org.egov.eis.model.DepartmentalTest;
import org.egov.eis.model.EducationalQualification;
import org.egov.eis.model.Employee;
import org.egov.eis.model.Probation;
import org.egov.eis.model.Regularisation;
import org.egov.eis.model.ServiceHistory;
import org.egov.eis.model.TechnicalQualification;
import org.egov.web.indexer.eis.service.CommonMasterService;
import org.egov.web.indexer.eis.service.HRMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeAdapter {
	
	@Autowired
	private HRMasterService hrMasterService;
	
	@Autowired
	private CommonMasterService commonMasterService;
	


	public EmployeeIndex indexOnCreate(Employee employee) {

		EmployeeIndex employeeIndex = new EmployeeIndex();
		System.out.println("employee =" + employee);
		employeeIndex.setEmployeeDetails(getEmployeeDetails(employee));
		employeeIndex.setEmployeeAssignment(getEmployeeAssignments(employee));
		employeeIndex.setEmployeeEducation(getEmployeeEducation(employee));
		//employeeIndex.setEmployeeJurisdiction(getEmployeeJurisdiction(employee));
		employeeIndex.setEmployeeprobation(getEmployeeprobation(employee));
		employeeIndex.setEmployeeRegularisation(getEmployeeRegularisation(employee));
		employeeIndex.setEmployeeService(getEmployeeService(employee));
		employeeIndex.setEmployeeTechnical(getEmployeeTechnical(employee));
		employeeIndex.setEmployeeTest(getEmployeeTest(employee));


		return employeeIndex;
	}

	private EmployeeDetails getEmployeeDetails(Employee employee) {
		
		EmployeeDetails employeeDetails = new EmployeeDetails();
		if (employee.getUserInfo() != null) {
			if (employee.getUserInfo().getAadhaarNumber()!= null) {
				employeeDetails.setAadharNumber(employee.getUserInfo().getAadhaarNumber().toString()); // FIXME Long toString()
			}
			employeeDetails.setUserName(employee.getUserInfo().getName());
			employeeDetails.setPwdExpiryDate(employee.getUserInfo().getPwdExpiryDate()); 
			employeeDetails.setMobileNumber(employee.getUserInfo().getMobileNumber());
			employeeDetails.setAlternateNumber(employee.getUserInfo().getAltContactNumber());
			employeeDetails.setEmailId(employee.getUserInfo().getEmailId()); 
			employeeDetails.setEmployeeActive(employee.getUserInfo().getActive());
			employeeDetails.setSalutation(employee.getUserInfo().getSalutation());
			employeeDetails.setEmployeeName(employee.getUserInfo().getUserName());
			if (employee.getUserInfo().getType()!= null) {
				employeeDetails.setUserType(employee.getUserInfo().getType().toString());
			}
			if (employee.getUserInfo().getGender()!= null) {
				employeeDetails.setGender(employee.getUserInfo().getGender().toString());
			}
			employeeDetails.setPanNumber(employee.getUserInfo().getPan());
			if (employee.getUserInfo().getAadhaarNumber()!= null) {
				employeeDetails.setAadharNumber(employee.getUserInfo().getAadhaarNumber().toString()); // FIXME Long toString()
			}
			//employeeDetails.setDateOfBirth(employee.getUserInfo());dateOfBirth; FIXME Date of birth not included
			employeeDetails.setEmployeeCreatedDate(employee.getUserInfo().getCreatedDate());
			if (employee.getUserInfo().getCreatedBy()!= null) {
				employeeDetails.setEmployeeCreatedBy(employee.getUserInfo().getCreatedBy().toString());
			}
			employeeDetails.setPermanentAddress(employee.getUserInfo().getPermanentAddress()); 
			employeeDetails.setPermanentCity(employee.getUserInfo().getPermanentCity());
			employeeDetails.setPermanentPinCode(employee.getUserInfo().getPermanentPincode());
			employeeDetails.setCorrespondenceCity(employee.getUserInfo().getCorrespondenceCity());
			employeeDetails.setCorrespondencePinCode(employee.getUserInfo().getCorrespondencePincode());
			employeeDetails.setCorrespondenceAddress(employee.getUserInfo().getCorrespondenceAddress());
			employeeDetails.setAccountLocked(employee.getUserInfo().getAccountLocked());
			employeeDetails.setFatherOrHusbandname(employee.getUserInfo().getFatherOrHusbandName());
			employeeDetails.setBloodGroup(employee.getUserInfo().getBloodGroup());
			employeeDetails.setIdentificationMark(employee.getUserInfo().getIdentificationMark());
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
			employeeDetails.setEmployeeType(hrMasterService.getEmployeeType(employee.getEmployeeType()).getName());
		}
		if (employee.getRecruitmentMode()!= null) {
			employeeDetails.setRecruitmentMode(hrMasterService.getRecruitmentMode(employee.getRecruitmentMode()).getName());
		}
		if (employee.getRecruitmentType()!= null) {
			employeeDetails.setRecruitmentType(hrMasterService.getRecruitmentType(employee.getEmployeeType()).getName());
		}
		if (employee.getRecruitmentQuota()!= null) {
			employeeDetails.setRecruitmentQuota(hrMasterService.getRecruitmentQuota(employee.getEmployeeType()).getName());
		}
		employeeDetails.setRetirementAge(employee.getRetirementAge());
		employeeDetails.setDateOfResignation(employee.getDateOfResignation());
		employeeDetails.setDateOfTermination(employee.getDateOfTermination());
		if (employee.getMotherTongue()!= null) {
			employeeDetails.setMotherTongue(commonMasterService.getLanguage(employee.getMotherTongue()).getName()); //FIXME commonmaster
		}
		if (employee.getReligion()!= null) {
			employeeDetails.setReligion(commonMasterService.getReligion(employee.getReligion()).getName());
		}
		if (employee.getCommunity()!= null) {
			employeeDetails.setCommunity(commonMasterService.getCommunity(employee.getCommunity()).getName());
		}
		if (employee.getCategory()!= null) {
			employeeDetails.setEmployeeCategory(commonMasterService.getCategory(employee.getCategory()).getName());
		}
		employeeDetails.setPhysicallyDisabled(employee.getPhysicallyDisabled());
		employeeDetails.setMedicalReportProduced(employee.getMedicalReportProduced());
		if (employee.getLanguagesKnown()!= null) { // FIXME Makejust one call
			employeeDetails.setLanguagesKnown(employee.getLanguagesKnown().stream()
					.map(languageId -> commonMasterService.getLanguage(languageId).getName())
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

	private List<EmployeeProbation> getEmployeeprobation(Employee employee) {

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
	




}
