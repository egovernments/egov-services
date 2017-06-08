package org.egov.mr.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.egov.mr.model.ApprovalDetails;
import org.egov.mr.model.MarriageCertificate;
import org.egov.mr.model.MarriageRegn;
import org.egov.mr.model.enums.CertificateType;
import org.egov.mr.model.enums.ApplicationStatus;
import org.egov.mr.model.enums.Source;
import org.egov.mr.model.enums.Venue;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

@Component
public class MarriageCertRowMapper implements ResultSetExtractor<List<MarriageCertificate>> {

	@Override
	public List<MarriageCertificate> extractData(ResultSet rs) throws SQLException, DataAccessException {
		Map<String, MarriageCertificate> marriageCertMap = getMarriageCertMap(rs);
		List<MarriageCertificate> marriageCertList = getMarriageCertList(marriageCertMap);
		return marriageCertList;
	}

	private  Map<String, MarriageCertificate> getMarriageCertMap(ResultSet rs) throws SQLException {
		Map<String, MarriageCertificate> marriageCertMap = new LinkedHashMap();
		while (rs.next()) {
			String marriageCertId = (String) rs.getObject("applicationnumber");
			MarriageCertificate marriageCert = marriageCertMap.get(marriageCertId);
			ApprovalDetails approvalDetailsInfo = new ApprovalDetails();

			if (marriageCert == null) {
				marriageCert = new MarriageCertificate();
				marriageCert.setCertificateNo(rs.getString("certificateno"));
				marriageCert.setCertificateDate(rs.getLong("certificatedate"));
				marriageCert.setCertificateType(CertificateType.fromValue(rs.getString("certificatetype")));
				marriageCert.setRegnNumber(rs.getString("regnnumber"));
				marriageCert.setBridegroomPhoto(rs.getString("bridegroomphoto"));
				marriageCert.setBridePhoto(rs.getString("bridephoto"));
				marriageCert.setHusbandName(rs.getString("husbandname"));
				marriageCert.setHusbandAddress(rs.getString("husbandaddress"));
				marriageCert.setWifeName(rs.getString("wifename"));
				marriageCert.setWifeAddress(rs.getString("wifeaddress"));
				marriageCert.setMarriageDate(rs.getLong("marriagedate"));
				marriageCert.setMarriageVenueAddress(rs.getString("marriagevenueaddress"));
				marriageCert.setRegnDate(rs.getLong("regndate"));
				marriageCert.setRegnSerialNo(rs.getString("regnserialno"));
				marriageCert.setRegnVolumeNo(rs.getString("regnvolumeno"));
				marriageCert.setCertificatePlace(rs.getString("certificateplace"));
				marriageCert.setTemplateVersion(rs.getString("templateversion"));
				marriageCert.setApplicationNumber(rs.getString("applicationnumber"));
				
				approvalDetailsInfo.setDepartment(rs.getLong("approvaldepartment"));
				approvalDetailsInfo.setDesignation(rs.getLong("approvaldesignation"));
				approvalDetailsInfo.setAssignee(rs.getLong("approvalassignee"));
				approvalDetailsInfo.setAction(rs.getString("approvalaction"));
				approvalDetailsInfo.setStatus(rs.getString("approvalstatus"));
				approvalDetailsInfo.setComments(rs.getString("approvalcomments"));
				
				//marriageCert.setApprovalDetails(approvalDetailsInfo);
				marriageCert.setTenantId(rs.getString("tenantid"));
				marriageCertMap.put(marriageCertId, marriageCert);
			}
		}
		return marriageCertMap;
	}

	private List<MarriageCertificate> getMarriageCertList(Map<String, MarriageCertificate> marriageCertMap) {
		List<MarriageCertificate> marriageCertList = new ArrayList();
		for (Map.Entry<String, MarriageCertificate> marriageCertEntry : marriageCertMap.entrySet()) {
			MarriageCertificate marriageCert = marriageCertEntry.getValue();
			marriageCertList.add(marriageCert);
		}
		return marriageCertList;
	}
}
