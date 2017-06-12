package builders.pgrCollection.complaintType;

import entities.requests.pgrCollections.complaintType.ComplaintTypeRequest;
import entities.requests.pgrCollections.complaintType.RequestInfo;

public class ComplaintTypeRequestBuilder {

     ComplaintTypeRequest request = new ComplaintTypeRequest();

     public ComplaintTypeRequestBuilder(){}

     public ComplaintTypeRequestBuilder withRequestInfo(RequestInfo requestInfo){
          request.setRequestInfo(requestInfo);
          return this;
     }

     public ComplaintTypeRequest build(){
          return request;
     }
}
