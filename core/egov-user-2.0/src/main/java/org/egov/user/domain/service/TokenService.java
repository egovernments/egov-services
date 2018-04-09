package org.egov.user.domain.service;

import org.apache.commons.lang3.StringUtils;
import org.egov.user.domain.exception.InvalidAccessTokenException;
import org.egov.user.domain.model.Action;
import org.egov.user.domain.model.SecureUser;
import org.egov.user.domain.model.UserDetail;
import org.egov.user.persistence.repository.ActionRestRepository;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TokenService {

	private TokenStore tokenStore;

	private ActionRestRepository actionRestRepository;

	private TokenService(TokenStore tokenStore, ActionRestRepository actionRestRepository) {
		this.tokenStore = tokenStore;
		this.actionRestRepository = actionRestRepository;
	}

	/**
	 * Get UserDetails By AccessToken
	 * 
	 * @param accessToken
	 * @return
	 */
	public UserDetail getUser(String accessToken) {
		if (StringUtils.isEmpty(accessToken)) {
			throw new InvalidAccessTokenException();
		}

		OAuth2Authentication authentication = tokenStore.readAuthentication(accessToken);

		if (authentication == null) {
			throw new InvalidAccessTokenException();
		}

		SecureUser secureUser = ((SecureUser) authentication.getPrincipal());
		List<Action> actions = actionRestRepository.getActionByRoleCodes(secureUser.getRoleCodes(),
				secureUser.getTenantId());
		return new UserDetail(secureUser, actions);
	}
}
