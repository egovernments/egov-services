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
package org.egov.wcms.service;

import java.util.List;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.wcms.model.PipeSize;
import org.egov.wcms.repository.PipeSizeRepository;
import org.egov.wcms.web.contract.PipeSizeGetRequest;
import org.egov.wcms.web.contract.PipeSizeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PipeSizeService {

    @Autowired
    private PipeSizeRepository pipeSizeRepository;

    @Autowired
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private CodeGeneratorService codeGeneratorService;

    public PipeSizeRequest create(final PipeSizeRequest pipeSizeRequest) {
        return pipeSizeRepository.create(pipeSizeRequest);
    }

    public PipeSizeRequest update(final PipeSizeRequest pipeSizeRequest) {
        return pipeSizeRepository.update(pipeSizeRequest);
    }

    public List<PipeSize> pushCreateToQueue(final String topic, final String key, final PipeSizeRequest pipeSizeRequest) {
        for (final PipeSize pipeSize : pipeSizeRequest.getPipeSizes()) {
            pipeSize.setCode(codeGeneratorService.generate(PipeSize.SEQ_PIPESIZE));
            final double pipeSizeininch = pipeSize.getSizeInMilimeter() * 0.039370;
            pipeSize.setSizeInInch(Math.round(pipeSizeininch * 1000.0) / 1000.0);
        }
        try {
            kafkaTemplate.send(topic, key, pipeSizeRequest);
        } catch (final Exception ex) {
            log.error("Exception Encountered : " + ex);
        }
        return pipeSizeRequest.getPipeSizes();
    }

    public List<PipeSize> pushUpdateToQueue(final String topic, final String key, final PipeSizeRequest pipeSizeRequest) {
        for (final PipeSize pipeSize : pipeSizeRequest.getPipeSizes()) {
            final double pipeSizeininch = pipeSize.getSizeInMilimeter() * 0.039370;
            pipeSize.setSizeInInch(Math.round(pipeSizeininch * 1000.0) / 1000.0);
        }
        try {
            kafkaTemplate.send(topic, key, pipeSizeRequest);
        } catch (final Exception ex) {
            log.error("Exception Encountered : " + ex);
        }
        return pipeSizeRequest.getPipeSizes();
    }

    public boolean getPipeSizeInmmAndCode(final String code, final Double sizeInMilimeter, final String tenantId) {
        return pipeSizeRepository.checkPipeSizeInmmAndCode(code, sizeInMilimeter, tenantId);
    }

    public List<PipeSize> getPipeSizes(final PipeSizeGetRequest pipeSizeGetRequest) {
        return pipeSizeRepository.findForCriteria(pipeSizeGetRequest);

    }

}
