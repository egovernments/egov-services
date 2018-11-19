package org.egov.enc.config;


import lombok.Getter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Getter
@ToString
@Configuration
@Component
@PropertySource("classpath:application.properties")
public class AppProperties {

    private int symmetricKeySize;

    private int asymmetricKeySize;

    private String masterPassword;

    private String masterSalt;

    private String masterInitialVector;

    private String symmetricMethod;

    private String asymmetricMethod;

    private Integer keyIdLength;

    @Autowired
    public AppProperties(Environment environment) {
        this.symmetricKeySize = Integer.parseInt(environment.getRequiredProperty("size.key.symmetric"));
        this.asymmetricKeySize = Integer.parseInt(environment.getRequiredProperty("size.key.asymmetric"));
        this.keyIdLength = Integer.parseInt(environment.getRequiredProperty("length.keyid"));
        this.masterPassword = environment.getRequiredProperty("master.password");
        this.masterSalt = environment.getRequiredProperty("master.salt");
        this.masterInitialVector = environment.getRequiredProperty("master.initialvector");
        this.symmetricMethod = environment.getRequiredProperty("method.symmetric");
        this.asymmetricMethod = environment.getRequiredProperty("method.asymmetric");
    }

}
