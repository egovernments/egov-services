ALTER TABLE eg_wf_action_v2
ADD COLUMN isEditable boolean;

CREATE INDEX eg_wf_processinstance_v2_tenantid_idx ON eg_wf_processinstance_v2 (tenantid);
