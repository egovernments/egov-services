package builders.pgrCollection.searchComplaint;

import entities.requests.pgrCollections.searchComplaint.RequestInfo;
import entities.requests.pgrCollections.searchComplaint.SearchComplaintRequest;

public class SearchComplaintRequestBuilder {

    SearchComplaintRequest request = new SearchComplaintRequest();

    public SearchComplaintRequestBuilder(){}

    public SearchComplaintRequestBuilder withRequestInfo(RequestInfo requestInfo){
        request.setRequestInfo(requestInfo);
        return this;
    }

    public SearchComplaintRequest build(){
        return request;
    }
}
