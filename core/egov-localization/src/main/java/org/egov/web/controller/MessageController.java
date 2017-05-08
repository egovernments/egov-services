package org.egov.web.controller;

import org.egov.domain.model.Tenant;
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
        List<org.egov.domain.model.Message> domainMessages =
            messageService.getMessages(locale, new Tenant(tenantId));
        return createResponse(domainMessages);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public MessagesResponse createMessages(@Valid @RequestBody CreateMessagesRequest messageRequest,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new InvalidCreateMessageRequest(bindingResult.getFieldErrors());
        final List<org.egov.domain.model.Message> messages = messageRequest.toDomainMessages();
        messageService.createMessages(messages);
        return createResponse(messages);
    }

    @PostMapping("/cache-bust")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearMessagesCache() {
        messageService.bustCache();
    }

    private MessagesResponse createResponse(List<org.egov.domain.model.Message> domainMessages) {
        return new MessagesResponse(domainMessages.stream().map(Message::new).collect(Collectors.toList()));
    }

}