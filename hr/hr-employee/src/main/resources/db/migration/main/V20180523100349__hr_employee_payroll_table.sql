CREATE TABLE egeis_employeepayscale (
   id BIGINT NOT NULL,
   employeeid BIGINT NOT NULL,
   payscaleheaderid BIGINT NOT NULL,
   effectivefrom DATE NOT NULL,
   incrementmonth CHARACTER VARYING(50) NOT NULL,
   basicamount BIGINT NOT NULL,
   reason CHARACTER VARYING(250) NOT NULL,
   createdby BIGINT NOT NULL,
   createddate DATE NOT NULL,
   lastmodifiedby BIGINT,
   lastmodifieddate DATE,
   tenantid CHARACTER VARYING(250) NOT NULL,

  CONSTRAINT pk_egeis_employeepayscale PRIMARY KEY (Id, tenantid),
  CONSTRAINT fk_egeis_employeepayscale_headerid FOREIGN KEY (payscaleheaderid,tenantId)
	REFERENCES egeis_payscaleheader (id,tenantId),
  CONSTRAINT fk_egeis_employeepayscale_employeeid FOREIGN KEY (employeeid,tenantId)
	REFERENCES egeis_employee (id,tenantId)
);

CREATE SEQUENCE seq_egeis_employeepayscale
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;