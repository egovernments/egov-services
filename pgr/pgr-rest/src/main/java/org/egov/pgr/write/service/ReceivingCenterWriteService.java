package org.egov.pgr.write.service;

import org.egov.pgr.write.entity.ReceivingCenter;
import org.egov.pgr.write.repository.ReceivingCenterWriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReceivingCenterWriteService {

	private final ReceivingCenterWriteRepository receivingCenterWriteRepository;

	@Autowired
	public ReceivingCenterWriteService(final ReceivingCenterWriteRepository receivingCenterWriteRepository) {
		this.receivingCenterWriteRepository = receivingCenterWriteRepository;
	}

	public ReceivingCenter getReceivingCenterById(Long id) {
		return receivingCenterWriteRepository.findById(id);
	}
}
