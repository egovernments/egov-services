create table materialtypestoremapping(
      id character varying(50) NOT NULL,
      materialtype varchar(50),
      store varchar(50) NOT NULL,
      chartofaccount varchar(50),
      active boolean,
      createdby varchar(50),
      createdtime bigint,
      lastmodifiedby varchar(50),
      lastmodifiedtime bigint,
      tenantid varchar(256) NOT NULL,
      deleted boolean default false,
      version bigint
);

alter table materialtypestoremapping add constraint pk_materialtypestoremapping primary key (id,tenantid);

create sequence seq_materialtypestoremapping;
