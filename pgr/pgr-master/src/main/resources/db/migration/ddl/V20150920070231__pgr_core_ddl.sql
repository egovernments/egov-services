---ReceivingCenter  Master

CREATE TABLE egpgr_receiving_center
(
  id SERIAL primary key,
  code character varying(20) NULL,
  name character varying(100) NOT NULL,
  description character varying(250),
  iscrnrequired boolean DEFAULT false,
  orderno bigint DEFAULT 0,
  active boolean DEFAULT true,
  createddate timestamp without time zone,
  lastmodifieddate timestamp without time zone,
  createdby bigint,
  lastmodifiedby bigint,
  tenantid character varying(250) NOT NULL,
  version numeric DEFAULT 0,
  CONSTRAINT un_receiving_center UNIQUE (id, tenantid),
  CONSTRAINT un_document_name UNIQUE (name, tenantid)
);


CREATE SEQUENCE seq_egpgr_receiving_center
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
    