update service set parentmodule=(select id from service where name='Leave Management') where name='Leave Application';
update eg_action set url='/app/hr/leavemaster/search-leave-application.html',queryparams='type=create' where name='Create Leave Application';
update eg_action set url='/leaveapplications/_create',queryparams=null,parentmodule=(select id from service where name='HR Leave') where name='Create Leave Applications';
update eg_action set url='/leaveapplications/{id}/_update',queryparams=null,parentmodule=(select id from service where name='HR Leave') where name='Leave Application Update';
update eg_action set url='/app/hr/leavemaster/search-leave-application.html',queryparams='type=view' where name='View Leave Application';
update eg_action set url='/leaveapplications/_search',queryparams=null,parentmodule=(select id from service where name='HR Leave') where name='Search Leave Application';

