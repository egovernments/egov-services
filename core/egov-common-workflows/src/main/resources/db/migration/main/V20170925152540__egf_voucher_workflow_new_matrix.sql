delete from eg_wf_matrix where objecttype = 'Voucher';


INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate, tenantId) VALUES (NEXTVAL('SEQ_EG_WF_MATRIX'), 'ANY', 'Voucher', 'NEW', NULL, NULL,'Junior Assistant', null, 'Commissioner Approval pending', 'Commissioner Approval pending', 'Commissioner', 'Junior Assistant Approved', 'Submit', NULL, NULL, '2017-01-01', '2099-04-01', 'default');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate,tenantId) VALUES (nextval('SEQ_EG_WF_MATRIX'), 'ANY', 'Voucher', 'Commissioner Approval pending','Junior Assistant Approved', NULL, 'Commissioner', NULL,'END', 'END', NULL, 'Commissioner Approved', 'Approve,Reject', NULL, NULL, '2016-01-01', '2099-04-01','default');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate, tenantId) VALUES (NEXTVAL('SEQ_EG_WF_MATRIX'), 'ANY', 'Voucher', 'Rejected', NULL, NULL,'Junior Assistant', null, 'Commissioner Approval pending', 'Commissioner Approval pending', 'Commissioner', 'Junior Assistant Approved', 'Submit,Cancel', NULL, NULL, '2017-01-01', '2099-04-01', 'default');

