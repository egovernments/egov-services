package builders.pgrCollection.createComplaint;

import entities.requests.pgrCollections.createComplaint.AttribValues;
import entities.requests.pgrCollections.createComplaint.ServiceRequest;
import entities.requests.pgrCollections.createComplaint.Values;

public final class ServiceRequestBuilder {
    ServiceRequest serviceRequest = new ServiceRequest();

    AttribValues[] attribValues = new AttribValuesBuilder().build();

    AttribValues[] attribValues1 = new AttribValuesBuilder("PROCESSING").build();

    Values values1 = new ValuesBuilder("update").build();

    public ServiceRequestBuilder() {
       serviceRequest.setServiceCode("PHDMG");
       serviceRequest.setDescription("Testing the create");
       serviceRequest.setAddressId("127");
       serviceRequest.setLat("0");
       serviceRequest.setLng("0");
       serviceRequest.setAddress("");
       serviceRequest.setServiceRequestId("");
       serviceRequest.setFirstName("Sanjeev");
       serviceRequest.setPhone("9999999999");
       serviceRequest.setEmail("sanjeev@testing.com");
       serviceRequest.setStatus(true);
       serviceRequest.setServiceName("Public Health/Dengue/Malaria/Gastro-enteritis");
       serviceRequest.setRequestedDatetime("");
       serviceRequest.setMediaUrl("");
       serviceRequest.setTenantId("default");
       serviceRequest.setServiceRequestId("");
       serviceRequest.setAttribValues(attribValues);
       serviceRequest.setAttribValuesPopulated(true);
    }

    public ServiceRequestBuilder(String update) {
        serviceRequest.setTenantId("default");
        serviceRequest.setStatus(false);
        serviceRequest.setServiceName("Bio Medical waste/Health hazard waste removal");
        serviceRequest.setServiceCode("BMWHHWR");
        serviceRequest.setDescription("Testing for update");
        serviceRequest.setRequestedDatetime("22-03-2017 10:47:21");
        serviceRequest.setAddress("");
        serviceRequest.setAddressId(null);
        serviceRequest.setLat("0.0");
        serviceRequest.setLng("0.0");
        serviceRequest.setMediaUrl(null);
        serviceRequest.setFirstName("Mohammed");
        serviceRequest.setPhone("9999999999");
        serviceRequest.setEmail("Sanjeev@testing.com");
        serviceRequest.setUpdatedDateTime("22-03-2017 18:14:13");
        serviceRequest.setExpectedDateTime("22-03-2017 18:47:21");
        serviceRequest.setAttribValues(attribValues1);
        serviceRequest.setAttribValuesPopulated(true);
    }

    public ServiceRequestBuilder withServiceRequestId(String id){
        serviceRequest.setServiceRequestId(id);
        return this;
    }

    public ServiceRequest build() {
        return serviceRequest;
    }
}
