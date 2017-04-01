package org.egov.pgr.read.persistence.repository;

import org.egov.pgr.common.entity.ComplaintType;
import org.egov.pgr.common.repository.ComplaintTypeJpaRepository;
import org.egov.pgr.common.entity.Complaint;
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
public class ComplaintTypeRepository {

    private ComplaintTypeJpaRepository complaintTypeJpaRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public ComplaintTypeRepository(ComplaintTypeJpaRepository complaintTypeJpaRepository) {
        this.complaintTypeJpaRepository = complaintTypeJpaRepository;
    }

    public List<ComplaintType> findActiveComplaintTypesByCategory(Long categoryId) {
        return complaintTypeJpaRepository.findActiveComplaintTypes(categoryId);
    }

    public List<ComplaintType> getFrequentlyFiledComplaints(Integer count) {

        DateTime previousDate = new DateTime();
        final DateTime currentDate = new DateTime();
        previousDate = previousDate.minusMonths(1);

        final Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Complaint.class, "complaint");
        criteria.createAlias("complaint.complaintType", "compType");
        criteria.setProjection(Projections.projectionList().add(Projections.property("complaint.complaintType"))
                .add(Projections.count("complaint.complaintType").as("count"))
                .add(Projections.groupProperty("complaint.complaintType")));
        criteria.add(Restrictions.between("complaint.createdDate", previousDate.toDate(), currentDate.toDate()));
        criteria.add(Restrictions.eq("compType.active", Boolean.TRUE));
        criteria.setMaxResults(count).addOrder(Order.desc("count"));
        final List<Object> resultList = criteria.list();
        final List<ComplaintType> complaintTypeList = new ArrayList<ComplaintType>();

        for (final Object row : resultList) {
            final Object[] columns = (Object[]) row;
            complaintTypeList.add((ComplaintType) columns[0]);
        }
        return complaintTypeList;

    }

    public ComplaintType getComplaintType(String complaintTypeCode) {
        return complaintTypeJpaRepository.findByCode(complaintTypeCode);
    }
    
    public List<ComplaintType> getAllComplaintTypes(){
    	return complaintTypeJpaRepository.findAll();
    }
}
