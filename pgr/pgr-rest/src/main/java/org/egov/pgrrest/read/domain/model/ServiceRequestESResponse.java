package org.egov.pgrrest.read.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.egov.pgrrest.common.domain.model.AttributeEntry;
import org.egov.pgrrest.common.domain.model.AuthenticatedUser;
import org.egov.pgrrest.common.domain.model.Requester;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.springframework.util.StringUtils.isEmpty;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceRequestESResponse {

    private String assigneeId;

    private String crn;

    private String childLocationId;

    private String createdDate;

    private String departmentId;

    private String designationId;

    private String details;

    private String escalationDate;

    private String externalCrn;

    private List<String> keyword;

    private String landmarkDetails;

    private String lastModifiedDate;

    private String previousAssignee;

    private String priority;

    private String rating;

    private String receivingCenter;

    private String receivingMode;

    private String requesterAddress;

    private String requesterEmail;

    private String requesterName;

    private String requesterMobile;

    private String serviceGeo;

    private String serviceSLADays;

    private String serviceStatusCode;

    private String serviceTypeCode;

    private String serviceTypeName;

    private String stateId;

    private String tenantId;

    private String wardName;

    private String wardNo;

    public ServiceRequest toModel(String timeZone) {
        return ServiceRequest.builder()
            .serviceRequestLocation(getServiceRequestLocation())
            .authenticatedUser(AuthenticatedUser.createAnonymousUser())
            .address(landmarkDetails)
            .description(details)
            .requester(getRequester())
            .serviceRequestType(getServiceRequestType())
            .crn(crn)
            .createdDate(toDate(createdDate, timeZone))
            .lastModifiedDate(toDate(lastModifiedDate, timeZone))
            .mediaUrls(Collections.emptyList())
            .escalationDate(toDate(escalationDate, timeZone))
            .tenantId(tenantId)
            .attributeEntries(getAttributes())
            .build();
    }

    private Requester getRequester() {
        return Requester.builder()
            .firstName(requesterName)
            .email(requesterEmail)
            .mobile(requesterMobile)
            .address(requesterAddress)
            .build();
    }

    private ServiceRequestLocation getServiceRequestLocation() {
        if (!isEmpty(serviceGeo)) {
            String[] latLong = serviceGeo.split(",");
            Double latitude = Double.valueOf(latLong[0]);
            Double longitude = Double.valueOf(latLong[1]);
            Coordinates coordinates = new Coordinates(latitude, longitude);
            return new ServiceRequestLocation(coordinates, null, null);
        } else {
            return new ServiceRequestLocation(null, null, null);
        }
    }

    private ServiceRequestType getServiceRequestType() {
        return ServiceRequestType.builder()
            .code(serviceTypeCode)
            .name(serviceTypeName)
            .tenantId(tenantId)
            .build();
    }

    private List<AttributeEntry> getAttributes() {
        List<AttributeEntry> attributeEntries = new ArrayList<>();
        if (!isEmpty(departmentId)) {
            attributeEntries.add(new AttributeEntry("systemDepartmentId", departmentId));
        }
        if (!isEmpty(designationId)) {
            attributeEntries.add(new AttributeEntry("systemDesignationId", designationId));
        }
        if (!isEmpty(assigneeId)) {
            attributeEntries.add(new AttributeEntry("systemPositionId", assigneeId));
        }
        if (!isEmpty(serviceSLADays)) {
            attributeEntries.add(new AttributeEntry("systemEscalationHours", serviceSLADays));
        }
        if (!isEmpty(wardNo)) {
            attributeEntries.add(new AttributeEntry("systemLocationId", wardNo));
        }
        if (!isEmpty(stateId)) {
            attributeEntries.add(new AttributeEntry("systemStateId", stateId));
        }
        if (!isEmpty(requesterAddress)) {
            attributeEntries.add(new AttributeEntry("systemRequesterAddress", requesterAddress));
        }
        if (this.keyword.size() > 0) {
            attributeEntries.add(new AttributeEntry("keyword", this.keyword.get(0)));
        }
        if (!isEmpty(wardName)) {
            attributeEntries.add(new AttributeEntry("systemLocationName", wardName));
        }
        if (!isEmpty(receivingMode)) {
            attributeEntries.add(new AttributeEntry("systemReceivingMode", receivingMode));
        }
        if (!isEmpty(serviceStatusCode)) {
            attributeEntries.add(new AttributeEntry("systemStatus", serviceStatusCode));
        }
        if (!isEmpty(childLocationId)) {
            attributeEntries.add(new AttributeEntry("systemChildLocationId", childLocationId));
        }
        if (!isEmpty(rating)) {
            attributeEntries.add(new AttributeEntry("systemRating", rating));
        }
        if (!isEmpty(receivingCenter)) {
            attributeEntries.add(new AttributeEntry("systemReceivingCenter", receivingCenter));
        }
        if (!isEmpty(previousAssignee)) {
            attributeEntries.add(new AttributeEntry("systemPreviousAssignee", previousAssignee));
        }
        if (!isEmpty(externalCrn)) {
            attributeEntries.add(new AttributeEntry("systemExternalCRN", externalCrn));
        }

        if (!isEmpty(priority)) {
            attributeEntries.add(new AttributeEntry("PRIORITY", priority));
        }

        return attributeEntries;
    }

    private Date toDate(String date, String timeZone) {
        SimpleDateFormat ES_DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        SimpleDateFormat INPUT_DATE_TIME_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        ES_DATE_TIME_FORMAT.setTimeZone(TimeZone.getTimeZone(timeZone));
        INPUT_DATE_TIME_FORMAT.setTimeZone(TimeZone.getTimeZone(timeZone));

        if (isEmpty(date)) {
            return null;
        }
        try {
            final Date esDate = ES_DATE_TIME_FORMAT.parse(date);
            String formatedDate = INPUT_DATE_TIME_FORMAT.format(esDate);
            Date convertedDate = INPUT_DATE_TIME_FORMAT.parse(formatedDate);
            return convertedDate;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
