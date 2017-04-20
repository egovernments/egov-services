delete from eg_roleaction where actionid in (select id from eg_action where servicecode='EIS');

delete from eg_action where  servicecode='EIS';

delete from service where contextroot='eis';