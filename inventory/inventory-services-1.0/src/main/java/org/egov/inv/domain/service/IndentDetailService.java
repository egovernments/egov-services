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
import org.egov.egf.inv.domain.model.IndentDetail;
import org.egov.egf.inv.domain.model.IndentDetailSearch;
import org.egov.egf.inv.domain.repository.IndentDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

@Service
@Transactional(readOnly = true)
public class IndentDetailService {

	@Autowired
	private IndentDetailRepository indentDetailRepository;

	@Autowired
	private SmartValidator validator;
	@Autowired
	private ProjectCodeRepository projectCodeRepository;
	@Autowired
	private MaterialRepository materialRepository;
	@Autowired
	private AssetRepository assetRepository;
	@Autowired
	private UomRepository uomRepository;

	@Transactional
	public List<IndentDetail> create(List<IndentDetail> indentdetails, BindingResult errors, RequestInfo requestInfo) {

		try {

			indentdetails = fetchRelated(indentdetails);
			validate(indentdetails, Constants.ACTION_CREATE, errors);

			if (errors.hasErrors()) {
				throw new CustomBindException(errors);
			}
			for (IndentDetail b : indentdetails) {
				b.setId(indentDetailRepository.getNextSequence());
				b.add();
			}

		} catch (CustomBindException e) {
			throw e;
		}

		return indentDetailRepository.save(indentdetails, requestInfo);

	}

	@Transactional
	public List<IndentDetail> update(List<IndentDetail> indentdetails, BindingResult errors, RequestInfo requestInfo) {

		try {

			indentdetails = fetchRelated(indentdetails);

			validate(indentdetails, Constants.ACTION_UPDATE, errors);

			if (errors.hasErrors()) {
				throw new CustomBindException(errors);
			}
			for (IndentDetail b : indentdetails) {
				b.update();
			}

		} catch (CustomBindException e) {

			throw new CustomBindException(errors);
		}

		return indentDetailRepository.update(indentdetails, requestInfo);

	}

	private BindingResult validate(List<IndentDetail> indentdetails, String method, BindingResult errors) {

		try {
			switch (method) {
			case ACTION_VIEW:
				// validator.validate(indentDetailContractRequest.getIndentDetail(),
				// errors);
				break;
			case Constants.ACTION_CREATE:
				if (indentdetails == null) {
					throw new InvalidDataException("indentdetails", ErrorCode.NOT_NULL.getCode(), null);
				}
				for (IndentDetail indentDetail : indentdetails) {
					validator.validate(indentDetail, errors);
					if (!indentDetailRepository.uniqueCheck("name", indentDetail)) {
						errors.addError(new FieldError("indentDetail", "name", indentDetail.getName(), false,
								new String[] { ErrorCode.NON_UNIQUE_VALUE.getCode() }, null, null));
					}
					if (!indentDetailRepository.uniqueCheck("code", indentDetail)) {
						errors.addError(new FieldError("indentDetail", "code", indentDetail.getName(), false,
								new String[] { ErrorCode.NON_UNIQUE_VALUE.getCode() }, null, null));
					}
					if (!indentDetailRepository.uniqueCheck("identifier", indentDetail)) {
						errors.addError(new FieldError("indentDetail", "identifier", indentDetail.getName(), false,
								new String[] { ErrorCode.NON_UNIQUE_VALUE.getCode() }, null, null));
					}

				}
				break;
			case Constants.ACTION_UPDATE:
				if (indentdetails == null) {
					throw new InvalidDataException("indentdetails", ErrorCode.NOT_NULL.getCode(), null);
				}
				for (IndentDetail indentDetail : indentdetails) {
					if (indentDetail.getId() == null) {
						throw new InvalidDataException("id", ErrorCode.MANDATORY_VALUE_MISSING.getCode(),
								indentDetail.getId());
					}
					validator.validate(indentDetail, errors);
					if (!indentDetailRepository.uniqueCheck("name", indentDetail)) {
						errors.addError(new FieldError("indentDetail", "name", indentDetail.getName(), false,
								new String[] { ErrorCode.NON_UNIQUE_VALUE.getCode() }, null, null));
					}
					if (!indentDetailRepository.uniqueCheck("code", indentDetail)) {
						errors.addError(new FieldError("indentDetail", "code", indentDetail.getName(), false,
								new String[] { ErrorCode.NON_UNIQUE_VALUE.getCode() }, null, null));
					}
					if (!indentDetailRepository.uniqueCheck("identifier", indentDetail)) {
						errors.addError(new FieldError("indentDetail", "identifier", indentDetail.getName(), false,
								new String[] { ErrorCode.NON_UNIQUE_VALUE.getCode() }, null, null));
					}

				}
				break;
			case Constants.ACTION_SEARCH:
				if (indentdetails == null) {
					throw new InvalidDataException("indentdetails", ErrorCode.NOT_NULL.getCode(), null);
				}
				for (IndentDetail indentDetail : indentdetails) {
					if (indentDetail.getTenantId() == null) {
						throw new InvalidDataException("tenantId", ErrorCode.MANDATORY_VALUE_MISSING.getCode(),
								indentDetail.getTenantId());
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

	public List<IndentDetail> fetchRelated(List<IndentDetail> indentdetails) {
		for (IndentDetail indentDetail : indentdetails) {
			// fetch related items
			if (indentDetail.getMaterial() != null) {
				Material material = materialRepository.findById(indentDetail.getMaterial());
				if (material == null) {
					throw new InvalidDataException("material", "material.invalid", " Invalid material");
				}
				indentDetail.setMaterial(material);
			}
			if (indentDetail.getUom() != null) {
				Uom uom = uomRepository.findById(indentDetail.getUom());
				if (uom == null) {
					throw new InvalidDataException("uom", "uom.invalid", " Invalid uom");
				}
				indentDetail.setUom(uom);
			}
			if (indentDetail.getProjectCode() != null) {
				ProjectCode projectCode = projectCodeRepository.findById(indentDetail.getProjectCode());
				if (projectCode == null) {
					throw new InvalidDataException("projectCode", "projectCode.invalid", " Invalid projectCode");
				}
				indentDetail.setProjectCode(projectCode);
			}
			if (indentDetail.getAsset() != null) {
				Asset asset = assetRepository.findById(indentDetail.getAsset());
				if (asset == null) {
					throw new InvalidDataException("asset", "asset.invalid", " Invalid asset");
				}
				indentDetail.setAsset(asset);
			}

			indentDetail.setTenantId(ApplicationThreadLocals.getTenantId().get());
			if (indentDetail.getTenantId() != null)
				if (indentDetail.getParent() != null && indentDetail.getParent().getId() != null) {
					indentDetail.getParent().setTenantId(indentDetail.getTenantId());
					IndentDetail parentId = indentDetailRepository.findById(indentDetail.getParent());
					if (parentId == null) {
						throw new InvalidDataException("parentId", ErrorCode.INVALID_REF_VALUE.getCode(),
								indentDetail.getParent().getId());
					}
					indentDetail.setParent(parentId);
				}

		}

		return indentdetails;
	}

	public Pagination<IndentDetail> search(IndentDetailSearch indentDetailSearch, BindingResult errors) {

		try {

			List<IndentDetail> indentdetails = new ArrayList<>();
			indentdetails.add(indentDetailSearch);
			validate(indentdetails, Constants.ACTION_SEARCH, errors);

			if (errors.hasErrors()) {
				throw new CustomBindException(errors);
			}

		} catch (CustomBindException e) {

			throw new CustomBindException(errors);
		}

		return indentDetailRepository.search(indentDetailSearch);
	}

	@Transactional
	public IndentDetail save(IndentDetail indentDetail) {
		return indentDetailRepository.save(indentDetail);
	}

	@Transactional
	public IndentDetail update(IndentDetail indentDetail) {
		return indentDetailRepository.update(indentDetail);
	}

}*/