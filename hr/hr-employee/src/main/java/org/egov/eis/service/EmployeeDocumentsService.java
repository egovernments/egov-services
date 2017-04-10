package org.egov.eis.service;

import java.util.List;

import org.egov.eis.model.Assignment;
import org.egov.eis.model.DepartmentalTest;
import org.egov.eis.model.EducationalQualification;
import org.egov.eis.model.Employee;
import org.egov.eis.model.EmployeeDocument;
import org.egov.eis.model.Probation;
import org.egov.eis.model.Regularisation;
import org.egov.eis.model.ServiceHistory;
import org.egov.eis.model.TechnicalQualification;
import org.egov.eis.model.enums.DocumentReferenceType;
import org.egov.eis.repository.EmployeeDocumentsRepository;
import org.springframework.stereotype.Component;

@Component
public class EmployeeDocumentsService {
	
	private EmployeeDocumentsRepository employeeDocumentsRepository;

	/**
	 * Query db and get all documents in a list.
	 * Iterate all the input documents for the employee and check if this document is present in the 
	 * list of docuemnts made from db in the previous step. If present, do nothing; if absent, insert this record
	 * in db.
	 * Finally, delete all documents in db that are not in input list.
	 * @param employee
	 */
	public void update(Employee employee) {
		List<EmployeeDocument> documentsFromDb = employeeDocumentsRepository.findByEmployeeId(employee.getId(), employee.getTenantId());

		if(employee.getDocuments() != null){
		updateDocuments(employee.getDocuments(), DocumentReferenceType.EMPLOYEE_HEADER, employee.getId(),
				documentsFromDb, employee.getId(), employee.getTenantId());
		}
		
		employee.getAssignments().forEach((assignment) -> {
			if(assignment.getDocuments() != null){
			//updateDocumentsForAssignment(assignment, documentsFromDb, employee.getId(), employee.getTenantId());
			updateDocuments(assignment.getDocuments(), DocumentReferenceType.ASSIGNMENT, assignment.getId(),
					documentsFromDb, employee.getId(), employee.getTenantId());
			}
		});
		employee.getProbation().forEach((probation) -> {
			if(probation.getDocuments() != null){
			updateDocuments(probation.getDocuments(), DocumentReferenceType.PROBATION, probation.getId(),
					documentsFromDb, employee.getId(), employee.getTenantId());
			}
		});
		employee.getRegularisation().forEach((regularisation) -> {
			if(regularisation.getDocuments() != null){
			updateDocuments(regularisation.getDocuments(), DocumentReferenceType.REGULARISATION, regularisation.getId(),
					documentsFromDb, employee.getId(), employee.getTenantId());
			}
		});
		employee.getTest().forEach((test) -> {
			if(test.getDocuments() != null){
			updateDocuments(test.getDocuments(), DocumentReferenceType.TEST, test.getId(),
					documentsFromDb, employee.getId(), employee.getTenantId());
			}
		});
		employee.getTechnical().forEach((technical) -> {
			if(technical.getDocuments() != null){
			updateDocuments(technical.getDocuments(), DocumentReferenceType.TECHNICAL, technical.getId(),
					documentsFromDb, employee.getId(), employee.getTenantId());
		}
		});
		employee.getEducation().forEach((education) -> {
			if(education.getDocuments() != null){
			updateDocuments(education.getDocuments(), DocumentReferenceType.EDUCATION, education.getId(),
					documentsFromDb, employee.getId(), employee.getTenantId());
			}
		});
	}
	
	private void updateDocuments(List<String> inputDocuments, DocumentReferenceType documentReferenceType,
			Long referenceId, List<EmployeeDocument> documentsFromDb, Long employeeId, String tenantId) {
		// insert all documents in input list but not in db
		for (String docUrl : inputDocuments) {
			if (!documentPresentInDB(documentsFromDb, docUrl, documentReferenceType)) {
				employeeDocumentsRepository.save(employeeId, docUrl, 
						documentReferenceType.toString(), referenceId, tenantId);
			}
		}
		
		// delete all docs from db that are not in input list
		for (EmployeeDocument documentInDb : documentsFromDb) {
			if (DocumentReferenceType.valueOf(documentInDb.getReferenceType()) == documentReferenceType
				&& !inputDocuments.contains(documentInDb.getDocument())) {
				employeeDocumentsRepository.delete(employeeId, documentInDb.getDocument(), 
							documentReferenceType.toString(), referenceId, tenantId);
			}
		}
	}
	
	private boolean documentPresentInDB(List<EmployeeDocument> documentsFromDb, String docUrl, DocumentReferenceType documentReferenceType) {
		boolean foundDocInDb = false;
		for (EmployeeDocument documentInDb : documentsFromDb) {
			if (DocumentReferenceType.valueOf(documentInDb.getReferenceType()) == documentReferenceType
					&& documentInDb.getDocument().equals(docUrl)) {
					foundDocInDb = true;
					break;
			}
		}
		return foundDocInDb;
	}
	
	public void populateDocumentsInRespectiveObjects(Employee employee) {
		List<EmployeeDocument> documents = employeeDocumentsRepository.findByEmployeeId(employee.getId(),
				employee.getTenantId());
		for (EmployeeDocument document : documents) {

			DocumentReferenceType referenceType = DocumentReferenceType.valueOf(document.getReferenceType());
			switch (referenceType) {
			case EMPLOYEE_HEADER:
				employee.getDocuments().add(document.getDocument());
				break;
			case ASSIGNMENT:
				for (Assignment assignment : employee.getAssignments())
					if (assignment.getId().equals(document.getReferenceId()))
						assignment.getDocuments().add(document.getDocument());
				break;
			case SERVICE:
				for (ServiceHistory serviceHistory : employee.getServiceHistory())
					if (serviceHistory.getId().equals(document.getReferenceId()))
						serviceHistory.getDocuments().add(document.getDocument());
				break;
			case TECHNICAL:
				for (TechnicalQualification technicalQualification : employee.getTechnical())
					if (technicalQualification.getId().equals(document.getReferenceId()))
						technicalQualification.getDocuments().add(document.getDocument());
				break;
			case EDUCATION:
				for (EducationalQualification educationalQualification : employee.getEducation())
					if (educationalQualification.getId().equals(document.getReferenceId()))
						educationalQualification.getDocuments().add(document.getDocument());
				break;
			case TEST:
				for (DepartmentalTest departmentalTest : employee.getTest())
					if (departmentalTest.getId().equals(document.getReferenceId()))
						departmentalTest.getDocuments().add(document.getDocument());
				break;
			case REGULARISATION:
				for (Regularisation regularisation : employee.getRegularisation())
					if (regularisation.getId().equals(document.getReferenceId()))
						regularisation.getDocuments().add(document.getDocument());
				break;
			case PROBATION:
				for (Probation probation : employee.getProbation())
					if (probation.getId().equals(document.getReferenceId()))
						probation.getDocuments().add(document.getDocument());
				break;
			}
		}	
	}


}
