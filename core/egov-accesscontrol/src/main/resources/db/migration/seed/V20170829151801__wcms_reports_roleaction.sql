update eg_action set enabled=false where name='WaterCharges Metadata';
update eg_action set enabled=false where name='WaterCharges Get';
update eg_action set enabled=false,parentmodule=(select id from service where code ='COLLECTION-REPORTS' and tenantid='default') where name='Citizen Metadata';
update eg_action set enabled=false,parentmodule=(select id from service where code ='COLLECTION-REPORTS' and tenantid='default') where name='Citizen Get';

