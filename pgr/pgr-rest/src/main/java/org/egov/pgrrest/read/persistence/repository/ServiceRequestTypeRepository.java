package org.egov.pgrrest.read.persistence.repository;

import org.egov.pgrrest.common.entity.ServiceType;
import org.egov.pgrrest.common.repository.ServiceRequestTypeJpaRepository;
import org.egov.pgrrest.common.entity.Complaint;
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

        DateTime previousDate = new DateTime();
        final DateTime currentDate = new DateTime();
        previousDate = previousDate.minusMonths(1);

        final Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Complaint.class, "complaint");
        criteria.createAlias("complaint.complaintType", "compType");
        criteria.setProjection(Projections.projectionList()
            .add(Projections.property("complaint.complaintType"))
            .add(Projections.count("complaint.complaintType").as("count"))
            .add(Projections.groupProperty("complaint.complaintType")));
        criteria.add(Restrictions.between("complaint.createdDate", previousDate.toDate(), currentDate.toDate()));
        criteria.add(Restrictions.eq("compType.active", Boolean.TRUE));
        criteria.add(Restrictions.eq("complaint.tenantId", tenantId));
        criteria.setMaxResults(count)
            .addOrder(Order.desc("count"));
        final List<Object> resultList = criteria.list();
        final List<ServiceType> complaintTypeList = new ArrayList<ServiceType>();

        for (final Object row : resultList) {
            final Object[] columns = (Object[]) row;
            complaintTypeList.add((ServiceType) columns[0]);
        }
        return complaintTypeList;
    }

    public ServiceType getServiceRequestType(String complaintTypeCode, String tenantId) {
        return serviceRequestTypeJpaRepository.findByCodeAndTenantId(complaintTypeCode, tenantId);
    }

    public List<ServiceType> getAllServiceTypes() {
        return serviceRequestTypeJpaRepository.findAll();
    }
}
