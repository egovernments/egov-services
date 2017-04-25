INSERT INTO eglams_lamsconfiguration(
            id, keyname, description, createdby, createddate, lastmodifiedby, 
            lastmodifieddate, tenantid)
    VALUES (nextval('seq_eglams_lamsconfiguration'), 'FUND_CODE', 'Fund Code from financials', 61, current_timestamp, 61, 
            current_timestamp, 'ap.kurnool');

INSERT INTO eglams_lamsconfigurationvalues(
            id, keyid, value, effectivefrom, createdby, createddate, lastmodifiedby, 
            lastmodifieddate, tenantid)
    VALUES (nextval('seq_eglams_lamsconfigurationvalues'), (select id from eglams_lamsconfiguration where keyname='FUND_CODE') , '01', current_timestamp, 61, current_timestamp, 61, 
            current_timestamp, 'ap.kurnool');

INSERT INTO eglams_lamsconfiguration(
            id, keyname, description, createdby, createddate, lastmodifiedby, 
            lastmodifieddate, tenantid)
    VALUES (nextval('seq_eglams_lamsconfiguration'), 'MODULE_NAME', 'Module Name', 61, current_timestamp, 61, 
            current_timestamp, 'ap.kurnool');

INSERT INTO eglams_lamsconfigurationvalues(
            id, keyid, value, effectivefrom, createdby, createddate, lastmodifiedby, 
            lastmodifieddate, tenantid)
    VALUES (nextval('seq_eglams_lamsconfigurationvalues'), (select id from eglams_lamsconfiguration where keyname='MODULE_NAME') , 'Leases And Agreements', current_timestamp, 61, current_timestamp, 61, 
            current_timestamp, 'ap.kurnool');

INSERT INTO eglams_lamsconfiguration(
            id, keyname, description, createdby, createddate, lastmodifiedby, 
            lastmodifieddate, tenantid)
    VALUES (nextval('seq_eglams_lamsconfiguration'), 'FUNCTIONARY_CODE', 'Functionary Code from financials', 61, current_timestamp, 61, 
            current_timestamp, 'ap.kurnool');

INSERT INTO eglams_lamsconfigurationvalues(
            id, keyid, value, effectivefrom, createdby, createddate, lastmodifiedby, 
            lastmodifieddate, tenantid)
    VALUES (nextval('seq_eglams_lamsconfigurationvalues'), (select id from eglams_lamsconfiguration where keyname='FUNCTIONARY_CODE') , '1', current_timestamp, 61, current_timestamp, 61, 
            current_timestamp, 'ap.kurnool');

INSERT INTO eglams_lamsconfiguration(
            id, keyname, description, createdby, createddate, lastmodifiedby, 
            lastmodifieddate, tenantid)
    VALUES (nextval('seq_eglams_lamsconfiguration'), 'FUNDSOURCE_CODE', 'Fund Source Code from financials', 61, current_timestamp, 61, 
            current_timestamp, 'ap.kurnool');

INSERT INTO eglams_lamsconfigurationvalues(
            id, keyid, value, effectivefrom, createdby, createddate, lastmodifiedby, 
            lastmodifieddate, tenantid)
    VALUES (nextval('seq_eglams_lamsconfigurationvalues'), (select id from eglams_lamsconfiguration where keyname='FUNDSOURCE_CODE') , '01', current_timestamp, 61, current_timestamp, 61, 
            current_timestamp, 'ap.kurnool');

INSERT INTO eglams_lamsconfiguration(
            id, keyname, description, createdby, createddate, lastmodifiedby, 
            lastmodifieddate, tenantid)
    VALUES (nextval('seq_eglams_lamsconfiguration'), 'DEPARTMENT_CODE', 'Department Code', 61, current_timestamp, 61, 
            current_timestamp, 'ap.kurnool');

INSERT INTO eglams_lamsconfigurationvalues(
            id, keyid, value, effectivefrom, createdby, createddate, lastmodifiedby, 
            lastmodifieddate, tenantid)
    VALUES (nextval('seq_eglams_lamsconfigurationvalues'), (select id from eglams_lamsconfiguration where keyname='DEPARTMENT_CODE') , 'REV', current_timestamp, 61, current_timestamp, 61, 
            current_timestamp, 'ap.kurnool');

INSERT INTO eglams_lamsconfiguration(
            id, keyname, description, createdby, createddate, lastmodifiedby, 
            lastmodifieddate, tenantid)
    VALUES (nextval('seq_eglams_lamsconfiguration'), 'FUNCTION_CODE', 'Function Code from financials', 61, current_timestamp, 61, 
            current_timestamp, 'ap.kurnool');

INSERT INTO eglams_lamsconfigurationvalues(
            id, keyid, value, effectivefrom, createdby, createddate, lastmodifiedby, 
            lastmodifieddate, tenantid)
    VALUES (nextval('seq_eglams_lamsconfigurationvalues'), (select id from eglams_lamsconfiguration where keyname='FUNCTION_CODE') , '9100', current_timestamp, 61, current_timestamp, 61, 
            current_timestamp, 'ap.kurnool');

INSERT INTO eglams_lamsconfiguration(
            id, keyname, description, createdby, createddate, lastmodifiedby, 
            lastmodifieddate, tenantid)
    VALUES (nextval('seq_eglams_lamsconfiguration'), 'BOUNDARY_TYPE', 'Function Code from financials',61, current_timestamp, 61, 
            current_timestamp, 'ap.kurnool');

INSERT INTO eglams_lamsconfigurationvalues(
            id, keyid, value, effectivefrom, createdby, createddate, lastmodifiedby, 
            lastmodifieddate, tenantid)
    VALUES (nextval('seq_eglams_lamsconfigurationvalues'), (select id from eglams_lamsconfiguration where keyname='BOUNDARY_TYPE') , 'Ward', current_timestamp, 61, current_timestamp, 61, 
            current_timestamp, 'ap.kurnool');

INSERT INTO eglams_lamsconfiguration(
            id, keyname, description, createdby, createddate, lastmodifiedby, 
            lastmodifieddate, tenantid)
    VALUES (nextval('seq_eglams_lamsconfiguration'), 'SERVICE_CODE', 'Function Code from financials', 61, current_timestamp, 61, 
            current_timestamp, 'ap.kurnool');

INSERT INTO eglams_lamsconfigurationvalues(
            id, keyid, value, effectivefrom, createdby, createddate, lastmodifiedby, 
            lastmodifieddate, tenantid)
    VALUES (nextval('seq_eglams_lamsconfigurationvalues'), (select id from eglams_lamsconfiguration where keyname='SERVICE_CODE') , 'LAMS', current_timestamp, 61, current_timestamp,61, 
            current_timestamp, 'ap.kurnool');


INSERT INTO eglams_lamsconfiguration(id,keyname, description, createdby, createddate, lastmodifiedby,lastmodifieddate, tenantid)
VALUES (nextval('seq_eglams_lamsconfiguration'), 'lams_workflow_initiator_designation', 'designation name for initiator who creates the agreement', 61,current_timestamp, 61,current_timestamp, 'ap.kurnool');

INSERT INTO eglams_lamsconfigurationvalues(id, keyid, value, effectivefrom, createdby, createddate, lastmodifiedby,lastmodifieddate, tenantid)
VALUES (nextval('seq_eglams_lamsconfigurationvalues'),(select id from eglams_lamsconfiguration where keyname='lams_workflow_initiator_designation') , 'junior assistant', current_timestamp, 61, current_timestamp,61,current_timestamp,'ap.kurnool');

INSERT INTO eglams_lamsconfigurationvalues(id, keyid, value, effectivefrom, createdby, createddate, lastmodifiedby,lastmodifieddate, tenantid)
VALUES (nextval('seq_eglams_lamsconfigurationvalues'),(select id from eglams_lamsconfiguration where keyname='lams_workflow_initiator_designation') , 'senior assistant', current_timestamp, 61, current_timestamp,61,current_timestamp,'ap.kurnool');


INSERT INTO eglams_lamsconfiguration(id,keyname, description, createdby, createddate, lastmodifiedby,lastmodifieddate, tenantid)
VALUES (nextval('seq_eglams_lamsconfiguration'), 'lams_rentincrement_required_assetcategories', 'assetcategories for which rentincrement type is required', 61,current_timestamp, 61,current_timestamp, 'ap.kurnool');

INSERT INTO eglams_lamsconfigurationvalues(id, keyid, value, effectivefrom, createdby, createddate, lastmodifiedby,lastmodifieddate, tenantid)
VALUES (nextval('seq_eglams_lamsconfigurationvalues'),(select id from eglams_lamsconfiguration where keyname='lams_rentincrement_required_assetcategories') , 'land', current_timestamp, 61, current_timestamp,61,current_timestamp,'ap.kurnool');

INSERT INTO eglams_lamsconfigurationvalues(id, keyid, value, effectivefrom, createdby, createddate, lastmodifiedby,lastmodifieddate, tenantid)
VALUES (nextval('seq_eglams_lamsconfigurationvalues'),(select id from eglams_lamsconfiguration where keyname='lams_rentincrement_required_assetcategories') , 'shop', current_timestamp, 61, current_timestamp,61,current_timestamp,'ap.kurnool');




