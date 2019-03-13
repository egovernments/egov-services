CREATE SEQUENCE EG_HRMS_POSITION;
SELECT setval('EG_HRMS_POSITION', (SELECT MAX(positionid) FROM hr_employee_v2_schema.egeis_assignment), true);
