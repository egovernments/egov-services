package org.egov.pgr.rest.validate.impl;

import java.util.Objects;

import org.egov.pgr.model.RequestInfo;
import org.egov.pgr.model.ServiceRequest;
import org.egov.pgr.model.ServiceRequestReq;
import org.egov.pgr.rest.validate.ValidateSevaRequest;
import org.egov.pgr.utils.StringUtils;
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