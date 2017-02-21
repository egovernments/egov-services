package org.egov.user.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import redis.clients.jedis.JedisShardInfo;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    private static final String RESOURCE_ID = "pgr_rest_api";

    @Value("${spring.redis.host}")
    private String host;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(RESOURCE_ID).stateless(false).tokenStore(tokenStore());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and().exceptionHandling()
                .accessDeniedHandler(new OAuth2AccessDeniedHandler());
    }

    @Bean
    public TokenStore tokenStore() {
        try {
            return new RedisTokenStore(connectionFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Bean
    public JedisConnectionFactory connectionFactory() throws Exception {
        return new JedisConnectionFactory(new JedisShardInfo(host));
    }

}