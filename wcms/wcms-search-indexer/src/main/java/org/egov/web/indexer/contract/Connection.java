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

import java.util.List;

import javax.validation.constraints.NotNull;

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
public class Connection {

    @NotNull
    private long id;

    @NotNull
    private String connectionType;

    @NotNull
    private String applicationType;

    @NotNull
    private String billingType;

    @NotNull
    private String categoryType;

    private String categoryId;

    @NotNull
    private String hscPipeSizeType;

    private String pipesizeId;
    
    private Long executionDate;

    @NotNull
    private String supplyType;
    
    private int noOfFlats;

    private String supplyTypeId;

    private String propertyIdentifier;

    private String assetIdentifier;

    @NotNull
    private String sourceType;

    private String sourceTypeId;

    @NotNull
    private String waterTreatment;

    private String waterTreatmentId;

    @NotNull
    private String connectionStatus;

    @NotNull
    private String status;

    @NotNull
    private Long stateId;

    private String demandid;
    @NotNull
    private double sumpCapacity;

    @NotNull
    private double donationCharge;

    @NotNull
    private int numberOfTaps;

    @NotNull
    private int numberOfPersons;

    @NotNull
    private String legacyConsumerNumber;
    
    @NotNull
    private String estimationNumber;
    
    
    @NotNull
    private String workOrderNumber;

    @NotNull
    private Boolean isLegacy;

    @NotNull
    private String acknowledgementNumber;

    @NotNull
    private String consumerNumber;

    @NotNull
    private String bplCardHolderName;

    @NotNull
    private long parentConnectionId;

    @NotNull
    private List<DocumentOwner> documents;
    
    @NotNull
    private List<ConnectionDemand> connectionDemands;

    @NotNull
    private List<Timeline> timelines;

    @NotNull
    private Property property;
    
    @NotNull
    private Demand demand;

    @NotNull
    private Asset asset;

    @NotNull
    private List<Meter> meter;

    @NotNull
    private List<EstimationCharge> estimationCharge;

    @NotNull
    private WorkOrder workOrder;

    @NotNull
    private AuditDetails auditDetails;

    @NotNull
    private String tenantId;

    private WorkflowDetails workflowDetails;
    
    private String createdDate; 

}
