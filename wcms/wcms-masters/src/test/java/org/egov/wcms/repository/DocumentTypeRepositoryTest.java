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

package org.egov.wcms.repository;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.wcms.model.AuditDetails;
import org.egov.wcms.model.DocumentType;
import org.egov.wcms.repository.builder.DocumentTypeQueryBuilder;
import org.egov.wcms.repository.rowmapper.DocumentTypeRowMapper;
import org.egov.wcms.web.contract.DocumentTypeGetReq;
import org.egov.wcms.web.contract.DocumentTypeReq;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class DocumentTypeRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private DocumentTypeQueryBuilder docTypeQueryBuilder;

    @Mock
    private DocumentTypeRowMapper docTypeRowMapper;
    
    @InjectMocks
    private DocumentTypeRepository docTypeRepository;
    
    @Test
    public void test_Should_Create_DocumentType_Valid() {
    	DocumentTypeReq docTypeRequest = getDocumentTypeRequest();
        when(jdbcTemplate.update(any(String.class), any(Object[].class))).thenReturn(1);
        assertTrue(docTypeRequest.equals(docTypeRepository.persistCreateDocumentType(docTypeRequest)));
    }
    
    @Test
    public void test_Should_Create_DocumentType_Invalid() {
    	DocumentTypeReq docTypeRequest = getDocumentTypeRequest();
        DocumentType docType = docTypeRequest.getDocumentType();
        when(jdbcTemplate.update(any(String.class), any(Object[].class))).thenReturn(1);
        assertTrue(!docType.equals(docTypeRepository.persistCreateDocumentType(docTypeRequest)));
    }
    
    @Test
	public void test_Should_Find_DocumentType_Valid() {
		List<Object> preparedStatementValues = new ArrayList<Object>();
		DocumentTypeGetReq docTypeGetRequest = Mockito.mock(DocumentTypeGetReq.class);
		String queryString = "MyQuery" ;
		when(docTypeQueryBuilder.getQuery(docTypeGetRequest, preparedStatementValues)).thenReturn(queryString);
		List<DocumentType> docTypes = new ArrayList<>();
		when(jdbcTemplate.query(queryString, preparedStatementValues.toArray(), docTypeRowMapper))
				.thenReturn(docTypes);

		assertTrue(
				docTypes.equals(docTypeRepository.findForCriteria(docTypeGetRequest)));
	}

    private DocumentTypeReq getDocumentTypeRequest(){
    	DocumentTypeReq docTypeRequest = new DocumentTypeReq();
    	DocumentType docType = new DocumentType();
    	docType.setCode("23");
    	docType.setName("New Document Name");
    	docType.setDescription("Document Name Description");
    	docType.setActive(true);
    	RequestInfo requestInfo = new RequestInfo();
    	User newUser = new User();
    	newUser.setId(2L);
    	requestInfo.setUserInfo(newUser);
    	docTypeRequest.setRequestInfo(requestInfo);
    	docTypeRequest.setDocumentType(docType);
    	return docTypeRequest;
    }
    
	@Test(expected = Exception.class)
	public void test_throwException__FindforCriteria() throws Exception{
        List<Object> preparedStatementValues = new ArrayList<Object>();
        DocumentTypeGetReq docTypeGetRequest = new DocumentTypeGetReq();
        when(docTypeQueryBuilder.getQuery(docTypeGetRequest, preparedStatementValues)).thenThrow(Exception.class);
        List<DocumentType> docTypes = docTypeRepository.findForCriteria(docTypeGetRequest);
        
        assertTrue(docTypes != null);
        
	}
	
	@Test
	public void test_Should_Modify_DocumentType() throws Exception{
        DocumentTypeReq docTypeRequest = new DocumentTypeReq();
        
        RequestInfo requestInfo = new RequestInfo();
        User user = new User();
        user.setId(1L);
        requestInfo.setUserInfo(user);
        DocumentType docType = new  DocumentType();
        AuditDetails auditDetails = new AuditDetails();
        docType.setAuditDetails(auditDetails);
        docType.setCode("10");
        docType.setActive(true);
        docType.getAuditDetails().setCreatedBy(1L);
        docType.setName("test name");
        docType.setDescription("test description");
        docTypeRequest.setRequestInfo(requestInfo);
        docTypeRequest.setDocumentType(docType);
        
        assertNotNull(docTypeRepository.persistModifyDocumentType(docTypeRequest));
        
	}
	
	@Test(expected = Exception.class)
	public void test_throwException_Modify_DocumentType() throws Exception{
        DocumentTypeReq docTypeRequest = new DocumentTypeReq();
        RequestInfo requestInfo = new RequestInfo();
        DocumentType docType = new DocumentType();
        docType.setCode("10");
        docType.setActive(true);
        docType.getAuditDetails().setCreatedBy(1L);
        docType.setName("test name");
        docType.setDescription("test description");
        
        docTypeRequest.setRequestInfo(requestInfo);
        docTypeRequest.setDocumentType(docType);
        
        
        assertNotNull(docTypeRepository.persistModifyDocumentType(docTypeRequest));
        
        
	}
    
}