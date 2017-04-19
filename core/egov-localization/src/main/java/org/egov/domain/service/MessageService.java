package org.egov.domain.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.egov.persistence.repository.MessageRepository;
import org.egov.domain.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    private static final String EN_IN = "en_IN";
    @Autowired
    private MessageRepository messageRepository;

    public List<Message> getMessagesAsPerLocale(String locale, String tenantId) {

        final List<Message> messagesInEnglish = getMessages(EN_IN, tenantId);

        if (locale.equals(EN_IN)) return messagesInEnglish;

        List<Message> messagesInLocalLanguage = getMessages(locale, tenantId);

        List<String> messageCodesInLocalLanguage = messagesInLocalLanguage.stream()
            .map(Message::getCode)
            .collect(Collectors.toList());

        List<String> messageCodesInEnglish = messagesInEnglish.stream()
            .map(Message::getCode)
            .collect(Collectors.toList());

        List<String> codesMissingFromLocalLanguage = findMissingCodesInLocalLanguage(messageCodesInLocalLanguage, messageCodesInEnglish);

        List<Message> missingMessages = getMissingMessagesInLocalLanguage(codesMissingFromLocalLanguage, messagesInEnglish);

        return Stream.concat(messagesInLocalLanguage.stream(), missingMessages.stream()).collect(Collectors.toList());
    }

    private List<String> findMissingCodesInLocalLanguage(List<String> codeInLocalLanguage, List<String> codeInEnglish) {
        return codeInEnglish.stream()
            .filter(code -> !codeInLocalLanguage.contains(code))
            .collect(Collectors.toList());
    }

    private List<Message> getMissingMessagesInLocalLanguage(List<String> codes, List<Message> messagesInEnglish) {
        return messagesInEnglish.stream()
            .filter(message -> codes.contains(message.getCode()))
            .collect(Collectors.toList());
    }


    public List<Message> saveAllEntityMessages(List<org.egov.persistence.entity.Message> entityMessages) {
        return messageRepository.saveAllEntities(entityMessages);
    }

    private List<Message> getMessages(String locale, String tenantId) {
        return messageRepository.findByTenantIdAndLocale(tenantId, locale);
    }
}
