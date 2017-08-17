insert into message (id,locale,code,message,tenantid,module,createdby)
values(nextval('SEQ_MESSAGE'),'en_IN','pgr.0015','Description must have minimum 10 and max 1000 characters','default','PGR',1);

insert into message (id,locale,code,message,tenantid,module,createdby)
values(nextval('SEQ_MESSAGE'),'en_IN','pgr.0016','Email pattern is not valid','default','PGR',1);

insert into message (id,locale,code,message,tenantid,module,createdby)
values(nextval('SEQ_MESSAGE'),'en_IN','pgr.0017','Status is required','default','PGR',1);

insert into message (id,locale,code,message,tenantid,module,createdby)
values(nextval('SEQ_MESSAGE'),'en_IN','pgr.0018','Phone is required','default','PGR',1);

insert into message (id,locale,code,message,tenantid,module,createdby)
values(nextval('SEQ_MESSAGE'),'en_IN','pgr.0041','External CRN is required when Receiving Mode is Manual','default','PGR',1);

insert into message (id,locale,code,message,tenantid,module,createdby)
values(nextval('SEQ_MESSAGE'),'en_IN','pgr.042','Location ID is required','default','PGR',1);

insert into message (id,locale,code,message,tenantid,module,createdby)
values(nextval('SEQ_MESSAGE'),'en_IN','pgr.0021','Child Location ID is required','default','PGR',1);

insert into message (id,locale,code,message,tenantid,module,createdby)
values(nextval('SEQ_MESSAGE'),'en_IN','pgr.0022','Position ID is required','default','PGR',1);

insert into message (id,locale,code,message,tenantid,module,createdby)
values(nextval('SEQ_MESSAGE'),'en_IN','pgr.0043','Approval Comments is required','default','PGR',1);

insert into message (id,locale,code,message,tenantid,module,createdby)
values(nextval('SEQ_MESSAGE'),'en_IN','pgr.0026','Keyword is required','default','PGR',1);

insert into message (id,locale,code,message,tenantid,module,createdby)
values(nextval('SEQ_MESSAGE'),'en_IN','pgr.0045','Receiving Mode is required','default','PGR',1);

insert into message (id,locale,code,message,tenantid,module,createdby)
values(nextval('SEQ_MESSAGE'),'en_IN','pgr.0030','Rating is required','default','PGR',1);

insert into message (id,locale,code,message,tenantid,module,createdby)
values(nextval('SEQ_MESSAGE'),'en_IN','pgr.0044','First name must be below 20 characters','default','PGR',1);

insert into message (id,locale,code,message,tenantid,module,createdby)
values(nextval('SEQ_MESSAGE'),'en_IN','pgr.0029','State ID is required','default','PGR',1);
----------------------------------------------------------------------------------------------
insert into message (id,locale,code,message,tenantid,module,createdby)
values(nextval('SEQ_MESSAGE'),'en_IN','core.lbl.urmapping','User Role Mapping','default','default',1);

insert into message (id,locale,code,message,tenantid,module,createdby)
values(nextval('SEQ_MESSAGE'),'en_IN','core.lbl.username','User name','default','default',1);

insert into message (id,locale,code,message,tenantid,module,createdby)
values(nextval('SEQ_MESSAGE'),'en_IN','core.lbl.urinfo','User Role Information','default','PGR',1);

insert into message (id,locale,code,message,tenantid,module,createdby)
values(nextval('SEQ_MESSAGE'),'en_IN','core.lbl.allroles','All Roles','default','default',1);

insert into message (id,locale,code,message,tenantid,module,createdby)
values(nextval('SEQ_MESSAGE'),'en_IN','core.lbl.assgnedroles','Assigned Roles','default','default',1);
----------------------------------------------------------------------------------------------

--update
update message set message = 'Employee Name' where code= 'reports.pgr.functionary.name' and module = 'PGR' and tenantid ='default';

