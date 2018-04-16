CREATE TABLE egeis_position (
	id BIGINT NOT NULL,
	name CHARACTER VARYING(100) NOT NULL,
	deptdesigId BIGINT NOT NULL,
	isPostOutsourced BOOLEAN NOT NULL,
	active BOOLEAN NOT NULL,
	tenantId CHARACTER VARYING(250) NOT NULL,

	CONSTRAINT pk_egeis_position PRIMARY KEY (id),
	CONSTRAINT uk_egeis_position_name UNIQUE (name),
	CONSTRAINT fk_egeis_position_deptdesigId FOREIGN KEY (deptdesigId)
		REFERENCES egeis_departmentDesignation(id)
);

CREATE SEQUENCE seq_egeis_position
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;