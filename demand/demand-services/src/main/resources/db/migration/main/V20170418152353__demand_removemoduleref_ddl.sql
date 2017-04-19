alter table eg_demand_reason_master drop constraint fk_demand_reason_module;
alter table eg_demand_reason_master alter column module type character varying(128);

alter table eg_installment_master drop constraint fk_instmstr_module;
alter table eg_installment_master rename column id_module to module;
alter table eg_installment_master alter column module type character varying(128);

alter table eg_demand_reason rename column glcodeid to glcode;
alter table eg_demand_reason alter column glcode type character varying(32);

alter table eg_bill rename column module_id to module;
alter table eg_bill alter column module type character varying(128);

alter table eg_installment_master drop CONSTRAINT unq_year_number_mod;
alter table eg_installment_master add CONSTRAINT unq_year_number_mod UNIQUE (module, installment_num, installment_year, tenantid);