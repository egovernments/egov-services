ALTER TABLE indentdetail DROP CONSTRAINT pk_indentdetail;

alter table indentdetail add constraint pk_indentdetail primary key (id, tenantid);

