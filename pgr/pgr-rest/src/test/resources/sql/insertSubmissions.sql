INSERT INTO submission(crn, tenantid, escalationdate, landmarkdetails, department, positionid, latitude, longitude,
status, details, servicecode, email, mobile, name, loggedinrequester, requesteraddress, createddate,
createdby) VALUES ('crn1', 'tenant1', '1994-11-29', 'landmark1', 26, 3, 1.2, 4.5, 'REGISTERED',
 'details1', 'servicecode1', 'foo@bar.com', '123123122', 'name1', 2, 'requester add', now(), 0);

INSERT INTO submission(crn, tenantid, escalationdate, landmarkdetails, department, positionid, latitude, longitude,
status, details, servicecode, email, mobile, name, loggedinrequester, requesteraddress, createddate,
createdby) VALUES ('crn2', 'tenant1', '1994-11-28', 'landmark2', 26, 3, 1.2, 4.5, 'FORWARDED',
 'details1', 'servicecode1', 'foo2@bar.com', '123123122', 'name1', 2, 'requester add', now(), 0);