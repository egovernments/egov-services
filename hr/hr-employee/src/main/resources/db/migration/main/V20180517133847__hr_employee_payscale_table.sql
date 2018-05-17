
CREATE TABLE egeis_payscaleheader
(
  id BIGINT NOT NULL,
  paycommission CHARACTER VARYING(250) NOT NULL,
  payscale CHARACTER VARYING(100) NOT NULL,
  amountfrom BIGINT NOT NULL,
  amountto BIGINT NOT NULL,
  createdBy BIGINT NOT NULL,
  createdDate DATE NOT NULL,
  lastModifiedBy BIGINT,
  lastModifiedDate DATE,
  tenantId CHARACTER VARYING(250) NOT NULL,

	CONSTRAINT pk_egeis_payscaleheader PRIMARY KEY (Id, tenantid),
	CONSTRAINT uk_egeis_payscaleheader UNIQUE (paycommission, payscale, tenantId)
);

CREATE SEQUENCE seq_egeis_payscaleheader
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

create table egeis_payscaledetails
(
  id BIGINT NOT NULL,
  payscaleheaderid BIGINT NOT NULL,
  basicfrom BIGINT NOT NULL,
  basicto BIGINT NOT NULL,
  increment BIGINT NOT NULL,
  tenantId CHARACTER VARYING(250) NOT NULL,

  CONSTRAINT pk_egeis_payscaledetails PRIMARY KEY (Id, tenantid),
  CONSTRAINT fk_egeis_payscaledetails_headerid FOREIGN KEY (payscaleheaderid,tenantId)
	REFERENCES egeis_payscaleheader (id,tenantId)
);

CREATE SEQUENCE seq_egeis_payscaledetails
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;