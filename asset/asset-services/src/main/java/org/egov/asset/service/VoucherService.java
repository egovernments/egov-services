package org.egov.asset.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.RevaluationRequest;
import org.egov.asset.contract.VoucherRequest;
import org.egov.asset.contract.VoucherResponse;
import org.egov.asset.model.Asset;
import org.egov.asset.model.Fund;
import org.egov.asset.model.Voucher;
import org.egov.asset.model.VouchercreateAccountCodeDetails;
import org.egov.asset.model.enums.VoucherType;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class VoucherService {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private ApplicationProperties applicationProperties;

	public VoucherRequest createVoucherRequestForReevalaution(final RevaluationRequest revaluationRequest,
			final Asset asset, final List<VouchercreateAccountCodeDetails> accountCodeDetails) {
		final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

		final Fund fund = new Fund();
		fund.setId(revaluationRequest.getRevaluation().getFund());

		final Voucher voucher = new Voucher();
		voucher.setType(VoucherType.JOURNALVOUCHER);
		voucher.setVoucherDate(sdf.format(new Date()));
		voucher.setLedgers(accountCodeDetails);
		voucher.setDepartment(asset.getDepartment().getId());
		voucher.setName(applicationProperties.getReevaluationVoucherName());
		voucher.setDescription(applicationProperties.getReevaluationVoucherDescription());
		voucher.setFund(fund);

		final List<Voucher> vouchers = new ArrayList<>();
		vouchers.add(voucher);

		final VoucherRequest voucherRequest = new VoucherRequest();
		voucherRequest.setRequestInfo(new RequestInfo());
		voucherRequest.setVouchers(vouchers);
		return voucherRequest;
	}

	public List<Voucher> createVoucher(final VoucherRequest voucherRequest, final String tenantId) {

		// final String createVoucherUrl =
		// "http://localhost:8080/EGF/vouchers/_create?tenantId=" + tenantId;

		final String createVoucherUrl = applicationProperties.getMunicipalityHostName()
				+ applicationProperties.getEgfServiceVoucherCreatePath() + "?tenantId=" + tenantId;
		System.out.println(voucherRequest);
		Error err = new Error();
		try {
			final VoucherResponse voucherResponse = restTemplate.postForObject(createVoucherUrl, voucherRequest,
					VoucherResponse.class);
			return voucherResponse.getVouchers();
		} catch (final HttpClientErrorException e) {

			try {
				err = mapper.readValue(e.getResponseBodyAsString(), Error.class);
			} catch (final IOException e1) {
				e1.printStackTrace();
			}
			throw new RuntimeException("Voucher can not be created because : " + err.getMessage());
		}
	}
}
