package org.egov.calculator.api;

import javax.validation.Valid;

import org.egov.calculator.service.TaxCalculatorMasterService;
import org.egov.models.CalculationFactorRequest;
import org.egov.models.CalculationFactorResponse;
import org.egov.models.GuidanceValueRequest;
import org.egov.models.GuidanceValueResponse;
import org.egov.models.RequestInfoWrapper;
import org.egov.models.TaxPeriodRequest;
import org.egov.models.TaxPeriodResponse;
import org.egov.models.TaxRatesRequest;
import org.egov.models.TaxRatesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * TaxCalculatorMasterController have the api's related to taxes master
 * 
 * @author Pavan Kumar Kamma
 */
@RestController
@RequestMapping("/properties/taxes")
public class TaxCalculatorMasterController {

    @Autowired
    TaxCalculatorMasterService taxCalculationMasterService;

    /**
     * Description : This api for creating new factor(s)
     * 
     * @param tenantId
     * @param calculationFactorRequest
     * @return calculationFactorResponse
     * @throws Exception
     */
    @RequestMapping(path = "/factor/_create", method = RequestMethod.POST)
    public CalculationFactorResponse createFactor(@RequestParam String tenantId,
            @Valid @RequestBody CalculationFactorRequest calculationFactorRequest) throws Exception {

        return taxCalculationMasterService.createFactor(tenantId, calculationFactorRequest);
    }

    /**
     * Description : This api to Update any of the factor
     * 
     * @param tenantId
     * @param calculationFactorRequest
     * @return calculationFactorResponse
     * @throws Exception
     */
    @RequestMapping(path = "/factor/_update", method = RequestMethod.POST)
    public CalculationFactorResponse updateFactor(@RequestParam String tenantId,
            @Valid @RequestBody CalculationFactorRequest calculationFactorRequest) throws Exception {

        return taxCalculationMasterService.updateFactor(tenantId, calculationFactorRequest);
    }

    /**
     * Description : This api for getting factor details
     * 
     * @param tenantId
     * @param factorType
     * @param validDate
     * @param code
     * @param requestInfo
     * @return calculationFactorResponse
     * @throws Exception
     */
    @RequestMapping(path = "/factor/_search", method = RequestMethod.POST)
    public CalculationFactorResponse getFactor(@RequestBody RequestInfoWrapper requestInfo,
            @RequestParam(required = true) String tenantId, @RequestParam(required = true) String factorType,
            @RequestParam(required = true) String validDate, @RequestParam(required = false) String code)
            throws Exception {

        return taxCalculationMasterService.getFactor(requestInfo.getRequestInfo(), tenantId, factorType, validDate,
                code);

    }

    /**
     * Description : This api for creating new guidanceValue(s)
     * 
     * @param tenantId
     * @param guidanceValueRequest
     * @return GuidanceValueResponse
     * @throws Exception
     */
    @RequestMapping(path = "/guidancevalue/_create", method = RequestMethod.POST)
    public GuidanceValueResponse createGuidanceValue(@RequestParam String tenantId,
            @Valid @RequestBody GuidanceValueRequest guidanceValueRequest) throws Exception {

        return taxCalculationMasterService.createGuidanceValue(tenantId, guidanceValueRequest);
    }

    /**
     * Description : This api for updating guidanceValue(s)
     * 
     * @param tenantId
     * @param guidanceValueRequest
     * @return GuidanceValueResponse
     * @throws Exception
     */
    @RequestMapping(path = "/guidancevalue/_update", method = RequestMethod.POST)
    public GuidanceValueResponse updateGuidanceValue(@RequestParam String tenantId,
            @Valid @RequestBody GuidanceValueRequest guidanceValueRequest) throws Exception {

        return taxCalculationMasterService.updateGuidanceValue(tenantId, guidanceValueRequest);
    }

    /**
     * Description : This api for getting guidancevalue details
     * 
     * @param tenantId
     * @param boundary
     * @param structure
     * @param usage
     * @param subUsage
     * @param occupancy
     * @param validDate
     * @param code
     * @param requestInfo
     * @return GuidanceValueResponse
     * @throws Exception
     */
    @RequestMapping(path = "/guidancevalue/_search", method = RequestMethod.POST)
    public GuidanceValueResponse getGuidanceValue(@RequestBody RequestInfoWrapper requestInfo,
            @RequestParam(required = true) String tenantId, @RequestParam(required = true) String boundary,
            @RequestParam(required = false) String structure, @RequestParam(required = false) String usage,
            @RequestParam(required = false) String subUsage, @RequestParam(required = false) String occupancy,
            @RequestParam(required = true) String validDate) throws Exception {

        return taxCalculationMasterService.getGuidanceValue(requestInfo.getRequestInfo(), tenantId, boundary, structure,
                usage, subUsage, occupancy, validDate);

    }

    /**
     * Description : This api for creating new taxRate(s)
     * 
     * @param tenantId
     * @param taxRatesRequest
     * @return TaxRatesResponse
     * @throws Exception
     */
    @RequestMapping(path = "/taxrates/_create", method = RequestMethod.POST)
    public TaxRatesResponse createTaxRate(@RequestParam String tenantId, @Valid @RequestBody TaxRatesRequest taxRatesRequest)
            throws Exception {

        return taxCalculationMasterService.createTaxRate(tenantId, taxRatesRequest);
    }

    /**
     * Description : This api for updating taxRate(s)
     * 
     * @param tenantId
     * @param taxRatesRequest
     * @return TaxRatesResponse
     * @throws Exception
     */
    @RequestMapping(path = "/taxrates/_update", method = RequestMethod.POST)
    public TaxRatesResponse updateTaxRate(@RequestParam String tenantId, @Valid @RequestBody TaxRatesRequest taxRatesRequest)
            throws Exception {

        return taxCalculationMasterService.updateTaxRate(tenantId, taxRatesRequest);
    }

    /**
     * Description : This api for getting taxRate details
     * 
     * @param tenantId
     * @param taxHead
     * @param validDate
     * @param validARVAmount
     * @param parentTaxHead
     * @param requestInfo
     * @param usage
     * @param propertyType
     * @return TaxRatesResponse
     * @throws Exception
     */
    @RequestMapping(path = "/taxrates/_search", method = RequestMethod.POST)
    public TaxRatesResponse getTaxRate(@RequestBody RequestInfoWrapper requestInfo,
            @RequestParam(required = true) String tenantId, @RequestParam(required = true) String taxHead,
            @RequestParam(required = true) String validDate, @RequestParam(required = true) Double validARVAmount,
            @RequestParam(required = false) String parentTaxHead, @RequestParam(required = false) String usage,
            @RequestParam(required = false) String propertyType) throws Exception {

        return taxCalculationMasterService.getTaxRate(requestInfo.getRequestInfo(), tenantId, taxHead, validDate,
                validARVAmount, parentTaxHead, usage, propertyType);

    }

    /**
     * Description : This will create the tax period
     * 
     * @param tenantId
     * @param TaxPeriodRequest
     * @return {@link TaxPeriodResponse}
     * @throws Exception
     */
    @RequestMapping(path = "/taxperiods/_create", method = RequestMethod.POST)
    public TaxPeriodResponse createTaxPeriod(@RequestParam(required = true) String tenantId,
            @Valid @RequestBody(required = true) TaxPeriodRequest taxPeriodRequest) throws Exception {

        return taxCalculationMasterService.createTaxPeriod(tenantId, taxPeriodRequest);
    }

    /**
     * Description : This will update the tax period with the given request for the given tenantId
     * 
     * @param tenantId
     * @param TaxPeriodRequest
     * @return {@link TaxPeriodResponse}
     * @throws Exception
     */
    @RequestMapping(path = "/taxperiods/_update", method = RequestMethod.POST)
    public TaxPeriodResponse updateTaxPeriod(@RequestParam(required = true) String tenantId,
            @Valid @RequestBody TaxPeriodRequest taxPeriodRequest) throws Exception {

        return taxCalculationMasterService.updateTaxPeriod(tenantId, taxPeriodRequest);
    }

    /**
     * Description : This will search the tax periods based on the given parameter
     * 
     * @param requestInfo
     * @param tenantId
     * @param validDate
     * @param code
     * @param fromDate
     * @param toDate
     * @return TaxPeriodResponse
     * @throws Exception
     */
    @RequestMapping(path = "/taxperiods/_search", method = RequestMethod.POST)
    public TaxPeriodResponse getTaxPeriod(@RequestBody RequestInfoWrapper requestInfo,
			@RequestParam(required = true) String tenantId, @RequestParam(required = false) String validDate,
			@RequestParam(required = false) String code, @RequestParam(required = false) String fromDate,
			@RequestParam(required = false) String toDate, @RequestParam(required = false) String sortTaxPeriod) throws Exception {

		return taxCalculationMasterService.getTaxPeriod(requestInfo.getRequestInfo(), tenantId, validDate, code,
				fromDate, toDate, sortTaxPeriod);

	}
}