package org.egov.web.controller;

import org.egov.persistence.repository.MessageRepository;
import org.egov.web.contract.CreateMessagesRequest;
import org.egov.web.contract.Message;
import org.egov.web.contract.MessagesResponse;
import org.egov.web.exception.InvalidCreateMessageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private MessageRepository messageRepository;

    @Autowired
    public MessageController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @GetMapping()
    public MessagesResponse getMessagesForLocale(@RequestParam("locale") String locale,
                                                 @RequestParam("tenantId") String tenantId) {
        final List<Message> messages = getMessages(locale, tenantId);
        return new MessagesResponse(messages);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public MessagesResponse createMessages(@Valid @RequestBody CreateMessagesRequest messageRequest,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new InvalidCreateMessageRequest(bindingResult.getFieldErrors());

        final List<org.egov.persistence.entity.Message> entityMessages = messageRequest.toEntityMessages();
        messageRepository.save(entityMessages);
        List<Message> messages = mapToContractMessages(entityMessages);
        return new MessagesResponse(messages);
    }

    private List<Message> mapToContractMessages(List<org.egov.persistence.entity.Message> entityMessages) {
        return entityMessages.stream()
                .map(Message::new)
                .collect(Collectors.toList());
    }

    private List<Message> getMessages(String locale, String tenantId) {
        return mapToContractMessages(messageRepository.findByTenantIdAndLocale(tenantId, locale));
    }

}