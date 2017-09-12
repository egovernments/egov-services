package org.egov.tl.indexer.client;

import org.egov.tl.indexer.config.PropertiesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;

/**
 * 
 * @author Shubham pratap singh
 *
 */
@Service
public class JestClientEs {

	private static JestClient client;

	@Autowired
	private PropertiesManager manager;

	public JestClient getClient() {
		String url = "http://" + manager.getEsHost() + ":" + manager.getEsPort();
		if (this.client == null) {
			JestClientFactory factory = new JestClientFactory();
			factory.setHttpClientConfig(
					new HttpClientConfig.Builder(url).multiThreaded(Boolean.valueOf(manager.getIsMultithreaded()))
							.readTimeout(Integer.valueOf(manager.getTimeOut())).build());
			this.client = factory.getObject();
		}

		return this.client;
	}

}
