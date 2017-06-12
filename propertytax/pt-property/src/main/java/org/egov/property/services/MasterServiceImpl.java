package org.egov.property.services;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.egov.models.Department;
import org.egov.models.DepartmentRequest;
import org.egov.models.DepartmentResponseInfo;
import org.egov.models.Floor;
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
import org.egov.models.UsageMaster;
import org.egov.models.UsageMasterRequest;
import org.egov.models.UsageMasterResponse;
import org.egov.models.WallType;
import org.egov.models.WallTypeRequest;
import org.egov.models.WallTypeResponse;
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
			depeartmentQuery.append("insert into egpt_mstr_department(tenantId,code,data,");
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
			department.getAuditDetails().setCreatedTime(createdTime);
			department.getAuditDetails().setLastModifiedTime(createdTime);


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

			String departmentTypeUpdate = "UPDATE egpt_mstr_department set tenantId = ?, code = ?,data = ?, lastModifiedBy = ?, lastModifiedTime = ? where id = " +id;


			final PreparedStatementCreator psc = new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
					final PreparedStatement ps = connection.prepareStatement(departmentTypeUpdate, new String[] { "id" });
					ps.setString(1, department.getTenantId());	
					ps.setString(2,department.getCode());
					PGobject jsonObject = new PGobject();
					jsonObject.setType("json");
					jsonObject.setValue(data);
					ps.setObject(3,jsonObject);
					ps.setString(4, department.getAuditDetails().getLastModifiedBy());
					ps.setLong(5, modifiedTime);
					return ps;
				}
			};
			jdbcTemplate.update(psc);
			department.getAuditDetails().setLastModifiedTime(modifiedTime);


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

		departmentSearchSql.append("select * from egpt_mstr_department where tenantid ='"+tenantId+"'");

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

		floorTypeSearchSql.append("select * from egpt_mstr_floor where tenantid ='"+tenantId+"'");

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
			dataSearch.append(" AND data @> '");

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
			Gson gson=new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
			List<FloorType> floorTypes = jdbcTemplate.query(floorTypeSearchSql.toString(), new BeanPropertyRowMapper(FloorType.class));
			for (FloorType floor:floorTypes){
				FloorType floorData=gson.fromJson(floor.getData(),FloorType.class);

				floor.setName(floorData.getName());
				floor.setNameLocal(floorData.getNameLocal());
				floor.setDescription(floorData.getDescription());
				floor.setAuditDetails(floorData.getAuditDetails());
			}
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

			usageMasterCreateSQL.append("INSERT INTO egpt_mstr_floor")
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
			floorType.getAuditDetails().setCreatedTime(createdTime);
			floorType.getAuditDetails().setLastModifiedTime(createdTime);
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

			updateFloorTypeSql.append("UPDATE egpt_mstr_floor ")
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
			floorType.getAuditDetails().setLastModifiedTime(updatedTime);
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

		woodTypeSearchSql.append("select * from egpt_mstr_wood where tenantid ='"+tenantId+"'");




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
			dataSearch.append(" AND data @> '");

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
			Gson gson=new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
			for(WoodType woodType:woodTypes){
				WoodType woodData=	gson.fromJson(woodType.getData(),WoodType.class);
				woodType.setAuditDetails(woodData.getAuditDetails());
				woodType.setDescription(woodData.getDescription());
				woodType.setName(woodData.getName());
				woodType.setNameLocal(woodType.getNameLocal());
			}
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

			usageMasterCreateSQL.append("INSERT INTO egpt_mstr_wood")
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
			woodType.getAuditDetails().setCreatedTime(createdTime);
			woodType.getAuditDetails().setLastModifiedTime(createdTime);
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

			updateFloorTypeSql.append("UPDATE egpt_mstr_wood ")
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
			woodType.getAuditDetails().setLastModifiedTime(updatedTime);
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

		roofTypeSearchSql.append("select * from egpt_mstr_roof where tenantid ='"+tenantId+"'");




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
			dataSearch.append(" AND data @> '");

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
			Gson gson=new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
			for(RoofType roofType:roofTypes){
				RoofType roofData=	gson.fromJson(roofType.getData(),RoofType.class);
				roofType.setAuditDetails(roofData.getAuditDetails());
				roofType.setDescription(roofData.getDescription());
				roofType.setName(roofData.getName());
				roofType.setNameLocal(roofData.getNameLocal());
			}
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

			usageMasterCreateSQL.append("INSERT INTO egpt_mstr_roof")
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
			roofType.getAuditDetails().setCreatedTime(createdTime);
			roofType.getAuditDetails().setLastModifiedTime(createdTime);
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

			updateFloorTypeSql.append("UPDATE egpt_mstr_roof ")
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
			roofType.getAuditDetails().setLastModifiedTime(updatedTime);
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
			structureClassQuery.append("insert into egpt_mstr_structure(tenantId,code,data,");
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
			structureClass.getAuditDetails().setCreatedTime(createdTime);
			structureClass.getAuditDetails().setLastModifiedTime(createdTime);

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

			String departmentTypeUpdate = "UPDATE egpt_mstr_structure set tenantId = ?, code = ?,data = ?, lastModifiedBy = ?, lastModifiedTime = ? where id = " +id;


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
			structureClass.getAuditDetails().setLastModifiedTime(modifiedTime);

		}
		ResponseInfo responseInfo=responseInfoFactory.createResponseInfoFromRequestInfo(structureClassRequest.getRequestInfo(),true);

		StructureClassResponse structureClassResponse=new StructureClassResponse();
		structureClassResponse.setStructureClasses(structureClassRequest.getStructureClasses());
		structureClassResponse.setResponseInfo(responseInfo);
		return structureClassResponse;
	}


	/**
	 * Description : This api for searching strctureClass master
	 * @param requestInfo
	 * @param tenantId
	 * @param ids
	 * @param name
	 * @param code
	 * @param nameLocal
	 * @param active 
	 * @param orderNumber
	 * @param pageSize
	 * @param offSet
	 * @return structureClassResponse
	 * @throws Exception
	 */

	@Override
	public StructureClassResponse getStructureClassMaster(RequestInfo requestInfo, String tenantId, Integer[] ids, String name, String code, String nameLocal, Boolean active, Integer orderNumber, Integer pageSize, Integer offSet) {

		StringBuffer structureSearchSql = new StringBuffer();

		structureSearchSql.append("select * from egpt_mstr_structure where tenantid ='"+tenantId+"'");

		if (ids!=null && ids.length>0){

			String  structureIds= "";

			int count = 1;
			for (Integer id : ids){
				if (count<ids.length)
					structureIds = structureIds+id+",";
				else
					structureIds = structureIds+id;

				count++;
			}

			structureSearchSql.append(" AND id IN ("+structureIds+")");

		}

		StringBuffer dataSearch = new StringBuffer();

		if (code!=null && !code.isEmpty())
			structureSearchSql.append(" AND code = '"+code+"'");

		if (name != null || nameLocal != null || active != null || orderNumber != null)
			dataSearch.append(" AND data @> '");

		if (name!=null && !name.isEmpty())
			dataSearch.append("{ \"name\":\""+name+"\"");

		if (nameLocal!=null && !nameLocal.isEmpty()){
			if(name!=null && !name.isEmpty())
				dataSearch.append(" , \"nameLocal\":\""+nameLocal+"\"");
			else
				dataSearch.append("{\"nameLocal\":\""+nameLocal+"\"");	
		}
		if ( active!=null ){
			if( nameLocal!=null && !nameLocal.isEmpty())
				dataSearch.append(" ,  \"active\":\""+active+"\"");
			else if( name!=null && !name.isEmpty())
				dataSearch.append(" ,  \"active\":\""+active+"\"");
			else
				dataSearch.append("{\"active\":\""+active+"\"");
		}	
		if(orderNumber != null){
			if(nameLocal==null && name==null && active==null)
				dataSearch.append("{\"orderNumber\":\""+orderNumber+"\"");
			else
				dataSearch.append(" ,  \"orderNumber\":\""+orderNumber+"\"");
		}

		if(name!=null || active!=null || nameLocal!=null || orderNumber != null)
			dataSearch.append("}'");
		if ( pageSize == null)
			pageSize = 30;
		if ( offSet == null )
			offSet = 0;
		structureSearchSql.append("offset "+offSet+ " limit "+pageSize);

		StructureClassResponse structureClassResponse= new StructureClassResponse();

		try {

			List<StructureClass> strctureTypes= jdbcTemplate.query(structureSearchSql.toString(), new BeanPropertyRowMapper(StructureClass.class));
			ResponseInfo responseInfo=responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo,true);
			Gson gson=new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
			for(StructureClass structureType:strctureTypes){
				StructureClass structureData=	gson.fromJson(structureType.getData(),StructureClass.class);
				structureType.setAuditDetails(structureData.getAuditDetails());
				structureType.setDescription(structureData.getDescription());
				structureType.setName(structureData.getName());
				structureType.setNameLocal(structureData.getNameLocal());
				structureType.setActive(structureData.getActive());
				structureType.setOrderNumber(structureData.getOrderNumber());
			}
			structureClassResponse.setStructureClasses(strctureTypes);
			structureClassResponse.setResponseInfo(responseInfo);
		}
		catch (Exception e) {
			throw new PropertySearchException("invalid input",requestInfo);
		}
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
			propertyTypeQuery.append("insert into egpt_mstr_property(tenantId,code,data,");
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
			propertyType.getAuditDetails().setCreatedTime(createdTime);
			propertyType.getAuditDetails().setLastModifiedTime(createdTime);



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

			String propertyTypeUpdate = "UPDATE egpt_mstr_department set tenantId = ?, code = ?,data = ?, "
					+ "lastModifiedBy = ?, lastModifiedTime = ? where id = " +id;


			final PreparedStatementCreator psc = new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
					final PreparedStatement ps = connection.prepareStatement(propertyTypeUpdate, new String[] { "id" });
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
			propertyType.getAuditDetails().setCreatedTime(modifiedTime);


		}
		ResponseInfo responseInfo=responseInfoFactory.createResponseInfoFromRequestInfo(propertyTypeRequest.getRequestInfo(),true);

		PropertyTypeResponse propertyTypeResponse=new PropertyTypeResponse();
		propertyTypeResponse.setPropertyTypes(propertyTypeRequest.getPropertyTypes());
		propertyTypeResponse.setResponseInfo(responseInfo);
		return propertyTypeResponse;
	}

	/**
	 * Description : This api for searching propertyType master
	 * @param requestInfo
	 * @param tenantId
	 * @param ids
	 * @param name
	 * @param code
	 * @param nameLocal
	 * @param active
	 * @param orderNumber
	 * @param pageSize
	 * @param offSet
	 * @return
	 */


	@Override
	public PropertyTypeResponse getPropertyTypeMaster(RequestInfo requestInfo, String tenantId, Integer[] ids, String name, String code, String nameLocal, Boolean active, Integer orderNumber, Integer pageSize, Integer offSet) {

		StringBuffer propertyTypeSearchSql = new StringBuffer();

		propertyTypeSearchSql.append("select * from egpt_mstr_property where tenantid ='"+tenantId+"'");



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

		if(name!=null || nameLocal!=null || active!=null || orderNumber!=null)
			dataSearch.append(" AND data @> '");


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
		if(name!=null || active!=null || nameLocal!=null || active!=null || orderNumber != null)
			dataSearch.append("}'");

		if ( pageSize == null)
			pageSize = 30;
		if ( offSet == null )
			offSet = 0;
		propertyTypeSearchSql.append("offset "+offSet+ " limit "+pageSize);

		PropertyTypeResponse propertyTypeResponse = new PropertyTypeResponse();

		try {

			List<PropertyType> propertyTypes = jdbcTemplate.query(propertyTypeSearchSql.toString(), new BeanPropertyRowMapper(PropertyType.class));
			ResponseInfo responseInfo=responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo,true);
			Gson gson=new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
			for(PropertyType propertyType:propertyTypes){
				PropertyType propertyData=	gson.fromJson(propertyType.getData(),PropertyType.class);
				propertyType.setAuditDetails(propertyData.getAuditDetails());
				propertyType.setDescription(propertyData.getDescription());
				propertyType.setName(propertyData.getName());
				propertyType.setNameLocal(propertyData.getNameLocal());
				propertyType.setActive(propertyData.getActive());
				propertyType.setOrderNumber(propertyData.getOrderNumber());
			}
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
			occuapancyQuery.append("insert into egpt_mstr_occuapancy(tenantId,code,data,");
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
			occuapancy.getAuditDetails().setCreatedTime(createdTime);
			occuapancy.getAuditDetails().setLastModifiedTime(createdTime);

		}
		ResponseInfo responseInfo=responseInfoFactory.createResponseInfoFromRequestInfo(occuapancyMasterRequest.getRequestInfo(),true);

		OccuapancyMasterResponse occuapancyMasterResponse = new OccuapancyMasterResponse();

		occuapancyMasterResponse.setOccuapancyMasters(occuapancyMasterRequest.getOccuapancyMasters());

		occuapancyMasterResponse.setResponseInfo(responseInfo);

		return occuapancyMasterResponse;
	}

	/**
	 * Description : This api for updating occupancyType master
	 * @param tenantId
	 * @param id
	 * @param occuapancyRequest
	 * @return
	 */
	@Override
	public OccuapancyMasterResponse updateOccuapancyMaster(String tenantId, Long id, OccuapancyMasterRequest occuapancyRequest) {

		for(OccuapancyMaster occuapancyMaster: occuapancyRequest.getOccuapancyMasters()){


			Long modifiedTime=new Date().getTime();

			Gson gson=new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();

			String data=gson.toJson(occuapancyMaster);

			String occupancyTypeUpdate = "UPDATE egpt_mstr_occuapancy set tenantId = ?, code = ?,data = ?, "
					+ "lastModifiedBy = ?, lastModifiedTime = ? where id = " +id;


			final PreparedStatementCreator psc = new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
					final PreparedStatement ps = connection.prepareStatement(occupancyTypeUpdate, new String[] { "id" });
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
			occuapancyMaster.getAuditDetails().setLastModifiedTime(modifiedTime);


		}
		ResponseInfo responseInfo=responseInfoFactory.createResponseInfoFromRequestInfo(occuapancyRequest.getRequestInfo(),true);

		OccuapancyMasterResponse occuapancyResponse=new OccuapancyMasterResponse();
		occuapancyResponse.setOccuapancyMasters(occuapancyRequest.getOccuapancyMasters());
		occuapancyResponse.setResponseInfo(responseInfo);
		return occuapancyResponse;
	}

	/**
	 * Description : This api for searching occupancyType master
	 * @param requestInfo
	 * @param tenantId
	 * @param ids
	 * @param name
	 * @param code
	 * @param nameLocal
	 * @param active
	 * @param orderNumber
	 * @param pageSize
	 * @param offSet
	 * @return
	 */

	@Override
	public OccuapancyMasterResponse getOccuapancyMaster(RequestInfo requestInfo, String tenantId, Integer[] ids,
			String name, String code, String nameLocal, Boolean active, Integer orderNumber, Integer pageSize,
			Integer offSet) {

		StringBuffer occuapancySearchSql = new StringBuffer();

		occuapancySearchSql.append("select * from egpt_mstr_occuapancy where tenantid ='"+tenantId+"'");

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

		if(name!=null || nameLocal!=null || active!=null || orderNumber!=null)
			dataSearch.append(" AND data @> '");


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

		if(name!=null || active!=null || nameLocal!=null || active!=null || orderNumber != null)
			dataSearch.append("}'");

		if ( pageSize == null)
			pageSize = 30;
		if ( offSet == null )
			offSet = 0;
		occuapancySearchSql.append("offset "+offSet+ " limit "+pageSize);

		OccuapancyMasterResponse occuapancyResponse = new OccuapancyMasterResponse();

		try {

			List<OccuapancyMaster> occuapancyMaster = jdbcTemplate.query(occuapancySearchSql.toString(), new BeanPropertyRowMapper(OccuapancyMaster.class));
			ResponseInfo responseInfo=responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo,true);
			Gson gson=new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
			for(OccuapancyMaster occuapancyType:occuapancyMaster){
				OccuapancyMaster occuapancyData=	gson.fromJson(occuapancyType.getData(),OccuapancyMaster.class);
				occuapancyType.setAuditDetails(occuapancyData.getAuditDetails());
				occuapancyType.setDescription(occuapancyData.getDescription());
				occuapancyType.setName(occuapancyData.getName());
				occuapancyType.setNameLocal(occuapancyData.getNameLocal());
				occuapancyType.setActive(occuapancyData.getActive());
				occuapancyType.setOrderNumber(occuapancyData.getOrderNumber());
			}
			occuapancyResponse.setOccuapancyMasters(occuapancyMaster);
			occuapancyResponse.setResponseInfo(responseInfo);
		}
		catch (Exception e) {
			throw new PropertySearchException("invalid input",requestInfo);
		}
		return occuapancyResponse;

	}

	/**
	 *Description : This method for getting wall type master details
	 * @param tenantId
	 * @param code
	 * @param requestInfo
	 * @return masterModel
	 * @throws Exception
	 */

	@Override
	public WallTypeResponse getWallTypeMaster(RequestInfo requestInfo, 
			String tenantId, Integer[] ids, String name, String code, 
			String nameLocal, Integer pageSize, Integer offSet) throws Exception {

		StringBuffer wallTypeMasterSearchSQL = new StringBuffer();

		wallTypeMasterSearchSQL.append("SELECT * FROM egpt_mstr_wall where tenantid ='"+tenantId+"'");

		if (ids!=null && ids.length>0){

			String  wallTypeMasterIds = "";

			int count = 1;
			for (Integer id : ids){
				if (count<ids.length)
					wallTypeMasterIds = wallTypeMasterIds + id + ",";
				else
					wallTypeMasterIds = wallTypeMasterIds + id;

				count++;
			}

			wallTypeMasterSearchSQL.append(" AND id IN ("+ wallTypeMasterIds +")");

		}

		StringBuffer dataSearch = new StringBuffer();

		if (code!=null && !code.isEmpty())
			wallTypeMasterSearchSQL.append(" AND code = '"+code+"'");

		if(name!=null || nameLocal!=null)
			dataSearch.append(" AND data @> '");

		if (name!=null && !name.isEmpty())
			dataSearch.append("{ \"name\":\""+name+"\"");

		if (nameLocal!=null && !nameLocal.isEmpty()){
			if(name!=null && !name.isEmpty())
				dataSearch.append(" , {\"nameLocal\":\""+nameLocal+"\"");
			else
				dataSearch.append("{\"nameLocal\":\""+nameLocal+"\"");	
		}


		if(name!=null || nameLocal!=null)
			dataSearch.append("}'");

		wallTypeMasterSearchSQL.append( dataSearch.toString());

		if ( pageSize == null )
			pageSize = Integer.valueOf( environment.getProperty("default.page.size").trim());
		if ( offSet == null )
			offSet = Integer.valueOf( environment.getProperty("default.offset").trim());

		wallTypeMasterSearchSQL.append("offset "+offSet+" limit "+pageSize);

		WallTypeResponse wallTypeResponse = new WallTypeResponse();

		try {

			List<WallType> wallTypes = jdbcTemplate.query(wallTypeMasterSearchSQL.toString(), new BeanPropertyRowMapper(WallType.class));

			ResponseInfo responseInfo=responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo,true);
			Gson gson=new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
			for(WallType wallType:wallTypes){
				WallType wallData=	gson.fromJson(wallType.getData(),WallType.class);
				wallType.setAuditDetails(wallData.getAuditDetails());
				wallType.setDescription(wallData.getDescription());
				wallType.setName(wallData.getName());
				wallType.setNameLocal(wallType.getNameLocal());
			}
			wallTypeResponse.setWallTypes(wallTypes);
			wallTypeResponse.setResponseInfo(responseInfo);
		}
		catch (Exception e) {
			throw new PropertySearchException("invalid input",requestInfo);
		}

		return wallTypeResponse;
	}

	/**
	 *Description : This method for getting wall type master details
	 * @param tenantId
	 * @param code
	 * @param requestInfo
	 * @return masterModel
	 * @throws Exception
	 */

	@Override
	public WallTypeResponse createWallTypeMaster(String tenantId, WallTypeRequest wallTypeRequest) throws Exception {

		WallTypeResponse wallTypeResponse = new WallTypeResponse();

		List<WallType> wallTypes = wallTypeRequest.getWallTypes();

		for ( WallType wallType:wallTypes ) {

			long createdTime =new Date().getTime();

			Gson gson=new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();

			String data = gson.toJson(wallType);

			StringBuffer wallTypeMasterCreateSQL=new StringBuffer();

			wallTypeMasterCreateSQL.append("INSERT INTO egpt_mstr_wall")
			.append(" ( tenantid, code, data, createdby,")
			.append(" createdtime, lastmodifiedby, lastmodifiedtime) ")
			.append(" VALUES( ?, ?, ?, ?, ?, ?, ? )");

			final PreparedStatementCreator psc = new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {

					final PreparedStatement ps = connection.prepareStatement(wallTypeMasterCreateSQL.toString(), new String[] { "id" });

					PGobject jsonObject = new PGobject();
					jsonObject.setType("json");
					jsonObject.setValue(data);

					ps.setString(1, wallType.getTenantId());
					ps.setString(2, wallType.getCode());
					ps.setObject(3, jsonObject);
					ps.setString(4, wallType.getAuditDetails().getCreatedBy());
					ps.setLong(5, createdTime);
					ps.setString(6, wallType.getAuditDetails().getLastModifiedBy());
					ps.setLong(7, createdTime);

					return ps;
				}
			};

			// The newly generated key will be saved in this object
			final KeyHolder holder = new GeneratedKeyHolder();
			jdbcTemplate.update(psc, holder);
			wallType.setId(Long.valueOf(holder.getKey().intValue()));
			wallType.getAuditDetails().setCreatedTime(createdTime);
			wallType.getAuditDetails().setLastModifiedTime(createdTime);

		}

		ResponseInfo responseInfo=responseInfoFactory.createResponseInfoFromRequestInfo(wallTypeRequest.getRequestInfo(),true);


		wallTypeResponse.setWallTypes(wallTypeRequest.getWallTypes());

		wallTypeResponse.setResponseInfo(responseInfo);

		return wallTypeResponse;

	}

	/**
	 *Description : This method for getting wall type master details
	 * @param tenantId
	 * @param code
	 * @param requestInfo
	 * @return masterModel
	 * @throws Exception
	 */

	@Override
	public WallTypeResponse updateWallTypeMaster(String tenantId, Long id, WallTypeRequest wallTypeRequest) throws Exception {

		WallTypeResponse wallTypeResponse = new WallTypeResponse();

		List<WallType> wallTypes = wallTypeRequest.getWallTypes();

		for (WallType wallType : wallTypes) {

			long updatedTime =new Date().getTime();

			StringBuffer wallTypeMasterUpdateSQL=new StringBuffer();

			Gson gson=new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();

			String data=gson.toJson(wallType);

			wallTypeMasterUpdateSQL.append("UPDATE egpt_mstr_wall")
			.append(" SET tenantid = ?, code = ?, data =? ,")
			.append(" lastmodifiedby = ?, lastmodifieddate = ?")
			.append(" WHERE id = " + id );

			final PreparedStatementCreator psc = new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
					final PreparedStatement ps = connection.prepareStatement(wallTypeMasterUpdateSQL.toString());

					PGobject jsonObject = new PGobject();
					jsonObject.setType("json");
					jsonObject.setValue(data);

					ps.setString(1, wallType.getTenantId());
					ps.setString(3, wallType.getCode());
					ps.setObject(3, jsonObject);
					ps.setString(4, wallType.getAuditDetails().getLastModifiedBy());
					ps.setLong(5, updatedTime);
					ps.setString(6, data);

					return ps;
				}
			};

			try {

				jdbcTemplate.update(psc);
				wallType.getAuditDetails().setLastModifiedTime(updatedTime);
			}
			catch (Exception e) {

				throw new InvalidInputException(wallTypeRequest.getRequestInfo());
			}

			wallTypeResponse.setWallTypes(wallTypes);
		}

		ResponseInfo responseInfo=responseInfoFactory.createResponseInfoFromRequestInfo(wallTypeRequest.getRequestInfo(),true);

		wallTypeResponse.setResponseInfo(responseInfo);

		return wallTypeResponse;
	}


	/**
	 *Description : This method for getting usage master details
	 * @param tenantId
	 * @param code
	 * @param requestInfo
	 * @return masterModel
	 * @throws Exception
	 */

	@Override
	public UsageMasterResponse getUsageMaster(RequestInfo requestInfo,
			String tenantId, Integer[] ids, String name, String code, 
			String nameLocal, Integer pageSize, Integer offSet) throws Exception {

		StringBuffer usageMasterSearchSQL = new StringBuffer();

		usageMasterSearchSQL.append("SELECT * FROM egpt_mstr_usage where tenantid ='"+tenantId+"'");

		if (ids!=null && ids.length>0){

			String  usageMasterIds = "";

			int count = 1;
			for (Integer id : ids){
				if (count<ids.length)
					usageMasterIds = usageMasterIds + id + ",";
				else
					usageMasterIds = usageMasterIds + id;

				count++;
			}

			usageMasterSearchSQL.append(" AND id IN ("+ usageMasterIds +")");

		}

		StringBuffer dataSearch = new StringBuffer();

		if (code!=null && !code.isEmpty())
			usageMasterSearchSQL.append(" AND code = '"+code+"'");

		if(name!=null || nameLocal!=null)
			dataSearch.append(" AND data @> '");

		if (name!=null && !name.isEmpty())
			dataSearch.append("{ \"name\":\""+name+"\"");

		if (nameLocal!=null && !nameLocal.isEmpty()){
			if(name!=null && !name.isEmpty())
				dataSearch.append(" , {\"nameLocal\":\""+nameLocal+"\"");
			else
				dataSearch.append("{\"nameLocal\":\""+nameLocal+"\"");	
		}


		if(name!=null || nameLocal!=null)
			dataSearch.append("}'");

		usageMasterSearchSQL.append( dataSearch.toString());

		if ( pageSize == null )
			pageSize = Integer.valueOf( environment.getProperty("default.page.size").trim());
		if ( offSet == null )
			offSet = Integer.valueOf( environment.getProperty("default.offset").trim());

		usageMasterSearchSQL.append("offset "+offSet+" limit "+pageSize);

		UsageMasterResponse usageMasterResponse = new UsageMasterResponse();

		try {

			List<UsageMaster> usageTypes = jdbcTemplate.query(usageMasterSearchSQL.toString(), new BeanPropertyRowMapper(WallType.class));

			ResponseInfo responseInfo=responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo,true);

			Gson gson=new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
			for(UsageMaster usageType:usageTypes){
				UsageMaster usageData=	gson.fromJson(usageType.getData(),UsageMaster.class);
				usageType.setAuditDetails(usageData.getAuditDetails());
				usageType.setDescription(usageData.getDescription());
				usageType.setName(usageData.getName());
				usageType.setNameLocal(usageData.getNameLocal());
			}

			usageMasterResponse.setUsageMasters(usageTypes);
			usageMasterResponse.setResponseInfo(responseInfo);
		}
		catch (Exception e) {
			throw new PropertySearchException("invalid input",requestInfo);
		}

		return usageMasterResponse;

	}

	/**
	 *Description : This method for creating usageMaster
	 * @param tenantId
	 * @param usageMasters
	 * @return masterModel
	 * @throws Exception
	 */

	@Override
	public UsageMasterResponse createUsageMaster(String tenantId, UsageMasterRequest usageMasterRequest) throws Exception {

		UsageMasterResponse usageMasterResponse = new UsageMasterResponse();

		List<UsageMaster> usageMasters = usageMasterRequest.getUsageMasters();

		for (UsageMaster usageMaster:usageMasters) {

			Gson gson=new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();

			String data = gson.toJson(usageMaster);

			long createdTime =new Date().getTime();

			StringBuffer usageMasterCreateSQL=new StringBuffer();

			usageMasterCreateSQL.append("INSERT INTO egpt_mstr_usage")
			.append(" ( tenantid, code,")
			.append(" data, createdby, lastmodifiedby, createdtime, lastmodifiedtime) ")
			.append(" VALUES( ?, ?, ?, ?, ?, ?, ?)");

			final PreparedStatementCreator psc = new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {

					final PreparedStatement ps = connection.prepareStatement(usageMasterCreateSQL.toString(), new String[] { "id" });

					PGobject jsonObject = new PGobject();
					jsonObject.setType("json");
					jsonObject.setValue(data);

					ps.setString(1, usageMaster.getTenantId());
					ps.setString(3, usageMaster.getCode());
					ps.setObject(3, jsonObject);
					ps.setString(4,usageMaster.getAuditDetails().getCreatedBy());
					ps.setString(5, usageMaster.getAuditDetails().getLastModifiedBy());
					ps.setLong(6, createdTime);
					ps.setLong(7, createdTime);

					return ps;
				}
			};

			// The newly generated key will be saved in this object
			final KeyHolder holder = new GeneratedKeyHolder();
			jdbcTemplate.update(psc, holder);
			usageMaster.setId(Long.valueOf(holder.getKey().intValue()));
			usageMaster.getAuditDetails().setCreatedTime(createdTime);
			usageMaster.getAuditDetails().setLastModifiedTime(createdTime);

		}
		ResponseInfo responseInfo=responseInfoFactory.createResponseInfoFromRequestInfo(usageMasterRequest.getRequestInfo(),true);


		usageMasterResponse.setUsageMasters(usageMasterRequest.getUsageMasters());

		usageMasterResponse.setResponseInfo(responseInfo);

		return usageMasterResponse;

	}

	/**
	 *Description : This method for updating usageMaster
	 * @param tenantId
	 * @param usageMasters
	 * @return masterModel
	 * @throws Exception
	 */
	@Override
	public UsageMasterResponse updateUsageMaster(String tenantId, Long id, UsageMasterRequest usageMasterRequest) {

		UsageMasterResponse usageMasterResponse = new UsageMasterResponse();

		List<UsageMaster> usageMasters = usageMasterRequest.getUsageMasters();

		for (UsageMaster usageMaster:usageMasters) {

			long updatedTime =new Date().getTime();

			StringBuffer usageMasterUpdateSQL=new StringBuffer();

			Gson gson=new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();

			String data=gson.toJson(usageMaster);

			usageMasterUpdateSQL.append("UPDATE egpt_mstr_usage")
			.append(" SET tenantid = ?, code = ?,")
			.append(" data= ?, lastmodifiedby = ?, lastmodifiedtime = ?")
			.append(" WHERE id = " + id);

			final PreparedStatementCreator psc = new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
					final PreparedStatement ps = connection.prepareStatement(usageMasterUpdateSQL.toString());

					PGobject jsonObject = new PGobject();
					jsonObject.setType("json");
					jsonObject.setValue(data);
					ps.setString(1, tenantId);
					ps.setString(2, usageMaster.getCode());
					ps.setObject(3, jsonObject);
					ps.setString(4, usageMaster.getAuditDetails().getLastModifiedBy());
					ps.setLong(5, updatedTime);

					return ps;
				}
			};

			try {

				jdbcTemplate.update(psc);
				usageMaster.getAuditDetails().setLastModifiedTime(updatedTime);
			} catch (Exception e) {

				throw new InvalidInputException(usageMasterRequest.getRequestInfo());
			}

			usageMasterResponse.setUsageMasters(usageMasters);
		}

		ResponseInfo responseInfo=responseInfoFactory.createResponseInfoFromRequestInfo(usageMasterRequest.getRequestInfo(),true);

		usageMasterResponse.setResponseInfo(responseInfo);

		return usageMasterResponse;
	}
}
