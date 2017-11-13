update service set enabled='true' where code in ('WMS','AbstractEstimate') and tenantId='default';
update eg_action set enabled='true' where name in ('Estimate Create', 'Estimate Update', 'Estimate Search');
