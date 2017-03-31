package org.egov.pgr.write.service;

import org.egov.pgr.write.entity.ReceivingMode;
import org.egov.pgr.write.repository.ReceivingModeWriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReceivingModeWriteService {

	private final ReceivingModeWriteRepository receivingModeWriteRepository;

	@Autowired
	public ReceivingModeWriteService(final ReceivingModeWriteRepository receivingModeWriteRepository) {
		this.receivingModeWriteRepository = receivingModeWriteRepository;
	}

	public ReceivingMode getReceivingModeByCode(String code) {
		return receivingModeWriteRepository.findByCode(code);
	}
}
