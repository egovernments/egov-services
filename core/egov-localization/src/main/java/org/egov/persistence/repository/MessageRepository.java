package org.egov.persistence.repository;


import org.egov.domain.model.AuthenticatedUser;
import org.egov.domain.model.Message;
import org.egov.domain.model.Tenant;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MessageRepository {

    private MessageJpaRepository messageJpaRepository;

    public MessageRepository(MessageJpaRepository messageJpaRepository) {
        this.messageJpaRepository = messageJpaRepository;
    }

    public List<Message> findByTenantIdAndLocale(Tenant tenant, String locale) {
        return messageJpaRepository.find(tenant.getTenantId(), locale).stream()
            .map(org.egov.persistence.entity.Message::toDomain).collect(Collectors.toList());
    }

    public void save(List<Message> messages, AuthenticatedUser authenticatedUser) {
        final List<org.egov.persistence.entity.Message> entityMessages = messages.stream()
            .map(org.egov.persistence.entity.Message::new)
            .collect(Collectors.toList());
        setAuditFieldsForCreate(authenticatedUser, entityMessages);
        try {
            messageJpaRepository.save(entityMessages);
        } catch (DataIntegrityViolationException ex) {
            new DataIntegrityViolationExceptionTransformer(ex).transform();
        }
    }

    public void delete(String tenant, String locale, String module, List<String> codes) {
        final List<org.egov.persistence.entity.Message> messages = messageJpaRepository
            .find(tenant, locale, module, codes);
        if (CollectionUtils.isEmpty(messages)) {
            return;
        }
        messageJpaRepository.delete(messages);
    }

    public void update(String tenant, String locale, String module, List<Message> domainMessages,
                       AuthenticatedUser authenticatedUser) {
        final List<String> codes = getCodes(domainMessages);
        final List<org.egov.persistence.entity.Message> entityMessages =
            fetchMatchEntityMessages(tenant, locale, module, codes);
        updateMessages(domainMessages, entityMessages, authenticatedUser);
    }

    private void setAuditFieldsForCreate(AuthenticatedUser authenticatedUser,
                                         List<org.egov.persistence.entity.Message> entityMessages) {
        entityMessages.forEach(message -> {
            message.setCreatedDate(new Date());
            message.setCreatedBy(authenticatedUser.getUserId());
        });
    }

    private List<org.egov.persistence.entity.Message> fetchMatchEntityMessages(String tenant, String locale,
                                                                               String module, List<String> codes) {
        return messageJpaRepository.find(tenant, locale, module, codes);
    }

    private void updateMessages(List<Message> domainMessages,
                                List<org.egov.persistence.entity.Message> entityMessages, AuthenticatedUser
                                    authenticatedUser) {
        final Map<String, Message> codeToMessageMap = getCodeToMessageMap(domainMessages);
        entityMessages.forEach(entityMessage -> {
            final Message matchingMessage = codeToMessageMap.get(entityMessage.getCode());
            entityMessage.update(matchingMessage);
            setAuditFieldsForUpdate(authenticatedUser, entityMessage);
            messageJpaRepository.save(entityMessage);
        });
    }

    private void setAuditFieldsForUpdate(AuthenticatedUser authenticatedUser, org.egov.persistence.entity.Message
        entityMessage) {
        entityMessage.setLastModifiedBy(authenticatedUser.getUserId());
        entityMessage.setLastModifiedDate(new Date());
    }

    private Map<String, Message> getCodeToMessageMap(List<Message> messages) {
        return messages.stream()
            .collect(Collectors.toMap(Message::getCode, message -> message));
    }

    private List<String> getCodes(List<Message> messages) {
        return messages.stream()
            .map(Message::getCode)
            .collect(Collectors.toList());
    }

}
