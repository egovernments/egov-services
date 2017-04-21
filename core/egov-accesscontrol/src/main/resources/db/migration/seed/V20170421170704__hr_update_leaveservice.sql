update service set parentmodule=(select id from service where code='LEAVE-MGTMT') where code='Leave Mapping';
update service set parentmodule=(select id from service where code='LEAVE-MGTMT') where code='Leave Opening Balance';
update service set parentmodule=(select id from service where code='LEAVE-MGTMT') where code='Leave Application';