--Updating tenantid to default
update eg_wf_types set tenantid = 'default' where type = 'LeaveApplication';
