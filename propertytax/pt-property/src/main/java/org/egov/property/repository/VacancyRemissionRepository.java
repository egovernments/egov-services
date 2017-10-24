package org.egov.property.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.egov.enums.Reason;
import org.egov.models.AuditDetails;
import org.egov.models.Document;
import org.egov.models.RequestInfo;
import org.egov.models.TitleTransfer;
import org.egov.models.VacancyRemission;
import org.egov.models.VacancyRemissionRequest;
import org.egov.property.repository.builder.VacancyRemissionBuilder;
import org.egov.property.repository.builder.VacancyRemissionDocumentBuilder;
import org.egov.property.utility.TimeStampUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author Yosadhara
 *
 */
@Repository
public class VacancyRemissionRepository {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	VacancyRemissionBuilder vacancyRemissionBuilder;
	
	public Long saveVacancyRemission(VacancyRemission vacancyRemission) {

		final PreparedStatementCreator pscVacancyRemission = new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
	
				final PreparedStatement ps = connection
						.prepareStatement(VacancyRemissionBuilder.INSERT_VACANCYREMISSION_QUERY, new String[] { "id" });

				ps.setString(1, vacancyRemission.getUpicNo());
				ps.setString(2, vacancyRemission.getTenantId());
				ps.setString(3, vacancyRemission.getApplicationNo());
				ps.setTimestamp(4, TimeStampUtil.getTimeStamp(vacancyRemission.getFromDate()));
				ps.setTimestamp(5, TimeStampUtil.getTimeStamp(vacancyRemission.getToDate()));
				ps.setDouble(6, getDouble(vacancyRemission.getPercentage()));
				ps.setString(7, vacancyRemission.getReason().toString());
				ps.setTimestamp(8, TimeStampUtil.getTimeStamp(vacancyRemission.getRequestDate()));
				ps.setTimestamp(9, TimeStampUtil.getTimeStamp(vacancyRemission.getApprovedDate()));
				ps.setBoolean(10, vacancyRemission.getIsApproved());
				ps.setString(11, vacancyRemission.getRemarks());
				ps.setString(12, vacancyRemission.getStateId());
				ps.setString(13, vacancyRemission.getAuditDetails().getCreatedBy());
				ps.setString(14, vacancyRemission.getAuditDetails().getLastModifiedBy());
				ps.setLong(15, getLong(vacancyRemission.getAuditDetails().getCreatedTime()));
				ps.setLong(16, getLong(vacancyRemission.getAuditDetails().getLastModifiedTime()));
				return ps;
			}
		};

		// The newly generated key will be saved in this object
		final KeyHolder vacancyRemissionKey = new GeneratedKeyHolder();

		jdbcTemplate.update(pscVacancyRemission, vacancyRemissionKey);

		Long vacancyRemissionId = Long.parseLong(String.valueOf(vacancyRemissionKey.getKey().intValue()).trim());

		return vacancyRemissionId;

	}
	
	public Long saveVacancyRemissionDocument(Document document, Long vacancyRemissionId) {

		final PreparedStatementCreator pscDocument = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(
						VacancyRemissionDocumentBuilder.INSERT_VACANCYREMISSIONDOCUMENT_QUERY, new String[] { "id" });
				ps.setString(1, document.getFileStore());
				ps.setString(2, document.getAuditDetails().getCreatedBy());
				ps.setString(3, document.getAuditDetails().getLastModifiedBy());
				ps.setLong(4, getLong(document.getAuditDetails().getCreatedTime()));
				ps.setLong(5, getLong(document.getAuditDetails().getLastModifiedTime()));
				ps.setString(6, document.getDocumentType());
				ps.setLong(7, getLong(vacancyRemissionId));
				return ps;
			}
		};

		final KeyHolder holderVacancyRemissionDocument = new GeneratedKeyHolder();

		jdbcTemplate.update(pscDocument, holderVacancyRemissionDocument);

		Long vacancyRemissionDocumentId = Long
				.parseLong(String.valueOf(holderVacancyRemissionDocument.getKey().intValue()).trim());

		return vacancyRemissionDocumentId;
	}
	
	public void updateVacancyRemission(VacancyRemissionRequest vacancyRemissionRequest) throws Exception {

		String vacancyRemissionUpdate = VacancyRemissionBuilder.updateVacancyRemissionQuery();
		
		VacancyRemission vacancyRemission = vacancyRemissionRequest.getVacancyRemission();

		Object[] vacancyRemissionArgs = { vacancyRemission.getUpicNo(), vacancyRemission.getTenantId(),
				vacancyRemission.getApplicationNo(), TimeStampUtil.getTimeStamp(vacancyRemission.getFromDate()),
				TimeStampUtil.getTimeStamp(vacancyRemission.getToDate()), getDouble(vacancyRemission.getPercentage()),
				vacancyRemission.getReason().toString(), TimeStampUtil.getTimeStamp(vacancyRemission.getRequestDate()),
				TimeStampUtil.getTimeStamp(vacancyRemission.getApprovedDate()),
				getBoolean(vacancyRemission.getIsApproved()), vacancyRemission.getRemarks(),
				vacancyRemission.getStateId(), vacancyRemission.getAuditDetails().getLastModifiedBy(),
				vacancyRemission.getAuditDetails().getLastModifiedTime(), vacancyRemission.getId() };

		jdbcTemplate.update(vacancyRemissionUpdate, vacancyRemissionArgs);
		
		updateVacancyRemissionDocument(vacancyRemission, vacancyRemissionRequest.getRequestInfo());

	}
	
	public void updateVacancyRemissionDocument(VacancyRemission vacancyRemission, RequestInfo requestInfo) throws Exception {

		List<Long> documentIds = getDocumentIdsForVacancyRemission(vacancyRemission.getId());
		for (Document document : vacancyRemission.getDocuments()) {

			if (document.getId() != null && documentIds.contains(document.getId())) {
				
				String documentUpdate = VacancyRemissionDocumentBuilder.updateVacancyRemissionDocumentQuery();
				Object[] documentArgs = { 
						document.getFileStore(), 
						document.getAuditDetails().getLastModifiedBy(),
						document.getAuditDetails().getLastModifiedTime(), 
						document.getDocumentType(), 
						vacancyRemission.getId(),
						document.getId() };

				documentIds.remove(document.getId());
				jdbcTemplate.update(documentUpdate, documentArgs);
			} else if (document.getId() == null) {
				
				Long recordId = saveVacancyRemissionDocument(document, vacancyRemission.getId());
			}

		}
		documentIds.forEach(id -> {
			deleteDocuments(id);
		});
	}
	
	public List<Long> getDocumentIdsForVacancyRemission(Long id) {

		List<Long> documentIds = new ArrayList<Long>();
		String searchQuery = VacancyRemissionDocumentBuilder.GETDOCUMENTIDQUERY;
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(searchQuery, new Object[] { id });
		for (Map<String, Object> row : rows) {
			documentIds.add(getLong(row.get("id")));
		}
		return documentIds;
	}
	
	public void deleteDocuments(Long id) {
		
		String deleteQuery = VacancyRemissionDocumentBuilder.DELETEDOCUMENTQUERY;
		jdbcTemplate.update(deleteQuery, new Object[] { id });
	}
	
	/**
	 * Search vacancy remission repository
	 * 
	 * @param requestInfo
	 * @param tenantId
	 * @param pageSize
	 * @param pageNumber
	 * @param upicNumber
	 * @param applicationNo
	 * @param approvalDate
	 * @param isApproved
	 * @return
	 * @throws Exception
	 */
	public List<VacancyRemission> searchVacancyRemission(RequestInfo requestInfo, String tenantId, Integer pageSize,
			Integer pageNumber, String upicNumber, String applicationNo, String approvalDate, Boolean isApproved)
			throws Exception {
		List<Object> preparedStatementValues = new ArrayList<Object>();

		String searchQuery = vacancyRemissionBuilder.getSearchQuery(tenantId, pageSize, pageNumber, upicNumber,
				applicationNo, approvalDate, isApproved, preparedStatementValues);
		List<VacancyRemission> vacancyRemissions = getVacancyRemissions(searchQuery, preparedStatementValues);

		for (VacancyRemission vacancyRemission : vacancyRemissions) {
			vacancyRemission.setDocuments(getDocumentsForVacancyRemission(vacancyRemission.getId()));
		}

		return vacancyRemissions;
	}

	/**
	 * This API will return the list of Documents for the given vacancy
	 * remission Id
	 * 
	 * @param vacancyRemissionId
	 * @return {@link Document}
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<Document> getDocumentsForVacancyRemission(Long vacancyRemissionId) {
		List<Object> preparedStatementvalues = new ArrayList<>();
		String documentsQuery = VacancyRemissionBuilder.getDocumentsByVacancyRemission(vacancyRemissionId,
				preparedStatementvalues);
		List<Document> documents = null;

		documents = jdbcTemplate.query(documentsQuery, preparedStatementvalues.toArray(),
				new BeanPropertyRowMapper(Document.class));

		return documents;
	}

	/**
	 * This Api will give the list of vacancy remission records based on the
	 * given parameters
	 * 
	 * @param query
	 * @param preparedStatementValues
	 * @return Vacancy Remission List
	 * @throws Exception
	 * @return {@link List} of {@link TitleTransfer}
	 */
	private List<VacancyRemission> getVacancyRemissions(String query, List<Object> preparedStatementValues)
			throws Exception {

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, preparedStatementValues.toArray());
		List<VacancyRemission> vacancyRemissions = new ArrayList<VacancyRemission>();
		VacancyRemission vacancyRemission = new VacancyRemission();
		for (Map<String, Object> row : rows) {
			vacancyRemission.setId(getLong(row.get("id")));
			vacancyRemission.setUpicNo(getString(row.get("upicno")));
			vacancyRemission.setTenantId(getString(row.get("tenantid")));
			vacancyRemission.setApplicationNo(getString(row.get("applicationno")));
			vacancyRemission.setFromDate(TimeStampUtil.getDateFormat(getString(row.get("fromdate"))));
			vacancyRemission.setToDate(TimeStampUtil.getDateFormat(getString(row.get("todate"))));
			vacancyRemission.setPercentage(getDouble(row.get("percentage")));
			vacancyRemission.setReason(Reason.fromValue(getString(row.get("reason"))));
			String requestDate = getString(row.get("requestdate"));
			if (requestDate != null) {
				vacancyRemission.setRequestDate(TimeStampUtil.getDateFormat(requestDate));
			}
			String approvedDate = getString(row.get("approveddate"));
			if (approvedDate != null) {
				vacancyRemission.setApprovedDate(TimeStampUtil.getDateFormat(approvedDate));
			}

			vacancyRemission.setIsApproved(getBoolean(row.get("isapproved")));
			vacancyRemission.setRemarks(getString(row.get("remarks")));
			vacancyRemission.setStateId(getString(row.get("stateid")));
			AuditDetails auditDetails = new AuditDetails();
			auditDetails.setCreatedBy(getString(row.get("createdby")));
			auditDetails.setLastModifiedBy(getString(row.get("lastmodifiedby")));
			auditDetails.setCreatedTime(getLong(row.get("createdtime")));
			auditDetails.setLastModifiedTime(getLong(row.get("lastmodifiedtime")));
			vacancyRemission.setAuditDetails(auditDetails);
			vacancyRemissions.add(vacancyRemission);

		}
		return vacancyRemissions;
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

}
