drop index if exists idx_lamsagreement_agnumber;
drop index if exists idx_lamsagreement_acknumber;
create index idx_lamsagreement_acknumber on eglams_agreement(acknowledgementnumber,tenant_id);
create index idx_lamsagreement_agnumber on eglams_agreement(agreement_no,tenant_id);

drop index if exists idx_lamsdoc_agreement;
create index idx_lamsdoc_agreement on eglams_document(agreement);

drop index if exists idx_lamsconfig_key;
create index idx_lamsconfig_key on eglams_lamsconfigurationvalues(keyid);