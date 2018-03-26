package org.egov.boundary.domain.service;

import org.egov.boundary.domain.model.Location;
import org.egov.boundary.web.contract.tenant.model.Tenant;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

import static org.egov.boundary.util.BoundaryConstants.TENANT_SEARCH_STATE_MISMATCH;
import static org.egov.boundary.util.BoundaryConstants.TENANT_SEARCH_STATE_MISMATCH_DESC;

@Service
public class TenantService {
    private static final Logger LOG = LoggerFactory.getLogger(TenantService.class);
    private static final HashMap<String, String> STATE_CODES = new HashMap<>();

    static {
        STATE_CODES.put("Andhra Pradesh", "ap");
        STATE_CODES.put("Maharashtra", "mh");
        STATE_CODES.put("Punjab", "pb");
    }

    private final GoogleLocationService locationService;
    private final MdmsService mdmsService;

    @Autowired
    public TenantService(GoogleLocationService googleLocationService, MdmsService mdmsService) {
        this.locationService = googleLocationService;
        this.mdmsService = mdmsService;
    }

    /**
     * Two step process involving,
     * - Reverse geocoding to get a location object
     * - Resolve location object to Tenant
     *
     * @param lat          Latitude
     * @param lng          Longitude
     * @param baseTenantId Base tenant Id, usually state
     * @param requestInfo  Request Info object detailing the request
     * @return Tenant if exists, or null if no tenant exists for given lat/lng
     * @throws CustomException when there is a mismatch between the module source and resolved state from location
     */
    public Tenant resolveTenant(double lat, double lng, String baseTenantId, RequestInfo requestInfo) {
        String state = baseTenantId;
        if (state.contains(".")) {
            state = state.substring(0, state.indexOf('.'));
        }
        Optional<Location> location = locationService.reverseGeoCode(lat, lng);
        if (location.isPresent()) {
            Location loc = location.get();

            if (state.equalsIgnoreCase(STATE_CODES.get(loc.getState()))) {
                String filter = "$.[?(@.name == '" + loc.getCity() + "')]";

                Optional<Tenant> tenant = mdmsService.fetchTenant(state, filter, requestInfo);
                if (tenant.isPresent()) return tenant.get();
            } else {
                LOG.error("Expected state: {} , received {} ", STATE_CODES.get(loc.getState()), state);
                throw new CustomException(TENANT_SEARCH_STATE_MISMATCH, TENANT_SEARCH_STATE_MISMATCH_DESC);
            }
        }
        return null;
    }

}
