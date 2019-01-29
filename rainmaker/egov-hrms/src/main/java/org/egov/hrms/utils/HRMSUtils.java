package org.egov.hrms.utils;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class HRMSUtils {
	
	@Value("${egov.hrms.default.pwd.length}")
	private Integer pwdLength;
	
	public String generatePassword(List<String> params) {
		StringBuilder password = new StringBuilder();
		Random random = new Random();
		for(int i = 0; i < params.size(); i++) {
			String param = params.get(i);
			password.append(param.split("")[random.nextInt(param.length() - 1)]);
			if(password.length() > pwdLength)
				break;
			else {
				if(i == params.size())
					i = 0;
			}
		}
		return password.toString();
	}

}
