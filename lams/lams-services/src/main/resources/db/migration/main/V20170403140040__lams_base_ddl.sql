Create sequence seq_eglams_agreement;
Create sequence seq_eglams_rentincrement;
Create sequence seq_eglams_acknowledgementnumber;
Create sequence seq_eglams_agreementnumber;
Create sequence seq_eglams_demand;

CREATE TABLE eglams_rentincrementtype
(
  id bigint NOT NULL,
  type character varying(64) NOT NULL,
  asset_category bigint NOT NULL, 
  flat_amount decimal(12,2) NOT NULL, 
  fromdate timestamp without time zone NOT NULL, 
  todate timestamp without time zone NOT NULL, 
  percentage decimal (5,2) NOT NULL, 
  created_by character varying(64),
  created_date timestamp without time zone,
  last_modified_by character varying(64),
  last_modified_date timestamp without time zone,
  CONSTRAINT pk_eglams_rentincrementtype PRIMARY KEY (id)
);

CREATE TABLE eglams_agreement
(
  id bigint NOT NULL,
  agreement_date timestamp without time zone,
  agreement_no character varying(64),
  bank_guarantee_amount numeric(12,2),
  bank_guarantee_date timestamp without time zone,
  case_no character varying(64),
  commencement_date timestamp without time zone NOT NULL,
  council_date timestamp without time zone,
  council_number character varying(64),
  expiry_date timestamp without time zone,
  nature_of_allotment character varying(16),
  order_date timestamp without time zone,
  order_details character varying(256),
  order_no character varying(64),
  payment_cycle character varying(16) NOT NULL,
  registration_fee numeric(12,2),
  remarks character varying(256),
  rent numeric(12,2) NOT NULL,
  rr_reading_no character varying(64),
  security_deposit numeric(12,2),
  security_deposit_date timestamp without time zone,
  solvency_certificate_date timestamp without time zone,
  solvency_certificate_no character varying(64),
  status character varying(32) NOT NULL,
  tin_number character varying(64),
  tender_date timestamp without time zone,
  tender_number character varying(64),
  trade_license_number character varying(64),
  created_by character varying(64),
  last_modified_by character varying(64),
  created_date timestamp without time zone,
  last_modified_date timestamp without time zone,
  allottee bigint NOT NULL,
  asset bigint NOT NULL,
  rent_increment_method bigint NOT NULL,
  acknowledgementnumber character varying(64),
  stateid character varying(64),
  tenant_id character varying,
  goodwillamount numeric,
  closeDate time without time zone,
  timePeriod bigint,
  CONSTRAINT pk_eglams_agreement PRIMARY KEY (id)
);

CREATE TABLE eglams_demand
(
  id bigint NOT NULL,
  tenantid character varying NOT NULL,
  agreementid bigint NOT NULL,
  demandid character varying NOT NULL,
  CONSTRAINT pk_eglams_demand PRIMARY KEY (id)
)
