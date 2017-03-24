
Create table EG_WF_MATRIX( 
	id bigint,
	department varchar(50),
	objectType varchar(50) NOT NULL,
	currentState varchar(50),
	currentStatus varchar(50),
	pendingActions varchar(50),
	currentDesignation varchar(50),
	additionalRule varchar(50),
	nextState varchar(50),
	nextAction varchar(50),
	nextDesignation varchar(50),
	nextStatus varchar(50),
	validActions varchar(50),
	fromQty numeric (13,2),
	toQty numeric (13,2),
	fromDate date,
	toDate date,
		createdby bigint,
		createddate timestamp without time zone,
		lastmodifiedby bigint,
		lastmodifieddate timestamp without time zone,
		version bigint,
		tenantId varchar(250)
);
alter table EG_WF_MATRIX add constraint pk_EG_WF_MATRIX primary key (id);
create sequence seq_EG_WF_MATRIX;
