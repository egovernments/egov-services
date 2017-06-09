package org.egov.property.services;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.egov.models.AuditDetails;
import org.egov.models.Department;
import org.egov.models.DepartmentRequest;
import org.egov.models.DepartmentResponseInfo;
import org.egov.models.FloorType;
import org.egov.models.FloorTypeRequest;
import org.egov.models.FloorTypeResponse;
import org.egov.models.OccuapancyMaster;
import org.egov.models.OccuapancyMasterRequest;
import org.egov.models.OccuapancyMasterResponse;
import org.egov.models.PropertyType;
import org.egov.models.PropertyTypeRequest;
import org.egov.models.PropertyTypeResponse;
import org.egov.models.RequestInfo;
import org.egov.models.ResponseInfo;
import org.egov.models.ResponseInfoFactory;
import org.egov.models.RoofType;
import org.egov.models.RoofTypeRequest;
import org.egov.models.RoofTypeResponse;
import org.egov.models.StructureClass;
import org.egov.models.StructureClassRequest;
import org.egov.models.StructureClassResponse;
import org.egov.models.WoodType;
import org.egov.models.WoodTypeRequest;
import org.egov.models.WoodTypeResponse;
import org.egov.property.exception.InvalidInputException;
import org.egov.property.exception.PropertySearchException;
import org.egov.property.model.ExcludeFileds;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Description : MasterService interface implementation class 
 * @author Narendra
 *
 */

@Service
@SuppressWarnings({"unchecked","rawtypes"})
public class MasterServiceImpl  implements Masterservice{

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired

	private ResponseInfoFactory responseInfoFactory;

	@Autowired
	private Environment environment;



	/**
	 * Description : This method will use for creating department type
	 * @param tenantId
	 * @param departmentRequest
	 * @return
	 */

	@Override
	@Transactional
	public DepartmentResponseInfo createDepartmentMaster(String tenantId, DepartmentRequest departmentRequest) {
		// TODO Auto-generated method stub

		for(Department department:departmentRequest.getDepartments()){

			Long createdTime=new Date().getTime();

			Gson gson=new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();

			String data=gson.toJson(department);

			StringBuffer depeartmentQuery=new StringBuffer();
			depeartmentQuery.append("insert into egpt_department_master(tenantId,code,data,");
			depeartmentQuery.append("createdBy, lastModifiedBy, createdTime,lastModifiedTime)");
			depeartmentQuery.append(" values(?,?,?,?,?,?,?)");


			final PreparedStatementCreator psc = new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
					final PreparedStatement ps = connection.prepareStatement(depeartmentQuery.toString(), new String[] { "id" });
					ps.setString(1, department.getTenantId());
					ps.setString(2, department.getCode());
					PGobject jsonObject = new PGobject();
					jsonObject.setType("json");
					jsonObject.setValue(data);
					ps.setObject(3,jsonObject);
					ps.setString(4, department.getAuditDetails().getCreatedBy());
					ps.setString(5, department.getAuditDetails().getLastModifiedBy());
					ps.setLong(6, createdTime);
					ps.setLong(7, createdTime);
					return ps;
				}
			};

			// The newly generated key will be saved in this object
			final KeyHolder holder = new GeneratedKeyHolder();
			jdbcTemplate.update(psc, holder);
			department.setId(Long.valueOf(holder.getKey().intValue()));

		}
		ResponseInfo responseInfo=responseInfoFactory.createResponseInfoFromRequestInfo(departmentRequest.getRequestInfo(),true);

		DepartmentResponseInfo departmentResponse=new DepartmentResponseInfo();
		departmentResponse.setDepartments(departmentRequest.getDepartments());
		departmentResponse.setResponseInfo(responseInfo);
		return departmentResponse;
	}

	@Override
	@Transactional
	public DepartmentResponseInfo updateDepartmentMaster(String tenantId, Long id, DepartmentRequest departmentRequest) {

		for(Department department:departmentRequest.getDepartments()){

			Long modifiedTime=new Date().getTime();

			Gson gson=new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();

			String data=gson.toJson(department);

			String departmentTypeUpdate = "UPDATE egpt_department_master set tenantId = ?, code = ?,data = ?, lastModifiedBy = ?, lastModifiedTime = ? where id = " +id;


			final PreparedStatementCreator psc = new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
					final PreparedStatement ps = connection.prepareStatement(departmentTypeUpdate, new String[] { "id" });
					ps.setString(1, department.getTenantId());	
					ps.setString(2,department.getCode());
					PGobject jsonObject = new PGobject();
					jsonObject.setType("json");
					jsonObject.setValue(data);
					ps.setString(3, data);
					ps.setString(4, department.getAuditDetails().getLastModifiedBy());
					ps.setLong(5, modifiedTime);
					return ps;
				}
			};
			jdbcTemplate.update(psc);

		}
		ResponseInfo responseInfo=responseInfoFactory.createResponseInfoFromRequestInfo(departmentRequest.getRequestInfo(),true);

		DepartmentResponseInfo departmentResponse=new DepartmentResponseInfo();
		departmentResponse.setDepartments(departmentRequest.getDepartments());
		departmentResponse.setResponseInfo(responseInfo);
		return departmentResponse;
	}


	@Override
	public DepartmentResponseInfo getDepartmentMaster(RequestInfo requestInfo, String tenantId, Integer[] ids, String category, String name, String code, String nameLocal, Integer pageSize, Integer offSet) {
		Gson gson=new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
		StringBuffer departmentSearchSql = new StringBuffer();

		departmentSearchSql.append("select * from egpt_department_master where tenantid ='"+tenantId+"'");

		if (ids!=null && ids.length>0){

			String  departmentIds= "";

			int count = 1;
			for (Integer id : ids){
				if (count<ids.length)
					departmentIds = departmentIds+id+",";
				else
					departmentIds = departmentIds+id;

				count++;
			}

			departmentSearchSql.append(" AND id IN ("+departmentIds+")");

		}

		StringBuffer dataSearch = new StringBuffer();

		if (code!=null && !code.isEmpty())
			departmentSearchSql.append(" AND code = '"+code+"'");

		if(name!=null || category!=null || nameLocal!=null)
			dataSearch.append(" AND data @> '");

		if (name!=null && !name.isEmpty())
			dataSearch.append("{ \"name\":\""+name+"\"");

		if (nameLocal!=null && !nameLocal.isEmpty()){
			if(name!=null && !name.isEmpty())
				dataSearch.append(" , \"nameLocal\":\""+nameLocal+"\"");
			else
				dataSearch.append("{\"nameLocal\":\""+nameLocal+"\"");	
		}
		if ( category!=null &&  !category.isEmpty()  ){
			if( nameLocal!=null && !nameLocal.isEmpty())
				dataSearch.append(" , \"category\":\""+category+"\"");
			else if( name!=null && !name.isEmpty())
				dataSearch.append(" , \"category\":\""+category+"\"");
			else
				dataSearch.append("{\"category\":\""+category+"\"");
		}	

		if(name!=null || category!=null || nameLocal!=null)
			dataSearch.append("}'");

		departmentSearchSql.append( dataSearch.toString());
		if ( pageSize == null )
			pageSize = 30;
		if ( offSet ==null)
			offSet = 0;
		departmentSearchSql.append("offset "+offSet+" limit "+pageSize);

		DepartmentResponseInfo departmentResponse = new DepartmentResponseInfo();

		try {

		
			
			List<Department> departments = jdbcTemplate.query(departmentSearchSql.toString(), new BeanPropertyRowMapper(Department.class));
			for (Department department:departments){
			Department departMentData=	gson.fromJson(department.getData(),Department.class);
			department.setCategory(departMentData.getCategory());
			department.setName(departMentData.getName());
			department.setNameLocal(departMentData.getNameLocal());
			department.setDescription(departMentData.getDescription());
			department.setAuditDetails(departMentData.getAuditDetails());
			}
			ResponseInfo responseInfo=responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo,true);

			departmentResponse.setDepartments(departments);
			departmentResponse.setResponseInfo(responseInfo);
		}
		catch (Exception e) {
			throw new PropertySearchException("invalid input",requestInfo);
		}
		return departmentResponse;

	}





	/**
	 *Description : This method for getting floor master details
	 * @param tenantId
	 * @param code
	 * @param requestInfo
	 * @return masterModel
	 * @throws Exception
	 */


	@Override
	public FloorTypeResponse getFloorTypeMaster(RequestInfo requestInfo, String tenantId,Integer []ids, String name ,String code,String nameLocal, Integer pageSize, Integer offSet) throws Exception {

		StringBuffer floorTypeSearchSql = new StringBuffer();

		floorTypeSearchSql.append("select * from egpt_floortype_master where tenantid ='"+tenantId+"'");




		if (ids!=null && ids.length>0){

			String  florTypeIds= "";

			int count = 1;
			for (Integer id : ids){
				if (count<ids.length)
					florTypeIds = florTypeIds+id+",";
				else
					florTypeIds = florTypeIds+id;

				count++;

			}

			floorTypeSearchSql.append(" AND id IN ("+florTypeIds+")");

		}

		StringBuffer dataSearch = new StringBuffer();

		if (code!=null && !code.isEmpty())
			floorTypeSearchSql.append(" AND code = '"+code+"'");

		if(name!=null || nameLocal!=null)
			dataSearch.append(" AND data = '");

		if (name!=null && !name.isEmpty())
			dataSearch.append("{ \"name\":\""+name+"\"");

		if (nameLocal!=null && !nameLocal.isEmpty()){
			if(name!=null && !name.isEmpty())
				dataSearch.append(" , \"nameLocal\":\""+nameLocal+"\"");
			else
				dataSearch.append("{\"nameLocal\":\""+nameLocal+"\"");	
		}


		if(name!=null ||  nameLocal!=null)
			dataSearch.append("}'");

		floorTypeSearchSql.append( dataSearch.toString());

		if ( pageSize == null )
			pageSize = Integer.valueOf( environment.getProperty("default.page.size").trim());
		if ( offSet == null )
			offSet = Integer.valueOf( environment.getProperty("default.offset").trim());

		floorTypeSearchSql.append("offset "+offSet+" limit "+pageSize);



		FloorTypeResponse floorTypeResponse = new FloorTypeResponse();

		try {

			List<FloorType> floorTypes = jdbcTemplate.query(floorTypeSearchSql.toString(), new BeanPropertyRowMapper(FloorType.class));
			ResponseInfo responseInfo=responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo,true);

			floorTypeResponse.setFloorTypes(floorTypes);
			floorTypeResponse.setResponseInfo(responseInfo);
		}
		catch (Exception e) {
			throw new PropertySearchException("invalid input",requestInfo);
		}
		return floorTypeResponse;


		//return	getMaster(masterList.getFloorTypeMaster(),tenantId,code,requestInfo);
	}


	/**
	 * <P> This method will presist the given floor type in the database</p>
	 * @author Prasad
	 * @param floorTypeRequest
	 * @param tenantId
	 * @return {@link FloorTypeResponse}
	 */

	@Override
	public FloorTypeResponse createFloorType(FloorTypeRequest floorTypeRequest, String tenantId) throws Exception {
		List<FloorType> floorTypes = floorTypeRequest.getFloorTypes();

		for ( FloorType floorType : floorTypes ){

			long createdTime =new Date().getTime();

			Gson gson=new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();

			String data=gson.toJson(floorType);

			StringBuffer usageMasterCreateSQL=new StringBuffer();

			usageMasterCreateSQL.append("INSERT INTO egpt_floortype_master")
			.append(" ( tenantid,code,data,createdby,lastModifiedBy, createdTime,lastModifiedtime) ")
			.append(" VALUES( ?, ?, ?, ?, ?, ?,?)");

			final PreparedStatementCreator psc = new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {

					final PreparedStatement ps = connection.prepareStatement(usageMasterCreateSQL.toString(), new String[] { "id" });

					ps.setString(1, floorType.getTenantId());
					ps.setString(2, floorType.getCode());
					PGobject jsonObject = new PGobject();
					jsonObject.setType("json");
					jsonObject.setValue(data);
					ps.setObject(3,jsonObject);
					ps.setString(6, floorType.getAuditDetails().getCreatedBy());
					ps.setString(7, floorType.getAuditDetails().getLastModifiedBy());
					ps.setBigDecimal(8, new BigDecimal(createdTime));
					ps.setBigDecimal(9, new BigDecimal(createdTime));
					return ps;
				}
			};

			// The newly generated key will be saved in this object
			final KeyHolder holder = new GeneratedKeyHolder();
			try {
				jdbcTemplate.update(psc, holder);
			}
			catch (Exception e) {
				throw new InvalidInputException(floorTypeRequest.getRequestInfo());
			}
			floorType.setId(holder.getKey().longValue());
		}

		FloorTypeResponse floorTypeResponse = new FloorTypeResponse();
		ResponseInfo responseInfo=responseInfoFactory.createResponseInfoFromRequestInfo(floorTypeRequest.getRequestInfo(),true);
		floorTypeResponse.setFloorTypes(floorTypes);
		floorTypeResponse.setResponseInfo(responseInfo);
		return floorTypeResponse;
	}

	/**
	 * <p>This method will update floor type object with the given details<p>
	 * @author Prasad
	 * @param floorTypeRequest
	 * @param tenantId
	 * @param id
	 * @return {@link FloorTypeRequest}
	 */
	@Override
	public FloorTypeResponse updateFloorType(FloorTypeRequest floorTypeRequest, String tenantId, Integer id)
			throws Exception {

		List<FloorType> floorTypes = floorTypeRequest.getFloorTypes();


		for (FloorType floorType : floorTypes ){


			long updatedTime =new Date().getTime();

			Gson gson=new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();

			String data=gson.toJson(floorType);

			StringBuffer updateFloorTypeSql=new StringBuffer();

			updateFloorTypeSql.append("UPDATE egpt_floortype_master ")
			.append(" SET tenantid = ? code = ?,")
			.append(" data=?, createdby =?,")
			.append(" lastModifiedBy =? ,createdTime = ?,lastModifiedtime= ?")
			.append(" WHERE id = " + id);

			final PreparedStatementCreator psc = new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
					final PreparedStatement ps = connection.prepareStatement(updateFloorTypeSql.toString());
					ps.setString(1, floorType.getTenantId());
					ps.setString(2, floorType.getCode());
					PGobject jsonObject = new PGobject();
					jsonObject.setType("json");
					jsonObject.setValue(data);
					ps.setObject(3,jsonObject);
					ps.setString(4, floorType.getAuditDetails().getCreatedBy());
					ps.setString(5, floorType.getAuditDetails().getLastModifiedBy());
					ps.setLong(6, floorType.getAuditDetails().getCreatedTime());
					ps.setLong(7, updatedTime);

					return ps;
				}
			};

			jdbcTemplate.update(psc);
		}

		FloorTypeResponse floorTypeResponse = new FloorTypeResponse();
		floorTypeResponse.setFloorTypes(floorTypes);
		ResponseInfo responseInfo=responseInfoFactory.createResponseInfoFromRequestInfo(floorTypeRequest.getRequestInfo(),true);
		floorTypeResponse.setResponseInfo(responseInfo);

		return floorTypeResponse;



	}

	/**
	 * <P> This method will search the Wood type records based on the given parameters<p>
	 * @author Prasad
	 * @param requestInfo
	 * @param tenantId
	 * @param ids
	 * @param name
	 * @param code
	 * @param nameLocal
	 * @param pageSize
	 * @param offSet
	 * @return {@link WoodTypeResponse}
	 */

	@Override
	public WoodTypeResponse getWoodTypes(RequestInfo requestInfo, String tenantId, Integer[] ids, String name,
			String code, String nameLocal, Integer pageSize, Integer offSet) throws Exception {
		StringBuffer woodTypeSearchSql = new StringBuffer();

		woodTypeSearchSql.append("select * from egpt_woodtypes_master where tenantid ='"+tenantId+"'");




		if (ids!=null && ids.length>0){

			String  florTypeIds= "";

			int count = 1;
			for (Integer id : ids){
				if (count<ids.length)
					florTypeIds = florTypeIds+id+",";
				else
					florTypeIds = florTypeIds+id;

				count ++;

			}


			woodTypeSearchSql.append(" AND id IN ("+florTypeIds+")");



		}



		StringBuffer dataSearch = new StringBuffer();

		if (code!=null && !code.isEmpty())
			woodTypeSearchSql.append(" AND code = '"+code+"'");

		if(name!=null  || nameLocal!=null)
			dataSearch.append(" AND data = '");

		if (name!=null && !name.isEmpty())
			dataSearch.append("{ \"name\":\""+name+"\"");

		if (nameLocal!=null && !nameLocal.isEmpty()){
			if(name!=null && !name.isEmpty())
				dataSearch.append(" ,\"nameLocal\":\""+nameLocal+"\"");
			else
				dataSearch.append("{\"nameLocal\":\""+nameLocal+"\"");	
		}

		if(name!=null || nameLocal!=null)
			dataSearch.append("}'");

		woodTypeSearchSql.append(dataSearch.toString());

		if ( pageSize == null )
			pageSize = Integer.valueOf( environment.getProperty("default.page.size").trim());
		if ( offSet == null )
			offSet = Integer.valueOf( environment.getProperty("default.offset").trim());

		woodTypeSearchSql.append("offset "+offSet+ " limit "+pageSize);


		WoodTypeResponse woodTypeResponse = new WoodTypeResponse();

		try {

			List<WoodType> woodTypes = jdbcTemplate.query(woodTypeSearchSql.toString(), new BeanPropertyRowMapper(WoodType.class));
			ResponseInfo responseInfo=responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo,true);

			woodTypeResponse.setWoodTypes(woodTypes);
			woodTypeResponse.setResponseInfo(responseInfo);
		}
		catch (Exception e) {
			throw new InvalidInputException(requestInfo);
		}
		return woodTypeResponse;



	}

	/**
	 * <p>Thos method will insert the wood type record in the database<p>
	 * @author Prasad
	 * @param woodTypeRequest
	 * @param tenantId
	 * @return {@link WoodTypeResponse}
	 */
	@Override
	public WoodTypeResponse createWoodType(WoodTypeRequest woodTypeRequest, String tenantId) throws Exception {
		List<WoodType> woodTypes = woodTypeRequest.getWoodTypes();

		for ( WoodType woodType : woodTypes ){

			long createdTime =new Date().getTime();

			StringBuffer usageMasterCreateSQL=new StringBuffer();


			Gson gson=new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();

			String data=gson.toJson(woodType);

			usageMasterCreateSQL.append("INSERT INTO egpt_woodtypes_master")
			.append(" ( tenantid,code,data,createdby,lastModifiedBy, createdTime,lastModifiedTime) ")
			.append(" VALUES( ?, ?, ?, ?, ?, ?,?)");

			final PreparedStatementCreator psc = new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {

					final PreparedStatement ps = connection.prepareStatement(usageMasterCreateSQL.toString(), new String[] { "id" });

					PGobject jsonObject = new PGobject();
					jsonObject.setType("json");
					jsonObject.setValue(data);
					ps.setString(1, woodType.getTenantId());
					ps.setString(2, woodType.getCode());
					ps.setObject(3, jsonObject);
					ps.setString(4, woodType.getAuditDetails().getCreatedBy());
					ps.setString(5, woodType.getAuditDetails().getLastModifiedBy());
					ps.setBigDecimal(6, new BigDecimal(createdTime));
					ps.setBigDecimal(7, new BigDecimal(createdTime));
					return ps;
				}
			};

			// The newly generated key will be saved in this object
			final KeyHolder holder = new GeneratedKeyHolder();
			try {
				jdbcTemplate.update(psc, holder);
			}
			catch (Exception e) {
				throw new InvalidInputException(woodTypeRequest.getRequestInfo());
			}
			woodType.setId(holder.getKey().longValue());
		}

		WoodTypeResponse woodTypeResponse = new WoodTypeResponse();
		ResponseInfo responseInfo=responseInfoFactory.createResponseInfoFromRequestInfo(woodTypeRequest.getRequestInfo(),true);

		woodTypeResponse.setWoodTypes(woodTypes);
		woodTypeResponse.setResponseInfo(responseInfo);
		return woodTypeResponse;

	}


	/**
	 * <p> This method will update the wood type object for the given Id for given wood type object<p>
	 * @author Prasad
	 * @param woodTypeRequest
	 * @param tenantId
	 * @param id
	 * @return {@link WoodTypeResponse}
	 */
	@Override
	public WoodTypeResponse updateWoodType(WoodTypeRequest woodTypeRequest, String tenantId, Integer id)
			throws Exception {


		List<WoodType> woodTypes = woodTypeRequest.getWoodTypes();


		for (WoodType woodType : woodTypes ){


			long updatedTime =new Date().getTime();

			StringBuffer updateFloorTypeSql=new StringBuffer();

			Gson gson=new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();

			String data=gson.toJson(woodType);

			updateFloorTypeSql.append("UPDATE egpt_woodtypes_master ")
			.append(" SET tenantid = ?, code = ?,")
			.append(" data = ?, createdby =?")
			.append(" lastModifiedBy =? ,createdTime = ?,lastModifiedTime= ?")
			.append(" WHERE id = " + id);

			final PreparedStatementCreator psc = new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
					final PreparedStatement ps = connection.prepareStatement(updateFloorTypeSql.toString());
					ps.setString(1, woodType.getTenantId());
					ps.setString(2, woodType.getCode());
					PGobject jsonObject = new PGobject();
					jsonObject.setType("json");
					jsonObject.setValue(data);
					ps.setObject(3,jsonObject );
					ps.setString(4, woodType.getAuditDetails().getCreatedBy());
					ps.setString(5, woodType.getAuditDetails().getLastModifiedBy());
					ps.setLong(6, woodType.getAuditDetails().getCreatedTime());
					ps.setBigDecimal(7, new BigDecimal(updatedTime));

					return ps;
				}
			};

			jdbcTemplate.update(psc);
		}

		WoodTypeResponse woodTypeResponse = new WoodTypeResponse();
		ResponseInfo responseInfo=responseInfoFactory.createResponseInfoFromRequestInfo(woodTypeRequest.getRequestInfo(),true);

		woodTypeResponse.setWoodTypes(woodTypes);
		woodTypeResponse.setResponseInfo(responseInfo);
		return woodTypeResponse;



	}

	/**
	 * 
	 * <P> This method will search for the roof type objects for the given parameters and returns the matched
	 * roof type Objects<p>
	 * @author Prasad
	 * @param requestInfo
	 * @param tenantId
	 * @param ids
	 * @param name
	 * @param code
	 * @param nameLocal
	 * @param pageSize
	 * @param offSet
	 * @return {@link RoofTypeResponse}
	 */
	@Override
	public RoofTypeResponse getRoofypes(RequestInfo requestInfo, String tenantId, Integer[] ids, String name,
			String code, String nameLocal, Integer pageSize, Integer offSet) throws Exception {

		StringBuffer roofTypeSearchSql = new StringBuffer();

		roofTypeSearchSql.append("select * from egpt_rooftypes_master where tenantid ='"+tenantId+"'");




		if (ids!=null && ids.length>0){

			String  florTypeIds= "";

			int count = 1;
			for (Integer id : ids){
				if (count<ids.length)
					florTypeIds = florTypeIds+id+",";
				else
					florTypeIds = florTypeIds+id;

				count++;

			}


			roofTypeSearchSql.append(" AND id IN ("+florTypeIds+")");



		}


		if (code!=null && !code.isEmpty())
			roofTypeSearchSql.append(" AND code ="+code);


		StringBuffer dataSearch = new StringBuffer();


		if(name!=null || nameLocal!=null)
			dataSearch.append(" AND data = '");

		if (name!=null && !name.isEmpty())
			dataSearch.append("{ \"name\":\""+name+"\"");

		if (nameLocal!=null && !nameLocal.isEmpty()){
			if(name!=null && !name.isEmpty())
				dataSearch.append(" , \"nameLocal\":\""+nameLocal+"\"");
			else
				dataSearch.append("{\"nameLocal\":\""+nameLocal+"\"");	
		}


		if(name!=null || nameLocal!=null)
			dataSearch.append("}'");

		roofTypeSearchSql.append(dataSearch.toString());


		if ( pageSize == null )
			pageSize = Integer.valueOf( environment.getProperty("default.page.size").trim());
		if ( offSet ==  null )
			offSet = Integer.valueOf( environment.getProperty("default.offset").trim());

		roofTypeSearchSql.append("offset "+offSet+" limit "+pageSize );


		RoofTypeResponse roofTypeResponse = new RoofTypeResponse();

		try {

			List<RoofType> roofTypes = jdbcTemplate.query(roofTypeSearchSql.toString(), new BeanPropertyRowMapper(RoofType.class));
			ResponseInfo responseInfo=responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo,true);

			roofTypeResponse.setRoofTypes(roofTypes);
			roofTypeResponse.setResponseInfo(responseInfo);
		}
		catch (Exception e) {
			throw new PropertySearchException("invalid input",requestInfo);
		}
		return roofTypeResponse;



	}

	/**
	 * <p>This methhod will insert the given roof object in the database</p>
	 * @author Prasad
	 * @param roofTypeRequest
	 * @param tenantId
	 * @return {@link RoofTypeResponse}
	 */
	@Override
	public RoofTypeResponse createRoofype(RoofTypeRequest roofTypeRequest, String tenantId) throws Exception {
		List<RoofType> roofTypes = roofTypeRequest.getRoofTypes();

		for ( RoofType roofType : roofTypes ){

			long createdTime =new Date().getTime();

			Gson gson=new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();

			String data=gson.toJson(roofType);

			StringBuffer usageMasterCreateSQL=new StringBuffer();

			usageMasterCreateSQL.append("INSERT INTO egpt_rooftypes_master")
			.append(" ( tenantid,code,data,createdby,lastModifiedBy, createdTime,lastModifiedTime) ")
			.append(" VALUES( ?, ?, ?, ?, ?,?,?)");

			final PreparedStatementCreator psc = new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {

					final PreparedStatement ps = connection.prepareStatement(usageMasterCreateSQL.toString(), new String[] { "id" });

					ps.setString(1, roofType.getTenantId());
					ps.setString(2, roofType.getCode());
					PGobject jsonObject = new PGobject();
					jsonObject.setType("json");
					jsonObject.setValue(data);
					ps.setObject(3,jsonObject);
					ps.setString(4, roofType.getAuditDetails().getCreatedBy());
					ps.setString(5, roofType.getAuditDetails().getLastModifiedBy());
					ps.setLong(6, createdTime);
					ps.setLong(7, createdTime);
					return ps;
				}
			};

			// The newly generated key will be saved in this object
			final KeyHolder holder = new GeneratedKeyHolder();
			try {
				jdbcTemplate.update(psc, holder);
			}
			catch (Exception e) {
				throw new InvalidInputException(roofTypeRequest.getRequestInfo());
			}
			roofType.setId(holder.getKey().longValue());
		}

		RoofTypeResponse roofTypeResponse = new RoofTypeResponse();
		ResponseInfo responseInfo=responseInfoFactory.createResponseInfoFromRequestInfo(roofTypeRequest.getRequestInfo(),true);

		roofTypeResponse.setRoofTypes(roofTypes);
		roofTypeResponse.setResponseInfo(responseInfo);
		return roofTypeResponse;
	}

	/**
	 * <p>This method will update the given rooftype object for the given rooftype Id </p>
	 * @author Prasad
	 * @param roofTypeRequest
	 * @param tenantId
	 * @param id
	 * @return {@link RoofTypeResponse}
	 */

	@Override
	public RoofTypeResponse updateRoofType(RoofTypeRequest roofTypeRequest, String tenantId, Integer id)
			throws Exception {
		List<RoofType> roofTypes = roofTypeRequest.getRoofTypes();


		for (RoofType roofType : roofTypes ){


			long updatedTime =new Date().getTime();

			StringBuffer updateFloorTypeSql=new StringBuffer();
			Gson gson=new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();

			String data=gson.toJson(roofType);

			updateFloorTypeSql.append("UPDATE egpt_rooftypes_master ")
			.append(" SET tenantid = ?, code = ?,")
			.append(" data=?, createdby =?")
			.append(" lastModifiedBy =? ,createdTime = ?,lastModifiedTime= ?")
			.append(" WHERE id = " + id);

			final PreparedStatementCreator psc = new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
					final PreparedStatement ps = connection.prepareStatement(updateFloorTypeSql.toString());
					ps.setString(1, roofType.getTenantId());
					ps.setString(2, roofType.getCode());
					PGobject jsonObject = new PGobject();
					jsonObject.setType("json");
					jsonObject.setValue(data);
					ps.setObject(3, jsonObject);
					ps.setString(4, roofType.getAuditDetails().getCreatedBy());
					ps.setString(5, roofType.getAuditDetails().getLastModifiedBy());
					ps.setLong(6, roofType.getAuditDetails().getCreatedTime());
					ps.setLong(7, updatedTime);

					return ps;
				}
			};

			jdbcTemplate.update(psc);
		}

		RoofTypeResponse roofTypeResponse = new RoofTypeResponse();
		ResponseInfo responseInfo=responseInfoFactory.createResponseInfoFromRequestInfo(roofTypeRequest.getRequestInfo(),true);

		roofTypeResponse.setRoofTypes(roofTypes);
		roofTypeResponse.setResponseInfo(responseInfo);
		return roofTypeResponse;


	}

	/**
	* Description : This api for creating strctureClass master
	* @param tenantId
	* @param StructureClassRequest
	* @return structureClassResponse
			* @throws Exception
			*/

			@Override
			@Transactional
			public StructureClassResponse craeateStructureClassMaster(String tenantId, StructureClassRequest structureClassRequest) {
		// TODO Auto-generated method stub

		for(StructureClass structureClass:structureClassRequest.getStructureClasses()){

			Long createdTime=new Date().getTime();

			Gson gson=new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();

			String data=gson.toJson(structureClass);

			StringBuffer structureClassQuery=new StringBuffer();
			structureClassQuery.append("insert into egpt_structureclasses_master(tenantId,code,data,");
			structureClassQuery.append("createdBy, lastModifiedBy, createdTime,lastModifiedTime)");
			structureClassQuery.append(" values(?,?,?,?,?,?,?)");


			final PreparedStatementCreator psc = new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
					final PreparedStatement ps = connection.prepareStatement(structureClassQuery.toString(), new String[] { "id" });
					ps.setString(1, structureClass.getTenantId());
					ps.setString(2, structureClass.getCode());
					PGobject jsonObject = new PGobject();
					jsonObject.setType("json");
					jsonObject.setValue(data);
					ps.setObject(3,jsonObject);
					ps.setString(4, structureClass.getAuditDetails().getCreatedBy());
					ps.setString(5, structureClass.getAuditDetails().getLastModifiedBy());
					ps.setLong(6, createdTime);
					ps.setLong(7, createdTime);
					return ps;
				}
			};

			// The newly generated key will be saved in this object
			final KeyHolder holder = new GeneratedKeyHolder();
			jdbcTemplate.update(psc, holder);
			structureClass.setId(Long.valueOf(holder.getKey().intValue()));

		}
		ResponseInfo responseInfo=responseInfoFactory.createResponseInfoFromRequestInfo(structureClassRequest.getRequestInfo(),true);

		StructureClassResponse structureClassResponse = new StructureClassResponse();

		structureClassResponse.setStructureClasses(structureClassRequest.getStructureClasses());
		structureClassResponse.setResponseInfo(responseInfo);

		return structureClassResponse;
		}

		/**
		* Description : This api for updating strctureClass master
		* @param tenantId
		* @param StructureClassRequest
		* @return structureClassResponse
		* @throws Exception
		*/


		@Override
		@Transactional
		public StructureClassResponse updateStructureClassMaster(String tenantId,Long id,StructureClassRequest structureClassRequest) {

		for(StructureClass structureClass:structureClassRequest.getStructureClasses()){

		Long modifiedTime=new Date().getTime();

		Gson gson=new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();

		String data=gson.toJson(structureClass);

		String departmentTypeUpdate = "UPDATE egpt_department_master set tenantId = ?, code = ?,data = ?, lastModifiedBy = ?, lastModifiedTime = ? where id = " +id;


		final PreparedStatementCreator psc = new PreparedStatementCreator() {
		@Override
		public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
		final PreparedStatement ps = connection.prepareStatement(departmentTypeUpdate, new String[] { "id" });
		ps.setString(1, structureClass.getTenantId());	
		ps.setString(2,structureClass.getCode());
		PGobject jsonObject = new PGobject();
		jsonObject.setType("json");
		jsonObject.setValue(data);
		ps.setString(3, data);
		ps.setString(4, structureClass.getAuditDetails().getLastModifiedBy());
		ps.setLong(5, modifiedTime);
		return ps;
		}
		};
		jdbcTemplate.update(psc);

		}
		ResponseInfo responseInfo=responseInfoFactory.createResponseInfoFromRequestInfo(structureClassRequest.getRequestInfo(),true);

		StructureClassResponse structureClassResponse=new StructureClassResponse();
		structureClassResponse.setStructureClasses(structureClassRequest.getStructureClasses());
		structureClassResponse.setResponseInfo(responseInfo);
		return structureClassResponse;
	}
		
		/**
	     * Description : This method will use for creating property type
	     * @param tenantId
	     * @param propertyTypeRequest
	     * @return
	     */

	    @Override
	    public PropertyTypeResponse createPropertyTypeMaster(String tenantId, PropertyTypeRequest propertyTypeRequest) {
	        // TODO Auto-generated method stub

	        for(PropertyType propertyType:propertyTypeRequest.getPropertyTypes()){

	            Long createdTime = new Date().getTime();

	            Gson gson=new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();

	            String data=gson.toJson(propertyType);

	            StringBuffer propertyTypeQuery=new StringBuffer();
	            propertyTypeQuery.append("insert into egpt_propertytypes_master(tenantId,code,data,");
	            propertyTypeQuery.append("createdBy, lastModifiedBy, createdTime,lastModifiedTime)");
	            propertyTypeQuery.append(" values(?,?,?,?,?,?,?)");

	            final PreparedStatementCreator psc = new PreparedStatementCreator() {
	                @Override
	                public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
	                    final PreparedStatement ps = connection.prepareStatement(propertyTypeQuery.toString(), new String[] { "id" });
	                    ps.setString(1, propertyType.getTenantId());
	                    ps.setString(2, propertyType.getCode());
	                    PGobject jsonObject = new PGobject();
	                    jsonObject.setType("json");
	                    jsonObject.setValue(data);
	                    ps.setObject(3,jsonObject);
	                    ps.setString(4, propertyType.getAuditDetails().getCreatedBy());
	                    ps.setString(5, propertyType.getAuditDetails().getLastModifiedBy());
	                    ps.setLong(6, createdTime);
	                    ps.setLong(7, createdTime);
	                    return ps;
	                }
	            };

	            // The newly generated key will be saved in this object
	            final KeyHolder holder = new GeneratedKeyHolder();
	            jdbcTemplate.update(psc, holder);
	            propertyType.setId(Long.valueOf(holder.getKey().intValue()));


	        }
	        ResponseInfo responseInfo=responseInfoFactory.createResponseInfoFromRequestInfo(propertyTypeRequest.getRequestInfo(),true);

	        PropertyTypeResponse propertyTypeResponse = new PropertyTypeResponse();

	        propertyTypeResponse.setPropertyTypes(propertyTypeRequest.getPropertyTypes());

	        propertyTypeResponse.setResponseInfo(responseInfo);

	        return propertyTypeResponse;
	    }


	    /**
	     * Description : This method will use for update property type
	     * @param tenantId
	     * @param propertyTypeResponse
	     * @return
	     */
	    @Override
	    public PropertyTypeResponse updatePropertyTypeMaster(String tenantId, Long id,PropertyTypeRequest propertyTypeRequest) {

	        for(PropertyType propertyType: propertyTypeRequest.getPropertyTypes()){


	            Long modifiedTime=new Date().getTime();

	            Gson gson=new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();

	            String data=gson.toJson(propertyType);

	            String departmentTypeUpdate = "UPDATE egpt_department_master set tenantId = ?, code = ?,data = ?, "
	                    + "lastModifiedBy = ?, lastModifiedTime = ? where id = " +id;


	            final PreparedStatementCreator psc = new PreparedStatementCreator() {
	                @Override
	                public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
	                    final PreparedStatement ps = connection.prepareStatement(departmentTypeUpdate, new String[] { "id" });
	                    ps.setString(1, propertyType.getTenantId());  
	                    ps.setString(2,propertyType.getCode());
	                    PGobject jsonObject = new PGobject();
	                    jsonObject.setType("json");
	                    jsonObject.setValue(data);
	                    ps.setObject(3,jsonObject);
	                    ps.setString(4, propertyType.getAuditDetails().getLastModifiedBy());
	                    ps.setLong(5, modifiedTime);
	                    return ps;
	                }
	            };
	            jdbcTemplate.update(psc);

	        }
	        ResponseInfo responseInfo=responseInfoFactory.createResponseInfoFromRequestInfo(propertyTypeRequest.getRequestInfo(),true);

	        PropertyTypeResponse propertyTypeResponse=new PropertyTypeResponse();
	        propertyTypeResponse.setPropertyTypes(propertyTypeRequest.getPropertyTypes());
	        propertyTypeResponse.setResponseInfo(responseInfo);
	        return propertyTypeResponse;
	    }



	    @Override
	    public PropertyTypeResponse getPropertyTypeMaster(RequestInfo requestInfo, String tenantId, Integer[] ids, String name, String code, String nameLocal, Boolean active, Integer orderNumber, Integer pageSize, Integer offSet) {

	        StringBuffer propertyTypeSearchSql = new StringBuffer();

	        propertyTypeSearchSql.append("select * from egpt_propertytypes_master where tenantid ='"+tenantId+"'");



	        if (ids!=null && ids.length>0){

	            String  propertyTypeIds= "";

	            int count = 1;
	            for (Integer id : ids){
	                if (count<ids.length)
	                    propertyTypeIds = propertyTypeIds+id+",";
	                else
	                    propertyTypeIds = propertyTypeIds+id;
	                count++;

	            }


	            propertyTypeSearchSql.append(" AND id IN ("+propertyTypeIds+")");



	        }

	        StringBuffer dataSearch = new StringBuffer();

	        if (code!=null && !code.isEmpty())
	            propertyTypeSearchSql.append(" AND code = '"+code+"'");


	        if (name!=null && !name.isEmpty())
	            dataSearch.append("{ \"name\":\""+name+"\"");

	        if (nameLocal!=null && !nameLocal.isEmpty()){
	            if(name!=null && !name.isEmpty())
	                dataSearch.append(" ,\"nameLocal\":\""+nameLocal+"\"");
	            else
	                dataSearch.append("{\"nameLocal\":\""+nameLocal+"\""); 

	        }

	        if ( active!=null){
	            if( nameLocal!=null && !nameLocal.isEmpty())
	                dataSearch.append(" ,\"active\":\""+active+"\"");
	            else if( name!=null && !name.isEmpty())
	                dataSearch.append(" ,\"active\":\""+active+"\"");
	            else
	                dataSearch.append("{\"active\":\""+active+"\"");
	        }   

	        if ( orderNumber!=null){
	            if( name==null  && nameLocal==null && active == null)
	            	 dataSearch.append("{\"orderNumber\":\""+orderNumber+"\"");
	         
	            else
	            	dataSearch.append(" ,\"orderNumber\":\""+orderNumber+"\"");
	               
	        }   


	        if ( pageSize == null)
	            pageSize = 30;
	        if ( offSet == null )
	            offSet = 0;
	        propertyTypeSearchSql.append("offset "+offSet+ " limit "+pageSize);

	        PropertyTypeResponse propertyTypeResponse = new PropertyTypeResponse();

	        try {

	            List<PropertyType> propertyTypes = jdbcTemplate.query(propertyTypeSearchSql.toString(), new BeanPropertyRowMapper(PropertyType.class));
	            ResponseInfo responseInfo=responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo,true);

	            propertyTypeResponse.setPropertyTypes(propertyTypes);
	            propertyTypeResponse.setResponseInfo(responseInfo);
	        }
	        catch (Exception e) {
	            throw new PropertySearchException("invalid input",requestInfo);
	        }
	        return propertyTypeResponse;

	    }

	    /**
	     * Description : This method will use for creating Occuapancy
	     * @param tenantId
	     * @param occuapancyRequest
	     * @return
	     */

	    @Override
	    public OccuapancyMasterResponse createOccuapancyMaster(String tenantId, OccuapancyMasterRequest occuapancyMasterRequest) {
	        // TODO Auto-generated method stub

	        for(OccuapancyMaster occuapancy: occuapancyMasterRequest.getOccuapancyMasters()){

	            Long createdTime = new Date().getTime();

	            Gson gson=new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();

	            String data=gson.toJson(occuapancy);

	            StringBuffer occuapancyQuery=new StringBuffer();
	            occuapancyQuery.append("insert into egpt_occuapancy_master(tenantId,code,data,");
	            occuapancyQuery.append("createdBy, lastModifiedBy, createdTime,lastModifiedTime)");
	            occuapancyQuery.append(" values(?,?,?,?,?,?,?)");

	            final PreparedStatementCreator psc = new PreparedStatementCreator() {
	                @Override
	                public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
	                    final PreparedStatement ps = connection.prepareStatement(occuapancyQuery.toString(), new String[] { "id" });
	                    ps.setString(1, occuapancy.getTenantId());
	                    ps.setString(2, occuapancy.getCode());
	                    PGobject jsonObject = new PGobject();
	                    jsonObject.setType("json");
	                    jsonObject.setValue(data);
	                    ps.setObject(3,jsonObject);
	                    ps.setString(4, occuapancy.getAuditDetails().getCreatedBy());
	                    ps.setString(5, occuapancy.getAuditDetails().getLastModifiedBy());
	                    ps.setLong(6, createdTime);
	                    ps.setLong(7, createdTime);
	                    return ps;
	                }
	            };

	            // The newly generated key will be saved in this object
	            final KeyHolder holder = new GeneratedKeyHolder();
	            jdbcTemplate.update(psc, holder);
	            occuapancy.setId(Long.valueOf(holder.getKey().intValue()));

	        }
	        ResponseInfo responseInfo=responseInfoFactory.createResponseInfoFromRequestInfo(occuapancyMasterRequest.getRequestInfo(),true);

	        OccuapancyMasterResponse occuapancyMasterResponse = new OccuapancyMasterResponse();

	        occuapancyMasterResponse.setOccuapancyMasters(occuapancyMasterRequest.getOccuapancyMasters());

	        occuapancyMasterResponse.setResponseInfo(responseInfo);

	        return occuapancyMasterResponse;
	    }

	    /**
	     * Description : This method will use for update occuapancy 
	     * @param tenantId
	     * @param propertyTypeResponse
	     * @return
	     */
	    @Override
	    public OccuapancyMasterResponse updateOccuapancyMaster(String tenantId, Long id, OccuapancyMasterRequest occuapancyRequest) {

	        for(OccuapancyMaster occuapancyMaster: occuapancyRequest.getOccuapancyMasters()){


	            Long modifiedTime=new Date().getTime();

	            Gson gson=new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();

	            String data=gson.toJson(occuapancyMaster);

	            String departmentTypeUpdate = "UPDATE egpt_occuapancy_master set tenantId = ?, code = ?,data = ?, "
	                    + "lastModifiedBy = ?, lastModifiedTime = ? where id = " +id;


	            final PreparedStatementCreator psc = new PreparedStatementCreator() {
	                @Override
	                public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
	                    final PreparedStatement ps = connection.prepareStatement(departmentTypeUpdate, new String[] { "id" });
	                    ps.setString(1, occuapancyMaster.getTenantId());  
	                    ps.setString(2,occuapancyMaster.getCode());
	                    PGobject jsonObject = new PGobject();
	                    jsonObject.setType("json");
	                    jsonObject.setValue(data);
	                    ps.setObject(3,jsonObject);
	                    ps.setString(4, occuapancyMaster.getAuditDetails().getLastModifiedBy());
	                    ps.setLong(5, modifiedTime);
	                    return ps;
	                }
	            };
	            jdbcTemplate.update(psc);

	        }
	        ResponseInfo responseInfo=responseInfoFactory.createResponseInfoFromRequestInfo(occuapancyRequest.getRequestInfo(),true);

	        OccuapancyMasterResponse occuapancyResponse=new OccuapancyMasterResponse();
	        occuapancyResponse.setOccuapancyMasters(occuapancyRequest.getOccuapancyMasters());
	        occuapancyResponse.setResponseInfo(responseInfo);
	        return occuapancyResponse;
	    }

	    @Override
	    public OccuapancyMasterResponse getOccuapancyMaster(RequestInfo requestInfo, String tenantId, Integer[] ids,
	            String name, String code, String nameLocal, Boolean active, Integer orderNumber, Integer pageSize,
	            Integer offSet) {

	        StringBuffer occuapancySearchSql = new StringBuffer();

	        occuapancySearchSql.append("select * from egpt_occuapancy_master where tenantid ='"+tenantId+"'");



	        if (ids!=null && ids.length>0){

	            String  occuapancyIds= "";

	            int count = 1;
	            for (Integer id : ids){
	                if (count<ids.length)
	                    occuapancyIds = occuapancyIds+id+",";
	                else
	                    occuapancyIds = occuapancyIds+id;
	                count++;

	            }


	            occuapancySearchSql.append(" AND id IN ("+occuapancyIds+")");



	        }

	        StringBuffer dataSearch = new StringBuffer();

	        if (code!=null && !code.isEmpty())
	            occuapancySearchSql.append(" AND code = '"+code+"'");


	        if (name!=null && !name.isEmpty())
	            dataSearch.append("{ \"name\":\""+name+"\"");

	        if (nameLocal!=null && !nameLocal.isEmpty()){
	            if(name!=null && !name.isEmpty())
	                dataSearch.append(" ,\"nameLocal\":\""+nameLocal+"\"");
	            else
	                dataSearch.append("{\"nameLocal\":\""+nameLocal+"\""); 

	        }

	        if ( active!=null){
	            if( nameLocal!=null && !nameLocal.isEmpty())
	                dataSearch.append(" ,  \"active\":\""+active+"\"");
	            else if( name!=null && !name.isEmpty())
	                dataSearch.append(" ,  \"active\":\""+active+"\"");
	            else
	                dataSearch.append("{\"active\":\""+active+"\"");
	        }   

	        if ( orderNumber!=null){
	            if( name==null  && nameLocal==null && active == null)
	            	dataSearch.append("{\"orderNumber\":\""+orderNumber+"\"");

	            else
	            dataSearch.append(" ,\"orderNumber\":\""+orderNumber+"\"");
	        }   


	        if ( pageSize == null)
	            pageSize = 30;
	        if ( offSet == null )
	            offSet = 0;
	        occuapancySearchSql.append("offset "+offSet+ " limit "+pageSize);

	        OccuapancyMasterResponse occuapancyResponse = new OccuapancyMasterResponse();

	        try {

	            List<OccuapancyMaster> occuapancyMaster = jdbcTemplate.query(occuapancySearchSql.toString(), new BeanPropertyRowMapper(OccuapancyMaster.class));
	            ResponseInfo responseInfo=responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo,true);

	            occuapancyResponse.setOccuapancyMasters(occuapancyMaster);
	            occuapancyResponse.setResponseInfo(responseInfo);
	        }
	        catch (Exception e) {
	            throw new PropertySearchException("invalid input",requestInfo);
	        }
	        return occuapancyResponse;

	    }
}
