UPDATE service SET ordernumber =1 WHERE code = 'GRVCTG' AND name='Grievance Category' AND tenantid='default';
UPDATE service SET ordernumber =2 WHERE code = 'GRVTYP' AND name='Grievance type' AND tenantid='default';
UPDATE service SET ordernumber =3 WHERE code = 'RUTR' AND name='Router' AND tenantid='default';
UPDATE service SET ordernumber =4 WHERE code = 'ESCLT' AND name='Escalation Time' AND tenantid='default';
UPDATE service SET ordernumber =5 WHERE code = 'ESCL' AND name='Escalation' AND tenantid='default';
UPDATE service SET ordernumber =6 WHERE code = 'RCMD' AND name='Receiving Mode' AND tenantid='default';
UPDATE service SET ordernumber =7 WHERE code = 'RCVC' AND name='Receiving Center' AND tenantid='default';


UPDATE eg_action SET displayname = 'View Category' WHERE servicecode = 'GRVCTG' AND name='Search a Service Group';

