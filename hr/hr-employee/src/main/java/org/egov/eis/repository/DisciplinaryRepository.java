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

package org.egov.eis.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.egov.eis.model.Disciplinary;
import org.egov.eis.model.DisciplinaryDocuments;
import org.egov.eis.repository.builder.DisciplinaryQueryBuilder;
import org.egov.eis.repository.rowmapper.DisciplinaryRowMapper;
import org.egov.eis.web.contract.DisciplinaryGetRequest;
import org.egov.eis.web.contract.DisciplinaryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

import static com.sun.jmx.snmp.ThreadContext.contains;

@Repository
@Slf4j
public class DisciplinaryRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DisciplinaryRowMapper disciplinaryRowMapper;

    @Autowired
    private DisciplinaryQueryBuilder disciplinaryQueryBuilder;

    @Autowired
    private DisciplinaryDocumentsRepository disciplinaryDocumentsRepository;

    public DisciplinaryRequest create(final DisciplinaryRequest disciplinaryRequest) {

        log.info("DisciplinaryRequest::" + disciplinaryRequest);

        final String disciplinaryInsert = DisciplinaryQueryBuilder.insertDisciplinaryQuery();

        final Disciplinary disciplinary = disciplinaryRequest.getDisciplinary();

        final List<DisciplinaryDocuments> disciplinaryDocuments = disciplinary.getDisciplinaryDocuments();
        if (disciplinaryDocuments != null) {
            final String sql = "INSERT INTO egeis_disciplinaryDocuments (id,disciplinaryId,documentType,filestoreId,tenantid) values "
                    + "(nextval('seq_egeis_disciplinaryDocument'),?,?,?,?);";
            log.info("the insert query for disciplinary docs : " + sql);
            final List<Object[]> documentBatchArgs = new ArrayList<>();
            for (final DisciplinaryDocuments documents : disciplinaryDocuments) {
                final Object[] documentRecord = { disciplinary.getId(), documents.getDocumentType(),
                        documents.getFileStoreId(),
                        disciplinary.getTenantId() };
                documentBatchArgs.add(documentRecord);
            }

            try {
                jdbcTemplate.batchUpdate(sql, documentBatchArgs);
            } catch (final DataAccessException ex) {
                log.info("exception saving disciplinary document details" + ex);
                throw new RuntimeException(ex.getMessage());
            }
        }

        final Object[] obj = new Object[] { disciplinary.getId(), disciplinary.getEmployeeId(), disciplinary.getGistCase(),
                disciplinary.getDisciplinaryAuthority(),
                disciplinary.getMemoNo(), disciplinary.getMemoDate(),
                disciplinary.getMemoServingDate(), disciplinary.getDateOfReceiptMemoDate(), disciplinary.getExplanationAccepted(),
                disciplinary.getChargeMemoNo(), disciplinary.getChargeMemoDate(),
                disciplinary.getDateOfReceiptToChargeMemoDate(),
                disciplinary.getAccepted(), disciplinary.getDateOfAppointmentOfEnquiryOfficerDate(),
                disciplinary.getEnquiryOfficerName(), disciplinary.getDateOfAppointmentOfPresentingOfficer(),
                disciplinary.getPresentingOfficerName(),
                disciplinary.getFindingsOfEO(), disciplinary.getEnquiryReportSubmittedDate(),
                disciplinary.getDateOfCommunicationOfER(),
                disciplinary.getDateOfSubmissionOfExplanationByCO(),
                disciplinary.getAcceptanceOfExplanation(), disciplinary.getProposedPunishmentByDA(),
                disciplinary.getShowCauseNoticeNo(),
                disciplinary.getShowCauseNoticeDate(), disciplinary.getShowCauseNoticeServingDate(),
                disciplinary.getExplanationToShowCauseNotice(), disciplinary.getExplanationToShowCauseNoticeAccepted(),
                disciplinary.getPunishmentAwarded(), disciplinary.getProceedingsNumber(), disciplinary.getProceedingsDate(),
                disciplinary.getProceedingsServingDate(),
                disciplinary.getCourtCase(), disciplinary.getCourtOrderNo(), disciplinary.getCourtOrderDate(),
                disciplinary.getGistOfDirectionIssuedByCourt(),
                disciplinaryRequest.getRequestInfo().getUserInfo().getId(), new Date(),
                disciplinaryRequest.getRequestInfo().getUserInfo().getId(), new Date(), disciplinary.getTenantId(),
                disciplinary.getCourtOrderType(),
                disciplinary.getPresentingOfficerDesignation(), disciplinary.getEnquiryOfficerDesignation(),disciplinary.getPunishmentImplemented(),disciplinary.getEndDateOfPunishment() };
        jdbcTemplate.update(disciplinaryInsert, obj);
        return disciplinaryRequest;
    }

    @SuppressWarnings("static-access")
    public DisciplinaryRequest update(final DisciplinaryRequest disciplinaryRequest) {

        log.info("DisciplinaryRequest::" + disciplinaryRequest);
        final String disciplinaryUpdate = DisciplinaryQueryBuilder.updateDisciplinaryQuery();
        final Disciplinary disciplinary = disciplinaryRequest.getDisciplinary();
        final Object[] obj = new Object[] { disciplinary.getEmployeeId(), disciplinary.getGistCase(),
                disciplinary.getDisciplinaryAuthority(),
                disciplinary.getMemoNo(), disciplinary.getMemoDate(),
                disciplinary.getMemoServingDate(), disciplinary.getDateOfReceiptMemoDate(), disciplinary.getExplanationAccepted(),
                disciplinary.getChargeMemoNo(), disciplinary.getChargeMemoDate(), disciplinary.getDateOfReceiptToChargeMemoDate(),
                disciplinary.getAccepted(), disciplinary.getDateOfAppointmentOfEnquiryOfficerDate(),
                disciplinary.getEnquiryOfficerName(),
                disciplinary.getDateOfAppointmentOfPresentingOfficer(), disciplinary.getPresentingOfficerName(),
                disciplinary.getFindingsOfEO(),
                disciplinary.getEnquiryReportSubmittedDate(), disciplinary.getDateOfCommunicationOfER(),
                disciplinary.getDateOfSubmissionOfExplanationByCO(),
                disciplinary.getAcceptanceOfExplanation(), disciplinary.getProposedPunishmentByDA(),
                disciplinary.getShowCauseNoticeNo(), disciplinary.getShowCauseNoticeDate(),
                disciplinary.getShowCauseNoticeServingDate(), disciplinary.getExplanationToShowCauseNotice(),
                disciplinary.getExplanationToShowCauseNoticeAccepted(),
                disciplinary.getPunishmentAwarded(), disciplinary.getProceedingsNumber(), disciplinary.getProceedingsDate(),
                disciplinary.getProceedingsServingDate(),
                disciplinary.getCourtCase(), disciplinary.getCourtOrderNo(), disciplinary.getCourtOrderDate(),
                disciplinary.getGistOfDirectionIssuedByCourt(),
                disciplinary.getLastModifiedBy(), new Date(), disciplinary.getCourtOrderType(),
                disciplinary.getPresentingOfficerDesignation(), disciplinary.getEnquiryOfficerDesignation(),
                disciplinary.getPunishmentImplemented(),disciplinary.getEndDateOfPunishment(),
                disciplinary.getId(), disciplinary.getTenantId() };
        jdbcTemplate.update(disciplinaryUpdate, obj);
        updateDisciplinaryDocuments(disciplinary);
        if(disciplinary.getDisciplinaryDocuments()!=null && !disciplinary.getDisciplinaryDocuments().isEmpty())
            disciplinaryDocumentsRepository.save(disciplinary.getId(),disciplinary.getDisciplinaryDocuments(),disciplinary.getTenantId());
        return disciplinaryRequest;

    }

    public List<Disciplinary> findForCriteria(final DisciplinaryGetRequest disciplinaryGetRequest) {
        final List<Object> preparedStatementValues = new ArrayList<>();
        final String queryStr = disciplinaryQueryBuilder.getQuery(disciplinaryGetRequest, preparedStatementValues);
        final List<Disciplinary> disciplinarys = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(),
                disciplinaryRowMapper);
        return disciplinarys;
    }

    public boolean checkIfdIDisciplinaryExists(final Long id, final String tenantId) {
        return jdbcTemplate.queryForObject(DisciplinaryQueryBuilder.DISCIPLINARY_EXISTENCE_CHECK_QUERY,
                new Object[] { id, tenantId },
                Boolean.class);
    }

    public Long generateSequences() {

        Integer result = null;
        try {
            result = jdbcTemplate.queryForObject(DisciplinaryQueryBuilder.GENERATE_SEQUENCES_QUERY, Integer.class);
            log.debug("result:" + result);
            return result.longValue();
        } catch (final Exception ex) {
            throw new RuntimeException("Next id is not generated.");
        }
    }


    private void updateDisciplinaryDocuments(Disciplinary disciplinary){
        List<String> documents = disciplinary.getDisciplinaryDocuments().stream().map(doc -> doc.getFileStoreId()).collect(Collectors.toList());
        List<DisciplinaryDocuments> documentsFromDB = disciplinaryDocumentsRepository.findByDisciplinaryId(disciplinary.getId(), disciplinary.getTenantId());
        for (DisciplinaryDocuments documentInDb : documentsFromDB) {
            if (!documents.contains(documentInDb.getFileStoreId())) {
                disciplinaryDocumentsRepository.delete(documentInDb.getDisciplinaryId(), documentInDb.getDocumentType(), documentInDb.getFileStoreId(),
                        disciplinary.getTenantId());
            }
        }
        if (disciplinary.getDisciplinaryDocuments() != null && !disciplinary.getDisciplinaryDocuments().isEmpty()) {
            disciplinary.getDisciplinaryDocuments().removeAll(documentsFromDB);
        }
    }
}
