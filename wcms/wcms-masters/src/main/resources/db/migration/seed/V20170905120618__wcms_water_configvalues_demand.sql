Insert into egwtr_configuration (id, keyname, description, createdby, lastmodifiedby, createddate, lastmodifieddate, tenantid)
 values (nextval('seq_egwtr_configuration'), 
 'DEMAND_ANNUAL','Demand for Annual' ,1, 1, 1504569600000, 1504569600000, 'default');

Insert into egwtr_configurationvalues (id, keyid, value,effectivefrom,createdby, lastmodifiedby, createddate, 
lastmodifieddate, tenantid)
 values (nextval('seq_egwtr_configurationvalues'), (select id from egwtr_configuration where 
 keyname='DEMAND_ANNUAL' and tenantid='default'),'ANNUAL',1504569600000, 1, 1, 1504569600000, 1504569600000,'default');

 
Insert into egwtr_configuration (id, keyname, description, createdby, lastmodifiedby, createddate, lastmodifieddate, tenantid)
 values (nextval('seq_egwtr_configuration'), 
 'DEMAND_HALFYEAR','Demand for Half Year' ,1, 1, 1504569600000, 1504569600000, 'default');

Insert into egwtr_configurationvalues (id, keyid, value,effectivefrom,createdby, lastmodifiedby, createddate, 
lastmodifieddate, tenantid)
 values (nextval('seq_egwtr_configurationvalues'), (select id from egwtr_configuration where 
 keyname='DEMAND_HALFYEAR' and tenantid='default'),'HALFYEAR',1504569600000, 1, 1, 1504569600000, 1504569600000,'default');

 
Insert into egwtr_configuration (id, keyname, description, createdby, lastmodifiedby, createddate, lastmodifieddate, tenantid)
 values (nextval('seq_egwtr_configuration'), 
 'DEMAND_QUARTERLY','Demand for Quarterly' ,1, 1, 1504569600000, 1504569600000, 'default');

Insert into egwtr_configurationvalues (id, keyid, value,effectivefrom,createdby, lastmodifiedby, createddate, 
lastmodifieddate, tenantid)
 values (nextval('seq_egwtr_configurationvalues'), (select id from egwtr_configuration where 
 keyname='DEMAND_QUARTERLY' and tenantid='default'),'QUARTER',1504569600000, 1, 1, 1504569600000, 1504569600000,'default');

 Insert into egwtr_configuration (id, keyname, description, createdby, lastmodifiedby, createddate, lastmodifieddate, tenantid)
 values (nextval('seq_egwtr_configuration'), 
 'DEMAND_MONTHLY','Demand for Monthly' ,1, 1, 1504569600000, 1504569600000, 'default');

Insert into egwtr_configurationvalues (id, keyid, value,effectivefrom,createdby, lastmodifiedby, createddate, 
lastmodifieddate, tenantid)
 values (nextval('seq_egwtr_configurationvalues'), (select id from egwtr_configuration where 
 keyname='DEMAND_MONTHLY' and tenantid='default'),'MONTH',1504569600000, 1, 1, 1504569600000, 1504569600000,'default');

 Insert into egwtr_configuration (id, keyname, description, createdby, lastmodifiedby, createddate, lastmodifieddate, tenantid)
 values (nextval('seq_egwtr_configuration'), 
 'DEMAND_DEFAULT','Default Demand' ,1, 1, 1504569600000, 1504569600000, 'default');

Insert into egwtr_configurationvalues (id, keyid, value,effectivefrom,createdby, lastmodifiedby, createddate, 
lastmodifieddate, tenantid)
 values (nextval('seq_egwtr_configurationvalues'), (select id from egwtr_configuration where 
 keyname='DEMAND_DEFAULT' and tenantid='default'),'ANNUAL',1504569600000, 1, 1, 1504569600000, 1504569600000,'default');