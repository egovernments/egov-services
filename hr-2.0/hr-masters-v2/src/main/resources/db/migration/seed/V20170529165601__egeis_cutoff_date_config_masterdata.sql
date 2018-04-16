insert into egeis_hrConfiguration(id,keyname,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) values(nextval('seq_egeis_hrConfiguration'),'Cutoff_Date','Define the cut off date after which the application will go live',1,now(),1,now(),'default');

insert into egeis_hrConfigurationValues(id,keyid,value,effectivefrom,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)
values(nextval('seq_egeis_hrConfigurationValues'),(select id from egeis_hrconfiguration where keyname='Cutoff_Date' and tenantid='default'),'2017-05-31',
'2016-01-01',1,now(),1,now(),'default');