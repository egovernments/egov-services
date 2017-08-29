package org.egov.egf.master.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.constants.Constants;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.exception.InvalidDataException;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.master.domain.model.Fund;
import org.egov.egf.master.domain.model.FundSearch;
import org.egov.egf.master.domain.repository.FundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

@Service
@Transactional(readOnly = true)
public class FundService {

	@Autowired
	private FundRepository fundRepository;

	@Autowired
	private SmartValidator validator;

	@Transactional
	public List<Fund> create(List<Fund> funds, BindingResult errors, RequestInfo requestInfo) {

		try {

			funds = fetchRelated(funds);

			validate(funds, Constants.ACTION_CREATE, errors);

			if (errors.hasErrors()) {
				throw new CustomBindException(errors);
			}
			for (Fund b : funds) {
				b.setId(fundRepository.getNextSequence());
				b.add();
			}

		} catch (CustomBindException e) {

			throw new CustomBindException(errors);
		}

		return fundRepository.save(funds, requestInfo);

	}

	@Transactional
	public List<Fund> update(List<Fund> funds, BindingResult errors, RequestInfo requestInfo) {

		try {

			funds = fetchRelated(funds);

			validate(funds, Constants.ACTION_UPDATE, errors);

			if (errors.hasErrors()) {
				throw new CustomBindException(errors);
			}
			for (Fund b : funds) {
				b.update();
			}

		} catch (CustomBindException e) {

			throw new CustomBindException(errors);
		}

		return fundRepository.update(funds, requestInfo);

	}

	private BindingResult validate(List<Fund> funds, String method, BindingResult errors) {

		try {
			switch (method) {
			case Constants.ACTION_VIEW:
				// validator.validate(fundContractRequest.getFund(), errors);
				break;
			case Constants.ACTION_CREATE:
				Assert.notNull(funds, "Funds to create must not be null");
				for (Fund fund : funds) {
					validator.validate(fund, errors);
				}
				break;
			case Constants.ACTION_UPDATE:
				Assert.notNull(funds, "Funds to update must not be null");
				for (Fund fund : funds) {
					Assert.notNull(fund.getId(), "Fund ID to update must not be null");
					validator.validate(fund, errors);
				}
				break;
                        case Constants.ACTION_SEARCH:
                            Assert.notNull(funds, "Funds to search must not be null");
                            for (Fund fund : funds) {
                                    Assert.notNull(fund.getTenantId(), "TenantID must not be null for search");
                            }
                            break;				
			default:

			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

	public List<Fund> fetchRelated(List<Fund> funds) {
		for (Fund fund : funds) {
			// fetch related items
			if (fund.getTenantId() != null)
				if (fund.getParent() != null && fund.getParent().getId() != null) {
					fund.getParent().setTenantId(fund.getTenantId());
					Fund parentId = fundRepository.findById(fund.getParent());
					if (parentId == null) {
						throw new InvalidDataException("parentId", "parentId.invalid", " Invalid parentId");
					}
					fund.setParent(parentId);
				}

		}

		return funds;
	}

        public Pagination<Fund> search(FundSearch fundSearch, BindingResult errors) {
            
            try {
                
                List<Fund> funds = new ArrayList<>();
                funds.add(fundSearch);
                validate(funds, Constants.ACTION_SEARCH, errors);
    
                if (errors.hasErrors()) {
                    throw new CustomBindException(errors);
                }
            
            } catch (CustomBindException e) {
    
                throw new CustomBindException(errors);
            }
    
            return fundRepository.search(fundSearch);
        }

	@Transactional
	public Fund save(Fund fund) {
		return fundRepository.save(fund);
	}

	@Transactional
	public Fund update(Fund fund) {
		return fundRepository.update(fund);
	}

}