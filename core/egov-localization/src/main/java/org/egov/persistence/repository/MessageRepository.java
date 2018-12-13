package org.egov.persistence.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.egov.domain.model.AuthenticatedUser;
import org.egov.domain.model.Message;
import org.egov.domain.model.Tenant;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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

	public List<Message> findAllMessage(Tenant tenant, String locale, String module, String code) {
		return messageJpaRepository.find(tenant.getTenantId(), locale, module, code).stream()
				.map(org.egov.persistence.entity.Message::toDomain).collect(Collectors.toList());
	}

	public void save(List<Message> messages, AuthenticatedUser authenticatedUser) {
		final List<org.egov.persistence.entity.Message> entityMessages = messages.stream()
				.map(org.egov.persistence.entity.Message::new).collect(Collectors.toList());
		setAuditFieldsForCreate(authenticatedUser, entityMessages);
		try {
			messageJpaRepository.save(entityMessages);
		} catch (DataIntegrityViolationException ex) {
			new DataIntegrityViolationExceptionTransformer(ex).transform();
		}
	}

	public void delete(String tenant, String locale, String module, List<String> codes) {
		final List<org.egov.persistence.entity.Message> messages = messageJpaRepository.find(tenant, locale, module,
				codes);
		if (CollectionUtils.isEmpty(messages)) {
			return;
		}
		messageJpaRepository.delete(messages);
	}

	public void update(String tenant, String locale, String module, List<Message> domainMessages,
			AuthenticatedUser authenticatedUser) {
		final List<String> codes = getCodes(domainMessages);
		final List<org.egov.persistence.entity.Message> entityMessages = fetchMatchEntityMessages(tenant, locale,
				module, codes);
		updateMessages(domainMessages, entityMessages, authenticatedUser);
	}

	public void upsert(String tenant, String locale, String module, List<Message> domainMessages,
			AuthenticatedUser authenticatedUser) {
		final List<String> codes = getCodes(domainMessages);

		final List<org.egov.persistence.entity.Message> entityMessages = 
				fetchMatchEntityMessages(tenant, locale,module, codes);

		List<String> newCodes = getNewCodes(entityMessages);
		
		List<Message> newMsgList =  domainMessages.stream()
        		.filter(msg -> !newCodes.contains(msg.getCode()))
        		.collect(Collectors.toList());
		
		save(newMsgList,authenticatedUser);
        
	    updateMessages(domainMessages, entityMessages, authenticatedUser);
		
		/*Map<String, org.egov.persistence.entity.Message> availableMsg = new HashMap<>();

		for (int i = 0; i < entityMessages.size(); i++) {
			org.egov.persistence.entity.Message message = entityMessages.get(i);
			availableMsg.put(message.getCode(), entityMessages.get(i));
		}

		List<Message> saveMsgList = new ArrayList<>();
		List<Message> updateMsgList = new ArrayList<>();

		List<? extends Message> newMsgList1 = domainMessages.stream()
				.map((Function<? super Message, ? extends Message>) msg -> {
					if (availableMsg.containsKey(msg.getCode())) {
						updateMsgList.add(msg);
					} else {
						saveMsgList.add(msg);
					}
					return msg;
				}).collect(Collectors.toList());


		saveOrUpdate(saveMsgList, updateMsgList, entityMessages, authenticatedUser);*/

	}

	@SuppressWarnings("unused")
	private void saveOrUpdate(List<Message> saveMsgList, List<Message> updateMsgList,
			List<org.egov.persistence.entity.Message> entityMessages, AuthenticatedUser authenticatedUser) {

		if (!CollectionUtils.isEmpty(updateMsgList)) {
			final Map<String, Message> codeToMessageMap = getCodeToMessageMap(updateMsgList);
			entityMessages.stream().forEach(entityMessage -> {
				final Message matchingMessage = codeToMessageMap.get(entityMessage.getCode());
				entityMessage.update(matchingMessage);
				setAuditFieldsForUpdate(authenticatedUser, entityMessage);

			});
			messageJpaRepository.save(entityMessages);
		}
		if (!CollectionUtils.isEmpty(saveMsgList)) {
			final List<org.egov.persistence.entity.Message> message = saveMsgList.stream()
					.map(org.egov.persistence.entity.Message::new).collect(Collectors.toList());
			setAuditFieldsForCreate(authenticatedUser, message);

			try {
				messageJpaRepository.save(message);
			} catch (DataIntegrityViolationException ex) {
				new DataIntegrityViolationExceptionTransformer(ex).transform();
			}
		}
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

	private void updateMessages(List<Message> domainMessages, List<org.egov.persistence.entity.Message> entityMessages,
			AuthenticatedUser authenticatedUser) {
		final Map<String, Message> codeToMessageMap = getCodeToMessageMap(domainMessages);
		entityMessages.stream().forEach(entityMessage -> {
			final Message matchingMessage = codeToMessageMap.get(entityMessage.getCode());
			entityMessage.update(matchingMessage);
			setAuditFieldsForUpdate(authenticatedUser, entityMessage);

		});
		messageJpaRepository.save(entityMessages);
	}

	private void setAuditFieldsForUpdate(AuthenticatedUser authenticatedUser,
			org.egov.persistence.entity.Message entityMessage) {
		entityMessage.setLastModifiedBy(authenticatedUser.getUserId());
		entityMessage.setLastModifiedDate(new Date());
	}

	private Map<String, Message> getCodeToMessageMap(List<Message> messages) {
		return messages.stream().collect(Collectors.toMap(Message::getCode, message -> message));
	}

	private List<String> getCodes(List<Message> messages) {
		return messages.stream().map(Message::getCode).collect(Collectors.toList());
	}
	
	private List<String> getNewCodes(List<org.egov.persistence.entity.Message> messages) {
		return messages.stream().map(org.egov.persistence.entity.Message::getCode).collect(Collectors.toList());
	}

}
