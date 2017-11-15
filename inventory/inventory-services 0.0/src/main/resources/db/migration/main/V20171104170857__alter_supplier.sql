alter table supplier add column gstno character varying(50);
alter table supplier drop column bankname ;
alter table supplier add column bank character varying(100) NOT NULL;
alter table supplier add column bankbranch character varying(100);
