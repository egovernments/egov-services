/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.empernments.org
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
 *  In case of any queries, you can reach eGovernments Foundation at contact@empernments.org.
 */

package org.egov.collection.consumer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PropertiesManager {

    @Value("${jalandhar.egov.fin.coe.erp.host}")
    private String jalandharErpHostUrl;

    @Value("${mohali.egov.fin.coe.erp.host}")
    private String mohaliErpHostUrl;

    @Value("${nayagaon.egov.fin.coe.erp.host}")
    private String nayagaonErpHostUrl;

    @Value("${amritsar.egov.fin.coe.erp.host}")
    private String amritsarErpHostUrl;

    @Value("${kharar.egov.fin.coe.erp.host}")
    private String khararErpHostUrl;

    @Value("${zirakpur.egov.fin.coe.erp.host}")
    private String zirakpurErpHostUrl;

    @Value("${egov.services.host}")
    private String hostUrl;

    @Value("${egov.services.common.masters.businessdetails.url}")
    private String businessDetailsServiceUrl;

    @Value("${egov.services.egf.voucher.create}")
    private String voucherCreateUrl;

    @Value("${si.microservice.user}")
    private String siUser;

    @Value("${si.microservice.password}")
    private String siPassword;

    @Value("${si.microservice.usertype}")
    private String siUserType;

    @Value("${si.microservice.scope}")
    private String siScope;

    @Value("${si.microservice.granttype}")
    private String siGrantType;

    @Value("${egov.services.user.token.url}")
    private String tokenGenUrl;

    @Value("${egov.services.egf.master.financialstatuses.search}")
    private String financialStatusesSearch;

    @Value("${egov.services.egf.instrument.instruments.create}")
    private String instrumentCreate;

    @Value("${egov.services.collection.services.receipts.update}")
    private String receiptsUpdate;

    public String getErpURLBytenantId(String tenantId) {
        String url = null;

        switch (tenantId) {
        case "pb.jalandhar":
            url = jalandharErpHostUrl;
            break;

        case "pb.mohali":
            url = mohaliErpHostUrl;
            break;

        case "pb.nayagaon":
            url = nayagaonErpHostUrl;
            break;

        case "pb.amritsar":
            url = amritsarErpHostUrl;
            break;

        case "pb.kharar":
            url = khararErpHostUrl;
            break;

        case "pb.zirakpur":
            url = zirakpurErpHostUrl;
            break;

        default:
            break;
        }

        return url;

    }

}