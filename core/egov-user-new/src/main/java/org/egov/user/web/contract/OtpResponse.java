package org.egov.user.web.contract;

import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.user.domain.model.Otp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OtpResponse {

	private ResponseInfo responseInfo;
    private List<Otp> otps;
}
