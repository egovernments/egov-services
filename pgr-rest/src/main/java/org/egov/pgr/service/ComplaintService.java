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

package org.egov.pgr.service;

import static org.egov.pgr.entity.enums.ComplaintStatus.FORWARDED;
import static org.egov.pgr.entity.enums.ComplaintStatus.PROCESSING;
import static org.egov.pgr.entity.enums.ComplaintStatus.REGISTERED;
import static org.egov.pgr.entity.enums.ComplaintStatus.REOPENED;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ValidationException;

import org.egov.pgr.entity.Complaint;
import org.egov.pgr.entity.enums.ComplaintStatus;
import org.egov.pgr.entity.enums.ReceivingMode;
import org.egov.pgr.model.ServiceRequest;
import org.egov.pgr.repository.ComplaintRepository;
import org.egov.pgr.specification.SevaSpecification;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ComplaintService {

    private static final Logger LOG = LoggerFactory.getLogger(ComplaintService.class);
    public final String COMPLAINT_ALL = "ALL";
    public final String COMPLAINT_PENDING = "PENDING";
    public final String COMPLAINT_COMPLETED = "COMPLETED";
    public final String COMPLAINT_REJECTED = "REJECTED";
    public final String COMPLAINTS_FILED = "FILED";
    public final String COMPLAINTS_RESOLVED = "RESOLVED";
    public final String COMPLAINTS_UNRESOLVED = "UNRESOLVED";
    final String[] pendingStatus = {"REGISTERED", "FORWARDED", "PROCESSING", "NOTCOMPLETED", "REOPENED"};
    final String[] completedStatus = {"COMPLETED", "WITHDRAWN", "CLOSED"};
    final String[] rejectedStatus = {"REJECTED"};
    final String[] resolvedStatus = {"COMPLETED", "WITHDRAWN", "CLOSED", "REJECTED"};
    @Autowired
    private ComplaintRepository complaintRepository;
    @Autowired
    private ComplaintStatusService complaintStatusService;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Transactional
    public Complaint createComplaint(Complaint complaint) throws ValidationException {
        complaint.setStatus(complaintStatusService.getByName("REGISTERED"));
        complaint.setAssignee(2L);
        complaint.setEscalationDate(new Date());
        System.out.println(complaint.toString());
        complaintRepository.save(complaint);
        return complaint;
    }

    public Complaint getComplaintById(final Long complaintID) {
        return complaintRepository.findOne(complaintID);
    }

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    public Complaint getComplaintByCRN(final String crn) {
        return complaintRepository.findByCrn(crn);
    }

    public List<Complaint> getComplaintsEligibleForEscalation() {
        final Criteria criteria = getCurrentSession().createCriteria(Complaint.class, "complaint")
                .createAlias("complaint.status", "complaintStatus");

        criteria.add(Restrictions.disjunction().add(Restrictions.eq("complaintStatus.name", REOPENED.name()))
                .add(Restrictions.eq("complaintStatus.name", FORWARDED.name()))
                .add(Restrictions.eq("complaintStatus.name", PROCESSING.name()))
                .add(Restrictions.eq("complaintStatus.name", REGISTERED.name())))
                .add(Restrictions.lt("complaint.escalationDate", new Date()))
                .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

        return criteria.list();
    }

    public List<ReceivingMode> getAllReceivingModes() {
        return Arrays.asList(ReceivingMode.values());
    }

    public List<Complaint> findAll(SevaSpecification sevaSpecification) {
        return complaintRepository.findAll(sevaSpecification);
    }

}