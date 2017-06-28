 ALTER TABLE egpgr_receiving_center
 ADD COLUMN code character varying(100) NOT NULL DEFAULT '001',
 ADD COLUMN description character varying(250) NULL,
 ADD COLUMN active boolean DEFAULT true,
 ADD COLUMN createdby bigint DEFAULT 1,
 ADD COLUMN createddate timestamp without time zone default CURRENT_TIMESTAMP,
 ADD COLUMN lastmodifiedby bigint DEFAULT 1,
 ADD COLUMN lastmodifieddate timestamp without time zone default CURRENT_TIMESTAMP;

update egpgr_receiving_center set code = name;

ALTER TABLE egpgr_receiving_center ALTER COLUMN code DROP DEFAULT;

ALTER TABLE egpgr_receiving_center DROP CONSTRAINT uk_receivingcenter_name_tenant;

ALTER TABLE egpgr_receiving_center ADD CONSTRAINT uk_receivingcenter_code_tenant UNIQUE(code, tenantid);

alter table egpgr_receivingmode
 ADD COLUMN description character varying(250) NULL,
 ADD COLUMN active boolean DEFAULT true,
 ADD COLUMN createdby bigint DEFAULT 1,
 ADD COLUMN createddate timestamp without time zone default CURRENT_TIMESTAMP,
 ADD COLUMN lastmodifiedby bigint DEFAULT 1,
 ADD COLUMN lastmodifieddate timestamp without time zone default CURRENT_TIMESTAMP;

alter table egpgr_receivingmode DROP COLUMN visible;

alter table egpgr_complainttype_category 
ADD COLUMN code character varying(100) NOT NULL DEFAULT '001',
ADD COLUMN createdby bigint DEFAULT 1,
ADD COLUMN createddate timestamp DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN lastmodifiedby bigint DEFAULT 1,
ADD COLUMN lastmodifieddate timestamp DEFAULT CURRENT_TIMESTAMP;

update egpgr_complainttype_category set code = name;

ALTER TABLE egpgr_complainttype_category ALTER COLUMN code DROP DEFAULT;

ALTER TABLE egpgr_complainttype_category DROP CONSTRAINT unique_complainttypecategory_name_tenant;

ALTER TABLE egpgr_complainttype_category ADD CONSTRAINT unique_egpgr_complainttype_category_code_tenant UNIQUE(code, tenantid);
