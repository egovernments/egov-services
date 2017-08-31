package org.egov.property.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.egov.enums.ApplicationEnum;
import org.egov.models.Apartment;
import org.egov.models.AuditDetails;
import org.egov.models.Department;
import org.egov.models.Depreciation;
import org.egov.models.DocumentType;
import org.egov.models.FloorType;
import org.egov.models.MutationMaster;
import org.egov.models.OccuapancyMaster;
import org.egov.models.PropertyType;
import org.egov.models.RequestInfo;
import org.egov.models.RoofType;
import org.egov.models.StructureClass;
import org.egov.models.UsageMaster;
import org.egov.models.WallType;
import org.egov.models.WoodType;
import org.egov.property.model.ExcludeFileds;
import org.egov.property.repository.builder.ApartmentBuilder;
import org.egov.property.repository.builder.AuditDetailsBuilder;
import org.egov.property.repository.builder.DepartmentQueryBuilder;
import org.egov.property.repository.builder.DepreciationBuilder;
import org.egov.property.repository.builder.DocumentTypeBuilder;
import org.egov.property.repository.builder.FloorTypeBuilder;
import org.egov.property.repository.builder.MutationMasterBuilder;
import org.egov.property.repository.builder.OccuapancyQueryBuilder;
import org.egov.property.repository.builder.PropertyTypesBuilder;
import org.egov.property.repository.builder.RoofTypeBuilder;
import org.egov.property.repository.builder.SearchMasterBuilder;
import org.egov.property.repository.builder.StructureClassesBuilder;
import org.egov.property.repository.builder.UsageMasterBuilder;
import org.egov.property.repository.builder.UtilityBuilder;
import org.egov.property.repository.builder.WallTypesBuilder;
import org.egov.property.repository.builder.WoodTypeBuilder;
import org.egov.property.utility.ConstantUtility;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

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
	public Long saveDepartment(String tenantId, Department department, String data) {

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
				ps.setLong(6, department.getAuditDetails().getCreatedTime());
				ps.setLong(7, department.getAuditDetails().getLastModifiedTime());
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
	 */
	public void updateDepartment(Department department, String data) {

		String updateDepartmentSql = DepartmentQueryBuilder.UPDATE_DEPARTMENT_QUERY;

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
				ps.setLong(5, department.getAuditDetails().getLastModifiedTime());
				ps.setLong(6, department.getId());
				return ps;
			}
		};

		jdbcTemplate.update(psc);
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
		List<Object> preparedStatementValues = new ArrayList<>();

		String departmentSearchSql = SearchMasterBuilder.buildSearchQuery(ConstantUtility.DEPARTMENT_TABLE_NAME,
				tenantId, ids, name, nameLocal, code, null, null, null, null, pageSize, offSet, preparedStatementValues,
				null, null, null, null, null);
		Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
		departments = jdbcTemplate.query(departmentSearchSql.toString(), preparedStatementValues.toArray(),
				new BeanPropertyRowMapper(Department.class));
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

	public Long saveOccuapancy(String tenantId, OccuapancyMaster occuapancy, String data) {

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
				ps.setLong(6, occuapancy.getAuditDetails().getCreatedTime());
				ps.setLong(7, occuapancy.getAuditDetails().getLastModifiedTime());
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
	 * @param occuapancyMaster
	 * @param data
	 */
	public void updateOccuapancy(OccuapancyMaster occuapancyMaster, String data) {

		String updateOccupancySql = OccuapancyQueryBuilder.UPDATE_OCCUAPANCY_QUERY;

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
				ps.setLong(5, occuapancyMaster.getAuditDetails().getLastModifiedTime());
				ps.setLong(6, occuapancyMaster.getId());
				return ps;
			}
		};

		jdbcTemplate.update(psc);
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
		List<Object> preparedStatementValues = new ArrayList<>();

		String occuapancySearchSql = SearchMasterBuilder.buildSearchQuery(ConstantUtility.OCCUPANCY_TABLE_NAME,
				tenantId, ids, name, nameLocal, code, active, null, orderNumber, null, pageSize, offSet,
				preparedStatementValues, null, null, null, null, null);

		Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
		occupancyMasters = jdbcTemplate.query(occuapancySearchSql.toString(), preparedStatementValues.toArray(),
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

	public Long savePropertyType(String tenantId, PropertyType propertyType, String data) {

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
				ps.setLong(6, propertyType.getAuditDetails().getCreatedTime());
				ps.setLong(7, propertyType.getAuditDetails().getLastModifiedTime());
				ps.setString(8, propertyType.getParent());
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
	 * @param propertyType
	 * @param data
	 */

	public void updatePropertyType(PropertyType propertyType, String data) {

		String updatePropertyTypeSql = PropertyTypesBuilder.UPDATE_PROPERTYTYPES_QUERY;

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
				ps.setLong(5, propertyType.getAuditDetails().getLastModifiedTime());
				ps.setString(6, propertyType.getParent());
				ps.setLong(7, propertyType.getId());
				return ps;
			}
		};

		jdbcTemplate.update(psc);
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
			String code, String nameLocal, Boolean active, Integer orderNumber, Integer pageSize, Integer offSet,
			String parent) {

		List<PropertyType> propertyTypes = new ArrayList<>();
		List<Object> preparedStatementValues = new ArrayList<>();

		String propertyTypeSearchSql = SearchMasterBuilder.buildSearchQuery(ConstantUtility.PROPERTY_TYPE_TABLE_NAME,
				tenantId, ids, name, nameLocal, code, active, null, orderNumber, null, pageSize, offSet,
				preparedStatementValues, null, null, null, parent, null);

		Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
		propertyTypes = jdbcTemplate.query(propertyTypeSearchSql.toString(), preparedStatementValues.toArray(),
				new BeanPropertyRowMapper(PropertyType.class));
		for (PropertyType propertyType : propertyTypes) {
			PropertyType propertyData = gson.fromJson(propertyType.getData(), PropertyType.class);
			propertyType.setAuditDetails(propertyData.getAuditDetails());
			propertyType.setDescription(propertyData.getDescription());
			propertyType.setName(propertyData.getName());
			propertyType.setNameLocal(propertyData.getNameLocal());
			propertyType.setActive(propertyData.getActive());
			propertyType.setOrderNumber(propertyData.getOrderNumber());
			propertyType.setParent(propertyData.getParent());
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

	public Long saveFloorType(FloorType floorType, String data) {

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
				ps.setLong(6, floorType.getAuditDetails().getCreatedTime());
				ps.setLong(7, floorType.getAuditDetails().getLastModifiedTime());
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
	 */
	public void updateFloorType(FloorType floorType, String data) {

		String updateFloorTypeSql = FloorTypeBuilder.UPDATE_FLOOR_QUERY;

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
				ps.setLong(5, floorType.getAuditDetails().getLastModifiedTime());
				ps.setLong(6, floorType.getId());

				return ps;
			}
		};

		jdbcTemplate.update(psc);
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

		List<Object> preparedStatementValues = new ArrayList<>();

		String searchQuery = SearchMasterBuilder.buildSearchQuery(ConstantUtility.FLOOR_TYPE_TABLE_NAME, tenantId, ids,
				name, nameLocal, code, null, null, null, null, pageSize, offSet, preparedStatementValues, null, null,
				null, null, null);

		Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
		floorTypes = jdbcTemplate.query(searchQuery.toString(), preparedStatementValues.toArray(),
				new BeanPropertyRowMapper(FloorType.class));
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

	public Long saveRoofType(RoofType roofType, String data) {

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
				ps.setLong(6, roofType.getAuditDetails().getCreatedTime());
				ps.setLong(7, roofType.getAuditDetails().getLastModifiedTime());
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
	 */
	public void updateRoofType(RoofType roofType, String data) {

		String updateRoofTypeSql = RoofTypeBuilder.UPDATE_ROOF_QUERY;

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
				ps.setLong(5, roofType.getAuditDetails().getLastModifiedTime());
				ps.setLong(6, roofType.getId());
				return ps;
			}
		};

		jdbcTemplate.update(psc);
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
		List<Object> preparedStatementValues = new ArrayList<>();
		String searchQuery = SearchMasterBuilder.buildSearchQuery(ConstantUtility.ROOF_TYPE_TABLE_NAME, tenantId, ids,
				name, nameLocal, code, null, null, null, null, pageSize, offSet, preparedStatementValues, null, null,
				null, null, null);

		Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
		roofTypes = jdbcTemplate.query(searchQuery.toString(), preparedStatementValues.toArray(),
				new BeanPropertyRowMapper(RoofType.class));
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

	public Long saveWoodType(WoodType woodType, String data) {

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
				ps.setLong(6, woodType.getAuditDetails().getCreatedTime());
				ps.setLong(7, woodType.getAuditDetails().getLastModifiedTime());
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
	 */
	public void updateWoodType(WoodType woodType, String data) {

		String updateWoodTypeSql = WoodTypeBuilder.UPDATE_WOOD_QUERY;

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
				ps.setLong(5, woodType.getAuditDetails().getLastModifiedTime());
				ps.setLong(6, woodType.getId());

				return ps;
			}
		};

		jdbcTemplate.update(psc);
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
		List<Object> preparedStatementValues = new ArrayList<>();

		String searchQuery = SearchMasterBuilder.buildSearchQuery(ConstantUtility.WOOD_TYPE_TABLE_NAME, tenantId, ids,
				name, nameLocal, code, null, null, null, null, pageSize, offSet, preparedStatementValues, null, null,
				null, null, null);

		Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
		woodTypes = jdbcTemplate.query(searchQuery.toString(), preparedStatementValues.toArray(),
				new BeanPropertyRowMapper(WoodType.class));
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

		List<Object> preparedStatementValues = new ArrayList<>();
		String wallTypeMasterSearchQuery = SearchMasterBuilder.buildSearchQuery(ConstantUtility.WALL_TYPE_TABLE_NAME,
				tenantId, ids, name, nameLocal, code, null, null, null, null, pageSize, offSet, preparedStatementValues,
				null, null, null, null, null);
		List<WallType> wallTypes = jdbcTemplate.query(wallTypeMasterSearchQuery, preparedStatementValues.toArray(),
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
			Boolean active, Boolean isResidential, Integer orderNumber, Integer pageSize, Integer offSet, String parent,
			List<String> service) {

		List<Object> preparedStatementValues = new ArrayList<>();
		String usageMasterSearchQuery = SearchMasterBuilder.buildSearchQuery(ConstantUtility.USAGE_TYPE_TABLE_NAME,
				tenantId, ids, name, nameLocal, code, active, isResidential, orderNumber, null, pageSize, offSet,
				preparedStatementValues, null, null, null, parent, service);
		List<UsageMaster> usageTypes = jdbcTemplate.query(usageMasterSearchQuery, preparedStatementValues.toArray(),
				new BeanPropertyRowMapper(UsageMaster.class));

		Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
		for (UsageMaster usageType : usageTypes) {

			UsageMaster usageData = gson.fromJson(usageType.getData(), UsageMaster.class);
			usageType.setActive(usageData.getActive());
			usageType.setIsResidential(usageData.getIsResidential());
			usageType.setOrderNumber(usageData.getOrderNumber());
			usageType.setService(usageType.getService());
			usageType.setAuditDetails(usageData.getAuditDetails());
			usageType.setDescription(usageData.getDescription());
			usageType.setName(usageData.getName());
			usageType.setNameLocal(usageData.getNameLocal());
		}

		return usageTypes;
	}

	public Long saveStructureClsses(String tenantId, StructureClass structureClass, String data) {

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
				ps.setLong(6, structureClass.getAuditDetails().getCreatedTime());
				ps.setLong(7, structureClass.getAuditDetails().getLastModifiedTime());
				return ps;
			}
		};

		// The newly generated key will be saved in this object
		final KeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(psc, holder);

		return Long.valueOf(holder.getKey().intValue());

	}

	/**
	 * This method will use for updating structure class
	 * 
	 * @param structureClass
	 * @param data
	 */
	public void updateStructureClsses(StructureClass structureClass, String data) {

		String structureClassesUpdate = StructureClassesBuilder.UPDATE_STRUCTURECLASSES_QUERY;

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
				ps.setLong(5, structureClass.getAuditDetails().getLastModifiedTime());
				ps.setLong(6, structureClass.getId());

				return ps;
			}
		};

		jdbcTemplate.update(psc);
	}

	/**
	 * This method for saving usage master
	 * 
	 * @param usageMaster
	 * @param data
	 * @return
	 */

	public Long saveUsageMaster(UsageMaster usageMaster, String data) {

		String usageInsert = UsageMasterBuilder.INSERT_USAGEMASTER_QUERY;

		final PreparedStatementCreator psc = new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {

				final PreparedStatement ps = connection.prepareStatement(usageInsert, new String[] { "id" });

				ps.setString(1, usageMaster.getTenantId());
				ps.setString(2, usageMaster.getCode());
				ps.setString(3, usageMaster.getParent());
				PGobject jsonObject = new PGobject();
				jsonObject.setType("jsonb");
				jsonObject.setValue(data);
				ps.setObject(4, jsonObject);
				ps.setString(5, usageMaster.getService());
				ps.setString(6, usageMaster.getAuditDetails().getCreatedBy());
				ps.setString(7, usageMaster.getAuditDetails().getLastModifiedBy());
				ps.setLong(8, usageMaster.getAuditDetails().getCreatedTime());
				ps.setLong(9, usageMaster.getAuditDetails().getLastModifiedTime());
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
	 * @param usageMaster
	 * @param data
	 */
	public void updateUsageMaster(UsageMaster usageMaster, String data) {

		String usageUpdate = UsageMasterBuilder.UPDATE_USAGEMASTER_QUERY;

		final PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(usageUpdate);

				PGobject jsonObject = new PGobject();
				jsonObject.setType("jsonb");
				jsonObject.setValue(data);
				ps.setString(1, usageMaster.getTenantId());
				ps.setString(2, usageMaster.getCode());
				ps.setString(3, usageMaster.getParent());
				ps.setObject(4, jsonObject);
				ps.setString(5, usageMaster.getService());
				ps.setString(6, usageMaster.getAuditDetails().getLastModifiedBy());
				ps.setLong(7, usageMaster.getAuditDetails().getLastModifiedTime());
				ps.setLong(8, usageMaster.getId());
				return ps;
			}
		};

		jdbcTemplate.update(psc);
	}

	/**
	 * This method for saving wall types
	 * 
	 * @param wallType
	 * @param data
	 * @return
	 */

	public Long saveWallTypes(WallType wallType, String data) {

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
				ps.setLong(5, wallType.getAuditDetails().getCreatedTime());
				ps.setString(6, wallType.getAuditDetails().getLastModifiedBy());
				ps.setLong(7, wallType.getAuditDetails().getLastModifiedTime());

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
	 * @param wallType
	 * @param data
	 */

	public void updateWallTypes(WallType wallType, String data) {

		String wallTypesUpdate = WallTypesBuilder.UPDATE_WALLTYPES_QUERY;

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
				ps.setLong(5, wallType.getAuditDetails().getLastModifiedTime());
				ps.setLong(6, wallType.getId());
				return ps;
			}
		};

		jdbcTemplate.update(psc);
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

		List<Object> preparedStatementValues = new ArrayList<>();
		String structureClassSearchQuery = SearchMasterBuilder.buildSearchQuery(
				ConstantUtility.STRUCTURE_CLASS_TABLE_NAME, tenantId, ids, name, nameLocal, code, active, null,
				orderNumber, null, pageSize, offSet, preparedStatementValues, null, null, null, null, null);

		List<StructureClass> structureClasses = jdbcTemplate.query(structureClassSearchQuery,
				preparedStatementValues.toArray(), new BeanPropertyRowMapper(StructureClass.class));

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
		count = (Integer) jdbcTemplate.queryForObject(query, Integer.class);

		if (count == 0)
			isExists = Boolean.FALSE;

		return isExists;

	}

	/**
	 * This will persist the given depreciation object in db
	 * 
	 * @param depreciation
	 * @param data
	 * @return {@link Long} id of the record inserted in databse
	 */

	public Long createDepreciation(Depreciation depreciation, String data) {

		String insertDepreciationQuery = DepreciationBuilder.INSERT_DEPRECIATION_QUERY;
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
				ps.setLong(6, depreciation.getAuditDetails().getCreatedTime());
				ps.setLong(7, depreciation.getAuditDetails().getLastModifiedTime());

				return ps;
			}
		};

		final KeyHolder holder = new GeneratedKeyHolder();

		jdbcTemplate.update(psc, holder);

		return Long.valueOf(holder.getKey().intValue());

	}

	/**
	 * This will update the given Depreciation object
	 * 
	 * @param depreciation
	 * @param data
	 */

	public void updateDepreciation(Depreciation depreciation, String data) {

		String updateDepreciation = DepreciationBuilder.UPDATE_DEPRECIATION_QUERY;

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
				ps.setLong(5, depreciation.getAuditDetails().getLastModifiedTime());
				ps.setLong(6, depreciation.getId());
				return ps;
			}
		};

		jdbcTemplate.update(psc);
	}

	/**
	 * This will return the list of depreciations based on the given search
	 * terms
	 * 
	 * @param tenantId
	 * @param ids
	 * @param fromYear
	 * @param toYear
	 * @param code
	 * @param nameLocal
	 * @param pageSize
	 * @param offset
	 * @return {@link Depreciation}
	 */

	public List<Depreciation> searchDepreciations(String tenantId, Integer[] ids, Integer fromYear, Integer toYear,
			String code, String nameLocal, Integer pageSize, Integer offset, Integer year) {

		List<Object> preparedStatementValues = new ArrayList<>();

		String searchDepreciationSql = SearchMasterBuilder.buildSearchQuery(ConstantUtility.DEPRECIATION_TABLE_NAME,
				tenantId, ids, null, nameLocal, code, null, null, null, null, pageSize, offset, preparedStatementValues,
				fromYear, toYear, year, null, null);

		List<Depreciation> depreciations = jdbcTemplate.query(searchDepreciationSql, preparedStatementValues.toArray(),
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

	/**
	 * This will persist the Mutation in the database
	 * 
	 * @param mutationMaster
	 * @param data
	 * @return {@link Long} id of the reocrd inserted in the database
	 */

	public Long createMutationMaster(MutationMaster mutationMaster, String data) {
		String insertMuationQuery = MutationMasterBuilder.INSERT_MUTATTION_QUERY;
		final PreparedStatementCreator psc = new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {

				final PreparedStatement ps = connection.prepareStatement(insertMuationQuery, new String[] { "id" });

				PGobject jsonObject = new PGobject();
				jsonObject.setType("jsonb");
				jsonObject.setValue(data);

				ps.setString(1, mutationMaster.getTenantId());
				ps.setString(2, mutationMaster.getCode());
				ps.setString(3, mutationMaster.getName());
				ps.setObject(4, jsonObject);
				ps.setString(5, mutationMaster.getAuditDetails().getCreatedBy());
				ps.setString(6, mutationMaster.getAuditDetails().getLastModifiedBy());
				ps.setLong(7, mutationMaster.getAuditDetails().getCreatedTime());
				ps.setLong(8, mutationMaster.getAuditDetails().getLastModifiedTime());

				return ps;
			}
		};
		final KeyHolder holder = new GeneratedKeyHolder();

		jdbcTemplate.update(psc, holder);
		return Long.valueOf(holder.getKey().intValue());

	}

	/**
	 * This will update the Mutation master
	 * 
	 * @param mutationMaster
	 * @param data
	 */

	public void updateMutationMaster(MutationMaster mutationMaster, String data) {

		String updateMutation = MutationMasterBuilder.UPDATE_MUTATION_QUERY;

		final PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(updateMutation);

				PGobject jsonObject = new PGobject();
				jsonObject.setType("jsonb");
				jsonObject.setValue(data);

				ps.setString(1, mutationMaster.getTenantId());
				ps.setString(2, mutationMaster.getCode());
				ps.setString(3, mutationMaster.getName());
				ps.setObject(4, jsonObject);
				ps.setString(5, mutationMaster.getAuditDetails().getLastModifiedBy());
				ps.setLong(6, mutationMaster.getAuditDetails().getLastModifiedTime());
				ps.setLong(7, mutationMaster.getId());
				return ps;
			}
		};

		jdbcTemplate.update(psc);
	}

	/**
	 * This will search the mutations based on the given search terms
	 * 
	 * @param tenantId
	 * @param ids
	 * @param name
	 * @param code
	 * @param nameLocal
	 * @param pageSize
	 * @param offSet
	 * @return {@link MutationMaster} List of mutation masters
	 */

	public List<MutationMaster> searchMutation(String tenantId, Integer[] ids, String name, String code,
			String nameLocal, Integer pageSize, Integer offSet) {

		List<Object> preparedStatemetValues = new ArrayList<>();

		String searchMutationQuery = SearchMasterBuilder.buildSearchQuery(ConstantUtility.MUTATION_MASTER_TABLE_NAME,
				tenantId, ids, name, nameLocal, code, null, null, null, null, pageSize, offSet, preparedStatemetValues,
				null, null, null, null, null);

		List<MutationMaster> mutationMasters = jdbcTemplate.query(searchMutationQuery, preparedStatemetValues.toArray(),
				new BeanPropertyRowMapper(MutationMaster.class));

		Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
		mutationMasters.forEach(mutation -> {

			MutationMaster data = gson.fromJson(mutation.getData(), MutationMaster.class);

			mutation.setAuditDetails(data.getAuditDetails());
			mutation.setDescription(data.getDescription());
			mutation.setNameLocal(data.getNameLocal());

		});

		return mutationMasters;
	}

	/**
	 * This will check whether there is any record in the mutation with the
	 * given code
	 * 
	 * @param code
	 * @return {@link Boolean} True /False if code exists/doesn't Exists
	 */
	public Boolean checkUniqueCodeForMutation(String code) {
		Boolean isExists = Boolean.TRUE;

		String query = MutationMasterBuilder.CHECK_UNIQUE_CODE;

		int count = jdbcTemplate.queryForObject(query, new Object[] { code }, Integer.class);

		if (count == 0)
			isExists = Boolean.FALSE;

		return isExists;
	}

	/**
	 * Checks whether Record with tenantId, name, Application name values exists
	 * or not
	 * 
	 * @param tenantId
	 * @param name
	 * @param applicationEnum
	 * @param tableName
	 * @param id
	 * @return
	 */
	public Boolean checkWhetherRecordWithTenantIdAndNameExits(String tenantId, String code, String application,
			String tableName, Long id) {

		Boolean isExists = Boolean.FALSE;

		List<Object> preparedStatementvalues = new ArrayList<>();

		String query = UtilityBuilder.getUniqueTenantIdNameQuery(tenantId, code, application, tableName, id,
				preparedStatementvalues);

		int count = 0;

		count = (Integer) jdbcTemplate.queryForObject(query, preparedStatementvalues.toArray(), Integer.class);

		if (count > 0)
			isExists = Boolean.TRUE;

		return isExists;

	}

	/**
	 * This will ceate the Document type master in the Database
	 * 
	 * @param documentType
	 * @return {@link Long}
	 */
	public Long createDocumentTypeMaster(DocumentType documentType) {
		String insertDocumentTypeQuery = DocumentTypeBuilder.INSERT_DOCUMENTTYPE_MASTER_QUERY;

		final PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {

				final PreparedStatement ps = connection.prepareStatement(insertDocumentTypeQuery,
						new String[] { "id" });

				ps.setString(1, documentType.getTenantId());
				ps.setString(2, documentType.getName());
				ps.setString(3, documentType.getCode());
				ps.setString(4, documentType.getApplication().toString());
				ps.setString(5, documentType.getAuditDetails().getCreatedBy());
				ps.setString(6, documentType.getAuditDetails().getLastModifiedBy());
				ps.setLong(7, documentType.getAuditDetails().getCreatedTime());
				ps.setLong(8, documentType.getAuditDetails().getLastModifiedTime());

				return ps;
			}
		};

		final KeyHolder holder = new GeneratedKeyHolder();

		jdbcTemplate.update(psc, holder);
		return Long.valueOf(holder.getKey().intValue());
	}

	/**
	 * This will update the document type based on the given object
	 * 
	 * @param documentType
	 */
	public void updateDocumentTypeMaster(DocumentType documentType) {
		String updateDocumentTypeQuery = DocumentTypeBuilder.UPDATE_DOCUMENTTYPE_MASTER_QUERY;
		final PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(updateDocumentTypeQuery);

				ps.setString(1, documentType.getTenantId());
				ps.setString(2, documentType.getName());
				ps.setString(3, documentType.getCode());
				ps.setString(4, documentType.getApplication().toString());
				ps.setString(5, documentType.getAuditDetails().getLastModifiedBy());
				ps.setLong(6, documentType.getAuditDetails().getLastModifiedTime());
				ps.setLong(7, documentType.getId());
				return ps;
			}
		};

		jdbcTemplate.update(psc);
	}

	/**
	 * This will search the document types based on the given parameters
	 * 
	 * @param tenantId
	 * @param name
	 * @param code
	 * @param application
	 * @return {@link DocumentType} List of document types
	 */
	public List<DocumentType> searchDocumentTypeMaster(String tenantId, String name, String code, String application,
			Integer pageSize, Integer offSet) {

		List<Object> preparedStatementValues = new ArrayList<Object>();
		String searchDocumentTypeQuery = DocumentTypeBuilder.searchDocumentType(tenantId, name, code, application,
				pageSize, offSet, preparedStatementValues);

		List<DocumentType> documentTypes = null;
		documentTypes = getDocumentTypes(searchDocumentTypeQuery, preparedStatementValues);

		return documentTypes;
	}

	/**
	 * This will add all the details to the Document type model
	 * 
	 * @param query
	 * @return List<DocumentType>
	 */
	private List<DocumentType> getDocumentTypes(String query, List<Object> preparedStatementvalues) {

		List<DocumentType> documentTypes = new ArrayList<DocumentType>();

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, preparedStatementvalues.toArray());
		for (Map row : rows) {
			DocumentType documentType = new DocumentType();
			documentType.setTenantId(getString(row.get("tenantId")));
			documentType.setId(getLong(row.get("id")));
			documentType.setName(getString(row.get("name")));
			documentType.setCode(getString(row.get("code")));
			documentType.setApplication(ApplicationEnum.valueOf(getString(row.get("application"))));

			AuditDetails auditDetails = new AuditDetails();
			auditDetails.setCreatedBy(getString(row.get("createdby")));
			auditDetails.setLastModifiedBy(getString(row.get("lastmodifiedby")));
			auditDetails.setCreatedTime(getLong(row.get("createdtime")));
			auditDetails.setLastModifiedTime(getLong(row.get("lastmodifiedtime")));
			documentType.setAuditDetails(auditDetails);

			documentTypes.add(documentType);

		}

		return documentTypes;
	}

	public void getCreatedAuditDetails(AuditDetails auditDetails, String tableName, Long id) {

		String query = AuditDetailsBuilder.getCreatedAuditDetails(tableName, id);
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);

		for (Map<String, Object> row : rows) {
			auditDetails.setCreatedBy(getString(row.get("createdby")));
			auditDetails.setCreatedTime(getLong(row.get("createdtime")));
		}
	}

	/**
	 * This will persist Apartment Master in database
	 * 
	 * @param apartment
	 * @return id
	 */
	public Long createApartment(Apartment apartment, String data) {

		String insertApartment = ApartmentBuilder.INSERT_APARTMENT_QUERY;
		final PreparedStatementCreator psc = new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {

				final PreparedStatement ps = connection.prepareStatement(insertApartment, new String[] { "id" });

				PGobject jsonObject = new PGobject();
				jsonObject.setType("jsonb");
				jsonObject.setValue(data);

				ps.setString(1, apartment.getTenantId());
				ps.setString(2, apartment.getCode());
				ps.setString(3, apartment.getName());
				ps.setObject(4, jsonObject);
				ps.setString(5, apartment.getAuditDetails().getCreatedBy());
				ps.setString(6, apartment.getAuditDetails().getLastModifiedBy());
				ps.setLong(7, apartment.getAuditDetails().getCreatedTime());
				ps.setLong(8, apartment.getAuditDetails().getLastModifiedTime());

				return ps;
			}
		};

		final KeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(psc, holder);

		return Long.valueOf(holder.getKey().intValue());
	}

	/**
	 * This will update Apartment Master
	 * 
	 * @param apartment
	 * @param data
	 */
	public void updateApartment(Apartment apartment, String data) {

		String updateApartment = ApartmentBuilder.UPDATE_APARTMENT_QUERY;

		final PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(updateApartment);

				PGobject jsonObject = new PGobject();
				jsonObject.setType("jsonb");
				jsonObject.setValue(data);

				ps.setString(1, apartment.getTenantId());
				ps.setString(2, apartment.getCode());
				ps.setString(3, apartment.getName());
				ps.setObject(4, jsonObject);
				ps.setString(5, apartment.getAuditDetails().getLastModifiedBy());
				ps.setLong(6, apartment.getAuditDetails().getLastModifiedTime());
				ps.setLong(7, apartment.getId());
				return ps;
			}
		};

		jdbcTemplate.update(psc);
	}

	/**
	 * This will search apartments in Apartment master
	 * 
	 * @param tenantId
	 * @param code
	 * @param name
	 * @param ids
	 * @param pageSize
	 * @param offSet
	 * @return
	 */
	public List<Apartment> searchApartment(String tenantId, String code, String name, Integer[] ids,
			Boolean liftFacility, Boolean powerBackUp, Boolean parkingFacility, Integer pageSize, Integer offSet) {

		List<Object> preparedStatementValues = new ArrayList<>();
		String searchApartmentQuery = SearchMasterBuilder.apartmentSearchQuery(ConstantUtility.APARTMENT_TABLE_NAME,
				tenantId, ids, name, code, liftFacility, powerBackUp, parkingFacility, pageSize, offSet,
				preparedStatementValues);
		List<Apartment> apartments = jdbcTemplate.query(searchApartmentQuery, preparedStatementValues.toArray(),
				new BeanPropertyRowMapper(Apartment.class));
		Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();

		for (Apartment apartment : apartments) {
			Apartment apartmentData = gson.fromJson(apartment.getData(), Apartment.class);
			apartment.setId(apartment.getId());
			apartment.setCode(apartmentData.getCode());
			apartment.setName(apartmentData.getName());
			apartment.setTotalBuiltUpArea(apartmentData.getTotalBuiltUpArea());
			apartment.setTotalProperties(apartmentData.getTotalProperties());
			apartment.setTotalFloors(apartmentData.getTotalFloors());
			apartment.setTotalOpenSpace(apartmentData.getTotalOpenSpace());
			apartment.setLiftFacility(apartmentData.getLiftFacility());
			apartment.setPowerBackUp(apartmentData.getPowerBackUp());
			apartment.setParkingFacility(apartmentData.getParkingFacility());
			apartment.setResidtinalProperties(apartmentData.getResidtinalProperties());
			apartment.setNonResidtinalProperties(apartmentData.getNonResidtinalProperties());
			apartment.setSourceOfWater(apartmentData.getSourceOfWater());
			apartment.setFloor(apartmentData.getFloor());
			apartment.setAuditDetails(apartmentData.getAuditDetails());
		}

		return apartments;
	}

	/**
	 * This method will cast the given object to String
	 * 
	 * @param object
	 *            that need to be cast to string
	 * @return {@link String} String value
	 */
	private String getString(Object object) {
		return object == null ? null : object.toString();
	}

	/**
	 * This method will cast the given object to Long
	 * 
	 * @param object
	 *            that need to be cast to Long
	 * @return {@link Long} Long value
	 */
	private Long getLong(Object object) {
		return object == null ? 0 : Long.parseLong(object.toString());
	}

	/**
	 * This will check whether any record exists with the given tenantId & diff
	 * b/t occupancy year & current year in database or not
	 * 
	 * @param tenantId
	 * @param diff
	 *            b/t current date & current date
	 * @return True / false if record exists / record does n't exists
	 */
	public String getAge(String tenantId, Integer diffValue, String tableName, List<Object> preparedStatementValues) {

		String query = UtilityBuilder.getAgeQuery(tableName, diffValue, tenantId, preparedStatementValues);
		String code = null;
		code = jdbcTemplate.queryForObject(query, preparedStatementValues.toArray(), String.class);
		return code;

	}
}
