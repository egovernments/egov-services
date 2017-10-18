package org.egov.property.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.egov.models.AuditDetails;
import org.egov.models.Demolition;
import org.egov.models.DemolitionRequest;
import org.egov.models.DemolitionSearchCriteria;
import org.egov.models.Document;
import org.egov.models.RequestInfo;
import org.egov.property.repository.builder.DemolitionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author Prasad
 *
 */

@Repository
public class DemolitionRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	DemolitionBuilder demolitionBuilder;

	public Long saveDemolition(Demolition demolition) throws Exception {

		final PreparedStatementCreator psc = new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(DemolitionBuilder.INSERT_DEMOLITION,
						new String[] { "id" });
				ps.setString(1, demolition.getTenantId());
				ps.setString(2, demolition.getUpicNumber());
				ps.setString(3, demolition.getApplicationNo());
				ps.setString(4, demolition.getPropertySubType());
				ps.setString(5, demolition.getUsageType());
				ps.setString(6, demolition.getUsageSubType());
				ps.setDouble(7, getDouble(demolition.getTotalArea()));
				ps.setObject(8, getInteger(demolition.getSequenceNo()));
				ps.setBoolean(9, getBoolean(demolition.getIsLegal()));
				ps.setString(10, demolition.getDemolitionReason());
				ps.setString(11, demolition.getComments());
				ps.setString(12, demolition.getStateId());
				ps.setString(13, demolition.getAuditDetails().getCreatedBy());
				ps.setString(14, demolition.getAuditDetails().getLastModifiedBy());
				ps.setLong(15, getLong(demolition.getAuditDetails().getCreatedTime()));
				ps.setLong(16, getLong(demolition.getAuditDetails().getLastModifiedTime()));

				return ps;
			}
		};

		// The newly generated key will be saved in this object
		final KeyHolder holder = new GeneratedKeyHolder();

		jdbcTemplate.update(psc, holder);

		Long propertyId = Long.parseLong(String.valueOf(holder.getKey().intValue()).trim());

		return propertyId;

	}

	/**
	 * This method will cast the given object to Long
	 * 
	 * @param object
	 *            that need to be cast to Long
	 * @return {@link Long}
	 */
	private Long getLong(Object object) {
		return object == null ? 0 : Long.parseLong(object.toString());
	}

	/**
	 * This method will cast the given object to double
	 * 
	 * @param object
	 *            that need to be cast to Double
	 * @return {@link Double}
	 */
	private Double getDouble(Object object) {
		return object == null ? 0.0 : Double.parseDouble(object.toString());
	}

	public Integer getInteger(Object value) {

		if (value == null)
			return null;
		else
			return Integer.valueOf(value.toString());
	}

	/**
	 * This method will cast the given object to Long
	 * 
	 * @param object
	 *            that need to be cast to Long
	 * @return {@link Long}
	 */
	private Boolean getBoolean(Object object) {
		return object == null ? Boolean.FALSE : Boolean.TRUE;
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

	public void saveDemolitionDocuments(Demolition demolition, Long demolitionId, AuditDetails auditDetails)
			throws Exception {

		for (Document document : demolition.getDocuments()) {
			final PreparedStatementCreator psc = new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
					final PreparedStatement ps = connection
							.prepareStatement(DemolitionBuilder.INSERT_DEMOLITION_DOCUMENT, new String[] { "id" });
					ps.setString(1, document.getFileStore());
					ps.setString(2, document.getDocumentType());
					ps.setInt(3, demolitionId.intValue());
					ps.setString(4, auditDetails.getCreatedBy());
					ps.setString(5, auditDetails.getLastModifiedBy());
					ps.setLong(6, auditDetails.getCreatedTime());
					ps.setLong(7, auditDetails.getLastModifiedTime());
					return ps;
				}
			};

			// The newly generated key will be saved in this object
			final KeyHolder holder = new GeneratedKeyHolder();

			jdbcTemplate.update(psc, holder);

		}
	}

	@Transactional
	public void updateDemolition(DemolitionRequest demolitionRequest) throws Exception {

		Demolition demolition = demolitionRequest.getDemolition();

		Object[] demolitionArgs = { demolition.getTenantId(), demolition.getUpicNumber(), demolition.getApplicationNo(),
				demolition.getPropertySubType(), demolition.getUsageType(), demolition.getUsageSubType(),
				getDouble(demolition.getTotalArea()), getInteger(demolition.getSequenceNo()),
				getBoolean(demolition.getIsLegal()), demolition.getDemolitionReason(), demolition.getComments(),
				demolition.getStateId(), demolition.getAuditDetails().getCreatedBy(),
				demolition.getAuditDetails().getLastModifiedBy(),
				getLong(demolition.getAuditDetails().getCreatedTime()),
				getLong(demolition.getAuditDetails().getLastModifiedTime()), demolition.getId() };

		jdbcTemplate.update(DemolitionBuilder.UPDATE_DEMOLITION, demolitionArgs);

		updateDemolitionDocument(demolition, demolitionRequest.getRequestInfo());
	}

	private void updateDemolitionDocument(Demolition demolition, RequestInfo requestInfo) throws Exception {

		List<Long> documentIds = getDocumentIdsForDemolition(demolition.getId());

		for (Document document : demolition.getDocuments()) {

			if (document.getId() != null && documentIds.contains(document.getId())) {

				AuditDetails auditDetails = document.getAuditDetails();
				Object[] demolitionArgs = { document.getFileStore(), document.getDocumentType(), demolition.getId(),
						auditDetails.getCreatedBy(), auditDetails.getLastModifiedBy(), auditDetails.getCreatedTime(),
						auditDetails.getCreatedTime(), document.getId() };

				documentIds.remove(document.getId());
				jdbcTemplate.update(DemolitionBuilder.UPDATE_DEMOLITION_DOCUMENT, demolitionArgs);
			} else if (document.getId() == null) {

				saveDemolitionDocument(document, demolition.getId(), getAuditDetail(requestInfo));
			}

		}
		documentIds.forEach(id -> {
			deleteDocuments(id);
		});

	}

	public List<Demolition> searchDemolitions(DemolitionSearchCriteria demolitionSearchCriteria) {
		List<Demolition> demolitions = new ArrayList<Demolition>();
		List<Object> preparedStatementValues = new ArrayList<>();

		if (demolitionSearchCriteria.getUpicNo() == null && demolitionSearchCriteria.getOldUpicNo() != null) {

			String upicNo = jdbcTemplate.queryForObject(DemolitionBuilder.GET_UPIC_NO_BY_OLD_UPIC,
					new Object[] { demolitionSearchCriteria.getOldUpicNo() }, String.class);
			demolitionSearchCriteria.setUpicNo(upicNo);
		}

		String searchQuery = demolitionBuilder.searchDemolitionsQuery(demolitionSearchCriteria,
				preparedStatementValues);
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(searchQuery, preparedStatementValues.toArray());

		for (Map<String, Object> row : rows) {
			Demolition demolition = new Demolition();
			demolition.setId(getLong(row.get("id")));
			demolition.setTenantId(getString(row.get("tenantId")));
			demolition.setUpicNumber(getString(row.get("upicNumber")));
			demolition.setApplicationNo(getString(row.get("applicationno")));
			demolition.setPropertySubType(getString(row.get("propertySubType")));
			demolition.setUsageType(getString(row.get("usageType")));
			demolition.setUsageSubType(getString(row.get("usageSubType")));
			demolition.setTotalArea(getDouble(row.get("totalArea")));
			demolition.setSequenceNo(getInteger(row.get("sequenceNo")));
			demolition.setIsLegal(getBoolean(row.get("isLegal")));
			demolition.setDemolitionReason(getString("demolitionReason"));
			demolition.setComments(getString(row.get("comments")));
			demolition.setStateId(getString(row.get("stateId")));
			demolition.setDocuments(getDocumentsForDemolition(getLong(row.get("id"))));
			AuditDetails auditDetails = new AuditDetails();
			auditDetails.setCreatedBy(getString(row.get("createdby")));
			auditDetails.setLastModifiedBy(getString(row.get("lastmodifiedby")));
			auditDetails.setCreatedTime(getLong(row.get("createdtime")));
			auditDetails.setLastModifiedTime(getLong(row.get("lastmodifiedtime")));
			demolition.setAuditDetails(auditDetails);
			demolitions.add(demolition);

		}

		return demolitions;
	}

	private List<Document> getDocumentsForDemolition(Long demolitionId) {

		String searchQuery = DemolitionBuilder.GET_DOCUMENTS_BY_DEMOLITION;

		List<Document> demolitionDocuments = new ArrayList<Document>();

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(searchQuery, new Object[] { demolitionId });
		for (Map<String, Object> row : rows) {
			Document document = new Document();
			document.setId(getLong(row.get("id")));
			document.setFileStore(getString(row.get("filestore")));
			document.setDocumentType(getString(row.get("documenttype")));
			AuditDetails auditDetails = new AuditDetails();
			auditDetails.setCreatedBy(getString(row.get("createdby")));
			auditDetails.setLastModifiedBy(getString(row.get("lastmodifiedby")));
			auditDetails.setCreatedTime(getLong(row.get("createdtime")));
			auditDetails.setLastModifiedTime(getLong(row.get("lastmodifiedtime")));
			document.setAuditDetails(auditDetails);
			demolitionDocuments.add(document);
		}

		return demolitionDocuments;

	}

	public List<Long> getDocumentIdsForDemolition(Long documentId) {
		List<Long> documentIds = new ArrayList<Long>();

		String searchQuery = DemolitionBuilder.GET_DOCUMENTIDS_BY_DOCUMENT;

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(searchQuery, new Object[] { documentId });
		for (Map<String, Object> row : rows) {
			documentIds.add(getLong(row.get("id")));
		}
		return documentIds;
	}

	private AuditDetails getAuditDetail(RequestInfo requestInfo) {
		String userId = requestInfo.getUserInfo().getId().toString();
		Long currEpochDate = new Date().getTime();
		AuditDetails auditDetail = new AuditDetails();
		auditDetail.setCreatedBy(userId);
		auditDetail.setCreatedTime(currEpochDate);
		auditDetail.setLastModifiedBy(userId);
		auditDetail.setLastModifiedTime(currEpochDate);
		return auditDetail;
	}

	private void deleteDocuments(Long documentId) {
		jdbcTemplate.update(DemolitionBuilder.DELETE_DOCUMENT_BY_DOCUMENT_ID, new Object[] { documentId });
	}

	private void saveDemolitionDocument(Document document, Long demolitionId, AuditDetails auditDetails)
			throws Exception {

		final PreparedStatementCreator psc = new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(DemolitionBuilder.INSERT_DEMOLITION_DOCUMENT,
						new String[] { "id" });
				ps.setString(1, document.getFileStore());
				ps.setString(2, document.getDocumentType());
				ps.setInt(3, demolitionId.intValue());
				ps.setString(4, auditDetails.getCreatedBy());
				ps.setString(5, auditDetails.getLastModifiedBy());
				ps.setLong(6, auditDetails.getCreatedTime());
				ps.setLong(7, auditDetails.getLastModifiedTime());
				return ps;
			}
		};

		// The newly generated key will be saved in this object
		final KeyHolder holder = new GeneratedKeyHolder();

		jdbcTemplate.update(psc, holder);

	}
}
