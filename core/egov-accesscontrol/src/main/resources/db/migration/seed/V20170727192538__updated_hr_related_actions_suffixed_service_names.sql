---------------------------------------- hr-employee ----------------------------------------

UPDATE eg_action SET url = '/hr-employee/employees' WHERE name = 'Employee';
UPDATE eg_action SET url = '/hr-employee/employees/_search' WHERE name = 'SearchEmployee';
UPDATE eg_action SET url = '/hr-employee/employees/_create' WHERE name = 'CreateEmployees';
UPDATE eg_action SET url = '/hr-employee/employees/_update' WHERE name = 'EmployeeUpdate';
UPDATE eg_action SET url = '/hr-employee/employees/_loggedinemployee' WHERE name = 'LoggedInEmployeeDetails';
UPDATE eg_action SET url = '/hr-employee/hod/employees/_search' WHERE name = 'HOD Employees';
UPDATE eg_action SET url = '/hr-employee/nominees/_search' WHERE name = 'NomineeSearch';
UPDATE eg_action SET url = '/hr-employee/nominees/_create' WHERE name = 'NomineeCreate';
UPDATE eg_action SET url = '/hr-employee/nominees/_update' WHERE name = 'NomineeUpdate';


------------------------------------ egov-common-masters ------------------------------------

UPDATE eg_action SET url = '/egov-common-masters/holidays' WHERE name = 'Holidays';
UPDATE eg_action SET url = '/egov-common-masters/holidays/_search' WHERE name = 'CalendarHolidaysSearch';
UPDATE eg_action SET url = '/egov-common-masters/holidays/_create' WHERE name = 'CreateHoliday';
UPDATE eg_action SET url = '/egov-common-masters/holidays/{id}/_update' WHERE name = 'UpdateHoliday';
UPDATE eg_action SET url = '/egov-common-masters/communities/_search' WHERE name = 'CommunitySearch';
UPDATE eg_action SET url = '/egov-common-masters/calendaryears/_search' WHERE name = 'CalendarSearch';
UPDATE eg_action SET url = '/egov-common-masters/languages/_search' WHERE name = 'CommonLanguages';
UPDATE eg_action SET url = '/egov-common-masters/religions/_search' WHERE name = 'CommonReligions';
UPDATE eg_action SET url = '/egov-common-masters/uoms/_search' WHERE name = 'SearchUomService';
UPDATE eg_action SET url = '/egov-common-masters/uomcategories/_search' WHERE name = 'SearchUomCategoriesService';
UPDATE eg_action SET url = '/egov-common-masters/departments/_search' WHERE name = 'CommonDepartmentsSearch';
UPDATE eg_action SET url = '/egov-common-masters/genders/_search' WHERE name = 'SearchGender';
UPDATE eg_action SET url = '/egov-common-masters/relationships/_search' WHERE name = 'SearchRelationship';
INSERT INTO eg_action (id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate)
	VALUES (nextval('SEQ_EG_ACTION'), 'SearchCategories', '/egov-common-masters/categories/_search', 'EIS', null,
		(SELECT code FROM service WHERE name = 'EIS' AND contextroot = 'EIS'), null, 'Search Categories', true, 1, now(), 1, now());


---------------------------------------- hr-masters -----------------------------------------

DELETE FROM eg_action WHERE name = 'SearchCategory';
DELETE FROM eg_action WHERE name = 'loaddesignations';
UPDATE eg_action SET url = '/hr-masters/designations/_create' WHERE name = 'DesignationCreate';
UPDATE eg_action SET url = '/hr-masters/designations/_update' WHERE name = 'DesignationUpdate';
UPDATE eg_action SET url = '/hr-masters/positions/_search' WHERE name = 'PositionSearch';
UPDATE eg_action SET url = '/hr-masters/positions/_create' WHERE name = 'PositionCreate';
UPDATE eg_action SET url = '/hr-masters/positions/_update' WHERE name = 'PositionUpdate';
UPDATE eg_action SET url = '/hr-masters/vacantpositions/_search' WHERE name = 'VacantPositions';
UPDATE eg_action SET url = '/hr-masters/recruitmentmodes/_search' WHERE name = 'RecruitmentModes';
UPDATE eg_action SET url = '/hr-masters/recruitmenttypes/_search' WHERE name = 'RecruitmentTypes';
UPDATE eg_action SET url = '/hr-masters/recruitmentquotas/_search' WHERE name = 'RecruitmentQuotas';
UPDATE eg_action SET url = '/hr-masters/employeetypes/_search' WHERE name = 'EmployeeTypes';
UPDATE eg_action SET url = '/hr-masters/grades/_search' WHERE name = 'SearchGrade';
UPDATE eg_action SET url = '/hr-masters/groups/_search' WHERE name = 'EmployeeGroup';
UPDATE eg_action SET url = '/hr-masters/hrstatuses/_search' WHERE name = 'EmployeeStatus';
UPDATE eg_action SET url = '/hr-masters/hrconfigurations/_search' WHERE name = 'HRConfigSearch';


--------------------------------------- hr-attendance ---------------------------------------

UPDATE eg_action SET url = '/hr-attendance/attendances' WHERE name = 'Attendance';
UPDATE eg_action SET url = '/hr-attendance/attendances/_search' WHERE name = 'AjaxSearchAttendances';
UPDATE eg_action SET url = '/hr-attendance/attendances/_create' WHERE name = 'AjaxCreateAttendances';
UPDATE eg_action SET url = '/hr-attendance/attendances/_update' WHERE name = 'AjaxUpdateAttendances';


------------------------------------------ hr-leave -----------------------------------------

UPDATE eg_action SET url = '/hr-leave/leavetypes' WHERE name = 'Leavetypes';
UPDATE eg_action SET url = '/hr-leave/leaveallotments' WHERE name = 'Leaveallotments';
UPDATE eg_action SET url = '/hr-leave/leaveopeningbalances' WHERE name = 'Leaveopeningbalances';
UPDATE eg_action SET url = '/hr-leave/leaveapplications' WHERE name = 'Leaveapplications';
UPDATE eg_action SET url = '/hr-leave/leavetypes/_search' WHERE name = 'Search Leave Type';
UPDATE eg_action SET url = '/hr-leave/leavetypes/_create' WHERE name = 'Create LeaveType';
UPDATE eg_action SET url = '/hr-leave/leavetypes/{id}/_update' WHERE name = 'Leave Type Update';
UPDATE eg_action SET url = '/hr-leave/leaveallotments/_search' WHERE name = 'SearchLeave Mapping';
UPDATE eg_action SET url = '/hr-leave/leaveallotments/_create' WHERE name = 'CreateLeave Mappings';
UPDATE eg_action SET url = '/hr-leave/leaveallotments/_update' WHERE name = 'Leave MappingUpdate';
UPDATE eg_action SET url = '/hr-leave/leaveopeningbalances/_search' WHERE name = 'SearchLeave Opening Balance';
UPDATE eg_action SET url = '/hr-leave/leaveopeningbalances/_create' WHERE name = 'CreateLeave Opening Balances';
UPDATE eg_action SET url = '/hr-leave/leaveopeningbalances/_update' WHERE name = 'Leave Opening BalanceUpdate';
UPDATE eg_action SET url = '/hr-leave/leaveapplications/_search' WHERE name = 'Search Leave Application';
UPDATE eg_action SET url = '/hr-leave/leaveapplications/_create' WHERE name = 'Create Leave Applications';
UPDATE eg_action SET url = '/hr-leave/leaveapplications/{id}/_update' WHERE name = 'Leave Application Update';
UPDATE eg_action SET url = '/hr-leave/eligibleleaves/_search' WHERE name = 'Search Eligible Leaves';


--------------------------- Blood Groups & Marital Status Enum APIs ---------------------------

UPDATE eg_action SET url = '/hr-employee/bloodgroups/_search' WHERE name = 'BloodMasterSearch';
UPDATE eg_action SET url = '/hr-employee/maritalstatuses/_search' WHERE name = 'MaritalStatusSearch';

INSERT INTO eg_action (id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate)
	VALUES (nextval('SEQ_EG_ACTION'), 'SearchBloodGroups', '/egov-common-masters/bloodgroups/_search', 'EIS', null,
		(SELECT code FROM service WHERE name = 'EIS' AND contextroot = 'EIS'), null, 'Search Blood Groups', true, 1, now(), 1, now());
INSERT INTO eg_action (id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate)
	VALUES (nextval('SEQ_EG_ACTION'), 'SearchMaritalStatuses', '/egov-common-masters/maritalstatuses/_search', 'EIS', null,
		(SELECT code FROM service WHERE name = 'EIS' AND contextroot = 'EIS'), null, 'Search Marital Statuses', true, 1, now(), 1, now());
