--Procedure to load employee data from ms to ml
---To execute use command- select ms_to_ml_load_employees();
set search_path to microservice;
CREATE OR REPLACE FUNCTION ms_to_ml_load_employees()
  RETURNS void AS
$BODY$
declare
v_emp_count integer;
emp_code varchar(100);
v_tenantid varchar(100);
v_user_seq integer;
v_assignment_seq integer;
v_old_emp_id integer;
v_old_user_id integer;
v_emp_type_id integer;
v_boundary_type_id integer;
v_boundary_id integer;
v_jurisdiction_id integer;
v_userid integer;
query varchar(2000);
v_emp_type_name varchar(100);
v_fund_code varchar(100);
v_function_code varchar(100);
v_designation_code varchar(100);
v_functionary_code varchar(100);
v_department_code varchar(100);
v_position_name varchar(100);
v_grade_name varchar(100);
v_fund_id integer;
v_function_id integer;
v_designation_id integer;
v_functionary_id integer;
v_department_id integer;
v_position_id integer;
v_grade_id integer;
v_role_id integer;
v_accountdetailtype_id integer;
v_emp_hod_id integer;
v_assignment_id integer;
v_accountdetailkey_id integer;
v_deptdesig_id integer;
max_position integer;
v_boundarytype_name varchar(100);
v_boundary_name varchar(100);
v_role_name varchar(100);
cur_emp record;
cur_user record;
cur_assignment record;
cur_jurisdiction record;
cur_userrole record;
cur_hoddept record;
queryvalues varchar(10000);
errorMessage varchar(512);

begin
RAISE NOTICE 'Inside Employee data load';
for cur_emp in( select * from egeis_employee where lastmodifieddate > (select epoch from data_sync_epoch where profile = 'ms-to-ml-employee') order by id)
loop
begin
emp_code:=cur_emp.code;
RAISE NOTICE 'Employee data for code %',emp_code;

select replace(cur_emp.tenantid,'ap.','') into v_tenantid;
RAISE NOTICE 'v_tenantid %',v_tenantid;
execute 'select count(*) from '||v_tenantid||'.egeis_employee where code = $1' into v_emp_count using cur_emp.code;
RAISE NOTICE 'emp count: %', v_emp_count;

select * from eg_user into cur_user where id = cur_emp.id and tenantid = cur_emp.tenantid;

if(v_emp_count = 0) then
begin
	RAISE NOTICE 'Creating new User';
	execute 'select nextval('''||v_tenantid||'.seq_eg_user'')' into v_user_seq;
	
	query := 'INSERT INTO '||v_tenantid||'.eg_user(title,salutation,dob,locale,username,password,pwdexpirydate,mobilenumber,altcontactnumber,emailid,createddate,lastmodifieddate,createdby,lastmodifiedby,active,name,gender,pan,aadhaarnumber,guardian,guardianrelation,signature,accountlocked,type,id,version) values ($1,$2,$3,$4,$5,$6,$7,$8,$9,$10,$11,$12,$13,$14,$15,$16,$17,$18,$19,$20,$21,$22,$23,$24,$25,0)';
	execute query using cur_user.title,cur_user.salutation,cur_user.dob,cur_user.locale,cur_user.username,cur_user.password,cur_user.pwdexpirydate,cur_user.mobilenumber,cur_user.altcontactnumber,cur_user.emailid,cur_user.createddate,cur_user.lastmodifieddate,cur_user.createdby,cur_user.lastmodifiedby,cur_user.active,cur_user.name,cur_user.gender,cur_user.pan,cur_user.aadhaarnumber,cur_user.guardian,cur_user.guardianrelation,cur_user.signature::bytea,cur_user.accountlocked,cur_user.type,v_user_seq;

	RAISE NOTICE 'Creating new Employee';
	select name into v_emp_type_name from egeis_employeetype where id = cur_emp.employeetypeid and tenantid = cur_emp.tenantid;
	execute 'select id from '||v_tenantid||'.egeis_employeetype where name = $1' into v_emp_type_id using v_emp_type_name;
		
	query := 'INSERT INTO '||v_tenantid||'.egeis_employee(code,dateofappointment,dateofretirement,employeestatus,employeetype,id,version) values ($1,$2,$3,$4,$5,$6,0)';
	execute query using cur_emp.code, cur_emp.dateofappointment,cur_emp.dateofretirement, (select code from microservice.egeis_hrstatus where id=cur_emp.employeestatus::integer), v_emp_type_id, v_user_seq;

	RAISE NOTICE 'Creating new assignments';
	for cur_assignment in (select * from egeis_assignment where employeeid = cur_emp.id and tenantid = cur_emp.tenantid order by id)
	loop
	begin
		RAISE NOTICE 'Inside loop';
		execute 'select nextval('''||v_tenantid||'.seq_egeis_assignment'')' into v_assignment_seq;

		select code into v_fund_code from egf_fund where id = cur_assignment.fundid and tenantid = cur_emp.tenantid;
		execute 'select id from '||v_tenantid||'.fund where code = $1' into v_fund_id using v_fund_code;

		select code into v_function_code from egf_function where id = cur_assignment.functionid and tenantid = cur_emp.tenantid;
		execute 'select id from '||v_tenantid||'.function where code = $1' into v_function_id using v_function_code;

		select code into v_designation_code from egeis_designation where id = cur_assignment.designationid and tenantid = cur_emp.tenantid;
		execute 'select id from '||v_tenantid||'.eg_designation where code = $1' into v_designation_id using v_designation_code;

		select code into v_functionary_code from egf_functionary where id = cur_assignment.functionaryid and tenantid = cur_emp.tenantid;
		execute 'select id from '||v_tenantid||'.functionary where code = $1' into v_functionary_id using v_functionary_code::bigint;

		select code into v_department_code from eg_department where id = cur_assignment.departmentid and tenantid = cur_emp.tenantid;
		execute 'select id from '||v_tenantid||'.eg_department where code = $1' into v_department_id using v_department_code;

		select name into v_position_name from egeis_position where id = cur_assignment.positionid and tenantid = cur_emp.tenantid;
		execute 'select id from '||v_tenantid||'.eg_position where name = $1' into v_position_id using v_position_name;

		select name into v_grade_name from egeis_grade where id = cur_assignment.gradeid and tenantid = cur_emp.tenantid;
		execute 'select grade_id from '||v_tenantid||'.egeis_grade_mstr where grade_value = $1' into v_grade_id using v_grade_name;

		RAISE NOTICE 'position id %', v_position_id;
		if (v_position_id is null) then
		begin
			RAISE NOTICE 'Inside position create loop';
			execute 'select id from '||v_tenantid||'.egeis_deptdesig where department = $1 and designation = $2' into v_deptdesig_id using v_department_id, v_designation_id;
			if (v_deptdesig_id is null) then
			begin
				RAISE NOTICE 'Inside dept_desig create loop';
				execute 'select nextval('''||v_tenantid||'.seq_egeis_deptdesig'')' into v_deptdesig_id;
				execute 'insert into '||v_tenantid||'.egeis_deptdesig (id,designation,department,sanctionedposts,version,createddate,lastmodifieddate,createdby,lastmodifiedby) values($1,$2,$3,1,1,$4,$5,1,1)' using v_deptdesig_id, v_designation_id, v_department_id, now(), now();
				RAISE NOTICE 'Inserted to DEpt-Desig %',v_designation_code;
				EXCEPTION WHEN others THEN
					select SQLERRM into errorMessage;
					RAISE NOTICE 'error message: %', errorMessage;
					queryvalues := '$1=' ||cur_emp.code|| ' || tenantid='||v_tenantId;
					insert into microservice.eg_common_log (name,logmessage,tenantid,objecttype,queryexecuted,triggername,queryvalues) values('',errorMessage,v_tenantid,'',query,'MS-to-ML Employee Data',queryvalues);
				END;
			
			end if;
			execute 'select nextval('''||v_tenantid||'.seq_eg_position'')' into v_position_id;
			RAISE NOTICE 'position seq %', v_position_id;
			execute 'select count(id) from '||v_tenantid||'.eg_position where deptdesig=$1' into max_position using v_deptdesig_id;
			execute 'insert into '||v_tenantid||'.eg_position(name,id,deptdesig,createddate,lastmodifieddate,createdby,lastmodifiedby,ispostoutsourced,version) values($1,$2,$3,$4,$5,1,1,false,0)' using v_position_name,v_position_id,v_deptdesig_id,now(),now();
			EXCEPTION WHEN others THEN
				select SQLERRM into errorMessage;
				RAISE NOTICE 'error message: %', errorMessage;
				queryvalues := '$1=' ||cur_emp.code|| ' || tenantid='||v_tenantId;
				insert into microservice.eg_common_log (name,logmessage,tenantid,objecttype,queryexecuted,triggername,queryvalues) values('',errorMessage,v_tenantid,'',query,'MS-to-ML Employee Data',queryvalues);
			END;
		end if;

		query := 'INSERT INTO '||v_tenantid||'.egeis_assignment(id,fund,function,designation,functionary,department,position,grade,lastmodifiedby,lastmodifieddate,createddate,createdby,fromdate,todate,employee,isprimary,version) values ($1,$2,$3,$4,$5,$6,$7,$8,$9,$10,$11,$12,$13,$14,$15,$16,0)';
		execute query using v_assignment_seq, v_fund_id,v_function_id,v_designation_id,v_functionary_id,v_department_id,v_position_id,v_grade_id,cur_assignment.lastmodifiedby,cur_assignment.lastmodifieddate,cur_assignment.createddate,cur_assignment.createdby,cur_assignment.fromdate,cur_assignment.todate, v_user_seq, cur_assignment.isprimary;

		--Creating new hod department
		select * from egeis_hoddepartment into cur_hoddept where assignmentid = cur_assignment.id and tenantid = cur_emp.tenantid;
		select code into v_department_code from eg_department where id = cur_hoddept.departmentid and tenantid = cur_emp.tenantid;
		execute 'select id from '||v_tenantid||'.eg_department where code = $1' into v_department_id using v_department_code;
		
		query := 'INSERT INTO '||v_tenantid||'.egeis_employee_hod(id,hod,assignment,lastmodifieddate) values (nextval('''||v_tenantid||'.seq_egeis_employee_hod''),$1,$2,$3)';
		execute query using v_department_id,v_assignment_seq,cur_hoddept.lastmodifieddate;
		EXCEPTION WHEN others THEN
			select SQLERRM into errorMessage;
			RAISE NOTICE 'error message: %', errorMessage;
			queryvalues := '$1=' ||cur_emp.code|| ' || tenantid='||v_tenantId;
			insert into microservice.eg_common_log (name,logmessage,tenantid,objecttype,queryexecuted,triggername,queryvalues) values('',errorMessage,v_tenantid,'',query,'MS-to-ML Employee Data',queryvalues);
	end;
	end loop;

	RAISE NOTICE 'Creating new Jurisdictions';
	select * from egeis_employeejurisdictions into cur_jurisdiction where employeeid = cur_emp.id and tenantid = cur_emp.tenantid;
	select name into v_boundarytype_name from eg_boundary_type where id = (select boundarytype from eg_boundary where id=cur_jurisdiction.jurisdictionid::integer and tenantid = cur_emp.tenantid) and tenantid = cur_emp.tenantid;
	execute 'select id from '||v_tenantid||'.eg_boundary_type where name = $1' into v_boundary_type_id using v_boundarytype_name;

	select name into v_boundary_name from eg_boundary where id = cur_jurisdiction.jurisdictionid and tenantid = cur_emp.tenantid;
	execute 'select id from '||v_tenantid||'.eg_boundary where name = $1' into v_boundary_id using v_boundary_name;
	
	query := 'INSERT INTO '||v_tenantid||'.egeis_jurisdiction(employee,boundarytype,boundary,lastmodifieddate,version,id) values ($1,$2,$3,$4,0,nextval('''||v_tenantid||'.seq_egeis_jurisdiction''))';
	execute query using v_user_seq,v_boundary_type_id,v_boundary_id, cur_jurisdiction.lastmodifieddate;

	RAISE NOTICE 'Creating new user roles';
	for cur_userrole in (select * from eg_userrole where userid = cur_emp.id and tenantid = cur_emp.tenantid)
	loop
	begin
		RAISE NOTICE 'Inside loop';
		select name into v_role_name from eg_role where id = cur_userrole.roleid and tenantid = cur_emp.tenantid;
		execute 'select id from '||v_tenantid||'.eg_role where name = $1' into v_role_id using v_role_name;
		
		query := 'INSERT INTO '||v_tenantid||'.eg_userrole(roleid,userid) values ($1,$2)';
		execute query using v_role_id, v_user_seq;
	end;
	end loop;

	RAISE NOTICE 'Creating new Accountdetailkey';
	execute 'select id from '||v_tenantid||'.accountdetailtype where name = ''Employee''' into v_accountdetailtype_id;
	
	query := 'INSERT INTO '||v_tenantid||'.accountdetailkey(groupid,detailname,detailkey,detailtypeid,id) values ($1,$2,$3,$4,nextval('''||v_tenantid||'.seq_accountdetailkey''))';
	execute query using 1,v_user_seq,v_user_seq,v_accountdetailtype_id, 1, now(), 1, now();
	EXCEPTION WHEN others THEN
		select SQLERRM into errorMessage;
		RAISE NOTICE 'error message: %', errorMessage;
		queryvalues := '$1=' ||cur_emp.code|| ' || tenantid='||v_tenantId;
		insert into microservice.eg_common_log (name,logmessage,tenantid,objecttype,queryexecuted,triggername,queryvalues) values('',errorMessage,v_tenantid,'',query,'MS-to-ML Employee Data',queryvalues);
	
end;
else
begin
	RAISE NOTICE 'Updating existing User';
	execute 'select id from '||v_tenantid||'.egeis_employee where code = $1' into v_old_emp_id using cur_emp.code;
	query := 'UPDATE '||v_tenantid||'.eg_user set title=$1,salutation=$2,dob=$3,locale=$4,username=$5,password=$6,pwdexpirydate=$7,mobilenumber=$8,altcontactnumber=$9,emailid=$10,createddate=$11,lastmodifieddate=$12,createdby=$13,lastmodifiedby=$14,active=$15,name=$16,gender=$17,pan=$18,aadhaarnumber=$19,guardian=$20,guardianrelation=$21,signature=$22,accountlocked=$23,type=$24 where id = $25';
	RAISE NOTICE 'Updating existing User';
	execute query using cur_user.title,cur_user.salutation,cur_user.dob,cur_user.locale,cur_user.username,cur_user.password,cur_user.pwdexpirydate,cur_user.mobilenumber,cur_user.altcontactnumber,cur_user.emailid,cur_user.createddate,cur_user.lastmodifieddate,cur_user.createdby,cur_user.lastmodifiedby,cur_user.active,cur_user.name,cur_user.gender,cur_user.pan,cur_user.aadhaarnumber,cur_user.guardian,cur_user.guardianrelation,cur_user.signature::bytea,cur_user.accountlocked,cur_user.type,v_old_emp_id;

	RAISE NOTICE 'Updating existing Employee';
	select name into v_emp_type_name from egeis_employeetype where id = cur_emp.employeetypeid and tenantid = cur_emp.tenantid;
	execute 'select id from '||v_tenantid||'.egeis_employeetype where name = $1' into v_emp_type_id using v_emp_type_name;
	
	query := 'UPDATE '||v_tenantid||'.egeis_employee SET dateofappointment=$1,dateofretirement=$2,employeestatus=$3,employeetype=$4 where code = $5';
	execute query using cur_emp.dateofappointment,cur_emp.dateofretirement, (select code from egeis_hrstatus where id=cur_emp.employeestatus::integer), v_emp_type_id, cur_emp.code;

	RAISE NOTICE 'Creating new assignments while upadting employee';
	for cur_assignment in (select * from egeis_assignment where employeeid = cur_emp.id and createddate > (select epoch from data_sync_epoch where profile = 'ms-to-ml-employee') and tenantid = cur_emp.tenantid order by id)
	loop
	begin
		RAISE NOTICE 'Inside loop';
		execute 'select nextval('''||v_tenantid||'.seq_egeis_assignment'')' into v_assignment_seq;

		select code into v_fund_code from egf_fund where id = cur_assignment.fundid and tenantid = cur_emp.tenantid;
		execute 'select id from '||v_tenantid||'.fund where code = $1' into v_fund_id using v_fund_code;

		select code into v_function_code from egf_function where id = cur_assignment.functionid and tenantid = cur_emp.tenantid;
		execute 'select id from '||v_tenantid||'.function where code = $1' into v_function_id using v_function_code;

		select code into v_designation_code from egeis_designation where id = cur_assignment.designationid and tenantid = cur_emp.tenantid;
		execute 'select id from '||v_tenantid||'.eg_designation where code = $1' into v_designation_id using v_designation_code;

		select code into v_functionary_code from egf_functionary where id = cur_assignment.functionaryid and tenantid = cur_emp.tenantid;
		execute 'select id from '||v_tenantid||'.functionary where code = $1' into v_functionary_id using v_functionary_code::bigint;

		select code into v_department_code from eg_department where id = cur_assignment.departmentid and tenantid = cur_emp.tenantid;
		execute 'select id from '||v_tenantid||'.eg_department where code = $1' into v_department_id using v_department_code;

		select name into v_position_name from egeis_position where id = cur_assignment.positionid and tenantid = cur_emp.tenantid;
		execute 'select id from '||v_tenantid||'.eg_position where name = $1' into v_position_id using v_position_name;

		select name into v_grade_name from egeis_grade where id = cur_assignment.gradeid and tenantid = cur_emp.tenantid;
		execute 'select grade_id from '||v_tenantid||'.egeis_grade_mstr where grade_value = $1' into v_grade_id using v_grade_name;

		RAISE NOTICE 'position id %', v_position_id;
		if (v_position_id is null) then
		begin
			RAISE NOTICE 'Inside position create loop';
			execute 'select id from '||v_tenantid||'.egeis_deptdesig where department = $1 and designation = $2' into v_deptdesig_id using v_department_id, v_designation_id;
			if (v_deptdesig_id is null) then
			begin
				RAISE NOTICE 'Inside dept_desig create loop';
				execute 'select nextval('''||v_tenantid||'.seq_egeis_deptdesig'')' into v_deptdesig_id;
				execute 'insert into '||v_tenantid||'.egeis_deptdesig (id,designation,department,sanctionedposts,version,createddate,lastmodifieddate,createdby,lastmodifiedby) values($1,$2,$3,1,1,$4,$5,1,1)' using v_deptdesig_id, v_designation_id, v_department_id, now(), now();
				RAISE NOTICE 'Inserted to DEpt-Desig %',v_designation_code;
				EXCEPTION WHEN others THEN
					select SQLERRM into errorMessage;
					RAISE NOTICE 'error message: %', errorMessage;
					queryvalues := '$1=' ||cur_emp.code|| ' || tenantid='||v_tenantId;
					insert into microservice.eg_common_log (name,logmessage,tenantid,objecttype,queryexecuted,triggername,queryvalues) values('',errorMessage,v_tenantid,'',query,'MS-to-ML Employee Data',queryvalues);
			END;
			end if;
			execute 'select nextval('''||v_tenantid||'.seq_eg_position'')' into v_position_id;
			RAISE NOTICE 'position seq %', v_position_id;
			execute 'select count(id) from '||v_tenantid||'.eg_position where deptdesig=$1' into max_position using v_deptdesig_id;
			execute 'insert into '||v_tenantid||'.eg_position(name,id,deptdesig,createddate,lastmodifieddate,createdby,lastmodifiedby,ispostoutsourced,version) values($1,$2,$3,$4,$5,1,1,false,0)' using v_position_name,v_position_id,v_deptdesig_id,now(),now();
			EXCEPTION WHEN others THEN
				select SQLERRM into errorMessage;
				RAISE NOTICE 'error message: %', errorMessage;
				queryvalues := '$1=' ||cur_emp.code|| ' || tenantid='||v_tenantId;
				insert into microservice.eg_common_log (name,logmessage,tenantid,objecttype,queryexecuted,triggername,queryvalues) values('',errorMessage,v_tenantid,'',query,'MS-to-ML Employee Data',queryvalues);
			END;
		end if;

		query := 'INSERT INTO '||v_tenantid||'.egeis_assignment(id,fund,function,designation,functionary,department,position,grade,lastmodifiedby,lastmodifieddate,createddate,createdby,fromdate,todate,employee,isprimary,version) values ($1,$2,$3,$4,$5,$6,$7,$8,$9,$10,$11,$12,$13,$14,$15,$16,0)';
		execute query using v_assignment_seq, v_fund_id,v_function_id,v_designation_id,v_functionary_id,v_department_id,v_position_id,v_grade_id,cur_assignment.lastmodifiedby,cur_assignment.lastmodifieddate,cur_assignment.createddate,cur_assignment.createdby,cur_assignment.fromdate,cur_assignment.todate, v_user_seq, cur_assignment.isprimary;

		RAISE NOTICE 'Creating new hod department';
		select * from egeis_hoddepartment into cur_hoddept where assignmentid = cur_assignment.id and tenantid = cur_emp.tenantid;
		select code into v_department_code from eg_department where id = cur_hoddept.departmentid and tenantid = cur_emp.tenantid;
		execute 'select id from '||v_tenantid||'.eg_department where code = $1' into v_department_id using v_department_code;
		
		query := 'INSERT INTO '||v_tenantid||'.egeis_employee_hod(id,hod,assignment,lastmodifieddate) values (nextval('''||v_tenantid||'.seq_egeis_employee_hod''),$1,$2,$3)';
		execute query using v_department_id,v_assignment_seq,cur_hoddept.lastmodifieddate;
		EXCEPTION WHEN others THEN
			select SQLERRM into errorMessage;
			RAISE NOTICE 'error message: %', errorMessage;
			queryvalues := '$1=' ||cur_emp.code|| ' || tenantid='||v_tenantId;
			insert into microservice.eg_common_log (name,logmessage,tenantid,objecttype,queryexecuted,triggername,queryvalues) values('',errorMessage,v_tenantid,'',query,'MS-to-ML Employee Data',queryvalues);
		
	end;
	end loop;

	RAISE NOTICE 'Updating existing assignments';
	for cur_assignment in (select * from egeis_assignment where employeeid = cur_emp.id and lastmodifieddate > (select epoch from data_sync_epoch where profile = 'ms-to-ml-employee') and tenantid = cur_emp.tenantid order by id)
	loop
	begin
		RAISE NOTICE 'Inside Loop';
		select code into v_fund_code from egf_fund where id = cur_assignment.fundid and tenantid = cur_emp.tenantid;
		execute 'select id from '||v_tenantid||'.fund where code = $1' into v_fund_id using v_fund_code;

		select code into v_function_code from egf_function where id = cur_assignment.functionid and tenantid = cur_emp.tenantid;
		execute 'select id from '||v_tenantid||'.function where code = $1' into v_function_id using v_function_code;

		select code into v_designation_code from egeis_designation where id = cur_assignment.designationid and tenantid = cur_emp.tenantid;
		execute 'select id from '||v_tenantid||'.eg_designation where code = $1' into v_designation_id using v_designation_code;

		select code into v_functionary_code from egf_functionary where id = cur_assignment.functionaryid and tenantid = cur_emp.tenantid;
		execute 'select id from '||v_tenantid||'.functionary where code = $1' into v_functionary_id using v_functionary_code::bigint;

		select code into v_department_code from eg_department where id = cur_assignment.departmentid and tenantid = cur_emp.tenantid;
		execute 'select id from '||v_tenantid||'.eg_department where code = $1' into v_department_id using v_department_code;

		select name into v_position_name from egeis_position where id = cur_assignment.positionid and tenantid = cur_emp.tenantid;
		execute 'select id from '||v_tenantid||'.eg_position where name = $1' into v_position_id using v_position_name;

		select name into v_grade_name from egeis_grade where id = cur_assignment.gradeid and tenantid = cur_emp.tenantid;
		execute 'select grade_id from '||v_tenantid||'.egeis_grade_mstr where grade_value = $1' into v_grade_id using v_grade_name;

		RAISE NOTICE 'position id %', v_position_id;
		if (v_position_id is null) then
		begin
			RAISE NOTICE 'Inside position create loop';
			execute 'select id from '||v_tenantid||'.egeis_deptdesig where department = $1 and designation = $2' into v_deptdesig_id using v_department_id, v_designation_id;
			if (v_deptdesig_id is null) then
			begin
				RAISE NOTICE 'Inside dept_desig create loop';
				execute 'select nextval('''||v_tenantid||'.seq_egeis_deptdesig'')' into v_deptdesig_id;
				execute 'insert into '||v_tenantid||'.egeis_deptdesig (id,designation,department,sanctionedposts,version,createddate,lastmodifieddate,createdby,lastmodifiedby) values($1,$2,$3,1,1,$4,$5,1,1)' using v_deptdesig_id, v_designation_id, v_department_id, now(), now();
				RAISE NOTICE 'Inserted to DEpt-Desig %',v_designation_code;
				EXCEPTION WHEN others THEN
					select SQLERRM into errorMessage;
					RAISE NOTICE 'error message: %', errorMessage;
					queryvalues := '$1=' ||cur_emp.code|| ' || tenantid='||v_tenantId;
					insert into microservice.eg_common_log (name,logmessage,tenantid,objecttype,queryexecuted,triggername,queryvalues) values('',errorMessage,v_tenantid,'',query,'MS-to-ML Employee Data',queryvalues);
				END;
			end if;
			execute 'select nextval('''||v_tenantid||'.seq_eg_position'')' into v_position_id;
			RAISE NOTICE 'position seq %', v_position_id;
			execute 'select count(id) from '||v_tenantid||'.eg_position where deptdesig=$1' into max_position using v_deptdesig_id;
			execute 'insert into '||v_tenantid||'.eg_position(name,id,deptdesig,createddate,lastmodifieddate,createdby,lastmodifiedby,ispostoutsourced,version) values($1,$2,$3,$4,$5,1,1,false,0)' using v_position_name,v_position_id,v_deptdesig_id,now(),now();
			EXCEPTION WHEN others THEN
				select SQLERRM into errorMessage;
				RAISE NOTICE 'error message: %', errorMessage;
				queryvalues := '$1=' ||cur_emp.code|| ' || tenantid='||v_tenantId;
				insert into microservice.eg_common_log (name,logmessage,tenantid,objecttype,queryexecuted,triggername,queryvalues) values('',errorMessage,v_tenantid,'',query,'MS-to-ML Employee Data',queryvalues);
			END;
		end if;

		RAISE NOTICE 'getting existing assignment';
		execute 'select id from '||v_tenantid||'.egeis_assignment where designation = $1 and department = $2 and position = $3 and fromdate = $4 and todate = $5 and employee = $6' into v_assignment_id using v_designation_id, v_department_id, v_position_id, cur_assignment.fromdate, cur_assignment.todate, v_old_emp_id;

		query := 'update '||v_tenantid||'.egeis_assignment set fund=$1,function=$2,designation=$3,functionary=$4,department=$5,position=$6,grade=$7,lastmodifiedby=$8,lastmodifieddate=$9,createddate=$10,createdby=$11,fromdate=$12,todate=$13,employee=$14,isprimary=$15 where id = $16';
		execute query using v_fund_id,v_function_id,v_designation_id,v_functionary_id,v_department_id,v_position_id,v_grade_id,cur_assignment.lastmodifiedby,cur_assignment.lastmodifieddate,cur_assignment.createddate,cur_assignment.createdby,cur_assignment.fromdate,cur_assignment.todate, v_user_seq, cur_assignment.isprimary, v_assignment_id;

		RAISE NOTICE 'Updating hod department';
		select * from egeis_hoddepartment into cur_hoddept where assignmentid = cur_assignment.id and tenantid = cur_emp.tenantid;
		select code into v_department_code from eg_department where id = cur_hoddept.departmentid and tenantid = cur_emp.tenantid;
		execute 'select id from '||v_tenantid||'.eg_department where code = $1' into v_department_id using v_department_code;

		execute 'select id from '||v_tenantid||'.egeis_employee_hod where hod = $1 and assignment = $2' into v_emp_hod_id using v_department_id, v_assignment_id;
		
		query := 'update '||v_tenantid||'.egeis_employee_hod set hod=$1,assignment=$2,lastmodifieddate=$3 where id=$4';
		execute query using v_department_id,v_assignment_id,cur_hoddept.lastmodifieddate,v_emp_hod_id;
		EXCEPTION WHEN others THEN
			select SQLERRM into errorMessage;
			RAISE NOTICE 'error message: %', errorMessage;
			queryvalues := '$1=' ||cur_emp.code|| ' || tenantid='||v_tenantId;
			insert into microservice.eg_common_log (name,logmessage,tenantid,objecttype,queryexecuted,triggername,queryvalues) values('',errorMessage,v_tenantid,'',query,'MS-to-ML Employee Data',queryvalues);
		
	end;
	end loop;

	RAISE NOTICE 'Deleting deleted assignments while upadting employee';
	execute 'set search_path to '||v_tenantid;
	for cur_assignment in (select * from egeis_assignment where employee = v_old_emp_id)
	loop
	begin
		RAISE NOTICE 'Inside loop';

		select code into v_fund_code from fund where id = cur_assignment.fund;
		select id into v_fund_id from microservice.egf_fund where code = v_fund_code and tenantid = cur_emp.tenantid;

		select code into v_function_code from function where id = cur_assignment.function;
		select id into v_function_id from microservice.egf_function where code = v_function_code and tenantid = cur_emp.tenantid;

		select code into v_designation_code from eg_designation where id = cur_assignment.designation;
		select id into v_designation_id from microservice.egeis_designation where code = v_designation_code and tenantid = cur_emp.tenantid;

		select code into v_functionary_code from functionary where id = cur_assignment.functionary;
		select id into v_functionary_id from microservice.egf_functionary where code = v_functionary_code and tenantid = cur_emp.tenantid;

		select code into v_department_code from eg_department where id = cur_assignment.department;
		select id into v_department_id from microservice.eg_department where code = v_department_code and tenantid = cur_emp.tenantid;

		select name into v_position_name from eg_position where id = cur_assignment.position;
		select id into v_position_id from microservice.egeis_position where name = v_position_name and tenantid = cur_emp.tenantid;

		select grade_value into v_grade_name from egeis_grade_mstr where grade_id = cur_assignment.grade;
		select id into v_grade_id from microservice.egeis_grade where name = v_grade_name and tenantid = cur_emp.tenantid;

		select id into v_assignment_id from microservice.egeis_assignment where designationid = v_designation_id and departmentid = v_department_id and positionid = v_position_id and fromdate = cur_assignment.fromdate and todate = cur_assignment.todate and employeeid = cur_emp.id;

		if (v_assignment_id is null) then
			RAISE NOTICE 'Deleted Assignment and Employee_Hod';
			delete from egeis_employee_hod where assignment = cur_assignment.id;
			delete from egeis_assignment where designation = cur_assignment.designation and department = cur_assignment.department and position = cur_assignment.position and fromdate = cur_assignment.fromdate and todate = cur_assignment.todate and employee = v_old_emp_id;
		end if;
	end;
	end loop;
	set search_path to microservice;
	
	RAISE NOTICE 'Updating Jurisdictions';
	select * from egeis_employeejurisdictions into cur_jurisdiction where employeeid = cur_emp.id and tenantid = cur_emp.tenantid;

	if (cur_jurisdiction.id is not null) then
	begin
		select name into v_boundarytype_name from eg_boundary_type where id = (select boundarytype from eg_boundary where id=cur_jurisdiction.jurisdictionid and tenantid = cur_emp.tenantid) and tenantid = cur_emp.tenantid;
		execute 'select id from '||v_tenantid||'.eg_boundary_type where name = $1' into v_boundary_type_id using v_boundarytype_name;
		
		select name into v_boundary_name from eg_boundary where id = cur_jurisdiction.jurisdictionid and tenantid = cur_emp.tenantid;
		execute 'select id from '||v_tenantid||'.eg_boundary where name = $1' into v_boundary_id using v_boundary_name;

		execute 'select id from '||v_tenantid||'.egeis_jurisdiction where employee = $1' into v_jurisdiction_id using v_old_emp_id;
		
		query := 'UPDATE '||v_tenantid||'.egeis_jurisdiction SET employee=$1,boundarytype=$2,boundary=$3,lastmodifieddate=$4 where id = $5';
		execute query using v_old_emp_id,v_boundary_type_id,v_boundary_id, cur_jurisdiction.lastmodifieddate, v_jurisdiction_id;
		EXCEPTION WHEN others THEN
			select SQLERRM into errorMessage;
			RAISE NOTICE 'error message: %', errorMessage;
			queryvalues := '$1=' ||cur_emp.code|| ' || tenantid='||v_tenantId;
			insert into microservice.eg_common_log (name,logmessage,tenantid,objecttype,queryexecuted,triggername,queryvalues) values('',errorMessage,v_tenantid,'',query,'MS-to-ML Employee Data',queryvalues);
		
	end;
	end if;

	RAISE NOTICE 'Creating new user roles';
	for cur_userrole in (select * from eg_userrole where userid = cur_emp.id and lastmodifieddate > (select epoch from data_sync_epoch where profile = 'ms-to-ml-employee') and tenantid = cur_emp.tenantid)
	loop
	begin
		select name into v_role_name from eg_role where id = cur_userrole.roleid and tenantid = cur_emp.tenantid;
		execute 'select id from '||v_tenantid||'.eg_role where name = $1' into v_role_id using v_role_name;
		
		query := 'INSERT INTO '||v_tenantid||'.eg_userrole(roleid,userid) values ($1,$2)';
		execute query using v_role_id, v_old_emp_id;
		EXCEPTION WHEN others THEN
			select SQLERRM into errorMessage;
			RAISE NOTICE 'error message: %', errorMessage;
			queryvalues := '$1=' ||cur_emp.code|| ' || tenantid='||v_tenantId;
			insert into microservice.eg_common_log (name,logmessage,tenantid,objecttype,queryexecuted,triggername,queryvalues) values('',errorMessage,v_tenantid,'',query,'MS-to-ML Employee Data',queryvalues);
		
	end;
	end loop;

	RAISE NOTICE 'Deleting deleted user roles';
	execute 'set search_path to '||v_tenantid;
	for cur_userrole in (select * from eg_userrole where userid = v_old_emp_id)
	loop
	begin
		select name into v_role_name from eg_role where id = cur_userrole.roleid;
		select id into v_role_id from microservice.eg_role where name = v_role_name;

		select userid into v_userid from microservice.eg_userrole where userid = cur_emp.id and roleid = v_role_id;

		if (v_userid is null) then
		RAISE NOTICE 'Deleting User role';
			delete from eg_userrole where userid = v_old_emp_id and roleid = cur_userrole.roleid;
		end if;
	end;
	end loop;
	set search_path to microservice;

	RAISE NOTICE 'Updating Accountdetailkey';
	execute 'select id from '||v_tenantid||'.accountdetailtype where name = ''Employee''' into v_accountdetailtype_id;

	execute 'select id from '||v_tenantid||'.accountdetailkey where detailkey = $1 and detailtypeid = $2' into v_accountdetailkey_id using v_old_emp_id, v_accountdetailtype_id;
	
	query := 'UPDATE '||v_tenantid||'.accountdetailkey SET groupid=$1,detailname=$2,detailkey=$3,detailtypeid=$4 where id = $5';
	execute query using 1,v_old_emp_id,v_old_emp_id,v_accountdetailtype_id,v_accountdetailkey_id;
	EXCEPTION WHEN others THEN
		select SQLERRM into errorMessage;
		RAISE NOTICE 'error message: %', errorMessage;
		queryvalues := '$1=' ||cur_emp.code|| ' || tenantid='||v_tenantId;
		insert into microservice.eg_common_log (name,logmessage,tenantid,objecttype,queryexecuted,triggername,queryvalues) values('',errorMessage,v_tenantid,'',query,'MS-to-ML Employee Data',queryvalues);
	
end;
end if;
end;
end loop;
UPDATE data_sync_epoch SET epoch = now() WHERE profile = 'ms-to-ml-employee';

EXCEPTION WHEN others THEN
		select SQLERRM into errorMessage;
		RAISE NOTICE 'error message: %', errorMessage;
		queryvalues := '$1=' ||cur_emp.code|| ' || tenantid='||v_tenantId;
		insert into microservice.eg_common_log (name,logmessage,tenantid,objecttype,queryexecuted,triggername,queryvalues) values('',errorMessage,v_tenantid,'',query,'MS-to-ML Employee Data',queryvalues);
end;
$BODY$
  LANGUAGE plpgsql;
