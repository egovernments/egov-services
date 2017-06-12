package builders.pgrCollection.fetchComplaints;

import entities.requests.pgrCollections.fetchComplaints.FetchComplaintRequest;
import entities.requests.pgrCollections.fetchComplaints.RequestInfo;

public class FetchComplaintRequestBuilder {

    FetchComplaintRequest request = new FetchComplaintRequest();

    public FetchComplaintRequestBuilder withRequestInfo(RequestInfo requestInfo){
        request.setRequestInfo(requestInfo);
        return this;
    }

    public FetchComplaintRequest build(){
        return request;
    }
}
