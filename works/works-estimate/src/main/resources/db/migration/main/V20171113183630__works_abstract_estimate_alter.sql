ALTER TABLE egw_abstractestimate DROP COLUMN workproposedasperdp;
ALTER TABLE egw_abstractestimate ADD COLUMN workproposedasperdp boolean not null default false;