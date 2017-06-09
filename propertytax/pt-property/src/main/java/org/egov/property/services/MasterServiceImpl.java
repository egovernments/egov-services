package org.egov.property.services;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.egov.models.Department;
import org.egov.models.DepartmentRequest;
import org.egov.models.DepartmentResponseInfo;
import org.egov.models.RequestInfo;
import org.egov.models.ResponseInfo;
import org.egov.models.ResponseInfoFactory;
import org.egov.property.exception.InvalidInputException;
import org.egov.property.model.ExcludeFileds;
import org.egov.property.model.MasterListModel;
import org.egov.property.model.MasterModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Description : MasterService interface implementation class 
 * @author Narendra
 *
 */

@Service
public class MasterServiceImpl  implements Masterservice{

	@Autowired
	private MasterListModel masterList;


	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	
	private ResponseInfoFactory responseInfoFactory;

	/**
	 *Description : This method for getting property types
	 * @param tenantId
	 * @param code
	 * @param requestInfo
	 * @return masterModel
	 * @throws Exception
	 */
	@Override
	public MasterModel getPropertyTypes(String tenantId, String code,RequestInfo requestInfo) throws Exception {
		return	getMaster(masterList.getPropertyType(),tenantId,code,requestInfo);
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
	public MasterModel getUsageMaster(String tenantId, String code,RequestInfo requestInfo) throws Exception {
		return	getMaster(masterList.getUsageMaster(),tenantId,code,requestInfo);
	}

	/**
	 *Description : This method for getting ocupancy master details
	 * @param tenantId
	 * @param code
	 * @param requestInfo
	 * @return masterModel
	 * @throws Exception
	 */


	@Override
	public MasterModel getOcupancyMaster(String tenantId, String code,RequestInfo requestInfo) throws Exception {
		return	getMaster(masterList.getOccupancyMaster(),tenantId,code,requestInfo);
	}

	/**
	 *Description : This method for getting tax rate details
	 * @param tenantId
	 * @param code
	 * @param requestInfo
	 * @return masterModel
	 * @throws Exception
	 */


	@Override
	public MasterModel getTaxRateMaster(String tenantId, String code,RequestInfo requestInfo) throws Exception {
		return	getMaster(masterList.getTaxRateMaster(),tenantId,code,requestInfo);
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
	public MasterModel getWallTypeMaster(String tenantId, String code,RequestInfo requestInfo) throws Exception {
		return	getMaster(masterList.getWallTypeMaster(),tenantId,code,requestInfo);
	}

	/**
	 *Description : This method for getting roof type master details
	 * @param tenantId
	 * @param code
	 * @param requestInfo
	 * @return masterModel
	 * @throws Exception
	 */


	@Override
	public MasterModel getRoofTypeMaster(String tenantId, String code,RequestInfo requestInfo) throws Exception {
		return	getMaster(masterList.getRoofTypeMaster(),tenantId,code,requestInfo);
	}


	/**
	 *Description : This method for getting wood type master details
	 * @param tenantId
	 * @param code
	 * @param requestInfo
	 * @return masterModel
	 * @throws Exception
	 */


	@Override
	public MasterModel getWoodTypeMaster(String tenantId, String code,RequestInfo requestInfo) throws Exception {
		return	getMaster(masterList.getWoodTypeMaster(),tenantId,code,requestInfo);
	}

	/**
	 *Description : This method for getting apartment master details
	 * @param tenantId
	 * @param code
	 * @param requestInfo
	 * @return masterModel
	 * @throws Exception
	 */


	@Override
	public MasterModel getApartmentMaster(String tenantId, String code,RequestInfo requestInfo) throws Exception {
		return	getMaster(masterList.getApartmentMaster(),tenantId,code,requestInfo);
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
	public MasterModel getFloorTypeMaster(String tenantId, String code,RequestInfo requestInfo) throws Exception {
		return	getMaster(masterList.getFloorTypeMaster(),tenantId,code,requestInfo);
	}

	/**
	 * Description : this method will get data from yaml
	 * @param masterData
	 * @param tenantId
	 * @param code
	 * @param requestInfo
	 * @return
	 */

	public MasterModel getMaster(List<MasterModel> masterData,String tenantId,String code,RequestInfo requestInfo){
		List<MasterModel> modelList = null;
		if(tenantId!=null && code!=null){
			modelList= masterData.stream().filter(m->m.getTenantId().equalsIgnoreCase(tenantId)&& m.getCode().equalsIgnoreCase(code)).collect(Collectors.toList());
		}
		else if(tenantId==null || tenantId.isEmpty()){
			modelList= masterData.stream().filter(m->m.getCode().equalsIgnoreCase(code)).collect(Collectors.toList());
		}
		else if(code==null || code.isEmpty()){
			modelList= masterData.stream().filter(m->m.getTenantId().equalsIgnoreCase(tenantId)).collect(Collectors.toList());
		}
		if(modelList.size()==0)
			throw new InvalidInputException(requestInfo);
		return modelList.get(0);
	}

	/**
	 *Description : This method for getting strcture master details
	 * @param tenantId
	 * @param code
	 * @param requestInfo
	 * @return masterModel
	 * @throws Exception
	 */


	@Override
	public MasterModel getStructureMaster(String tenantId, String code,RequestInfo requestInfo) throws Exception {
		return	getMaster(masterList.getStructureMaster(),tenantId,code,requestInfo);
	}

	/**
	 * Description : This method for getting usage mutation reason master details
	 * @param tenantId
	 * @param code
	 * @param requestInfo
	 * @return masterModel
	 * @throws Exception
	 */


	@Override
	public MasterModel getMutationReasonMaster(String tenantId, String code,RequestInfo requestInfo) throws Exception {
		return	getMaster(masterList.getMutationReasonMaster(),tenantId,code,requestInfo);
	}


	/**
	 * Description : This method for getting mutation rate master details
	 * @param tenantId
	 * @param code
	 * @param requestInfo
	 * @return masterModel
	 * @throws Exception
	 */


	@Override
	public MasterModel getMutationRateMaster(String tenantId, String code,RequestInfo requestInfo) throws Exception {
		return	getMaster(masterList.getMutationRateMaster(),tenantId,code,requestInfo);
	}

	/**
	 *Description : This method for getting document type master details
	 * @param tenantId
	 * @param code
	 * @param requestInfo
	 * @return masterModel
	 * @throws Exception
	 */

	@Override
	public MasterModel getDocumentTypeMaster(String tenantId, String code,RequestInfo requestInfo) throws Exception {
		return	getMaster(masterList.getDocumentTypeMaster(),tenantId,code,requestInfo);
	}

	/**
	 * Description : This method will use for creating department type
	 * @param tenantId
	 * @param departmentRequest
	 * @return
	 */
	
	@Override
	public DepartmentResponseInfo createDepartmentMaster(String tenantId, DepartmentRequest departmentRequest) {
		// TODO Auto-generated method stub

		for(Department department:departmentRequest.getDepartments()){
			
			Gson gson = new GsonBuilder()
					.disableHtmlEscaping()
					.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
					.setExclusionStrategies(new ExcludeFileds())
					.create();

			String data= gson.toJson(department).toString();
			long time=new Date().getTime();
			String departmentType="insert into egpt_department_master(tenantId,code,data,createdBy,createdDate,modifiedBy,modifiedDate) values(?,?,?,?,?,?,?)";	
			final PreparedStatementCreator psc = new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
					final PreparedStatement ps = connection.prepareStatement(departmentType, new String[] { "id" });
					ps.setString(1, department.getTenantId());
					ps.setString(2, department.getCode());
					ps.setString(3,data);
					ps.setInt(4, department.getCreatedBy());
					ps.setLong(5, time);
					ps.setInt(6, department.getLastModifiedBy());
					ps.setLong(7, time);
					return ps;
				}
			};

			// The newly generated key will be saved in this object
			final KeyHolder holder = new GeneratedKeyHolder();
			jdbcTemplate.update(psc, holder);
			department.setId(holder.getKey().intValue());
			department.setCreatedDate(time);
			department.setLastModifiedDate(time);

		}
		ResponseInfo responseInfo=responseInfoFactory.createResponseInfoFromRequestInfo(departmentRequest.getRequestInfo(),true);
		
		DepartmentResponseInfo departmentResponse=new DepartmentResponseInfo();
		departmentResponse.setDepartments(departmentRequest.getDepartments());
		departmentResponse.setResponseInfo(responseInfo);
		return departmentResponse;
	}
}
