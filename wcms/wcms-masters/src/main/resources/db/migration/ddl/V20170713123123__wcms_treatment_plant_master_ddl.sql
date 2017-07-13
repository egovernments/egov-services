CREATE TABLE egwtr_treatment_plant
(
 id bigint NOT NULL,
 code character varying(20) NOT NULL,
 name character varying(100) NOT NULL,
 planttype character varying(20) NOT NULL,
 location character varying(20) NOT NULL,
 ward character varying(20) NOT NULL,
 zone character varying(20) NOT NULL,
 capacity double precision NOT NULL,
 storagereservoirid bigint NOT NULL,
 description character varying(250) ,
 createddate timestamp without time zone,
 lastmodifieddate timestamp without time zone,
 createdby bigint,
 lastmodifiedby bigint,
 tenantid CHARACTER VARYING(250) NOT NULL,
 version numeric DEFAULT 0,
 CONSTRAINT pk_treatmentplant PRIMARY KEY (id,tenantid),
 CONSTRAINT unq_treatmentplant_name UNIQUE (name,tenantid),
 CONSTRAINT unq_treatmentplant_code UNIQUE (code,tenantid),
 CONSTRAINT fk_storagereservoirid FOREIGN KEY (storagereservoirid,tenantid) REFERENCES egwtr_storage_reservoir (id,tenantid) 
);

CREATE SEQUENCE seq_egwtr_treatment_plant;