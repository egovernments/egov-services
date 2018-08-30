
INSERT INTO eglams_lamsconfiguration(id, keyname, description, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantid)
VALUES (nextval('seq_eglams_lamsconfiguration'), 'lams_penalty_percentage', 'percentage of penalty to calculate on rent', 1, current_timestamp, 1, current_timestamp, 'default');

INSERT INTO eglams_lamsconfigurationvalues(id, keyid, value, effectivefrom, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantid)
VALUES (nextval('seq_eglams_lamsconfigurationvalues'),(select id from eglams_lamsconfiguration where keyname='lams_penalty_percentage') , '2', current_timestamp, 1,current_timestamp, 1, now(), 'default');


