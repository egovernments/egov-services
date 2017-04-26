DELETE from eg_roleaction where actionid in(select id from eg_action where name='Seva' and url='/pgr/seva');

DELETE from eg_action where name='Seva' and url='/pgr/seva';