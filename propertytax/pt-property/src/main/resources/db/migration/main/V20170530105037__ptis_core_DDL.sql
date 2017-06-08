
CREATE TABLE egpt_property (
    id integer NOT NULL,
    tenantid character varying NOT NULL,
    upicno character varying NOT NULL,
    oldupicno character varying,
    vltupicno character varying,
    creationreason character varying NOT NULL,
    assessmentdate character varying NOT NULL,
    occupancydate character varying NOT NULL,
    gisrefno character varying,
    isauthorised boolean,
    isunderworkflow boolean,
    channel character varying NOT NULL,
    createdby character varying,
    createddate character varying,
    lastmodifiedby character varying,
    lastmodifieddate character varying,
    boundary_id character varying
);



CREATE SEQUENCE egpt_property_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE egpt_property_id_seq OWNED BY egpt_property.id;



ALTER TABLE ONLY egpt_property ALTER COLUMN id SET DEFAULT nextval('egpt_property_id_seq'::regclass);



ALTER TABLE ONLY egpt_property
    ADD CONSTRAINT egpt_property_pk PRIMARY KEY (id);



CREATE TABLE egpt_address (
    id integer NOT NULL,
    tenantid character varying NOT NULL,
    housenobldgapt character varying,
    streetroadline character varying,
    landmark character varying,
    arealocalitysector character varying,
    citytownvillage character varying,
    district character varying,
    subdistrict character varying,
    postoffice character varying,
    state character varying,
    country character varying,
    pincode character varying,
    type character varying,
    createdby character varying,
    createddate character varying,
    lastmodifiedby character varying,
    lastmodifieddate character varying,
    property_id integer
);



CREATE SEQUENCE egpt_address_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



ALTER SEQUENCE egpt_address_id_seq OWNED BY egpt_address.id;

ALTER TABLE ONLY egpt_address ALTER COLUMN id SET DEFAULT nextval('egpt_address_id_seq'::regclass);


ALTER TABLE ONLY egpt_address
    ADD CONSTRAINT egpt_address_pk PRIMARY KEY (id);


ALTER TABLE ONLY egpt_address
    ADD CONSTRAINT egpt_address_property_id_fkey FOREIGN KEY (property_id) REFERENCES egpt_property(id) DEFERRABLE INITIALLY DEFERRED;



CREATE TABLE egpt_propertydetails (
    id integer NOT NULL,
    tenantid character varying NOT NULL,
    regddocno character varying,
    regddocdate character varying,
    occupancydate character varying,
    reason character varying,
    status character varying,
    isverified boolean,
    verificationdate character varying,
    isexempted boolean,
    exemptionreason character varying,
    propertytype character varying,
    category character varying,
    usage character varying,
    department character varying,
    apartment character varying,
    length integer,
    breadth integer,
    sitalarea integer,
    totalbuiltuparea integer,
    undividedshare integer,
    nooffloors integer,
    issuperstructure boolean,
    landowner character varying,
    floortype character varying,
    woodtype character varying,
    rooftype character varying,
    walltype character varying,
    property_id integer,
    createdby character varying,
    createddate character varying,
    lastmodifiedby character varying,
    lastmodifieddate character varying
);



CREATE SEQUENCE egpt_propertydetail_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE egpt_propertydetail_id_seq OWNED BY egpt_propertydetails.id;


ALTER TABLE ONLY egpt_propertydetails ALTER COLUMN id SET DEFAULT nextval('egpt_propertydetail_id_seq'::regclass);


ALTER TABLE ONLY egpt_propertydetails
    ADD CONSTRAINT egpt_propertydetail_pk PRIMARY KEY (id);


ALTER TABLE ONLY egpt_propertydetails
    ADD CONSTRAINT egpt_propertydetail_property_id_fkey FOREIGN KEY (property_id) REFERENCES egpt_property(id) DEFERRABLE INITIALLY DEFERRED;


CREATE TABLE egpt_floors (
    id integer NOT NULL,
    tenantid character varying NOT NULL,
    floorno character varying NOT NULL,
    unitno character varying,
    type character varying,
    length integer,
    width integer,
    builtuparea integer NOT NULL,
    assessablearea integer,
    bpabuiltuparea integer,
    category character varying,
    usage character varying NOT NULL,
    occupancy character varying NOT NULL,
    structure character varying NOT NULL,
    depreciation character varying,
    occupiername character varying,
    firmname character varying,
    rentcollected integer,
    exemptionreason character varying,
    isstructured boolean,
    occupancydate character varying,
    constcompletiondate character varying,
    bpano character varying,
    bpadate character varying,
    manualarv integer,
    arv integer,
    electricmeterno character varying,
    watermeterno character varying,
    createdby character varying,
    createddate character varying,
    lastmodifiedby character varying,
    lastmodifieddate character varying,
    property_details_id integer
);



CREATE SEQUENCE egpt_floors_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE egpt_floors_id_seq OWNED BY egpt_floors.id;


ALTER TABLE ONLY egpt_floors ALTER COLUMN id SET DEFAULT nextval('egpt_floors_id_seq'::regclass);


ALTER TABLE ONLY egpt_floors
    ADD CONSTRAINT egpt_floors_pk PRIMARY KEY (id);

ALTER TABLE ONLY egpt_floors
    ADD CONSTRAINT egpt_floors_property_details_id_fkey FOREIGN KEY (property_details_id) REFERENCES egpt_propertydetails(id) DEFERRABLE INITIALLY DEFERRED;


CREATE TABLE egpt_documenttype (
    id integer NOT NULL,
    name character varying,
    application character varying
);


CREATE SEQUENCE egpt_documenttype_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE egpt_documenttype_id_seq OWNED BY egpt_documenttype.id;


ALTER TABLE ONLY egpt_documenttype ALTER COLUMN id SET DEFAULT nextval('egpt_documenttype_id_seq'::regclass);


ALTER TABLE ONLY egpt_documenttype
    ADD CONSTRAINT egpt_documenttype_pk PRIMARY KEY (id);


CREATE TABLE egpt_document (
    id integer NOT NULL,
    documenttype integer NOT NULL,
    filestore character varying,
    property_details_id integer
);



CREATE SEQUENCE egpt_document_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE egpt_document_id_seq OWNED BY egpt_document.id;


ALTER TABLE ONLY egpt_document ALTER COLUMN id SET DEFAULT nextval('egpt_document_id_seq'::regclass);

ALTER TABLE ONLY egpt_document
    ADD CONSTRAINT egpt_document_pk PRIMARY KEY (id);

ALTER TABLE ONLY egpt_document
    ADD CONSTRAINT egpt_document_property_details_id_fkey FOREIGN KEY (property_details_id) REFERENCES egpt_propertydetails(id) DEFERRABLE INITIALLY DEFERRED;


ALTER TABLE ONLY egpt_document
    ADD CONSTRAINT egpt_documenttype_fk FOREIGN KEY (documenttype) REFERENCES egpt_documenttype(id) DEFERRABLE INITIALLY DEFERRED;


CREATE TABLE egpt_vacantland (
    id integer NOT NULL,
    tenantid character varying NOT NULL,
    surveynumber character varying,
    pattanumber character varying,
    marketvalue integer,
    capitalvalue integer,
    layoutapprovedauth character varying,
    layoutpermissionno character varying,
    layoutpermissiondate character varying,
    resdplotarea integer,
    nonresdplotarea integer,
    createdby character varying,
    createddate character varying,
    lastmodifiedby character varying,
    lastmodifieddate character varying,
    property_details_id integer
);



CREATE SEQUENCE egpt_vacantland_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE egpt_vacantland_id_seq OWNED BY egpt_vacantland.id;


ALTER TABLE ONLY egpt_vacantland ALTER COLUMN id SET DEFAULT nextval('egpt_vacantland_id_seq'::regclass);

ALTER TABLE ONLY egpt_vacantland
    ADD CONSTRAINT egpt_vacantland_pk PRIMARY KEY (id);

ALTER TABLE ONLY egpt_vacantland
    ADD CONSTRAINT egpt_vacantland_property_details_id_fkey FOREIGN KEY (property_details_id) REFERENCES egpt_propertydetails(id) DEFERRABLE INITIALLY DEFERRED;


CREATE TABLE egpt_property_user (
    id integer NOT NULL,
    property_id integer NOT NULL,
    user_id character varying
);


CREATE SEQUENCE egpt_property_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE egpt_property_user_id_seq OWNED BY egpt_property_user.id;


ALTER TABLE ONLY egpt_property_user ALTER COLUMN id SET DEFAULT nextval('egpt_property_user_id_seq'::regclass);


ALTER TABLE ONLY egpt_property_user
    ADD CONSTRAINT egpt_property_user_pk PRIMARY KEY (id);


ALTER TABLE ONLY egpt_property_user
    ADD CONSTRAINT egpt_property_fk FOREIGN KEY (property_id) REFERENCES egpt_property(id) DEFERRABLE INITIALLY DEFERRED;











