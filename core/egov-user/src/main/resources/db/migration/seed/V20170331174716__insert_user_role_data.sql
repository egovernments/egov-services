insert into eg_role (id,name,description,createddate,createdby,lastmodifiedby,lastmodifieddate,version)values
(nextval('SEQ_EG_ROLE'),'Citizen','Citizen who can raise complaint',now(),1,1,now(),0);
insert into eg_role (id,name,description,createddate,createdby,lastmodifiedby,lastmodifieddate,version)values
(nextval('SEQ_EG_ROLE'),'Employee','Default role for all employees',now(),1,1,now(),0);
insert into eg_role (id,name,description,createddate,createdby,lastmodifiedby,lastmodifieddate,version)values
(nextval('SEQ_EG_ROLE'),'Super User','System Administrator. Can change all master data and has access to all the system screens.',now(),1,1,now(),0);
insert into eg_role (id,name,description,createddate,createdby,lastmodifiedby,lastmodifieddate,version)values
(nextval('SEQ_EG_ROLE'),'Grievance Officer','Heads the grivance cell. Also all complaints that cannot be routed based on the rules are routed to Grievance Officer.',now(),1,1,now(),0);
insert into eg_role (id,name,description,createddate,createdby,lastmodifiedby,lastmodifieddate,version)values
(nextval('SEQ_EG_ROLE'),'Redressal Officer','Employees that address citizens grievances.',now(),1,1,now(),0);
insert into eg_role (id,name,description,createddate,createdby,lastmodifiedby,lastmodifieddate,version)values
(nextval('SEQ_EG_ROLE'),'Grivance Administrator','System Administator for PGR. Can change PGR Master data only.',now(),1,1,now(),0);
insert into eg_role (id,name,description,createddate,createdby,lastmodifiedby,lastmodifieddate,version)values
(nextval('SEQ_EG_ROLE'),'Grievance Routing Officer','Grievance Routing Officer',now(),1,1,now(),0);
