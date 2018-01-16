create sequence seq_eglams_reservation_category;

create table eglams_reservation_category (
id bigint PRIMARY KEY,
code character varying(32),
name character varying(64),
isactive boolean,
createddate timestamp without time zone,
createdby character varying(64),
lastmodifieddate timestamp without time zone,
lastmodifiedby character varying(64),
tenantid character varying(64) NOT NULL
);
