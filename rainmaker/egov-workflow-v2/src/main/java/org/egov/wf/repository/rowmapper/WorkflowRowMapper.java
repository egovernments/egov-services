package org.egov.wf.repository.rowmapper;


import org.egov.wf.web.models.AuditDetails;
import org.egov.wf.web.models.Document;
import org.egov.wf.web.models.ProcessInstance;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class WorkflowRowMapper implements ResultSetExtractor<List<ProcessInstance>> {


    /**
     * Converts resultset to List of processInstances
     * @param rs The resultSet from db query
     * @return List of ProcessInstances from the resultset
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<ProcessInstance> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<String,ProcessInstance> processInstanceMap = new HashMap<>();

        while (rs.next()){
            String id = rs.getString("wf_id");
            ProcessInstance processInstance = processInstanceMap.get(id);

            if(processInstance==null) {
                Long lastModifiedTime = rs.getLong("wf_lastModifiedTime");
                if (rs.wasNull()) {
                    lastModifiedTime = null;
                }

                Long sla = rs.getLong("sla");
                if (rs.wasNull()) {
                    sla = null;
                }

                AuditDetails auditdetails = AuditDetails.builder()
                        .createdBy(rs.getString("wf_createdBy"))
                        .createdTime(rs.getLong("wf_createdTime"))
                        .lastModifiedBy(rs.getString("wf_lastModifiedBy"))
                        .lastModifiedTime(lastModifiedTime)
                        .build();

                processInstance = ProcessInstance.builder()
                        .id(rs.getString("id"))
                        .tenantId(rs.getString("tenantid"))
                        .businessService(rs.getString("businessService"))
                        .businessId(rs.getString("businessId"))
                        .action(rs.getString("action"))
                        .status(rs.getString("status"))
                        .comment(rs.getString("comment"))
                        .assignee(rs.getString("assignee"))
                        .assigner(rs.getString("assigner"))
                        .sla(sla)
                        .previousStatus(rs.getString("previousStatus"))
                        .auditDetails(auditdetails)
                        .build();
            }
            addChildrenToProperty(rs,processInstance);
            processInstanceMap.put(id,processInstance);
        }
        return new ArrayList<>(processInstanceMap.values());
    }


    /**
     * Adds nested object to the parent
     * @param rs The resultSet from db query
     * @param processInstance The parent ProcessInstance Object
     * @throws SQLException
     */
    private void addChildrenToProperty(ResultSet rs, ProcessInstance processInstance) throws SQLException {

        String documentId = rs.getString("doc_id");

        if(documentId!=null){

            Long lastModifiedTime = rs.getLong("doc_lastModifiedTime");
            if (rs.wasNull()) {
                lastModifiedTime = null;
            }

            AuditDetails auditdetails = AuditDetails.builder()
                    .createdBy(rs.getString("doc_createdBy"))
                    .createdTime(rs.getLong("doc_createdTime"))
                    .lastModifiedBy(rs.getString("doc_lastModifiedBy"))
                    .lastModifiedTime(lastModifiedTime)
                    .build();

            Document document = Document.builder()
                    .id(documentId)
                    .tenantId(rs.getString("doc_tenantid"))
                    .documentUid(rs.getString("documentUid"))
                    .documentType(rs.getString("documentType"))
                    .fileStoreId(rs.getString("fileStoreId"))
                    .auditDetails(auditdetails)
                    .build();

            processInstance.addDocumentsItem(document);
        }
    }



    }
