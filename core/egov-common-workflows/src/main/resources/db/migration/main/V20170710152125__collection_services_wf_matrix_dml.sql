INSERT INTO eg_wf_types (id,module,type,link,createdby,createddate,lastmodifiedby,lastmodifieddate,typefqn,displayname,version,tenantid,enabled,grouped) 
VALUES (nextval('seq_eg_wf_types'), NULL,'ReceiptHeader','/collection-svc/app/collection/transaction/create-connection.html?stateId=:ID',1,now(),1,now(), 
'org.egov.collection.model.ReceiptHeader', 'Collections Receipt Header', 0,'default',true,false);


INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, 
nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate, tenantId) 
VALUES (NEXTVAL('SEQ_EG_WF_MATRIX'), 'ANY', 'ReceiptHeader', 'NEW', NULL, NULL, 'Senior Assistant,Junior Assistant', NULL, 
'Receipt Created', 'Create Receipt', 'Senior Assistant,Junior Assistant', 'Receipt Created', 'Approve', NULL, NULL, '2017-01-01', '2099-04-01', 'default');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, 
nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate, tenantId) 
VALUES (NEXTVAL('SEQ_EG_WF_MATRIX'), 'ANY', 'ReceiptHeader', 'NEW', NULL, NULL, 'Senior Assistant,Junior Assistant', NULL, 
'Receipt Voucher Created', 'Create Receipt Voucher', 'Senior Assistant,Junior Assistant', 'Receipt Voucher Created', 'Approve', NULL, NULL, '2017-01-01', '2099-04-01', 'default');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, 
nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate, tenantId) 
VALUES (NEXTVAL('SEQ_EG_WF_MATRIX'), 'ANY', 'ReceiptHeader', 'Receipt Created', NULL, NULL, 'Senior Assistant,Junior Assistant', NULL, 
'Receipt Submitted', 'Submit for Approval', 'Senior Assistant,Junior Assistant', 'Receipt Submitted', 'Submit', NULL, NULL, '2017-01-01', '2099-04-01', 'default');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, 
nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate, tenantId) 
VALUES (NEXTVAL('SEQ_EG_WF_MATRIX'), 'ANY', 'ReceiptHeader', 'Receipt Voucher Created', NULL, NULL, 'Senior Assistant,Junior Assistant', NULL, 
'Receipt Submitted', 'Submit for Approval', 'Senior Assistant,Junior Assistant', 'Receipt Submitted', 'Submit', NULL, NULL, '2017-01-01', '2099-04-01', 'default');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, 
nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate, tenantId) 
VALUES (NEXTVAL('SEQ_EG_WF_MATRIX'), 'ANY', 'ReceiptHeader', 'Receipt Submitted', NULL, NULL, 'Manager', NULL, 
'Approved', 'Approve Receipt', 'Manager', 'Approved', 'Approve', NULL, NULL, '2017-01-01', '2099-04-01', 'default');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, 
nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate, tenantId) 
VALUES (NEXTVAL('SEQ_EG_WF_MATRIX'), 'ANY', 'ReceiptHeader', 'Receipt Submitted', NULL, NULL, 'Manager', NULL, 
'Rejected', 'Reject Receipt', 'Manager', 'Rejected', 'Reject', NULL, NULL, '2017-01-01', '2099-04-01', 'default');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, 
nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate, tenantId) 
VALUES (NEXTVAL('SEQ_EG_WF_MATRIX'), 'ANY', 'ReceiptHeader', 'Approved', NULL, NULL, 'Manager', NULL, 
'END', NULL, NULL, NULL, NULL, NULL, NULL, '2017-01-01', '2099-04-01', 'default');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, 
nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate, tenantId) 
VALUES (NEXTVAL('SEQ_EG_WF_MATRIX'), 'ANY', 'ReceiptHeader', 'Rejected', NULL, NULL, 'Manager', NULL, 
'Receipt Submitted', 'Submit for Approval', 'Senior Assistant,Junior Assistant', 'Receipt Submitted', 'Submit,Reject', NULL, NULL, '2017-01-01', '2099-04-01', 'default');
