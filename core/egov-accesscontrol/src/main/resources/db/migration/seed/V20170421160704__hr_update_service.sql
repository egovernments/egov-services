update service set displayname='Employee Management' ,contextroot='hr-web' where code='EIS';

update service set parentmodule=(select id from service where code='EIS') where code='EIS Masters';

insert into service (id, code, name, enabled, contextroot, displayname, ordernumber, parentmodule, tenantId) VALUES (nextval('SEQ_SERVICE'),'LEAVE-MGTMT', 'Leave Management', true, '/hr-leave', 'Leave Management', null, (select id from service where code='EIS'), 'default');
