package org.egov.pgr.web.validation.impl;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.egov.pgr.web.validation.ValidateSevaRequest;
import org.egov.pgr.web.validation.model.RequestInfo;
import org.egov.pgr.web.validation.model.ServiceRequest;
import org.egov.pgr.web.validation.model.ServiceRequestReq;
import org.springframework.stereotype.Service;

@Service
public class ValidateSevaRequestImpl implements ValidateSevaRequest {

	@Override
	public Boolean validate(final ServiceRequestReq request) {

		return validateRequestInfo(request.getRequestInfo()) && validateServiceRequest(request.getServiceRequest());
	}

	private boolean validateRequestInfo(RequestInfo requestInfo) {
		return (StringUtils.isNotBlank(requestInfo.getApiId()) && StringUtils.isNotBlank(requestInfo.getVer())
				&& Objects.nonNull(requestInfo.getTs()));
	}

	private boolean validateServiceRequest(ServiceRequest serviceRequest) {
		if (StringUtils.isNotBlank(serviceRequest.getComplaintTypeCode())
				&& StringUtils.isNotBlank(serviceRequest.getDetails())
				&& StringUtils.isNotBlank(serviceRequest.getCrossHierarchyId()))
			return true;
		else if (StringUtils.isNotBlank(serviceRequest.getCrossHierarchyId())
				|| (Objects.nonNull(serviceRequest.getLat()) && (Objects.nonNull(serviceRequest.getLat()))))
			return true;
		else
			return false;
	}
}