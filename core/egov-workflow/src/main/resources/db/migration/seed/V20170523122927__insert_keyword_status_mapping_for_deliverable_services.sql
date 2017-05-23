DELETE FROM keyword_service_status where tenantid = 'default' and servicestatuscode = 'DO';
DELETE FROM keyword_service_status where tenantid = 'default' and servicestatuscode = 'DC';

INSERT INTO keyword_service_status (keyword,tenantId,servicestatuscode,version,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values('Deliverable_service','default','DSNEW',0,0,now(),0,now());
INSERT INTO keyword_service_status (keyword,tenantId,servicestatuscode,version,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values('Deliverable_service','default','DSPROGRESS',0,0,now(),0,now());
INSERT INTO keyword_service_status (keyword,tenantId,servicestatuscode,version,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values('Deliverable_service','default','DSAPPROVED',0,0,now(),0,now());
INSERT INTO keyword_service_status (keyword,tenantId,servicestatuscode,version,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values('Deliverable_service','default','DSREJECTED',0,0,now(),0,now());
