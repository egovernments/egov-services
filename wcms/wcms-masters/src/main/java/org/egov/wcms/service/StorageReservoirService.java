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
package org.egov.wcms.service;

import java.util.List;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.wcms.config.PropertiesManager;
import org.egov.wcms.model.StorageReservoir;
import org.egov.wcms.repository.StorageReservoirRepository;
import org.egov.wcms.util.WcmsConstants;
import org.egov.wcms.web.contract.BoundaryRequestInfo;
import org.egov.wcms.web.contract.BoundaryRequestInfoWrapper;
import org.egov.wcms.web.contract.BoundaryResponse;
import org.egov.wcms.web.contract.StorageReservoirGetRequest;
import org.egov.wcms.web.contract.StorageReservoirRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StorageReservoirService {

    @Autowired
    private StorageReservoirRepository storageReservoirRepository;

    @Autowired
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private PropertiesManager propertiesManager;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CodeGeneratorService codeGeneratorService;

    public StorageReservoirRequest create(final StorageReservoirRequest storageReservoirRequest) {
        return storageReservoirRepository.persistCreateStorageReservoir(storageReservoirRequest);
    }

    public StorageReservoirRequest update(final StorageReservoirRequest storageReservoirRequest) {
        return storageReservoirRepository.persistUpdateStorageReservoir(storageReservoirRequest);
    }

    public List<StorageReservoir> createStorageReservoir(final String topic, final String key,
            final StorageReservoirRequest storageReservoirRequest) {
        for (final StorageReservoir storageReservoir : storageReservoirRequest.getStorageReservoir())
            storageReservoir.setCode(codeGeneratorService.generate(StorageReservoir.SEQ_STORAGE_RESERVOIR));
        try {
            kafkaTemplate.send(topic, key, storageReservoirRequest);
        } catch (final Exception ex) {
            log.error("Exception Encountered : " + ex);
        }
        return storageReservoirRequest.getStorageReservoir();
    }

    public List<StorageReservoir> updateStorageReservoir(final String topic, final String key,
            final StorageReservoirRequest storageReservoirRequest) {
        try {
            kafkaTemplate.send(topic, key, storageReservoirRequest);
        } catch (final Exception ex) {
            log.error("Exception Encountered : " + ex);
        }
        return storageReservoirRequest.getStorageReservoir();
    }

    public List<StorageReservoir> getStorageReservoir(
            final StorageReservoirGetRequest storageReservoirGetRequest) {
        if (storageReservoirGetRequest.getZoneName() != null) {
            final BoundaryResponse boundary = getBoundaryId(
                    WcmsConstants.ZONE,
                    WcmsConstants.REVENUE, storageReservoirGetRequest.getTenantId());
            if (boundary != null)
                storageReservoirGetRequest.setZone(boundary.getBoundarys().get(0).getId());

        }
        if (storageReservoirGetRequest.getWardName() != null) {
            final BoundaryResponse boundary = getBoundaryId(
                    WcmsConstants.WARD,
                    WcmsConstants.REVENUE, storageReservoirGetRequest.getTenantId());
            if (boundary != null)
                storageReservoirGetRequest.setWard(boundary.getBoundarys().get(0).getId());

        }
        if (storageReservoirGetRequest.getLocationName() != null) {
            final BoundaryResponse boundary = getBoundaryId(
                    WcmsConstants.LOCALITY,
                    WcmsConstants.LOCATION, storageReservoirGetRequest.getTenantId());
            if (boundary != null)
                storageReservoirGetRequest.setLocation(boundary.getBoundarys().get(0).getId());

        }
        return storageReservoirRepository.findForCriteria(storageReservoirGetRequest);
    }

    public Boolean getBoundaryByZone(
            final StorageReservoir storageReservoir) {
        Boolean isValidBoundaryByZone = Boolean.FALSE;
        BoundaryResponse boundaryRespose = null;
        boundaryRespose = getBoundaryId(
                WcmsConstants.ZONE,
                WcmsConstants.REVENUE,
                storageReservoir.getTenantId());
        if (boundaryRespose.getBoundarys().get(0) != null) {
            isValidBoundaryByZone = Boolean.TRUE;
            storageReservoir.setZone(
                    boundaryRespose.getBoundarys() != null && boundaryRespose.getBoundarys().get(0) != null
                            ? boundaryRespose.getBoundarys().get(0).getId() : "");

        }
        return isValidBoundaryByZone;

    }

    public Boolean getBoundaryByWard(final StorageReservoir storageReservoir) {
        Boolean isValidBoundaryByWard = Boolean.FALSE;
        BoundaryResponse boundaryRespose = null;
        boundaryRespose = getBoundaryId(
                WcmsConstants.WARD,
                WcmsConstants.REVENUE,
                storageReservoir.getTenantId());
        if (boundaryRespose.getBoundarys().get(0) != null) {
            isValidBoundaryByWard = Boolean.TRUE;
            storageReservoir.setWard(
                    boundaryRespose.getBoundarys() != null && boundaryRespose.getBoundarys().get(0) != null
                            ? boundaryRespose.getBoundarys().get(0).getId() : "");

        }
        return isValidBoundaryByWard;

    }

    public Boolean getBoundaryByLocation(final StorageReservoir storageReservoir) {
        Boolean isValidBoundaryByLocation = Boolean.FALSE;
        BoundaryResponse boundaryRespose = null;
        boundaryRespose = getBoundaryId(
                WcmsConstants.LOCALITY,
                WcmsConstants.LOCATION,
                storageReservoir.getTenantId());
        if (boundaryRespose.getBoundarys().get(0) != null) {
            isValidBoundaryByLocation = Boolean.TRUE;
            storageReservoir.setLocation(
                    boundaryRespose.getBoundarys() != null && boundaryRespose.getBoundarys().get(0) != null
                            ? boundaryRespose.getBoundarys().get(0).getId() : "");

        }
        return isValidBoundaryByLocation;

    }

    public BoundaryResponse getBoundaryId(final String boundaryTypeName, final String hierarchiTypeName, final String tenantId) {
        String url = propertiesManager.getLocationServiceBasePathTopic()
                + propertiesManager.getLocationServiceBoundarySearchPathTopic();
        url = url.replace("{boundaryTypeName}", boundaryTypeName);
        url = url.replace("{hierarchyTypeName}", hierarchiTypeName);
        url = url.replace("{tenantId}", tenantId);
        final BoundaryResponse boundary = getBoundary(url);
        return boundary;
    }

    public BoundaryResponse getBoundary(final String url) {
        final BoundaryRequestInfo requestInfo = BoundaryRequestInfo.builder().build();
        final BoundaryRequestInfoWrapper wrapper = BoundaryRequestInfoWrapper.builder().requestInfo(requestInfo).build();
        final HttpEntity<BoundaryRequestInfoWrapper> request = new HttpEntity<>(wrapper);
        final BoundaryResponse boundary = restTemplate.postForObject(url.toString(), request,
                BoundaryResponse.class);
        return boundary;
    }

    public boolean getStorageReservoirByNameAndCode(final String code, final String name, final String tenantId) {
        return storageReservoirRepository.checkStorageReservoirByNameAndCode(code, name, tenantId);
    }

}
