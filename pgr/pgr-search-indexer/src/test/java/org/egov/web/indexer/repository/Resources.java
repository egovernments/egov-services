package org.egov.web.indexer.repository;

import java.io.IOException;

import org.apache.commons.io.IOUtils;

public class Resources {
	public String getFileContents(String fileName) {
		try {
			return IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream(fileName), "UTF-8")
					.replace(" ", "").replace("\n", "");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
