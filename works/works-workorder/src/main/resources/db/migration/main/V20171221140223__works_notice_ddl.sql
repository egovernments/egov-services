create table egw_notice
(
  id character varying(256) not null,
  tenantid character varying(256) not null,
  letterofacceptance character varying(100) not null,
  noticenumber character varying(100) not null,
  closingline character varying(100),
  daysofreply numeric,
  createdby character varying(256) NOT NULL,
  createdtime bigint NOT NULL,
  lastmodifiedby character varying(256),
  lastmodifiedtime bigint,
  deleted boolean DEFAULT false,
  constraint pk_egw_notice PRIMARY KEY(id, tenantId)
  );

create table egw_notice_details
(
  id character varying(256) not null,
  tenantid character varying(256) not null,
  notice character varying(256) not null,
  remarks character varying(1024),
  createdby character varying(256) NOT NULL,
  createdtime bigint NOT NULL,
  lastmodifiedby character varying(256),
  lastmodifiedtime bigint,
  deleted boolean DEFAULT false,
  constraint pk_egw_noticedetails PRIMARY KEY(id, tenantId)
  );