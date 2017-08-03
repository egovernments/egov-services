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
package org.egov.collection.indexer.consumer;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.collection.indexer.contract.ReceiptRequest;
import org.egov.collection.indexer.repository.ElasticSearchRepository;
import org.egov.collection.indexer.repository.contract.ReceiptRequestDocument;
import org.egov.collection.indexer.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class IndexerListener {


    private ElasticSearchRepository elasticSearchRepository;
    private ObjectMapper objectMapper;
    private DocumentService documentService;

    @Autowired
    public IndexerListener(ElasticSearchRepository elasticSearchRepository,
                           ObjectMapper objectMapper,
                           DocumentService documentService) {
        this.elasticSearchRepository = elasticSearchRepository;
        this.objectMapper = objectMapper;
        this.documentService = documentService;
    }

    @KafkaListener(topics = "${kafka.topics.egov.index.name}")
    public void listen(HashMap<String, Object> receiptRequestMap) {
        ReceiptRequest receiptRequest = objectMapper.convertValue(receiptRequestMap, ReceiptRequest.class);
        final List<ReceiptRequestDocument> documents = documentService.enrich(receiptRequest);
        log.debug("Documents pushed to elastic search : "+ documents.size() + " and ",documents);
        elasticSearchRepository.index(documents);
    }
}
