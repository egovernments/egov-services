package org.egov.works.commons.web.contract;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets DetailedEstimateOfflineStatus
 */
public enum DetailedEstimateOfflineStatus {

	NOTICEINVITINGTENDERRELEASED("NOTICEINVITINGTENDERRELEASED"),

	TENDER_DOCUMENT_RELEASED("TENDER_DOCUMENT_RELEASED"),

	TENDER_OPENED("TENDER_OPENED"),

	TECHNICAL_EVALUATION_DONE("TECHNICAL_EVALUATION_DONE"),

	COMMERCIAL_EVALUATION_DONE("COMMERCIAL_EVALUATION_DONE"),

	L1_TENDER_FINALIZED("L1_TENDER_FINALIZED");

	private String value;

	DetailedEstimateOfflineStatus(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static DetailedEstimateOfflineStatus fromValue(String text) {
		for (DetailedEstimateOfflineStatus b : DetailedEstimateOfflineStatus.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}
}
