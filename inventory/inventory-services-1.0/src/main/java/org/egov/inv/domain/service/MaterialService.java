package org.egov.inv.domain.service;

import java.util.List;

import org.egov.common.DomainService;
import org.egov.common.exception.CustomBindException;
import org.egov.inv.model.Material;
import org.egov.inv.model.MaterialRequest;
import org.egov.inv.persistence.repository.MaterialJdbcRepository;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MaterialService extends DomainService{
	
	
	@Value("${inv.materials.save.topic}")
	private String saveTopic;
	@Value("${inv.materials.save.key}")
	private String saveKey;
	
	@Value("${inv.materials.save.topic}")
	private String updateTopic;
	@Value("${inv.materials.save.key}")
	private String updateKey;

	@Autowired
	private MaterialJdbcRepository  materialRepository;

	@Autowired
	protected LogAwareKafkaTemplate<String, Object> kafkaQue;

	/*@Autowired
	private MaterialTypeRepository materialTypeRepository;
	@Autowired
	private UomRepository uomRepository;
	@Autowired
	private ChartofAccountRepository chartofAccountRepository;
	 */

	@Transactional
	public MaterialRequest create(MaterialRequest materialRequest) {

		try {
			List<Material> materials=materialRequest.getMaterials();
			//materials = fetchRelated(materials);
			//validate(materials, Constants.ACTION_CREATE);

			 
			for (Material b : materials) {
				b.setId(materialRepository.getSequence(b));
				
			}
			
			kafkaQue.send(saveTopic, saveKey, materialRequest);

		} catch (CustomBindException e) {
			throw e;
		}

		
		return materialRequest;

	}

	 

/*	private BindingResult validate(List<Material> materials, String method ) {

		try {
			switch (method) {
			 
			case Constants.ACTION_CREATE:
				if (materials == null) {
					throw new InvalidDataException("materials", ErrorCode.NOT_NULL.getCode(), null);
				}
				for (Material material : materials) {
					if (!materialRepository.uniqueCheck("name", material)) {
						errors.addError(new FieldError("material", "name", material.getName(), false,
								new String[] { ErrorCode.NON_UNIQUE_VALUE.getCode() }, null, null));
					}
					if (!materialRepository.uniqueCheck("code", material)) {
						errors.addError(new FieldError("material", "code", material.getName(), false,
								new String[] { ErrorCode.NON_UNIQUE_VALUE.getCode() }, null, null));
					}
					 

				}
				break;
			case Constants.ACTION_UPDATE:
				if (materials == null) {
					throw new InvalidDataException("materials", ErrorCode.NOT_NULL.getCode(), null);
				}
				for (Material material : materials) {
					if (material.getId() == null) {
						throw new InvalidDataException("id", ErrorCode.MANDATORY_VALUE_MISSING.getCode(),
								material.getId());
					}
					validator.validate(material, errors);
					if (!materialRepository.uniqueCheck("name", material)) {
						errors.addError(new FieldError("material", "name", material.getName(), false,
								new String[] { ErrorCode.NON_UNIQUE_VALUE.getCode() }, null, null));
					}
					if (!materialRepository.uniqueCheck("code", material)) {
						errors.addError(new FieldError("material", "code", material.getName(), false,
								new String[] { ErrorCode.NON_UNIQUE_VALUE.getCode() }, null, null));
					}
					if (!materialRepository.uniqueCheck("identifier", material)) {
						errors.addError(new FieldError("material", "identifier", material.getName(), false,
								new String[] { ErrorCode.NON_UNIQUE_VALUE.getCode() }, null, null));
					}

				}
				break;
			case Constants.ACTION_SEARCH:
				if (materials == null) {
					throw new InvalidDataException("materials", ErrorCode.NOT_NULL.getCode(), null);
				}
				for (Material material : materials) {
					if (material.getTenantId() == null) {
						throw new InvalidDataException("tenantId", ErrorCode.MANDATORY_VALUE_MISSING.getCode(),
								material.getTenantId());
					}
				}
				break;
			default:

			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;
	}

	public List<Material> fetchRelated(List<Material> materials) {
		for (Material material : materials) {
			// fetch related items
			if (material.getMaterialType() != null) {
				MaterialType materialType = materialTypeRepository.findById(material.getMaterialType());
				if (materialType == null) {
					throw new InvalidDataException("materialType", "materialType.invalid", " Invalid materialType");
				}
				material.setMaterialType(materialType);
			}
			if (material.getBaseUom() != null) {
				Uom baseUom = uomRepository.findById(material.getBaseUom());
				if (baseUom == null) {
					throw new InvalidDataException("baseUom", "baseUom.invalid", " Invalid baseUom");
				}
				material.setBaseUom(baseUom);
			}
			if (material.getPurchaseUom() != null) {
				Uom purchaseUom = uomRepository.findById(material.getPurchaseUom());
				if (purchaseUom == null) {
					throw new InvalidDataException("purchaseUom", "purchaseUom.invalid", " Invalid purchaseUom");
				}
				material.setPurchaseUom(purchaseUom);
			}
			if (material.getExpenseAccount() != null) {
				ChartofAccount expenseAccount = chartofAccountRepository.findById(material.getExpenseAccount());
				if (expenseAccount == null) {
					throw new InvalidDataException("expenseAccount", "expenseAccount.invalid",
							" Invalid expenseAccount");
				}
				material.setExpenseAccount(expenseAccount);
			}
			if (material.getStockingUom() != null) {
				Uom stockingUom = uomRepository.findById(material.getStockingUom());
				if (stockingUom == null) {
					throw new InvalidDataException("stockingUom", "stockingUom.invalid", " Invalid stockingUom");
				}
				material.setStockingUom(stockingUom);
			}
			if (material.getAuditDetails() != null) {
				AuditDetails auditDetails = auditDetailsRepository.findById(material.getAuditDetails());
				if (auditDetails == null) {
					throw new InvalidDataException("auditDetails", "auditDetails.invalid", " Invalid auditDetails");
				}
				material.setAuditDetails(auditDetails);
			}

			material.setTenantId(ApplicationThreadLocals.getTenantId().get());
			if (material.getTenantId() != null)
				if (material.getParent() != null && material.getParent().getId() != null) {
					material.getParent().setTenantId(material.getTenantId());
					Material parentId = materialRepository.findById(material.getParent());
					if (parentId == null) {
						throw new InvalidDataException("parentId", ErrorCode.INVALID_REF_VALUE.getCode(),
								material.getParent().getId());
					}
					material.setParent(parentId);
				}

		}

		return materials;
	}

	public Pagination<Material> search(MaterialSearch materialSearch, BindingResult errors) {

		try {

			List<Material> materials = new ArrayList<>();
			materials.add(materialSearch);
			validate(materials, Constants.ACTION_SEARCH, errors);

			if (errors.hasErrors()) {
				throw new CustomBindException(errors);
			}

		} catch (CustomBindException e) {

			throw new CustomBindException(errors);
		}

		return materialRepository.search(materialSearch);
	}

	@Transactional
	public Material save(Material material) {
		return materialRepository.save(material);
	}

	@Transactional
	public Material update(Material material) {
		return materialRepository.update(material);
	}*/

}