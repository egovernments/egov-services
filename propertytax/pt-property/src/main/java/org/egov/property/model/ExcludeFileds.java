package org.egov.property.model;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class ExcludeFileds implements ExclusionStrategy {
	public boolean shouldSkipClass(Class<?> arg0) {
		return false;
	}

	public boolean shouldSkipField(FieldAttributes f) {

		return (f.getName().equals("id") || f.getName().equals("tenantId"));
	}
}
