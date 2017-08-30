alter table egwtr_waterconnection drop column meterowner, drop column metermodel;

alter table egwtr_meter add column meterowner character varying(50), add column metermodel character varying(50), add column maximumMeterreading character varying(50),add column meterstatus character varying(50); 

alter table egwtr_meterreading add column gapcode character varying(50), add column consumption character varying(50), add column consumptionadjusted character varying(50), add column numberofdays character varying(50), add column resetflag boolean default false;
 



