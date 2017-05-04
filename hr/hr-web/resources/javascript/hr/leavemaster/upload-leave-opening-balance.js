class UploadLeaveType extends React.Component{

  constructor(props) {
    super(props);
    this.state={LeaveType:{
        "id": "",
        "my_file_input":"",
        "tenantId": tenantId
  },temp:[],dataType:[],employees:[],_leaveTypes:[]}
    this.handleChange=this.handleChange.bind(this);
    this.addOrUpdate=this.addOrUpdate.bind(this);
    this.filePicked=this.filePicked.bind(this);
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
          });
          var finalObject = [];

          oJS.forEach(function(d){
            finalObject.push({"employee": d["Employee Code"],
                              "calendarYear":d["Calendar Year"],
                              "leaveType":  { "id": d["Leave type"]},
                              "noOfDays" : d["Number of days as on 1st Jan 2017"]
            });
          });

          _this.setState({
            LeaveType:{
              ..._this.state.LeaveType
            }, temp : finalObject
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
        var serverObject = [];
        var tempInfo=Object.assign([],this.state.temp);

        try {
            var _leaveTypes = getCommonMaster("hr-leave", "leavetypes", "LeaveType").responseJSON["LeaveType"] || [];
        } catch(e) {
            var _leaveTypes = [];
        }
        try {
            var _years = getCommonMaster("egov-common-masters", "calendaryears", "CalendarYear").responseJSON["CalendarYear"] || [];
        } catch(e) {
            var _years = [];
        }
        try {
        var  employees = getCommonMaster("hr-employee","employees","Employee").responseJSON["Employee"] || [];
        } catch (e) {
        var  employees = [];
        }
        var leaveArray =[],calendarYearArray=[],employeeArray=[];
        var checkLeave = [],checkCalenderYear= [],checkEmployee=[];
        _leaveTypes.forEach(function(d) {
          checkLeave.push(d.name);
        });

        _leaveTypes.forEach(function(d) {
          leaveArray.push({"name":d.name,
                            "id":d.id});
        });

        _years.forEach(function(d) {
          checkCalenderYear.push(d.name.toString());
        });


        _years.forEach(function(d) {
          calendarYearArray.push({"name":d.name.toString(),
                            "id":d.id});
        });


        employees.forEach(function(d) {
          checkEmployee.push(d.code);
        });


        employees.forEach(function(d) {
          employeeArray.push({"code":d.code,
                            "id":d.id});
        });


        var finalErrorMessage="Invalid Details : ",post=0,neagativeDays="",invalidLeaveTypes="";
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
            neagativeDays = neagativeDays.concat(" "+noOfDays+", ");
            post=1;
          }

          if(leaveValidate<0){
            invalidLeaveTypes = invalidLeaveTypes.concat(" "+leaveName+", ");
            post =1;
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
              invalidEmployees = invalidEmployees.concat(" "+employeeName+", ");
              //showError("invalid Employee");
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
                invalidCalendarYears = invalidCalendarYears.concat(" "+calenderName+", ");
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
          serverObject.push({"employee": employeeId,
                            "calendarYear": calenderId,
                            "leaveType":  { "id": leaveId},
                            "noOfDays" : noOfDays,
                            "tenantId": tenantId
          });
        }

        if(post===0){

        // try {
        //   employees = commonApiPost("hr-employee","employees","_search", {tenantId,code,pageSize:500},this.state.searchSet).responseJSON["Employee"] || [];
        // } catch (e) {
        //   employees = [];
        // }


        var body={
            "RequestInfo":requestInfo,
            "LeaveOpeningBalance":serverObject
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
      </div>
    );
  }
}

ReactDOM.render(
  <UploadLeaveType />,
  document.getElementById('root')
);
