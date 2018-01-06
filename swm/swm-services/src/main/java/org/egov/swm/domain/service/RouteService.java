package org.egov.swm.domain.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.CollectionPoint;
import org.egov.swm.domain.model.CollectionPointSearch;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.Route;
import org.egov.swm.domain.model.RouteCollectionPointMap;
import org.egov.swm.domain.model.RouteSearch;
import org.egov.swm.domain.repository.RouteRepository;
import org.egov.swm.web.requests.RouteRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class RouteService {

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private CollectionPointService collectionPointService;

    @Autowired
    private CollectionTypeService collectionTypeService;

    @Autowired
    private DumpingGroundService dumpingGroundService;

    @Transactional
    public RouteRequest create(final RouteRequest routeRequest) {

        validate(routeRequest);

        Long userId = null;

        if (routeRequest.getRequestInfo() != null && routeRequest.getRequestInfo().getUserInfo() != null
                && null != routeRequest.getRequestInfo().getUserInfo().getId())
            userId = routeRequest.getRequestInfo().getUserInfo().getId();

        for (final Route r : routeRequest.getRoutes()) {

            setAuditDetails(r, userId);
            r.setCode(UUID.randomUUID().toString().replace("-", ""));

        }

        return routeRepository.save(routeRequest);

    }

    @Transactional
    public RouteRequest update(final RouteRequest routeRequest) {

        Long userId = null;

        if (routeRequest.getRequestInfo() != null && routeRequest.getRequestInfo().getUserInfo() != null
                && null != routeRequest.getRequestInfo().getUserInfo().getId())
            userId = routeRequest.getRequestInfo().getUserInfo().getId();

        for (final Route r : routeRequest.getRoutes())
            setAuditDetails(r, userId);

        validate(routeRequest);

        return routeRepository.update(routeRequest);

    }

    public Pagination<Route> search(final RouteSearch routeSearch) {

        return routeRepository.search(routeSearch);
    }

    private void validate(final RouteRequest routeRequest) {

        findDuplicatesInUniqueFields(routeRequest);

        CollectionPointSearch search;

        Pagination<CollectionPoint> collectionPoints;

        for (final Route route : routeRequest.getRoutes()) {

            // Validate Collection Type

            if (route.getCollectionType() != null
                    && (route.getCollectionType().getCode() == null || route.getCollectionType().getCode().isEmpty()))
                throw new CustomException("CollectionType",
                        "The field CollectionType Code is Mandatory . It cannot be not be null or empty.Please provide correct value ");

            if (route.getCollectionType() != null && route.getCollectionType().getCode() != null)
                route.setCollectionType(collectionTypeService.getCollectionType(route.getTenantId(),
                        route.getCollectionType().getCode(), routeRequest.getRequestInfo()));
            else
                throw new CustomException("CollectionType", "CollectionType is required");

            if (route.getEndingCollectionPoint() != null && route.getEndingCollectionPoint().getCollectionPoint() != null
                    && route.getEndingCollectionPoint().getCollectionPoint().getCode() != null
                    && !route.getEndingCollectionPoint().getCollectionPoint().getCode().isEmpty()
                    && route.getEndingDumpingGround() != null && route.getEndingDumpingGround().getDumpingGround() != null
                    && route.getEndingDumpingGround().getDumpingGround().getCode() != null
                    && !route.getEndingDumpingGround().getDumpingGround().getCode().isEmpty())
                throw new CustomException("CollectionPoint",
                        "Both Ending Collection point and  Ending Dumping Ground cannot pass .");

            // Validate CollectionPoints
            if (route.getCollectionPoints() != null) {

                route.getStartingCollectionPoint().setIsStartingCollectionPoint(true);

                route.getCollectionPoints().add(route.getStartingCollectionPoint());

                if (route.getEndingCollectionPoint() != null && route.getEndingCollectionPoint().getCollectionPoint() != null
                        && route.getEndingCollectionPoint().getCollectionPoint().getCode() != null
                        && !route.getEndingCollectionPoint().getCollectionPoint().getCode().isEmpty()) {

                    route.getEndingCollectionPoint().setIsEndingCollectionPoint(true);
                    route.getCollectionPoints().add(route.getEndingCollectionPoint());

                }

                if (route.getEndingDumpingGround() != null && route.getEndingDumpingGround().getDumpingGround() != null
                        && route.getEndingDumpingGround().getDumpingGround().getCode() != null
                        && !route.getEndingDumpingGround().getDumpingGround().getCode().isEmpty()) {

                    route.getCollectionPoints().add(route.getEndingDumpingGround());

                }

                for (RouteCollectionPointMap rcpm : route.getCollectionPoints()) {
                    rcpm.setId(UUID.randomUUID().toString().replace("-", ""));
                    rcpm.setTenantId(route.getTenantId());

                    if (rcpm != null && rcpm.getCollectionPoint() != null && rcpm.getCollectionPoint().getCode() != null
                            && !rcpm.getCollectionPoint().getCode().isEmpty()) {
                        search = new CollectionPointSearch();
                        search.setTenantId(route.getTenantId());
                        search.setCode(rcpm.getCollectionPoint().getCode());

                        collectionPoints = collectionPointService.search(search);

                        if (collectionPoints == null || collectionPoints.getPagedData() == null
                                || collectionPoints.getPagedData().isEmpty())
                            throw new CustomException("CollectionPoint",
                                    "Given CollectionPoint is invalid: " + rcpm.getCollectionPoint());
                        else
                            rcpm.setCollectionPoint(collectionPoints.getPagedData().get(0));
                    }

                    // Validate Ending Dumping ground
                    if (rcpm.getDumpingGround() != null && rcpm.getDumpingGround().getCode() != null
                            && !rcpm.getDumpingGround().getCode().isEmpty())
                        rcpm.setDumpingGround(dumpingGroundService.getDumpingGround(route.getTenantId(),
                                rcpm.getDumpingGround().getCode(), routeRequest.getRequestInfo()));

                }
            }

            validateUniqueFields(route);

        }
    }

    private void findDuplicatesInUniqueFields(final RouteRequest routeRequest) {

        final Map<String, String> nameMap = new HashMap<>();

        for (final Route route : routeRequest.getRoutes())
            if (route.getName() != null) {
                if (nameMap.get(route.getName()) != null)
                    throw new CustomException("name", "Duplicate names in given routes : " + route.getName());

                nameMap.put(route.getName(), route.getName());
            }

    }

    private void validateUniqueFields(final Route route) {

        if (route.getName() != null)
            if (!routeRepository.uniqueCheck(route.getTenantId(), "name", route.getName(), "code", route.getCode()))
                throw new CustomException("name",
                        "The field name must be unique in the system The  value " + route.getName()
                                + " for the field name already exists in the system. Please provide different value ");

    }

    private void setAuditDetails(final Route contract, final Long userId) {

        if (contract.getAuditDetails() == null)
            contract.setAuditDetails(new AuditDetails());

        if (null == contract.getCode() || contract.getCode().isEmpty()) {
            contract.getAuditDetails().setCreatedBy(null != userId ? userId.toString() : null);
            contract.getAuditDetails().setCreatedTime(new Date().getTime());
        }

        contract.getAuditDetails().setLastModifiedBy(null != userId ? userId.toString() : null);
        contract.getAuditDetails().setLastModifiedTime(new Date().getTime());
    }

}