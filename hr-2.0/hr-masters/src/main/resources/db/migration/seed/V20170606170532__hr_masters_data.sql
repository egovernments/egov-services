insert into egeis_group(id,name,description,tenantid) values (nextval('seq_egeis_group'),'State','State','default');
insert into egeis_group(id,name,description,tenantid) values (nextval('seq_egeis_group'),'Central','Central','default');
insert into egeis_group(id,name,description,tenantid) values (nextval('seq_egeis_group'),'Local','Local','default');


insert into egeis_recruitmenttype(id,name,description,tenantid) values(nextval('seq_egeis_recruitmentType'),'Direct','Direct','default');
insert into egeis_recruitmenttype(id,name,description,tenantid) values(nextval('seq_egeis_recruitmentType'),'Transfer','Transfer','default');
insert into egeis_recruitmenttype(id,name,description,tenantid) values(nextval('seq_egeis_recruitmentType'),'Compensatory','Compensatory','default');


insert into egeis_recruitmentMode(id,name,description,tenantid) values(nextval('seq_egeis_recruitmentMode'),'UPSC','Public Service','default');
insert into egeis_recruitmentMode(id,name,description,tenantid) values(nextval('seq_egeis_recruitmentMode'),'Direct','Direct','default');
insert into egeis_recruitmentMode(id,name,description,tenantid) values(nextval('seq_egeis_recruitmentMode'),'Department Exams','Department Exams','default');


insert into egeis_recruitmentQuota(id,name,description,tenantid) values(nextval('seq_egeis_recruitmentQuota'),'Sports','Sports Quota','default');
insert into egeis_recruitmentQuota(id,name,description,tenantid) values(nextval('seq_egeis_recruitmentQuota'),'Ex-Serviceman','Ex-Serviceman Quota','default');
insert into egeis_recruitmentQuota(id,name,description,tenantid) values(nextval('seq_egeis_recruitmentQuota'),'General','General Quota','default');

insert into egeis_grade values (nextval('seq_egeis_grade'),'A','First Grade',1,true,'default');
insert into egeis_grade values (nextval('seq_egeis_grade'),'B','Second Level',2,true,'default');
insert into egeis_grade values (nextval('seq_egeis_grade'),'G','General Level',3,true,'default');