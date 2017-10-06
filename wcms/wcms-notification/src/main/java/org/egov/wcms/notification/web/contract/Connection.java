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
package org.egov.wcms.notification.web.contract;

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
    private String connectionStatus;

    @NotNull
    private String acknowledgementNumber;

    @NotNull
    private String consumerNumber;

    @NotNull
    private String status;

    @NotNull
    private String applicationType;

    @NotNull
    private String billingType;

    private Long userid;

    private String pipesizeId;

    @NotNull
    private String hscPipeSizeType;

    private String supplyTypeId;

    @NotNull
    private String supplyType;

    private String sourceTypeId;

    @NotNull
    private String sourceType;

    private String waterTreatmentId;

    private String waterTreatment;

    private String storageReservoirId;

    @NotNull
    private String storageReservoir;

    @NotNull
    private String usageType;

    private String usageTypeName;

    private String usageTypeId;

    @NotNull
    private String subUsageType;

    private String subUsageTypeName;

    private String subUsageTypeId;

    private int noOfFlats;

    private int numberOfFamily;

    @NotNull
    private double sumpCapacity;

    @NotNull
    private int numberOfTaps;

    @NotNull
    private int numberOfPersons;

    private String propertyIdentifier;

    private String assetIdentifier;

    @NotNull
    private Long stateId;

    private String demandid;

    @NotNull
    private double donationCharge;

    @NotNull
    private long manualReceiptDate;

    @NotNull
    private String manualReceiptNumber;

    @NotNull
    private String estimationNumber;

    @NotNull
    private String workOrderNumber;

    @NotNull
    private String legacyConsumerNumber;

    private Double billSequenceNumber;

    private String manualConsumerNumber;

    private Long executionDate;

    private String houseNumber;

    @NotNull
    private String bplCardHolderName;

    @NotNull
    private long parentConnectionId;

    @NotNull
    private List<DocumentOwner> documents;

    @NotNull
    private List<ConnectionDemand> connectionDemands;

    @NotNull
    private Property property;

    @NotNull
    private Demand demand;

    private List<ConnectionOwner> connectionOwners;

    private Address address;

    private ConnectionLocation connectionLocation;

    @NotNull
    private List<Meter> meter;

    @NotNull
    private AuditDetails auditDetails;

    private WorkflowDetails workflowDetails;

    private String createdDate;

    private String plumberName;

    @NotNull
    private String tenantId;

    private Boolean outsideULB;

    private Boolean withProperty;

    @NotNull
    private Boolean isLegacy;

    private PeriodCycle periodCycle;

}
