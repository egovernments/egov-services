package org.egov.mr.repository.rowmapper;

import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import java.sql.ResultSet;

import org.egov.mr.model.AuditDetails;
import org.egov.mr.model.Location;
import org.egov.mr.model.RegistrationUnit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RegistrationUnitRowMapperTest {

	@Mock
	ResultSet rs;

	@InjectMocks
	private RegistrationUnitRowMapper registrationUnitRowMapper;

	@Test
	public void test() throws Exception {

		Mockito.when(rs.next()).thenReturn(true);

		when(rs.getString("doorno")).thenReturn("11");
		when(rs.getLong("electionward")).thenReturn(Long.valueOf("168"));
		when(rs.getInt("pincode")).thenReturn(Integer.valueOf("186"));
		when(rs.getLong("revenueward")).thenReturn(Long.valueOf("816"));
		when(rs.getLong("street")).thenReturn(Long.valueOf("861"));
		when(rs.getLong("block")).thenReturn(Long.valueOf("618"));
		when(rs.getLong("locality")).thenReturn(Long.valueOf("681"));
		when(rs.getLong("zone")).thenReturn(Long.valueOf("0168"));

		when(rs.getString("createdBy")).thenReturn("user2");
		when(rs.getString("lastModifiedBy")).thenReturn("user6");
		when(rs.getLong("createdTime")).thenReturn(Long.valueOf("123654789"));
		when(rs.getLong("lastModifiedTime")).thenReturn(Long.valueOf("123654789"));

		when(rs.getLong("id")).thenReturn(Long.valueOf("6"));
		when(rs.getString("name")).thenReturn("Bangalore");
		when(rs.getBoolean("isactive")).thenReturn(true);

		when(rs.getString("tenantid")).thenReturn("ap.kurnool");

		RegistrationUnit registrationUnit = registrationUnitRowMapper.mapRow(rs, 1);
		assertEquals(getRegistrationUnit(), registrationUnit);
	}

	private RegistrationUnit getRegistrationUnit() {
		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("user2");
		auditDetails.setCreatedTime(Long.valueOf("123654789"));
		auditDetails.setLastModifiedBy("user6");
		auditDetails.setLastModifiedTime(Long.valueOf("123654789"));

		Location location = new Location();
		location.setDoorNo("11");
		location.setBlock(Long.valueOf("618"));
		location.setElectionWard(Long.valueOf("168"));
		location.setLocality(Long.valueOf("681"));
		location.setPinCode(Integer.valueOf("186"));
		location.setRevenueWard(Long.valueOf("816"));
		location.setStreet(Long.valueOf("861"));
		location.setZone(Long.valueOf("168"));

		RegistrationUnit registrationUnit = new RegistrationUnit();
		registrationUnit.setId(Long.valueOf("6"));
		registrationUnit.setIsActive(true);
		registrationUnit.setName("Bangalore");
		registrationUnit.setTenantId("ap.kurnool");
		registrationUnit.setIsMainRegistrationUnit(false);
		registrationUnit.setAuditDetails(auditDetails);
		registrationUnit.setAddress(location);

		return registrationUnit;
	}
}
