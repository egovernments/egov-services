package org.egov.user;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import redis.clients.jedis.JedisShardInfo;

@SpringBootApplication
public class EgovUserApplication {

    @Value("${spring.redis.host}")
    private String host;

    @Bean
    public WebMvcConfigurerAdapter webMvcConfigurerAdapter() {
        return new WebMvcConfigurerAdapter() {

            @Override
            public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
                configurer.defaultContentType(MediaType.APPLICATION_JSON_UTF8);
            }
        };
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
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

    public static void main(String[] args) {
        SpringApplication.run(EgovUserApplication.class, args);
    }

}