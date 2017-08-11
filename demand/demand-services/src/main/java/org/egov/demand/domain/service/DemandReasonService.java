package org.egov.demand.domain.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.egov.demand.persistence.entity.EgDemandReason;
import org.egov.demand.persistence.repository.DemandReasonRepository;
import org.egov.demand.web.contract.DemandReasonCriteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DemandReasonService {
	@Autowired
	private DemandReasonRepository demandReasonRepository;
	@PersistenceContext
	private EntityManager entityManager;

	private static final Logger LOGGER = Logger.getLogger(DemandReasonService.class);

	public EgDemandReason findByCodeInstModule(String demandReasonCode, String instDescription, String moduleName,
			String tenantId) {

		LOGGER.info("demand reason search params : " + demandReasonCode + " ," + instDescription + " ," + moduleName
				+ " ," + tenantId);
		return demandReasonRepository.findByCodeInstModule(demandReasonCode, instDescription, moduleName, tenantId);
	}

	public List<EgDemandReason> search(DemandReasonCriteria demandReasonCriteria) {

		LOGGER.info("the request criteria for reason search : " + demandReasonCriteria);
		StringBuilder queryStr = new StringBuilder();
		queryStr.append("from EgDemandReason dr where dr.tenantId=:tenantId");
		if (demandReasonCriteria.getModuleName() != null) {
			queryStr.append(" and dr.egInstallmentMaster.module=:moduleName");
		}
		if (demandReasonCriteria.getTaxCategory() != null) {
			queryStr.append(" and dr.egDemandReasonMaster.egReasonCategory.code=:reasonCategory");
		}
		if (demandReasonCriteria.getTaxReason() != null) {
			queryStr.append(" and dr.egDemandReasonMaster.code=:taxReason");
		}
		if (demandReasonCriteria.getInstallmentType() != null) {
			queryStr.append(" and dr.egInstallmentMaster.installmentType=:installmenttype");
		}
		if (demandReasonCriteria.getFromDate() != null) {
			queryStr.append(" and dr.egInstallmentMaster.toDate>=:fromDate");
		}
		if (demandReasonCriteria.getToDate() != null) {
			queryStr.append(" and dr.egInstallmentMaster.toDate<=:toDate");
		}
		queryStr.append(" order by dr.egInstallmentMaster.toDate");
		
		final Query query = entityManager.unwrap(Session.class).createQuery(queryStr.toString());
		query.setString("tenantId", demandReasonCriteria.getTenantId());
		if (demandReasonCriteria.getModuleName() != null) {
			query.setString("moduleName", demandReasonCriteria.getModuleName());
		}
		if (demandReasonCriteria.getTaxCategory() != null) {
			query.setString("reasonCategory", demandReasonCriteria.getTaxCategory());
		}
		if (demandReasonCriteria.getTaxReason() != null) {
			query.setString("taxReason", demandReasonCriteria.getTaxReason());
		}
		if (demandReasonCriteria.getInstallmentType() != null) {
			query.setString("installmenttype", demandReasonCriteria.getInstallmentType());
		}
		if (demandReasonCriteria.getFromDate() != null) {
				query.setTimestamp("fromDate", demandReasonCriteria.getFromDate());
		}
		if (demandReasonCriteria.getToDate() != null) {
			Date date = getEndOfDay(demandReasonCriteria.getToDate());
					LOGGER.info("demandreason toDate-------" +date);
					query.setTimestamp("toDate", date);
		}
		return (List<EgDemandReason>) query.list();
	}
	
	public static Date getEndOfDay(Date date) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    calendar.set(Calendar.HOUR_OF_DAY, 23);
	    calendar.set(Calendar.MINUTE, 59);
	    calendar.set(Calendar.SECOND, 59);
	    calendar.set(Calendar.MILLISECOND, 999);
	    return calendar.getTime();
	}
}
