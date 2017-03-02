insert into egpgr_receivingmode (id, name, code, visible) values
(nextval('seq_egpgr_receivingmode'), 'Website', 'WEBSITE', false),
(nextval('seq_egpgr_receivingmode'), 'SMS', 'SMS', false),
(nextval('seq_egpgr_receivingmode'), 'Call', 'CALL', true),
(nextval('seq_egpgr_receivingmode'), 'Email', 'EMAIL', true),
(nextval('seq_egpgr_receivingmode'), 'Manual', 'MANUAL', true),
(nextval('seq_egpgr_receivingmode'), 'Mobile', 'MOBILE', false);




