create sequence SEQ_EG_REASON_CATEGORY;
CREATE TABLE if not exists eg_reason_category
(
  id bigint NOT NULL, -- Primary Key
  name character varying(64) NOT NULL, -- Name of the demand category, Eg: Tax, Penalty, Rebate
  code character varying(64) NOT NULL, -- demand category code(uses internally)
  "order" bigint NOT NULL, -- Order no to display list
  modified_date timestamp without time zone NOT NULL,
  CONSTRAINT pk_eg_reason_category PRIMARY KEY (id)
);

alter table eg_reason_category add column tenantid character varying(250);

create sequence SEQ_EG_DEMAND_REASON_MASTER;
CREATE TABLE if not exists eg_demand_reason_master
(
  id bigint NOT NULL, -- Primary Key
  reasonmaster character varying(64) NOT NULL, -- Name of the demand head
  category bigint NOT NULL, -- FK to eg_reason_category
  isdebit character(1) NOT NULL, -- is tax head is debit. 0 or 1
  module bigint NOT NULL, -- FK to eg_module
  code character varying(16) NOT NULL, -- tax head code(uses internally)
  "order" bigint NOT NULL, -- Order no to display list
  create_date timestamp without time zone NOT NULL,
  modified_date timestamp without time zone NOT NULL,
  isdemand boolean NOT NULL,
  CONSTRAINT pk_eg_demand_reason_master PRIMARY KEY (id),
  CONSTRAINT fk_demand_reason_module FOREIGN KEY (module)
      REFERENCES eg_module (id),
  CONSTRAINT fk_reason_category FOREIGN KEY (category)
      REFERENCES eg_reason_category (id)
);

alter table eg_demand_reason_master add column tenantid character varying(250);

create sequence seq_eg_installment_master;
CREATE TABLE if not exists eg_installment_master
(
  id bigint NOT NULL, -- primary key
  installment_num bigint NOT NULL, -- Installment number
  installment_year timestamp without time zone NOT NULL, -- Installment year
  start_date timestamp without time zone NOT NULL, -- installment start date
  end_date timestamp without time zone NOT NULL, -- installment end date
  id_module bigint, -- fk to eg_module
  lastupdatedtimestamp timestamp without time zone, -- last updated time when row got updated
  description character varying(25), -- Descriptiion of installment
  installment_type character varying(50), -- type of installment
  financial_year character varying(50),
  CONSTRAINT pk_egpt_installment_master PRIMARY KEY (id),
  CONSTRAINT fk_instmstr_module FOREIGN KEY (id_module)
      REFERENCES eg_module (id),
  CONSTRAINT unq_year_number_mod UNIQUE (id_module, installment_num, installment_year)
);

alter table eg_installment_master add column tenantid character varying(250);

create sequence SEQ_EG_DEMAND_REASON;
CREATE TABLE if not exists eg_demand_reason
(
  id bigint NOT NULL, -- Primary Key
  id_demand_reason_master bigint NOT NULL, -- FK to eg_demand_reason_master
  id_installment bigint NOT NULL, -- FK to eg_installment_master
  percentage_basis real, -- Not used
  id_base_reason bigint, -- FK to eg_demand_reason
  create_date timestamp without time zone NOT NULL,
  modified_date timestamp without time zone NOT NULL,
  glcodeid bigint, -- FK to chartofaccounts
  CONSTRAINT pk_egdm_demand_reason PRIMARY KEY (id),
  CONSTRAINT fk_eg_dem_reason_id_base_dem FOREIGN KEY (id_base_reason)
      REFERENCES eg_demand_reason (id),
  CONSTRAINT fk_eg_installment_id FOREIGN KEY (id_installment)
      REFERENCES eg_installment_master (id),
  CONSTRAINT fk_egdemandreasonmaster_id FOREIGN KEY (id_demand_reason_master)
      REFERENCES eg_demand_reason_master (id)
);

alter table eg_demand_reason add column tenantid character varying(250);

create sequence SEQ_EG_DEMAND_REASON_DETAILS;
CREATE TABLE eg_demand_reason_details
(
  id bigint NOT NULL, -- Primary Key
  id_demand_reason bigint NOT NULL, -- FK to eg_demand_reason
  percentage real, -- tax perc for each demand head
  from_date timestamp without time zone NOT NULL, -- tax perc for each demand head validity start date
  to_date timestamp without time zone NOT NULL,
  low_limit double precision, -- low limit amount for tax head
  high_limit double precision, -- High limit amount for tax head
  create_date timestamp without time zone NOT NULL,
  modified_date timestamp without time zone NOT NULL,
  flat_amount double precision, -- Flat tax amount to be applicable
  is_flatamnt_max double precision, -- if the tax for head is flat amount then amount comes here
  CONSTRAINT pk_egdm_demand_reason_details PRIMARY KEY (id),
  CONSTRAINT fk_egpt_dem_reason_id FOREIGN KEY (id_demand_reason)
      REFERENCES eg_demand_reason (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

alter table eg_demand_reason_details add column tenantid character varying(250);

create sequence seq_eg_bill_type;
CREATE TABLE if not exists eg_bill_type
(
  id bigint NOT NULL, -- Primary Key
  name character varying(32) NOT NULL, -- Name for bill type
  code character varying(10) NOT NULL, -- Code
  create_date date NOT NULL,
  modified_date date NOT NULL,
  CONSTRAINT prk_eg_bill_type PRIMARY KEY (id)
);

alter table eg_bill_type add column tenantid character varying(250);

create sequence SEQ_EG_DEMAND;
CREATE TABLE if not exists eg_demand
(
  id bigint NOT NULL, -- Primary Key
  id_installment bigint NOT NULL, -- FK to eg_installment_master
  base_demand double precision, -- Total demand for a installment
  is_history character(1) NOT NULL DEFAULT 'N'::bpchar, -- history status of demand
  create_date timestamp without time zone NOT NULL,
  modified_date timestamp without time zone NOT NULL,
  amt_collected double precision, -- Tax amount collected
  status character(1), -- Not used
  min_amt_payable double precision, -- Minimum Amount payable
  amt_rebate double precision, -- Tax rebate given
  CONSTRAINT pk_egdm_demand PRIMARY KEY (id),
  CONSTRAINT fk_eg_installment_master_id FOREIGN KEY (id_installment)
      REFERENCES eg_installment_master (id)
);

alter table eg_demand add column tenantid character varying(250);

create sequence seq_eg_bill;
CREATE TABLE if not exists eg_bill
(
  id bigint NOT NULL, -- Primary Key
  id_demand bigint, -- FK to eg_demand
  citizen_name character varying(1024) NOT NULL, -- citizen name
  citizen_address character varying(1024) NOT NULL, -- Citizen address
  bill_no character varying(20) NOT NULL, -- Bill no
  id_bill_type bigint NOT NULL, -- FK to eg_bill_type
  issue_date timestamp without time zone NOT NULL, -- Bill issue date
  last_date timestamp without time zone, -- Last date of payment using this bill
  module_id bigint NOT NULL, -- FK to eg_module
  user_id bigint NOT NULL, -- FK to eg_user
  create_date timestamp without time zone NOT NULL,
  modified_date timestamp without time zone NOT NULL,
  is_history character(1) NOT NULL DEFAULT 'N'::bpchar, -- Bill history status
  is_cancelled character(1) NOT NULL DEFAULT 'N'::bpchar, -- Bill cancel status
  fundcode character varying(32), -- fund code
  functionary_code double precision, -- functionary code
  fundsource_code character varying(32), -- fund source code
  department_code character varying(32), -- Department that bill entity belongs to
  coll_modes_not_allowed character varying(512), -- allowd collection modes for this bill
  boundary_num bigint, -- boundary of entity, for which bill is generated
  boundary_type character varying(512), -- boundary type of entity, for which bill is generated
  total_amount double precision, -- total bill amount
  total_collected_amount double precision, -- total amount collected for this bill
  service_code character varying(50), -- service code from collection system for each billing system
  part_payment_allowed character(1), -- information to collection system, do system need to allow partial payment
  override_accountheads_allowed character(1), -- information to collection system, do collection system allow for override  of account head wise collection
  description character varying(250), -- Description of entity for which bill is created
  min_amt_payable double precision, -- minimu amount payable for this bill
  consumer_id character varying(64), -- consumer id, for different billing system diff unique ref no will be thr
  dspl_message character varying(256), -- message need to be shown on collection screen
  callback_for_apportion character(1) NOT NULL DEFAULT 0, -- call back required or not while doing collection
  emailid character varying(128),
  consumertype character varying(100),
  CONSTRAINT pk_eg_bill PRIMARY KEY (id),
  CONSTRAINT fk_egbilltype_id FOREIGN KEY (id_bill_type)
      REFERENCES eg_bill_type (id),
  CONSTRAINT fk_egdemand_id FOREIGN KEY (id_demand)
      REFERENCES eg_demand (id)
);

alter table eg_bill add column tenantid character varying(250);

create sequence seq_eg_billreceipt;
CREATE TABLE eg_billreceipt
(
  id bigint NOT NULL, -- Primary Key
  billid bigint NOT NULL, -- FK to eg_bill
  receipt_number character varying(50), -- receipt NUMBER
  receipt_date timestamp without time zone, -- receipt date
  receipt_amount double precision NOT NULL, -- receipt amount
  collection_status character varying(20), -- status of collection (approved, pending, etc)
  created_date timestamp without time zone NOT NULL,
  modified_date timestamp without time zone NOT NULL,
  createdby bigint,
  lastmodifiedby bigint,
  is_cancelled character(1) NOT NULL DEFAULT 'N'::bpchar, -- receipt status
  CONSTRAINT pk_eg_billreceipt PRIMARY KEY (id)
);

alter table eg_billreceipt add column tenantid character varying(250);

create sequence seq_eg_bill_details;
CREATE TABLE if not exists eg_bill_details
(
  id bigint NOT NULL, -- Primary Key
  id_demand_reason bigint, -- FK to eg_demand_reason
  create_date timestamp without time zone,
  modified_date timestamp without time zone NOT NULL,
  id_bill bigint NOT NULL, -- FK to eg_bill
  collected_amount double precision, -- Amount collected
  order_no bigint, -- order no
  glcode character varying(64), -- Financials GL Code
  function_code character varying(32), -- Financials function code
  cr_amount double precision, -- Credit amount
  dr_amount double precision, -- Debit amount
  description character varying(128), -- Text to understand amount to be paid for which head and period
  id_installment bigint, -- FK to eg_installment_master
  additional_flag bigint, -- Additional Flag
  purpose character varying(50) NOT NULL,
  CONSTRAINT pk_eg_bill_details PRIMARY KEY (id),
  CONSTRAINT eg_installment_id FOREIGN KEY (id_installment)
      REFERENCES eg_installment_master (id),
  CONSTRAINT fk_eg_bill_det_idbill FOREIGN KEY (id_bill)
      REFERENCES eg_bill (id)
);

alter table eg_bill_details add column tenantid character varying(250);

create sequence seq_eg_demand_details;
CREATE TABLE if not exists eg_demand_details
(
  id bigint NOT NULL, -- Primary Key
  id_demand bigint NOT NULL, -- FK to eg_demand
  id_demand_reason bigint NOT NULL, -- FK to eg_demand_reason
  id_status bigint, -- Not used
  file_reference_no character varying(32), -- Not used
  remarks character varying(512), -- remarks if any
  amount double precision NOT NULL, -- Tax Amount
  modified_date timestamp without time zone NOT NULL,
  create_date timestamp without time zone NOT NULL,
  amt_collected double precision DEFAULT 0, -- Tax Amount collected
  amt_rebate double precision DEFAULT 0, -- tax rebate given
  CONSTRAINT pk_eg_demand_details PRIMARY KEY (id),
  CONSTRAINT fk_egdm_dem_reason_id FOREIGN KEY (id_demand_reason)
      REFERENCES eg_demand_reason (id),
  CONSTRAINT fk_egdm_demand_id FOREIGN KEY (id_demand)
      REFERENCES eg_demand (id)
);

alter table eg_demand_details add column tenantid character varying(250);

create sequence seq_egdm_collected_receipts;
CREATE TABLE if not exists egdm_collected_receipts
(
  id bigint NOT NULL, -- Primary Key
  id_demand_detail bigint NOT NULL, -- FK to eg_demand_detail
  receipt_number character varying(50) NOT NULL, -- Receipt number for a pyment
  receipt_date timestamp without time zone NOT NULL, -- Receipt date for a pyment
  receipt_amount double precision NOT NULL, -- Total receipt amount for a pyment
  reason_amount double precision, -- amount paid for reason head
  status character varying(1) NOT NULL, -- Receipt status
  updated_time timestamp without time zone NOT NULL,
  CONSTRAINT pk_egdm_collected_receipts PRIMARY KEY (id),
  CONSTRAINT fk_eg_dmd_detail_id FOREIGN KEY (id_demand_detail)
      REFERENCES eg_demand_details (id)
);

alter table egdm_collected_receipts add column tenantid character varying(250);

