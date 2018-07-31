
---index for demand details
drop index if exists idx_demanddtl_demand;
drop index if exists idx_demanddtl_dmndreason;
create index idx_demanddtl_demand on eg_demand_details(id_demand);
create index idx_demanddtl_dmndreason on eg_demand_details(id_demand_reason);

drop index if exists idx_dmdreason_reason;
drop index if exists idx_dmdreason_installment;
create index idx_dmdreason_reason on eg_demand_reason(id_demand_reason_master);
create index idx_dmdreason_installment on eg_demand_reason(id_installment);
