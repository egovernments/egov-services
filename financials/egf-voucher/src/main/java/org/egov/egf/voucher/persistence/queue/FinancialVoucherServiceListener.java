/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *      accountability and the service delivery of the government  organizations.
 *  
 *       Copyright (C) <2015>  eGovernments Foundation
 *  
 *       The updated version of eGov suite of products as by eGovernments Foundation
 *       is available at http://www.egovernments.org
 *  
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       any later version.
 *  
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *  
 *       You should have received a copy of the GNU General Public License
 *       along with this program. If not, see http://www.gnu.org/licenses/ or
 *       http://www.gnu.org/licenses/gpl.html .
 *  
 *       In addition to the terms of the GPL license to be adhered to in using this
 *       program, the following additional terms are to be complied with:
 *  
 *           1) All versions of this program, verbatim or modified must carry this
 *              Legal Notice.
 *  
 *           2) Any misrepresentation of the origin of the material is prohibited. It
 *              is required that all modified versions of this material be marked in
 *              reasonable ways as different from the original version.
 *  
 *           3) This license does not grant any rights to any user of the program
 *              with regards to rights under trademark law for use of the trade names
 *              or trademarks of eGovernments Foundation.
 *  
 *     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.egf.voucher.persistence.queue;

import java.util.HashMap;

import org.egov.egf.voucher.domain.model.Voucher;
import org.egov.egf.voucher.domain.model.VoucherSubType;
import org.egov.egf.voucher.domain.service.VoucherService;
import org.egov.egf.voucher.domain.service.VoucherSubTypeService;
import org.egov.egf.voucher.web.contract.VoucherContract;
import org.egov.egf.voucher.web.contract.VoucherSubTypeContract;
import org.egov.egf.voucher.web.requests.VoucherRequest;
import org.egov.egf.voucher.web.requests.VoucherSubTypeRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FinancialVoucherServiceListener {

    @Value("${egov.services.egf.voucher.completed.topic}")
    private String completedTopic;

    @Value("${egov.services.egf.voucher.completed.key}")
    private String voucherCompletedKey;

    @Value("${egov.services.egf.voucher.vouchersubtype.completed.key}")
    private String voucherSubTypeCompletedKey;

    @Autowired
    private ObjectMapper objectMapper;

    private ObjectMapperFactory objectMapperFactory;

    @Autowired
    private FinancialProducer financialProducer;

    @Autowired
    private VoucherService voucherService;

    @Autowired
    private VoucherSubTypeService voucherSubTypeService;

    @KafkaListener(id = "${egov.services.egf-voucher.workflow.populated.id}", topics = {
            "${egov.services.egf-voucher.workflow.populated.topic}",
            "${egov.services.egf.voucher.subtype.validated.topic}" }, group = "${egov.services.egf-voucher.workflow.populated.group}")
    public void process(final HashMap<String, Object> mastersMap) {

        objectMapperFactory = new ObjectMapperFactory(objectMapper);
        ModelMapper mapper = new ModelMapper();

        if (mastersMap.get("voucher_create") != null) {

            final VoucherRequest request = objectMapperFactory.create().convertValue(mastersMap.get("voucher_create"),
                    VoucherRequest.class);

            for (VoucherContract voucherContract : request.getVouchers()) {
                final Voucher domain = mapper.map(voucherContract, Voucher.class);
                voucherService.save(domain);
            }

            mastersMap.clear();
            mastersMap.put("voucher_persisted", request);
            financialProducer.sendMessage(completedTopic, voucherCompletedKey, mastersMap);
        }

        if (mastersMap.get("voucher_update") != null) {

            final VoucherRequest request = objectMapperFactory.create().convertValue(mastersMap.get("voucher_update"),
                    VoucherRequest.class);

            for (final VoucherContract voucherContract : request.getVouchers()) {
                final Voucher domain = mapper.map(voucherContract, Voucher.class);
                voucherService.update(domain);
            }

            mastersMap.clear();
            mastersMap.put("voucher_persisted", request);
            financialProducer.sendMessage(completedTopic, voucherCompletedKey, mastersMap);
        }

        if (mastersMap.get("vouchersubtype_create") != null) {

            final VoucherSubTypeRequest request = objectMapperFactory.create().convertValue(
                    mastersMap.get("vouchersubtype_create"),
                    VoucherSubTypeRequest.class);

            for (final VoucherSubTypeContract voucherSubTypeContract : request.getVoucherSubTypes()) {
                final VoucherSubType domain = mapper.map(voucherSubTypeContract, VoucherSubType.class);
                voucherSubTypeService.save(domain);
            }

            mastersMap.clear();
            mastersMap.put("vouchersubtype_persisted", request);
            financialProducer.sendMessage(completedTopic, voucherSubTypeCompletedKey, mastersMap);
        }

        if (mastersMap.get("vouchersubtype_update") != null) {

            final VoucherSubTypeRequest request = objectMapperFactory.create().convertValue(
                    mastersMap.get("vouchersubtype_update"),
                    VoucherSubTypeRequest.class);

            for (final VoucherSubTypeContract voucherSubTypeContract : request.getVoucherSubTypes()) {
                final VoucherSubType domain = mapper.map(voucherSubTypeContract, VoucherSubType.class);
                voucherSubTypeService.update(domain);
            }

            mastersMap.clear();
            mastersMap.put("vouchersubtype_persisted", request);
            financialProducer.sendMessage(completedTopic, voucherSubTypeCompletedKey, mastersMap);
        }

    }

}
