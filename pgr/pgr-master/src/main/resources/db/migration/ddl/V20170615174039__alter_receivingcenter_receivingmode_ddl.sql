delete from egpgr_receivingmode where id=1;
delete from egpgr_receivingmode where id=2;
delete from egpgr_receivingmode where id=3;
delete from egpgr_receivingmode where id=4;
delete from egpgr_receivingmode where id=5;
delete from egpgr_receivingmode where id=6;

delete from egpgr_receiving_center where id=1;
delete from egpgr_receiving_center where id=2;
delete from egpgr_receiving_center where id=3;
delete from egpgr_receiving_center where id=4;
delete from egpgr_receiving_center where id=5;
delete from egpgr_receiving_center where id=6;
delete from egpgr_receiving_center where id=7;
delete from egpgr_receiving_center where id=8;
delete from egpgr_receiving_center where id=9;
delete from egpgr_receiving_center where id=10;



ALTER TABLE egpgr_receiving_center DROP CONSTRAINT uk_receivingcenter_name_tenant;

ALTER TABLE egpgr_receiving_center 
ADD code character varying(100) NOT NULL,
ADD description character varying(250) NULL,
ADD active boolean DEFAULT true,
ADD createdby bigint,
ADD createddate timestamp without time zone,
ADD lastmodifiedby bigint,
ADD lastmodifieddate timestamp without time zone,
ADD CONSTRAINT un_receivingcenter_code UNIQUE (code, tenantid);

ALTER TABLE egpgr_receivingmode drop column visible;

ALTER TABLE egpgr_receivingmode 
ADD description character varying(250) NULL,
ADD active boolean DEFAULT true,
ADD createdby bigint,
ADD createddate timestamp without time zone,
ADD lastmodifiedby bigint,
ADD lastmodifieddate timestamp without time zone,
ADD channel varchar(255) NOT NULL,
ADD CONSTRAINT chk_receivingMode CHECK (channel IN ('WEB', 'MOBILE','WEB,MOBILE','MOBILE,WEB'));