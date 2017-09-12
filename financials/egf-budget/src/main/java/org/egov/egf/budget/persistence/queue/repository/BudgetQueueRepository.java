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
package org.egov.egf.budget.persistence.queue.repository;

import java.util.HashMap;

import org.egov.egf.budget.persistence.queue.FinancialProducer;
import org.egov.egf.budget.web.contract.BudgetRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BudgetQueueRepository {

    private final FinancialProducer financialProducer;

    private final String validatedTopic;

    private final String budgetValidatedKey;

    private final String completedTopic;

    private final String budgetCompletedKey;

    @Autowired
    public BudgetQueueRepository(final FinancialProducer financialProducer,
            @Value("${kafka.topics.egf.budget.service.validated.topic}") final String validatedTopic,
            @Value("${kafka.topics.egf.budget.budget.validated.key}") final String budgetValidatedKey,
            @Value("${kafka.topics.egf.budget.service.completed.topic}") final String completedTopic,
            @Value("${kafka.topics.egf.budget.budget.completed.key}") final String budgetCompletedKey) {

        this.financialProducer = financialProducer;
        this.validatedTopic = validatedTopic;
        this.budgetValidatedKey = budgetValidatedKey;
        this.completedTopic = completedTopic;
        this.budgetCompletedKey = budgetCompletedKey;
    }

    public void addToQue(final BudgetRequest request) {
        final HashMap<String, Object> topicMap = new HashMap<String, Object>();

        switch (request.getRequestInfo().getAction().toLowerCase()) {

        case "create":
            topicMap.put("budget_create", request);
            System.out.println("push create topic" + request);
            break;
        case "update":
            topicMap.put("budget_update", request);
            break;
        case "delete":
            topicMap.put("budget_delete", request);
            break;

        }
        financialProducer.sendMessage(validatedTopic, budgetValidatedKey, topicMap);
    }

    public void addToSearchQue(final BudgetRequest request) {

        final HashMap<String, Object> topicMap = new HashMap<String, Object>();

        if (!request.getBudgets().isEmpty()) {

            topicMap.put("budget_persisted", request);

            System.out.println("push search topic" + request);

        }

        financialProducer.sendMessage(completedTopic, budgetCompletedKey, topicMap);

    }
}
