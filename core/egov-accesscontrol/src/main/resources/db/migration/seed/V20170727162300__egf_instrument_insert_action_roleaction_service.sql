--Instrument
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES (nextval('seq_service'), 'Instrument', 'Instrument', false, '/egf-instrument', (select id from service where name='Financials'), 'Instrument', 2,'default');

--Instrument Transactions & Masters
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES (nextval('seq_service'), 'Instrument Masters', 'Instrument Masters', false, '/egf-instrument', (select id from service where name='Instrument'), 'Masters', 1,'default');
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES (nextval('seq_service'), 'Instrument Transactions', 'Instrument Transactions', false, '/egf-instrument', (select id from service where name='Instrument'), 'Transactions', 2,'default');

--Instrument Transactions
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES (nextval('seq_service'), 'Create Instrument', 'Create Instrument', false, '/egf-instrument', (select id from service where name='Instrument Transactions'), 'Instrument', 1,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Create Instrument'),'createInstrument',
'/egf-instrument/instruments/_create',null,(select id from service where name='Create Instrument'),1,'Create Instrument',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Create Instrument'),'updateInstrument',
'/egf-instrument/instruments/_update',null,(select id from service where name='Create Instrument'),2,'Modify Instrument',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Create Instrument'),'searchInstrument',
'/egf-instrument/instruments/_search',null,(select id from service where name='Create Instrument'),3,'Search Instrument',false,1,now(),1,now());

--Instrument Masters
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES (nextval('seq_service'), 'Instrument Account Code', 'Instrument Account Code', false, '/egf-instrument', (select id from service where name='Instrument Masters'), 'Instrument Account Code', 1,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Instrument Account Code'),'createInstrumentAccountCode',
'/egf-instrument/instrumentaccountcodes/_create',null,(select id from service where name='Instrument Account Code'),1,'Instrument Account Code',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Instrument Account Code'),'updateInstrumentAccountCode',
'/egf-instrument/instrumentaccountcodes/_update',null,(select id from service where name='Instrument Account Code'),2,'Instrument Account Code',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Instrument Account Code'),'searchInstrumentAccountCode',
'/egf-instrument/instrumentaccountcodes/_search',null,(select id from service where name='Instrument Account Code'),3,'Instrument Account Code',false,1,now(),1,now());

INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES (nextval('seq_service'), 'Instrument Type', 'Instrument Type', false, '/egf-instrument', (select id from service where name='Instrument Masters'), 'Instrument Types', 1,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Instrument Type'),'createInstrumentType',
'/egf-instrument/instrumenttypes/_create',null,(select id from service where name='Instrument Type'),1,'Create Instrument Type',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Instrument Type'),'updateInstrumentType',
'/egf-instrument/instrumenttypes/_update',null,(select id from service where name='Instrument Type'),2,'Modify Instrument Type',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Instrument Type'),'searchInstrumentType',
'/egf-instrument/instrumenttypes/_search',null,(select id from service where name='Instrument Type'),3,'Search Instrument Type',false,1,now(),1,now());


INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES (nextval('seq_service'), 'Surrender Reason', 'Surrender Reason', false, '/egf-instrument', (select id from service where name='Instrument Masters'), 'Surrender Reason', 1,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Surrender Reason'),'createSurrenderReason',
'/egf-instrument/surrenderreasons/_create',null,(select id from service where name='Surrender Reason'),1,'Create Surrender Reasons',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Surrender Reason'),'updateSurrenderReason',
'/egf-instrument/surrenderreasons/_update',null,(select id from service where name='Surrender Reason'),2,'Modify Surrender Reasons',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Surrender Reason'),'searchSurrenderReason',
'/egf-instrument/surrenderreasons/_search',null,(select id from service where name='Surrender Reason'),3,'Search Surrender Reasons',false,1,now(),1,now());


insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'createInstrument'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'updateInstrument'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'searchInstrument'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'createInstrumentAccountCode'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'updateInstrumentAccountCode'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'searchInstrumentAccountCode'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'createInstrumentType'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'updateInstrumentType'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'searchInstrumentType'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'createSurrenderReason'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'updateSurrenderReason'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'searchSurrenderReason'),'default');