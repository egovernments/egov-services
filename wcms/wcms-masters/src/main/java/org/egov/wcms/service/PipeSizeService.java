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


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.wcms.model.ConnectionCategory;
import org.egov.wcms.model.PipeSize;
import org.egov.wcms.producers.ConnectionCategoryProducer;
import org.egov.wcms.producers.PipeSizeProducer;
import org.egov.wcms.repository.ConnectionCategoryRepository;
import org.egov.wcms.repository.PipeSizeRepository;
import org.egov.wcms.web.contract.CategoryGetRequest;
import org.egov.wcms.web.contract.ConnectionCategoryRequest;
import org.egov.wcms.web.contract.PipeSizeGetRequest;
import org.egov.wcms.web.contract.PipeSizeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PipeSizeService {

    public static final Logger logger = LoggerFactory.getLogger(PipeSizeService.class);

    @Autowired
    private PipeSizeRepository pipeSizeRepository;

    @Autowired
    private PipeSizeProducer pipeSizeProducer;

    @Autowired
    private CodeGeneratorService codeGeneratorService;


    public PipeSizeRequest create(final PipeSizeRequest pipeSizeRequest) {
        return pipeSizeRepository.persistCreatePipeSize(pipeSizeRequest);
    }

    public PipeSizeRequest update(final PipeSizeRequest pipeSizeRequest) {
        return pipeSizeRepository.persistModifyPipeSize(pipeSizeRequest);
    }


    public PipeSize createPipeSize(final String topic, final String key, final PipeSizeRequest pipeSizeRequest) {
        pipeSizeRequest.getPipeSize().setCode(codeGeneratorService.generate(pipeSizeRequest.getPipeSize().SEQ_PIPESIZE));
        final double pipeSizeininch = pipeSizeRequest.getPipeSize().getSizeInMilimeter() * 0.039370;
        pipeSizeRequest.getPipeSize().setSizeInInch(Math.round(pipeSizeininch * 1000.0) / 1000.0);
        final ObjectMapper mapper = new ObjectMapper();
        String pipeSizeValue = null;
        try {
            logger.info("createPipeSize service::" + pipeSizeRequest);
            pipeSizeValue = mapper.writeValueAsString(pipeSizeRequest);
            logger.info("pipeSizeValue::" + pipeSizeValue);
        } catch (final JsonProcessingException e) {
            e.printStackTrace();
        }
        try {
            pipeSizeProducer.sendMessage(topic,key,pipeSizeValue);
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        return pipeSizeRequest.getPipeSize();
    }

    public PipeSize updatePipeSize(final String topic,final String key,final PipeSizeRequest pipeSizeRequest) {
        final ObjectMapper mapper = new ObjectMapper();
        final double pipeSizeininch = pipeSizeRequest.getPipeSize().getSizeInMilimeter() * 0.039370;
        pipeSizeRequest.getPipeSize().setSizeInInch(Math.round(pipeSizeininch * 1000.0) / 1000.0);
        String pipeSizeValue = null;
        try {
            logger.info("updatePipeSize service::" + pipeSizeRequest);
            pipeSizeValue = mapper.writeValueAsString(pipeSizeRequest);
            logger.info("pipeSizeValue::" + pipeSizeValue);
        } catch (final JsonProcessingException e) {
            e.printStackTrace();
        }
        try {
            pipeSizeProducer.sendMessage(topic,key,pipeSizeValue);
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        return pipeSizeRequest.getPipeSize();
    }


    public boolean getPipeSizeInmmAndCode(final String code,final Double sizeInMilimeter,final String tenantId) {
        return pipeSizeRepository.checkPipeSizeInmmAndCode(code,sizeInMilimeter,tenantId);
    }

    public List<PipeSize> getPipeSizes(final PipeSizeGetRequest pipeSizeGetRequest) {
        return pipeSizeRepository.findForCriteria(pipeSizeGetRequest);

    }


}
