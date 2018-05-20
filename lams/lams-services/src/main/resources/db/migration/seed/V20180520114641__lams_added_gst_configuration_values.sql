INSERT INTO eglams_lamsconfiguration(id, keyname, description, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantid)
VALUES (nextval('seq_eglams_lamsconfiguration'), 'lams_gst_effectivedate', 'GST effective date from  which date GST can be applied', 1, current_timestamp, 1, current_timestamp, 'default');

INSERT INTO eglams_lamsconfigurationvalues(id, keyid, value, effectivefrom, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantid)
VALUES (nextval('seq_eglams_lamsconfigurationvalues'),(select id from eglams_lamsconfiguration where keyname='lams_gst_effectivedate') , '01/07/2017', current_timestamp, 1,current_timestamp, 1, now(), 'default');

INSERT INTO eglams_lamsconfiguration(id, keyname, description, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantid)
VALUES (nextval('seq_eglams_lamsconfiguration'), 'lams_gst_rate', 'GST rate to be calculate on rent', 1, current_timestamp, 1, current_timestamp, 'default');

INSERT INTO eglams_lamsconfigurationvalues(id, keyid, value, effectivefrom, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantid)
VALUES (nextval('seq_eglams_lamsconfigurationvalues'),(select id from eglams_lamsconfiguration where keyname='lams_gst_rate') , '18', current_timestamp, 1,current_timestamp, 1, now(), 'default');

