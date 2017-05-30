package org.egov.collection.persistence.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.egov.collection.domain.model.ServiceCategorySearchCriteria;
import org.egov.collection.persistence.entity.ServiceCategory;
import org.egov.collection.persistence.entity.ServiceCategory_;
import org.springframework.data.jpa.domain.Specification;


import lombok.EqualsAndHashCode;


@EqualsAndHashCode
public class ServiceCategorySpecification implements Specification<ServiceCategory> {

	private ServiceCategorySearchCriteria serviceCategorySearchCriteria;

	public ServiceCategorySpecification(ServiceCategorySearchCriteria serviceCategorySearchCriteria) {
		this.serviceCategorySearchCriteria = serviceCategorySearchCriteria;
	}

	@Override
	public Predicate toPredicate(Root<ServiceCategory> root, CriteriaQuery<?> criteriaQuery,
			CriteriaBuilder criteriaBuilder) {
		Path<String> name = root.get(ServiceCategory_.name);
		Path<String> tenantId = root.get(ServiceCategory_.tenantId);
		Path<Boolean> active = root.get(ServiceCategory_.isactive);
		Path<Long> id = root.get(ServiceCategory_.id);

		List<Predicate> predicates = new ArrayList<>();

		if (serviceCategorySearchCriteria.getBusinessCategoryName() != null) {
			predicates.add(criteriaBuilder.equal(name, serviceCategorySearchCriteria.getBusinessCategoryName()));
		}

		if (serviceCategorySearchCriteria.getIsactive() != null) {
			predicates.add(criteriaBuilder.equal(active, serviceCategorySearchCriteria.getIsactive()));
		}

		if (serviceCategorySearchCriteria.getTenantId() != null) {
			predicates.add(criteriaBuilder.equal(tenantId, serviceCategorySearchCriteria.getTenantId()));
		}

		if (serviceCategorySearchCriteria.getIds() != null) {
			predicates.add(id.in(serviceCategorySearchCriteria.getIds()));
		}
		return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
	}
}
