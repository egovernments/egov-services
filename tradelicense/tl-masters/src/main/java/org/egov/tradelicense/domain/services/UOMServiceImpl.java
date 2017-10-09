package org.egov.tradelicense.domain.services;

import java.util.List;

import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.contract.ResponseInfo;
import org.egov.tl.commons.web.contract.UOM;
import org.egov.tl.commons.web.requests.ResponseInfoFactory;
import org.egov.tl.commons.web.requests.UOMRequest;
import org.egov.tl.commons.web.response.UOMResponse;
import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.domain.exception.InvalidInputException;
import org.egov.tradelicense.domain.services.validator.UOMValidator;
import org.egov.tradelicense.persistence.repository.UOMRepository;
import org.egov.tradelicense.persistence.repository.helper.UtilityHelper;
import org.egov.tradelicense.producers.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * UOMService implementation class
 * 
 * @author Pavan Kumar Kamma
 *
 */
@Service
public class UOMServiceImpl implements UOMService {

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	@Autowired
	UtilityHelper utilityHelper;

	@Autowired
	UOMRepository uomRepository;

	@Autowired
	UOMValidator uomValidator;

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	Producer producer;

	@Override
	@Transactional
	public UOMResponse createUomMaster(UOMRequest uomRequest) {

		uomValidator.validateUOMRequest(uomRequest, true);
		RequestInfo requestInfo = uomRequest.getRequestInfo();
		producer.send(propertiesManager.getCreateUomValidated(), uomRequest);
		UOMResponse uomResponse = new UOMResponse();
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		uomResponse.setUoms(uomRequest.getUoms());
		uomResponse.setResponseInfo(responseInfo);

		return uomResponse;

	}

	@Override
	@Transactional
	public void createUom(UOMRequest uomRequest) {
		RequestInfo requestInfo = uomRequest.getRequestInfo();

		for (UOM uom : uomRequest.getUoms()) {
			try {

				uomRepository.createUom(uom);

			} catch (Exception ex) {
				throw new InvalidInputException(ex.getMessage(), requestInfo);
			}
		}

	}

	@Override
	@Transactional
	public UOMResponse updateUomMaster(UOMRequest uomRequest) {

		// RequestInfo requestInfo = uomRequest.getRequestInfo();
		// for (UOM uom : uomRequest.getUoms()) {
		//
		// Boolean isExists =
		// utilityHelper.checkWhetherDuplicateRecordExits(uom.getTenantId(),
		// uom.getCode(),
		// null, ConstantUtility.UOM_TABLE_NAME, uom.getId());
		//
		// if (isExists)
		// throw new
		// DuplicateIdException(propertiesManager.getUomCustomMsg(),uomRequest.getRequestInfo());
		//
		// try {
		//
		// AuditDetails auditDetails = uom.getAuditDetails();
		// auditDetails =
		// utilityHelper.getUpdateMasterAuditDetails(auditDetails, requestInfo);
		// uom.setAuditDetails(auditDetails);
		// uom = uomRepository.updateUom(uom);
		//
		// } catch (Exception e) {
		//
		// throw new InvalidInputException(e.getLocalizedMessage(),
		// uomRequest.getRequestInfo());
		// }
		// }

		RequestInfo requestInfo = uomRequest.getRequestInfo();
		uomValidator.validateUOMRequest(uomRequest, false);
		producer.send(propertiesManager.getUpdateUomValidated(), uomRequest);
		UOMResponse uomResponse = new UOMResponse();
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		uomResponse.setUoms(uomRequest.getUoms());
		uomResponse.setResponseInfo(responseInfo);

		return uomResponse;
	}

	@Override
	@Transactional
	public void updateUom(UOMRequest uomRequest) {
		RequestInfo requestInfo = uomRequest.getRequestInfo();

		for (UOM uom : uomRequest.getUoms()) {
			try {

				uomRepository.updateUom(uom);

			} catch (Exception ex) {
				throw new InvalidInputException(ex.getMessage(), requestInfo);
			}
		}
	}

	@Override
	public UOMResponse getUomMaster(RequestInfo requestInfo, String tenantId, Integer[] ids, String name, String[] codes,
			String active, Integer pageSize, Integer offSet) {

		UOMResponse uomResponse = new UOMResponse();

		try {
			List<UOM> uoms = uomRepository.searchUom(tenantId, ids, name, codes, active, pageSize, offSet);
			ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
			uomResponse.setUoms(uoms);
			uomResponse.setResponseInfo(responseInfo);

		} catch (Exception e) {
			throw new InvalidInputException(e.getLocalizedMessage(), requestInfo);
		}

		return uomResponse;

	}
}