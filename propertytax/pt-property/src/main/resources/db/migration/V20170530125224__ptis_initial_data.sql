INSERT INTO egpt_address VALUES(1, 'default', 11, 20, NULL, 'test', 'ameerpet', 'mitrivanam', 'test travels', 'secundrabad', '500082', 'testing', 'kumar', 'kumar', 1496835742464, 1496835742464, 1);
INSERT INTO egpt_address VALUES(2, 'default', 10, 10, NULL, 'acgaurd', 'khirathabad', 'hillscolony', 'noori travels', 'hyderabad', '500004', 'test', 'anil', 'anil', 1496835749070, 1496835749070, 2);



SELECT pg_catalog.setval('seq_egpt_address', 2, true);


INSERT INTO egpt_document VALUES(1, 'testing', 'kumar', 'kumar', 1496835742464, 1496835742464,1);
INSERT INTO egpt_document VALUES(2, 'test', 'anil',  'anil', 1496835749070, 1496835749070, 2);



SELECT pg_catalog.setval('seq_egpt_document', 2, true);


INSERT INTO egpt_documenttype VALUES(1, 'anilkumar', 'CREATE', 1, 'kumar', 'kumar', 1496835742464, 1496835742464);
INSERT INTO egpt_documenttype VALUES(2, 'sanil', 'CREATE', 2, 'anil', 'anil', 1496835749070, 1496835749070);


SELECT pg_catalog.setval('seq_egpt_documenttype', 2, true);



INSERT INTO egpt_floors VALUES(1, 'fn3', 'kumar', 'kumar', 1496835742464, 1496835742464, 1);
INSERT INTO egpt_floors VALUES(2, 'fn2', 'anil', 'anil', 1496835749070, 1496835749070, 2);



SELECT pg_catalog.setval('seq_egpt_floors', 2, true);


INSERT INTO egpt_property VALUES(1, 'default', 'un2', 'oun2', 'vun2', 'NEWPROPERTY', '15/02/17', '15/02/17', 'gfn2', false, false, true, 'SYSTEM', 'kumar', 'kumar', 1496835742464, 1496835742464);
INSERT INTO egpt_property VALUES(2, 'default', 'un1', 'oun1', 'vun1', 'NEWPROPERTY', '25/05/2017', '25/05/2017', 'gfn1', true, true, true, 'SYSTEM', 'anil', 'anil', 1496835749070, 1496835749070);



SELECT pg_catalog.setval('seq_egpt_property', 2, true);


INSERT INTO egpt_property_user VALUES(1, 1, 2, true, true, 0, 'anilkumarsandrapati', 'kumar', 'kumar', 1496835742464, 1496835742464);
INSERT INTO egpt_property_user VALUES(2, 2, 1, true, true, 0, 'sandrapatianilkumar', 'anil', 'anil', 1496835749070, 1496835749070);



SELECT pg_catalog.setval('seq_egpt_property_user', 2, true);


INSERT INTO egpt_propertydetails VALUES(1, 'MUNICIPAL_RECORDS', 'rdn2', '15/02/17', 'trying to purchase', 'ACTIVE', true, '25/05/2017', false, 'anything', 'house', 'land', 'no', 'incometax', 'no', 12, 15, 14, 12, 17, 1, false, 'kumar', 'normal', 'modern', 'new one', 'painting', 'si2', 'an2', 'kumar', 'kumar', 1496835742464, 1496835742464, 1);
INSERT INTO egpt_propertydetails VALUES(2, 'MUNICIPAL_RECORDS', 'rdn1', '25/05/2017', 'test', 'ACTIVE', true, '25/05/2017', true, 'test', 'land', 'any', 'yes', 'tax', 'yes', 10, 10, 10, 10, 10, 2, true, 'anil', 'marble', 'normal', 'normal', 'normal', 'si1', 'an1', 'anil', 'anil', 1496835749070, 1496835749070, 2);


SELECT pg_catalog.setval('seq_egpt_propertydetails', 2, true);


INSERT INTO egpt_propertylocation VALUES(1, 2, 2, 2, 'nbb1', 'ebb1', 'wbb1', 'sbb1', 'kumar', 'kumar', 1496835742464, 1496835742464, 1);
INSERT INTO egpt_propertylocation VALUES(2, 1, 1, 1, 'nbb', 'ebb', 'wbb', 'sbb', 'anil', 'anil', 1496835749070, 1496835749070, 2);


SELECT pg_catalog.setval('seq_egpt_propertylocation', 2, true);


INSERT INTO egpt_unit VALUES(1, 2, 'FLAT', 114, 175, 120, 105, 195, 'bpn2', '15/02/17', 'tset', 'asf', 'kumar', 'wtc technologies', 17, 'rec14', '28', 'testcase', false, '15/02/17', '15/02/17', 14, 17, 'emn2', 'wmn2', 'kumar', 'kumar', 1496835742464, 1496835742464, 1);
INSERT INTO egpt_unit VALUES(2, 1, 'FLAT', 10, 10, 10, 10, 10, 'bpn1', '25/05/2017', 'nothing', 'sf', 'sanil', 'wtc', 10, 'rec', '27', 'testing', true, '25/05/2017', '25/05/2017', 10, 10, 'emn1', 'wmn1', 'anil', 'anil', 1496835749070, 1496835749070, 2);



SELECT pg_catalog.setval('seq_egpt_unit', 2, true);


INSERT INTO egpt_vacantland VALUES(1, 'sn2', 'pn2', 10748, 452200, 'laa2', 'lpn2', 'lpd2', 475, 658, 'kumar', 'kumar', 1496835742464, 1496835742464, 1);
INSERT INTO egpt_vacantland VALUES(2, 'sn1', 'pn1', 10000, 125200, 'laa1', 'lpn1', 'lpd1', 10, 10, 'anil', 'anil', 1496835749070, 1496835749070, 2);


SELECT pg_catalog.setval('seq_egpt_vacantland', 2, true);
