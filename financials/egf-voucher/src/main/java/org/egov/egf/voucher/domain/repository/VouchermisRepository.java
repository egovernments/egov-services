package org.egov.egf.voucher.domain.repository;

import org.egov.egf.voucher.domain.model.Vouchermis;
import org.egov.egf.voucher.persistence.entity.VouchermisEntity;
import org.egov.egf.voucher.persistence.repository.VouchermisJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VouchermisRepository {

	@Autowired
	private VouchermisJdbcRepository vouchermisJdbcRepository;
	

	public Vouchermis findById(Vouchermis vouchermis) {
		return vouchermisJdbcRepository.findById(new VouchermisEntity().toEntity(vouchermis)).toDomain();

	}

	public Vouchermis save(Vouchermis vouchermis) {
		return vouchermisJdbcRepository.create(new VouchermisEntity().toEntity(vouchermis)).toDomain();
	}

	public Vouchermis update(Vouchermis entity) {
		return vouchermisJdbcRepository.update(new VouchermisEntity().toEntity(entity)).toDomain();
	}

	

	
}