/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.web.notification.sms.config.properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.List;

@Configuration
public class ApplicationProperties {

	public static final String SMS_ENABLED = "sms.enabled";
	public static final String SMS_PROVIDER_URL = "sms.provider.url";
	public static final String SMS_SENDER_USERNAME = "sms.sender.username";
	public static final String SMS_SENDER_PASWRD = "sms.sender.password";
	public static final String SMS_SENDER = "sms.sender";
	public static final String SMS_ERROR_CODES = "sms.error.codes";

	@Autowired
	private Environment environment;

	public String smsProviderURL() {
		return this.environment.getProperty(SMS_PROVIDER_URL);
	}

	public String smsSenderUsername() {
		return this.environment.getProperty(SMS_SENDER_USERNAME);
	}

	public String smsSenderPassword() {
		return this.environment.getProperty(SMS_SENDER_PASWRD);
	}

	public String smsSender() {
		return this.environment.getProperty(SMS_SENDER);
	}

	public boolean smsEnabled() {
		return this.environment.getProperty(SMS_ENABLED, Boolean.class);
	}

	public String smsResponseMessageForCode(String errorCode) {
		return this.environment.getProperty(errorCode, "No Message");
	}

	public List<String> smsErrorCodes() {
		return Arrays.asList(this.environment.getProperty(SMS_ERROR_CODES).split(","));
	}

	public String getProperty(String propKey) {
		return this.environment.getProperty(propKey, "");
	}

	public <T> T getProperty(String propKey, Class<T> type) {
		return this.environment.getProperty(propKey, type);
	}

}