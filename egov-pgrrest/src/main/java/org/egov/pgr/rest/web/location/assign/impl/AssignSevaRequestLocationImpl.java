package org.egov.pgr.rest.web.location.assign.impl;

import java.util.Objects;

import org.egov.pgr.rest.web.location.assign.AssignSevaRequestLocation;
import org.egov.pgr.rest.web.model.ServiceRequest;
import org.springframework.stereotype.Service;

@Service
public class AssignSevaRequestLocationImpl implements AssignSevaRequestLocation {

	@Override
	public Boolean assign(final ServiceRequest request) {

		return (Objects.nonNull(request.getCrossHierarchyId()) && Long.parseLong(request.getCrossHierarchyId()) > 0);
	}

}