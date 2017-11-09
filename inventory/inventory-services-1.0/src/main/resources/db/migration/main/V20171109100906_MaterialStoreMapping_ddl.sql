
Create table materialstoremapping( 
	id varchar(50),
	material varchar(50),
	store varchar(50),
	chartofAccount varchar(50),
	active boolean,
	createdby bigint,
		createdTime timestamp without time zone,
		lastmodifiedby bigint,
		lastModifiedTime timestamp without time zone,
		tenantId varchar(250),
			version bigint
);
alter table materialstoremapping add constraint pk_materialstoremapping primary key (id,tenantId);
create sequence seq_materialstoremapping;
