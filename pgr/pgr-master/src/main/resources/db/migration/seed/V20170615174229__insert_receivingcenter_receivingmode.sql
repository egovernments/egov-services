insert into egpgr_receivingmode (id, name, code, channel, tenantid) values
(nextval('seq_egpgr_receivingmode'), 'Website', 'WEBSITE', 'WEB', 'default'),
(nextval('seq_egpgr_receivingmode'), 'SMS', 'SMS', 'WEB', 'default'),
(nextval('seq_egpgr_receivingmode'), 'Call', 'CALL', 'WEB', 'default'),
(nextval('seq_egpgr_receivingmode'), 'Email', 'EMAIL', 'WEB', 'default'),
(nextval('seq_egpgr_receivingmode'), 'Manual', 'MANUAL', 'WEB', 'default'),
(nextval('seq_egpgr_receivingmode'), 'Mobile', 'MOBILE', 'WEB', 'default');


INSERT INTO egpgr_receiving_center (id, code, name, iscrnrequired, orderno, version, tenantid) VALUES (nextval('seq_egpgr_receiving_center'), 'Complaint Cell','Complaint Cell', true, 8, 0, 'default');
INSERT INTO egpgr_receiving_center (id, code, name, iscrnrequired, orderno, version, tenantid) VALUES (nextval('seq_egpgr_receiving_center'), 'Mayor/Chairperson Office','Mayor/Chairperson Office', true, 2,0, 'default');
INSERT INTO egpgr_receiving_center (id, code, name, iscrnrequired, orderno, version, tenantid) VALUES (nextval('seq_egpgr_receiving_center'), 'Zonal Office','Zonal Office', true, 7,0, 'default');
INSERT INTO egpgr_receiving_center (id, code, name, iscrnrequired, orderno, version, tenantid) VALUES (nextval('seq_egpgr_receiving_center'), 'Commissioner Office','Commissioner Office', true, 6,0, 'default');
INSERT INTO egpgr_receiving_center (id, code, name, iscrnrequired, orderno, version, tenantid) VALUES (nextval('seq_egpgr_receiving_center'), 'CM Office','CM Office', true, 1,0, 'default');
INSERT INTO egpgr_receiving_center (id, code, name, iscrnrequired, orderno, version, tenantid) VALUES (nextval('seq_egpgr_receiving_center'), 'Field visits','Field visits',true,9,0, 'default');
INSERT INTO egpgr_receiving_center (id, code, name, iscrnrequired, orderno, version, tenantid) VALUES (nextval('seq_egpgr_receiving_center'), 'Adverse Items(Paper/news)','Adverse Items(Paper/news)',true,10,0, 'default');
INSERT INTO egpgr_receiving_center (id, code, name, iscrnrequired, orderno, version, tenantid) VALUES (nextval('seq_egpgr_receiving_center'), 'Public Representatives','Public Representatives',true,3,0, 'default');
INSERT INTO egpgr_receiving_center (id, code, name, iscrnrequired, orderno, version, tenantid) VALUES (nextval('seq_egpgr_receiving_center'), 'Office of CDMA','Office of CDMA',true,4,0, 'default');
INSERT INTO egpgr_receiving_center (id, code, name, iscrnrequired, orderno, version, tenantid) VALUES (nextval('seq_egpgr_receiving_center'), 'Office of RDMA','Office of RDMA',true,5,0, 'default');