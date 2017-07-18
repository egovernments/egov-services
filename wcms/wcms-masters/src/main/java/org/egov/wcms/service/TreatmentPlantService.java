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
import org.egov.wcms.model.TreatmentPlant;
import org.egov.wcms.repository.TreatmentPlantRepository;
import org.egov.wcms.util.WcmsConstants;
import org.egov.wcms.web.contract.BoundaryResponse;
import org.egov.wcms.web.contract.TreatmentPlantGetRequest;
import org.egov.wcms.web.contract.TreatmentPlantRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TreatmentPlantService {

    @Autowired
    private TreatmentPlantRepository treatmentPlantRepository;

    @Autowired
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private CodeGeneratorService codeGeneratorService;

    @Autowired
    private RestWaterExternalMasterService restExternalMasterService;

    public TreatmentPlantRequest create(final TreatmentPlantRequest treatmentPlantRequest) {
        return treatmentPlantRepository.persistCreateTreatmentPlant(treatmentPlantRequest);
    }

    public TreatmentPlantRequest update(final TreatmentPlantRequest treatmentPlantRequest) {
        return treatmentPlantRepository.persistUpdateTreatmentPlant(treatmentPlantRequest);
    }

    public List<TreatmentPlant> createTreatmentPlant(final String topic, final String key,
            final TreatmentPlantRequest treatmentPlantRequest) {
        for (final TreatmentPlant treatmentPlant : treatmentPlantRequest.getTreatmentPlants())
            treatmentPlant.setCode(codeGeneratorService.generate(TreatmentPlant.SEQ_TREATMENT_PLANT));
        try {
            kafkaTemplate.send(topic, key, treatmentPlantRequest);
        } catch (final Exception ex) {
            log.error("Exception Encountered : " + ex);
        }
        return treatmentPlantRequest.getTreatmentPlants();
    }

    public List<TreatmentPlant> updateTreatmentPlant(final String topic, final String key,
            final TreatmentPlantRequest treatmentPlantRequest) {
        try {
            kafkaTemplate.send(topic, key, treatmentPlantRequest);
        } catch (final Exception ex) {
            log.error("Exception Encountered : " + ex);
        }
        return treatmentPlantRequest.getTreatmentPlants();
    }

    public List<TreatmentPlant> getTreatmentPlant(
            final TreatmentPlantGetRequest treatmentPlantGetRequest) {
        if (treatmentPlantGetRequest.getZoneName() != null) {
            final BoundaryResponse boundary = restExternalMasterService.getBoundaryId(
                    WcmsConstants.ZONE,
                    WcmsConstants.REVENUE, treatmentPlantGetRequest.getTenantId());
            if (boundary.getBoundarySize())
                treatmentPlantGetRequest.setZone(boundary.getBoundarys().get(0).getId());

        }
        if (treatmentPlantGetRequest.getWardName() != null) {
            final BoundaryResponse boundary = restExternalMasterService.getBoundaryId(
                    WcmsConstants.WARD,
                    WcmsConstants.REVENUE, treatmentPlantGetRequest.getTenantId());
            if (boundary.getBoundarySize())
                treatmentPlantGetRequest.setWard(boundary.getBoundarys().get(0).getId());

        }
        if (treatmentPlantGetRequest.getLocationName() != null) {
            final BoundaryResponse boundary = restExternalMasterService.getBoundaryId(
                    WcmsConstants.LOCALITY,
                    WcmsConstants.LOCATION, treatmentPlantGetRequest.getTenantId());
            if (boundary.getBoundarySize())
                treatmentPlantGetRequest.setLocation(boundary.getBoundarys().get(0).getId());

        }
        return treatmentPlantRepository.findForCriteria(treatmentPlantGetRequest);
    }

    public Boolean getBoundaryByZone(
            final TreatmentPlant treatmentPlant) {
        Boolean isValidBoundaryByZone = Boolean.FALSE;
        BoundaryResponse boundaryRespose = null;
        boundaryRespose = restExternalMasterService.getBoundaryId(
                WcmsConstants.ZONE,
                WcmsConstants.REVENUE,
                treatmentPlant.getTenantId());
        if (boundaryRespose.getBoundarySize() && boundaryRespose.getBoundarys().get(0) != null) {
            isValidBoundaryByZone = Boolean.TRUE;
            treatmentPlant.setZone(
                    boundaryRespose.getBoundarys() != null && boundaryRespose.getBoundarys().get(0) != null
                            ? boundaryRespose.getBoundarys().get(0).getId() : "");

        }
        return isValidBoundaryByZone;

    }

    public Boolean getBoundaryByWard(final TreatmentPlant treatmentPlant) {
        Boolean isValidBoundaryByWard = Boolean.FALSE;
        BoundaryResponse boundaryRespose = null;
        boundaryRespose = restExternalMasterService.getBoundaryId(
                WcmsConstants.WARD,
                WcmsConstants.REVENUE,
                treatmentPlant.getTenantId());
        if (boundaryRespose.getBoundarySize() && boundaryRespose.getBoundarys().get(0) != null) {
            isValidBoundaryByWard = Boolean.TRUE;
            treatmentPlant.setWard(
                    boundaryRespose.getBoundarys() != null && boundaryRespose.getBoundarys().get(0) != null
                            ? boundaryRespose.getBoundarys().get(0).getId() : "");

        }
        return isValidBoundaryByWard;

    }

    public Boolean getBoundaryByLocation(final TreatmentPlant treatmentPlant) {
        Boolean isValidBoundaryByLocation = Boolean.FALSE;
        BoundaryResponse boundaryRespose = null;
        boundaryRespose = restExternalMasterService.getBoundaryId(
                WcmsConstants.LOCALITY,
                WcmsConstants.LOCATION,
                treatmentPlant.getTenantId());
        if (boundaryRespose.getBoundarySize() && boundaryRespose.getBoundarys().get(0) != null) {
            isValidBoundaryByLocation = Boolean.TRUE;
            treatmentPlant.setLocation(
                    boundaryRespose.getBoundarys() != null && boundaryRespose.getBoundarys().get(0) != null
                            ? boundaryRespose.getBoundarys().get(0).getId() : "");

        }
        return isValidBoundaryByLocation;

    }

    public boolean getTreatmentPlantByNameAndCode(final String code, final String name, final String tenantId) {
        return treatmentPlantRepository.checkTreatmentPlantByNameAndCode(code, name, tenantId);
    }

    public boolean checkStorageReservoirExists(final String storageReservoirName, final String tenantId) {
        return treatmentPlantRepository.checkStorageReservoirExists(storageReservoirName, tenantId);
    }

}
