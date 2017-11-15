
Create table indent( 
	id varchar(50),
	tenantId varchar(128),
	issueStore varchar(50),
	indentStore varchar(50),
	indentDate bigint,
	indentNumber varchar(64),
	indentType varchar(14),
	indentPurpose varchar(21),
	inventoryType varchar(10),
	expectedDeliveryDate bigint,
	materialHandOverTo varchar(128),
	narration varchar(1000),
	indentStatus varchar(8),
	department varchar(50),
	totalIndentValue numeric (13,2),
	fileStoreId varchar(50),
	indentCreatedBy varchar(128),
	designation varchar(50),
	stateId bigint,
	createdby bigint,
	createdTime timestamp without time zone,
	lastmodifiedby bigint,
	lastModifiedTime timestamp without time zone,
	version bigint
);
alter table indent add constraint pk_indent primary key (indentNumber,tenantId);
create sequence seq_indent;
