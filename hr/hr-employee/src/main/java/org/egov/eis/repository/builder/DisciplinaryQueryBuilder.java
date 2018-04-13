/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.eis.repository.builder;

import lombok.extern.slf4j.Slf4j;
import org.egov.eis.config.ApplicationProperties;
import org.egov.eis.web.contract.DisciplinaryGetRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class DisciplinaryQueryBuilder {

    public static final String DISCIPLINARY_EXISTENCE_CHECK_QUERY = "SELECT exists(SELECT id FROM egeis_disciplinary"
            + " WHERE id = ? AND tenantId = ?)";
    public static final String GENERATE_SEQUENCES_QUERY = "SELECT nextval('seq_egeis_disciplinary') AS id";
    public String BASE_QUERY = "SELECT * FROM  egeis_disciplinary as disciplinary ";
    @Autowired
    private ApplicationProperties applicationProperties;

    private static String getIdQuery(final List<Long> idList) {
        final StringBuilder query = new StringBuilder("(");
        if (idList.size() >= 1) {
            query.append(idList.get(0).toString());
            for (int i = 1; i < idList.size(); i++)
                query.append(", " + idList.get(i));
        }
        return query.append(")").toString();
    }

    public static String insertDisciplinaryQuery() {
        return "INSERT INTO egeis_disciplinary(id,employeeId,gistCase,disciplinaryAuthority,memoNo,memoDate,"

                + " memoServingDate,dateOfReceiptMemoDate,explanationAccepted,chargeMemoNo,chargeMemoDate,"

                + "dateOfReceiptToChargeMemoDate,accepted,dateOfAppointmentOfEnquiryOfficerDate,enquiryOfficerName,"

                + "dateOfAppointmentOfPresentingOfficer,presentingOfficerName,findingsOfEO,enquiryReportSubmittedDate,"

                + "dateOfCommunicationOfER,dateOfSubmissionOfExplanationByCO,acceptanceOfExplanation,"

                + "proposedPunishmentByDA,showCauseNoticeNo,showCauseNoticeDate,showCauseNoticeServingDate,"

                + "explanationToShowCauseNotice,explanationToShowCauseNoticeAccepted,punishmentAwarded,proceedingsNumber"

                + ",proceedingsDate,proceedingsServingDate,courtCase,courtOrderNo,courtOrderDate,gistOfDirectionIssuedByCourt,"

                + "createdBy,createdDate,lastModifiedBy,lastModifiedDate,tenantId,courtOrderType,presentingOfficerDesignation,enquiryOfficerDesignation,punishmentimplemented,enddateofpunishment) values "

                + "(?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,"
                + "?,?,?,?,"
                + "?,?,?,"
                + "?,?,?,?,"
                + "?,?,?,?,"
                + "?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?)";
    }

    public static String updateDisciplinaryQuery() {
        return "UPDATE egeis_disciplinary SET employeeId=?,gistCase=?,disciplinaryAuthority=?,memoNo=?,"
                + "memoDate=?,memoServingDate=?,dateOfReceiptMemoDate=?,explanationAccepted=?,chargeMemoNo=?,chargeMemoDate=?"
                + ",dateOfReceiptToChargeMemoDate=?,accepted=?,dateOfAppointmentOfEnquiryOfficerDate=?,enquiryOfficerName=?,"
                + "dateOfAppointmentOfPresentingOfficer=?,presentingOfficerName=?,findingsOfEO=?,enquiryReportSubmittedDate=?,"
                + "dateOfCommunicationOfER=?,dateOfSubmissionOfExplanationByCO=?,acceptanceOfExplanation=?,"
                + "proposedPunishmentByDA=?,showCauseNoticeNo=?,showCauseNoticeDate=?,showCauseNoticeServingDate=?,"
                + "explanationToShowCauseNotice=?,explanationToShowCauseNoticeAccepted=?,punishmentAwarded=?,proceedingsNumber=?,"
                + "proceedingsDate=?,proceedingsServingDate=?,courtCase=?,courtOrderNo=?,courtOrderDate=?,gistOfDirectionIssuedByCourt=?,"
                + " lastModifiedBy=?,lastModifiedDate=?,courtOrderType=?,presentingOfficerDesignation=?,enquiryOfficerDesignation=?,punishmentimplemented=?,enddateofpunishment=? where id = ? and tenantid = ? ";
    }

    @SuppressWarnings("rawtypes")
    public String getQuery(final DisciplinaryGetRequest disciplinaryGetRequest, final List preparedStatementValues) {
        final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);

        addWhereClause(selectQuery, preparedStatementValues, disciplinaryGetRequest);
        addOrderByClause(selectQuery, disciplinaryGetRequest);
        addPagingClause(selectQuery, preparedStatementValues, disciplinaryGetRequest);

        log.debug("Query : " + selectQuery);
        return selectQuery.toString();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void addWhereClause(final StringBuilder selectQuery, final List preparedStatementValues,
                                final DisciplinaryGetRequest disciplinaryGetRequest) {

        if (disciplinaryGetRequest.getId() == null &&
                disciplinaryGetRequest.getTenantId() == null)
            return;

        selectQuery.append(" WHERE");
        boolean isAppendAndClause = false;

        if (disciplinaryGetRequest.getTenantId() != null) {
            isAppendAndClause = true;
            selectQuery.append(" disciplinary.tenantId = ?");
            preparedStatementValues.add(disciplinaryGetRequest.getTenantId());
        }

        if (disciplinaryGetRequest.getId() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" disciplinary.id IN " + getIdQuery(disciplinaryGetRequest.getId()));
        }

        if (disciplinaryGetRequest.getEmployeeId() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" disciplinary.employeeid IN " + getIdQuery(disciplinaryGetRequest.getEmployeeId()));
        }

    }

    private void addOrderByClause(final StringBuilder selectQuery, final DisciplinaryGetRequest disciplinaryGetRequest) {
        final String sortBy = disciplinaryGetRequest.getSortBy() == null ? "disciplinary.id" : disciplinaryGetRequest.getSortBy();
        final String sortOrder = disciplinaryGetRequest.getSortOrder() == null ? "DESC"
                : disciplinaryGetRequest.getSortOrder();
        selectQuery.append(" ORDER BY " + sortBy + " " + sortOrder);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void addPagingClause(final StringBuilder selectQuery, final List preparedStatementValues,
                                 final DisciplinaryGetRequest disciplinaryGetRequest) {
        // handle limit(also called pageSize) here
        selectQuery.append(" LIMIT ?");
        long pageSize = Integer.parseInt(applicationProperties.empSearchPageSizeDefault());
        if (disciplinaryGetRequest.getPageSize() != null)
            pageSize = disciplinaryGetRequest.getPageSize();
        preparedStatementValues.add(pageSize); // Set limit to pageSize

        // handle offset here
        selectQuery.append(" OFFSET ?");
        int pageNumber = 0; // Default pageNo is zero meaning first page
        if (disciplinaryGetRequest.getPageNumber() != null)
            pageNumber = disciplinaryGetRequest.getPageNumber() - 1;
        preparedStatementValues.add(pageNumber * pageSize); // Set offset to
        // pageNo * pageSize
    }

    /**
     * This method is always called at the beginning of the method so that and is prepended before the field's predicate is
     * handled.
     *
     * @param appendAndClauseFlag
     * @param queryString
     * @return boolean indicates if the next predicate should append an "AND"
     */
    private boolean addAndClauseIfRequired(final boolean appendAndClauseFlag, final StringBuilder queryString) {
        if (appendAndClauseFlag)
            queryString.append(" AND");

        return true;
    }


}
