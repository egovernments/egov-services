package org.egov.tradelicense.domain.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.tradelicense.domain.model.TradePartner;
import org.egov.tradelicense.domain.model.TradePartnerSearch;
import org.egov.tradelicense.persistence.entity.TradePartnerEntity;
import org.egov.tradelicense.persistence.repository.TradePartnerJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TradePartnerRepository {

	@Autowired
	TradePartnerJdbcRepository tradePartnerJdbcRepository;

	public Long getNextSequence() {

		String id = tradePartnerJdbcRepository.getSequence(TradePartnerEntity.SEQUENCE_NAME);
		return Long.valueOf(id);

	}

	public List<TradePartner> search(TradePartnerSearch tradePartnerSearch) {

		List<TradePartner> tradePartners = new ArrayList<TradePartner>();

		List<TradePartnerEntity> tradePartnerEntities = tradePartnerJdbcRepository.search(tradePartnerSearch);

		for (TradePartnerEntity tradePartnerEntity : tradePartnerEntities) {

			TradePartner tradePartner = tradePartnerEntity.toDomain();
			tradePartners.add(tradePartner);
		}

		return tradePartners;
	}

	public Boolean idExistenceCheck(TradePartner tradePartner) {
		
		return tradePartnerJdbcRepository.idExistenceCheck(new TradePartnerEntity().toEntity(tradePartner));
	}
}