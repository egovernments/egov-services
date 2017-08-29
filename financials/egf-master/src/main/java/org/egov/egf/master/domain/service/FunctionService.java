package org.egov.egf.master.domain.service;

import java.util.List;

import org.egov.common.constants.Constants;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.exception.InvalidDataException;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.master.domain.model.Function;
import org.egov.egf.master.domain.model.FunctionSearch;
import org.egov.egf.master.domain.repository.FunctionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

@Service
@Transactional(readOnly = true)
public class FunctionService {

	@Autowired
	private FunctionRepository functionRepository;

	@Autowired
	private SmartValidator validator;

	@Transactional
	public List<Function> create(List<Function> functions, BindingResult errors, RequestInfo requestInfo) {

		try {

			functions = fetchRelated(functions);

			validate(functions, Constants.ACTION_CREATE, errors);

			if (errors.hasErrors()) {
				throw new CustomBindException(errors);
			}
			for (Function b : functions) {
				b.setId(functionRepository.getNextSequence());
				b.add();
			}

		} catch (CustomBindException e) {

			throw new CustomBindException(errors);
		}

		return functionRepository.save(functions, requestInfo);

	}

	@Transactional
	public List<Function> update(List<Function> functions, BindingResult errors, RequestInfo requestInfo) {

		try {

			functions = fetchRelated(functions);

			validate(functions, Constants.ACTION_UPDATE, errors);

			if (errors.hasErrors()) {
				throw new CustomBindException(errors);
			}
			for (Function b : functions) {
				b.update();
			}

		} catch (CustomBindException e) {

			throw new CustomBindException(errors);
		}

		return functionRepository.update(functions, requestInfo);

	}

	private BindingResult validate(List<Function> functions, String method, BindingResult errors) {

		try {
			switch (method) {
			case Constants.ACTION_VIEW:
				// validator.validate(functionContractRequest.getFunction(),
				// errors);
				break;
			case Constants.ACTION_CREATE:
				Assert.notNull(functions, "Functions to create must not be null");
				for (Function function : functions) {
					validator.validate(function, errors);
				}
				break;
			case Constants.ACTION_UPDATE:
				Assert.notNull(functions, "Functions to update must not be null");
				for (Function function : functions) {
					Assert.notNull(function.getId(), "Function ID to update must not be null");
					validator.validate(function, errors);
				}
				break;
			default:

			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

	public List<Function> fetchRelated(List<Function> functions) {
		for (Function function : functions) {
			// fetch related items
			if (function.getTenantId() != null)
				if (function.getParentId() != null && function.getParentId().getId() != null) {
					function.getParentId().setTenantId(function.getTenantId());
					Function parentId = functionRepository.findById(function.getParentId());
					if (parentId == null) {
						throw new InvalidDataException("parentId", "parentId.invalid", " Invalid parentId");
					}
					function.setParentId(parentId);
				}

		}

		return functions;
	}

	public Pagination<Function> search(FunctionSearch functionSearch) {
	        Assert.notNull(functionSearch.getTenantId(), "tenantId is mandatory for function search");
		return functionRepository.search(functionSearch);
	}

	@Transactional
	public Function save(Function function) {
		return functionRepository.save(function);
	}

	@Transactional
	public Function update(Function function) {
		return functionRepository.update(function);
	}

}