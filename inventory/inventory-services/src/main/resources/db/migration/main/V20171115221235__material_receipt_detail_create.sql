
Create table materialreceiptdetail( 
	id varchar(128),
	tenantId varchar(128),
	material varchar(50),
	uomNo varchar(50),
	orderNumber character varying(128),
	PoDetailId varchar(50),
	receivedQty numeric (13,2),
	acceptedQty numeric (13,2),
	unitRate numeric (13,2),
	asset varchar(50),
	voucherHeader varchar(50),
	rejectionRemark varchar(50),
	isScrapItem boolean,
	remarks varchar(512),
    createdby character varying(128),
    createdTime bigint,
    lastmodifiedby character varying(128),
    lastModifiedTime bigint,
	mrnNumber varchar(50) ,	version bigint
);
alter table materialreceiptdetail add constraint pk_materialreceiptdetail primary key (id,tenantId);
create sequence seq_materialreceiptdetail;

