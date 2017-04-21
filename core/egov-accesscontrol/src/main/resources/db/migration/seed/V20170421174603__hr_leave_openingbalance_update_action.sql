update service set parentmodule=(select id from service where name='Leave Management'),displayname='Leave Opening Balance' where name='Leave Opening Balance';
update eg_action set url='/app/hr/leavemaster/pis.html',queryparams=null,parentmodule=(select id from service where name='Leave Opening Balance') where name='CreateLeave Opening Balance';
update eg_action set url='/leaveopeningbalances/_create',queryparams=null,parentmodule=(select id from service where name='HR Leave') where name='CreateLeave Opening Balances';
update eg_action set url='/app/hr/leavemaster/pis.html',parentmodule=(select id from service where name='Leave Opening Balance'), queryparams='type=update' where name='UpdateLeave Opening Balance';
update eg_action set url='/leaveopeningbalances/_update',queryparams=null,parentmodule=(select id from service where name='HR Leave') where name='Leave Opening BalanceUpdate';
update eg_action set url='/app/hr/leavemaster/pis.html',queryparams='type=view',parentmodule=(select id from service where name='Leave Opening Balance') where name='ViewLeave Opening Balance';
update eg_action set url='/leaveopeningbalances/_search',queryparams=null,parentmodule=(select id from service where name='HR Leave') where name='Search Leave Opening Balance';
