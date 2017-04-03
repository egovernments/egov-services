insert into service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId)values
(nextval('SEQ_SERVICE'),'PGR','PGR',true,'pgr','Grievance Redressal',3,'','ap.kurnool');
insert into service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId)values
(nextval('SEQ_SERVICE'),'PgrComp','PGRComplaints',true,'','Grievance',1,(select id from service where name='PGR'and contextroot='pgr'),'ap.kurnool');

