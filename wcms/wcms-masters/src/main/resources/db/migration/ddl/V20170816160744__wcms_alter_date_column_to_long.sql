alter table egwtr_meter_water_rates drop column fromdate;
alter table egwtr_meter_water_rates drop column todate;


alter table egwtr_meter_water_rates add column fromdate bigint ;
alter table egwtr_meter_water_rates add column todate bigint ;