package org.egov.swm.domain.service;

import java.util.ArrayList;
import java.util.Date;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.CollectionPoint;
import org.egov.swm.domain.model.CollectionPointSearch;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.Route;
import org.egov.swm.domain.model.RouteSearch;
import org.egov.swm.domain.model.SanitationStaffTarget;
import org.egov.swm.domain.model.SanitationStaffTargetSearch;
import org.egov.swm.domain.repository.SanitationStaffTargetRepository;
import org.egov.swm.web.contract.EmployeeResponse;
import org.egov.swm.web.repository.EmployeeRepository;
import org.egov.swm.web.repository.IdgenRepository;
import org.egov.swm.web.requests.SanitationStaffTargetRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SanitationStaffTargetService {

    @Autowired
    private SanitationStaffTargetRepository sanitationStaffTargetRepository;

    @Autowired
    private IdgenRepository idgenRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private RouteService routeService;

    @Autowired
    private DumpingGroundService dumpingGroundService;

    @Autowired
    private SwmProcessService swmProcessService;

    @Autowired
    private CollectionPointService collectionPointService;

    @Value("${egov.swm.sanitationstaff.targetnum.idgen.name}")
    private String idGenNameForTargetNumPath;

    @Transactional
    public SanitationStaffTargetRequest create(final SanitationStaffTargetRequest sanitationStaffTargetRequest) {

        validate(sanitationStaffTargetRequest);

        Long userId = null;

        if (sanitationStaffTargetRequest.getRequestInfo() != null
                && sanitationStaffTargetRequest.getRequestInfo().getUserInfo() != null
                && null != sanitationStaffTargetRequest.getRequestInfo().getUserInfo().getId())
            userId = sanitationStaffTargetRequest.getRequestInfo().getUserInfo().getId();

        for (final SanitationStaffTarget v : sanitationStaffTargetRequest.getSanitationStaffTargets()) {

            setAuditDetails(v, userId);

            v.setTargetNo(generateTargetNumber(v.getTenantId(), sanitationStaffTargetRequest.getRequestInfo()));

            if (v.getCollectionPoints() == null)
                v.setCollectionPoints(new ArrayList<>());
        }

        return sanitationStaffTargetRepository.save(sanitationStaffTargetRequest);

    }

    @Transactional
    public SanitationStaffTargetRequest update(final SanitationStaffTargetRequest sanitationStaffTargetRequest) {

        Long userId = null;

        if (sanitationStaffTargetRequest.getRequestInfo() != null
                && sanitationStaffTargetRequest.getRequestInfo().getUserInfo() != null
                && null != sanitationStaffTargetRequest.getRequestInfo().getUserInfo().getId())
            userId = sanitationStaffTargetRequest.getRequestInfo().getUserInfo().getId();

        for (final SanitationStaffTarget v : sanitationStaffTargetRequest.getSanitationStaffTargets()) {

            setAuditDetails(v, userId);

            if (v.getCollectionPoints() == null)
                v.setCollectionPoints(new ArrayList<>());

        }

        validate(sanitationStaffTargetRequest);

        return sanitationStaffTargetRepository.update(sanitationStaffTargetRequest);

    }

    private void validate(final SanitationStaffTargetRequest sanitationStaffTargetRequest) {

        EmployeeResponse employeeResponse = null;
        final RouteSearch routeSearch = new RouteSearch();
        Pagination<Route> routes;
        CollectionPointSearch search;
        Pagination<CollectionPoint> collectionPoints;

        for (final SanitationStaffTarget sanitationStaffTarget : sanitationStaffTargetRequest.getSanitationStaffTargets()) {

            if (sanitationStaffTarget.getSwmProcess() != null
                    && (sanitationStaffTarget.getSwmProcess().getCode() == null
                            || sanitationStaffTarget.getSwmProcess().getCode().isEmpty()))
                throw new CustomException("ServicesOffered",
                        "The field ServicesOffered Code is Mandatory . It cannot be not be null or empty.Please provide correct value ");

            // Validate Swm Process

            if (sanitationStaffTarget.getSwmProcess() != null
                    && sanitationStaffTarget.getSwmProcess().getCode() != null)
                sanitationStaffTarget.setSwmProcess(swmProcessService.getSwmProcess(sanitationStaffTarget.getTenantId(),
                        sanitationStaffTarget.getSwmProcess().getCode(),
                        sanitationStaffTargetRequest.getRequestInfo()));

            // Validate Route

            if (sanitationStaffTarget.getRoute() != null && sanitationStaffTarget.getRoute().getCode() != null
                    && !sanitationStaffTarget.getRoute().getCode().isEmpty()) {

                routeSearch.setTenantId(sanitationStaffTarget.getTenantId());
                routeSearch.setCode(sanitationStaffTarget.getRoute().getCode());
                routes = routeService.search(routeSearch);

                if (routes == null || routes.getPagedData() == null || routes.getPagedData().isEmpty())
                    throw new CustomException("Route",
                            "Given Route is invalid: " + sanitationStaffTarget.getRoute().getCode());
                else
                    sanitationStaffTarget.setRoute(routes.getPagedData().get(0));

            }

            if (sanitationStaffTarget.getEmployee() != null && (sanitationStaffTarget.getEmployee().getCode() == null
                    || sanitationStaffTarget.getEmployee().getCode().isEmpty()))
                throw new CustomException("Employee",
                        "The field Employee Code is Mandatory . It cannot be not be null or empty.Please provide correct value ");

            // Validate Employee

            if (sanitationStaffTarget.getEmployee() != null && sanitationStaffTarget.getEmployee().getCode() != null) {

                employeeResponse = employeeRepository.getEmployeeByCode(sanitationStaffTarget.getEmployee().getCode(),
                        sanitationStaffTarget.getTenantId(), sanitationStaffTargetRequest.getRequestInfo());

                if (employeeResponse == null || employeeResponse.getEmployees() == null
                        || employeeResponse.getEmployees().isEmpty())
                    throw new CustomException("Employee",
                            "Given Employee is invalid: " + sanitationStaffTarget.getEmployee().getCode());
                else
                    sanitationStaffTarget.setEmployee(employeeResponse.getEmployees().get(0));

            }

            // Validate Dumping ground
            if (sanitationStaffTarget.getDumpingGround() != null
                    && sanitationStaffTarget.getDumpingGround().getCode() != null)
                sanitationStaffTarget.setDumpingGround(dumpingGroundService.getDumpingGround(
                        sanitationStaffTarget.getTenantId(), sanitationStaffTarget.getDumpingGround().getCode(),
                        sanitationStaffTargetRequest.getRequestInfo()));

            // Validate CollectionPoints
            if (sanitationStaffTarget.getCollectionPoints() != null) {

                for (CollectionPoint cp : sanitationStaffTarget.getCollectionPoints()) {

                    if (cp != null && (cp.getCode() == null || cp.getCode().isEmpty()))
                        throw new CustomException("CollectionPoint",
                                "The field CollectionPoint Code is Mandatory . It cannot be not be null or empty.Please provide correct value ");

                    if (cp != null && cp.getCode() != null) {
                        search = new CollectionPointSearch();
                        search.setTenantId(sanitationStaffTarget.getTenantId());
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
            }

        }
    }

    private String generateTargetNumber(final String tenantId, final RequestInfo requestInfo) {

        return idgenRepository.getIdGeneration(tenantId, requestInfo, idGenNameForTargetNumPath);
    }

    public Pagination<SanitationStaffTarget> search(final SanitationStaffTargetSearch sanitationStaffTargetSearch) {

        return sanitationStaffTargetRepository.search(sanitationStaffTargetSearch);
    }

    private void setAuditDetails(final SanitationStaffTarget contract, final Long userId) {

        if (contract.getAuditDetails() == null)
            contract.setAuditDetails(new AuditDetails());

        if (null == contract.getTargetNo() || contract.getTargetNo().isEmpty()) {
            contract.getAuditDetails().setCreatedBy(null != userId ? userId.toString() : null);
            contract.getAuditDetails().setCreatedTime(new Date().getTime());
        }

        contract.getAuditDetails().setLastModifiedBy(null != userId ? userId.toString() : null);
        contract.getAuditDetails().setLastModifiedTime(new Date().getTime());
    }

}