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

package org.egov.citizen.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import lombok.Getter;
import lombok.ToString;

@Configuration
@Getter
@PropertySource(value = { "classpath:config/application-config.properties" }, ignoreResourceNotFound = true)
@ToString
public class ApplicationProperties {

	private static final String SEARCH_PAGESIZE_DEFAULT = "search.pagesize.default";

	@Autowired
	private Environment environment;

	@Value("${kafka.topics.save.service}")
	private String createServiceTopic;
	
	@Value("${kafka.topics.save.service.key}")
	private String createServiceTopicKey;
	
	@Value("${kafka.topics.update.service}")
	private String updateServiceTopic;
	
	@Value("${kafka.topics.update.service.key}")
	private String updateServiceTopicKey;
	
	@Value("${egov.services.billing_service.hostname}")
	private String billingServiceHostName;
	
	@Value("${egov.services.billing_service.search}")
	private String searchBill;
	
	@Value("${egov.services.billing_service.bill.generate}")
	private String generateBill;
	
	@Value("${egov.services.collection_service.hostname}")
	private String collectionServiceHostName;
	
	@Value("${egov.services.collection_service.receipt.create}")
	private String createReceiptURI;
	
	@Value("${egov.services.collection_service.receipt.search}")
	private String searchReceiptURI;
	
	@Value("${egov.services.billing_service.dues.search}")
	private String searchDues;
	
	@Value("${egov.citizen.service.businessservice}")
	private String businessService;

	@Value("${egov.citizen.service.taxPeriodFrom}")
	private String taxPeriodFrom;
	
	@Value("${egov.citizen.service.taxPeriodTo}")
	private String taxPeriodTo;
	
	@Value("${egov.citizen.service.pgrequest.hash.key}")
	private String hashKey;
	
	@Value("${egov.citizen.redirect.hostname}")
	private String redirectHostName;
	
	@Value("${egov.citizen.redirect.url}")
	private String redirectUrl;
	
	@Value("${egov.citizen.redirect}")
	private String redirectAppend;

	@Value("${egov.citizen.return.url}")
	private String returnUrl;

			
	public String commonsSearchPageSizeDefault() {
		return environment.getProperty(SEARCH_PAGESIZE_DEFAULT);
	}
}