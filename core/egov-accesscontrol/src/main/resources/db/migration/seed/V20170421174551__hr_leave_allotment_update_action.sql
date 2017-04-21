update service set parentmodule=(select id from service where name='Leave Management') where name='Leave Mapping';
update eg_action set url='/app/hr/leavemaster/leave-mapping.html',queryparams=null where name='CreateLeave Mapping';
update eg_action set url='/leaveallotments/_create',queryparams=null,parentmodule=(select id from service where name='HR Leave') where name='CreateLeave Mappings';
update eg_action set url='/app/hr/common/show-leave-mapping.html',queryparams='type=update' where name='UpdateLeave Mapping';
update eg_action set url='/leaveallotments/_update',queryparams=null,parentmodule=(select id from service where name='HR Leave') where name='Leave MappingUpdate';
update eg_action set url='/app/hr/common/show-leave-mapping.html',queryparams='type=view' where name='ViewLeave Mapping';
update eg_action set url='/leaveallotments/_search',queryparams=null,parentmodule=(select id from service where name='HR Leave') where name='SearchLeave Mapping';



