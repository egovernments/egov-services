package org.egov.user.domain.model;

import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.egov.user.domain.model.enums.AddressType;

@Getter
@Builder
public class Address {
    private String pinCode;
    private String city;
    private String address;
    private AddressType type;

    public boolean isInvalid() {
    	return isPinCodeInvalid()
				|| isCityInvalid()
				|| isAddressInvalid();
	}

	public boolean isNotEmpty() {
    	return StringUtils.isNotEmpty(pinCode)
				|| StringUtils.isNotEmpty(city)
				|| StringUtils.isNotEmpty(address) ;
	}

	public boolean isPinCodeInvalid() {
    	return pinCode != null && pinCode.length() > 10;
	}

	public boolean isCityInvalid() {
    	return city != null && city.length() > 300;
	}

	public boolean isAddressInvalid() {
    	return address != null && address.length() > 300;
	}
}
