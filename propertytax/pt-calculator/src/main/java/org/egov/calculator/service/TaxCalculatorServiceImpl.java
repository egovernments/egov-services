package org.egov.calculator.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.egov.calculator.models.TaxCalculationModel;
import org.egov.calculator.models.TaxCalculationWrapper;
import org.egov.calculator.repository.FactorRepository;
import org.egov.calculator.repository.TaxPeriodRespository;
import org.egov.calculator.repository.TaxRatesRepository;
import org.egov.models.CalculationFactor;
import org.egov.models.CalculationRequest;
import org.egov.models.CalculationResponse;
import org.egov.models.Floor;
import org.egov.models.GuidanceValueResponse;
import org.egov.models.ResponseInfoFactory;
import org.egov.models.TaxPeriod;
import org.egov.models.TaxRates;
import org.egov.models.Unit;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

/**
 * Description : CalculatorService interface implementation class
 * 
 * @author Pavan Kumar Kamma
 *
 */

@Service
public class TaxCalculatorServiceImpl implements TaxCalculatorService {

	@Autowired
	TaxCalculatorMasterService taxCalculatorMasterService;

	@Autowired
	FactorRepository taxFactorRepository;

	@Autowired
	ResponseInfoFactory responseInfoFactory;

	@Autowired
	TaxRatesRepository taxRatesRepository;

	@Autowired
	private TaxPeriodRespository taxPeriodRespository;

	@Autowired
	private Environment environment;

	private List<TaxRates> getTaxRateByTenantAndDate(String tenantId, String validDate) throws Exception {
		List<TaxRates> listOfTaxRates = taxRatesRepository.searchTaxRatesByTenantAndDate(tenantId, validDate);
		return listOfTaxRates;
	};

	private List<TaxPeriod> getTaxPeriodsByTenantIdAndDate(String tenantId, String validDate) {

		return taxPeriodRespository.searchTaxPeriodsByTenantAndDate(tenantId, validDate);

	}

	@Override
	public CalculationResponse calculatePropertyTax(CalculationRequest calculationRequest) throws Exception {
		// TODO Auto-generated method stub
		KieSession kieSession = kieSession(calculationRequest.getProperty().getTenantId());
		TaxCalculationModel taxCalculationModel = getDataForPropertyTax(calculationRequest);
		TaxCalculationWrapper taxCalculationWrapper = new TaxCalculationWrapper();
		taxCalculationWrapper.setProperty(calculationRequest.getProperty());
		taxCalculationWrapper.setTaxCalculationModel(taxCalculationModel);
		kieSession.insert(taxCalculationWrapper);
		kieSession.fireAllRules();
		return new CalculationResponse();
	};

	public KieFileSystem kieFileSystem(String tenantId) throws IOException {
		KieFileSystem kieFileSystem = getKieServices().newKieFileSystem();

		for (Resource file : getRuleFiles(tenantId)) {
			kieFileSystem.write(ResourceFactory
					.newClassPathResource(environment.getProperty("rules.path") + file.getFilename(), "UTF-8"));
		}

		return kieFileSystem;
	}

	private Resource[] getRuleFiles(String tenantId) throws IOException {
		ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		return resourcePatternResolver
				.getResources("classpath*:" + environment.getProperty("rules.path") + "**/" + tenantId + ".*");
	}

	private KieServices getKieServices() {
		return KieServices.Factory.get();
	}

	public KieSession kieSession(String tenantId) throws IOException {
		final KieRepository kieRepository = getKieServices().getRepository();
		kieRepository.addKieModule(new KieModule() {
			public ReleaseId getReleaseId() {
				return kieRepository.getDefaultReleaseId();
			}
		});

		KieBuilder kieBuilder = getKieServices().newKieBuilder(kieFileSystem(tenantId));
		kieBuilder.buildAll();

		KieContainer kieContainer = getKieServices().newKieContainer(kieRepository.getDefaultReleaseId());
		return kieContainer.newKieSession();
	}

	private List<CalculationFactor> getFactorsByTenantIdAndValidDate(String tenantId, String validDate) {

		List<CalculationFactor> calculationFactors = new ArrayList<CalculationFactor>();
		calculationFactors = taxFactorRepository.getFactorsByTenantIdAndValidDate(tenantId, validDate);

		return calculationFactors;
	}

	private TaxCalculationModel getDataForPropertyTax(CalculationRequest calculationRequest) throws Exception {
		TaxCalculationModel taxCalculationModel = new TaxCalculationModel();
		for (Floor floor : calculationRequest.getProperty().getPropertyDetail().getFloors()) {
			List<Map<String, Double>> factorsMapList = new ArrayList<Map<String, Double>>();
			List<Map<String, TaxRates>> taxRatesMapList = new ArrayList<Map<String, TaxRates>>();
			List<Map<String, TaxPeriod>> taxPeriodMapList = new ArrayList<Map<String, TaxPeriod>>();
			List<Double> guidanceValues = new ArrayList<Double>();
			for (Unit unit : floor.getUnits()) {
				List<CalculationFactor> factorsList = null;
				List<TaxRates> taxRates = null;
				List<TaxPeriod> taxPeriods = null;
				String tenantId = calculationRequest.getProperty().getTenantId();
				factorsList = getFactorsByTenantIdAndValidDate(tenantId, unit.getOccupancyDate());
				Map<String, Double> factors = factorsList.stream().collect(Collectors
						.toMap(factor -> factor.getFactorType().toString(), factor -> factor.getFactorValue()));
				factorsMapList.add(factors);
				taxRates = getTaxRateByTenantAndDate(tenantId, unit.getOccupancyDate());

				Map<String, TaxRates> taxHeads = taxRates.stream()
						.collect(Collectors.toMap(taxHead -> taxHead.getTaxHead(), taxHead -> taxHead));

				taxRatesMapList.add(taxHeads);
				taxPeriods = getTaxPeriodsByTenantIdAndDate(tenantId, unit.getOccupancyDate());

				Map<String, TaxPeriod> taxPeriodsMap = taxPeriods.stream().collect(Collectors.toMap(
						taxPeriod -> taxPeriod.getFromDate() + "-" + taxPeriod.getToDate(), taxPeriod -> taxPeriod));
				taxPeriodMapList.add(taxPeriodsMap);

				GuidanceValueResponse guidanceValueResponse = taxCalculatorMasterService.getGuidanceValue(
						calculationRequest.getRequestInfo(), tenantId,
						calculationRequest.getProperty().getBoundary().getRevenueBoundary().getId().toString(),
						unit.getStructure(), calculationRequest.getProperty().getPropertyDetail().getUsage(), null,
						unit.getOccupancy(), unit.getOccupancyDate());
				Double guidanceValue = guidanceValueResponse.getGuidanceValues().get(0).getValue();
				guidanceValues.add(guidanceValue);
			}
			taxCalculationModel.setFactors(factorsMapList);
			taxCalculationModel.setTaxPeriods(taxPeriodMapList);
			taxCalculationModel.setTaxRates(taxRatesMapList);
			taxCalculationModel.setGuidanceValues(guidanceValues);

		}
		return taxCalculationModel;
	}

}