ALTER TABLE egw_letterofacceptance DROP COLUMN code;
alter table egw_letterofacceptance add column stamppaperamount double precision;
alter table egw_letterofacceptance add column councilResolutionNumber varchar(100);
alter table egw_letterofacceptance add column councilResolutionDate bigint;
alter table egw_letterofacceptance add column spilloverflag boolean default false;