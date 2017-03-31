package org.egov.pgr.write.service;

import org.egov.pgr.common.entity.ReceivingCenter;
import org.egov.pgr.common.repository.ReceivingCenterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReceivingCenterWriteService {

	private final ReceivingCenterRepository receivingCenterWriteRepository;

	@Autowired
	public ReceivingCenterWriteService(final ReceivingCenterRepository receivingCenterWriteRepository) {
		this.receivingCenterWriteRepository = receivingCenterWriteRepository;
	}

	public ReceivingCenter getReceivingCenterById(Long id) {
		return receivingCenterWriteRepository.findById(id);
	}
}
