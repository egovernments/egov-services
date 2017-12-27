alter table indentdetail add column userquantity numeric (13,2);

alter table PurchaseOrderDetail add column userquantity numeric (13,2);

alter table MaterialReceiptDetail add column userReceivedQty numeric (13,2);

alter table MaterialReceiptDetail add column userAcceptedQty numeric (13,2);

alter table MaterialReceiptDetailAddnlinfo add column userQuantity numeric (13,2);

alter table MaterialIssueDetail add column userQuantityIssued numeric (13,2);


