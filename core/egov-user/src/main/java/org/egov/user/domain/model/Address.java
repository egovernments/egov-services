package org.egov.user.domain.model;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.egov.user.domain.model.enums.AddressType;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Address {
    private String pinCode;
    private String city;
    private String address;
    private AddressType type;
	private Long id;
	private String tenantId;
	private Long userId;
	private String addressType;
	private Long LastModifiedBy;
	private Date LastModifiedDate;

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
