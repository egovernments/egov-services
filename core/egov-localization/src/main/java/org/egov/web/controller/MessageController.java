package org.egov.web.controller;

import org.egov.domain.service.MessageService;

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
    @Autowired
    private MessageService messageService;

    @GetMapping()
    public MessagesResponse getMessagesForLocale(@RequestParam("locale") String locale,
            @RequestParam("tenantId") String tenantId) {
        List<org.egov.domain.model.Message> contractMessages = messageService.getMessagesAsPerLocale(locale, tenantId);
        return new MessagesResponse(contractMessages.stream().map(this::toEntityMessage).collect(Collectors.toList()));
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public MessagesResponse createMessages(@Valid @RequestBody CreateMessagesRequest messageRequest,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new InvalidCreateMessageRequest(bindingResult.getFieldErrors());
        final List<org.egov.persistence.entity.Message> entityMessages = messageRequest.toEntityMessages();
        List<org.egov.domain.model.Message> contractmessages = messageService.saveAllEntityMessages(entityMessages);
        return new MessagesResponse(contractmessages.stream().map(this::toEntityMessage).collect(Collectors.toList()));
    }

    private Message toEntityMessage(org.egov.domain.model.Message contractMessage) {
        return Message.builder().code(contractMessage.getCode()).message(contractMessage.getMessage())
                .tenantId(contractMessage.getTenantId()).build();
    }

}