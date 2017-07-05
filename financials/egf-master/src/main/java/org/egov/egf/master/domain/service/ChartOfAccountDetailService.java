package org.egov.egf.master.domain.service;

import java.util.List;

import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.exception.InvalidDataException;
import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.CommonRequest;
import org.egov.egf.master.domain.model.AccountDetailType;
import org.egov.egf.master.domain.model.ChartOfAccount;
import org.egov.egf.master.domain.model.ChartOfAccountDetail;
import org.egov.egf.master.domain.model.ChartOfAccountDetailSearch;
import org.egov.egf.master.domain.repository.AccountDetailTypeRepository;
import org.egov.egf.master.domain.repository.ChartOfAccountDetailRepository;
import org.egov.egf.master.domain.repository.ChartOfAccountRepository;
import org.egov.egf.master.web.contract.ChartOfAccountDetailContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

@Service
@Transactional(readOnly = true)
public class ChartOfAccountDetailService {

	public static final String ACTION_CREATE = "create";
	public static final String ACTION_UPDATE = "update";
	public static final String ACTION_VIEW = "view";
	public static final String ACTION_EDIT = "edit";
	public static final String ACTION_SEARCH = "search";

	@Autowired
	private ChartOfAccountDetailRepository chartOfAccountDetailRepository;

	@Autowired
	private SmartValidator validator;
	@Autowired
	private ChartOfAccountRepository chartOfAccountRepository;
	@Autowired
	private AccountDetailTypeRepository accountDetailTypeRepository;

	public BindingResult validate(List<ChartOfAccountDetail> chartofaccountdetails, String method,
			BindingResult errors) {

		try {
			switch (method) {
			case ACTION_VIEW:
				// validator.validate(chartOfAccountDetailContractRequest.getChartOfAccountDetail(),
				// errors);
				break;
			case ACTION_CREATE:
				Assert.notNull(chartofaccountdetails, "ChartOfAccountDetails to create must not be null");
				for (ChartOfAccountDetail chartOfAccountDetail : chartofaccountdetails) {
					validator.validate(chartOfAccountDetail, errors);
				}
				break;
			case ACTION_UPDATE:
				Assert.notNull(chartofaccountdetails, "ChartOfAccountDetails to update must not be null");
				for (ChartOfAccountDetail chartOfAccountDetail : chartofaccountdetails) {
					validator.validate(chartOfAccountDetail, errors);
				}
				break;
			default:

			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

	public List<ChartOfAccountDetail> fetchRelated(List<ChartOfAccountDetail> chartofaccountdetails) {
		for (ChartOfAccountDetail chartOfAccountDetail : chartofaccountdetails) {
			// fetch related items
			if (chartOfAccountDetail.getChartOfAccount() != null) {
				ChartOfAccount chartOfAccount = chartOfAccountRepository
						.findById(chartOfAccountDetail.getChartOfAccount());
				if (chartOfAccount == null) {
					throw new InvalidDataException("chartOfAccount", "chartOfAccount.invalid",
							" Invalid chartOfAccount");
				}
				chartOfAccountDetail.setChartOfAccount(chartOfAccount);
			}
			if (chartOfAccountDetail.getAccountDetailType() != null) {
				AccountDetailType accountDetailType = accountDetailTypeRepository
						.findById(chartOfAccountDetail.getAccountDetailType());
				if (accountDetailType == null) {
					throw new InvalidDataException("accountDetailType", "accountDetailType.invalid",
							" Invalid accountDetailType");
				}
				chartOfAccountDetail.setAccountDetailType(accountDetailType);
			}

		}

		return chartofaccountdetails;
	}

	public List<ChartOfAccountDetail> add(List<ChartOfAccountDetail> chartofaccountdetails, BindingResult errors) {
		chartofaccountdetails = fetchRelated(chartofaccountdetails);
		validate(chartofaccountdetails, ACTION_CREATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		return chartofaccountdetails;

	}

	public List<ChartOfAccountDetail> update(List<ChartOfAccountDetail> chartofaccountdetails, BindingResult errors) {
		chartofaccountdetails = fetchRelated(chartofaccountdetails);
		validate(chartofaccountdetails, ACTION_UPDATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		return chartofaccountdetails;

	}

	public void addToQue(CommonRequest<ChartOfAccountDetailContract> request) {
		chartOfAccountDetailRepository.add(request);
	}

	public Pagination<ChartOfAccountDetail> search(ChartOfAccountDetailSearch chartOfAccountDetailSearch) {
		return chartOfAccountDetailRepository.search(chartOfAccountDetailSearch);
	}

}