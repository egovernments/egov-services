package org.egov.tradelicense.persistence.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.tradelicense.common.config.PropertiesManager;
import org.egov.tradelicense.common.persistense.repository.JdbcRepository;
import org.egov.tradelicense.persistence.entity.TradeLicenseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TradeLicenseJdbcRepository extends JdbcRepository {

	@Autowired
	 PropertiesManager propertiesManager;
	
	private static final Logger LOG = LoggerFactory.getLogger(TradeLicenseJdbcRepository.class);
	static {
		LOG.debug("init accountCodePurpose");
		init(TradeLicenseEntity.class);
		LOG.debug("end init accountCodePurpose");
	}

	public TradeLicenseEntity create(TradeLicenseEntity entity) {
		super.create(entity);

		return entity;
	}
	
	public List<TradeLicenseEntity> search(String tenantId, Integer pageSize, Integer pageNumber, String sort, String active,
			String tradeLicenseId, String applicationNumber, String licenseNumber, 
			String mobileNumber, String aadhaarNumber, String emailId, String propertyAssesmentNo, Integer revenueWard,
			Integer locality, String ownerName, String tradeTitle, String tradeType, Integer tradeCategory,
			Integer tradeSubCategory, String legacy, Integer status) {
		
		
		List<Object> preparedStatementValues = new ArrayList<>();
		
		String query = buildSearchQuery(tenantId, pageSize, pageNumber, sort, active,
				tradeLicenseId, applicationNumber, licenseNumber, mobileNumber,
				aadhaarNumber, emailId, propertyAssesmentNo, revenueWard, locality,
				ownerName, tradeTitle, tradeType, tradeCategory, tradeSubCategory,
				legacy, status, preparedStatementValues);


		return super.executeSearchQuery(query, preparedStatementValues);


	}


	public String buildSearchQuery(String tenantId, Integer pageSize, Integer pageNumber, String sort, String active,
			String tradeLicenseId, String applicationNumber, String licenseNumber,
			String mobileNumber, String aadhaarNumber, String emailId, String propertyAssesmentNo, Integer revenueWard,
			Integer locality, String ownerName, String tradeTitle, String tradeType, Integer tradeCategory,
			Integer tradeSubCategory,String legacy, Integer status, List<Object> preparedStatementValues) {

		StringBuffer searchSql = new StringBuffer();
		searchSql.append("select * from " + "egtl_license" + " where ");
		searchSql.append(" tenantId = ? ");
		preparedStatementValues.add(tenantId);

	
		if (active != null ) {

			if(active.equalsIgnoreCase("true")){
				searchSql.append(" AND active = ? ");
				preparedStatementValues.add(true);	
			}
			else if(active.equalsIgnoreCase("false")){
				searchSql.append(" AND active = ? ");
				preparedStatementValues.add(false);	
			}

		}
		
		if (tradeLicenseId != null && !tradeLicenseId.isEmpty()) {
			searchSql.append(" AND id = ? ");
			preparedStatementValues.add(tradeLicenseId);
		}
		
		if (applicationNumber != null && !applicationNumber.isEmpty()) {
			searchSql.append(" AND applicationNumber = ? ");
			preparedStatementValues.add(applicationNumber);
		}
		
		if (licenseNumber != null && !licenseNumber.isEmpty()) {
			searchSql.append(" AND licenseNumber = ? ");
			preparedStatementValues.add(licenseNumber);
		}
		
		if (mobileNumber != null && !mobileNumber.isEmpty()) {
			searchSql.append(" AND mobileNumber = ? ");
			preparedStatementValues.add(mobileNumber);
		}
		
		if (aadhaarNumber != null && !aadhaarNumber.isEmpty()) {
			searchSql.append(" AND adhaarNumber = ? ");
			preparedStatementValues.add(aadhaarNumber);
		}
		
		if (emailId != null && !emailId.isEmpty()) {
			searchSql.append(" AND emailId = ? ");
			preparedStatementValues.add(emailId);
		}
		
		if (propertyAssesmentNo != null && !propertyAssesmentNo.isEmpty()) {
			searchSql.append(" AND propertyAssesmentNo = ? ");
			preparedStatementValues.add(propertyAssesmentNo);
		}

		
		
		if (revenueWard != null) {
			searchSql.append(" AND revenueWardId = ? ");
			preparedStatementValues.add(revenueWard);
		}
		
		if (locality != null) {
			searchSql.append(" AND localityId = ? ");
			preparedStatementValues.add(locality);
		}
		
		if (ownerName != null && !ownerName.isEmpty()) {
			searchSql.append(" AND ownerName = ? ");
			preparedStatementValues.add(ownerName);
		}
		
		if (tradeTitle != null && !tradeTitle.isEmpty()) {
			searchSql.append(" AND tradeTitle = ? ");
			preparedStatementValues.add(tradeTitle);
		}
		
		if (tradeType != null && !tradeType.isEmpty()) {
			searchSql.append(" AND tradeType = ? ");
			preparedStatementValues.add(tradeType);
		}
		if (tradeCategory != null ) {
			searchSql.append(" AND categoryId = ? ");
			preparedStatementValues.add(tradeCategory);
		}
		
		if ( tradeSubCategory != null ) {
			searchSql.append(" AND subCategoryId = ? ");
			preparedStatementValues.add(tradeSubCategory);
		}
		

		if (pageSize == null) {
			pageSize = Integer.valueOf(propertiesManager.getPageSize());	
		}
		
		if (pageNumber == null) {
			pageNumber = Integer.valueOf(propertiesManager.getPageNumber());
		}	
		
			
			
			if (sort == null || sort.isEmpty()) {
				searchSql.append("  ORDER BY licenseNumber ASC");
				
			}
			else{
				searchSql.append("  ORDER BY licenseNumber,tradeTitle,ownerName ASC");	
			}
		
			searchSql.append(" offset ? ");
			preparedStatementValues.add(((pageNumber-1)*pageSize));
			
			
				searchSql.append(" limit ? ");
				preparedStatementValues.add(pageSize);


		return searchSql.toString();
	}
}