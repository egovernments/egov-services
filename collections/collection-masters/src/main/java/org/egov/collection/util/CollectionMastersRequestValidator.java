/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.collection.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egov.collection.config.CollectionMasterServiceConstants;
import org.egov.collection.domain.model.BankAccountServiceMappingSearchCriteria;
import org.egov.collection.service.BankAccountMappingService;
import org.egov.collection.web.contract.BankAccountServiceMapping;
import org.egov.collection.web.contract.BankAccountServiceMappingReq;
import org.egov.common.contract.response.*;
import org.egov.common.contract.response.Error;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CollectionMastersRequestValidator {

    @Autowired
    private BankAccountMappingService bankAccountMappingService;

    public ErrorResponse validateBankAccountSearchRequest(final BankAccountServiceMappingSearchCriteria searchCriteria) {
        ErrorResponse errorResponse = null;
        List<ErrorField> errorFields = new ArrayList<>();
        Error error = null;
        if(StringUtils.isBlank(searchCriteria.getTenantId())) {
            ErrorField errorField = ErrorField
                    .builder()
                    .code(CollectionMasterServiceConstants.TENANT_ID_REQUIRED_CODE)
                    .message(
                            CollectionMasterServiceConstants.TENANT_ID_REQUIRED_MESSAGE)
                    .field(CollectionMasterServiceConstants.TENANT_ID_REQUIRED_FIELD)
                    .build();
            errorFields.add(errorField);

        }

        if(errorFields != null && !errorFields.isEmpty()) {
            error = Error.builder().code(HttpStatus.BAD_REQUEST.value())
                    .message(CollectionMasterServiceConstants.SEARCH_BANKACCOUNT_SERVICE_MAPPING_REQUEST)
                    .fields(errorFields).build();
            error.setFields(errorFields);
            errorResponse = new ErrorResponse();
            errorResponse.setError(error);
        }
       return errorResponse;
    }

    public ErrorResponse validateBankAccountServiceRequest(final BankAccountServiceMappingReq bankAccountServiceMappingReq) {
        ErrorResponse errorResponse = null;
        List<ErrorField> errorFields = new ArrayList<>();
        Error error = null;
        for(BankAccountServiceMapping bankAccountServiceMapping : bankAccountServiceMappingReq.getBankAccountServiceMapping()) {
            if(StringUtils.isEmpty(bankAccountServiceMapping.getTenantId())) {
                ErrorField errorField = ErrorField
                        .builder()
                        .code(CollectionMasterServiceConstants.TENANT_ID_REQUIRED_CODE)
                        .message(
                                CollectionMasterServiceConstants.TENANT_ID_REQUIRED_MESSAGE)
                        .field(CollectionMasterServiceConstants.TENANT_ID_REQUIRED_FIELD)
                        .build();
                errorFields.add(errorField);
            }
            if(StringUtils.isEmpty(bankAccountServiceMapping.getBusinessDetails())) {
                ErrorField errorField = ErrorField
                        .builder()
                        .code(CollectionMasterServiceConstants.BANKACCOUNT_SERVICE_MAPPING_BUSINESSDETAILS_CODE)
                        .message(
                                CollectionMasterServiceConstants.BANKACCOUNT_SERVICE_MAPPING_BUSINESSDETAILS_MESSAGE)
                        .field(CollectionMasterServiceConstants.BANKACCOUNT_SERVICE_MAPPING_BUSINESSDETAILS_FIELD)
                        .build();
                errorFields.add(errorField);
            }
            if(StringUtils.isEmpty(bankAccountServiceMapping.getBankAccount())) {
                ErrorField errorField = ErrorField
                        .builder()
                        .code(CollectionMasterServiceConstants.BANKACCOUNT_SERVICE_MAPPING_BANKACCOUNT_CODE)
                        .message(
                                CollectionMasterServiceConstants.BANKACCOUNT_SERVICE_MAPPING_BANKACCOUNT_MESSAGE)
                        .field(CollectionMasterServiceConstants.BANKACCOUNT_SERVICE_MAPPING_BANKACCOUNT_FIELD)
                        .build();
                errorFields.add(errorField);
            }

            if(StringUtils.isNotBlank(bankAccountServiceMapping.getTenantId()) && StringUtils.isNotBlank(bankAccountServiceMapping.getBusinessDetails())
                    && StringUtils.isNotBlank(bankAccountServiceMapping.getBankAccount())) {
                BankAccountServiceMappingSearchCriteria searchCriteria = new BankAccountServiceMappingSearchCriteria();
                searchCriteria.setTenantId(bankAccountServiceMapping.getTenantId());
                searchCriteria.setBankAccount(bankAccountServiceMapping.getBankAccount());
                List<org.egov.collection.domain.model.BankAccountServiceMapping> bankAccountServiceMappings =
                        bankAccountMappingService.searchBankAccountService(searchCriteria);
                if(!bankAccountServiceMappings.isEmpty()) {
                    ErrorField errorField = ErrorField
                            .builder()
                            .code(CollectionMasterServiceConstants.BANKACCOUNT_SERVICE_MAPPING_EXISTS_CODE)
                            .message(
                                    CollectionMasterServiceConstants.BANKACCOUNT_SERVICE_MAPPING_EXISTS_MESSAGE1 + bankAccountServiceMapping.getBusinessDetails() +
                            CollectionMasterServiceConstants.BANKACCOUNT_SERVICE_MAPPING_EXISTS_MESSAGE2 + bankAccountServiceMapping.getBankAccount() +
                            CollectionMasterServiceConstants.BANKACCOUNT_SERVICE_MAPPING_EXISTS_MESSAGE3)
                            .field(CollectionMasterServiceConstants.BANKACCOUNT_SERVICE_MAPPING_EXISTS_FIELD)
                            .build();
                    errorFields.add(errorField);
                }

            }


        }
        if(errorFields != null && !errorFields.isEmpty()) {
            error = Error.builder().code(HttpStatus.BAD_REQUEST.value())
                    .message(CollectionMasterServiceConstants.SEARCH_BANKACCOUNT_SERVICE_MAPPING_REQUEST)
                    .fields(errorFields).build();
            error.setFields(errorFields);
            errorResponse = new ErrorResponse();
            errorResponse.setError(error);
        }
        return errorResponse;
    }
}
