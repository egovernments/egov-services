
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

package org.egov.asset.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource(value = { "classpath:config/application-config.properties" }, ignoreResourceNotFound = true)
@Order(0)
public class ApplicationProperties {

	private static final String SEARCH_PAGESIZE_DEFAULT = "search.pagesize.default";
	public static final String SEARCH_PAGENO_MAX = "search.pageno.max";
	public static final String SEARCH_PAGESIZE_MAX = "search.pagesize.max";

	@Value("${egov.assetcategory.async}")
	private Boolean assetCategoryAsync;

	@Value("${kafka.topics.save.asset}")
	private String createAssetTopicName;

	@Value("${kafka.topics.update.asset}")
	private String updateAssetTopicName;

	@Value("${kafka.topics.save.assetcategory}")
	private String createAssetCategoryTopicName;

	@Value("${kafka.topics.update.assetcategory}")
	private String updateAssetCategoryTopicName;

	@Value("${kafka.topics.save.revaluation}")
	private String createAssetRevaluationTopicName;

	@Value("${kafka.topics.save.disposal}")
	private String createAssetDisposalTopicName;

	@Autowired
	private Environment environment;

	public String commonsSearchPageSizeDefault() {
		return environment.getProperty(SEARCH_PAGESIZE_DEFAULT);
	}

	public String commonsSearchPageNumberMax() {
		return environment.getProperty(SEARCH_PAGENO_MAX);
	}

	public String commonsSearchPageSizeMax() {
		return environment.getProperty(SEARCH_PAGESIZE_MAX);
	}

	public Boolean getAssetCategoryAsync() {
		return assetCategoryAsync;
	}

	public String getCreateAssetCategoryTopicName() {
		return createAssetCategoryTopicName;
	}

	public String getCreateAssetTopicName() {
		return createAssetTopicName;
	}

	public String getUpdateAssetTopicName() {
		return updateAssetTopicName;
	}

	public String getUpdateAssetCategoryTopicName() {
		return updateAssetCategoryTopicName;
	}

	public String getCreateAssetRevaluationTopicName() {
		return createAssetRevaluationTopicName;
	}

	public String getCreateAssetDisposalTopicName() {
		return createAssetDisposalTopicName;
	}

}