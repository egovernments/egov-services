package org.egov.eis.service;

import static org.springframework.util.ObjectUtils.isEmpty;

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
import org.egov.eis.model.enums.EntityType;
import org.egov.eis.repository.EmployeeDocumentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmployeeDocumentsService {

	@Autowired
	private EmployeeDocumentsRepository employeeDocumentsRepository;

	/**
	 * Query db and get all documents in a list. Iterate all the input documents
	 * for the employee and check if this document is present in the list of
	 * docuemnts made from db in the previous step. If present, do nothing; if
	 * absent, insert this record in db. Finally, delete all documents in db
	 * that are not in input list.
	 * 
	 * @param employee
	 */
	public void update(Employee employee) {
		if(isEmpty(employee.getDocuments()))
			return;
		List<EmployeeDocument> documentsFromDb = employeeDocumentsRepository.findByEmployeeId(employee.getId(),
				employee.getTenantId());

		if (employee.getDocuments() != null) {
			updateDocuments(employee.getDocuments(), EntityType.EMPLOYEE_HEADER, employee.getId(), documentsFromDb,
					employee.getId(), employee.getTenantId());
		}

		if(!isEmpty(employee.getAssignments()))
		employee.getAssignments().forEach((assignment) -> {
			if (assignment.getDocuments() != null) {
				// updateDocumentsForAssignment(assignment, documentsFromDb,
				// employee.getId(), employee.getTenantId());
				updateDocuments(assignment.getDocuments(), EntityType.ASSIGNMENT, assignment.getId(), documentsFromDb,
						employee.getId(), employee.getTenantId());
			}
		});
		employee.getProbations().forEach((probation) -> {
			if (probation.getDocuments() != null) {
				updateDocuments(probation.getDocuments(), EntityType.PROBATION, probation.getId(), documentsFromDb,
						employee.getId(), employee.getTenantId());
			}
		});
		employee.getRegularisations().forEach((regularisation) -> {
			if (regularisation.getDocuments() != null) {
				updateDocuments(regularisation.getDocuments(), EntityType.REGULARISATION, regularisation.getId(),
						documentsFromDb, employee.getId(), employee.getTenantId());
			}
		});
		employee.getTests().forEach((test) -> {
			if (test.getDocuments() != null) {
				updateDocuments(test.getDocuments(), EntityType.TEST, test.getId(), documentsFromDb, employee.getId(),
						employee.getTenantId());
			}
		});
		employee.getTechnicals().forEach((technical) -> {
			if (technical.getDocuments() != null) {
				updateDocuments(technical.getDocuments(), EntityType.TECHNICAL, technical.getId(), documentsFromDb,
						employee.getId(), employee.getTenantId());
			}
		});
		employee.getEducations().forEach((education) -> {
			if (education.getDocuments() != null) {
				updateDocuments(education.getDocuments(), EntityType.EDUCATION, education.getId(), documentsFromDb,
						employee.getId(), employee.getTenantId());
			}
		});
	}

	private void updateDocuments(List<String> inputDocuments, EntityType entityType, Long referenceId,
			List<EmployeeDocument> documentsFromDb, Long employeeId, String tenantId) {
		// insert all documents in input list but not in db
		for (String docUrl : inputDocuments) {
			if (!documentPresentInDB(documentsFromDb, docUrl, entityType)) {
				employeeDocumentsRepository.save(employeeId, docUrl, entityType.getValue().toString(), referenceId,
						tenantId);
			}
		}

		// delete all docs from db that are not in input list
		for (EmployeeDocument documentInDb : documentsFromDb) {
			if (EntityType.valueOf(documentInDb.getReferenceType()) == entityType
					&& !inputDocuments.contains(documentInDb.getDocument())) {
				employeeDocumentsRepository.delete(employeeId, documentInDb.getDocument(), entityType.toString(),
						referenceId, tenantId);
			}
		}
	}

	private boolean documentPresentInDB(List<EmployeeDocument> documentsFromDb, String docUrl, EntityType entityType) {
		boolean foundDocInDb = false;
		for (EmployeeDocument documentInDb : documentsFromDb) {
			if (EntityType.valueOf(documentInDb.getReferenceType()) == entityType
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

			EntityType referenceType = EntityType.valueOf(document.getReferenceType());
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
				for (ServiceHistory serviceHistory : employee.getServiceHistories())
					if (serviceHistory.getId().equals(document.getReferenceId()))
						serviceHistory.getDocuments().add(document.getDocument());
				break;
			case TECHNICAL:
				for (TechnicalQualification technicalQualification : employee.getTechnicals())
					if (technicalQualification.getId().equals(document.getReferenceId()))
						technicalQualification.getDocuments().add(document.getDocument());
				break;
			case EDUCATION:
				for (EducationalQualification educationalQualification : employee.getEducations())
					if (educationalQualification.getId().equals(document.getReferenceId()))
						educationalQualification.getDocuments().add(document.getDocument());
				break;
			case TEST:
				for (DepartmentalTest departmentalTest : employee.getTests())
					if (departmentalTest.getId().equals(document.getReferenceId()))
						departmentalTest.getDocuments().add(document.getDocument());
				break;
			case REGULARISATION:
				for (Regularisation regularisation : employee.getRegularisations())
					if (regularisation.getId().equals(document.getReferenceId()))
						regularisation.getDocuments().add(document.getDocument());
				break;
			case PROBATION:
				for (Probation probation : employee.getProbations())
					if (probation.getId().equals(document.getReferenceId()))
						probation.getDocuments().add(document.getDocument());
				break;
			}
		}
	}
}
