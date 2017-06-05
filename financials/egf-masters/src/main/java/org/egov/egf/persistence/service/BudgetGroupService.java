package org.egov.egf.persistence.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.domain.exception.InvalidDataException;
import org.egov.egf.json.ObjectMapperFactory;
import org.egov.egf.persistence.entity.BudgetGroup;
import org.egov.egf.persistence.entity.ChartOfAccount;
import org.egov.egf.persistence.queue.contract.BudgetGroupContract;
import org.egov.egf.persistence.queue.contract.BudgetGroupContractRequest;
import org.egov.egf.persistence.queue.contract.BudgetGroupContractResponse;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.repository.BudgetGroupJpaRepository;
import org.egov.egf.persistence.repository.BudgetGroupQueueRepository;
import org.egov.egf.persistence.specification.BudgetGroupSpecification;
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
public class BudgetGroupService {

	private final BudgetGroupJpaRepository budgetGroupJpaRepository;

	private final BudgetGroupQueueRepository budgetGroupQueueRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private SmartValidator validator;

	@Autowired
	private ChartOfAccountService chartOfAccountService;

	@Autowired
	public BudgetGroupService(final BudgetGroupJpaRepository budgetGroupJpaRepository,
			final BudgetGroupQueueRepository budgetGroupQueueRepository) {
		this.budgetGroupJpaRepository = budgetGroupJpaRepository;
		this.budgetGroupQueueRepository = budgetGroupQueueRepository;
	}

	@Transactional
	public BudgetGroup create(final BudgetGroup budgetGroup) {
		setBudgetGroup(budgetGroup);
		return budgetGroupJpaRepository.save(budgetGroup);
	}

	public void push(final BudgetGroupContractRequest budgetGroupContractRequest) {
		budgetGroupQueueRepository.push(budgetGroupContractRequest);
	}

	@Transactional
	public BudgetGroupContractResponse create(HashMap<String, Object> financialContractRequestMap) {
		final BudgetGroupContractRequest budgetGroupContractRequest = ObjectMapperFactory.create()
				.convertValue(financialContractRequestMap.get("BudgetGroupCreate"), BudgetGroupContractRequest.class);
		BudgetGroupContractResponse budgetGroupContractResponse = new BudgetGroupContractResponse();
		budgetGroupContractResponse.setBudgetGroups(new ArrayList<BudgetGroupContract>());
		ModelMapper modelMapper = new ModelMapper();
		if (budgetGroupContractRequest.getBudgetGroups() != null
				&& !budgetGroupContractRequest.getBudgetGroups().isEmpty()) {
			for (BudgetGroupContract budgetGroupContract : budgetGroupContractRequest.getBudgetGroups()) {
				BudgetGroup budgetGroupEntity = new BudgetGroup(budgetGroupContract);
				budgetGroupJpaRepository.save(budgetGroupEntity);
				BudgetGroupContract resp = modelMapper.map(budgetGroupEntity, BudgetGroupContract.class);
				budgetGroupContractResponse.getBudgetGroups().add(resp);
			}
		} else if (budgetGroupContractRequest.getBudgetGroup() != null) {
			BudgetGroup budgetGroupEntity = new BudgetGroup(budgetGroupContractRequest.getBudgetGroup());
			budgetGroupJpaRepository.save(budgetGroupEntity);
			BudgetGroupContract resp = modelMapper.map(budgetGroupEntity, BudgetGroupContract.class);
			budgetGroupContractResponse.setBudgetGroup(resp);
		}
		budgetGroupContractResponse.setResponseInfo(getResponseInfo(budgetGroupContractRequest.getRequestInfo()));
		return budgetGroupContractResponse;
	}
	
	@Transactional
	public BudgetGroupContractResponse updateAll(HashMap<String, Object> financialContractRequestMap) {
		final BudgetGroupContractRequest budgetGroupContractRequest = ObjectMapperFactory.create()
				.convertValue(financialContractRequestMap.get("BudgetGroupUpdateAll"), BudgetGroupContractRequest.class);
		BudgetGroupContractResponse budgetGroupContractResponse = new BudgetGroupContractResponse();
		budgetGroupContractResponse.setBudgetGroups(new ArrayList<BudgetGroupContract>());
		ModelMapper modelMapper = new ModelMapper();
		if (budgetGroupContractRequest.getBudgetGroups() != null
				&& !budgetGroupContractRequest.getBudgetGroups().isEmpty()) {
			for (BudgetGroupContract budgetGroupContract : budgetGroupContractRequest.getBudgetGroups()) {
				BudgetGroup budgetGroupEntity = new BudgetGroup(budgetGroupContract);
				budgetGroupEntity.setVersion(findOne(budgetGroupEntity.getId()).getVersion());
				budgetGroupJpaRepository.save(budgetGroupEntity);
				BudgetGroupContract resp = modelMapper.map(budgetGroupEntity, BudgetGroupContract.class);
				budgetGroupContractResponse.getBudgetGroups().add(resp);
			}
		}
		budgetGroupContractResponse.setResponseInfo(getResponseInfo(budgetGroupContractRequest.getRequestInfo()));
		return budgetGroupContractResponse;
	}
	
	
	
	@Transactional
	public BudgetGroupContractResponse update(HashMap<String, Object> financialContractRequestMap) {
		final BudgetGroupContractRequest budgetGroupContractRequest = ObjectMapperFactory.create()
				.convertValue(financialContractRequestMap.get("BudgetGroupUpdate"), BudgetGroupContractRequest.class);
		BudgetGroupContractResponse budgetGroupContractResponse = new BudgetGroupContractResponse();
		budgetGroupContractResponse.setBudgetGroups(new ArrayList<BudgetGroupContract>());
		ModelMapper modelMapper = new ModelMapper();
		BudgetGroup budgetGroupEntity = new BudgetGroup(budgetGroupContractRequest.getBudgetGroup());
		budgetGroupEntity.setVersion(budgetGroupJpaRepository.findOne(budgetGroupEntity.getId()).getVersion());
		budgetGroupJpaRepository.save(budgetGroupEntity);
		BudgetGroupContract resp = modelMapper.map(budgetGroupEntity, BudgetGroupContract.class);
		budgetGroupContractResponse.setBudgetGroup(resp);
		budgetGroupContractResponse.setResponseInfo(getResponseInfo(budgetGroupContractRequest.getRequestInfo()));
		return budgetGroupContractResponse;
	}

	private void setBudgetGroup(final BudgetGroup budgetGroup) {
		if (budgetGroup.getMajorCode() != null) {
			ChartOfAccount majorCode = chartOfAccountService.findOne(budgetGroup.getMajorCode());
			if (majorCode == null) {
				throw new InvalidDataException("majorCode", "majorCode.invalid", " Invalid majorCode");
			}
			budgetGroup.setMajorCode(majorCode.getId());
		}
		if (budgetGroup.getMaxCode() != null) {
			ChartOfAccount maxCode = chartOfAccountService.findOne(budgetGroup.getMaxCode());
			if (maxCode == null) {
				throw new InvalidDataException("maxCode", "maxCode.invalid", " Invalid maxCode");
			}
			budgetGroup.setMaxCode(maxCode.getId());
		}
		if (budgetGroup.getMinCode() != null) {
			ChartOfAccount minCode = chartOfAccountService.findOne(budgetGroup.getMinCode());
			if (minCode == null) {
				throw new InvalidDataException("minCode", "minCode.invalid", " Invalid minCode");
			}
			budgetGroup.setMinCode(minCode.getId());
		}
	}

	@Transactional
	public BudgetGroup update(final BudgetGroup budgetGroup) {
		setBudgetGroup(budgetGroup);
		return budgetGroupJpaRepository.save(budgetGroup);
	}

	public List<BudgetGroup> findAll() {
		return budgetGroupJpaRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	}

	public BudgetGroup findByName(String name) {
		return budgetGroupJpaRepository.findByName(name);
	}

	public BudgetGroup findOne(Long id) {
		return budgetGroupJpaRepository.findOne(id);
	}

	public Page<BudgetGroup> search(BudgetGroupContractRequest budgetGroupContractRequest) {
		final BudgetGroupSpecification specification = new BudgetGroupSpecification(
				budgetGroupContractRequest.getBudgetGroup());
		Pageable page = new PageRequest(budgetGroupContractRequest.getPage().getOffSet(),
				budgetGroupContractRequest.getPage().getPageSize());
		return budgetGroupJpaRepository.findAll(specification, page);
	}

	public BindingResult validate(BudgetGroupContractRequest budgetGroupContractRequest, String method,
			BindingResult errors) {

		try {
			switch (method) {
			case "update":
				Assert.notNull(budgetGroupContractRequest.getBudgetGroup(), "BudgetGroup to edit must not be null");
				validator.validate(budgetGroupContractRequest.getBudgetGroup(), errors);
				break;
			case "view":
				// validator.validate(budgetGroupContractRequest.getBudgetGroup(),
				// errors);
				break;
			case "create":
				Assert.notNull(budgetGroupContractRequest.getBudgetGroups(), "BudgetGroups to create must not be null");
				for (BudgetGroupContract b : budgetGroupContractRequest.getBudgetGroups()) {
					validator.validate(b, errors);
				}
				break;
			case "updateAll":
				Assert.notNull(budgetGroupContractRequest.getBudgetGroups(), "BudgetGroups to create must not be null");
				for (BudgetGroupContract b : budgetGroupContractRequest.getBudgetGroups()) {
					validator.validate(b, errors);
				}
				break;
			default:
				validator.validate(budgetGroupContractRequest.getRequestInfo(), errors);
			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

	public BudgetGroupContractRequest fetchRelatedContracts(BudgetGroupContractRequest budgetGroupContractRequest) {
		ModelMapper model = new ModelMapper();
		for (BudgetGroupContract budgetGroup : budgetGroupContractRequest.getBudgetGroups()) {
			if (budgetGroup.getMajorCode() != null) {
				ChartOfAccount majorCode = chartOfAccountService.findOne(budgetGroup.getMajorCode().getId());
				if (majorCode == null) {
					throw new InvalidDataException("majorCode", "majorCode.invalid", " Invalid majorCode");
				}
				model.map(majorCode, budgetGroup.getMajorCode());
			}
			if (budgetGroup.getMaxCode() != null) {
				ChartOfAccount maxCode = chartOfAccountService.findOne(budgetGroup.getMaxCode().getId());
				if (maxCode == null) {
					throw new InvalidDataException("maxCode", "maxCode.invalid", " Invalid maxCode");
				}
				model.map(maxCode, budgetGroup.getMaxCode());
			}
			if (budgetGroup.getMinCode() != null) {
				ChartOfAccount minCode = chartOfAccountService.findOne(budgetGroup.getMinCode().getId());
				if (minCode == null) {
					throw new InvalidDataException("minCode", "minCode.invalid", " Invalid minCode");
				}
				model.map(minCode, budgetGroup.getMinCode());
			}

		}
		BudgetGroupContract budgetGroup = budgetGroupContractRequest.getBudgetGroup();
		if (budgetGroup.getMajorCode() != null) {
			ChartOfAccount majorCode = chartOfAccountService.findOne(budgetGroup.getMajorCode().getId());
			if (majorCode == null) {
				throw new InvalidDataException("majorCode", "majorCode.invalid", " Invalid majorCode");
			}
			model.map(majorCode, budgetGroup.getMajorCode());
		}
		if (budgetGroup.getMaxCode() != null) {
			ChartOfAccount maxCode = chartOfAccountService.findOne(budgetGroup.getMaxCode().getId());
			if (maxCode == null) {
				throw new InvalidDataException("maxCode", "maxCode.invalid", " Invalid maxCode");
			}
			model.map(maxCode, budgetGroup.getMaxCode());
		}
		if (budgetGroup.getMinCode() != null) {
			ChartOfAccount minCode = chartOfAccountService.findOne(budgetGroup.getMinCode().getId());
			if (minCode == null) {
				throw new InvalidDataException("minCode", "minCode.invalid", " Invalid minCode");
			}
			model.map(minCode, budgetGroup.getMinCode());
		}

		return budgetGroupContractRequest;
	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		new ResponseInfo();
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}