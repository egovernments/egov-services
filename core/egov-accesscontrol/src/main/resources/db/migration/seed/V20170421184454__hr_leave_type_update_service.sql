update service set parentmodule=(select id from service where name='EIS Masters') where name='Leave Type';
