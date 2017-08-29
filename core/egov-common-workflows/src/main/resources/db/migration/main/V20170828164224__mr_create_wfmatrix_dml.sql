INSERT into eg_wf_types (id, type, link, createdby, createddate, lastmodifiedby, lastmodifieddate, typefqn, displayname, version, tenantid,enabled,grouped)
 values (nextval('seq_eg_wf_types'), 'MarriageRegn', '/mr-web/app/search-marriageRegn/view-create-marriageRegn.html?view=inbox&state=:ID', 1, now(), 1, now(), 'org.egov.mr.models.MarriageRegn', 'Create MarriageRegn', 0, 'default',true,true);

INSERT INTO eg_wf_matrix (id,department,objecttype,currentstate,currentstatus,pendingactions,currentdesignation,additionalrule,nextstate,nextaction,nextdesignation,nextstatus,validactions,fromqty,toqty,fromdate,todate,tenantId)
 VALUES (NEXTVAL('SEQ_EG_WF_MATRIX'),'ANY','MarriageRegn','NEW',NULL,NULL,		    'Junior Assistant','NEW MARRIAGEREGN','Assistant Approved','Payament Pending','Commissioner','Assistant Approved', NULL,NULL,NULL,'2017-01-01','2099-04-01','default');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate, tenantId)
 VALUES (NEXTVAL('SEQ_EG_WF_MATRIX'), 'ANY', 'MarriageRegn', 'Assistant Approved', NULL, NULL, 'Commissioner', 'NEW MARRIAGEREGN', 'Advance paid', 'Commissioner Approval Pending', 'Commissioner', 'Advance paid', 'Approve,Reject', NULL, NULL, '2017-01-01', '2099-04-01', 'default');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate, tenantId)
 VALUES (NEXTVAL('SEQ_EG_WF_MATRIX'), 'ANY', 'MarriageRegn', 'Advance paid', NULL, NULL, 'Commissioner', 'NEW MARRIAGEREGN', 'Assistant Approved', 'Commissioner Approval Pending', 'Commissioner', 'Assistant Approved', 'Approve,Reject', NULL, NULL, '2017-01-01', '2099-04-01', 'default');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate, tenantId)
 VALUES (NEXTVAL('SEQ_EG_WF_MATRIX'), 'ANY', 'MarriageRegn', 'Commissioner approved', NULL, NULL, 'Junior Assistant', 'NEW MARRIAGEREGN', 'END', 'END', NULL, NULL, NULL, NULL, NULL, '2017-01-01', '2099-04-01', 'default');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate, tenantId)
 VALUES (NEXTVAL('SEQ_EG_WF_MATRIX'), 'ANY', 'MarriageRegn', 'Rejected', NULL, NULL, 'Junior Assistant', 'NEW MARRIAGEREGN', 'Assistant Approved', 'Assistant Approval Pending', 'Commissioner', 'Assistant Approved', 'Forward,Reject', NULL, NULL, '2017-01-01', '2099-04-01', 'default');