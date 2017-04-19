package org.egov.persistence.repository;


import java.util.List;
import java.util.stream.Collectors;


import org.springframework.stereotype.Service;

@Service
public class MessageRepository {
    private MessageJpaRepository messageJpaRepository;

    public MessageRepository(MessageJpaRepository messageJpaRepository) {
        this.messageJpaRepository = messageJpaRepository;
    }


    public List<org.egov.domain.model.Message> findByTenantIdAndLocale(String tenantId, String locale) {
        return messageJpaRepository.findByTenantIdAndLocale(tenantId, locale).stream()
            .map(org.egov.persistence.entity.Message::toDomain).collect(Collectors.toList());
    }

    public List<org.egov.domain.model.Message> saveAllEntities(List<org.egov.persistence.entity.Message> entityMessages) {
        messageJpaRepository.save(entityMessages);
        return  entityMessages.stream().map(org.egov.persistence.entity.Message::toDomain).collect(Collectors.toList());


    }


}
