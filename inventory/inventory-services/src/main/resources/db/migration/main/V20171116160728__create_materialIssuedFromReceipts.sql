create sequence seq_materialissuedfromreceipt;

create table materialissuedfromreceipt(
id varchar(50) NOT NULL,
tenantId varchar(128) NOT NULL,
receiptid varchar(50),
receiptdetailId varchar(50),
receiptdetailaddnlinfoid  varchar(50),
quantity numeric,
status boolean,
issuedetailid varchar(50) NOT NULL,
constraint pk_materialissuedfromreceipt primary key (id,tenantId),
constraint for_materialissuedfromreceipt foreign key (issuedetailid,tenantid) REFERENCES 
materialissuedetail(id,tenantId)
);

alter table materialissuedetail drop column materialissuedfromreceiptsid;
