UPDATE message SET message = 'First name must be below 100 characters' WHERE tenantid='default' and code='pgr.0044' and module='PGR';

UPDATE message SET message = 'Status in SLA' WHERE tenantid='default' and code='reports.pgr.status.issla' and module='PGR';
