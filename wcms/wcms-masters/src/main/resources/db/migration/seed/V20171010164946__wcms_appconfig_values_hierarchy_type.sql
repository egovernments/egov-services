Insert into egwtr_configuration (id, keyname, description, createdby, lastmodifiedby, createddate, lastmodifieddate, tenantid)
 values (nextval('seq_egwtr_configuration'), 
 'HIERACHYTYPEFORWC','HierarchyType for ULB' ,1, 1, (SELECT EXTRACT(EPOCH FROM TIMESTAMP  '2017-10-10')), (SELECT EXTRACT(EPOCH FROM TIMESTAMP  '2017-10-10')), 'default');

Insert into egwtr_configurationvalues (id, keyid, value,effectivefrom,createdby, lastmodifiedby, createddate, 
lastmodifieddate, tenantid)
 values (nextval('seq_egwtr_configurationvalues'), (select id from egwtr_configuration where 
 keyname='HIERACHYTYPEFORWC' and tenantid='default'),'REVENUE',(SELECT EXTRACT(EPOCH FROM TIMESTAMP  '2017-10-10')), 1, 1, (SELECT EXTRACT(EPOCH FROM TIMESTAMP  '2017-10-10')), (SELECT EXTRACT(EPOCH FROM TIMESTAMP  '2017-10-10')),'default');