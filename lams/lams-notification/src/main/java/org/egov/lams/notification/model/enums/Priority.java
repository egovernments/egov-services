package org.egov.lams.notification.model.enums;

public enum Priority {
	HIGH, MEDIUM, LOW;

	@Override
	public String toString() {
		return this.name().toLowerCase();
	}

}
