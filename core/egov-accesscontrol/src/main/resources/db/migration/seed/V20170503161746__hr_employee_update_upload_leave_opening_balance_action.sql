update eg_action set queryparams=null,parentmodule=(select id from service where name='Leave Opening Balance') where name='Upload Leave Opening Balance';
