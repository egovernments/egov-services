package org.egov.lcms.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum NoticeType {

	CREATE_VAKALATNAMA("CREATE_VAKALATNAMA");

	private String value;

	NoticeType(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return name().toUpperCase();
	}

	@JsonCreator
	public static NoticeType fromValue(String text) {
		for (NoticeType noticeType : NoticeType.values()) {
			if (String.valueOf(noticeType.value).equals(text)) {
				return noticeType;
			}
		}
		return null;
	}
}
