package org.egov.calculator.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.egov.calculator.exception.InvalidInputException;
import org.egov.calculator.models.TaxCalculationModel;
import org.egov.calculator.models.TaxCalculationWrapper;
import org.egov.calculator.repository.GuidanceValueRepostory;
import org.egov.calculator.repository.TaxFactorRepository;
import org.egov.calculator.repository.TaxPeriodRespository;
import org.egov.calculator.repository.TaxRatesRepository;
import org.egov.models.CalculationFactor;
import org.egov.models.CalculationFactorRequest;
import org.egov.models.CalculationFactorResponse;
import org.egov.models.CalculationRequest;
import org.egov.models.CalculationResponse;
import org.egov.models.Floor;
import org.egov.models.GuidanceValue;
import org.egov.models.GuidanceValueRequest;
import org.egov.models.GuidanceValueResponse;
import org.egov.models.OccuapancyMasterResponse;
import org.egov.models.PropertyTypeResponse;
import org.egov.models.RequestInfo;
import org.egov.models.RequestInfoWrapper;
import org.egov.models.ResponseInfo;
import org.egov.models.ResponseInfoFactory;
import org.egov.models.StructureClassResponse;
import org.egov.models.TaxPeriod;
import org.egov.models.TaxPeriodRequest;
import org.egov.models.TaxPeriodResponse;
import org.egov.models.TaxRates;
import org.egov.models.TaxRatesRequest;
import org.egov.models.TaxRatesResponse;
import org.egov.models.Unit;
import org.egov.models.UsageMasterResponse;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

/**
 * Description : CalculatorService interface implementation class
 * 
 * @author Pavan Kumar Kamma
 *
 */

@Service
public class TaxCalculatorServiceImpl implements TaxCalculatorService {

	@Autowired
	TaxFactorRepository taxFactorRepository;

	@Autowired
	ResponseInfoFactory responseInfoFactory;

	@Autowired
	GuidanceValueRepostory calculatorRepository;

	@Autowired
	TaxRatesRepository taxRatesRepository;

	@Autowired
	private TaxPeriodRespository taxPeriodRespository;

	@Autowired
	private Environment environment;

	@Override
	@Transactional
	public CalculationFactorResponse createFactor(String tenantId,
			CalculationFactorRequest calculationFactorRequest) {

		for (CalculationFactor calculationFactor : calculationFactorRequest
				.getCalculationFactors()) {

			validateFactorCode(calculationFactor, calculationFactorRequest);

			try {

				Long id = taxFactorRepository.saveFactor(tenantId,
						calculationFactor);
				calculationFactor.setId(id);

			} catch (Exception e) {

				throw new InvalidInputException(
						calculationFactorRequest.getRequestInfo());

			}
		}

		CalculationFactorResponse calculationFactorResponse = new CalculationFactorResponse();

		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(
						calculationFactorRequest.getRequestInfo(), true);
		calculationFactorResponse.setCalculationFactors(
				calculationFactorRequest.getCalculationFactors());
		calculationFactorResponse.setResponseInfo(responseInfo);

		return calculationFactorResponse;
	}

	private void validateFactorCode(CalculationFactor calculationFactor,
			CalculationFactorRequest calculationFactorRequest) {
		
		if (calculationFactor.getFactorType() != null
				&& !calculationFactor.getFactorType().isEmpty()) {

			RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
			requestInfoWrapper
					.setRequestInfo(calculationFactorRequest.getRequestInfo());
			RestTemplate restTemplate = new RestTemplate();
			StringBuilder uri = new StringBuilder();
			Map<String, String> params = new HashMap<String, String>();
			uri.append(environment.getProperty(
					"egov.services.property_master_service.hostname"));
			uri.append(environment.getProperty(
					"egov.services.property_master_service.basepath"));
			uri.append(environment.getProperty(
					"egov.services.property_master_service.search"));
			params.put("tenantId", calculationFactor.getTenantId());
			params.put("code", calculationFactor.getFactorCode());

			switch (calculationFactor.getFactorType()) {

				case "occupancy" :

					params.put("factorType", "occuapancies");
					OccuapancyMasterResponse occuapancyMasterResponse = restTemplate
							.postForObject(uri.toString(), requestInfoWrapper,
									OccuapancyMasterResponse.class, params);
					if (occuapancyMasterResponse.getOccuapancyMasters()
							.size() == 0) {
						throw new InvalidInputException(
								calculationFactorRequest.getRequestInfo());
					}
					break;
				case "usage" :

					params.put("factorType", "usages");
					UsageMasterResponse usageMasterResponse = restTemplate
							.postForObject(uri.toString(), requestInfoWrapper,
									UsageMasterResponse.class, params);
					if (usageMasterResponse.getUsageMasters().size() == 0) {
						throw new InvalidInputException(
								calculationFactorRequest.getRequestInfo());
					}
					break;
				case "structure" :

					params.put("factorType", "structureclasses");
					StructureClassResponse structureClassResponse = restTemplate
							.postForObject(uri.toString(), requestInfoWrapper,
									StructureClassResponse.class, params);
					if (structureClassResponse.getStructureClasses()
							.size() == 0) {
						throw new InvalidInputException(
								calculationFactorRequest.getRequestInfo());
					}
					break;
				case "propertytype" :

					params.put("factorType", "propertytypes");
					PropertyTypeResponse propertyTypeResponse = restTemplate
							.postForObject(uri.toString(), requestInfoWrapper,
									PropertyTypeResponse.class, params);
					if (propertyTypeResponse.getPropertyTypes().size() == 0) {
						throw new InvalidInputException(
								calculationFactorRequest.getRequestInfo());
					}
					break;
				case "age" :

					params.put("factorType", "age");
					restTemplate.getForObject(uri.toString(), String.class,
							params);
					break;
				default :
					break;

			}
		}
	}

	@Override
	@Transactional
	public CalculationFactorResponse updateFactor(String tenantId,
			CalculationFactorRequest calculationFactorRequest) {

		for (CalculationFactor calculationFactor : calculationFactorRequest
				.getCalculationFactors()) {

			try {

				long updatedTime = new Date().getTime();
				long id = calculationFactor.getId();
				calculationFactor.getAuditDetails()
						.setLastModifiedTime(updatedTime);
				taxFactorRepository.updateFactor(tenantId, id,
						calculationFactor);

			} catch (Exception e) {

				throw new InvalidInputException(
						calculationFactorRequest.getRequestInfo());

			}
		}

		CalculationFactorResponse calculationFactorResponse = new CalculationFactorResponse();

		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(
						calculationFactorRequest.getRequestInfo(), true);
		calculationFactorResponse.setCalculationFactors(
				calculationFactorRequest.getCalculationFactors());
		calculationFactorResponse.setResponseInfo(responseInfo);

		return calculationFactorResponse;
	}

	@Override
	public CalculationFactorResponse getFactor(RequestInfo requestInfo,
			String tenantId, String factorType, String validDate, String code) {

		CalculationFactorResponse calculationFactorResponse = new CalculationFactorResponse();

		try {

			List<CalculationFactor> calculationFactors = taxFactorRepository
					.searchFactor(tenantId, factorType, validDate, code);
			ResponseInfo responseInfo = responseInfoFactory
					.createResponseInfoFromRequestInfo(requestInfo, true);

			calculationFactorResponse.setCalculationFactors(calculationFactors);
			calculationFactorResponse.setResponseInfo(responseInfo);
		} catch (Exception e) {
			throw new InvalidInputException(requestInfo);
		}
		return calculationFactorResponse;
	}

	public List<CalculationFactor> getFactorsByTenantIdAndValidDate(
			String tenantId, String validDate) {

		List<CalculationFactor> calculationFactors = new ArrayList<CalculationFactor>();
		calculationFactors = taxFactorRepository
				.getFactorsByTenantIdAndValidDate(tenantId, validDate);

		return calculationFactors;
	}

	@Override
	@Transactional
	public GuidanceValueResponse createGuidanceValue(String tenantId,
			GuidanceValueRequest guidanceValueRequest) throws Exception {
		// TODO Auto-generated method stub

		for (GuidanceValue guidanceValue : guidanceValueRequest
				.getGuidanceValues()) {

			Long createdTime = new Date().getTime();
			Long id = calculatorRepository.saveGuidanceValue(tenantId,
					guidanceValue);

			guidanceValue.setId(id);
			guidanceValue.getAuditDetails().setCreatedTime(createdTime);
			guidanceValue.getAuditDetails().setLastModifiedTime(createdTime);
		}

		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(
						guidanceValueRequest.getRequestInfo(), true);
		GuidanceValueResponse guidanceValueResponce = new GuidanceValueResponse();
		guidanceValueResponce
				.setGuidanceValues(guidanceValueRequest.getGuidanceValues());
		guidanceValueResponce.setResponseInfo(responseInfo);
		return guidanceValueResponce;
	}

	@Override
	@Transactional
	public GuidanceValueResponse updateGuidanceValue(String tenantId,
			GuidanceValueRequest guidanceValueRequest) throws Exception {
		// TODO Auto-generated method stub
		for (GuidanceValue guidanceValue : guidanceValueRequest
				.getGuidanceValues()) {

			Long modifiedTime = new Date().getTime();
			calculatorRepository.udpateGuidanceValue(tenantId, guidanceValue);
			guidanceValue.getAuditDetails().setLastModifiedTime(modifiedTime);
		}

		ResponseInfo requestInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(
						guidanceValueRequest.getRequestInfo(), true);
		GuidanceValueResponse guidanceValueResponse = new GuidanceValueResponse();
		guidanceValueResponse
				.setGuidanceValues(guidanceValueRequest.getGuidanceValues());
		guidanceValueResponse.setResponseInfo(requestInfo);
		return guidanceValueResponse;
	}

	@Override
	public GuidanceValueResponse getGuidanceValue(RequestInfo requestInfo,
			String tenantId, String boundary, String structure, String usage,
			String subUsage, String occupancy, String validDate)
			throws Exception {
		GuidanceValueResponse guidanceValueResponse = new GuidanceValueResponse();

		try {

			List<GuidanceValue> guidanceValues = calculatorRepository
					.searchGuidanceValue(tenantId, boundary, structure, usage,
							subUsage, occupancy, validDate);
			ResponseInfo responseInfo = responseInfoFactory
					.createResponseInfoFromRequestInfo(requestInfo, true);

			guidanceValueResponse.setGuidanceValues(guidanceValues);
			guidanceValueResponse.setResponseInfo(responseInfo);
		} catch (Exception e) {
			throw new InvalidInputException(requestInfo);
		}
		return guidanceValueResponse;
	}

	@Override
	@Transactional
	public TaxRatesResponse createTaxRate(String tenantId,
			TaxRatesRequest taxRatesRequest) throws Exception {

		for (TaxRates taxRates : taxRatesRequest.getTaxRates()) {

			try {

				Long id = taxRatesRepository.createTaxRates(tenantId, taxRates);
				taxRates.setId(id);

			} catch (Exception e) {

				throw new InvalidInputException(
						taxRatesRequest.getRequestInfo());
			}
		}

		TaxRatesResponse taxRatesResponse = new TaxRatesResponse();
		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(
						taxRatesRequest.getRequestInfo(), true);
		taxRatesResponse.setTaxRates(taxRatesRequest.getTaxRates());
		taxRatesResponse.setResponseInfo(responseInfo);
		return taxRatesResponse;
	};

	@Override
	@Transactional
	public TaxRatesResponse updateTaxRate(String tenantId,
			TaxRatesRequest taxRatesRequest) throws Exception {

		for (TaxRates taxRates : taxRatesRequest.getTaxRates()) {

			try {
				long updatedTime = new Date().getTime();
				taxRates.getAuditDetails().setLastModifiedTime(updatedTime);
				taxRatesRepository.updateTaxRates(tenantId, taxRates);

			} catch (Exception e) {

				throw new InvalidInputException(
						taxRatesRequest.getRequestInfo());
			}
		}
		TaxRatesResponse taxRatesResponse = new TaxRatesResponse();

		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(
						taxRatesRequest.getRequestInfo(), true);
		taxRatesResponse.setTaxRates(taxRatesRequest.getTaxRates());
		taxRatesResponse.setResponseInfo(responseInfo);

		return taxRatesResponse;
	};

	@Override
	public TaxRatesResponse getTaxRate(RequestInfo requestInfo, String tenantId,
			String taxHead, String validDate, Double validARVAmount,
			String parentTaxHead) throws Exception {

		TaxRatesResponse taxRatesResponse = new TaxRatesResponse();

		try {

			List<TaxRates> listOfTaxRates = taxRatesRepository.searchTaxRates(
					tenantId, taxHead, validDate, validARVAmount,
					parentTaxHead);
			ResponseInfo responseInfo = responseInfoFactory
					.createResponseInfoFromRequestInfo(requestInfo, true);
			taxRatesResponse.setTaxRates(listOfTaxRates);
			taxRatesResponse.setResponseInfo(responseInfo);

		} catch (Exception e) {

			throw new InvalidInputException(requestInfo);

		}

		return taxRatesResponse;
	};

	@Override
	public TaxPeriodResponse createTaxPeriod(String tenantId,
			TaxPeriodRequest taxPeriodRequest) throws Exception {

		for (TaxPeriod taxPeriod : taxPeriodRequest.getTaxPeriods()) {

			try {

				Long id = taxPeriodRespository.saveTaxPeriod(taxPeriod,
						tenantId);
				taxPeriod.setId(id);
			} catch (Exception e) {
				throw new InvalidInputException(
						taxPeriodRequest.getRequestInfo());
			}

		}

		TaxPeriodResponse taxPeriodResponse = new TaxPeriodResponse();
		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(
						taxPeriodRequest.getRequestInfo(), true);
		taxPeriodResponse.setResponseInfo(responseInfo);
		taxPeriodResponse.setTaxPeriods(taxPeriodRequest.getTaxPeriods());

		return taxPeriodResponse;
	};

	@Override
	public TaxPeriodResponse updateTaxPeriod(String tenantId,
			TaxPeriodRequest taxPeriodRequest) throws Exception {

		for (TaxPeriod taxPeriod : taxPeriodRequest.getTaxPeriods()) {

			try {
				taxPeriodRespository.updateTaxPeriod(taxPeriod, tenantId);
			} catch (Exception e) {
				throw new InvalidInputException(
						taxPeriodRequest.getRequestInfo());
			}

		}

		TaxPeriodResponse taxPeriodResponse = new TaxPeriodResponse();
		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(
						taxPeriodRequest.getRequestInfo(), true);
		taxPeriodResponse.setResponseInfo(responseInfo);
		taxPeriodResponse.setTaxPeriods(taxPeriodRequest.getTaxPeriods());

		return taxPeriodResponse;
	};

	@Override
	public TaxPeriodResponse getTaxPeriod(RequestInfo requestInfo,
			String tenantId, String validDate, String code) throws Exception {

		List<TaxPeriod> taxPeriods = null;
		try {
			taxPeriods = taxPeriodRespository.searchTaxPeriod(tenantId,
					validDate, code);
		} catch (Exception e) {
			throw new InvalidInputException(requestInfo);
		}

		TaxPeriodResponse taxPeriodResponse = new TaxPeriodResponse();
		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(requestInfo, true);
		taxPeriodResponse.setResponseInfo(responseInfo);
		taxPeriodResponse.setTaxPeriods(taxPeriods);

		return taxPeriodResponse;
	}

	
	
	  public List<TaxRates> getTaxRateByTenantAndDate(String tenantId, String validDate) throws Exception {
	        List<TaxRates> listOfTaxRates = taxRatesRepository.searchTaxRatesByTenantAndDate(tenantId, validDate);
	        return listOfTaxRates;
	    };

	    public List<TaxPeriod> getTaxPeriodsByTenantIdAndDate(String tenantId, String validDate) {

	        return taxPeriodRespository.searchTaxPeriodsByTenantAndDate(tenantId, validDate);

	    }

	    @Override
	    public CalculationResponse calculatePropertyTax(
	            CalculationRequest calculationRequest) throws Exception {
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
	        
	          for (Resource file : getRuleFiles(tenantId)) { kieFileSystem.write(
	          ResourceFactory.newClassPathResource(environment.getProperty("rules.path") + file.getFilename(),"UTF-8")); }
	         
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

	    public TaxCalculationModel getDataForPropertyTax(CalculationRequest calculationRequest) throws Exception {
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
	                factorsList = getFactorsByTenantIdAndValidDate(tenantId,
	                        unit.getOccupancyDate());
	                Map<String, Double> factors = factorsList.stream().collect(
	                        Collectors.toMap(factor -> factor.getFactorType(), factor -> factor.getFactorValue()));
	                factorsMapList.add(factors);
	                taxRates = getTaxRateByTenantAndDate(tenantId, unit.getOccupancyDate());

	                Map<String, TaxRates> taxHeads = taxRates.stream()
	                        .collect(Collectors.toMap(taxHead -> taxHead.getTaxHead(), taxHead -> taxHead));

	                taxRatesMapList.add(taxHeads);
	                taxPeriods = getTaxPeriodsByTenantIdAndDate(tenantId,
	                        unit.getOccupancyDate());

	                Map<String, TaxPeriod> taxPeriodsMap = taxPeriods.stream()
	                        .collect(Collectors.toMap(taxPeriod -> taxPeriod.getFromDate() + "-" + taxPeriod.getToDate(),
	                                taxPeriod -> taxPeriod));
	                taxPeriodMapList.add(taxPeriodsMap);

	                GuidanceValueResponse guidanceValueResponse = getGuidanceValue(calculationRequest.getRequestInfo(), tenantId,
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
