class UploadLeaveType extends React.Component{

  constructor(props) {
    super(props);
    this.state={LeaveType:{
        "id": "",
        "my_file_input":"",
        "tenantId": tenantId
  },temp:[],dataType:[],employees:[],_leaveTypes:[],_years:[],duplicate:[]}
    this.handleChange=this.handleChange.bind(this);
    this.addOrUpdate=this.addOrUpdate.bind(this);
    this.filePicked=this.filePicked.bind(this);
  }



  componentDidMount(){
    if(window.opener && window.opener.document) {
    var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
    if(logo_ele && logo_ele[0]) {
        document.getElementsByClassName("homepage_logo")[0].src = window.location.origin + logo_ele[0].getAttribute("src");
       }
    }
    try {
        var _leaveTypes = getCommonMaster("hr-leave", "leavetypes", "LeaveType").responseJSON["LeaveType"] || [];
    } catch(e) {
        var _leaveTypes = [];
    }
    console.log("Leave type required",_leaveTypes);
    this.setState({
      _leaveTypes : _leaveTypes
    });
    try {
        var _years = getCommonMaster("egov-common-masters", "calendaryears", "CalendarYear").responseJSON["CalendarYear"] || [];
    } catch(e) {
        var _years = [];
    }

    this.setState({
      _years : _years
    });
    try {
    var  employees = getCommonMaster("hr-employee","employees","Employee").responseJSON["Employee"] || [];
    } catch (e) {
    var  employees = [];
    }
    console.log(employees);
    this.setState({
      employees : employees
    });


  }

  handleChange(e,name)
  {
      if(name === "active"){
        this.setState({
          LeaveType:{
              ...this.state.LeaveType,
              active: !this.state.LeaveType.active

          }
        })
      } else{
        this.setState({
            LeaveType:{
                ...this.state.LeaveType,
                [name]:e.target.value
            }
        })
      }


  }


  filePicked(oEvent) {
      // Get The File From The Input
      var oFile = oEvent.target.files[0];
      var sFilename = oFile.name;
      var _this = this, oJS;
      var key = [];
      // Create A File Reader HTML5
      var reader = new FileReader();

      // Ready The Event For When A File Gets Selected
      reader.onload = function(e) {
          var data = e.target.result;

          var cfb = XLSX.read(data, {
              type: 'binary'
          });
          cfb.SheetNames.forEach(function(sheetName) {
              // Obtain The Current Row As CSV
                var sCSV = XLS.utils.make_csv(cfb.Sheets[sheetName]);

                oJS = XLS.utils.sheet_to_json(cfb.Sheets[sheetName]);

                key = Object.keys(oJS[0]);
                console.log("key",key);
          });
          console.log("oJS",oJS);
          var finalObject = [],duplicateObject = [],scannedObject = [];
          oJS.forEach(function(d){
            var employee = d[key[0]];
            employee = employee.trim();
            var leaveType = d[key[3]];
            leaveType = leaveType.trim();
            var calendarYear = d[key[4]];
            calendarYear = calendarYear.trim();
            var noOfDays = d[key[5]];
            noOfDays = noOfDays.trim();

            scannedObject.push({"employee": employee,
                              "calendarYear": calendarYear,
                              "leaveType":  { "id":leaveType },
                              "noOfDays" : noOfDays,
                              "duplicate" : "false",
                              "employeeCode" : employee,
                              "employeeName" : d[key[1]],
                              "department": d[key[2]],
                              "leaveTypeName": leaveType

            });
          });


          for(var i=0;i<scannedObject.length;i++){
              for(var j=i+1;j<=scannedObject.length-1;j++)
              if(scannedObject[i].employee===scannedObject[j].employee){
                  if(scannedObject[i].leaveType.id===scannedObject[j].leaveType.id)
                    scannedObject[i].duplicate = "true";
                    scannedObject[j].duplicate = "true";
              }
          }
          scannedObject.forEach(function(d){
              if(d.duplicate === "true"){
                d.errorMessage = "Duplicate row in the excel scanned";
                duplicateObject.push(d);
              }
          });
          scannedObject.forEach(function(d){
              if(d.duplicate === "false"){
                finalObject.push(d);
              }
          });

          _this.setState({
            LeaveType:{
              ..._this.state.LeaveType
            }, temp : finalObject,
               duplicate : duplicateObject
          })
      };

      // Tell JS To Start Reading The File.. You could delay this if desired
      reader.readAsBinaryString(oFile);
  }

  close(){
      // widow.close();
      open(location, '_self').close();
  }



addOrUpdate(e,mode)
{

        e.preventDefault();
        var serverObject = [],errorObject=[],finalValidatedServerObject=[],finalSuccessObject=[];
        var tempInfo=Object.assign([],this.state.temp);
        var duplicateInfo = Object.assign([],this.state.duplicate);
        console.log("TEMP ",tempInfo);
        duplicateInfo.forEach(function(d){
          errorObject.push(d);
        });
        var leaveArray =[],calendarYearArray=[],employeeArray=[];
        var checkLeave = [],checkCalenderYear= [],checkEmployee=[];
        this.state._leaveTypes.forEach(function(d) {
          checkLeave.push(d.name);
        });

        this.state._leaveTypes.forEach(function(d) {
          leaveArray.push({"name":d.name,
                            "id":d.id});
        });

        this.state._years.forEach(function(d) {
          checkCalenderYear.push(d.name.toString());
        });


        this.state._years.forEach(function(d) {
          calendarYearArray.push({"name":d.name.toString(),
                            "id":d.id});
        });


        this.state.employees.forEach(function(d) {
          checkEmployee.push(d.code);
        });


        this.state.employees.forEach(function(d) {
          employeeArray.push({"code":d.code,
                            "id":d.id});
        });


        var finalErrorMessage="",post=0,neagativeDays="",invalidLeaveTypes="",error=0;
        var i=0,invalidEmployees="",invalidCalendarYears="";
        var leaveName,calendarYearName,employeeName,calenderName,noOfDays;
        var searchName;
        var leaveId = 0,employeeId=0,calenderId=0;
        for(var k=0;k<tempInfo.length;k++){

          var d = tempInfo[k];
          console.log("d----->",d);
          noOfDays = parseInt(d.noOfDays)
          leaveName = d.leaveType.id;
          employeeName = d.employee;
          calenderName = d.calendarYear;
          var leaveValidate = checkLeave.indexOf(d.leaveType.id);
          var employeeValidate = checkEmployee.indexOf(d.employee);
          var calenderValidate = checkCalenderYear.indexOf(d.calendarYear);

          if(noOfDays<0){
            d.errorMessage = "Number of days is negative "+noOfDays;
            neagativeDays = neagativeDays.concat(" "+noOfDays+", ");
            error=1;
            post=1;
          }

          if(leaveValidate<0){
            if(d.errorMessage===""){
                d.errorMessage = " Leave type "+leaveName +" is not exist in system";
            }else{
                d.errorMessage = d.errorMessage+" Leave type "+leaveName +" is not exist in system";
            }
            invalidLeaveTypes = invalidLeaveTypes.concat(" "+leaveName+", ");
            post =1;
            error=1;
          }else{
              for(var j=0;i<leaveArray.length;j++){
                if(leaveName===leaveArray[j].name)
                {
                    leaveId = leaveArray[j].id;
                    break;
                }else{
                  leaveId = 0;
                }
              }
            }

            if(employeeValidate<0){
              if(d.errorMessage===""){
                  d.errorMessage = " Employee Code "+employeeName +" is not exist in system";
              }else{
                  d.errorMessage = d.errorMessage+" Employee Code "+employeeName +" is not exist in system";
              }
              invalidEmployees = invalidEmployees.concat(" "+employeeName+", ");
              error=1;
              post =1;
            }else{
                for(var j=0;i<employeeArray.length;j++){
                if(employeeName===employeeArray[j].code)
                  {
                      employeeId = employeeArray[j].id;

                      break;
                  }else{
                    employeeId = 0;
                  }
                }
              }

              if(calenderValidate<0){
                if(d.errorMessage===""){
                    d.errorMessage = " Calender Year "+calenderName +" is not exist in system";
                }else{
                    d.errorMessage = d.errorMessage+" Calender Year "+calenderName +" is not exist in system";
                }

                invalidCalendarYears = invalidCalendarYears.concat(" "+calenderName+", ");
                error=1;
                post =1;
              }else{
                  for(var j=0;i<calendarYearArray.length;j++){
                  if(calenderName===calendarYearArray[j].name)
                    {
                        calenderId = calendarYearArray[j].name;

                        break;
                    }else{
                      calenderId = 0;
                    }
                  }
                }

          if(error===0) {

          serverObject.push({"employee": employeeId,
                            "calendarYear": calenderId,
                            "leaveType":  { "id": leaveId},
                            "noOfDays" : noOfDays,
                            "tenantId": tenantId,
                            "employeeCode" : d.employeeCode,
                            "employeeName" : d.employeeName,
                            "department": d.department,
                            "leaveTypeName": d.leaveTypeName
          });
        }else{
          errorObject.push(d);
          error = 0;
          }
        }
        console.log("Success Objects",serverObject);
        console.log("Error Objects",errorObject);
        var calenderYearApi = serverObject[0].calendarYear;

        try {
            var leaveBal = commonApiPost("hr-leave", "leaveopeningbalances", "_search", {
                tenantId,
                pageSize: 500,
                year : calenderYearApi
              }).responseJSON["LeaveOpeningBalance"] || [];
        } catch(e) {
            var leaveBal = [];
        }

        console.log(leaveBal);
        var errorLeaveOpening=[]
        var exists = false;
        for(var i=0;i<serverObject.length;i++){
          console.log("Success Object-->",serverObject[i]);
              var calendarNumber = parseInt(serverObject[i].calendarYear);
              exists = false;
              for(var j=0;j<leaveBal.length;j++){
                if(calendarNumber===leaveBal[j].calendarYear){
                   if(serverObject[i].employee===leaveBal[j].employee){
                       if(serverObject[i].leaveType["id"]===leaveBal[j].leaveType["id"]){
                         console.log("Duplicate object from server -->",serverObject[i]);
                               exists=true;
                               serverObject[i].errorMessage = "Leave opening balance already present in the system for this Employee";
                               errorLeaveOpening.push(serverObject[i]);
                               break;
                          }
                    }
                  }
                }

                if(exists===false){
                  console.log("finalSuccessObject-->"serverObject[i]);
                  serverObject[i].successMessage = "Employee leaves created successfully";
                  finalValidatedServerObject.push(serverObject[i]);
                }

        }
        console.log("Data already present",errorLeaveOpening);

        errorLeaveOpening.forEach(function(d){
          errorObject.push(d);
        });

        console.log("Final Server Object After Validation",finalValidatedServerObject);
        console.log("Final Error Object After Validation",errorObject);

        var ep1=new ExcelPlus();
        var b=0;

          ep1.createFile("Success");
          ep1.write({ "content":[ ["Employee Code","Employee Name","Department","Leave type","Calendar Year","Number of days as on 1st Jan 2017","Success Message"] ] });
          for(b=0;b<finalValidatedServerObject.length;b++){
            ep1.writeNextRow([finalValidatedServerObject[b].employeeCode,finalValidatedServerObject[b].employeeName,finalValidatedServerObject[b].department,finalValidatedServerObject[b].leaveTypeName,finalValidatedServerObject[b].calendarYear,finalValidatedServerObject[b].noOfDays,finalValidatedServerObject[b].successMessage])
          }
          ep1.saveAs("success.xlsx");

        var ep2=new ExcelPlus();
        var b=0;

          ep2.createFile("Error");
          ep2.write({ "content":[ ["Employee Code","Employee Name","Department","Leave type","Calendar Year","Number of days as on 1st Jan 2017","Error Message"] ] });
          for(b=0;b<errorObject.length;b++){
            ep2.writeNextRow([errorObject[b].employeeCode,errorObject[b].employeeName,errorObject[b].department,errorObject[b].leaveTypeName,errorObject[b].calendarYear,errorObject[b].noOfDays,errorObject[b].errorMessage])
          }
          ep2.saveAs("error.xlsx");


          finalValidatedServerObject.forEach(function(d){
            console.log(d);
              finalSuccessObject.push({"employee": d.employee,
                              "calendarYear": d.calendarYear,
                              "leaveType":  { "id": d.leaveType["id"]},
                              "noOfDays" : d.noOfDays,
                              "tenantId": d.tenantId
                            });
          });
          console.log("FINSSL SNJNCJ",finalSuccessObject);

        if(finalSuccessObject.length!==0){

        // try {
        //   employees = commonApiPost("hr-employee","employees","_search", {tenantId,code,pageSize:500},this.state.searchSet).responseJSON["Employee"] || [];
        // } catch (e) {
        //   employees = [];
        // }


        var body={
            "RequestInfo":requestInfo,
            "LeaveOpeningBalance":finalSuccessObject
          },_this=this;

          $.ajax({

                url: baseUrl + "/hr-leave/leaveopeningbalances/_create?tenantId=" + tenantId,
                type: 'POST',
                dataType: 'json',
                data:JSON.stringify(body),
                contentType: 'application/json',
                headers:{
                  'auth-token': authToken
                },
                success: function(res) {
                        showSuccess("File Uploaded successfully.");
                        _this.setState({
                          LeaveType:{
                            "id": "",
                            "my_file_input": "",
                            "tenantId": tenantId
                        }
                        })

                },
                error: function(err) {
                    showError("Only excel file can Upload");

                }
            });
          }else{
            if(neagativeDays!="")
            finalErrorMessage =  finalErrorMessage.concat(neagativeDays+" is not a valid days");

            if(invalidLeaveTypes!="")
            finalErrorMessage =   finalErrorMessage.concat(invalidLeaveTypes+" Leave types does not exist in system");

            if(invalidEmployees!="")
            finalErrorMessage =   finalErrorMessage.concat(invalidEmployees+"  Employees does not exist in system");

            if(invalidCalendarYears!="")
            finalErrorMessage =   finalErrorMessage.concat(invalidCalendarYears+" Calendar Years does not exist in system");
            showError(finalErrorMessage);
          }
  }

  render()
  {
    let {handleChange,addOrUpdate,filePicked}=this;
    let {name,payEligible,encashable,halfdayAllowed,accumulative,description,active}=this.state.LeaveType;
    let mode=getUrlVars()["type"];


    return(<div>
      <form onSubmit={(e)=>{addOrUpdate(e,mode)}}>
      <fieldset>
      <div className="row">
        <div className="col-sm-6">
            <div className="row">
                <div className="col-sm-6 label-text">
                    <label for="">Upload File <span> * </span></label>
                </div>
                <div className="col-sm-6">
                    <input type="file" id="my_file_input" name="my_file_input"
                    onChange={(e)=>{filePicked(e)}} required/>

                </div>
            </div>
          </div>
      </div>
                <div className="text-center">
                    <button type="submit" className="btn btn-submit">Upload</button>  &nbsp;&nbsp;
                    <button type="button" className="btn btn-close"onClick={(e)=>{this.close()}}>Close</button>
                </div>
                </fieldset>
                </form>
                <a href="resources/sample/LeaveOpeningBalance_Upload_template.xls" target="_blank">Download Sample Template</a>
      </div>
    );
  }
}

ReactDOM.render(
  <UploadLeaveType />,
  document.getElementById('root')
);
