package org.egov.user.web.contract.auth;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class CustomUserDetails {
    private Long id;
    private String userName;
    private String name;
    private String mobileNumber;
    private String emailId;
    private String locale;
    private String type;
    private List<Role> roles;
    private boolean active;
    private List<Action> actions;

    public CustomUserDetails(SecureUser secureUser,List<Action> action){
        this.id=secureUser.getUser().getId();
        this.userName=secureUser.getUser().getUserName();
        this.name=secureUser.getUser().getName();
        this.mobileNumber=secureUser.getUser().getMobileNumber();
        this.emailId=secureUser.getUser().getEmailId();
        this.locale=secureUser.getUser().getLocale();
        this.type=secureUser.getUser().getType();
        this.roles=secureUser.getUser().getRoles();
        this.active=secureUser.getUser().isActive();
        this.actions=action;
    }

}

