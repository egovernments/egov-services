CREATE TABLE egpt_property(
    id bigint NOT NULL,
    tenantid character varying NOT NULL,
    upicnumber character varying NOT NULL,
    oldUpicNumber character varying,
    vltUpicNumber character varying,
    creationreason character varying NOT NULL,
    assessmentdate timestamp without time zone,
    occupancydate timestamp without time zone NOT NULL,
    gisrefno character varying,
    isauthorised boolean,
    isunderworkflow boolean,
    active boolean
    default true,
    channel character varying NOT NULL,
    createdby character varying,
    lastmodifiedby character varying,
    createdtime bigint,
    lastmodifiedtime bigint,
    CONSTRAINT pk_egpt_property PRIMARY KEY (id)
);
CREATE SEQUENCE seq_egpt_property;

CREATE TABLE egpt_address(
    id bigint NOT NULL,
    tenantid character varying,
    latitude integer,
    longitude integer,
    addressId character varying,
    addressNumber character varying,
    addressLine1 character varying,
    addressLine2 character varying,
    landmark character varying,
    city character varying,
    pincode character varying,
    detail character varying,
    createdby character varying,
    lastmodifiedby character varying,
    createdtime bigint,
    lastmodifiedtime bigint,
    property integer,
    CONSTRAINT pk_egpt_address PRIMARY KEY (id)
);
CREATE SEQUENCE seq_egpt_address;

ALTER TABLE egpt_address ADD CONSTRAINT fk_egpt_address_property FOREIGN KEY(property) REFERENCES egpt_property(id);



CREATE TABLE egpt_propertydetails(
    id bigint NOT NULL,
    source character varying,
    regddocno character varying,
    regddocdate character varying,
    reason character varying,
    status character varying,
    isverified boolean,
    verificationdate timestamp without time zone,
    isexempted boolean,
    exemptionreason character varying,
    propertytype character varying NOT NULL,
    category character varying,
    usage character varying,
    department character varying,
    apartment character varying,
    sitelength integer,
    sitebreadth integer,
    sitalarea integer NOT NULL,
    totalbuiltuparea integer,
    undividedshare integer,
    nooffloors integer,
    issuperstructure boolean,
    landowner character varying,
    floortype character varying,
    woodtype character varying,
    rooftype character varying,
    walltype character varying,
    stateid character varying,
    applicationno character varying,
    createdby character varying,
    lastmodifiedby character varying,
    createdtime bigint,
    lastmodifiedtime bigint,
    property integer,
    CONSTRAINT pk_egpt_propertydetails PRIMARY KEY (id)
);



CREATE SEQUENCE seq_egpt_propertydetails;

ALTER TABLE egpt_propertydetails  ADD CONSTRAINT fk_egpt_propertydetail_property FOREIGN KEY(property) REFERENCES egpt_property(id);

CREATE TABLE egpt_floors(
    id bigint NOT NULL,
    floorno character varying NOT NULL,
    createdby character varying,
    lastmodifiedby character varying,
    createdtime bigint,
    lastmodifiedtime bigint,
    propertydetails integer,
    CONSTRAINT pk_egpt_floors PRIMARY KEY (id)
);



CREATE SEQUENCE seq_egpt_floors;

ALTER TABLE  egpt_floors ADD CONSTRAINT fk_egpt_floors_propertydetails FOREIGN KEY(propertydetails) REFERENCES egpt_propertydetails(id);


CREATE TABLE egpt_unit(
    id bigint NOT NULL,
    unitno integer NOT NULL,
    unittype character varying,
    length integer,
    width integer,
    builtuparea integer NOT NULL,
    assessablearea integer,
    bpabuiltuparea integer,
    bpano character varying,
    bpadate timestamp without time zone,
    usage character varying NOT NULL,
    occupancy character varying NOT NULL,
    occupiername character varying,
    firmname character varying,
    rentcollected integer,
    structure character varying NOT NULL,
    age character varying,
    exemptionreason character varying,
    isstructured boolean,
    occupancydate timestamp without time zone,
    constcompletiondate timestamp without time zone,
    manualarv integer,
    arv integer,
    electricmeterno character varying,
    watermeterno character varying,
    createdby character varying,
    lastmodifiedby character varying,
    createdtime bigint,
    lastmodifiedtime bigint,
    floor integer,
    CONSTRAINT pk_egpt_unit PRIMARY KEY (id)    
);
CREATE SEQUENCE seq_egpt_unit;
ALTER TABLE  egpt_unit ADD CONSTRAINT fk_egpt_unit_floor FOREIGN KEY(floor) REFERENCES egpt_floors(id);

CREATE TABLE egpt_document(
    id bigint NOT NULL,
    filestore character varying,
    createdby character varying,
    lastmodifiedby character varying,
    createdtime bigint,
    lastmodifiedtime bigint,
    propertydetails integer,
    CONSTRAINT egpt_document_pk PRIMARY KEY(id)
);

CREATE SEQUENCE seq_egpt_document;
ALTER TABLE  egpt_document ADD CONSTRAINT fk_egpt_document_propertydetails FOREIGN KEY(propertydetails) REFERENCES egpt_propertydetails(id);


CREATE TABLE egpt_documenttype(
    id bigint NOT NULL,
    name character varying,
    application character varying,
    document  bigint,
    createdby character varying,
    lastmodifiedby character varying,
    createdtime bigint,
    lastmodifiedtime bigint,
    CONSTRAINT egpt_documenttype_pk PRIMARY KEY(id)
);
CREATE SEQUENCE seq_egpt_documenttype;
ALTER TABLE  egpt_documenttype ADD CONSTRAINT fk_egpt_documenttype_document FOREIGN KEY(document) REFERENCES egpt_document(id);


CREATE TABLE egpt_vacantland(
    id bigint NOT NULL,
    surveynumber character varying,
    pattanumber character varying,
    marketvalue integer,
    capitalvalue integer,
    layoutapprovedauth character varying,
    layoutpermissionno character varying,
    layoutpermissiondate timestamp without time zone,
    resdplotarea integer,
    nonresdplotarea integer,
    createdby character varying,
    lastmodifiedby character varying,
    createdtime bigint,
    lastmodifiedtime bigint,
    property integer,
    CONSTRAINT egpt_vacantland_pk PRIMARY KEY(id)
);

CREATE SEQUENCE seq_egpt_vacantland;
ALTER TABLE  egpt_vacantland ADD CONSTRAINT fk_egpt_vacantland_property FOREIGN KEY(property) REFERENCES egpt_property(id);


CREATE TABLE egpt_property_owner(
    id bigint NOT NULL,
    property integer NOT NULL,
    owner integer,
    isPrimaryOwner boolean,
    isSecondaryOwner boolean,
    ownerShipPercentage integer,
    ownerType character varying,
    createdby character varying,
    lastmodifiedby character varying,
    createdtime bigint,
    lastmodifiedtime bigint,
    CONSTRAINT egpt_property_owner_pk PRIMARY KEY(id)
);


CREATE SEQUENCE seq_egpt_property_owner;
ALTER TABLE  egpt_property_owner ADD CONSTRAINT fk_egpt_property_owner_property FOREIGN KEY(property) REFERENCES egpt_property(id);



CREATE TABLE egpt_propertylocation(
    id bigint NOT NULL,
    revenueboundary integer,
    locationboundary integer,
    adminboundary integer,
    northboundedby character varying,
    eastboundedby character varying,
    westboundedby character varying,
    southboundedby character varying,
    createdby character varying,
    lastmodifiedby character varying,
    createdtime bigint,
    lastmodifiedtime bigint,
    property integer,
   CONSTRAINT egpt_propertylocation_pk PRIMARY KEY(id) 
);


CREATE SEQUENCE seq_egpt_propertylocation;

ALTER TABLE  egpt_propertylocation ADD CONSTRAINT fk_egpt_propertylocation FOREIGN KEY(property) REFERENCES egpt_property(id);

CREATE TABLE egpt_mstr_department(
    id bigint NOT NULL,
    tenantId character varying NOT NULL,
    code character varying NOT NULL,
    data jsonb NOT NULL,
    createdBy character varying,
    lastModifiedBy character varying,
    createdTime bigint,
    lastModifiedTime bigint,
    CONSTRAINT egpt_mstr_department_pkey PRIMARY KEY(id)
);

CREATE SEQUENCE seq_egpt_mstr_department;

CREATE TABLE egpt_mstr_floor(
    id bigint NOT NULL,
    tenantid character varying NOT NULL,
    code character varying NOT NULL,
    data jsonb NOT NULL,
    createdBy character varying,
    lastModifiedBy character varying,
    createdTime bigint,
    lastModifiedTime bigint,
     CONSTRAINT egpt_mstr_floor_pkey PRIMARY KEY(id)
);

CREATE SEQUENCE seq_egpt_mstr_floor;




CREATE TABLE egpt_mstr_occuapancy(
    id bigint NOT NULL,
    tenantid character varying NOT NULL,
    code character varying NOT NULL,
    data jsonb NOT NULL,
    createdBy character varying,
    lastModifiedBy character varying,
    createdTime bigint,
    lastModifiedTime bigint,
CONSTRAINT egpt_mstr_occuapancy_pkey PRIMARY KEY(id)
);

CREATE SEQUENCE seq_egpt_mstr_occuapancy;

CREATE TABLE egpt_mstr_property(
    id bigint NOT NULL,
    tenantid character varying NOT NULL,
    code character varying NOT NULL,
   data jsonb NOT NULL,
    createdBy character varying,
    lastModifiedBy character varying,
    createdTime bigint,
    lastModifiedTime bigint,
CONSTRAINT egpt_mstr_property_pkey PRIMARY KEY(id)
);

CREATE SEQUENCE seq_egpt_mstr_property;



CREATE TABLE egpt_mstr_roof(
    id bigint NOT NULL,
    tenantid character varying NOT NULL,
    code character varying NOT NULL,
    data jsonb NOT NULL,
    createdBy character varying,
    lastModifiedBy character varying,
    createdTime bigint,
    lastModifiedTime bigint,
CONSTRAINT egpt_mstr_roof_pkey PRIMARY KEY(id)
);


CREATE SEQUENCE seq_egpt_mstr_roof;


CREATE TABLE egpt_mstr_structure(
    id bigint NOT NULL,
    tenantid character varying NOT NULL,
    code character varying NOT NULL,
   data jsonb NOT NULL,
    createdBy character varying,
    lastModifiedBy character varying,
    createdTime bigint,
    lastModifiedTime bigint,
    CONSTRAINT egpt_mstr_structure_pkey PRIMARY KEY(id)

);


CREATE SEQUENCE seq_egpt_mstr_structure;


CREATE TABLE egpt_mstr_usage(
    id bigint NOT NULL,
    tenantid character varying NOT NULL,
    code character varying NOT NULL,
    data jsonb NOT NULL,
    createdBy character varying,
    lastModifiedBy character varying,
    createdTime bigint,
    lastModifiedTime bigint,
CONSTRAINT egpt_mstr_usage_pkey PRIMARY KEY(id)
);


CREATE SEQUENCE seq_egpt_mstr_usage;


CREATE TABLE egpt_mstr_wall(
    id bigint NOT NULL,
    tenantid character varying NOT NULL,
    code character varying NOT NULL,
    data jsonb NOT NULL,
    createdBy character varying,
    lastModifiedBy character varying,
    createdTime bigint,
    lastModifiedTime bigint,
    CONSTRAINT egpt_mstr_wall_pkey PRIMARY KEY(id)
);


CREATE SEQUENCE seq_egpt_mstr_wall;


CREATE TABLE egpt_mstr_wood(
    id bigint NOT NULL,
    tenantid character varying NOT NULL,
    code character varying NOT NULL,
    data jsonb NOT NULL,
    createdBy character varying,
    lastModifiedBy character varying,
    createdTime bigint,
    lastModifiedTime bigint,
CONSTRAINT egpt_mstr_wood_pkey PRIMARY KEY(id)
);
CREATE SEQUENCE seq_egpt_mstr_wood;
