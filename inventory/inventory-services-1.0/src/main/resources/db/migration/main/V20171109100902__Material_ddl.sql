Create table material(
	id varchar(50),
	tenantId varchar(128) NOT NULL ,
	code varchar(50),
	name varchar(50) NOT NULL,
	description varchar(1000) NOT NULL,
	oldCode varchar(50),
	materialType varchar(50) NOT NULL,
	baseUom varchar(50) NOT NULL,
	inventoryType varchar(10) NOT NULL,
	status varchar(50),
	purchaseUom varchar(50) NOT NULL,
	expenseAccount varchar(50),
	minQuantity numeric (13,2) NOT NULL,
	maxQuantity numeric (13,2) NOT NULL,
	stockingUom varchar(50) NOT NULL,
	materialClass varchar(11) NOT NULL,
	reorderLevel numeric (13,2) NOT NULL,
	reorderQuantity numeric (13,2) NOT NULL,
	model varchar(50),
	manufacturePartNo varchar(50),
	techincalSpecs varchar(50),
	termsOfDelivery varchar(50),
	inactivedate bigint,
	assetcategory character varying(50),
	lotcontrol boolean,
	shelfLifeControl boolean,
	serialNumber boolean,
	scrapable boolean,
	createdby varchar(50),
  createdtime bigint,
  lastmodifiedby varchar(50),
  lastmodifiedtime bigint,
  version bigint
);
alter table material add constraint pk_material primary key (code,tenantId);

create sequence seq_material;

create sequence seq_material_code_serial_no;
