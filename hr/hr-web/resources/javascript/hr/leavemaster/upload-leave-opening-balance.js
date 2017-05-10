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

          });
          var finalObject = [],duplicateObject = [],scannedObject = [];
          oJS.forEach(function(d){

            scannedObject.push({"employee": d[key[0]],
                              "calendarYear":d[key[4]],
                              "leaveType":  { "id": d[key[3]]},
                              "noOfDays" : d[key[5]],
                              "duplicate" : "false"

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
        var serverObject = [],errorObject=[],finalValidatedServerObject=[];
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


        var finalErrorMessage="",post=0,neagativeDays="",invalidLeaveTypes="",error=0;
        var i=0,invalidEmployees="",invalidCalendarYears="";
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

          if(noOfDays<0){
            d.errorMessage = "Invalid : Number of days is negative "+noOfDays;
            neagativeDays = neagativeDays.concat(" "+noOfDays+", ");
            error=1;
            post=1;
          }

          if(leaveValidate<0){
            if(d.errorMessage===""){
                d.errorMessage = "Invalid: Leave type "+leaveName;
            }else{
                d.errorMessage = d.errorMessage+" Invalid: Leave type "+leaveName;
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
                  d.errorMessage = "Invalid: Employee Code "+employeeName;
              }else{
                  d.errorMessage = d.errorMessage+" Invalid: Employee Code "+employeeName;
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
                    d.errorMessage = "Invalid: Calender Year "+calenderName;
                }else{
                    d.errorMessage = d.errorMessage+" Invalid: Calender Year "+calenderName;
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
                            "tenantId": tenantId
          });
        }else{
          errorObject.push(d);
          error = 0;
          }
        }
        console.log("Success Object",serverObject);
        console.log("Error Object",errorObject);
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

        for(var i=0;i<serverObject.length;i++){
              var calendarNumber = parseInt(serverObject[i].calendarYear);
                var count = 0;
              for(var j=0;j<leaveBal.length;j++){
                if(calendarNumber===leaveBal[j].calendarYear){


                   if(serverObject[i].employee===leaveBal[j].employee){

                             if(serverObject[i].leaveType["id"]===leaveBal[j].leaveType["id"]){

                               serverObject[i].errorMessage = "Invalid: Leave Already present in leaveopening Balanace for this Employee";
                               errorLeaveOpening.push(serverObject[i]);
                               break;
                             }else {

                                finalValidatedServerObject.push(serverObject[i]);
                                break;

                             }

                     serverObject[i].errorMessage = "Invalid: Employee Already present in leaveopening Balanace";
                               errorLeaveOpening.push(serverObject[i])
                               break;
                   }

                   count++;
                    // for(var k=0;k<leaveBal.length;k++){
                    //   console.log("Server object ",serverObject[i].employee);
                    //   console.log("leave balance",leaveBal[k].employee);
                    //
                    //   if(serverObject[i].employee===leaveBal[k].employee){
                    //         console.log("hi");
                    //           serverObject[i].errorMessage = "Invalid: Leave Already present in leaveopening Balanace";
                    //           errorLeaveOpening.push(serverObject[i])
                    //           break;
                    //         }
                    //         break;
                    // }

                  }

                }
                  console.log("count",count);
                if(count===leaveBal.length){

                console.log(serverObject[i]);
                 finalValidatedServerObject.push(serverObject[i]);
                  break;
                }

        }
        console.log("Data already present",errorLeaveOpening);

        errorLeaveOpening.forEach(function(d){
          errorObject.push(d);
        });

        console.log("Final Server Object After Validation",finalValidatedServerObject);
        console.log("Final Error Object After Validation",errorObject);



        if(finalValidatedServerObject.length!==0){

        // try {
        //   employees = commonApiPost("hr-employee","employees","_search", {tenantId,code,pageSize:500},this.state.searchSet).responseJSON["Employee"] || [];
        // } catch (e) {
        //   employees = [];
        // }


        var body={
            "RequestInfo":requestInfo,
            "LeaveOpeningBalance":finalValidatedServerObject
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
