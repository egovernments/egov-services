
package org.egov.lams.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.log4j.Logger;
import org.egov.lams.config.PropertiesManager;
import org.egov.lams.exceptions.CollectionExceedException;
import org.egov.lams.model.Agreement;
import org.egov.lams.model.AgreementCriteria;
import org.egov.lams.model.Allottee;
import org.egov.lams.model.Demand;
import org.egov.lams.model.DemandDetails;
import org.egov.lams.model.PaymentInfo;
import org.egov.lams.model.enums.Action;
import org.egov.lams.model.enums.Source;
import org.egov.lams.model.enums.Status;
import org.egov.lams.repository.BillRepository;
import org.egov.lams.repository.DemandRepository;
import org.egov.lams.repository.FinancialsRepository;
import org.egov.lams.repository.builder.AgreementQueryBuilder;
import org.egov.lams.repository.rowmapper.AgreementRowMapper;
import org.egov.lams.util.BillNumberUtil;
import org.egov.lams.web.contract.AgreementRequest;
import org.egov.lams.web.contract.BillDetailInfo;
import org.egov.lams.web.contract.BillInfo;
import org.egov.lams.web.contract.BillReceiptInfoReq;
import org.egov.lams.web.contract.BillReceiptReq;
import org.egov.lams.web.contract.BillSearchCriteria;
import org.egov.lams.web.contract.BoundaryResponse;
import org.egov.lams.web.contract.ChartOfAccountContract;
import org.egov.lams.web.contract.DemandSearchCriteria;
import org.egov.lams.web.contract.LamsConfigurationGetRequest;
import org.egov.lams.web.contract.ReceiptAccountInfo;
import org.egov.lams.web.contract.ReceiptAmountInfo;
import org.egov.lams.web.contract.RequestInfo;
import org.egov.lams.web.contract.RequestInfoWrapper;
import org.egov.lams.web.validator.AgreementValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    private static final Logger LOGGER = Logger.getLogger(PaymentService.class);
    private static final String ADVANCE_TAX = "ADVANCE_TAX";
    private static final String GOODWILL_AMOUNT = "GOODWILL_AMOUNT";
    private static final String RENT = "RENT";
    private static final String PENALTY = "PENALTY";
    private static final String SERVICE_TAX = "SERVICE_TAX";
    private static final String CENTRAL_GST = "CENTRAL_GST";
    private static final String STATE_GST = "STATE_GST";
    private static final String CGST_ON_ADVANCE = "ADV_CGST";
    private static final String SGST_ON_ADVANCE = "ADV_SGST";
    private static final String CGST_ON_GOODWILL = "GW_CGST";
    private static final String SGST_ON_GOODWILL = "GW_SGST";
    private static final String ST_ON_GOODWILL = "GW_ST";
    private static final String ST_ON_ADVANCE = "ADV_ST";
    private static final String ADVANCE_COLLECTION = "Advance Collection";
    private static final String EVENT_RECEIPT_CREATED = "RECEIPT_CREATED";
    private static final String EVENT_RECEIPT_CANCELLED = "RECEIPT_CANCELLED";
    public static final String RCPT_CANCEL_STATUS = "C";



    @Autowired
    PropertiesManager propertiesManager;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    AgreementService agreementService;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    LamsConfigurationService lamsConfigurationService;

    @Autowired
    DemandRepository demandRepository;

    @Autowired
    BillRepository billRepository;

    @Autowired
    BillNumberUtil billNumberService;

    @Autowired
    FinancialsRepository financialsRepository;

    @Autowired
    private AgreementValidator agreementValidator;

    public String generateBillXml(Agreement agreement, RequestInfo requestInfo) {
        String collectXML = "";
        try {

            Allottee allottee = agreement.getAllottee();
            LamsConfigurationGetRequest lamsGetRequest = new LamsConfigurationGetRequest();
            List<BillInfo> billInfos = new ArrayList<>();
            BillInfo billInfo = new BillInfo();
            billInfo.setId(null);
            LOGGER.info("the demands for a agreement object" + agreement.getDemands());
            if (agreement.getDemands() != null && !agreement.getDemands().isEmpty()) {
                LOGGER.info("the demand id from agreement object" + agreement.getDemands().get(0));
                billInfo.setDemandId(Long.valueOf(agreement.getDemands().get(0)));
            }
            billInfo.setCitizenName(allottee.getName());
            billInfo.setTenantId(agreement.getTenantId());
            // billInfo.setCitizenAddress(agreement.getAllottee().getAddress());
            // TODO: Fix me after the issue is fixed by user service

            if (allottee.getAddress() != null)
                billInfo.setCitizenAddress(allottee.getAddress());
            else
                billInfo.setCitizenAddress("NA");
            billInfo.setBillType("AUTO");
            billInfo.setIssuedDate(new Date());
            billInfo.setLastDate(new Date());
            lamsGetRequest.setName("MODULE_NAME");
            LOGGER.info("before moduleName>>>>>>>");

            String moduleName = lamsConfigurationService.getLamsConfigurations(lamsGetRequest).get("MODULE_NAME")
                    .get(0);
            LOGGER.info("after moduleName>>>>>>>" + moduleName);
            billInfo.setModuleName(moduleName);
            lamsGetRequest.setTenantId(agreement.getTenantId());
            lamsGetRequest.setName("FUND_CODE");
            String fundCode = lamsConfigurationService.getLamsConfigurations(lamsGetRequest).get("FUND_CODE").get(0);
            billInfo.setFundCode(fundCode);
            LOGGER.info("after fundCode>>>>>>>" + fundCode);

            lamsGetRequest.setName("FUNCTIONARY_CODE");
            String functionaryCode = lamsConfigurationService.getLamsConfigurations(lamsGetRequest)
                    .get("FUNCTIONARY_CODE").get(0);
            LOGGER.info("after functionaryCode>>>>>>>" + functionaryCode);

            billInfo.setFunctionaryCode(Long.valueOf(functionaryCode));
            lamsGetRequest.setName("FUNDSOURCE_CODE");
            String fundSourceCode = lamsConfigurationService.getLamsConfigurations(lamsGetRequest)
                    .get("FUNDSOURCE_CODE").get(0);
            LOGGER.info("after fundSourceCode>>>>>>>" + fundSourceCode);

            billInfo.setFundSourceCode(fundSourceCode);
            lamsGetRequest.setName("DEPARTMENT_CODE");
            String departmentCode = lamsConfigurationService.getLamsConfigurations(lamsGetRequest)
                    .get("DEPARTMENT_CODE").get(0);
            billInfo.setDepartmentCode(departmentCode);
            LOGGER.info("after departmentCode>>>>>>>" + departmentCode);

            billInfo.setCollModesNotAllowed("");
            if (agreement.getAsset().getLocationDetails().getElectionWard() != null) {
                LOGGER.info("setting boundary details with Election ward");
                LOGGER.info("Election ward is: " +agreement.getAsset().getLocationDetails().getElectionWard());
                BoundaryResponse boundaryResponse = getBoundariesById(
                        agreement.getAsset().getLocationDetails().getElectionWard(),agreement.getTenantId());
                billInfo.setBoundaryNumber(boundaryResponse.getBoundarys().get(0).getBoundaryNum());
                lamsGetRequest.setName("BOUNDARY_TYPE");
                String boundaryType = lamsConfigurationService.getLamsConfigurations(lamsGetRequest)
                        .get("BOUNDARY_TYPE").get(0);
                LOGGER.info("after boundaryType>>>>>>>" + boundaryType);

                billInfo.setBoundaryType(boundaryType);
            } else {
                // Passing Admin City boundary details when election ward is not
                // available
                billInfo.setBoundaryType("City");
                billInfo.setBoundaryNumber(1l);
            }
            lamsGetRequest.setName("SERVICE_CODE");
            String serviceCode = lamsConfigurationService.getLamsConfigurations(lamsGetRequest).get("SERVICE_CODE")
                    .get(0);
            LOGGER.info("after serviceCode>>>>>>>" + serviceCode);

            billInfo.setServiceCode(serviceCode);

            billInfo.setOverrideAccHeadAllowed('N');
            String description = String.format("Asset Name : %s,  Asset Code : %s", agreement.getAsset().getName(),agreement.getAsset().getCode());
            billInfo.setDescription(description);
            LOGGER.info("after billInfo.setDescription>>>>>>>" + billInfo.getDescription());

            billInfo.setConsumerCode(StringUtils.isBlank(agreement.getAgreementNumber())
                    ? agreement.getAcknowledgementNumber() : agreement.getAgreementNumber());
            billInfo.setCallbackForApportion('N');
            LOGGER.info("after billInfo.setConsumerCode>>>>>>>" + billInfo.getConsumerCode());

            billInfo.setEmailId(agreement.getAllottee().getEmailId());
            billInfo.setConsumerType("Agreement");
            LOGGER.info("before Bill Number" + billNumberService.generateBillNumber());
            billInfo.setBillNumber(billNumberService.generateBillNumber());
            LOGGER.info("after Bill Number" + billNumberService.generateBillNumber());
            List<Demand> demands =  agreement.getLegacyDemands();
            Demand demand = demands.get(0);
            if ("SYSTEM".equalsIgnoreCase(agreement.getSource().toString())) {
                for (DemandDetails demandDetails : demand.getDemandDetails()) {
                    BigDecimal balance = demandDetails.getTaxAmount().subtract(demandDetails.getCollectionAmount());
                    if (balance.compareTo(BigDecimal.ZERO) > 0 && ADVANCE_TAX.equalsIgnoreCase(demandDetails.getTaxReasonCode())
                            || GOODWILL_AMOUNT.equalsIgnoreCase(demandDetails.getTaxReasonCode())
                            || CGST_ON_GOODWILL.equalsIgnoreCase(demandDetails.getTaxReasonCode())
                            || SGST_ON_GOODWILL.equalsIgnoreCase(demandDetails.getTaxReasonCode())
                            || CGST_ON_ADVANCE.equalsIgnoreCase(demandDetails.getTaxReasonCode())
                            || SGST_ON_ADVANCE.equalsIgnoreCase(demandDetails.getTaxReasonCode())) {
                        billInfo.setPartPaymentAllowed('N');

                    } else {
                        billInfo.setPartPaymentAllowed('N');
                        billInfo.setMinAmountPayable(calculateMinAmount(demand).doubleValue());
                    }
                }
            } else {
                billInfo.setPartPaymentAllowed('N');
            }

            billInfo.setDisplayMessage(demand.getModuleName());
            lamsGetRequest.setName("FUNCTION_CODE");
            String functionCode = lamsConfigurationService.getLamsConfigurations(lamsGetRequest).get("FUNCTION_CODE")
                    .get(0);
            BigDecimal totalAmount = BigDecimal.ZERO;
            List<BillDetailInfo> billDetailInfos = new ArrayList<>();
            List<DemandDetails> demandDetails = getOrderedDemandDetails(demand.getDemandDetails());

            int orderNo = 0;
            int groupId =2;
            LOGGER.info("PaymentService- generateBillXml - getting purpose");
            Map<String, String> purposeMap = billRepository.getPurpose(billInfo.getTenantId());
            List<Date> installmentDates = demandDetails.stream().map(demandDetail -> demandDetail.getPeriodStartDate())
                    .distinct().collect(Collectors.toList());

            for (DemandDetails demandDetail : demandDetails) {
                if (demandDetail != null) {
                    LOGGER.info("the reason for demanddetail : " + demandDetail.getTaxReason());
                    if (ADVANCE_TAX.equalsIgnoreCase(demandDetail.getTaxReasonCode())
                            || GOODWILL_AMOUNT.equalsIgnoreCase(demandDetail.getTaxReasonCode())
                            || (demandDetail.getPeriodStartDate().compareTo(new Date()) <= 0)) {
                        orderNo++;
                        totalAmount = totalAmount
                                .add(demandDetail.getTaxAmount().subtract(demandDetail.getCollectionAmount()));
                        LOGGER.info("the amount added to bill : " + totalAmount);

                        for (Date date : installmentDates) {
                            if (date.compareTo(demandDetail.getPeriodStartDate()) == 0
                                    && RENT.equalsIgnoreCase(demandDetail.getTaxReasonCode())) {
                                groupId++;
                            }

                        }
                        billDetailInfos.addAll(getBilldetails(demandDetail, functionCode, orderNo, groupId, requestInfo, purposeMap, installmentDates));
                    }
                }
            }
            billDetailInfos.sort((b1, b2) -> b1.getPeriod().compareTo(b2.getPeriod()));
            billInfo.setTotalAmount(totalAmount.doubleValue());
            billInfo.setBillAmount(totalAmount.doubleValue());
            if (billDetailInfos.isEmpty()) {
                LOGGER.info("No bill details for collection");
                throw new CollectionExceedException();
            } else
                billInfo.setBillDetailInfos(billDetailInfos);
            LOGGER.info("billInfo before>>>>>>>" + billInfo);
            billInfos.add(billInfo);
            final String billXml = billRepository.createBillAndGetXml(billInfos, requestInfo);

            try {
                collectXML = URLEncoder.encode(billXml, "UTF-8");
            } catch (final UnsupportedEncodingException e) {
                throw new RuntimeException(e.getMessage());
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return collectXML;
    }

   /* public String generateBillXml(Agreement agreement, RequestInfo requestInfo) {
        String collectXML = "";
        try {

            Allottee allottee = agreement.getAllottee();
            LamsConfigurationGetRequest lamsGetRequest = new LamsConfigurationGetRequest();
            List<BillInfo> billInfos = new ArrayList<>();
            BillInfo billInfo = new BillInfo();
            billInfo.setId(null);
            LOGGER.info("the demands for a agreement object" + agreement.getDemands());
            if (agreement.getDemands() != null && !agreement.getDemands().isEmpty()) {
                LOGGER.info("the demand id from agreement object" + agreement.getDemands().get(0));
                billInfo.setDemandId(Long.valueOf(agreement.getDemands().get(0)));
            }
            billInfo.setCitizenName(allottee.getName());
            billInfo.setTenantId(agreement.getTenantId());
            // billInfo.setCitizenAddress(agreement.getAllottee().getAddress());
            // TODO: Fix me after the issue is fixed by user service

            if (allottee.getAddress() != null)
                billInfo.setCitizenAddress(allottee.getAddress());
            else
                billInfo.setCitizenAddress("NA");
            billInfo.setBillType("AUTO");
            billInfo.setIssuedDate(new Date());
            billInfo.setLastDate(new Date());
            lamsGetRequest.setName("MODULE_NAME");
            LOGGER.info("before moduleName>>>>>>>");

            String moduleName = lamsConfigurationService.getLamsConfigurations(lamsGetRequest).get("MODULE_NAME")
                    .get(0);
            LOGGER.info("after moduleName>>>>>>>" + moduleName);
            billInfo.setModuleName(moduleName);
            lamsGetRequest.setTenantId(agreement.getTenantId());
            lamsGetRequest.setName("FUND_CODE");
            String fundCode = lamsConfigurationService.getLamsConfigurations(lamsGetRequest).get("FUND_CODE").get(0);
            billInfo.setFundCode(fundCode);
            LOGGER.info("after fundCode>>>>>>>" + fundCode);

            lamsGetRequest.setName("FUNCTIONARY_CODE");
            String functionaryCode = lamsConfigurationService.getLamsConfigurations(lamsGetRequest)
                    .get("FUNCTIONARY_CODE").get(0);
            LOGGER.info("after functionaryCode>>>>>>>" + functionaryCode);

            billInfo.setFunctionaryCode(Long.valueOf(functionaryCode));
            lamsGetRequest.setName("FUNDSOURCE_CODE");
            String fundSourceCode = lamsConfigurationService.getLamsConfigurations(lamsGetRequest)
                    .get("FUNDSOURCE_CODE").get(0);
            LOGGER.info("after fundSourceCode>>>>>>>" + fundSourceCode);

            billInfo.setFundSourceCode(fundSourceCode);
            lamsGetRequest.setName("DEPARTMENT_CODE");
            String departmentCode = lamsConfigurationService.getLamsConfigurations(lamsGetRequest)
                    .get("DEPARTMENT_CODE").get(0);
            billInfo.setDepartmentCode(departmentCode);
            LOGGER.info("after departmentCode>>>>>>>" + departmentCode);

            billInfo.setCollModesNotAllowed("");
            if (agreement.getAsset().getLocationDetails().getElectionWard() != null) {
                LOGGER.info("setting boundary details with Election ward");
                LOGGER.info("Election ward is: " +agreement.getAsset().getLocationDetails().getElectionWard());
                BoundaryResponse boundaryResponse = getBoundariesById(
                        agreement.getAsset().getLocationDetails().getElectionWard(),agreement.getTenantId());
                billInfo.setBoundaryNumber(boundaryResponse.getBoundarys().get(0).getBoundaryNum());
                lamsGetRequest.setName("BOUNDARY_TYPE");
                String boundaryType = lamsConfigurationService.getLamsConfigurations(lamsGetRequest)
                        .get("BOUNDARY_TYPE").get(0);
                LOGGER.info("after boundaryType>>>>>>>" + boundaryType);

                billInfo.setBoundaryType(boundaryType);
            } else {
                // Passing Admin City boundary details when election ward is not
                // available
                billInfo.setBoundaryType("City");
                billInfo.setBoundaryNumber(1l);
            }
            lamsGetRequest.setName("SERVICE_CODE");
            String serviceCode = lamsConfigurationService.getLamsConfigurations(lamsGetRequest).get("SERVICE_CODE")
                    .get(0);
            LOGGER.info("after serviceCode>>>>>>>" + serviceCode);

            billInfo.setServiceCode(serviceCode);

            billInfo.setOverrideAccHeadAllowed('N');
            String description = String.format("Asset Name : %s,  Asset Code : %s", agreement.getAsset().getName(),agreement.getAsset().getCode());
            billInfo.setDescription(description);
            LOGGER.info("after billInfo.setDescription>>>>>>>" + billInfo.getDescription());

            billInfo.setConsumerCode(StringUtils.isBlank(agreement.getAgreementNumber())
                    ? agreement.getAcknowledgementNumber() : agreement.getAgreementNumber());
            billInfo.setCallbackForApportion('N');
            LOGGER.info("after billInfo.setConsumerCode>>>>>>>" + billInfo.getConsumerCode());

            billInfo.setEmailId(agreement.getAllottee().getEmailId());
            billInfo.setConsumerType("Agreement");
            LOGGER.info("before Bill Number" + billNumberService.generateBillNumber());
            billInfo.setBillNumber(billNumberService.generateBillNumber());
            LOGGER.info("after Bill Number" + billNumberService.generateBillNumber());
            DemandSearchCriteria demandSearchCriteria = new DemandSearchCriteria();
            demandSearchCriteria.setDemandId(Long.valueOf(agreement.getDemands().get(0)));

            LOGGER.info("demand before>>>>>>>" + demandSearchCriteria);

            Demand demand = demandRepository.getDemandBySearch(demandSearchCriteria, requestInfo).getDemands().get(0);
            LOGGER.info("demand>>>>>>>" + demand);

            if ("SYSTEM".equalsIgnoreCase(agreement.getSource().toString())) {
                for (DemandDetails demandDetails : demand.getDemandDetails()) {
                    BigDecimal balance = demandDetails.getTaxAmount().subtract(demandDetails.getCollectionAmount());
                    if (balance.compareTo(BigDecimal.ZERO) > 0 && ADVANCE_TAX.equalsIgnoreCase(demandDetails.getTaxReasonCode())
                            || GOODWILL_AMOUNT.equalsIgnoreCase(demandDetails.getTaxReasonCode())
                            || CGST_ON_GOODWILL.equalsIgnoreCase(demandDetails.getTaxReasonCode())
                            || SGST_ON_GOODWILL.equalsIgnoreCase(demandDetails.getTaxReasonCode())
                            || CGST_ON_ADVANCE.equalsIgnoreCase(demandDetails.getTaxReasonCode())
                            || SGST_ON_ADVANCE.equalsIgnoreCase(demandDetails.getTaxReasonCode())) {
                        billInfo.setPartPaymentAllowed('N');

                    } else {
                        billInfo.setPartPaymentAllowed('Y');
                        billInfo.setMinAmountPayable(calculateMinAmount(demand).doubleValue());
                    }
                }
            } else {
                billInfo.setPartPaymentAllowed('Y');
            }

            billInfo.setDisplayMessage(demand.getModuleName());
            lamsGetRequest.setName("FUNCTION_CODE");
            String functionCode = lamsConfigurationService.getLamsConfigurations(lamsGetRequest).get("FUNCTION_CODE")
                    .get(0);
            BigDecimal totalAmount = BigDecimal.ZERO;
            List<BillDetailInfo> billDetailInfos = new ArrayList<>();
            List<DemandDetails> demandDetails = getOrderedDemandDetails(demand.getDemandDetails());

            int orderNo = 0;
            int groupId =2;
            LOGGER.info("PaymentService- generateBillXml - getting purpose");
            Map<String, String> purposeMap = billRepository.getPurpose(billInfo.getTenantId());
            List<Date> installmentDates = demandDetails.stream().map(demandDetail -> demandDetail.getPeriodStartDate())
                    .distinct().collect(Collectors.toList());

            for (DemandDetails demandDetail : demandDetails) {
                if(demandDetail!=null){
                    LOGGER.info("the reason for demanddetail : "+ demandDetail.getTaxReason());
                    if (ADVANCE_TAX.equalsIgnoreCase(demandDetail.getTaxReasonCode())
                            || GOODWILL_AMOUNT.equalsIgnoreCase(demandDetail.getTaxReasonCode())
                            || (demandDetail.getPeriodStartDate().compareTo(new Date()) <= 0)) {
                        orderNo++;
                        totalAmount = totalAmount
                                .add(demandDetail.getTaxAmount().subtract(demandDetail.getCollectionAmount()));
                        LOGGER.info("the amount added to bill : "+totalAmount);

                        for(Date date : installmentDates){
                            if(date.compareTo(demandDetail.getPeriodStartDate())==0
                                    && RENT.equalsIgnoreCase(demandDetail.getTaxReasonCode())){
                                groupId++;
                            }

                        }
                        billDetailInfos
                                .addAll(getBilldetails(demandDetail, functionCode, orderNo, groupId,requestInfo, purposeMap,installmentDates));
                    }
                }
            }
            billDetailInfos.sort((b1, b2) -> b1.getPeriod().compareTo(b2.getPeriod()));
            billInfo.setTotalAmount(totalAmount.doubleValue());
            billInfo.setBillAmount(totalAmount.doubleValue());
            if (billDetailInfos.isEmpty()) {
                LOGGER.info("No bill details for collection");
                throw new CollectionExceedException();
            } else
                billInfo.setBillDetailInfos(billDetailInfos);
            LOGGER.info("billInfo before>>>>>>>" + billInfo);
            billInfos.add(billInfo);
            final String billXml = billRepository.createBillAndGetXml(billInfos, requestInfo);

            try {
                collectXML = URLEncoder.encode(billXml, "UTF-8");
            } catch (final UnsupportedEncodingException e) {
                throw new RuntimeException(e.getMessage());
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return collectXML;
    }*/
    public Agreement getDemandDetails(String agreementNo, String ackNo, String tenantId, final RequestInfoWrapper requestInfoWrapper) {

        DemandSearchCriteria demandSearchCriteria = new DemandSearchCriteria();
        AgreementCriteria agreementCriteria = new AgreementCriteria();
        BigDecimal balance = BigDecimal.ZERO;
        List<DemandDetails> demandDetails = new ArrayList<>();
        List<Demand> demands = new ArrayList<>();

         if (StringUtils.isNotBlank(ackNo))
            agreementCriteria.setAcknowledgementNumber(ackNo);
         else if (StringUtils.isNotBlank(agreementNo))
             agreementCriteria.setAgreementNumber(agreementNo);


        agreementCriteria.setTenantId(tenantId);
        Agreement agreement = agreementService.searchAgreement(
                agreementCriteria, requestInfoWrapper.getRequestInfo()).get(0);

        if (agreement.getDemands() != null && !agreement.getDemands().isEmpty()) {
            demandSearchCriteria.setDemandId(Long.valueOf(agreement.getDemands().get(0)));
        }
        Demand demand = demandRepository.getDemandBySearch(demandSearchCriteria, requestInfoWrapper.getRequestInfo()).getDemands().get(0);
        if(demand != null) {
            for (DemandDetails details : demand.getDemandDetails()) {
                if (details != null ) {
                    balance = details.getTaxAmount().subtract(details.getCollectionAmount());
                    if (balance.compareTo(BigDecimal.ZERO) > 0
                            && (ADVANCE_TAX.equalsIgnoreCase(details.getTaxReasonCode())
                            || CGST_ON_ADVANCE.equalsIgnoreCase(details.getTaxReasonCode())
                            || SGST_ON_ADVANCE.equalsIgnoreCase(details.getTaxReasonCode())
                            || GOODWILL_AMOUNT.equalsIgnoreCase(details.getTaxReasonCode())
                            || CGST_ON_GOODWILL.equalsIgnoreCase(details.getTaxReasonCode())
                            || SGST_ON_GOODWILL.equalsIgnoreCase(details.getTaxReasonCode())
                            || (details.getPeriodStartDate().compareTo(new Date()) <= 0))) {
                        demandDetails.add(details);
                    }
                }
            }
        }

        demand.setDemandDetails(demandDetails);
        demands.add(demand);
        agreement.setLegacyDemands(demands);

        return agreement;
    }


    public List<BillDetailInfo> getBilldetails(final DemandDetails demandDetail, String functionCode, int orderNo,int groupId,
                                               RequestInfo requestInfo, Map<String, String> purpose,List<Date> instalmentDates) {
        final List<BillDetailInfo> billDetails = new ArrayList<>();
        BigDecimal balance = BigDecimal.ZERO;
        LOGGER.info("paymentservice demand detail ::"+demandDetail);
        try {
            BillDetailInfo billdetail = new BillDetailInfo();
            balance=demandDetail.getTaxAmount().subtract(demandDetail.getCollectionAmount());
            billdetail.setOrderNo(orderNo);
            billdetail.setCreditAmount(balance);
            billdetail.setDebitAmount(BigDecimal.ZERO);
            LOGGER.info("getGlCode before>>>>>>>" + demandDetail.getGlCode());
            billdetail.setGlCode(demandDetail.getGlCode());
            LOGGER.info("getGlCode after >>>>>>>" + demandDetail.getGlCode());
            billdetail.setDescription(demandDetail.getTaxPeriod().concat(":").concat(demandDetail.getTaxReason()));
            billdetail.setPeriod(demandDetail.getTaxPeriod());
            if (ADVANCE_TAX.equalsIgnoreCase(demandDetail.getTaxReasonCode())) {
                billdetail.setPurpose(purpose.get("ADVANCE_AMOUNT"));
            } else if (GOODWILL_AMOUNT.equalsIgnoreCase(demandDetail.getTaxReasonCode())) {
                billdetail.setPurpose(purpose.get("CURRENT_AMOUNT"));
            }else if (SERVICE_TAX.equalsIgnoreCase(demandDetail.getTaxReasonCode())) {
                billdetail.setPurpose(purpose.get("SERVICETAX"));
            } else if (CENTRAL_GST.equalsIgnoreCase(demandDetail.getTaxReasonCode())
                    || CGST_ON_ADVANCE.equalsIgnoreCase(demandDetail.getTaxReasonCode())
                    || CGST_ON_GOODWILL.equalsIgnoreCase(demandDetail.getTaxReasonCode())) {
                billdetail.setPurpose(purpose.get("CG_SERVICETAX"));
            } else if (STATE_GST.equalsIgnoreCase(demandDetail.getTaxReasonCode())
                    || SGST_ON_ADVANCE.equalsIgnoreCase(demandDetail.getTaxReasonCode())
                    || SGST_ON_GOODWILL.equalsIgnoreCase(demandDetail.getTaxReasonCode())) {
                billdetail.setPurpose(purpose.get("SG_SERVICETAX"));
            } else if (demandDetail.getPeriodEndDate().compareTo(new Date()) < 0) {
                if (RENT.equalsIgnoreCase(demandDetail.getTaxReasonCode())) {
                    billdetail.setPurpose(purpose.get("ARREAR_AMOUNT"));
                } else if (PENALTY.equalsIgnoreCase(demandDetail.getTaxReasonCode())) {
                    billdetail.setPurpose(purpose.get("ARREAR_LATEPAYMENT_CHARGES"));
                }

            } else {
                if (RENT.equalsIgnoreCase(demandDetail.getTaxReasonCode())) {
                    billdetail.setPurpose(purpose.get("CURRENT_AMOUNT"));
                } else if (PENALTY.equalsIgnoreCase(demandDetail.getTaxReasonCode())) {
                    billdetail.setPurpose(purpose.get("CURRENT_LATEPAYMENT_CHARGES"));
                }

            }
            billdetail.setIsActualDemand(demandDetail.getIsActualDemand());
            billdetail.setFunctionCode(functionCode);
            if (balance.compareTo(BigDecimal.ZERO) > 0) {
                billDetails.add(billdetail);
            }

            for(Date date : instalmentDates){
                if(date.compareTo(demandDetail.getPeriodStartDate() ) == 0){
                    if(ADVANCE_TAX.equalsIgnoreCase(demandDetail.getTaxReasonCode())
                            || CGST_ON_ADVANCE.equalsIgnoreCase(demandDetail.getTaxReasonCode())
                            || SGST_ON_ADVANCE.equalsIgnoreCase(demandDetail.getTaxReasonCode())
                            || ST_ON_ADVANCE.equalsIgnoreCase(demandDetail.getTaxReasonCode())) {
                        billdetail.setGroupId(1);
                    }
                    else if (GOODWILL_AMOUNT.equalsIgnoreCase(demandDetail.getTaxReasonCode())
                            || CGST_ON_GOODWILL.equalsIgnoreCase(demandDetail.getTaxReasonCode())
                            || SGST_ON_GOODWILL.equalsIgnoreCase(demandDetail.getTaxReasonCode())
                            || ST_ON_GOODWILL.equalsIgnoreCase(demandDetail.getTaxReasonCode())) {
                        billdetail.setGroupId(2);
                    }
                    else if(RENT.equalsIgnoreCase(demandDetail.getTaxReasonCode())
                            || CENTRAL_GST.equalsIgnoreCase(demandDetail.getTaxReasonCode())
                            || STATE_GST.equalsIgnoreCase(demandDetail.getTaxReasonCode())
                            || SERVICE_TAX.equalsIgnoreCase(demandDetail.getTaxReasonCode())
                            || PENALTY.equalsIgnoreCase(demandDetail.getTaxReasonCode())) {
                        if(billdetail.getGroupId() == null) {
                            billdetail.setGroupId(groupId);
                        }

                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return billDetails;
    }

    public ResponseEntity<ReceiptAmountInfo> updateDemand(BillReceiptInfoReq billReceiptInfoReq) throws Exception {
        LOGGER.info("PaymentService- updateDemand - billReceiptInfoReq::: - "
                + billReceiptInfoReq.getBillReceiptInfo().getBillReferenceNum());

        RequestInfo requestInfo = billReceiptInfoReq.getRequestInfo();
        BillSearchCriteria billSearchCriteria = new BillSearchCriteria();
        DemandSearchCriteria demandSearchCriteria = new DemandSearchCriteria();
        BillReceiptReq billReceiptInfo = billReceiptInfoReq.getBillReceiptInfo();
        billSearchCriteria.setBillId(Long.valueOf(billReceiptInfo.getBillReferenceNum()));
        BillInfo billInfo = billRepository.searchBill(billSearchCriteria, requestInfo);
        LOGGER.info("PaymentService- updateDemand - billInfo - " + billInfo.getBillNumber());
        demandSearchCriteria.setDemandId(billInfo.getDemandId());
        Demand currentDemand = demandRepository.getDemandBySearch(demandSearchCriteria, requestInfo).getDemands()
                .get(0);
        LOGGER.info("PaymentService- updateDemand - currentDemand - " + currentDemand.getId());
            currentDemand.setMinAmountPayable(0d);
            currentDemand.setPaymentInfos(setPaymentInfos(billReceiptInfo));

        if(billReceiptInfoReq.getBillReceiptInfo().getEvent().equalsIgnoreCase(EVENT_RECEIPT_CREATED)){
            updateDemandDetailForReceiptCreate(currentDemand, billReceiptInfoReq.getBillReceiptInfo());
            LOGGER.info("PaymentService- updateDemand - updateDemandDetailForReceiptCreate done");
            LOGGER.info("The amount collected from citizen is ::: " + currentDemand.getCollectionAmount());
            updateWorkflow(billInfo.getConsumerCode(), requestInfo);
            isAdvanceCollection(billInfo.getConsumerCode(),currentDemand);
            LOGGER.info("the consumer code from bill object ::: " + billInfo.getConsumerCode());
            demandRepository.updateDemandForCollection(Arrays.asList(currentDemand), requestInfo).getDemands().get(0);

        }
        else if (billReceiptInfoReq.getBillReceiptInfo().getEvent().equalsIgnoreCase(EVENT_RECEIPT_CANCELLED)){
            LOGGER.info("PaymentService- updateDemand - updateDemandDetailFor Receipt cancel ");
            updateDemandDetailForReceiptCancel(currentDemand, billReceiptInfoReq.getBillReceiptInfo(),billInfo);
            LOGGER.info("PaymentService- updateDemand - updateDemandDetailFor Receipt cancel done");
            for (PaymentInfo info : currentDemand.getPaymentInfos()){
                info.setStatus(RCPT_CANCEL_STATUS);
            }
            billRepository.updateBill(Arrays.asList(billInfo), requestInfo).getBillInfos().get(0);
            demandRepository.updateDemandForCollectionWithCancelReceipt(Arrays.asList(currentDemand), requestInfo).getDemands().get(0);
        }
        //TODO : implement for instrument bounced

        LOGGER.info("PaymentService- updateDemand - setPaymentInfos done");

        // / FIXME put update workflow here

        return receiptAmountBifurcation(billReceiptInfo, billInfo);
    }

    private void updateWorkflow(String consumerCode, RequestInfo requestInfo) {

        String sql = AgreementQueryBuilder.BASE_SEARCH_QUERY + " where agreement.acknowledgementnumber='"
                + consumerCode + "' OR agreement.agreement_no='" + consumerCode + "' order by agreement.id desc ";

        LOGGER.info("the sql query for fetching agreement using consumercode ::: " + sql);
        List<Agreement> agreements = new ArrayList<>();
        try {
            agreements = jdbcTemplate.query(sql, new AgreementRowMapper());
        } catch (DataAccessException e) {
            LOGGER.info("exception while fetching agreemment in paymentService"+e);
        }
        agreements.sort((agreement1, agreement2) -> agreement2.getId().compareTo(agreement1.getId()));
        Agreement agreement = agreements.get(0);
        LOGGER.info("agreement under workflow --> "+agreement.getId());

        if (!agreement.getIsAdvancePaid() && ((Source.SYSTEM.equals(agreement.getSource())
                && (Action.CREATE.equals(agreement.getAction()) || Action.RENEWAL.equals(agreement.getAction())))
                || (Source.DATA_ENTRY.equals(agreement.getSource()) && Action.RENEWAL.equals(agreement.getAction())))) {
            AgreementRequest agreementRequest = new AgreementRequest();
            agreementRequest.setRequestInfo(requestInfo);
            agreementRequest.setAgreement(agreement);

            agreementService.updateAgreement(agreementRequest);
            LOGGER.info("Workflow update for collection has been put into Kafka Queue");
        }
    }

    private List<PaymentInfo> setPaymentInfos(BillReceiptReq billReceiptInfo) {

        List<PaymentInfo> paymentInfos = new ArrayList<>();
        for (ReceiptAccountInfo accountIfo : billReceiptInfo.getAccountDetails()) {
            PaymentInfo paymentInfo = new PaymentInfo();

            paymentInfo.setReceiptAmount(billReceiptInfo.getTotalAmount());
            paymentInfo.setReceiptDate(billReceiptInfo.getReceiptDate());
            paymentInfo.setReceiptNumber(billReceiptInfo.getReceiptNum());
            paymentInfo.setStatus(billReceiptInfo.getReceiptStatus());

            paymentInfo.setCreditedAmount(accountIfo.getCrAmount());
            paymentInfo.setDebitedAmount(accountIfo.getDrAmount());
            paymentInfo.setDescription(accountIfo.getDescription());
            paymentInfo.setGlCode(accountIfo.getGlCode());
            paymentInfo.setCreditAmountToBePaid(accountIfo.getCreditAmountToBePaid());
            if (accountIfo.getDescription() != null) {
                String[] taxperiods = accountIfo.getDescription().split(":");
                paymentInfo.setTaxPeriod(taxperiods[0]);
                paymentInfo.setTaxReason(taxperiods[1]);
            }
            paymentInfos.add(paymentInfo);
        }
        LOGGER.info("paymengtinfo" + paymentInfos);
        return paymentInfos;
    }

    private void updateDemandDetailForReceiptCreate(Demand demand, BillReceiptReq billReceiptInfo) {
        BigDecimal totalAmountCollected = BigDecimal.ZERO;
        LOGGER.info("the size of objects ::: " + billReceiptInfo.getAccountDetails().size()
                + "the size of demand details ::" + demand.getDemandDetails().size());
        prepareDemandDetailsForCollectionUpdate(demand);
        for (final ReceiptAccountInfo rcptAccInfo : billReceiptInfo.getAccountDetails()) {

            totalAmountCollected = totalAmountCollected.add(updateDmdDetForRcptCreate(demand, rcptAccInfo));
        }
        LOGGER.info("updateDemandDetailForReceiptCreate  ::: totalAmountCollected " + totalAmountCollected);
        demand.setCollectionAmount(totalAmountCollected);
    }

    /*
     * making collection amount zero if rent is fully paid
     * and actual collection should be updated in back update of collection
     */
    private void prepareDemandDetailsForCollectionUpdate(Demand demand) {

        for (DemandDetails demandDetail : demand.getDemandDetails()) {
            demandDetail.setCollectionAmount(BigDecimal.ZERO);
        }

    }
    private BigDecimal updateDmdDetForRcptCreate(Demand demand, final ReceiptAccountInfo rcptAccInfo) {

        BigDecimal totalAmountCollected = BigDecimal.ZERO;

        LOGGER.info("updateDemandDetailForReceiptCreate rcptAccInfo ::: " + rcptAccInfo);
        if (rcptAccInfo.getCrAmount() != null && rcptAccInfo.getCrAmount() > 0 && !rcptAccInfo.isRevenueAccount()

                && rcptAccInfo.getDescription() != null) {
            String[] description = rcptAccInfo.getDescription().split(":");
            String taxPeriod = description[0];
            String taxReason = description[1];
            LOGGER.info("taxPeriod  ::: " + taxPeriod + "taxReason ::::::" + taxReason);
            // updating the existing demand detail..
            for (final DemandDetails demandDetail : demand.getDemandDetails()) {
                LOGGER.info("demandDetail.getTaxPeriod()  ::: " + demandDetail.getTaxPeriod()
                        + "demandDetail.getTaxReason() ::::::" + demandDetail.getTaxReason());
                if (demandDetail.getTaxPeriod() != null && demandDetail.getTaxPeriod().equalsIgnoreCase(taxPeriod)
                        && demandDetail.getTaxReason() != null
                        && demandDetail.getTaxReason().equalsIgnoreCase(taxReason)) {
                    demandDetail.setCollectionAmount(BigDecimal.valueOf(rcptAccInfo.getCrAmount()));
                    totalAmountCollected = totalAmountCollected.add(BigDecimal.valueOf(rcptAccInfo.getCrAmount()));
                    LOGGER.info("everytime totalAmountCollected ::: " + totalAmountCollected);
                }
            }
        }

        return totalAmountCollected;
    }

    public ResponseEntity<ReceiptAmountInfo> receiptAmountBifurcation(final BillReceiptReq billReceiptInfo,
                                                                      BillInfo billInfo) {
        ResponseEntity<ReceiptAmountInfo> receiptAmountInfoResponse = null;
        LOGGER.info("PaymentService- receiptAmountBifurcation - billReceiptInfo - " + billReceiptInfo);
        LOGGER.info("PaymentService- receiptAmountBifurcation - billInfo - " + billInfo);
        final ReceiptAmountInfo receiptAmountInfo = new ReceiptAmountInfo();
        BigDecimal currentInstallmentAmount = BigDecimal.ZERO;
        BigDecimal arrearAmount = BigDecimal.ZERO;
        LOGGER.info("PaymentService- receiptAmountBifurcation - getting purpose");
        Map<String, String> purposeMap = billRepository.getPurpose(billReceiptInfo.getTenantId());
        final List<BillDetailInfo> billDetails = new ArrayList<>(billInfo.getBillDetailInfos());
        for (final ReceiptAccountInfo rcptAccInfo : billReceiptInfo.getAccountDetails()) {
            LOGGER.info("PaymentService- receiptAmountBifurcation - rcptAccInfo - " + rcptAccInfo);
            if (rcptAccInfo.getCrAmount() != null
                    && BigDecimal.valueOf(rcptAccInfo.getCrAmount()).compareTo(BigDecimal.ZERO) == 1) {
                if (rcptAccInfo.getPurpose().equals(purposeMap.get("ARREAR_AMOUNT").toString()))
                    arrearAmount = arrearAmount.add(BigDecimal.valueOf(rcptAccInfo.getCrAmount()));
                else
                    currentInstallmentAmount = currentInstallmentAmount
                            .add(BigDecimal.valueOf(rcptAccInfo.getCrAmount()));

                for (final BillDetailInfo billDet : billDetails) {
                    if (billDet.getOrderNo() == 1) {
                        receiptAmountInfo.setInstallmentFrom(billDet.getDescription());
                    }
                    receiptAmountInfo.setCurrentInstallmentAmount(currentInstallmentAmount.doubleValue());
                    receiptAmountInfo.setArrearsAmount(arrearAmount.doubleValue());
                    receiptAmountInfoResponse = new ResponseEntity<>(receiptAmountInfo, HttpStatus.OK);
                }
            }
            LOGGER.info("PaymentService- receiptAmountBifurcation - receiptAmountInfo - " + receiptAmountInfo);
        }
        return receiptAmountInfoResponse;
    }


    // Receipt cancellation ,updating bill,demand details,demand
    public void updateDemandDetailForReceiptCancel(Demand demand, BillReceiptReq billReceiptInfo, BillInfo billInfo) throws Exception {
        LOGGER.debug("reconcileCollForRcpt Cancel : Updating Collection Started For Demand : " + demand
                + " with BillReceiptInfo - " + billReceiptInfo);
        try {
            updateDmdDetForRcptCancel(demand, billReceiptInfo,billInfo);
            LOGGER.debug("reconcileCollForRcptCancel : Updating Collection finished For Demand : " + demand);
            if (billInfo.getId() != null)
                billInfo.setCancelled("Y");
        } catch (final Exception e) {
            throw new RuntimeException("Error occured during back update of DCB : " + e.getMessage());
        }
    }



    private void updateDmdDetForRcptCancel(Demand demand, BillReceiptReq billReceiptInfo,BillInfo billInfo) throws Exception {
        Boolean activeAgreement =Boolean.FALSE;
        LOGGER.debug("validation for cancellation receipt ");
        String consumerCode = billInfo.getConsumerCode();
        
        String sql = AgreementQueryBuilder.BASE_SEARCH_QUERY + " where agreement.acknowledgementnumber='"
                + consumerCode + "' OR agreement.agreement_no='" + consumerCode + "' ";
        List<Agreement> agreements = null;
        Agreement agreement;
        try {
            agreements = jdbcTemplate.query(sql, new AgreementRowMapper());
        } catch (DataAccessException e) {
            throw new RuntimeException("exception while fetching agreemment to update advance" + e);
        }
        
        if (agreements != null && !agreements.isEmpty()) {
            agreement = agreements.get(0);
            if (Source.SYSTEM.equals(agreement.getSource()) && Status.ACTIVE.equals(agreement.getStatus()))
                activeAgreement = Boolean.TRUE;
        }
        LOGGER.debug("reconcileCollForRcpt Cancel : Updating Collection Started For Demand : " + demand
                + " with BillReceiptInfo - " + billReceiptInfo);
        for (final ReceiptAccountInfo rcptAccInfo : billReceiptInfo.getAccountDetails())
            if (rcptAccInfo.getCrAmount() != null && rcptAccInfo.getCrAmount() > 0
                    && !rcptAccInfo.isRevenueAccount() && rcptAccInfo.getDescription() != null) {

                String[] description = rcptAccInfo.getDescription().split(":");
                String taxPeriod = description[0];
                String taxReason = description[1];
                LOGGER.info("taxPeriod  ::: " + taxPeriod + "taxReason ::::::" + taxReason);
                // updating the existing demand detail..
                for (final DemandDetails demandDetail : demand.getDemandDetails()) {
                    LOGGER.info("demandDetail.getTaxPeriod()  ::: " + demandDetail.getTaxPeriod()
                            + "demandDetail.getTaxReason() ::::::" + demandDetail.getTaxReason());
                    if (demandDetail.getTaxPeriod() != null && demandDetail.getTaxPeriod().equalsIgnoreCase(taxPeriod)
                            && demandDetail.getTaxReason() != null
                            && demandDetail.getTaxReason().equalsIgnoreCase(taxReason)) {
                        LOGGER.info("validating Advance Tax and Good will Amount");
                        if (activeAgreement &&(ADVANCE_TAX.equalsIgnoreCase(demandDetail.getTaxReasonCode())||(GOODWILL_AMOUNT.equalsIgnoreCase(demandDetail.getTaxReasonCode()))))
                          throw new RuntimeException("Cannot cancel receipt as agreement number is already generated.");
                        if (demandDetail.getCollectionAmount().compareTo(BigDecimal.valueOf(rcptAccInfo.getCrAmount())) < 0)
                            throw new RuntimeException(
                                    "updateDmdDetForRcptCancel : Exception while updating cancel receipt, "
                                            + "to be deducted amount " + rcptAccInfo.getCrAmount()
                                            + " is greater than the collected amount " + demandDetail.getCollectionAmount()
                                            + " for demandDetail " + demandDetail);

                        demandDetail
                                .setCollectionAmount(demandDetail.getCollectionAmount().subtract(BigDecimal.valueOf(rcptAccInfo.getCrAmount())));
                        demand.setCollectionAmount(demand.getCollectionAmount().subtract(BigDecimal.valueOf(rcptAccInfo.getCrAmount())));
                        LOGGER.info("Deducted Collected amount Rs." + rcptAccInfo.getCrAmount() + " for tax : " + taxReason
                        );
                    }
                }
            }
    }

    private String getGlcodeById(Long id, String tenantId, RequestInfo requestInfo) {
        ChartOfAccountContract chartOfAccountContract = new ChartOfAccountContract();
        chartOfAccountContract.setId(id);
        chartOfAccountContract.setTenantId(tenantId);
        return financialsRepository.getChartOfAccountGlcodeById(chartOfAccountContract, requestInfo);
    }

    private BoundaryResponse getBoundariesById(Long boundaryId,String tenantId) {

        BoundaryResponse boundaryResponse = null;
        String boundaryUrl = propertiesManager.getBoundaryServiceHostName()
                + propertiesManager.getBoundaryServiceSearchPath()
                + "?Boundary.id=" + boundaryId
                + "&Boundary.tenantId=" + tenantId;
        // FIXME in boundary contract id is string
        LOGGER.info("the boundary url from payment service ::"+boundaryUrl);
        try {
            boundaryResponse = restTemplate.getForObject(boundaryUrl, BoundaryResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.info("the exception thrown from boundary request is :: " + e);
        }
        LOGGER.info("the response from boundary ::"+boundaryResponse);
        return boundaryResponse;
    }

    private void isAdvanceCollection(String acknowledgementno, Demand demand) {
        boolean isAdvanceTax = false;
        String sql = AgreementQueryBuilder.BASE_SEARCH_QUERY + " where agreement.acknowledgementnumber='"
                + acknowledgementno + "'";
        List<Agreement> agreements = null;
        Agreement agreement;
        try {
            agreements = jdbcTemplate.query(sql, new AgreementRowMapper());
        } catch (DataAccessException e) {
            LOGGER.info("exception while fetching agreemment to update advance" + e);
        }
        LOGGER.info("the result form jdbc query ::: " + agreements);
        if (agreements != null && !agreements.isEmpty()) {
            agreement = agreements.get(0);

            for (DemandDetails demandDetail : demand.getDemandDetails()) {
                LOGGER.info("the reason for demanddetail : " + demandDetail.getTaxReasonCode());
                if ("ADVANCE_TAX".equalsIgnoreCase(demandDetail.getTaxReasonCode())) {
                    isAdvanceTax = true;
                }
            }
            if (isAdvanceTax
                    && (Action.CREATE.equals(agreement.getAction()) || Action.RENEWAL.equals(agreement.getAction()))) {
                agreementService.updateAdvanceFlag(agreement);
            }
        }

    }

    /*
     * preparing map installment wise
     *  key is installment date and value is list of demand details for that installment
     *
     */
    private Map<Date, List<DemandDetails>> getDemandDetailsMap(List<DemandDetails> demandDetails) {
        Map<Date, List<DemandDetails>> demandDetailsMap = new LinkedHashMap<>();
        for (DemandDetails demandDetail : demandDetails) {

            if (!demandDetailsMap.containsKey(demandDetail.getPeriodStartDate())) {
                List<DemandDetails> demandDetailsList = new ArrayList<>();
                demandDetailsList.add(demandDetail);
                demandDetailsMap.put(demandDetail.getPeriodStartDate(), demandDetailsList);

            } else {
                demandDetailsMap.get(demandDetail.getPeriodStartDate()).add(demandDetail);
            }

        }
        return demandDetailsMap;

    }

    /*
     * Method to order the demand details in custom order this order will be
     * shown in account details of collection screen
     */
    private List<DemandDetails> getOrderedDemandDetails(List<DemandDetails> demandDetails) {
        demandDetails.sort((d1, d2) -> d1.getPeriodStartDate().compareTo(d2.getPeriodStartDate()));

        List<DemandDetails> orderedDemandDetailsList = new LinkedList<>();
        Map<Date, List<DemandDetails>> demandDetailsMap = getDemandDetailsMap(demandDetails);

        Set<Date> installmentDates = demandDetailsMap.keySet();
        for (Date installmentDate : installmentDates) {
            Set<DemandDetails> demandDetailsSet = new LinkedHashSet<>();
            Map<String, DemandDetails> demandReasonMap = new HashMap<>();
            List<DemandDetails> installmentDemands = demandDetailsMap.get(installmentDate);
            for (DemandDetails demandDetail : installmentDemands) {

                demandReasonMap.put(demandDetail.getTaxReasonCode(), demandDetail);

            }
            if (demandReasonMap.containsKey(ADVANCE_TAX)) {
                demandDetailsSet.add(demandReasonMap.get(ADVANCE_TAX));
            }

            if (demandReasonMap.containsKey(CGST_ON_ADVANCE)) {
                demandDetailsSet.add(demandReasonMap.get(CGST_ON_ADVANCE));
            }
            if (demandReasonMap.containsKey(SGST_ON_ADVANCE)) {
                demandDetailsSet.add(demandReasonMap.get(SGST_ON_ADVANCE));
            }
            if (demandReasonMap.containsKey(GOODWILL_AMOUNT)) {
                demandDetailsSet.add(demandReasonMap.get(GOODWILL_AMOUNT));
            }
            if (demandReasonMap.containsKey(CGST_ON_GOODWILL)) {
                demandDetailsSet.add(demandReasonMap.get(CGST_ON_GOODWILL));
            }
            if (demandReasonMap.containsKey(SGST_ON_GOODWILL)) {
                demandDetailsSet.add(demandReasonMap.get(SGST_ON_GOODWILL));
            }
            if (demandReasonMap.containsKey(RENT)) {
                demandDetailsSet.add(demandReasonMap.get(RENT));
            }
            if (demandReasonMap.containsKey(PENALTY)) {
                demandDetailsSet.add(demandReasonMap.get(PENALTY));
            }
            if (demandReasonMap.containsKey(SERVICE_TAX)) {
                demandDetailsSet.add(demandReasonMap.get(SERVICE_TAX));
            }
            if (demandReasonMap.containsKey(CENTRAL_GST)) {
                demandDetailsSet.add(demandReasonMap.get(CENTRAL_GST));
            }
            if (demandReasonMap.containsKey(STATE_GST)) {
                demandDetailsSet.add(demandReasonMap.get(STATE_GST));
            }
            orderedDemandDetailsList.addAll(demandDetailsSet);

        }
        return orderedDemandDetailsList;

    }

    /*
     * preparing the description for the installment ex: if the collection is
     * for January then the description will be like Jan,<YEAR>:<DemandReason> (Jan,2018:Rent)
     * If it is for quarter/halfyear/annual it will be
     * <startmonth>,<year> - <endingmonth>,<year>:<demandreason> (Apr,2018-Jun,2018:Rent)
     */
    private String getInstallmentDescription(DemandDetails demandDetail) {

        StringBuilder installmentDescription = new StringBuilder();
        StringBuilder timePeriod = new StringBuilder();
        Instant startInstant = Instant.ofEpochMilli(demandDetail.getPeriodStartDate().getTime());
        LocalDateTime startDate = LocalDateTime.ofInstant(startInstant, ZoneId.systemDefault());
        LocalDate installmentFromDate = startDate.toLocalDate();
        Month fromDate = installmentFromDate.getMonth();
        String startingMonth = fromDate.toString();
        int fromYear = installmentFromDate.getYear();

        Instant endInstant = Instant.ofEpochMilli(demandDetail.getPeriodEndDate().getTime());
        LocalDateTime endDate = LocalDateTime.ofInstant(endInstant, ZoneId.systemDefault());
        LocalDate installmentToDate = endDate.toLocalDate();
        Month toDate = installmentToDate.getMonth();
        String endingMonth = toDate.toString();
        int toYear = installmentToDate.getYear();

        if (ADVANCE_TAX.equalsIgnoreCase(demandDetail.getTaxReasonCode())) {
            timePeriod.append(ADVANCE_COLLECTION).append(",").append(fromYear);
        } else {

            if (startingMonth.equalsIgnoreCase(endingMonth)) {

                timePeriod.append(WordUtils.capitalizeFully(startingMonth.substring(0, 3))).append(",")
                        .append(fromYear);
            } else {
                timePeriod.append(WordUtils.capitalizeFully(startingMonth.substring(0, 3))).append(",").append(fromYear)
                        .append("-").append(WordUtils.capitalizeFully(endingMonth.substring(0, 3))).append(",")
                        .append(toYear);

            }
        }
        installmentDescription.append(timePeriod.toString()).append(":").append(demandDetail.getTaxReason());

        return installmentDescription.toString();
    }

    private BigDecimal calculateMinAmount(Demand demand) {
        List<Date> installmentDates = demand.getDemandDetails().stream().map(demandDetails -> demandDetails.getPeriodStartDate())
                .distinct().collect(Collectors.toList());
        BigDecimal rentSum = BigDecimal.ZERO;
        for (DemandDetails demandDetails : demand.getDemandDetails()) {

            if (demandDetails.getPeriodStartDate().compareTo((installmentDates.get(0))) == 0) {
                if (demandDetails.getTaxReasonCode().equalsIgnoreCase(RENT)
                        || demandDetails.getTaxReasonCode().equalsIgnoreCase(CENTRAL_GST)
                        || demandDetails.getTaxReasonCode().equalsIgnoreCase(STATE_GST)
                        || demandDetails.getTaxReasonCode().equalsIgnoreCase(PENALTY)
                        || demandDetails.getTaxReasonCode().equalsIgnoreCase(SERVICE_TAX)) {
                    rentSum = rentSum.add(demandDetails.getTaxAmount());
                }
            }
        }
        return rentSum;
    }
}