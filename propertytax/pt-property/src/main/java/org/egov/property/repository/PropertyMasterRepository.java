package org.egov.property.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.models.Department;
import org.egov.models.Depreciation;
import org.egov.models.FloorType;
import org.egov.models.OccuapancyMaster;
import org.egov.models.PropertyType;
import org.egov.models.RequestInfo;
import org.egov.models.RoofType;
import org.egov.models.StructureClass;
import org.egov.models.UsageMaster;
import org.egov.models.WallType;
import org.egov.models.WoodType;
import org.egov.property.model.ExcludeFileds;
import org.egov.property.repository.builder.DepartmentQueryBuilder;
import org.egov.property.repository.builder.DepreciationBuilder;
import org.egov.property.repository.builder.FloorTypeBuilder;
import org.egov.property.repository.builder.OccuapancyQueryBuilder;
import org.egov.property.repository.builder.PropertyTypesBuilder;
import org.egov.property.repository.builder.RoofTypeBuilder;
import org.egov.property.repository.builder.SearchMasterBuilder;
import org.egov.property.repository.builder.StructureClassesBuilder;
import org.egov.property.repository.builder.UsageMasterBuilder;
import org.egov.property.repository.builder.UtilityBuilder;
import org.egov.property.repository.builder.WallTypesBuilder;
import org.egov.property.repository.builder.WoodTypeBuilder;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Repository
@SuppressWarnings({ "unchecked", "rawtypes" })
public class PropertyMasterRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * Description: create department prepared statement
	 * 
	 * @param tenantId
	 * @param department
	 * @param data
	 * @return keyholder id
	 */
	@Transactional
	public Long saveDepartment(String tenantId, Department department, String data) {

		Long createdTime = new Date().getTime();

		final PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(DepartmentQueryBuilder.INSERT_DEPARTMENT_QUERY,
						new String[] { "id" });
				ps.setString(1, department.getTenantId());
				ps.setString(2, department.getCode());
				PGobject jsonObject = new PGobject();
				jsonObject.setType("jsonb");
				jsonObject.setValue(data);
				ps.setObject(3, jsonObject);
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
		return Long.valueOf(holder.getKey().intValue());
	}

	/**
	 * Description: update department prepared statement
	 * 
	 * @param department
	 * @param data
	 * @param id
	 */
	@Transactional
	public Department updateDepartment(Department department, String data, Long id) {
		Long modifiedTime = new Date().getTime();

		String updateDepartmentSql = DepartmentQueryBuilder.UPDATE_DEPARTMENT_QUERY;
		String selectDepartmentSql = DepartmentQueryBuilder.SELECT_DEPARTMENT_CREATETIME;

		final PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(updateDepartmentSql, new String[] { "id" });
				ps.setString(1, department.getTenantId());
				ps.setString(2, department.getCode());
				PGobject jsonObject = new PGobject();
				jsonObject.setType("jsonb");
				jsonObject.setValue(data);
				ps.setObject(3, jsonObject);
				ps.setString(4, department.getAuditDetails().getLastModifiedBy());
				ps.setLong(5, modifiedTime);
				ps.setLong(6, id);
				return ps;
			}
		};
		Long createdTime = jdbcTemplate.queryForObject(selectDepartmentSql, new Object[] { department.getId() },
				Long.class);
		jdbcTemplate.update(psc);
		department.getAuditDetails().setCreatedTime(createdTime);
		return department;

	}

	/**
	 * department search query formation
	 * 
	 * @param tenantId
	 * @param ids
	 * @param name
	 * @param code
	 * @param nameLocal
	 * @param pageSize
	 * @param offSet
	 * @return
	 */
	public List<Department> searchDepartment(String tenantId, Integer[] ids, String name, String code, String nameLocal,
			Integer pageSize, Integer offSet) {

		List<Department> departments = new ArrayList<>();

		String departmentSearchSql = SearchMasterBuilder.buildSearchQuery("egpt_mstr_department", tenantId, ids, name,
				nameLocal, code, null, null, null, null, pageSize, offSet);
		Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
		departments = jdbcTemplate.query(departmentSearchSql.toString(), new BeanPropertyRowMapper(Department.class));
		for (Department department : departments) {
			Department departMentData = gson.fromJson(department.getData(), Department.class);
			department.setCategory(departMentData.getCategory());
			department.setName(departMentData.getName());
			department.setNameLocal(departMentData.getNameLocal());
			department.setDescription(departMentData.getDescription());
			department.setAuditDetails(departMentData.getAuditDetails());
		}

		return departments;
	}

	/**
	 * Description: create occuapancy prepared statement
	 * 
	 * @param tenantId
	 * @param occuapancy
	 * @param data
	 * @return keyholder id
	 */
	@Transactional
	public Long saveOccuapancy(String tenantId, OccuapancyMaster occuapancy, String data) {
		Long createdTime = new Date().getTime();

		final PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(OccuapancyQueryBuilder.INSERT_OCCUAPANCY_QUERY,
						new String[] { "id" });
				ps.setString(1, occuapancy.getTenantId());
				ps.setString(2, occuapancy.getCode());
				PGobject jsonObject = new PGobject();
				jsonObject.setType("jsonb");
				jsonObject.setValue(data);
				ps.setObject(3, jsonObject);
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
		return Long.valueOf(holder.getKey().intValue());

	}

	/**
	 * Description: update Occuapancy prepared statement
	 * 
	 * @param tenantId
	 * @param id
	 * @param occuapancyMaster
	 * @param data
	 */
	public OccuapancyMaster updateOccuapancy(OccuapancyMaster occuapancyMaster, String data) {

		Long modifiedTime = new Date().getTime();

		String updateOccupancySql = OccuapancyQueryBuilder.UPDATE_OCCUAPANCY_QUERY;
		String selectOccupancySql = OccuapancyQueryBuilder.SELECT_OCCUAPANCY_CREATETIME;

		final PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(updateOccupancySql, new String[] { "id" });
				ps.setString(1, occuapancyMaster.getTenantId());
				ps.setString(2, occuapancyMaster.getCode());
				PGobject jsonObject = new PGobject();
				jsonObject.setType("jsonb");
				jsonObject.setValue(data);
				ps.setObject(3, jsonObject);
				ps.setString(4, occuapancyMaster.getAuditDetails().getLastModifiedBy());
				ps.setLong(5, modifiedTime);
				ps.setLong(6, occuapancyMaster.getId());
				return ps;
			}
		};

		Long createdTime = jdbcTemplate.queryForObject(selectOccupancySql, new Object[] { occuapancyMaster.getId() },
				Long.class);
		jdbcTemplate.update(psc);
		occuapancyMaster.getAuditDetails().setCreatedTime(createdTime);
		return occuapancyMaster;

	}

	/**
	 * Description: occupancy search query
	 * 
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
	public List<OccuapancyMaster> searchOccupancy(RequestInfo requestInfo, String tenantId, Integer[] ids, String name,
			String code, String nameLocal, Boolean active, Integer orderNumber, Integer pageSize, Integer offSet) {

		List<OccuapancyMaster> occupancyMasters = new ArrayList<>();

		String occuapancySearchSql = SearchMasterBuilder.buildSearchQuery("egpt_mstr_occuapancy", tenantId, ids, name,
				nameLocal, code, active, null, orderNumber, null, pageSize, offSet);

		Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
		occupancyMasters = jdbcTemplate.query(occuapancySearchSql.toString(),
				new BeanPropertyRowMapper(OccuapancyMaster.class));
		for (OccuapancyMaster occuapancyType : occupancyMasters) {
			OccuapancyMaster occuapancyData = gson.fromJson(occuapancyType.getData(), OccuapancyMaster.class);
			occuapancyType.setAuditDetails(occuapancyData.getAuditDetails());
			occuapancyType.setDescription(occuapancyData.getDescription());
			occuapancyType.setName(occuapancyData.getName());
			occuapancyType.setNameLocal(occuapancyData.getNameLocal());
			occuapancyType.setActive(occuapancyData.getActive());
			occuapancyType.setOrderNumber(occuapancyData.getOrderNumber());
		}
		return occupancyMasters;
	}

	/**
	 * Description: create propertytype preparedstatement
	 * 
	 * @param tenantId
	 * @param propertyType
	 * @param data
	 * @return keyholder id
	 */
	@Transactional
	public Long savePropertyType(String tenantId, PropertyType propertyType, String data) {

		Long createdTime = new Date().getTime();

		final PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection
						.prepareStatement(PropertyTypesBuilder.INSERT_PROPERTYTYPES_QUERY, new String[] { "id" });
				ps.setString(1, propertyType.getTenantId());
				ps.setString(2, propertyType.getCode());
				PGobject jsonObject = new PGobject();
				jsonObject.setType("jsonb");
				jsonObject.setValue(data);
				ps.setObject(3, jsonObject);
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

		return Long.valueOf(holder.getKey().intValue());

	}

	/**
	 * Description: update propertytype preparedstatement
	 * 
	 * @param tenantId
	 * @param id
	 * @param propertyType
	 * @param data
	 */
	@Transactional
	public PropertyType updatePropertyType(PropertyType propertyType, String data) {

		Long modifiedTime = new Date().getTime();

		String updatePropertyTypeSql = PropertyTypesBuilder.UPDATE_PROPERTYTYPES_QUERY;
		String selectPropertyTypeSql = PropertyTypesBuilder.SELECT_PROPERTYTYPES_CREATETIME;

		final PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(updatePropertyTypeSql, new String[] { "id" });
				ps.setString(1, propertyType.getTenantId());
				ps.setString(2, propertyType.getCode());
				PGobject jsonObject = new PGobject();
				jsonObject.setType("jsonb");
				jsonObject.setValue(data);
				ps.setObject(3, jsonObject);
				ps.setString(4, propertyType.getAuditDetails().getLastModifiedBy());
				ps.setLong(5, modifiedTime);
				ps.setLong(6, propertyType.getId());
				return ps;
			}
		};
		Long createdTime = jdbcTemplate.queryForObject(selectPropertyTypeSql, new Object[] { propertyType.getId() },
				Long.class);
		jdbcTemplate.update(psc);
		propertyType.getAuditDetails().setCreatedTime(createdTime);
		return propertyType;

	}

	/**
	 * Description: PropertyType search query
	 * 
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
	public List<PropertyType> searchPropertyType(RequestInfo requestInfo, String tenantId, Integer[] ids, String name,
			String code, String nameLocal, Boolean active, Integer orderNumber, Integer pageSize, Integer offSet) {

		List<PropertyType> propertyTypes = new ArrayList<>();

		String propertyTypeSearchSql = SearchMasterBuilder.buildSearchQuery("egpt_mstr_propertytype", tenantId, ids,
				name, nameLocal, code, active, null, orderNumber, null, pageSize, offSet);

		Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
		propertyTypes = jdbcTemplate.query(propertyTypeSearchSql.toString(),
				new BeanPropertyRowMapper(PropertyType.class));
		for (PropertyType propertyType : propertyTypes) {
			PropertyType propertyData = gson.fromJson(propertyType.getData(), PropertyType.class);
			propertyType.setAuditDetails(propertyData.getAuditDetails());
			propertyType.setDescription(propertyData.getDescription());
			propertyType.setName(propertyData.getName());
			propertyType.setNameLocal(propertyData.getNameLocal());
			propertyType.setActive(propertyData.getActive());
			propertyType.setOrderNumber(propertyData.getOrderNumber());
		}
		return propertyTypes;
	}

	/**
	 * Description : This method for saving floor type master
	 * 
	 * @param floorType
	 * @param data
	 * @return
	 */

	@Transactional
	public Long saveFloorType(FloorType floorType, String data) {

		Long createdTime = new Date().getTime();
		String saveFloorTypeSql = FloorTypeBuilder.INSERT_FLOOR_QUERY;
		final PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(saveFloorTypeSql, new String[] { "id" });
				ps.setString(1, floorType.getTenantId());
				ps.setString(2, floorType.getCode());
				PGobject jsonObject = new PGobject();
				jsonObject.setType("jsonb");
				jsonObject.setValue(data);
				ps.setObject(3, jsonObject);
				ps.setString(4, floorType.getAuditDetails().getCreatedBy());
				ps.setString(5, floorType.getAuditDetails().getLastModifiedBy());
				ps.setLong(6, createdTime);
				ps.setLong(7, createdTime);
				return ps;
			}

		};

		// The newly generated key will be saved in this object
		final KeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(psc, holder);
		return Long.valueOf(holder.getKey().intValue());
	}

	/**
	 * This method for updating floor type
	 * 
	 * @param floorType
	 * @param data
	 * @param id
	 */

	@Transactional
	public FloorType updateFloorType(FloorType floorType, String data) {

		Long updatedTime = new Date().getTime();
		String updateFloorTypeSql = FloorTypeBuilder.UPDATE_FLOOR_QUERY;
		String selectFloorCreateTime = FloorTypeBuilder.SELECT_FLOOR_CREATETIME;

		final PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(updateFloorTypeSql);
				ps.setString(1, floorType.getTenantId());
				ps.setString(2, floorType.getCode());
				PGobject jsonObject = new PGobject();
				jsonObject.setType("jsonb");
				jsonObject.setValue(data);
				ps.setObject(3, jsonObject);
				ps.setString(4, floorType.getAuditDetails().getLastModifiedBy());
				ps.setLong(5, updatedTime);
				ps.setLong(6, floorType.getId());

				return ps;
			}
		};

		jdbcTemplate.update(psc);

		Long createdTime = jdbcTemplate.queryForObject(selectFloorCreateTime, new Object[] { floorType.getId() },
				Long.class);

		floorType.getAuditDetails().setCreatedTime(createdTime);
		return floorType;

	}

	/**
	 * This method will use for searching floor type
	 * 
	 * @param tenantId
	 * @param ids
	 * @param name
	 * @param code
	 * @param nameLocal
	 * @param pageSize
	 * @param offSet
	 * @return
	 */
	public List<FloorType> searchFloorType(String tenantId, Integer[] ids, String name, String code, String nameLocal,
			Integer pageSize, Integer offSet) {
		List<FloorType> floorTypes = new ArrayList<FloorType>();

		String searchQuery = SearchMasterBuilder.buildSearchQuery("egpt_mstr_floortype", tenantId, ids, name, nameLocal,
				code, null, null, null, null, pageSize, offSet);

		Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
		floorTypes = jdbcTemplate.query(searchQuery.toString(), new BeanPropertyRowMapper(FloorType.class));
		for (FloorType floor : floorTypes) {
			FloorType floorData = gson.fromJson(floor.getData(), FloorType.class);

			floor.setName(floorData.getName());
			floor.setNameLocal(floorData.getNameLocal());
			floor.setDescription(floorData.getDescription());
			floor.setAuditDetails(floorData.getAuditDetails());
		}

		return floorTypes;
	}

	/**
	 * This method for saving roof type
	 * 
	 * @param roofType
	 * @param data
	 * @return
	 */

	@Transactional
	public Long saveRoofType(RoofType roofType, String data) {

		Long createdTime = new Date().getTime();
		String SaveRoofTypeSql = RoofTypeBuilder.INSERT_ROOF_TYPE;
		final PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(SaveRoofTypeSql, new String[] { "id" });
				ps.setString(1, roofType.getTenantId());
				ps.setString(2, roofType.getCode());
				PGobject jsonObject = new PGobject();
				jsonObject.setType("jsonb");
				jsonObject.setValue(data);
				ps.setObject(3, jsonObject);
				ps.setString(4, roofType.getAuditDetails().getCreatedBy());
				ps.setString(5, roofType.getAuditDetails().getLastModifiedBy());
				ps.setLong(6, createdTime);
				ps.setLong(7, createdTime);
				return ps;
			}

		};

		// The newly generated key will be saved in this object
		final KeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(psc, holder);
		return Long.valueOf(holder.getKey().intValue());
	}

	/**
	 * This method will use for updating roof type
	 * 
	 * @param roofType
	 * @param data
	 * @param id
	 */

	@Transactional
	public RoofType updateRoofType(RoofType roofType, String data) {

		long updatedTime = new Date().getTime();
		String updateRoofTypeSql = RoofTypeBuilder.UPDATE_ROOF_QUERY;
		String selectRoofTypeSql = RoofTypeBuilder.SELECT_ROOF_CREATETIME;

		final PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(updateRoofTypeSql);
				ps.setString(1, roofType.getTenantId());
				ps.setString(2, roofType.getCode());
				PGobject jsonObject = new PGobject();
				jsonObject.setType("jsonb");
				jsonObject.setValue(data);
				ps.setObject(3, jsonObject);
				ps.setString(4, roofType.getAuditDetails().getLastModifiedBy());
				ps.setLong(5, updatedTime);
				ps.setLong(6, roofType.getId());
				return ps;
			}
		};

		Long createdTime = jdbcTemplate.queryForObject(selectRoofTypeSql, new Object[] { roofType.getId() },
				Long.class);
		jdbcTemplate.update(psc);
		roofType.getAuditDetails().setCreatedTime(createdTime);
		return roofType;
	}

	/**
	 * This method will use for searching roof type
	 * 
	 * @param tenantId
	 * @param ids
	 * @param name
	 * @param code
	 * @param nameLocal
	 * @param pageSize
	 * @param offSet
	 * @return
	 */
	public List<RoofType> searchRoofType(String tenantId, Integer[] ids, String name, String code, String nameLocal,
			Integer pageSize, Integer offSet) {
		List<RoofType> roofTypes = new ArrayList<RoofType>();

		String searchQuery = SearchMasterBuilder.buildSearchQuery("egpt_mstr_Rooftype", tenantId, ids, name, nameLocal,
				code, null, null, null, null, pageSize, offSet);

		Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
		roofTypes = jdbcTemplate.query(searchQuery.toString(), new BeanPropertyRowMapper(RoofType.class));
		for (RoofType roof : roofTypes) {
			FloorType floorData = gson.fromJson(roof.getData(), FloorType.class);

			roof.setName(floorData.getName());
			roof.setNameLocal(floorData.getNameLocal());
			roof.setDescription(floorData.getDescription());
			roof.setAuditDetails(floorData.getAuditDetails());
		}

		return roofTypes;
	}

	/**
	 * This method will use for saving wood type
	 * 
	 * @param woodType
	 * @param data
	 * @return
	 */

	@Transactional
	public Long saveWoodType(WoodType woodType, String data) {

		Long createdTime = new Date().getTime();
		String saveWoodTypeSql = WoodTypeBuilder.INSERT_WOOD_QUERY;
		final PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(saveWoodTypeSql, new String[] { "id" });
				ps.setString(1, woodType.getTenantId());
				ps.setString(2, woodType.getCode());
				PGobject jsonObject = new PGobject();
				jsonObject.setType("jsonb");
				jsonObject.setValue(data);
				ps.setObject(3, jsonObject);
				ps.setString(4, woodType.getAuditDetails().getCreatedBy());
				ps.setString(5, woodType.getAuditDetails().getLastModifiedBy());
				ps.setLong(6, createdTime);
				ps.setLong(7, createdTime);
				return ps;
			}

		};

		// The newly generated key will be saved in this object
		final KeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(psc, holder);
		return Long.valueOf(holder.getKey().intValue());
	}

	/**
	 * This method will use for updating wood type
	 * 
	 * @param woodType
	 * @param data
	 * @param id
	 */

	@Transactional
	public WoodType updateWoodType(WoodType woodType, String data) {

		long updatedTime = new Date().getTime();
		String updateWoodTypeSql = WoodTypeBuilder.UPDATE_WOOD_QUERY;
		String selectWoodCreateTime = WoodTypeBuilder.SELECT_WOOD_CREATETIME;

		final PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(updateWoodTypeSql);
				ps.setString(1, woodType.getTenantId());
				ps.setString(2, woodType.getCode());
				PGobject jsonObject = new PGobject();
				jsonObject.setType("jsonb");
				jsonObject.setValue(data);
				ps.setObject(3, jsonObject);
				ps.setString(4, woodType.getAuditDetails().getLastModifiedBy());
				ps.setLong(5, updatedTime);
				ps.setLong(6, woodType.getId());

				return ps;
			}
		};

		Long createdTime = jdbcTemplate.queryForObject(selectWoodCreateTime, new Object[] { woodType.getId() },
				Long.class);
		jdbcTemplate.update(psc);
		woodType.getAuditDetails().setCreatedTime(createdTime);
		return woodType;

	}

	/**
	 * This method for searching wood type based on given paramaters
	 * 
	 * @param tenantId
	 * @param ids
	 * @param name
	 * @param code
	 * @param nameLocal
	 * @param pageSize
	 * @param offSet
	 * @return
	 */

	public List<WoodType> searchWoodType(String tenantId, Integer[] ids, String name, String code, String nameLocal,
			Integer pageSize, Integer offSet) {
		List<WoodType> woodTypes = new ArrayList<WoodType>();

		String searchQuery = SearchMasterBuilder.buildSearchQuery("egpt_mstr_Woodtype", tenantId, ids, name, nameLocal,
				code, null, null, null, null, pageSize, offSet);

		Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
		woodTypes = jdbcTemplate.query(searchQuery.toString(), new BeanPropertyRowMapper(WoodType.class));
		for (WoodType wood : woodTypes) {
			FloorType floorData = gson.fromJson(wood.getData(), FloorType.class);

			wood.setName(floorData.getName());
			wood.setNameLocal(floorData.getNameLocal());
			wood.setDescription(floorData.getDescription());
			wood.setAuditDetails(floorData.getAuditDetails());
		}

		return woodTypes;
	}

	/**
	 * This method for searching wall types
	 * 
	 * @param tenantId
	 * @param ids
	 * @param name
	 * @param code
	 * @param nameLocal
	 * @param pageSize
	 * @param offSet
	 * @return
	 */

	public List<WallType> searchWallType(String tenantId, Integer[] ids, String name, String code, String nameLocal,
			Integer pageSize, Integer offSet) {
		String wallTypeMasterSearchQuery = SearchMasterBuilder.buildSearchQuery("egpt_mstr_walltype", tenantId, ids,
				name, nameLocal, code, null, null, null, null, pageSize, offSet);
		List<WallType> wallTypes = jdbcTemplate.query(wallTypeMasterSearchQuery,
				new BeanPropertyRowMapper(WallType.class));

		Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();

		for (WallType wallType : wallTypes) {
			WallType wallData = gson.fromJson(wallType.getData(), WallType.class);
			wallType.setCode(wallData.getCode());
			wallType.setNameLocal(wallData.getNameLocal());
			wallType.setAuditDetails(wallData.getAuditDetails());
			wallType.setDescription(wallData.getDescription());
			wallType.setName(wallData.getName());
		}
		return wallTypes;
	}

	/**
	 * This method for searching usage types
	 * 
	 * @param tenantId
	 * @param ids
	 * @param name
	 * @param code
	 * @param nameLocal
	 * @param active
	 * @param isResidential
	 * @param orderNumber
	 * @param pageSize
	 * @param offSet
	 * @return
	 */

	public List<UsageMaster> searchUsage(String tenantId, Integer[] ids, String name, String code, String nameLocal,
			Boolean active, Boolean isResidential, Integer orderNumber, Integer pageSize, Integer offSet) {

		String usageMasterSearchQuery = SearchMasterBuilder.buildSearchQuery("egpt_mstr_usage", tenantId, ids, name,
				nameLocal, code, active, isResidential, orderNumber, null, pageSize, offSet);
		List<UsageMaster> usageTypes = jdbcTemplate.query(usageMasterSearchQuery,
				new BeanPropertyRowMapper(UsageMaster.class));

		Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
		for (UsageMaster usageType : usageTypes) {

			UsageMaster usageData = gson.fromJson(usageType.getData(), UsageMaster.class);
			usageType.setActive(usageData.getActive());
			usageType.setIsResidential(usageData.getIsResidential());
			usageType.setOrderNumber(usageData.getOrderNumber());

			usageType.setAuditDetails(usageData.getAuditDetails());
			usageType.setDescription(usageData.getDescription());
			usageType.setName(usageData.getName());
			usageType.setNameLocal(usageData.getNameLocal());
		}

		return usageTypes;
	}

	public Long saveStructureClsses(String tenantId, StructureClass structureClass, String data) {

		Long createdTime = new Date().getTime();

		String structureClassInsert = StructureClassesBuilder.INSERT_STRUCTURECLASSES_QUERY;

		final PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(structureClassInsert, new String[] { "id" });

				PGobject jsonObject = new PGobject();
				jsonObject.setType("jsonb");
				jsonObject.setValue(data);

				ps.setString(1, structureClass.getTenantId());
				ps.setString(2, structureClass.getCode());
				ps.setObject(3, jsonObject);
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

		return Long.valueOf(holder.getKey().intValue());

	}

	public StructureClass updateStructureClsses(StructureClass structureClass, String data) {

		Long updatedTime = new Date().getTime();

		String structureClassesUpdate = StructureClassesBuilder.UPDATE_STRUCTURECLASSES_QUERY;
		String selectStructureClassessSql = StructureClassesBuilder.SELECT_STRUCTURECLASSES_CREATETIME;

		final PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(structureClassesUpdate);

				PGobject jsonObject = new PGobject();
				jsonObject.setType("jsonb");
				jsonObject.setValue(data);

				ps.setString(1, structureClass.getTenantId());
				ps.setString(2, structureClass.getCode());
				ps.setObject(3, jsonObject);
				ps.setString(4, structureClass.getAuditDetails().getLastModifiedBy());
				ps.setLong(5, updatedTime);
				ps.setLong(6, structureClass.getId());

				return ps;
			}
		};
		Long createdTime = jdbcTemplate.queryForObject(selectStructureClassessSql,
				new Object[] { structureClass.getId() }, Long.class);
		jdbcTemplate.update(psc);
		structureClass.getAuditDetails().setCreatedTime(createdTime);
		return structureClass;
	}

	/**
	 * This method for saving usage master
	 * 
	 * @param usageMaster
	 * @param data
	 * @return
	 */

	public Long saveUsageMaster(UsageMaster usageMaster, String data) {

		Long createdTime = new Date().getTime();

		String usageInsert = UsageMasterBuilder.INSERT_USAGEMASTER_QUERY;

		final PreparedStatementCreator psc = new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {

				final PreparedStatement ps = connection.prepareStatement(usageInsert, new String[] { "id" });

				ps.setString(1, usageMaster.getTenantId());
				ps.setString(2, usageMaster.getCode());
				ps.setLong(3, usageMaster.getParent());
				PGobject jsonObject = new PGobject();
				jsonObject.setType("jsonb");
				jsonObject.setValue(data);
				ps.setObject(4, jsonObject);
				ps.setString(5, usageMaster.getAuditDetails().getCreatedBy());
				ps.setString(6, usageMaster.getAuditDetails().getLastModifiedBy());
				ps.setLong(7, createdTime);
				ps.setLong(8, createdTime);
				return ps;
			}
		};

		final KeyHolder holder = new GeneratedKeyHolder();

		jdbcTemplate.update(psc, holder);

		return Long.valueOf(holder.getKey().intValue());
	}

	/**
	 * This method for updating usagemaster
	 * 
	 * @param tenantId
	 * @param id
	 * @param usageMaster
	 * @param data
	 */
	public UsageMaster updateUsageMaster(UsageMaster usageMaster, String data) {

		Long updatedTime = new Date().getTime();

		String usageUpdate = UsageMasterBuilder.UPDATE_USAGEMASTER_QUERY;
		String selectUsageMasterSql = UsageMasterBuilder.SELECT_USAGEMASTER_CREATETIME;

		final PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(usageUpdate);

				PGobject jsonObject = new PGobject();
				jsonObject.setType("jsonb");
				jsonObject.setValue(data);
				ps.setString(1, usageMaster.getTenantId());
				ps.setString(2, usageMaster.getCode());
				ps.setLong(3, usageMaster.getParent());
				ps.setObject(4, jsonObject);
				ps.setString(5, usageMaster.getAuditDetails().getLastModifiedBy());
				ps.setLong(6, updatedTime);
				ps.setLong(7, usageMaster.getId());
				return ps;
			}
		};
		Long createdTime = jdbcTemplate.queryForObject(selectUsageMasterSql, new Object[] { usageMaster.getId() },
				Long.class);
		jdbcTemplate.update(psc);
		usageMaster.getAuditDetails().setCreatedTime(createdTime);
		return usageMaster;
	}

	/**
	 * This method for saving wall types
	 * 
	 * @param wallType
	 * @param data
	 * @return
	 */

	public Long saveWallTypes(WallType wallType, String data) {

		Long createdTime = new Date().getTime();

		String wallTypeInsert = WallTypesBuilder.INSERT_WALLTYPES_QUERY;

		final PreparedStatementCreator psc = new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {

				final PreparedStatement ps = connection.prepareStatement(wallTypeInsert, new String[] { "id" });
				PGobject jsonObject = new PGobject();
				jsonObject.setType("jsonb");
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

		final KeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(psc, holder);

		return Long.valueOf(holder.getKey().intValue());
	}

	/**
	 * This method for updating wall type
	 * 
	 * @param tenantId
	 * @param id
	 * @param wallType
	 * @param data
	 */

	public WallType updateWallTypes(WallType wallType, String data) {

		Long updatedTime = new Date().getTime();

		String wallTypesUpdate = WallTypesBuilder.UPDATE_WALLTYPES_QUERY;
		String selectWallTypeSql = WallTypesBuilder.SELECT_WALLTYPES_CREATETIME;

		final PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(wallTypesUpdate);

				PGobject jsonObject = new PGobject();
				jsonObject.setType("jsonb");
				jsonObject.setValue(data);

				ps.setString(1, wallType.getTenantId());
				ps.setString(2, wallType.getCode());
				ps.setObject(3, jsonObject);
				ps.setString(4, wallType.getAuditDetails().getLastModifiedBy());
				ps.setLong(5, updatedTime);
				ps.setLong(6, wallType.getId());
				return ps;
			}
		};
		Long createdTime = jdbcTemplate.queryForObject(selectWallTypeSql, new Object[] { wallType.getId() },
				Long.class);
		jdbcTemplate.update(psc);
		wallType.getAuditDetails().setCreatedTime(createdTime);
		return wallType;
	}

	/**
	 * This method for searching structureclass
	 * 
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

	public List<StructureClass> searchStructureClass(String tenantId, Integer[] ids, String name, String code,
			String nameLocal, Boolean active, Integer orderNumber, Integer pageSize, Integer offSet) {

		String structureClassSearchQuery = SearchMasterBuilder.buildSearchQuery("egpt_mstr_structureclass", tenantId,
				ids, name, nameLocal, code, active, null, orderNumber, null, pageSize, offSet);

		List<StructureClass> structureClasses = jdbcTemplate.query(structureClassSearchQuery,
				new BeanPropertyRowMapper(StructureClass.class));

		Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();

		for (StructureClass structureType : structureClasses) {
			StructureClass structureData = gson.fromJson(structureType.getData(), StructureClass.class);
			structureType.setCode(structureData.getCode());
			structureType.setAuditDetails(structureData.getAuditDetails());
			structureType.setDescription(structureData.getDescription());
			structureType.setName(structureData.getName());
			structureType.setNameLocal(structureData.getNameLocal());
			structureType.setActive(structureData.getActive());
			structureType.setOrderNumber(structureData.getOrderNumber());
		}

		return structureClasses;

	}

	/**
	 * This will check whether any record exists with the given tenantId & code
	 * in database or not
	 * 
	 * @param tenantId
	 * @param code
	 * @return True / false if record exists / record does n't exists
	 */
	public Boolean checkWhetherRecordExits(String tenantId, String code, String tableName, Long id) {

		Boolean isExists = Boolean.TRUE;

		String query = UtilityBuilder.getUniqueTenantCodeQuery(tableName, code, tenantId, id);

		int count = 0;
		try {

			count = (Integer) jdbcTemplate.queryForObject(query, Integer.class);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		if (count == 0)
			isExists = Boolean.FALSE;

		return isExists;

	}

	@Transactional
	public Long createDepreciation(Depreciation depreciation, String data) {

		String insertDepreciationQuery = DepreciationBuilder.INSERT_DEPRECIATION_QUERY;
		Long createdTime = new Date().getTime();
		final PreparedStatementCreator psc = new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {

				final PreparedStatement ps = connection.prepareStatement(insertDepreciationQuery,
						new String[] { "id" });

				PGobject jsonObject = new PGobject();
				jsonObject.setType("jsonb");
				jsonObject.setValue(data);

				ps.setString(1, depreciation.getTenantId());
				ps.setString(2, depreciation.getCode());
				ps.setObject(3, jsonObject);
				ps.setString(4, depreciation.getAuditDetails().getCreatedBy());
				ps.setString(5, depreciation.getAuditDetails().getLastModifiedBy());
				ps.setLong(6, createdTime);
				ps.setLong(7, createdTime);

				return ps;
			}
		};

		final KeyHolder holder = new GeneratedKeyHolder();

		jdbcTemplate.update(psc, holder);

		return Long.valueOf(holder.getKey().intValue());

	}

	@Transactional
	public void updateDepreciation(Depreciation depreciation, String data) {

		String updateDepreciation = DepreciationBuilder.UPDATE_DEPRECIATION_QUERY;
		Long updatedTime = new Date().getTime();
		final PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(updateDepreciation);

				PGobject jsonObject = new PGobject();
				jsonObject.setType("jsonb");
				jsonObject.setValue(data);

				ps.setString(1, depreciation.getTenantId());
				ps.setString(2, depreciation.getCode());
				ps.setObject(3, jsonObject);
				ps.setString(4, depreciation.getAuditDetails().getLastModifiedBy());
				ps.setLong(5, updatedTime);
				ps.setLong(6, depreciation.getId());
				return ps;
			}
		};
		Long createdTime = jdbcTemplate.queryForObject(DepreciationBuilder.SELECT_DEPRECIATION_CREATETIME,
				new Object[] { depreciation.getId() }, Long.class);

		jdbcTemplate.update(psc);
		depreciation.getAuditDetails().setCreatedTime(createdTime);

	}

	public List<Depreciation> searchDepreciations(String tenantId, Integer[] ids, Integer fromYear, Integer toYear,
			String code, String nameLocal, Integer pageSize, Integer offset) {

		String searchDepreciationSql = DepreciationBuilder.searchQuery(tenantId, ids, fromYear, toYear, code, nameLocal,
				pageSize, offset);

		List<Depreciation> depreciations = jdbcTemplate.query(searchDepreciationSql,
				new BeanPropertyRowMapper(Depreciation.class));

		Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
		for (Depreciation depreciation : depreciations) {

			Depreciation usageData = gson.fromJson(depreciation.getData(), Depreciation.class);

			depreciation.setAuditDetails(usageData.getAuditDetails());
			depreciation.setDescription(usageData.getDescription());
			depreciation.setNameLocal(usageData.getNameLocal());
		}

		return depreciations;
	}
}
