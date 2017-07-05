package org.egov.pgrrest.read.persistence.repository;

import org.egov.pgrrest.common.persistence.entity.ServiceType;
import org.egov.pgrrest.common.persistence.entity.ServiceTypeKeyword;
import org.egov.pgrrest.common.persistence.entity.Submission;
import org.egov.pgrrest.common.persistence.repository.ServiceRequestTypeJpaRepository;
import org.egov.pgrrest.common.persistence.repository.ServiceTypeKeywordJpaRepository;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ServiceRequestTypeRepository {

    private ServiceRequestTypeJpaRepository serviceRequestTypeJpaRepository;
    private ServiceTypeKeywordJpaRepository serviceTypeKeywordJpaRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public ServiceRequestTypeRepository(ServiceRequestTypeJpaRepository serviceRequestTypeJpaRepository,
                                        ServiceTypeKeywordJpaRepository serviceTypeKeywordJpaRepository) {
        this.serviceRequestTypeJpaRepository = serviceRequestTypeJpaRepository;
        this.serviceTypeKeywordJpaRepository = serviceTypeKeywordJpaRepository;
    }

    public List<ServiceType> findActiveServiceRequestTypesByCategoryAndTenantId(Long categoryId, String tenantId) {
        final List<ServiceType> serviceTypes = serviceRequestTypeJpaRepository
            .findActiveServiceTypes(categoryId, tenantId);
        enrichWithKeywords(serviceTypes, tenantId);
        return serviceTypes;
    }

    public List<ServiceType> getFrequentlyFiledServiceRequests(Integer count, String tenantId) {
        final List<String> serviceCodes = getFrequentlyUsedServiceCodes(count, tenantId);
        if (CollectionUtils.isEmpty(serviceCodes)) {
            return Collections.emptyList();
        }
        final List<ServiceType> serviceTypes = serviceRequestTypeJpaRepository
            .findActiveServiceTypes(serviceCodes, tenantId);
        enrichWithKeywords(serviceTypes, tenantId);
        return serviceTypes;
    }

    public ServiceType getServiceRequestType(String serviceTypeCode, String tenantId) {
        final ServiceType serviceType = serviceRequestTypeJpaRepository
            .findByCodeAndTenantId(serviceTypeCode, tenantId);
        if (serviceType != null) {
            enrichWithKeywords(Collections.singletonList(serviceType), tenantId);
            return serviceType;
        }
        return null;
    }

    public List<ServiceType> getAllServiceTypes(String tenantId) {
        final List<ServiceType> serviceTypes = serviceRequestTypeJpaRepository.findByTenantId(tenantId);
        enrichWithKeywords(serviceTypes, tenantId);
        return serviceTypes;
    }

    private void enrichWithKeywords(List<ServiceType> serviceTypes, String tenantId) {
        final List<String> serviceCodes = getServiceCodes(serviceTypes);
        final Map<String, List<ServiceTypeKeyword>> serviceCodeToKeywordMap =
            getServiceCodeToKeywordMap(serviceCodes, tenantId);
        serviceTypes.forEach(serviceType -> {
            final List<ServiceTypeKeyword> matchingEntityKeywords = serviceCodeToKeywordMap
                .getOrDefault(serviceType.getCode(), Collections.emptyList());
            serviceType.setKeywords(getKeywords(matchingEntityKeywords));
        });
    }

    private List<String> getKeywords(List<ServiceTypeKeyword> keywordEntities) {
        return keywordEntities.stream()
            .map(ServiceTypeKeyword::getKeyword)
            .collect(Collectors.toList());
    }

    private List<String> getServiceCodes(List<ServiceType> serviceTypes) {
        return serviceTypes.stream()
            .map(ServiceType::getCode)
            .collect(Collectors.toList());
    }

    private Map<String, List<ServiceTypeKeyword>> getServiceCodeToKeywordMap(List<String> serviceCodes,
                                                                             String tenantId) {
        final List<ServiceTypeKeyword> keywords = serviceTypeKeywordJpaRepository
            .findByServiceCodeInAndTenantId(serviceCodes, tenantId);
        return keywords.stream().collect(Collectors.groupingBy(ServiceTypeKeyword::getServiceCode));
    }

    private List<String> getFrequentlyUsedServiceCodes(Integer count, String tenantId) {
        DateTime previousDate = new DateTime();
        final DateTime currentDate = new DateTime();
        previousDate = previousDate.minusMonths(1);

        final Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Submission.class, "submission");
        criteria.setProjection(Projections.projectionList()
            .add(Projections.property("submission.serviceCode"))
            .add(Projections.count("submission.serviceCode").as("count"))
            .add(Projections.groupProperty("submission.serviceCode")));
        criteria.add(Restrictions.between("submission.createdDate", previousDate.toDate(), currentDate.toDate()));
        criteria.add(Restrictions.eq("submission.id.tenantId", tenantId));
        criteria.setMaxResults(count)
            .addOrder(Order.desc("count"));
        final List resultList = criteria.list();
        final List<String> serviceCodes = new ArrayList<>();

        for (final Object row : resultList) {
            final Object[] columns = (Object[]) row;
            serviceCodes.add((String) columns[0]);
        }
        return serviceCodes;
    }
}
