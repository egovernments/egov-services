package org.egov.egf.master.persistence.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.egov.common.domain.model.Pagination;
import org.egov.common.persistence.repository.JdbcRepository;
import org.egov.egf.master.domain.model.Supplier;
import org.egov.egf.master.domain.model.SupplierSearch;
import org.egov.egf.master.persistence.entity.SupplierEntity;
import org.egov.egf.master.persistence.entity.SupplierSearchEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

@Service
public class SupplierJdbcRepository extends JdbcRepository {
	private static final Logger LOG = LoggerFactory.getLogger(SupplierJdbcRepository.class);

	static {
		LOG.debug("init supplier");
		init(SupplierEntity.class);
		LOG.debug("end init supplier");
	}

	public SupplierEntity create(SupplierEntity entity) {

		entity.setId(UUID.randomUUID().toString().replace("-", ""));
		super.create(entity);
		return entity;
	}

	public SupplierEntity update(SupplierEntity entity) {
		super.update(entity);
		return entity;

	}

	public Pagination<Supplier> search(SupplierSearch domain) {
		SupplierSearchEntity supplierSearchEntity = new SupplierSearchEntity();
		supplierSearchEntity.toEntity(domain);

		String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();
		String orderBy = "";

		searchQuery = searchQuery.replace(":tablename", SupplierEntity.TABLE_NAME);

		searchQuery = searchQuery.replace(":selectfields", " * ");

		// implement jdbc specfic search
if( supplierSearchEntity.getId()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "id =: id");
paramValues.put("id" ,supplierSearchEntity.getId());} 
if( supplierSearchEntity.getCode()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "code =: code");
paramValues.put("code" ,supplierSearchEntity.getCode());} 
if( supplierSearchEntity.getName()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "name =: name");
paramValues.put("name" ,supplierSearchEntity.getName());} 
if( supplierSearchEntity.getAddress()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "address =: address");
paramValues.put("address" ,supplierSearchEntity.getAddress());} 
if( supplierSearchEntity.getMobile()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "mobile =: mobile");
paramValues.put("mobile" ,supplierSearchEntity.getMobile());} 
if( supplierSearchEntity.getEmail()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "email =: email");
paramValues.put("email" ,supplierSearchEntity.getEmail());} 
if( supplierSearchEntity.getDescription()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "description =: description");
paramValues.put("description" ,supplierSearchEntity.getDescription());} 
if( supplierSearchEntity.getActive()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "active =: active");
paramValues.put("active" ,supplierSearchEntity.getActive());} 
if( supplierSearchEntity.getPanNo()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "panNo =: panNo");
paramValues.put("panNo" ,supplierSearchEntity.getPanNo());} 
if( supplierSearchEntity.getTinNo()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "tinNo =: tinNo");
paramValues.put("tinNo" ,supplierSearchEntity.getTinNo());} 
if( supplierSearchEntity.getRegistationNo()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "registationNo =: registationNo");
paramValues.put("registationNo" ,supplierSearchEntity.getRegistationNo());} 
if( supplierSearchEntity.getBankAccountId()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "bankAccount =: bankAccount");
paramValues.put("bankAccount" ,supplierSearchEntity.getBankAccountId());} 
if( supplierSearchEntity.getIfscCode()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "ifscCode =: ifscCode");
paramValues.put("ifscCode" ,supplierSearchEntity.getIfscCode());} 
if( supplierSearchEntity.getBankId()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "bank =: bank");
paramValues.put("bank" ,supplierSearchEntity.getBankId());} 

		 

		Pagination<Supplier> page = new Pagination<>();
		page.setOffSet(supplierSearchEntity.getOffset());
		page.setPageSize(supplierSearchEntity.getPageSize());

		if (params.length() > 0) {

			searchQuery = searchQuery.replace(":condition", " where " + params.toString());

		} else {
			searchQuery = searchQuery.replace(":condition", "");
		}

		searchQuery = searchQuery.replace(":orderby", "order by id ");

		page = getPagination(searchQuery, page);
		searchQuery = searchQuery + " :pagination";

		searchQuery = searchQuery.replace(":pagination", "limit " + supplierSearchEntity.getPageSize() + " offset "
				+ supplierSearchEntity.getOffset() * supplierSearchEntity.getPageSize());

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(SupplierEntity.class);

		List<SupplierEntity> supplierEntities = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);

		page.setTotalResults(supplierEntities.size());

		List<Supplier> suppliers = new ArrayList<Supplier>();
		for (SupplierEntity supplierEntity : supplierEntities) {

			suppliers.add(supplierEntity.toDomain());
		}
		page.setPagedData(suppliers);

		return page;
	}

	public SupplierEntity findById(SupplierEntity entity) {
		List<String> list = allUniqueFields.get(entity.getClass().getSimpleName());

		final List<Object> preparedStatementValues = new ArrayList<>();

		for (String s : list) {
			preparedStatementValues.add(getValue(getField(entity, s), entity));
		}

		List<SupplierEntity> suppliers = jdbcTemplate.query(getByIdQuery.get(entity.getClass().getSimpleName()),
				preparedStatementValues.toArray(), new BeanPropertyRowMapper<SupplierEntity>());
		if (suppliers.isEmpty()) {
			return null;
		} else {
			return suppliers.get(0);
		}

	}

}