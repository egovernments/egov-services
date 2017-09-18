package org.egov.tl.masters.persistence.queue;

import java.util.Map;

import org.egov.tl.commons.web.contract.FeeMatrixDetailContract;
import org.egov.tl.masters.domain.model.FeeMatrixDetail;
import org.egov.tl.masters.domain.service.FeeMatrixService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FeeMatrixDetailListener {
	@Autowired
	ApplicationContext applicationContext;

	@Autowired
	ObjectMapper objectMapper;

	@Value("${egov.tradelicense.feematrixdetail.validated}")
	private String validatedTopic;

	@Value("${egov.tradelicense.feematrixdetail.delete.validated.key}")
	private String deleteValidatedKey;

	@Autowired
	private FeeMatrixService feeMatrixService;

	@KafkaListener(id = "${egov.tradelicense.feematrixdetail.validated.id}", topics = "${egov.tradelicense.feematrixdetail.validated}", group = "${egov.tradelicense.feematrixdetail.validated.group}")
	public void process(Map<String, Object> mastersMap) {
	
		if (mastersMap.get(deleteValidatedKey) != null) {
			FeeMatrixDetailContract request = objectMapper.convertValue(mastersMap.get(deleteValidatedKey),
					FeeMatrixDetailContract.class);
			ModelMapper mapper = new ModelMapper();
			FeeMatrixDetail domain = mapper.map(request, FeeMatrixDetail.class);
			feeMatrixService.delete(domain);
		}
		mastersMap.clear();
	}
}
