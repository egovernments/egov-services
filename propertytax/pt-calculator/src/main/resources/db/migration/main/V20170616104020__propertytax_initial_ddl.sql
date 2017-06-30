CREATE TABLE egpt_mstr_factor
(
    id bigserial NOT NULL,
    tenantId character varying NOT NULL,
    factorCode character varying NOT NULL,
    factorType character varying NOT NULL,
    factorValue numeric NOT NULL,    
    fromdate timestamp with time zone NOT NULL,
    todate timestamp with time zone NOT NULL,
    createdby character varying,
    lastmodifiedby character varying,
    createdtime bigint,
    lastmodifiedtime bigint,
   CONSTRAINT pk_egpt_mstr_factor PRIMARY KEY(id)
);

CREATE SEQUENCE seq_egpt_mstr_factor;

ALTER TABLE egpt_mstr_factor ALTER COLUMN id SET DEFAULT nextval('seq_egpt_mstr_factor');



CREATE TABLE egpt_mstr_guidancevalue
(
    id bigserial NOT NULL,
    tenantid  character varying NOT NULL,
    name character varying,
    boundary character varying NOT NULL,
    structure character varying,
    usage character varying,
    subUsage character varying,
    occupancy character varying,
    value numeric NOT NULL,    
    fromdate timestamp with time zone NOT NULL,
    todate timestamp with time zone NOT NULL,
    createdby character varying,
    lastmodifiedby character varying,
    createdtime bigint,
    lastmodifiedtime bigint,
   CONSTRAINT pk_egpt_mstr_guidancevalue PRIMARY KEY(id)
);

CREATE SEQUENCE seq_egpt_mstr_guidancevalue;

ALTER TABLE egpt_mstr_guidancevalue ALTER COLUMN id SET DEFAULT nextval('seq_egpt_mstr_guidancevalue');


CREATE TABLE egpt_mstr_taxrates
(
    id bigserial NOT NULL,
    tenantid  character varying NOT NULL,
    taxHead character varying NOT NULL,
    dependentTaxHead character varying,    
    fromdate timestamp with time zone NOT NULL,
    todate timestamp with time zone NOT NULL,
    fromValue integer NOT NULL,
    toValue integer NOT NULL,
    ratePercentage numeric,
    taxFlatValue integer,
    createdby character varying,
    lastmodifiedby character varying,
    createdtime bigint,
    lastmodifiedtime bigint,
   CONSTRAINT pk_egpt_mstr_taxrates PRIMARY KEY(id)
);

CREATE SEQUENCE seq_egpt_mstr_taxrates;

ALTER TABLE egpt_mstr_taxrates ALTER COLUMN id SET DEFAULT nextval('seq_egpt_mstr_taxrates');



CREATE TABLE egpt_mstr_taxperiods
(
    id bigserial NOT NULL,
    tenantid  character varying NOT NULL,
    fromdate timestamp with time zone NOT NULL,
    todate timestamp with time zone NOT NULL,
    code character varying NOT NULL,
    periodType character varying NOT NULL,
    financialYear character varying,
    createdby character varying,
    lastmodifiedby character varying,
    createdtime bigint,
    lastmodifiedtime bigint,
   CONSTRAINT pk_egpt_mstr_taxperiods PRIMARY KEY(id)
);

CREATE SEQUENCE seq_egpt_mstr_taxperiods;

ALTER TABLE egpt_mstr_taxperiods ALTER COLUMN id SET DEFAULT nextval('seq_egpt_mstr_taxperiods');
