package org.egov.property.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.models.Address;
import org.egov.models.AuditDetails;
import org.egov.models.Document;
import org.egov.models.DocumentType;
import org.egov.models.Floor;
import org.egov.models.OwnerInfo;
import org.egov.models.Property;
import org.egov.models.PropertyDetail;
import org.egov.models.PropertyLocation;
import org.egov.models.PropertyResponse;
import org.egov.models.RequestInfo;
import org.egov.models.ResponseInfo;
import org.egov.models.ResponseInfoFactory;
import org.egov.models.User;
import org.egov.models.UserResponseInfo;
import org.egov.models.VacantLandDetail;
import org.egov.property.exception.PropertySearchException;
import org.egov.property.model.PropertyLocationRowMapper;
import org.egov.property.model.PropertyUser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * 
 * @author Prasad
 * This class will have the search APIs for RDMS/Elastic search 
 *
 */
@Service
public class PropertySearchService {

	@Autowired
	Environment environment;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	ResponseInfoFactory responseInfoFactory;


	/**
	 *<p>This method will search the documents in Database(Postgres)  with the given parameters</p> 
	 * 
	 * @author Prasad
	 * @param requestInfo
	 * @param tenantId
	 * @param active
	 * @param upicNo
	 * @param pageSize
	 * @param pageNumber
	 * @param sort
	 * @param oldUpicNo
	 * @param mobileNumber
	 * @param aadhaarNumber
	 * @param houseNoBldgApt
	 * @param revenueZone
	 * @param revenueWard
	 * @param locality
	 * @param ownerName
	 * @param demandFrom
	 * @param demandTo
	 * @return Property Object if search is successful or Error Object if search will fail
	 */

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PropertyResponse searchProperty( RequestInfo requestInfo, 
			String tenantId ,
			Boolean active,
			String upicNo,
			int pageSize,
			int pageNumber,
			String[] sort,
			String oldUpicNo,
			String mobileNumber,
			String aadhaarNumber,
			String houseNoBldgApt,
			int revenueZone,
			int revenueWard,
			int locality,
			String ownerName,
			int demandFrom,
			int demandTo ){

		StringBuffer searchPropertySql = new StringBuffer();
		Map<String,Object> userSearchRequestInfo=new HashMap<String,Object >();
		if ( ownerName!=null && !ownerName.isEmpty())
			userSearchRequestInfo.put("username",ownerName);

		if ( mobileNumber!=null && !mobileNumber.isEmpty())

			userSearchRequestInfo.put("mobileNumber",mobileNumber);

		if ( aadhaarNumber!=null && !aadhaarNumber.isEmpty())
			userSearchRequestInfo.put("aadhaarNumber", aadhaarNumber);

		userSearchRequestInfo.put("tenantId",tenantId);
		userSearchRequestInfo.put("RequestInfo",requestInfo);

		UserResponseInfo userResponse= restTemplate.postForObject(environment.getProperty("user.searchUrl"), userSearchRequestInfo, UserResponseInfo.class);
		String Ids = "";


		List<User> users =null;

		if ( userResponse.getUser()!=null){
			users = userResponse.getUser();

			int count = 1;

			for ( User user : users ){
				if (count < users.size())
					Ids = Ids+user.getId()+",";
				else
					Ids=Ids+""+user.getId()+"";

				count ++;
			}

		}

		searchPropertySql.append("select * from egpt_property prop  JOIN egpt_address Addr"
				+ " on Addr.property_id =  prop.id JOIN egpt_property_user puser on puser.property_id = prop.id where ");

		if ( tenantId!=null && !tenantId.isEmpty())
			searchPropertySql.append("prop.tenantid='"+tenantId.trim()+"'");

		if (active!=null)
			searchPropertySql.append("prop.active='"+active+"'");

		if ( upicNo!=null && !upicNo.isEmpty())
			searchPropertySql.append(" AND prop.upicNo='"+upicNo.trim()+"'");

		if (oldUpicNo!=null && !oldUpicNo.isEmpty())
			searchPropertySql.append(" AND prop.oldUpicNo='"+oldUpicNo.trim()+"'");

		if (!Ids.isEmpty())
			searchPropertySql.append(" AND puser.user_id IN ("+Ids+")");

		if ( houseNoBldgApt!=null && !houseNoBldgApt.isEmpty() )
			searchPropertySql.append(" AND Addr.housenobldgapt='"+houseNoBldgApt.trim()+"'");

		/*if ( revenueZone > 0)
			searchPropertySql.append(" AND puser.revenueZone= "+revenueZone);

		if (revenueWard > 0)
			searchPropertySql.append(" AND puser.revenueWard="+revenueWard);

		if (locality > 0)
			searchPropertySql.append(" AND puser.locality="+locality);*/


		if ( sort!=null && sort.length > 0 ){
			searchPropertySql.append(" ORDER BY ");

			// Count loop to add the coma ,

			int orderBycount = 1;

			StringBuffer orderByCondition= new StringBuffer();
			for (String order : sort){
				if ( orderBycount < sort.length)
					orderByCondition.append("prop."+order+",");
				else
					orderByCondition.append("prop."+order);
				orderBycount++;
			}

			if (orderBycount>1)
				orderByCondition.append(" asc");

			searchPropertySql.append(orderByCondition.toString());
		}

		//
		// Appending the pagination related logic,if the page size and page number is -1 
		// then we need to put the default page size and page number
		//

		if ( pageNumber == -1 )
			pageNumber	=	Integer.valueOf(environment.getProperty("default.page.number").trim());

		if ( pageSize == -1 )
			pageSize 	= 	Integer.valueOf(environment.getProperty("default.page.size").trim());

		int offset  =  0;
		int limit   =  pageNumber*pageSize;

		if ( pageNumber <=1 )
			offset = ( limit-pageSize );
		else
			offset = ( limit-pageSize )+1;

		searchPropertySql.append(" offset "+offset+" limit "+limit);

		List<Property> updatedPropety = null;

		try {

			List<Property> property = jdbcTemplate.query(searchPropertySql.toString(),
					new BeanPropertyRowMapper(Property.class));

			//
			// updating the property object ,with the all the details(Floor ,owner,property details.etc)
			//

			updatedPropety = addAllPropertyDetails(property,requestInfo,users);
		}
		catch (Exception e) {
			throw new PropertySearchException(environment.getProperty("invalid.input"),requestInfo);
		}

		PropertyResponse propertyResponse = new PropertyResponse();
		propertyResponse.setProperties(updatedPropety);
		ResponseInfo responseInfo=responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo,true);
		propertyResponse.setResponseInfo(responseInfo);

		return propertyResponse;

	}


	/**
	 * <p>This method will add the property details to the given list of property objects ,such as floors,owners etc</p>
	 * @author Prasad
	 * @param properties
	 * @return List of property Object's 
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 * @see ArrayList
	 * 
	 */

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<Property> addAllPropertyDetails(List<Property> properties,RequestInfo requestInfo,List<User> users) throws JsonParseException, JsonMappingException, IOException {

		List<Property> updatedPropertyDetails = new ArrayList<>();

		for ( Property property : properties ){

			//
			// get the address for property
			//

			Long propertyId = property.getId(); 

			String addressSql = "select * from egpt_address where property_id ="+propertyId;
			Address address = (Address) jdbcTemplate.queryForObject(addressSql, new BeanPropertyRowMapper(Address.class));
			property.setAddress(address);

			List<OwnerInfo> ownerInfos = new ArrayList<>();

			String userIdsSql = " select * from egpt_property_user where property_id ="+propertyId;
			List<PropertyUser> propertyUsers = jdbcTemplate.query(userIdsSql, new BeanPropertyRowMapper(PropertyUser.class));

			List<Integer> userIds = new ArrayList<>();

			for (PropertyUser propertyUser : propertyUsers ){

				userIds.add(propertyUser.getUserId());
			}

			List<User> userOfProperty = getUserObjectForUserIds(userIds,users);


			//get owener info for property

			for ( User propertyUser : userOfProperty ){

				Long userId = propertyUser.getId();
				String propertyUserSql 		= 	"select * from egpt_property_user where user_id ="+userId;
				ModelMapper modelMapper 	= 	new ModelMapper();
				OwnerInfo owernInfo 		= 	modelMapper.map(propertyUser, OwnerInfo.class);
				PropertyUser proprtyUser 	= 	(PropertyUser) jdbcTemplate.queryForObject(propertyUserSql, new BeanPropertyRowMapper(PropertyUser.class));
				owernInfo.setIsPrimaryOwner(proprtyUser.getIsPrimaryOwner());
				owernInfo.setIsSecondaryOwner(proprtyUser.getIsSecondaryOwner());
				owernInfo.setOwnerShipPercentage(proprtyUser.getOwnerShipPercentage());
				owernInfo.setOwnerType(proprtyUser.getOwnerType());
				ownerInfos.add(owernInfo);
			}

			property.setOwners(ownerInfos);

			//
			// get property details for property
			//

			String propertyDetailSql = " select * from egpt_propertydetails where property_id ="+propertyId;
			PropertyDetail propertyDetail = (PropertyDetail)jdbcTemplate.queryForObject( propertyDetailSql,new BeanPropertyRowMapper(PropertyDetail.class));
			property.setPropertyDetail(propertyDetail);

			//
			// get vacant land for property
			//

			String VacantLandSql = " select * from egpt_vacantland where property_id ="+propertyId;
			VacantLandDetail vacantLandDetail = (VacantLandDetail) jdbcTemplate.queryForObject(VacantLandSql , new BeanPropertyRowMapper(VacantLandDetail.class));
			property.setVacantLand(vacantLandDetail);

			//
			// get property location for property
			//

			String propertyLocationSql = "select * from egpt_propertylocation where property_id = ?";
			PropertyLocation propertyLocation = (PropertyLocation)jdbcTemplate.queryForObject(propertyLocationSql, new Object[] { propertyId }, new PropertyLocationRowMapper());
			property.setBoundary(propertyLocation);

			Long propertyDetailId = property.getPropertyDetail().getId();

			//
			// get the floors for the property detail
			//

			String floorSql = " select * from egpt_floors where property_details_id ="+propertyDetailId;
			List<Floor> floors = jdbcTemplate.query(floorSql ,new BeanPropertyRowMapper(Floor.class));
			property.getPropertyDetail().setFloors(floors);

			//
			// get  document for property detail
			//

			String documnetSql = "select * from egpt_document where property_details_id = "+propertyDetailId;
			List<Map<String, Object>> rows  = jdbcTemplate.queryForList(documnetSql);
			List<Document> documents = getDocumentObject(rows);
			property.getPropertyDetail().setDocuments(documents);

			//
			// Audit details for the property detail 
			//

			String auditDetailSql = "select createdBy,lastModifiedBy,createdTime,lastModifiedTime from egpt_propertydetails where property_id ="+propertyId;
			AuditDetails auditDetails = (AuditDetails)jdbcTemplate.queryForObject(auditDetailSql, new BeanPropertyRowMapper(AuditDetails.class));
			property.getPropertyDetail().setAuditDetails(auditDetails);

			updatedPropertyDetails.add(property);

		}
		return updatedPropertyDetails;

	}


	/**
	 * 
	 * @author Prasad
	 * @param documentList
	 */


	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<Document> getDocumentObject(List<Map<String, Object>> documentList) {

		List<Document> documents = new ArrayList<>();

		for (Map<String,Object> documentdata : documentList){
			Document document = new Document();
			String documentTypeSQl = "select * from egpt_documenttype where document_id= ?";
			int documentId= (int) documentdata.get("id");
			DocumentType documentType = (DocumentType)jdbcTemplate.queryForObject(documentTypeSQl,  new Object[] { documentId }, new BeanPropertyRowMapper( DocumentType.class));
			document.setDocumentType(documentType);
			document.setFileStore(documentdata.get("filestore").toString());
			document.setId(Long.valueOf(documentdata.get("id").toString()));
			documents.add(document);

		}

		return documents;

	}


	/**
	 * <p>This method will give you the user Objects which has the given userIds<p>
	 * @author Prasad
	 * @param userIds
	 * @param users
	 * @return List Of user Object
	 */
	
	private List<User> getUserObjectForUserIds(List<Integer> userIds, List<User> users) {

		List<User> userList = 	new ArrayList<User>();
		for ( User user : users )
		{
			Long userId 	=  user.getId();
			if (userIds.contains(userId.intValue()) )
			{
				userList.add(user);
			}
		}
		return userList;
	}

}
