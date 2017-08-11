ALTER TABLE egwtr_estimationnotice_audit_log RENAME penaltychargedescription1  TO chargedescription1;

ALTER TABLE egwtr_estimationnotice_audit_log
   ALTER COLUMN chargedescription1 TYPE character varying(100);

ALTER TABLE egwtr_estimationnotice_audit_log RENAME penaltychargedescription2  TO chargedescription2;

ALTER TABLE egwtr_estimationnotice_audit_log
   ALTER COLUMN chargedescription2 TYPE character varying(100);

ALTER TABLE egwtr_estimationnotice_audit_log
  ADD COLUMN chargedescription3 character varying(100);

ALTER TABLE egwtr_estimationnotice_audit_log
  ADD COLUMN chargedescription4 character varying(100);

ALTER TABLE egwtr_estimationnotice_audit_log
  ADD COLUMN chargedescription5 character varying(100);

ALTER TABLE egwtr_estimationnotice_audit_log
  ADD COLUMN chargedescription6 character varying(100);