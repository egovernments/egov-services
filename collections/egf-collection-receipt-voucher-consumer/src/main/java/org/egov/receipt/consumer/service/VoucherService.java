package org.egov.receipt.consumer.service;

import org.egov.receipt.consumer.model.ReceiptReq;
import org.egov.receipt.consumer.model.VoucherResponse;

public interface VoucherService {
	public VoucherResponse createVoucher(ReceiptReq req) throws Exception;
	public VoucherResponse cancelVoucher(ReceiptReq req) throws Exception;
	public boolean isVoucherCretionEnabled(ReceiptReq req) throws Exception;
	public boolean isVoucherExists(ReceiptReq receiptRequest) throws Exception;
}
