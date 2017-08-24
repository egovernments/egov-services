package org.egov.mr.repository.rowmapper;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

import java.sql.ResultSet;

import org.egov.mr.model.MarriageDocumentType;
import org.egov.mr.model.enums.ApplicationType;
import org.egov.mr.model.enums.DocumentProof;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MarriageDocumentTypeRowMapperTest {

	@Mock
	ResultSet rs;

	@InjectMocks
	private MarriageDocumentTypeRowMapper marriageDocumenttypeRowMapper;

	@Test
	public void test() throws Exception {
		Mockito.when(rs.next()).thenReturn(true);
		when(rs.getLong("id")).thenReturn(Long.valueOf("6"));
		when(rs.getString("name")).thenReturn("MarriageCertificate");
		when(rs.getString("code")).thenReturn("00015");
		when(rs.getBoolean("isactive")).thenReturn(true);
		when(rs.getBoolean("isindividual")).thenReturn(true);
		when(rs.getBoolean("isrequired")).thenReturn(true);
		when(rs.getString("proof")).thenReturn(DocumentProof.ADDRESS_PROOF.toString());
		when(rs.getString("appltype")).thenReturn(ApplicationType.REGISTRATION.toString());
		when(rs.getString("tenantid")).thenReturn("ap.kurnool");

		MarriageDocumentType marriageDocumentType = marriageDocumenttypeRowMapper.mapRow(rs, 1);
		assertEquals(getMarriageDocumentType(), marriageDocumentType);
	}

	private MarriageDocumentType getMarriageDocumentType() {
		MarriageDocumentType marriageDocumentType = new MarriageDocumentType();
		marriageDocumentType.setApplicationType(ApplicationType.REGISTRATION);
		marriageDocumentType.setCode("00015");
		marriageDocumentType.setId(Long.valueOf("6"));
		marriageDocumentType.setIsActive(true);
		marriageDocumentType.setIsIndividual(true);
		marriageDocumentType.setIsRequired(true);
		marriageDocumentType.setName("MarriageCertificate");
		marriageDocumentType.setProof(DocumentProof.ADDRESS_PROOF);
		marriageDocumentType.setTenantId("ap.kurnool");
		return marriageDocumentType;
	}
}
