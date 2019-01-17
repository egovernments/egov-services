package org.egov.hrms.service;

import org.egov.hrms.model.*;
import org.egov.hrms.model.enums.EntityType;
import org.egov.hrms.repository.EmployeeDocumentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;

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

		List<EmployeeDocument> documentsFromDb = employeeDocumentsRepository.findByEmployeeId(employee.getId(),
				employee.getTenantId());

		if (employee.getDocuments() == null)
			employee.setDocuments(new ArrayList<>());

		updateDocuments(employee.getDocuments(), EntityType.EMPLOYEE_HEADER, employee.getId(), documentsFromDb,
				employee.getId(), employee.getTenantId());

		if (!isEmpty(employee.getAssignments()))
			employee.getAssignments().forEach((assignment) -> {
				if (assignment.getDocuments() == null)
					assignment.setDocuments(new ArrayList<>());
				updateDocuments(assignment.getDocuments(), EntityType.ASSIGNMENT, assignment.getId(), documentsFromDb,
						employee.getId(), employee.getTenantId());
			});
		if (!isEmpty(employee.getProbation()))
			employee.getProbation().forEach((probation) -> {
				if (probation.getDocuments() == null)
					probation.setDocuments(new ArrayList<>());
				updateDocuments(probation.getDocuments(), EntityType.PROBATION, probation.getId(), documentsFromDb,
						employee.getId(), employee.getTenantId());
			});
		if (!isEmpty(employee.getRegularisation()))
			employee.getRegularisation().forEach((regularisation) -> {
				if (regularisation.getDocuments() == null)
					regularisation.setDocuments(new ArrayList<>());
				updateDocuments(regularisation.getDocuments(), EntityType.REGULARISATION, regularisation.getId(),
						documentsFromDb, employee.getId(), employee.getTenantId());
			});
		if (!isEmpty(employee.getTest()))
			employee.getTest().forEach((test) -> {
				if (test.getDocuments() == null)
					test.setDocuments(new ArrayList<>());
				updateDocuments(test.getDocuments(), EntityType.TEST, test.getId(), documentsFromDb, employee.getId(),
						employee.getTenantId());
			});
		if (!isEmpty(employee.getTechnical()))
			employee.getTechnical().forEach((technical) -> {
				if (technical.getDocuments() == null)
					technical.setDocuments(new ArrayList<>());
				updateDocuments(technical.getDocuments(), EntityType.TECHNICAL, technical.getId(), documentsFromDb,
						employee.getId(), employee.getTenantId());
			});
		if (!isEmpty(employee.getEducation()))
			employee.getEducation().forEach((education) -> {
				if (education.getDocuments() == null)
					education.setDocuments(new ArrayList<>());
				updateDocuments(education.getDocuments(), EntityType.EDUCATION, education.getId(), documentsFromDb,
						employee.getId(), employee.getTenantId());
			});
		if (!isEmpty(employee.getServiceHistory()))
			employee.getServiceHistory().forEach((serviceHistory) -> {
				if (serviceHistory.getDocuments() == null)
					serviceHistory.setDocuments(new ArrayList<>());
				updateDocuments(serviceHistory.getDocuments(), EntityType.SERVICE, serviceHistory.getId(),
						documentsFromDb, employee.getId(), employee.getTenantId());
			});
		if (!isEmpty(employee.getAprDetails()))
			employee.getAprDetails().forEach((aprDetail) -> {
				if (aprDetail.getDocuments() == null)
					aprDetail.setDocuments(new ArrayList<>());
				updateDocuments(aprDetail.getDocuments(), EntityType.APR_DETAILS, aprDetail.getId(),
						documentsFromDb, employee.getId(), employee.getTenantId());
			});
	}

	protected void updateDocuments(List<String> inputDocuments, EntityType entityType, Long referenceId,
			List<EmployeeDocument> documentsFromDb, Long employeeId, String tenantId) {
		// insert all documents in input list but not in db
		for (String docUrl : inputDocuments) {
			if (!documentPresentInDB(documentsFromDb, docUrl, entityType, referenceId)) {
				employeeDocumentsRepository.save(employeeId, docUrl, entityType.getValue(), referenceId,
						tenantId);
			}
		}

		// delete all docs from db that are not in input list
		for (EmployeeDocument documentInDb : documentsFromDb) {
			if (EntityType.valueOf(documentInDb.getReferenceType()) == entityType
					&& documentInDb.getReferenceId().equals(referenceId)
					&& !inputDocuments.contains(documentInDb.getDocument())) {
				employeeDocumentsRepository.delete(employeeId, documentInDb.getDocument(), entityType.toString(),
						referenceId, tenantId);
			}
		}
	}

	private boolean documentPresentInDB(List<EmployeeDocument> documentsFromDb, String docUrl, EntityType entityType,
			Long referenceId) {
		boolean foundDocInDb = false;
		for (EmployeeDocument documentInDb : documentsFromDb) {
			if (EntityType.valueOf(documentInDb.getReferenceType()) == entityType
					&& documentInDb.getReferenceId().equals(referenceId) && documentInDb.getDocument().equals(docUrl)) {
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
			case APR_DETAILS:
				for (APRDetail aprDetail : employee.getAprDetails()) {
					if (aprDetail.getId().equals(document.getReferenceId()))
						aprDetail.getDocuments().add(document.getDocument());
				}
				break;
			default:
				break;
			}
		}
	}

}
