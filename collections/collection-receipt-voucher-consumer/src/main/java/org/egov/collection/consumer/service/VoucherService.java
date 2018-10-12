/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any Long of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.collection.consumer.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import org.apache.log4j.Logger;
import org.egov.collection.consumer.config.PropertiesManager;
import org.egov.collection.consumer.model.AccountDetail;
import org.egov.collection.consumer.model.BillAccountDetail;
import org.egov.collection.consumer.model.BusinessDetails;
import org.egov.collection.consumer.model.Function;
import org.egov.collection.consumer.model.Functionary;
import org.egov.collection.consumer.model.Fund;
import org.egov.collection.consumer.model.Receipt;
import org.egov.collection.consumer.model.ReceiptRequest;
import org.egov.collection.consumer.model.Voucher;
import org.egov.collection.consumer.model.VoucherRequest;
import org.egov.collection.consumer.model.VoucherResponse;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class VoucherService {

    private static final Logger LOGGER = Logger.getLogger(VoucherService.class);
    final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    public static final String RECEIPTS_VOUCHER_NAME = "Other receipts";
    public static final String RECEIPTS_VOUCHER_TYPE = "Receipt";
    public static final String RECEIPTS_VOUCHER_DESCRIPTION = "Collection Module";;
    public static final String COLLECTIONS_EG_MODULES_ID = "10";
    public static final String RECEIPT_VIEW_SOURCEPATH = "/collection/receipts/receipt-viewReceipts.action?selectedReceipts=";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PropertiesManager propertiesManager;

    @Autowired
    private BusinessDetailsService businessDetailsService;

    public VoucherResponse createVoucher(ReceiptRequest receiptRequest) {

        BusinessDetails servcie = null;

        String tenantId = receiptRequest.getTenantId();

        if (receiptRequest != null && receiptRequest.getReceipt() != null && !receiptRequest.getReceipt().isEmpty()
                && receiptRequest.getReceipt().get(0).getBill() != null
                && !receiptRequest.getReceipt().get(0).getBill().isEmpty()) {

            servcie = businessDetailsService.getBusinessDetailsByCode(
                    receiptRequest.getReceipt().get(0).getBill().get(0).getBillDetails().get(0).getBusinessService(),
                    tenantId);
        }

        Receipt receipt = receiptRequest.getReceipt().get(0);

        final String voucher_create_url = propertiesManager.getErpURLBytenantId(tenantId)
                + propertiesManager.getVoucherCreateUrl();

        LOGGER.info("voucher_create_url:" + voucher_create_url);

        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setAuthToken(tokenService.generateAdminToken(tenantId));

        VoucherRequest voucherRequest = new VoucherRequest();

        Voucher voucher = new Voucher();

        voucher.setTenantId(tenantId);
        voucher.setName(RECEIPTS_VOUCHER_NAME);
        voucher.setType(RECEIPTS_VOUCHER_TYPE);
        voucher.setFund(new Fund());
        voucher.getFund().setCode(servcie != null ? servcie.getFund() : null);
        voucher.setFunction(new Function());
        voucher.getFunction().setCode(servcie != null ? servcie.getFunction() : null);
        voucher.setDepartment(servcie != null ? servcie.getDepartment() : null);
        voucher.setFunctionary(new Functionary());
        voucher.getFunctionary().setCode(servcie != null ? servcie.getFunctionary() : null);
        voucher.setDescription(RECEIPTS_VOUCHER_DESCRIPTION);
        voucher.setVoucherDate(format.format(new Date()));
        voucher.setModuleId(Long.valueOf(COLLECTIONS_EG_MODULES_ID));
        voucher.setSource(RECEIPT_VIEW_SOURCEPATH + receipt.getTransactionId());
        AccountDetail accountDetail = null;
        voucher.setLedgers(new ArrayList<>());

        for (BillAccountDetail bad : receipt.getBill().get(0).getBillDetails().get(0).getBillAccountDetails()) {

            accountDetail = new AccountDetail();
            accountDetail.setGlcode(bad.getGlcode());
            accountDetail.setCreditAmount(bad.getCreditAmount() != null ? bad.getCreditAmount().doubleValue() : 0);
            accountDetail.setDebitAmount(bad.getDebitAmount() != null ? bad.getDebitAmount().doubleValue() : 0);
            accountDetail.setFunction(new Function());
            accountDetail.getFunction().setCode(servcie != null ? servcie.getFunction() : null);
            voucher.getLedgers().add(accountDetail);
        }

        voucherRequest.setVouchers(Collections.singletonList(voucher));
        voucherRequest.setRequestInfo(requestInfo);
        voucherRequest.setTenantId(tenantId);
        LOGGER.info("call:" + voucher_create_url);
        return restTemplate.postForObject(voucher_create_url, voucherRequest, VoucherResponse.class);
    }

}
