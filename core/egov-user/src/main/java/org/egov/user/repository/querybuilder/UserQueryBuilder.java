package org.egov.user.repository.querybuilder;

import java.util.List;
import java.util.Set;

import org.egov.user.model.UserSearchCriteria;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserQueryBuilder {
	
	@Value("${egov.default.page.size}")
	private long pageSize;
	
	public final String USER_INSERT_QUERY = "INSERT INTO eg_user(title, salutation, dob, locale, username,"
			+ " password, pwdexpirydate, mobilenumber, altcontactnumber, emailid, createddate, lastmodifieddate,"
			+ " createdby, lastmodifiedby, active, name, gender, pan, aadhaarnumber, type, version,"
			+ " guardian, guardianrelation, signature, accountlocked, bloodgroup, photo, identificationmark,"
			+ " tenantid, id)"
			+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
	
	public final String USER_ADDRESS_INSERT_QUERY = "INSERT INTO eg_user_address(id, version, createddate, lastmodifieddate,"
			+ " createdby, lastmodifiedby, type, address, city, pincode, userid, tenantid)"
			+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

	public final String USERROLE_INSERT_QUERY = "INSERT INTO eg_userrole(roleid, roleidtenantid, userid, tenantid, lastmodifieddate)"
			+ " VALUES (?, ?, ?, ?, ?);";
	
    public final String USER_TENANT_INSERT_QUERY = "INSERT INTO eg_usertenantrole(id, userid, tenantids, "
    		+ "createdby, createddate,lastmodifiedby, lastmodifieddate)VALUES (?, ?, ?, ?, ?, ?, ?);";
    
    public final String USER_UPDATE_QUERY = "UPDATE eg_user SET title=?, salutation=?, dob=?, locale=?, "
    		+ "username=?, password=?, pwdexpirydate=?, mobilenumber=?, altcontactnumber=?, emailid=?, "
    		+ "lastmodifieddate=?,lastmodifiedby=?, active=?, name=?, "
    		+ "gender=?, pan=?, aadhaarnumber=?, type=?, version=?, guardian=?, guardianrelation=?, "
    		+ "signature=?, accountlocked=?, bloodgroup=?, photo=?, identificationmark=? "
    		+ "WHERE id=? and tenantid=?;";
    
    public final String USER_ADDRESS_UPDATE_QUERY = "UPDATE eg_user_address SET "
    		+ "version=?, lastmodifieddate=?, "
    		+ "lastmodifiedby=?, type=?, address=?, city=?, "
    		+ "pincode=? WHERE id=? and tenantid=?;";
    
    public final String USER_TENANT_UPDATE_QUERY = "UPDATE eg_usertenantrole SET "
    		+ "tenantids=?, lastmodifiedby=?, lastmodifieddate=? WHERE id=?;";
    
    public final String USERROLE_UPDATE_QUERY = "UPDATE eg_userrole SET roleid=?, roleidtenantid=?, "
    		+ "lastmodifieddate=? WHERE userid=? and tenantid=?;";
	
	private final String BASE_QUERY="select usr.id AS usr_id, usr.tenantid AS usr_tenantid, usr.title,"
			+ " usr.salutation AS usr_salutation, usr.dob AS usr_dob, usr.locale AS usr_locale,"
			+ " usr.username AS usr_username, usr.password AS usr_password, usr.pwdexpirydate AS usr_pwdexpirydate,"
			+ " usr.mobilenumber AS usr_mobilenumber, usr.altcontactnumber AS usr_altcontactnumber,"
			+ " usr.emailid AS usr_emailid, usr.createddate AS usr_createddate, usr.lastmodifieddate AS usr_lastmodifieddate,"
			+ " usr.createdby AS usr_createdby, usr.lastmodifiedby AS usr_lastmodifiedby, usr.active AS usr_active,"
			+ " usr.name AS usr_name, usr.gender AS usr_gender, usr.pan AS usr_pan, usr.aadhaarnumber AS usr_aadhaarnumber,"
			+ " usr.type AS usr_type, usr.version AS usr_version, usr.guardian AS usr_guardian,"
			+ " usr.guardianrelation AS usr_guardianrelation, usr.signature AS usr_signature, usr.accountlocked AS usr_accountlocked,"
			+ " usr.bloodgroup AS usr_bloodgroup, usr.photo AS usr_photo, usr.identificationmark AS usr_identificationmark,"
			+ " address.id AS address_id, address.version AS address_version, address.createddate AS address_createddate,"
			+ " address.lastmodifieddate AS address_lastmodifieddate, address.createdby AS address_createdby,"
			+ " address.lastmodifiedby AS address_lastmodifiedby, address.type AS address_type, address.address AS address_address,"
			+ " address.city AS address_city, address.pincode AS address_pincode, address.userid AS address_userid, address.tenantid AS address_tenantid,"
			+ " role.name AS role_name, role.code AS role_code, role.description AS role_description, role.createddate AS role_createddate,"
			+ " role.createdby AS role_createdby, role.lastmodifiedby AS role_lastmodifiedby, role.lastmodifieddate AS role_lastmodifieddate,"
			+ " role.version AS role_version, role.tenantid AS role_tenantid, role.id AS role_id, "
			+ " utenanant.id as utenanant_id, utenanant.tenantids as utenanant_tenantids"
			+ " from eg_user usr"
			+ " LEFT OUTER JOIN eg_user_address address ON usr.id = address.userid ANd usr.tenantid = address.tenantid"
			+ " INNER JOIN eg_userrole urole ON usr.id = urole.userid AND usr.tenantid = urole.tenantid"
			+ " INNER JOIN eg_role role ON urole.roleid = role.id AND urole.tenantid = role.tenantid"
			+ " LEFT OUTER JOIN eg_usertenantrole utenanant ON usr.id = utenanant.userid"
			+ " WHERE usr.tenantid = ?";
	
	public String getQuery(UserSearchCriteria userSearchCriteria,  List<Object> preparedStatementValues){
		
		final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);
		addWhereClause(selectQuery,preparedStatementValues,userSearchCriteria);
		addPagingClause(selectQuery, preparedStatementValues, userSearchCriteria);
		
		return selectQuery.toString();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addWhereClause(StringBuilder selectQuery,List preparedStatementValues,UserSearchCriteria userSearchCriteria){
		
		preparedStatementValues.add(userSearchCriteria.getTenantId());
		
		if(userSearchCriteria.getActive() == null && userSearchCriteria.getIncludeDetails() == null
				&& userSearchCriteria.getId() == null && userSearchCriteria.getLastChangedSince() == null
				&& userSearchCriteria.getRoleCodes() == null && userSearchCriteria.getType() == null 
				&& userSearchCriteria.getUserName() == null)
		 return;
		
		if(userSearchCriteria.getLastChangedSince() != null){
			selectQuery.append(" AND usr.lastmodifiedby <= ? ");
			preparedStatementValues.add(userSearchCriteria.getLastChangedSince());
		}
		
		if(userSearchCriteria.getUserName() != null){
			selectQuery.append(" AND usr.username = ? ");
			preparedStatementValues.add(userSearchCriteria.getUserName());
		}
		
		if(userSearchCriteria.getActive() != null){
			selectQuery.append(" AND usr.active = ? ");
			preparedStatementValues.add(userSearchCriteria.getActive());
		}
		
		if(userSearchCriteria.getId() != null && !userSearchCriteria.getId().isEmpty()){
			selectQuery.append(" AND usr.id IN (" + getIdQuery(userSearchCriteria.getId()));
		}else if(userSearchCriteria.getRoleCodes() != null && !userSearchCriteria.getRoleCodes().isEmpty()){
			selectQuery.append(" AND role.code IN (" + getCodeQuery(userSearchCriteria.getRoleCodes()));
		}
		
		if(userSearchCriteria.getType() != null){
			selectQuery.append(" AND usr.type = ? ");
			preparedStatementValues.add(userSearchCriteria.getType().toString());
		}
		/*if(searchCriteria.getIncludeDetails() != null){
			selectQuery.append(" AND ");
			preparedStatementValues.add(searchCriteria.getIncludeDetails());
		}*/
		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addPagingClause(final StringBuilder selectQuery, final List preparedStatementValues,
			final UserSearchCriteria userSearchCriteria) {
		if(userSearchCriteria.getSort() != null && !userSearchCriteria.getSort().isEmpty())
			selectQuery.append(" ORDER BY ");
		else 
			selectQuery.append(" ORDER BY usr.name ASC");

		selectQuery.append(" LIMIT ?");
//		long pageSize = 0 ;
		if (userSearchCriteria.getPageSize() != null)
			pageSize = userSearchCriteria.getPageSize();
		preparedStatementValues.add(pageSize); // Set limit to pageSize

		// handle offset here
		selectQuery.append(" OFFSET ?");
		long pageNumber = 0; // Default pageNo is zero meaning first page
		if (userSearchCriteria.getPageNumber() != null)
			pageNumber = userSearchCriteria.getPageNumber() - 1;
		preparedStatementValues.add(pageNumber * pageSize); // Set offset to
															// pageNo * pageSize
	}
	
	private String getOrderByQuery(Set<Long> idList) {

		StringBuilder query = new StringBuilder();
		if (!idList.isEmpty()) {
			String[] list = idList.toArray(new String[idList.size()]);
			query.append(list[0]+" ASC");
			for (int i = 1; i < idList.size(); i++)
				query.append("," +list[i]+" ASC");
		}
		return query.toString();
	}
	
	private String getIdQuery(Set<Long> idList) {

		StringBuilder query = new StringBuilder();
		if (!idList.isEmpty()) {
			Object[] list = idList.toArray();
			query.append(list[0].toString());
			for (int i = 1; i < idList.size(); i++)
				query.append("," +list[i].toString());
		}
		return query.append(")").toString();
	}
	
	private String getCodeQuery(Set<String> idList) {

		StringBuilder query = new StringBuilder();
		if (!idList.isEmpty()) {
			String[] list = idList.toArray(new String[idList.size()]);
			query.append("'"+list[0]+"'");
			for (int i = 1; i < idList.size(); i++)
				query.append("," + "'"+list[i]+"'");
		}
		return query.append(")").toString();
	}

}
