
CREATE TABLE egpt_specialnotice(
id bigserial NOT NULL,
upicNo character varying NOT NULL,
tenantId character varying(128),
ulbName character varying,
ulbLogo character varying,
noticeDate timestamp without time zone,
noticeNumber character varying,
applicationNo character varying,
applicationDate timestamp without time zone,
PRIMARY KEY (id)
);

CREATE TABLE egpt_specialnotice_address(
sno bigserial,
notice bigint,
address bigint
);


ALTER TABLE egpt_specialnotice_address ADD constraint fk_egpt_specialnotice_address_notice FOREIGN KEY(notice) REFERENCES egpt_specialnotice(id);


CREATE TABLE egpt_specialnotice_owners(
sno bigserial NOT NULL,
notice bigint,
owner bigint,
PRIMARY KEY (sno)
);

ALTER TABLE egpt_specialnotice_owners ADD constraint fk_egpt_specialnotice_owners_notice FOREIGN KEY(notice) REFERENCES egpt_specialnotice(id);



CREATE TABLE egpt_specialnotice_floorspec(
id bigserial NOT NULL,
notice bigint,
floorNo character varying,
unitDetails character varying,
usage character varying,
construction character varying,
assessableArea character varying,
alv character varying,
rv character varying,
PRIMARY KEY (id)
);


CREATE TABLE egpt_specialnotice_tax_details(
id bigserial NOT NULL,
notice bigint,
totalTax numeric,
PRIMARY KEY (id)
);

ALTER TABLE egpt_specialnotice_tax_details ADD CONSTRAINT fk_egpt_specialnotice_tax_details_notice FOREIGN KEY(notice) REFERENCES egpt_specialnotice(id);

CREATE TABLE egpt_specialnotice_taxwise_details(
sno bigserial NOT NULL,
notice bigint,
taxdetails bigint,
taxName character varying NOT NULL,
taxDays numeric,	
taxValue numeric NOT NULL,
PRIMARY KEY (sno)
);

ALTER TABLE egpt_specialnotice_taxwise_details ADD CONSTRAINT fk_egpt_specialnotice_taxwise_details_notice FOREIGN KEY (notice) REFERENCES egpt_specialnotice(id);

ALTER TABLE egpt_specialnotice_taxwise_details ADD CONSTRAINT fk_egpt_specialnotice_taxwise_details_taxdetails FOREIGN KEY (taxdetails) REFERENCES egpt_specialnotice_tax_details(id);
