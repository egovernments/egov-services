CREATE SEQUENCE seq_registartionunit_id;

CREATE TABLE egmr_registartion_unit(
		id BIGINT NOT NULL, 
		code CHARACTER VARYING(250) NOT NULL,
		name CHARACTER VARYING(250),
		isactive boolean,
		tenantid CHARACTER VARYING(250) NOT NULL,
		locality BIGINT,
		zone BIGINT,
		revenueward BIGINT,
		block BIGINT,
		street BIGINT,
		electionward BIGINT,
		doorno CHARACTER VARYING(250),
		pincode INTEGER,
		createdby CHARACTER VARYING(250),
		lastmodifiedby CHARACTER VARYING(250),
		createdtime BIGINT,
		lastmodifiedtime BIGINT,
		CONSTRAINT pkey_regn_unit PRIMARY KEY(id,tenantid)
		
)