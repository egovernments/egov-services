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

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.collection.indexer.contract.ReceiptRequest;
import org.egov.collection.indexer.repository.ElasticSearchRepository;
import org.egov.collection.indexer.repository.contract.ReceiptRequestDocument;
import org.egov.collection.indexer.service.DocumentService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class IndexerListenerTest {

    @Mock
    private ElasticSearchRepository elasticSearchRepository;

    @Mock
    private DocumentService documentService;

    private IndexerListener indexerListener;

    @Before
    public void before() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        indexerListener = new IndexerListener(elasticSearchRepository, objectMapper, documentService);
    }

    @Test
    public void test_should_index_document() {
        final List<ReceiptRequestDocument> expectedDocumentToIndex = new ArrayList<ReceiptRequestDocument>();
        final HashMap<String, Object> sevaRequestMap = getReceiptRequestMap();
        when(documentService.enrich(any(ReceiptRequest.class))).thenReturn(expectedDocumentToIndex);

        indexerListener.listen(sevaRequestMap);

        verify(elasticSearchRepository).index(expectedDocumentToIndex);
    }

    private HashMap<String, Object> getReceiptRequestMap() {
        final HashMap<String, Object> receiptRequestMap = new HashMap<>();
        final HashMap<String, Object> receiptRequest = new HashMap<>();
        final ArrayList<HashMap<String, String>> attributeEntries = new ArrayList<>();
        final HashMap<String, String> statusEntry = new HashMap<>();
        statusEntry.put("key", "status");
        statusEntry.put("name", "REGISTERED");
        attributeEntries.add(statusEntry);
        receiptRequest.put("attribValues", attributeEntries);
        receiptRequestMap.put("receiptRequest", receiptRequest);
        final HashMap<String, Object> requestInfo = new HashMap<>();
        requestInfo.put("action", "POST");
        receiptRequestMap.put("RequestInfo", requestInfo);

        return receiptRequestMap;
    }
}
