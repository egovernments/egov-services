create table purchaseorder( 
	id varchar(50) not null,
	store varchar(50) not null,
	purchaseOrderNumber varchar(50) not null,
	purchaseOrderDate bigint not null,
	purchaseType varchar(25) not null,
	rateType varchar(25) not null,
	supplier varchar(50) not null,
	advanceAmount numeric (13,2),
	advancePercentage numeric (13,2),
	expectedDeliveryDate bigint,
	deliveryTerms varchar(512),
	paymentTerms varchar(512),
	remarks varchar(1000),
	status varchar(50) not null,
	stateId bigint,
	fileStoreId varchar(50),
	designation varchar(50),
	createdby bigint,
		createdTime timestamp without time zone,
		lastmodifiedby bigint,
		lastModifiedTime timestamp without time zone,
		tenantId varchar(250),
			version bigint  default 0
);
alter table purchaseorder add constraint pk_purchaseorder primary key (purchaseOrderNumber,tenantId);
create sequence seq_purchaseorder;

create table purchaseorderdetail( 
	id varchar(50) not null,
	tenantId varchar(128) not null,
	PurchaseOrder varchar(50) not null,
	material varchar(50) not null,
	orderNumber bigint,
	uom varchar(50) not null,
	priceList varchar(50),
	orderQuantity numeric (13,2) not null,
	receivedQuantity numeric (13,2),
	unitPrice numeric (13,2) not null,
	description varchar(512),
	deleted boolean default false,
	version bigint  default 0
);
alter table purchaseorderdetail add constraint pk_purchaseorderdetail primary key (id,tenantId);
create sequence seq_purchaseorderdetail;

create table purchaseindentdetail( 
	id varchar(50) not null,
	tenantId varchar(128) not null,
	indentDetail varchar(50) not null ,
	PurchaseOrderDetail varchar(50) not null,
	quantity numeric (13,2) not null,
	createdby bigint,
		createdTime timestamp without time zone,
		lastmodifiedby bigint,
		lastModifiedTime timestamp without time zone,
		deleted boolean default false,
   	version bigint default 0
);
alter table purchaseindentdetail add constraint pk_purchaseindentdetail primary key (id,tenantId);
create sequence seq_purchaseindentdetail;
