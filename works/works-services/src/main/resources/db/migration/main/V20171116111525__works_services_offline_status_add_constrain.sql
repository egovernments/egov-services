ALTER TABLE egw_offlinestatus DROP CONSTRAINT IF EXISTS unique_egw_offlinestatus_;
alter table egw_offlinestatus add constraint unique_egw_offlinestatus unique  (objectnumber,status,objecttype,tenantid);
ALTER TABLE egw_estimate_appropriation DROP CONSTRAINT IF EXISTS tenantid_detailedestimate_unique;