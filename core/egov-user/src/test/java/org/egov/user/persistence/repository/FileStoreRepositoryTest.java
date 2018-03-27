package org.egov.user.persistence.repository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
public class FileStoreRepositoryTest {

	@InjectMocks
	private FileStoreRepository fileStoreRepository;

	@Mock
	private RestTemplate restTemplate;

	@Test
	public void test_should_geturl_by_fileStoreId() {

		Map<String, String> expectedFileStoreUrls = new HashMap<String, String>();
		expectedFileStoreUrls.put("key", "value");
		when(restTemplate.getForObject(any(String.class), eq(Map.class))).thenReturn(expectedFileStoreUrls);
		String fileStoreUrl = null;
		try {
			fileStoreUrl = fileStoreRepository.getUrlByFileStoreId("default", "key");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		assertEquals(fileStoreUrl, "value");
	}

	@Test
	public void test_should_return_null_ifurllist_isempty() {
		Map<String, String> expectedFileStoreUrls = new HashMap<String, String>();
		when(restTemplate.getForObject(any(String.class), eq(Map.class))).thenReturn(expectedFileStoreUrls);
		String fileStoreUrl = null;
		try {
			fileStoreUrl = fileStoreRepository.getUrlByFileStoreId("default", "key");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Assert.assertNull(fileStoreUrl);
	}

	@Test
	public void test_should_return_null_ifurllist_null() {
		when(restTemplate.getForObject(any(String.class), eq(Map.class))).thenReturn(null);
		String fileStoreUrl = null;
		try {
			fileStoreUrl = fileStoreRepository.getUrlByFileStoreId("default", "key");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Assert.assertNull(fileStoreUrl);
	}

	@Test(expected = RuntimeException.class)
	public void test_should_throwexception_restcallfails() throws Exception {
		when(restTemplate.getForObject(any(String.class), eq(Map.class))).thenThrow(new RuntimeException());
		String fileStoreUrl = null;

		fileStoreUrl = fileStoreRepository.getUrlByFileStoreId("default", "key");
		Assert.assertNull(fileStoreUrl);
	}

}
