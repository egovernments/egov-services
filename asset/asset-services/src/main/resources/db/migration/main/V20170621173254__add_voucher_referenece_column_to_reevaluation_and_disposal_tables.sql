alter table egasset_revalution add column voucherreference bigint;
alter table egasset_disposal add column voucherreference bigint;

--rollback alter table egasset_revalution drop column voucherreference;
--rollback alter table egasset_disposal drop column voucherreference;