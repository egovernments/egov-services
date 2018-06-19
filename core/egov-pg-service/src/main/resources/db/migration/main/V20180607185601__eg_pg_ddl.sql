CREATE TABLE "eg_pg_transactions" (
	"txn_id" VARCHAR(128) NOT NULL,
	"txn_amount" NUMERIC(15,2) NOT NULL,
	"txn_status" VARCHAR(64) NOT NULL,
	"gateway" VARCHAR(64) NOT NULL,
	"module" VARCHAR(64) NOT NULL,
	"order_id" VARCHAR(64) NOT NULL,
	"product_info" VARCHAR(512) NOT NULL,
	"user_name" VARCHAR(64) NOT NULL,
	"tenant_id" VARCHAR(128) NOT NULL,
	"gateway_txn_id" VARCHAR(128) NULL DEFAULT NULL,
	"gateway_payment_mode" VARCHAR(128) NULL DEFAULT NULL,
	"gateway_status_code" VARCHAR(128) NULL DEFAULT NULL,
	"gateway_status_msg" VARCHAR(128) NULL DEFAULT NULL,
	"created_time" BIGINT NULL DEFAULT NULL,
	"last_modified_time" BIGINT NULL DEFAULT NULL,
	PRIMARY KEY ("txn_id")
);

CREATE TABLE eg_pg_user (
    user_name character varying(64) NOT NULL,
    mobile_number character varying(50),
    email_id character varying(128),
    name character varying(100),
    tenant_id character varying(256) not null
);

CREATE TABLE "eg_pg_transactions_dump" (
	"txn_id" VARCHAR(128) NOT NULL,
	"txn_request" varchar NULL,
	"txn_response" JSONB NULL,
	"created_time" BIGINT NULL DEFAULT NULL,
	"last_modified_time" BIGINT NULL DEFAULT NULL,
	PRIMARY KEY ("txn_id")
);