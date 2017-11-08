ALTER TABLE egswm_sanitationstafftarget ADD COLUMN location varchar(256);

ALTER TABLE egswm_sanitationstafftarget ADD COLUMN wetWaste numeric (13,2);

ALTER TABLE egswm_sanitationstafftarget ADD COLUMN dryWaste numeric (13,2);

ALTER TABLE egswm_sanitationstafftarget ADD COLUMN dumpingGround varchar(256);