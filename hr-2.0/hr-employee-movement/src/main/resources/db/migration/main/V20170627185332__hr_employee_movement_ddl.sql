CREATE TABLE egeis_movement (
	id BIGINT NOT NULL,
	description CHARACTER VARYING(250),
	employee BIGINT NOT NULL,
	typeOfMovement CHARACTER VARYING(30) NOT NULL,
	currentAssignment BIGINT,
	transferType CHARACTER VARYING(100),
	promotionBasis BIGINT,
	remarks CHARACTER VARYING(250),
	reason BIGINT,
	effectiveFrom DATE NOT NULL,
	departmentAssigned BIGINT,
	designationAssigned BIGINT,
	positionAssigned BIGINT,
	fundAssigned BIGINT,
	functionAssigned BIGINT,
	documents CHARACTER VARYING(30),
	employeeAcceptance BOOLEAN,
	status BIGINT,
	stateId BIGINT,
	createdBy BIGINT,
	createdDate TIMESTAMP WITHOUT TIME ZONE,
	lastModifiedBy BIGINT,
	lastModifiedDate TIMESTAMP WITHOUT TIME ZONE,
	tenantId CHARACTER VARYING(30),
	
	CONSTRAINT pk_egeis_movement PRIMARY KEY (id, tenantId)
);

CREATE SEQUENCE seq_egeis_movement
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;