ALTER TABLE egtl_license 
ADD COLUMN oldLicenseNumber character varying(20),
ADD COLUMN adminWardID bigint NOT NULL Default 0;

ALTER TABLE egtl_license
ALTER COLUMN remarks TYPE character varying(1000),
ALTER COLUMN emailId TYPE character varying(50),
ALTER COLUMN tradeAddress TYPE character varying(250),
ALTER COLUMN licenseNumber TYPE character varying(20),
ALTER COLUMN propertyAssesmentNo TYPE character(20);

