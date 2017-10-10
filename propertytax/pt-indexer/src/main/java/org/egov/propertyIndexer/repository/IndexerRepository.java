package org.egov.propertyIndexer.repository;

import java.util.List;
import org.egov.models.City;
import org.egov.models.Floor;
import org.egov.models.Property;
import org.egov.models.RequestInfo;
import org.egov.models.SearchTenantResponse;
import org.egov.models.Unit;
import org.egov.propertyIndexer.config.PropertiesManager;
import org.egov.propertyIndexer.model.ApartmentES;
import org.egov.propertyIndexer.model.DepartmentES;
import org.egov.propertyIndexer.model.FloorES;
import org.egov.propertyIndexer.model.FloorTypeES;
import org.egov.propertyIndexer.model.OccupancyTypeES;
import org.egov.propertyIndexer.model.PropertyES;
import org.egov.propertyIndexer.model.PropertyTypeES;
import org.egov.propertyIndexer.model.RoofTypeES;
import org.egov.propertyIndexer.model.StructureES;
import org.egov.propertyIndexer.model.SubUsage;
import org.egov.propertyIndexer.model.UnitES;
import org.egov.propertyIndexer.model.Usage;
import org.egov.propertyIndexer.model.WallTypeES;
import org.egov.propertyIndexer.model.WoodTypeES;
import org.egov.propertyIndexer.repository.builder.IndexerBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author Prasad 
 * This class will have all the DB operations related to indexer module
 *
 */
@Repository
public class IndexerRepository {

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	SearchTenantRepository searchTenantRepository;

	/**
	 * This API will give the name of the usage based on the given usageCode
	 * 
	 * @param usageCode
	 * @return {@link String} Usage Name
	 */
	public String getUsageName(String usageCode, String tenantId) {

		String usageName = jdbcTemplate.queryForObject(IndexerBuilder.GET_USAGE_NAME_BY_USAGE_CODE,
				new Object[] { usageCode, tenantId }, String.class);
		return usageName;
	}

	/**
	 * This API will give the name of the roof type based on the given code
	 * 
	 * @param roofCode
	 * @return {@link String} Roof Type Name
	 */
	public String getRoofTypeName(String roofCode, String tenantId) {
		String roofName = jdbcTemplate.queryForObject(IndexerBuilder.GET_ROOF_NAME_BY_ROOF_CODE,
				new Object[] { roofCode, tenantId }, String.class);
		return roofName;
	}

	/**
	 * This API will give the name of the floor based on the given floor code
	 * 
	 * @param floorCode
	 * @return {@link String} Floor Type Name
	 */
	public String getFloorTypeName(String floorCode, String tenantId) {
		String floorName = jdbcTemplate.queryForObject(IndexerBuilder.GET_FLOOR_NAME_BY_FLOOR_CODE,
				new Object[] { floorCode, tenantId }, String.class);
		return floorName;
	}

	/**
	 * This API will give the name of the wood based on the given wood code
	 * 
	 * @param woodCode
	 * @return {@link String} wood Type Name
	 */

	public String getWoodTypeName(String woodCode, String tenantId) {
		String woodName = jdbcTemplate.queryForObject(IndexerBuilder.GET_WOOD_NAME_BY_WOOD_CODE,
				new Object[] { woodCode, tenantId }, String.class);
		return woodName;
	}

	/**
	 * This API will give the name of the Wall based on the given Wall code
	 * 
	 * @param wallCode
	 * @return {@link String} Wall Type Name
	 */
	public String getWallTypeName(String wallCode, String tenantId) {
		String wallName = jdbcTemplate.queryForObject(IndexerBuilder.GET_WALL_NAME_BY_WALL_CODE,
				new Object[] { wallCode, tenantId }, String.class);
		return wallName;
	}

	/**
	 * This API will give the name of the property type based on the given
	 * property code
	 * 
	 * @param propertyTypeCode
	 * @return {@link String} propertyTypeName
	 */
	public String getPropertyTypeName(String propertyTypeCode, String tenantId) {
		String propertyName = jdbcTemplate.queryForObject(IndexerBuilder.GET_PROPERTY_NAME_BY_PROPERTY_CODE,
				new Object[] { propertyTypeCode, tenantId }, String.class);
		return propertyName;
	}

	/**
	 * This API will give the name of the sub usage based on the given sub usage
	 * code
	 * 
	 * @param subUsageCode
	 * @return {@link String} subUsageName
	 */
	public String getSubUsageName(String subUsageCode, String tenantId) {
		String subUsageName = jdbcTemplate.queryForObject(IndexerBuilder.GET_SUBUSAGE_NAME_BY_SUBUSAGE_CODE,
				new Object[] { subUsageCode, tenantId }, String.class);
		return subUsageName;
	}

	/**
	 * This API will give the name of occupancy based on the given occupancyCode
	 * code
	 * 
	 * @param occupancyCode
	 * @return {@link String} occupancyName
	 */
	public String getOccupancyName(String occupancyCode, String tenantId) {
		String occupancyName = jdbcTemplate.queryForObject(IndexerBuilder.GET_OCCUPANCY_NAME_BY_OCCUPANCY_CODE,
				new Object[] { occupancyCode, tenantId }, String.class);
		return occupancyName;
	}

	/**
	 * This API will give the name of department based on the given department
	 * code
	 * 
	 * @param departmentCode
	 * @return {@link String} departmentName
	 */

	public String getDepartmentName(String departmentCode, String tenantId) {
		String departmentName = jdbcTemplate.queryForObject(IndexerBuilder.GET_DEPARTMENT_NAME_BY_DEPARTMENT_CODE,
				new Object[] { departmentCode, tenantId }, String.class);
		return departmentName;
	}

	/**
	 * This API will give the name of apartment based on the given apartmentCode
	 * 
	 * @param apartmentCode
	 * @return {@link String} apartmentName
	 */
	public String getApartmentName(String apartmentCode, String tenantId) {
		String apartmentName = jdbcTemplate.queryForObject(IndexerBuilder.GET_DEPARTMENT_NAME_BY_DEPARTMENT_CODE,
				new Object[] { apartmentCode, tenantId }, String.class);
		return apartmentName;
	}

	/**
	 * This API will give the name of structure based on the given structureName
	 * 
	 * @param structureCode
	 * @return {@link String} apartmentName
	 */
	public String getStructureName(String structureCode, String tenantId) {
		String structureName = jdbcTemplate.queryForObject(IndexerBuilder.GET_STRUCTURE_NAME_BY_STRUCTURE_CODE,
				new Object[] { structureCode, tenantId }, String.class);
		return structureName;
	}

	/**
	 * This API will add the master data(name & code)
	 * 
	 * @param propertyRequest
	 * @return {@link PropertyES}
	 */
	public PropertyES addMasterData(Property property, RequestInfo requestInfo) throws Exception {

		String tenantId = property.getTenantId();

		ModelMapper modelMapper = new ModelMapper();
		PropertyES propertyES = modelMapper.map(property, PropertyES.class);
		List<Floor> floors = property.getPropertyDetail().getFloors();

		Usage usage = new Usage();
		String usageCode = property.getPropertyDetail().getUsage();
		if (usageCode != null) {
			usage.setCode(usageCode);
			usage.setName(getUsageName(usageCode, tenantId));
			propertyES.getPropertyDetail().setUsage(usage);
		}

		String subUsageCode = property.getPropertyDetail().getSubUsage();
		if (subUsageCode != null) {
			SubUsage subUsage = new SubUsage();
			subUsage.setCode(subUsageCode);
			subUsage.setName(getSubUsageName(subUsageCode, tenantId));
			propertyES.getPropertyDetail().setSubUsage(subUsage);
		}

		String propertyTypeCode = property.getPropertyDetail().getPropertyType();

		if (propertyTypeCode != null) {
			PropertyTypeES propertyType = new PropertyTypeES();
			propertyType.setCode(propertyTypeCode);
			propertyType.setName(getPropertyTypeName(propertyTypeCode, tenantId));
			propertyES.getPropertyDetail().setPropertyType(propertyType);

		}

		String departmentCode = property.getPropertyDetail().getDepartment();
		if (departmentCode != null) {
			DepartmentES department = new DepartmentES();
			department.setCode(departmentCode);
			department.setName(getDepartmentName(departmentCode, tenantId));
			propertyES.getPropertyDetail().setDepartment(department);
		}

		String apartmentCode = property.getPropertyDetail().getApartment();

		if (apartmentCode != null) {
			ApartmentES apartment = new ApartmentES();
			apartment.setCode(apartmentCode);
			apartment.setName(getApartmentName(apartmentCode, tenantId));
			propertyES.getPropertyDetail().setApartment(apartment);
		}

		String floorTypeCode = property.getPropertyDetail().getFloorType();
		if (floorTypeCode != null) {
			FloorTypeES floorType = new FloorTypeES();
			floorType.setCode(floorTypeCode);
			floorType.setName(getFloorTypeName(floorTypeCode, tenantId));
			propertyES.getPropertyDetail().setFloorType(floorType);
		}

		String woodTypeCode = property.getPropertyDetail().getWoodType();
		if (woodTypeCode != null) {
			WoodTypeES woodType = new WoodTypeES();
			woodType.setCode(woodTypeCode);
			woodType.setName(getWoodTypeName(woodTypeCode, tenantId));
			propertyES.getPropertyDetail().setWoodType(woodType);
		}

		String roofTypeCode = property.getPropertyDetail().getRoofType();

		if (roofTypeCode != null) {
			RoofTypeES roofType = new RoofTypeES();
			roofType.setCode(roofTypeCode);
			roofType.setName(getRoofTypeName(roofTypeCode, tenantId));
			propertyES.getPropertyDetail().setRoofType(roofType);
		}

		String wallTypeCode = property.getPropertyDetail().getWallType();

		if (wallTypeCode != null) {
			WallTypeES wallType = new WallTypeES();
			wallType.setCode(wallTypeCode);
			wallType.setName(getWallTypeName(wallTypeCode, tenantId));
			propertyES.getPropertyDetail().setWallType(wallType);
		}

		List<FloorES> floorEss = propertyES.getPropertyDetail().getFloors();

		SetFloorAndUnitDetails(floors, floorEss, tenantId);

		SearchTenantResponse searchTenantResponse = searchTenantRepository
				.getCityDetailsForTenant(property.getTenantId(), requestInfo);
		City city = searchTenantResponse.getTenant().get(0).getCity();
		propertyES.setCityCode(city.getCode());
		propertyES.setCityDistrictCode(city.getDistrictCode());
		propertyES.setCityDistrictName(city.getDistrictName());
		propertyES.setCityDistrictName(city.getDistrictName());
		propertyES.setCityGrade(city.getUlbGrade());
		propertyES.setCityName(city.getName());
		propertyES.setCityRegionName(city.getRegionName());
		return propertyES;

	}

	private void SetFloorAndUnitDetails(List<Floor> floors, List<FloorES> floorEss, String tenantId) {

		for (Floor floor : floors) {

			for (FloorES floorEs : floorEss) {

				if (floor.getId() == floorEs.getId()) {
					setUnitDetails(floor.getUnits(), floorEs.getUnits(), tenantId);
				}
			}

		}

	}

	private void setUnitDetails(List<Unit> units, List<UnitES> unitsEs, String tenantId) {

		for (Unit unit : units) {

			for (UnitES unites : unitsEs) {

				if (unit.getId() == unites.getId()) {

					if ((unit.getUnits() != null && unit.getUnits().size() > 0)
							&& (unites.getUnits() != null && unites.getUnits().size() > 0)) {
						setUnitDetails(unit.getUnits().get(0), unites.getUnits().get(0), tenantId);

					}

					setUnitDetails(unit, unites, tenantId);

				}

			}

		}

	}

	private void setUnitDetails(Unit unit, UnitES unites, String tenantId) {

		String usageCode = unit.getUsage();

		if (usageCode != null) {
			Usage usage = new Usage();
			usage.setCode(usageCode);
			usage.setName(getUsageName(usageCode, tenantId));
			unites.setUsage(usage);
		}

		String subUsageCode = unit.getSubUsage();

		if (subUsageCode != null) {
			SubUsage subUsage = new SubUsage();
			subUsage.setCode(subUsageCode);
			subUsage.setName(getSubUsageName(subUsageCode, tenantId));
			unites.setSubUsage(subUsage);
		}

		String occupancyCode = unit.getOccupancyType();

		if (occupancyCode != null) {
			OccupancyTypeES occupancyType = new OccupancyTypeES();
			occupancyType.setCode(occupancyCode);
			occupancyType.setName(getOccupancyName(occupancyCode, tenantId));
			unites.setOccupancyType(occupancyType);
		}

		String structureCode = unit.getStructure();

		if (structureCode != null) {
			StructureES structure = new StructureES();
			structure.setCode(structureCode);
			structure.setName(getStructureName(structureCode, tenantId));
			unites.setStructure(structure);
		}

	}

}
