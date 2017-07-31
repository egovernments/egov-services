package org.egov.tradelicense.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.models.AuditDetails;
import org.egov.models.FeeMatrix;
import org.egov.models.FeeMatrixDetail;
import org.egov.models.FeeMatrixRequest;
import org.egov.models.FeeMatrixResponse;
import org.egov.models.RequestInfo;
import org.egov.models.ResponseInfo;
import org.egov.models.ResponseInfoFactory;
import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.exception.DuplicateIdException;
import org.egov.tradelicense.exception.InvalidInputException;
import org.egov.tradelicense.exception.InvalidRangeException;
import org.egov.tradelicense.repository.FeeMatrixRepository;
import org.egov.tradelicense.repository.helper.FeeMatrixHelper;
import org.egov.tradelicense.repository.helper.UtilityHelper;
import org.egov.tradelicense.utility.ConstantUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FeeMatrixService implementation class
 * 
 * @author Pavan Kumar Kamma
 *
 */
@Service
public class FeeMatrixServiceImpl implements FeeMatrixService {

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	@Autowired
	FeeMatrixRepository feeMatrixRepository;

	@Autowired
	UtilityHelper utilityHelper;

	@Autowired
	private PropertiesManager propertiesManager;
	
	@Override
	@Transactional
	public FeeMatrixResponse createFeeMatrixMaster(String tenantId, FeeMatrixRequest feeMatrixRequest) {

		RequestInfo requestInfo = feeMatrixRequest.getRequestInfo();
		AuditDetails auditDetails = utilityHelper.getCreateMasterAuditDetals(requestInfo);
		for (FeeMatrix feeMatrix : feeMatrixRequest.getFeeMatrices()) {

			tenantId = feeMatrix.getTenantId();
			String applicationType = feeMatrix.getApplicationType().toString();
			Long categoryId = feeMatrix.getCategoryId();
			Long subCategoryId = feeMatrix.getSubCategoryId();
			String financialYear = feeMatrix.getFinancialYear();
			Boolean isExists = utilityHelper.checkWhetherDuplicateFeeMatrixRecordExits(tenantId, applicationType,
					categoryId, subCategoryId, financialYear, ConstantUtility.FEE_MATRIX_TABLE_NAME, null);
			if (isExists) {
				throw new DuplicateIdException(propertiesManager.getCategoryCustomMsg(), requestInfo);
			}
			Long UomFrom = null;
			Long oldUomTo = null;
			int count = 0;
			List<FeeMatrixDetail> feeMatrixDetails = new ArrayList<>();
			feeMatrixDetails = feeMatrix.getFeeMatrixDetails();
			feeMatrixDetails.sort((r1, r2) -> r1.getUomFrom().compareTo(r2.getUomFrom()));
			for (FeeMatrixDetail feeMatrixDetail : feeMatrixDetails) {

				UomFrom = feeMatrixDetail.getUomFrom();
				if (count > 0) {
					if (!UomFrom.equals(oldUomTo)) {
						throw new InvalidRangeException(requestInfo);
					}
				}
				oldUomTo = feeMatrixDetail.getUomTo();
				count++;
			}
			try {
				feeMatrix.setAuditDetails(auditDetails);
				Long feeMatrixId = feeMatrixRepository.createFeeMatrix(tenantId, feeMatrix);
				feeMatrix.setId(feeMatrixId);
				for (FeeMatrixDetail feeMatrixDetail : feeMatrix.getFeeMatrixDetails()) {
					feeMatrixDetail.setFeeMatrixId(feeMatrixId);
					Long feeMatrixDetailId = feeMatrixRepository.createFeeMatrixDetails(tenantId, feeMatrixDetail);
					feeMatrixDetail.setId(feeMatrixDetailId);
				}
			} catch (Exception e) {
				throw new InvalidInputException(requestInfo);
			}
		}

		FeeMatrixResponse feeMatrixResponse = new FeeMatrixResponse();
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		feeMatrixResponse.setFeeMatrices(feeMatrixRequest.getFeeMatrices());
		feeMatrixResponse.setResponseInfo(responseInfo);

		return feeMatrixResponse;
	}

	public FeeMatrixResponse updateFeeMatrixMaster(FeeMatrixRequest feeMatrixRequest) {

		RequestInfo requestInfo = feeMatrixRequest.getRequestInfo();
		for (FeeMatrix feeMatrix : feeMatrixRequest.getFeeMatrices()) {

			String tenantId = feeMatrix.getTenantId();
			String applicationType = feeMatrix.getApplicationType().toString();
			Long categoryId = feeMatrix.getCategoryId();
			Long subCategoryId = feeMatrix.getSubCategoryId();
			String financialYear = feeMatrix.getFinancialYear();
			Long feeMatrixId = feeMatrix.getId();
			Boolean isExists = utilityHelper.checkWhetherDuplicateFeeMatrixRecordExits(tenantId, applicationType,
					categoryId, subCategoryId, financialYear, ConstantUtility.FEE_MATRIX_TABLE_NAME, feeMatrixId);
			if (isExists) {
				throw new DuplicateIdException(propertiesManager.getCategoryCustomMsg(), requestInfo);
			}
			List<FeeMatrixDetail> feeMatrixDetails = new ArrayList<>();
			try {
				feeMatrixDetails = feeMatrixRepository.getFeeMatrixDetailsByFeeMatrixId(feeMatrixId);
			} catch (Exception e) {
				throw new InvalidInputException(requestInfo);
			}
			for (FeeMatrixDetail feeMatrixDetail : feeMatrix.getFeeMatrixDetails()) {
				for (int i = 0; i < feeMatrixDetails.size(); i++) {
					Long id = feeMatrixDetails.get(i).getId();
					if (feeMatrixDetail.getId() != null && id == feeMatrixDetail.getId()) {
						feeMatrixDetails.set(i, feeMatrixDetail);
					}
				}
			}
			Long UomFrom = null;
			Long oldUomTo = null;
			int count = 0;
			feeMatrixDetails.sort((r1, r2) -> r1.getUomFrom().compareTo(r2.getUomFrom()));
			for (FeeMatrixDetail feeMatrixDetail : feeMatrixDetails) {

				UomFrom = feeMatrixDetail.getUomFrom();
				if (count > 0) {
					if (!UomFrom.equals(oldUomTo)) {
						throw new InvalidRangeException(requestInfo);
					}
				}
				oldUomTo = feeMatrixDetail.getUomTo();
				count++;
			}
			try {
				Long updatedTime = new Date().getTime();
				feeMatrix.getAuditDetails().setLastModifiedTime(updatedTime);
				if(requestInfo.getUserInfo() != null)
				feeMatrix.getAuditDetails().setLastModifiedBy(requestInfo.getUserInfo().getUsername());
				for (FeeMatrixDetail feeMatrixDetail : feeMatrix.getFeeMatrixDetails()) {
					feeMatrixDetail = feeMatrixRepository.updateFeeMatrixDetail(feeMatrixDetail);
				}
				feeMatrix = feeMatrixRepository.updateFeeMatrix(tenantId, feeMatrix);
			} catch (Exception e) {
				throw new InvalidInputException(requestInfo);
			}
		}

		FeeMatrixResponse feeMatrixResponse = new FeeMatrixResponse();
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		feeMatrixResponse.setFeeMatrices(feeMatrixRequest.getFeeMatrices());
		feeMatrixResponse.setResponseInfo(responseInfo);

		return feeMatrixResponse;
	}

	public FeeMatrixResponse getFeeMatrixMaster(RequestInfo requestInfo, String tenantId, Integer[] ids,
			Integer categoryId, Integer subCategoryId, String financialYear, String applicationType,
			String businessNature, Integer pageSize, Integer offSet) {

		FeeMatrixResponse feeMatrixResponse = new FeeMatrixResponse();
		try {
			List<FeeMatrix> feeMatrices = feeMatrixRepository.searchFeeMatrix(tenantId, ids, categoryId, subCategoryId,
					financialYear, applicationType, businessNature, pageSize, offSet);
			for (int i = 0; i < feeMatrices.size(); i++) {
				FeeMatrix feeMatrix = feeMatrices.get(i);
				List<FeeMatrixDetail> feeMatrixDetails = feeMatrixRepository
						.getFeeMatrixDetailsByFeeMatrixId(feeMatrix.getId());
				feeMatrix.setFeeMatrixDetails(feeMatrixDetails);
			}
			ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
			feeMatrixResponse.setFeeMatrices(feeMatrices);
			feeMatrixResponse.setResponseInfo(responseInfo);

		} catch (Exception e) {
			throw new InvalidInputException(requestInfo);
		}

		return feeMatrixResponse;
	}
}