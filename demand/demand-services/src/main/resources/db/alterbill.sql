ALTER TABLE eg_bill ALTER part_payment_allowed TYPE boolean USING CASE part_payment_allowed WHEN 'Y' THEN TRUE ELSE FALSE END;
ALTER TABLE eg_bill ALTER override_accountheads_allowed TYPE boolean USING CASE override_accountheads_allowed WHEN 'Y' THEN TRUE ELSE FALSE END;

ALTER TABLE eg_bill ALTER callback_for_apportion DROP DEFAULT;
ALTER TABLE eg_bill ALTER callback_for_apportion TYPE boolean USING CASE callback_for_apportion WHEN '1' THEN TRUE ELSE FALSE END;
ALTER TABLE eg_bill ALTER callback_for_apportion set DEFAULT false;
