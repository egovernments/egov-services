CREATE TABLE egwtr_connectionlocation(
    id bigint NOT NULL,
    revenueboundary integer,
    locationboundary integer,
    adminboundary integer,
    createdby character varying,
    lastmodifiedby character varying,
    createdtime TIMESTAMP ,
    lastmodifiedtime TIMESTAMP ,
   CONSTRAINT egwtr_connectionlocation_pk PRIMARY KEY(id) 
);

CREATE SEQUENCE seq_egwtr_connectionlocation;



CREATE TABLE egwtr_address(
    id bigint NOT NULL,
    tenantid character varying NOT NULL,
    latitude integer,
    longitude integer,
    addressId character varying,
    addressNumber character varying,
    addressLine1 character varying,
    addressLine2 character varying,
    landmark character varying,
    doorno character varying,
    city character varying,
    pincode character varying,
    detail character varying,
    route character varying,
    street  character varying,
    area character varying,
    roadname character varying,
    createdby character varying,
    lastmodifiedby character varying,
    createdtime TIMESTAMP ,
    lastmodifiedtime TIMESTAMP ,
    CONSTRAINT pk_egwtr_address PRIMARY KEY (id,tenantid)
);
CREATE SEQUENCE seq_egwtr_address;
