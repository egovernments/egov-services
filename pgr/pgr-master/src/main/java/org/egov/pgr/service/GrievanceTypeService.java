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
package org.egov.pgr.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.egov.pgr.domain.model.GrievanceType;
import org.egov.pgr.producers.PGRProducer;
import org.egov.pgr.repository.GrievanceTypeRepository;
import org.egov.pgr.web.contract.ServiceGetRequest;
import org.egov.pgr.web.contract.ServiceRequest;
import org.egov.pgr.web.contract.SevaConfigurationGetRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class GrievanceTypeService {

    public static final Logger logger = LoggerFactory.getLogger(GrievanceTypeService.class);

    @Autowired
    private GrievanceTypeRepository grievanceRepository;

    @Autowired
    private PGRProducer pgrProducer;

    @Autowired
    private SevaConfigurationService sevaConfigurationService;

    public ServiceRequest create(final ServiceRequest serviceRequest) {
        return grievanceRepository.persistServiceType(serviceRequest);
    }

    public ServiceRequest update(final ServiceRequest serviceRequest) {
        return grievanceRepository.persistModifyServiceType(serviceRequest);
    }

    public GrievanceType createServiceType(final String topic, final String key, final ServiceRequest serviceRequest) {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String serviceRequestValue = null;
        try {
            logger.info("Service Type service::" + serviceRequest);
            if (null != serviceRequest.getService().getConfig()) {
                Map<String, List<String>> serviceTypes = sevaConfigurationService.getSevaConfigurations(
                        SevaConfigurationGetRequest.builder().keyName(serviceRequest.getService().getConfig().get(0))
                                .tenantId(serviceRequest.getService().getTenantId()).build());

                List<String> configValue = Collections.singletonList("true");

                if (null != serviceTypes.get("sla.enabled") && serviceTypes.get("sla.enabled").stream().anyMatch(configValue::contains)
                        && serviceRequest.getService().getSlaHours() == null) {
                    serviceRequest.getService().setSlaHours(0);
                }
            }
            serviceRequestValue = mapper.writeValueAsString(serviceRequest);
            logger.info("Service Type Value::" + serviceRequestValue);
        } catch (final JsonProcessingException e) {
            logger.error("Exception Encountered : " + e);
        }
        try {
            pgrProducer.sendMessage(topic, key, serviceRequestValue);
        } catch (final Exception ex) {
            logger.error("Exception Encountered : " + ex);
        }
        return serviceRequest.getService();
    }

    public GrievanceType updateServices(final String topic, final String key, final ServiceRequest servicesRequest) {
        final ObjectMapper mapper = new ObjectMapper();
        String servicesValues = null;
        try {
            logger.info("Update Service Type Service::" + servicesRequest);
            if (null != servicesRequest.getService().getConfig()) {
                Map<String, List<String>> serviceTypes = sevaConfigurationService.getSevaConfigurations(
                        SevaConfigurationGetRequest.builder().keyName(servicesRequest.getService().getConfig().get(0))
                                .tenantId(servicesRequest.getService().getTenantId()).build());
                List<String> configValue = Collections.singletonList("true");
                if (null != serviceTypes.get("sla.enabled") && serviceTypes.get("sla.enabled").stream().anyMatch(configValue::contains)
                        && servicesRequest.getService().getSlaHours() == null) {
                    servicesRequest.getService().setSlaHours(0);
                }
            }
            servicesValues = mapper.writeValueAsString(servicesRequest);
            logger.info("Service Type Value::" + servicesValues);
        } catch (final JsonProcessingException e) {
            logger.error("Exception Encountered : " + e);
        }
        try {
            pgrProducer.sendMessage(topic, key, servicesValues);
        } catch (final Exception ex) {
            logger.error("Exception Encountered : " + ex);
        }
        return servicesRequest.getService();
    }

    public boolean getCategoryByNameAndCode(final String code, final String name, final String tenantId) {
        return grievanceRepository.checkServiceByNameAndCode(code, name, tenantId);
    }

    public List<GrievanceType> getServiceTypes(final ServiceGetRequest serviceGetRequest) {
        return grievanceRepository.findForCriteria(serviceGetRequest);
    }

    public boolean checkServiceCodeIfExists(final String serviceCode, final String tenantId) {
        return grievanceRepository.checkServiceCodeIfExists(serviceCode, tenantId);
    }

    public boolean checkComplaintNameIfExists(final String serviceName, final String tenantId,
                                              final String serviceCode, final String mode) {
        return grievanceRepository.checkComplaintNameIfExists(serviceName, tenantId, serviceCode, mode);
    }

    public boolean checkComplaintCodeNameIfExists(final String serviceName, final String tenantId,
                                              final String serviceCode, final String mode) {
        return grievanceRepository.checkComplaintCodeNameIfExists(serviceName, tenantId, serviceCode, mode);
    }

}
