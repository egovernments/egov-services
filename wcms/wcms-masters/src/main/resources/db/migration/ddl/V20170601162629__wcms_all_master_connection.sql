CREATE TABLE "egwtr_waterconnection" (
	"id" serial NOT NULL,
	"tenantid" varchar NOT NULL,
	"connectiontype" varchar NOT NULL,
	"billingtype" varchar NOT NULL,
	"categorytype" bigint NOT NULL,  
	"hscpipesizetype" bigint NOT NULL,
	"supplytype" varchar NOT NULL,
	"sourcetype" bigint NOT NULL,
	"connectionstatus" varchar NOT NULL,
	"sumpcapacity" bigint NOT NULL,
	"donationcharge" varchar NOT NULL,
	"numberoftaps" bigint NOT NULL,
	"numberofpersons" bigint NOT NULL,
	"parentconnectionid" bigint NOT NULL,
	"legacyconsumernumber" varchar NOT NULL,
	"acknowledgmentnumber" varchar NOT NULL,
	"consumernumber" varchar NOT NULL,
	CONSTRAINT egwtr_water_connection_pk PRIMARY KEY (id)
) WITH (
  OIDS=FALSE
);



CREATE TABLE "egwtr_meter" (
	"id" serial NOT NULL,
	"metermake" varchar NOT NULL,
	"meterreading" varchar NOT NULL,
	"auditdetails" bigint NOT NULL,
	"tenantid" varchar NOT NULL,
	"connectionid" bigint NOT NULL,
	CONSTRAINT egwtr_meter_pk PRIMARY KEY (id)
) WITH (
  OIDS=FALSE
);



CREATE TABLE "egwtr_property" (
	"id" serial NOT NULL,
	"usagetype" varchar NOT NULL,
	"propertytype" varchar NOT NULL,
	"address" varchar NOT NULL,
	"connectionid" bigint NOT NULL,
	CONSTRAINT egwtr_property_pk PRIMARY KEY (id)
) WITH (
  OIDS=FALSE
);



CREATE TABLE "egwtr_estimationcharge" (
	"id" serial NOT NULL,
	"connectionid" bigint NOT NULL,
	"material" bigint NOT NULL,
	"existingdistributionpipeline" varchar NOT NULL,
	"pipelinetohomedistance" bigint NOT NULL,
	"estimationcharges" bigint NOT NULL,
	"supervisioncharges" bigint NOT NULL,
	"materialcharges" bigint NOT NULL,
	"auditdetails" bigint NOT NULL,
	"tenantid" varchar NOT NULL,
	CONSTRAINT egwtr_estimationcharge_pk PRIMARY KEY (id)
) WITH (
  OIDS=FALSE
);



CREATE TABLE "egwtr_workorder" (
	"id" serial NOT NULL,
	"connectionid" bigint NOT NULL,
	"remarks" TEXT NOT NULL,
	"auditdetails" bigint NOT NULL,
	"tenantid" varchar NOT NULL,
	CONSTRAINT egwtr_workorder_pk PRIMARY KEY (id)
) WITH (
  OIDS=FALSE
);



CREATE TABLE "egwtr_meterreading" (
	"id" serial NOT NULL,
	"connectionid" bigint NOT NULL,
	"reading" bigint NOT NULL,
	"auditdetails" bigint NOT NULL,
	"tenantid" varchar NOT NULL,
	CONSTRAINT egwtr_meterreading_pk PRIMARY KEY (id)
) WITH (
  OIDS=FALSE
);



CREATE TABLE "egwtr_timeline" (
	"id" serial NOT NULL,
	"connectionid" bigint NOT NULL,
	"remarks" TEXT NOT NULL,
	"assigner" bigint NOT NULL,
	"assignee" bigint NOT NULL,
	"workflowdetails" bigint NOT NULL,
	"auditdetails" bigint NOT NULL,
	"tenantid" varchar NOT NULL,
	CONSTRAINT egwtr_timeline_pk PRIMARY KEY (id)
) WITH (
  OIDS=FALSE
);



CREATE TABLE "egwtr_documents" (
	"document" bigint NOT NULL,
	"name" varchar NOT NULL,
	"filestoreid" varchar NOT NULL,
	"id" serial NOT NULL,
	"connectionid" serial NOT NULL,
	CONSTRAINT egwtr_documents_pk PRIMARY KEY (id)
) WITH (
  OIDS=FALSE
);



CREATE TABLE "egwtr_auditdetails" (
	"id" serial NOT NULL,
	"connectionid" bigint NOT NULL,
	"createdby" varchar NOT NULL,
	"lastmodifiedby" varchar NOT NULL,
	"createdtime" TIMESTAMP NOT NULL,
	"lastmodifiedtime" TIMESTAMP NOT NULL,
	CONSTRAINT egwtr_auditdetails_pk PRIMARY KEY (id)
) WITH (
  OIDS=FALSE
);



ALTER TABLE "egwtr_meter" ADD CONSTRAINT "egwtr_meter_fk0" FOREIGN KEY (connectionid) REFERENCES "egwtr_waterconnection"(id);

ALTER TABLE "egwtr_property" ADD CONSTRAINT "egwtr_property_fk0" FOREIGN KEY (connectionid) REFERENCES "egwtr_waterconnection"(id);

ALTER TABLE "egwtr_estimationcharge" ADD CONSTRAINT "egwtr_estimationcharge_fk0" FOREIGN KEY (connectionid) REFERENCES "egwtr_waterconnection"(id);

ALTER TABLE "egwtr_workorder" ADD CONSTRAINT "egwtr_workorder_fk0" FOREIGN KEY (connectionid) REFERENCES "egwtr_waterconnection"(id);

ALTER TABLE "egwtr_meterreading" ADD CONSTRAINT "egwtr_meterreading_fk0" FOREIGN KEY (connectionid) REFERENCES "egwtr_waterconnection"(id);

ALTER TABLE "egwtr_timeline" ADD CONSTRAINT "egwtr_timeline_fk0" FOREIGN KEY (connectionid) REFERENCES "egwtr_waterconnection"(id);

ALTER TABLE "egwtr_documents" ADD CONSTRAINT "egwtr_documents_fk0" FOREIGN KEY (connectionid) REFERENCES "egwtr_waterconnection"(id);

ALTER TABLE "egwtr_auditdetails" ADD CONSTRAINT "egwtr_auditdetails_fk0" FOREIGN KEY (connectionid) REFERENCES "egwtr_waterconnection"(id);

ALTER TABLE "egwtr_waterconnection" ADD CONSTRAINT "egwtr_connection_category_fk0" FOREIGN KEY (categorytype,tenantid) REFERENCES "egwtr_category"(id,tenantid);

ALTER TABLE "egwtr_waterconnection" ADD CONSTRAINT "egwtr_connection_pipesize_fk0" FOREIGN KEY (hscpipesizetype,tenantid) REFERENCES "egwtr_pipesize"(id,tenantid);

