alter table supplier rename suppliertype to type;
alter table supplier rename narration to description;
alter table supplier rename bank to bankcode;
alter table supplier rename suppliercontactno to contactno;
alter table supplier add column active boolean;