package org.egov.egf.persistence.service;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.persistence.entity.ChartOfAccountDetail;
import org.egov.egf.persistence.queue.contract.ChartOfAccountDetailContract;
import org.egov.egf.persistence.queue.contract.ChartOfAccountDetailContractRequest;
import org.egov.egf.persistence.repository.ChartOfAccountDetailRepository;
import org.egov.egf.persistence.specification.ChartOfAccountDetailSpecification;
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
public class ChartOfAccountDetailService  {

  private final ChartOfAccountDetailRepository chartOfAccountDetailRepository;
  @PersistenceContext
private EntityManager entityManager;

  @Autowired
public ChartOfAccountDetailService(final ChartOfAccountDetailRepository chartOfAccountDetailRepository) {
   this.chartOfAccountDetailRepository = chartOfAccountDetailRepository;
  }

@Autowired
	private SmartValidator validator;   @Transactional
   public ChartOfAccountDetail create(final ChartOfAccountDetail chartOfAccountDetail) {
  return chartOfAccountDetailRepository.save(chartOfAccountDetail);
  } 
   @Transactional
   public ChartOfAccountDetail update(final ChartOfAccountDetail chartOfAccountDetail) {
  return chartOfAccountDetailRepository.save(chartOfAccountDetail);
    } 
  public List<ChartOfAccountDetail> findAll() {
   return chartOfAccountDetailRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
     }
  public ChartOfAccountDetail findOne(Long id){
  return chartOfAccountDetailRepository.findOne(id);
  }
  public Page<ChartOfAccountDetail> search(ChartOfAccountDetailContractRequest chartOfAccountDetailContractRequest){
final ChartOfAccountDetailSpecification specification = new ChartOfAccountDetailSpecification(chartOfAccountDetailContractRequest.getChartOfAccountDetail());
Pageable page = new PageRequest(chartOfAccountDetailContractRequest.getPage().getOffSet(),chartOfAccountDetailContractRequest.getPage().getPageSize());
  return chartOfAccountDetailRepository.findAll(specification,page);
  }
public BindingResult validate(ChartOfAccountDetailContractRequest chartOfAccountDetailContractRequest, String method,BindingResult errors) { 
	 
		try { 
			switch(method) 
			{ 
			case "update": 
				Assert.notNull(chartOfAccountDetailContractRequest.getChartOfAccountDetail(), "ChartOfAccountDetail to edit must not be null"); 
				validator.validate(chartOfAccountDetailContractRequest.getChartOfAccountDetail(), errors); 
				break; 
			case "view": 
				//validator.validate(chartOfAccountDetailContractRequest.getChartOfAccountDetail(), errors); 
				break; 
			case "create": 
				Assert.notNull(chartOfAccountDetailContractRequest.getChartOfAccountDetails(), "ChartOfAccountDetails to create must not be null"); 
				for(ChartOfAccountDetailContract b:chartOfAccountDetailContractRequest.getChartOfAccountDetails()) 
				 validator.validate(b, errors); 
				break; 
			case "updateAll": 
				Assert.notNull(chartOfAccountDetailContractRequest.getChartOfAccountDetails(), "ChartOfAccountDetails to create must not be null"); 
				for(ChartOfAccountDetailContract b:chartOfAccountDetailContractRequest.getChartOfAccountDetails()) 
				 validator.validate(b, errors); 
				break; 
			default : validator.validate(chartOfAccountDetailContractRequest.getRequestInfo(), errors); 
			} 
		} catch (IllegalArgumentException e) { 
			 errors.addError(new ObjectError("Missing data", e.getMessage())); 
		} 
		return errors; 
 
	}
}