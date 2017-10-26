
package org.egov.property.consumer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.egov.models.CalculationRequest;
import org.egov.models.CalculationResponse;
import org.egov.models.Floor;
import org.egov.models.Property;
import org.egov.models.PropertyRequest;
import org.egov.models.TaxCalculation;
import org.egov.models.TaxPeriod;
import org.egov.models.TaxPeriodResponse;
import org.egov.models.Unit;
import org.egov.models.UnitTax;
import org.egov.property.config.PropertiesManager;
import org.egov.property.repository.CalculatorRepository;
import org.egov.property.utility.UpicNoGeneration;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * Consumer class will use for listing property object from kafka server to
 * update the property data &send it back to kafka topic
 * 
 * @author: Prasad Khandagale
 */
@Service
@Slf4j
public class TaxCalculatorConsumer {

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	UpicNoGeneration upicGeneration;

	@Autowired
	CalculatorRepository calculatorRepository;

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	/**
	 * receive method
	 * 
	 * @param PropertyRequest
	 *            This method is listened whenever property is created and
	 *            updated
	 */
	@KafkaListener(topics = { "#{propertiesManager.getCreateValidatedUser()}",
			"#{propertiesManager.getUpdateValidatedUser()}", "#{propertiesManager.getModifyValidatedUser()}" })
	public void receive(Map<String, Object> consumerRecord, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic)
			throws Exception {
		log.info("consumer topic value is: " + topic + " consumer value is" + consumerRecord);
		ObjectMapper objectMapper = new ObjectMapper();
		PropertyRequest propertyRequest = objectMapper.convertValue(consumerRecord, PropertyRequest.class);
		Property property = propertyRequest.getProperties().get(0);
		CalculationRequest calculationRequest = new CalculationRequest();
		calculationRequest.setRequestInfo(propertyRequest.getRequestInfo());
		calculationRequest.setProperty(property);

		log.info("CalculationRequest is:" + calculationRequest);
		CalculationResponse calculationResponse = calculatorRepository.getCalculation(calculationRequest);
		if (calculationResponse!=null && calculationResponse.getTaxes()!=null ){
			
			List<TaxCalculation> currentTaxCalulation = getCurrentTaxCalculation(calculationResponse, propertyRequest);
			property.getPropertyDetail().setRv(currentTaxCalulation.get(0).getPropertyTaxes().getCalculatedRV());
			property.getPropertyDetail().setArv(currentTaxCalulation.get(0).getPropertyTaxes().getCalculatedARV());
			setUnitsRVAndARV(property.getPropertyDetail().getFloors(),currentTaxCalulation);
		calculationResponse.getTaxes();
		}
		log.info("CalculationResponse is:" + calculationResponse);
		String taxCalculations = objectMapper.writeValueAsString(calculationResponse.getTaxes());
		property.getPropertyDetail().setTaxCalculations(taxCalculations);
		propertyRequest.getProperties().get(0).getPropertyDetail().setTaxCalculations(taxCalculations);

		if (topic.equalsIgnoreCase(propertiesManager.getCreateValidatedUser())) {

			kafkaTemplate.send(propertiesManager.getCreateTaxCalculated(), propertyRequest);

		} else if (topic.equalsIgnoreCase(propertiesManager.getUpdateValidatedUser())) {

			kafkaTemplate.send(propertiesManager.getUpdateTaxCalculated(), propertyRequest);

		}

		else if (topic.equalsIgnoreCase(propertiesManager.getModifyValidatedUser())) {

			kafkaTemplate.send(propertiesManager.getModifyTaxCalculated(), propertyRequest);
		}

	}
	
	private void setUnitsRVAndARV(List<Floor> floors,List<TaxCalculation> taxCalculations) {
		
		List<UnitTax> unitsTaxes = taxCalculations.get(0).getUnitTaxes();
		
		
		for (Floor floor : floors ) {
			
			for ( UnitTax unitTax : unitsTaxes){
			for (Unit unit : floor.getUnits()) {
				if (unit.getUnitType().toString().equalsIgnoreCase(propertiesManager.getUnitType())
						&& unit.getUnits() != null) {
					for (Unit room : unit.getUnits()) {
						if(room.getUnitNo()== unitTax.getUnitNo() && floor.getFloorNo()==unitTax.getFloorNumber()){
							unit.setArv(unitTax.getUnitTaxes().getCalculatedARV());
							unit.setRv(unitTax.getUnitTaxes().getCalculatedRV());
						}
						
						
					}
				} else {
					
					if(unit.getUnitNo()== unitTax.getUnitNo() && floor.getFloorNo()==unitTax.getFloorNumber()){
						unit.setArv(unitTax.getUnitTaxes().getCalculatedARV());
						unit.setRv(unitTax.getUnitTaxes().getCalculatedRV());
					}
				
				}
			}
			}

		}
		
		
		
		
	}

	private List<TaxCalculation> getCurrentTaxCalculation(CalculationResponse calculationResponse,PropertyRequest propertyRequest) throws Exception{
		
		
		SimpleDateFormat sdf = new SimpleDateFormat(propertiesManager.getDate());

		SimpleDateFormat sdff = new SimpleDateFormat(propertiesManager.getDateAndTimeFormat());
		
		List<TaxCalculation> currentYearTax = new ArrayList<TaxCalculation>();
		Property property = propertyRequest.getProperties().get(0);
		TaxPeriodResponse taxPeriodsResponse = calculatorRepository.getTaxPeriods(property.getTenantId(), new SimpleDateFormat(propertiesManager.getSimpleDateFormat()).format(new Date()), propertyRequest.getRequestInfo());
		for (TaxPeriod taxPeriod : taxPeriodsResponse.getTaxPeriods()) {

			for (TaxCalculation taxCalculation : calculationResponse.getTaxes()) {
				if ((sdff.parse(taxPeriod.getFromDate()).getTime() == sdf.parse(taxCalculation.getFromDate()).getTime())
						&& (sdff.parse(taxPeriod.getToDate()).getTime() == sdf.parse(taxCalculation.getToDate())
								.getTime())) {

					currentYearTax.add(taxCalculation);

				}
			}
		}
		
		return currentYearTax;
		
		
		
	}

}
