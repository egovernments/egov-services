/*package org.egov.inv.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.Constants;
import org.egov.common.Pagination;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.exception.CustomBindException;
import org.egov.common.exception.ErrorCode;
import org.egov.common.exception.InvalidDataException;
import org.egov.inv.model.AuditDetails;
import org.egov.inv.model.Department;
import org.egov.inv.model.Employee;
import org.egov.inv.model.Location;
import org.egov.inv.model.Store;
import org.egov.inv.persistence.repository.StoreJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

@Service
@Transactional(readOnly = true)
public class StoreService {

	@Autowired
	private StoreJdbcRepository storeRepository;

	 
	@Autowired
	private LocationRepository locationRepository;
	@Autowired
	private DepartmentRepository departmentRepository;
	@Autowired
	private AuditDetailsRepository auditDetailsRepository;
	@Autowired
	private EmployeeRepository employeeRepository;

	@Transactional
	public List<Store> create(List<Store> stores, BindingResult errors, RequestInfo requestInfo) {

		try {

			stores = fetchRelated(stores);
			validate(stores, Constants.ACTION_CREATE, errors);

			if (errors.hasErrors()) {
				throw new CustomBindException(errors);
			}
			for (Store b : stores) {
				b.setId(storeRepository.getNextSequence());
				b.add();
			}

		} catch (CustomBindException e) {
			throw e;
		}

		return storeRepository.save(stores, requestInfo);

	}

	@Transactional
	public List<Store> update(List<Store> stores, BindingResult errors, RequestInfo requestInfo) {

		try {

			stores = fetchRelated(stores);

			validate(stores, Constants.ACTION_UPDATE, errors);

			if (errors.hasErrors()) {
				throw new CustomBindException(errors);
			}
			for (Store b : stores) {
				b.update();
			}

		} catch (CustomBindException e) {

			throw new CustomBindException(errors);
		}

		return storeRepository.update(stores, requestInfo);

	}

	private BindingResult validate(List<Store> stores, String method, BindingResult errors) {

		try {
			switch (method) {
			case ACTION_VIEW:
				// validator.validate(storeContractRequest.getStore(), errors);
				break;
			case Constants.ACTION_CREATE:
				if (stores == null) {
					throw new InvalidDataException("stores", ErrorCode.NOT_NULL.getCode(), null);
				}
				for (Store store : stores) {
					validator.validate(store, errors);
					if (!storeRepository.uniqueCheck("name", store)) {
						errors.addError(new FieldError("store", "name", store.getName(), false,
								new String[] { ErrorCode.NON_UNIQUE_VALUE.getCode() }, null, null));
					}
					if (!storeRepository.uniqueCheck("code", store)) {
						errors.addError(new FieldError("store", "code", store.getName(), false,
								new String[] { ErrorCode.NON_UNIQUE_VALUE.getCode() }, null, null));
					}
					if (!storeRepository.uniqueCheck("identifier", store)) {
						errors.addError(new FieldError("store", "identifier", store.getName(), false,
								new String[] { ErrorCode.NON_UNIQUE_VALUE.getCode() }, null, null));
					}

				}
				break;
			case Constants.ACTION_UPDATE:
				if (stores == null) {
					throw new InvalidDataException("stores", ErrorCode.NOT_NULL.getCode(), null);
				}
				for (Store store : stores) {
					if (store.getId() == null) {
						throw new InvalidDataException("id", ErrorCode.MANDATORY_VALUE_MISSING.getCode(),
								store.getId());
					}
					validator.validate(store, errors);
					if (!storeRepository.uniqueCheck("name", store)) {
						errors.addError(new FieldError("store", "name", store.getName(), false,
								new String[] { ErrorCode.NON_UNIQUE_VALUE.getCode() }, null, null));
					}
					if (!storeRepository.uniqueCheck("code", store)) {
						errors.addError(new FieldError("store", "code", store.getName(), false,
								new String[] { ErrorCode.NON_UNIQUE_VALUE.getCode() }, null, null));
					}
					if (!storeRepository.uniqueCheck("identifier", store)) {
						errors.addError(new FieldError("store", "identifier", store.getName(), false,
								new String[] { ErrorCode.NON_UNIQUE_VALUE.getCode() }, null, null));
					}

				}
				break;
			case Constants.ACTION_SEARCH:
				if (stores == null) {
					throw new InvalidDataException("stores", ErrorCode.NOT_NULL.getCode(), null);
				}
				for (Store store : stores) {
					if (store.getTenantId() == null) {
						throw new InvalidDataException("tenantId", ErrorCode.MANDATORY_VALUE_MISSING.getCode(),
								store.getTenantId());
					}
				}
				break;
			default:

			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;
	}

	public List<Store> fetchRelated(List<Store> stores) {
		for (Store store : stores) {
			// fetch related items
			if (store.getDepartment() != null) {
				Department department = departmentRepository.findById(store.getDepartment());
				if (department == null) {
					throw new InvalidDataException("department", "department.invalid", " Invalid department");
				}
				store.setDepartment(department);
			}
			if (store.getOfficeLocation() != null) {
				Location officeLocation = locationRepository.findById(store.getOfficeLocation());
				if (officeLocation == null) {
					throw new InvalidDataException("officeLocation", "officeLocation.invalid",
							" Invalid officeLocation");
				}
				store.setOfficeLocation(officeLocation);
			}
			if (store.getStoreInCharge() != null) {
				Employee storeInCharge = employeeRepository.findById(store.getStoreInCharge());
				if (storeInCharge == null) {
					throw new InvalidDataException("storeInCharge", "storeInCharge.invalid", " Invalid storeInCharge");
				}
				store.setStoreInCharge(storeInCharge);
			}
			if (store.getAuditDetails() != null) {
				AuditDetails auditDetails = auditDetailsRepository.findById(store.getAuditDetails());
				if (auditDetails == null) {
					throw new InvalidDataException("auditDetails", "auditDetails.invalid", " Invalid auditDetails");
				}
				store.setAuditDetails(auditDetails);
			}

			store.setTenantId(ApplicationThreadLocals.getTenantId().get());
			if (store.getTenantId() != null)
				if (store.getParent() != null && store.getParent().getId() != null) {
					store.getParent().setTenantId(store.getTenantId());
					Store parentId = storeRepository.findById(store.getParent());
					if (parentId == null) {
						throw new InvalidDataException("parentId", ErrorCode.INVALID_REF_VALUE.getCode(),
								store.getParent().getId());
					}
					store.setParent(parentId);
				}

		}

		return stores;
	}

	public Pagination<Store> search(StoreSearch storeSearch, BindingResult errors) {

		try {

			List<Store> stores = new ArrayList<>();
			stores.add(storeSearch);
			validate(stores, Constants.ACTION_SEARCH, errors);

			if (errors.hasErrors()) {
				throw new CustomBindException(errors);
			}

		} catch (CustomBindException e) {

			throw new CustomBindException(errors);
		}

		return storeRepository.search(storeSearch);
	}

	@Transactional
	public Store save(Store store) {
		return storeRepository.save(store);
	}

	@Transactional
	public Store update(Store store) {
		return storeRepository.update(store);
	}

}*/