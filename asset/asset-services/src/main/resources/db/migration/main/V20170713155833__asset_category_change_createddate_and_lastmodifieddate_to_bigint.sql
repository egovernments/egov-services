alter table egasset_assetcategory alter column createddate type bigint using extract(EPOCH from createddate);
alter table egasset_assetcategory alter column lastmodifieddate type bigint using extract(EPOCH from lastmodifieddate);

--rollback alter table egasset_assetcategory alter column lastmodifieddate type timestamp without time zone 
--rollback	using to_timestamp(lastmodifieddate) AT TIME ZONE 'UTC';
--rollback alter table egasset_assetcategory alter column createddate type timestamp without time zone 
--rollback	using to_timestamp(createddate) AT TIME ZONE 'UTC';