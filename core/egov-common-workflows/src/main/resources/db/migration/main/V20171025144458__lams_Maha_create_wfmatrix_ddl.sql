
INSERT into eg_wf_types (id, type, link, createdby, createddate, lastmodifiedby, lastmodifieddate, typefqn, displayname, version, tenantid,enabled,grouped)
 values (nextval('seq_eg_wf_types'), 'Create Estate', '/mr-web/app/search-lams-maha/view-create-lamsmaha.html?view=inbox&state=:ID', 1, now(), 1, now(), 'org.egov.lams.common.web.contract.EstateRegister', 'Create Estate Register', 0, 'default',true,true);

INSERT INTO eg_wf_matrix (id,department,objecttype,currentstate  ,currentstatus,pendingactions   ,currentdesignation,additionalrule,nextstate,nextaction,nextdesignation,nextstatus,validactions,fromqty,toqty,fromdate,todate,tenantId)
 VALUES (NEXTVAL('SEQ_EG_WF_MATRIX'),'ANY','Create Estate','NEW',NULL,NULL ,'Clerk','New EstateRegister','Clerk Submitted','Revenue Officer Approval Pending','Revenue Officer','Clerk Submitted', NULL,NULL,NULL,'2017-01-01','2099-04-01','default');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate, tenantId)
 VALUES (NEXTVAL('SEQ_EG_WF_MATRIX'),'ANY','Create Estate','Clerk Submitted', NULL , 'Revenue Officer Approved' , 'Revenue Officer', 'New EstateRegister', 'Revenue Officer Approved', 'Chief Officer Approval Pending', 'Chief Officer', 'Revenue Officer Approved', 'Approve,Reject', NULL, NULL, '2017-01-01', '2099-04-01', 'default');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate, tenantId)
 VALUES (NEXTVAL('SEQ_EG_WF_MATRIX'), 'ANY', 'Create Estate', 'Revenue Officer Approved', NULL, 'Chief Officer Approval Pending', 'Chief Officer', 'New EstateRegister', 'Chief Officer Approved', 'Chief Officer Approval Pending', 'Chief Officer', 'Chief Officer Approved', 'Approve,Reject', NULL, NULL, '2017-01-01', '2099-04-01', 'default');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate, tenantId)
 VALUES (NEXTVAL('SEQ_EG_WF_MATRIX'), 'ANY', 'Create Estate', 'Chief Officer Approved', NULL, NULL, 'Clerk', 'New EstateRegister', 'END', 'END', NULL, NULL, NULL, NULL, NULL, '2017-01-01', '2099-04-01', 'default');

INSERT INTO eg_wf_matrix (id, department, objecttype, 	currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate, tenantId)
 VALUES (NEXTVAL('SEQ_EG_WF_MATRIX'), 'ANY', 'Create Estate', 'Rejected', NULL, NULL, 'Clerk', 'New EstateRegister', 'NEW', 'Clerk Submission Pending', 'Clerk', 'Clerk Submitted', 'Forward,Close Application', NULL, NULL, '2017-01-01', '2099-04-01', 'default');
