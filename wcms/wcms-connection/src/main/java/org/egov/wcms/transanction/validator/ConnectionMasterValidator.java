/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.wcms.transanction.validator;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ErrorField;
import org.egov.wcms.transanction.config.ConfigurationManager;
import org.egov.wcms.transanction.util.WcmsTranasanctionConstants;
import org.egov.wcms.transanction.web.contract.CategoryResponseInfo;
import org.egov.wcms.transanction.web.contract.DonationResponseInfo;
import org.egov.wcms.transanction.web.contract.PipeSizeResponseInfo;
import org.egov.wcms.transanction.web.contract.RequestInfoWrapper;
import org.egov.wcms.transanction.web.contract.SupplyResponseInfo;
import org.egov.wcms.transanction.web.contract.WaterConnectionReq;
import org.egov.wcms.transanction.web.contract.WaterSourceResponseInfo;
import org.egov.wcms.transanction.web.contract.factory.ResponseInfoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class ConnectionMasterValidator {
    
    @Autowired
    private ConfigurationManager configurationManager;
    
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ResponseInfoFactory responseInfoFactory;
    

    public List<ErrorField> getMasterValidation(final WaterConnectionReq waterConnectionRequest) {
        final List<ErrorField> errorFields = new ArrayList<>();

        if (getCategoryTypeByName(waterConnectionRequest) ==null) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsTranasanctionConstants.CATEGORY_INVALID_CODE)
                    .message(WcmsTranasanctionConstants.CATEGORY_INVALID_FIELD_NAME)
                    .field(WcmsTranasanctionConstants.CATEGORY_INVALID_ERROR_MESSAGE)
                    .build();
            errorFields.add(errorField);
        }
        if (getPipesizeTypeByCode(waterConnectionRequest)==null) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsTranasanctionConstants.PIPESIZE_INVALID_CODE)
                    .message(WcmsTranasanctionConstants.PIPESIZE_INVALID_FIELD_NAME)
                    .field(WcmsTranasanctionConstants.PIPESIZE_INVALID_ERROR_MESSAGE)
                    .build();
            errorFields.add(errorField);
        }
        if (getSourceTypeByName(waterConnectionRequest)==null) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsTranasanctionConstants.SOURCETYPE_INVALID_CODE)
                    .message(WcmsTranasanctionConstants.SOURCETYPE_INVALID_FIELD_NAME)
                    .field(WcmsTranasanctionConstants.SOURCETYPE_INVALID_ERROR_MESSAGE)
                    .build();
            errorFields.add(errorField);
        }
        if (getSupplyTypeByName(waterConnectionRequest)==null) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsTranasanctionConstants.SUPPLYTYPE_INVALID_CODE)
                    .message(WcmsTranasanctionConstants.SUPPLYTYPE_INVALID_FIELD_NAME)
                    .field(WcmsTranasanctionConstants.SUPPLYTYPE_INVALID_ERROR_MESSAGE)
                    .build();
            errorFields.add(errorField);
        }
        return errorFields;
    }

   
    public CategoryResponseInfo getCategoryTypeByName(WaterConnectionReq waterConnectionRequest) {
        StringBuilder url = new StringBuilder();
        url.append(configurationManager.getWaterMasterServiceBasePathTopic())
        .append(configurationManager.getWaterMasterServiceCategorySearchPathTopic());
        final RequestInfo requestInfo = RequestInfo.builder().build();
        RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
        final HttpEntity<RequestInfoWrapper> request = new HttpEntity<>(wrapper);
        CategoryResponseInfo positions = restTemplate.postForObject(url.toString(), request,CategoryResponseInfo.class,waterConnectionRequest.getConnection().getCategoryType(),waterConnectionRequest.getConnection().getTenantId());
        if(positions!=null){
        waterConnectionRequest.getConnection().setCategoryId(positions.getCategory()!=null && positions.getCategory().get(0)!=null?positions.getCategory().get(0).getId():"");
        }
        return positions;
    }
    
    public PipeSizeResponseInfo getPipesizeTypeByCode(WaterConnectionReq waterConnectionRequest) {
        StringBuilder url = new StringBuilder();
        url.append(configurationManager.getWaterMasterServiceBasePathTopic())
        .append(configurationManager.getWaterMasterServicePipesizeSearchPathTopic());
        final RequestInfo requestInfo = RequestInfo.builder().build();
        RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
        final HttpEntity<RequestInfoWrapper> request = new HttpEntity<>(wrapper);
        PipeSizeResponseInfo pipesize = restTemplate.postForObject(url.toString(), request,PipeSizeResponseInfo.class,Double.parseDouble(waterConnectionRequest.getConnection().getHscPipeSizeType()),waterConnectionRequest.getConnection().getTenantId());
        if(pipesize!=null){
        waterConnectionRequest.getConnection().setPipesizeId(pipesize.getPipeSize()!=null && pipesize.getPipeSize().get(0)!=null?pipesize.getPipeSize().get(0).getId():"");
        }
        return pipesize;
    }
    
    public WaterSourceResponseInfo getSourceTypeByName(WaterConnectionReq waterConnectionRequest) {
        StringBuilder url = new StringBuilder();
        url.append(configurationManager.getWaterMasterServiceBasePathTopic())
        .append(configurationManager.getWaterMasterServiceSourceSearchPathTopic());
        final RequestInfo requestInfo = RequestInfo.builder().build();
        RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
        final HttpEntity<RequestInfoWrapper> request = new HttpEntity<>(wrapper);
        WaterSourceResponseInfo sourcetype = restTemplate.postForObject(url.toString(), request,WaterSourceResponseInfo.class,waterConnectionRequest.getConnection().getSourceType(),waterConnectionRequest.getConnection().getTenantId());
        if(sourcetype!=null){
        waterConnectionRequest.getConnection().setSourceTypeId(sourcetype.getWaterSourceType()!=null
                && sourcetype.getWaterSourceType().get(0)!=null?sourcetype.getWaterSourceType().get(0).getId():"");
        }
        return sourcetype;
    }
    
    public SupplyResponseInfo getSupplyTypeByName(WaterConnectionReq waterConnectionRequest) {
        StringBuilder url = new StringBuilder();
        url.append(configurationManager.getWaterMasterServiceBasePathTopic())
        .append(configurationManager.getWaterMasterServiceSupplySearchPathTopic());
        final RequestInfo requestInfo = RequestInfo.builder().build();
        RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
        final HttpEntity<RequestInfoWrapper> request = new HttpEntity<>(wrapper);
        SupplyResponseInfo supplytype = restTemplate.postForObject(url.toString(), request,SupplyResponseInfo.class,waterConnectionRequest.getConnection().getSupplyType(),waterConnectionRequest.getConnection().getTenantId());
        if(supplytype!=null){
        waterConnectionRequest.getConnection().setSupplyTypeId(supplytype.getSupplytypes()!=null
                && supplytype.getSupplytypes().get(0)!=null?supplytype.getSupplytypes().get(0).getId():"");
        }
        return supplytype;
    }
    
    public DonationResponseInfo validateDonationAmount(WaterConnectionReq waterConnectionRequest) {
        final RequestInfo requestInfo = RequestInfo.builder().build();
        RequestInfoWrapper wrapper = new RequestInfoWrapper(requestInfo);
        String url1 = configurationManager.getWaterMasterServiceBasePathTopic()
                + configurationManager.getWaterMasterServiceDonationSearchPathTopic() + "?propertyType=2"
                + "&usageType=3" + "&categoryType="  + waterConnectionRequest.getConnection().getCategoryId() +
                 "&maxHSCPipeSize=" +  waterConnectionRequest.getConnection().getPipesizeId()
                + "&minHSCPipeSize=" +waterConnectionRequest.getConnection().getPipesizeId() +
                 "&tenantId=" + waterConnectionRequest.getConnection().getTenantId();
        //TODO:waterConnectionRequest.getConnection().getProperty().getPropertyType() and waterConnectionRequest.getConnection().getProperty().getUsageType()
        //need to get From PTIS TEAM:usageType ,propertyType
        DonationResponseInfo donation1 = new RestTemplate().postForObject(url1, wrapper,
                DonationResponseInfo.class);
        if(donation1!=null){
        waterConnectionRequest.getConnection().setDonationCharge(donation1.getDonations()!=null
                && donation1.getDonations().get(0)!=null?donation1.getDonations().get(0).getDonationAmount():"");
        }
        return donation1;
    }
    
}
