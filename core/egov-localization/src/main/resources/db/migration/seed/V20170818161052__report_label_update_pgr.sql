update message set message = 'Routed To' where code = 'reports.pgr.position.name' and module = 'PGR' and tenantid = 'default';
update message set message = 'First Escalation' where code = 'reports.pgr.position1.name' and module = 'PGR' and tenantid = 'default';
update message set message = 'Second Escalation' where code = 'reports.pgr.position2.name' and module = 'PGR' and tenantid = 'default';
update message set message = 'Third Escalation' where code = 'reports.pgr.position3.name' and module = 'PGR' and tenantid = 'default';
update message set message = 'Grievance Type' where code = 'reports.pgr.complainttype.name' and module = 'PGR' and tenantid = 'default';

update message set message = 'Grievance Category name already exists' where code = 'pgr.0059' and module = 'PGR' and tenantid = 'default';
update message set message = 'Grievance Category code already exists' where code = 'pgr.0058' and module = 'PGR' and tenantid = 'default';
update message set message = 'Grievance Category code is required' where code = 'pgr.0056' and module = 'PGR' and tenantid = 'default';
update message set message = 'Grievance Category name is required' where code = 'pgr.0055' and module = 'PGR' and tenantid = 'default';
