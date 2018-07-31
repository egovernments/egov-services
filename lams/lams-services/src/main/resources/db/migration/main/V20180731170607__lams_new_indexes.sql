
---indexs for agreement and defaulters table
drop index if exists idx_lamsagreement_status;
create index idx_lamsagreement_status on eglams_agreement(status);

drop index if exists idx_lamsdefaulter_agmt;
create index idx_lamsdefaulter_agmt on eglams_defaulters(agreementnumber,assetcode,assetcategory,locality,ward,tenantid);

drop index if exists idx_lamsdemand_agmt;
create index idx_lamsdemand_agmt on eglams_demand(agreementid);

drop index if exists idx_defaulterdtl_agmt;
create index idx_defaulterdtl_agmt on eglams_defaulters_details(agreementnumber,balance, tenantid);

