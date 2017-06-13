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
    this.setInitialState=this.setInitialState.bind(this);
  }

  setInitialState(initState) {
    this.setState(initState);
  }

  componentDidMount(){
    if(window.opener && window.opener.document) {
    var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
    if(logo_ele && logo_ele[0]) {
        document.getElementsByClassName("homepage_logo")[0].src = window.location.origin + logo_ele[0].getAttribute("src");
       }
    }

    var count = 4, _this = this, _state = {};
    var checkCountNCall = function(key, res) {
    count--;
    _state[key] = res;
    if(count == 0)
      _this.setInitialState(_state);
    }

    getDropdown("leaveTypes", function(res) {
      checkCountNCall("_leaveTypes", res);
    });

    getCommonMaster("hr-employee","employees",function(err,res){
      checkCountNCall("employees", res ? res.Employee : [])
    });

    commonApiPost("hr-masters", "hrconfigurations", "_search",{tenantId},function(err, res){
      checkCountNCall("hrConfigurations", res || {})
    });

    commonApiPost("egov-common-masters", "holidays", "_search", {tenantId},function(err,res) {
      checkCountNCall("allHolidayList", res ? res.Holiday : [])
    })
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

  isSecondSat (d) {
    return (d.getDay() == 6 && Math.ceil(d.getDate()/7) == 2);
  }

  isFourthSat (d) {
    return (d.getDay() == 6 && Math.ceil(d.getDate()/7) == 4);
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
         var allHolidayList = Object.assign([],this.state.allHolidayList);
         var hrConfigurations = Object.assign([],this.state.hrConfigurations);

          console.log("_leaveTypes",this.state.employees);
         console.log("_leaveTypes",this.state._leaveTypes);
         console.log(hrConfigurations);
         console.log("TEMP ",allHolidayList);
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


         var post=0,error=0,numberOfLeave,errorList=[],successList=[],serverSuccessList=[];
         var i=0;
         var leaveName,calendarYearName,employeeName,calenderName,noOfDays;
         var searchName;
         var leaveId = 0,employeeId=0,calenderId=0;
         for(var k=0;k<tempInfo.length;k++){

           var d = tempInfo[k];
           ////console.log("from",d.fromDate);
           ////console.log("to",d.toDate);
             d.errorMsg="";
             var _from = d.fromDate;
             var _to = d.toDate;
             if(_from && _to) {
               var dateParts1 = _from.split("/");
               var newDateStr = dateParts1[1] + "/" + dateParts1[0] + "/ " + dateParts1[2];
               var date1 = new Date(newDateStr);
               var dateParts2 = _to.split("/");
               var newDateStr = dateParts2[1] + "/" + dateParts2[0] + "/" + dateParts2[2];
               var date2 = new Date(newDateStr);

               if (date1 > date2) {
                 console.log("From date must be before End date.");
                 showError("From date must be before End date.");
               } else {
                 var holidayList = [], m1 = dateParts1[1], m2 = dateParts2[1], y1 = dateParts1[2], y2 = dateParts2[2];
                 for(var i=0; i<allHolidayList.length;i++) {
                   if(allHolidayList[i].applicableOn && +allHolidayList[i].applicableOn.split("/")[1] >= +m1 && +allHolidayList[i].applicableOn.split("/")[1] <= +m2 && +allHolidayList[i].applicableOn.split("/")[2] <= y1 && +allHolidayList[i].applicableOn.split("/")[2] >= y2) {
                     holidayList.push(new Date(allHolidayList[i].applicableOn.split("/")[2], allHolidayList[i].applicableOn.split("/")[1]-1, allHolidayList[i].applicableOn.split("/")[0]).getTime());
                   }
                 }
                 //Calculate working days
                 var _days = 0;
                 var parts1 = _from.split("/");
                 var parts2 = _to.split("/");
                 var startDate = new Date(parts1[2], (+parts1[1]-1), parts1[0]);
                 var endDate = new Date(parts2[2], (+parts2[1]-1), parts2[0]);

                 if(hrConfigurations["HRConfiguration"]["Weekly_holidays"][0] == "5-day week") {
                   for (var p = startDate ; p <= endDate; p.setDate(p.getDate() + 1)) {
                       if(holidayList.indexOf(p.getTime()) == -1 && hrConfigurations["HRConfiguration"]["Include_enclosed_holidays"][0]!="Y" && !(p.getDay()===0||p.getDay()===6))
                           _days++;
                    }
                 } else if (hrConfigurations["HRConfiguration"]["Weekly_holidays"][0] == "5-day week with 2nd Saturday holiday") {
                   for (var p = startDate ; p <= endDate; p.setDate(p.getDate() + 1)) {
                       if(holidayList.indexOf(p.getTime()) == -1 && hrConfigurations["HRConfiguration"]["Include_enclosed_holidays"][0]!="Y" && !_this.isSecondSat(p) && p.getDay() != 0)
                           _days++;
                   }
                 } else if (hrConfigurations["HRConfiguration"]["Weekly_holidays"][0] == "5-day week with 2nd and 4th Saturday holiday"){
                   for (var p = startDate ; p <= endDate; p.setDate(p.getDate() + 1)) {
                       if(holidayList.indexOf(p.getTime()) == -1 && hrConfigurations["HRConfiguration"]["Include_enclosed_holidays"][0]!="Y" && p.getDay() != 0 && !_this.isSecondSat(p) && !_this.isFourthSat(p))
                           _days++;
                   }
                 } else {
                   for (var p = startDate ; p <= endDate; p.setDate(p.getDate() + 1)) {
                       if(holidayList.indexOf(p.getTime()) == -1 && hrConfigurations["HRConfiguration"]["Include_enclosed_holidays"][0]!="Y" && !(p.getDay()===0))
                         _days++;
                   }
                 }
                 console.log(_days);
               }
             }
             if(_days===0){
               d.errorMsg = "Leave applied on a Holiday";
               error=1;
               post=1;
             }

             console.log("d----->",d);
             d.leaveDays = _days;



           noOfDays = parseInt(d.noOfDays);
           leaveName = d.leaveType.id;
           employeeName = d.employee;

           var leaveValidate = checkLeave.indexOf(d.leaveType.id);
           var employeeValidate = checkEmployee.indexOf(d.employee);


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
                             "leaveDays":d.leaveDays,
                             "successMessage" : d.successMessage,
                             "duplicate":false
           });


         }else{
           errorObject.push(d);
           error = 0;
           }
         }
   console.log("Success Objects",serverObject);
     console.log("Error Objects",errorObject);


     // date1 = date1.addDays(1);
     for(var i=0;i<serverObject.length;i++){
         for(var j=i+1;j<=serverObject.length-1;j++){
           console.log("serverObject[i]",i,serverObject[i]);
           console.log("serverObject[i]",j,serverObject[j]);
         if(serverObject[i].duplicate===false && serverObject[j].duplicate===false)
         {
               if(serverObject[i].employee===serverObject[j].employee){
                 console.log("duplicate1",serverObject[i]);
                 console.log("duplicate2",serverObject[j])

                   if(serverObject[i].fromDate===serverObject[j].fromDate){
                     console.log("duplicate");
                     serverObject[i].duplicate = "true";
                     serverObject[j].duplicate = "true";

                   }else if(serverObject[i].toDate===serverObject[j].toDate){
                     console.log("duplicate");
                     serverObject[i].duplicate = "true";
                     serverObject[j].duplicate = "true";

                   }
                   var fromDateToLoop,toDateToLoop,array1=[],array2=[],toDate2,fromDate2;
                   fromDateToLoop = serverObject[i].fromDate;
                  toDateToLoop = serverObject[i].toDate;
                  fromDate2 = serverObject[j].fromDate;
                  toDate2 = serverObject[j].toDate;


                   var dateParts1 = fromDateToLoop.split("/");
                    var newDateStr = dateParts1[1] + "/" + dateParts1[0] + "/ " + dateParts1[2];
                    var date1 = new Date(newDateStr);
                    var dateParts2 = toDateToLoop.split("/");
                    var newDateStr = dateParts2[1] + "/" + dateParts2[0] + "/" + dateParts2[2];
                    var date2 = new Date(newDateStr);


                    var dateParts3 = fromDate2.split("/");
                     var newDateStr = dateParts3[1] + "/" + dateParts3[0] + "/ " + dateParts3[2];
                     var date3 = new Date(newDateStr);
                     var dateParts4 = toDate2.split("/");
                     var newDateStr = dateParts4[1] + "/" + dateParts4[0] + "/" + dateParts4[2];
                     var date4 = new Date(newDateStr);


                    Date.prototype.addDays = function(days) {
                         var dat = new Date(this.valueOf())
                         dat.setDate(dat.getDate() + days);
                         return dat;
                     }

                     function getDates(startDate, stopDate) {
                        var dateArray = new Array();
                        var currentDate = startDate;
                        while (currentDate <= stopDate) {
                          dateArray.push(currentDate)
                          currentDate = currentDate.addDays(1);
                        }
                        return dateArray;
                      }

                   var dateArray1 = getDates(date1, date2);
                   var dateArray2 = getDates(date3, date4);



                   console.log("First",dateArray1);
                   console.log("Second",dateArray2);

                   for(var m=0;m<dateArray1.length;m++){
                     for(var n=0;n<dateArray2.length;n++){
                       console.log("dateArray1[m]",dateArray1[m]);
                       console.log("dateArray2[n]",dateArray2[n]);
                       if(dateArray1[m].getTime()===dateArray2[n].getTime()){
                       console.log("duplicate");
                       serverObject[i].duplicate = "true";
                       serverObject[j].duplicate = "true";
                       break;
                       }
                     }
                   }

         }
       }
     }
   }
   console.log("FINALDKK  KJHKU",serverObject);
     serverObject.forEach(function(d){
         if(d.duplicate === "true"){
           d.errorMsg = "Leave days already present in the scanned Excel";
           d.errorMsg = "Leave days already present in the scanned Excel";
           errorObject.push(d);
         }else {
           d.successMessage = "Employee leaves created successfully";
           finalValidatedServerObject.push(d);
         }
     });

     console.log("Final",finalValidatedServerObject);
     console.log("finalSuccessObject",finalSuccessObject.length);
     console.log("length",finalValidatedServerObject.length);
     console.log("Error",errorObject);

     for(var n=0;n<finalValidatedServerObject.length;n++){
       console.log(n);
       var d = finalValidatedServerObject[n];
         finalSuccessObject.push({
           "employee": d.employee,
           "leaveType": {
             "id": d.leaveType["id"]
           },
           "fromDate": d.fromDate,
           "toDate": d.toDate,
           "availableDays": 0,
           "leaveDays": d.leaveDays,
           "reason":  d.reason,
           "status": "",
           "stateId": "",
           "tenantId": tenantId,
           "workflowDetails": {
             "department": "",
             "designation": "",
             "assignee": "",
             "action": "",
             "status": ""
           }
         });
     }
          console.log("finalSuccessObject",finalSuccessObject);




         if(serverObject.length!==0){

           var body={
             "RequestInfo":requestInfo,
             "LeaveApplication":finalSuccessObject
           },_this=this;

           $.ajax({

                 url: baseUrl + "/hr-leave/leaveapplications/_create?tenantId=" + tenantId+"&type=upload",
                 type: 'POST',
                 dataType: 'json',
                 data:JSON.stringify(body),
                 contentType: 'application/json',
                 headers:{
                   'auth-token': authToken
                 },
                 success: function(res) {
                   console.log("res",res);
                   errorList = res.ErrorList;
                    serverSuccessList =  res.SuccessList;
                   if(serverSuccessList.length!==0){
                     console.log("serverSuccessList",serverSuccessList);
                     for(var t=0;t<serverSuccessList.length;t++){
                       for(var q=0;q<finalValidatedServerObject.length;q++){
                          if(serverSuccessList[t].employee===finalValidatedServerObject[q].employee && serverSuccessList[t].fromDate===finalValidatedServerObject[q].fromDate && serverSuccessList[t].toDate===finalValidatedServerObject[q].toDate){
                             successList.push(finalValidatedServerObject[q]);
                             console.log("g--->",finalValidatedServerObject[q]);
                             break;
                          }
                       }
                     }
                     var ep1=new ExcelPlus();
                     var b=0;

                       ep1.createFile("Success");
                       ep1.write({ "content":[ ["Employee Code","Employee Name","Department","Leave type","Leave from date (dd/mm/yyyy)","Leave to date (dd/mm/yyyy)","No of days","Reason","Success Message"] ] });
                       for(b=0;b<successList.length;b++){
                         ep1.writeNextRow([successList[b].employeeCode,successList[b].employeeName,successList[b].department,successList[b].leaveTypeName,successList[b].fromDate,successList[b].toDate,successList[b].leaveDays,successList[b].reason,successList[b].successMessage])
                       }
                       ep1.saveAs("success.xlsx");
                   }

                 console.log("successListlast",successList);


                 if(errorList.length!==0){

                   for(var t=0;t<errorList.length;t++){
                     for(var q=0;q<finalValidatedServerObject.length;q++){
                        if(errorList[t].employee===finalValidatedServerObject[q].employee && errorList[t].fromDate===finalValidatedServerObject[q].fromDate && errorList[t].toDate===finalValidatedServerObject[q].toDate){
                           finalValidatedServerObject[q].errorMsg = errorList[t].errorMsg;
                           errorObject.push(finalValidatedServerObject[q]);
                           console.log("g--->",finalValidatedServerObject[q]);
                           break;
                        }
                     }
                   }
                   var ep2=new ExcelPlus();
                   var b=0;
                   console.log("errorObject",errorObject);
                   console.log("errorList",errorList);
                   ep2.createFile("Error");
                   ep2.write({ "content":[ ["Employee Code","Employee Name","Department","Leave type","Leave from date (dd/mm/yyyy)","Leave to date (dd/mm/yyyy)","No of days","Reason","Error Message"] ] });
                   for(b=0;b<errorObject.length;b++){
                     ep2.writeNextRow([errorObject[b].employeeCode,errorObject[b].employeeName,errorObject[b].department,errorObject[b].leaveTypeName,errorObject[b].fromDate,errorObject[b].toDate,errorObject[b].leaveDays,errorObject[b].reason,errorObject[b].errorMsg])
                   }
                   ep2.saveAs("error.xlsx");

                 }


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
             var ep2=new ExcelPlus();
             var b=0;

             ep2.createFile("Error");
             ep2.write({ "content":[ ["Employee Code","Employee Name","Department","Leave type","Leave from date (dd/mm/yyyy)","Leave to date (dd/mm/yyyy)","No of days","Reason","Error Message"] ] });
             for(b=0;b<errorObject.length;b++){
               ep2.writeNextRow([errorObject[b].employeeCode,errorObject[b].employeeName,errorObject[b].department,errorObject[b].leaveTypeName,errorObject[b].fromDate,errorObject[b].toDate,errorObject[b].leaveDays,errorObject[b].reason,errorObject[b].errorMsg])
             }
             ep2.saveAs("error.xlsx");
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
