ALTER TABLE eg_wf_types ALTER COLUMN typefqn TYPE varchar(250);

ALTER TABLE eg_wf_matrix ALTER COLUMN currentstate TYPE varchar(250);

ALTER TABLE eg_wf_matrix ALTER COLUMN currentstatus TYPE varchar(250);

ALTER TABLE eg_wf_matrix ALTER COLUMN pendingactions TYPE varchar(250);

ALTER TABLE eg_wf_matrix ALTER COLUMN currentdesignation TYPE varchar(250);

ALTER TABLE eg_wf_matrix ALTER COLUMN additionalrule TYPE varchar(250);

ALTER TABLE eg_wf_matrix ALTER COLUMN nextstate TYPE varchar(250);

ALTER TABLE eg_wf_matrix ALTER COLUMN nextdesignation TYPE varchar(250);

ALTER TABLE eg_wf_matrix ALTER COLUMN nextstatus TYPE varchar(250);

ALTER TABLE eg_wf_matrix ALTER COLUMN validactions TYPE varchar(250);
