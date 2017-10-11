package org.egov.mr.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.egov.mr.model.MarriageDocument;
import org.egov.mr.model.MarriageRegn;
import org.egov.mr.repository.querybuilder.MarriageRegnQueryBuilder;
import org.egov.mr.repository.rowmapper.MarriageRegnIdsRowMapper;
import org.egov.mr.repository.rowmapper.MarriageRegnRowMapper;
import org.egov.mr.web.contract.MarriageRegnCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
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

	public static final String INSERT_MARRIAGE_REGN_QUERY = "INSERT INTO egmr_marriage_regn(id, regnunitid,"
			+ " marriagedate, venue, street, placeofmarriage,locality, city, marriagephoto, bridegroomid,"
			+ " brideid, priestname,priestreligion, priestaddress, priestaadhaar, priestmobileno,"
			+ " priestemail, serialno, volumeno, applicationnumber, regnnumber, regndate, status, source,"
			+ " stateid, isactive, tenantid, feeid, demandid, approvaldepartment, approvaldesignation,"
			+ " approvalassignee, approvalaction, approvalstatus, approvalcomments, createdby,lastmodifiedby,"
			+ " createdtime, lastmodifiedtime)"
			+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

	public static final String UPDATE_MARRIAGE_REGN_QUERY = "UPDATE egmr_marriage_regn  SET regnunitid=?,"
			+ " marriagedate=?, venue=?, street=?, placeofmarriage=?, locality=?, city=?, marriagephoto=?,"
			+ " bridegroomid=?, brideid=?, feeid=?, demandid=?, priestname=?, priestreligion=?, priestaddress=?,"
			+ " priestaadhaar=?, priestmobileno=?, priestemail=?, serialno=?, volumeno=?, regnnumber=?,"
			+ " regndate=?, status=?, source=?, stateid=?, isactive=?, approvaldepartment=?, approvaldesignation=?,"
			+ " approvalassignee=?, approvalaction=?, approvalstatus=?, approvalcomments=?, "
			+ " lastmodifiedby=?, lastmodifiedtime=? WHERE tenantid=? AND applicationnumber=? ;";

	@SuppressWarnings("unchecked")
	public List<MarriageRegn> findForCriteria(MarriageRegnCriteria marriageRegnCriteria) {
		List<Object> preparedStatementValuesForListOfMarriageRegnIds = new ArrayList<Object>();
		String queryStrForListOfMarriageRegnIds = marriageRegnQueryBuilder.getQueryForListOfMarriageRegnIds(
				marriageRegnCriteria, preparedStatementValuesForListOfMarriageRegnIds);

		List<String> listOfApplNos = jdbcTemplate.query(queryStrForListOfMarriageRegnIds,
				preparedStatementValuesForListOfMarriageRegnIds.toArray(), marriageRegnIdsRowMapper);

		System.out.println("queryStrForListOfMarriageRegnIds " + queryStrForListOfMarriageRegnIds);

		System.out.println("listOfApplNos " + listOfApplNos);
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
	
		jdbcTemplate.update(INSERT_MARRIAGE_REGN_QUERY, new PreparedStatementSetter(){
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, marriageRegn.getId());
				ps.setLong(2, marriageRegn.getRegnUnit().getId());
				ps.setLong(3, marriageRegn.getMarriageDate());
				ps.setString(4, marriageRegn.getVenue().toString());
				ps.setString(5, marriageRegn.getStreet());
				ps.setString(6, marriageRegn.getPlaceOfMarriage());
				ps.setString(7, marriageRegn.getLocality());
				ps.setString(8, marriageRegn.getCity());
				ps.setString(9, marriageRegn.getMarriagePhoto());
				ps.setLong(10, marriageRegn.getBridegroom().getId());
				ps.setLong(11, marriageRegn.getBride().getId());
				ps.setString(12, marriageRegn.getPriest().getName());
				ps.setLong(13, marriageRegn.getPriest().getReligion());
				ps.setString(14, marriageRegn.getPriest().getAddress());
				ps.setString(15, marriageRegn.getPriest().getAadhaar());
				ps.setString(16, marriageRegn.getPriest().getMobileNo());
				ps.setString(17, marriageRegn.getPriest().getEmail());
				ps.setString(18, marriageRegn.getSerialNo());
				ps.setString(19, marriageRegn.getVolumeNo());
				ps.setString(20, marriageRegn.getApplicationNumber());
				ps.setString(21, marriageRegn.getRegnNumber());
				ps.setLong(22, marriageRegn.getRegnDate());
				ps.setString(23, marriageRegn.getStatus().toString());
				ps.setString(24, marriageRegn.getSource().toString());
				ps.setString(25, marriageRegn.getStateId());
				ps.setBoolean(26, marriageRegn.getIsActive());
				ps.setString(27, marriageRegn.getTenantId());
				ps.setString(28, marriageRegn.getFee().getId());
				ps.setString(29, marriageRegn.getDemands().get(0).getId());
				ps.setLong(30, marriageRegn.getApprovalDetails().getDepartment());
				ps.setLong(31, marriageRegn.getApprovalDetails().getDesignation());
				ps.setLong(32, marriageRegn.getApprovalDetails().getAssignee());
				ps.setString(33, marriageRegn.getApprovalDetails().getAction());
				ps.setString(34, marriageRegn.getApprovalDetails().getStatus());
				ps.setString(35, marriageRegn.getApprovalDetails().getComments());
				ps.setString(36,marriageRegn.getAuditDetails().getCreatedBy());
				ps.setString(37, marriageRegn.getAuditDetails().getLastModifiedBy());
				ps.setLong(38,marriageRegn.getAuditDetails().getCreatedTime());
				ps.setLong(39, marriageRegn.getAuditDetails().getLastModifiedTime());
				
				
			}
		});
		
		if (marriageRegn.getWitnesses() != null)
			marriageRegn.getWitnesses().forEach(witness -> {
				witnessRepository.save(marriageRegn.getApplicationNumber(), marriageRegn.getTenantId(), witness);
			});
	}

	/*public void save(MarriageRegn marriageRegn) {
		Object[] obj = new Object[] { marriageRegn.getRegnUnit().getId(), marriageRegn.getMarriageDate(),
				marriageRegn.getVenue().toString(), marriageRegn.getStreet(), marriageRegn.getPlaceOfMarriage(),
				marriageRegn.getLocality(), marriageRegn.getCity(), marriageRegn.getMarriagePhoto(),
				marriageRegn.getBridegroom().getId(), marriageRegn.getBride().getId(),
				marriageRegn.getPriest().getName(), marriageRegn.getPriest().getReligion(),
				marriageRegn.getPriest().getAddress(), marriageRegn.getPriest().getAadhaar(),
				marriageRegn.getPriest().getMobileNo(), marriageRegn.getPriest().getEmail(), marriageRegn.getSerialNo(),
				marriageRegn.getVolumeNo(), marriageRegn.getApplicationNumber(), marriageRegn.getRegnNumber(),
				marriageRegn.getRegnDate(), marriageRegn.getStatus().toString(), marriageRegn.getSource().toString(),
				marriageRegn.getStateId(), marriageRegn.getIsActive(),
				marriageRegn.getApprovalDetails().getDepartment(), marriageRegn.getApprovalDetails().getDesignation(),
				marriageRegn.getApprovalDetails().getAssignee(), marriageRegn.getApprovalDetails().getAction(),
				marriageRegn.getApprovalDetails().getStatus(), marriageRegn.getApprovalDetails().getComments(),
				marriageRegn.getAuditDetails().getCreatedBy(), marriageRegn.getAuditDetails().getCreatedTime(),
				marriageRegn.getAuditDetails().getLastModifiedBy(),
				marriageRegn.getAuditDetails().getLastModifiedTime(), marriageRegn.getTenantId(),
				marriageRegn.getFee().getId(), marriageRegn.getDemands().get(0).getId()};
		System.err.println("INSERT_MARRIAGE_REGN_QUERY" + INSERT_MARRIAGE_REGN_QUERY);
		jdbcTemplate.update(INSERT_MARRIAGE_REGN_QUERY, obj);

		System.out.println("marriageRegn.getWitnesses()-------------------->" + marriageRegn.getWitnesses());
		if (marriageRegn.getWitnesses() != null)
			marriageRegn.getWitnesses().forEach(witness -> {
				witnessRepository.save(marriageRegn.getApplicationNumber(), marriageRegn.getTenantId(), witness);
			});
	}*/

	public String generateApplicationNumber() {
		String applicationnumber = jdbcTemplate
				.queryForObject("SELECT nextval('seq_egmr_marriageregn_application_number')", String.class);

		System.out.println("applicationnumber-------------------->" + applicationnumber);
		return applicationnumber;
	}

	public void update(MarriageRegn marriageRegn) {
		
		jdbcTemplate.update(UPDATE_MARRIAGE_REGN_QUERY, new PreparedStatementSetter(){

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setLong(1, marriageRegn.getRegnUnit().getId());
				ps.setLong(2, marriageRegn.getMarriageDate());
				ps.setString(3, marriageRegn.getVenue().toString());
				ps.setString(4, marriageRegn.getStreet());
				ps.setString(5, marriageRegn.getPlaceOfMarriage());
				ps.setString(6, marriageRegn.getLocality());
				ps.setString(7, marriageRegn.getCity());
				ps.setString(8, marriageRegn.getMarriagePhoto());
				ps.setLong(9, marriageRegn.getBridegroom().getId());
				ps.setLong(10, marriageRegn.getBride().getId());
				ps.setString(11, marriageRegn.getFee().getId());
				ps.setString(12, marriageRegn.getDemands().get(0).getId());
				ps.setString(13, marriageRegn.getPriest().getName());
				ps.setLong(14, marriageRegn.getPriest().getReligion());
				ps.setString(15, marriageRegn.getPriest().getAddress());
				ps.setString(16, marriageRegn.getPriest().getAadhaar());
				ps.setString(17, marriageRegn.getPriest().getMobileNo());
				ps.setString(18, marriageRegn.getPriest().getEmail());
				ps.setString(19, marriageRegn.getSerialNo());
				ps.setString(20, marriageRegn.getVolumeNo());
				ps.setString(21, marriageRegn.getRegnNumber());
				ps.setLong(22, marriageRegn.getRegnDate());
				ps.setString(23, marriageRegn.getStatus().toString());
				ps.setString(24, marriageRegn.getSource().toString());
				ps.setString(25, marriageRegn.getStateId());
				ps.setBoolean(26, marriageRegn.getIsActive());
				ps.setLong(27, marriageRegn.getApprovalDetails().getDepartment());
				ps.setLong(28, marriageRegn.getApprovalDetails().getDesignation());
				ps.setLong(29, marriageRegn.getApprovalDetails().getAssignee());
				ps.setString(30, marriageRegn.getApprovalDetails().getAction());
				ps.setString(31, marriageRegn.getApprovalDetails().getStatus());
				ps.setString(32, marriageRegn.getApprovalDetails().getComments());
				ps.setString(33, marriageRegn.getAuditDetails().getLastModifiedBy());
				ps.setLong(34, marriageRegn.getAuditDetails().getLastModifiedTime());
				ps.setString(35, marriageRegn.getTenantId());
				ps.setString(36, marriageRegn.getApplicationNumber());
			}
		});
		
		if (marriageRegn.getWitnesses() != null)
			marriageRegn.getWitnesses().forEach(witness -> {
				witnessRepository.update(marriageRegn.getApplicationNumber(), marriageRegn.getTenantId(), witness);
			});
	}

	public void saveJson(String applicationNumber, String marriageRegnJson, String tenantId) {
		jdbcTemplate.update(INSERT_JSON_QUERY, applicationNumber, marriageRegnJson, tenantId);
	}
}
