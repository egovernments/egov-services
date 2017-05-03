package org.egov.lams.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.lams.contract.AgreementRequest;
import org.egov.lams.model.Agreement;
import org.egov.lams.querybuilder.AgreementQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Repository
@EnableTransactionManagement
public class AgreementDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public static final Logger LOGGER = LoggerFactory.getLogger(AgreementDao.class);

	public void saveAgreement(AgreementRequest agreementRequest) {
		
		Agreement agreement = agreementRequest.getAgreement();

		LOGGER.info("AgreementDao agreement::" + agreement);
		
		String agreementIdQuery = "select nextval('seq_eglams_agreement')";
		try {
			agreement.setId(jdbcTemplate.queryForObject(agreementIdQuery,Long.class));
		} catch (DataAccessException ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex.getMessage());
		}

		String agreementinsert = AgreementQueryBuilder.insertAgreementQuery();
		
		Long rentIncrement = null;
		if(agreement.getRentIncrementMethod() !=null)
			rentIncrement = agreement.getRentIncrementMethod().getId();
		
		Object[] obj = new Object[] { agreement.getId(), agreement.getAgreementDate(), agreement.getAgreementNumber(),
				agreement.getBankGuaranteeAmount(), agreement.getBankGuaranteeDate(), agreement.getCaseNo(),
				agreement.getCommencementDate(), agreement.getCouncilDate(), agreement.getCouncilNumber(),
				agreement.getExpiryDate(), agreement.getNatureOfAllotment().toString(), agreement.getOrderDate(),
				agreement.getOrderDetails(), agreement.getOrderNo(), agreement.getPaymentCycle().toString(),
				agreement.getRegistrationFee(), agreement.getRemarks(), agreement.getRent(), agreement.getRrReadingNo(),
				agreement.getSecurityDeposit(), agreement.getSecurityDepositDate(),
				agreement.getSolvencyCertificateDate(), agreement.getSolvencyCertificateNo(),
				agreement.getStatus().toString(), agreement.getTinNumber(), agreement.getTenderDate(),
				agreement.getTenderNumber(), agreement.getTradelicenseNumber(), agreement.getTenantId(),
				agreement.getTenantId(), new Date(), new Date(), agreement.getAllottee().getId(),
				agreement.getAsset().getId(), rentIncrement, agreement.getAcknowledgementNumber(),
				agreement.getStateId(), agreement.getTenantId(), agreement.getGoodWillAmount(),
				agreement.getTimePeriod(), agreement.getCollectedSecurityDeposit(),
				agreement.getCollectedGoodWillAmount(), agreement.getSource().toString() };

		try {
			jdbcTemplate.update(agreementinsert, obj);
		} catch (DataAccessException ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex.getMessage());
		}
		
		String sql ="INSERT INTO eglams_demand values ( nextval('seq_eglams_demand'),?,?,?)";
		List<Object[]> batchArgs = new ArrayList<>();
		List<String> demands = agreement.getDemands();
		int demandsCount = demands.size();
		for(int i=0;i<demandsCount;i++){
			Object[] demandRecord = {agreement.getTenantId(),agreement.getId(),demands.get(i)};
			batchArgs.add(demandRecord);
		}
		try {
			jdbcTemplate.batchUpdate(sql, batchArgs);
		} catch (DataAccessException ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex.getMessage());
		}
	}

	public void updateAgreement(AgreementRequest agreementRequest) {

		Agreement agreement = agreementRequest.getAgreement();
		LOGGER.info("AgreementDao agreement::" + agreement);

		String agreementUpdate = AgreementQueryBuilder.updateAgreementQuery();

		Object[] obj = new Object[] { agreement.getId(),agreement.getAgreementDate(), agreement.getAgreementNumber(),
				agreement.getBankGuaranteeAmount(), agreement.getBankGuaranteeDate(), agreement.getCaseNo(),
				agreement.getCommencementDate(), agreement.getCouncilDate(), agreement.getCouncilNumber(),
				agreement.getExpiryDate(), agreement.getNatureOfAllotment().toString(), agreement.getOrderDate(),
				agreement.getOrderDetails(), agreement.getOrderNo(), agreement.getPaymentCycle().toString(),
				agreement.getRegistrationFee(), agreement.getRemarks(), agreement.getRent(), agreement.getRrReadingNo(),
				agreement.getSecurityDeposit(), agreement.getSecurityDepositDate(),
				agreement.getSolvencyCertificateDate(), agreement.getSolvencyCertificateNo(),
				agreement.getStatus().toString(), agreement.getTinNumber(), agreement.getTenderDate(),
				agreement.getTenderNumber(), agreement.getTradelicenseNumber(), 1, null, new Date(), null,
				agreement.getAllottee().getId(), agreement.getAsset().getId(),
				agreement.getRentIncrementMethod().getId(), agreement.getAcknowledgementNumber(),
				agreement.getStateId(), agreement.getTenantId(),
				
				agreement.getAcknowledgementNumber(),agreement.getTenantId()};

		try {
			jdbcTemplate.update(agreementUpdate, obj);
		} catch (DataAccessException ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex.getMessage());
		}
		
		/*if (agreement.getAgreementNumber() != null) {
			String sql = "UPDATE eglams_demand SET agreementnumber=" + agreement.getId()
					+ " WHERE demandid=?";
			List<Object[]> batchArgs = new ArrayList<>();
			List<String> demands = agreement.getDemands();
			int demandsCount = demands.size();
			for (int i = 0; i < demandsCount; i++) {
				Object[] demandRecord = { demands.get(i) };
				batchArgs.add(demandRecord);
			}
			try {
				jdbcTemplate.batchUpdate(sql, batchArgs);
			} catch (DataAccessException ex) {
				ex.printStackTrace();
				throw new RuntimeException(ex.getMessage());
			}
		}*/
		//FIXME do we need to update demand 
	}
}
