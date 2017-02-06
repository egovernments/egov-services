package org.egov.pgr.resolver;

import org.egov.pgr.model.User;
import org.egov.pgr.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Service
public class AuthenticatedUserResolver implements org.springframework.web.method.support.HandlerMethodArgumentResolver {

    private static final String AUTH_TOKEN_HEADER = "auth_token";
    private UserRepository userRepository;

    @Autowired
    public AuthenticatedUserResolver(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().isAssignableFrom(User.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory) throws Exception {
        final String authToken = nativeWebRequest.getHeader(AUTH_TOKEN_HEADER);
        if (isNotEmpty(authToken)) {
            return userRepository.findUser(authToken);
        } else {
            return null;
        }
    }

}
