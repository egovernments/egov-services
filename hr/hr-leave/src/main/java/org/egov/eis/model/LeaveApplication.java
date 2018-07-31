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

package org.egov.eis.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
public class LeaveApplication {

    private Long id;

    @Size(max = 100)
    private String applicationNumber;

    @NotNull
    private Long employee;

    private LeaveType leaveType;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date fromDate;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date toDate;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date compensatoryForDate;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date prefixDate;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date suffixDate;

    private List<String> holidays;

    private Float leaveDays;

    private Float workingDays;

    private Float availableDays;

    private Float totalLeavesEligible;

    private Float balance;

    private Float noOfDays;

    private Integer halfdays;

    private String leaveGround;

    private Boolean firstHalfleave;

    private Boolean encashable;

    @Size(min = 5, max = 500)
    private String reason;

    private Long status;

    private Long stateId;

    private List<String> documents = new ArrayList<>();

    private Long createdBy;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date createdDate;

    private Long lastModifiedBy;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date lastModifiedDate;

    @Size(max = 256)
    private String tenantId;

    private WorkFlowDetails workflowDetails;

    private String errorMsg;

}