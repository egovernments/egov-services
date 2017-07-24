
CREATE TABLE egpt_titletransfer(
    id bigserial NOT NULL,
    tenantId character varying(128) NOT NULL,
    upicNo character varying NOT NULL,
    transferReason character varying NOT NULL,
    registrationDocNo character varying(15),
    registrationDocDate timestamp without time zone,
    departmentGuidelineValue numeric NOT NULL,
    partiesConsiderationValue numeric,
    courtOrderNumber integer,
    subRegOfficeName character varying(15),
    titleTrasferFee numeric,
    stateId character varying,
    receiptnumber character varying,
    receiptdate timestamp without time zone,
    createdby character varying,
    lastmodifiedby character varying,
    createdtime bigint,
    lastmodifiedtime bigint,
    applicationNo character varying,
    CONSTRAINT pk_egpt_titletransfer PRIMARY KEY (id)
);

CREATE SEQUENCE seq_egpt_titletransfer;
ALTER TABLE egpt_titletransfer ALTER COLUMN id SET DEFAULT nextval('seq_egpt_titletransfer');


CREATE TABLE egpt_titletransfer_owner(
    id bigserial NOT NULL,
    titletransfer bigint NOT NULL,
    owner bigint,
    isPrimaryOwner boolean,
    isSecondaryOwner boolean,
    ownerShipPercentage integer,
    ownerType character varying(32),
    createdby character varying,
    lastmodifiedby character varying,
    createdtime bigint,
    lastmodifiedtime bigint,
    CONSTRAINT pk_egpt_titletransfer_owner PRIMARY KEY(id)
);


CREATE SEQUENCE seq_egpt_titletransfer_owner;
ALTER TABLE egpt_titletransfer_owner ALTER COLUMN id SET DEFAULT nextval('seq_egpt_titletransfer_owner');
ALTER TABLE  egpt_titletransfer_owner ADD CONSTRAINT fk_egpt_titletransfer_owner FOREIGN KEY(titletransfer) REFERENCES egpt_titletransfer(id);



CREATE TABLE egpt_titletransfer_address(
    id bigserial NOT NULL,
    tenantid character varying,
    latitude numeric,
    longitude numeric,
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
    titletransfer bigint,
    CONSTRAINT pk_egpt_titletransfer_address PRIMARY KEY (id)
);
CREATE SEQUENCE seq_egpt_titletransfer_address;
ALTER TABLE egpt_titletransfer_address ALTER COLUMN id SET DEFAULT nextval('seq_egpt_titletransfer_address');
ALTER TABLE egpt_titletransfer_address ADD CONSTRAINT fk_egpt_titletransfer_address FOREIGN KEY(titletransfer) REFERENCES egpt_titletransfer(id);

CREATE TABLE egpt_titletransfer_document(
    id bigserial NOT NULL,
    filestore character varying,
    createdby character varying,
    lastmodifiedby character varying,
    createdtime bigint,
    lastmodifiedtime bigint,
    titletransfer bigint,
    documenttype character varying,
    CONSTRAINT pk_egpt_titletransfer_document PRIMARY KEY(id)
);

CREATE SEQUENCE seq_egpt_titletransfer_document;
ALTER TABLE egpt_titletransfer_document ALTER COLUMN id SET DEFAULT nextval('seq_egpt_titletransfer_document');
ALTER TABLE  egpt_titletransfer_document ADD CONSTRAINT fk_egpt_titletransfer_document FOREIGN KEY(titletransfer) REFERENCES egpt_titletransfer(id);


CREATE TABLE egpt_property_history(
    id bigint,
    tenantid character varying(128) NOT NULL,
    upicnumber character varying(128) NOT NULL,
    oldUpicNumber character varying(128),
    vltUpicNumber character varying(128),
    creationreason character varying(256) NOT NULL,
    assessmentdate timestamp without time zone,
    occupancydate timestamp without time zone NOT NULL,
    gisrefno character varying(32),
    isauthorised boolean,
    isunderworkflow boolean,
    active boolean default true,
    channel character varying(16) NOT NULL,
    createdby character varying,
    lastmodifiedby character varying,
    createdtime bigint,
    lastmodifiedtime bigint,
    demands jsonb
);

CREATE TABLE egpt_address_history(
    id bigint,
    tenantid character varying,
    latitude numeric,
    longitude numeric,
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
    property bigint
);

CREATE TABLE egpt_propertydetails_history(
    id bigint,
    source character varying(64),
    regddocno character varying(64),
    regddocdate timestamp without time zone,
    reason character varying(16),
    status character varying(8),
    isverified boolean,
    verificationdate timestamp without time zone,
    isexempted boolean,
    exemptionreason character varying(32),
    propertytype character varying(16) NOT NULL,
    category character varying(16),
    usage character varying(16),
    department character varying(16),
    apartment character varying(16),
    sitelength numeric,
    sitebreadth numeric,
    sitalarea numeric NOT NULL,
    totalbuiltuparea numeric,
    undividedshare numeric,
    nooffloors integer,
    issuperstructure boolean,
    landowner character varying(128),
    floortype character varying(16),
    woodtype character varying(16),
    rooftype character varying(16),
    walltype character varying(16),
    stateid character varying,
    applicationno character varying(64),
    createdby character varying,
    lastmodifiedby character varying,
    createdtime bigint,
    lastmodifiedtime bigint,
    property bigint,
    taxCalculations jsonb
);

CREATE TABLE egpt_floors_history(
    id bigint,
    floorno character varying(16) NOT NULL,
    createdby character varying,
    lastmodifiedby character varying,
    createdtime bigint,
    lastmodifiedtime bigint,
    propertydetails bigint
);

CREATE TABLE egpt_unit_history(
    id bigint,
    unitno integer NOT NULL,
    unittype character varying(16),
    length numeric,
    width numeric,
    builtuparea numeric NOT NULL,
    assessablearea numeric,
    bpabuiltuparea numeric,
    bpano character varying(16),
    bpadate timestamp without time zone,
    usage character varying(16) NOT NULL,
    occupancy character varying(16) NOT NULL,
    occupiername character varying(128),
    firmname character varying(128),
    rentcollected numeric,
    structure character varying(16) NOT NULL,
    age character varying(16),
    exemptionreason character varying(32),
    isstructured boolean,
    occupancydate timestamp without time zone,
    constcompletiondate timestamp without time zone,
    manualarv numeric,
    arv numeric,
    electricmeterno character varying(64),
    watermeterno character varying(64),
    createdby character varying,
    lastmodifiedby character varying,
    createdtime bigint,
    lastmodifiedtime bigint,
    floor bigint,
    parentid bigint  DEFAULT 0,
    isAuthorised boolean DEFAULT true
);

CREATE TABLE egpt_document_history(
    id bigint,
    filestore character varying,
    createdby character varying,
    lastmodifiedby character varying,
    createdtime bigint,
    lastmodifiedtime bigint,
    propertydetails bigint,
    documenttype character varying(128)
);

CREATE TABLE egpt_vacantland_history(
    id bigint,
    surveynumber character varying(64),
    pattanumber character varying(64),
    marketvalue numeric,
    capitalvalue numeric,
    layoutapprovedauth character varying(64),
    layoutpermissionno character varying(64),
    layoutpermissiondate timestamp without time zone,
    resdplotarea numeric,
    nonresdplotarea numeric,
    createdby character varying,
    lastmodifiedby character varying,
    createdtime bigint,
    lastmodifiedtime bigint,
    property bigint
);

CREATE TABLE egpt_property_owner_history(
    id bigint,
    property bigint NOT NULL,
    owner bigint,
    isPrimaryOwner boolean,
    isSecondaryOwner boolean,
    ownerShipPercentage integer,
    ownerType character varying(32),
    createdby character varying,
    lastmodifiedby character varying,
    createdtime bigint,
    lastmodifiedtime bigint
);

CREATE TABLE egpt_propertylocation_history(
    id bigint,
    revenueboundary integer,
    locationboundary integer,
    adminboundary integer,
    northboundedby character varying(256),
    eastboundedby character varying(256),
    westboundedby character varying(256),
    southboundedby character varying(256),
    createdby character varying,
    lastmodifiedby character varying,
    createdtime bigint,
    lastmodifiedtime bigint,
    property bigint
);

