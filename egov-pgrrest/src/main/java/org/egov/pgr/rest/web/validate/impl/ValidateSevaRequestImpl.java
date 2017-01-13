package org.egov.pgr.rest.web.validate.impl;

import java.util.Objects;

import org.egov.infra.utils.StringUtils;
import org.egov.pgr.rest.web.model.RequestInfo;
import org.egov.pgr.rest.web.model.ServiceRequest;
import org.egov.pgr.rest.web.model.ServiceRequestReq;
import org.egov.pgr.rest.web.validate.ValidateSevaRequest;
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