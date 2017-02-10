package org.egov.pgr.employee.enrichment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertiesManager {

    @Value("${egov.services.workflow.hostname}")
    private String workflowServiceHostname;

    @Value("${egov.services.workflow.create_workflow}")
    private String createWorkflowContext;

    @Value("${kafka.config.bootstrap_server_config}")
    private String serverConfig;

    @Value("${kafka.producer.config.buffer_memory_config}")
    private Integer bufferMemoryConfig;

    @Value("${kafka.producer.config.linger_ms_config}")
    private Integer lingerMsConfig;

    @Value("${kafka.producer.config.batch_size_config}")
    private Integer batchSizeConfig;

    @Value("${kafka.producer.config.retries_config}")
    private Integer retiresConfig;

    public String getCreateWorkflowUrl() {
        return this.workflowServiceHostname + this.createWorkflowContext;
    }

    public String getServerConfig() {
        return serverConfig;
    }

    public Integer getBatchSizeConfig() {
        return batchSizeConfig;
    }

    public Integer getLingerMsConfig() {
        return lingerMsConfig;
    }

    public Integer getBufferMemoryConfig() {
        return bufferMemoryConfig;
    }

    public Integer getRetriesConfig() {
        return retiresConfig;
    }
}
