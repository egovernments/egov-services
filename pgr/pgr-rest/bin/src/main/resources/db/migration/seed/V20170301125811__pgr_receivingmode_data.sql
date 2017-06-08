insert into egpgr_receivingmode (id, name, code, visible, tenantid) values
(nextval('seq_egpgr_receivingmode'), 'Website', 'WEBSITE', false, 'default'),
(nextval('seq_egpgr_receivingmode'), 'SMS', 'SMS', false, 'default'),
(nextval('seq_egpgr_receivingmode'), 'Call', 'CALL', true, 'default'),
(nextval('seq_egpgr_receivingmode'), 'Email', 'EMAIL', true, 'default'),
(nextval('seq_egpgr_receivingmode'), 'Manual', 'MANUAL', true, 'default'),
(nextval('seq_egpgr_receivingmode'), 'Mobile', 'MOBILE', false, 'default');




