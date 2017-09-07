package org.egov.wcms.web.controller;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.wcms.TestConfiguration;
import org.egov.wcms.config.ApplicationProperties;
import org.egov.wcms.model.Gapcode;
import org.egov.wcms.service.GapcodeService;
import org.egov.wcms.util.ValidatorUtils;
import org.egov.wcms.web.contract.GapcodeGetRequest;
import org.egov.wcms.web.contract.GapcodeRequest;
import org.egov.wcms.web.contract.factory.ResponseInfoFactory;
import org.egov.wcms.web.errorhandlers.ErrorHandler;
import org.egov.wcms.web.errorhandlers.ErrorResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@RunWith(SpringRunner.class)
@WebMvcTest(GapcodeController.class)
@Import(TestConfiguration.class)
public class GapcodeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GapcodeService gapcodeService;

    @InjectMocks
    private GapcodeController gapcodeController;

    @MockBean
    private ResponseInfoFactory responseInfoFactory;

    @MockBean
    private ValidatorUtils validatorUtils;

    @MockBean
    private ApplicationProperties applicationProperties;

    @MockBean
    private ErrorHandler errHandler;

    @Test
    public void test_should_create_gapcode() throws Exception {
        final List<ErrorResponse> errorResponses = new ArrayList<>();
        when(validatorUtils.validateGapcodeRequest(getGapcodeRequest(), false)).thenReturn(errorResponses);
        when(gapcodeService.createGapcode(applicationProperties.getCreateGapcodeTopicName(), "gapcode-create",
                getGapcodeRequest())).thenReturn(getListOfGapcode());
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), eq(true)))
                .thenReturn(getSuccessRequestInfo());
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), eq(false)))
                .thenReturn(getFailureRequestInfo());
        final ResultActions msb = mockMvc.perform(post("/gapcode/_create").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(getFileContents("GapcodeRequestCreate.json")));
        msb.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
        // .andExpect(content().json(getFileContents("GapcodeResponseCreate.json")));
    }

    @Test(expected = Exception.class)
    public void test_Should_Search_PropertyCategory() throws Exception {

        final List<Gapcode> glist = new ArrayList<>();
        final RequestInfo requestInfo = new RequestInfo();
        final ResponseInfo responseInfo = new ResponseInfo();
        final Gapcode gapcode = new Gapcode();
        gapcode.setActive(true);
        gapcode.setCode("Gapcode1");
        gapcode.setTenantId("1");

        glist.add(gapcode);

        final GapcodeGetRequest propertyCategoryGetRequest = Mockito.mock(GapcodeGetRequest.class);

        when(gapcodeService.getGapcodes(propertyCategoryGetRequest)).thenReturn(glist);
        when(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true)).thenReturn(responseInfo);

        mockMvc.perform(post("/gapcode/_search?")
                .param("tenantId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getFileContents("requestinfowrapper.json")))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(getFileContents("GapcodeResponse.json")));
    }

    private ResponseInfo getFailureRequestInfo() {
        return ResponseInfo.builder().apiId("emp").ver("1.0").resMsgId("uief87324").msgId("20170826")
                .status("failed").build();
    }

    private ResponseInfo getSuccessRequestInfo() {
        return ResponseInfo.builder().apiId("emp").ver("1.0").resMsgId("uief87324").msgId("20170826").ts(1L)
                .status("successful").build();
    }

    private List<Gapcode> getListOfGapcode() {
        final Gapcode gapcode = Gapcode.builder().id(1L).code("Gapcode123").logic("logic").active(true).tenantId("default")
                .build();
        return Arrays.asList(gapcode);
    }

    private GapcodeRequest getGapcodeRequest() {
        final User userInfo = User.builder().id(1L).build();
        final RequestInfo requestInfo = RequestInfo.builder().apiId("emp").ver("1.0").action("POST")
                .did("4354648646").key("xyz").msgId("20170826").authToken("345678f").userInfo(userInfo).build();
        final Gapcode gapcode = Gapcode.builder().id(1L).code("Gapcode123").outSideUlb(true).noOfMonths("Gapcode Test 123")
                .logic("").description("").active(true).tenantId("default").build();
        return GapcodeRequest.builder().requestInfo(requestInfo).gapcode(Arrays.asList(gapcode)).build();

    }

    private String getFileContents(final String fileName) {
        try {
            return IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream(fileName), "UTF-8");
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}
