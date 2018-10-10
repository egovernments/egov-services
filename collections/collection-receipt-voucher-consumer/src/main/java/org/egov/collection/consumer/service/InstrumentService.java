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

import java.util.Collections;

import org.egov.collection.consumer.config.PropertiesManager;
import org.egov.collection.consumer.model.FinancialStatus;
import org.egov.collection.consumer.model.Instrument;
import org.egov.collection.consumer.model.InstrumentContract;
import org.egov.collection.consumer.model.InstrumentRequest;
import org.egov.collection.consumer.model.InstrumentResponse;
import org.egov.collection.consumer.model.InstrumentVoucherContract;
import org.egov.collection.consumer.model.Receipt;
import org.egov.collection.consumer.model.ReceiptRequest;
import org.egov.collection.consumer.model.VoucherResponse;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class InstrumentService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PropertiesManager propertiesManager;

    @Autowired
    private FinancialStatusService financialStatusService;

    public InstrumentResponse createInstruemtn(ReceiptRequest receiptRequest, VoucherResponse voucherResponse) {

        FinancialStatus status = financialStatusService.getByCode("New", receiptRequest.getTenantId());
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setAuthToken(tokenService.generateAdminToken(receiptRequest.getTenantId()));

        Receipt receipt = receiptRequest.getReceipt().get(0);
        Instrument instrument = receipt.getInstrument();
        InstrumentContract instrumentContract = instrument.toContract();
        instrumentContract.setFinancialStatus(status);
        if (voucherResponse != null) {
            prepareInstrumentVoucher(instrumentContract, voucherResponse, receipt);
        }
        InstrumentRequest request = new InstrumentRequest();

        request.setInstruments(Collections.singletonList(instrumentContract));

        request.setRequestInfo(requestInfo);

        return restTemplate.postForObject(propertiesManager.getInstrumentCreate(), request, InstrumentResponse.class);
    }

    private void prepareInstrumentVoucher(InstrumentContract instrumentContract, VoucherResponse voucherResponse,
            Receipt receipt) {

        InstrumentVoucherContract ivContract = new InstrumentVoucherContract();
        ivContract.setVoucherHeaderId(voucherResponse.getVouchers().get(0).getVoucherNumber());
        ivContract.setReceiptHeaderId(receipt.getBill().get(0).getBillDetails().get(0).getId());
        instrumentContract.setInstrumentVouchers(Collections.singletonList(ivContract));
    }

}
