package org.egov.eis.service;

import static org.junit.Assert.assertEquals;
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

/**
 * This test class could have been written using Mockito like
 * RegularisationServiceTest. Nevertheless, mocking of dependencies is done
 * without a framework and it is still a unit test.
 */
public class EmployeeDocumentsServiceTest {

	private EmployeeDocumentsService employeeDocumentsService;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * 1) Tests documents in all entities 2) DB has no documents stored. Hence
	 * all documents in input are inserted in db. Totally 15 documents are
	 * defined: 2 at employee level; 2 in first assignment, 2 in second
	 * assignment. Also, 1 or 2 documents each for all other entities like
	 * regularisation, education, serviceHistory etc. When "save" method in
	 * repository is called, it is stubbed and adds the added record into a
	 * "savedDocuments" list. We compare this with the expectedDocuments list
	 * for success. Also, note that the method
	 * EmployeeDocumentsRepository.findByEmployeeId() is stubbed with an empty
	 * list meaning there are no documents in the db for this employee. So, all
	 * documents sent are saved in db.
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
				deletedDocuments
						.add(new EmployeeDocument(0L, employeeId, docUrl, referenceType, referenceId, tenantId));
				return 1;
			}

			public List<EmployeeDocument> findByEmployeeId(Long id, String tenantId) {
				return new ArrayList<>();
			}
		});

		Employee employee = null;
		try {
			employee = getEmployee("org/egov/eis/service/EmployeeDocumentService.employees2.json");
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

		employeeDocumentsService.update(employee);

		List<EmployeeDocument> expectedDocuments = getExpectedEmployeeDocumentsForDBHavingNoDocuments();
		System.out.println("expectedDocuments = " + expectedDocuments);
		System.out.println("savedDocuments = " + savedDocuments);

		// The following two lines ensure expectedDocuments is identical to
		// savedDocuments
		assertTrue(expectedDocuments.containsAll(savedDocuments));
		assertEquals(expectedDocuments.size(), savedDocuments.size());
		assertTrue(deletedDocuments.isEmpty());
	}

	private List<EmployeeDocument> getExpectedEmployeeDocumentsForDBHavingNoDocuments() {
		List<EmployeeDocument> employeeDocuments = new ArrayList<>();
		employeeDocuments.add(new EmployeeDocument(0L, 100L, "http://www.egov.org/egovservices/employee/dob_100.pdf",
				EntityType.EMPLOYEE_HEADER.getValue(), 100L, "1"));
		employeeDocuments
				.add(new EmployeeDocument(0L, 100L, "http://www.egov.org/egovservices/employee/addr_proof_100.pdf",
						EntityType.EMPLOYEE_HEADER.getValue(), 100L, "1"));
		employeeDocuments
				.add(new EmployeeDocument(0L, 100L, "http://www.egov.org/egovservices/employee/100/assgn/1_1.pdf",
						EntityType.ASSIGNMENT.getValue(), 5L, "1"));
		employeeDocuments
				.add(new EmployeeDocument(0L, 100L, "http://www.egov.org/egovservices/employee/100/assgn/1_2.pdf",
						EntityType.ASSIGNMENT.getValue(), 5L, "1"));
		employeeDocuments
				.add(new EmployeeDocument(0L, 100L, "http://www.egov.org/egovservices/employee/100/assgn/2_1.pdf",
						EntityType.ASSIGNMENT.getValue(), 6L, "1"));
		employeeDocuments
				.add(new EmployeeDocument(0L, 100L, "http://www.egov.org/egovservices/employee/100/assgn/2_2.pdf",
						EntityType.ASSIGNMENT.getValue(), 6L, "1"));
		employeeDocuments.add(new EmployeeDocument(0L, 100L,
				"http://www.egov.org/egovservices/employee/100/probation/10/offer_letter.pdf",
				EntityType.PROBATION.getValue(), 10L, "1"));
		employeeDocuments.add(new EmployeeDocument(0L, 100L,
				"http://www.egov.org/egovservices/employee/100/service/10/relievingLetter.pdf",
				EntityType.SERVICE.getValue(), 10L, "1"));
		employeeDocuments.add(
				new EmployeeDocument(0L, 100L, "http://www.egov.org/egovservices/employee/100/reg/5/appointment.pdf",
						EntityType.REGULARISATION.getValue(), 5L, "1"));
		employeeDocuments
				.add(new EmployeeDocument(0L, 100L, "http://www.egov.org/egovservices/employee/100/reg/5/nda.pdf",
						EntityType.REGULARISATION.getValue(), 5L, "1"));
		employeeDocuments.add(
				new EmployeeDocument(0L, 100L, "http://www.egov.org/egovservices/employee/100/tech/5000/java_cert.pdf",
						EntityType.TECHNICAL.getValue(), 5000L, "1"));
		employeeDocuments.add(
				new EmployeeDocument(0L, 100L, "http://www.egov.org/egovservices/employee/100/edu/501/degree_cert.pdf",
						EntityType.EDUCATION.getValue(), 501L, "1"));
		employeeDocuments.add(new EmployeeDocument(0L, 100L,
				"http://www.egov.org/egovservices/employee/100/edu/501/degree_marksheet.pdf",
				EntityType.EDUCATION.getValue(), 501L, "1"));
		employeeDocuments.add(
				new EmployeeDocument(0L, 100L, "http://www.egov.org/egovservices/employee/100/test/1/postgres_test.pdf",
						EntityType.TEST.getValue(), 1L, "1"));
		employeeDocuments.add(
				new EmployeeDocument(0L, 100L, "http://www.egov.org/egovservices/employee/100/test/1/java_test.pdf",
						EntityType.TEST.getValue(), 1L, "1"));

		return employeeDocuments;
	}

	/**
	 * 1) Test Scenario: DB has some documents already inserted. Hence, insert
	 * happens only for the remaining documents that are not in db.
	 * 
	 * Totally 6 documents are defined: 2 at employee level; 2 in first
	 * assignment, 2 in second assignment. When "save" method in repository is
	 * called, it is stubbed and adds the added record into a "savedDocuments"
	 * list. We compare this with the expectedDocuments list for success. Also,
	 * note that the method EmployeeDocumentsRepository.findByEmployeeId() is
	 * stubbed with a list having one document in employee, one document in
	 * assignment 1 and both the documents in assignment 2 meaning there four
	 * documents in the db for this employee. So, only 2 documents sent are
	 * saved in db.
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
				deletedDocuments
						.add(new EmployeeDocument(0L, employeeId, docUrl, referenceType, referenceId, tenantId));
				return 1;
			}

			public List<EmployeeDocument> findByEmployeeId(Long id, String tenantId) {
				List<EmployeeDocument> employeeDocuments = new ArrayList<>();
				employeeDocuments.add(
						new EmployeeDocument(0L, 100L, "http://www.egov.org/egovservices/employee/addr_proof_100.pdf",
								EntityType.EMPLOYEE_HEADER.getValue(), 100L, "1"));
				employeeDocuments.add(
						new EmployeeDocument(0L, 100L, "http://www.egov.org/egovservices/employee/100/assgn/1_1.pdf",
								EntityType.ASSIGNMENT.getValue(), 5L, "1"));
				employeeDocuments.add(
						new EmployeeDocument(0L, 100L, "http://www.egov.org/egovservices/employee/100/assgn/2_1.pdf",
								EntityType.ASSIGNMENT.getValue(), 6L, "1"));
				employeeDocuments.add(
						new EmployeeDocument(0L, 100L, "http://www.egov.org/egovservices/employee/100/assgn/2_2.pdf",
								EntityType.ASSIGNMENT.getValue(), 6L, "1"));
				return employeeDocuments;
			}
		});

		Employee employee = null;
		try {
			employee = getEmployee();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

		employeeDocumentsService.update(employee);
		List<EmployeeDocument> expectedDocuments = getExpectedEmployeeDocumentsForDBHavingSomeDocuments();

		System.out.println("expectedDocuments = " + expectedDocuments);
		System.out.println("savedDocuments = " + savedDocuments);
		// The following two lines ensure expectedDocuments is identical to
		// savedDocuments
		assertTrue(expectedDocuments.containsAll(savedDocuments));
		assertEquals(expectedDocuments.size(), savedDocuments.size());
		assertTrue(deletedDocuments.isEmpty());
	}

	private List<EmployeeDocument> getExpectedEmployeeDocumentsForDBHavingSomeDocuments() {
		List<EmployeeDocument> employeeDocuments = new ArrayList<>();
		employeeDocuments.add(new EmployeeDocument(0L, 100L, "http://www.egov.org/egovservices/employee/dob_100.pdf",
				EntityType.EMPLOYEE_HEADER.getValue(), 100L, "1"));
		employeeDocuments
				.add(new EmployeeDocument(0L, 100L, "http://www.egov.org/egovservices/employee/100/assgn/1_2.pdf",
						EntityType.ASSIGNMENT.getValue(), 5L, "1"));
		return employeeDocuments;
	}

	// ------------------test Delete starts
	// here-------------------------------------//

	/**
	 * 1) Test Scenario: DB has some documents that are not in input list. Hence
	 * these will be removed from db. 2) This test also encompasses scenario
	 * tested in "testUpdateDocumentsWithDBHavingSomeDocuments()" 3) When input
	 * list of documents is null for a given entity but db has documents for the
	 * entity, then also and deletion should happen for these records in db.
	 * 
	 * Totally 6 documents are defined: 2 at employee level; 2 in first
	 * assignment, 2 in second assignment. When "save" method in repository is
	 * called, it is stubbed and adds the added record into a "savedDocuments"
	 * list. We compare this with the expectedDocuments list for success. Also,
	 * note that the method EmployeeDocumentsRepository.findByEmployeeId() is
	 * stubbed with a list having one document in employee, one document in
	 * assignment 1 and both the documents in assignment 2 meaning there four
	 * documents in the db for this employee. Also, 4 more documents that are
	 * not in inputList is also present in DB. These two should be deleted in db
	 * which is taken care by a stubbed "delete" method in repository. This adds
	 * records in a "deletedList" which is used for assertion. So, only 2
	 * documents sent are saved in db.
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
				deletedDocuments
						.add(new EmployeeDocument(0L, employeeId, docUrl, referenceType, referenceId, tenantId));
				return 1;
			}

			public List<EmployeeDocument> findByEmployeeId(Long id, String tenantId) {
				List<EmployeeDocument> employeeDocuments = new ArrayList<>();
				employeeDocuments.add(
						new EmployeeDocument(0L, 100L, "http://www.egov.org/egovservices/employee/addr_proof_100.pdf",
								EntityType.EMPLOYEE_HEADER.getValue(), 100L, "1"));
				employeeDocuments.add(new EmployeeDocument(0L, 100L,
						"http://www.egov.org/egovservices/employee/perm_addr_proof_100.pdf",
						EntityType.EMPLOYEE_HEADER.getValue(), 100L, "1"));
				employeeDocuments.add(
						new EmployeeDocument(0L, 100L, "http://www.egov.org/egovservices/employee/100/assgn/1_1.pdf",
								EntityType.ASSIGNMENT.getValue(), 5L, "1"));
				employeeDocuments.add(
						new EmployeeDocument(0L, 100L, "http://www.egov.org/egovservices/employee/100/assgn/2_1.pdf",
								EntityType.ASSIGNMENT.getValue(), 6L, "1"));
				employeeDocuments.add(
						new EmployeeDocument(0L, 100L, "http://www.egov.org/egovservices/employee/100/assgn/2_2.pdf",
								EntityType.ASSIGNMENT.getValue(), 6L, "1"));
				employeeDocuments.add(new EmployeeDocument(0L, 100L,
						"http://www.egov.org/egovservices/employee/100/reg/5/appointment.pdf",
						EntityType.REGULARISATION.getValue(), 5L, "1"));
				employeeDocuments.add(
						new EmployeeDocument(0L, 100L, "http://www.egov.org/egovservices/employee/100/reg/5/nda.pdf",
								EntityType.REGULARISATION.getValue(), 5L, "1"));
				return employeeDocuments;
			}
		});

		Employee employee = null;
		try {
			employee = getEmployee();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

		employeeDocumentsService.update(employee);
		List<EmployeeDocument> expectedDocumentsToSave = getExpectedEmployeeDocumentsForSaveInvolvingBothSaveAndDeleteSomeDocumentsInDB();
		List<EmployeeDocument> expectedDocumentsToDelete = getExpectedEmployeeDocumentsForDeleteInvolvingBothSaveAndDeleteSomeDocumentsInDB();

		System.out.println("expectedDocuments = " + expectedDocumentsToSave);
		System.out.println("savedDocuments = " + savedDocuments);
		System.out.println("expectedDocumentsToDelete = " + expectedDocumentsToDelete);
		System.out.println("deletedDocuments = " + deletedDocuments);

		// The following two lines ensure expectedDocumentsToSave is identical
		// to savedDocuments
		assertTrue(expectedDocumentsToSave.containsAll(savedDocuments));
		assertEquals(expectedDocumentsToSave.size(), savedDocuments.size());

		// The following two lines ensure expectedDocumentsToDelete is identical
		// to deletedDocuments
		assertTrue(expectedDocumentsToDelete.containsAll(deletedDocuments));
		assertEquals(expectedDocumentsToDelete.size(), deletedDocuments.size());
	}

	private List<EmployeeDocument> getExpectedEmployeeDocumentsForSaveInvolvingBothSaveAndDeleteSomeDocumentsInDB() {
		List<EmployeeDocument> employeeDocuments = new ArrayList<>();
		employeeDocuments.add(new EmployeeDocument(0L, 100L, "http://www.egov.org/egovservices/employee/dob_100.pdf",
				EntityType.EMPLOYEE_HEADER.getValue(), 100L, "1"));
		employeeDocuments
				.add(new EmployeeDocument(0L, 100L, "http://www.egov.org/egovservices/employee/100/assgn/1_2.pdf",
						EntityType.ASSIGNMENT.getValue(), 5L, "1"));
		return employeeDocuments;
	}

	private List<EmployeeDocument> getExpectedEmployeeDocumentsForDeleteInvolvingBothSaveAndDeleteSomeDocumentsInDB() {
		List<EmployeeDocument> employeeDocuments = new ArrayList<>();
		employeeDocuments
				.add(new EmployeeDocument(0L, 100L, "http://www.egov.org/egovservices/employee/perm_addr_proof_100.pdf",
						EntityType.EMPLOYEE_HEADER.getValue(), 100L, "1"));
		employeeDocuments.add(
				new EmployeeDocument(0L, 100L, "http://www.egov.org/egovservices/employee/100/reg/5/appointment.pdf",
						EntityType.REGULARISATION.getValue(), 5L, "1"));
		employeeDocuments
				.add(new EmployeeDocument(0L, 100L, "http://www.egov.org/egovservices/employee/100/reg/5/nda.pdf",
						EntityType.REGULARISATION.getValue(), 5L, "1"));
		return employeeDocuments;
	}
	// -------------------test Delete ends here--------------------------//

	private Employee getEmployee() throws IOException {
		String empJson = new FileUtils()
				.getFileContents("org/egov/eis/service/EmployeeDocumentService.employees1.json");
		return new ObjectMapper().readValue(empJson, Employee.class);
	}

	private Employee getEmployee(String filePath) throws IOException {
		String empJson = new FileUtils().getFileContents(filePath);
		return new ObjectMapper().readValue(empJson, Employee.class);
	}

	/**
	 * 1) Tests all documents in db (mocked) get populated in documents of
	 * individual entities.
	 * 
	 * An employee object is constructed from json with no documents initially.
	 * Then
	 * employeeDocumentsService.populateDocumentsInRespectiveObjects(employee)
	 * is called which populates documents through the stub for
	 * EmployeeDocumentsRepository.findByEmployeeId()
	 */
	@Test
	public void testPopulateDocumentsInRespectiveObjects() {
		employeeDocumentsService = new EmployeeDocumentsService();

		// Set a stub of repository to employeeDocumentsService
		employeeDocumentsService.setEmployeeDocumentsRepository(new EmployeeDocumentsRepository() {
			public List<EmployeeDocument> findByEmployeeId(Long id, String tenantId) {
				List<EmployeeDocument> employeeDocuments = new ArrayList<>();
				employeeDocuments
						.add(new EmployeeDocument(0L, 100L, "http://www.egov.org/egovservices/employee/dob_100.pdf",
								EntityType.EMPLOYEE_HEADER.getValue(), 100L, "1"));
				employeeDocuments.add(
						new EmployeeDocument(0L, 100L, "http://www.egov.org/egovservices/employee/addr_proof_100.pdf",
								EntityType.EMPLOYEE_HEADER.getValue(), 100L, "1"));
				employeeDocuments.add(
						new EmployeeDocument(0L, 100L, "http://www.egov.org/egovservices/employee/100/assgn/1_1.pdf",
								EntityType.ASSIGNMENT.getValue(), 5L, "1"));
				employeeDocuments.add(
						new EmployeeDocument(0L, 100L, "http://www.egov.org/egovservices/employee/100/assgn/1_2.pdf",
								EntityType.ASSIGNMENT.getValue(), 5L, "1"));
				employeeDocuments.add(
						new EmployeeDocument(0L, 100L, "http://www.egov.org/egovservices/employee/100/assgn/2_1.pdf",
								EntityType.ASSIGNMENT.getValue(), 6L, "1"));
				employeeDocuments.add(
						new EmployeeDocument(0L, 100L, "http://www.egov.org/egovservices/employee/100/assgn/2_2.pdf",
								EntityType.ASSIGNMENT.getValue(), 6L, "1"));
				employeeDocuments.add(new EmployeeDocument(0L, 100L,
						"http://www.egov.org/egovservices/employee/100/probation/10/offer_letter.pdf",
						EntityType.PROBATION.getValue(), 10L, "1"));
				employeeDocuments.add(new EmployeeDocument(0L, 100L,
						"http://www.egov.org/egovservices/employee/100/service/10/relievingLetter.pdf",
						EntityType.SERVICE.getValue(), 10L, "1"));
				employeeDocuments.add(new EmployeeDocument(0L, 100L,
						"http://www.egov.org/egovservices/employee/100/reg/5/appointment.pdf",
						EntityType.REGULARISATION.getValue(), 5L, "1"));
				employeeDocuments.add(
						new EmployeeDocument(0L, 100L, "http://www.egov.org/egovservices/employee/100/reg/5/nda.pdf",
								EntityType.REGULARISATION.getValue(), 5L, "1"));
				employeeDocuments.add(new EmployeeDocument(0L, 100L,
						"http://www.egov.org/egovservices/employee/100/tech/5000/java_cert.pdf",
						EntityType.TECHNICAL.getValue(), 5000L, "1"));
				employeeDocuments.add(new EmployeeDocument(0L, 100L,
						"http://www.egov.org/egovservices/employee/100/edu/501/degree_cert.pdf",
						EntityType.EDUCATION.getValue(), 501L, "1"));
				employeeDocuments.add(new EmployeeDocument(0L, 100L,
						"http://www.egov.org/egovservices/employee/100/edu/501/degree_marksheet.pdf",
						EntityType.EDUCATION.getValue(), 501L, "1"));
				employeeDocuments.add(new EmployeeDocument(0L, 100L,
						"http://www.egov.org/egovservices/employee/100/test/1/postgres_test.pdf",
						EntityType.TEST.getValue(), 1L, "1"));
				employeeDocuments.add(new EmployeeDocument(0L, 100L,
						"http://www.egov.org/egovservices/employee/100/test/1/java_test.pdf",
						EntityType.TEST.getValue(), 1L, "1"));
				return employeeDocuments;
			}
		});

		Employee employee = null;
		try {
			employee = getEmployee("org/egov/eis/service/EmployeeDocumentService.populateDocuments.json");
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

		// before the populate method is called, all document lists are empty
		assertEquals(employee.getDocuments().size(), 0);
		assertEquals(employee.getAssignments().get(0).getDocuments().size(), 0);
		assertEquals(employee.getAssignments().get(1).getDocuments().size(), 0);
		assertEquals(employee.getProbation().get(0).getDocuments().size(), 0);
		assertEquals(employee.getServiceHistory().get(0).getDocuments().size(), 0);
		assertEquals(employee.getRegularisation().get(0).getDocuments().size(), 0);
		assertEquals(employee.getTechnical().get(0).getDocuments().size(), 0);
		assertEquals(employee.getEducation().get(0).getDocuments().size(), 0);
		assertEquals(employee.getTest().get(0).getDocuments().size(), 0);

		employeeDocumentsService.populateDocumentsInRespectiveObjects(employee);

		// check size and contents of documents list after populating by method
		// call.
		assertEquals(employee.getDocuments().size(), 2);
		assertEquals(employee.getAssignments().get(0).getDocuments().size(), 2);
		assertEquals(employee.getAssignments().get(1).getDocuments().size(), 2);
		assertEquals(employee.getProbation().get(0).getDocuments().size(), 1);
		assertEquals(employee.getServiceHistory().get(0).getDocuments().size(), 1);
		assertEquals(employee.getRegularisation().get(0).getDocuments().size(), 2);
		assertEquals(employee.getTechnical().get(0).getDocuments().size(), 1);
		assertEquals(employee.getEducation().get(0).getDocuments().size(), 2);
		assertEquals(employee.getTest().get(0).getDocuments().size(), 2);

		assertTrue(employee.getDocuments().get(0).equals("http://www.egov.org/egovservices/employee/dob_100.pdf"));
		assertTrue(
				employee.getDocuments().get(1).equals("http://www.egov.org/egovservices/employee/addr_proof_100.pdf"));
		assertTrue(employee.getAssignments().get(0).getDocuments().get(0)
				.equals("http://www.egov.org/egovservices/employee/100/assgn/1_1.pdf"));
		assertTrue(employee.getAssignments().get(0).getDocuments().get(1)
				.equals("http://www.egov.org/egovservices/employee/100/assgn/1_2.pdf"));
		assertTrue(employee.getAssignments().get(1).getDocuments().get(0)
				.equals("http://www.egov.org/egovservices/employee/100/assgn/2_1.pdf"));
		assertTrue(employee.getAssignments().get(1).getDocuments().get(1)
				.equals("http://www.egov.org/egovservices/employee/100/assgn/2_2.pdf"));
		assertTrue(employee.getProbation().get(0).getDocuments().get(0)
				.equals("http://www.egov.org/egovservices/employee/100/probation/10/offer_letter.pdf"));
		assertTrue(employee.getServiceHistory().get(0).getDocuments().get(0)
				.equals("http://www.egov.org/egovservices/employee/100/service/10/relievingLetter.pdf"));
		assertTrue(employee.getRegularisation().get(0).getDocuments().get(0)
				.equals("http://www.egov.org/egovservices/employee/100/reg/5/appointment.pdf"));
		assertTrue(employee.getRegularisation().get(0).getDocuments().get(1)
				.equals("http://www.egov.org/egovservices/employee/100/reg/5/nda.pdf"));
		assertTrue(employee.getTechnical().get(0).getDocuments().get(0)
				.equals("http://www.egov.org/egovservices/employee/100/tech/5000/java_cert.pdf"));
		assertTrue(employee.getEducation().get(0).getDocuments().get(0)
				.equals("http://www.egov.org/egovservices/employee/100/edu/501/degree_cert.pdf"));
		assertTrue(employee.getEducation().get(0).getDocuments().get(1)
				.equals("http://www.egov.org/egovservices/employee/100/edu/501/degree_marksheet.pdf"));
		assertTrue(employee.getTest().get(0).getDocuments().get(0)
				.equals("http://www.egov.org/egovservices/employee/100/test/1/postgres_test.pdf"));
		assertTrue(employee.getTest().get(0).getDocuments().get(1)
				.equals("http://www.egov.org/egovservices/employee/100/test/1/java_test.pdf"));
	}

}
