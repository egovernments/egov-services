package org.egov.user.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.egov.user.entity.User;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserRes {
	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo = null;

	@JsonProperty("User")
	private List<User> user = new ArrayList<User>();

	public UserRes responseInfo(ResponseInfo responseInfo) {
		this.responseInfo = responseInfo;
		return this;
	}

	public ResponseInfo getResponseInfo() {
		return responseInfo;
	}

	public void setResponseInfo(ResponseInfo responseInfo) {
		this.responseInfo = responseInfo;
	}

	public UserRes user(List<User> user) {
		this.user = user;
		return this;
	}

	public UserRes addUserItem(User userItem) {
		this.user.add(userItem);
		return this;
	}

	public List<User> getUser() {
		return user;
	}

	public void setUser(List<User> user) {
		this.user = user;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		UserRes userRes = (UserRes) o;
		return Objects.equals(this.responseInfo, userRes.responseInfo) && Objects.equals(this.user, userRes.user);
	}

	@Override
	public int hashCode() {
		return Objects.hash(responseInfo, user);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class UserRes {\n");

		sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
		sb.append("    user: ").append(toIndentedString(user)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	private String toIndentedString(java.lang.Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
}
