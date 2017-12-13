package org.egov.inv.api;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.egov.common.contract.request.RequestInfo;
import org.egov.inv.domain.service.OpeningBalanceService;
import org.egov.inv.model.MaterialReceipt;
import org.egov.inv.model.MaterialReceiptSearch;
import org.egov.inv.model.OpeningBalanceRequest;
import org.egov.inv.model.OpeningBalanceResponse;
import org.egov.inv.model.MaterialReceipt.MrnStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-07T12:05:30.189Z")

@Controller
public class OpeningbalanceApiController implements OpeningbalanceApi {

	private static final Logger log = LoggerFactory.getLogger(OpeningbalanceApiController.class);

	@Autowired
	private OpeningBalanceService openingBalanceService;

	public ResponseEntity<OpeningBalanceResponse> openingbalanceCreatePost(
			@ApiParam(value = "Details for the new Oening Balance request.", required = true) @Valid @RequestParam(value = "tenantId", required = true) String tenantId,
			@RequestBody OpeningBalanceRequest openingBalance) {
		List<MaterialReceipt> openbal = openingBalanceService.create(openingBalance, tenantId);
		OpeningBalanceResponse materialResponse = buildOpenBalanceResponse(openbal, openingBalance.getRequestInfo());
		return new ResponseEntity<OpeningBalanceResponse>(materialResponse, HttpStatus.OK);
	}

	public ResponseEntity<OpeningBalanceResponse> openingbalanceUpdatePost(
			@ApiParam(value = "Details for the new opening balance.", required = true) @Valid @RequestParam(value = "tenantId", required = true) String tenantId,
			@RequestBody OpeningBalanceRequest openingBalanace) {
		List<MaterialReceipt> openbal = openingBalanceService.update(openingBalanace, tenantId);
		OpeningBalanceResponse materialResponse = buildOpenBalanceResponse(openbal, openingBalanace.getRequestInfo());
		return new ResponseEntity<OpeningBalanceResponse>(materialResponse, HttpStatus.OK);

	}

	public ResponseEntity<OpeningBalanceResponse> openingbalanceSearchPost(
			@NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
			@NotNull @ApiParam(value = "Unique id .", required = false) @RequestParam(value = "ids", required = false) String ids,
			@ApiParam(value = "search on basis of storeName ") @RequestParam(value = "storeName", required = false) String storeName,
			@ApiParam(value = "Parameter to carry Request metadata in the request body") @Valid @RequestBody RequestInfo requestInfo,
			@ApiParam(value = "search on basis of financial year ") @RequestParam(value = "financialYear", required = false) String financialYear,
			@ApiParam(value = "search on basis of mrnNumber ") @RequestParam(value = "mrnNumber", required = false) String mrnNumber,
			@ApiParam(value = "search on basis of materialName ") @RequestParam(value = "materialName", required = false) String receiptType,
			@Min(0) @Max(100) @ApiParam(value = "Number of records returned.", defaultValue = "20") @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
			@ApiParam(value = "Page number", defaultValue = "1") @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@ApiParam(value = "This takes any field from the Object seperated by comma and asc,desc keywords. example name asc,code desc or name,code or name,code desc", defaultValue = "id") @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy) {
		// do some magic!
		MaterialReceiptSearch receiptSearch = MaterialReceiptSearch.builder()
				.ids(null != ids ? Arrays.asList(ids) : null).tenantId(tenantId).financialYear(financialYear)
				.receivingStore(storeName)
				// .mrnNumber(Collections.singletonList(receiptNumber))
				.mrnNumber(null != mrnNumber ? Arrays.asList(mrnNumber) : null)
				.receiptType(null != receiptType ? Arrays.asList(receiptType) : null).pageNumber(pageNumber)
				.pageSize(pageSize).build();

		return new ResponseEntity<>(openingBalanceService.search(receiptSearch), HttpStatus.OK);
	}

	private OpeningBalanceResponse buildOpenBalanceResponse(List<MaterialReceipt> material,
			org.egov.inv.model.RequestInfo requestInfo) {
		return OpeningBalanceResponse.builder().responseInfo(getResponseInfo(requestInfo)).materialReceipt(material)
				.build();
	}

	private org.egov.inv.model.ResponseInfo getResponseInfo(org.egov.inv.model.RequestInfo requestInfo) {
		return org.egov.inv.model.ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").build();
	}
}
