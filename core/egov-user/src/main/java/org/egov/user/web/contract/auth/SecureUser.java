package org.egov.user.web.contract.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SecureUser implements UserDetails {
	private static final long serialVersionUID = -8756608845278722035L;
	private final User user;
	private final List<SimpleGrantedAuthority> authorities = new ArrayList<>();

	public SecureUser(User user) {
		if (user == null) {
			throw new UsernameNotFoundException("UserRequest not found");
		} else {
			this.user = user;
			user.getRoles().forEach(role -> this.authorities.add(new SimpleGrantedAuthority(role.getName())));
		}
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return this.user.isActive();
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
		return this.user.getUserName();
	}

	public Long getUserId() {
		return this.user.getId();
	}

	public String getUserType() {
		return this.user.getType();
	}

	public User getUser() {
		return this.user;
	}
}