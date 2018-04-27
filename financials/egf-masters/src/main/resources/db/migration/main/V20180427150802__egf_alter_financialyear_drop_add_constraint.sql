
ALTER TABLE egf_financialyear DROP CONSTRAINT if exists pk_egf_financialyear cascade;

ALTER TABLE egf_financialyear ADD primary key (id,tenantid); 

ALTER TABLE egf_fiscalperiod DROP CONSTRAINT if exists fk_egf_fiscalperiod_financialyearid ;

alter table egf_fiscalperiod add constraint fk_egf_fiscalperiod_financialyearid  
FOREIGN KEY (financialyearid,tenantid) REFERENCES egf_financialyear(id,tenantid);