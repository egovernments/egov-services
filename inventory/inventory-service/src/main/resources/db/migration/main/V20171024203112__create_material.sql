create table material (
      id character varying(50) NOT NULL,
      code varchar(50),
      name varchar(256) NOT NULL,
      description varchar(1024) NOT NULL,
      oldcode varchar(50) ,
      materialtype varchar(256) NOT NULL,
      baseuom varchar(256) NOT NULL,
      inventorytype varchar(256),
      status varchar(256),
      purchaseuom varchar(256) NOT NULL,
      expenseaccount varchar(256) NOT NULL,
      minquality bigint NOT NULL,
      maxquality bigint NOT NULL ,
      stockinguom varchar(256) NOT NULL,
      materialclass varchar(256),
      reorderlevel bigint NOT NULL,
      reorderquantity bigint NOT NULL,
      materialcontroltype varchar(256) NOT NULL,
      model varchar(256),
      manufacturepartno varchar(256),
      technicalspecs varchar(256) ,
      termsofdelivery varchar(256) ,
      overridematerialcontroltype boolean,
      createdby varchar(50),
      createdtime bigint,
      lastmodifiedby varchar(50),
      lastmodifiedtime bigint,
      tenantid varchar(256) NOT NULL
      );

alter table material add constraint pk_material primary key (name, tenantid);

create sequence seq_material;

