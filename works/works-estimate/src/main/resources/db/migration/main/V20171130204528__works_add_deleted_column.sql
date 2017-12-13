ALTER TABLE egw_abstractestimate ADD COLUMN deleted boolean DEFAULT false;
ALTER TABLE egw_abstractestimate_details  ADD COLUMN deleted boolean DEFAULT false;
ALTER TABLE egw_abstractestimate_sanction_details ADD COLUMN deleted boolean DEFAULT false;
ALTER TABLE egw_abstractestimate_asset_details ADD COLUMN deleted boolean DEFAULT false;

ALTER TABLE egw_detailedestimate ADD COLUMN deleted boolean DEFAULT false;
ALTER TABLE egw_estimate_activity ADD COLUMN deleted boolean DEFAULT false;
ALTER TABLE egw_estimate_assets ADD COLUMN deleted boolean DEFAULT false;
ALTER TABLE egw_estimate_deductions ADD COLUMN deleted boolean DEFAULT false;
ALTER TABLE egw_estimate_overheads ADD COLUMN deleted boolean DEFAULT false;
ALTER TABLE egw_estimate_measurementsheet ADD COLUMN deleted boolean DEFAULT false;
ALTER TABLE egw_estimate_technicalsanction ADD COLUMN deleted boolean DEFAULT false;
ALTER TABLE egw_projectcode ADD COLUMN deleted boolean DEFAULT false;