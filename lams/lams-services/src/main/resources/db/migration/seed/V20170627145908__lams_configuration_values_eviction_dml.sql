
INSERT INTO eglams_lamsconfiguration(id, keyname, description, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantid)
VALUES (nextval('seq_eglams_lamsconfiguration'), 'lams_eviction_required_assetcategories', 'assetcategory name for which eviction can be done', 61, current_timestamp, 61, current_timestamp, 'ap.kurnool');

INSERT INTO eglams_lamsconfigurationvalues(id, keyid, value, effectivefrom, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantid)
VALUES (nextval('seq_eglams_lamsconfigurationvalues'),(select id from eglams_lamsconfiguration where keyname='lams_eviction_required_assetcategories') , 'shop', current_timestamp, 61, current_timestamp, 61, current_timestamp, 'ap.kurnool');
