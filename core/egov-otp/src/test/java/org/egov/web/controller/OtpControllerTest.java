package org.egov.web.controller;

import org.egov.Resources;
import org.egov.domain.InvalidTokenRequestException;
import org.egov.domain.model.Token;
import org.egov.domain.model.TokenRequest;
import org.egov.domain.service.TokenService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(OtpController.class)
public class OtpControllerTest {

    private final static String IDENTITY = "identity";
    private final static String TENANT_ID = "tenantId";

	@Autowired
	private MockMvc mockMvc;

	private Resources resources = new Resources();

	@MockBean
	private TokenService tokenService;

	@Test
	public void test_should_return_token() throws Exception {

		final Token token = Token.builder()
				.uuid("uuid")
				.identity(IDENTITY)
                .tenantId(TENANT_ID)
				.number("randomNumber")
				.build();
		final TokenRequest tokenRequest = new TokenRequest(IDENTITY, TENANT_ID);
		when(tokenService.createToken(tokenRequest)).thenReturn(token);

		mockMvc.perform(post("/v1/_create").contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(resources.getFileContents("createOtpRequest.json")))
				.andExpect(status().isCreated())
				.andExpect(content().json(resources.getFileContents("createOtpResponse.json")));
	}

    @Test
    public void test_should_return_error_response_when_token_request_is_not_valid() throws Exception {
		final TokenRequest tokenRequest = new TokenRequest(null, TENANT_ID);
        when(tokenService.createToken(tokenRequest)).thenThrow(new InvalidTokenRequestException(tokenRequest));

        mockMvc.perform(post("/v1/_create").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(resources.getFileContents("createOtpRequestWithoutIdentity.json")))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(resources.getFileContents("invalidOtpResponse.json")));
    }

    @Test
    public void test_should_error_message_when_unhandled_exception_occurs() throws Exception {
		final TokenRequest tokenRequest = new TokenRequest(null, TENANT_ID);
        final String expectedMessage = "exception message";
        when(tokenService.createToken(tokenRequest)).thenThrow(new RuntimeException(expectedMessage));

        mockMvc.perform(post("/v1/_create").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(resources.getFileContents("createOtpRequestWithoutIdentity.json")))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(expectedMessage));
    }

}