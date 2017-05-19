delete from service_status where code = 'DO';
delete from service_status where code = 'DC';

INSERT INTO service_status values(nextval('seq_service_status'),'NEW',0,'default','DSNEW');
INSERT INTO service_status values(nextval('seq_service_status'),'In Progress',0,'default','DSPROGRESS');
INSERT INTO service_status values(nextval('seq_service_status'),'Approved',0,'default','DSAPPROVED');
INSERT INTO service_status values(nextval('seq_service_status'),'Rejected',0,'default','DSREJECTED');