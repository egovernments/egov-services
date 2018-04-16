----Disciplinary Authority
CREATE TABLE egeis_disciplinaryauthority (
	id BIGINT NOT NULL,
	name CHARACTER VARYING(250),
  tenantid character varying(250) NOT NULL,
	CONSTRAINT uk_egeis_disciplinaryauthority_name_tenantid UNIQUE (name,tenantid)
);

CREATE SEQUENCE seq_egeis_disciplinaryauthority
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-------Court Order Type
 CREATE TABLE egeis_courtordertype (
	id BIGINT NOT NULL,
	name CHARACTER VARYING(250),
	tenantid character varying(250) NOT NULL,
  CONSTRAINT uk_egeis_courtordertype_name_tenantid UNIQUE (name,tenantid)
);

CREATE SEQUENCE seq_egeis_courtordertype
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
