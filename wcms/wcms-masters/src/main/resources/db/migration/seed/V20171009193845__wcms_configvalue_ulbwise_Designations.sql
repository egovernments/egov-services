Insert into egwtr_configuration (id, keyname, description, createdby, lastmodifiedby, createddate, lastmodifieddate, tenantid)
 values (nextval('seq_egwtr_configuration'), 
 'DESIGNATION_AFTER_APPROVE','Designation after Approve in ULB' ,1, 1, 1503170942971, 1503170942971, 'default');

Insert into egwtr_configurationvalues (id, keyid, value,effectivefrom,createdby, lastmodifiedby, createddate, 
lastmodifieddate, tenantid)
 values (nextval('seq_egwtr_configurationvalues'), (select id from egwtr_configuration where 
 keyname='DESIGNATION_AFTER_APPROVE' and tenantid='default'),'Clerk',1503170942971, 1, 1, 1503170942971, 1503170942971,'default');


Insert into egwtr_configuration (id, keyname, description, createdby, lastmodifiedby, createddate, lastmodifieddate, tenantid)
 values (nextval('seq_egwtr_configuration'), 
 'DESIGNATION_AFTER_WORKORDER','Designation after WorkOrder in ULB' ,1, 1, 1503170942971, 1503170942971, 'default');

Insert into egwtr_configurationvalues (id, keyid, value,effectivefrom,createdby, lastmodifiedby, createddate, 
lastmodifieddate, tenantid)
 values (nextval('seq_egwtr_configurationvalues'), (select id from egwtr_configuration where 
 keyname='DESIGNATION_AFTER_WORKORDER' and tenantid='default'),'junior Engineer',1503170942971, 1, 1, 1503170942971, 1503170942971,'default');