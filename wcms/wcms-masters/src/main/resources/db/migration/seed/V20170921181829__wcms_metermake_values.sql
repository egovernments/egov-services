INSERT INTO egwtr_metercost (id, code, metermake, amount,active,createdDate,lastModifiedDate,createdBy,lastModifiedBy,tenantid,version) 
VALUES (nextval('seq_egwtr_gapcode'),'KRANTI','Kranti',0,true,(SELECT EXTRACT(EPOCH FROM TIMESTAMP  '2017-09-21')),(SELECT EXTRACT(EPOCH FROM TIMESTAMP  '2017-09-21')),1, 1,'default',0);

INSERT INTO egwtr_metercost (id, code, metermake, amount,active,createdDate,lastModifiedDate,createdBy,lastModifiedBy,tenantid,version)
VALUES (nextval('seq_egwtr_gapcode'),'CAPSTAN','Capstan',0,true,(SELECT EXTRACT(EPOCH FROM TIMESTAMP  '2017-09-21')),(SELECT EXTRACT(EPOCH FROM TIMESTAMP  '2017-09-21')),1, 1,'default',0);