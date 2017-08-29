package org.egov.egf.master.domain.service;

import java.util.List;

import org.egov.common.constants.Constants;
import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.exception.InvalidDataException;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.master.domain.model.Bank;
import org.egov.egf.master.domain.model.FinancialYear;
import org.egov.egf.master.domain.model.FiscalPeriod;
import org.egov.egf.master.domain.model.FiscalPeriodSearch;
import org.egov.egf.master.domain.repository.FinancialYearRepository;
import org.egov.egf.master.domain.repository.FiscalPeriodRepository;
import org.egov.egf.master.web.requests.FiscalPeriodRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

@Service
@Transactional(readOnly = true)
public class FiscalPeriodService {

	@Autowired
	private FiscalPeriodRepository fiscalPeriodRepository;

	@Autowired
	private SmartValidator validator;
	@Autowired
	private FinancialYearRepository financialYearRepository;

	private BindingResult validate(List<FiscalPeriod> fiscalperiods, String method, BindingResult errors) {

		try {
			switch (method) {
			case Constants.ACTION_VIEW:
				// validator.validate(fiscalPeriodContractRequest.getFiscalPeriod(),
				// errors);
				break;
			case Constants.ACTION_CREATE:
				Assert.notNull(fiscalperiods, "FiscalPeriods to create must not be null");
				for (FiscalPeriod fiscalPeriod : fiscalperiods) {
					validator.validate(fiscalPeriod, errors);
				}
				break;
			case Constants.ACTION_UPDATE:
				Assert.notNull(fiscalperiods, "FiscalPeriods to update must not be null");
				for (FiscalPeriod fiscalPeriod : fiscalperiods) {
				        Assert.notNull(fiscalPeriod.getId(), "FiscalPeriod ID to update must not be null");
					validator.validate(fiscalPeriod, errors);
				}
				break;
			default:

			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

	public List<FiscalPeriod> fetchRelated(List<FiscalPeriod> fiscalperiods) {
		for (FiscalPeriod fiscalPeriod : fiscalperiods) {
			// fetch related items
			if (fiscalPeriod.getFinancialYear() != null) {
				FinancialYear financialYear = financialYearRepository.findById(fiscalPeriod.getFinancialYear());
				if (financialYear == null) {
					throw new InvalidDataException("financialYear", "financialYear.invalid", " Invalid financialYear");
				}
				fiscalPeriod.setFinancialYear(financialYear);
			}

		}

		return fiscalperiods;
	}

	@Transactional
	public List<FiscalPeriod> add(List<FiscalPeriod> fiscalperiods, BindingResult errors) {
		fiscalperiods = fetchRelated(fiscalperiods);
		validate(fiscalperiods, Constants.ACTION_CREATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		for(FiscalPeriod b:fiscalperiods)b.setId(fiscalPeriodRepository.getNextSequence());
		return fiscalperiods;

	}

	@Transactional
	public List<FiscalPeriod> update(List<FiscalPeriod> fiscalperiods, BindingResult errors) {
		fiscalperiods = fetchRelated(fiscalperiods);
		validate(fiscalperiods, Constants.ACTION_UPDATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		return fiscalperiods;

	}

	public void addToQue(FiscalPeriodRequest request) {
		fiscalPeriodRepository.add(request);
	}

	public Pagination<FiscalPeriod> search(FiscalPeriodSearch fiscalPeriodSearch) {
	        Assert.notNull(fiscalPeriodSearch.getTenantId(), "tenantId is mandatory for fiscalPeriod search");
		return fiscalPeriodRepository.search(fiscalPeriodSearch);
	}

	@Transactional
	public FiscalPeriod save(FiscalPeriod fiscalPeriod) {
		return fiscalPeriodRepository.save(fiscalPeriod);
	}

	@Transactional
	public FiscalPeriod update(FiscalPeriod fiscalPeriod) {
		return fiscalPeriodRepository.update(fiscalPeriod);
	}

}