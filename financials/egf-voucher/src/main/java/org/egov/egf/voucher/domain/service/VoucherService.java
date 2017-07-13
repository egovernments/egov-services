package org.egov.egf.voucher.domain.service;

import java.util.List;

import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.CommonRequest;
import org.egov.egf.voucher.domain.model.Voucher;
import org.egov.egf.voucher.domain.model.VoucherSearch;
import org.egov.egf.voucher.domain.repository.VoucherRepository;
import org.egov.egf.voucher.web.contract.VoucherContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

@Service
@Transactional(readOnly = true)
public class VoucherService {

	public static final String ACTION_CREATE = "create";
	public static final String ACTION_UPDATE = "update";
	public static final String ACTION_VIEW = "view";
	public static final String ACTION_EDIT = "edit";
	public static final String ACTION_SEARCH = "search";

	@Autowired
	private VoucherRepository voucherRepository;

	@Autowired
	private SmartValidator validator;
	
//	@Autowired
//	private FundContractRepository fundContractRepository;
//	@Autowired
//	private EgfStatusRepository egfStatusRepository;

	public BindingResult validate(List<Voucher> vouchers, String method, BindingResult errors) {

		try {
			switch (method) {
			case ACTION_VIEW:
				// validator.validate(voucherContractRequest.getVoucher(),
				// errors);
				break;
			case ACTION_CREATE:
				Assert.notNull(vouchers, "Vouchers to create must not be null");
				for (Voucher voucher : vouchers) {
					validator.validate(voucher, errors);
				}
				break;
			case ACTION_UPDATE:
				Assert.notNull(vouchers, "Vouchers to update must not be null");
				for (Voucher voucher : vouchers) {
					validator.validate(voucher, errors);
				}
				break;
			default:

			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

	public List<Voucher> fetchRelated(List<Voucher> vouchers) {
		for (Voucher voucher : vouchers) {
			// fetch related items
			/*if (voucher.getFund() != null) {
				FundContract fund = fundContractRepository.findById(voucher.getFund());
				if (fund == null) {
					throw new InvalidDataException("fund", "fund.invalid", " Invalid fund");
				}
				voucher.setFund(fund);
			}
			if (voucher.getStatus() != null) {
				EgfStatus status = egfStatusRepository.findById(voucher.getStatus());
				if (status == null) {
					throw new InvalidDataException("status", "status.invalid", " Invalid status");
				}
				voucher.setStatus(status);
			}
			if (voucher.getVouchermis() != null) {
				Vouchermis vouchermis = vouchermisRepository.findById(voucher.getVouchermis());
				if (vouchermis == null) {
					throw new InvalidDataException("vouchermis", "vouchermis.invalid", " Invalid vouchermis");
				}
				voucher.setVouchermis(vouchermis);
			}*/

		}

		return vouchers;
	}
	@Transactional
	public List<Voucher> add(List<Voucher> vouchers, BindingResult errors) {
		vouchers = fetchRelated(vouchers);
		validate(vouchers, ACTION_CREATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		for(Voucher voucher:vouchers)
			voucherRepository.save(voucher);
		
		return vouchers;

	}

	public List<Voucher> update(List<Voucher> vouchers, BindingResult errors) {
		vouchers = fetchRelated(vouchers);
		validate(vouchers, ACTION_UPDATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		return vouchers;

	}

	public void addToQue(CommonRequest<VoucherContract> request) {
		voucherRepository.add(request);
	}

	public Pagination<Voucher> search(VoucherSearch voucherSearch) {
		return voucherRepository.search(voucherSearch);
	}

}