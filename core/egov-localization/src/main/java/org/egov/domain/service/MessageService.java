package org.egov.domain.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.egov.domain.model.Message;
import org.egov.domain.model.Tenant;
import org.egov.persistence.repository.MessageCacheRepository;
import org.egov.persistence.repository.MessageRepository;
import org.egov.web.contract.DeleteMessagesRequest;
import org.egov.web.contract.NewMessagesRequest;
import org.springframework.stereotype.Service;

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

    public void createMessages(List<Message> messages) {
        messageRepository.save(messages);
    }

    public void bustCache() {
        messageCacheRepository.bustCache();
    }

    public List<Message> getMessages(String locale, Tenant tenant) {
        final List<Message> cachedMessages = messageCacheRepository.getComputedMessages(locale, tenant);
        if(cachedMessages != null) {
            return cachedMessages;
        }
        final List<Message> computedMessages = computeMessageList(locale, tenant);
        messageCacheRepository.cacheComputedMessages(locale, tenant, computedMessages);
        return computedMessages;
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
            fetchMessageForRepository(ENGLISH_INDIA, new Tenant(Tenant.DEFAULT_TENANT));

        Set<String> messageCodesInGivenLanguage = messagesForGivenLocale.stream()
            .map(Message::getCode)
            .collect(Collectors.toSet());

        return getEnglishMessagesForCodesNotPresentInLocalLanguage(messageCodesInGivenLanguage,
			messagesInEnglishForDefaultTenant);
    }

    private Collection<Message> getMessagesForGivenLocale(String locale, Tenant tenant) {
        final Map<String, Message> codeToMessageMap = new HashMap<>();
        final List<Message> messages = tenant.getTenantHierarchy().stream()
            .map(tenantItem -> fetchMessageForRepository(locale, tenantItem))
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

    private List<Message> fetchMessageForRepository(String locale, Tenant tenant) {
        final List<Message> cachedMessages = messageCacheRepository.getMessages(locale, tenant);
        if (cachedMessages != null) {
            return cachedMessages;
        }
        final List<Message> messages = messageRepository.findByTenantIdAndLocale(tenant, locale);
        messageCacheRepository.cacheMessages(locale, tenant, messages);
        return messages;
    }
    
    public boolean createMessage(NewMessagesRequest newMessagesRequest){
    	List<Message> messageList = getOnlyMessages(newMessagesRequest);
    	messageRepository.deleteMessages(newMessagesRequest.getLocale(), newMessagesRequest.getTenantId(), messageList);
   		boolean createStatus = messageRepository.createMessage(newMessagesRequest.getLocale(), newMessagesRequest.getTenantId(), messageList);
   		if(createStatus){
   			return true;
   		}
   		return false; 
    }
    
    public boolean deleteMessage(DeleteMessagesRequest deleteMessagesRequest){
    	List<Message> messageList = getOnlyMessages(deleteMessagesRequest);
    	boolean deleteStatus = messageRepository.deleteMessages(deleteMessagesRequest.getLocale(), deleteMessagesRequest.getTenantId(), messageList);
    	if(deleteStatus){
    		return true;
    	}
    	return false;     	
    }
    
    private List<Message> getOnlyMessages(NewMessagesRequest newMessagesRequest){
    	List<org.egov.web.contract.Message> requestMessages = newMessagesRequest.getMessages();
    	List<Message> persistMessages = new ArrayList<>();
    	if(requestMessages.size() > 0){
    		Iterator<org.egov.web.contract.Message> itr = requestMessages.iterator();
    		while(itr.hasNext()){
    			org.egov.web.contract.Message msg = itr.next();
    			Message myMessage = new Message(msg.getCode(), msg.getMessage(), new Tenant(newMessagesRequest.getTenantId()), newMessagesRequest.getLocale(), msg.getModule());
    			persistMessages.add(myMessage);
    		}
    	}
    	return persistMessages;
    }
    
    private List<Message> getOnlyMessages(DeleteMessagesRequest deleteMessagesRequest){
    	List<org.egov.web.contract.Message> requestMessages = deleteMessagesRequest.getMessages();
    	List<Message> persistMessages = new ArrayList<>();
    	if(requestMessages.size() > 0){
    		Iterator<org.egov.web.contract.Message> itr = requestMessages.iterator();
    		while(itr.hasNext()){
    			org.egov.web.contract.Message msg = itr.next();
    			Message myMessage = new Message(msg.getCode(), msg.getMessage(), new Tenant(deleteMessagesRequest.getTenantId()), deleteMessagesRequest.getLocale(), msg.getModule());
    			persistMessages.add(myMessage);
    		}
    	}
    	return persistMessages;
    }
}
