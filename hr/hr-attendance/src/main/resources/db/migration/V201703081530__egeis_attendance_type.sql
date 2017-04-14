CREATE TABLE egeis_attendance_type (
	id BIGINT NOT NULL,
	code CHARACTER VARYING(250),
	description CHARACTER VARYING(250),

	CONSTRAINT pk_egeis_attendance_type PRIMARY KEY (id)
);

CREATE SEQUENCE seq_egeis_attendance_type
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;