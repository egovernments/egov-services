drop index if exists idx_assetdoc_assetid;
create index idx_assetdoc_assetid on egasset_document(asset,tenantid);