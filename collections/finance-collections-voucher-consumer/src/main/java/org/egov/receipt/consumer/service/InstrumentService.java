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

package org.egov.receipt.consumer.service;

import java.util.Collections;
import org.egov.receipt.consumer.model.FinancialStatus;
import org.egov.receipt.consumer.model.Instrument;
import org.egov.receipt.consumer.model.InstrumentContract;
import org.egov.receipt.consumer.model.InstrumentRequest;
import org.egov.receipt.consumer.model.InstrumentResponse;
import org.egov.receipt.consumer.model.InstrumentVoucherContract;
import org.egov.receipt.consumer.model.Receipt;
import org.egov.receipt.consumer.model.ReceiptReq;
import org.egov.receipt.consumer.model.RequestInfo;
import org.egov.receipt.consumer.model.VoucherResponse;
import org.egov.receipt.consumer.repository.ServiceRequestRepository;
import org.egov.receipt.custom.exception.VoucherCustomException;
import org.egov.reciept.consumer.config.PropertiesManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class InstrumentService {

	public static final Logger LOGGER = LoggerFactory.getLogger(InstrumentService.class);

	private static final String FINANCE_STATUS_NEW = "New";

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	private FinancialStatusService financialStatusService;
	
	@Autowired
    private ServiceRequestRepository serviceRequestRepository;
    
    @Autowired
	private ObjectMapper mapper;

	/**
	 * 
	 * @param receiptRequest
	 * @param voucherResponse
	 * @return
	 * Function is used to create the instrument for created voucher.
	 */
	public InstrumentResponse createInstrument(ReceiptReq receiptRequest, VoucherResponse voucherResponse) {
		
		try {
			Receipt receipt = receiptRequest.getReceipt().get(0);
			FinancialStatus status = financialStatusService.getByCode(FINANCE_STATUS_NEW, receipt.getTenantId(), receiptRequest.getRequestInfo());
			Instrument instrument = receipt.getInstrument();
			InstrumentContract instrumentContract = instrument.toContract();
			instrumentContract.setFinancialStatus(status);
			if (voucherResponse != null) {
				prepareInstrumentVoucher(instrumentContract, voucherResponse, receipt);
			}
			StringBuilder url = new StringBuilder(propertiesManager.getInstrumentHostUrl() + propertiesManager.getInstrumentCreate());
			InstrumentRequest request = new InstrumentRequest();
			request.setInstruments(Collections.singletonList(instrumentContract));
			request.setRequestInfo(receiptRequest.getRequestInfo());
			return mapper.convertValue(serviceRequestRepository.fetchResult(url, request, receipt.getTenantId()), InstrumentResponse.class);
		} catch (Exception e) {
			LOGGER.error("ERROR occured while creating instrument "+e.getStackTrace());
		}
		return null;
	}

	private void prepareInstrumentVoucher(InstrumentContract instrumentContract, VoucherResponse voucherResponse,
			Receipt receipt) {

		InstrumentVoucherContract ivContract = new InstrumentVoucherContract();
		ivContract.setVoucherHeaderId(voucherResponse.getVouchers().get(0).getVoucherNumber());
		ivContract.setReceiptHeaderId(receipt.getReceiptNumber());
		instrumentContract.setInstrumentVouchers(Collections.singletonList(ivContract));
	}

	/**
	 * 
	 * @param receipt
	 * Function is used to cancel the instruments
	 * @throws VoucherCustomException 
	 */
	public void cancelInstrument(Receipt receipt, RequestInfo requestInfo) throws VoucherCustomException {
		StringBuilder url = new StringBuilder(propertiesManager.getInstrumentHostUrl() + propertiesManager.getInstrumentCancel());
		InstrumentRequest request = new InstrumentRequest();
		this.setInstrumentRequest(request, receipt, requestInfo);
		serviceRequestRepository.fetchResult(url, request, receipt.getTenantId());
	}

	private void setInstrumentRequest(InstrumentRequest request, Receipt receipt, RequestInfo requestInfo) {
		Instrument instrument = receipt.getInstrument();
		InstrumentContract instrumentContract = instrument.toContract();
		request.setInstruments(Collections.singletonList(instrumentContract));
		request.setRequestInfo(requestInfo);
	}

}
