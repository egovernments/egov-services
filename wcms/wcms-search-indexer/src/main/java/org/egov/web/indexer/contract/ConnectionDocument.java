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
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.web.indexer.contract;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConnectionDocument {

    private static final String ES_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    @JsonProperty("tenantId")
    private String tenantId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT)
    @JsonProperty("connectionDate")
    private Date connectionDate;

    @JsonProperty("connectionId")
    private Long connectionId;

    @JsonProperty("connectionType")
    private String connectionType;

    @JsonProperty("applicationType")
    private String applicationType;

    @JsonProperty("billingType")
    private String billingType;

    @JsonProperty("categoryId")    
    private String categoryId;

    @JsonProperty("pipesizeId")
    private String pipesizeId;
    
    @JsonProperty("supplyTypeId")
    private String supplyTypeId;

    @JsonProperty("propertyIdentifier")
    private String propertyIdentifier;

    @JsonProperty("sourceTypeId")
    private String sourceTypeId;

    @JsonProperty("waterTreatmentId")
    private String waterTreatmentId;

    @JsonProperty("connectionStatus")
    private String connectionStatus;

    @JsonProperty("status")
    private String status;

    @JsonProperty("stateId")
    private Long stateId;

    @JsonProperty("acknowledgementNumber")
    private String acknowledgementNumber;

    @JsonProperty("consumerNumber")
    private String consumerNumber;

}
