package org.egov.eis.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.eis.model.Employee;
import org.egov.eis.model.ServiceHistory;
import org.egov.eis.repository.ServiceHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServiceHistoryService {
	
	@Autowired
	private ServiceHistoryRepository repository;

	public void update(Employee employee) {
		List<ServiceHistory> services = repository.findByEmployeeId(employee.getId(), employee.getTenantId());
		employee.getServiceHistory().forEach((service) -> {
			if (needsInsert(service, services)) {
				repository.insert(service, employee.getId());
			} else if (needsUpdate(service, services)) {
				service.setTenantId(employee.getTenantId());
				repository.update(service);
			}
     });
		deleteInDBThatAreNotInInput(employee.getServiceHistory(), services,  employee.getId(),employee.getTenantId());
	}

	private void deleteInDBThatAreNotInInput(List<ServiceHistory> inputServices,
			List<ServiceHistory> servicesFromDb, Long employeeId, String tenantId) {
		List<Long> servicesIdsToDelete = getListIdsToDelete(inputServices, servicesFromDb);
		if (!servicesIdsToDelete.isEmpty())
			repository.delete(servicesIdsToDelete, employeeId, tenantId);
    }

	private List<Long> getListIdsToDelete(List<ServiceHistory> inputServices, List<ServiceHistory> serviceHistoriesFromDb) {
		List<Long> idsToDelete = new ArrayList<>();
		for (ServiceHistory shInDb : serviceHistoriesFromDb) {
			boolean found = false;
			for (ServiceHistory inputService : inputServices)
				if (inputService.getId().equals(shInDb.getId())) {
					found = true;
					break;
				}
			if (!found) idsToDelete.add(shInDb.getId());
		}
		return idsToDelete;
	}

	private boolean needsUpdate(ServiceHistory sh, List<ServiceHistory> services) {
		for (ServiceHistory oldSh : services) 
			if (sh.equals(oldSh)) return false;
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
