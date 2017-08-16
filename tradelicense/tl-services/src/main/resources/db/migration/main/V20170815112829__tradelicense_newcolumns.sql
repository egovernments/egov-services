ALTER TABLE egtl_license 
ADD COLUMN oldLicenseNumber character varying(20),
ADD COLUMN adminWardID bigint NOT NULL  DEFAULT 0,
ADD COLUMN licenseValidFromDate timestamp without time zone NOT NULL  DEFAULT now(),
ADD COLUMN status bigint NOT NULL  DEFAULT 0;

ALTER TABLE egtl_license
ALTER COLUMN remarks TYPE character varying(1000),
ALTER COLUMN adhaarNumber TYPE character(20),
ALTER COLUMN ownerName TYPE character varying(100),
ALTER COLUMN fatherSpouseName TYPE character varying(100),
ALTER COLUMN emailId TYPE character varying(50),
ALTER COLUMN tradeAddress TYPE character varying(250),
ALTER COLUMN ownerAddress TYPE character varying(250) ,
ALTER COLUMN licenseNumber TYPE character varying(20),
ALTER COLUMN propertyAssesmentNo TYPE character(20);
