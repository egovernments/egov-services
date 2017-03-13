package org.egov.lams.repository;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.egov.lams.config.PropertiesManager;
import org.egov.lams.contract.AssetResponse;
import org.egov.lams.model.Agreement;
import org.egov.lams.model.AgreementCriteria;
import org.egov.lams.model.Allottee;
import org.egov.lams.model.Asset;
import org.egov.lams.model.RequestInfo;
import org.egov.lams.repository.builder.AgreementQueryBuilder;
import org.egov.lams.repository.helper.AgreementHelper;
import org.egov.lams.repository.helper.AllotteeHelper;
import org.egov.lams.repository.helper.AssetHelper;
import org.egov.lams.repository.rowmapper.AgreementRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class AgreementRepository {
	public static final Logger logger = LoggerFactory.getLogger(AgreementRepository.class);

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	AssetHelper assetHelper;

	@Autowired
	AllotteeHelper allotteeHelper;
	
	@Autowired
	AgreementHelper agreementHelper;

	/**
	 * Allottee criteria is on name or mobile number which is expected to return 1 or 2 records.
	 * With this, we make the agreementQuery. so, the result set also is like 1 or 2 records.
	 * Then, we get all the assets for given asset criteria in a list and then
	 * filter agreement results based on the asset. 
	 */
	public List<Agreement> findByAllotee(AgreementCriteria agreementCriteria) {
		List<Object> preparedStatementValues = new ArrayList<Object>();
		List<Agreement> agreements = null;
		
		List<Allottee> allottees = getAllottees(agreementCriteria);
		agreementCriteria.setAllottee(allotteeHelper.getAllotteeIdList(allottees));
		String queryStr = AgreementQueryBuilder.agreementQueryBuilder(agreementCriteria, preparedStatementValues);
		try {
			agreements = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), new AgreementRowMapper());
		} catch (DataAccessException e) {
			// FIXME log exception here
			// FIXME see if it can be better than RunTimeException rethrown
			throw new RuntimeException(e.getMessage());
		}
		if (agreements.isEmpty()) 
			return agreements; // empty agreement list is returned
			//throw new RuntimeException("The criteria provided did not match any agreements");
		agreementCriteria.setAsset(assetHelper.getAssetIdListByAgreements(agreements));
		
		List<Asset> assets = getAssets(agreementCriteria);
		agreements = agreementHelper.filterAndEnrichAgreements(agreements, allottees, assets);
		
		return agreements;
	}

	public List<Agreement> findByAsset(AgreementCriteria agreementCriteria) {
		logger.info("AgreementController SearchAgreementService AgreementRepository : inside findByAsset");
		List<Object> preparedStatementValues = new ArrayList<Object>();
		List<Agreement> agreements = null;
		
		List<Asset> assets = getAssets(agreementCriteria);
		if (assets.size() > 1000) // FIXME
			throw new RuntimeException("Asset criteria is too big");
		agreementCriteria.setAsset(assetHelper.getAssetIdList(assets));
		String queryStr = AgreementQueryBuilder.agreementQueryBuilder(agreementCriteria, preparedStatementValues);
		try{
			agreements = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), new AgreementRowMapper());
		}catch (DataAccessException e) {
			// FIXME log exception
			throw new RuntimeException(e.getMessage());
		}
		if(agreements.isEmpty()) 
			return agreements;
		agreementCriteria.setAllottee(allotteeHelper.getAllotteeIdListByAgreements(agreements));
		List<Allottee> allottees = getAllottees(agreementCriteria);
		agreements = agreementHelper.filterAndEnrichAgreements(agreements, allottees, assets);

		return agreements;
	}

	public List<Agreement> findByAgreement(AgreementCriteria fetchAgreementsModel) {
		logger.info("AgreementController SearchAgreementService AgreementRepository : inside findByAgreement");
		List<Object> preparedStatementValues = new ArrayList<Object>();
		List<Agreement> agreements = null;
		
		

		String queryStr = AgreementQueryBuilder.agreementQueryBuilder(fetchAgreementsModel, preparedStatementValues);
		try{
			agreements = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), new AgreementRowMapper());
		}catch (DataAccessException e) {
			throw new RuntimeException(e.getMessage());
		}
		if(agreements.isEmpty()) throw new RuntimeException("The criteria provided did not match any agreements");
		fetchAgreementsModel.setAsset(assetHelper.getAssetIdListByAgreements(agreements));
		fetchAgreementsModel.setAllottee(allotteeHelper.getAllotteeIdListByAgreements(agreements));
		List<Asset> assets = getAssets(fetchAgreementsModel);
		List<Allottee> allottees = getAllottees(fetchAgreementsModel);
		agreements = agreementHelper.filterAndEnrichAgreements(agreements, allottees, assets);
		
		return agreements;
	}

	public List<Agreement> findByAgreementAndAllotee(AgreementCriteria agreementCriteria) {
		logger.info("AgreementController SearchAgreementService AgreementRepository : inside findByAgreementAndAllotee");
		List<Object> preparedStatementValues = new ArrayList<Object>();
		List<Agreement> agreements = null;

		String queryStr = AgreementQueryBuilder.agreementQueryBuilder(agreementCriteria, preparedStatementValues);
		try{
			agreements = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), new AgreementRowMapper());
		}catch (DataAccessException e) {
			throw new RuntimeException(e.getMessage());
		}
		if(agreements.isEmpty()) throw new RuntimeException("The criteria provided did not match any agreements");
		agreementCriteria.setAllottee(allotteeHelper.getAllotteeIdListByAgreements(agreements));
		List<Allottee> allottees = getAllottees(agreementCriteria);
		agreementCriteria.setAsset(assetHelper.getAssetIdListByAgreements(agreements));
		List<Asset> assets  = getAssets(agreementCriteria);
		agreements = agreementHelper.filterAndEnrichAgreements(agreements, allottees, assets);
		
		return agreements;
	}

	public List<Agreement> findByAgreementAndAsset(AgreementCriteria fetchAgreementsModel) {
		logger.info("AgreementController SearchAgreementService AgreementRepository : inside findByAgreementAndAsset");
		List<Object> preparedStatementValues = new ArrayList<Object>();
		List<Agreement> agreements = null;
		
		String queryStr = AgreementQueryBuilder.agreementQueryBuilder(fetchAgreementsModel, preparedStatementValues);
		try{
			agreements = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), new AgreementRowMapper());
		}catch (DataAccessException e) {
			throw new RuntimeException(e.getMessage());
		}
		if(agreements.isEmpty()) throw new RuntimeException("The criteria provided did not match any agreements");
		fetchAgreementsModel.setAsset(assetHelper.getAssetIdListByAgreements(agreements));
		List<Asset> assets  = getAssets(fetchAgreementsModel);
		fetchAgreementsModel.setAllottee(allotteeHelper.getAllotteeIdListByAgreements(agreements));
		List<Allottee> allottees = getAllottees(fetchAgreementsModel);
		agreements = agreementHelper.filterAndEnrichAgreements(agreements, allottees, assets);
		
		return agreements;
	}
	
	/*
	 * method to return a list of Allottee objects by making an API call to Allottee API 
	 */
	public List<Allottee> getAllottees(AgreementCriteria agreementCriteria) {
		//String queryString = allotteeBuilder.getAllotteeUrl(agreementCriteria);
		logger.info("AgreementController SearchAgreementService AgreementRepository : inside Allottee API caller");
		Allottee allottee = new Allottee();
		allottee.setId(45l);
		allottee.setAadhaarNumber("1235509945");
		allottee.setMobileNumber(9990002224l);
		allottee.setAddress("home");
		allottee.setName("ghanshyam");
		allottee.setPan("axy93jwbd");
		allottee.setEmailId("xyz.riflexions.com");
		List<Allottee> allottees = new ArrayList<>();
		allottees.add(allottee);
		
		return allottees;
	}
	/*
	 * method to return a list of Asset objects by making an API call to Asset API 
	 */
	public List<Asset> getAssets(AgreementCriteria agreementCriteria) {
		String queryString = assetHelper.getAssetUrl(agreementCriteria);
		String url = null;
		AssetResponse AssetResponse = null;
		try {
			url = propertiesManager.getAssetServiceHostName() + "?" + queryString;
			logger.info(url.toString());
			AssetResponse = restTemplate.postForObject(url,new RequestInfo(), AssetResponse.class);
		} catch (Exception e) {
			// FIXME log error to getstacktrace
			throw new RuntimeException("check if entered asset API url is correct or the asset service is running");
		}
		System.err.println(AssetResponse);
		if (AssetResponse.getAssets() == null || AssetResponse.getAssets().size()<=0)
			throw new RuntimeException("No assets found for given criteria");

		return AssetResponse.getAssets();
	}
}
