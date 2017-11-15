alter table store add column officelocation character varying(100) DEFAULT 'foo' NOT NULL;
alter table store alter column officelocation drop DEFAULT;