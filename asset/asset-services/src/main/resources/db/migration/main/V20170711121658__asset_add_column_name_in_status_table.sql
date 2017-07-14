alter table egasset_statuses add column name character varying(20);

--rollback alter table egasset_statuses drop column name;