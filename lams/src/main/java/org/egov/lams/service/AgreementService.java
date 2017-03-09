package org.egov.lams.service;

import java.util.List;

import org.egov.lams.model.Agreement;
import org.egov.lams.model.AgreementCriteria;
import org.egov.lams.model.RentIncrementType;
import org.egov.lams.producers.AgreementProducer;
import org.egov.lams.repository.AgreementRepository;
import org.egov.lams.repository.builder.AgreementQueryBuilder;
import org.egov.lams.repository.rowmapper.RentIncrementRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AgreementService {
	public static final Logger logger = LoggerFactory.getLogger(AgreementService.class);

	@Autowired
	private AgreementRepository agreementRepository;
	
	@Autowired
	private AgreementProducer agreementProducer;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Agreement> searchAgreement(AgreementCriteria agreementCriteria) {
		/*
		 * three boolean variables isAgreementNull,isAssetNull and
		 * isAllotteeNull declared to indicate whether criteria arguments for
		 * each of the Agreement,Asset and Allottee objects are given or not.
		 */
		boolean isAgreementNull = (agreementCriteria.getAgreementId() == null
				&& agreementCriteria.getAgreementNumber() == null && agreementCriteria.getStatus() == null
				&& (agreementCriteria.getFromDate() == null && agreementCriteria.getToDate() == null)
				&& agreementCriteria.getTenderNumber() == null && agreementCriteria.getTinNumber() == null
				&& agreementCriteria.getTradelicenseNumber() == null && agreementCriteria.getAsset() == null
				&& agreementCriteria.getAllottee() == null);

		boolean isAllotteeNull = (agreementCriteria.getAllotteeName() == null
				&& agreementCriteria.getMobilenumber() == null);

		boolean isAssetNull = (agreementCriteria.getAssetCategory() == null
				&& agreementCriteria.getShoppingComplexNo() == null && agreementCriteria.getAssetCode() == null
				&& agreementCriteria.getLocality() == null && agreementCriteria.getRevenueWard() == null
				&& agreementCriteria.getElectionWard() == null && agreementCriteria.getTenantId() == null
				&& agreementCriteria.getDoorno() == null);

		if (!isAgreementNull && !isAssetNull && !isAllotteeNull) {
			logger.info("agreementRepository.findByAllotee");
			return agreementRepository.findByAllotee(agreementCriteria);
			
		} else if (!isAgreementNull && isAssetNull && !isAllotteeNull) {
			logger.info("agreementRepository.findByAllotee");
			return agreementRepository.findByAgreementAndAllotee(agreementCriteria);

		} else if (!isAgreementNull && !isAssetNull && isAllotteeNull) {
			logger.info("agreementRepository.findByAgreementAndAsset : both agreement and ");
			return agreementRepository.findByAgreementAndAsset(agreementCriteria);

		} else if ((isAgreementNull && isAssetNull && !isAllotteeNull)
				|| (isAgreementNull && !isAssetNull && !isAllotteeNull)) {
			logger.info("agreementRepository.findByAllotee : only allottee || allotte and asset");
			return agreementRepository.findByAllotee(agreementCriteria);

		} else if (isAgreementNull && !isAssetNull && isAllotteeNull) {
			logger.info("agreementRepository.findByAsset : only asset");
			return agreementRepository.findByAsset(agreementCriteria);

		} else if (!isAgreementNull && isAssetNull && isAllotteeNull) {
			logger.info("agreementRepository.findByAgreement : only agreement");
			return agreementRepository.findByAgreement(agreementCriteria);
		} else {
			// if no values are given for all the three criteria objects
			// (isAgreementNull && isAssetNull && isAllotteeNull)
			logger.info("agreementRepository.findByAgreement : all values null");
			return agreementRepository.findByAgreement(agreementCriteria);
		}
	}
	
	/*
	 * This method is used to create new agreement
	 * 
	 * @return Agreement, return the agreement details with current status
	 * 
	 * @param agreement, hold agreement details 
	 * 
	 * */
	
	public Agreement createAgreement(Agreement agreement){
		ObjectMapper mapper = new ObjectMapper();
		String agreementValue=null;
		String rentIncrementTypeqQuery=AgreementQueryBuilder.findRentIncrementTypeQuery();
	    Object[] rentObj = new Object[]{ agreement.getRentIncrementMethod().getId() };
	    Long agreementNumber=null;
	    RentIncrementType rentIncrementType=null;
	    try {
	    	 	rentIncrementType=jdbcTemplate.queryForObject(rentIncrementTypeqQuery,rentObj,new RentIncrementRowMapper());
				agreementNumber=(Long) jdbcTemplate.queryForList("SELECT NEXTVAL('seq_lams_rentincrement')").get(0).get("nextval");
				agreement.setAgreementNumber(agreementNumber.toString());
	    }catch(Exception ex){
	    		ex.printStackTrace();
	    		throw new RuntimeException("Invalid rent increment type");
	    }
	    if(rentIncrementType==null||rentIncrementType.getId()==null){
	    		throw new RuntimeException("Invalid rent increment method type");
	    }
		try {
				logger.info("createAgreement service::"+agreement);
				agreementValue = mapper.writeValueAsString(agreement);
				logger.info("agreementValue::"+agreementValue);
		} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
		try {
				agreementProducer.sendMessage("agreement-save-db", "save-agreement", agreementValue);
		}catch(Exception ex){
				ex.printStackTrace();
		}
		return agreement;
	}

}
