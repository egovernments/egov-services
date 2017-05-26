package org.egov.mr.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.egov.mr.model.MarriageRegn;
import org.egov.mr.repository.querybuilder.MarriageRegnQueryBuilder;
import org.egov.mr.repository.rowmapper.MarriageRegnIdsRowMapper;
import org.egov.mr.repository.rowmapper.MarriageRegnRowMapper;
import org.egov.mr.web.contract.MarriageRegnCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MarriageRegnRepository {

	@Autowired
	private MarriageRegnQueryBuilder marriageRegnQueryBuilder;

	@Autowired
	private MarriageRegnIdsRowMapper marriageRegnIdsRowMapper;

	@Autowired
	private MarriageRegnRowMapper marriageRegnRowMapper;

	@Autowired
	private WitnessRepository witnessRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public static final String INSERT_JSON_QUERY = "INSERT INTO marriage_regn_json(applicationnumber, doc, tenantid)"
			+ " VALUES(?, to_json(?::json), ?)";
			
	public static final String INSERT_MARRIAGE_REGN_QUERY = "INSERT INTO egmr_marriage_regn("
			+ " regnunitid, marriagedate, venue, street, placeofmarriage, locality, city, marriagephoto, fee,"
			+ " bridegroomid, brideid, priestname, priestreligion, priestaddress, serialno, volumeno, applicationnumber,"
			+ " regnnumber, status, source, stateid, approvaldepartment, approvaldesignation, approvalassignee,"
			+ " approvalaction, approvalstatus, approvalcomments, tenantid)"
			+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

	public static final String UPDATE_MARRIAGE_REGN_QUERY = "UPDATE egmr_marriage_regn"
			+ " SET(regnunitid, marriagedate, venue, street, placeofmarriage, locality, city, marriagephoto, fee,"
			+ " bridegroomid, brideid, priestname, priestreligion, priestaddress, serialno, volumeno,"
			+ " regnnumber, status, source, stateid, approvaldepartment, approvaldesignation, approvalassignee,"
			+ " approvalaction, approvalstatus, approvalcomments)"
			+" = (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
			+" WHERE applicationnumber = ? AND tenantid = ?" ;

	@SuppressWarnings("unchecked")
	public List<MarriageRegn> findForCriteria(MarriageRegnCriteria marriageRegnCriteria) {
		List<Object> preparedStatementValuesForListOfMarriageRegnIds = new ArrayList<Object>();
		String queryStrForListOfMarriageRegnIds = marriageRegnQueryBuilder.getQueryForListOfMarriageRegnIds(
				marriageRegnCriteria, preparedStatementValuesForListOfMarriageRegnIds);

		List<String> listOfApplNos = jdbcTemplate.query(queryStrForListOfMarriageRegnIds,
				preparedStatementValuesForListOfMarriageRegnIds.toArray(), marriageRegnIdsRowMapper);

		if (listOfApplNos.isEmpty()) {
			return Collections.EMPTY_LIST;
		}
		List<Object> preparedStatementValues = new ArrayList<Object>();
		String queryStr = marriageRegnQueryBuilder.getQuery(marriageRegnCriteria, preparedStatementValues,
				listOfApplNos);
		List<MarriageRegn> marriageRegn = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(),
				marriageRegnRowMapper);
		return marriageRegn;
	}

	public void save(MarriageRegn marriageRegn) {
		Object[] obj = new Object[] { marriageRegn.getRegnUnit().getId(), marriageRegn.getMarriageDate(),
				marriageRegn.getVenue().toString(), marriageRegn.getStreet(), marriageRegn.getPlaceOfMarriage(),
				marriageRegn.getLocality(), marriageRegn.getCity(), marriageRegn.getMarriagePhoto(),
				marriageRegn.getFee(), marriageRegn.getBridegroom().getId(), marriageRegn.getBride().getId(),
				marriageRegn.getPriest().getName(), marriageRegn.getPriest().getReligion(),
				marriageRegn.getPriest().getAddress(), marriageRegn.getSerialNo(), marriageRegn.getVolumeNo(),
				marriageRegn.getApplicationNumber(), marriageRegn.getRegistrationNumber(), marriageRegn.getStatus().toString(),
				marriageRegn.getSource().toString(), marriageRegn.getStateId(), marriageRegn.getApprovalDetails().getDepartment(),
				marriageRegn.getApprovalDetails().getDesignation(), marriageRegn.getApprovalDetails().getAssignee(),
				marriageRegn.getApprovalDetails().getAction(), marriageRegn.getApprovalDetails().getStatus(),
				marriageRegn.getApprovalDetails().getComments(), marriageRegn.getTenantId() };
		jdbcTemplate.update(INSERT_MARRIAGE_REGN_QUERY, obj);
		
		if (marriageRegn.getWitnesses() != null)
			marriageRegn.getWitnesses().forEach(witness -> {
				witnessRepository.save(marriageRegn.getApplicationNumber(), marriageRegn.getTenantId(), witness);
			});
	}

	public String generateApplicationNumber() {
		return jdbcTemplate.queryForObject("SELECT nextval('seq_marriageregn_application_number')", String.class);
	}

	public void update(MarriageRegn marriageRegn) {
		Object[] obj = new Object[] { marriageRegn.getRegnUnit().getId(), marriageRegn.getMarriageDate(),
				marriageRegn.getVenue().toString(), marriageRegn.getStreet(), marriageRegn.getPlaceOfMarriage(),
				marriageRegn.getLocality(), marriageRegn.getCity(), marriageRegn.getMarriagePhoto(),
				marriageRegn.getFee(), marriageRegn.getBridegroom().getId(), marriageRegn.getBride().getId(),
				marriageRegn.getPriest().getName(), marriageRegn.getPriest().getReligion(),
				marriageRegn.getPriest().getAddress(), marriageRegn.getSerialNo(), marriageRegn.getVolumeNo(),
			    marriageRegn.getRegistrationNumber(), marriageRegn.getStatus().toString(),
				marriageRegn.getSource().toString(), marriageRegn.getStateId(), marriageRegn.getApprovalDetails().getDepartment(),
				marriageRegn.getApprovalDetails().getDesignation(), marriageRegn.getApprovalDetails().getAssignee(),
				marriageRegn.getApprovalDetails().getAction(), marriageRegn.getApprovalDetails().getStatus(),
				marriageRegn.getApprovalDetails().getComments(), marriageRegn.getApplicationNumber(), marriageRegn.getTenantId() };
		jdbcTemplate.update(UPDATE_MARRIAGE_REGN_QUERY, obj);
		
		if (marriageRegn.getWitnesses() != null)
			marriageRegn.getWitnesses().forEach(witness -> {
				witnessRepository.update(marriageRegn.getApplicationNumber(), marriageRegn.getTenantId(), witness);
			});
	}

	public void saveJson(String applicationNumber, String marriageRegnJson, String tenantId) {
		jdbcTemplate.update(INSERT_JSON_QUERY, applicationNumber, marriageRegnJson, tenantId);	
	}
}
