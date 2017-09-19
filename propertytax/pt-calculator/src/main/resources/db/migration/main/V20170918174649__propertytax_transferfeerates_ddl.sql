CREATE TABLE egpt_mstr_transferfeerates
(
   id bigserial NOT NULL,
   tenantid  character varying(128) NOT NULL,
   feefactor character varying NOT NULL,
   fromdate timestamp with time zone NOT NULL,
   todate timestamp with time zone ,
   fromvalue numeric NOT NULL,
   tovalue numeric NOT NULL,
   feepercentage numeric,
   flatvalue numeric,
   createdby character varying,
   lastmodifiedby character varying,
   createdtime bigint,
   lastmodifiedtime bigint,
  CONSTRAINT pk_egpt_mstr_transferfeerates PRIMARY KEY(id)
);

CREATE SEQUENCE seq_egpt_mstr_transferfeerates;

ALTER TABLE egpt_mstr_transferfeerates ALTER COLUMN id SET DEFAULT nextval('seq_egpt_mstr_transferfeerates');
