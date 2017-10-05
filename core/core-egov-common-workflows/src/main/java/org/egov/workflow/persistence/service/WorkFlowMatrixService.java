package org.egov.workflow.persistence.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.workflow.persistence.entity.WorkFlowMatrix;
import org.egov.workflow.persistence.repository.WorkflowMatrixJdbcRepository;
import org.egov.workflow.web.contract.Designation;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class WorkFlowMatrixService {

	private final static Logger Log = LoggerFactory.getLogger(WorkFlowMatrixService.class);

	private final WorkflowMatrixJdbcRepository workFlowMatrixjdbcRepository;
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	public WorkFlowMatrixService(final WorkflowMatrixJdbcRepository workFlowMatrixjdbcRepository) {
		this.workFlowMatrixjdbcRepository = workFlowMatrixjdbcRepository;
	}

	public Session getSession() {
		return entityManager.unwrap(Session.class);
	}

	@Transactional
	public WorkFlowMatrix create(final WorkFlowMatrix workFlowMatrix) {
		return workFlowMatrixjdbcRepository.create(workFlowMatrix);
	}

	@Transactional
	public WorkFlowMatrix update(final WorkFlowMatrix workFlowMatrix) {
		return workFlowMatrixjdbcRepository.update(workFlowMatrix);
	}

	public  List<WorkFlowMatrix> findAll(final WorkFlowMatrix workflowMatrixSearchModel) {
		return workFlowMatrixjdbcRepository.search(workflowMatrixSearchModel);
	}

	public WorkFlowMatrix findOne(final WorkFlowMatrix workFlowMatrix) {
		return workFlowMatrixjdbcRepository.findById(workFlowMatrix);
	}


	public WorkFlowMatrix getWfMatrix(final String type, final String department, final BigDecimal amountRule,
			final String additionalRule, final String currentState, final String pendingActions, String tenantId) {
		final List<WorkFlowMatrix> wfMatrixCriteria = createWfMatrixAdditionalCriteria(type, department, amountRule, additionalRule,
				currentState, pendingActions, tenantId);

		return getWorkflowMatrixObj(type, additionalRule, currentState, pendingActions, wfMatrixCriteria,tenantId);

	}

	public WorkFlowMatrix getWfMatrix(final String type, final String department, final BigDecimal amountRule,
			final String additionalRule, final String currentState, final String pendingActions, final Date date,
			String tenantId) {
		final List<WorkFlowMatrix> wfMatrixCriteria = createWfMatrixAdditionalCriteria(type, department, amountRule, additionalRule,
				currentState, pendingActions, tenantId);
		
		WorkFlowMatrix works =WorkFlowMatrix.builder()
											.fromDate(date)
											.toDate(date)
											.build();
		List<WorkFlowMatrix> defaultObjectTypeList=workFlowMatrixjdbcRepository.search(works);
		List<WorkFlowMatrix> workFlowList = new ArrayList<WorkFlowMatrix>();
		workFlowList .addAll(wfMatrixCriteria);
		workFlowList.addAll(defaultObjectTypeList);
		/*final Criterion crit1 = Restrictions.le("fromDate", date == null ? new Date() : date);
		final Criterion crit2 = Restrictions.ge("toDate", date == null ? new Date() : date);
		final Criterion crit3 = Restrictions.conjunction().add(crit1).add(crit2);
		wfMatrixCriteria.add(Restrictions.or(crit3, crit1));*/

		return getWorkflowMatrixObj(type, additionalRule, currentState, pendingActions, workFlowList,tenantId);

	}

	public WorkFlowMatrix getWfMatrix(final String type, final String department, final BigDecimal amountRule,
			final String additionalRule, final String currentState, final String pendingActions, final Date date,
			final String designation, String tenantId) {
		final List<WorkFlowMatrix> wfMatrixCriteria = createWfMatrixAdditionalCriteria(type, department, amountRule, additionalRule,
				currentState, pendingActions, tenantId);
		
		WorkFlowMatrix works=WorkFlowMatrix.builder()
											.fromDate(date)
											.toDate(date)
											.currentDesignation(designation)
											.build();
		List<WorkFlowMatrix> defaultObjectTypeList=workFlowMatrixjdbcRepository.search(works);
		List<WorkFlowMatrix> workFlowList = new ArrayList<WorkFlowMatrix>();
		workFlowList .addAll(wfMatrixCriteria);
		workFlowList.addAll(defaultObjectTypeList);
		/*final Criterion crit1 = Restrictions.le("fromDate", date == null ? new Date() : date);
		final Criterion crit2 = Restrictions.ge("toDate", date == null ? new Date() : date);
		final Criterion crit3 = Restrictions.conjunction().add(crit1).add(crit2);
		
		if (StringUtils.isNotBlank(designation)) {
			final Criterion criteriaDesignation = Restrictions.ilike("currentDesignation", designation);
			wfMatrixCriteria.add(criteriaDesignation);
		}*/
		
		//wfMatrixCriteria.add(Restrictions.or(crit3, crit1));

		return getWorkflowMatrixObj(type, additionalRule, currentState, pendingActions, designation, workFlowList,tenantId);

	}

	private WorkFlowMatrix getWorkflowMatrixObj(final String type, final String additionalRule,
			final String currentState, final String pendingActions, final List<WorkFlowMatrix> wfMatrixCriteria,String tenantId) {
		final List<WorkFlowMatrix> objectTypeList = wfMatrixCriteria;
		
		
		if (objectTypeList.isEmpty()) {
			final List<WorkFlowMatrix> defaulfWfMatrixCriteria = commonWorkFlowMatrixCriteria(type, additionalRule, currentState,
					pendingActions);
			
			WorkFlowMatrix work=WorkFlowMatrix.builder()
											.department("ANY")
											.tenantId(tenantId)
											.build();
			List<WorkFlowMatrix> defaultObjectTypeList=workFlowMatrixjdbcRepository.search(work);
			/*defaulfWfMatrixCriteria.add(Restrictions.eq("department", "ANY"));
			defaulfWfMatrixCriteria.add(Restrictions.eq("tenantId", tenantId));*/
			//final List<WorkFlowMatrix> defaultObjectTypeList = defaulfWfMatrixCriteria.list();
			if (defaultObjectTypeList.isEmpty())
			{
			   Log.warn("Workflow not configured  ");
			return null;
			}
			else
				return defaultObjectTypeList.get(0);
		} else {
			for (final WorkFlowMatrix matrix : objectTypeList)
				if (matrix.getToDate() == null)
					return matrix;
			return objectTypeList.get(0);
		}
	}

	private WorkFlowMatrix getWorkflowMatrixObj(final String type, final String additionalRule,
			final String currentState, final String pendingActions, final String designation,
			final List<WorkFlowMatrix> wfMatrixCriteria,String tenantId) {
		final List<WorkFlowMatrix> objectTypeList = wfMatrixCriteria;

		
		if (objectTypeList.isEmpty()) {
			final List<WorkFlowMatrix> defaulfWfMatrixCriteria = commonWorkFlowMatrixCriteria(type, additionalRule, currentState,
					pendingActions);
			
			WorkFlowMatrix works=WorkFlowMatrix.builder()
												.department("ANY")
												.tenantId(tenantId)
												.currentDesignation(designation)
												.build();
			
			List<WorkFlowMatrix> defaultObjectTypeList=workFlowMatrixjdbcRepository.search(works);
			

			/*defaulfWfMatrixCriteria.add(Restrictions.eq("department", "ANY"));
			defaulfWfMatrixCriteria.add(Restrictions.eq("tenantId",tenantId));
			if (StringUtils.isNotBlank(designation))
				defaulfWfMatrixCriteria.add(Restrictions.ilike("currentDesignation", designation));
			final List<WorkFlowMatrix> defaultObjectTypeList = defaulfWfMatrixCriteria.list();*/
			if (defaultObjectTypeList.isEmpty())
			{
			Log.error("Workflow not configured  ");
			return null;
			}
			else
				return defaultObjectTypeList.get(0);
		} else {
			for (final WorkFlowMatrix matrix : objectTypeList)
				if (matrix.getToDate() == null)
					return matrix;
			return objectTypeList.get(0);
		}
	}

	private List<WorkFlowMatrix> createWfMatrixAdditionalCriteria(final String type, final String department,
			final BigDecimal amountRule, final String additionalRule, final String currentState,
			final String pendingActions, String tenantId) {
		final List<WorkFlowMatrix> wfMatrixCriteria = commonWorkFlowMatrixCriteria(type, additionalRule, currentState,
				pendingActions);
		WorkFlowMatrix work = WorkFlowMatrix.builder()
											.department(department)
											.tenantId(tenantId)
											.fromQty(amountRule)
											.toQty(amountRule)
											.build();
		final List<WorkFlowMatrix> objectTypeList= workFlowMatrixjdbcRepository.search(work);
		List<WorkFlowMatrix> workFlowList = new ArrayList<WorkFlowMatrix>();
		workFlowList .addAll(objectTypeList);
		workFlowList.addAll(wfMatrixCriteria);
		return workFlowList;

	/*	if (department != null && !"".equals(department.trim()))
			wfMatrixCriteria.add(Restrictions.eq("department", department));

		
		if (tenantId != null) {
			wfMatrixCriteria.add(Restrictions.eq("tenantId", tenantId));
		}else
		{
		    Log.warn("tenantId is not passed. Result may not be correct");
		}

		// Added restriction for amount rule
		if (amountRule != null && BigDecimal.ZERO.compareTo(amountRule) != 0) {
			final Criterion amount1st = Restrictions.conjunction().add(Restrictions.le("fromQty", amountRule))
					.add(Restrictions.ge("toQty", amountRule));

			final Criterion amount2nd = Restrictions.conjunction().add(Restrictions.le("fromQty", amountRule))
					.add(Restrictions.isNull("toQty"));
			wfMatrixCriteria.add(Restrictions.disjunction().add(amount1st).add(amount2nd));

		}
		return wfMatrixCriteria;*/
	}

	private WorkFlowMatrix createWfMatrixAdditionalCriteria(final String type, final String department,
			final BigDecimal amountRule, final String additionalRule, final String currentState,
			final String pendingActions, final String designation, String tenantId) {
		final List<WorkFlowMatrix> wfMatrixCriteria = commonWorkFlowMatrixCriteria(type, additionalRule, currentState,
				pendingActions);
		
		
		WorkFlowMatrix works=WorkFlowMatrix.builder()
											.department(department)
											.tenantId(tenantId)
											.fromQty(amountRule)
											.toQty(amountRule)
											.build();
		final List<WorkFlowMatrix> objectTypeList= workFlowMatrixjdbcRepository.search(works);
		List<WorkFlowMatrix> workFlowList = new ArrayList<WorkFlowMatrix>();
		workFlowList .addAll(objectTypeList);
		workFlowList.addAll(wfMatrixCriteria);
		return workFlowList.get(0);
		/*if (department != null && !"".equals(department.trim()))
			wfMatrixCriteria.add(Restrictions.eq("department", department));

		if (tenantId != null) {
			wfMatrixCriteria.add(Restrictions.eq("tenantId", tenantId));
		}

		// Added restriction for amount rule
		if (amountRule != null && BigDecimal.ZERO.compareTo(amountRule) != 0) {
			final Criterion amount1st = Restrictions.conjunction().add(Restrictions.le("fromQty", amountRule))
					.add(Restrictions.ge("toQty", amountRule));

			final Criterion amount2nd = Restrictions.conjunction().add(Restrictions.le("fromQty", amountRule))
					.add(Restrictions.isNull("toQty"));
			wfMatrixCriteria.add(Restrictions.disjunction().add(amount1st).add(amount2nd));

		}

		if (StringUtils.isNotBlank(designation))
			wfMatrixCriteria.add(Restrictions.ilike("currentDesignation", designation));

		return wfMatrixCriteria;*/
	}

	public WorkFlowMatrix getPreviousStateFromWfMatrix(final String type, final String department,
			final BigDecimal amountRule, final String additionalRule, final String currentState,
			final String pendingActions) {

		final List<WorkFlowMatrix> wfMatrixCriteria = previousWorkFlowMatrixCriteria(type, additionalRule, currentState,
				pendingActions);
		
		WorkFlowMatrix workFlowMatrix=WorkFlowMatrix.builder()
									.department(department)
									.fromQty(amountRule)
									.toQty(amountRule)
									.build();

		List<WorkFlowMatrix> query= workFlowMatrixjdbcRepository.search(workFlowMatrix);
		List<WorkFlowMatrix> workFlowList = new ArrayList<WorkFlowMatrix>();
		workFlowList .addAll(wfMatrixCriteria);
		workFlowList.addAll(query);		
		/*if (department != null && !"".equals(department))
			wfMatrixCriteria.add(Restrictions.eq("department", department));
		else
			wfMatrixCriteria.add(Restrictions.eq("department", "ANY"));

		// Added restriction for amount rule
		if (amountRule != null && BigDecimal.ZERO.compareTo(amountRule) != 0) {
			final Criterion amount1st = Restrictions.conjunction().add(Restrictions.le("fromQty", amountRule))
					.add(Restrictions.ge("toQty", amountRule));
			final Criterion amount2nd = Restrictions.conjunction().add(Restrictions.le("fromQty", amountRule))
					.add(Restrictions.isNull("toQty"));
			wfMatrixCriteria.add(Restrictions.disjunction().add(amount1st).add(amount2nd));

		}*/

		final List<WorkFlowMatrix> objectTypeList = workFlowList;

		if (!objectTypeList.isEmpty())
			return objectTypeList.get(0);

		return null;

	}

	private List<WorkFlowMatrix> previousWorkFlowMatrixCriteria(final String type, final String additionalRule,
			final String currentState, final String pendingActions) {
		
		WorkFlowMatrix works=WorkFlowMatrix.builder()
											.objectType(type)
											.additionalRule(additionalRule)
											.currentState(currentState)
											.pendingActions(pendingActions)
											.build();
		return workFlowMatrixjdbcRepository.search(works);
		

		/*final Criteria commonWfMatrixCriteria = getSession().createCriteria(WorkFlowMatrix.class);
		commonWfMatrixCriteria.add(Restrictions.eq("objectType", type));

		if (StringUtils.isNotBlank(additionalRule))
			commonWfMatrixCriteria.add(Restrictions.eq("additionalRule", additionalRule));

		if (StringUtils.isNotBlank(pendingActions))
			commonWfMatrixCriteria.add(Restrictions.ilike("nextAction", pendingActions, MatchMode.EXACT));

		if (StringUtils.isNotBlank(currentState))
			commonWfMatrixCriteria.add(Restrictions.ilike("nextState", currentState, MatchMode.EXACT));
		return commonWfMatrixCriteria;*/
	}

	private List<WorkFlowMatrix> commonWorkFlowMatrixCriteria(final String type, final String additionalRule,
			final String currentState, final String pendingActions) {
		
		WorkFlowMatrix work=WorkFlowMatrix.builder()
								.objectType(type)
								.additionalRule(additionalRule)
								.currentState(currentState)
								.pendingActions(pendingActions)
								.build();
		return workFlowMatrixjdbcRepository.search(work);

		/*final Criteria commonWfMatrixCriteria = this.getSession().createCriteria(WorkFlowMatrix.class);

		commonWfMatrixCriteria.add(Restrictions.eq("objectType", type));

		if (additionalRule != null && !"".equals(additionalRule.trim()))
			commonWfMatrixCriteria.add(Restrictions.eq("additionalRule", additionalRule));

		if (pendingActions != null && !"".equals(pendingActions.trim()))
			commonWfMatrixCriteria.add(Restrictions.ilike("pendingActions", pendingActions, MatchMode.ANYWHERE));

		if (currentState != null && !"".equals(currentState.trim()))
			commonWfMatrixCriteria.add(Restrictions.ilike("currentState", currentState, MatchMode.EXACT));
		else
			commonWfMatrixCriteria.add(Restrictions.ilike("currentState", "NEW", MatchMode.ANYWHERE));

		return commonWfMatrixCriteria;*/
	}

	public List<String> getNextValidActions(final String type, final String departmentName,
			final BigDecimal businessRule, final String additionalRule, final String currentState,
			final String pendingAction, final Date date, String tenantId) {

		final WorkFlowMatrix wfMatrix = getWfMatrix(type, departmentName, businessRule, additionalRule, currentState,
				pendingAction, date, tenantId);
		List<String> validActions = Collections.EMPTY_LIST;

		if (wfMatrix != null && wfMatrix.getValidActions() != null)
			validActions = Arrays.asList(wfMatrix.getValidActions().split(","));
		return validActions;
	}

	public List<String> getNextValidActions(final String type, final String departmentName,
			final BigDecimal businessRule, final String additionalRule, final String currentState,
			final String pendingAction, final Date date, final String currentDesignation, String tenantId) {

		final WorkFlowMatrix wfMatrix = getWfMatrix(type, departmentName, businessRule, additionalRule, currentState,
				pendingAction, date, currentDesignation, tenantId);
		List<String> validActions = Collections.EMPTY_LIST;

		if (wfMatrix != null && wfMatrix.getValidActions() != null)
			validActions = Arrays.asList(wfMatrix.getValidActions().split(","));
		return validActions;
	}

	/**
	 * 
	 * @param type
	 * @param department
	 * @param businessRule
	 * @param additionalRule
	 * @param currentState
	 * @param pendingAction
	 * @param date
	 * @param designation
	 * @return This api requires designations are added as id:name,id:name
	 *         format in workflow matrix
	 */
	public List<Designation> getNextDesignations(final String type, final String department,
			final BigDecimal businessRule, final String additionalRule, final String currentState,
			final String pendingAction, final Date date, final String designation, String tenantId) {

		final WorkFlowMatrix wfMatrix = getWfMatrix(type, department, businessRule, additionalRule, currentState,
				pendingAction, date, designation, tenantId);
		List<Designation> designationList = new ArrayList<Designation>();
		if (wfMatrix != null && wfMatrix.getNextDesignation() != null) {
			final List<String> tempDesignationName = Arrays.asList(wfMatrix.getNextDesignation().split(","));
			for (final String desgName : tempDesignationName)
				if (desgName != null && !"".equals(desgName.trim())) {
					if (!desgName.contains(":")) {
						Log.warn("designations not stored properly in matrix. Please check the api documentation ");
						Designation d = new Designation();
						d.setName(desgName);
						designationList.add(d);
					} else {
						Designation d = new Designation();
						d.setId(Long.valueOf(desgName.split(":")[0]));
						d.setName(desgName.split(":")[1]);
						designationList.add(d);
					}

				}

		}

		return designationList;
	}

}