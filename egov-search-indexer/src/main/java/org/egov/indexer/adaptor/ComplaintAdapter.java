package org.egov.indexer.adaptor;

import java.util.Map;

import org.egov.indexer.entity.ComplaintIndex;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ComplaintAdapter {
	
	public ComplaintIndex index(Map jsonContent) {
		final ObjectMapper mapper = new ObjectMapper();
		final ComplaintIndex complaintIndex = mapper.convertValue(jsonContent, ComplaintIndex.class);
		return complaintIndex;
	}

}
