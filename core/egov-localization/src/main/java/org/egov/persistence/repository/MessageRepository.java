package org.egov.persistence.repository;


import java.util.List;
import java.util.stream.Collectors;


import org.egov.domain.model.Message;
import org.egov.domain.model.Tenant;
import org.springframework.stereotype.Service;

@Service
public class MessageRepository {
    private MessageJpaRepository messageJpaRepository;

    public MessageRepository(MessageJpaRepository messageJpaRepository) {
        this.messageJpaRepository = messageJpaRepository;
    }

    public List<Message> findByTenantIdAndLocale(Tenant tenant, String locale) {
        return messageJpaRepository.findByTenantIdAndLocale(tenant.getTenantId(), locale).stream()
            .map(org.egov.persistence.entity.Message::toDomain).collect(Collectors.toList());
    }

    public void save(List<Message> messages) {
        final List<org.egov.persistence.entity.Message> entityMessages = messages.stream()
            .map(org.egov.persistence.entity.Message::new)
            .collect(Collectors.toList());
        messageJpaRepository.save(entityMessages);
    }
}
