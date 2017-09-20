package org.egov.tl.masters.persistence.queue;

import java.util.Map;

import org.egov.tl.commons.web.contract.FeeMatrixContract;
import org.egov.tl.commons.web.contract.FeeMatrixDetailContract;
import org.egov.tl.commons.web.requests.FeeMatrixRequest;
import org.egov.tl.masters.domain.model.FeeMatrix;
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
public class FeeMatrixListner {
	@Autowired
	ApplicationContext applicationContext;

	@Autowired
	ObjectMapper objectMapper;

	@Value("${egov.tradelicense.feematrix.validated}")
	private String validatedTopic;

	@Value("${egov.tradelicense.feematrix.create.validated.key}")
	private String createValidatedKey;

	@Value("${egov.tradelicense.feematrix.update.validated.key}")
	private String updateValidatedKey;
	
	@Value("${egov.tradelicense.feematrixdetail.delete.validated.key}")
	private String deleteValidatedKey;

	@Autowired
	private FeeMatrixService feeMatrixService;

	@KafkaListener(id = "${egov.tradelicense.feematrix.validated.id}", topics = "${egov.tradelicense.feematrix.validated}", group = "${egov.tradelicense.feematrix.validated.group}")
	public void process(Map<String, Object> mastersMap) {
		// implement the details here

		if (mastersMap.get(createValidatedKey) != null) {
			FeeMatrixRequest request = objectMapper.convertValue(mastersMap.get(createValidatedKey),
					FeeMatrixRequest.class);
			ModelMapper mapper = new ModelMapper();
			for (FeeMatrixContract feeMatrix : request.getFeeMatrices()) {
				FeeMatrix domain = mapper.map(feeMatrix, FeeMatrix.class);
				feeMatrixService.save(domain);
			}
			mastersMap.clear();
		}
		
		if(mastersMap.get(updateValidatedKey) != null){
			FeeMatrixRequest request = objectMapper.convertValue(mastersMap.get(updateValidatedKey),
					FeeMatrixRequest.class);
			ModelMapper mapper = new ModelMapper();
			for (FeeMatrixContract feeMatrix : request.getFeeMatrices()) {
				FeeMatrix domain = mapper.map(feeMatrix, FeeMatrix.class);
				feeMatrixService.update(domain);
			}
			mastersMap.clear();
		}
		
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
