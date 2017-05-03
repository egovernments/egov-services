package org.egov.persistence.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.egov.domain.model.User;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchResponseContent {
	private Long id;
	private String emailId;

	public User toDomainUser() {
		return new User(id, emailId);
	}
}