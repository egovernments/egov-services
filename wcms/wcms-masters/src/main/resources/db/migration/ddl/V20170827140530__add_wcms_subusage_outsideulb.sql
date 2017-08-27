alter table egwtr_non_meter_water_rates add column subusagetypeid character varying(20);
alter table egwtr_non_meter_water_rates add column outsideulb boolean;
alter table egwtr_meter_water_rates add column outsideulb boolean;
alter table egwtr_donation add column outsideulb boolean;