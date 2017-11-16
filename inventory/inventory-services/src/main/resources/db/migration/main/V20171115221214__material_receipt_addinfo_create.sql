
Create table materialreceiptdetailaddnlinfo( 
	id varchar(128),
	tenantId varchar(128),
	lotNo varchar(50),
	serialNo varchar(50),
	manufactureDate bigint,
	oldReceiptNumber varchar(50),
	receivedDate bigint,
	expiryDate bigint,
	receiptDetailId varchar(50) ,	version bigint
);
alter table materialreceiptdetailaddnlinfo add constraint pk_materialreceiptdetailaddnlinfo primary key (id,tenantId);
create sequence seq_materialreceiptdetailaddnlinfo;



