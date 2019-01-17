package org.egov.hrms.service;

import org.egov.hrms.model.APRDetail;
import org.egov.hrms.model.Employee;
import org.egov.hrms.model.enums.EntityType;
import org.egov.hrms.repository.APRDetailRepository;
import org.egov.hrms.repository.EmployeeDocumentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;

@Component
public class APRDetailService {

	@Autowired
	private APRDetailRepository aprDetailRepository;

	@Autowired
	private EmployeeDocumentsRepository employeeDocumentsRepository;

	public void update(Employee employee) {
		if (isEmpty(employee.getAprDetails()))
			return;
		List<APRDetail> aprDetails = aprDetailRepository.findByEmployeeId(employee.getId(), employee.getTenantId());
		employee.getAprDetails().forEach((aprDetail) -> {
			if (needsInsert(aprDetail, aprDetails)) {
				aprDetailRepository.insert(aprDetail, employee.getId());
			} else if (needsUpdate(aprDetail, aprDetails)) {
				aprDetail.setTenantId(employee.getTenantId());
				aprDetailRepository.update(aprDetail);
			}
		});
		deleteAPRDetailsInDBThatAreNotInInput(employee.getAprDetails(), aprDetails, employee.getId(),
				employee.getTenantId());
	}

	private void deleteAPRDetailsInDBThatAreNotInInput(List<APRDetail> inputAPRDetails,
													   List<APRDetail> aprDetailsFromDb, Long employeeId, String tenantId) {

		List<Long> aprDetailsIdsToDelete = getListOfAPRDetailsIdsToDelete(inputAPRDetails, aprDetailsFromDb);
		if (!aprDetailsIdsToDelete.isEmpty()) {
			employeeDocumentsRepository.deleteForReferenceIds(employeeId, EntityType.PROBATION, aprDetailsIdsToDelete, tenantId);
			aprDetailRepository.delete(aprDetailsIdsToDelete, employeeId, tenantId);
		}
	}

	private List<Long> getListOfAPRDetailsIdsToDelete(List<APRDetail> inputAPRDetails,
			List<APRDetail> aprDetailsFromDb) {
		List<Long> aprDetailsIdsToDelete = new ArrayList<>();
		for (APRDetail aprDetailInDb : aprDetailsFromDb) {
			boolean found = false;
			for (APRDetail inputAPRDetail : inputAPRDetails)
				if (inputAPRDetail.getId().equals(aprDetailInDb.getId())) {
					found = true;
					break;
				}
			if (!found)
				aprDetailsIdsToDelete.add(aprDetailInDb.getId());
		}
		return aprDetailsIdsToDelete;
	}

	private boolean needsUpdate(APRDetail aprDetail, List<APRDetail> aprDetails) {
		for (APRDetail oldAPRDetails : aprDetails)
			if (aprDetail.equals(oldAPRDetails))
				return false;
		return true;
	}

	private boolean needsInsert(APRDetail aprDetail, List<APRDetail> aprDetails) {
		for (APRDetail oldAPRDetails : aprDetails) {
			if (aprDetail.getId().equals(oldAPRDetails.getId()))
				return false;
		}
		return true;
	}

}
