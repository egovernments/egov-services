CREATE TABLE scrap(
	id character varying(50) NOT NULL,
	tenantid varchar(256) NOT NULL,
	storeCode character varying(50),
	scrapNumber character varying(50),
	scrapdate bigint,
	description character varying(500),
	status character varying(50),
	workflowdetailid character varying(50),
	stateid character varying(50),
	designation character varying(50),
        createdby varchar(50),
        createdtime bigint,
        lastmodifiedby varchar(50),
        lastmodifiedtime bigint
       );

alter table scrap add constraint pk_scrap primary key (id,tenantId);
create sequence seq_scrap;

CREATE TABLE scrapDetail(
	id character varying(50) NOT NULL,
	tenantid varchar(256) NOT NULL,
	scrapNumber character varying(50),
	material character varying(50),
	uom character varying(50),
	lotnumber character varying(50),
	expirydate bigint,
	scrapReason character varying(500),
	quantity numeric (13,2),
	disposalQuantity numeric (13,2),
	scrapValue numeric (13,2),
        createdby varchar(50),
        createdtime bigint,
        lastmodifiedby varchar(50),
        lastmodifiedtime bigint
       );

alter table scrapDetail add constraint pk_scrapDetail primary key (id,tenantId);
create sequence seq_scrapDetail;
