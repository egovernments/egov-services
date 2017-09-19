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

package org.egov.tradelicense.domain.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.tradelicense.domain.model.AuditDetails;
import org.egov.tradelicense.domain.model.NoticeDocument;
import org.egov.tradelicense.domain.repository.NoticeDocumentRepository;
import org.egov.tradelicense.persistence.entity.NoticeDocumentEntity;
import org.egov.tradelicense.web.contract.NoticeDocumentGetRequest;
import org.egov.tradelicense.web.contract.NoticeDocumentRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class NoticeDocumentService {

    public static final Logger LOGGER = LoggerFactory.getLogger(NoticeDocumentService.class);

    @Value("${kafka.topics.noticedocument.create.name}")
    private String noticeDocumentCreateTopic;

    @Value("${kafka.topics.noticedocument.update.name}")
    private String noticeDocumentUpdateTopic;

    @Autowired
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private NoticeDocumentRepository noticeDocumentRepository;

    public List<NoticeDocument> getNoticeDocuments(final NoticeDocumentGetRequest noticeDocumentGetRequest,
            final RequestInfo requestInfo) {
        final List<NoticeDocumentEntity> noticeDocumentEntities = noticeDocumentRepository
                .findForCriteria(noticeDocumentGetRequest, requestInfo);
        final List<NoticeDocument> noticeDocuments = new ArrayList<>();
        for (NoticeDocumentEntity entity : noticeDocumentEntities)
            noticeDocuments.add(entity.toDomain());
        return noticeDocuments;
    }

    @Transactional
    public List<NoticeDocument> createNoticeDocument(final NoticeDocumentRequest noticeDocumentRequest) {
        for (NoticeDocument document : noticeDocumentRequest.getNoticeDocument()) {
            document.setId(Long.valueOf(noticeDocumentRepository.getSequence(NoticeDocumentEntity.SEQUENCE_NAME)));
            AuditDetails auditDetails = new AuditDetails();
            auditDetails.setCreatedTime(new Date().getTime());
            auditDetails.setLastModifiedTime(new Date().getTime());
            RequestInfo requestInfo = noticeDocumentRequest.getRequestInfo();
            if (requestInfo != null && requestInfo.getUserInfo() != null && requestInfo.getUserInfo().getId() != null) {
                auditDetails.setCreatedBy(requestInfo.getUserInfo().getId().toString());
                auditDetails.setLastModifiedBy(requestInfo.getUserInfo().getId().toString());
            }
            document.setAuditDetails(auditDetails);
        }
        kafkaTemplate.send(noticeDocumentCreateTopic, noticeDocumentRequest);
        return noticeDocumentRequest.getNoticeDocument();
    }

    @Transactional
    public NoticeDocumentRequest create(final NoticeDocumentRequest noticeDocumentRequest) {
        for (NoticeDocument document : noticeDocumentRequest.getNoticeDocument())
            noticeDocumentRepository.create(new NoticeDocumentEntity().toEntity(document));
        return noticeDocumentRequest;
    }

    public List<NoticeDocument> updateNoticeDocument(final NoticeDocumentRequest noticeDocumentRequest) {
        for (NoticeDocument document : noticeDocumentRequest.getNoticeDocument()) {
            AuditDetails auditDetails = document.getAuditDetails();
            if (auditDetails == null) {
                auditDetails = new AuditDetails();
            }
            RequestInfo requestInfo = noticeDocumentRequest.getRequestInfo();
            auditDetails.setLastModifiedTime(new Date().getTime());
            if (requestInfo != null && requestInfo.getUserInfo() != null && requestInfo.getUserInfo().getId() != null) {
                auditDetails.setLastModifiedBy(requestInfo.getUserInfo().getId().toString());
            }
            document.setAuditDetails(auditDetails);
        }
        kafkaTemplate.send(noticeDocumentUpdateTopic, noticeDocumentRequest);
        return noticeDocumentRequest.getNoticeDocument();
    }

    @Transactional
    public NoticeDocumentRequest update(final NoticeDocumentRequest noticeDocumentRequest) {
        for (NoticeDocument document : noticeDocumentRequest.getNoticeDocument())
            noticeDocumentRepository.update(new NoticeDocumentEntity().toEntity(document));
        return noticeDocumentRequest;
    }
}