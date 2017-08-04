package org.egov.mr.repository;

import java.util.ArrayList;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.egov.mr.model.MarriageRegn;
import org.egov.mr.model.ReissueCertAppl;
import org.egov.mr.model.enums.CertificateType;
import org.egov.mr.repository.querybuilder.MarriageCertQueryBuilder;
import org.egov.mr.repository.rowmapper.MarriageCertRowMapper;
import org.egov.mr.service.CertificateNumberService;
import org.egov.mr.web.contract.MarriageCertCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class MarriageCertRepository {

	public static final String CERTIFICATE_INSERT_QUERY = "INSERT INTO egmr_marriage_certificate("
			+ " certificateno, certificatedate, certificatetype, regnnumber, bridegroomphoto,"
			+ " bridephoto, husbandname, husbandaddress, wifename, wifeaddress, marriagedate, marriagevenueaddress, regndate,"
			+ " regnserialno, regnvolumeno,"
			// + " certificateplace, templateversion,"
			+ " applicationnumber, tenantid)" + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	public static final String GET_APPLICATION_NO_QUERY = "SELECT applicationnumber FROM egmr_marriage_certificate"
			+ " WHERE tenantid = ?";

	@Autowired
	private CertificateNumberService certificateNumberService;

	@Autowired
	private MarriageCertQueryBuilder marriageCertQueryBuilder;

	@Autowired
	private MarriageCertRowMapper marriageCertRowMapper;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void insert(MarriageRegn marriageRegn) {
		String certificateNo = certificateNumberService.generateCertificateNumber();
		Object[] obj = new Object[] { certificateNo, new Date().getTime(), CertificateType.REGISTRATION.toString(),
				marriageRegn.getRegnNumber(), marriageRegn.getBridegroom().getPhoto(),
				marriageRegn.getBride().getPhoto(), marriageRegn.getBridegroom().getName(),
				marriageRegn.getBridegroom().getResidenceAddress(), marriageRegn.getBride().getName(),
				marriageRegn.getBride().getResidenceAddress(), marriageRegn.getMarriageDate(),
				marriageRegn.getPlaceOfMarriage(), marriageRegn.getRegnDate(), marriageRegn.getSerialNo(),
				marriageRegn.getVolumeNo(),
				// marriageRegn.getCertificatePlace(),
				// marriageRegn.getTemplateVersion(),
				marriageRegn.getApplicationNumber(), marriageRegn.getTenantId() };
		jdbcTemplate.update(CERTIFICATE_INSERT_QUERY, obj);
	}

	public List<ReissueCertAppl> findForCriteria(MarriageCertCriteria marriageCertCriteria) {
		log.info("MarriageCertRepository  marriageCertCriteria:::" + marriageCertCriteria.getTenantId());
		List<Object> preparedStatementValues = new ArrayList<Object>();
		log.info("MarriageCertRepository  preparedStatementValues:::" + preparedStatementValues);
		String queryStr = marriageCertQueryBuilder.getQuery(marriageCertCriteria, preparedStatementValues);
		log.info("queryStr;;"+queryStr);
		List<ReissueCertAppl> marriageCert = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(),
				marriageCertRowMapper);
		return marriageCert;

	}

}
