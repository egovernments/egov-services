package org.egov.eis.service;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.egov.eis.model.Employee;
import org.egov.eis.model.EmployeeDocument;
import org.egov.eis.model.enums.EntityType;
import org.egov.eis.repository.EmployeeDocumentsRepository;
import org.egov.eis.utils.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class EmployeeDocumentsServiceTest {
	
	private EmployeeDocumentsService employeeDocumentsService;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Totally 6 documents are defined: 2 at employee level;
	 * 2 in first assignment, 2 in second assignment.
	 * When "save" method in repository is called, it is stubbed and adds the 
	 * added record into a "savedDocuments" list. We compare this with the expectedDocuments list
	 * for success. Also, note that the method EmployeeDocumentsRepository.findByEmployeeId() is stubbed
	 * with an empty list meaning there are no documents in the db for this employee. 
	 * So, all documents sent are saved in db. 
	 */
	@Test
	public void testUpdateDocumentsWithDBHavingNoDocuments() {
		final List<EmployeeDocument> savedDocuments = new ArrayList<>();
		final List<EmployeeDocument> deletedDocuments = new ArrayList<>();
		
		employeeDocumentsService = new EmployeeDocumentsService();
		
		// Set a stub of repository to employeeDocumentsService 
		employeeDocumentsService.setEmployeeDocumentsRepository(new EmployeeDocumentsRepository() {
			public int save(Long employeeId, String docUrl, String referenceType, Long referenceId, String tenantId) {
				savedDocuments.add(new EmployeeDocument(0L, employeeId, docUrl, referenceType, referenceId, tenantId));
				return 1;
			}
			public int delete(Long employeeId, String docUrl, String referenceType, Long referenceId, String tenantId) {
				deletedDocuments.add(new EmployeeDocument(0L, employeeId, docUrl, referenceType, referenceId, tenantId));
				return 1;
			}
			public List<EmployeeDocument> findByEmployeeId(Long id, String tenantId) {
				return new ArrayList<>();
			}
		});
		
		Employee employee = null;
		try {
			employee = getEmployee();
		} catch(Exception e) {
			e.printStackTrace();
			fail();
		}
		
		List<EmployeeDocument> documentsFromDb = new ArrayList<>();
		employeeDocumentsService.update(employee);
		
		List<EmployeeDocument> expectedDocuments = getExpectedEmployeeDocumentsForDBHavingNoDocuments();
		
		System.out.println("expectedDocuments = " + expectedDocuments);
		System.out.println("savedDocuments = " + savedDocuments);
		
		// The following two lines ensure expectedDocuments have save contents
		assertTrue(expectedDocuments.containsAll(savedDocuments));
		assertTrue(savedDocuments.containsAll(expectedDocuments));
		assertTrue(deletedDocuments.isEmpty());
	}
	
	private List<EmployeeDocument> getExpectedEmployeeDocumentsForDBHavingNoDocuments() {
		List<EmployeeDocument> employeeDocuments =  new ArrayList<>();
		employeeDocuments.add(new EmployeeDocument(0L, 100L, "http://www.egov.org/egovservices/employee/dob_100.pdf", 
				EntityType.EMPLOYEE_HEADER.getValue(), 100L, "1" ));
		employeeDocuments.add(new EmployeeDocument(0L, 100L, "http://www.egov.org/egovservices/employee/addr_proof_100.pdf", 
				EntityType.EMPLOYEE_HEADER.getValue(), 100L, "1" ));
		employeeDocuments.add(new EmployeeDocument(0L, 100L, "http://www.egov.org/egovservices/employee/100/assgn/1_1.pdf", 
				EntityType.ASSIGNMENT.getValue(), 5L, "1" ));
		employeeDocuments.add(new EmployeeDocument(0L, 100L, "http://www.egov.org/egovservices/employee/100/assgn/1_2.pdf", 
				EntityType.ASSIGNMENT.getValue(), 5L, "1" ));
		employeeDocuments.add(new EmployeeDocument(0L, 100L, "http://www.egov.org/egovservices/employee/100/assgn/2_1.pdf", 
				EntityType.ASSIGNMENT.getValue(), 6L, "1" ));
		employeeDocuments.add(new EmployeeDocument(0L, 100L, "http://www.egov.org/egovservices/employee/100/assgn/2_2.pdf", 
				EntityType.ASSIGNMENT.getValue(), 6L, "1" ));
		return employeeDocuments;
	}
	
	/**
	 * Totally 6 documents are defined: 2 at employee level;
	 * 2 in first assignment, 2 in second assignment.
	 * When "save" method in repository is called, it is stubbed and adds the 
	 * added record into a "savedDocuments" list. We compare this with the expectedDocuments list
	 * for success. Also, note that the method EmployeeDocumentsRepository.findByEmployeeId() is stubbed
	 * with a list having one document in employee, one document in assignment 1 and both the documents in 
	 * assignment 2 meaning there four documents in the db for this employee. 
	 * So, only 2 documents sent are saved in db. 
	 */
	@Test
	public void testUpdateDocumentsWithDBHavingSomeDocuments() {
		final List<EmployeeDocument> savedDocuments = new ArrayList<>();
		final List<EmployeeDocument> deletedDocuments = new ArrayList<>();
		
		employeeDocumentsService = new EmployeeDocumentsService();
		
		// Set a stub of repository to employeeDocumentsService 
		employeeDocumentsService.setEmployeeDocumentsRepository(new EmployeeDocumentsRepository() {
			public int save(Long employeeId, String docUrl, String referenceType, Long referenceId, String tenantId) {
				savedDocuments.add(new EmployeeDocument(0L, employeeId, docUrl, referenceType, referenceId, tenantId));
				return 1;
			}
			public int delete(Long employeeId, String docUrl, String referenceType, Long referenceId, String tenantId) {
				deletedDocuments.add(new EmployeeDocument(0L, employeeId, docUrl, referenceType, referenceId, tenantId));
				return 1;
			}
			public List<EmployeeDocument> findByEmployeeId(Long id, String tenantId) {
				List<EmployeeDocument> employeeDocuments =  new ArrayList<>();
				employeeDocuments.add(new EmployeeDocument(0L, 100L, "http://www.egov.org/egovservices/employee/addr_proof_100.pdf", 
						EntityType.EMPLOYEE_HEADER.getValue(), 100L, "1" ));
				employeeDocuments.add(new EmployeeDocument(0L, 100L, "http://www.egov.org/egovservices/employee/100/assgn/1_1.pdf", 
						EntityType.ASSIGNMENT.getValue(), 5L, "1" ));
				employeeDocuments.add(new EmployeeDocument(0L, 100L, "http://www.egov.org/egovservices/employee/100/assgn/2_1.pdf", 
						EntityType.ASSIGNMENT.getValue(), 6L, "1" ));
				employeeDocuments.add(new EmployeeDocument(0L, 100L, "http://www.egov.org/egovservices/employee/100/assgn/2_2.pdf", 
						EntityType.ASSIGNMENT.getValue(), 6L, "1" ));
				return employeeDocuments;
			}
		});
		
		Employee employee = null;
		try {
			employee = getEmployee();
		} catch(Exception e) {
			e.printStackTrace();
			fail();
		}
		
		List<EmployeeDocument> documentsFromDb = new ArrayList<>();
		employeeDocumentsService.update(employee);
		List<EmployeeDocument> expectedDocuments = getExpectedEmployeeDocumentsForDBHavingSomeDocuments();
		
		System.out.println("expectedDocuments = " + expectedDocuments);
		System.out.println("savedDocuments = " + savedDocuments);
		// The following two lines ensure expectedDocuments have save contents
		assertTrue(expectedDocuments.containsAll(savedDocuments));
		assertTrue(savedDocuments.containsAll(expectedDocuments));
		assertTrue(deletedDocuments.isEmpty());
	}
	

	private List<EmployeeDocument> getExpectedEmployeeDocumentsForDBHavingSomeDocuments() {
		List<EmployeeDocument> employeeDocuments =  new ArrayList<>();
		employeeDocuments.add(new EmployeeDocument(0L, 100L, "http://www.egov.org/egovservices/employee/dob_100.pdf", 
				EntityType.EMPLOYEE_HEADER.getValue(), 100L, "1" ));
		employeeDocuments.add(new EmployeeDocument(0L, 100L, "http://www.egov.org/egovservices/employee/100/assgn/1_2.pdf", 
				EntityType.ASSIGNMENT.getValue(), 5L, "1" ));
		return employeeDocuments;
	}
	
	//------------------test Delete-------------------------------------//
	
	/**
	 * Totally 6 documents are defined: 2 at employee level;
	 * 2 in first assignment, 2 in second assignment.
	 * When "save" method in repository is called, it is stubbed and adds the 
	 * added record into a "savedDocuments" list. We compare this with the expectedDocuments list
	 * for success. 
	 * Also, note that the method EmployeeDocumentsRepository.findByEmployeeId() is stubbed
	 * with a list having one document in employee, one document in assignment 1 and both the documents in 
	 * assignment 2 meaning there four documents in the db for this employee. Also, 2 more documents that are not in 
	 * inputList is also present in DB. These two should be deleted in db which is taken care by a stubbed "delete"
	 * method in repository. This adds records in a "deletedList" which is used for assertion.
	 * So, only 2 documents sent are saved in db. 
	 */
	@Test
	public void testUpdateDocumentsInvolvingBothSaveAndDeleteSomeDocumentsInDB() {
		final List<EmployeeDocument> savedDocuments = new ArrayList<>();
		final List<EmployeeDocument> deletedDocuments = new ArrayList<>();
		
		employeeDocumentsService = new EmployeeDocumentsService();
		
		// Set a stub of repository to employeeDocumentsService 
		employeeDocumentsService.setEmployeeDocumentsRepository(new EmployeeDocumentsRepository() {
			public int save(Long employeeId, String docUrl, String referenceType, Long referenceId, String tenantId) {
				savedDocuments.add(new EmployeeDocument(0L, employeeId, docUrl, referenceType, referenceId, tenantId));
				return 1;
			}
			public int delete(Long employeeId, String docUrl, String referenceType, Long referenceId, String tenantId) {
				deletedDocuments.add(new EmployeeDocument(0L, employeeId, docUrl, referenceType, referenceId, tenantId));
				return 1;
			}
			public List<EmployeeDocument> findByEmployeeId(Long id, String tenantId) {
				List<EmployeeDocument> employeeDocuments =  new ArrayList<>();
				employeeDocuments.add(new EmployeeDocument(0L, 100L, "http://www.egov.org/egovservices/employee/addr_proof_100.pdf", 
						EntityType.EMPLOYEE_HEADER.getValue(), 100L, "1" ));
				employeeDocuments.add(new EmployeeDocument(0L, 100L, "http://www.egov.org/egovservices/employee/perm_addr_proof_100.pdf", 
						EntityType.EMPLOYEE_HEADER.getValue(), 100L, "1" ));
				employeeDocuments.add(new EmployeeDocument(0L, 100L, "http://www.egov.org/egovservices/employee/100/assgn/1_1.pdf", 
						EntityType.ASSIGNMENT.getValue(), 5L, "1" ));
				employeeDocuments.add(new EmployeeDocument(0L, 100L, "http://www.egov.org/egovservices/employee/100/assgn/2_1.pdf", 
						EntityType.ASSIGNMENT.getValue(), 6L, "1" ));
				employeeDocuments.add(new EmployeeDocument(0L, 100L, "http://www.egov.org/egovservices/employee/100/assgn/2_2.pdf", 
						EntityType.ASSIGNMENT.getValue(), 6L, "1" ));
				employeeDocuments.add(new EmployeeDocument(0L, 100L, "http://www.egov.org/egovservices/employee/100/serv/1_1.pdf", 
						EntityType.SERVICE.getValue(), 10L, "1" ));
				return employeeDocuments;
			}
		});
		
		Employee employee = null;
		try {
			employee = getEmployee();
		} catch(Exception e) {
			e.printStackTrace();
			fail();
		}
		
		List<EmployeeDocument> documentsFromDb = new ArrayList<>();
		employeeDocumentsService.update(employee);
		List<EmployeeDocument> expectedDocumentsToSave = getExpectedEmployeeDocumentsForSaveInvolvingBothSaveAndDeleteSomeDocumentsInDB();
		List<EmployeeDocument> expectedDocumentsToDelete = getExpectedEmployeeDocumentsForDeleteInvolvingBothSaveAndDeleteSomeDocumentsInDB();
		
		System.out.println("expectedDocuments = " + expectedDocumentsToSave);
		System.out.println("savedDocuments = " + savedDocuments);
		System.out.println("expectedDocumentsToDelete = " + expectedDocumentsToDelete);
		System.out.println("deletedDocuments = " + deletedDocuments);
		
		// The following two lines ensure expectedDocuments have same contents
		assertTrue(expectedDocumentsToSave.containsAll(savedDocuments));
		assertTrue(savedDocuments.containsAll(expectedDocumentsToSave));
		
		// The following two lines ensure expectedDocumentsToDelete have same contents
		assertTrue(expectedDocumentsToDelete.containsAll(deletedDocuments));
		assertTrue(deletedDocuments.containsAll(expectedDocumentsToDelete));
	}

	private List<EmployeeDocument> getExpectedEmployeeDocumentsForSaveInvolvingBothSaveAndDeleteSomeDocumentsInDB() {
		List<EmployeeDocument> employeeDocuments =  new ArrayList<>();
		employeeDocuments.add(new EmployeeDocument(0L, 100L, "http://www.egov.org/egovservices/employee/dob_100.pdf", 
				EntityType.EMPLOYEE_HEADER.getValue(), 100L, "1" ));
		employeeDocuments.add(new EmployeeDocument(0L, 100L, "http://www.egov.org/egovservices/employee/100/assgn/1_2.pdf", 
				EntityType.ASSIGNMENT.getValue(), 5L, "1" ));
		return employeeDocuments;
	}
	
	private List<EmployeeDocument> getExpectedEmployeeDocumentsForDeleteInvolvingBothSaveAndDeleteSomeDocumentsInDB() {
		List<EmployeeDocument> employeeDocuments =  new ArrayList<>();
		employeeDocuments.add(new EmployeeDocument(0L, 100L, "http://www.egov.org/egovservices/employee/perm_addr_proof_100.pdf", 
				EntityType.EMPLOYEE_HEADER.getValue(), 100L, "1" ));
		employeeDocuments.add(new EmployeeDocument(0L, 100L, "http://www.egov.org/egovservices/employee/100/serv/1_1.pdf", 
				EntityType.SERVICE.getValue(), 10L, "1" ));
		return employeeDocuments;
	}
	//-------------------test Delete ends here--------------------------//

	private Employee getEmployee() throws IOException {
		String empJson = new FileUtils().getFileContents("org/egov/eis/service/EmployeeDocumentService.employees1.json");
		return new ObjectMapper().readValue(empJson, Employee.class);
	}

//	@Test
	public void testPopulateDocumentsInRespectiveObjects() {
		fail("Not yet implemented");
	}

}
