package org.egov.inv.domain.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.Constants;
import org.egov.common.DomainService;
import org.egov.common.MdmsRepository;
import org.egov.common.Pagination;
import org.egov.common.exception.CustomBindException;
import org.egov.common.exception.ErrorCode;
import org.egov.common.exception.InvalidDataException;
import org.egov.inv.domain.util.InventoryUtilities;
import org.egov.inv.model.Disposal;
import org.egov.inv.model.Disposal.DisposalStatusEnum;
import org.egov.inv.model.DisposalDetail;
import org.egov.inv.model.DisposalRequest;
import org.egov.inv.model.DisposalResponse;
import org.egov.inv.model.DisposalSearchContract;
import org.egov.inv.model.Material;
import org.egov.inv.model.RequestInfo;
import org.egov.inv.model.Scrap;
import org.egov.inv.model.ScrapDetail;
import org.egov.inv.model.ScrapResponse;
import org.egov.inv.model.ScrapSearch;
import org.egov.inv.model.Store;
import org.egov.inv.model.StoreGetRequest;
import org.egov.inv.model.StoreResponse;
import org.egov.inv.model.Uom;
import org.egov.inv.persistence.entity.DisposalDetailEntity;
import org.egov.inv.persistence.entity.ScrapDetailEntity;
import org.egov.inv.persistence.repository.DisposalDetailJdbcRepository;
import org.egov.inv.persistence.repository.DisposalJdbcRepository;
import org.egov.inv.persistence.repository.ScrapDetailJdbcRepository;
import org.egov.inv.persistence.repository.StoreJdbcRepository;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.JSONArray;

@Service
public class DisposalService extends DomainService {

	@Autowired
	private StoreJdbcRepository storeJdbcRepository;

	@Autowired
	private DisposalJdbcRepository disposalJdbcRepository;

	@Autowired
	private DisposalDetailJdbcRepository disposalDetailJdbcRepository;

	@Autowired
	private ScrapDetailJdbcRepository scrapDetailJdbcRepository;

	@Autowired
	private ScrapService scrapService;

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	@Autowired
	private MdmsRepository mdmsRepository;

	@Value("${inv.disposal.create.key}")
	private String createKey;

	@Value("${inv.disposal.create.topic}")
	private String createTopic;

	@Value("${inv.disposal.update.key}")
	private String updateKey;

	@Value("${inv.disposal.update.topic}")
	private String updateTopic;

	@Autowired
	private StoreService storeService;

	public DisposalResponse create(DisposalRequest disposalRequest, String tenantId) {
		try {
			List<Disposal> disposals = new ArrayList<>();
			if (!disposalRequest.getDisposals().isEmpty()) {
				disposals = disposalRequest.getDisposals();
				fetchRelated(disposals, tenantId, Constants.ACTION_CREATE.toString());
				validate(disposals, Constants.ACTION_CREATE);
				List<String> disposalIds = disposalJdbcRepository.getSequence(Disposal.class.getSimpleName(),
						disposals.size());
				int i = 0;
				for (Disposal disposal : disposals) {
					BigDecimal totalDisposalValue = BigDecimal.ZERO;
					disposal.setId(disposalIds.get(i));
					setDisposalData(Constants.ACTION_CREATE, disposal);
					disposal.setAuditDetails(mapAuditDetails(disposalRequest.getRequestInfo()));
					if (!disposal.getDisposalDetails().isEmpty()) {
						List<String> disposalDetailIds = disposalDetailJdbcRepository.getSequence(
								DisposalDetail.class.getSimpleName(), disposal.getDisposalDetails().size());
						int j = 0;
						for (DisposalDetail disposalDetail : disposal.getDisposalDetails()) {
							disposalDetail.setId(disposalDetailIds.get(j));
							disposalDetail.setTenantId(tenantId);
							totalDisposalValue = totalDisposalValue.add(disposalDetail.getDisposalValue());
							backUpdateScrap(disposalDetail, tenantId);
							j++;
						}
					}
					disposal.setTotalDisposalValue(totalDisposalValue);
					i++;
				}
			}
			kafkaTemplate.send(createTopic, createKey, disposalRequest);
			DisposalResponse disResponse = new DisposalResponse();
			disResponse.setDisposals(disposals);
			disResponse.setResponseInfo(getResponseInfo(disposalRequest.getRequestInfo()));
			return disResponse;
		} catch (CustomBindException e) {
			throw e;
		}
	}

	private void backUpdateScrap(DisposalDetail disposalDetail, String tenantId) {
		HashMap<String, String> hashMap = new HashMap<>();
		hashMap.put("disposalquantity", "disposalquantity +" + disposalDetail.getDisposalQuantity().toString());
		disposalDetail.getScrapDetails().setTenantId(tenantId);
		disposalJdbcRepository.updateColumn(new ScrapDetailEntity().toEntity(disposalDetail.getScrapDetails()),
				"scrapdetail", hashMap, null);
	}

	private void setDisposalData(String action, Disposal disposal) {
		if (action.equals(Constants.ACTION_CREATE)) {
			int year = Calendar.getInstance().get(Calendar.YEAR);
			disposal.setDisposalNumber("DISP-" + String.valueOf(year) + "-" + disposal.getId());
			disposal.setDisposalStatus(DisposalStatusEnum.CREATED);
		}
	}

	private void validate(List<Disposal> disposals, String action) {
		InvalidDataException errors = new InvalidDataException();
		try {
			switch (action) {
			case "create":
				if (disposals == null) {
					errors.addDataError(ErrorCode.NOT_NULL.getCode(), "materialIssues", "null");
				}
				for (Disposal disposal : disposals) {
					int i = 0;
					if (!disposal.getDisposalDetails().isEmpty())
						for (DisposalDetail disposalDetail : disposal.getDisposalDetails()) {
							if (disposalDetail.getUserDisposalQuantity() != null && disposalDetail.getUom() != null) {
								if (disposalDetail.getUom().getConversionFactor() != null)
									disposalDetail.setDisposalQuantity(InventoryUtilities.getQuantityInBaseUom(
											disposalDetail.getUserDisposalQuantity(),
											disposalDetail.getUom().getConversionFactor()));
							}
							if (disposalDetail.getDisposalQuantity().compareTo(BigDecimal.ZERO) <= 0)
								errors.addDataError(ErrorCode.QUANTITY_GT_ZERO.getCode(), "disposalquantity",
										String.valueOf(i), disposalDetail.getDisposalQuantity().toString());
							if (disposalDetail.getScrapDetails() != null)
								if (disposalDetail.getScrapDetails().getQuantity() != null
										&& disposalDetail.getScrapDetails().getDisposalQuantity() != null)
									disposalDetail
											.setPendingScrapQuantity(disposalDetail.getScrapDetails().getQuantity()
													.subtract(disposalDetail.getScrapDetails().getDisposalQuantity()));
							if (disposalDetail.getDisposalQuantity()
									.compareTo(disposalDetail.getPendingScrapQuantity()) > 0)
								errors.addDataError(ErrorCode.QUANTITY1_LTE_QUANTITY2.getCode(), "disposalquantity",
										"scrapquantity", disposalDetail.getDisposalQuantity().toString(),
										disposalDetail.getPendingScrapQuantity().toString(), String.valueOf(i));
							if (disposalDetail.getDisposalValue().compareTo(BigDecimal.ZERO) <= 0)
								errors.addDataError(ErrorCode.QUANTITY_GT_ZERO.getCode(), "disposalvalue",
										String.valueOf(i), disposalDetail.getDisposalValue().toString());
						}
				}
				break;
			case "update":
				for (Disposal disposal : disposals) {
					if (StringUtils.isEmpty(disposal.getDisposalNumber()))
						errors.addDataError(ErrorCode.NOT_NULL.getCode(), "disposalnumber", "null");
					int i = 0;
					if (!disposal.getDisposalDetails().isEmpty())
						for (DisposalDetail disposalDetail : disposal.getDisposalDetails()) {
							if (disposalDetail.getUserDisposalQuantity() != null && disposalDetail.getUom() != null) {
								if (disposalDetail.getUom().getConversionFactor() != null)
									disposalDetail.setDisposalQuantity(InventoryUtilities.getQuantityInBaseUom(
											disposalDetail.getUserDisposalQuantity(),
											disposalDetail.getUom().getConversionFactor()));
							}
							if (disposalDetail.getDisposalQuantity().compareTo(BigDecimal.ZERO) <= 0)
								errors.addDataError(ErrorCode.QUANTITY_GT_ZERO.getCode(), "disposalquantity",
										String.valueOf(i), disposalDetail.getDisposalQuantity().toString());
							DisposalDetailEntity disposalDetailEntity = new DisposalDetailEntity();
							disposalDetailEntity.setId(disposalDetail.getId());
							disposalDetailEntity.setTenantId(disposal.getTenantId());
							DisposalDetailEntity disposalDetEntity = disposalDetailJdbcRepository
									.findById(disposalDetailEntity, "DisposalDetailEntity");
							DisposalDetail disDetail = new DisposalDetail();
							if (disposalDetEntity != null)
								disDetail = disposalDetEntity.toDomain();
							if (disposalDetail.getScrapDetails() != null)
								if (disposalDetail.getScrapDetails().getQuantity() != null
										&& disposalDetail.getScrapDetails().getDisposalQuantity() != null)
									disposalDetail
											.setPendingScrapQuantity(
													(disposalDetail.getScrapDetails().getQuantity()
															.subtract(disposalDetail.getScrapDetails()
																	.getDisposalQuantity()))
																			.add(disDetail.getDisposalQuantity()));
							if (disposalDetail.getDisposalQuantity()
									.compareTo(disposalDetail.getPendingScrapQuantity()) > 0)
								errors.addDataError(ErrorCode.QUANTITY1_LTE_QUANTITY2.getCode(), "disposalquantity",
										"scrapquantity", disposalDetail.getDisposalQuantity().toString(),
										disposalDetail.getPendingScrapQuantity().toString(), String.valueOf(i));
							if (disposalDetail.getDisposalValue().compareTo(BigDecimal.ZERO) <= 0)
								errors.addDataError(ErrorCode.QUANTITY_GT_ZERO.getCode(), "disposalvalue",
										String.valueOf(i), disposalDetail.getDisposalValue().toString());
						}

				}
				break;

			}
		} catch (IllegalArgumentException e) {
		}
		if (errors.getValidationErrors().size() > 0)
			throw errors;
	}

	private void fetchRelated(List<Disposal> disposals, String tenantId, String action) {
		for (Disposal disposal : disposals) {
			if (disposal.getStore() != null)
				if (disposal.getStore().getCode() != null) {
					List<Store> stores = new ArrayList<>();
					StoreGetRequest storeGetReq = new StoreGetRequest();
					storeGetReq.setCode(Arrays.asList(disposal.getStore().getCode()));
					storeGetReq.setTenantId(tenantId);
					Pagination<Store> pageStore = storeJdbcRepository.search(storeGetReq);
					if (pageStore != null)
						stores = pageStore.getPagedData();
					if (stores.isEmpty())
						throw new CustomException("invalid.ref.value",
								"the field store should have a valid value which exists in the system.");
					else
						disposal.setStore(stores.get(0));
				}
			if (!disposal.getDisposalDetails().isEmpty()) {
				ObjectMapper mapper = new ObjectMapper();
				Map<String, Material> materialMap = getMaterials(disposal.getTenantId(), mapper, new RequestInfo());
				Map<String, Uom> uomMap = getUoms(disposal.getTenantId(), mapper, new RequestInfo());
				for (DisposalDetail disposalDetail : disposal.getDisposalDetails()) {
					if (action.equals(Constants.ACTION_CREATE)) {
						if (disposalDetail.getScrapDetails() != null)
							if (disposalDetail.getScrapDetails().getMaterial() != null
									&& disposalDetail.getScrapDetails().getMaterial().getCode() != null) {
								if (materialMap.get(disposalDetail.getScrapDetails().getMaterial().getCode()) == null)
									throw new CustomException("invalid.ref.value",
											"the field material should have a valid value which exists in the system.");
								else
									disposalDetail.setMaterial(
											materialMap.get(disposalDetail.getScrapDetails().getMaterial().getCode()));
							}
					} else {
						if (disposalDetail.getMaterial() != null && disposalDetail.getMaterial().getCode() != null) {
							if (materialMap.get(disposalDetail.getMaterial().getCode()) == null)
								throw new CustomException("invalid.ref.value",
										"the field material should have a valid value which exists in the system.");
							else
								disposalDetail.setMaterial(materialMap.get(disposalDetail.getMaterial().getCode()));

						}
					}
					if (action.equals(Constants.ACTION_CREATE)) {
						if (disposalDetail.getScrapDetails() != null)
							if (disposalDetail.getScrapDetails().getUom() != null
									&& disposalDetail.getScrapDetails().getUom().getCode() != null) {
								if (uomMap.get(disposalDetail.getScrapDetails().getUom().getCode()) == null)
									throw new CustomException("invalid.ref.value",
											"the field uom should have a valid value which exists in the system.");
								else
									disposalDetail
											.setUom(uomMap.get(disposalDetail.getScrapDetails().getUom().getCode()));
							}
					} else {
						if (disposalDetail.getUom() != null && disposalDetail.getUom().getCode() != null) {
							if (uomMap.get(disposalDetail.getUom().getCode()) == null)
								throw new CustomException("invalid.ref.value",
										"the field uom should have a valid value which exists in the system.");
							else
								disposalDetail.setUom(uomMap.get(disposalDetail.getUom().getCode()));
						}
					}
					if (disposalDetail.getScrapDetails() != null && disposalDetail.getScrapDetails().getId() != null) {
						ScrapDetailEntity entity = new ScrapDetailEntity();
						entity.setId(disposalDetail.getScrapDetails().getId());
						entity.setTenantId(disposal.getTenantId());

						disposalDetail.setScrapDetails(scrapDetailJdbcRepository.findById(entity) != null
								? scrapDetailJdbcRepository.findById(entity).toDomain() : null);
					}
				}
			}
		}
	}

	public DisposalResponse update(DisposalRequest disposalRequest, String tenantId) {
		try {
			if (!disposalRequest.getDisposals().isEmpty()) {
				List<Disposal> disposals = disposalRequest.getDisposals();
				fetchRelated(disposals, tenantId, Constants.ACTION_UPDATE.toString());
				validate(disposals, Constants.ACTION_UPDATE);
				int i = 0;
				for (Disposal disposal : disposals) {
					if (disposal.getTenantId() != null)
						disposal.setTenantId(tenantId);
					DisposalSearchContract disposalSearchContract = new DisposalSearchContract();
					disposalSearchContract.setDisposalNumber(disposal.getDisposalNumber());
					disposalSearchContract.setTenantId(tenantId);
					DisposalResponse disposalResponse = search(disposalSearchContract);
					if (disposal.getDisposalStatus().toString().equals(DisposalStatusEnum.CANCELED.toString())) {
						backUpdateScrapMinus(disposalResponse, tenantId);
						Disposal dpsl = new Disposal();
						if (disposalResponse != null && disposalResponse.getDisposals() != null)
							dpsl = disposalResponse.getDisposals().get(0);
						dpsl.setDisposalStatus(DisposalStatusEnum.CANCELED);
						disposals.set(i, dpsl);
						i++;
						continue;
					}
					disposal.setAuditDetails(mapAuditDetailsForUpdate(disposalRequest.getRequestInfo()));
					List<String> listOfDisposalDetails = new ArrayList<>();
					BigDecimal totalDisposalValue = BigDecimal.ZERO;
					for (DisposalDetail disposalDetail : disposal.getDisposalDetails()) {
						listOfDisposalDetails.add(disposalDetail.getId());
						backUpdateScrapForUpdationCase(disposalResponse, disposalDetail, tenantId);
						totalDisposalValue = totalDisposalValue.add(disposalDetail.getDisposalValue());
					}
					disposal.setTotalDisposalValue(totalDisposalValue);
					backUpdateScrapForDeletionCase(disposalResponse, listOfDisposalDetails, tenantId);
					disposalDetailJdbcRepository.markDeleted(listOfDisposalDetails, tenantId, "disposaldetail",
							"disposalnumber", disposal.getDisposalNumber());
					i++;
				}
			}
			kafkaTemplate.send(updateTopic, updateKey, disposalRequest);
			DisposalResponse disResponse = new DisposalResponse();
			disResponse.setDisposals(disposalRequest.getDisposals());
			disResponse.setResponseInfo(getResponseInfo(disposalRequest.getRequestInfo()));
			return disResponse;
		} catch (CustomBindException e) {
			throw e;
		}
	}

	private void backUpdateScrapForDeletionCase(DisposalResponse disposalResponse, List<String> listOfDisposalDetails,
			String tenantId) {
		Disposal disposal = new Disposal();
		if (disposalResponse != null) {
			disposal = disposalResponse.getDisposals().get(0);
			List<DisposalDetail> disDetails = new ArrayList<>();
			for (DisposalDetail disposalDet : disposal.getDisposalDetails()) {
				int flag = 0;
				for (String dd : listOfDisposalDetails) {
					if (disposalDet.getId().equals(dd))
						flag = 1;
					if (flag == 1)
						break;
				}
				if (flag == 0)
					disDetails.add(disposalDet);

			}
			for (DisposalDetail disDet : disDetails) {
				backUpdateScrapSub(disDet, tenantId);
			}
		}
	}

	private void backUpdateScrapForUpdationCase(DisposalResponse disposalResponse, DisposalDetail disposalDetail,
			String tenantId) {
		Disposal disposal = new Disposal();
		if (disposalResponse != null) {
			disposal = disposalResponse.getDisposals().get(0);
			for (DisposalDetail disposalDet : disposal.getDisposalDetails()) {
				if (disposalDet.getId().equals(disposalDetail.getId()))
					backUpdateScrapSub(disposalDet, tenantId);
			}
		}
		backUpdateScrap(disposalDetail, tenantId);
	}

	private void backUpdateScrapSub(DisposalDetail disposalDet, String tenantId) {
		HashMap<String, String> hashMap = new HashMap<>();
		hashMap.put("disposalquantity", "disposalquantity -" + disposalDet.getDisposalQuantity().toString());
		disposalDet.getScrapDetails().setTenantId(tenantId);
		disposalJdbcRepository.updateColumn(new ScrapDetailEntity().toEntity(disposalDet.getScrapDetails()),
				"scrapdetail", hashMap, null);
	}

	private void backUpdateScrapMinus(DisposalResponse disposalResponse, String tenantId) {
		if (!disposalResponse.getDisposals().get(0).getDisposalDetails().isEmpty()) {
			for (DisposalDetail disposalDetail : disposalResponse.getDisposals().get(0).getDisposalDetails()) {
				HashMap<String, String> hashMap = new HashMap<>();
				hashMap.put("disposalquantity", "disposalquantity -" + disposalDetail.getDisposalQuantity().toString());
				disposalDetail.getScrapDetails().setTenantId(tenantId);
				disposalJdbcRepository.updateColumn(new ScrapDetailEntity().toEntity(disposalDetail.getScrapDetails()),
						"scrapdetail", hashMap, null);
			}
		}

	}

	private Map<String, Uom> getUoms(String tenantId, final ObjectMapper mapper, RequestInfo requestInfo) {
		JSONArray responseJSONArray = mdmsRepository.getByCriteria(tenantId, "common-masters", "Uom", null, null,
				requestInfo);
		Map<String, Uom> uomMap = new HashMap<>();

		if (responseJSONArray != null && responseJSONArray.size() > 0) {
			for (int i = 0; i < responseJSONArray.size(); i++) {
				Uom uom = mapper.convertValue(responseJSONArray.get(i), Uom.class);
				uomMap.put(uom.getCode(), uom);
			}
		}
		return uomMap;
	}

	private Map<String, Material> getMaterials(String tenantId, final ObjectMapper mapper, RequestInfo requestInfo) {
		JSONArray responseJSONArray = mdmsRepository.getByCriteria(tenantId, "inventory", "Material", null, null,
				requestInfo);
		Map<String, Material> materialMap = new HashMap<>();

		if (responseJSONArray != null && responseJSONArray.size() > 0) {
			for (int i = 0; i < responseJSONArray.size(); i++) {
				Material material = mapper.convertValue(responseJSONArray.get(i), Material.class);
				materialMap.put(material.getCode(), material);
			}
		}
		return materialMap;
	}

	public DisposalResponse search(DisposalSearchContract disposalSearchContract) {
		Pagination<Disposal> pageDisposal = disposalJdbcRepository.search(disposalSearchContract);
		List<Disposal> listOfDisposals = new ArrayList<>();
		if (pageDisposal != null)
			listOfDisposals = pageDisposal.getPagedData();
		if (!listOfDisposals.isEmpty())
			for (Disposal disposal : listOfDisposals) {
				Pagination<DisposalDetail> pageDisposalDetail = disposalDetailJdbcRepository
						.search(disposal.getDisposalNumber(), disposal.getTenantId());
				List<DisposalDetail> listOfDisposalDetails = new ArrayList<>();
				if (pageDisposalDetail != null)
					listOfDisposalDetails = pageDisposalDetail.getPagedData();
				disposal.setDisposalDetails(listOfDisposalDetails);
			}
		DisposalResponse disposalResponse = new DisposalResponse();
		disposalResponse.setDisposals(listOfDisposals);
		return disposalResponse;
	}

	public DisposalResponse prepareDisposalFromScrap(final DisposalRequest disposalRequest, String tenantId) {
		if (!disposalRequest.getDisposals().isEmpty()) {
			for (Disposal disposal : disposalRequest.getDisposals()) {
				ObjectMapper objectMapper = new ObjectMapper();
				Map<String, Material> materialMap = getMaterials(tenantId, objectMapper, new RequestInfo());
				Map<String, Uom> uomMap = getUoms(tenantId, objectMapper, new RequestInfo());
				if (disposal.getScrapNumbers() != null) {
					List<String> scrapNumbers = disposal.getScrapNumbers();
					ScrapSearch scrapSearch = new ScrapSearch();
					scrapSearch.setScrapNumber(scrapNumbers);
					scrapSearch.setTenantId(tenantId);
					ScrapResponse scrapResponse = scrapService.search(scrapSearch);
					List<Scrap> scraps = new ArrayList<>();
					if (scrapResponse != null) {
						if (!scrapResponse.getScraps().isEmpty()) {
							scraps = scrapResponse.getScraps();
							Store store = scraps.get(0).getStore();
							StoreGetRequest storeGetRequest = new StoreGetRequest();
							storeGetRequest.setCode(Arrays.asList(store.getCode()));
							storeGetRequest.setTenantId(tenantId);
							StoreResponse storeResponse = storeService.search(storeGetRequest);
							if (storeResponse != null)
								disposal.setStore(storeResponse.getStores().get(0));
							for (Scrap scrap : scrapResponse.getScraps()) {
								for (ScrapDetail scrapDetail : scrap.getScrapDetails()) {
									for (DisposalDetail disposalDetail : disposal.getDisposalDetails()) {
										if (scrapDetail.getScrapNumber()
												.equals(disposalDetail.getScrapDetails().getScrapNumber())) {
											if (scrapDetail.getMaterial().getCode() != null)
												disposalDetail.setMaterial(
														materialMap.get(scrapDetail.getMaterial().getCode()));
											if (disposalDetail.getScrapDetails() != null)
												disposalDetail.getScrapDetails().setMaterial(
														materialMap.get(scrapDetail.getMaterial().getCode()));
											if (scrapDetail.getUom().getCode() != null)
												disposalDetail.setUom(uomMap.get(scrapDetail.getUom().getCode()));
											if (disposalDetail.getScrapDetails() != null)
												disposalDetail.getScrapDetails()
														.setUom(uomMap.get(scrapDetail.getUom().getCode()));
											disposalDetail.setPendingScrapQuantity(InventoryUtilities
													.getQuantityInSelectedUom(scrapDetail.getQuantity()
												    .subtract(scrapDetail.getDisposalQuantity()),
													uomMap.get(scrapDetail.getUom().getCode()).getConversionFactor()));
										}
									}
								}
							}
						}
					}
				}
			}
		}
		DisposalResponse disposalResponse = new DisposalResponse();
		disposalResponse.setDisposals(disposalRequest.getDisposals());
		return disposalResponse;
	}

}
