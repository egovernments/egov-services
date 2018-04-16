package org.egov.eis.service;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.ArrayList;
import java.util.List;

import org.egov.eis.model.Employee;
import org.egov.eis.model.ServiceHistory;
import org.egov.eis.model.enums.EntityType;
import org.egov.eis.repository.EmployeeDocumentsRepository;
import org.egov.eis.repository.ServiceHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServiceHistoryService {

	@Autowired
	private ServiceHistoryRepository serviceHistoryRepository;

	@Autowired
	private EmployeeDocumentsRepository employeeDocumentsRepository;

	public void update(Employee employee) {
		List<ServiceHistory> services = serviceHistoryRepository.findByEmployeeId(employee.getId(),
				employee.getTenantId());
		if (!isEmpty(employee.getServiceHistory()))
			employee.getServiceHistory().forEach((service) -> {
				if (needsInsert(service, services)) {
					serviceHistoryRepository.insert(service, employee.getId());
				} else if (needsUpdate(service, services)) {
					service.setTenantId(employee.getTenantId());
					serviceHistoryRepository.update(service);
				}
			});
		deleteInDBThatAreNotInInput(employee.getServiceHistory(), services, employee.getId(), employee.getTenantId());

	}

	private void deleteInDBThatAreNotInInput(List<ServiceHistory> inputServices, List<ServiceHistory> servicesFromDb,
			Long employeeId, String tenantId) {
		List<Long> servicesIdsToDelete = getListIdsToDelete(inputServices, servicesFromDb);
		if (!servicesIdsToDelete.isEmpty()) {
			employeeDocumentsRepository.deleteForReferenceIds(employeeId, EntityType.SERVICE, servicesIdsToDelete, tenantId);
			serviceHistoryRepository.delete(servicesIdsToDelete, employeeId, tenantId);
		}
	}

	private List<Long> getListIdsToDelete(List<ServiceHistory> inputServices,
			List<ServiceHistory> serviceHistoriesFromDb) {
		List<Long> idsToDelete = new ArrayList<>();
		for (ServiceHistory shInDb : serviceHistoriesFromDb) {
			boolean found = false;
			if (!isEmpty(inputServices)) {
				// if empty, found remains false and the record becomes eligible for deletion.
				for (ServiceHistory inputService : inputServices)
					if (inputService.getId().equals(shInDb.getId())) {
						found = true;
						break;
					}
			}
			if (!found)
				idsToDelete.add(shInDb.getId());
		}
		return idsToDelete;
	}

	/**
	 * Note: needsUpdate checks if any field has changed by comparing with
	 * contents from db. If yes, then only do an update.
	 */
	private boolean needsUpdate(ServiceHistory sh, List<ServiceHistory> services) {
		for (ServiceHistory oldSh : services)
			if (sh.equals(oldSh))
				return false;
		return true;
	}

	private boolean needsInsert(ServiceHistory service, List<ServiceHistory> services) {
		for (ServiceHistory oldService : services) {
			if (service.getId().equals(oldService.getId()))
				return false;
		}
		return true;
	}

}
