package org.egov.web.controller;

import org.egov.domain.service.MessageService;

import org.egov.web.contract.CreateMessagesRequest;
import org.egov.web.contract.Message;
import org.egov.web.contract.MessagesResponse;
import org.egov.web.exception.InvalidCreateMessageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping()
    public MessagesResponse getMessagesForLocale(@RequestParam("locale") String locale,
                                                 @RequestParam("tenantId") String tenantId) {
        List<org.egov.domain.model.Message> domainMessages = messageService.getMessagesAsPerLocale(locale, tenantId);
        return createResponse(domainMessages);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public MessagesResponse createMessages(@Valid @RequestBody CreateMessagesRequest messageRequest,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new InvalidCreateMessageRequest(bindingResult.getFieldErrors());
        final List<org.egov.persistence.entity.Message> entityMessages = messageRequest.toEntityMessages();
        List<org.egov.domain.model.Message> domainMessages = messageService.saveAllEntityMessages(entityMessages);
        return createResponse(domainMessages);
    }

    private MessagesResponse createResponse(List<org.egov.domain.model.Message> domainMessages) {
        return new MessagesResponse(domainMessages.stream().map(Message::new).collect(Collectors.toList()));
    }

}