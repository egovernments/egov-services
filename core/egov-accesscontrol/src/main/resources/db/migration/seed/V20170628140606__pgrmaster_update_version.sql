UPDATE eg_action SET url = '/pgr-master/service/v1/_create' WHERE url = 'pgr-master/service/v1/_create' AND name = 'Create a Service Type';

UPDATE eg_action SET url = '/pgr-master/service/v1/{code}/_update' WHERE url = 'pgr-master/service/v1/{code}/_update' AND name = 'Update a Service Type';

UPDATE eg_action SET url = '/pgr-master/service/v1/_search' WHERE url = 'pgr-master/service/v1/_search' AND name = 'Search a Service Type';

UPDATE eg_action SET url = '/pgr-master/serviceGroup/v1/_search' WHERE url = 'pgr-master/serviceGroup/v1/_search' AND name = 'Search a Service Group';
