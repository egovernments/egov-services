package org.egov.workflow.persistence.repository;

import org.egov.workflow.domain.model.EscalationHoursSearchCriteria;
import org.egov.workflow.domain.model.EscalationTimeType;
import org.egov.workflow.persistence.QueryBuilder.EscalationTimeTypeQueryBuilder;
import org.egov.workflow.web.contract.EscalationTimeTypeReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Optional;

@Service
public class EscalationRepository {

	private static final Logger logger = LoggerFactory.getLogger(EscalationRepository.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
    private int defaultEscalationHours;
    private EscalationJpaRepository escalationJpaRepository;
    
    @Autowired
    private EscalationTimeTypeQueryBuilder escalationTimeTypeQueryBuilder;

    public EscalationRepository(@Value("${defaults.escalationHours}") int defaultEscalationHours,
                                EscalationJpaRepository escalationJpaRepository) {
        this.defaultEscalationHours = defaultEscalationHours;
        this.escalationJpaRepository = escalationJpaRepository;
    }

    public int getEscalationHours(EscalationHoursSearchCriteria searchCriteria) {
        final Integer configuredEscalationHours = escalationJpaRepository.
            findBy(searchCriteria.getDesignationId(), searchCriteria.getComplaintTypeId(),
                searchCriteria.getTenantId());
        return Optional.ofNullable(configuredEscalationHours)
            .orElse(defaultEscalationHours);
    }
    
	public EscalationTimeTypeReq persistCreateEscalationTimeType(final EscalationTimeTypeReq escalationTimeTypeRequest) {
		logger.info("EscalationTimeTypeRequest::" + escalationTimeTypeRequest);
		final String escalationTimeTypeInsert = escalationTimeTypeQueryBuilder.insertEscalationTimeType();
		final EscalationTimeType ecalationTimeType = escalationTimeTypeRequest.getEscalationTimeType();
		final Object[] obj = new Object[] { ecalationTimeType.getGrievanceType().getId(), ecalationTimeType.getNoOfHours(),
				ecalationTimeType.getDesignation(), ecalationTimeType.getTenantId(),
				Long.valueOf(escalationTimeTypeRequest.getRequestInfo().getUserInfo().getId()),
				Long.valueOf(escalationTimeTypeRequest.getRequestInfo().getUserInfo().getId()),
				new Date(new java.util.Date().getTime()), new Date(new java.util.Date().getTime()) };
		jdbcTemplate.update(escalationTimeTypeInsert, obj);
		return escalationTimeTypeRequest;
	}
	
	public EscalationTimeTypeReq persistUpdateEscalationTimeType(final EscalationTimeTypeReq escalationTimeTypeRequest) {
		logger.info("EscalationTimeTypeRequest::" + escalationTimeTypeRequest);
		final String escalationTimeTypeInsert = escalationTimeTypeQueryBuilder.updateEscalationTimeType();
		final EscalationTimeType ecalationTimeType = escalationTimeTypeRequest.getEscalationTimeType();
		final Object[] obj = new Object[] { ecalationTimeType.getGrievanceType().getId(), ecalationTimeType.getNoOfHours(),
				ecalationTimeType.getDesignation(), ecalationTimeType.getTenantId(),
				Long.valueOf(escalationTimeTypeRequest.getRequestInfo().getUserInfo().getId()),
				Long.valueOf(escalationTimeTypeRequest.getRequestInfo().getUserInfo().getId()),
				new Date(new java.util.Date().getTime()), new Date(new java.util.Date().getTime()), ecalationTimeType.getId()};
		jdbcTemplate.update(escalationTimeTypeInsert, obj);
		return escalationTimeTypeRequest;
	}
    
}
