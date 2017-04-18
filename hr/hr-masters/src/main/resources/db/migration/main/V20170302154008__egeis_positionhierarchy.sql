CREATE TABLE egeis_positionHierarchy (
	id BIGINT NOT NULL,
	fromPositionId BIGINT NOT NULL,
	toPositionId BIGINT NOT NULL,
	objectTypeId BIGINT NOT NULL,
	objectSubType CHARACTER VARYING(25),
	tenantId CHARACTER VARYING(250) NOT NULL,

	CONSTRAINT pk_egeis_positionHierarchy PRIMARY KEY (id),
	CONSTRAINT fk_egeis_departmentDesignation_fromPositionId FOREIGN KEY (fromPositionId)
		REFERENCES egeis_position(id),
	CONSTRAINT fk_egeis_departmentDesignation_toPositionId FOREIGN KEY (toPositionId)
		REFERENCES egeis_position(id),
	CONSTRAINT fk_egeis_departmentDesignation_objectTypeid FOREIGN KEY (objectTypeid)
		REFERENCES egeis_objectType(id)
);

CREATE SEQUENCE seq_egeis_positionHierarchy
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;