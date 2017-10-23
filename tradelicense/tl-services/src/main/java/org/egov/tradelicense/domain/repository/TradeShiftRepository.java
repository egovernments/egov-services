package org.egov.tradelicense.domain.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.tradelicense.domain.model.TradeShift;
import org.egov.tradelicense.domain.model.TradeShiftSearch;
import org.egov.tradelicense.persistence.entity.TradeShiftEntity;
import org.egov.tradelicense.persistence.repository.TradeShiftJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TradeShiftRepository {

	@Autowired
	TradeShiftJdbcRepository tradeShiftJdbcRepository;

	public Long getNextSequence() {

		String id = tradeShiftJdbcRepository.getSequence(TradeShiftEntity.SEQUENCE_NAME);
		return Long.valueOf(id);

	}

	public List<TradeShift> search(TradeShiftSearch tradeShiftSearch) {

		List<TradeShift> tradeShifts = new ArrayList<TradeShift>();

		List<TradeShiftEntity> tradeShiftEntities = tradeShiftJdbcRepository.search(tradeShiftSearch);

		for (TradeShiftEntity tradeShiftEntity : tradeShiftEntities) {

			TradeShift tradeShift = tradeShiftEntity.toDomain();
			tradeShifts.add(tradeShift);
		}

		return tradeShifts;
	}

	public Boolean idExistenceCheck(TradeShift tradeShift) {

		return tradeShiftJdbcRepository.idExistenceCheck(new TradeShiftEntity().toEntity(tradeShift));
	}
}