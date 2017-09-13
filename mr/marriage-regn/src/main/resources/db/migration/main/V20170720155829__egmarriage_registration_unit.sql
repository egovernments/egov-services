CREATE SEQUENCE seq_registartion_unit;

CREATE TABLE egmr_registration_unit(
		id BIGINT NOT NULL, 
		name CHARACTER VARYING(250),
		isactive BOOLEAN NOT NULL,
		tenantid CHARACTER VARYING(250) NOT NULL,
		locality BIGINT,
		zone BIGINT NOT NULL,
		revenueward BIGINT,
		block BIGINT,
		street BIGINT,
		electionward BIGINT,
		doorno CHARACTER VARYING(250),
		pincode INTEGER,
		isMainRegistrationUnit BOOLEAN NOT NULL,
		createdby CHARACTER VARYING(250),
		lastmodifiedby CHARACTER VARYING(250),
		createdtime BIGINT,
		lastmodifiedtime BIGINT,
		CONSTRAINT pkey_regn_unit PRIMARY KEY(id,tenantid)
		
)
