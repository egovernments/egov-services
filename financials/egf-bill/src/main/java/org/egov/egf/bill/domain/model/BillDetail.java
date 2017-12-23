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
package org.egov.egf.bill.domain.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.codehaus.jackson.annotate.JsonProperty;
import org.egov.egf.bill.web.contract.ChartOfAccount;
import org.egov.egf.bill.web.contract.Function;
import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BillDetail {

    /**
     * tenantId Unique Identifier of the tenant, Like AP, AP.Kurnool etc. represents the client for which the transaction is
     * created.
     * @return tenantId
     **/
    @NotNull
    @Size(min = 0, max = 256)
    @JsonProperty("tenantId")
    private String tenantId = null;

    /**
     * Unique Identifier of the BillDetail
     * @return id
     **/
    private String id;

    /**
     * order id of the BillDetail
     * @return orderId
     **/
    private Integer orderId;

    private ChartOfAccount chartOfAccount;

    /**
     * glcode of the BillDetail
     * @return glcode
     **/
    @NotNull
    @Length(max = 16)
    private String glcode;

    /**
     * debit amount of the BillDetail
     * @return debitAmount
     **/
    @NotNull
    @Min(value = 0)
    @Max(value = 999999999)
    private BigDecimal debitAmount;

    @NotNull
    @Min(value = 0)
    @Max(value = 999999999)
    private BigDecimal creditAmount;

    /**
     * function of the BillDetail
     * @return function
     **/
    private Function function;

    /**
     * bill payee details of the BillDetail
     * @return billPayeeDetails
     **/

    private List<BillPayeeDetail> billPayeeDetails = new ArrayList<>();

    /**
     * Get auditDetails
     * @return auditDetails
     **/
    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

}
