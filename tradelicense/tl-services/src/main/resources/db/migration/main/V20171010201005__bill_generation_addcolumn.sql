ALTER TABLE egtl_tradelicense_bill
ADD COLUMN applicationbillid varchar;

ALTER TABLE egtl_tradelicense_bill ALTER COLUMN billid SET NOT NULL;