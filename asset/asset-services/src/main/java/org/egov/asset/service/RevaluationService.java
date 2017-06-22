package org.egov.asset.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.RevaluationRequest;
import org.egov.asset.contract.RevaluationResponse;
import org.egov.asset.contract.VoucherRequest;
import org.egov.asset.model.Asset;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.ChartOfAccountContractResponse;
import org.egov.asset.model.ChartOfAccountDetailContract;
import org.egov.asset.model.ChartOfAccountDetailContractResponse;
import org.egov.asset.model.Function;
import org.egov.asset.model.Revaluation;
import org.egov.asset.model.RevaluationCriteria;
import org.egov.asset.model.Voucher;
import org.egov.asset.model.VouchercreateAccountCodeDetails;
import org.egov.asset.model.enums.TypeOfChangeEnum;
import org.egov.asset.producers.AssetProducer;
import org.egov.asset.repository.RevaluationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RevaluationService {

	@Autowired
	private RevaluationRepository revaluationRepository;

	@Autowired
	private AssetProducer assetProducer;

	@Autowired
	private ApplicationProperties applicationProperties;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private AssetCurrentAmountService assetCurrentAmountService;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private VoucherService voucherService;

	private static final Logger logger = LoggerFactory.getLogger(RevaluationService.class);

	public RevaluationResponse createAsync(final RevaluationRequest revaluationRequest) {

		logger.info("RevaluationService createAsync revaluationRequest:" + revaluationRequest);

		revaluationRequest.getRevaluation()
				.setId(Long.valueOf(revaluationRepository.getNextRevaluationId().longValue()));

		try {
			final List<Voucher> vouchers = createVoucherForReevaluation(revaluationRequest);
			if (vouchers != null && !vouchers.isEmpty())
				revaluationRequest.getRevaluation().setVoucherReference(vouchers.get(0).getId());
		} catch (final Exception e) {
			logger.info("Voucher Generation is failed due to :" + e.getMessage());
		}

		String json = null;

		try {
			json = objectMapper.writeValueAsString(revaluationRequest);
		} catch (final JsonProcessingException ex) {
			logger.info("RevaluationService createAsync catch block:" + ex.getMessage());
		}

		// Send data to kafka for persist into db
		try {
			if (json != null)
				assetProducer.sendMessage(applicationProperties.getCreateAssetRevaluationTopicName(),
						"save-revaluation", json);
		} catch (final Exception ex) {
			logger.info("RevaluationService send kafka createAsync:" + ex.getMessage());
		}

		final List<Revaluation> revaluations = new ArrayList<Revaluation>();
		revaluations.add(revaluationRequest.getRevaluation());
		return getRevaluationResponse(revaluations);
	}

	public void create(final RevaluationRequest revaluationRequest) {
		revaluationRepository.create(revaluationRequest);
	}

	public RevaluationResponse search(final RevaluationCriteria revaluationCriteria) {
		List<Revaluation> revaluations = null;
		try {
			revaluations = revaluationRepository.search(revaluationCriteria);
		} catch (final Exception ex) {
			ex.printStackTrace();
		}
		return getRevaluationResponse(revaluations);
	}

	private List<Voucher> createVoucherForReevaluation(final RevaluationRequest revaluationRequest) {
		final Asset asset = assetCurrentAmountService.getAsset(revaluationRequest.getRevaluation().getAssetId(),
				revaluationRequest.getRevaluation().getTenantId(), revaluationRequest.getRequestInfo());

		final AssetCategory assetCategory = asset.getAssetCategory();

		List<Voucher> vouchers = new ArrayList<Voucher>();
		if (assetCategory != null) {
			if (revaluationRequest.getRevaluation().getTypeOfChange().equals(TypeOfChangeEnum.INCREASED)) {
				final List<ChartOfAccountDetailContract> subledgerDetailsForAssetAccount = getSubledgerDetails(
						revaluationRequest, assetCategory.getAssetAccount());
				final List<ChartOfAccountDetailContract> subledgerDetailsForRevaluationReserverAccount = getSubledgerDetails(
						revaluationRequest, assetCategory.getRevaluationReserveAccount());

				if (subledgerDetailsForAssetAccount != null && subledgerDetailsForRevaluationReserverAccount != null
						&& !subledgerDetailsForAssetAccount.isEmpty()
						&& !subledgerDetailsForRevaluationReserverAccount.isEmpty())
					throw new RuntimeException("Subledger Details Should not be present for Chart Of Accounts");

			} else if (revaluationRequest.getRevaluation().getTypeOfChange().equals(TypeOfChangeEnum.DECREASED)) {
				final List<ChartOfAccountDetailContract> subledgerDetailsForAssetAccount = getSubledgerDetails(
						revaluationRequest, assetCategory.getAssetAccount());
				final List<ChartOfAccountDetailContract> subledgerDetailsForFixedAssetWrittenOffAccount = getSubledgerDetails(
						revaluationRequest, revaluationRequest.getRevaluation().getFixedAssetsWrittenOffAccount());

				if (subledgerDetailsForAssetAccount != null && subledgerDetailsForFixedAssetWrittenOffAccount != null
						&& !subledgerDetailsForAssetAccount.isEmpty()
						&& !subledgerDetailsForFixedAssetWrittenOffAccount.isEmpty())
					throw new RuntimeException("Subledger Details Should not be present for Chart Of Accounts");
			} else {
				final List<VouchercreateAccountCodeDetails> accountCodeDetails = getAccountDetails(revaluationRequest,
						assetCategory);

				final VoucherRequest voucherRequest = voucherService
						.createVoucherRequestForReevalaution(revaluationRequest, asset, accountCodeDetails);

				vouchers = voucherService.createVoucher(voucherRequest,
						revaluationRequest.getRevaluation().getTenantId());
			}
			return vouchers;
		} else
			throw new RuntimeException("Asset Category should be present for asset : " + asset.getName());
	}

	private List<VouchercreateAccountCodeDetails> getAccountDetails(final RevaluationRequest revaluationRequest,
			final AssetCategory assetCategory) {
		final List<VouchercreateAccountCodeDetails> accountCodeDetails = new ArrayList<VouchercreateAccountCodeDetails>();
		if (assetCategory != null
				&& revaluationRequest.getRevaluation().getTypeOfChange().equals(TypeOfChangeEnum.INCREASED)) {
			accountCodeDetails.add(getGlCodes(revaluationRequest, assetCategory.getAssetAccount(), false, true));
			accountCodeDetails
					.add(getGlCodes(revaluationRequest, assetCategory.getRevaluationReserveAccount(), true, false));
		} else if (assetCategory != null
				&& revaluationRequest.getRevaluation().getTypeOfChange().equals(TypeOfChangeEnum.DECREASED)) {
			accountCodeDetails.add(getGlCodes(revaluationRequest,
					revaluationRequest.getRevaluation().getFixedAssetsWrittenOffAccount(), false, true));
			accountCodeDetails.add(getGlCodes(revaluationRequest, assetCategory.getAssetAccount(), true, false));

		}
		return accountCodeDetails;
	}

	private List<ChartOfAccountDetailContract> getSubledgerDetails(final RevaluationRequest revaluationRequest,
			final Long accountId) {
		final String url = applicationProperties.getEgfServiceHostName()
				+ applicationProperties.getEgfServiceChartOfAccountsDetailsSearchPath() + "?tenantId="
				+ revaluationRequest.getRevaluation().getTenantId() + "&id=" + accountId;
		final ChartOfAccountDetailContractResponse coAccountDetailContractResponse = restTemplate.postForObject(url,
				revaluationRequest.getRequestInfo(), ChartOfAccountDetailContractResponse.class);
		return coAccountDetailContractResponse.getChartOfAccountDetails();
	}

	private VouchercreateAccountCodeDetails getGlCodes(final RevaluationRequest revaluationRequest,
			final Long accountId, final Boolean iscredit, final Boolean isDebit) {

		final VouchercreateAccountCodeDetails debitAccountCodeDetail = new VouchercreateAccountCodeDetails();
		ChartOfAccountContractResponse chartOfAccountContractResponse = new ChartOfAccountContractResponse();
		final String url = applicationProperties.getEgfServiceHostName()
				+ applicationProperties.getEgfServiceChartOfAccountsSearchPath() + "?tenantId="
				+ revaluationRequest.getRevaluation().getTenantId() + "&id=" + accountId;
		try {
			chartOfAccountContractResponse = restTemplate.postForObject(url, revaluationRequest.getRequestInfo(),
					ChartOfAccountContractResponse.class);

			debitAccountCodeDetail.setGlcode(chartOfAccountContractResponse.getChartOfAccounts().get(0).getGlcode());

			if (iscredit)
				debitAccountCodeDetail.setCreditAmount(revaluationRequest.getRevaluation().getRevaluationAmount());
			if (isDebit)
				debitAccountCodeDetail.setDebitAmount(revaluationRequest.getRevaluation().getRevaluationAmount());

			final Function function = new Function();
			function.setId(revaluationRequest.getRevaluation().getFunction());
			debitAccountCodeDetail.setFunction(function);

		} catch (final Exception ex) {
			logger.info("RevaluationService getAccountDetails:", ex);
			throw new RuntimeException(ex);
		}

		return debitAccountCodeDetail;
	}

	private RevaluationResponse getRevaluationResponse(final List<Revaluation> revaluations) {
		final RevaluationResponse revaluationResponse = new RevaluationResponse();
		revaluationResponse.setRevaluations(revaluations);
		return revaluationResponse;
	}

}
