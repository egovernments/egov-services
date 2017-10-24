package org.egov.property.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.egov.models.AuditDetails;
import org.egov.models.Document;
import org.egov.models.PropertyDetail;
import org.egov.models.RequestInfo;
import org.egov.models.TaxExemption;
import org.egov.models.TaxExemptionRequest;
import org.egov.models.TaxExemptionSearchCriteria;
import org.egov.property.config.PropertiesManager;
import org.egov.property.repository.builder.TaxExemptionBuilder;
import org.egov.property.repository.builder.TaxExemptionDocumentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author Anil
 *
 */
@Repository
public class TaxExemptionRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	TaxExemptionBuilder taxExemptionBuilder;

	public Long saveTaxExemption(TaxExemption taxExemption) {

		final PreparedStatementCreator pscTaxExemption = new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				// TODO Auto-generated method stub
				final PreparedStatement ps = connection.prepareStatement(TaxExemptionBuilder.INSERT_TAXEXEMPTION_QUERY,
						new String[] { "id" });

				ps.setString(1, taxExemption.getTenantId());
				ps.setString(2, taxExemption.getUpicNumber());
				ps.setString(3, taxExemption.getApplicationNo());
				ps.setString(4, taxExemption.getExemptionReason());
				ps.setDouble(5, getDouble(taxExemption.getExemptionPercentage()));
				ps.setString(6, taxExemption.getComments());
				ps.setString(7, taxExemption.getStateId());
				ps.setString(8, taxExemption.getAuditDetails().getCreatedBy());
				ps.setString(9, taxExemption.getAuditDetails().getLastModifiedBy());
				ps.setLong(10, getLong(taxExemption.getAuditDetails().getCreatedTime()));
				ps.setLong(11, getLong(taxExemption.getAuditDetails().getLastModifiedTime()));

				return ps;
			}
		};

		// The newly generated key will be saved in this object
		final KeyHolder taxExemptionKey = new GeneratedKeyHolder();

		jdbcTemplate.update(pscTaxExemption, taxExemptionKey);

		Long taxExemptionId = Long.parseLong(String.valueOf(taxExemptionKey.getKey().intValue()).trim());

		return taxExemptionId;

	}

	public Long saveTaxExemptionDocument(Document document, Long taxExemptionId) {

		final PreparedStatementCreator pscDocument = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(
						TaxExemptionDocumentBuilder.INSERT_TAXEXEMPTIONDOCUMENT_QUERY, new String[] { "id" });
				ps.setString(1, document.getFileStore());
				ps.setString(2, document.getAuditDetails().getCreatedBy());
				ps.setString(3, document.getAuditDetails().getLastModifiedBy());
				ps.setLong(4, getLong(document.getAuditDetails().getCreatedTime()));
				ps.setLong(5, getLong(document.getAuditDetails().getLastModifiedTime()));
				ps.setLong(6, getLong(taxExemptionId));
				ps.setString(7, document.getDocumentType());
				return ps;
			}
		};

		final KeyHolder holderTaxExemptionDocument = new GeneratedKeyHolder();

		jdbcTemplate.update(pscDocument, holderTaxExemptionDocument);

		Long taxExemptionDocumentId = Long
				.parseLong(String.valueOf(holderTaxExemptionDocument.getKey().intValue()).trim());

		return taxExemptionDocumentId;
	}

	public void updateTaxExemption(TaxExemptionRequest taxExemptionRequest) throws Exception {
		
		TaxExemption taxExemption = taxExemptionRequest.getTaxExemption();
		String taxExemptionUpdate = TaxExemptionBuilder.UPDATE_TAXEXEMPTION_QUERY;

		Object[] taxExemptionArgs = { taxExemption.getTenantId(), taxExemption.getUpicNumber(),
				taxExemption.getApplicationNo(), taxExemption.getExemptionReason(),
				getDouble(taxExemption.getExemptionPercentage()), taxExemption.getComments(), taxExemption.getStateId(),
				taxExemption.getAuditDetails().getLastModifiedBy(),
				taxExemption.getAuditDetails().getLastModifiedTime(), taxExemption.getId() };

		jdbcTemplate.update(taxExemptionUpdate, taxExemptionArgs);
		
		updateTaxExemptionDocument(taxExemption, taxExemptionRequest.getRequestInfo());

	}
	
	public void updateTaxExemptionDocument(TaxExemption taxExemption, RequestInfo requestInfo) throws Exception {

		List<Long> documentIds = getDocumentIdsForTaxExemption(taxExemption.getId());
		for (Document document : taxExemption.getDocuments()) {

			if (document.getId() != null && documentIds.contains(document.getId())) {
	
				String documentUpdate = TaxExemptionDocumentBuilder.updateTaxExemptionDocumentQuery();
				Object[] documentArgs = { 
						document.getFileStore(), 
						document.getAuditDetails().getLastModifiedBy(),
						document.getAuditDetails().getLastModifiedTime(), 
						taxExemption.getId(),
						document.getDocumentType(),
						document.getId() };

				documentIds.remove(document.getId());
				jdbcTemplate.update(documentUpdate, documentArgs);
			} else if (document.getId() == null) {
				
				Long recordId = saveTaxExemptionDocument(document, taxExemption.getId());
			}

		}
		documentIds.forEach(id -> {
			deleteDocuments(id);
		});
	}
	
	public List<Long> getDocumentIdsForTaxExemption(Long id) {

		List<Long> documentIds = new ArrayList<Long>();
		String searchQuery = TaxExemptionDocumentBuilder.GETDOCUMENTIDQUERY;
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(searchQuery, new Object[] { id });
		for (Map<String, Object> row : rows) {
			documentIds.add(getLong(row.get("id")));
		}
		return documentIds;
	}
	
	public void deleteDocuments(Long id) {
		
		String deleteQuery = TaxExemptionDocumentBuilder.DELETEDOCUMENTQUERY;
		jdbcTemplate.update(deleteQuery, new Object[] { id });
	}

	public void updateTaxExemptionDocument(Document document, Long taxExemptionId) {

		String documentUpdate = TaxExemptionDocumentBuilder.updateTaxExemptionDocumentQuery();

		Object[] documentArgs = { document.getFileStore(), document.getAuditDetails().getLastModifiedBy(),
				document.getAuditDetails().getLastModifiedTime(), taxExemptionId, document.getDocumentType(),
				document.getId() };

		jdbcTemplate.update(documentUpdate, documentArgs);
	}

	/**
	 * Tax Exemption PropertyDetails Udpate final approval - udpating tax
	 * exemption isexemption and exemption reason to propertydetails
	 * 
	 * @param propertyDetails
	 * @throws Exception
	 */
	@Transactional
	public void updateTaxExemptionPropertyDetail(PropertyDetail propertyDetails) throws Exception {

		String propertyDetailsUpdate = TaxExemptionBuilder.UPDATE_TAXEXEMPTIONPROPERTYDETAIL_QUERY;

		Object[] propertyDetailsArgs = { propertyDetails.getIsExempted(), propertyDetails.getExemptionReason(),
				propertyDetails.getAuditDetails().getLastModifiedBy(),
				propertyDetails.getAuditDetails().getLastModifiedTime(), propertyDetails.getId() };

		jdbcTemplate.update(propertyDetailsUpdate, propertyDetailsArgs);
	}

	/**
	 * Tax Exemption Search repository
	 * 
	 * @param requestInfo
	 * @param tenantId
	 * @param pageSize
	 * @param pageNumber
	 * @param sort
	 * @param upicNo
	 * @param oldUpicNo
	 * @param applicationNo
	 * @param taxExemptionId
	 * @return
	 * @throws Exception
	 */
	public List<TaxExemption> searchTaxExemption(RequestInfo requestInfo,
			TaxExemptionSearchCriteria taxExemptionSearchCriteria) throws Exception {

		List<Object> preparedStatementValues = new ArrayList<Object>();

		String upicNo = null;

		if (taxExemptionSearchCriteria.getUpicNo() == null && taxExemptionSearchCriteria.getOldUpicNo() != null) {

			upicNo = jdbcTemplate.queryForObject(TaxExemptionBuilder.GET_UPIC_NO_BY_OLD_UPIC,
					new Object[] { taxExemptionSearchCriteria.getOldUpicNo() }, String.class);

			if (upicNo != null) {
				taxExemptionSearchCriteria.setUpicNo(upicNo);
			}
		}

		String searchQuery = taxExemptionBuilder.getSearchQuery(taxExemptionSearchCriteria, preparedStatementValues);
		List<TaxExemption> taxExemptions = getTaxExemption(searchQuery, preparedStatementValues);

		return taxExemptions;
	}

	/**
	 * This Api will give the list of tax exemption records based on the given
	 * parameters
	 * 
	 * @param query
	 * @param preparedStatementValues
	 * @return TaxExemption List
	 * @throws Exception
	 * @return {@link List} of {@link TaxExemption}
	 */
	private List<TaxExemption> getTaxExemption(String query, List<Object> preparedStatementValues) throws Exception {

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, preparedStatementValues.toArray());
		List<TaxExemption> taxExemptions = new ArrayList<TaxExemption>();
		TaxExemption taxExemption = new TaxExemption();
		for (Map<String, Object> row : rows) {
			taxExemption.setId(getLong(row.get("id")));
			taxExemption.setTenantId(getString(row.get("tenantid")));
			taxExemption.setUpicNumber(getString(row.get("upicnumber")));
			taxExemption.setApplicationNo(getString(row.get("applicationno")));
			taxExemption.setExemptionReason(getString(row.get("exemptionreason")));
			taxExemption.setExemptionPercentage(getDouble(row.get("exemptionpercentage")));
			taxExemption.setComments(getString(row.get("comments")));
			taxExemption.setStateId(getString(row.get("stateid")));
			taxExemption.setDocuments(getDocumentsForTaxExemption(getLong(row.get("id"))));
			AuditDetails auditDetails = new AuditDetails();
			auditDetails.setCreatedBy(getString(row.get("createdby")));
			auditDetails.setLastModifiedBy(getString(row.get("lastmodifiedby")));
			auditDetails.setCreatedTime(getLong(row.get("createdtime")));
			auditDetails.setLastModifiedTime(getLong(row.get("lastmodifiedtime")));
			taxExemption.setAuditDetails(auditDetails);
			taxExemptions.add(taxExemption);

		}
		return taxExemptions;
	}

	private List<Document> getDocumentsForTaxExemption(Long demolitionId) {

		List<Document> taxExemptionDocuments = new ArrayList<Document>();

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(TaxExemptionBuilder.GET_DOCUMENTSBY_TAX_EXEMPTION,
				new Object[] { demolitionId });
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
			taxExemptionDocuments.add(document);
		}

		return taxExemptionDocuments;

	}

	/**
	 * * This method will cast the given object to String
	 * 
	 * @param object
	 *            that need to be cast to string
	 * @return {@link String}
	 */
	private String getString(Object object) {
		return object == null ? null : object.toString();
	}

	// /**
	// * This API will return the list of Documents for the given tax exemption
	// Id
	// *
	// * @param taxExemptionId
	// * @return {@link Document}
	// */
	// @SuppressWarnings({ "unchecked", "rawtypes" })
	// private List<Document> getDocumentsForTaxExemption(Long taxExemptionId) {
	// List<Object> preparedStatementvalues = new ArrayList<>();
	// String documentsQuery =
	// TaxExemptionBuilder.getDocumentsByTaxExemption(taxExemptionId,
	// preparedStatementvalues);
	// List<Document> documents = null;
	//
	// documents = jdbcTemplate.query(documentsQuery,
	// preparedStatementvalues.toArray(),
	// new BeanPropertyRowMapper(Document.class));
	//
	// return documents;
	// }

	//
	// public List<Document> getDocumentByPropertyDetails(Long propertyDetailId)
	// {
	// List<Map<String, Object>> rows = null;
	// try {
	// rows =
	// jdbcTemplate.queryForList(DocumentBuilder.DOCUMENT_BY_PROPERTY_DETAILS_QUERY,
	// new Object[] { propertyDetailId });
	// } catch (EmptyResultDataAccessException e) {
	// return null;
	// }
	// List<Document> documents = getDocumentObject(rows);
	// documents.forEach(document -> {
	// if (document.getId() != null && document.getId() > 0)
	// document.setAuditDetails(getAuditDetailsForDocument(document.getId()));
	// });
	//
	// return documents;
	// }

	/**
	 * This will give the audit details for the given document Id
	 * 
	 * @param taxexemption
	 * @return {@link AuditDetails}
	 */
	@SuppressWarnings("rawtypes")
	public AuditDetails getAuditDetailsForDocument(Long documentId) {
		String query = TaxExemptionBuilder.AUDIT_DETAILS_QUERY;
		@SuppressWarnings("unchecked")
		AuditDetails auditDetails = (AuditDetails) jdbcTemplate.queryForObject(query, new Object[] { documentId },
				new BeanPropertyRowMapper(AuditDetails.class));
		return auditDetails;
	}

	// conversion methods

	public Long getLong(Long value) {

		if (value == null)
			return 0l;
		else
			return value;
	}

	public Double getDouble(Double value) {

		if (value == null)
			return 0.0;
		else
			return value;
	}

	private Double getDouble(Object object) {
		return object == null ? 0.0 : Double.parseDouble(object.toString());
	}

	public Integer getInteger(Integer value) {

		if (value == null)
			return 0;
		else
			return value;
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
	private Long getLong(Object object) {
		return object == null ? 0 : Long.parseLong(object.toString());
	}

}
