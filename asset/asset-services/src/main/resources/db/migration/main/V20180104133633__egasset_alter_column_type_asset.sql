alter table egasset_asset alter column createddate type bigint using extract(EPOCH from createddate);
alter table egasset_asset alter column lastmodifieddate type bigint using extract(EPOCH from lastmodifieddate);
alter table egasset_asset alter column dateofcreation type bigint using extract(EPOCH from dateofcreation);