package org.egov.egf.master.domain.service;

import java.util.List;

import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.exception.InvalidDataException;
import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.CommonRequest;
import org.egov.egf.master.domain.model.Function;
import org.egov.egf.master.domain.model.FunctionSearch;
import org.egov.egf.master.domain.repository.FunctionRepository;
import org.egov.egf.master.web.contract.FunctionContract;
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

	public static final String ACTION_CREATE = "create";
	public static final String ACTION_UPDATE = "update";
	public static final String ACTION_VIEW = "view";
	public static final String ACTION_EDIT = "edit";
	public static final String ACTION_SEARCH = "search";
 

	@Autowired
	private SmartValidator validator;
	@Autowired
	private FunctionRepository functionRepository;

	public BindingResult validate(List<Function> functions, String method, BindingResult errors) {

		try {
			switch (method) {
			case ACTION_VIEW:
				// validator.validate(functionContractRequest.getFunction(),
				// errors);
				break;
			case ACTION_CREATE:
				Assert.notNull(functions, "Functions to create must not be null");
				for (Function function : functions) {
					validator.validate(function, errors);
				}
				break;
			case ACTION_UPDATE:
				Assert.notNull(functions, "Functions to update must not be null");
				for (Function function : functions) {
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
			if (function.getParentId() != null) {
				Function parentId = functionRepository.findById(function.getParentId());
				if (parentId == null) {
					throw new InvalidDataException("parentId", "parentId.invalid", " Invalid parentId");
				}
				function.setParentId(parentId);
			}

		}

		return functions;
	}

	public List<Function> add(List<Function> functions, BindingResult errors) {
		functions = fetchRelated(functions);
		validate(functions, ACTION_CREATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		return functions;

	}

	public List<Function> update(List<Function> functions, BindingResult errors) {
		functions = fetchRelated(functions);
		validate(functions, ACTION_UPDATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		return functions;

	}

	public void addToQue(CommonRequest<FunctionContract> request) {
		functionRepository.add(request);
	}

	public Pagination<Function> search(FunctionSearch functionSearch) {
		return functionRepository.search(functionSearch);
	}

}