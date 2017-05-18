package org.egov.pgrrest.read.persistence.repository;

import org.egov.pgrrest.common.entity.ServiceType;
import org.egov.pgrrest.common.entity.Submission;
import org.egov.pgrrest.common.repository.ServiceRequestTypeJpaRepository;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceRequestTypeRepository {

    private ServiceRequestTypeJpaRepository serviceRequestTypeJpaRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public ServiceRequestTypeRepository(ServiceRequestTypeJpaRepository serviceRequestTypeJpaRepository) {
        this.serviceRequestTypeJpaRepository = serviceRequestTypeJpaRepository;
    }

    public List<ServiceType> findActiveServiceRequestTypesByCategoryAndTenantId(Long categoryId, String tenantId) {
        return serviceRequestTypeJpaRepository.findActiveServiceTypes(categoryId, tenantId);
    }

    public List<ServiceType> getFrequentlyFiledServiceRequests(Integer count, String tenantId) {
        final List<String> serviceCodes = getFrequentlyUsedServiceCodes(count, tenantId);
        return serviceRequestTypeJpaRepository.findActiveServiceTypes(serviceCodes, tenantId);
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

    public ServiceType getServiceRequestType(String complaintTypeCode, String tenantId) {
        return serviceRequestTypeJpaRepository.findByCodeAndTenantId(complaintTypeCode, tenantId);
    }

    public List<ServiceType> getAllServiceTypes() {
        return serviceRequestTypeJpaRepository.findAll();
    }
}
