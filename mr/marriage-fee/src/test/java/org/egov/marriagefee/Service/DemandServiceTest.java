package org.egov.marriagefee.Service;

import static org.junit.Assert.*;

import org.apache.coyote.RequestInfo;
import org.egov.marriagefee.config.PropertiesManager;
import org.egov.marriagefee.model.ApprovalDetails;
import org.egov.marriagefee.model.AuditDetails;
import org.egov.marriagefee.model.Demand;
import org.egov.marriagefee.model.DemandDetail;
import org.egov.marriagefee.model.Fee;
import org.egov.marriagefee.model.Location;
import org.egov.marriagefee.model.MarriageCertificate;
import org.egov.marriagefee.model.MarriageDocument;
import org.egov.marriagefee.model.MarriageRegn;
import org.egov.marriagefee.model.MarryingPerson;
import org.egov.marriagefee.model.Owner;
import org.egov.marriagefee.model.PriestInfo;
import org.egov.marriagefee.model.RegistrationUnit;
import org.egov.marriagefee.model.Witness;
import org.egov.marriagefee.model.enums.Action;
import org.egov.marriagefee.model.enums.ApplicationStatus;
import org.egov.marriagefee.model.enums.CertificateType;
import org.egov.marriagefee.model.enums.MaritalStatus;
import org.egov.marriagefee.model.enums.RelatedTo;
import org.egov.marriagefee.model.enums.Source;
import org.egov.marriagefee.model.enums.Venue;
import org.egov.marriagefee.repository.DemandServiceRepository;
import org.egov.marriagefee.service.DemandService;
import org.egov.marriagefee.web.contract.DemandResponse;
import org.egov.marriagefee.web.contract.MarriageRegnRequest;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.SendResult;

@RunWith(MockitoJUnitRunner.class)
public class DemandServiceTest {
	@Mock
	private DemandServiceRepository demandServiceRepository;

	@Mock
	private PropertiesManager propertiesManager;
	
	@Mock
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	@InjectMocks
	private DemandService demandService;

	@Test
	public void testCreateMarriageRegnDemandTest() {
		DemandResponse demandResponse= new DemandResponse();
		demandResponse.setDemands(getDemand());
		List<Demand> demands=getDemand();
		
		when(demandServiceRepository.prepareDemand(any(MarriageRegnRequest.class))).thenReturn(demands);
		when(demandServiceRepository.createDemand((List<Demand>) any(Demand.class), 
				any(org.egov.common.contract.request.RequestInfo.class))).thenReturn(demandResponse);
		when(kafkaTemplate.send(any(String.class),any(Object.class))).thenReturn(new SendResult<>(null, null));
		
	}

	private List<Demand> getDemand() {
		List<DemandDetail> demandDetails = new ArrayList<>();
		Demand demand = new Demand();
		DemandDetail demandDetail = new DemandDetail();
		demandDetail.setDemandId("1");
		demandDetail.setTaxAmount(new BigDecimal(1.9));
		demandDetail.setCollectionAmount(new BigDecimal(1));
		demandDetail.setTaxHeadMasterCode("MR_FEE");
		demandDetails.add(demandDetail);
		AuditDetails auditDetails = AuditDetails.builder().createdBy("asish").createdTime(1503056473884l)
				.lastModifiedBy("asish").lastModifiedTime(1503056473884l).build();
		demand.setBusinessService(propertiesManager.getDemandBusinessService());
		demand.setConsumerCode("MR001");
		demand.setConsumerType("consumertype");
		demand.setId("1");
		demand.setAuditDetail(auditDetails);
		demand.setDemandDetails(demandDetails);
		demand.setMinimumAmountPayable(new BigDecimal(2));
		Owner owner = Owner.builder().id(85l).name("sankar").userName("9884187166").mobileNumber("9884187166").build();
		demand.setOwner(owner);
		demand.setTaxPeriodFrom(1427826600l);
		demand.setTaxPeriodTo(1443637799l);
		List<Demand> demands = new ArrayList<>();
		demands.add(demand);
		return demands;

	}

	
}
