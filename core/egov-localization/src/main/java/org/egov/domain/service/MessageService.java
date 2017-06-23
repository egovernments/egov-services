package org.egov.domain.service;

import org.egov.domain.model.Message;
import org.egov.domain.model.MessageIdentity;
import org.egov.domain.model.Tenant;
import org.egov.persistence.repository.MessageCacheRepository;
import org.egov.persistence.repository.MessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MessageService {
    private static final String ENGLISH_INDIA = "en_IN";
    private MessageRepository messageRepository;
    private MessageCacheRepository messageCacheRepository;

    public MessageService(MessageRepository messageRepository,
                          MessageCacheRepository messageCacheRepository) {
        this.messageRepository = messageRepository;
        this.messageCacheRepository = messageCacheRepository;
    }

    public void create(List<Message> messages) {
        messageRepository.save(messages);
    }

    public void update(List<Message> messages) {
        if (CollectionUtils.isEmpty(messages)) {
            throw new IllegalArgumentException("update message list cannot be empty");
        }
        final Message message = messages.get(0);
        messageRepository.update(message.getTenant(), message.getLocale(), message.getModule(), messages);
    }

    public void bustCache() {
        messageCacheRepository.bustCache();
    }

    public List<Message> getMessages(String locale, Tenant tenant) {
        final List<Message> cachedMessages = messageCacheRepository.getComputedMessages(locale, tenant);
        if (cachedMessages != null) {
            return cachedMessages;
        }
        final List<Message> computedMessages = computeMessageList(locale, tenant);
        messageCacheRepository.cacheComputedMessages(locale, tenant, computedMessages);
        return computedMessages;
    }

    public void delete(List<MessageIdentity> messageIdentities) {
        final Map<Tenant, List<MessageIdentity>> tenantToMessageIdentitiesMap = messageIdentities.stream()
            .collect(Collectors.groupingBy(MessageIdentity::getTenant));
        tenantToMessageIdentitiesMap.keySet().forEach(tenant ->
            deleteMessagesForGivenTenant(tenantToMessageIdentitiesMap, tenant));
    }

    private void deleteMessagesForGivenTenant(Map<Tenant, List<MessageIdentity>> tenantToMessageIdentitiesMap,
                                              Tenant tenant) {
        final List<MessageIdentity> messageIdentitiesForGivenTenant = tenantToMessageIdentitiesMap.get(tenant);
        final Map<String, List<MessageIdentity>> localeToMessageIdentitiesMap = messageIdentitiesForGivenTenant
            .stream().collect(Collectors.groupingBy(MessageIdentity::getLocale));
        localeToMessageIdentitiesMap.keySet().forEach(locale ->
            deleteMessagesForGivenLocale(tenant, localeToMessageIdentitiesMap, locale));
    }

    private void deleteMessagesForGivenLocale(Tenant tenant, Map<String,
        List<MessageIdentity>> localeToMessageIdentitiesMap, String locale) {
        final List<MessageIdentity> messageIdentitiesForGivenLocale = localeToMessageIdentitiesMap.get(locale);
        final Map<String, List<MessageIdentity>> moduleToMessageIdentitiesMap = messageIdentitiesForGivenLocale
            .stream().collect(Collectors.groupingBy(MessageIdentity::getModule));
        moduleToMessageIdentitiesMap.keySet()
            .forEach(module -> deleteMessagesForGivenModule(tenant, locale, moduleToMessageIdentitiesMap, module));
    }

    private void deleteMessagesForGivenModule(Tenant tenant, String locale,
                                              Map<String, List<MessageIdentity>> moduleToMessageIdentitiesMap,
                                              String module) {
        final List<MessageIdentity> messageIdentitiesForGivenModule = moduleToMessageIdentitiesMap.get(module);
        final List<String> codes = getCodesForGivenMessage(messageIdentitiesForGivenModule);
        messageRepository.delete(tenant.getTenantId(), locale, module, codes);
    }

    private List<String> getCodesForGivenMessage(List<MessageIdentity> messageIdentitiesForGivenModule) {
        return messageIdentitiesForGivenModule.stream()
            .map(MessageIdentity::getCode)
            .collect(Collectors.toList());
    }

    private List<Message> computeMessageList(String locale, Tenant tenant) {
        final Collection<Message> messagesForGivenLocale = getMessagesForGivenLocale(locale, tenant);
        List<Message> defaultMessages = getDefaultMessagesForMissingCodes(messagesForGivenLocale);
        return Stream.concat(messagesForGivenLocale.stream(), defaultMessages.stream())
            .sorted(Comparator.comparing(Message::getCode))
            .collect(Collectors.toList());
    }

    private List<Message> getDefaultMessagesForMissingCodes(Collection<Message> messagesForGivenLocale) {
        final List<Message> messagesInEnglishForDefaultTenant =
            fetchMessageFromRepository(ENGLISH_INDIA, new Tenant(Tenant.DEFAULT_TENANT));

        Set<String> messageCodesInGivenLanguage = messagesForGivenLocale.stream()
            .map(Message::getCode)
            .collect(Collectors.toSet());

        return getEnglishMessagesForCodesNotPresentInLocalLanguage(messageCodesInGivenLanguage,
            messagesInEnglishForDefaultTenant);
    }

    private Collection<Message> getMessagesForGivenLocale(String locale, Tenant tenant) {
        final Map<String, Message> codeToMessageMap = new HashMap<>();
        final List<Message> messages = tenant.getTenantHierarchy().stream()
            .map(tenantItem -> fetchMessageFromRepository(locale, tenantItem))
            .flatMap(List::stream)
            .collect(Collectors.toList());

        messages.forEach(message -> {
            final Message matchingMessage = codeToMessageMap.get(message.getCode());
            if (matchingMessage == null) {
                codeToMessageMap.put(message.getCode(), message);
            } else {
                if (message.isMoreSpecificComparedTo(matchingMessage)) {
                    codeToMessageMap.put(message.getCode(), message);
                }
            }
        });

        return codeToMessageMap.values();
    }

    private List<Message> getEnglishMessagesForCodesNotPresentInLocalLanguage(Set<String> messageCodesForGivenLocale,
                                                                              List<Message> messagesInEnglish) {
        return messagesInEnglish.stream()
            .filter(message -> !messageCodesForGivenLocale.contains(message.getCode()))
            .collect(Collectors.toList());
    }

    private List<Message> fetchMessageFromRepository(String locale, Tenant tenant) {
        final List<Message> cachedMessages = messageCacheRepository.getMessages(locale, tenant);
        if (cachedMessages != null) {
            return cachedMessages;
        }
        final List<Message> messages = messageRepository.findByTenantIdAndLocale(tenant, locale);
        messageCacheRepository.cacheMessages(locale, tenant, messages);
        return messages;
    }

}
