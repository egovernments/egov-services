package org.egov.calculator.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.egov.calculator.models.TaxCalculationModel;
import org.egov.calculator.models.TaxCalculationWrapper;
import org.egov.calculator.models.TaxResponse;
import org.egov.calculator.repository.FactorRepository;
import org.egov.calculator.repository.TaxPeriodRespository;
import org.egov.calculator.repository.TaxRatesRepository;
import org.egov.models.CalculationFactor;
import org.egov.models.CalculationRequest;
import org.egov.models.CalculationResponse;
import org.egov.models.CommonTaxDetails;
import org.egov.models.Floor;
import org.egov.models.GuidanceValueResponse;
import org.egov.models.ResponseInfoFactory;
import org.egov.models.TaxCalculation;
import org.egov.models.TaxPeriod;
import org.egov.models.TaxRates;
import org.egov.models.Unit;
import org.egov.models.UnitTax;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
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
    TaxPeriodRespository taxPeriodRespository;

    @Autowired
    Environment environment;

    /**
     * Description: this method will get all factors based on tenantId and valid date
     * @param tenantId
     * @param validDate
     * @return calculationFactorList
     */
    public List<CalculationFactor> getFactorsByTenantIdAndValidDate(
            String tenantId, String validDate) {

        List<CalculationFactor> calculationFactors = new ArrayList<CalculationFactor>();
        calculationFactors = taxFactorRepository
                .getFactorsByTenantIdAndValidDate(tenantId, validDate);

        return calculationFactors;
    }

    /**
     * Description: this method will get all tax rates based on tenantId and valid date
     * @param tenantId
     * @param validDate
     * @return taxRatesList
     */
    public List<TaxRates> getTaxRateByTenantAndDate(String tenantId, String validDate) throws Exception {
        List<TaxRates> listOfTaxRates = taxRatesRepository.searchTaxRatesByTenantAndDate(tenantId, validDate);
        return listOfTaxRates;
    };

    /**
     * Description: this method will get all tax periods based on tenantId and valid date
     * @param tenantId
     * @param validDate
     * @return taxPeriodsList
     */

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
        CalculationResponse calculationResponse = new CalculationResponse();
        TaxCalculation taxCalculation = new TaxCalculation();
        CommonTaxDetails commonTaxDetails = new CommonTaxDetails();
        commonTaxDetails.setCalculatedARV(taxCalculationWrapper.getTaxCalculationModel().getTaxResponse().getAnnualRentalValue());
        commonTaxDetails.setDepreciation(taxCalculationWrapper.getTaxCalculationModel().getTaxResponse().getDepreciation());
        commonTaxDetails.setHeadWiseTaxes(taxCalculationWrapper.getTaxCalculationModel().getTaxResponse().getTaxHeadWiseList());
        taxCalculation.setPropertyTaxes(commonTaxDetails);
        List<UnitTax> unitTaxes = new ArrayList<UnitTax>();
        for (TaxResponse taxResponse : taxCalculationWrapper.getTaxResponses()) {
            UnitTax unitTax = getUnitTax(taxResponse);
            unitTaxes.add(unitTax);
        }
        taxCalculation.setUnitTaxes(unitTaxes);
        List<TaxCalculation> taxCalculationList = new ArrayList<TaxCalculation>();
        taxCalculationList.add(taxCalculation);
        calculationResponse.setResponseInfo(
                responseInfoFactory.createResponseInfoFromRequestInfo(calculationRequest.getRequestInfo(), true));

        return new CalculationResponse();
    };

    public UnitTax getUnitTax(TaxResponse taxResponse) {
        UnitTax unitTax = new UnitTax();
        unitTax.setFloorNumber(taxResponse.getFloorNumber());
        unitTax.setUnitNo(taxResponse.getUnitNo());
        CommonTaxDetails commonTaxDetails = new CommonTaxDetails();
        commonTaxDetails.setCalculatedARV(taxResponse.getAnnualRentalValue());
        commonTaxDetails.setDepreciation(taxResponse.getDepreciation());
        commonTaxDetails.setHeadWiseTaxes(taxResponse.getTaxHeadWiseList());
        unitTax.setUnitTaxes(commonTaxDetails);
        return unitTax;

    }

    /**
     * Description: this method will get and create keyFileSystem object based on tenantId
     * @param tenantId
     * @throws IOException
     * @return kieFileSystem
     */
    public KieFileSystem kieFileSystem(String tenantId) throws IOException {
        KieFileSystem kieFileSystem = getKieServices().newKieFileSystem();

        for (Resource file : getRuleFiles(tenantId)) {
            kieFileSystem.write(
                    ResourceFactory.newClassPathResource(environment.getProperty("rules.path") + file.getFilename(), "UTF-8"));
        }

        return kieFileSystem;
    }

    /**
     * Description :this method wil return the resources from rules path
     * @param tenantId
     * @throws IOException
     */
    private Resource[] getRuleFiles(String tenantId) throws IOException {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        return resourcePatternResolver
                .getResources("classpath*:" + environment.getProperty("rules.path") + "**/" + tenantId + ".*");
    }

    private KieServices getKieServices() {
        return KieServices.Factory.get();
    }

    /**
     * Description : this method will return kiesession object
     * @param tenantId
     * @return kieSession
     * @throws IOException
     */
    public KieSession kieSession(String tenantId) throws IOException {
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kfs = kieServices.newKieFileSystem();
        FileInputStream fis = new FileInputStream(environment.getProperty("rules.path") + "/" + tenantId + ".drl");

        kfs.write(environment.getProperty("rules.path") + "/" + tenantId + ".drl",
                kieServices.getResources().newInputStreamResource(fis));

        KieBuilder kieBuilder = kieServices.newKieBuilder(kfs).buildAll();

        Results results = kieBuilder.getResults();
        if (results.hasMessages(Message.Level.ERROR)) {
            System.out.println(results.getMessages());
            throw new IllegalStateException("### errors ###");
        }

        KieContainer kieContainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());

        KieSession kieSession = kieContainer.newKieSession();
        return kieSession;
    }

    /**
     * Description : this method for getting all requried data for tax calculation
     * @param calculationRequest
     * @return taxCalculationModel
     * @throws Exception
     */
    public TaxCalculationModel getDataForPropertyTax(CalculationRequest calculationRequest) throws Exception {
        TaxCalculationModel taxCalculationModel = new TaxCalculationModel();
        List<List<Map<String, Double>>> factorTypes = new ArrayList<List<Map<String, Double>>>();
        List<List<Map<String, TaxPeriod>>> taxPeriodsList = new ArrayList<List<Map<String, TaxPeriod>>>();
        List<List<List<TaxRates>>> taxRatesData = new ArrayList<List<List<TaxRates>>>();
        List<List<Double>> guidanceValuesList = new ArrayList<List<Double>>();

        for (Floor floor : calculationRequest.getProperty().getPropertyDetail().getFloors()) {
            List<Map<String, Double>> factorsMapList = new ArrayList<Map<String, Double>>();
            List<List<TaxRates>> taxRatesList = new ArrayList<List<TaxRates>>();
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
                        Collectors.toMap(factor -> factor.getFactorType().toString(), factor -> factor.getFactorValue()));
                factorsMapList.add(factors);
                taxRates = getTaxRateByTenantAndDate(tenantId, unit.getOccupancyDate());

                taxRatesList.add(taxRates);
                taxPeriods = getTaxPeriodsByTenantIdAndDate(tenantId,
                        unit.getOccupancyDate());

                Map<String, TaxPeriod> taxPeriodsMap = taxPeriods.stream()
                        .collect(Collectors.toMap(taxPeriod -> taxPeriod.getFromDate() + "-" + taxPeriod.getToDate(),
                                taxPeriod -> taxPeriod));
                taxPeriodMapList.add(taxPeriodsMap);

                GuidanceValueResponse guidanceValueResponse = taxCalculatorMasterService.getGuidanceValue(
                        calculationRequest.getRequestInfo(), tenantId,
                        calculationRequest.getProperty().getBoundary().getRevenueBoundary().getId().toString(),
                        unit.getStructure(), calculationRequest.getProperty().getPropertyDetail().getUsage(), null,
                        unit.getOccupancyType(), unit.getOccupancyDate());
                Double guidanceValue = guidanceValueResponse.getGuidanceValues().get(0).getValue();
                guidanceValues.add(guidanceValue);

            }
            factorTypes.add(factorsMapList);
            taxPeriodsList.add(taxPeriodMapList);
            taxRatesData.add(taxRatesList);
            guidanceValuesList.add(guidanceValues);

        }
        taxCalculationModel.setFactors(factorTypes);
        taxCalculationModel.setTaxPeriods(taxPeriodsList);
        taxCalculationModel.setTaxRates(taxRatesData);
        taxCalculationModel.setGuidanceValues(guidanceValuesList);
        return taxCalculationModel;
    }
}