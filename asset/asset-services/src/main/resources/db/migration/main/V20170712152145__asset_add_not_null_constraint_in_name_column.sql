alter table egasset_statuses alter column name set not null;

--rollback alter table egasset_statuses alter column drop not null;