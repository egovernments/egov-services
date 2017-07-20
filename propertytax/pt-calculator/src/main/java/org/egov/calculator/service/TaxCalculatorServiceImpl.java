package org.egov.calculator.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.egov.calculator.exception.InvalidTaxCalculationDataException;
import org.egov.calculator.models.TaxCalculationWrapper;
import org.egov.calculator.models.TaxperiodWrapper;
import org.egov.calculator.models.UnitWrapper;
import org.egov.calculator.repository.FactorRepository;
import org.egov.calculator.repository.TaxPeriodRespository;
import org.egov.calculator.repository.TaxRatesRepository;
import org.egov.models.CalculationFactor;
import org.egov.models.CalculationRequest;
import org.egov.models.CalculationResponse;
import org.egov.models.CommonTaxDetails;
import org.egov.models.Floor;
import org.egov.models.GuidanceValueResponse;
import org.egov.models.HeadWiseTax;
import org.egov.models.RequestInfo;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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

    public List<TaxPeriod> getTaxPeriodsByTenantIdAndDate(String tenantId, String fromDate, String toDate) {

        return taxPeriodRespository.searchTaxPeriodsByTenantAndDate(tenantId, fromDate, toDate);

    }

    @Override
    public CalculationResponse calculatePropertyTax(
            CalculationRequest calculationRequest) throws Exception {
        CalculationResponse calculationResponse = new CalculationResponse();
        // TODO Auto-generated method stub

        List<UnitWrapper> roomsList = new ArrayList<UnitWrapper>();
        for (Floor floor : calculationRequest.getProperty().getPropertyDetail().getFloors()) {
            for (Unit unit : floor.getUnits()) {
                if (unit.getUnitType().toString().equalsIgnoreCase(environment.getProperty("unit.type"))
                        && unit.getUnits() != null) {
                    for (Unit room : unit.getUnits()) {
                        UnitWrapper unitWrapper = new UnitWrapper();
                        unitWrapper.setUnit(room);
                        unitWrapper.setFloorNo(floor.getFloorNo());
                        roomsList.add(unitWrapper);
                    }
                } else {
                    UnitWrapper unitWrapper = new UnitWrapper();
                    unitWrapper.setUnit(unit);
                    unitWrapper.setFloorNo(floor.getId().toString());
                    roomsList.add(unitWrapper);
                }
            }

        }
        List<TaxperiodWrapper> taxPeriods = getTaxPeriods(roomsList, calculationRequest.getProperty().getTenantId());
        taxPeriods = getDataForTaxCalculation(taxPeriods, calculationRequest.getProperty().getTenantId(), calculationRequest);
        TaxCalculationWrapper taxWrapper = new TaxCalculationWrapper();
        taxWrapper.setProperty(calculationRequest.getProperty());
        taxWrapper.setTaxPeriods(taxPeriods);

        KieSession kieSession = kieSession(calculationRequest.getProperty().getTenantId(), calculationRequest.getRequestInfo());

        kieSession.insert(taxWrapper);
        kieSession.fireAllRules();
        calculationResponse = taxWrapper.getCalculationResponse();
        calculationResponse.setResponseInfo(
                responseInfoFactory.createResponseInfoFromRequestInfo(calculationRequest.getRequestInfo(), true));
        return calculationResponse;
    };

    public List<TaxperiodWrapper> getDataForTaxCalculation(List<TaxperiodWrapper> taxperiods, String tenantId,
            CalculationRequest calculationRequest) throws Exception {

        for (TaxperiodWrapper taxWrapper : taxperiods) {
            for (UnitWrapper wrapper : taxWrapper.getUnits()) {
                Unit unit = wrapper.getUnit();
                DateFormat dateFormat = new SimpleDateFormat(environment.getProperty("date.format"));
                Date fromDate = dateFormat.parse( taxWrapper.getTaxPeriod().getFromDate());
                String date = new SimpleDateFormat(environment.getProperty("date.input.format")).format(fromDate);
                List<CalculationFactor> factorsList = getFactorsByTenantIdAndValidDate(tenantId,
                        date);
                Map<String, Double> factors = factorsList.stream().collect(
                        Collectors.toMap(factor -> factor.getFactorType().toString() + factor.getFactorCode(),
                                factor -> factor.getFactorValue()));
                wrapper.setFactors(factors);
                List<TaxRates> taxRates = getTaxRateByTenantAndDate(tenantId, date);
                wrapper.setTaxRates(taxRates);
                GuidanceValueResponse guidanceValueResponse = taxCalculatorMasterService.getGuidanceValue(
                        calculationRequest.getRequestInfo(), tenantId,
                        calculationRequest.getProperty().getBoundary().getRevenueBoundary().getId().toString(),
                        unit.getStructure(),
                       unit.getUsage(), null, unit.getOccupancyType(),
                        unit.getOccupancyDate());
                Double guidanceValue = guidanceValueResponse.getGuidanceValues().get(0).getValue();
                wrapper.setGuidanceValue(guidanceValue);
            }
        }

        return taxperiods;
    }

    /**
     * Description : this method will return kiesession object
     * @param tenantId
     * @return kieSession
     * @throws IOException
     * @throws InvalidTaxCalculationData
     */
    public KieSession kieSession(String tenantId, RequestInfo requestInfo)
            throws IOException, InvalidTaxCalculationDataException {
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kfs = kieServices.newKieFileSystem();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(environment.getProperty("rules.path") + "/" + tenantId + ".drl");
        } catch (Exception ex) {
            while (tenantId.contains(".")) {

                tenantId = tenantId.substring(0, tenantId.lastIndexOf("."));
                try {
                    fis = new FileInputStream(environment.getProperty("rules.path") + "/" + tenantId + ".drl");
                    break;
                } catch (Exception exception) {

                }
            }
        }
        kfs.write(environment.getProperty("rules.path") + "/" + tenantId + ".drl",
                kieServices.getResources().newInputStreamResource(fis));

        KieBuilder kieBuilder = kieServices.newKieBuilder(kfs).buildAll();

        Results results = kieBuilder.getResults();
        if (results.hasMessages(Message.Level.ERROR)) {
            throw new InvalidTaxCalculationDataException(results.getMessages().toString(), requestInfo,
                    results.getMessages().toString());
        }

        KieContainer kieContainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());

        KieSession kieSession = kieContainer.newKieSession();
        return kieSession;
    }

    /**
     * This method will construct all tax periods along with corresponding units
     * @param units
     * @param tenantId
     * @return unitWrapperLis
     * @throws ParseException
     */
    public List<TaxperiodWrapper> getTaxPeriods(List<UnitWrapper> units, String tenantId) throws ParseException {
        List<TaxperiodWrapper> taxPeriodList = new ArrayList<TaxperiodWrapper>();
        for (UnitWrapper unitWrapper : units) {
            Unit unit = unitWrapper.getUnit();
            List<TaxPeriod> periodList = getTaxPeriodListForUnit(unit, tenantId);
            for (TaxPeriod taxPeriod : periodList) {
                long taxPeriodId = taxPeriod.getId();
                if (taxPeriodList.size() != 0) {
                    Optional<TaxperiodWrapper> matchingTaxPeriod = taxPeriodList.stream()
                            .filter(t -> t.getTaxPeriod().getId() == taxPeriodId).findFirst();
                    TaxperiodWrapper taxWrapper = matchingTaxPeriod.orElse(null);
                    if (taxWrapper != null) {
                        int index = taxPeriodList.indexOf(taxWrapper);
                        List<UnitWrapper> unitList = taxWrapper.getUnits();
                        UnitWrapper unitData = new UnitWrapper();
                        unitData.setFloorNo(unitWrapper.getFloorNo());
                        unitData.setUnit(unit);
                        unitList.add(unitData);
                        taxWrapper.setUnits(unitList);
                        taxPeriodList.set(index, taxWrapper);
                    } else {
                        TaxperiodWrapper unitData = new TaxperiodWrapper();
                        unitData.setTaxPeriod(taxPeriod);
                        List<UnitWrapper> unitList = new ArrayList<UnitWrapper>();
                        UnitWrapper unitWithFloor = new UnitWrapper();
                        unitWithFloor.setFloorNo(unitWrapper.getFloorNo());
                        unitWithFloor.setUnit(unit);
                        unitList.add(unitWithFloor);
                        unitData.setUnits(unitList);
                        taxPeriodList.add(unitData);
                    }
                } else {
                    TaxperiodWrapper taxWrapper = new TaxperiodWrapper();
                    taxWrapper.setTaxPeriod(taxPeriod);
                    List<UnitWrapper> unitList = new ArrayList<UnitWrapper>();
                    UnitWrapper unitWithFloor = new UnitWrapper();
                    unitWithFloor.setFloorNo(unitWrapper.getFloorNo());
                    unitWithFloor.setUnit(unit);
                    unitList.add(unitWithFloor);
                    taxWrapper.setUnits(unitList);
                    taxPeriodList.add(taxWrapper);
                }
            }
        }
        return taxPeriodList;
    }

    /**
     * Description : This method will get all tax periods for which we have to calculate tax
     * @param unit
     * @param tenantId
     * @return taxPeriodList
     * @throws ParseException
     */

    public List<TaxPeriod> getTaxPeriodListForUnit(Unit unit, String tenantId) throws ParseException {

     
        Date todayDate = new Date();
        String currentDate= new SimpleDateFormat(environment.getProperty("date.input.format")).format(todayDate);
        TaxPeriod taxperiod = taxPeriodRespository.getToDateForTaxCalculation(tenantId,currentDate);
        String currentFinincialYear = taxperiod.getToDate();
        TaxPeriod taxperiodForFrom = taxPeriodRespository.getToDateForTaxCalculation(tenantId, unit.getOccupancyDate());
        DateFormat dateFormat = new SimpleDateFormat(environment.getProperty("date.format"));
        Date occupancy = dateFormat.parse(taxperiodForFrom.getFromDate());
        String date = new SimpleDateFormat(environment.getProperty("date.input.format")).format(occupancy);
        List<TaxPeriod> taxPeriodsList = getTaxPeriodsByTenantIdAndDate(tenantId, date,
                currentFinincialYear);
        return taxPeriodsList;
    }

    /**
     * Description : This method will construct taxCalculation Object
     * @param unitTaxList
     * @param taxPeriod
     * @return taxCalculation
     * @throws ParseException
     */

    public TaxCalculation getTaxCalculation(List<UnitTax> unitTaxList, TaxPeriod taxPeriod) throws ParseException {
        TaxCalculation taxCalculation = new TaxCalculation();
        taxCalculation.setEffectiveDate(taxPeriod.getFromDate());
        taxCalculation.setToDate(taxPeriod.getToDate());
        taxCalculation.setFromDate(taxPeriod.getFromDate());
        CommonTaxDetails commonTaxDetails = new CommonTaxDetails();
        Double calculatedARV = 0.0;
        Double manualARV = 0.0;
        Double depreciation = 0.0;
        Double totalTax = 0.0;
        List<HeadWiseTax> headWiseTaxesList = new ArrayList<HeadWiseTax>();

        for (UnitTax unitTax : unitTaxList) {
            calculatedARV += unitTax.getUnitTaxes().getCalculatedARV();
            if (unitTax.getUnitTaxes().getManualARV() != null)
                manualARV += unitTax.getUnitTaxes().getManualARV();
            depreciation += unitTax.getUnitTaxes().getDepreciation();

            for (HeadWiseTax headWise : unitTax.getUnitTaxes().getHeadWiseTaxes()) {
                if (headWiseTaxesList.size() != 0) {
                    Optional<HeadWiseTax> headWiseTax = headWiseTaxesList.stream()
                            .filter(HeadWiseTax -> HeadWiseTax.getTaxName().equalsIgnoreCase(headWise.getTaxName())).findFirst();
                    HeadWiseTax headTax = headWiseTax.orElse(null);
                    if (headTax != null) {
                        int index = getIndexOfHeadWise(headWiseTaxesList, headWise);
                        Double taxValue = Math.ceil(headWise.getTaxValue() + headTax.getTaxValue());
                        headTax.setTaxValue(taxValue);
                        headWiseTaxesList.set(index, headTax);
                    } else {
                        HeadWiseTax head = new HeadWiseTax();
                        head.setTaxDays(headWise.getTaxDays());
                        head.setTaxName(headWise.getTaxName());
                        head.setTaxValue(headWise.getTaxValue());
                        headWiseTaxesList.add(head);
                    }
                } else {
                    HeadWiseTax head = new HeadWiseTax();
                    head.setTaxDays(headWise.getTaxDays());
                    head.setTaxName(headWise.getTaxName());
                    head.setTaxValue(headWise.getTaxValue());
                    headWiseTaxesList.add(head);
                }
            }
        }
        taxCalculation.setUnitTaxes(unitTaxList);
        List<String> occupancyList = unitTaxList.stream().map(UnitTax -> UnitTax.getUnitTaxes().getOccupancyDate())
                .collect(Collectors.toList());
        String minimumDate = minimumOccupancyDate(occupancyList);
        totalTax = headWiseTaxesList.stream().filter(HeadWiseTax -> HeadWiseTax.getTaxValue() >= 0.0)
                .mapToDouble(HeadWiseTax -> HeadWiseTax.getTaxValue()).sum();
        commonTaxDetails.setCalculatedARV(Math.ceil(calculatedARV));
        commonTaxDetails.setDepreciation(Math.ceil(depreciation));
        commonTaxDetails.setEffectiveDate(taxPeriod.getFromDate());
        commonTaxDetails.setTotalTax(Math.ceil(totalTax));
        commonTaxDetails.setOccupancyDate(minimumDate);
        commonTaxDetails.setManualARV(Math.ceil(manualARV));
        commonTaxDetails.setHeadWiseTaxes(headWiseTaxesList);
        taxCalculation.setPropertyTaxes(commonTaxDetails);
        return taxCalculation;
    }

    /**
     * Description : This method will calculate unit wise tax details
     * @param unitWrapper
     * @param taxPeriod
     * @return unitTax
     * @throws ParseException
     */

    public UnitTax calculateUnitTax(UnitWrapper unitWrapper, TaxPeriod taxPeriod, Double rentForMonth, Double agePercentage,
            Double grossPercentage) throws ParseException {

        Double depreciation = rentForMonth * (agePercentage / 100);
        depreciation = Math.ceil(depreciation);
        Double finalMRV = rentForMonth - depreciation;
        finalMRV = Math.round(finalMRV * 100.0) / 100.0;
        Double grossARV = 12 * finalMRV;
        grossARV = Math.round(grossARV * 100.0) / 100.0;
        Double finalARV = grossARV - (grossPercentage * grossARV);
        finalARV = Math.round(finalARV * 100.0) / 100.0;
        Double calculatedARV = finalARV;
        if (unitWrapper.getUnit().getManualArv() != null) {
            finalARV = unitWrapper.getUnit().getManualArv();
            finalARV = Math.round(finalARV * 100.0) / 100.0;
        } else {
            if (unitWrapper.getUnit().getRentCollected() != null && unitWrapper.getUnit().getRentCollected() >= finalARV) {
                finalARV = unitWrapper.getUnit().getRentCollected();
                finalARV = Math.round(finalARV * 100.0) / 100.0;
            }
        }

        List<TaxRates> independentTaxes = unitWrapper.getTaxRates().stream()
                .filter(taxRate -> taxRate.getDependentTaxHead() == null ||  taxRate.getDependentTaxHead().isEmpty()).collect(Collectors.toList());
        List<HeadWiseTax> headWiseTaxes = new ArrayList<HeadWiseTax>();
        Map<String, List<TaxRates>> independentTaxesMap = independentTaxes.stream()
                .collect(Collectors.groupingBy(taxRate -> taxRate.getTaxHead()));
        List<TaxRates> dependentTaxes = unitWrapper.getTaxRates().stream()
                .filter(taxRate -> taxRate.getDependentTaxHead()!=null && !taxRate.getDependentTaxHead().isEmpty()).collect(Collectors.toList());
        Map<String, List<TaxRates>> depenentTaxesMap = dependentTaxes.stream()
                .collect(Collectors.groupingBy(taxRate -> taxRate.getTaxHead()));

        List<HeadWiseTax> parentHeadWiseTaxes = getTaxHead(independentTaxesMap, finalARV, null, taxPeriod);
        headWiseTaxes.addAll(parentHeadWiseTaxes);
        List<HeadWiseTax> dependentHeadWiseTaxes = getTaxHead(depenentTaxesMap, finalARV, headWiseTaxes, taxPeriod);
        headWiseTaxes.addAll(dependentHeadWiseTaxes);
        UnitTax unitTax = getUnitTax(unitWrapper, calculatedARV, depreciation, headWiseTaxes, taxPeriod);
        return unitTax;
    }

    /**
     * Description : This method is utility to get UnitTax object
     * @param unitWrapper
     * @param calculatedARV
     * @param depreciation
     * @param HeadWise
     * @param taxPeriod
     * @return unitTax
     */

    public UnitTax getUnitTax(UnitWrapper unitWrapper, Double calculatedARV, Double depreciation, List<HeadWiseTax> headWiseTaxes,
            TaxPeriod taxPeriod) {
        UnitTax unitTax = new UnitTax();
        unitTax.setAssessableArea(unitWrapper.getUnit().getAssessableArea());
        unitTax.setFloorNumber(unitWrapper.getFloorNo().toString());
        unitTax.setUnitNo(unitWrapper.getUnit().getUnitNo());
        unitTax.setStructureFactor(unitWrapper.getFactors().get("structure" + unitWrapper.getUnit().getStructure()));
        unitTax.setUsageFactor(unitWrapper.getFactors().get("usage" + unitWrapper.getUnit().getUsage()));
        CommonTaxDetails commonTaxDetails = new CommonTaxDetails();
        commonTaxDetails.setCalculatedARV(calculatedARV);
        commonTaxDetails.setDepreciation(depreciation);
        commonTaxDetails.setEffectiveDate(taxPeriod.getFromDate());
        if (unitWrapper.getUnit().getManualArv() != null)
            commonTaxDetails.setManualARV(unitWrapper.getUnit().getManualArv());
        commonTaxDetails.setHeadWiseTaxes(headWiseTaxes);
        commonTaxDetails.setOccupancyDate(unitWrapper.getUnit().getOccupancyDate());
        Double totalTax = headWiseTaxes.stream().filter(HeadWiseTax -> HeadWiseTax.getTaxValue() >= 0.0)
                .mapToDouble(HeadWiseTax -> HeadWiseTax.getTaxValue()).sum();
        commonTaxDetails.setTotalTax(totalTax);
        unitTax.setUnitTaxes(commonTaxDetails);
        return unitTax;
    }

    /**
     * Description : This method will calculate all tax heads for particular unit
     * @param TaxesMap
     * @param finalARV
     * @param headWise
     * @param taxPeriod
     * @return headWiseTaxList
     * @throws ParseException
     */

    public List<HeadWiseTax> getTaxHead(Map<String, List<TaxRates>> independentTaxesMap, Double finalARV,
            List<HeadWiseTax> headWises, TaxPeriod taxPeriod) throws ParseException {

        List<HeadWiseTax> headWiseList = new ArrayList<HeadWiseTax>();
        for (Map.Entry<String, List<TaxRates>> entry : independentTaxesMap.entrySet()) {
            HeadWiseTax headWiseTax = new HeadWiseTax();
            if (headWises != null) {
                String parentTaxName = entry.getValue().get(0).getDependentTaxHead();
                HeadWiseTax parentHeadWiseTax = headWises.stream()
                        .filter(HeadWiseTax -> HeadWiseTax.getTaxName().equalsIgnoreCase(parentTaxName))
                        .collect(Collectors.toList()).get(0);
                finalARV = parentHeadWiseTax.getTaxValue();
            }
            for (TaxRates taxRate : entry.getValue()) {
                if (finalARV >= taxRate.getFromValue() && finalARV <= taxRate.getToValue()) {
                    Double tax = finalARV * (taxRate.getRatePercentage() / 100);
                    tax = Math.ceil(tax);
                    headWiseTax.setTaxName(entry.getKey());
                    headWiseTax.setTaxValue(tax);
                    Integer days = getDifferenceDays(taxPeriod.getFromDate(), taxPeriod.getToDate());
                    headWiseTax.setTaxDays(days);
                    headWiseList.add(headWiseTax);
                    break;
                }
            }
        }
        return headWiseList;
    }

    /**
     * Description : This method will calculate number of days between two dates
     * @param fromDate
     * @param toDate
     * @return
     * @throws ParseException
     */

    public Integer getDifferenceDays(String fromDate, String toDate) throws ParseException {
        Integer daysdiff = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        // sdf.setTimeZone(TimeZone.getTimeZone("IST"));
        Date from = sdf.parse(fromDate);
        Date to = sdf.parse(toDate);
        Long diff = to.getTime() - from.getTime();
        Long diffDays = diff / (24 * 60 * 60 * 1000) + 1;
        daysdiff = Integer.valueOf(diffDays.toString());
        return daysdiff;
    }

    /**
     * Description: This method will give index of matching taxHead record from list of taxHeads
     * @param taxHeads
     * @param tax
     * @return integer
     */

    public int getIndexOfHeadWise(List<HeadWiseTax> taxHeads, HeadWiseTax tax) {
        int index = 0;
        for (HeadWiseTax head : taxHeads) {
            if (head.getTaxName().equalsIgnoreCase(tax.getTaxName())) {
                break;
            } else {
                index++;
            }
        }
        return index;
    }

    /**
     * Description : This method will return minimum occupancyDate from the list of dates
     * @param occupancyDates
     * @return string
     * @throws ParseException
     */

    public String minimumOccupancyDate(List<String> occupancyDates) throws ParseException {
        if (occupancyDates.size() > 0) {
            List<Date> dateList = new ArrayList<Date>();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            for (String date : occupancyDates) {
                dateList.add(sdf.parse(date));
            }

            Date minDate = dateList.stream().map(Date -> Date).min(Date::compareTo).get();
            return minDate.toString();
        }
        return null;
    }

}