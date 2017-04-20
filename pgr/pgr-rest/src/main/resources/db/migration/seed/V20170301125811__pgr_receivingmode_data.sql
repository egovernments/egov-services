insert into egpgr_receivingmode (id, name, code, visible, tenantid) values
(nextval('seq_egpgr_receivingmode'), 'Website', 'WEBSITE', false, 'ap.public'),
(nextval('seq_egpgr_receivingmode'), 'SMS', 'SMS', false, 'ap.public'),
(nextval('seq_egpgr_receivingmode'), 'Call', 'CALL', true, 'ap.public'),
(nextval('seq_egpgr_receivingmode'), 'Email', 'EMAIL', true, 'ap.public'),
(nextval('seq_egpgr_receivingmode'), 'Manual', 'MANUAL', true, 'ap.public'),
(nextval('seq_egpgr_receivingmode'), 'Mobile', 'MOBILE', false, 'ap.public');




