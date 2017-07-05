package org.egov.pgrrest.common.persistence.entity.enums;

//any future Receiving mode addition should be added at the end of this
// enum
// since we are asking hibernate use its ordinal to be persisted
public enum ReceivingMode {
	WEBSITE, SMS, CALL, EMAIL, MANUAL, MOBILE;
	@Override
	public String toString() {
		return name().toUpperCase();
	}
}