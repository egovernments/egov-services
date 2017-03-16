package org.egov.eis.service.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.eis.model.EmployeeInfo;
import org.egov.eis.model.User;
import org.springframework.stereotype.Component;

@Component
public class EmployeeUserMapper {

	public List<EmployeeInfo> mapUsersWithEmployees(List<EmployeeInfo> employeeInfoList, List<User> userInfoList) {
		Map<Long, User> userInfoMap = new HashMap<Long, User>();

		for (User userInfo : userInfoList) {
			userInfoMap.put(userInfo.getId(), userInfo);
		}

		for (EmployeeInfo employeeInfo : employeeInfoList) {
			if (userInfoMap.containsKey(employeeInfo.getId())) {
				User userInfo = userInfoMap.get(employeeInfo.getId());

				employeeInfo.setSalutation(userInfo.getSalutation());
				employeeInfo.setName(userInfo.getName());
				employeeInfo.setUserName(userInfo.getUserName());
				employeeInfo.setGender(userInfo.getGender());
				employeeInfo.setMobileNumber(userInfo.getMobileNumber());
				employeeInfo.setEmailId(userInfo.getEmailId());
				employeeInfo.setPan(userInfo.getPan());
				employeeInfo.setAadhaarNumber(userInfo.getAadhaarNumber());
				employeeInfo.setType(userInfo.getType());
				employeeInfo.setActive(userInfo.getActive());
			}
		}
		return employeeInfoList;
	}
}