package org.egov.lams.repository.rowmapper;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import java.sql.ResultSet;
import java.util.List;
import org.egov.lams.model.Agreement;
import org.egov.lams.model.Allottee;
import org.egov.lams.model.Asset;
import org.egov.lams.model.RentIncrementType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AgreementRowmapperTest {

	@Mock
	ResultSet rs;
	
	@InjectMocks
	private AgreementRowMapper agreementRowMapper ;

	@Test
	public void test_should_map_result_set_to_entity() throws Exception {
		
		Mockito.when(rs.next()).thenReturn(true).thenReturn(false);
		
		
		when(rs.getLong("id")).thenReturn(1l);
		when(rs.getString("acknowledgementnumber")).thenReturn(null);
		when(rs.getString("stateid")).thenReturn(null);
		when(rs.getDouble("goodwillamount")).thenReturn(1d);
		when(rs.getLong("timePeriod")).thenReturn(1l);
		when(rs.getTimestamp("agreement_date")).thenReturn(null);
		when(rs.getString("agreement_no")).thenReturn(null);
		when(rs.getDouble("bank_guarantee_amount")).thenReturn(1d);
		when(rs.getTimestamp("bank_guarantee_date")).thenReturn(null);
		when(rs.getString("case_no")).thenReturn(null);
		when(rs.getTimestamp("commencement_date")).thenReturn(null);
		when(rs.getTimestamp("council_date")).thenReturn(null);
		when(rs.getString("council_number")).thenReturn(null);
		when(rs.getTimestamp("expiry_date")).thenReturn(null);
		when(rs.getString("nature_of_allotment")).thenReturn(null);
		when(rs.getTimestamp("order_date")).thenReturn(null);
		when(rs.getString("order_details")).thenReturn(null);
		when(rs.getString("order_no")).thenReturn(null);
		when(rs.getString("payment_cycle")).thenReturn(null);
		when(rs.getDouble("registration_fee")).thenReturn(1d);
		when(rs.getString("remarks")).thenReturn(null);
		when(rs.getDouble("rent")).thenReturn(1d);
		when(rs.getString("rr_reading_no")).thenReturn(null);
		when(rs.getString("status")).thenReturn(null);
		when(rs.getString("tin_number")).thenReturn(null);
		when(rs.getString("tenant_id")).thenReturn(null);
		when(rs.getTimestamp("tender_date")).thenReturn(null);
		when(rs.getString("tender_number")).thenReturn(null);
		when(rs.getDouble("security_deposit")).thenReturn(1d);
		when(rs.getTimestamp("security_deposit_date")).thenReturn(null);
		when(rs.getTimestamp("solvency_certificate_date")).thenReturn(null);
		when(rs.getString("solvency_certificate_no")).thenReturn(null);
		when(rs.getString("trade_license_number")).thenReturn(null);
		when(rs.getLong("rent_increment_method")).thenReturn(1l);
		when(rs.getLong("allottee")).thenReturn(1l);
		when(rs.getLong("asset")).thenReturn(1l);
		when(rs.getString("demandid")).thenReturn(null);
		AgreementRowMapper agreementRowMapper = new AgreementRowMapper();
		
		List<Agreement> agreements = agreementRowMapper.extractData(rs);
		
		Agreement agreementGiven = getAgreement();
		assertThat(agreements.get(0).equals(agreementGiven));

	}

	private Agreement getAgreement() {
		Agreement agreement = new Agreement();
		agreement.setId(1l);
		
		Allottee allottee = new Allottee();
		allottee.setId(1l);
		agreement.setAllottee(allottee);
		
		Asset asset = new Asset();
		asset.setId(1l);
		agreement.setAsset(asset);
		
		RentIncrementType incrementType = new RentIncrementType();
		incrementType.setId(1l);
		agreement.setRentIncrementMethod(incrementType);
		
		agreement.setTimePeriod(1l);
		agreement.setGoodWillAmount(1d);
		agreement.setBankGuaranteeAmount(1d);
		agreement.setRegistrationFee(1d);
		agreement.setRent(1d);
		agreement.setSecurityDeposit(1d);
		
		return agreement;
	}

}
