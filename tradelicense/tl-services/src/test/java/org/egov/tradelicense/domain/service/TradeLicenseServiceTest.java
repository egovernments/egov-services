package org.egov.tradelicense.domain.service;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.tl.commons.web.contract.CategoryDetailSearch;
import org.egov.tl.commons.web.contract.CategorySearch;
import org.egov.tl.commons.web.contract.DocumentType;
import org.egov.tl.commons.web.contract.DocumentTypeContract;
import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.requests.DocumentTypeV2Response;
import org.egov.tl.commons.web.requests.RequestInfoWrapper;
import org.egov.tl.commons.web.requests.ResponseInfoFactory;
import org.egov.tl.commons.web.response.CategorySearchResponse;
import org.egov.tl.commons.web.response.DocumentTypeResponse;
import org.egov.tradelicense.common.config.PropertiesManager;
import org.egov.tradelicense.configuration.TestConfiguration;
import org.egov.tradelicense.domain.enums.ApplicationType;
import org.egov.tradelicense.domain.enums.BusinessNature;
import org.egov.tradelicense.domain.enums.OwnerShipType;
import org.egov.tradelicense.domain.model.AuditDetails;
import org.egov.tradelicense.domain.model.LicenseFeeDetail;
import org.egov.tradelicense.domain.model.SupportDocument;
import org.egov.tradelicense.domain.model.TradeLicense;
import org.egov.tradelicense.domain.repository.TradeLicenseRepository;
import org.egov.tradelicense.domain.service.validator.TradeLicenseServiceValidator;
import org.egov.tradelicense.web.contract.Boundary;
import org.egov.tradelicense.web.repository.BoundaryContractRepository;
import org.egov.tradelicense.web.repository.CategoryContractRepository;
import org.egov.tradelicense.web.repository.DocumentTypeContractRepository;
import org.egov.tradelicense.web.repository.PropertyContractRespository;
import org.egov.tradelicense.web.response.BoundaryResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;

@Import(TestConfiguration.class)
@RunWith(SpringRunner.class)
public class TradeLicenseServiceTest {

	@Mock
	TradeLicenseService tradeLicenseService;

	@Mock
	TradeLicenseServiceValidator tradeLicenseServiceValidator;

	@Mock
	private SmartValidator validator;

	@Mock
	private TradeLicenseRepository tradeLicenseRepository;

	@Mock
	private BoundaryContractRepository boundaryContractRepository;

	@Mock
	private CategoryContractRepository categoryContractRepository;

	@Mock
	private DocumentTypeContractRepository documentTypeContractRepository;

	@Mock
	private TradeLicenseNumberGeneratorService licenseNumberGenerationService;

	@Mock
	PropertyContractRespository propertyContractRepository;

	@Mock
	private PropertiesManager propertiesManager;

	@Mock
	private ResponseInfoFactory responseInfoFactory;

	private BindingResult errors = new BeanPropertyBindingResult(null, null);

	private List<TradeLicense> tradeLicenses = new ArrayList<>();

	private List<LicenseFeeDetail> licenseFeeDetails = new ArrayList<>();

	private List<SupportDocument> supportDocuments = new ArrayList<>();

	@Mock
	private RequestInfo requestInfo = new RequestInfo();

	@Before
	public void setup() {

	}

	@Test
	public final void testvalidateRelated() {
		when(boundaryContractRepository.findByLocalityCodes(any(String.class), any(String.class), any(RequestInfoWrapper.class)))
				.thenReturn(getBoundaryResponse());
		when(boundaryContractRepository.findByAdminWardCodes(any(String.class), any(String.class), any(RequestInfoWrapper.class)))
				.thenReturn(getBoundaryResponse());
		when(boundaryContractRepository.findByRevenueWardCodes(any(String.class), any(String.class), any(RequestInfoWrapper.class)))
				.thenReturn(getBoundaryResponse());
		when(categoryContractRepository.findByCategoryCode(any(TradeLicense.class), any(RequestInfoWrapper.class)))
				.thenReturn(getCategoyResponse());
		when(categoryContractRepository.findBySubCategoryCode(any(TradeLicense.class), any(RequestInfoWrapper.class)))
				.thenReturn(getSubCategoyResponse());
		when(categoryContractRepository.findBySubCategoryUomCode(any(TradeLicense.class), any(RequestInfoWrapper.class)))
				.thenReturn(getSubCategoyResponse());
		when(documentTypeContractRepository.findByIdAndTlValues(any(TradeLicense.class), any(SupportDocument.class),
				any(RequestInfoWrapper.class))).thenReturn(getDocumentTypeV2Response());
		tradeLicenses.add(getTradeLicense());
		tradeLicenseServiceValidator.validateCreateTradeLicenseRelated(tradeLicenses, requestInfo);
	}

	@Test
	public final void testAdd() {
		when(boundaryContractRepository.findByLocalityCodes(any(String.class), any(String.class), any(RequestInfoWrapper.class)))
				.thenReturn(getBoundaryResponse());
		when(boundaryContractRepository.findByAdminWardCodes(any(String.class), any(String.class), any(RequestInfoWrapper.class)))
				.thenReturn(getBoundaryResponse());
		when(boundaryContractRepository.findByRevenueWardCodes(any(String.class), any(String.class), any(RequestInfoWrapper.class)))
				.thenReturn(getBoundaryResponse());
		when(categoryContractRepository.findByCategoryCode(any(TradeLicense.class), any(RequestInfoWrapper.class)))
				.thenReturn(getCategoyResponse());
		when(categoryContractRepository.findBySubCategoryCode(any(TradeLicense.class), any(RequestInfoWrapper.class)))
				.thenReturn(getSubCategoyResponse());
		when(categoryContractRepository.findBySubCategoryUomCode(any(TradeLicense.class), any(RequestInfoWrapper.class)))
				.thenReturn(getSubCategoyResponse());
		when(documentTypeContractRepository.findByIdAndTlValues(any(TradeLicense.class), any(SupportDocument.class),
				any(RequestInfoWrapper.class))).thenReturn(getDocumentTypeV2Response());
		tradeLicenses.add(getTradeLicense());
		tradeLicenseService.add(tradeLicenses, requestInfo, errors);
	}

	/*
	 * @Test public final void testSearch() { List<TradeLicense> searchTrades =
	 * new ArrayList<>(); searchTrades.add(getTradeLicense());
	 * 
	 * when(tradeLicenseRepository.search(any(String.class), any(Integer.class),
	 * any(Integer.class), any(String.class), any(String.class),
	 * any(String.class), any(String.class), any(String.class),
	 * any(String.class), any(String.class), any(String.class),
	 * any(String.class), any(String.class), any(Integer.class),
	 * any(Integer.class), any(String.class), any(String.class),
	 * any(String.class), any(Integer.class), any(Integer.class),
	 * any(String.class), any(Integer.class))).thenReturn(searchTrades);
	 * 
	 * tradeLicenseService.getTradeLicense(requestInfo, "default", 0, 0, "sort",
	 * "true", "1", null, null, "12345", "9999999999", null, "xyz@abc.com",
	 * null, 7, 20, "test", "test", "PERMANENT", 1, 2, "true", null); }
	 */

	public BoundaryResponse getBoundaryResponse() {

		return BoundaryResponse.builder().boundarys(getBoundary()).build();
	}

	public List<Boundary> getBoundary() {
		List<Boundary> boundaries = new ArrayList<>();
		Boundary boundary = Boundary.builder().id("1").build();
		boundaries.add(boundary);
		return boundaries;
	}

	public CategorySearchResponse getCategoyResponse() {

		return CategorySearchResponse.builder().categories(getCategoy()).build();
	}

	public List<CategorySearch> getCategoy() {
		List<CategorySearch> categories = new ArrayList<>();
		CategorySearch category = CategorySearch.builder().id(1l).build();
		categories.add(category);
		return categories;
	}

	public CategorySearchResponse getSubCategoyResponse() {

		return CategorySearchResponse.builder().categories(getSubCategoy()).build();
	}

	public List<CategorySearch> getSubCategoy() {

		List<CategorySearch> categories = new ArrayList<>();
		CategorySearch category = CategorySearch.builder().id(1l).build();
		List<CategoryDetailSearch> categoryDetails = new ArrayList<>();
		CategoryDetailSearch categoryDetail = CategoryDetailSearch.builder().id(1l).build();
		categoryDetail.setUom("Area");
		categoryDetails.add(categoryDetail);
		category.setValidityYears(1l);
		category.setDetails(categoryDetails);
		categories.add(category);
		return categories;
	}

	public DocumentTypeResponse getDocumentTypeResponse() {

		return DocumentTypeResponse.builder().documentTypes(getDocumentType()).build();
	}
	
	public DocumentTypeV2Response getDocumentTypeV2Response() {

		return DocumentTypeV2Response.builder().documentTypes(getDocumentTypeContract()).build();
	}

	public List<DocumentType> getDocumentType() {

		List<DocumentType> documentTypes = new ArrayList<>();
		DocumentType documentType = DocumentType.builder().id(1l).build();
		documentTypes.add(documentType);
		return documentTypes;
	}
	
	public List<DocumentTypeContract> getDocumentTypeContract() {

		List<DocumentTypeContract> documentTypescontracts = new ArrayList<>();
		DocumentTypeContract documentTypeContract = DocumentTypeContract.builder().id(1l).build();
		documentTypescontracts.add(documentTypeContract);
		return documentTypescontracts;
	}

	private TradeLicense getTradeLicense() {

		licenseFeeDetails.add(getFeeDetail());
		supportDocuments.add(getSupportDocument());
		return TradeLicense.builder().id(1l).tenantId("default").applicationType(ApplicationType.NEW).active(true)
				.applicationDate((new Date("15/08/2017")).getTime() / 1000).ownerEmailId("abc@xyz.com").isLegacy(true)
				.oldLicenseNumber("12345").ownerMobileNumber("9999999999").ownerName("pavan").fatherSpouseName("Venkat")
				.ownerAddress("1-12 kamma street").locality("7").adminWard("7").revenueWard("20").category("Flammables")
				.subCategory("Crackers").uom("area").quantity(10.0).validityYears(1l).tradeAddress("1-12 kamma street")
				.ownerShipType(OwnerShipType.RENTED).tradeTitle("restaurants").tradeType(BusinessNature.PERMANENT)
				.isPropertyOwner(Boolean.FALSE).feeDetails(licenseFeeDetails).supportDocuments(supportDocuments)
				.tradeCommencementDate((new Date("15/08/2017")).getTime() / 1000).auditDetails(getAuditDetails())
				.build();
	}

	private AuditDetails getAuditDetails() {

		return AuditDetails.builder().createdBy("1").createdTime(12345678912l).lastModifiedBy("1")
				.lastModifiedTime(12345678912l).build();
	}

	private SupportDocument getSupportDocument() {

		return SupportDocument.builder().id(1l).documentTypeId(1l).auditDetails(getAuditDetails()).build();
	}

	private LicenseFeeDetail getFeeDetail() {

		return LicenseFeeDetail.builder().id(1l).auditDetails(getAuditDetails()).build();
	}

}
