package org.egov.egf.voucher.persistence.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.egov.common.domain.model.Pagination;
import org.egov.common.persistence.repository.JdbcRepository;
import org.egov.egf.voucher.domain.model.Voucher;
import org.egov.egf.voucher.domain.model.VoucherSearch;
import org.egov.egf.voucher.persistence.entity.VoucherEntity;
import org.egov.egf.voucher.persistence.entity.VoucherSearchEntity;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VoucherJdbcRepository extends JdbcRepository {
	private static final Logger LOG = LoggerFactory.getLogger(VoucherJdbcRepository.class);

	static {
		LOG.debug("init voucher");
		init(VoucherEntity.class);
		LOG.debug("end init voucher");
	}

	@Transactional
	public VoucherEntity create(VoucherEntity entity) {

		entity.setId(UUID.randomUUID().toString().replace("-", ""));
		try {
			super.create(entity);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if(e.getClass().getSimpleName().equals("PSQLException"))
			{
				PSQLException e1=(PSQLException)e;
			e1.getNextException();
			}
			e.printStackTrace();
			 
		}
		 
		return entity;    
	}

	public VoucherEntity update(VoucherEntity entity) {
		super.update(entity);
		return entity;

	}

	public Pagination<Voucher> search(VoucherSearch domain) {
		VoucherSearchEntity voucherSearchEntity = new VoucherSearchEntity();
		voucherSearchEntity.toEntity(domain);

		String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();
		String orderBy = "";

		searchQuery = searchQuery.replace(":tablename", VoucherEntity.TABLE_NAME);

		searchQuery = searchQuery.replace(":selectfields", " * ");

		// implement jdbc specfic search
		if (voucherSearchEntity.getId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("id =:id");
			paramValues.put("id", voucherSearchEntity.getId());
		}
		if (voucherSearchEntity.getType() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("type =:type");
			paramValues.put("type", voucherSearchEntity.getType());
		}
		if (voucherSearchEntity.getName() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("name =:name");
			paramValues.put("name", voucherSearchEntity.getName());
		}
		if (voucherSearchEntity.getDescription() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("description =:description");
			paramValues.put("description", voucherSearchEntity.getDescription());
		}
		if (voucherSearchEntity.getVoucherNumber() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("voucherNumber =:voucherNumber");
			paramValues.put("voucherNumber", voucherSearchEntity.getVoucherNumber());
		}
		if (voucherSearchEntity.getVoucherDate() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("voucherDate =:voucherDate");
			paramValues.put("voucherDate", voucherSearchEntity.getVoucherDate());
		}
		if (voucherSearchEntity.getFundId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("fund =:fund");
			paramValues.put("fund", voucherSearchEntity.getFundId());
		}
		/*if (voucherSearchEntity.getStatusId() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("status =:status");
			paramValues.put("status", voucherSearchEntity.getStatusId());
		}*/
		if (voucherSearchEntity.getOriginalVoucherNumber() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("originalVoucherNumber =:originalVoucherNumber");
			paramValues.put("originalVoucherNumber", voucherSearchEntity.getOriginalVoucherNumber());
		}
		if (voucherSearchEntity.getRefVoucherNumber() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("refVoucherNumber =:refVoucherNumber");
			paramValues.put("refVoucherNumber", voucherSearchEntity.getRefVoucherNumber());
		}
		if (voucherSearchEntity.getModuleName() != null) {
			if (params.length() > 0)
				params.append(" and ");
			params.append("moduleName =:moduleName");
			paramValues.put("moduleName", voucherSearchEntity.getModuleName());
		}
		

		Pagination<Voucher> page = new Pagination<>();
		page.setOffset(voucherSearchEntity.getOffset());
		page.setPageSize(voucherSearchEntity.getPageSize());

		if (params.length() > 0) {

			searchQuery = searchQuery.replace(":condition", " where " + params.toString());

		} else {
			searchQuery = searchQuery.replace(":condition", "");
		}

		searchQuery = searchQuery.replace(":orderby", "order by id ");

		page = (Pagination<Voucher>) getPagination(searchQuery, page, paramValues);
		searchQuery = searchQuery + " :pagination";

		searchQuery = searchQuery.replace(":pagination", "limit " + voucherSearchEntity.getPageSize() + " offset "
				+ voucherSearchEntity.getOffset() * voucherSearchEntity.getPageSize());

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(VoucherEntity.class);

		List<VoucherEntity> voucherEntities = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues,
				row);

		page.setTotalResults(voucherEntities.size());

		List<Voucher> vouchers = new ArrayList<Voucher>();
		for (VoucherEntity voucherEntity : voucherEntities) {

			vouchers.add(voucherEntity.toDomain());
		}
		page.setPagedData(vouchers);

		return page;
	}

	public VoucherEntity findById(VoucherEntity entity) {
		List<String> list = allUniqueFields.get(entity.getClass().getSimpleName());

		final List<Object> preparedStatementValues = new ArrayList<>();

		for (String s : list) {
			preparedStatementValues.add(getValue(getField(entity, s), entity));
		}

		List<VoucherEntity> vouchers = jdbcTemplate.query(getByIdQuery.get(entity.getClass().getSimpleName()),
				preparedStatementValues.toArray(), new BeanPropertyRowMapper<VoucherEntity>());
		if (vouchers.isEmpty()) {
			return null;
		} else {
			return vouchers.get(0);
		}

	}

}