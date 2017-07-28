update eg_roleaction set tenantid = 'default' where actionid = (select id from eg_action where url = '/workflow/history/v1/_search' and name = 'Get Workflow History' and servicecode = 'PGR') and tenantid  = 'ap.public';

update eg_action set url = '/workflow/v1/nextstatuses/_search' where  url = '/pgr/nextstatuses/v1/_search' and servicecode = 'PGR';