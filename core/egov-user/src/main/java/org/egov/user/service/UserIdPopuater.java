package org.egov.user.service;

import java.util.List;

import org.egov.user.model.Address;
import org.egov.user.model.Role;
import org.egov.user.model.TenantRole;
import org.egov.user.model.User;
import org.egov.user.model.UserReq;
import org.egov.user.utils.SequenceGenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UserIdPopuater {
	
	@Autowired
	private SequenceGenService sequenceGenService;
	
	@Value("${egov.seq.name.user}")
	private String userSeq;
	
	@Value("${egov.seq.name.address}")
	private String addSeq;
	
	@Value("${egov.seq.name.usertenant}")
	private String usertenantSeq;
	
	public void populateId(UserReq userReq){
		List<User> users = userReq.getUsers();
		
		Integer totalNoOfUser = users.size();
		Integer totalNoOfAddress = 0;
		Integer totalNoOfUserTenant = 0;
		
		for(User user:users){
			List<Address> addresses = user.getUserDetails().getAddresses();
			List<TenantRole> tenantRoles = user.getAdditionalroles();
			if(addresses != null)
				totalNoOfAddress = totalNoOfAddress + addresses.size();
			if(tenantRoles != null)
				totalNoOfUserTenant = totalNoOfUserTenant + tenantRoles.size();
		}
		
		List<Long> userIds = sequenceGenService.getIds(totalNoOfUser,userSeq);
		List<Long> addressIds = sequenceGenService.getIds(totalNoOfAddress,addSeq);
		List<Long> userTenantIds = sequenceGenService.getIds(totalNoOfUserTenant,usertenantSeq);
		
		int userIndex = 0;
		int addIndex = 0;
		int userTenantIndex = 0;
		for(User user : users) {
			user.setId(userIds.get(userIndex++));
			List<Address> addresses = user.getUserDetails().getAddresses();
			
			for(Address address : addresses){
				address.setId(addressIds.get(addIndex++));
				address.setUserId(user.getId());
				address.setTenantId(user.getTenantId());
			}
			
			for(Role role : user.getPrimaryrole()){
				role.setUserId(user.getId());
				role.setTenantId(user.getTenantId());
			}
			
			for(TenantRole tenantRole : user.getAdditionalroles()){
				tenantRole.setId(userTenantIds.get(userTenantIndex++));
				tenantRole.setUserId(user.getId());
				
			}
		}
		log.debug("UserIdPopuater userReq:"+userReq);
	}
	
	public void populateAddressAndUserTenantId(List<Address> addresses, List<TenantRole> tenantRoles){
		
		if(addresses !=null && addresses.size()>=1){
			List<Long> addressIds = sequenceGenService.getIds(addresses.size(),addSeq);
			int addIndex = 0;
			for(Address address : addresses)
				address.setId(addressIds.get(addIndex++));
			
		}
		if(tenantRoles !=null && tenantRoles.size()>=1){
			List<Long> userTenantIds = sequenceGenService.getIds(tenantRoles.size(),usertenantSeq);
			int userTenantIndex = 0;
			for(TenantRole tenantRole : tenantRoles)
				tenantRole.setId(userTenantIds.get(userTenantIndex++));
		}
	}
}
