alter table disposal add constraint pk_disposal PRIMARY KEY (disposalnumber,tenantId); 
alter table disposaldetail add constraint pk_disposaldetail PRIMARY KEY (id,tenantId);
alter table disposaldetail add constraint foreign_disposaldetail FOREIGN KEY 
(disposalnumber, tenantId) REFERENCES disposal (disposalnumber,tenantId);
alter table disposal add column deleted boolean DEFAULT false; 
alter table disposaldetail add column deleted boolean DEFAULT false;


