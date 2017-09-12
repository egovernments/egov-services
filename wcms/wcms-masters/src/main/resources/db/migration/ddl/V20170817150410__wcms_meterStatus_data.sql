Create sequence SEQ_EGWTR_METERSTATUS;

Create table egwtr_meterstatus
(
   id bigint NOT NULL,
   code character varying(50) NOT NULL,
   status character varying(100) NOT NULL,
   description character varying(124),
   createdby bigint NOT NULL,
   createddate bigint,
   lastmodifiedby bigint NOT NULL,
   lastmodifieddate bigint,
   tenantid character varying(250) NOT NULL,
   version numeric DEFAULT 0,
   CONSTRAINT pk_meterstatus PRIMARY KEY (id,tenantid),
   CONSTRAINT unq_meterstatus_code UNIQUE (code,tenantid)
);