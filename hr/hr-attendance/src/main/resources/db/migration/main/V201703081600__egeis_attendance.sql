CREATE TABLE egeis_attendance (
	id BIGINT NOT NULL,
	date date,
	employee BIGINT NOT NULL,
	month CHARACTER VARYING(250),
	year CHARACTER VARYING(250),
	type BIGINT NOT NULL,
	remarks CHARACTER VARYING(250),
	createdby BIGINT NOT NULL,
	createddate date NOT NULL,
	lastmodifiedby BIGINT NOT NULL,
	lastmodifieddate date NOT NULL,
	tenantId CHARACTER VARYING(250) NOT NULL,

	CONSTRAINT pk_egeis_attendance PRIMARY KEY (id),
	CONSTRAINT fk_egeis_attendance_type FOREIGN KEY (type)
		REFERENCES egeis_attendance_type(id)
);

CREATE SEQUENCE seq_egeis_attendance
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;