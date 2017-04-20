-------------UPDATE PRIMARY KEY CONSTRAINT-------------

--EG_USER

ALTER TABLE eg_user DROP CONSTRAINT eg_user_pkey CASCADE;
ALTER TABLE eg_user ADD CONSTRAINT eg_user_pkey PRIMARY KEY (id, tenantId);