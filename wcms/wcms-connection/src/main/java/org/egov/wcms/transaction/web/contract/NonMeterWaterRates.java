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
package org.egov.wcms.transaction.web.contract;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
@Builder
public class NonMeterWaterRates {

    public static final String SEQ_NONMETERWATERRATES = "seq_egwtr_non_meter_water_rates";

    private Long id;

    @NotNull
    private String code;

    @NotNull
    private String billingType;

    @NotNull
    private String connectionType;

    @NotNull
    private String usageTypeCode;
    
    private String usageTypeName;

    @NotNull
    private Long usageTypeId;

    private String subUsageTypeCode;
    
    private String subUsageTypeName;

    private Long subUsageTypeId;

    @NotNull
    private String sourceTypeName;

    @NotNull
    private Long sourceTypeId;

    @NotNull
    private Double pipeSize;
    
    private Double pipeSizeInInch;

    @NotNull
    private Long pipeSizeId;

    @NotNull
    private Long fromDate;
    @NotNull
    private Long noOfTaps;

    @NotNull
    private Double amount;

    @NotNull
    private Boolean active;
    
    private Boolean outsideUlb;

    @Size(min=4,max = 128)
    @NotNull
    private String tenantId;

    @NotNull
    @JsonIgnore
    private Long createdBy;

    @JsonIgnore
    private Long createdDate;

    @NotNull
    @JsonIgnore
    private Long lastModifiedBy;
    @JsonIgnore
    private Long lastModifiedDate;
}
