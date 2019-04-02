CREATE INDEX IF NOT EXISTS idx_egbs_demand_id ON egbs_demand(id);
CREATE INDEX IF NOT EXISTS idx_egbs_demand_consumercode ON egbs_demand(consumercode);
CREATE INDEX IF NOT EXISTS idx_egbs_demand_consumertype ON egbs_demand(consumertype);
CREATE INDEX IF NOT EXISTS idx_egbs_demand_businessservice ON egbs_demand(businessservice);
CREATE INDEX IF NOT EXISTS idx_egbs_demand_payer ON egbs_demand(payer);
CREATE INDEX IF NOT EXISTS idx_egbs_demand_taxperiodfrom ON egbs_demand(taxperiodfrom);
CREATE INDEX IF NOT EXISTS idx_egbs_demand_taxperiodto ON egbs_demand(taxperiodto);
CREATE INDEX IF NOT EXISTS idx_egbs_demand_tenantid ON egbs_demand(tenantid);

CREATE INDEX IF NOT EXISTS idx_egbs_demanddetail_tenantid ON egbs_demanddetail(tenantid);
CREATE INDEX IF NOT EXISTS idx_egbs_demanddetail_demandid ON egbs_demanddetail(demandid);

CREATE INDEX IF NOT EXISTS idx_egbs_bill_id ON egbs_bill(id);
CREATE INDEX IF NOT EXISTS idx_egbs_bill_isactive ON egbs_bill(isactive);
CREATE INDEX IF NOT EXISTS idx_egbs_bill_tenantid ON egbs_bill(tenantid);

CREATE INDEX IF NOT EXISTS idx_egbs_billdetail_businessservice ON egbs_billdetail(businessservice);
CREATE INDEX IF NOT EXISTS idx_egbs_billdetail_consumercode ON egbs_billdetail(consumercode);
CREATE INDEX IF NOT EXISTS idx_egbs_billdetail_tenantid ON egbs_billdetail(tenantid);