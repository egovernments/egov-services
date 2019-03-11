package org.egov.encryption.config;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class AppProperties {

    @Value("${kafka.config.bootstrap_server_config}")
    private String kafkaBootstrapServerConfig;
    @Value("${kafka.topic.audit}")
    private String auditTopicName;

    @Value("${egov.mdms.host}")
    private String egovMdmsHost;
    @Value("${egov.mdms.search.endpoint}")
    private String egovMdmsSearchEndpoint;

    @Value("${egov.state.level.tenant.id}")
    private String stateLevelTenantId;

    @Value("${egov.enc.host}")
    private String egovEncHost;
    @Value("${egov.enc.encrypt.endpoint}")
    private String egovEncEncryptPath;
    @Value("${egov.enc.decrypt.endpoint}")
    private String egovEncDecryptPath;

}
