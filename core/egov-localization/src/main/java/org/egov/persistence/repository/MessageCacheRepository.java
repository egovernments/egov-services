package org.egov.persistence.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.domain.model.Message;
import org.egov.domain.model.Tenant;
import org.egov.persistence.dto.MessageCacheEntry;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class MessageCacheRepository {

    private static final String MESSAGES_HASH_KEY = "messages";
    private static final String COMPUTED_MESSAGES_HASH_KEY = "computedMessages";
    private StringRedisTemplate stringRedisTemplate;
    private ObjectMapper objectMapper;

    public MessageCacheRepository(StringRedisTemplate stringRedisTemplate,
                                  ObjectMapper objectMapper) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.objectMapper = objectMapper;
    }

    public List<Message> getComputedMessages(String locale, Tenant tenant) {
        return getMessages(locale, tenant, COMPUTED_MESSAGES_HASH_KEY);
    }

    public void cacheComputedMessages(String locale, Tenant tenant, List<Message> messages) {
        putMessages(locale, tenant, COMPUTED_MESSAGES_HASH_KEY, messages);
    }

    public List<Message> getMessages(String locale, Tenant tenant) {
        return getMessages(locale, tenant, MESSAGES_HASH_KEY);
    }

    public void cacheMessages(String locale, Tenant tenant, List<Message> messages) {
        putMessages(locale, tenant, MESSAGES_HASH_KEY, messages);
    }

    public void bustCache() {
        stringRedisTemplate.delete(MESSAGES_HASH_KEY);
        stringRedisTemplate.delete(COMPUTED_MESSAGES_HASH_KEY);
    }

    private List<Message> getMessages(String locale, Tenant tenant, String hashKey) {
        String messageKey = getKey(locale, tenant.getTenantId());
        final String entry = (String) stringRedisTemplate.opsForHash().get(hashKey, messageKey);
        if (entry != null) {
            final MessageCacheEntry messageCacheEntry;
            try {
                messageCacheEntry = objectMapper.readValue(entry, MessageCacheEntry.class);
                return messageCacheEntry.getDomainMessages();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    private void putMessages(String locale, Tenant tenant, String hashKey, List<Message> messages) {
        String messageKey = getKey(locale, tenant.getTenantId());
        final MessageCacheEntry messageCacheEntry = new MessageCacheEntry(messages);
        try {
            final String cacheEntry = objectMapper.writeValueAsString(messageCacheEntry);
            stringRedisTemplate.opsForHash().put(hashKey, messageKey, cacheEntry);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String getKey(String locale, String tenant) {
        return String.format("%s:%s", locale, tenant);
    }

}
