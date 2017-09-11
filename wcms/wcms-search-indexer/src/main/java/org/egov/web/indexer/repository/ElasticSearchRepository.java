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
package org.egov.web.indexer.repository;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.apache.tomcat.util.codec.binary.Base64;
import org.egov.web.indexer.contract.ConnectionDocument;
import org.egov.web.indexer.contract.ConnectionIndex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ElasticSearchRepository {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchRepository.class);

    private static final String AUTHORIZATION = "Authorization";
    private static final String US_ASCII = "US-ASCII";
    private static final String BASIC_AUTH = "Basic %s";
    private final RestTemplate restTemplate;
    private final String indexServiceHost;
    private final String userName;
    private final String password;
    private final String indexName;
    private final String documentType;

    public ElasticSearchRepository(RestTemplate restTemplate,
                                   @Value("${egov.services.esindexer.host}") String indexServiceHost,
                                   @Value("${egov.services.esindexer.username}") String userName,
                                   @Value("${egov.services.esindexer.password}") String password,
                                   @Value("${es.index.name}") String indexName,
                                   @Value("${es.document.type}") String documentType) {
        this.restTemplate = restTemplate;
        this.indexServiceHost = indexServiceHost;
        this.userName = userName;
        this.password = password;
        this.indexName = indexName;
        this.documentType = documentType;
    }

    public void index(List<ConnectionDocument> documents) {
        for(ConnectionDocument document : documents) {
             String url = String.format("%s%s/%s/", this.indexServiceHost, indexName, documentType);
             HttpHeaders headers = getHttpHeaders();
             restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(document, headers), Map.class);
       }
     }

    public void saveConnection(ConnectionIndex connectionIndex) {
    	String url = String.format("%s%s/%s/%s", this.indexServiceHost, indexName, documentType, connectionIndex.getConnectionDetails().getId());
        HttpHeaders headers = getHttpHeaders();
        LOGGER.info("Connection Index to be added to ES : " + connectionIndex);
        LOGGER.info("URL to invoke : " + url);
        restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(connectionIndex.getConnectionDetails(), headers), Map.class);
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION, getBase64Value(userName, password));
        return headers;
    }

    private String getBase64Value(String userName, String password) {
        String authString = String.format("%s:%s", userName, password);
        byte[] encodedAuthString = Base64.encodeBase64(authString.getBytes(Charset.forName(US_ASCII)));
        return String.format(BASIC_AUTH, new String(encodedAuthString));
    }

    public void updateConnection(ConnectionIndex connectionIndex) {
    	String url = String.format("%s%s/%s/%s", this.indexServiceHost, indexName, documentType, connectionIndex.getConnectionDetails().getId());
        HttpHeaders headers = getHttpHeaders();
        restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(connectionIndex.getConnectionDetails(), headers), Map.class);
    }
}