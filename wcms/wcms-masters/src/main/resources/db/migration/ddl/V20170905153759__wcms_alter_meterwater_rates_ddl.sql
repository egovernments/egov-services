ALTER TABLE egwtr_meter_water_rates ALTER COLUMN usagetypeid type bigint USING usagetypeid::bigint, 
		ALTER column subusagetypeid type bigint USING subusagetypeid::bigint, 
		add CONSTRAINT fk_meterwaterrates_usagetype FOREIGN KEY (usagetypeid, tenantid)
		REFERENCES egwtr_usage_type (id, tenantid);

ALTER TABLE egwtr_non_meter_water_rates ALTER COLUMN usagetypeid type bigint USING usagetypeid::bigint, 
		ALTER column subusagetypeid type bigint USING subusagetypeid::bigint, 
		add CONSTRAINT fk_meterwaterrates_usagetype FOREIGN KEY (usagetypeid, tenantid)
		REFERENCES egwtr_usage_type (id, tenantid);