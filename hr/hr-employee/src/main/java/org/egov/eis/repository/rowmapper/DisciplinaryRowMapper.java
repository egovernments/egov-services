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

package org.egov.eis.repository.rowmapper;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.eis.model.Disciplinary;
import org.egov.eis.model.DisciplinaryDocuments;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class DisciplinaryRowMapper implements RowMapper<Disciplinary> {

    @Override
    public Disciplinary mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        final Disciplinary disciplinary = new Disciplinary();
        disciplinary.setId(rs.getLong("id"));
        disciplinary.setMemoNo(rs.getString("memoNo"));

        disciplinary.setGistCase(rs.getString("gistCase"));
        disciplinary.setAcceptanceOfExplanation(rs.getBoolean("acceptanceOfExplanation"));
        disciplinary.setAccepted(rs.getBoolean("accepted"));
        disciplinary.setChargeMemoNo(rs.getString("chargeMemoNo"));
        disciplinary.setCourtCase(rs.getBoolean("courtCase"));
        disciplinary.setCourtOrderNo(rs.getString("courtOrderNo"));
        disciplinary.setCreatedBy((Long) rs.getObject("createdBy"));
        disciplinary.setDisciplinaryAuthority(rs.getString("disciplinaryAuthority"));
        disciplinary.setEmployeeId((Long) rs.getObject("employeeId"));
        disciplinary.setEnquiryOfficerName(rs.getString("enquiryOfficerName"));
        disciplinary.setExplanationAccepted(rs.getBoolean("explanationAccepted"));
        disciplinary.setExplanationToShowCauseNotice(rs.getString("explanationToShowCauseNotice"));
        disciplinary.setExplanationToShowCauseNoticeAccepted(rs.getBoolean("explanationToShowCauseNoticeAccepted"));
        disciplinary.setFindingsOfEO(rs.getString("findingsOfEO"));
        disciplinary.setGistOfDirectionIssuedByCourt(rs.getString("gistOfDirectionIssuedByCourt"));
        disciplinary.setLastModifiedBy((Long) rs.getObject("lastModifiedBy"));
        disciplinary.setOrderNo(rs.getString("orderNo"));
        disciplinary.setPresentingOfficerName(rs.getString("presentingOfficerName"));
        disciplinary.setProceedingsNumber(rs.getString("proceedingsNumber"));
        disciplinary.setProposedPunishmentByDA(rs.getString("proposedPunishmentByDA"));
        disciplinary.setPunishmentAwarded(rs.getString("punishmentAwarded"));
        disciplinary.setShowCauseNoticeNo(rs.getString("showCauseNoticeNo"));
        disciplinary.setTenantId(rs.getString("tenantId"));

        try {
            Date date = isEmpty(rs.getDate("memoDate")) ? null
                    : sdf.parse(sdf.format(rs.getDate("memoDate")));
            disciplinary.setMemoDate(date);

            date = isEmpty(rs.getDate("chargeMemoDate")) ? null : sdf.parse(sdf.format(rs.getDate("chargeMemoDate")));
            disciplinary.setChargeMemoDate(date);
            date = isEmpty(rs.getDate("courtOrderDate")) ? null
                    : sdf.parse(sdf.format(rs.getDate("courtOrderDate")));
            disciplinary.setCourtOrderDate(date);

            date = isEmpty(rs.getDate("dateOfAppointmentOfEnquiryOfficerDate")) ? null
                    : sdf.parse(sdf.format(rs.getDate("dateOfAppointmentOfEnquiryOfficerDate")));
            disciplinary.setDateOfAppointmentOfEnquiryOfficerDate(date);

            date = isEmpty(rs.getDate("dateOfAppointmentOfPresentingOfficer")) ? null
                    : sdf.parse(sdf.format(rs.getDate("dateOfAppointmentOfPresentingOfficer")));
            disciplinary.setDateOfAppointmentOfPresentingOfficer(date);

            date = isEmpty(rs.getDate("dateOfCommunicationOfER")) ? null
                    : sdf.parse(sdf.format(rs.getDate("dateOfCommunicationOfER")));
            disciplinary.setDateOfCommunicationOfER(date);

            date = isEmpty(rs.getDate("dateOfReceiptMemoDate")) ? null
                    : sdf.parse(sdf.format(rs.getDate("dateOfReceiptMemoDate")));
            disciplinary.setDateOfReceiptMemoDate(date);

            date = isEmpty(rs.getDate("dateOfReceiptToChargeMemoDate")) ? null
                    : sdf.parse(sdf.format(rs.getDate("dateOfReceiptToChargeMemoDate")));
            disciplinary.setDateOfReceiptToChargeMemoDate(date);

            date = isEmpty(rs.getDate("dateOfSubmissionOfExplanationByCO")) ? null
                    : sdf.parse(sdf.format(rs.getDate("dateOfSubmissionOfExplanationByCO")));
            disciplinary.setDateOfSubmissionOfExplanationByCO(date);

            date = isEmpty(rs.getDate("enquiryReportSubmittedDate")) ? null
                    : sdf.parse(sdf.format(rs.getDate("enquiryReportSubmittedDate")));
            disciplinary.setEnquiryReportSubmittedDate(date);

            date = isEmpty(rs.getDate("memoServingDate")) ? null
                    : sdf.parse(sdf.format(rs.getDate("memoServingDate")));
            disciplinary.setMemoServingDate(date);

            date = isEmpty(rs.getDate("proceedingsDate")) ? null
                    : sdf.parse(sdf.format(rs.getDate("proceedingsDate")));
            disciplinary.setProceedingsDate(date);

            date = isEmpty(rs.getDate("orderDate")) ? null
                    : sdf.parse(sdf.format(rs.getDate("orderDate")));
            disciplinary.setOrderDate(date);

            date = isEmpty(rs.getDate("proceedingsServingDate")) ? null
                    : sdf.parse(sdf.format(rs.getDate("proceedingsServingDate")));
            disciplinary.setProceedingsServingDate(date);

            date = isEmpty(rs.getDate("showCauseNoticeDate")) ? null
                    : sdf.parse(sdf.format(rs.getDate("showCauseNoticeDate")));
            disciplinary.setShowCauseNoticeDate(date);

            date = isEmpty(rs.getDate("showCauseNoticeServingDate")) ? null
                    : sdf.parse(sdf.format(rs.getDate("showCauseNoticeServingDate")));
            disciplinary.setShowCauseNoticeServingDate(date);

            date = isEmpty(rs.getDate("createdDate")) ? null
                    : sdf.parse(sdf.format(rs.getDate("createdDate")));

            disciplinary.setCreatedDate(date);

            date = isEmpty(rs.getDate("lastModifiedDate")) ? null
                    : sdf.parse(sdf.format(rs.getDate("lastModifiedDate")));
            disciplinary.setLastModifiedDate(date);
        } catch (final ParseException e) {
            e.printStackTrace();
            throw new SQLException("Parse exception while parsing date");
        }

        List<DisciplinaryDocuments> docList = new ArrayList<>();
        DisciplinaryDocuments documents = new DisciplinaryDocuments();
        documents.setDisciplinaryId((Long) rs.getObject("disciplinaryId"));
        documents.setFileStoreId(rs.getString("filestoreId"));
        documents.setId((Long) rs.getObject("documentsId"));
        if (documents.getId() == null)
            disciplinary.setDisciplinaryDocuments(new ArrayList<>());
        else
            docList.add(documents);
        disciplinary.setDisciplinaryDocuments(docList);
        return disciplinary;
    }

}
