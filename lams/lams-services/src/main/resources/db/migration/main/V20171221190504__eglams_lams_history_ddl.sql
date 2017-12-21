create sequence seq_eglams_history;

CREATE TABLE eglams_history (
id bigint NOT NULL,
agreementid bigint NOT NULL,
fromdate timestamp without time zone,
todate timestamp without time zone,
years numeric(6,2),
rent numeric(12,2),
tenantid character varying(64) NOT NULL
);


