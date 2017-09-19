package org.egov.tl.masters.persistence.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.egov.tl.masters.persistence.entity.FeeMatrixDetailEntity;
import org.egov.tl.masters.persistence.entity.FeeMatrixEntity;
import org.egov.tradelicense.persistence.repository.builder.FeeMatrixQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class FeeMatrixDetailJdbcRepository extends JdbcRepository {

	@Autowired
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	static {

		init(FeeMatrixDetailEntity.class);

	}

	/**
	 * 
	 * @param FeeMatrixEntity
	 * @return FeeMatrixEntity
	 * 
	 *         this method will call the JdbcRepository create method that will
	 *         insert the data into database and returns FeeMatrixEntity
	 *         whatever it receives from JdbcRepository create method
	 */
	public FeeMatrixDetailEntity create(FeeMatrixDetailEntity entity) {
		super.create(entity);
		return entity;
	}

	/**
	 * this method will call the JdbcRepository create method that will update
	 * the data in the database and returns FeeMatrixEntity whatever it receives
	 * from JdbcRepository update method
	 * 
	 * @param entity
	 * @return
	 */
	public FeeMatrixDetailEntity update(FeeMatrixDetailEntity entity) {
		super.update(entity);
		return entity;
	}

	public Long getNextSequence() {

		String id = getSequence(FeeMatrixEntity.SEQUENCE_NAME);
		return Long.valueOf(id);
	}

	public List<FeeMatrixDetailEntity> getFeeMatrixDetails(Long id) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String feeMatrixDetailSearchQuery = FeeMatrixQueryBuilder.buildFeeMatrixDetailSearchQuery(id, parameters);
		List<FeeMatrixDetailEntity> feeMatrixDetails = new ArrayList<>();
		List<Map<String, Object>> rows = namedParameterJdbcTemplate.queryForList(feeMatrixDetailSearchQuery,
				parameters);

		for (Map<String, Object> row : rows) {
			FeeMatrixDetailEntity feeMatrixDetail = new FeeMatrixDetailEntity();
			feeMatrixDetail.setId(getLong(row.get("id")));
			feeMatrixDetail.setFeeMatrixId(getLong(row.get("feeMatrixId")));
			feeMatrixDetail.setTenantId(getString(row.get("tenantId")));
			feeMatrixDetail.setUomFrom(getLong(row.get("uomFrom")));
			feeMatrixDetail.setUomTo(getLong(row.get("uomTo")));
			feeMatrixDetail.setAmount(getDouble(row.get("amount")));
			feeMatrixDetail.setCreatedBy(getString(row.get("createdby")));
			feeMatrixDetail.setLastModifiedBy(getString(row.get("lastmodifiedby")));
			feeMatrixDetail.setCreatedTime(getLong(row.get("createdtime")));
			feeMatrixDetail.setLastModifiedTime(getLong(row.get("lastmodifiedtime")));

			feeMatrixDetails.add(feeMatrixDetail);
		}

		return feeMatrixDetails;
	}

	/**
	 * This method will cast the given object to String
	 * 
	 * @param object
	 *            that need to be cast to string
	 * @return {@link String}
	 */
	private String getString(Object object) {
		return object == null ? null : object.toString();
	}

	/**
	 * This method will cast the given object to double
	 * 
	 * @param object
	 *            that need to be cast to Double
	 * @return {@link Double}
	 */
	private Double getDouble(Object object) {
		return object == null ? null : Double.parseDouble(object.toString());
	}

	/**
	 * This method will cast the given object to Long
	 * 
	 * @param object
	 *            that need to be cast to Long
	 * @return {@link Long}
	 */
	private Long getLong(Object object) {
		return object == null ? null : Long.parseLong(object.toString());
	}

	public void deleteFeeMatrixDetailWithId(Long id) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String Query = FeeMatrixQueryBuilder.getDeleteFeeMatrixDetaisWithIdQuery();
		parameters.addValue("id", id);
		namedParameterJdbcTemplate.update(Query, parameters);
	}
}