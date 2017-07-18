package org.egov.workflow.persistence.repository;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.egov.workflow.config.ApplicationProperties;
import org.egov.workflow.domain.model.EscalationHoursSearchCriteria;
import org.egov.workflow.domain.model.EscalationTimeType;
import org.egov.workflow.domain.model.ServiceType;
import org.egov.workflow.persistence.QueryBuilder.EscalationTimeTypeQueryBuilder;
import org.egov.workflow.persistence.repository.rowmapper.EscalationTimeTypeRowMapper;
import org.egov.workflow.web.contract.EscalationTimeTypeGetReq;
import org.egov.workflow.web.contract.EscalationTimeTypeReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class EscalationRepository {

	private static final Logger logger = LoggerFactory.getLogger(EscalationRepository.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
    private int defaultEscalationHours;
    private EscalationJpaRepository escalationJpaRepository;
    
    @Autowired
	private EscalationTimeTypeRowMapper escalationRowMapper;
    
    @Autowired
    private EscalationTimeTypeQueryBuilder escalationTimeTypeQueryBuilder;
    
    @Autowired
    private ApplicationProperties applicationProp; 

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
	public List<EscalationTimeType> getAllEscalationTimeTypes(final EscalationTimeTypeGetReq escalationGetRequest) {
		logger.info("EscalationTimeType search Request::" + escalationGetRequest);
		final List<Object> preparedStatementValues = new ArrayList<>();
		final String queryStr = escalationTimeTypeQueryBuilder.getQuery(escalationGetRequest, preparedStatementValues);
		final List<EscalationTimeType> escalationTypes = jdbcTemplate.query(queryStr,
				preparedStatementValues.toArray(), escalationRowMapper);
		if(escalationTypes.size() <= 0){ 
			EscalationTimeType escalationTimeType = new EscalationTimeType(); 
			escalationTimeType.setNoOfHours(Long.parseLong(applicationProp.getDefaultEscalationHours()));
			escalationTimeType.setTenantId(escalationGetRequest.getTenantId());
			escalationTimeType.setDesignation(0L == escalationGetRequest.getDesignation() ? 0 : escalationGetRequest.getDesignation() );
			if (null != escalationGetRequest.getId()) {
				for (int i = 0; i < escalationGetRequest.getId().size(); i++) {
					ServiceType serviceType = new ServiceType();
					serviceType.setId(escalationGetRequest.getId().get(i));
					escalationTimeType.setGrievanceType(serviceType);
				}
			} else if (escalationGetRequest.getServiceId() > 0){ 
				ServiceType serviceType = new ServiceType();
				serviceType.setId(escalationGetRequest.getServiceId());
				escalationTimeType.setGrievanceType(serviceType);
			}
			escalationTypes.add(escalationTimeType);
		}
		return escalationTypes;
	}
	
	public boolean checkRecordExists(EscalationTimeTypeReq escalationTimeTypeReq) {
		List<Object> preparedStatementValues = new ArrayList<>();
		preparedStatementValues.add(escalationTimeTypeReq.getEscalationTimeType().getGrievanceType().getId());
		preparedStatementValues.add(escalationTimeTypeReq.getEscalationTimeType().getDesignation());
		preparedStatementValues.add(escalationTimeTypeReq.getEscalationTimeType().getTenantId());
		String checkQuery = escalationTimeTypeQueryBuilder.checkRecordExistsQuery();
		final List<Integer> count = jdbcTemplate.queryForList(checkQuery,
				preparedStatementValues.toArray(), Integer.class);
		if(count.size()>0){
			logger.info("Size of the count is : " + count);
			if(count.get(0) > 0){
				return true;
			}
		}
		return false; 
	}
    
}
