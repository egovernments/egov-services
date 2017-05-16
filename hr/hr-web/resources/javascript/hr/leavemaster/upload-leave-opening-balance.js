class UploadLeaveType extends React.Component{

  constructor(props) {
    super(props);
    this.state={LeaveType:{
        "id": "",
        "my_file_input":"",
        "tenantId": tenantId
  },temp:[],dataType:[],employees:[],_leaveTypes:[],_years:[],duplicate:[]}
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
    var _leaveTypes = commonApiPost("hr-leave", "leavetypes", "_search", {
      tenantId,
      pageSize: 500,
      accumulative: true
      }).responseJSON["LeaveType"] || [];
    } catch(e) {
            var _leaveTypes = [];
    }
    try {
        var _years = getCommonMaster("egov-common-masters", "calendaryears", "CalendarYear").responseJSON["CalendarYear"] || [];
    } catch(e) {
        var _years = [];``
    }

    try {
    var  employees = getCommonMaster("hr-employee","employees","Employee").responseJSON["Employee"] || [];
    } catch (e) {
    var  employees = [];
    }

    this.setState({
      employees : employees,
      _years : _years,
      _leaveTypes : _leaveTypes
    });


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

          });

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
                d.errorMsg = "Duplicate row in the excel scanned";
                duplicateObject.push(d);
              }else {
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


        var post=0,error=0,errorList=[],successList=[];
        var i=0;
        var leaveName,calendarYearName,employeeName,calenderName,noOfDays;
        var searchName;
        var leaveId = 0,employeeId=0,calenderId=0;
        for(var k=0;k<tempInfo.length;k++){

          var d = tempInfo[k];

          noOfDays = parseInt(d.noOfDays)
          leaveName = d.leaveType.id;
          employeeName = d.employee;
          calenderName = d.calendarYear;
          var leaveValidate = checkLeave.indexOf(d.leaveType.id);
          var employeeValidate = checkEmployee.indexOf(d.employee);
          var calenderValidate = checkCalenderYear.indexOf(d.calendarYear);
          d.errorMsg = "";
          if(noOfDays<0){
            d.errorMsg = "Number of days is negative "+noOfDays;
            error=1;
            post=1;
          }

          if(leaveValidate<0){
            if(d.errorMsg===""){
                d.errorMsg = " Leave type "+leaveName +" is not exist in system";
            }else{
                d.errorMsg = d.errorMsg+" Leave type "+leaveName +" is not exist in system";
            }
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
              if(d.errorMsg===""){
                  d.errorMsg = " Employee Code "+employeeName +" is not exist in system";
              }else{
                  d.errorMsg = d.errorMsg+" Employee Code "+employeeName +" is not exist in system";
              }
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
                if(d.errorMsg===""){
                    d.errorMsg = " Calender Year "+calenderName +" is not exist in system";
                }else{
                    d.errorMsg = d.errorMsg+" Calender Year "+calenderName +" is not exist in system";
                }
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

        var errorLeaveOpening=[]
        var exists = false;
        for(var i=0;i<serverObject.length;i++){
            var calendarNumber = parseInt(serverObject[i].calendarYear);
              exists = false;
              for(var j=0;j<leaveBal.length;j++){
                if(calendarNumber===leaveBal[j].calendarYear){
                   if(serverObject[i].employee===leaveBal[j].employee){
                       if(serverObject[i].leaveType["id"]===leaveBal[j].leaveType["id"]){
                               exists=true;
                               serverObject[i].errorMsg = "Leave opening balance already present in the system for this Employee";
                               errorLeaveOpening.push(serverObject[i]);
                               break;
                          }
                    }
                  }
                }

                if(exists===false){
                  serverObject[i].successMessage = "Employee leaves created successfully";
                  finalValidatedServerObject.push(serverObject[i]);
                }

        }


        errorLeaveOpening.forEach(function(d){
          errorObject.push(d);
        });


          finalValidatedServerObject.forEach(function(d){
              finalSuccessObject.push({"employee": d.employee,
                              "calendarYear": d.calendarYear,
                              "leaveType":  { "id": d.leaveType["id"]},
                              "noOfDays" : d.noOfDays,
                              "departmentName" : d.department,
                              "tenantId": d.tenantId
                            });
          });

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

                url: baseUrl + "/hr-leave/leaveopeningbalances/_create?tenantId=" + tenantId +"&type=upload",
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

                        errorList = res.ErrorList;
                        res.SuccessList.forEach(function(d){
                          d.successMessage = "Leave Opening balance created successfully";
                            successList.push(d);
                        });

                          var ep1=new ExcelPlus();
                          var b=0;

                            ep1.createFile("Success");
                            ep1.write({ "content":[ ["Employee Code","Employee Name","Department","Leave type","Calendar Year","Number of days as on 1st Jan 2017","Success Message"] ] });
                            for(b=0;b<successList.length;b++){
                              ep1.writeNextRow([successList[b].employeeCode,successList[b].employeeName,successList[b].departmentName,successList[b].leaveType["name"],successList[b].calendarYear,successList[b].noOfDays,successList[b].successMessage])
                            }
                            ep1.saveAs("success.xlsx");

                      if(errorList.length!==0){
                        errorList.forEach(function(d){
                            errorObject.push(d);
                        });
                      }
                      var ep2=new ExcelPlus();
                      var b=0;

                      ep2.createFile("Error");
                      ep2.write({ "content":[ ["Employee Code","Employee Name","Department","Leave type","Calendar Year","Number of days as on 1st Jan 2017","Error Message"] ] });
                      for(b=0;b<errorObject.length;b++){
                        ep2.writeNextRow([errorObject[b].employeeCode,errorObject[b].employeeName,errorObject[b].department,errorObject[b].leaveTypeName,errorObject[b].calendarYear,errorObject[b].noOfDays,errorObject[b].errorMsg])
                      }
                      ep2.saveAs("error.xlsx");


                },
                error: function(err) {
                    showError("Only excel file can Upload");

                }
            });
          }else{
            showError("No vaild data in the Uploaded Excel");
          }
  }

  render()
  {
    let {addOrUpdate,filePicked}=this;
    let {my_file_input}=this.state.LeaveType;
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
