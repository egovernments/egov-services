package org.egov.egf.master.domain.service;

import java.util.List;

import org.egov.common.constants.EgfConstants;
import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.exception.InvalidDataException;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.master.domain.model.Function;
import org.egov.egf.master.domain.model.FunctionSearch;
import org.egov.egf.master.domain.repository.FunctionRepository;
import org.egov.egf.master.web.requests.FunctionRequest;
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


	private BindingResult validate(List<Function> functions, String method, BindingResult errors) {

		try {
			switch (method) {
			case EgfConstants.ACTION_VIEW:
				// validator.validate(functionContractRequest.getFunction(),
				// errors);
				break;
			case EgfConstants.ACTION_CREATE:
				Assert.notNull(functions, "Functions to create must not be null");
				for (Function function : functions) {
					validator.validate(function, errors);
				}
				break;
			case EgfConstants.ACTION_UPDATE:
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

	@Transactional
	public List<Function> add(List<Function> functions, BindingResult errors) {
		functions = fetchRelated(functions);
		validate(functions, EgfConstants.ACTION_CREATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		return functions;

	}

	@Transactional
	public List<Function> update(List<Function> functions, BindingResult errors) {
		functions = fetchRelated(functions);
		validate(functions, EgfConstants.ACTION_UPDATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		return functions;

	}

	public void addToQue(FunctionRequest request) {
		functionRepository.add(request);
	}

	public Pagination<Function> search(FunctionSearch functionSearch) {
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