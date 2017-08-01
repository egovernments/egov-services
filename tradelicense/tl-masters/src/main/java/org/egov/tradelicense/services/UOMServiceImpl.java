package org.egov.tradelicense.services;

import java.util.Date;
import java.util.List;

import org.egov.models.AuditDetails;
import org.egov.models.RequestInfo;
import org.egov.models.ResponseInfo;
import org.egov.models.ResponseInfoFactory;
import org.egov.models.UOM;
import org.egov.models.UOMRequest;
import org.egov.models.UOMResponse;
import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.exception.DuplicateIdException;
import org.egov.tradelicense.exception.InvalidInputException;
import org.egov.tradelicense.repository.UOMRepository;
import org.egov.tradelicense.repository.helper.UtilityHelper;
import org.egov.tradelicense.utility.ConstantUtility;
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
	private PropertiesManager propertiesManager;

	@Override
	@Transactional
	public UOMResponse createUomMaster( UOMRequest uomRequest) {
		
	

		for (UOM uom : uomRequest.getUoms()) {

			Boolean isExists = utilityHelper.checkWhetherDuplicateRecordExits(uom.getTenantId(), uom.getCode(),
					ConstantUtility.UOM_TABLE_NAME, null);
			if (isExists)
				throw new DuplicateIdException(propertiesManager.getUomCustomMsg(),uomRequest.getRequestInfo());

			RequestInfo requestInfo = uomRequest.getRequestInfo();
			AuditDetails auditDetails = utilityHelper.getCreateMasterAuditDetals(requestInfo);
			try {

				uom.setAuditDetails(auditDetails);
				Long id = uomRepository.createUom(uom);
				uom.setId(id);

			} catch (Exception e) {
				throw new InvalidInputException(uomRequest.getRequestInfo());
			}
		}

		UOMResponse uomResponse = new UOMResponse();

		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(uomRequest.getRequestInfo(),
				true);

		uomResponse.setUoms(uomRequest.getUoms());
		uomResponse.setResponseInfo(responseInfo);

		return uomResponse;
	}

	@Override
	@Transactional
	public UOMResponse updateUomMaster(UOMRequest uomRequest) {

		for (UOM uom : uomRequest.getUoms()) {

			Boolean isExists = utilityHelper.checkWhetherDuplicateRecordExits(uom.getTenantId(), uom.getCode(),
					ConstantUtility.UOM_TABLE_NAME, uom.getId());

			if (isExists)
				throw new DuplicateIdException(propertiesManager.getUomCustomMsg(),uomRequest.getRequestInfo());

			RequestInfo requestInfo = uomRequest.getRequestInfo();
			try {

				Long updatedTime = new Date().getTime();
				uom.getAuditDetails().setLastModifiedTime(updatedTime);
				uom.getAuditDetails().setLastModifiedBy(requestInfo.getUserInfo().getUsername());
				uom = uomRepository.updateUom(uom);

			} catch (Exception e) {

				throw new InvalidInputException(uomRequest.getRequestInfo());
			}
		}

		UOMResponse uomResponse = new UOMResponse();

		uomResponse.setUoms(uomRequest.getUoms());
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(uomRequest.getRequestInfo(),
				true);
		uomResponse.setResponseInfo(responseInfo);
		return uomResponse;
	}

	@Override
	public UOMResponse getUomMaster(RequestInfo requestInfo, String tenantId, Integer[] ids, String name, String code,
			Boolean active, Integer pageSize, Integer offSet) {

		UOMResponse uomResponse = new UOMResponse();

		try {
			List<UOM> uoms = uomRepository.searchUom(tenantId, ids, name, code, active, pageSize, offSet);
			ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
			uomResponse.setUoms(uoms);
			uomResponse.setResponseInfo(responseInfo);

		} catch (Exception e) {
			throw new InvalidInputException(requestInfo);
		}

		return uomResponse;

	}
}