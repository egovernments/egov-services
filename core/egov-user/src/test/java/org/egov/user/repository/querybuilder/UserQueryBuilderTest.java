package org.egov.user.repository.querybuilder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.user.TestConfiguration;
import org.egov.user.model.UserSearchCriteria;
import org.egov.user.utils.UserConfigurationUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@WebMvcTest(UserQueryBuilder.class)
@Import(TestConfiguration.class)
public class UserQueryBuilderTest {

	@MockBean
	private UserConfigurationUtil userConfigurationUtil;
	
	@InjectMocks
	private UserQueryBuilder userQueryBuilder;
	
	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}
	private String query = "select usr.id AS usr_id, usr.tenantid AS usr_tenantid, usr.title,"
			+ " usr.salutation AS usr_salutation, usr.dob AS usr_dob, usr.locale AS usr_locale,"
			+ " usr.username AS usr_username, usr.password AS usr_password, usr.pwdexpirydate AS usr_pwdexpirydate,"
			+ " usr.mobilenumber AS usr_mobilenumber, usr.altcontactnumber AS usr_altcontactnumber,"
			+ " usr.emailid AS usr_emailid, usr.createddate AS usr_createddate, usr.lastmodifieddate AS usr_lastmodifieddate,"
			+ " usr.createdby AS usr_createdby, usr.lastmodifiedby AS usr_lastmodifiedby, usr.active AS usr_active,"
			+ " usr.name AS usr_name, usr.gender AS usr_gender, usr.pan AS usr_pan, usr.aadhaarnumber AS usr_aadhaarnumber,"
			+ " usr.type AS usr_type, usr.version AS usr_version, usr.guardian AS usr_guardian, usr.guardianrelation AS usr_guardianrelation,"
			+ " usr.signature AS usr_signature, usr.accountlocked AS usr_accountlocked, usr.bloodgroup AS usr_bloodgroup,"
			+ " usr.photo AS usr_photo, usr.identificationmark AS usr_identificationmark,"
			+ " address.id AS address_id, address.version AS address_version, address.createddate AS address_createddate,"
			+ " address.lastmodifieddate AS address_lastmodifieddate, address.createdby AS address_createdby,"
			+ " address.lastmodifiedby AS address_lastmodifiedby, address.type AS address_type,"
			+ " address.address AS address_address, address.city AS address_city, address.pincode AS address_pincode,"
			+ " address.userid AS address_userid, address.tenantid AS address_tenantid,"
			+ " role.name AS role_name, role.code AS role_code, role.description AS role_description,"
			+ " role.createddate AS role_createddate, role.createdby AS role_createdby, role.lastmodifiedby AS role_lastmodifiedby,"
			+ " role.lastmodifieddate AS role_lastmodifieddate, role.version AS role_version, role.tenantid AS role_tenantid, role.id AS role_id, "
			+ " utenanant.id as utenanant_id, utenanant.tenantids as utenanant_tenantids"
			+ " from eg_user usr"
			+ " LEFT OUTER JOIN eg_user_address address ON usr.id = address.userid ANd usr.tenantid = address.tenantid"
			+ " INNER JOIN eg_userrole urole ON usr.id = urole.userid AND usr.tenantid = urole.tenantid"
			+ " INNER JOIN eg_role role ON urole.roleid = role.id AND urole.tenantid = role.tenantid"
			+ " LEFT OUTER JOIN eg_usertenantrole utenanant ON usr.id = utenanant.userid"
			+ " WHERE usr.tenantid = ? ORDER BY usr.name ASC LIMIT ? OFFSET ?";
	
	@Test
	public void testGetQuery() {
		List<Object> preparedStatementValues = new ArrayList<>();
		UserSearchCriteria userSearchCriteria=UserSearchCriteria.builder().tenantId("default").build();
		
		Mockito.doReturn(500).when(userConfigurationUtil).getPageSize();
		
		assertEquals(query,
				userQueryBuilder.getQuery(userSearchCriteria, preparedStatementValues));
		
		List<Object> expectedPreparedStatementValues = new ArrayList<>();
		expectedPreparedStatementValues.add("default");
		expectedPreparedStatementValues.add(Long.valueOf("500"));
		expectedPreparedStatementValues.add(Long.valueOf("0"));
		
		assertTrue(preparedStatementValues.equals(expectedPreparedStatementValues));
	}

}
