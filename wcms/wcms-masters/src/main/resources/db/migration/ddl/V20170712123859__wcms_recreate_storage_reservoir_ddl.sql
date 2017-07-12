Drop table if exists egwtr_storage_reservoir;

Drop SEQUENCE if exists seq_egwtr_storage_reservoir;

----create table
CREATE TABLE egwtr_storage_reservoir
(
 id bigint NOT NULL,
 code character varying(20) NOT NULL,
 name character varying(100) NOT NULL,
 reservoirtype character varying(100) NOT NULL,
 location character varying(20) NOT NULL,
 ward character varying(20) NOT NULL,
 zone character varying(20) NOT NULL,
 capacity double precision NOT NULL,
 noofsublines bigint ,
 noofmaindistributionlines bigint,
 noofconnection bigint,
 createddate timestamp without time zone,
 lastmodifieddate timestamp without time zone,
 createdby bigint,
 lastmodifiedby bigint,
 tenantid CHARACTER VARYING(250) NOT NULL,
 version numeric DEFAULT 0,
 CONSTRAINT pk_storagereservoir PRIMARY KEY (id,tenantid),
 CONSTRAINT unq_storagereservoir_name UNIQUE (name,tenantid),
 CONSTRAINT unq_storagereservoir_code UNIQUE (code,tenantid)
);

CREATE SEQUENCE seq_egwtr_storage_reservoir;
