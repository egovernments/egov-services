ALTER TABLE egtl_license 
ADD COLUMN oldLicenseNumber character varying(20) NOT NULL,
ADD COLUMN adminWardID bigint NOT NULL;

ALTER TABLE egtl_license
ALTER COLUMN remarks character varying(1000),
ALTER COLUMN emailId character varying(50) NOT NULL,
ALTER COLUMN tradeAddress character varying(250) NOT NULL,
ALTER COLUMN licenseNumber character varying(20) NOT NULL,
ALTER COLUMN propertyAssesmentNo character(20);

