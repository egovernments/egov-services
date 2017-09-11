
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
import org.springframework.core.env.Environment;

import lombok.Getter;

@Configuration
@Getter
public class ApplicationProperties {

    @Autowired
    private Environment environment;

    public String getBootstrapServer() {
        return environment.getProperty("spring.kafka.bootstrap.servers");
    }

    @Value("${kafka.topics.save.asset}")
    private String createAssetTopicName;

    @Value("${kafka.topics.update.asset}")
    private String updateAssetTopicName;

    @Value("${egov.services.boundary_service.hostname}")
    private String boundaryServiceHostName;

    @Value("${egov.services.boundary_service.searchpath}")
    private String boundaryServiceSearchPath;

    @Value("${egov.services.asset.indexer.host}")
    private String indexerHost;

    @Value("${egov.services.asset.indexer.name}")
    private String assetIndexName;

    @Value("${egov.services.disposal.indexer.name}")
    private String disposalIndexName;

    @Value("${egov.services.revaluation.indexer.name}")
    private String revaluationIndexName;

    @Value("${egov.services.assetcategory.indexer.name}")
    private String assetCategoryIndex;

    @Value("${kafka.topics.save.assetcategory}")
    private String saveassetCategoryTopic;

    @Value("${kafka.topics.update.assetcategory}")
    private String updateAssetCategoryTopic;

    @Value("${kafka.topics.save.revaluation}")
    private String createAssetRevaluationTopicName;

    @Value("${kafka.topics.update.revaluation}")
    private String updateAssetRevaluationTopicName;

    @Value("${egov.services.egf_service.hostname}")
    private String egfServiceHostName;

    @Value("${egov.services.egf_service.functions.searchpath}")
    private String egfServiceFunctionsSearchPath;

    @Value("${egov.services.egf_service.funds.searchpath}")
    private String egfServiceFundsSearchPath;

    @Value("${egov.services.egf_service.schemes.searchpath}")
    private String egfServiceSchemesSearchPath;

    @Value("${egov.services.egf_service.subschemes.searchpath}")
    private String egfServiceSubSchemesSearchPath;

    @Value("${egov.services.asset_service.hostname}")
    private String assetServiceHostName;

    @Value("${egov.services.asset_service.searchpath}")
    private String assetServiceSearchPath;

    @Value("${kafka.topics.save.disposal}")
    private String createAssetDisposalTopicName;

    @Value("${kafka.topics.update.disposal}")
    private String updateAssetDisposalTopicName;

    @Value("${egov.services.tenant.host}")
    private String tenantServiceHostName;

    @Value("${egov.services.tenant.search.path}")
    private String tenantServiceSearchPath;

    @Value("${kafka.topics.save.depreciation}")
    private String saveDepreciationTopic;

    @Value("${kafka.topics.save.currentvalue}")
    private String saveCurrentvalueTopic;

    @Value("${egov.services.depreciaition.indexer.url}")
    private String depreciaitionIndexUrl;

    @Value("${egov.services.currentvalue.indexer.url}")
    private String currentValueIndexUrl;

}