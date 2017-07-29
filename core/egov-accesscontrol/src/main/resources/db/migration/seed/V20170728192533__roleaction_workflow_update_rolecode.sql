update eg_roleaction set rolecode='SUPERUSER' where actionid=(select id from eg_action where name = 'designationsMSSearch') and rolecode='Super User';
update eg_roleaction set rolecode='SUPERUSER' where actionid=(select id from eg_action where name = 'processMSStart') and rolecode='Super User';
update eg_roleaction set rolecode='SUPERUSER' where actionid=(select id from eg_action where name = 'processMSEnd') and rolecode='Super User';
update eg_roleaction set rolecode='SUPERUSER' where actionid=(select id from eg_action where name = 'historyMS') and rolecode='Super User';
update eg_roleaction set rolecode='SUPERUSER' where actionid=(select id from eg_action where name = 'tasksMS') and rolecode='Super User';
update eg_roleaction set rolecode='SUPERUSER' where actionid=(select id from eg_action where name = 'tasksMSSearch') and rolecode='Super User';
update eg_roleaction set rolecode='SUPERUSER' where actionid=(select id from eg_action where name = 'processMSSearch') and rolecode='Super User';