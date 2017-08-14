ALTER TABLE egpt_unit ADD COLUMN constructionStartDate timestamp without time zone;
ALTER TABLE egpt_unit ADD COLUMN landCost numeric;
ALTER TABLE egpt_unit ADD COLUMN buildingCost numeric;

ALTER TABLE egpt_property ADD COLUMN sequenceNo integer;

ALTER TABLE egpt_propertydetails ADD COLUMN factors jsonb;
ALTER TABLE egpt_propertydetails ADD COLUMN assessmentDates jsonb;
ALTER TABLE egpt_propertydetails ADD COLUMN builderDetails jsonb;

ALTER TABLE egpt_unit_history ADD COLUMN constructionStartDate timestamp without time zone;
ALTER TABLE egpt_unit_history ADD COLUMN landCost numeric;
ALTER TABLE egpt_unit_history ADD COLUMN buildingCost numeric;

ALTER TABLE egpt_property_history ADD COLUMN sequenceNo integer;

ALTER TABLE egpt_propertydetails_history ADD COLUMN factors jsonb;
ALTER TABLE egpt_propertydetails_history ADD COLUMN assessmentDates jsonb;
ALTER TABLE egpt_propertydetails_history ADD COLUMN builderDetails jsonb;

