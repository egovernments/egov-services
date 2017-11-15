
Create table indentdetail( 
	id varchar(50),
	indentNumber varchar(64) ,
	tenantId varchar(128),
	material varchar(50),
	uom varchar(50),
	parentIndentLine varchar(50),
	orderNumber numeric (13,2),
	projectCode varchar(50),
	asset varchar(50),
	indentQuantity numeric (13,2),
	totalProcessedQuantity numeric (13,2),
	indentIssuedQuantity numeric (13,2),
	poOrderedQuantity numeric (13,2),
	interstoreRequestQuantity numeric (13,2),
	deliveryTerms varchar(512),
	remarks varchar(512),

	version bigint
);
alter table indentdetail add constraint pk_indentdetail primary key (id);
create sequence seq_indentdetail;
