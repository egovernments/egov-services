package builders.eGovEIS.employee;

import entities.requests.eGovEIS.employee.ServiceHistory;

public class ServiceHistoryBuilder {

    ServiceHistory serviceHistory = new ServiceHistory();

    public ServiceHistoryBuilder() {
        serviceHistory.setServiceInfo("ADS1");
        serviceHistory.setServiceFrom("18/09/2016");
        serviceHistory.setRemarks("None");
        serviceHistory.setOrderNo("A1");
        serviceHistory.setCreatedBy(1);
        serviceHistory.setCreatedDate("18/09/2016");
        serviceHistory.setLastModifiedBy(1);
        serviceHistory.setLastModifiedDate("18/09/2016");
    }

    public ServiceHistory build() {
        return serviceHistory;
    }

}
