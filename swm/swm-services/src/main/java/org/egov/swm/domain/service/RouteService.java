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

            // Validate Starting Collection Point

            if (route.getStartingCollectionPoint() != null && (route.getStartingCollectionPoint().getCode() == null
                    || route.getStartingCollectionPoint().getCode().isEmpty()))
                throw new CustomException("StartingCollectionPoint",
                        "The field StartingCollectionPoint Code is Mandatory . It cannot be not be null or empty.Please provide correct value ");

            if (route.getStartingCollectionPoint() != null && route.getStartingCollectionPoint().getCode() != null) {

                search = new CollectionPointSearch();
                search.setTenantId(route.getTenantId());
                search.setCode(route.getStartingCollectionPoint().getCode());

                collectionPoints = collectionPointService.search(search);

                if (collectionPoints == null || collectionPoints.getPagedData() == null
                        || collectionPoints.getPagedData().isEmpty())
                    throw new CustomException("StartingCollectionPoint", "Given StartingCollectionPoint is invalid: "
                            + route.getStartingCollectionPoint().getCode());
                else
                    route.setStartingCollectionPoint(collectionPoints.getPagedData().get(0));
            }

            if (route.getEndingCollectionPoint() != null && (route.getEndingCollectionPoint().getCode() == null
                    || route.getEndingCollectionPoint().getCode().isEmpty()))
                throw new CustomException("EndingCollectionPoint",
                        "The field EndingCollectionPoint Code is Mandatory . It cannot be not be null or empty.Please provide correct value ");

            // Validate Ending Collection Point

            if (route.getEndingCollectionPoint() != null && route.getEndingCollectionPoint().getCode() != null) {

                search = new CollectionPointSearch();
                search.setTenantId(route.getTenantId());
                search.setCode(route.getEndingCollectionPoint().getCode());

                collectionPoints = collectionPointService.search(search);

                if (collectionPoints == null || collectionPoints.getPagedData() == null
                        || collectionPoints.getPagedData().isEmpty())
                    throw new CustomException("EndingCollectionPoint",
                            "Given EndingCollectionPoint is invalid: " + route.getEndingCollectionPoint().getCode());
                else
                    route.setEndingCollectionPoint(collectionPoints.getPagedData().get(0));
            }

            if (route.getEndingDumpingGroundPoint() != null && (route.getEndingDumpingGroundPoint().getCode() == null
                    || route.getEndingDumpingGroundPoint().getCode().isEmpty()))
                throw new CustomException("DumpingGround",
                        "The field DumpingGround Code is Mandatory . It cannot be not be null or empty.Please provide correct value ");

            // Validate Ending Dumping ground
            if (route.getEndingDumpingGroundPoint() != null && route.getEndingDumpingGroundPoint().getCode() != null)
                route.setEndingDumpingGroundPoint(dumpingGroundService.getDumpingGround(route.getTenantId(),
                        route.getEndingDumpingGroundPoint().getCode(), routeRequest.getRequestInfo()));

            // Validate CollectionPoints
            if (route.getCollectionPoints() != null)
                for (CollectionPoint cp : route.getCollectionPoints()) {

                    if (cp != null && (cp.getCode() == null || cp.getCode().isEmpty()))
                        throw new CustomException("CollectionPoint",
                                "The field CollectionPoint Code is Mandatory . It cannot be not be null or empty.Please provide correct value ");

                    if (cp != null && cp.getCode() != null) {
                        search = new CollectionPointSearch();
                        search.setTenantId(route.getTenantId());
                        search.setCode(cp.getCode());

                        collectionPoints = collectionPointService.search(search);

                        if (collectionPoints == null || collectionPoints.getPagedData() == null
                                || collectionPoints.getPagedData().isEmpty())
                            throw new CustomException("CollectionPoint",
                                    "Given CollectionPoint is invalid: " + cp.getName());
                        else
                            cp = collectionPoints.getPagedData().get(0);
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