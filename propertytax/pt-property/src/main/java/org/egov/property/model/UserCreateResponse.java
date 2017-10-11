package org.egov.property.model;

import lombok.*;
import org.egov.models.ResponseInfo;
import org.egov.models.User;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserCreateResponse {

	private List<User> users;

	private ResponseInfo responseInfo;
}
