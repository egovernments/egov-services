
package org.egov.pgrrest.common.persistence.entity.enums;

public enum CitizenFeedback {
	UNSPECIFIED, ONE, TWO, THREE, FOUR, FIVE;
	@Override
	public String toString() {
		return name().toUpperCase();
	}
}
