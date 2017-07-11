package org.egov.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Property can be created from different channels Eg. System (properties
 * created by ULB officials), CFC Counter (From citizen faciliation counters)
 * etc. Here we are defining some known channels, there can be more client to
 * client.
 */
public enum ChannelEnum {
	SYSTEM("SYSTEM"),

	CFC_COUNTER("CFC_COUNTER"),

	CITIZEN("CITIZEN"),

	DATA_ENTRY("DATA_ENTRY");

	private String value;

	ChannelEnum(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static ChannelEnum fromValue(String text) {
		for (ChannelEnum b : ChannelEnum.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}
}