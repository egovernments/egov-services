insert into message (id,locale,code,message,tenantid,module,createdby)
values(nextval('SEQ_MESSAGE'),'en_IN','core.lbl.bulkcreated','Bulk Escalation Created / Update Successfully','default','default',1);


--update
update message set message = 'From Position' where code= 'pgr.lbl.fromposition' and module = 'PGR' and tenantid ='default';

