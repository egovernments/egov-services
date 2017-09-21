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
import org.egov.wcms.model.SourceType;
import org.egov.wcms.repository.SourceTypeRepository;
import org.egov.wcms.web.contract.SourceTypeGetRequest;
import org.egov.wcms.web.contract.SourceTypeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SourceTypeService {

    @Autowired
    private SourceTypeRepository waterSourceTypeRepository;

    @Autowired
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private CodeGeneratorService codeGeneratorService;

    public SourceTypeRequest create(final SourceTypeRequest waterSourceRequest) {
        return waterSourceTypeRepository.create(waterSourceRequest);
    }

    public SourceTypeRequest update(final SourceTypeRequest waterSourceRequest) {
        return waterSourceTypeRepository.update(waterSourceRequest);
    }

    public List<SourceType> pushCreateToQueue(final String topic, final String key,
            final SourceTypeRequest sourcetypeRequest) {
        for (final SourceType sourceType : sourcetypeRequest.getSourceTypes())
            sourceType.setCode(codeGeneratorService.generate(SourceType.SEQ_WATERSOURCE));

        try {
            kafkaTemplate.send(topic, key, sourcetypeRequest);
        } catch (final Exception ex) {
            log.error("Exception Encountered : " + ex);
        }
        return sourcetypeRequest.getSourceTypes();
    }

    public List<SourceType> pushUpdateToQueue(final String topic, final String key,
            final SourceTypeRequest sourcetypeRequest) {

        try {
            kafkaTemplate.send(topic, key, sourcetypeRequest);
        } catch (final Exception ex) {
            log.error("Exception Encountered : " + ex);
        }
        return sourcetypeRequest.getSourceTypes();
    }

    public boolean getWaterSourceByNameAndCode(final String code, final String name, final String tenantId) {
        return waterSourceTypeRepository.checkWaterSourceTypeByNameAndCode(code, name, tenantId);
    }

    public List<SourceType> getWaterSourceTypes(final SourceTypeGetRequest waterSourceTypeGetRequest) {
        return waterSourceTypeRepository.findForCriteria(waterSourceTypeGetRequest);

    }

}
