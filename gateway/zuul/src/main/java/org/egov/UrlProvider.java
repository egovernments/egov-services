package org.egov;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UrlProvider {

	private static Map<String, String> urlMap;

	@Value("${url.lists}")
	private String url;

	@PostConstruct
	public void loadUrl() {

		Map<String, String> map = new HashMap<>();
		String[] urlArray = url.split("\\|");
		
		for (int i = 0; i < urlArray.length; i++) {

			String[] index = urlArray[i].split(":", 1);
			map.put(index[0], index[1]);
		}
		urlMap = Collections.unmodifiableMap(map);
		log.info(" the urls : " + urlMap);
	}
	
	public static Map<String, String> getUrlMap() {
		return urlMap;
	}
}
