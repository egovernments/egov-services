 --root of Grievance Redressal
    insert into service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId) 
	values (nextval('SEQ_SERVICE'),'PGR','Grievance Redressal',true,'pgr' ,'Grievance Redressal',NULL ,NULL ,'default');

--inside Grievance Redressal 
	--1.Grievance
	insert into service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId) 
	values (nextval('SEQ_SERVICE'),'GRV','Grievance',true,'pgr' ,'Grievance', 2 ,(select id from service where name ='Grievance Redressal' and code='PGR' and tenantId='default'), 'default');

--inside Grievance Redressal 
	--2.Masters
	insert into service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId) 
	values (nextval('SEQ_SERVICE'),'MSTR','Masters',true,'pgr' ,'Masters', 1 ,(select id from service where name ='Grievance Redressal' and code='PGR' and tenantId='default'), 'default');

--inside Grievance Redressal 
	--3.Reports
	insert into service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId) 
	values (nextval('SEQ_SERVICE'),'RPT','Reports',true,'pgr' ,'Reports', 3 ,(select id from service where name ='Grievance Redressal' and code='PGR' and tenantId='default'), 'default');



--inside Masters  
	--1.Grievance type
	insert into service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId) 
	values (nextval('SEQ_SERVICE'),'GRVTYP','Grievance type',true,'pgr' ,'Grievance type',1 ,(select id from service where name ='Masters' and code='MSTR' and tenantId='default') ,'default');

	--2. Router
	insert into service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId) 
	values (nextval('SEQ_SERVICE'),'RUTR','Router',true,'pgr' ,'Router',2 ,(select id from service where name ='Masters' and code='MSTR' and tenantId='default') ,'default');

	--3.Escalation Time
	insert into service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId) 
	values (nextval('SEQ_SERVICE'),'ESCLT','Escalation Time',true,'pgr' ,'Escalation Time',3 ,(select id from service where name ='Masters' and code='MSTR' and tenantId='default') ,'default');
 
 	--4. Escalation
 	insert into service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId) 
	values (nextval('SEQ_SERVICE'),'ESCL','Escalation',true,'pgr' ,'Escalation',4 ,(select id from service where name ='Masters' and code='MSTR' and tenantId='default') ,'default');
 
 	--5. Receivingmode
 	insert into service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId) 
	values (nextval('SEQ_SERVICE'),'RCMD','Receiving Mode',true,'pgr' ,'Receiving Mode',5 ,(select id from service where name ='Masters' and code='MSTR' and tenantId='default') ,'default');
 
 	--6. ReceivingCenter
 	insert into service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId) 
	values (nextval('SEQ_SERVICE'),'RCVC','Receiving Center',true,'pgr' ,'Receiving Center',6 ,(select id from service where name ='Masters' and code='MSTR' and tenantId='default') ,'default');
 
 	--7.Grievance category
	insert into service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId) 
	values (nextval('SEQ_SERVICE'),'GRVCTG','Grievance Category',true,'pgr' ,'Grievance Category',7 ,(select id from service where name ='Masters' and code='MSTR' and tenantId='default') ,'default');

-- Updating eg_action table  Grievance  screen related urls
	UPDATE eg_action SET displayname= 'Officials Register Grievance',parentmodule = (select id from service where code='GRV' and tenantid ='default'), servicecode = 'GRV' ,enabled=true , ordernumber =1 WHERE servicecode='PGR' and url='/pgr/seva/v1/_create';

	UPDATE eg_action SET displayname = 'Search Grievance',parentmodule = (select id from service where code='GRV' and tenantid ='default'), servicecode = 'GRV' , enabled=true , ordernumber =2 WHERE servicecode='PGR' and url='/pgr/seva/v1/_search';

	UPDATE eg_action SET servicecode = 'GRV' ,parentmodule = (select id from service where code='GRV' and tenantid ='default'), ordernumber =3 WHERE servicecode='PGR' and url='/pgr/seva/v1/_update';

 	
-- Updating eg_action table  Master  screen related urls 

	UPDATE eg_action SET displayname= 'Create Category', servicecode = 'GRVCTG' , parentmodule = (select id from service where code='GRVCTG' and tenantid ='default'), enabled=true , ordernumber =1 WHERE servicecode='pgr' and url='/pgr-master/serviceGroup/v1/_create';

	UPDATE eg_action SET displayname = 'Search Category', servicecode = 'GRVCTG' , parentmodule =(select id from service where code='GRVCTG' and tenantid ='default'), enabled=true , ordernumber =2 WHERE servicecode='PGR' and url='/pgr-master/serviceGroup/v1/_search';

	UPDATE eg_action SET displayname = 'Update Category',  servicecode = 'GRVCTG' , parentmodule =(select id from service where code='GRVCTG' and tenantid ='default'), enabled=true , ordernumber =3  WHERE servicecode='pgr' and url='/pgr-master/serviceGroup/v1/{id}/_update';

	UPDATE eg_action SET displayname = 'Create Grievance Type' , servicecode = 'GRVTYP' , parentmodule = (select id from service where code='GRVTYP' and tenantid ='default'), enabled=true , ordernumber =1 WHERE servicecode='PGR' and url='/pgr-master/service/v1/_create';

	UPDATE eg_action SET displayname = 'View Grievance Type' ,servicecode = 'GRVTYP' , parentmodule = (select id from service where code='GRVTYP' and tenantid ='default'), enabled=true , ordernumber =2 WHERE servicecode='PGR' and url='/pgr-master/service/v1/_search';

	UPDATE eg_action SET displayname = 'Update Grievance Type' ,servicecode = 'GRVTYP' , parentmodule = (select id from service where code='GRVTYP' and tenantid ='default'), enabled=true , ordernumber =3 WHERE servicecode='PGR' and url='/pgr-master/service/v1/{code}/_update';

	UPDATE eg_action SET displayname = 'Create Receiving Center' , servicecode = 'RCVC' , parentmodule = (select id from service where code='RCVC' and tenantid ='default'), enabled=true, ordernumber =1  WHERE servicecode='PGR' and url='/pgr-master/receivingcenter/v1/_create';

	UPDATE eg_action SET displayname = 'Update Receiving Center' ,servicecode = 'RCVC' , parentmodule = (select id from service where code='RCVC' and tenantid ='default'), enabled=true , ordernumber =2 WHERE servicecode='PGR' and url='/pgr-master/receivingcenter/v1/_search';

	UPDATE eg_action SET displayname = 'View Receiving Center' , servicecode = 'RCVC' , parentmodule = (select id from service where code='RCVC' and tenantid ='default'), enabled=true , ordernumber =3 WHERE servicecode='PGR' and url='/pgr-master/receivingcenter/v1/{id}/_update';

	UPDATE eg_action SET displayname = 'Create Receiving Mode', servicecode = 'RCMD' , parentmodule = (select id from service where code='RCMD' and tenantid ='default'), enabled=true, ordernumber =1 WHERE servicecode='PGR' and url='/pgr-master/receivingmode/v1/_create';

	UPDATE eg_action SET displayname = 'View Receiving Mode', servicecode = 'RCMD' , parentmodule = (select id from service where code='RCMD' and tenantid ='default'), enabled=true, ordernumber =2 WHERE servicecode='PGR' and url='/pgr-master/receivingmode/v1/_search';

	UPDATE eg_action SET displayname = 'Update Receiving Mode', servicecode = 'RCMD' , parentmodule = (select id from service where code='RCMD' and tenantid ='default'), enabled=true, ordernumber =3 WHERE servicecode='PGR' and url='/pgr-master/receivingmode/v1/{id}/_update';

	UPDATE eg_action SET displayname = 'Create Router', servicecode = 'RUTR' , parentmodule = (select id from service where code='RUTR' and tenantid ='default'), enabled=true, ordernumber =1 WHERE servicecode='PGR' and url='/workflow/router/v1/_create';

	UPDATE eg_action SET displayname = 'View Router', servicecode = 'RUTR' , parentmodule = (select id from service where code='RUTR' and tenantid ='default'), enabled=true, ordernumber =2 WHERE servicecode='PGR' and url='/workflow/router/v1/_search';

	UPDATE eg_action SET displayname = 'Update Router', servicecode = 'RUTR' , parentmodule = (select id from service where code='RUTR' and tenantid ='default'), enabled=true, ordernumber =3 WHERE servicecode='PGR' and url='/workflow/router/v1/_update';
