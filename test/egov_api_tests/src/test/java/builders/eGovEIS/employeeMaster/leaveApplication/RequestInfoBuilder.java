package builders.eGovEIS.employeeMaster.leaveApplication;

import entities.requests.eGovEIS.employeeMaster.leaveApplication.RequestInfo;
import entities.requests.eGovEIS.employeeMaster.leaveApplication.UserInfo;

public class RequestInfoBuilder {

    RequestInfo requestInfo = new RequestInfo();

    public RequestInfoBuilder(){
        requestInfo.setApiId("eis");
        requestInfo.setVer("1.0");
        requestInfo.setTs("01-04-2017 01:01:01");
        requestInfo.setAction("asd");
        requestInfo.setDid("4354648646");
        requestInfo.setKey("xyz");
        requestInfo.setMsgId("654654");
        requestInfo.setAuthToken(null);
    }

    public RequestInfoBuilder withUserInfo(UserInfo userInfo){
        requestInfo.setUserInfo(userInfo);
        return this;
    }

    public RequestInfo build(){
        return requestInfo;
    }
}
