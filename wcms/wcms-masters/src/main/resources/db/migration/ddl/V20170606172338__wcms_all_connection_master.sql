CREATE TABLE "egwtr_waterconnection" (
	"id" serial NOT NULL,
	"tenantid" varchar NOT NULL,
	"connectiontype" varchar NOT NULL,
	"billingtype" varchar NOT NULL,
	"categorytype" bigint NOT NULL,
	"hscpipesizetype" bigint NOT NULL,
	"supplytype" varchar NOT NULL,
	"sourcetype" bigint NOT NULL,
	"connectionstatus" varchar DEFAULT NULL,
	"sumpcapacity" bigint NOT NULL,
	"donationcharge" varchar DEFAULT NULL,
	"numberofftaps" bigint NOT NULL,
	"numberofpersons" bigint NOT NULL,
	"parentconnectionid" bigint DEFAULT NULL,
	"legacyconsumernumber" varchar DEFAULT NULL,
	"acknowledgmentnumber" varchar NOT NULL,
	"consumernumber" varchar DEFAULT NULL,
	"createdby" varchar NOT NULL,
	"lastmodifiedby" varchar NOT NULL,
	"createdtime" TIMESTAMP NOT NULL,
	"lastmodifiedtime" TIMESTAMP NOT NULL,
	"propertyid" bigint NOT NULL,
	"usagetype" varchar NOT NULL,
	"propertytype" varchar NOT NULL,
	"propertyaddress" varchar NOT NULL,
	CONSTRAINT egwtr_waterconnection_pk PRIMARY KEY ("id","tenantid")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "egwtr_meter" (
	"id" serial NOT NULL,
	"metermake" varchar NOT NULL,
	"meterreading" varchar NOT NULL,
	"tenantid" varchar NOT NULL,
	"connectionid" bigint NOT NULL,
	"createdby" varchar NOT NULL,
	"lastmodifiedby" varchar NOT NULL,
	"createdtime" TIMESTAMP NOT NULL,
	"lastmodifiedtime" TIMESTAMP NOT NULL,
	CONSTRAINT egwtr_meter_pk PRIMARY KEY ("id")
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
	"tenantId" varchar NOT NULL,
	"createdby" varchar NOT NULL,
	"lastmodifiedby" varchar NOT NULL,
	"createdtime" TIMESTAMP NOT NULL,
	"lastmodifiedtime" TIMESTAMP NOT NULL,
	CONSTRAINT egwtr_estimationcharge_pk PRIMARY KEY ("id")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "egwtr_workorder" (
	"id" serial NOT NULL,
	"connectionid" bigint NOT NULL,
	"remarks" TEXT NOT NULL,
	"tenantid" varchar NOT NULL,
	"createdby" varchar NOT NULL,
	"lastmodifiedby" varchar NOT NULL,
	"createdtime" TIMESTAMP NOT NULL,
	"lastmodifiedtime" TIMESTAMP NOT NULL,
	CONSTRAINT egwtr_workorder_pk PRIMARY KEY ("id")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "egwtr_meterreading" (
	"id" serial NOT NULL,
	"connectionid" bigint NOT NULL,
	"reading" bigint NOT NULL,
	"tenantid" varchar NOT NULL,
	"createdby" varchar NOT NULL,
	"lastmodifiedby" varchar NOT NULL,
	"createdtime" TIMESTAMP NOT NULL,
	"lastmodifiedtime" TIMESTAMP NOT NULL,
	CONSTRAINT egwtr_meterreading_pk PRIMARY KEY ("id")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "egwtr_timeline" (
	"id" serial NOT NULL,
	"connectionid" bigint NOT NULL,
	"remarks" TEXT NOT NULL,
	"assigner" bigint NOT NULL,
	"assignee" bigint NOT NULL,
	"tenantid" varchar NOT NULL,
	"createdby" varchar NOT NULL,
	"lastmodifiedby" varchar NOT NULL,
	"createdtime" TIMESTAMP NOT NULL,
	"lastmodifiedtime" TIMESTAMP NOT NULL,
	"department" bigint NOT NULL,
	"designation" bigint NOT NULL,
	"approver" bigint NOT NULL,
	"comments" TEXT NOT NULL,
	"status" TEXT NOT NULL,
	CONSTRAINT egwtr_timeline_pk PRIMARY KEY ("id")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "egwtr_documentowner" (
    "id" serial NOT NULL,
    "document" bigint NOT NULL,
	"name" varchar NOT NULL,
	"fileStoreId" varchar NOT NULL,
	"connectionId" serial NOT NULL,
	"tenantid" varchar NOT NULL,
	CONSTRAINT egwtr_documentowner_pk PRIMARY KEY ("id")
) WITH (
  OIDS=FALSE
);




ALTER TABLE "egwtr_meter" ADD CONSTRAINT "egwtr_meter_fk0" FOREIGN KEY ("connectionid","tenantid") REFERENCES "egwtr_waterconnection"("id","tenantid");


ALTER TABLE "egwtr_estimationcharge" ADD CONSTRAINT "egwtr_estimationcharge_fk0" FOREIGN KEY ("connectionid","tenantId") REFERENCES "egwtr_waterconnection"("id","tenantid");


ALTER TABLE "egwtr_workorder" ADD CONSTRAINT "egwtr_workorder_fk0" FOREIGN KEY ("connectionid","tenantid") REFERENCES "egwtr_waterconnection"("id","tenantid");


ALTER TABLE "egwtr_meterreading" ADD CONSTRAINT "egwtr_meterreading_fk0" FOREIGN KEY ("connectionid","tenantid") REFERENCES "egwtr_waterconnection"("id","tenantid");


ALTER TABLE "egwtr_timeline" ADD CONSTRAINT "egwtr_timeline_fk0" FOREIGN KEY ("connectionid","tenantid") REFERENCES "egwtr_waterconnection"("id","tenantid");


ALTER TABLE "egwtr_documentowner" ADD CONSTRAINT "egwtr_documentowner_fk0" FOREIGN KEY ("connectionId","tenantid") REFERENCES "egwtr_waterconnection"("id","tenantid");

ALTER TABLE "egwtr_waterconnection" ADD CONSTRAINT "egwtr_connection_category_fk0" FOREIGN KEY (categorytype,tenantid) REFERENCES "egwtr_category"(id,tenantid);

ALTER TABLE "egwtr_waterconnection" ADD CONSTRAINT "egwtr_connection_pipesize_fk0" FOREIGN KEY (hscpipesizetype,tenantid) REFERENCES "egwtr_pipesize"(id,tenantid);

