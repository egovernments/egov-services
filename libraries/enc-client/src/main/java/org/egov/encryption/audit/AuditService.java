package org.egov.encryption.audit;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.request.User;
import org.egov.encryption.config.EncProperties;
import org.egov.encryption.models.AuditObject;
import org.egov.encryption.producer.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuditService {

    @Autowired
    private Producer producer;
    @Autowired
    private EncProperties encProperties;

    private ObjectMapper objectMapper;

    public AuditService() {
        objectMapper = new ObjectMapper(new JsonFactory());
    }

    public void audit(JsonNode data, User user) {
        AuditObject auditObject = AuditObject.builder().build();
        auditObject.setData(data);
        auditObject.setTimestamp(System.currentTimeMillis());
        auditObject.setUserId(user.getUuid());
        auditObject.setId(UUID.randomUUID().toString());

        producer.push(encProperties.getAuditTopicName(), auditObject.getId(), objectMapper.valueToTree(auditObject).toString());
    }

}
