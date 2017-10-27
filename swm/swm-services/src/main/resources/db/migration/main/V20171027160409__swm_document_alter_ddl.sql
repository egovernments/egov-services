ALTER TABLE egswm_vehicle DROP COLUMN insuranceDocuments;
ALTER TABLE egswm_document DROP COLUMN documentTypeId;
ALTER TABLE egswm_document DROP COLUMN comments;
ALTER TABLE egswm_document RENAME COLUMN refId to regNumber;

