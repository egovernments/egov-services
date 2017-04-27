update eg_role set tenantid = 'default' where tenantid = 'ap.public' and code not in('CITIZEN','EMPLOYEE');

delete from eg_address where id = 1;

ALTER TABLE eg_user ALTER COLUMN mobilenumber SET NOT NULL;
