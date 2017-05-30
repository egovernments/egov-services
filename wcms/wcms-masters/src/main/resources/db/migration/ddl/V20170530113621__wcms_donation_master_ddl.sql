---Donation Master
CREATE TABLE egwtr_donation
(
  id SERIAL,
  property_type bigint NOT NULL,
  usage_type bigint NOT NULL,
  category bigint NOT NULL,
  hsc_pipesize_max bigint,
  hsc_pipesize_min bigint,
  from_date date,
  to_date date,
  donation_amount character varying(10),
  active boolean NOT NULL,
  createddate timestamp without time zone ,
  lastmodifieddate timestamp without time zone,
  createdby bigint,
  lastmodifiedby bigint,
  tenantid CHARACTER VARYING(250) NOT NULL,
  version numeric DEFAULT 0,
  CONSTRAINT pk_donation PRIMARY KEY (id,tenantid),
  CONSTRAINT donation_unique UNIQUE (property_type, usage_type, category, hsc_pipesize_max, hsc_pipesize_min, from_date, to_date, tenantid )
);
CREATE SEQUENCE seq_donation;