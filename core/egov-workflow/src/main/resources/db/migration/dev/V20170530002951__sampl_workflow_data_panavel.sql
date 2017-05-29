INSERT INTO egpgr_router (id, complainttypeid, "position", bndryid, version, createdby, createddate, lastmodifiedby, lastmodifieddate,tenantid) VALUES (2, NULL,5,300, 0, 1, '2015-08-28', 1, '2015-08-28','panavel');



INSERT INTO service_status values(nextval('seq_service_status'),'REGISTERED',0,'panavel','REGISTERED');
INSERT INTO service_status values(nextval('seq_service_status'),'FORWARDED',0,'panavel','FORWARDED');
INSERT INTO service_status values(nextval('seq_service_status'),'PROCESSING',0,'panavel','PROCESSING');
INSERT INTO service_status values(nextval('seq_service_status'),'COMPLETED',0,'panavel','COMPLETED');
INSERT INTO service_status values(nextval('seq_service_status'),'REJECTED',0,'panavel','REJECTED');
INSERT INTO service_status values(nextval('seq_service_status'),'NOTCOMPLETED',0,'panavel','NOTCOMPLETED');
INSERT INTO service_status values(nextval('seq_service_status'),'WITHDRAWN',0,'panavel','WITHDRAWN');
INSERT INTO service_status values(nextval('seq_service_status'),'CLOSED',0,'panavel','CLOSED');
INSERT INTO service_status values(nextval('seq_service_status'),'REOPENED',0,'panavel','REOPENED');
INSERT INTO service_status values(nextval('seq_service_status'),'NEW',0,'panavel','DSNEW');
INSERT INTO service_status values(nextval('seq_service_status'),'In Progress',0,'panavel','DSPROGRESS');
INSERT INTO service_status values(nextval('seq_service_status'),'Approved',0,'panavel','DSAPPROVED');
INSERT INTO service_status values(nextval('seq_service_status'),'Rejected',0,'panavel','DSREJECTED');


insert into keyword_service_status (keyword,tenantId,servicestatuscode,version,createdby,createddate,lastmodifiedby,lastmodifieddate) values('Complaint','panavel','REGISTERED',0,0,now(),0,now());
insert into keyword_service_status (keyword,tenantId,servicestatuscode,version,createdby,createddate,lastmodifiedby,lastmodifieddate) values('Complaint','panavel','FORWARDED',0,0,now(),0,now());
insert into keyword_service_status (keyword,tenantId,servicestatuscode,version,createdby,createddate,lastmodifiedby,lastmodifieddate) values('Complaint','panavel','PROCESSING',0,0,now(),0,now());
insert into keyword_service_status (keyword,tenantId,servicestatuscode,version,createdby,createddate,lastmodifiedby,lastmodifieddate) values('Complaint','panavel','COMPLETED',0,0,now(),0,now());
insert into keyword_service_status (keyword,tenantId,servicestatuscode,version,createdby,createddate,lastmodifiedby,lastmodifieddate) values('Complaint','panavel','REJECTED',0,0,now(),0,now());
insert into keyword_service_status (keyword,tenantId,servicestatuscode,version,createdby,createddate,lastmodifiedby,lastmodifieddate) values('Complaint','panavel','NOTCOMPLETED',0,0,now(),0,now());
insert into keyword_service_status (keyword,tenantId,servicestatuscode,version,createdby,createddate,lastmodifiedby,lastmodifieddate) values('Complaint','panavel','WITHDRAWN',0,0,now(),0,now());
insert into keyword_service_status (keyword,tenantId,servicestatuscode,version,createdby,createddate,lastmodifiedby,lastmodifieddate) values('Complaint','panavel','CLOSED',0,0,now(),0,now());
insert into keyword_service_status (keyword,tenantId,servicestatuscode,version,createdby,createddate,lastmodifiedby,lastmodifieddate) values('Complaint','panavel','REOPENED',0,0,now(),0,now());
INSERT INTO keyword_service_status (keyword,tenantId,servicestatuscode,version,createdby,createddate,lastmodifiedby,lastmodifieddate)
values('Deliverable_service','panavel','DSNEW',0,0,now(),0,now());
INSERT INTO keyword_service_status (keyword,tenantId,servicestatuscode,version,createdby,createddate,lastmodifiedby,lastmodifieddate)
values('Deliverable_service','panavel','DSPROGRESS',0,0,now(),0,now());
INSERT INTO keyword_service_status (keyword,tenantId,servicestatuscode,version,createdby,createddate,lastmodifiedby,lastmodifieddate)
values('Deliverable_service','panavel','DSAPPROVED',0,0,now(),0,now());
INSERT INTO keyword_service_status (keyword,tenantId,servicestatuscode,version,createdby,createddate,lastmodifiedby,lastmodifieddate)
values('Deliverable_service','panavel','DSREJECTED',0,0,now(),0,now());

ALTER SEQUENCE seq_egpgr_complaintstatus_mapping RESTART WITH 100;


INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 20, (select id from service_status where code = 'REGISTERED' and tenantid = 'panavel'), 1, (select id from service_status where code = 'REGISTERED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 20, (select id from service_status where code = 'REGISTERED' and tenantid = 'panavel'), 2, (select id from service_status where code = 'FORWARDED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 20, (select id from service_status where code = 'REGISTERED' and tenantid = 'panavel'), 3, (select id from service_status where code = 'PROCESSING' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 20, (select id from service_status where code = 'REGISTERED' and tenantid = 'panavel'), 4, (select id from service_status where code = 'COMPLETED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 20, (select id from service_status where code = 'REGISTERED' and tenantid = 'panavel'), 5, (select id from service_status where code = 'REJECTED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 20, (select id from service_status where code = 'FORWARDED' and tenantid = 'panavel'), 1, (select id from service_status where code = 'FORWARDED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 20, (select id from service_status where code = 'FORWARDED' and tenantid = 'panavel'), 2, (select id from service_status where code = 'PROCESSING' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 20, (select id from service_status where code = 'FORWARDED' and tenantid = 'panavel'), 3, (select id from service_status where code = 'COMPLETED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 20, (select id from service_status where code = 'FORWARDED' and tenantid = 'panavel'), 4, (select id from service_status where code = 'REJECTED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 20, (select id from service_status where code = 'PROCESSING' and tenantid = 'panavel'), 1, (select id from service_status where code = 'PROCESSING' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 20, (select id from service_status where code = 'PROCESSING' and tenantid = 'panavel'), 2, (select id from service_status where code = 'COMPLETED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 20, (select id from service_status where code = 'PROCESSING' and tenantid = 'panavel'), 3, (select id from service_status where code = 'REJECTED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 20, (select id from service_status where code = 'PROCESSING' and tenantid = 'panavel'), 4, (select id from service_status where code = 'FORWARDED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 20, (select id from service_status where code = 'REJECTED' and tenantid = 'panavel'), 1, (select id from service_status where code = 'REJECTED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 20, (select id from service_status where code = 'COMPLETED' and tenantid = 'panavel'), 1, (select id from service_status where code = 'COMPLETED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 21, (select id from service_status where code = 'REGISTERED' and tenantid = 'panavel'), 1, (select id from service_status where code = 'REGISTERED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 21, (select id from service_status where code = 'REGISTERED' and tenantid = 'panavel'), 2, (select id from service_status where code = 'FORWARDED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 21, (select id from service_status where code = 'REGISTERED' and tenantid = 'panavel'), 3, (select id from service_status where code = 'PROCESSING' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 21, (select id from service_status where code = 'REGISTERED' and tenantid = 'panavel'), 4, (select id from service_status where code = 'REJECTED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 21, (select id from service_status where code = 'REGISTERED' and tenantid = 'panavel'), 5, (select id from service_status where code = 'COMPLETED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 21, (select id from service_status where code = 'FORWARDED' and tenantid = 'panavel'), 1, (select id from service_status where code = 'FORWARDED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 21, (select id from service_status where code = 'FORWARDED' and tenantid = 'panavel'), 2, (select id from service_status where code = 'PROCESSING' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 21, (select id from service_status where code = 'FORWARDED' and tenantid = 'panavel'), 3, (select id from service_status where code = 'REJECTED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 21, (select id from service_status where code = 'FORWARDED' and tenantid = 'panavel'), 4, (select id from service_status where code = 'COMPLETED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 21, (select id from service_status where code = 'PROCESSING' and tenantid = 'panavel'), 1, (select id from service_status where code = 'PROCESSING' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 21, (select id from service_status where code = 'PROCESSING' and tenantid = 'panavel'), 2, (select id from service_status where code = 'FORWARDED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 21, (select id from service_status where code = 'PROCESSING' and tenantid = 'panavel'), 3, (select id from service_status where code = 'REJECTED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 21, (select id from service_status where code = 'PROCESSING' and tenantid = 'panavel'), 4, (select id from service_status where code = 'COMPLETED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 21, (select id from service_status where code = 'REJECTED' and tenantid = 'panavel'), 1, (select id from service_status where code = 'REJECTED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 23, (select id from service_status where code = 'REGISTERED' and tenantid = 'panavel'), 1, (select id from service_status where code = 'REGISTERED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 23, (select id from service_status where code = 'REGISTERED' and tenantid = 'panavel'), (select id from service_status where code = 'FORWARDED' and tenantid = 'panavel'), 2, 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 23, (select id from service_status where code = 'REGISTERED' and tenantid = 'panavel'), (select id from service_status where code = 'PROCESSING' and tenantid = 'panavel'), 3, 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 23, (select id from service_status where code = 'REGISTERED' and tenantid = 'panavel'), (select id from service_status where code = 'COMPLETED' and tenantid = 'panavel'), 5, 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 23, (select id from service_status where code = 'REGISTERED' and tenantid = 'panavel'), (select id from service_status where code = 'REJECTED' and tenantid = 'panavel'), 4, 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 23, (select id from service_status where code = 'FORWARDED' and tenantid = 'panavel'), 1, (select id from service_status where code = 'FORWARDED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 23, (select id from service_status where code = 'FORWARDED' and tenantid = 'panavel'), 2, (select id from service_status where code = 'PROCESSING' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 23, (select id from service_status where code = 'FORWARDED' and tenantid = 'panavel'), 3, (select id from service_status where code = 'REJECTED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 23, (select id from service_status where code = 'FORWARDED' and tenantid = 'panavel'), 4, (select id from service_status where code = 'COMPLETED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 23, (select id from service_status where code = 'PROCESSING' and tenantid = 'panavel'), 1, (select id from service_status where code = 'PROCESSING' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 23, (select id from service_status where code = 'PROCESSING' and tenantid = 'panavel'), 2, (select id from service_status where code = 'FORWARDED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 23, (select id from service_status where code = 'PROCESSING' and tenantid = 'panavel'), 3, (select id from service_status where code = 'REJECTED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 23, (select id from service_status where code = 'PROCESSING' and tenantid = 'panavel'), 4, (select id from service_status where code = 'COMPLETED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 23, (select id from service_status where code = 'REJECTED' and tenantid = 'panavel'), 1, (select id from service_status where code = 'REJECTED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 23, (select id from service_status where code = 'COMPLETED' and tenantid = 'panavel'), 1, (select id from service_status where code = 'COMPLETED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 18, (select id from service_status where code = 'REGISTERED' and tenantid = 'panavel'), 1, (select id from service_status where code = 'WITHDRAWN' and tenantid = 'panavel'), NULL,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 18, (select id from service_status where code = 'PROCESSING' and tenantid = 'panavel'), 1, (select id from service_status where code = 'WITHDRAWN' and tenantid = 'panavel'), NULL,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 18, (select id from service_status where code = 'REJECTED' and tenantid = 'panavel'), 1, (select id from service_status where code = 'REOPENED' and tenantid = 'panavel'), NULL,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 18, (select id from service_status where code = 'COMPLETED' and tenantid = 'panavel'), 1, (select id from service_status where code = 'REOPENED' and tenantid = 'panavel'), NULL,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 18, (select id from service_status where code = 'REOPENED' and tenantid = 'panavel'), 1, (select id from service_status where code = 'WITHDRAWN' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 18, (select id from service_status where code = 'FORWARDED' and tenantid = 'panavel'), 1, (select id from service_status where code = 'WITHDRAWN' and tenantid = 'panavel'), NULL,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 22, (select id from service_status where code = 'REGISTERED' and tenantid = 'panavel'), 1, (select id from service_status where code = 'REGISTERED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 22, (select id from service_status where code = 'REGISTERED' and tenantid = 'panavel'), 2, (select id from service_status where code = 'FORWARDED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 22, (select id from service_status where code = 'REGISTERED' and tenantid = 'panavel'), 3, (select id from service_status where code = 'PROCESSING' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 22, (select id from service_status where code = 'REGISTERED' and tenantid = 'panavel'), 4, (select id from service_status where code = 'REJECTED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 22, (select id from service_status where code = 'REGISTERED' and tenantid = 'panavel'), 5, (select id from service_status where code = 'COMPLETED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 22, (select id from service_status where code = 'FORWARDED' and tenantid = 'panavel'), 1, (select id from service_status where code = 'FORWARDED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 22, (select id from service_status where code = 'FORWARDED' and tenantid = 'panavel'), 2, (select id from service_status where code = 'PROCESSING' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 22, (select id from service_status where code = 'FORWARDED' and tenantid = 'panavel'), 3, (select id from service_status where code = 'COMPLETED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 22, (select id from service_status where code = 'FORWARDED' and tenantid = 'panavel'), 4, (select id from service_status where code = 'REJECTED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 22, (select id from service_status where code = 'PROCESSING' and tenantid = 'panavel'), 1, (select id from service_status where code = 'PROCESSING' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 22, (select id from service_status where code = 'PROCESSING' and tenantid = 'panavel'), 2, (select id from service_status where code = 'FORWARDED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 22, (select id from service_status where code = 'PROCESSING' and tenantid = 'panavel'), 3, (select id from service_status where code = 'REJECTED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 22, (select id from service_status where code = 'PROCESSING' and tenantid = 'panavel'), 4, (select id from service_status where code = 'COMPLETED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 22, (select id from service_status where code = 'REJECTED' and tenantid = 'panavel'), 1, (select id from service_status where code = 'REJECTED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 22, (select id from service_status where code = 'COMPLETED' and tenantid = 'panavel'), 1, (select id from service_status where code = 'COMPLETED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 22, (select id from service_status where code = 'REOPENED' and tenantid = 'panavel'), 1, (select id from service_status where code = 'REOPENED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 22, (select id from service_status where code = 'REOPENED' and tenantid = 'panavel'), 2, (select id from service_status where code = 'FORWARDED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 22, (select id from service_status where code = 'REOPENED' and tenantid = 'panavel'), 3, (select id from service_status where code = 'PROCESSING' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 22, (select id from service_status where code = 'REOPENED' and tenantid = 'panavel'), 4, (select id from service_status where code = 'REJECTED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 22, (select id from service_status where code = 'REOPENED' and tenantid = 'panavel'), 5, (select id from service_status where code = 'COMPLETED' and tenantid = 'panavel'), 0,'panavel');



INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version,tenantid) VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 25, (select id from service_status where code = 'DSNEW' and tenantid = 'panavel'), 1, (select id from service_status where code = 'DSPROGRESS' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version,tenantid) VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 25, (select id from service_status where code = 'DSNEW' and tenantid = 'panavel'), 2, (select id from service_status where code = 'DSAPPROVED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version,tenantid) VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 25, (select id from service_status where code = 'DSNEW' and tenantid = 'panavel'), 3, (select id from service_status where code = 'DSREJECTED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version,tenantid) VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 25, (select id from service_status where code = 'DSPROGRESS' and tenantid = 'panavel'), 1, (select id from service_status where code = 'DSAPPROVED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version,tenantid) VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 25, (select id from service_status where code = 'DSPROGRESS' and tenantid = 'panavel'), 2, (select id from service_status where code = 'DSREJECTED' and tenantid = 'panavel'), 0,'panavel');

INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version,tenantid) VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 26, (select id from service_status where code = 'DSNEW' and tenantid = 'panavel'), 1, (select id from service_status where code = 'DSPROGRESS' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version,tenantid) VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 26, (select id from service_status where code = 'DSNEW' and tenantid = 'panavel'), 2, (select id from service_status where code = 'DSAPPROVED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version,tenantid) VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 26, (select id from service_status where code = 'DSNEW' and tenantid = 'panavel'), 3, (select id from service_status where code = 'DSREJECTED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version,tenantid) VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 26, (select id from service_status where code = 'DSPROGRESS' and tenantid = 'panavel'), 1, (select id from service_status where code = 'DSAPPROVED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version,tenantid) VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 26, (select id from service_status where code = 'DSPROGRESS' and tenantid = 'panavel'), 2, (select id from service_status where code = 'DSREJECTED' and tenantid = 'panavel'), 0,'panavel');

INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version,tenantid) VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 27, (select id from service_status where code = 'DSNEW' and tenantid = 'panavel'), 1, (select id from service_status where code = 'DSPROGRESS' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version,tenantid) VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 27, (select id from service_status where code = 'DSNEW' and tenantid = 'panavel'), 2, (select id from service_status where code = 'DSAPPROVED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version,tenantid) VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 27, (select id from service_status where code = 'DSNEW' and tenantid = 'panavel'), 3, (select id from service_status where code = 'DSREJECTED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version,tenantid) VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 27, (select id from service_status where code = 'DSNEW' and tenantid = 'panavel'), 1, (select id from service_status where code = 'DSAPPROVED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version,tenantid) VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 27, (select id from service_status where code = 'DSPROGRESS' and tenantid = 'panavel'), 2, (select id from service_status where code = 'DSREJECTED' and tenantid = 'panavel'), 0,'panavel');

INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version,tenantid) VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 28, (select id from service_status where code = 'DSNEW' and tenantid = 'panavel'), 1, (select id from service_status where code = 'DSPROGRESS' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version,tenantid) VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 28, (select id from service_status where code = 'DSNEW' and tenantid = 'panavel'), 2, (select id from service_status where code = 'DSAPPROVED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version,tenantid) VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 28, (select id from service_status where code = 'DSNEW' and tenantid = 'panavel'), 3, (select id from service_status where code = 'DSREJECTED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version,tenantid) VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 28, (select id from service_status where code = 'DSPROGRESS' and tenantid = 'panavel'), 1, (select id from service_status where code = 'DSAPPROVED' and tenantid = 'panavel'), 0,'panavel');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version,tenantid) VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 28, (select id from service_status where code = 'DSPROGRESS' and tenantid = 'panavel'), 2, (select id from service_status where code = 'DSREJECTED' and tenantid = 'panavel'), 0,'panavel');






