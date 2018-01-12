-- alter materialreceiptdetail quantity column size and default value
ALTER TABLE materialreceiptdetail ALTER COLUMN receivedqty type numeric (18,6);
ALTER TABLE materialreceiptdetail ALTER COLUMN acceptedqty type numeric (18,6);
ALTER TABLE materialreceiptdetail ALTER COLUMN userreceivedqty type numeric (18,6);
ALTER TABLE materialreceiptdetail ALTER COLUMN useracceptedqty type numeric (18,6);
ALTER TABLE materialreceiptdetail ALTER COLUMN userreceivedqty set default 0.00;
ALTER TABLE materialreceiptdetail ALTER COLUMN useracceptedqty set default 0.00;

-- alter materialissuedfromreceipt  quantity column size
ALTER TABLE materialissuedfromreceipt ALTER COLUMN quantity type numeric (18,6);

-- alter materialreceiptdetailaddnlinfo  quantity column size
ALTER TABLE materialreceiptdetailaddnlinfo ALTER COLUMN quantity type numeric (18,6);
ALTER TABLE materialreceiptdetailaddnlinfo ALTER COLUMN userquantity type numeric (18,6);
ALTER TABLE materialreceiptdetailaddnlinfo ALTER COLUMN userquantity set default 0.00;

-- alter materialissuedetail  quantity column size
ALTER TABLE materialissuedetail ALTER COLUMN userquantityissued type numeric (18,6);
ALTER TABLE materialissuedetail ALTER COLUMN scrapedquantity type numeric (18,6);
ALTER TABLE materialissuedetail ALTER COLUMN quantityissued type numeric (18,6);
ALTER TABLE materialissuedetail ALTER COLUMN userquantityissued set default 0.00;
ALTER TABLE materialissuedetail ALTER COLUMN scrapedquantity set default 0.00;

-- alter indentdetail  quantity column size
ALTER TABLE indentdetail ALTER COLUMN indentquantity type numeric (18,6);
ALTER TABLE indentdetail ALTER COLUMN totalprocessedquantity type numeric (18,6);
ALTER TABLE indentdetail ALTER COLUMN indentissuedquantity type numeric (18,6);
ALTER TABLE indentdetail ALTER COLUMN poorderedquantity type numeric (18,6);
ALTER TABLE indentdetail ALTER COLUMN interstorerequestquantity type numeric (18,6);
ALTER TABLE indentdetail ALTER COLUMN userquantity type numeric (18,6);
ALTER TABLE indentdetail ALTER COLUMN userquantity set default 0.00;

-- alter purchaseindentdetail  quantity column size
ALTER TABLE purchaseindentdetail ALTER COLUMN quantity type numeric (18,6);
ALTER TABLE purchaseindentdetail ALTER COLUMN quantity set default 0.00;

-- alter purchaseorderdetail  quantity column size
ALTER TABLE purchaseorderdetail ALTER COLUMN orderquantity type numeric (18,6);
ALTER TABLE purchaseorderdetail ALTER COLUMN receivedquantity type numeric (18,6);
ALTER TABLE purchaseorderdetail ALTER COLUMN tenderquantity type numeric (18,6);
ALTER TABLE purchaseorderdetail ALTER COLUMN usedquantity type numeric (18,6);
ALTER TABLE purchaseorderdetail ALTER COLUMN userquantity type numeric (18,6);
ALTER TABLE purchaseorderdetail ALTER COLUMN orderquantity set default 0.00;
ALTER TABLE purchaseorderdetail ALTER COLUMN receivedquantity set default 0.00;
ALTER TABLE purchaseorderdetail ALTER COLUMN tenderquantity set default 0.00;
ALTER TABLE purchaseorderdetail ALTER COLUMN usedquantity set default 0.00;
ALTER TABLE purchaseorderdetail ALTER COLUMN userquantity set default 0.00;

-- alter material  quantity column size
ALTER TABLE material ALTER COLUMN minquantity type numeric (18,6);
ALTER TABLE material ALTER COLUMN maxquantity type numeric (18,6);
ALTER TABLE material ALTER COLUMN reorderquantity type numeric (18,6);
ALTER TABLE material ALTER COLUMN minquantity set default 0.00;
ALTER TABLE material ALTER COLUMN maxquantity set default 0.00;
ALTER TABLE material ALTER COLUMN reorderquantity set default 0.00;

-- alter disposaldetail  quantity column size
ALTER TABLE disposaldetail ALTER COLUMN userdisposalquantity type numeric (18,6);
ALTER TABLE disposaldetail ALTER COLUMN disposalquantity type numeric (18,6);
ALTER TABLE disposaldetail ALTER COLUMN userdisposalquantity set default 0.00;
ALTER TABLE disposaldetail ALTER COLUMN disposalquantity set default 0.00;

-- alter scrapdetail  quantity column size
ALTER TABLE scrapdetail ALTER COLUMN quantity type numeric (18,6);
ALTER TABLE scrapdetail ALTER COLUMN disposalquantity type numeric (18,6);
ALTER TABLE scrapdetail ALTER COLUMN userquantity type numeric (18,6);
ALTER TABLE scrapdetail ALTER COLUMN scrapquantity type numeric (18,6);

ALTER TABLE scrapdetail ALTER COLUMN quantity set default 0.00;
ALTER TABLE scrapdetail ALTER COLUMN disposalquantity set default 0.00;
ALTER TABLE scrapdetail ALTER COLUMN userquantity set default 0.00;
ALTER TABLE scrapdetail ALTER COLUMN scrapquantity set default 0.00;





























