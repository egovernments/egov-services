INSERT INTO eglams_lamsconfiguration(id, keyname, description, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantid)
VALUES (nextval('seq_eglams_lamsconfiguration'), 'lams_remission_assetcategories', 'assetcategory name for which remission can be done', 1, current_timestamp, 1, current_timestamp, 'default');

INSERT INTO eglams_lamsconfigurationvalues(id, keyid, value, effectivefrom, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantid)
VALUES (nextval('seq_eglams_lamsconfigurationvalues'),(select id from eglams_lamsconfiguration where keyname='lams_remission_assetcategories') , 'Market', current_timestamp, 1,current_timestamp, 1, now(), 'default');
