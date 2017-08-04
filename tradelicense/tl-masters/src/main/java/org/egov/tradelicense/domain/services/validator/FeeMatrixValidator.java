/**
 * 
 */
package org.egov.tradelicense.domain.services.validator;

import org.egov.models.AuditDetails;
import org.egov.models.FeeMatrix;
import org.egov.models.FeeMatrixDetail;
import org.egov.models.FeeMatrixRequest;
import org.egov.models.RequestInfo;
import org.egov.models.RequestInfoWrapper;
import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.domain.exception.DuplicateIdException;
import org.egov.tradelicense.domain.exception.InvalidInputException;
import org.egov.tradelicense.persistence.repository.FeeMatrixRepository;
import org.egov.tradelicense.persistence.repository.helper.FeeMatrixHelper;
import org.egov.tradelicense.persistence.repository.helper.UtilityHelper;
import org.egov.tradelicense.utility.ConstantUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author phani
 *
 */
@Component
public class FeeMatrixValidator {

	@Autowired
	UtilityHelper utilityHelper;

	@Autowired
	PropertiesManager propertiesManager;
	
	@Autowired
	FeeMatrixHelper feeMatrixHelper;
	
	@Autowired
	FeeMatrixRepository feeMatrixRepository;
	
	
	public void validateFeeMatrixRequest( FeeMatrixRequest feeMatrixRequest, Boolean isNew){
		
		RequestInfo requestInfo = feeMatrixRequest.getRequestInfo();
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		AuditDetails auditDetails  = utilityHelper.getCreateMasterAuditDetails(requestInfo);
		
		
		String tenantId;
		
		for (FeeMatrix feeMatrix : feeMatrixRequest.getFeeMatrices()) {

			tenantId = feeMatrix.getTenantId();
			String applicationType = feeMatrix.getApplicationType().toString();
			Long categoryId = feeMatrix.getCategoryId();
			Long subCategoryId = feeMatrix.getSubCategoryId();
			String financialYear = feeMatrix.getFinancialYear();

			// validating financial year
			feeMatrixHelper.validateFinancialYear(financialYear, requestInfoWrapper);
			// validating category
			feeMatrixHelper.validateCategory(categoryId, requestInfo);
			// validating sub category
			feeMatrixHelper.validateCategory(subCategoryId, requestInfo);
			// checking existence of duplicate fee matrix record
			Boolean isExists = utilityHelper.checkWhetherDuplicateFeeMatrixRecordExits(tenantId, applicationType,
					categoryId, subCategoryId, financialYear, ConstantUtility.FEE_MATRIX_TABLE_NAME, ( isNew ? null: feeMatrix.getId() ) );
			
			if (isExists) {
				throw new DuplicateIdException(propertiesManager.getCategoryCustomMsg(), requestInfo);
			}
			// validating fee matrix details
			feeMatrixHelper.validateFeeMatrixDetailsRange(feeMatrix, requestInfo, true);
			
			if( !isNew ){
				
				auditDetails = utilityHelper.getUpdateMasterAuditDetails(auditDetails, requestInfo);
			}
			
			feeMatrix.setAuditDetails(auditDetails);
		}
	}
}
