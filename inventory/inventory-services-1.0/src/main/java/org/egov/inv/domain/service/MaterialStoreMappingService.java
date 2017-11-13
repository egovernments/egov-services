/*package org.egov.inv.domain.service;

import static org.egov.common.constants.Constants.ACTION_VIEW;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.constants.Constants;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.exception.ErrorCode;
import org.egov.common.domain.exception.InvalidDataException;
import org.egov.common.domain.model.Pagination;
import org.egov.common.util.ApplicationThreadLocals;
import org.egov.egf.inv.domain.model.MaterialStoreMapping;
import org.egov.egf.inv.domain.model.MaterialStoreMappingSearch;
import org.egov.egf.inv.domain.repository.MaterialStoreMappingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

@Service
@Transactional(readOnly = true)
public class MaterialStoreMappingService {

	@Autowired
	private MaterialStoreMappingRepository materialStoreMappingRepository;

	@Autowired
	private SmartValidator validator;
@Autowired
private StoreRepository storeRepository;
@Autowired
private MaterialRepository materialRepository;
@Autowired
private ChartofAccountRepository chartofAccountRepository;
@Autowired
private AuditDetailsRepository auditDetailsRepository;


	@Transactional
	public List<MaterialStoreMapping> create(List<MaterialStoreMapping> materialstoremappings, BindingResult errors, RequestInfo requestInfo) {

		try {

			materialstoremappings = fetchRelated(materialstoremappings);
			validate(materialstoremappings, Constants.ACTION_CREATE, errors);

			if (errors.hasErrors()) {
				throw new CustomBindException(errors);
			}
			for (MaterialStoreMapping b : materialstoremappings) {
				b.setId(materialStoreMappingRepository.getNextSequence());
				b.add();
			}

		} catch (CustomBindException e) {
			throw e;
		}

		return materialStoreMappingRepository.save(materialstoremappings, requestInfo);

	}

	@Transactional
	public List<MaterialStoreMapping> update(List<MaterialStoreMapping> materialstoremappings, BindingResult errors, RequestInfo requestInfo) {

		try {

			materialstoremappings = fetchRelated(materialstoremappings);

			validate(materialstoremappings, Constants.ACTION_UPDATE, errors);

			if (errors.hasErrors()) {
				throw new CustomBindException(errors);
			}
			for (MaterialStoreMapping b : materialstoremappings) {
				b.update();
			}

		} catch (CustomBindException e) {

			throw new CustomBindException(errors);
		}

		return materialStoreMappingRepository.update(materialstoremappings, requestInfo);

	}

	private BindingResult validate(List<MaterialStoreMapping> materialstoremappings, String method, BindingResult errors) {

                try {
                    switch (method) {
                    case ACTION_VIEW:
                        // validator.validate(materialStoreMappingContractRequest.getMaterialStoreMapping(), errors);
                        break;
                    case Constants.ACTION_CREATE:
                        if (materialstoremappings == null) {
                            throw new InvalidDataException("materialstoremappings", ErrorCode.NOT_NULL.getCode(), null);
                        }
                        for (MaterialStoreMapping materialStoreMapping : materialstoremappings) {
                            validator.validate(materialStoreMapping, errors);
                            if (!materialStoreMappingRepository.uniqueCheck("name", materialStoreMapping)) {
                                errors.addError(new FieldError("materialStoreMapping", "name", materialStoreMapping.getName(), false,
                                        new String[] { ErrorCode.NON_UNIQUE_VALUE.getCode() }, null, null));
                            }
                            if (!materialStoreMappingRepository.uniqueCheck("code", materialStoreMapping)) {
                                errors.addError(new FieldError("materialStoreMapping", "code", materialStoreMapping.getName(), false,
                                        new String[] { ErrorCode.NON_UNIQUE_VALUE.getCode() }, null, null));
                            }
                            if (!materialStoreMappingRepository.uniqueCheck("identifier", materialStoreMapping)) {
                                errors.addError(new FieldError("materialStoreMapping", "identifier", materialStoreMapping.getName(), false,
                                        new String[] { ErrorCode.NON_UNIQUE_VALUE.getCode() }, null, null));
                            }
        
                        }
                        break;
                    case Constants.ACTION_UPDATE:
                        if (materialstoremappings == null) {
                            throw new InvalidDataException("materialstoremappings", ErrorCode.NOT_NULL.getCode(), null);
                        }
                        for (MaterialStoreMapping materialStoreMapping : materialstoremappings) {
                            if (materialStoreMapping.getId() == null) {
                                throw new InvalidDataException("id", ErrorCode.MANDATORY_VALUE_MISSING.getCode(), materialStoreMapping.getId());
                            }
                            validator.validate(materialStoreMapping, errors);
                            if (!materialStoreMappingRepository.uniqueCheck("name", materialStoreMapping)) {
                                errors.addError(new FieldError("materialStoreMapping", "name", materialStoreMapping.getName(), false,
                                        new String[] { ErrorCode.NON_UNIQUE_VALUE.getCode() }, null, null));
                            }
                            if (!materialStoreMappingRepository.uniqueCheck("code", materialStoreMapping)) {
                                errors.addError(new FieldError("materialStoreMapping", "code", materialStoreMapping.getName(), false,
                                        new String[] { ErrorCode.NON_UNIQUE_VALUE.getCode() }, null, null));
                            }
                            if (!materialStoreMappingRepository.uniqueCheck("identifier", materialStoreMapping)) {
                                errors.addError(new FieldError("materialStoreMapping", "identifier", materialStoreMapping.getName(), false,
                                        new String[] { ErrorCode.NON_UNIQUE_VALUE.getCode() }, null, null));
                            }
        
                        }
                        break;
                    case Constants.ACTION_SEARCH:
                        if (materialstoremappings == null) {
                            throw new InvalidDataException("materialstoremappings", ErrorCode.NOT_NULL.getCode(), null);
                        }
                        for (MaterialStoreMapping materialStoreMapping : materialstoremappings) {
                            if (materialStoreMapping.getTenantId() == null) {
                                throw new InvalidDataException("tenantId", ErrorCode.MANDATORY_VALUE_MISSING.getCode(),
                                        materialStoreMapping.getTenantId());
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

	public List<MaterialStoreMapping> fetchRelated(List<MaterialStoreMapping> materialstoremappings) {
		for (MaterialStoreMapping materialStoreMapping : materialstoremappings) {
			// fetch related items
if(materialStoreMapping.getMaterial()!=null)
{
Material  material=materialRepository.findById(materialStoreMapping.getMaterial());
if(material==null)
{
throw new InvalidDataException("material","material.invalid"," Invalid material");
}materialStoreMapping.setMaterial(material);
}
if(materialStoreMapping.getStore()!=null)
{
Store  store=storeRepository.findById(materialStoreMapping.getStore());
if(store==null)
{
throw new InvalidDataException("store","store.invalid"," Invalid store");
}materialStoreMapping.setStore(store);
}
if(materialStoreMapping.getChartofAccount()!=null)
{
ChartofAccount  chartofAccount=chartofAccountRepository.findById(materialStoreMapping.getChartofAccount());
if(chartofAccount==null)
{
throw new InvalidDataException("chartofAccount","chartofAccount.invalid"," Invalid chartofAccount");
}materialStoreMapping.setChartofAccount(chartofAccount);
}
if(materialStoreMapping.getAuditDetails()!=null)
{
AuditDetails  auditDetails=auditDetailsRepository.findById(materialStoreMapping.getAuditDetails());
if(auditDetails==null)
{
throw new InvalidDataException("auditDetails","auditDetails.invalid"," Invalid auditDetails");
}materialStoreMapping.setAuditDetails(auditDetails);
}

            materialStoreMapping.setTenantId(ApplicationThreadLocals.getTenantId().get());
			if (materialStoreMapping.getTenantId() != null)
				if (materialStoreMapping.getParent() != null && materialStoreMapping.getParent().getId() != null) {
					materialStoreMapping.getParent().setTenantId(materialStoreMapping.getTenantId());
					MaterialStoreMapping parentId = materialStoreMappingRepository.findById(materialStoreMapping.getParent());
					if (parentId == null) {
						throw new InvalidDataException("parentId", ErrorCode.INVALID_REF_VALUE.getCode(),
								materialStoreMapping.getParent().getId());
					}
					materialStoreMapping.setParent(parentId);
				}

		}

		return materialstoremappings;
	}

        public Pagination<MaterialStoreMapping> search(MaterialStoreMappingSearch materialStoreMappingSearch, BindingResult errors) {
            
            try {
                
                List<MaterialStoreMapping> materialstoremappings = new ArrayList<>();
                materialstoremappings.add(materialStoreMappingSearch);
                validate(materialstoremappings, Constants.ACTION_SEARCH, errors);
    
                if (errors.hasErrors()) {
                    throw new CustomBindException(errors);
                }
            
            } catch (CustomBindException e) {
    
                throw new CustomBindException(errors);
            }
    
            return materialStoreMappingRepository.search(materialStoreMappingSearch);
        }

	@Transactional
	public MaterialStoreMapping save(MaterialStoreMapping materialStoreMapping) {
		return materialStoreMappingRepository.save(materialStoreMapping);
	}

	@Transactional
	public MaterialStoreMapping update(MaterialStoreMapping materialStoreMapping) {
		return materialStoreMappingRepository.update(materialStoreMapping);
	}

}*/