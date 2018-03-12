package org.egov.eis.web.controller;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.eis.TestConfiguration;
import org.egov.eis.model.Nominee;
import org.egov.eis.service.NomineeService;
import org.egov.eis.utils.FileUtils;
import org.egov.eis.utils.SeedHelper;
import org.egov.eis.web.contract.NomineeGetRequest;
import org.egov.eis.web.contract.NomineeRequest;
import org.egov.eis.web.contract.factory.ResponseInfoFactory;
import org.egov.eis.web.errorhandler.ErrorHandler;
import org.egov.eis.web.validator.DataIntegrityValidatorForCreateNominee;
import org.egov.eis.web.validator.DataIntegrityValidatorForUpdateNominee;
import org.egov.eis.web.validator.RequestValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;

import java.io.IOException;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringRunner.class)
@WebMvcTest(NomineeController.class)
@PrepareForTest( { ValidationUtils.class } )
@Import(TestConfiguration.class)
public class NomineeControllerTest {

    private SeedHelper seedHelper = new SeedHelper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NomineeService nomineeService;

    @MockBean
    private RequestValidator requestValidator;

    @MockBean
    private ErrorHandler errorHandler;

    @MockBean
    private ResponseInfoFactory responseInfoFactory;

    @MockBean
    private DataIntegrityValidatorForCreateNominee dataIntegrityValidatorForCreateNominee;

    @MockBean
    private DataIntegrityValidatorForUpdateNominee dataIntegrityValidatorForUpdateNominee;

    @Before
    public void mockValidators() {
        PowerMockito.mockStatic(ValidationUtils.class);
    }

    @Test
    public void testSearch_forValidRequest() throws Exception {
        List<Nominee> expectedNominees = seedHelper.getNominees();
        ResponseInfo responseInfo = seedHelper.getResponseInfo();

        doReturn(expectedNominees).when(nomineeService).getNominees(any(NomineeGetRequest.class), any(RequestInfo.class));
        doReturn(responseInfo).when(responseInfoFactory).createResponseInfoFromRequestInfo(any(RequestInfo.class), anyBoolean());

        mockMvc.perform(post("/nominees/_search")
                .param("tenantId", "default").param("id", "4")
                .content(getFileContents("RequestInfo.json"))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(getFileContents("nomineeResponse.json")));

        verify(requestValidator, times(1))
                .validateSearchRequest(any(RequestInfo.class), any(BindingResult.class), any(BindingResult.class));
        verify(nomineeService, times(1))
                .getNominees(any(NomineeGetRequest.class), any(RequestInfo.class));
        verify(errorHandler, never()).getResponseEntityForUnexpectedErrors(any(RequestInfo.class));
        verify(responseInfoFactory, times(1))
                .createResponseInfoFromRequestInfo(any(RequestInfo.class), anyBoolean());
    }

    @Test
    public void testSearch_forInvalidRequest() throws Exception {
        ResponseEntity<?> errorResponseEntity = seedHelper.getErrorResponseEntity();

        doReturn(errorResponseEntity).when(requestValidator)
                .validateSearchRequest(any(RequestInfo.class), any(BindingResult.class), any(BindingResult.class));

        mockMvc.perform(post("/nominees/_search")
                .content(getFileContents("RequestInfo.json"))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(getFileContents("errorResponse.json")));

        verify(requestValidator, times(1))
                .validateSearchRequest(any(RequestInfo.class), any(BindingResult.class), any(BindingResult.class));
        verify(nomineeService, never()).getNominees(any(NomineeGetRequest.class), any(RequestInfo.class));
        verify(errorHandler, never()).getResponseEntityForUnexpectedErrors(any(RequestInfo.class));
        verify(responseInfoFactory, never()).createResponseInfoFromRequestInfo(any(RequestInfo.class), anyBoolean());
    }

    /*@Test
    public void testCreate_forValidRequest() throws Exception {
        List<Nominee> expectedNominees = seedHelper.getNominees();
        ResponseInfo expectedResponseInfo = seedHelper.getResponseInfo();
        doNothing().when(ValidationUtils.class, "invokeValidator", any(), any(), any());
        doReturn(expectedNominees).when(nomineeService).createAsync(any(NomineeRequest.class));
        doReturn(expectedResponseInfo).when(responseInfoFactory)
                .createResponseInfoFromRequestInfo(any(RequestInfo.class), any(Boolean.class));

        mockMvc.perform(post("/nominees/_create")
                .content(getFileContents("nomineeRequest.json")).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(getFileContents("nomineeResponse.json")));

        verifyStatic(times(1));
        ValidationUtils.invokeValidator(any(), any(), any());
        verify(errorHandler, never())
                .getErrorResponseEntityForInvalidRequest(any(BindingResult.class), any(RequestInfo.class));
        verify(nomineeService, times(1)).createAsync(any(NomineeRequest.class));
        verify(errorHandler, never()).getResponseEntityForUnexpectedErrors(any(RequestInfo.class));
        verify(responseInfoFactory, times(1))
                .createResponseInfoFromRequestInfo(any(RequestInfo.class), anyBoolean());
    }*/

  /*  @Test
    public void testCreate_forInvalidRequest() throws Exception {
        ResponseEntity<?> expectedErrorResponse = seedHelper.getErrorResponseEntity();

        doNothing().when(ValidationUtils.class, "invokeValidator", any(), any(), any());
        doReturn(expectedErrorResponse).when(errorHandler)
                .getErrorResponseEntityForInvalidRequest(any(BindingResult.class), any(RequestInfo.class));

        mockMvc.perform(post("/nominees/_create")
                .content(getFileContents("nomineeInvalidRequest.json")).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(getFileContents("errorResponse.json")));

        verify(errorHandler, times(1))
                .getErrorResponseEntityForInvalidRequest(any(BindingResult.class), any(RequestInfo.class));
        verifyStatic(never());
        ValidationUtils.invokeValidator(any(), any(), any());
        verify(nomineeService, never()).createAsync(any(NomineeRequest.class));
        verify(errorHandler, never()).getResponseEntityForUnexpectedErrors(any(RequestInfo.class));
        verify(responseInfoFactory, never()).createResponseInfoFromRequestInfo(any(RequestInfo.class), anyBoolean());
    }*/

  /*  @Test
    public void testUpdate_forValidRequest() throws Exception {
        List<Nominee> expectedNominees = seedHelper.getNominees();
        ResponseInfo expectedResponseInfo = seedHelper.getResponseInfo();

        doNothing().when(ValidationUtils.class, "invokeValidator", any(), any(), any());
        doReturn(expectedNominees).when(nomineeService).updateAsync(any(NomineeRequest.class));
        doReturn(expectedResponseInfo).when(responseInfoFactory)
                .createResponseInfoFromRequestInfo(any(RequestInfo.class), any(Boolean.class));

        mockMvc.perform(post("/nominees/_update")
                .content(getFileContents("nomineeRequest.json")).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(getFileContents("nomineeResponse.json")));

        verifyStatic(times(1));
        ValidationUtils.invokeValidator(any(), any(), any());
        verify(errorHandler, never())
                .getErrorResponseEntityForInvalidRequest(any(BindingResult.class), any(RequestInfo.class));
        verify(nomineeService, times(1)).updateAsync(any(NomineeRequest.class));
        verify(errorHandler, never()).getResponseEntityForUnexpectedErrors(any(RequestInfo.class));
        verify(responseInfoFactory, times(1))
                .createResponseInfoFromRequestInfo(any(RequestInfo.class), anyBoolean());
    }
*/
   /* @Test
    public void testUpdate_forInvalidRequest() throws Exception {
        ResponseEntity<?> expectedErrorResponse = seedHelper.getErrorResponseEntity();

        doNothing().when(ValidationUtils.class, "invokeValidator", any(), any(), any());
        doReturn(expectedErrorResponse).when(errorHandler)
                .getErrorResponseEntityForInvalidRequest(any(BindingResult.class), any(RequestInfo.class));

        mockMvc.perform(post("/nominees/_update")
                .content(getFileContents("nomineeInvalidRequest.json")).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(getFileContents("errorResponse.json")));

        verify(errorHandler, times(1))
                .getErrorResponseEntityForInvalidRequest(any(BindingResult.class), any(RequestInfo.class));
        verifyStatic(never());
        ValidationUtils.invokeValidator(any(), any(), any());
        verify(nomineeService, never()).createAsync(any(NomineeRequest.class));
        verify(errorHandler, never()).getResponseEntityForUnexpectedErrors(any(RequestInfo.class));
        verify(responseInfoFactory, never()).createResponseInfoFromRequestInfo(any(RequestInfo.class), anyBoolean());
    }
*/
    private String getFileContents(String filePath) throws IOException {
        return new FileUtils().getFileContents(
                "org/egov/eis/web/controller/NomineeController/" + filePath);
    }

}
