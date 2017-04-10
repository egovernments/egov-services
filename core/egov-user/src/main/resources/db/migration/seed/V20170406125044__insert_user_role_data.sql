insert into eg_userrole values((select id from eg_role where name='Employee'),(select id from eg_user where username='narasappa'));
insert into eg_userrole values((select id from eg_role where name='Employee'),(select id from eg_user where username='manas'));
insert into eg_userrole values((select id from eg_role where name='Employee'),(select id from eg_user where username='ramana'));
insert into eg_userrole values((select id from eg_role where name='Citizen'),(select id from eg_user where username='9999999999'));