Create sequence seq_materialissue;

Create table materialissue(
id varchar(32) NOT NULL,
tenantId varchar(128) NOT NULL,
issuetype varchar(50) NOT NULL,
fromstore varchar(50) NOT NULL,
tostore varchar(50),
issuenumber varchar(100) NOT NULL,
issuedate bigint NOT NULL,
materialissuestatus varchar(50) NOT NULL,
description varchar(500),
totalissuevalue numeric NOT NULL,
fileStoreId varchar(500),
designation varchar(100),
indentid varchar(50),
issuedtoemployee varchar(100),
issuedtodesignation varchar(100),
issuepurpose varchar (100),
supplier varchar(50),
stateId bigint,
createdby varchar(50) NOT NULL,
createdTime bigint NOT NULL,
lastmodifiedby varchar(50) NOT NULL,
lastModifiedTime bigint NOT NULL,
version bigint,
constraint  pk_inv_materialissue PRIMARY KEY (issuenumber,tenantId)
);


create sequence seq_materialissuedetail;

create table materialissuedetail(
id varchar(50) NOT NULL,
tenantId varchar(128) NOT NULL,
orderNumber numeric,
value numeric NOT NULL,
uom varchar(50) NOT NULL,
scrapvalue numeric,
voucherheader varchar(100),
materialissuenumber varchar(50) NOT NULL,
indentdetailid varchar(50) NOT NULL,
quantitytobeissued numeric,
materialissuedfromreceiptsid varchar(50),
materialid varchar(50),
quantityissued numeric NOT NULL,
description varchar(500),
constraint  pk_inv_materialissuedetail PRIMARY KEY (id,tenantId),
constraint for_inv_materialissuedetail FOREIGN KEY(materialissuenumber,tenantId) REFERENCES materialissue(issuenumber,tenantId)
);