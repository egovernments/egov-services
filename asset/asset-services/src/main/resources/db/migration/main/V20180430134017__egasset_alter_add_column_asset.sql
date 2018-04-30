alter table egasset_asset add column purchaseValue double precision;
alter table egasset_asset add column purchaseDate bigint;
alter table egasset_asset add column constructionValue double precision;
alter table egasset_asset add column acquisitionValue double precision;
alter table egasset_asset add column acquisitionDate bigint;
alter table egasset_asset add column notApplicableForSaleOrDisposal boolean;