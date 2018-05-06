CREATE TABLE eg_wcms_connection_v2 (

tenantid character varying(256),
uuid character varying(64),
type character varying(64),
status character varying(64),
acknowledgmentNumber character varying(128),
connectionNumber character varying(128),
oldConnectionNumber character varying(128),
applicationType character varying(64),
billingType character varying(128),
pipesize character varying(64),
sourceType character varying(128),
numberOfTaps bigint,
numberOfPersons bigint,
parentConnection character varying(64),
property character varying(64),
location character varying(64),
additionaldetails JSONB,
createdby character varying(64),
lastmodifiedby character varying(64),
createdtime bigint,
lastmodifiedtime bigint,

CONSTRAINT uk_eg_wcms_connection_v2_uuid UNIQUE (uuid),
CONSTRAINT pk_eg_wcms_connection_v2 PRIMARY KEY (tenantid, connectionNumber)
);


CREATE TABLE eg_wcms_meter_v2(

connectionuuid character varying(128),
uuid character varying(256),
meterOwner character varying(64),
meterModel character varying(64),
meterCost bigint,
meterSlNo bigint,
createdby character varying(64),
lastmodifiedby character varying(64),
createdtime bigint,
lastmodifiedtime bigint,

CONSTRAINT pk_eg_wcms_meter_v2 PRIMARY KEY (uuid),
CONSTRAINT fk_eg_wcms_meter_v2 FOREIGN KEY (connectionuuid) REFERENCES eg_wcms_connection_v2(uuid)
);



CREATE TABLE eg_wcms_address_v2(

connectionuuid character varying(128),
uuid character varying(64),
latitude numeric(9,6),
longitude numeric(10,7),
addressId character varying(64),
addressNumber character varying(64),
addresstype character varying(64),
addressLine1 character varying(1024),
addressLine2 character varying(1024),
landmark character varying(1024),
city character varying(1024),
pincode character varying(6),
addressdetail character varying(2048),
createdby character varying(64),
lastmodifiedby character varying(64),
createdtime bigint,
lastmodifiedtime bigint,

CONSTRAINT pk_eg_wcms_address_v2 PRIMARY KEY (uuid),
CONSTRAINT fk_eg_wcms_address_v2 FOREIGN KEY (connectionuuid) REFERENCES eg_wcms_connection_v2(uuid)
);

CREATE TABLE eg_wcms_document_v2(

uuid character varying(64),
connectionuuid character varying(128),
documenttype character varying(64),
isActive boolean,
createdby character varying(64),
lastmodifiedby character varying(64),
createdtime bigint,
lastmodifiedtime bigint,

CONSTRAINT pk_eg_wcms_documents_v2 PRIMARY KEY (uuid, connectionuuid),
CONSTRAINT fk_eg_wcms_documents_v2 FOREIGN KEY (connectionuuid) REFERENCES eg_wcms_connection_v2(uuid)
);

CREATE TABLE eg_wcms_owner_v2(

connectionuuid character varying(128),
userid character varying(1024),
isActive boolean,
createdby character varying(64),
lastmodifiedby character varying(64),
createdtime bigint,
lastmodifiedtime bigint,

CONSTRAINT pk_eg_wcms_owner_v2 PRIMARY KEY (userid,connectionuuid),
CONSTRAINT fk_eg_wcms_owner_v2 FOREIGN KEY (connectionuuid) REFERENCES eg_wcms_connection_v2(uuid)
);
