DELETE from eg_wf_matrix where objecttype = 'ReceiptHeader';



INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, 
nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate, tenantId) 
VALUES (NEXTVAL('SEQ_EG_WF_MATRIX'), 'ANY', 'ReceiptHeader', 'NEW', NULL, NULL, 'Senior Assistant,Junior Assistant', NULL, 
'Created', 'Create', 'Senior Assistant,Junior Assistant', 'Created', 'Create', NULL, NULL, '2017-01-01', '2099-04-01', 'default');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, 
nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate, tenantId) 
VALUES (NEXTVAL('SEQ_EG_WF_MATRIX'), 'ANY', 'ReceiptHeader', 'To be Submitted', NULL, NULL, 'Senior Assistant,Junior Assistant', NULL, 
'Approval Pending', 'Submit', 'Manager', 'Approval Pending', 'Submit,Cancel', NULL, NULL, '2017-01-01', '2099-04-01', 'default');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, 
nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate, tenantId) 
VALUES (NEXTVAL('SEQ_EG_WF_MATRIX'), 'ANY', 'ReceiptHeader', 'Approval Pending', NULL, NULL, 'Manager', NULL, 
'Approved', 'Approve', 'Manager', 'Approved', 'Approve,Cancel', NULL, NULL, '2017-01-01', '2099-04-01', 'default');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, 
nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate, tenantId) 
VALUES (NEXTVAL('SEQ_EG_WF_MATRIX'), 'ANY', 'ReceiptHeader', 'Approval Pending', NULL, NULL, 'Manager', NULL, 
'Rejected', 'Reject', 'Manager', 'Rejected', 'Reject,Cancel', NULL, NULL, '2017-01-01', '2099-04-01', 'default');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, 
nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate, tenantId) 
VALUES (NEXTVAL('SEQ_EG_WF_MATRIX'), 'ANY', 'ReceiptHeader', 'Approved', NULL, NULL, 'Manager', NULL, 
'END', NULL, NULL, NULL, NULL, NULL, NULL, '2017-01-01', '2099-04-01', 'default');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, 
nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate, tenantId) 
VALUES (NEXTVAL('SEQ_EG_WF_MATRIX'), 'ANY', 'ReceiptHeader', 'Rejected', NULL, NULL, 'Manager', NULL, 
'END', NULL, NULL, NULL, NULL, NULL, NULL, '2017-01-01', '2099-04-01', 'default');