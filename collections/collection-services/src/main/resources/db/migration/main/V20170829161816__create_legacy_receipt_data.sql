CREATE SEQUENCE seq_legacy_receipt_header;

  
  CREATE TABLE egcl_legacy_receipt_header
(
   id bigint NOT NULL,
   legacyreceiptid bigint,
   receiptNo character varying(50) ,
   receiptDate bigint NOT NULL, 
   department character varying(125), 
   serviceName character varying(125), 
   consumerNo character varying(125), 
   consumerName character varying(256), 
   totalAmount double precision, 
   advanceAmount double precision, 
   adjustmentAmount double precision, 
   consumerAddress character varying(512), 
   payeeName character varying(256),
   instrumentType character varying(50) , 
   instrumentDate bigint NOT NULL, 
   instrumentNo character varying(256), 
   bankName character varying(256),
   manualreceiptnumber character varying(50) ,
   manualreceiptDate bigint,
   tenantid character varying(256) NOT NULL,
   remarks character varying(256),
   CONSTRAINT PK_legacy_egcl_receipt_header PRIMARY KEY (id,tenantid)
);
COMMENT ON COLUMN egcl_legacy_receipt_header.id IS 'PK of receipt header.';
COMMENT ON COLUMN egcl_legacy_receipt_header.legacyreceiptid IS 'Id of legacy Receipt';
COMMENT ON COLUMN egcl_legacy_receipt_header.receiptNo IS 'Receipt Number of legacy Receipt';
COMMENT ON COLUMN egcl_legacy_receipt_header.receiptDate IS 'Receipt Date of legacy Receipt';
COMMENT ON COLUMN egcl_legacy_receipt_header.department IS 'Department Name for the Legacy Receipt';
COMMENT ON COLUMN egcl_legacy_receipt_header.serviceName IS 'Service Name for the Legacy Receipt viz. Property tax, Water charges etc.';
COMMENT ON COLUMN egcl_legacy_receipt_header.consumerNo IS 'Consumer Number for the legacy Receipt like propertyID, ConnectionNumber etc.';
COMMENT ON COLUMN egcl_legacy_receipt_header.consumerName IS 'Consumer Name for the Legacy receipt';
COMMENT ON COLUMN egcl_legacy_receipt_header.totalAmount IS 'Total amount of the receipt';
COMMENT ON COLUMN egcl_legacy_receipt_header.advanceAmount IS 'Advance amount in the Given Receipt';
COMMENT ON COLUMN egcl_legacy_receipt_header.adjustmentAmount IS 'Adjustment amount in the Given Receipt';
COMMENT ON COLUMN egcl_legacy_receipt_header.consumerAddress IS 'Address of Consumer/Service for the Receipt in Legacy Record';
COMMENT ON COLUMN egcl_legacy_receipt_header.payeeName IS 'Paid at counter by';
COMMENT ON COLUMN egcl_legacy_receipt_header.instrumentType IS 'Instrument Type value : cash/cheque/dd/online';
COMMENT ON COLUMN egcl_legacy_receipt_header.instrumentDate IS 'Applicable in case of cheque/dd/online';
COMMENT ON COLUMN egcl_legacy_receipt_header.instrumentNo IS 'Cheque/DD Number in case of cheque/dd. Transaction Number in case of online payment';
COMMENT ON COLUMN egcl_legacy_receipt_header.bankName IS 'Drawn on bank name, applicable for cheque/dd';
COMMENT ON COLUMN egcl_legacy_receipt_header.manualreceiptnumber IS 'Manual Receipt Number of legacy Receipt';
COMMENT ON COLUMN egcl_legacy_receipt_header.manualreceiptDate IS 'Manual Receipt Date of legacy Receipt';
COMMENT ON COLUMN egcl_legacy_receipt_header.tenantid IS 'Tenant id for the Receipt Header';
COMMENT ON COLUMN egcl_legacy_receipt_header.remarks IS 'Remarks for the Receipt Header';
COMMENT ON TABLE egcl_legacy_receipt_header
  IS 'Legacy data for the receipt Header, to display Receipt (header) information from legacy system. ';
  
CREATE SEQUENCE seq_legacy_receipt_details;

CREATE TABLE egcl_legacy_receipt_details
(
   id bigint NOT NULL,
   billNo character varying(50) , 
   billId bigint,
   billYear bigint,
   taxId bigint,
   billDate bigint NOT NULL, 
   description character varying(256), 
   currDemand double precision, 
   arrDemand double precision, 
   currCollection double precision, 
   arrCollection double precision, 
   currBalance double precision, 
   arrBalance double precision,
   id_receipt_header bigint NOT NULL ,	
   tenantid character varying(256) NOT NULL,
   CONSTRAINT pk_legacy_egcl_receipt_detail PRIMARY KEY (id,tenantid),
   CONSTRAINT fk_legacy_detail_header FOREIGN KEY (id_receipt_header,tenantid)
      REFERENCES egcl_legacy_receipt_header (id,tenantid) 
);

COMMENT ON COLUMN egcl_legacy_receipt_details.id IS 'PK of receipt details.';
COMMENT ON COLUMN egcl_legacy_receipt_details.billNo IS 'Bill Number against which payment has been made';
COMMENT ON COLUMN egcl_legacy_receipt_details.billId IS 'Bill Id for the Respective Bill';
COMMENT ON COLUMN egcl_legacy_receipt_details.billYear IS 'Bill Year for the Respective Bill';
COMMENT ON COLUMN egcl_legacy_receipt_details.taxId IS 'Tax Id for the Respective Bill';
COMMENT ON COLUMN egcl_legacy_receipt_details.billDate IS 'Bill Date for the Respective Bill';
COMMENT ON COLUMN egcl_legacy_receipt_details.description IS 'Description of the receipt Details';
COMMENT ON COLUMN egcl_legacy_receipt_details.currDemand IS 'Current  Demand';
COMMENT ON COLUMN egcl_legacy_receipt_details.arrDemand IS 'Arrear Demand';
COMMENT ON COLUMN egcl_legacy_receipt_details.currCollection IS 'Current  Demand';
COMMENT ON COLUMN egcl_legacy_receipt_details.arrCollection IS 'Arrear Demand';
COMMENT ON COLUMN egcl_legacy_receipt_details.currBalance IS 'Current  Balance';
COMMENT ON COLUMN egcl_legacy_receipt_details.arrBalance IS 'Arrear Balance';
COMMENT ON COLUMN egcl_legacy_receipt_details.id_receipt_header IS 'FK of Receipt header table';
COMMENT ON COLUMN egcl_legacy_receipt_details.tenantid IS 'Tenant Information';
COMMENT ON TABLE egcl_legacy_receipt_details
  IS 'Legacy data for the receipt Details, to display Receipt (details) information from legacy system. ';

  
  