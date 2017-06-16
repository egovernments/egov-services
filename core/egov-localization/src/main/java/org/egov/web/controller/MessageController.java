package org.egov.web.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.egov.domain.model.Tenant;
import org.egov.domain.service.MessageService;
import org.egov.web.contract.CreateMessagesRequest;
import org.egov.web.contract.DeleteMessagesRequest;
import org.egov.web.contract.Message;
import org.egov.web.contract.MessagesResponse;
import org.egov.web.contract.NewMessagesRequest;
import org.egov.web.exception.InvalidCreateMessageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private MessageService messageService;

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

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
    
    @PostMapping(value = "/_create")
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody @Valid final NewMessagesRequest messagesRequest,
			final BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new InvalidCreateMessageRequest(bindingResult.getFieldErrors());
		}
		logger.info("Create Message Request:" + messagesRequest);
		messageService.createMessage(messagesRequest);
		final List<Message> messages = messagesRequest.getMessages();
		return getSuccessResponse(messages);
	}
    
    @PostMapping(value = "/_update")
	@ResponseBody
	public ResponseEntity<?> update(@RequestBody @Valid final NewMessagesRequest messagesRequest,
			final BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new InvalidCreateMessageRequest(bindingResult.getFieldErrors());
		}
		logger.info("Update Message Request:" + messagesRequest);
		messageService.createMessage(messagesRequest);
		final List<Message> messages = messagesRequest.getMessages();
		return getSuccessResponse(messages);
	}
    
    @PostMapping(value = "/_delete")
	@ResponseBody
	public ResponseEntity<?> delete(@RequestBody @Valid final DeleteMessagesRequest deleteMessagesRequest,
			final BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new InvalidCreateMessageRequest(bindingResult.getFieldErrors());
		}
		logger.info("Delete Message Request:" + deleteMessagesRequest);
		messageService.deleteMessage(deleteMessagesRequest);
		final List<Message> messages = deleteMessagesRequest.getMessages();
		return getSuccessResponse(messages);
	}
    
    private ResponseEntity<?> getSuccessResponse(final List<Message> messages) {
		final MessagesResponse messageResponse = new MessagesResponse(messages); 
		return new ResponseEntity<>(messageResponse, HttpStatus.CREATED);
	}

}