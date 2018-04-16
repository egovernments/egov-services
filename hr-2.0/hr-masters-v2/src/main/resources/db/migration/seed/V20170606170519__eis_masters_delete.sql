delete from egeis_group where name in ('State','Central','Local') and tenantid = 'default' ;
delete from egeis_recruitmenttype where name in ('Direct','Transfer','Compensatory') and tenantid='default' ;
delete from egeis_recruitmentMode where name in ('UPSC','Direct','Department Exams') and tenantid = 'default' ;
delete from egeis_recruitmentQuota where name in ('Sports','Ex-Serviceman','General') and tenantid = 'default' ;
delete from egeis_grade where name in ('A','B','G') and tenantid = 'default' ;