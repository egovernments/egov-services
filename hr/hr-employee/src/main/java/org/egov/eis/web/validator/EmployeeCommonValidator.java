package org.egov.eis.web.validator;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.egov.eis.model.Employee;
import org.egov.eis.model.EmployeeDocument;
import org.egov.eis.model.enums.EntityType;
import org.egov.eis.repository.EmployeeDocumentsRepository;
import org.egov.eis.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
/**
 * Has all validations that are common to create and update employee
 * @author ibm
 *
 */
@Component
public abstract class EmployeeCommonValidator {
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private EmployeeDocumentsRepository employeeDocumentsRepository;

	protected void validateEmployee(Employee employee, Errors errors) {
		if (employee.getRetirementAge() != null && employee.getRetirementAge() > 100)
			errors.rejectValue("employee.retirementAge", "invalid", "Invalid retirementAge");

		if ((employee.getDateOfAppointment() != null && employee.getDateOfJoining() != null)
				&& (employee.getDateOfAppointment().after(employee.getDateOfJoining()))) {
			errors.rejectValue("employee.dateOfAppointment", "invalid", "Invalid dateOfAppointment");
			errors.rejectValue("employee.dateOfJoining", "invalid", "Invalid dateOfJoining");
		}
		if ((employee.getDateOfResignation() != null && employee.getDateOfJoining() != null)
				&& (employee.getDateOfResignation().before(employee.getDateOfJoining()))) {
			errors.rejectValue("employee.dateOfJoining", "invalid", "Invalid dateOfJoining");
			errors.rejectValue("employee.dateOfResignation", "invalid", "Invalid dateOfResignation");
		}
		if ((employee.getDateOfTermination() != null && employee.getDateOfJoining() != null)
				&& (employee.getDateOfTermination().before(employee.getDateOfJoining()))) {
			errors.rejectValue("employee.dateOfJoining", "invalid", "Invalid dateOfJoining");
			errors.rejectValue("employee.dateOfTermination", "invalid", "Invalid dateOfTermination");
		}
		if ((employee.getDateOfRetirement() != null && employee.getDateOfJoining() != null)
				&& (employee.getDateOfRetirement().before(employee.getDateOfJoining()))) {
			errors.rejectValue("employee.dateOfJoining", "invalid", "Invalid dateOfJoining");
			errors.rejectValue("employee.dateOfRetirement", "invalid", "Invalid dateOfRetirement");
		}
	}
	
	public void validateDocumentsForNewEntity(List<String> docs, EntityType docType, String tenantId, Errors errors, int index) {
		if (isEmpty(docs))
			return;
		if (employeeRepository.checkForDuplicatesForAnyOneOfGivenCSV(
						"egeis_employeeDocuments", "document", getDocumentsAsCSVs(docs), tenantId)) {
			errors.rejectValue("employee." + docType.toString().toLowerCase() + "[" + index + "].documents", "concurrent",
					"document(s) already exists");
		}
	}
	
	public void validateEntityId(Map<Long, Integer> idsMap, EntityType entityType, Long employeeId,
			String tenantId, Errors errors) {
		List<Long> idsFromDB = employeeRepository.getListOfIds(entityType.getDbTable(), employeeId, tenantId);
		idsMap.keySet().forEach((id) -> {
			if (!idsFromDB.contains(id))
				errors.rejectValue("employee." + entityType.getValue().toLowerCase() + "[" + idsMap.get(id) + "].id", "doesn't exist",
						" " +entityType.getValue().toLowerCase() + " doesn't exist for this employee");
		});
	}
	

	
	private String getDocumentsAsCSVs(List<String> documents) {
		return "'" + String.join("','", documents) + "'";
	}

	public List<EmployeeDocument> getDocuments(Employee employee) {
		
		List<EmployeeDocument> inputDocuments = new ArrayList<>();
		if(!isEmpty(employee.getDocuments()))
			
		inputDocuments.addAll(employee.getDocuments().stream()
				.map(document -> new EmployeeDocument(null, null, document, EntityType.EMPLOYEE_HEADER.getValue(), employee.getId(),
						null))
				.collect(Collectors.toList()));

		if(!isEmpty(employee.getAssignments()))
		employee.getAssignments().forEach(assignment -> {
			if(!isEmpty(assignment.getDocuments()))
			inputDocuments.addAll(assignment.getDocuments().stream().map(document -> new EmployeeDocument(null, null,
					document, EntityType.ASSIGNMENT.getValue(), assignment.getId(), null)).collect(Collectors.toList()));
		});

		if(!isEmpty(employee.getTest()))
		employee.getTest().forEach(test -> {
			if(!isEmpty(test.getDocuments()))
			inputDocuments.addAll(test.getDocuments().stream().map(document -> new EmployeeDocument(null, null,
					document, EntityType.TEST.getValue(), test.getId(), null)).collect(Collectors.toList()));
		});

		if(!isEmpty(employee.getEducation()))
		employee.getEducation().forEach(education -> {
			if(!isEmpty(education.getDocuments()))
			inputDocuments.addAll(education.getDocuments().stream().map(document -> new EmployeeDocument(null, null,
					document, EntityType.EDUCATION.getValue(), education.getId(), null)).collect(Collectors.toList()));
		});

		if(!isEmpty(employee.getProbation()))
		employee.getProbation().forEach(probation -> {
			if(!isEmpty(probation.getDocuments()))
			inputDocuments.addAll(probation.getDocuments().stream().map(document -> new EmployeeDocument(null, null,
					document, EntityType.PROBATION.getValue(), probation.getId(), null)).collect(Collectors.toList()));
		});

		if(!isEmpty(employee.getRegularisation()))
		employee.getRegularisation().forEach(regularisation -> {
			if(!isEmpty(regularisation.getDocuments()))
			inputDocuments.addAll(regularisation.getDocuments().stream().map(document -> new EmployeeDocument(null,
					null, document, EntityType.REGULARISATION.getValue(), regularisation.getId(), null)).collect(Collectors.toList()));
		});

		if(!isEmpty(employee.getServiceHistory()))
		employee.getServiceHistory().forEach(serviceHistory -> {
			if(!isEmpty(serviceHistory.getDocuments()))
			inputDocuments.addAll(serviceHistory.getDocuments().stream().map(document -> new EmployeeDocument(null,
					null, document, EntityType.SERVICE.getValue(), serviceHistory.getId(), null)).collect(Collectors.toList()));
		});

		if(!isEmpty(employee.getTechnical()))
		employee.getTechnical().forEach(technical -> {
			if(!isEmpty(technical.getDocuments()))
			inputDocuments.addAll(technical.getDocuments().stream().map(document -> new EmployeeDocument(null, null,
					document, EntityType.TECHNICAL.getValue(), technical.getId(), null)).collect(Collectors.toList()));
		});
		
		return inputDocuments;
	}
	
	protected void validateDocuments(Employee employee, Errors errors) {

		List<EmployeeDocument> inputDocuments = getDocuments(employee);
		if (isEmpty(inputDocuments))
			return;
		EmployeeDocument duplicateDocument = getDuplicateWithinTheInput(inputDocuments);
		if (duplicateDocument != null) {
			List<Integer> index = getIndex(employee, EntityType.valueOf(duplicateDocument.getReferenceType()),
					duplicateDocument.getDocument());
			System.out.println("index=" + index);
			errors.rejectValue(getDocErrorMsg(index, EntityType.valueOf(duplicateDocument.getReferenceType())),
					"concurrent", "Duplicate document(s) found within the input");
			return;
		}
		List<String> inputDocumentUrls = inputDocuments.stream().map(d -> d.getDocument()).collect(Collectors.toList());
		List<EmployeeDocument> employeeDocumentsFromDb = employeeDocumentsRepository.findByDocuments(inputDocumentUrls,
				employee.getTenantId());
		populateDocumentErrors(inputDocuments, employeeDocumentsFromDb, employee, errors);
	}
	
	protected abstract void populateDocumentErrors(List<EmployeeDocument> inputDocuments,
			List<EmployeeDocument> employeeDocumentsFromDb, Employee employee, Errors errors);

	/**
	 * 
	 * @param inputDocuments
	 * @return the first duplicate within the given input documents
	 */
	private EmployeeDocument getDuplicateWithinTheInput(List<EmployeeDocument> inputDocuments) {
		for (int i=0; i<inputDocuments.size(); i++) 
			for (int j=0; j<inputDocuments.size(); j++) 
				if (i != j && inputDocuments.get(i).getDocument().equals(inputDocuments.get(j).getDocument()))
					return inputDocuments.get(i);
		return null;
	}

	protected String getDocErrorMsg(List<Integer> index, EntityType referenceType) {
		if (referenceType == EntityType.EMPLOYEE_HEADER) 
			return "employee.documents" + "[" + index.get(1) + "]";
		else
			return "employee." + referenceType.getContractFieldName() + "[" + index.get(0) + "]" 
			+ ".documents" + "[" + index.get(1) + "]";
	}

	protected List<Integer> getIndex(Employee employee, EntityType entityType, String document) {
		switch(entityType) {
		case EMPLOYEE_HEADER: for (int i=0; i<employee.getDocuments().size(); i++)
			if (employee.getDocuments().get(i).equals(document))
				return Arrays.asList(0, i); // for employee, we just need document index. 
			break; // So, we just put 0 for reference id and we ignore this anyway.
		case ASSIGNMENT: 
			for (int i=0; i<employee.getAssignments().size(); i++)
				for (int j=0; j<employee.getAssignments().get(i).getDocuments().size(); j++)
					if (employee.getAssignments().get(i).getDocuments().get(j).equals(document))
						return Arrays.asList(i, j);
			break;
		case SERVICE: 
			for (int i=0; i<employee.getServiceHistory().size(); i++)
				for (int j=0; j<employee.getServiceHistory().get(i).getDocuments().size(); j++)
					if (employee.getServiceHistory().get(i).getDocuments().get(j).equals(document))
						return Arrays.asList(i, j);
			break;
		case TECHNICAL: 
			for (int i=0; i<employee.getTechnical().size(); i++)
				for (int j=0; j<employee.getTechnical().get(i).getDocuments().size(); j++)
					if (employee.getTechnical().get(i).getDocuments().get(j).equals(document))
						return Arrays.asList(i, j);
			break;
		case EDUCATION: 
			for (int i=0; i<employee.getEducation().size(); i++)
				for (int j=0; j<employee.getEducation().get(i).getDocuments().size(); j++)
					if (employee.getEducation().get(i).getDocuments().get(j).equals(document))
						return Arrays.asList(i, j);
			break;
		case TEST: 
			for (int i=0; i<employee.getTest().size(); i++)
				for (int j=0; j<employee.getTest().get(i).getDocuments().size(); j++)
					if (employee.getTest().get(i).getDocuments().get(j).equals(document))
						return Arrays.asList(i, j);
			break;
		case REGULARISATION: 
			if (isEmpty(employee.getRegularisation()))
				break;
			for (int i=0; i<employee.getRegularisation().size(); i++) {
				if (isEmpty(employee.getRegularisation().get(i).getDocuments()))
					continue;
				for (int j=0; j<employee.getRegularisation().get(i).getDocuments().size(); j++) {
					System.out.printf("i=%d,j=%d,regsize=%d,docsize=%d\n",i,j,employee.getRegularisation().size(), 
							employee.getRegularisation().get(i).getDocuments().size());
					if (employee.getRegularisation().get(i).getDocuments().get(j).equals(document))
						return Arrays.asList(i, j);
				};
			};
			break;
		case PROBATION: 
			for (int i=0; i<employee.getProbation().size(); i++)
				for (int j=0; j<employee.getProbation().get(i).getDocuments().size(); j++)
					if (employee.getProbation().get(i).getDocuments().get(j).equals(document))
						return Arrays.asList(i, j);
			break;
		default:
			return null;
		}
		
		return null;
	}


}
