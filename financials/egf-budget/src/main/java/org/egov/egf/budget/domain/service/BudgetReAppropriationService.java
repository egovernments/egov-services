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
import org.egov.egf.budget.domain.model.BudgetDetail;
import org.egov.egf.budget.domain.model.BudgetReAppropriation;
import org.egov.egf.budget.domain.model.BudgetReAppropriationSearch;
import org.egov.egf.budget.domain.repository.BudgetDetailRepository;
import org.egov.egf.budget.domain.repository.BudgetReAppropriationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

@Service
@Transactional(readOnly = true)
public class BudgetReAppropriationService {

    public static final String ACTION_CREATE = "create";
    public static final String ACTION_UPDATE = "update";
    public static final String ACTION_DELETE = "delete";
    public static final String ACTION_VIEW = "view";
    public static final String ACTION_EDIT = "edit";
    public static final String ACTION_SEARCH = "search";

    private final BudgetReAppropriationRepository budgetReAppropriationRepository;

    private final SmartValidator validator;

    private final BudgetDetailRepository budgetDetailRepository;

    @Autowired
    public BudgetReAppropriationService(final BudgetReAppropriationRepository budgetReAppropriationRepository,
            final SmartValidator validator, final BudgetDetailRepository budgetDetailRepository) {

        this.budgetReAppropriationRepository = budgetReAppropriationRepository;
        this.validator = validator;
        this.budgetDetailRepository = budgetDetailRepository;

    }

    @Transactional
    public List<BudgetReAppropriation> create(List<BudgetReAppropriation> budgetReAppropriations, final BindingResult errors,
            final RequestInfo requestInfo) {

        try {

            budgetReAppropriations = fetchRelated(budgetReAppropriations);

            validate(budgetReAppropriations, ACTION_CREATE, errors);

            if (errors.hasErrors())
                throw new CustomBindException(errors);

        } catch (final CustomBindException e) {

            throw new CustomBindException(errors);
        }

        return budgetReAppropriationRepository.save(budgetReAppropriations, requestInfo);

    }

    @Transactional
    public List<BudgetReAppropriation> update(List<BudgetReAppropriation> budgetReAppropriations, final BindingResult errors,
            final RequestInfo requestInfo) {

        try {

            budgetReAppropriations = fetchRelated(budgetReAppropriations);

            validate(budgetReAppropriations, ACTION_UPDATE, errors);

            if (errors.hasErrors())
                throw new CustomBindException(errors);

        } catch (final CustomBindException e) {

            throw new CustomBindException(errors);
        }

        return budgetReAppropriationRepository.update(budgetReAppropriations, requestInfo);

    }
    
    @Transactional
    public List<BudgetReAppropriation> delete(List<BudgetReAppropriation> budgetReAppropriations, final BindingResult errors,
            final RequestInfo requestInfo) {

        try {

            validate(budgetReAppropriations, ACTION_DELETE, errors);

            if (errors.hasErrors())
                throw new CustomBindException(errors);

        } catch (final CustomBindException e) {

            throw new CustomBindException(errors);
        }

        return budgetReAppropriationRepository.delete(budgetReAppropriations, requestInfo);

    }

    private BindingResult validate(final List<BudgetReAppropriation> budgetreappropriations, final String method,
            final BindingResult errors) {

        try {
            switch (method) {
            case ACTION_VIEW:
                // validator.validate(budgetReAppropriationContractRequest.getBudgetReAppropriation(),
                // errors);
                break;
            case ACTION_CREATE:
            	if (budgetreappropriations == null) {
                    throw new InvalidDataException("budgetreappropriations", ErrorCode.NOT_NULL.getCode(), null);
                }
                for (final BudgetReAppropriation budgetReAppropriation : budgetreappropriations) {
                    validator.validate(budgetReAppropriation, errors);
                    if (!budgetReAppropriationRepository.uniqueCheck("name", budgetReAppropriation)) {
                        errors.addError(new FieldError("budgetReAppropriation", "name", budgetReAppropriation.getBudgetDetail(), false,
                                new String[] { ErrorCode.NON_UNIQUE_VALUE.getCode() }, null, null));
                    }
                }
                break;
            case ACTION_UPDATE:
            	if (budgetreappropriations == null) {
                    throw new InvalidDataException("budgetreappropriations", ErrorCode.NOT_NULL.getCode(), null);
                }
                for (final BudgetReAppropriation budgetReAppropriation : budgetreappropriations){
                	if (budgetReAppropriation.getId() == null) {
                        throw new InvalidDataException("id", ErrorCode.MANDATORY_VALUE_MISSING.getCode(), budgetReAppropriation.getId());
                    }
                    validator.validate(budgetReAppropriation, errors);
                    if (!budgetReAppropriationRepository.uniqueCheck("name", budgetReAppropriation)) {
                        errors.addError(new FieldError("budgetReAppropriation", "name", budgetReAppropriation.getBudgetDetail(), false,
                                new String[] { ErrorCode.NON_UNIQUE_VALUE.getCode() }, null, null));
                    }
                }
                break;
            case ACTION_DELETE:
            	if (budgetreappropriations == null) {
                    throw new InvalidDataException("budgetreappropriations", ErrorCode.NOT_NULL.getCode(), null);
                }
                for (final BudgetReAppropriation budgetReAppropriation : budgetreappropriations){
                	if (budgetReAppropriation.getId() == null) {
                        throw new InvalidDataException("id", ErrorCode.MANDATORY_VALUE_MISSING.getCode(), budgetReAppropriation.getId());
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

    public List<BudgetReAppropriation> fetchRelated(final List<BudgetReAppropriation> budgetreappropriations) {
        if (budgetreappropriations != null)
            for (final BudgetReAppropriation budgetReAppropriation : budgetreappropriations)
                // fetch related items
                if (budgetReAppropriation.getBudgetDetail() != null
                        && budgetReAppropriation.getBudgetDetail().getId() != null
                        && budgetReAppropriation.getBudgetDetail().getTenantId() != null) {
                    final BudgetDetail budgetDetail = budgetDetailRepository
                            .findById(budgetReAppropriation.getBudgetDetail());
                    if (budgetDetail == null)
                        throw new InvalidDataException("budgetDetail", "budgetDetail.invalid", " Invalid budgetDetail");
                    budgetReAppropriation.setBudgetDetail(budgetDetail);
                }

        return budgetreappropriations;
    }

    public Pagination<BudgetReAppropriation> search(final BudgetReAppropriationSearch budgetReAppropriationSearch) {
        return budgetReAppropriationRepository.search(budgetReAppropriationSearch);
    }

    @Transactional
    public BudgetReAppropriation save(final BudgetReAppropriation budgetReAppropriation) {
        return budgetReAppropriationRepository.save(budgetReAppropriation);
    }

    @Transactional
    public BudgetReAppropriation update(final BudgetReAppropriation budgetReAppropriation) {
        return budgetReAppropriationRepository.update(budgetReAppropriation);
    }
    
    @Transactional
    public BudgetReAppropriation delete(final BudgetReAppropriation budgetReAppropriation) {
        return budgetReAppropriationRepository.delete(budgetReAppropriation);
    }

}
