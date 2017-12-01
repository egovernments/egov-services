ALTER TABLE egw_documentdetail ADD COLUMN deleted boolean DEFAULT false;
ALTER TABLE egw_estimate_appropriation  ADD COLUMN deleted boolean DEFAULT false;
ALTER TABLE egw_offlinestatus ADD COLUMN deleted boolean DEFAULT false;