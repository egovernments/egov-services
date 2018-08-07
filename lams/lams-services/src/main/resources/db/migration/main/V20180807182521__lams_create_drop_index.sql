drop index if exists idx_lamsagreement_rentmethod;
create index idx_lamsagreement_rentmethod on eglams_agreement(rent_increment_method);

drop index if exists idx_lamsconfig_name;
create index idx_lamsconfig_name on eglams_lamsconfiguration(keyname,tenantid);

drop index if exists idx_lamsconfig_value;
drop index if exists idx_lamsconfig_key;
create index idx_lamsconfig_value on eglams_lamsConfigurationvalues(keyid,tenantid);