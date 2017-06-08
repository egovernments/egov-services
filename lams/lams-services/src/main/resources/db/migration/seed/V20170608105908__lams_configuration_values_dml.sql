
INSERT INTO eglams_lamsconfiguration(id,keyname, description, createdby, createddate, lastmodifiedby,lastmodifieddate, tenantid)
VALUES (nextval('seq_eglams_lamsconfiguration'), 'lams_securitydeposit_factor', 'the factor by which security deposit is determined', 61,current_timestamp, 61,current_timestamp, 'ap.kurnool');

INSERT INTO eglams_lamsconfigurationvalues(id, keyid, value, effectivefrom, createdby, createddate, lastmodifiedby,lastmodifieddate, tenantid)
VALUES (nextval('seq_eglams_lamsconfigurationvalues'),(select id from eglams_lamsconfiguration where keyname='lams_securitydeposit_factor') , '3', current_timestamp, 61, current_timestamp,61,current_timestamp,'ap.kurnool');


INSERT INTO eglams_lamsconfiguration(id,keyname, description, createdby, createddate, lastmodifiedby,lastmodifieddate, tenantid)
VALUES (nextval('seq_eglams_lamsconfiguration'), 'lams_renewal_time_before_expirydate', 'the time period in months before expiry date from which renewal is allowed for any agreement', 61,current_timestamp, 61,current_timestamp, 'ap.kurnool');

INSERT INTO eglams_lamsconfigurationvalues(id, keyid, value, effectivefrom, createdby, createddate, lastmodifiedby,lastmodifieddate, tenantid)
VALUES (nextval('seq_eglams_lamsconfigurationvalues'),(select id from eglams_lamsconfiguration where keyname='lams_renewal_time_before_expirydate') , '3', current_timestamp, 61, current_timestamp,61,current_timestamp,'ap.kurnool');


INSERT INTO eglams_lamsconfiguration(id,keyname, description, createdby, createddate, lastmodifiedby,lastmodifieddate, tenantid)
VALUES (nextval('seq_eglams_lamsconfiguration'), 'lams_renewal_time_after_expirydate', 'the time period in months after expiry date from which renewal is allowed for any agreement', 61,current_timestamp, 61,current_timestamp, 'ap.kurnool');

INSERT INTO eglams_lamsconfigurationvalues(id, keyid, value, effectivefrom, createdby, createddate, lastmodifiedby,lastmodifieddate, tenantid)
VALUES (nextval('seq_eglams_lamsconfigurationvalues'),(select id from eglams_lamsconfiguration where keyname='lams_renewal_time_after_expirydate') , '3', current_timestamp, 61, current_timestamp,61,current_timestamp,'ap.kurnool');
