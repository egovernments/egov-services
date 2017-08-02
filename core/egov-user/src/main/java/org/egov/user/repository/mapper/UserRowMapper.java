package org.egov.user.repository.mapper;


import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.user.model.Address;
import org.egov.user.model.Role;
import org.egov.user.model.TenantRole;
import org.egov.user.model.User;
import org.egov.user.model.UserDetails;
import org.egov.user.model.enums.AddressType;
import org.egov.user.model.enums.BloodGroup;
import org.egov.user.model.enums.Gender;
import org.egov.user.model.enums.Type;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserRowMapper implements ResultSetExtractor<List<User>> {

	@Override
	public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
		Map<Long, User> map = new HashMap<Long, User>();
		Gender [] genders = Gender.values();
		
		while (rs.next()) {
			Long userId = (Long)rs.getObject("usr_id");

			log.info("user id in row mapper" + userId);

			User user = map.get(userId);
			UserDetails userDetails = null;
			if (user == null) {
				
				user = new User();
				user.setId(userId);
				user.setTenantId(rs.getString("usr_tenantid"));
				user.setUsername(rs.getString("usr_username"));
				user.setMobile(rs.getString("usr_mobilenumber"));
				user.setEmail(rs.getString("usr_emailid"));
				//FIXME: How to get the auth token
				//user.setAuthToken(authToken);
				
				user.setSalutation(rs.getString("usr_salutation"));
				user.setName(rs.getString("usr_name"));
				
					
				user.setGender(genders[Integer.parseInt(rs.getString("usr_gender"))]);
				user.setAadhaarNumber(rs.getString("usr_aadhaarnumber"));
				user.setActive(rs.getBoolean("usr_active"));
				
				//FIXME: date convert from date to long
				Date date = rs.getDate("usr_pwdexpirydate");
				
				if(date != null) 
				user.setPwdExpiryDate(date.getTime());
				
				user.setLocale(rs.getString("usr_locale"));
				user.setType(Type.valueOf(rs.getString("usr_type")));
				user.setAccountLocked(rs.getBoolean("usr_accountlocked"));	
				
				userDetails = new UserDetails();
				
				//userDetails.setFirstName(firstName);
				//userDetails.setMiddleName(middleName);
				//userDetails.setLastName(lastName);
				userDetails.setDob(rs.getDate("usr_dob"));
				userDetails.setAltContactNumber(rs.getString("usr_altcontactnumber"));
				//userDetails.setFatherName(fatherName);
				//userDetails.setHusbandName(husbandName);
				userDetails.setBloodGroup(BloodGroup.fromValue(rs.getString("usr_bloodgroup")));
				
				userDetails.setPan(rs.getString("usr_pan"));
				userDetails.setSignature(rs.getString("usr_signature"));
				userDetails.setPhoto(rs.getString("usr_photo"));
				
				user.setUserDetails(userDetails);
				map.put(userId, user);
			}
			List<Role> roles = user.getPrimaryrole();
			if(roles == null){
				roles = new ArrayList<>();
				user.setPrimaryrole(roles);
			}
			Long roleId = (Long)rs.getObject("role_id");
			if(roleId != null){					
				Role role = new Role();
				
				role.setId(roleId);
				role.setCode(rs.getString("role_code"));
				role.setName(rs.getString("role_name"));
				role.setDescription(rs.getString("role_description"));
				boolean flag = roles.contains(role);
				
				if(!flag)
				roles.add(role);
			}
		
			List<TenantRole> tenantRoles = user.getAdditionalroles();
			if(tenantRoles == null){
				tenantRoles = new ArrayList<>();
				user.setAdditionalroles(tenantRoles);
			}
			Long tenantRoleId = (Long)rs.getObject("utenanant_id");
			if(tenantRoleId != null){
				TenantRole tenantRole = new TenantRole();
				tenantRole.setId(tenantRoleId);
				tenantRole.setTenantId(rs.getString("utenanant_tenantids"));
				boolean flag = tenantRoles.contains(tenantRole);
				
				if(!flag)
				tenantRoles.add(tenantRole);
			}
			
			List<Address> addresses = user.getUserDetails().getAddresses();
			if(addresses == null){
				addresses = new ArrayList<>();
				userDetails.setAddresses(addresses);
			}
			Long addId = (Long)rs.getObject("address_id");
			if(addId != null){
				Address address = new Address();
				address.setId(addId);
				address.setAddressType(AddressType.valueOf(rs.getString("address_type")));
				address.setAddressLine1(rs.getString("address_address"));
				address.setCity(rs.getString("address_city"));
				address.setPincode(rs.getString("address_pincode"));
				address.setTenantId(rs.getString("address_tenantid"));
				boolean flag = addresses.contains(address);
				
				if(!flag)
					addresses.add(address);
			}
			
		}
		return new ArrayList<>(map.values());
	}
}	

