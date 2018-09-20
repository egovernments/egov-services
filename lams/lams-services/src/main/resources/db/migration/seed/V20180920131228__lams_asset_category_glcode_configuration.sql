---community hall
INSERT INTO eglams_lamsconfiguration(id, keyname, description, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantid)
VALUES (nextval('seq_eglams_lamsconfiguration'), 'COMMUNITYHALL_GLCODE', 'GlCode For Community Hall', 1, current_timestamp, 1, current_timestamp, 'default');

INSERT INTO eglams_lamsconfigurationvalues(id, keyid, value, effectivefrom, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantid)
VALUES (nextval('seq_eglams_lamsconfigurationvalues'),(select id from eglams_lamsconfiguration where keyname='COMMUNITYHALL_GLCODE' and tenantid='default') , '1301003', current_timestamp, 1,current_timestamp, 1, now(), 'default');

---toilet complex
INSERT INTO eglams_lamsconfiguration(id, keyname, description, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantid)
VALUES (nextval('seq_eglams_lamsconfiguration'), 'COMMUNITY_TOILETCOMPLEX_GLCODE', 'GlCode For Toilet Complex', 1, current_timestamp, 1, current_timestamp, 'default');

INSERT INTO eglams_lamsconfigurationvalues(id, keyid, value, effectivefrom, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantid)
VALUES (nextval('seq_eglams_lamsconfigurationvalues'),(select id from eglams_lamsconfiguration where keyname='COMMUNITY_TOILETCOMPLEX_GLCODE' and tenantid='default') , '1301027', current_timestamp, 1,current_timestamp, 1, now(), 'default');


---fish tanks
INSERT INTO eglams_lamsconfiguration(id, keyname, description, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantid)
VALUES (nextval('seq_eglams_lamsconfiguration'), 'FISHTANKS_GLCODE', 'GlCode For Fish Tanks', 1, current_timestamp, 1, current_timestamp, 'default');

INSERT INTO eglams_lamsconfigurationvalues(id, keyid, value, effectivefrom, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantid)
VALUES (nextval('seq_eglams_lamsconfigurationvalues'),(select id from eglams_lamsconfiguration where keyname='FISHTANKS_GLCODE' and tenantid='default') , '1301021', current_timestamp, 1,current_timestamp, 1, now(), 'default');

---kalyana mandapam
INSERT INTO eglams_lamsconfiguration(id, keyname, description, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantid)
VALUES (nextval('seq_eglams_lamsconfiguration'), 'KALYANAMANDAPAM_GLCODE', 'GlCode For Kalyana Mandapam', 1, current_timestamp, 1, current_timestamp, 'default');

INSERT INTO eglams_lamsconfigurationvalues(id, keyid, value, effectivefrom, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantid)
VALUES (nextval('seq_eglams_lamsconfigurationvalues'),(select id from eglams_lamsconfiguration where keyname='KALYANAMANDAPAM_GLCODE' and tenantid='default') , '1301003', current_timestamp, 1,current_timestamp, 1, now(), 'default');


---land
INSERT INTO eglams_lamsconfiguration(id, keyname, description, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantid)
VALUES (nextval('seq_eglams_lamsconfiguration'), 'LAND_GLCODE', 'GlCode For Land', 1, current_timestamp, 1, current_timestamp, 'default');

INSERT INTO eglams_lamsconfigurationvalues(id, keyid, value, effectivefrom, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantid)
VALUES (nextval('seq_eglams_lamsconfigurationvalues'),(select id from eglams_lamsconfiguration where keyname='LAND_GLCODE' and tenantid='default') , '1304001', current_timestamp, 1,current_timestamp, 1, now(), 'default');

---market
INSERT INTO eglams_lamsconfiguration(id, keyname, description, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantid)
VALUES (nextval('seq_eglams_lamsconfiguration'), 'MARKET_GLCODE', 'GlCode For Market', 1, current_timestamp, 1, current_timestamp, 'default');

INSERT INTO eglams_lamsconfigurationvalues(id, keyid, value, effectivefrom, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantid)
VALUES (nextval('seq_eglams_lamsconfigurationvalues'),(select id from eglams_lamsconfiguration where keyname='MARKET_GLCODE' and tenantid='default') , '1301001', current_timestamp, 1,current_timestamp, 1, now(), 'default');


---parking space
INSERT INTO eglams_lamsconfiguration(id, keyname, description, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantid)
VALUES (nextval('seq_eglams_lamsconfiguration'), 'PARKINGSPACE_GLCODE', 'GlCode For Parking Space', 1, current_timestamp, 1, current_timestamp, 'default');

INSERT INTO eglams_lamsconfigurationvalues(id, keyid, value, effectivefrom, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantid)
VALUES (nextval('seq_eglams_lamsconfigurationvalues'),(select id from eglams_lamsconfiguration where keyname='PARKINGSPACE_GLCODE' and tenantid='default') , '1301010', current_timestamp, 1,current_timestamp, 1, now(), 'default');

---parks
INSERT INTO eglams_lamsconfiguration(id, keyname, description, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantid)
VALUES (nextval('seq_eglams_lamsconfiguration'), 'PARKS_GLCODE', 'GlCode For Parks', 1, current_timestamp, 1, current_timestamp, 'default');

INSERT INTO eglams_lamsconfigurationvalues(id, keyid, value, effectivefrom, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantid)
VALUES (nextval('seq_eglams_lamsconfigurationvalues'),(select id from eglams_lamsconfiguration where keyname='PARKS_GLCODE' and tenantid='default') , '1301026', current_timestamp, 1,current_timestamp, 1, now(), 'default');

---shop
INSERT INTO eglams_lamsconfiguration(id, keyname, description, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantid)
VALUES (nextval('seq_eglams_lamsconfiguration'), 'SHOP_GLCODE', 'GlCode For Shop', 1, current_timestamp, 1, current_timestamp, 'default');

INSERT INTO eglams_lamsconfigurationvalues(id, keyid, value, effectivefrom, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantid)
VALUES (nextval('seq_eglams_lamsconfigurationvalues'),(select id from eglams_lamsconfiguration where keyname='SHOP_GLCODE' and tenantid='default') , '1301015', current_timestamp, 1,current_timestamp, 1, now(), 'default');

---shopping complex
INSERT INTO eglams_lamsconfiguration(id, keyname, description, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantid)
VALUES (nextval('seq_eglams_lamsconfiguration'), 'SHOPPINGCOMPLEX_GLCODE', 'GlCode For Shopping Complex', 1, current_timestamp, 1, current_timestamp, 'default');

INSERT INTO eglams_lamsconfigurationvalues(id, keyid, value, effectivefrom, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantid)
VALUES (nextval('seq_eglams_lamsconfigurationvalues'),(select id from eglams_lamsconfiguration where keyname='SHOPPINGCOMPLEX_GLCODE' and tenantid='default') , '1301015', current_timestamp, 1,current_timestamp, 1, now(), 'default');


---slaughter house
INSERT INTO eglams_lamsconfiguration(id, keyname, description, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantid)
VALUES (nextval('seq_eglams_lamsconfiguration'), 'SLAUGHTERHOUSE_GLCODE', 'GlCode For Slaughter House', 1, current_timestamp, 1, current_timestamp, 'default');

INSERT INTO eglams_lamsconfigurationvalues(id, keyid, value, effectivefrom, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantid)
VALUES (nextval('seq_eglams_lamsconfigurationvalues'),(select id from eglams_lamsconfiguration where keyname='SLAUGHTERHOUSE_GLCODE' and tenantid='default') , '1301007', current_timestamp, 1,current_timestamp, 1, now(), 'default');
