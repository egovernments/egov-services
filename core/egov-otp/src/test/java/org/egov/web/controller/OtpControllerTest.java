package org.egov.web.controller;

import org.egov.Resources;
import org.egov.domain.InvalidTokenException;
import org.egov.domain.model.Token;
import org.egov.domain.service.TokenService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(OtpController.class)
public class OtpControllerTest {

    final static String IDENTITY = "identity";
    final static String TENANT_ID = "tenantId";

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
		when(tokenService.createToken(IDENTITY, TENANT_ID)).thenReturn(token);

		mockMvc.perform(post("/_create").contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(resources.getFileContents("createOtpRequest.json")))
				.andExpect(status().isCreated())
				.andExpect(content().json(resources.getFileContents("createOtpResponse.json")));
	}

    @Test
    public void test_should_return_error_response_when_identity_is_not_provided() throws Exception {
        when(tokenService.createToken(null, TENANT_ID)).thenThrow(new InvalidTokenException());

        mockMvc.perform(post("/_create").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(resources.getFileContents("createOtpRequestWithoutIdentity.json")))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(resources.getFileContents("invalidOtpResponse.json")));
    }

    @Test
    public void test_should_error_message_when_unhandled_exception_occurs() throws Exception {
        final String expectedMessage = "exception message";
        when(tokenService.createToken(anyString(), anyString())).thenThrow(new RuntimeException(expectedMessage));

        mockMvc.perform(post("/_create").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(resources.getFileContents("createOtpRequestWithoutIdentity.json")))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(expectedMessage));
    }

}