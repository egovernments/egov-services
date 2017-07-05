ALTER TABLE egpt_propertydetails
ALTER regddocno type timestamp USING regdDocDate::timestamp

ALTER TABLE egpt_propertydetails ADD COLUMN taxCalculations jsonb;

ALTER TABLE egpt_property ADD COLUMN demands jsonb;