package org.egov.works.commons.web.contract;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets WorkOrderOfflineStatus
 */
public enum WorkOrderOfflineStatus {

	WORK_ORDER_ACKNOWLEDGED("WORK_ORDER_ACKNOWLEDGED"),

	SITE_HANDED_OVER("SITE_HANDED_OVER"),

	WORK_COMMENCED("WORK_COMMENCED");

	private String value;

	WorkOrderOfflineStatus(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static WorkOrderOfflineStatus fromValue(String text) {
		for (WorkOrderOfflineStatus b : WorkOrderOfflineStatus.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}
}
