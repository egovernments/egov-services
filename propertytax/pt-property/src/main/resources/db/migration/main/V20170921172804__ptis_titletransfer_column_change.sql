ALTER TABLE egpt_titletransfer ADD COLUMN demandId character varying;

ALTER TABLE egpt_propertydetails_history ADD COLUMN bpaNo character varying(16) DEFAULT NULL;
ALTER TABLE egpt_propertydetails_history ADD COLUMN bpaDate timestamp without time zone;

alter table egpt_propertydetails_history drop column subusage;
alter table egpt_propertydetails_history add column subusage character varying(128);


ALTER TABLE egpt_unit_history ALTER COLUMN structure TYPE character varying(64); 
ALTER TABLE egpt_unit_history ALTER COLUMN age TYPE character varying(64);  
ALTER TABLE egpt_unit_history ALTER COLUMN exemptionReason TYPE character varying(64);
ALTER TABLE egpt_unit_history ALTER COLUMN occupancytype TYPE character varying(64);

