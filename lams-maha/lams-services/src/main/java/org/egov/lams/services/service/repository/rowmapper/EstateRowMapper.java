package org.egov.lams.services.service.repository.rowmapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.egov.lams.common.web.contract.Boundary;
import org.egov.lams.common.web.contract.EstateRegister;
import org.egov.lams.common.web.contract.FloorDetail;
import org.egov.lams.common.web.contract.PropertyType;
import org.egov.lams.common.web.contract.Register;
import org.egov.lams.common.web.contract.RegisterName;
import org.egov.lams.common.web.contract.SubRegister;
import org.egov.lams.common.web.contract.SubRegisterName;
import org.egov.lams.common.web.contract.UnitDetail;
import org.egov.lams.common.web.contract.WorkFlowDetails;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EstateRowMapper implements ResultSetExtractor<List<EstateRegister>>{

	@Override
	public List<EstateRegister> extractData(ResultSet rs) throws SQLException, DataAccessException {
		 Map<String, EstateInfo> estateMap = getEstateMap(rs);
		 log.info(":estateMap.size::::"+estateMap.size());
	        List<EstateRegister> estateList = getEstateList(estateMap);

	        return estateList;
	}

	 private Map<String, EstateInfo> getEstateMap(ResultSet rs) throws SQLException {

	        Map<String, EstateInfo> estateInfoMap = new LinkedHashMap<>();
	        
	        while (rs.next()) {
	        	String estateId = (String) rs.getObject("rg_id");
	            log.info("estateId:::"+estateId);
	            EstateInfo estateInfo = estateInfoMap.get(estateId);

	            if (estateInfo == null) {
	                estateInfo = new EstateInfo();
	                estateInfo.setId(estateId);
	                estateInfo.setTenantId(rs.getString("rg_tenantid"));
					Register regName=new Register();
					regName.setCode(rs.getString("rg_registername"));
					estateInfo.setRegisterName(regName);
					SubRegister subRegName=new SubRegister();
					subRegName.setCode(rs.getString("rg_subregistername"));
					estateInfo.setSubRegisterName(subRegName);
					Boundary regionalOffice=new Boundary();
					regionalOffice.setId(rs.getString("rg_regionaloffice"));
					estateInfo.setRegionalOffice(regionalOffice);
					Boundary location=new Boundary();
					location.setId(rs.getString("rg_location"));
					estateInfo.setLocation(location);
					estateInfo.setPropertyName(rs.getString("rg_propertyname"));
					PropertyType propertyType=new PropertyType();
					propertyType.setCode(rs.getString("rg_propertytype"));
					estateInfo.setPropertyType(propertyType);
					estateInfo.setPropertyAddress(rs.getString("rg_propertyaddress"));
					estateInfo.setSurveyNo(rs.getString("rg_surveyno"));
					estateInfo.setGattNo(rs.getString("rg_gattno"));
					estateInfo.setBuildUpArea(rs.getDouble("rg_builduparea"));
					estateInfo.setPurpose(rs.getString("rg_purpose"));
					estateInfo.setModeOfAcquisition(rs.getString("rg_modeofacquisition"));
					estateInfo.setCarpetArea(rs.getDouble("rg_carpetarea"));
					estateInfo.setHoldingType(rs.getString("rg_holdingtype"));
					estateInfo.setLandID(rs.getString("rg_landid"));
					estateInfo.setConstructionStartDate(rs.getLong("rg_constructionstartdate"));
					estateInfo.setConstructionEndDate(rs.getLong("rg_constructionenddate"));
					estateInfo.setProposedBuildingBudget(rs.getDouble("rg_proposedbuildingbudget"));
					estateInfo.setActualBuildingExpense(rs.getDouble("rg_actualbuildingexpense"));
					estateInfo.setLatitude(rs.getDouble("rg_latitude"));
					estateInfo.setLongitude(rs.getDouble("rg_longitude"));
					estateInfo.setStateId(rs.getString("rg_stateid"));
					estateInfo.setComments(rs.getString("rg_comments"));
					
					estateInfoMap.put(estateId, estateInfo);
	            }

	            Map<String, floorInfo> floorInfoMap = estateInfo.getFloors();

	            String floorId = (String) rs.getObject("fl_id");
	            floorInfo floor = floorInfoMap.get(floorId);

	            // populate floor fields from result set
	            if (floor == null) {
	                floor = new floorInfo();
	                floor.setId(floorId);
	                floor.setFloorNo(rs.getString("fl_floorno"));
	    			floor.setFloorArea(rs.getDouble("fl_floorarea"));
	    			floor.setNoOfUnits(rs.getBigDecimal("fl_noofunits"));

	                floorInfoMap.put(floorId, floor);
	            }

	            Map<String, UnitDetail> unitMap = floor.getUnits();
	            String unitId = (String) rs.getObject("ud_id");

	            if ((Long) rs.getObject("ud_id") != null) {
	            	UnitDetail unit = unitMap.get(unitId);
	                if (unit == null) {
	                    unit = new UnitDetail();
	                    unit.setId(unitId);
	                    unit.setUsage(rs.getString("ud_usagetype"));
	        			unit.setPreviousUnitNo(rs.getString("ud_previousunitno"));
	        			unit.setBuiltUpArea(rs.getDouble("ud_builtuparea"));
	        			unit.setHoldingType(rs.getString("ud_holdingtype"));
	        			unit.setDepartmentName(rs.getString("ud_departmentname"));
	        			unit.setDescription(rs.getString("ud_description"));
	                    unitMap.put(unitId, unit);
	                }
	            }

	        }
	        return estateInfoMap;
	    }
	 
	 private List<EstateRegister> getEstateList(Map<String, EstateInfo> empInfoMap) {
	        List<EstateRegister> estateInfoList = new ArrayList<>();
	        for (Map.Entry<String, EstateInfo> empInfoEntry : empInfoMap.entrySet()) {
	        	EstateInfo estateInfo = empInfoEntry.getValue();

	        	EstateRegister estateRegister = new EstateRegister();
	        	estateRegister.setId(estateInfo.getId());
	        	estateRegister.setTenantId(estateInfo.getTenantId());
	        	estateRegister.setActualBuildingExpense(estateInfo.getActualBuildingExpense());
	        	estateRegister.setBuildUpArea(estateInfo.getBuildUpArea());
	        	estateRegister.setCarpetArea(estateInfo.getCarpetArea());
	        	estateRegister.setComments(estateInfo.getComments());
	        	estateRegister.setConstructionEndDate(estateInfo.getConstructionEndDate());
	        	estateRegister.setConstructionStartDate(estateInfo.getConstructionStartDate());
	        	estateRegister.setEstateNumber(estateInfo.getEstateNumber());
	        	estateRegister.setGattNo(estateInfo.getGattNo());
	        	estateRegister.setHoldingType(estateInfo.getHoldingType());
	        	estateRegister.setLandID(estateInfo.getLandID());
	        	estateRegister.setLatitude(estateInfo.getLatitude());
	        	estateRegister.setLocation(estateInfo.getLocation());
	        	estateRegister.setLongitude(estateInfo.getLongitude());
	        	estateRegister.setModeOfAcquisition(estateInfo.getModeOfAcquisition());
	        	estateRegister.setPropertyAddress(estateInfo.getPropertyAddress());
	        	estateRegister.setPropertyName(estateInfo.getPropertyName());
	        	estateRegister.setPropertyType(estateInfo.propertyType);
	        	estateRegister.setProposedBuildingBudget(estateInfo.getProposedBuildingBudget());
	        	estateRegister.setPurpose(estateInfo.getPurpose());
	        	estateRegister.setRegionalOffice(estateInfo.getRegionalOffice());
	        	estateRegister.setRegister(estateInfo.getRegisterName());
	        	estateRegister.setStateId(estateInfo.getStateId());
	        	estateRegister.setSubRegister(estateInfo.getSubRegisterName());
	        	estateRegister.setSurveyNo(estateInfo.getSurveyNo());
	        	
	            List<FloorDetail> floorList = new ArrayList<>();
	            for (Map.Entry<String, floorInfo> floorInfoEntry : estateInfo.getFloors().entrySet()) {
	            	floorInfo floorInfo = floorInfoEntry.getValue();

	            	FloorDetail floorDetails =new FloorDetail();
	            	floorDetails.setId(floorInfo.getId());
	            	floorDetails.setFloorArea(floorInfo.getFloorArea());
	            	floorDetails.setFloorNo(floorInfo.getFloorNo());
	            	floorDetails.setNoOfUnits(floorInfo.getNoOfUnits());

	                List<UnitDetail> unit = new ArrayList<>();
	                for (Map.Entry<String, UnitDetail> unitEntry : floorInfo.units.entrySet()) {
	                	UnitDetail unitDetails = unitEntry.getValue();
	                    unit.add(unitDetails);
	                }

	                floorDetails.setUnits(unit);

	                floorList.add(floorDetails);
	            }

	            estateRegister.setFloors(floorList);

	            estateInfoList.add(estateRegister);
	        }
	        return estateInfoList;
	    }
	 
	 
	 
@Getter
@Setter
private class EstateInfo {
	private String id = null;
	  private String tenantId = null;
	  private String estateNumber = null;
	  private Register registerName = null;
	  private SubRegister subRegisterName = null;
	  private Boundary regionalOffice = null;
	  private Boundary location = null;
	  private String propertyName = null;
	  private PropertyType propertyType = null;
	  private String propertyAddress = null;
	  private String surveyNo = null;
	  private String gattNo = null;
	  private Double buildUpArea = null;
	  private String purpose = null;
	  private String modeOfAcquisition = null;
	  private Double carpetArea = null;
	  private String holdingType = null;
	  private String landID = null;
	  private Long constructionStartDate = null;
	  private Long constructionEndDate = null;
	  private Double proposedBuildingBudget = null;
	  private Double actualBuildingExpense = null;
	  private Double latitude = null;
	  private Double longitude = null;
	  private Map<String,floorInfo> floors =  new LinkedHashMap<>();
	  private String stateId = null;
	  private WorkFlowDetails workFlowDetails = null;
	  private String comments = null;
}
@Getter
@Setter
private class floorInfo{
	private String id = null;
	  private String floorNo = null;
	  private Double floorArea = null;
	  private BigDecimal noOfUnits = null;
	  private Map<String,UnitDetail> units = new LinkedHashMap<>();
}
}