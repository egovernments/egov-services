package org.egov.tl.masters.domain.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.tl.masters.domain.model.FeeMatrixDetail;
import org.egov.tl.masters.persistence.entity.FeeMatrixDetailEntity;
import org.egov.tl.masters.persistence.repository.FeeMatrixDetailJdbcRepository;
import org.egov.tradelicense.config.PropertiesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeeMatrixDetailDomainRepository {
	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	FeeMatrixDetailJdbcRepository feeMatrixDetailJdbcRepository;

	public FeeMatrixDetailEntity add(FeeMatrixDetailEntity feeDetailEntity) {
		return feeMatrixDetailJdbcRepository.create(feeDetailEntity);
	}

	public FeeMatrixDetailEntity update(FeeMatrixDetailEntity feeDetailEntity) {
		return feeMatrixDetailJdbcRepository.update(feeDetailEntity);
	}

	public Long getFeeDetailMatrixNextSequence() {

		String id = feeMatrixDetailJdbcRepository.getSequence(FeeMatrixDetailEntity.SEQUENCE_NAME);
		return Long.valueOf(id);
	}

	public List<FeeMatrixDetail> getFeeMatrixDetailsByFeeMatrixId(Long id) {
		List<FeeMatrixDetail> feeMatrixDetails = new ArrayList<FeeMatrixDetail>();
		List<FeeMatrixDetailEntity> feeMatrixDetailEntity = feeMatrixDetailJdbcRepository.getFeeMatrixDetails(id);
		for (FeeMatrixDetailEntity entity : feeMatrixDetailEntity) {
			FeeMatrixDetail feeMatrixDetail = entity.toDomain();
			feeMatrixDetails.add(feeMatrixDetail);
		}
		return feeMatrixDetails;
	}

	public void deleteFeeMatrixDetail(FeeMatrixDetail feeMatrixDetail) {
		feeMatrixDetailJdbcRepository.deleteFeeMatrixDetailWithId(feeMatrixDetail.getId());
	}
}
