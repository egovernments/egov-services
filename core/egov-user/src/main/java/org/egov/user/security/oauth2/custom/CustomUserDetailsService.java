package org.egov.user.security.oauth2.custom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.egov.user.domain.model.SecureUser;
import org.egov.user.domain.model.User;
import org.egov.user.domain.service.UserService;
import org.egov.user.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
    private UserService userService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {	
		System.out.println("USER NAME: "+username);
		System.out.println("Tenant: "+CustomAuthenticationProvider.getTenant());
	    User user;
	    if (username.contains("@") && username.contains(".")) {
	        user = userService.getUserByEmailId(username, CustomAuthenticationProvider.getTenant());
	    } else {
	        user = userService.getUserByUsername(username, "default");
	    }
	    if (null == user) {
	        throw new UsernameNotFoundException(username);
	    }
        final SecureUser secureUser = new SecureUser(getUser(user));
		System.out.println("USER: "+secureUser.toString());
		
		List<GrantedAuthority> grantedAuths = new ArrayList<>();
        grantedAuths.add(new SimpleGrantedAuthority("ROLE_" + user.getType()));

		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =  
				new UsernamePasswordAuthenticationToken(secureUser, user.getPassword(), grantedAuths);
		
	    return new UserDetailsImpl(usernamePasswordAuthenticationToken);
	}
	
	private org.egov.user.web.contract.auth.User getUser(User user) {
		System.out.println("building secure user");
        return org.egov.user.web.contract.auth.User.builder()
                .id(user.getId())
                .userName(user.getUsername())
                .name(user.getName())
                .mobileNumber(user.getMobileNumber())
                .emailId(user.getEmailId())
                .locale(user.getLocale())
                .active(user.getActive())
                .type(user.getType().name())
                .roles(toAuthRole(user.getRoles()))
                .tenantId(user.getTenantId())
                .build();
    }
	
    private List<org.egov.user.web.contract.auth.Role> toAuthRole(List<org.egov.user.domain.model.Role> domainRoles) {
		System.out.println("building authroles");

        if (domainRoles == null) 
        	return new ArrayList<>();
        
        return domainRoles.stream().map(org.egov.user.web.contract.auth.Role::new)
                .collect(Collectors.toList());
    }

	public static class UserDetailsImpl implements UserDetails {

		private static final long serialVersionUID = 1L;
		private User user;
	    private SecureUser secureUser;
	    private UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken;
	 
	    public UserDetailsImpl(User user) {
	        this.user = user;
	    }
	    
	    public UserDetailsImpl(SecureUser secureUser){
	    	this.secureUser = secureUser;
	    }

	    public UserDetailsImpl(UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken){
	    	this.usernamePasswordAuthenticationToken = usernamePasswordAuthenticationToken;
	    }
	    
		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			 List<GrantedAuthority> grantedAuths = new ArrayList<>();
	            grantedAuths.add(new SimpleGrantedAuthority("ROLE_" + user.getType()));
			return grantedAuths;
		}

		@Override
		public String getPassword() {
			return user.getPassword();
		}

		@Override
		public String getUsername() {
			return user.getUsername();
		}

		@Override
		public boolean isAccountNonExpired() {
			return true;
		}

		@Override
		public boolean isAccountNonLocked() {
			return true;
		}

		@Override
		public boolean isCredentialsNonExpired() {
			return true;
		}

		@Override
		public boolean isEnabled() {
			return true;
		}
	}
	
	

}
