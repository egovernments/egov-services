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

package org.egov.pgr.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.pgr.domain.model.ReceivingModeType;
import org.egov.pgr.producers.PGRProducer;
import org.egov.pgr.repository.ReceivingModeTypeRepository;
import org.egov.pgr.web.contract.ReceivingModeTypeGetReq;
import org.egov.pgr.web.contract.ReceivingModeTypeReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReceivingModeTypeService {

    public static final Logger logger = LoggerFactory.getLogger(ReceivingCenterTypeService.class);

    @Autowired
    private ReceivingModeTypeRepository receivingModeRepository;

    @Autowired
    private PGRProducer pgrProducer;

    public ReceivingModeTypeReq create(final ReceivingModeTypeReq modeTypeReq) {
        return receivingModeRepository.persistReceivingModeType(modeTypeReq);
    }

    public ReceivingModeTypeReq update(final ReceivingModeTypeReq modeTypeReq) {
        return receivingModeRepository.persistModifyReceivingModeType(modeTypeReq);
    }

    public List<ReceivingModeType> getAllReceivingModeTypes(final ReceivingModeTypeGetReq modeTypeGetRequest) {
        return receivingModeRepository.getAllReceivingModeTypes(modeTypeGetRequest);

    }

    public boolean checkReceivingModeTypeByNameAndCode(final String code, final String name, final String tenantId, String mode) {
        return receivingModeRepository.checkReceivingModeTypeByNameAndCode(code, name, tenantId, mode);
    }

    public boolean checkReceivingModeTypeByName(final String code, final String name, final String tenantId, final String mode) {
        return receivingModeRepository.checkReceivingModeTypeByName(code, name, tenantId, mode);
    }

    public boolean checkReceivingModeTypeByCode(final String code, final String tenantId, final String mode) {
        return receivingModeRepository.checkReceivingModeTypeByCode(code, tenantId, mode);
    }


    public ReceivingModeType sendMessage(String topic, String key, final ReceivingModeTypeReq modeTypeRequest) {

        final ObjectMapper mapper = new ObjectMapper();
        String receivingModeValue = null;

        try {
            logger.info("createReceivingCModeType Request::" + modeTypeRequest);
            receivingModeValue = mapper.writeValueAsString(modeTypeRequest);
            logger.info("receivingModeValue::" + receivingModeValue);
        } catch (final JsonProcessingException e) {
            logger.error("Exception Encountered : " + e);
        }
        try {
            pgrProducer.sendMessage(topic, key, receivingModeValue);
        } catch (final Exception ex) {
            logger.error("Exception Encountered : " + ex);
        }
        return modeTypeRequest.getModeType();
    }

}
