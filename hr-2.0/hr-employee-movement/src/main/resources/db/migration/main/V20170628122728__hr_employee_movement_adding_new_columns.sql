ALTER TABLE egeis_movement DROP COLUMN description;

ALTER TABLE egeis_movement add column enquiryPassedDate TIMESTAMP WITHOUT TIME ZONE;
ALTER TABLE egeis_movement add column transferedLocation BIGINT;
