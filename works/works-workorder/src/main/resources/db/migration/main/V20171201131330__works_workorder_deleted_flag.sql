ALTER TABLE egw_letterofacceptanceestimate ADD COLUMN deleted boolean DEFAULT false;
ALTER TABLE egw_loaactivity ADD COLUMN deleted boolean DEFAULT false;
ALTER TABLE egw_loameasurementsheet ADD COLUMN deleted boolean DEFAULT false;
ALTER TABLE egw_securitydeposit ADD COLUMN deleted boolean DEFAULT false;
ALTER TABLE egw_assetsforloa ADD COLUMN deleted boolean DEFAULT false;

ALTER TABLE egw_workorder_details ADD COLUMN deleted boolean DEFAULT false;