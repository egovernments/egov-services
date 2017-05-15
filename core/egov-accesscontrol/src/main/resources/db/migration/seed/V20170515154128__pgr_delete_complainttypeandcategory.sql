DELETE from eg_roleaction where actionid in(select id from eg_action where name='Get all CompaintTypeCategory' and url='/pgr/complaintTypeCategories');
DELETE from eg_action where name='Get all CompaintTypeCategory' and url='/pgr/complaintTypeCategories';

DELETE from eg_roleaction where actionid in(select id from eg_action where name='Get ComplaintType by type,count and tenantId' and url='/pgr/services');
DELETE from eg_action where name='Get ComplaintType by type,count and tenantId' and url='/pgr/services';

DELETE from eg_roleaction where actionid in(select id from eg_action where name='Get ComplaintType by type,categoryId and tenantId' and url='/pgr/services');
DELETE from eg_action where name='Get ComplaintType by type,categoryId and tenantId' and url='/pgr/services';

DELETE from eg_roleaction where actionid in(select id from eg_action where name='Get ComplaintType by type and tenantId' and url='/pgr/services');
DELETE from eg_action where name='Get ComplaintType by type and tenantId' and url='/pgr/services';