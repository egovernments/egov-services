package builders.pgrCollection.receivingCenters;

import entities.requests.pgrCollections.receivingCenters.ReceivingCentersRequest;
import entities.requests.pgrCollections.receivingCenters.RequestInfo;

public class ReceivingCentersRequestBuilder {

    ReceivingCentersRequest request = new ReceivingCentersRequest();

    public ReceivingCentersRequestBuilder(){}

    public ReceivingCentersRequestBuilder withRequestInfo(RequestInfo requestInfo){
        request.setRequestInfo(requestInfo);
        return this;
    }

    public ReceivingCentersRequest build(){
        return request;
    }
}
