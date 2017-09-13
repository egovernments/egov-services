package org.egov.mr.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.egov.mr.model.MarriageDocumentType;
import org.egov.mr.model.enums.ServiceConfigurationKeys;
import org.egov.mr.repository.querybuilder.MarriageDocumentTypeQueryBuilder;
import org.egov.mr.repository.rowmapper.MarriageDocumentTypeRowMapper;
import org.egov.mr.service.ServiceConfigurationService;
import org.egov.mr.web.contract.MarriageDocumentTypeSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class MarriageDocumentTypeRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private MarriageDocumentTypeRowMapper rowMapper;

	@Autowired
	private MarriageDocumentTypeQueryBuilder marriageDocumentTypeQueryBuilder;

	@Autowired
	private ServiceConfigurationService serviceConfigurationService;

	public List<MarriageDocumentType> search(MarriageDocumentTypeSearchCriteria marriageDocumentTypeSearchCriteria) {
		List<Object> preparedStatementValues = new ArrayList<>();
		String query = marriageDocumentTypeQueryBuilder.getSelectQuery(marriageDocumentTypeSearchCriteria,
				preparedStatementValues);
		/**
		 * @Get_Query
		 */
		List<MarriageDocumentType> marriageDocumentTypesList = jdbcTemplate.query(query,
				preparedStatementValues.toArray(), rowMapper);
		log.info(marriageDocumentTypesList.toString());
		return marriageDocumentTypesList;
	}

	public void create(List<MarriageDocumentType> marriageDocumentTypes) {
		log.debug("MarriageDocumentTypes: " + marriageDocumentTypes);

		final String sql = MarriageDocumentTypeQueryBuilder.BATCH_INSERT_QUERY;
		final String tenantId = marriageDocumentTypes.get(0).getTenantId();
		final int batchSize = Integer.parseInt(serviceConfigurationService
				.getServiceConfigValueByKeyAndTenantId(ServiceConfigurationKeys.MARRIAGEBATCHSIZE, tenantId)
				.toString());
		log.debug("Batch Size :: " + batchSize);
		for (int j = 0; j < marriageDocumentTypes.size(); j += batchSize) {

			final List<MarriageDocumentType> batchList = marriageDocumentTypes.subList(j,
					j + batchSize > marriageDocumentTypes.size() ? marriageDocumentTypes.size() : j + batchSize);

			jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps, int index) throws SQLException {
					final MarriageDocumentType marriageDocumentType = marriageDocumentTypes.get(index);
					ps.setLong(1, marriageDocumentType.getId());
					ps.setString(2, marriageDocumentType.getName());
					ps.setString(3, marriageDocumentType.getCode());
					ps.setBoolean(4, marriageDocumentType.getIsActive());
					ps.setBoolean(5, marriageDocumentType.getIsIndividual());
					ps.setBoolean(6, marriageDocumentType.getIsRequired());
					ps.setString(7, marriageDocumentType.getProof().toString());
					ps.setString(8, marriageDocumentType.getApplicationType().toString());
					ps.setString(9, marriageDocumentType.getTenantId());
				}

				@Override
				public int getBatchSize() {
					return marriageDocumentTypes.size();
				}
			});
		}
	}

	public void update(List<MarriageDocumentType> marriageDocumentTypes) {
		log.debug("MarriageDocumentTypes: " + marriageDocumentTypes);
		final String sql = MarriageDocumentTypeQueryBuilder.BATCH_UPDATE_QUERY;
		final String tenantId = marriageDocumentTypes.get(0).getTenantId();
		final int batchSize = Integer.parseInt(serviceConfigurationService
				.getServiceConfigValueByKeyAndTenantId(ServiceConfigurationKeys.MARRIAGEBATCHSIZE, tenantId)
				.toString());
		log.debug("Batch Size :: " + batchSize);
		for (int j = 0; j < marriageDocumentTypes.size(); j += batchSize) {

			final List<MarriageDocumentType> batchList = marriageDocumentTypes.subList(j,
					j + batchSize > marriageDocumentTypes.size() ? marriageDocumentTypes.size() : j + batchSize);

			jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int index) throws SQLException {
					final MarriageDocumentType marriageDocumentType = marriageDocumentTypes.get(index);
					ps.setString(1, marriageDocumentType.getName());
					ps.setBoolean(2, marriageDocumentType.getIsActive());
					ps.setBoolean(3, marriageDocumentType.getIsIndividual());
					ps.setBoolean(4, marriageDocumentType.getIsRequired());
					ps.setString(5, marriageDocumentType.getProof().toString());
					ps.setString(6, marriageDocumentType.getApplicationType().toString());
					ps.setLong(7, marriageDocumentType.getId());
					ps.setString(8, marriageDocumentType.getCode());
					ps.setString(9, marriageDocumentType.getTenantId());
				}

				@Override
				public int getBatchSize() {
					return marriageDocumentTypes.size();
				}
			});
		}

	}

	/**
	 * @Get Ids_List
	 * @param marriageDocumentTypeList
	 * @return
	 */
	public List<Long> getIds(List<MarriageDocumentType> marriageDocumentTypeList) {
		log.debug("Differentiating Update And Create Records in getIds:: MarriageDocumentTypeRepository");
		StringBuilder idQuery = new StringBuilder("(" + marriageDocumentTypeList.get(0).getId());
		for (int index = 1; index < marriageDocumentTypeList.size(); index++) {
			idQuery.append("," + marriageDocumentTypeList.get(index).getId());
		}
		idQuery.append(")");

		List<Long> ids = jdbcTemplate.queryForList("SELECT id FROM egmr_marriage_document_type WHERE id IN " + idQuery,
				Long.class);
		return ids;
	}
}
