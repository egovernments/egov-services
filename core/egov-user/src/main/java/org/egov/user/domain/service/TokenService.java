package org.egov.user.domain.service;


import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.egov.user.domain.exception.InvalidAccessTokenException;
import org.egov.user.domain.model.Action;
import org.egov.user.domain.model.SecureUser;
import org.egov.user.domain.model.UserDetail;
import org.egov.user.persistence.repository.ActionRestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TokenService.class);
	
	private TokenStore tokenStore;

	private ActionRestRepository actionRestRepository;

	private TokenService(TokenStore tokenStore, ActionRestRepository actionRestRepository) {
		this.tokenStore = tokenStore;
		this.actionRestRepository = actionRestRepository;
	}

	public UserDetail getUser(String accessToken) {
		if (StringUtils.isEmpty(accessToken)) {
			throw new InvalidAccessTokenException();
		}
	
		LOGGER.info("accessToken: " +accessToken); 
		OAuth2Authentication authentication = tokenStore.readAuthentication(accessToken);
		OAuth2RefreshToken oath2RefreshToken= tokenStore.readRefreshToken(accessToken);
		LOGGER.info("oath2RefreshToken: " +oath2RefreshToken); 
		OAuth2Authentication refreshAuthentication =tokenStore.readAuthenticationForRefreshToken(oath2RefreshToken);

		LOGGER.info("refreshAuthentication: " +refreshAuthentication); 
		
		if (refreshAuthentication == null) {
			throw new InvalidAccessTokenException();
		}

		SecureUser secureUser = ((SecureUser) authentication.getPrincipal());
		List<Action> actions = actionRestRepository
				.getActionByRoleCodes(secureUser.getRoleCodes(), secureUser.getTenantId());
		return new UserDetail(secureUser, actions);
	}
}
