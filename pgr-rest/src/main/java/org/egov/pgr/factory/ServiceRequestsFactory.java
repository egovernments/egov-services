package org.egov.pgr.factory;

import org.egov.pgr.entity.Complaint;
import org.egov.pgr.model.ServiceRequest;

import java.util.ArrayList;
import java.util.List;

public class ServiceRequestsFactory {
    public static List<ServiceRequest> createServiceRequestsFromComplaints(List<Complaint> complaints) {
        List<ServiceRequest> serviceRequests = new ArrayList<>();
        for (Complaint complaint : complaints) {
            serviceRequests.add(ServiceRequestFactory.createFromComplaint(complaint));
        }
        return serviceRequests;
    }
}
