INSERT INTO egpt_property VALUES (1, 'default', '123', '123', '123', 'new property', '2017-05-30', '2017-05-16', 'test123', true, true, 'new property', '123', '2017-05-16', '123', '2017-05-16', 'b123');
INSERT INTO egpt_property VALUES (2, 'default', '456', '456', '456', 'old property', '2017-06-12', '2017-06-12', 'test456', true, true, 'old property', '456', '2017-06-12', '456', '2017-06-12', 'b456');

SELECT pg_catalog.setval('egpt_property_id_seq', 2, true);




INSERT INTO egpt_address VALUES (1, 'default', 'A-123', 'Hills colony', 'Near MRO Office', 'urban', 'town', 'westgodavari', 'westgodavari', 'eluru', 'Andrapradesh', 'India', '534001', 'create', '123', '2017-05-16', '123', '2017-05-16', 1);
INSERT INTO egpt_address VALUES (2, 'default', 'A-456', 'Eastreen Street', 'Near juniourCollege', 'city', 'city', 'hyderabad', 'hyderabad', 'hyderabad', 'Telangana', 'India', '500004', 'old', '456', '2017-06-12', '456', '2017-06-12', 2);


SELECT pg_catalog.setval('egpt_address_id_seq', 2, true);




SELECT pg_catalog.setval('egpt_propertydetail_id_seq', 2, true);


INSERT INTO egpt_propertydetails VALUES (1, 'default', '123', '2017-05-16', '2017-05-16', 'new property purchase', 'new', true, '2017-05-16', true, 'Need property', 'new', 'land', 'own', 'incometax', 'yes', 12, 25, 15, 14, 12, 2, true, 'anil', 'marble', 'normal', 'silling', 'normal', 1, '123', '2017-05-30', 'test', '2017-05-30');
INSERT INTO egpt_propertydetails VALUES (2, 'default', '456', '2017-06-12', '2017-06-12', 'old property purchase', 'processing', false, '2017-06-12', true, 'property', 'old', 'house', 'lease', 'government', 'no', 25, 14, 13, 10, 5, 0, true, 'raj', 'classic', 'classic', 'normal', 'normal', 2, '456', '20172017-06-12', '456', '2017-06-12');



INSERT INTO egpt_floors VALUES (1, 'default', '1', '1', 'new', 125, 50, 10, 10, 15, 'new', 'yes', 'no', 'squre', 'no', 'anil', 'wtc', 15000, 'new property', true, '2017-05-16', '2017-05-16', '123', '123', 10, 10, 'TA123', 'SA123', '123', '2017-05-16', '123', '2017-05-16', 1);
INSERT INTO egpt_floors VALUES (2, 'default', '2', '2', 'old', 124, 40, 12, 11, 14, 'old', 'yes', 'yes', 'rectangle', 'yes', 'raj', 'trust', 11000, 'old property', true, '2017-06-12', '2017-06-12', '456', '456', 12, 11, 'TA456', 'SA456', '456', '2017-06-12', '456', '2017-06-12', 2);



SELECT pg_catalog.setval('egpt_floors_id_seq', 2, true);




INSERT INTO egpt_documenttype VALUES (1, 'new', 'CREATE');
INSERT INTO egpt_documenttype VALUES (2, 'new', 'CREATE');



SELECT pg_catalog.setval('egpt_documenttype_id_seq', 2, true);


INSERT INTO egpt_document VALUES (1, 1, 'testfile', 1);
INSERT INTO egpt_document VALUES (2, 2, 'creatingfile', 2);



SELECT pg_catalog.setval('egpt_document_id_seq', 2, true);




INSERT INTO egpt_vacantland VALUES (1, 'defaul', '123', '123', 100000, 100000, 'yes', '123', '2017-05-16', 125, 50, 'employee', '10-04-2017', 'employee', '10-04-2017', 1);
INSERT INTO egpt_vacantland VALUES (2, 'defaul', '456', '456', 1235236, 145258, 'No', '456', '2017-06-12', 123, 40, 'employee', '2017-06-12', 'employee', '2017-06-12', 2);



SELECT pg_catalog.setval('egpt_vacantland_id_seq', 2, true);




INSERT INTO egpt_property_user VALUES (1, 1, 1);
INSERT INTO egpt_property_user VALUES (2, 2, 2);


SELECT pg_catalog.setval('egpt_property_user_id_seq', 2, true);





