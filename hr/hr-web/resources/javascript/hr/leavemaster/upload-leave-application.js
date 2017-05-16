class UploadLeaveApplication extends React.Component{

  constructor(props) {
    super(props);
    this.state={LeaveType:{
        "id": "",
        "my_file_input":"",
        "tenantId": tenantId
  },temp:[],dataType:[],employees:[],_leaveTypes:[],_years:[],duplicate:[],hrConfigurations:[],allHolidayList:[]}
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
    ////console.log(_leaveTypes);
    ////console.log("Leave type required",_leaveTypes);
    try {
    var  employees = getCommonMaster("hr-employee","employees","Employee").responseJSON["Employee"] || [];
    } catch (e) {
    var  employees = [];
    }
    try {
        var hrConfigurations = commonApiPost("hr-masters", "hrconfigurations", "_search", {tenantId:"default"}).responseJSON || [];
    } catch(e) {
        var hrConfigurations = [];
    }
    try {
             var allHolidayList = commonApiPost("egov-common-masters", "holidays", "_search", { tenantId}).responseJSON["Holiday"] || [];
    } catch(e) {
        var allHolidayList = [];
    }
    ////console.log(employees);
    this.setState({
      employees : employees,
      _leaveTypes : _leaveTypes,
      hrConfigurations: hrConfigurations,
      allHolidayList : allHolidayList
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
                ////console.log("key",key);
          });

          var finalObject = [],duplicateObject = [],scannedObject = [];
          oJS.forEach(function(d){
            var employee = d[key[0]];
            employee = employee.trim();
            var leaveType = d[key[3]];
            leaveType = leaveType.trim();
            var fromDate = d[key[4]];
            fromDate = fromDate.trim();
            var toDate = d[key[5]];
            toDate = toDate.trim();
            var noOfDays = d[key[6]];
            noOfDays = noOfDays.trim();

            scannedObject.push({"employee": employee,
                              "leaveType":  { "id":leaveType },
                              "fromDate": fromDate,
                              "toDate": toDate,
                              "leaveDays" : noOfDays,
                              "reason": d[key[7]],
                              "duplicate" : "false",
                              "employeeCode" : employee,
                              "employeeName" : d[key[1]],
                              "department": d[key[2]],
                              "leaveTypeName": leaveType

            });
          });
            ////console.log("scannedObject",scannedObject);

          for(var i=0;i<scannedObject.length;i++){
              for(var j=i+1;j<=scannedObject.length-1;j++)
              if(scannedObject[i].employee===scannedObject[j].employee){
                  if(scannedObject[i].leaveType.id===scannedObject[j].leaveType.id){
                        if(scannedObject[i].fromDate===scannedObject[j].fromDate){
                            if(scannedObject[i].toDate===scannedObject[j].toDate){
                                  scannedObject[i].duplicate = "true";
                                  scannedObject[j].duplicate = "true";
                          }
                      }
                  }
              }
          }
          scannedObject.forEach(function(d){
              if(d.duplicate === "true"){
                d.errorMessage = "Duplicate row in the excel scanned";
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
          var holidayList = Object.assign([],this.state.allHolidayList);
          //console.log(holidayList);
          //console.log("TEMP ",tempInfo);
          duplicateInfo.forEach(function(d){
            errorObject.push(d);
          });
          var leaveArray =[],employeeArray=[];
          var checkLeave = [],checkEmployee=[];
          this.state._leaveTypes.forEach(function(d) {
            checkLeave.push(d.name);
          });

          this.state._leaveTypes.forEach(function(d) {
            leaveArray.push({"name":d.name,
                              "id":d.id});
          });




          this.state.employees.forEach(function(d) {
            checkEmployee.push(d.code);
          });


          this.state.employees.forEach(function(d) {
            employeeArray.push({"code":d.code,
                              "id":d.id});
          });


          var post=0,error=0,numberOfLeave;
          var i=0;
          var leaveName,calendarYearName,employeeName,calenderName,noOfDays;
          var searchName;
          var leaveId = 0,employeeId=0,calenderId=0;
          for(var k=0;k<tempInfo.length;k++){

            var d = tempInfo[k];


          ////console.log("d----->",d);

            ////console.log("from",d.fromDate);
            ////console.log("to",d.toDate);



              var from = d.fromDate;
              var to = d.toDate;
              var dateParts = from.split("/");
              var newDateStr = dateParts[1] + "/" + dateParts[0] + "/ " + dateParts[2];
              var date1 = new Date(newDateStr);
              //console.log("date1",date1);
              var dateParts = to.split("/");
              var newDateStr = dateParts[1] + "/" + dateParts[0] + "/" + dateParts[2];
              var date2 = new Date(newDateStr);
              //console.log("date2",date2);
              d.errorMessage = "";
              if (date1 > date2) {
                d.errorMessage = "from date is greater than to date ";
                error=1;
                  showError("From date must be before of End date");
                  //console.log("Greater");
              }

              if(date1.getTime() === date2.getTime()){
                //console.log("same day");
                var month = "";
                var mon =  date1.getMonth();
                switch(mon){
                  case 0: month= "January";
                          break;
                 case 1: month= "February";
                                  break;
                 case 2: month= "March";
                                 break;
                 case 3: month= "April";
                                    break;
                 case 4: month= "May";
                                   break;
                 case 5: month= "June";
                                   break;
                 case 6: month= "July";
                                   break;
                 case 7: month= "August";
                                  break;
                 case 8: month= "September";
                                   break;
                 case 9: month= "October";
                                   break;
                 case 10: month= "November";
                                 break;
                 case 11: month= "December";
                                   break;
                }
                var year = date1.getFullYear();
                var saturdays =  calculate(month,year);
                console.log("Holiday",saturdays);
                var firstSat = new Date(saturdays[0]);
                var secSat = new Date(saturdays[1]);
                if(date1.getTime() === firstSat.getTime()){
                    console.log("saturday1");
                    error=1;
                    d.errorMessage = "Leave applied on first saturday which is Holiday "+firstSat;

                }
                if(date1.getTime() === secSat.getTime()){
                  console.log("saturday1");
                  error=1;
                  d.errorMessage = "Leave applied on Second saturday which is Holiday "+secSat;
                }
                numberOfLeave = 1;
                d.successMessage = "Leaves applied successfully ";

              }



              Date.prototype.addDays = function(days) {
                var dat = new Date(this.valueOf());
                dat.setDate(dat.getDate() + days);
                return dat;
              }

          function calculate(mon,year) {
            var mon = mon;
            var yea = year;
            var dat = 1;
            var myDays = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"]
            var myDate = new Date(eval('"' + dat + ',' + mon + ',' + yea + '"'))
            var first = myDays[myDate.getDay()];
            var secnd;
            var forth;
            switch (first) {
                case "Sunday":
                    secnd = "14-" + mon + '-' + yea;
                    forth = "28-" + mon + '-' + yea;
                    break;
                case "Monday":
                    secnd = "13-" + mon + '-' + yea;
                    forth = "27-" + mon + '-' + yea;
                    break;
                case "Tuesday":
                    secnd = "12-" + mon + '-' + yea;
                    forth = "26-" + mon + '-' + yea;
                    break;
                case "Wednesday":
                    secnd = "11-" + mon + '-' + yea;
                    forth = "25-" + mon + '-' + yea;
                    break;
                case "Thursday":
                    secnd = "10-" + mon + '-' + yea;
                    forth = "24-" + mon + '-' + yea;
                    break;
                case "Friday":
                    secnd = "9-" + mon + '-' + yea;
                    forth = "23-" + mon + '-' + yea;
                    break;
                case "Saturday":
                    secnd = "8-" + mon + '-' + yea;
                    forth = "22-" + mon + '-' + yea;
                    break;
                default:
                    break;
            }
            //console.log("secnd",secnd);
            //console.log("forth",forth);
            return [secnd,forth];
        }
         var month = "";
         var mon =  date1.getMonth();
         var year = date1.getFullYear();

         switch(mon){
           case 0: month= "January";
                   break;
          case 1: month= "February";
                           break;
          case 2: month= "March";
                          break;
          case 3: month= "April";
                             break;
          case 4: month= "May";
                            break;
          case 5: month= "June";
                            break;
          case 6: month= "July";
                            break;
          case 7: month= "August";
                           break;
          case 8: month= "September";
                            break;
          case 9: month= "October";
                            break;
          case 10: month= "November";
                          break;
          case 11: month= "December";
                            break;
         }
         //console.log("MONTH",month);
         //console.log("MONTH",year);
        var saturdays =  calculate(month,year);
        console.log(saturdays);
        numberOfLeave=1;
        while(date1 < date2){
            if(mon!=date1.getMonth())
            {
              console.log("month cahnge");
              mon=date1.getMonth();
              switch(mon){
                case 0: month= "January";
                        break;
               case 1: month= "February";
                                break;
               case 2: month= "March";
                               break;
               case 3: month= "April";
                                  break;
               case 4: month= "May";
                                 break;
               case 5: month= "June";
                                 break;
               case 6: month= "July";
                                 break;
               case 7: month= "August";
                                break;
               case 8: month= "September";
                                 break;
               case 9: month= "October";
                                 break;
               case 10: month= "November";
                               break;
               case 11: month= "December";
                                 break;
              }
              saturdays= calculate(month,year);
              console.log(saturdays);
            }
            var day =  date1.getDay();
                  console.log(day);
                  console.log(date1);
                  var firstSat = new Date(saturdays[0]);
                  var secSat = new Date(saturdays[1]);
                  if(date1.getTime() === firstSat.getTime()){
                      console.log("saturday1");
                      numberOfLeave--;
                  }
                  if(date1.getTime() === secSat.getTime()){
                    console.log("saturday2");
                    numberOfLeave--;
                  }


                date1 = date1.addDays(1);
                //console.log("next day",date1);
                //console.log("final day",date2);
              if(day !== 6){
                numberOfLeave++;
                console.log("------------"+numberOfLeave+"----------");
              }

        }
              console.log("numberOfLeave",numberOfLeave);

            noOfDays = parseInt(d.noOfDays)
            leaveName = d.leaveType.id;
            employeeName = d.employee;

            var leaveValidate = checkLeave.indexOf(d.leaveType.id);
            var employeeValidate = checkEmployee.indexOf(d.employee);


            if(noOfDays<0){
              d.errorMessage = "Number of days is negative "+noOfDays;
              error=1;
              post=1;
            }

            if(leaveValidate<0){
              if(d.errorMessage===""){
                  d.errorMessage = " Leave type "+leaveName +" is not exist in system";
              }else{
                  d.errorMessage = d.errorMessage+" Leave type "+leaveName +" is not exist in system";
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
                if(d.errorMessage===""){
                    d.errorMessage = " Employee Code "+employeeName +" is not exist in system";
                }else{
                    d.errorMessage = d.errorMessage+" Employee Code "+employeeName +" is not exist in system";
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


            if(error===0) {

            serverObject.push({"employee": employeeId,
                              "leaveType":  { "id": leaveId},
                              "tenantId": tenantId,
                              "employeeCode" : d.employeeCode,
                              "employeeName" : d.employeeName,
                              "department": d.department,
                              "leaveTypeName": d.leaveTypeName,
                              "reason": d.reason,
                              "fromDate": d.fromDate,
                              "toDate": d.toDate,
                              "leaveDays":numberOfLeave,
                              "successMessage" : d.successMessage
            });
          }else{
            errorObject.push(d);
            error = 0;
            }
          }
      console.log("Success Objects",serverObject);
      console.log("Error Objects",errorObject);

        for(i=0;i<serverObject.length;i++){
          try{
             var leaveType = serverObject[i].leaveType["id"];
             var asOnDate = serverObject[i].toDate;
             var employeeid = serverObject[i].employee;
             ////console.log(leaveType,asOnDate,employeeid);
             var object =  commonApiPost("hr-leave","eligibleleaves","_search",{leaveType,tenantId,asOnDate,employeeid}).responseJSON["EligibleLeave"][0];
              ////console.log(object);

              }
              catch (e){
                ////console.log(e);
              }
          }



            ////console.log("HR",hrConfigurations);
            ////console.log("all",allHolidayList);




          // var calenderYearApi = serverObject[0].calendarYear;
          //
          // try {
          //     var leaveBal = commonApiPost("hr-leave", "leaveopeningbalances", "_search", {
          //         tenantId,
          //         pageSize: 500,
          //         year : calenderYearApi
          //       }).responseJSON["LeaveOpeningBalance"] || [];
          // } catch(e) {
          //     var leaveBal = [];
          // }

        ////console.log(leaveBal);
          var errorLeaveOpening=[]
          var exists = false;
          for(var i=0;i<serverObject.length;i++){
          ////console.log("Success Object-->",serverObject[i]);
                var calendarNumber = parseInt(serverObject[i].calendarYear);
                exists = false;
                for(var j=0;j<leaveBal.length;j++){
                  if(calendarNumber===leaveBal[j].calendarYear){
                     if(serverObject[i].employee===leaveBal[j].employee){
                         if(serverObject[i].leaveType["id"]===leaveBal[j].leaveType["id"]){
                         ////console.log("Duplicate object from server -->",serverObject[i]);
                                 exists=true;
                                 serverObject[i].errorMessage = "Leave opening balance already present in the system for this Employee";
                                 errorLeaveOpening.push(serverObject[i]);
                                 break;
                            }
                      }
                    }
                  }

                  if(exists===false){
                  ////console.log("finalSuccessObject-->",serverObject[i]);
                    serverObject[i].successMessage = "Employee leaves created successfully";
                    finalValidatedServerObject.push(serverObject[i]);
                  }

          }
        ////console.log("Data already present",errorLeaveOpening);

          errorLeaveOpening.forEach(function(d){
            errorObject.push(d);
          });

        ////console.log("Final Server Object After Validation",finalValidatedServerObject);
        ////console.log("Final Error Object After Validation",errorObject);

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
            ////console.log(d);
                finalSuccessObject.push({"employee": d.employee,
                                "calendarYear": d.calendarYear,
                                "leaveType":  { "id": d.leaveType["id"]},
                                "noOfDays" : d.noOfDays,
                                "tenantId": d.tenantId
                              });
            });
          ////console.log("FINSSL SNJNCJ",finalSuccessObject);

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
                <a href="resources/sample/leave_application_v1.xlsx" target="_blank">Download Sample Template</a>
      </div>
    );
  }
}

ReactDOM.render(
  <UploadLeaveApplication />,
  document.getElementById('root')
);
