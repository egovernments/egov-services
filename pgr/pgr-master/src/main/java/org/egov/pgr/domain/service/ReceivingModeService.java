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

package org.egov.pgr.domain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.pgr.domain.service.validator.ReceivingModeValidator;
import org.egov.pgr.producers.PGRProducer;
import org.egov.pgr.repository.ReceivingModeRepository;
import org.egov.pgr.service.ReceivingCenterTypeService;
import org.egov.pgr.web.contract.ReceivingMode;
import org.egov.pgr.web.contract.ReceivingModeRequest;
import org.egov.pgr.web.contract.ReceivingModeTypeGetReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReceivingModeService {

    public static final Logger logger = LoggerFactory.getLogger(ReceivingCenterTypeService.class);

    @Autowired
    private ReceivingModeRepository receivingModeRepository;

    @Autowired
    private PGRProducer pgrProducer;

    @Autowired
    private List<ReceivingModeValidator> validators;

    public ReceivingModeRequest create(final ReceivingModeRequest modeTypeReq) {
        return receivingModeRepository.persistReceivingModeType(modeTypeReq);
    }

    public ReceivingModeRequest update(final ReceivingModeRequest modeTypeReq) {
        return receivingModeRepository.persistModifyReceivingModeType(modeTypeReq);
    }

/*    public List<ReceivingMode> getAllReceivingModeTypes(final ReceivingModeTypeGetReq modeTypeGetRequest) {
        return receivingModeRepository.getAllReceivingModeTypes(modeTypeGetRequest);

    }*/

    public boolean checkReceivingModeTypeByCode(final String code, final String tenantId) {
        return receivingModeRepository.checkReceivingModeTypeByCode(code, tenantId);
    }

    public boolean checkReceivingModeTypeByName(final String code, final String name, final String tenantId) {
        return receivingModeRepository.checkReceivingModeTypeByName(code, name, tenantId);
    }


    public void sendMessage(String topic, String key, final org.egov.pgr.domain.model.ReceivingMode receivingMode) {

        final ObjectMapper mapper = new ObjectMapper();
        String receivingModeValue = null;
        validate(receivingMode);

        try {
            logger.info("createReceivingCModeType Request::" + receivingMode);
            receivingModeValue = mapper.writeValueAsString(receivingMode);
            logger.info("receivingModeValue::" + receivingModeValue);
        } catch (final JsonProcessingException e) {
            logger.error("Exception Encountered : " + e);
        }
        try {
            pgrProducer.sendMessage(topic, key, receivingModeValue);
        } catch (final Exception ex) {
            logger.error("Exception Encountered : " + ex);
        }
    }

    private void validate(org.egov.pgr.domain.model.ReceivingMode receivingMode) {
        validators.stream()
                .filter(v -> v.canValidate(receivingMode))
                .forEach(validator -> validator.validate(receivingMode));
    }
}
