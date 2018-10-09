/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any Long of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.collection.consumer.factory;

import javax.json.JsonObject;

import org.egov.common.contract.request.RequestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RequestInfoFactory {

    public static final Logger LOGGER = LoggerFactory.getLogger(RequestInfoFactory.class);

    public RequestInfo getRequestInfo(JsonObject requestInfoJSONObject, String tenantId) {

        RequestInfo requestInfo = RequestInfo.builder()
                .apiId(requestInfoJSONObject.isNull("apiId") ? null : requestInfoJSONObject.getString("apiId"))
                .ver(requestInfoJSONObject.isNull("ver") ? null : requestInfoJSONObject.getString("ver"))
                .ts(requestInfoJSONObject.isNull("ts") ? null : Long.valueOf(requestInfoJSONObject.getString("ts")))
                .action(requestInfoJSONObject.isNull("action") ? null : requestInfoJSONObject.getString("action"))
                .did(requestInfoJSONObject.isNull("did") ? null : requestInfoJSONObject.getString("did"))
                .key(requestInfoJSONObject.isNull("key") ? null : requestInfoJSONObject.getString("key"))
                .msgId(requestInfoJSONObject.isNull("msgId") ? null : requestInfoJSONObject.getString("msgId"))
                .authToken(requestInfoJSONObject.isNull("authToken") ? null : requestInfoJSONObject.getString("authToken"))
                .build();

        LOGGER.debug("requestInfo : " + requestInfo);
        return requestInfo;
    }

}
