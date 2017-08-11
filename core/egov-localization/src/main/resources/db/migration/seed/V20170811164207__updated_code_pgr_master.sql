update message set code = 'pgr.0028' where code = 'pgr.0006' and module = 'PGR' and tenantid = 'default';
update message set code = 'pgr.0023' where code = 'pgr.0013' and module = 'PGR' and tenantid = 'default';
update message set code = 'pgr.0025' where code = 'pgr.0015' and module = 'PGR' and tenantid = 'default';
update message set code = 'pgr.0027' where code = 'pgr.0016' and module = 'PGR' and tenantid = 'default';
update message set code = 'pgr.0024' where code = 'pgr.0014' and module = 'PGR' and tenantid = 'default';

--delete repeated message
delete from message where code ='pgr.0001' and module = 'PGR' and tenantid = 'default';