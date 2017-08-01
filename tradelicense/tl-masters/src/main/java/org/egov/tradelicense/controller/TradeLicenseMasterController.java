package org.egov.tradelicense.controller;

import javax.validation.Valid;

import org.egov.models.CategoryRequest;
import org.egov.models.CategoryResponse;
import org.egov.models.DocumentTypeRequest;
import org.egov.models.DocumentTypeResponse;
import org.egov.models.FeeMatrixRequest;
import org.egov.models.FeeMatrixResponse;
import org.egov.models.PenaltyRateRequest;
import org.egov.models.PenaltyRateResponse;
import org.egov.models.RequestInfoWrapper;
import org.egov.models.UOMRequest;
import org.egov.models.UOMResponse;
import org.egov.tradelicense.services.CategoryService;
import org.egov.tradelicense.services.DocumentTypeService;
import org.egov.tradelicense.services.FeeMatrixService;
import org.egov.tradelicense.services.PenaltyRateService;
import org.egov.tradelicense.services.UOMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller have the all api's related to trade license master
 * 
 * @author Pavan Kumar Kamma
 *
 */

@RestController
@RequestMapping(path = "/tradelicense")
public class TradeLicenseMasterController {

	@Autowired
	CategoryService categoryService;

	@Autowired
	UOMService uomService;

	@Autowired
	DocumentTypeService documentTypeService;

	@Autowired
	PenaltyRateService penaltyRateService;

	@Autowired
	FeeMatrixService feeMatrixService;

	/**
	 * Description : This api for creating category master
	 * 
	 * @param CategoryRequest
	 * @return CategoryResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/category/_create", method = RequestMethod.POST)
	public CategoryResponse createCategoryMaster(@Valid @RequestBody CategoryRequest categoryRequest) throws Exception {

		return categoryService.createCategoryMaster(categoryRequest);
	}

	/**
	 * Description : This api for updating category master
	 * 
	 * 
	 * @param CategoryRequest
	 * @return CategoryResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/category/_update", method = RequestMethod.POST)
	public CategoryResponse updateCategoryMaster(@Valid @RequestBody CategoryRequest categoryRequest) throws Exception {

		return categoryService.updateCategoryMaster(categoryRequest);
	}

	/**
	 * Description : This api for searching category master
	 * 
	 * @param requestInfo
	 * @param tenantId
	 * @param ids
	 * @param name
	 * @param code
	 * @param type
	 * @param pageSize
	 * @param offSet
	 * @return CategoryResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/category/_search", method = RequestMethod.POST)
	public CategoryResponse getCategoryMaster(@RequestBody RequestInfoWrapper requestInfo,
			@RequestParam(required = true) String tenantId, @RequestParam(required = false) Integer[] ids,
			@RequestParam(required = false) String name, @RequestParam(required = false) String code,
			@RequestParam(required = true) String type, @RequestParam(required = false) Integer pageSize,
			@RequestParam(required = false) Integer offSet) throws Exception {

		return categoryService.getCategoryMaster(requestInfo.getRequestInfo(), tenantId, ids, name, code, type,
				pageSize, offSet);
	}

	/**
	 * Description : This api for creating UOM master
	 * 
	 * @param UOMRequest
	 * @return UOMResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/uom/_create", method = RequestMethod.POST)
	public UOMResponse createUomMaster(@Valid @RequestBody UOMRequest uomRequest) throws Exception {

		return uomService.createUomMaster(uomRequest);
	}

	/**
	 * Description : This api for updating UOM master
	 * 
	 * 
	 * @param UOMRequest
	 * @return UOMResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/uom/_update", method = RequestMethod.POST)
	public UOMResponse updateUomMaster(@Valid @RequestBody UOMRequest uomRequest) throws Exception {

		return uomService.updateUomMaster(uomRequest);
	}

	/**
	 * Description : This api for searching UOM master
	 * 
	 * @param requestInfo
	 * @param tenantId
	 * @param ids
	 * @param name
	 * @param code
	 * @param active
	 * @param pageSize
	 * @param offSet
	 * @return UOMResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/uom/_search", method = RequestMethod.POST)
	public UOMResponse getUomMaster(@RequestBody RequestInfoWrapper requestInfo,
			@RequestParam(required = true) String tenantId, @RequestParam(required = false) Integer[] ids,
			@RequestParam(required = false) String name, @RequestParam(required = false) String code,
			@RequestParam(required = false) Boolean active, @RequestParam(required = false) Integer pageSize,
			@RequestParam(required = false) Integer offSet) throws Exception {

		return uomService.getUomMaster(requestInfo.getRequestInfo(), tenantId, ids, name, code, active, pageSize,
				offSet);
	}

	/**
	 * Description : This api for creating documentType master
	 * 
	 * @param tenantId
	 * @param DocumentTypeRequest
	 * @return DocumentTypeResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/documenttype/_create", method = RequestMethod.POST)
	public DocumentTypeResponse createDocumentTypeMaster(@Valid @RequestBody DocumentTypeRequest documentTypeRequest)
			throws Exception {

		return documentTypeService.createDocumentType(documentTypeRequest);
	}

	/**
	 * Description : This api for updating DocumentType master
	 * 
	 * @param DocumentTypeRequest
	 * @return DocumentTypeResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/documenttype/_update", method = RequestMethod.POST)
	public DocumentTypeResponse updateDocumentTypeMaster(@Valid @RequestBody DocumentTypeRequest documentTypeRequest)
			throws Exception {

		return documentTypeService.updateDocumentType(documentTypeRequest);
	}

	/**
	 * Description : This api for searching DocumentType master
	 * 
	 * @param requestInfo
	 * @param tenantId
	 * @param ids
	 * @param name
	 * @param enabled
	 * @param applicationType
	 * @param pageSize
	 * @param offSet
	 * @return DocumentTypeResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/documenttype/_search", method = RequestMethod.POST)
	public DocumentTypeResponse getDocumentTypeMaster(@RequestBody RequestInfoWrapper requestInfo,
			@RequestParam(required = true) String tenantId, @RequestParam(required = false) Integer[] ids,
			@RequestParam(required = false) String name, @RequestParam(required = false) Boolean enabled,
			@RequestParam(required = false) String applicationType, @RequestParam(required = false) Integer pageSize,
			@RequestParam(required = false) Integer offSet) throws Exception {

		return documentTypeService.getDocumentType(requestInfo.getRequestInfo(), tenantId, ids, name, enabled,
				applicationType, pageSize, offSet);
	}

	/**
	 * Description : This api for creating penaltyRate master
	 * 
	 * @param tenantId
	 * @param PenaltyRateRequest
	 * @return PenaltyRateResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/penaltyrate/_create", method = RequestMethod.POST)
	public PenaltyRateResponse createPenaltyRateMaster(@RequestParam(required = true) String tenantId,
			@Valid @RequestBody PenaltyRateRequest penaltyRateRequest) throws Exception {

		return penaltyRateService.createPenaltyRateMaster(tenantId, penaltyRateRequest);
	}

	/**
	 * Description : This api for updating penaltyRate master
	 * 
	 * @param PenaltyRateRequest
	 * @return PenaltyRateResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/penaltyrate/_update", method = RequestMethod.POST)
	public PenaltyRateResponse updatePenaltyRateMaster(@Valid @RequestBody PenaltyRateRequest penaltyRateRequest)
			throws Exception {

		return penaltyRateService.updatePenaltyRateMaster(penaltyRateRequest);
	}

	/**
	 * Description : This api for searching penaltyRate master
	 * 
	 * @param requestInfo
	 * @param tenantId
	 * @param ids
	 * @param applicationType
	 * @param pageSize
	 * @param offSet
	 * @return PenaltyRateResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/penaltyrate/_search", method = RequestMethod.POST)
	public PenaltyRateResponse getPenaltyRateMaster(@RequestBody RequestInfoWrapper requestInfo,
			@RequestParam(required = true) String tenantId, @RequestParam(required = false) Integer[] ids,
			@RequestParam(required = true) String applicationType, @RequestParam(required = false) Integer pageSize,
			@RequestParam(required = false) Integer offSet) throws Exception {

		return penaltyRateService.getPenaltyRateMaster(requestInfo.getRequestInfo(), tenantId, ids, applicationType,
				pageSize, offSet);
	}

	/**
	 * Description : This api for creating feeMatrix master
	 * 
	 * @param tenantId
	 * @param FeeMatrixRequest
	 * @return FeeMatrixResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/feematrix/_create", method = RequestMethod.POST)
	public FeeMatrixResponse createFeeMatrixMaster(@RequestParam(required = true) String tenantId,
			@Valid @RequestBody FeeMatrixRequest feeMatrixRequest) throws Exception {

		return feeMatrixService.createFeeMatrixMaster(tenantId, feeMatrixRequest);
	}

	/**
	 * Description : This api for updating feeMatrix master
	 * 
	 * @param FeeMatrixRequest
	 * @return FeeMatrixResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/feematrix/_update", method = RequestMethod.POST)
	public FeeMatrixResponse updateFeeMatrixMaster(@Valid @RequestBody FeeMatrixRequest feeMatrixRequest)
			throws Exception {

		return feeMatrixService.updateFeeMatrixMaster(feeMatrixRequest);
	}

	/**
	 * Description : This api for searching feeMatrix master
	 * 
	 * @param requestInfo
	 * @param tenantId
	 * @param ids
	 * @param categoryId
	 * @param subcategoryId
	 * @param financialYear
	 * @param applicationType
	 * @param businessNature
	 * @param pageSize
	 * @param offSet
	 * @return FeeMatrixResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/feematrix/_search", method = RequestMethod.POST)
	public FeeMatrixResponse getFeeMatrixMaster(@RequestBody RequestInfoWrapper requestInfo,
			@RequestParam(required = true) String tenantId, @RequestParam(required = false) Integer[] ids,
			@RequestParam(required = false) Integer categoryId, @RequestParam(required = false) Integer subcategoryId,
			@RequestParam(required = false) String financialYear,
			@RequestParam(required = false) String applicationType,
			@RequestParam(required = false) String businessNature, @RequestParam(required = false) Integer pageSize,
			@RequestParam(required = false) Integer offSet) throws Exception {

		return feeMatrixService.getFeeMatrixMaster(requestInfo.getRequestInfo(), tenantId, ids, categoryId,
				subcategoryId, financialYear, applicationType, businessNature, pageSize, offSet);
	}
}