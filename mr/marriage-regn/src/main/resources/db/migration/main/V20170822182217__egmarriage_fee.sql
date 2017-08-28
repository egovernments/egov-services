CREATE TABLE egmr_fee
(
 id character varying(250) NOT NULL,
 feecriteria character varying(250) NOT NULL,
 fee numeric(12,2) NOT NULL,
 fromdate bigint NOT NULL,
 todate bigint NOT NULL,
 tenantid character varying(250) NOT NULL,
 createddate bigint NOT NULL,
 lastmodifieddate bigint NOT NULL,
 createdby character varying(64) NOT NULL,
 lastmodifiedby character varying(64) NOT NULL,
 
 CONSTRAINT pk_egmr_fee PRIMARY KEY (tenantid,id)
)

