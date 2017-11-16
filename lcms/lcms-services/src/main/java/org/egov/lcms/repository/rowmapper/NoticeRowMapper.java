package org.egov.lcms.repository.rowmapper;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.egov.lcms.enums.NoticeType;
import org.egov.lcms.models.AuditDetails;
import org.egov.lcms.models.Notice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class NoticeRowMapper implements RowMapper<Notice>{
	
	@Autowired
	private ObjectMapper objectMapper;
	@Override
	public Notice mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		Notice notice = new Notice();
		notice.setCode(rs.getString("code"));
		notice.setTenantId(rs.getString("tenantId"));
		notice.setExhibitNo(rs.getString("exhibitNo"));
		notice.setCourtName(rs.getString("courtName"));
		notice.setCourtAddress(rs.getString("courtAddress"));
		notice.setApplicant(rs.getString("applicant"));
		notice.setDefendant(rs.getString("defendant"));
		notice.setChiefOfficerDetails(rs.getString("chiefOfficerDetails"));
		notice.setAdvocateName(rs.getString("advocateName"));
		notice.setDay(rs.getString("day"));
		notice.setNoticeType(NoticeType.fromValue(rs.getString("noticeType")));
		notice.setFileStoreId(rs.getString("fileStoreId"));
		notice.setCaseNo(rs.getString("caseNo"));
		notice.setCaseCode(rs.getString("caseCode"));
		notice.setCaseRefernceNo(rs.getString("caseRefernceNo"));
		notice.setSummonReferenceNo(rs.getString("summonReferenceNo"));
		
		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy(rs.getString("createdBy"));
		auditDetails.setLastModifiedBy(rs.getString("lastModifiedBy"));
		auditDetails.setCreatedTime(rs.getBigDecimal("createdTime"));
		auditDetails.setLastModifiedTime(rs.getBigDecimal("lastModifiedTime"));
		notice.setAuditDetails(auditDetails);
		
		if (rs.getString("witness") != null) {
			List<String> witness = new ArrayList<String>();
			TypeReference<List<String>> witnessRefType = new TypeReference<List<String>>() {
			};
			try {
				witness = objectMapper.readValue(getString(rs.getString("witness")), witnessRefType);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			notice.setWitness(witness);
		}
		
		return notice;
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
