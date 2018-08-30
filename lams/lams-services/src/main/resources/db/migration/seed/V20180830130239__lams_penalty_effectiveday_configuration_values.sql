
INSERT INTO eglams_lamsconfiguration(id, keyname, description, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantid)
VALUES (nextval('seq_eglams_lamsconfiguration'), 'lams_penalty_effectiveday', 'day from which penalty should be added', 1, current_timestamp, 1, current_timestamp, 'default');

INSERT INTO eglams_lamsconfigurationvalues(id, keyid, value, effectivefrom, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantid)
VALUES (nextval('seq_eglams_lamsconfigurationvalues'),(select id from eglams_lamsconfiguration where keyname='lams_penalty_effectiveday') , '10', current_timestamp, 1,current_timestamp, 1, now(), 'default');

