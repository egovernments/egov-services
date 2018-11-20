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

package org.egov.egf.listner;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.egf.domain.model.contract.RequestInfo;
import org.egov.egf.listner.factory.RequestInfoFactory;
import org.egov.egf.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class EmployeeListener {

    public static final Logger LOGGER = LoggerFactory.getLogger(EmployeeListener.class);

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private RequestInfoFactory requestInfoFactory;

    @KafkaListener(topics = "${kafka.topics.employee.finance.name}")
    public void listen(ConsumerRecord<String, String> record) {
        LOGGER.info("key : " + record.key() + "\t\t" + "value : " + record.value());
        JsonReader jsonReader = Json.createReader(new StringReader(record.value()));
        JsonObject jsonObject = jsonReader.readObject();
        jsonReader.close();
        JsonObject requestInfoJSONObject = jsonObject.getJsonObject("RequestInfo");
        LOGGER.info("requestInfoJSONObject : " + requestInfoJSONObject);
        JsonObject employeeJSONObject = jsonObject.getJsonObject("Employee");
        LOGGER.info("employeeJSONObject : " + employeeJSONObject);
        String tenantId = employeeJSONObject.isNull("tenantId") ? null : employeeJSONObject.getString("tenantId");
        LOGGER.info("tenantId : " + tenantId);
        
        RequestInfo requestInfo = requestInfoFactory.getRequestInfo(requestInfoJSONObject, tenantId);

        // FIXME : Parse id in long once egf-masters update their AccountDetailKey Contract & table schema.
        	if(!employeeJSONObject.isNull("id")&&!employeeJSONObject.isNull("user")){
        		JsonObject user = employeeJSONObject.getJsonObject("user");
        		if(!user.isNull("name")){
        			employeeService.processRequest(String.valueOf(employeeJSONObject.getJsonNumber("id").longValue()),
        	                employeeJSONObject.getString("code") + "-" + user.getString("name"), tenantId, requestInfo);
        		}
        	}
        
        
    }

}