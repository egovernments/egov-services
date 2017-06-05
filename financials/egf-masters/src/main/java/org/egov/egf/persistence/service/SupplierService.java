package org.egov.egf.persistence.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.domain.exception.InvalidDataException;
import org.egov.egf.json.ObjectMapperFactory;
import org.egov.egf.persistence.entity.Bank;
import org.egov.egf.persistence.entity.Supplier;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.queue.contract.SupplierContract;
import org.egov.egf.persistence.queue.contract.SupplierContractRequest;
import org.egov.egf.persistence.queue.contract.SupplierContractResponse;
import org.egov.egf.persistence.repository.SupplierJpaRepository;
import org.egov.egf.persistence.repository.SupplierQueueRepository;
import org.egov.egf.persistence.specification.SupplierSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

@Service
@Transactional(readOnly = true)
public class SupplierService {

	private final SupplierJpaRepository supplierJpaRepository;

	private final SupplierQueueRepository supplierQueueRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private SmartValidator validator;

	@Autowired
	private BankService bankService;

	@Autowired
	public SupplierService(final SupplierJpaRepository supplierJpaRepository,
			final SupplierQueueRepository supplierQueueRepository) {
		this.supplierJpaRepository = supplierJpaRepository;
		this.supplierQueueRepository = supplierQueueRepository;
	}

	public void push(final SupplierContractRequest financialYearContractRequest) {
		supplierQueueRepository.push(financialYearContractRequest);
	}

	@Transactional
	public SupplierContractResponse create(HashMap<String, Object> financialContractRequestMap) {
		final SupplierContractRequest supplierContractRequest = ObjectMapperFactory.create()
				.convertValue(financialContractRequestMap.get("SupplierCreate"), SupplierContractRequest.class);
		SupplierContractResponse supplierContractResponse = new SupplierContractResponse();
		supplierContractResponse.setSuppliers(new ArrayList<SupplierContract>());
		ModelMapper modelMapper = new ModelMapper();
		if (supplierContractRequest.getSuppliers() != null && !supplierContractRequest.getSuppliers().isEmpty()) {
			for (SupplierContract supplierContract : supplierContractRequest.getSuppliers()) {
				Supplier supplierEntity = new Supplier(supplierContract);
				supplierJpaRepository.save(supplierEntity);
				SupplierContract resp = modelMapper.map(supplierEntity, SupplierContract.class);
				supplierContractResponse.getSuppliers().add(resp);
			}
		} else if (supplierContractRequest.getSupplier() != null) {
			Supplier supplierEntity = new Supplier(supplierContractRequest.getSupplier());
			supplierJpaRepository.save(supplierEntity);
			SupplierContract resp = modelMapper.map(supplierEntity, SupplierContract.class);
			supplierContractResponse.setSupplier(resp);
		}
		supplierContractResponse.setResponseInfo(getResponseInfo(supplierContractRequest.getRequestInfo()));
		return supplierContractResponse;
	}

	@Transactional
	public SupplierContractResponse updateAll(HashMap<String, Object> financialContractRequestMap) {
		final SupplierContractRequest supplierContractRequest = ObjectMapperFactory.create()
				.convertValue(financialContractRequestMap.get("SupplierUpdateAll"), SupplierContractRequest.class);
		SupplierContractResponse supplierContractResponse = new SupplierContractResponse();
		supplierContractResponse.setSuppliers(new ArrayList<SupplierContract>());
		ModelMapper modelMapper = new ModelMapper();
		if (supplierContractRequest.getSuppliers() != null && !supplierContractRequest.getSuppliers().isEmpty()) {
			for (SupplierContract supplierContract : supplierContractRequest.getSuppliers()) {
				Supplier supplierEntity = new Supplier(supplierContract);
				supplierEntity.setVersion(findOne(supplierEntity.getId()).getVersion());
				supplierJpaRepository.save(supplierEntity);
				SupplierContract resp = modelMapper.map(supplierEntity, SupplierContract.class);
				supplierContractResponse.getSuppliers().add(resp);
			}
		} else if (supplierContractRequest.getSupplier() != null) {
			Supplier supplierEntity = new Supplier(supplierContractRequest.getSupplier());
			supplierJpaRepository.save(supplierEntity);
			SupplierContract resp = modelMapper.map(supplierEntity, SupplierContract.class);
			supplierContractResponse.setSupplier(resp);
		}
		supplierContractResponse.setResponseInfo(getResponseInfo(supplierContractRequest.getRequestInfo()));
		return supplierContractResponse;
	}

	@Transactional
	public SupplierContractResponse update(HashMap<String, Object> financialContractRequestMap) {
		final SupplierContractRequest supplierContractRequest = ObjectMapperFactory.create()
				.convertValue(financialContractRequestMap.get("SupplierUpdate"), SupplierContractRequest.class);
		SupplierContractResponse supplierContractResponse = new SupplierContractResponse();
		ModelMapper modelMapper = new ModelMapper();
		Supplier supplierEntity = new Supplier(supplierContractRequest.getSupplier());
		supplierEntity.setVersion(supplierJpaRepository.findOne(supplierEntity.getId()).getVersion());
		supplierJpaRepository.save(supplierEntity);
		SupplierContract resp = modelMapper.map(supplierEntity, SupplierContract.class);
		supplierContractResponse.setSupplier(resp);
		supplierContractResponse.setResponseInfo(getResponseInfo(supplierContractRequest.getRequestInfo()));
		return supplierContractResponse;
	}

	@Transactional
	public Supplier create(final Supplier supplier) {
		setSupplier(supplier);
		return supplierJpaRepository.save(supplier);
	}

	private void setSupplier(final Supplier supplier) {
		if (supplier.getBank() != null) {
			Bank bank = bankService.findOne(supplier.getBank());
			if (bank == null) {
				throw new InvalidDataException("bank", "bank.invalid", " Invalid bank");
			}
			supplier.setBank(bank.getId());
		}
	}

	@Transactional
	public Supplier update(final Supplier supplier) {
		setSupplier(supplier);
		return supplierJpaRepository.save(supplier);
	}

	public List<Supplier> findAll() {
		return supplierJpaRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	}

	public Supplier findByName(String name) {
		return supplierJpaRepository.findByName(name);
	}

	public Supplier findByCode(String code) {
		return supplierJpaRepository.findByCode(code);
	}

	public Supplier findOne(Long id) {
		return supplierJpaRepository.findOne(id);
	}

	public Page<Supplier> search(SupplierContractRequest supplierContractRequest) {
		final SupplierSpecification specification = new SupplierSpecification(supplierContractRequest.getSupplier());
		Pageable page = new PageRequest(supplierContractRequest.getPage().getOffSet(),
				supplierContractRequest.getPage().getPageSize());
		return supplierJpaRepository.findAll(specification, page);
	}

	public BindingResult validate(SupplierContractRequest supplierContractRequest, String method,
			BindingResult errors) {

		try {
			switch (method) {
			case "update":
				Assert.notNull(supplierContractRequest.getSupplier(), "Supplier to edit must not be null");
				validator.validate(supplierContractRequest.getSupplier(), errors);
				break;
			case "view":
				// validator.validate(supplierContractRequest.getSupplier(),
				// errors);
				break;
			case "create":
				Assert.notNull(supplierContractRequest.getSuppliers(), "Suppliers to create must not be null");
				for (SupplierContract b : supplierContractRequest.getSuppliers()) {
					validator.validate(b, errors);
				}
				break;
			case "updateAll":
				Assert.notNull(supplierContractRequest.getSuppliers(), "Suppliers to create must not be null");
				for (SupplierContract b : supplierContractRequest.getSuppliers()) {
					validator.validate(b, errors);
				}
				break;
			default:
				validator.validate(supplierContractRequest.getRequestInfo(), errors);
			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

	public SupplierContractRequest fetchRelatedContracts(SupplierContractRequest supplierContractRequest) {
		ModelMapper model = new ModelMapper();
		for (SupplierContract supplier : supplierContractRequest.getSuppliers()) {
			if (supplier.getBank() != null) {
				Bank bank = bankService.findOne(supplier.getBank().getId());
				if (bank == null) {
					throw new InvalidDataException("bank", "bank.invalid", " Invalid bank");
				}
				model.map(bank, supplier.getBank());
			}
		}
		SupplierContract supplier = supplierContractRequest.getSupplier();
		if (supplier.getBank() != null) {
			Bank bank = bankService.findOne(supplier.getBank().getId());
			if (bank == null) {
				throw new InvalidDataException("bank", "bank.invalid", " Invalid bank");
			}
			model.map(bank, supplier.getBank());
		}
		return supplierContractRequest;
	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		new ResponseInfo();
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}
}