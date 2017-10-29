package org.egov.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum KafkaTopicName {
    
	   SAVEASSETCATEGORY("save-aasetcategory"),UPDATEASSETCATEGORY("update-aasetcategory"),SAVEASSET("save-asset"),UPDATEASSET("update-asset"),
	   SAVEREVALUATION( "save-revaluation"),SAVEDISPOSAL("save-disposal");

	    private String value;

	    KafkaTopicName(final String value) {
	            this.value = value;
	    }

	    @Override
	    @JsonValue
	    public String toString() {
	            return String.valueOf(value);
	    }

	    @JsonCreator
	    public static KafkaTopicName fromValue(final String text) {
	            for (final KafkaTopicName b : KafkaTopicName.values())
	                    if (String.valueOf(b.value).equals(text))
	                            return b;
	            return null;
	    }

	}