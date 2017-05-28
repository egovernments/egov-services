CREATE SEQUENCE seq_registartionunit_id;

CREATE TABLE registartion_unit(
		id BIGINT PRIMARY KEY, 
		name CHARACTER VARYING(250),
		address BIGINT,
		isactive boolean,
		tenantId CHARACTER VARYING(250),
		locality BIGINT,
		zone BIGINT,
		revenueWard BIGINT,
		block BIGINT,
		street BIGINT,
		electionWard BIGINT,
		doorNo CHARACTER VARYING(250),
		pinCode BIGINT 
)