create table materialstoremapping(
      id character varying(50) NOT NULL,
      material varchar(50),
      store varchar(50) NOT NULL,
      chartofaccount varchar(50),
      active boolean,
      createdby varchar(50),
      createdtime bigint,
      lastmodifiedby varchar(50),
      lastmodifiedtime bigint,
      tenantid varchar(256) NOT NULL,
      version bigint
);

alter table materialstoremapping add constraint pk_materialstoremapping primary key (id,tenantid);

create sequence seq_materialstoremapping;
