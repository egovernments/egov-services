/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.pgr.domain.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.pgr.domain.model.Role;
import org.egov.pgr.persistence.entity.ComplaintStatus;
import org.egov.pgr.persistence.entity.ComplaintStatusMapping;
import org.egov.pgr.persistence.repository.EmployeeRepository;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ComplaintStatusMappingService {

    @Autowired
    private final EmployeeRepository employeeRepository;
    
    @PersistenceContext
    private EntityManager entityManager;

    public ComplaintStatusMappingService(final EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, noRollbackFor = SQLGrammarException.class)
    public List<ComplaintStatus> getStatusByRoleAndCurrentStatus(final Long userId, final String status, final String tenantId) {
        final Set<Role> userRoles = employeeRepository.getRolesByUserId(userId, tenantId);
        final List<Long> roles = userRoles.stream().map(role -> role.getId()).collect(Collectors.toList());
        final Criteria criteria = entityManager.unwrap(Session.class)
                .createCriteria(ComplaintStatusMapping.class, "complaintMapping")
                .createAlias("complaintMapping.currentStatus", "complaintStatus");
        criteria.add(Restrictions.eq("complaintStatus.name", status))
                .add(Restrictions.in("complaintMapping.role", roles.toArray()))
                .addOrder(Order.asc("complaintMapping.orderNo"));
        criteria.setProjection(Projections.property("complaintMapping.showStatus"));
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

}
