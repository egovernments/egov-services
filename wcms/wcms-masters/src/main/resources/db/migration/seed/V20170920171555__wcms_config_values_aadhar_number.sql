Insert into egwtr_configuration (id, keyname, description, createdby, lastmodifiedby, createddate, lastmodifieddate, tenantid)
 values (nextval('seq_egwtr_configuration'), 
 'AADHRANUMBER','Aadhar Number required' ,1, 1, 1503170942971, 1503170942971, 'default');

Insert into egwtr_configurationvalues (id, keyid, value,effectivefrom,createdby, lastmodifiedby, createddate, 
lastmodifieddate, tenantid)
 values (nextval('seq_egwtr_configurationvalues'), (select id from egwtr_configuration where 
 keyname='AADHRANUMBER' and tenantid='default'),'NO',1503170942971, 1, 1, 1503170942971, 1503170942971,'default');