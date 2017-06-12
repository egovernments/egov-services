package builders.eGovEIS.hrMaster.position.search;

import entities.responses.eGovEIS.hrMaster.position.search.UserInfo;

public class UserInfoBuilder {

    UserInfo userInfo = new UserInfo();

    public UserInfoBuilder() {
        userInfo.setId(1);
    }

    public UserInfoBuilder withId(int id) {
        userInfo.setId(id);
        return this;
    }

    public UserInfo build() {
        return userInfo;
    }
}
