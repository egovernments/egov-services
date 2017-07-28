update eg_action set url = '/workflow/escalation-hours/v1/_update' where url = '/workflow/escalation-hours/v1/_update/{id}' and servicecode = 'pgr';

update eg_action set url = '/pgr-master/receivingmode/v1/_update' where url = '/pgr-master/receivingmode/v1/{id}/_update' and servicecode = 'RCMD';

update eg_action set url = '/pgr-master/receivingcenter/v1/_update' where url = '/pgr-master/receivingcenter/v1/{id}/_update' and servicecode = 'RCVC';

update eg_action set url = '/pgr-master/serviceGroup/v1/_update' where url = '/pgr-master/serviceGroup/v1/{id}/_update' and servicecode = 'GRVCTG';

update eg_action set url = '/pgr-master/service/v1/_update' where url = '/pgr-master/service/v1/{code}/_update' and servicecode = 'GRVTYP';