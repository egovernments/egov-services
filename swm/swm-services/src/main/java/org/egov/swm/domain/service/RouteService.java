package org.egov.swm.domain.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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

        Pagination<Route> routePage = routeRepository.search(routeSearch);

        // Filter ending collection point or dumping ground
        if (routeSearch.getIsEndingDumpingGround() != null &&
                routeSearch.getIsEndingDumpingGround() && !routePage.getPagedData().isEmpty()) {
            List<Route> routes = routePage.getPagedData();
            for (Route route : routes) {
                List<RouteCollectionPointMap> routeCollectionPointMapList = route.getCollectionPoints().stream()
                        .filter(collectionPoint -> collectionPoint.getIsEndingCollectionPoint()
                                || collectionPoint.getDumpingGround() != null)
                        .collect(Collectors.toList());

                route.setCollectionPoints(routeCollectionPointMapList);
            }
            routePage.setPagedData(routes);
        }

        // Filter to return only collection point's excluding dumping ground.
        if (routeSearch.getExcludeDumpingGround() != null &&
                routeSearch.getExcludeDumpingGround() && !routePage.getPagedData().isEmpty()) {
            List<Route> routes = routePage.getPagedData();
            for (Route route : routes) {
                route.getCollectionPoints()
                        .removeIf(routeCollectionPointMap -> routeCollectionPointMap.getDumpingGround() != null);
            }
        }

        return routePage;
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

            // Validate CollectionPoints
            if (route.getCollectionPoints() != null) {
                for (RouteCollectionPointMap rcpm : route.getCollectionPoints()) {
                    rcpm.setId(UUID.randomUUID().toString().replace("-", ""));
                    rcpm.setTenantId(route.getTenantId());

                    if (rcpm != null && rcpm.getIsStartingCollectionPoint() != null && rcpm.getIsEndingCollectionPoint() != null
                            && rcpm.getIsStartingCollectionPoint() && rcpm.getIsEndingCollectionPoint())
                        throw new CustomException("CollectionPoint",
                                "Both IsStartingCollectionPoint and IsEndingCollectionPoint cannot be true");

                    if (rcpm != null && rcpm.getDumpingGround() != null
                            && rcpm.getDumpingGround().getCode() != null
                            && !rcpm.getDumpingGround().getCode().isEmpty() && rcpm.getCollectionPoint() != null
                            && rcpm.getCollectionPoint().getCode() != null && !rcpm.getCollectionPoint().getCode().isEmpty()
                            && rcpm.getIsEndingCollectionPoint() != null && rcpm.getIsEndingCollectionPoint())
                        throw new CustomException("CollectionPoint",
                                "Both Collection point and  Ending DumpingGround cannot be  Mandatory .");

                    if (rcpm != null
                            && (rcpm.getCollectionPoint() == null
                                    || (rcpm.getCollectionPoint().getCode() == null
                                            || rcpm.getCollectionPoint().getCode().isEmpty()))
                            && (rcpm.getDumpingGround() == null || rcpm.getDumpingGround().getCode() == null
                                    || rcpm.getDumpingGround().getCode().isEmpty()))
                        throw new CustomException("CollectionPoint",
                                "The field CollectionPoint Code or Ending DumpingGround Code is Mandatory . It cannot be not be null or empty.Please provide correct value ");

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
        final Map<String, String> collectionPointMap = new HashMap<>();
        Boolean startingCollectionPoint, endingCollectionPoint, dumpingGround;
        Double totalDistance, totalGarbageEstimate = (double) 0;

        for (final Route route : routeRequest.getRoutes()) {

            startingCollectionPoint = endingCollectionPoint = dumpingGround = false;
            totalDistance = totalGarbageEstimate = (double) 0;
            if (route.getName() != null) {
                if (nameMap.get(route.getName()) != null)
                    throw new CustomException("name", "Duplicate names in given routes : " + route.getName());

                nameMap.put(route.getName(), route.getName());
            }
            if (route.getCollectionPoints() != null) {
                for (RouteCollectionPointMap rcpm : route.getCollectionPoints()) {

                    if (rcpm.getDistance() != null)
                        totalDistance = totalDistance + rcpm.getDistance();

                    if (rcpm.getGarbageEstimate() != null)
                        totalGarbageEstimate = totalGarbageEstimate + rcpm.getGarbageEstimate();

                    if (dumpingGround && rcpm.getDumpingGround() != null && rcpm.getDumpingGround().getCode() != null
                            && !rcpm.getDumpingGround().getCode().isEmpty()) {
                        throw new CustomException("dumpingGround",
                                "Duplicate ending dumpingGrounds in given routes : "
                                        + rcpm.getDumpingGround().getCode());
                    }

                    if (rcpm.getDumpingGround() != null && rcpm.getDumpingGround().getCode() != null
                            && !rcpm.getDumpingGround().getCode().isEmpty()) {
                        dumpingGround = true;
                    }

                    if (rcpm.getCollectionPoint() != null && rcpm.getCollectionPoint().getCode() != null
                            && !rcpm.getCollectionPoint().getCode().isEmpty()) {

                        if (startingCollectionPoint && rcpm.getIsStartingCollectionPoint())
                            throw new CustomException("collectionPoint",
                                    "Duplicate starting collectionPoints in given routes : "
                                            + rcpm.getCollectionPoint().getCode());

                        if (rcpm.getIsStartingCollectionPoint() != null && rcpm.getIsStartingCollectionPoint())
                            startingCollectionPoint = true;

                        if (endingCollectionPoint && rcpm.getIsEndingCollectionPoint())
                            throw new CustomException("collectionPoint",
                                    "Duplicate ending collectionPoints in given routes : "
                                            + rcpm.getCollectionPoint().getCode());

                        if (rcpm.getIsEndingCollectionPoint() != null && rcpm.getIsEndingCollectionPoint())
                            endingCollectionPoint = true;

                        if (rcpm.getCollectionPoint() != null && rcpm.getCollectionPoint().getCode() != null
                                && collectionPointMap.get(rcpm.getCollectionPoint().getCode()) != null)
                            throw new CustomException("collectionPoint",
                                    "Duplicate collectionPoints in given routes : " + rcpm.getCollectionPoint().getCode());
                        if (rcpm.getCollectionPoint() != null && rcpm.getCollectionPoint().getCode() != null) {
                            collectionPointMap.put(rcpm.getCollectionPoint().getCode(), rcpm.getCollectionPoint().getCode());
                        }
                    }
                }

                if (!startingCollectionPoint) {
                    throw new CustomException("startingCollectionPoint",
                            "The field starting collection point is Mandatory . It cannot be not be null or empty.Please provide correct value");
                }

                if (endingCollectionPoint && dumpingGround) {
                    throw new CustomException("collectionPoint",
                            "Both ending collection point and ending dumping ground cannot be sent");
                }

                if (!endingCollectionPoint && !dumpingGround)
                    throw new CustomException("collectionPoint",
                            "Either ending collection point or ending dumping should be mandatory while route creation");

            }

            if (route.getTotalDistance() != null && totalDistance != null
                    && totalDistance.compareTo(route.getTotalDistance()) != 0) {
                throw new CustomException("totalDistance",
                        "Total distance covered is not same as the sum of distance covered by the collection points");
            }

            if (route.getTotalGarbageEstimate() != null && totalGarbageEstimate != null
                    && totalGarbageEstimate.compareTo(route.getTotalGarbageEstimate()) != 0) {
                throw new CustomException("totalGarbageEstimate",
                        "Total garbage collection estimate is not same as the sum of garbage collection estimates defined in the collection points");
            }

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