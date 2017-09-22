DELETE FROM egwtr_metercost where code = 'KRANTI' and tenantid = 'default';
DELETE FROM egwtr_metercost where code = 'CAPSTAN' and tenantid = 'default';


INSERT INTO egwtr_metercost (id, code, metermake, amount,active,createdDate,lastModifiedDate,createdBy,lastModifiedBy,tenantid,version) 
VALUES (nextval('seq_egwtr_meter_cost'),'KRANTI','Kranti',0,true,(SELECT EXTRACT(EPOCH FROM TIMESTAMP  '2017-09-22')),(SELECT EXTRACT(EPOCH FROM TIMESTAMP  '2017-09-22')),1, 1,'default',0);

INSERT INTO egwtr_metercost (id, code, metermake, amount,active,createdDate,lastModifiedDate,createdBy,lastModifiedBy,tenantid,version)
VALUES (nextval('seq_egwtr_meter_cost'),'CAPSTAN','Capstan',0,true,(SELECT EXTRACT(EPOCH FROM TIMESTAMP  '2017-09-22')),(SELECT EXTRACT(EPOCH FROM TIMESTAMP  '2017-09-22')),1, 1,'default',0);