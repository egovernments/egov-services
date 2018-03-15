
---PROMOTION BASIS

delete from egeis_promotion_basis;
insert into egeis_promotion_basis values (nextval('seq_egeis_promotion_basis'),'Caste Certificate/PH Certificate (RoR)','default');
insert into egeis_promotion_basis values (nextval('seq_egeis_promotion_basis'),'Educational Qualifications','default');
insert into egeis_promotion_basis values (nextval('seq_egeis_promotion_basis'),'Other qualifications','default');


---TRANSFER REASON

delete from egeis_transfer_reason;
insert into egeis_transfer_reason values (nextval('seq_egeis_transfer_reason'),'Medical grounds','default');
insert into egeis_transfer_reason values (nextval('seq_egeis_transfer_reason'),'Spouse grounds','default');
insert into egeis_transfer_reason values (nextval('seq_egeis_transfer_reason'),'Administrative grounds','default');
insert into egeis_transfer_reason values (nextval('seq_egeis_transfer_reason'),'Others','default');