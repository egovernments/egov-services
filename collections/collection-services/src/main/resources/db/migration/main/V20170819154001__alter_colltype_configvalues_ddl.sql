alter table egcl_receiptheader alter column collectiontype set not null; 

update egcl_configurationvalues set tenantId = 'inactivetenant' where value = 'PREAPPROVED';