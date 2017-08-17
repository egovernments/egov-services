ALTER TABLE egtl_license
ADD COLUMN isTradeOwner  boolean NOT NULL DEFAULT FALSE;

ALTER TABLE egtl_license
DROP CONSTRAINT unq_tl_agrmtno;