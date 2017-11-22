
UPDATE eg_action SET enabled = true WHERE url ='/inventory-services/openingbalance/_create' AND name = 'Opening Balance Create' AND servicecode = 'OPENING BALANCE';

UPDATE eg_action SET enabled = true WHERE url ='/inventory-services/openingbalance/_update' AND name = 'Opening Balance Update' AND servicecode = 'OPENING BALANCE';

UPDATE eg_action SET enabled = true WHERE url ='/inventory-services/openingbalance/_search' AND name = 'Opening Balance Search' AND servicecode = 'OPENING BALANCE';
