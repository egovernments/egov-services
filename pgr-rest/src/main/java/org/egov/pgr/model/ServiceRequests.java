package org.egov.pgr.model;

import org.egov.pgr.entity.Complaint;

import java.util.ArrayList;
import java.util.List;

public class ServiceRequests {
    public static List<ServiceRequest> createServiceRequestsFromComplaints(List<Complaint> complaints) {
        List<ServiceRequest> serviceRequests = new ArrayList<>();
        for (Complaint complaint : complaints) {
            serviceRequests.add(ServiceRequest.createFromComplaint(complaint));
        }
        return serviceRequests;
    }
}
