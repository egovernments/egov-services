/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *      accountability and the service delivery of the government  organizations.
 *  
 *       Copyright (C) <2015>  eGovernments Foundation
 *  
 *       The updated version of eGov suite of products as by eGovernments Foundation
 *       is available at http://www.egovernments.org
 *  
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       any later version.
 *  
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *  
 *       You should have received a copy of the GNU General Public License
 *       along with this program. If not, see http://www.gnu.org/licenses/ or
 *       http://www.gnu.org/licenses/gpl.html .
 *  
 *       In addition to the terms of the GPL license to be adhered to in using this
 *       program, the following additional terms are to be complied with:
 *  
 *           1) All versions of this program, verbatim or modified must carry this
 *              Legal Notice.
 *  
 *           2) Any misrepresentation of the origin of the material is prohibited. It
 *              is required that all modified versions of this material be marked in
 *              reasonable ways as different from the original version.
 *  
 *           3) This license does not grant any rights to any user of the program
 *              with regards to rights under trademark law for use of the trade names
 *              or trademarks of eGovernments Foundation.
 *  
 *     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.egf.budget.domain.service;

import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.exception.ErrorCode;
import org.egov.common.domain.exception.InvalidDataException;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.budget.domain.model.Budget;
import org.egov.egf.budget.domain.model.BudgetDetail;
import org.egov.egf.budget.domain.model.BudgetDetailSearch;
import org.egov.egf.budget.domain.repository.BudgetDetailRepository;
import org.egov.egf.budget.domain.repository.BudgetRepository;
import org.egov.egf.budget.web.contract.Boundary;
import org.egov.egf.budget.web.contract.DepartmentRes;
import org.egov.egf.budget.web.repository.BoundaryRepository;
import org.egov.egf.budget.web.repository.DepartmentRepository;
import org.egov.egf.master.web.contract.BudgetGroupContract;
import org.egov.egf.master.web.contract.BudgetGroupSearchContract;
import org.egov.egf.master.web.contract.FunctionContract;
import org.egov.egf.master.web.contract.FunctionSearchContract;
import org.egov.egf.master.web.contract.FunctionaryContract;
import org.egov.egf.master.web.contract.FunctionarySearchContract;
import org.egov.egf.master.web.contract.FundContract;
import org.egov.egf.master.web.contract.FundSearchContract;
import org.egov.egf.master.web.contract.SchemeContract;
import org.egov.egf.master.web.contract.SchemeSearchContract;
import org.egov.egf.master.web.contract.SubSchemeContract;
import org.egov.egf.master.web.contract.SubSchemeSearchContract;
import org.egov.egf.master.web.repository.BudgetGroupContractRepository;
import org.egov.egf.master.web.repository.FunctionContractRepository;
import org.egov.egf.master.web.repository.FunctionaryContractRepository;
import org.egov.egf.master.web.repository.FundContractRepository;
import org.egov.egf.master.web.repository.SchemeContractRepository;
import org.egov.egf.master.web.repository.SubSchemeContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

@Service
@Transactional(readOnly = true)
public class BudgetDetailService {

    public static final String ACTION_CREATE = "create";
    public static final String ACTION_UPDATE = "update";
    public static final String ACTION_DELETE = "delete";
    public static final String ACTION_VIEW = "view";
    public static final String ACTION_EDIT = "edit";
    public static final String ACTION_SEARCH = "search";

    @Autowired
    private BudgetDetailRepository budgetDetailRepository;

    @Autowired
    private SmartValidator validator;

    @Autowired
    private SchemeContractRepository schemeContractRepository;

    @Autowired
    private FunctionContractRepository functionContractRepository;

    @Autowired
    private FunctionaryContractRepository functionaryContractRepository;

    @Autowired
    private BudgetGroupContractRepository budgetGroupContractRepository;

    @Autowired
    private FundContractRepository fundContractRepository;

    @Autowired
    private SubSchemeContractRepository subSchemeContractRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private BoundaryRepository boundaryRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Transactional
    public List<BudgetDetail> create(List<BudgetDetail> budgetDetails, final BindingResult errors,
            final RequestInfo requestInfo) {

        try {

            budgetDetails = fetchRelated(budgetDetails,requestInfo);

            validate(budgetDetails, ACTION_CREATE, errors);

            if (errors.hasErrors())
                throw new CustomBindException(errors);

        } catch (final CustomBindException e) {

            throw new CustomBindException(errors);
        }

        return budgetDetailRepository.save(budgetDetails, requestInfo);

    }

    @Transactional
    public List<BudgetDetail> update(List<BudgetDetail> budgetDetails, final BindingResult errors,
            final RequestInfo requestInfo) {

        try {

            budgetDetails = fetchRelated(budgetDetails,requestInfo);

            validate(budgetDetails, ACTION_UPDATE, errors);

            if (errors.hasErrors())
                throw new CustomBindException(errors);

        } catch (final CustomBindException e) {

            throw new CustomBindException(errors);
        }

        return budgetDetailRepository.update(budgetDetails, requestInfo);

    }
    
    @Transactional
    public List<BudgetDetail> delete(List<BudgetDetail> budgetDetails, final BindingResult errors,
            final RequestInfo requestInfo) {

        try {

            validate(budgetDetails, ACTION_DELETE, errors);

            if (errors.hasErrors())
                throw new CustomBindException(errors);

        } catch (final CustomBindException e) {

            throw new CustomBindException(errors);
        }

        return budgetDetailRepository.delete(budgetDetails, requestInfo);

    }

    private BindingResult validate(final List<BudgetDetail> budgetdetails, final String method, final BindingResult errors) {

        try {
            switch (method) {
            case ACTION_VIEW:
                // validator.validate(budgetDetailContractRequest.getBudgetDetail(),
                // errors);
                break;
            case ACTION_CREATE:
            	if (budgetdetails == null) {
                    throw new InvalidDataException("budgetdetails", ErrorCode.NOT_NULL.getCode(), null);
                }
            	for (final BudgetDetail budgetDetail : budgetdetails) {
                    validator.validate(budgetDetail, errors);
                    if (!budgetDetailRepository.uniqueCheck("name", budgetDetail)) {
                        errors.addError(new FieldError("budgetDetail", "name", budgetDetail.getBudget(), false,
                                new String[] { ErrorCode.NON_UNIQUE_VALUE.getCode() }, null, null));
                    }
            	}
                break;
            case ACTION_UPDATE:
            	if (budgetdetails == null) {
                    throw new InvalidDataException("budgetdetails", ErrorCode.NOT_NULL.getCode(), null);
                }
            	for (final BudgetDetail budgetDetail : budgetdetails){
            		if (budgetDetail.getId() == null) {
                        throw new InvalidDataException("id", ErrorCode.MANDATORY_VALUE_MISSING.getCode(), budgetDetail.getId());
                    }
            		validator.validate(budgetDetail, errors);
            		if (!budgetDetailRepository.uniqueCheck("name", budgetDetail)) {
                        errors.addError(new FieldError("budgetDetail", "name", budgetDetail.getBudget(), false,
                                new String[] { ErrorCode.NON_UNIQUE_VALUE.getCode() }, null, null));
                    }
                }
                break;
            case ACTION_DELETE:
            	if (budgetdetails == null) {
                    throw new InvalidDataException("budgetdetails", ErrorCode.NOT_NULL.getCode(), null);
                }
            	for (final BudgetDetail budgetDetail : budgetdetails){
            		if (budgetDetail.getId() == null) {
                        throw new InvalidDataException("id", ErrorCode.MANDATORY_VALUE_MISSING.getCode(), budgetDetail.getId());
                    }
            	}
                break;
            default:

            }
        } catch (final IllegalArgumentException e) {
            errors.addError(new ObjectError("Missing data", e.getMessage()));
        }
        return errors;

    }

    public List<BudgetDetail> fetchRelated(final List<BudgetDetail> budgetdetails,RequestInfo requestInfo) {
        if (budgetdetails != null)
            for (final BudgetDetail budgetDetail : budgetdetails) {

                // fetch related items
                if (budgetDetail.getBudget() != null && budgetDetail.getBudget().getId() != null
                        && budgetDetail.getBudget().getTenantId() != null) {
                    final Budget budget = budgetRepository.findById(budgetDetail.getBudget());
                    if (budget == null)
                        throw new InvalidDataException("budget", "budget.invalid", " Invalid budget");
                    budgetDetail.setBudget(budget);
                }

                if (budgetDetail.getFund() != null && budgetDetail.getFund().getId() != null
                        && budgetDetail.getFund().getTenantId() != null) {
                    final FundSearchContract contract = new FundSearchContract();
                    contract.setId(budgetDetail.getFund().getId());
                    contract.setTenantId(budgetDetail.getFund().getTenantId());
                    final FundContract fund = fundContractRepository.findById(contract,requestInfo);
                    if (fund == null)
                        throw new InvalidDataException("fund", "fund.invalid", " Invalid fund");
                    budgetDetail.setFund(fund);
                }

                if (budgetDetail.getBudgetGroup() != null && budgetDetail.getBudgetGroup().getId() != null
                        && budgetDetail.getBudgetGroup().getTenantId() != null) {
                    final BudgetGroupSearchContract contract = new BudgetGroupSearchContract();
                    contract.setId(budgetDetail.getBudgetGroup().getId());
                    contract.setTenantId(budgetDetail.getBudgetGroup().getTenantId());
                    final BudgetGroupContract budgetGroup = budgetGroupContractRepository.findById(contract,requestInfo);
                    if (budgetGroup == null)
                        throw new InvalidDataException("budgetGroup", "budgetGroup.invalid", " Invalid budgetGroup");
                    budgetDetail.setBudgetGroup(budgetGroup);
                }

                if (budgetDetail.getUsingDepartment() != null && budgetDetail.getUsingDepartment().getId() != null
                        && budgetDetail.getUsingDepartment().getTenantId() != null) {
                    final DepartmentRes usingDepartment = departmentRepository.getDepartmentById(
                            budgetDetail.getUsingDepartment().getId().toString(), budgetDetail.getTenantId());
                    if (usingDepartment == null || usingDepartment.getDepartment() == null
                            || usingDepartment.getDepartment().isEmpty())
                        throw new InvalidDataException("usingDepartment", "usingDepartment.invalid",
                                " Invalid usingDepartment");
                    budgetDetail.setUsingDepartment(usingDepartment.getDepartment().get(0));
                }

                if (budgetDetail.getExecutingDepartment() != null
                        && budgetDetail.getExecutingDepartment().getId() != null
                        && budgetDetail.getExecutingDepartment().getTenantId() != null) {
                    final DepartmentRes executingDepartment = departmentRepository.getDepartmentById(
                            budgetDetail.getExecutingDepartment().getId().toString(), budgetDetail.getTenantId());
                    if (executingDepartment == null)
                        throw new InvalidDataException("executingDepartment", "executingDepartment.invalid",
                                " Invalid executingDepartment");
                    budgetDetail.setExecutingDepartment(executingDepartment.getDepartment().get(0));
                }

                if (budgetDetail.getFunction() != null && budgetDetail.getFunction().getId() != null
                        && budgetDetail.getFunction().getTenantId() != null) {
                    final FunctionSearchContract contract = new FunctionSearchContract();
                    contract.setId(budgetDetail.getFunction().getId());
                    contract.setTenantId(budgetDetail.getFunction().getTenantId());
                    final FunctionContract function = functionContractRepository.findById(contract,requestInfo);
                    if (function == null)
                        throw new InvalidDataException("function", "function.invalid", " Invalid function");
                    budgetDetail.setFunction(function);
                }

                if (budgetDetail.getScheme() != null && budgetDetail.getScheme().getId() != null
                        && budgetDetail.getScheme().getTenantId() != null) {
                    final SchemeSearchContract contract = new SchemeSearchContract();
                    contract.setId(budgetDetail.getScheme().getId());
                    contract.setTenantId(budgetDetail.getScheme().getTenantId());
                    final SchemeContract scheme = schemeContractRepository.findById(contract,requestInfo);
                    if (scheme == null)
                        throw new InvalidDataException("scheme", "scheme.invalid", " Invalid scheme");
                    budgetDetail.setScheme(scheme);
                }

                if (budgetDetail.getSubScheme() != null && budgetDetail.getSubScheme().getId() != null
                        && budgetDetail.getSubScheme().getTenantId() != null) {
                    final SubSchemeSearchContract contract = new SubSchemeSearchContract();
                    contract.setId(budgetDetail.getSubScheme().getId());
                    contract.setTenantId(budgetDetail.getSubScheme().getTenantId());
                    final SubSchemeContract subScheme = subSchemeContractRepository.findById(contract,requestInfo);
                    if (subScheme == null)
                        throw new InvalidDataException("subScheme", "subScheme.invalid", " Invalid subScheme");
                    budgetDetail.setSubScheme(subScheme);
                }

                if (budgetDetail.getFunctionary() != null && budgetDetail.getFunctionary().getId() != null
                        && budgetDetail.getFunctionary().getTenantId() != null) {
                    final FunctionarySearchContract contract = new FunctionarySearchContract();
                    contract.setId(budgetDetail.getFunctionary().getId());
                    contract.setTenantId(budgetDetail.getFunctionary().getTenantId());
                    final FunctionaryContract functionary = functionaryContractRepository.findById(contract,requestInfo);
                    if (functionary == null)
                        throw new InvalidDataException("functionary", "functionary.invalid", " Invalid functionary");
                    budgetDetail.setFunctionary(functionary);
                }

                if (budgetDetail.getBoundary() != null && budgetDetail.getBoundary().getId() != null
                        && budgetDetail.getBoundary().getTenantId() != null) {
                    final Boundary boundary = boundaryRepository.getBoundaryById(budgetDetail.getBoundary().getId(),
                            budgetDetail.getBoundary().getTenantId());
                    if (boundary == null)
                        throw new InvalidDataException("boundary", "boundary.invalid", " Invalid boundary");
                    budgetDetail.setBoundary(boundary);
                }

            }

        return budgetdetails;

    }

    public Pagination<BudgetDetail> search(final BudgetDetailSearch budgetDetailSearch) {
        return budgetDetailRepository.search(budgetDetailSearch);
    }

    @Transactional
    public BudgetDetail save(final BudgetDetail budgetDetail) {
        return budgetDetailRepository.save(budgetDetail);
    }

    @Transactional
    public BudgetDetail update(final BudgetDetail budgetDetail) {
        return budgetDetailRepository.update(budgetDetail);
    }
    
    @Transactional
    public BudgetDetail delete(final BudgetDetail budgetDetail) {
        return budgetDetailRepository.delete(budgetDetail);
    }

}
