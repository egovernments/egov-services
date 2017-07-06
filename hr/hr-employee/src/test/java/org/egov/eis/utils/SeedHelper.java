package org.egov.eis.utils;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.eis.model.NominatingEmployee;
import org.egov.eis.model.Nominee;
import org.egov.eis.model.enums.Gender;
import org.egov.eis.model.enums.MaritalStatus;
import org.egov.eis.model.enums.Relationship;
import org.egov.eis.web.contract.*;
import org.egov.eis.web.errorhandler.Error;
import org.egov.eis.web.errorhandler.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

public class SeedHelper {

    public RequestInfo getRequestInfo() {
        return RequestInfo.builder().apiId("apiId").ver("ver").ts(new Date()).action("action").did("did").key("key")
                .msgId("msgId").authToken("authToken")
                .userInfo(new User()).build();
    }

    public ResponseInfo getResponseInfo() {
        return ResponseInfo.builder().apiId("apiId").ver("ver").ts("06-01-1964 00:00:00").msgId("msgId")
                .resMsgId("resMsgId").status("200").build();
    }

    @SuppressWarnings("serial")
	public Error getError() {
        Map<String, Object> fields = new HashMap<String, Object>() {{
            put("field1", "error message for field 1");
            put("field2", "error message for field 2");
        }};
        return Error.builder().code(400).message("error message").description("error description").fields(fields).build();
    }

    public NomineeRequest getNomineeRequest() {
        return NomineeRequest.builder().nominees(getNominees()).requestInfo(getRequestInfo()).build();
    }

    public ResponseEntity<?> getErrorResponseEntity() {
        return new ResponseEntity<>(ErrorResponse.builder().error(getError()).responseInfo(getResponseInfo()).build(),
                HttpStatus.BAD_REQUEST);
    }

    public List<Nominee> getNominees() {
        NominatingEmployee employee = NominatingEmployee.builder().id(4L).name("employee").code("EMP04").build();
        return Arrays.asList(Nominee.builder().id(5L).employee(employee).name("nominee").gender(Gender.FEMALE)
                .dateOfBirth(123456789L).maritalStatus(MaritalStatus.UNMARRIED).relationship(Relationship.DAUGHTER)
                .bank(1L).bankBranch(1L).bankAccount("123456789").nominated(true).employed(true)
                .documents(Arrays.asList("doc1", "doc2")).tenantId("default").createdBy(1L).createdDate(12345L)
                .lastModifiedBy(1L).lastModifiedDate(12345L).build());
    }

}
