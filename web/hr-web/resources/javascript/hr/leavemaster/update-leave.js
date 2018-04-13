function today() {
  var today = new Date();
  var dd = today.getDate();
  var mm = today.getMonth() + 1;
  var yyyy = today.getFullYear();
  if (dd < 10) {
    dd = '0' + dd;
  }
  if (mm < 10) {
    mm = '0' + mm;
  }
  var today = dd + '/' + mm + '/' + yyyy;

  return today;
}

class UpdateLeave extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      list: [],
      leaveSet: {
        "employee": "",
        "name": "",
        "code": "",
        "leaveType": {
          "id": ""
        },
        "fromDate": "",
        "toDate": "",
        "availableDays": "",
        "leaveGround": "",
        "leaveDays": "",
        "reason": "",
        "status": "",
        "stateId": "",
        "tenantId": tenantId,
        "encashable": false,
        "totalWorkingDays": "",
        "workflowDetails": {
          "department": "",
          "designation": "",
          "assignee": "",
          "action": "",
          "status": ""
        }
      }, leaveNumber: "", employeeid: "", positionId: "",
      leaveList: [],
      positionList: [],
      departmentList: [],
      designationList: [],
      userList: [],
      buttons: []
    }
    this.handleChange = this.handleChange.bind(this);
    this.handleChangeThreeLevel = this.handleChangeThreeLevel.bind(this);
    this.getPrimaryAssigmentDep = this.getPrimaryAssigmentDep.bind(this);
    this.handleProcess = this.handleProcess.bind(this);
    this.getUsersFun = this.getUsersFun.bind(this);
    this.setInitialState = this.setInitialState.bind(this);

  }

  setInitialState(initState) {
    this.setState(initState);
  }

  componentDidMount() {
    if (window.opener && window.opener.document) {
      var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
      if (logo_ele && logo_ele[0]) {
        document.getElementsByClassName("homepage_logo")[0].src = window.location.origin + logo_ele[0].getAttribute("src");
      }
    }
    var type = getUrlVars()["type"], _this = this;
    var stateId = getUrlVars()["stateId"];
    var asOnDate = _this.state.leaveSet.toDate;
    var process = [], employee;
    var _leaveSet = {}, prefixSuffixDays = "", enclosingDays = "";
    var hrConfigurations = [], allHolidayList = [];
    $('#availableDays,#leaveDays,#name,#code').prop("disabled", true);

    var _state = {}, count = 3;
    const checkCountAndCall = function (key, res) {
      _state[key] = res;
      count--;
      if (count == 0) {
        _this.setInitialState(_state);
      }
    }

    getDropdown("assignments_designation", function (res) {
      checkCountAndCall("designationList", res);
    });
    getDropdown("assignments_department", function (res) {
      checkCountAndCall("departmentList", res);
    });
    getDropdown("assignments_position", function (res) {
      checkCountAndCall("positionList", res);
    });

    commonApiPost("hr-masters", "hrstatuses", "_search", { tenantId, objectName: "LeaveApplication" }, function (err, res) {
      if (res && res.HRStatus) {
        _this.setState({
          statusList: res.HRStatus
        })
      }
    });

    commonApiPost("hr-masters", "hrconfigurations", "_search", {
      tenantId,
      asOnDate
    }, function (err, res) {
      if (res) {
        hrConfigurations = res;
      }
    })
    commonApiPost("egov-common-masters", "holidays", "_search", {
      tenantId
    }, function (err, res) {
      allHolidayList = res ? res.Holiday : [];
    });

    getCommonMaster("hr-leave", "leavetypes", function (err, res) {
      if (res) {
        _this.setState({
          leaveList: res.LeaveType
        })
      }
    })

    commonApiPost("hr-leave", "leaveapplications", "_search", { tenantId, stateId }, function (err, res) {
      if (res && res.LeaveApplication && res.LeaveApplication[0]) {
        _leaveSet = res.LeaveApplication[0];

        if (!_leaveSet.workflowDetails)
          _leaveSet.workflowDetails = {};

        commonApiPost("hr-employee", "employees", "_search", {
          tenantId,
          id: _leaveSet.employee
        }, function (err, res1) {
          if (res1 && res1.Employee && res1.Employee[0]) {
            employee = res1.Employee[0];
            _leaveSet.name = employee.name;
            _leaveSet.code = employee.code;
            _leaveSet.leaveGround = _leaveSet.leaveGround ? _leaveSet.leaveGround : "";
            _leaveSet.totalWorkingDays = _leaveSet.leaveDays;

            _this.setState({
              leaveSet: Object.assign({}, _leaveSet),
              leaveNumber: _leaveSet.applicationNumber,
              departmentId: _this.getPrimaryAssigmentDep(employee, "department"),
              employeeid: employee.id
            })
          } else {
            showError("Something went wrong please contact Administrator");
          }
        });


        //Calling enclosing Holiday api
        let enclosingHoliday = getNameById(_this.state.leaveList, _leaveSet.leaveType.id, "encloseHoliday");
        if (enclosingHoliday || enclosingHoliday == "TRUE" || enclosingHoliday == "true") {
          commonApiPost("egov-common-masters", "holidays", "_search", { tenantId, fromDate, toDate: asOnDate }, function (err, res) {
            if (res) {
              console.log("enclosingDays", res.Holiday.length);
              enclosingDays = res.Holiday.length;
              _this.setState({
                encloseHoliday: res.Holiday
              });
            }
          });
        } else {
          _this.setState({
            encloseHoliday: ""
          });
        }

        //calling PrefixSuffix api
        let includePrefixSuffix = getNameById(_this.state.leaveList, _this.state.leaveSet.leaveType.id, "includePrefixSuffix");
        if (includePrefixSuffix || includePrefixSuffix == "TRUE" || includePrefixSuffix == "true") {
          commonApiPost("egov-common-masters", "holidays", "_searchprefixsuffix", { tenantId, fromDate, toDate: asOnDate }, function (err, res) {
            if (res) {
              console.log("prefixSuffixDays", res.Holiday[0].noOfDays);
              prefixSuffixDays = res.Holiday[0].noOfDays;
              _this.setState({
                perfixSuffix: res.Holiday[0]
              });
            }
          });
        } else {
          _this.setState({
            perfixSuffix: ""
          });
        }

        setTimeout(function () {
          console.log("prefixSuffixDays ", prefixSuffixDays, " ", enclosingDays);
          _this.setState({
            leaveSet: {
              ..._this.state.leaveSet,
              leaveDays: _leaveSet.leaveDays - (prefixSuffixDays + enclosingDays)
            }
          })
        }, 500);


      }
      else {
        showError("Something went wrong please contact Administrator");
      }
    });

    $('#fromDate, #toDate').datepicker({
      format: 'dd/mm/yyyy',
      autoclose: true

    });

    $('#fromDate, #toDate').on("change", function (e) {
      var _from = $('#fromDate').val();
      var _to = $('#toDate').val();
      var _triggerId = e.target.id;

      if (_this.state.leaveSet.leaveType.id) {

        _this.setState({
          leaveSet: {
            ..._this.state.leaveSet,
            [_triggerId]: $("#" + _triggerId).val()
          }
        });

        if (_from && _to) {

          let dateParts1 = _from.split("/");
          let newDateStr = dateParts1[1] + "/" + dateParts1[0] + "/ " + dateParts1[2];
          let date1 = new Date(newDateStr);

          let dateParts2 = _to.split("/");
          let newDateStr1 = dateParts2[1] + "/" + dateParts2[0] + "/" + dateParts2[2];
          let date2 = new Date(newDateStr1);


          if (date1 > date2) {
            showError("From date must be before End date.");
            $('#' + _triggerId).val("");
          }


          _this.calculate();
        }

      } else {
        showError("Please select Leave Type before entering from date and to date.");
        $('#' + _triggerId).val("");
      }
    });

    commonApiPost("egov-common-workflows", "process", "_search", {
      tenantId: tenantId,
      id: stateId
    }, function (err, res) {
      if (res) {

        process = res["processInstance"];



        $.ajax({
          url: baseUrl + "/egov-common-workflows/designations/_search?businessKey=" + process.businessKey + "&approvalDepartmentName=&departmentRule=&currentStatus=" + process.status + "&tenantId=" + tenantId + "&additionalRule=&pendingAction=&designation=&amountRule=",
          type: 'POST',
          dataType: 'json',
          data: JSON.stringify({ RequestInfo: requestInfo }),
          headers: {
            'auth-token': authToken
          },
          contentType: 'application/json',
          success: function (result) {
            if (result) {
              _this.setState({
                designationList: result
              })
            }
          },
          error: function (error) {
            console.log(error);
          }
        });

        if (process && process.attributes && process.attributes.validActions && process.attributes.validActions.values && process.attributes.validActions.values.length) {
          var _btns = [];
          for (var i = 0; i < process.attributes.validActions.values.length; i++) {
            if (process.attributes.validActions.values[i].key) {
              _btns.push({
                key: process.attributes.validActions.values[i].key,
                name: process.attributes.validActions.values[i].name
              });
            }
          }

          _this.setState({
            positionId: process.owner.id,
            buttons: _btns.length ? _btns : [],
          })
        }
      }
    });


  }

  componentDidUpdate() {

    let status = getNameById(this.state.statusList, this.state.leaveSet.status, "code");

    if (status != "REJECTED") {
      $("input,select,textarea").prop("disabled", true);
    }

    if (status == "APPLIED") {
      $("#department, #designation, #assignee").prop("disabled", false);
    }

  }

  calculate() {

    let _this = this;
    let asOnDate = _this.state.leaveSet.toDate;
    let fromDate = _this.state.leaveSet.fromDate;
    let leaveDays = _this.state.leaveSet.leaveDays;
    let leaveType = _this.state.leaveSet.leaveType.id;
    let employeeid = getUrlVars()["id"] || _this.state.leaveSet.employee;
    let allHolidayList = _this.state.allHolidayList;
    let hrConfigurations = _this.state.hrConfigurations;
    let enclosingDays = 0;
    let prefixSuffixDays = 0;

    //Calling enclosing Holiday api
    let enclosingHoliday = getNameById(_this.state.leaveList, _this.state.leaveSet.leaveType.id, "encloseHoliday");
    if (enclosingHoliday || enclosingHoliday == "TRUE" || enclosingHoliday == "true") {
      commonApiPost("egov-common-masters", "holidays", "_search", { tenantId, fromDate, toDate: asOnDate }, function (err, res) {
        if (res) {
          console.log("enclosingDays", res.Holiday.length);
          enclosingDays = res.Holiday.length;
          _this.setState({
            encloseHoliday: res.Holiday
          });
        }
      });
    } else {
      _this.setState({
        encloseHoliday: ""
      });
    }

    //calling PrefixSuffix api
    let includePrefixSuffix = getNameById(_this.state.leaveList, _this.state.leaveSet.leaveType.id, "includePrefixSuffix");
    if (includePrefixSuffix || includePrefixSuffix == "TRUE" || includePrefixSuffix == "true") {
      commonApiPost("egov-common-masters", "holidays", "_searchprefixsuffix", { tenantId, fromDate, toDate: asOnDate }, function (err, res) {
        if (res) {
          console.log("prefixSuffixDays", res.Holiday[0].noOfDays);
          prefixSuffixDays = res.Holiday[0].noOfDays;
          _this.setState({
            perfixSuffix: res.Holiday[0]
          });
        }
      });
    } else {
      _this.setState({
        perfixSuffix: ""
      });
    }

    var holidayList = [], m1 = fromDate.split("/")[1], m2 = asOnDate.split("/")[1], y1 = fromDate.split("/")[2], y2 = asOnDate.split("/")[2];
    for (var i = 0; i < allHolidayList.length; i++) {
      if (allHolidayList[i].applicableOn && +allHolidayList[i].applicableOn.split("/")[1] >= +m1 && +allHolidayList[i].applicableOn.split("/")[1] <= +m2 && +allHolidayList[i].applicableOn.split("/")[2] <= y1 && +allHolidayList[i].applicableOn.split("/")[2] >= y2) {
        holidayList.push(new Date(allHolidayList[i].applicableOn.split("/")[2], allHolidayList[i].applicableOn.split("/")[1] - 1, allHolidayList[i].applicableOn.split("/")[0]).getTime());
      }
    }
    //Calculate working days
    var _days = 0;
    var parts1 = $('#fromDate').val().split("/");
    var parts2 = $('#toDate').val().split("/");
    var startDate = new Date(parts1[2], (+parts1[1] - 1), parts1[0]);
    var endDate = new Date(parts2[2], (+parts2[1] - 1), parts2[0]);

    if (hrConfigurations["HRConfiguration"]["Weekly_holidays"][0] == "5-day week") {
      for (var d = startDate; d <= endDate; d.setDate(d.getDate() + 1)) {
        if (holidayList.indexOf(d.getTime()) == -1 && hrConfigurations["HRConfiguration"]["Include_enclosed_holidays"][0] != "Y" && !(d.getDay() === 0 || d.getDay() === 6))
          _days++;
      }
    } else if (hrConfigurations["HRConfiguration"]["Weekly_holidays"][0] == "5-day week with 2nd Saturday holiday") {
      for (var d = startDate; d <= endDate; d.setDate(d.getDate() + 1)) {
        if (holidayList.indexOf(d.getTime()) == -1 && hrConfigurations["HRConfiguration"]["Include_enclosed_holidays"][0] != "Y" && !_this.isSecondSat(d) && d.getDay() != 0)
          _days++;
      }
    } else if (hrConfigurations["HRConfiguration"]["Weekly_holidays"][0] == "5-day week with 2nd and 4th Saturday holiday") {
      for (var d = startDate; d <= endDate; d.setDate(d.getDate() + 1)) {
        if (holidayList.indexOf(d.getTime()) == -1 && hrConfigurations["HRConfiguration"]["Include_enclosed_holidays"][0] != "Y" && d.getDay() != 0 && !_this.isSecondSat(d) && !_this.isFourthSat(d))
          _days++;
      }
    } else {
      for (var d = startDate; d <= endDate; d.setDate(d.getDate() + 1)) {
        if (holidayList.indexOf(d.getTime()) == -1 && hrConfigurations["HRConfiguration"]["Include_enclosed_holidays"][0] != "Y" && !(d.getDay() === 0))
          _days++;
      }
    }


    setTimeout(function () {
      console.log("prefixSuffixDays ", prefixSuffixDays, " ", enclosingDays);
      _this.setState({
        leaveSet: {
          ..._this.state.leaveSet,
          leaveDays: _days,
          totalWorkingDays: _days + prefixSuffixDays + enclosingDays
        }
      })
    }, 500);


    commonApiPost("hr-leave", "eligibleleaves", "_search", {
      leaveType, tenantId, asOnDate, employeeid
    }, function (err, res) {
      if (res) {
        var _day = res && res["EligibleLeave"] && res["EligibleLeave"][0] ? res["EligibleLeave"][0].noOfDays : "";
        if (_day <= 0 || _day == "") {
          _this.setState({
            leaveSet: {
              ..._this.state.leaveSet,
              availableDays: ""
            }
          });
          return (showError("You do not have leave for this leave type."));
        }
        else {
          _this.setState({
            leaveSet: {
              ..._this.state.leaveSet,
              availableDays: _day
            }
          });
        }
      }
    });

  }

  printNotice(noticeData) {

    console.log(noticeData);
    var today = new Date();
    var dd = today.getDate();
    var mm = today.getMonth() + 1;
    var yyyy = today.getFullYear();
    if (dd < 10) {
      dd = '0' + dd;
    }
    if (mm < 10) {
      mm = '0' + mm;
    }
    var today = dd + '.' + mm + '.' + yyyy;

    let financialYear = "";
    if (new Date().getMonth() > 3)
      financialYear = new Date().getFullYear() + "-" + (new Date().getFullYear() + 1);
    else
      financialYear = (new Date().getFullYear() - 1) + "-" + new Date().getFullYear();

    var doc = new jsPDF();

    let encashable = noticeData.encashable;
    if (!(encashable || encashable == "TRUE" || encashable == "true")) {

      doc.setFont('times', 'normal');
      doc.setFontSize(12);
      doc.setFontType("bold");
      doc.text(105, 20, "PROCEEDINGS OF THE COMMISSIONER", 'center');
      doc.text(105, 27, tenantId.split(".")[1].toUpperCase() + ' MUNICIPALITY/MUNICIPAL CORPORATION', 'center');

      doc.text(15, 42, 'Roc.No. ' + noticeData.applicationNumber + '/2018');
      doc.setLineWidth(0.5);
      doc.line(15, 43, 66, 43);
      doc.text(130, 42, 'Dt. ' + today);
      doc.setLineWidth(0.5);
      doc.line(130, 43, 156, 43);

      doc.setFontType("bold");
      doc.text(35, 52, "Sub:");
      doc.setFontType("normal");
      doc.text(55, 52, doc.splitTextToSize('Establish -' + tenantId.split(".")[1].charAt(0).toUpperCase() + tenantId.split(".")[1].slice(1) + ' Muncipality/Municipal Corporation - '));
      doc.text(55, 57, doc.splitTextToSize('Sri/Smt' + noticeData.name + ' - Saction of ' + noticeData.leaveTypeName + ' for period of (' + noticeData.totalWorkingDays + ')'));
      doc.text(55, 62, doc.splitTextToSize('days on' + noticeData.leaveGround + ' grounds - Orders - Issued'));

      doc.setFontType("bold");
      doc.text(35, 72, "Read:");
      doc.setFontType("normal");
      doc.text(55, 72, doc.splitTextToSize('Representation of Sri/Smt/Kum ' + noticeData.name + '.'));

      doc.setFontType("bold");
      doc.text(15, 82, 'ORDER:');
      doc.setLineWidth(0.5);
      doc.line(15, 83, 32, 83);
      doc.setFontType("normal");
      doc.text(15, 92, doc.splitTextToSize('        In view of the circumstances stated in the reference read above Sri/Smt ' + noticeData.name + ','));
      doc.text(15, 97, doc.splitTextToSize('is hereby sactioned Earned Leave/HPL for a period of (' + noticeData.totalWorkingDays + ') days on ' + noticeData.leaveGround + ' grounds from ' + noticeData.fromDate + ' to ' + noticeData.toDate + '.'));
      doc.text(15, 105, doc.splitTextToSize('2.     Certified that necessary entries have been made in the service Register of the individual.'));
      doc.text(15, 113, doc.splitTextToSize('3.     He/She is informed that, after sanction of the above leave is having (' + noticeData.availableDays + ') days of '));
      doc.text(15, 118, doc.splitTextToSize('       ' + noticeData.leaveTypeName + ' at his/her credit.'));

      doc.setFontType("bold");
      doc.text(125, 134, 'COMMISSIONER');
      doc.text(100, 140, tenantId.split(".")[1].charAt(0).toUpperCase() + tenantId.split(".")[1].slice(1) + ' Municipality/Municipal Corporation');

      doc.setFontType("bold");
      doc.text(15, 145, doc.splitTextToSize('To'));
      doc.setFontType("normal");
      doc.text(15, 150, doc.splitTextToSize('Sri/Smt _______'));
      doc.text(15, 155, doc.splitTextToSize('Copy to the _________'));
      doc.text(15, 160, doc.splitTextToSize('Spare'));

    } else {

      doc.setFont('times', 'normal');
      doc.setFontSize(12);
      doc.setFontType("bold");
      doc.text(105, 20, "PROCEEDINGS OF THE COMMISSIONER", 'center');
      doc.text(105, 27, tenantId.split(".")[1].toUpperCase() + ' MUNICIPALITY/MUNICIPAL CORPORATION', 'center');

      doc.text(15, 42, 'Roc.No. ' + noticeData.applicationNumber + '/2018');
      doc.setLineWidth(0.5);
      doc.line(15, 43, 66, 43);
      doc.text(130, 42, 'Dt. ' + today);
      doc.setLineWidth(0.5);
      doc.line(130, 43, 156, 43);

      doc.setFontType("bold");
      doc.text(35, 52, "Sub:");
      doc.setFontType("normal");
      doc.text(55, 52, doc.splitTextToSize('Establish -' + tenantId.split(".")[1].charAt(0).toUpperCase() + tenantId.split(".")[1].slice(1) + ' Muncipality/Municipal Corporation - '));
      doc.text(55, 57, doc.splitTextToSize('Sri/Smt ' + noticeData.name + ' - Saction of ' + noticeData.leaveTypeName + ' for period of (' + noticeData.totalWorkingDays + ')'));
      doc.text(55, 62, doc.splitTextToSize('days on ' + noticeData.leaveGround + ' grounds - Orders - Issued'));

      doc.setFontType("bold");
      doc.text(35, 72, "Read:");
      doc.setFontType("normal");
      doc.text(55, 72, doc.splitTextToSize('1) Govt. Circular Memo No. 4338/-A/95/FR-I/13, Dt. 18.02.2013.'));
      doc.text(55, 77, doc.splitTextToSize('2) Representation of Sri/Smt/Kum ' + noticeData.name + '.'));

      doc.setFontType("bold");
      doc.text(15, 87, 'ORDER:');
      doc.setLineWidth(0.5);
      doc.line(15, 88, 32, 88);
      doc.setFontType("normal");
      doc.text(15, 97, doc.splitTextToSize('	In terms of the Govt. Circular Memo issued in the reference read above and'));
      doc.text(15, 105, doc.splitTextToSize('application of Sri/Smt ' + noticeData.name + ', he/she is hereby permitted to surrender Earned'));
      doc.text(15, 113, doc.splitTextToSize('Leave (' + noticeData.totalWorkingDays + ') days as on ' + today + ' for encashment purpose during the financial year'));
      doc.text(15, 121, doc.splitTextToSize(financialYear));
      doc.text(15, 130, doc.splitTextToSize('2.     Certified that necessary entries have been made in the service Register of the individual.'));
      doc.text(15, 139, doc.splitTextToSize('3.     He/She is informed that, after sanction of the above leave is having (' + noticeData.availableDays + ') days of '));
      doc.text(15, 147, doc.splitTextToSize('       ' + noticeData.leaveTypeName + ' at his/her credit.'));

      doc.setFontType("bold");
      doc.text(125, 157, 'COMMISSIONER');
      doc.text(100, 165, tenantId.split(".")[1].charAt(0).toUpperCase() + tenantId.split(".")[1].slice(1) + ' Municipality/Municipal Corporation');

      doc.setFontType("bold");
      doc.text(15, 172, doc.splitTextToSize('To'));
      doc.setFontType("normal");
      doc.text(15, 180, doc.splitTextToSize('Sri/Smt _______'));
      doc.text(15, 188, doc.splitTextToSize('Copy to the _________'));
      doc.text(15, 196, doc.splitTextToSize('Spare'));
    }


    doc.save('Leave Proceeding-' + noticeData.applicationNumber + '.pdf');

    var blob = doc.output('blob');

  }

  getUsersFun(departmentId, designationId) {
    var _this = this;
    var asOnDate = new Date();
    var dd = asOnDate.getDate();
    var mm = asOnDate.getMonth() + 1;
    var yyyy = asOnDate.getFullYear();

    if (dd < 10) {
      dd = '0' + dd
    }

    if (mm < 10) {
      mm = '0' + mm
    }

    asOnDate = dd + '/' + mm + '/' + yyyy;
    commonApiPost("hr-employee", "employees", "_search",
      {
        tenantId,
        departmentId,
        designationId,
        isPrimary: true,
        asOnDate,
        active: true
      }, function (err, res) {
        if (res) {
          _this.setState({
            ..._this.state,
            userList: res.Employee
          })
        }
      })
  }

  getPrimaryAssigmentDep(obj, type) {
    for (var i = 0; i < obj.assignments.length; i++) {
      if (obj.assignments[i].isPrimary) {
        return obj.assignments[i][type];
      }
    }
  }

  handleChangeThreeLevel(e, pName, name) {
    var _this = this, val = e.target.value;
    if (pName == "leaveType") {

      this.setState({
        leaveSet: {
          ...this.state.leaveSet,
          [pName]: {
            ...this.state.leaveSet[pName],
            [name]: e.target.value
          }
        }
      });

      if (_this.state.leaveSet.fromDate && _this.state.leaveSet.toDate)
        _this.calculate();


    } else {

      this.setState({
        leaveSet: {
          ...this.state.leaveSet,
          [pName]: {
            ...this.state.leaveSet[pName],
            [name]: e.target.value
          }
        }
      });
    }
  }

  handleChange(e, name) {

    let _this = this;

    switch (name) {
      case "department":

        _this.state.leaveSet.workflowDetails.assignee = "";
        if (_this.state.leaveSet.workflowDetails.designation) {
          var _designation = this.state.leaveSet.workflowDetails.designation;
          _this.getUsersFun(e.target.value, _designation);
        }
        break;
      case "designation":

        _this.state.leaveSet.workflowDetails.assignee = "";
        if (_this.state.leaveSet.workflowDetails.department) {
          var _department = this.state.leaveSet.workflowDetails.department;
          _this.getUsersFun(_department, e.target.value);
        }
        break;

    }

    if (name === "encashable") {
      if (e.target.checked)
        $('#totalWorkingDays').prop("disabled", false);
      else
        $('#totalWorkingDays').prop("disabled", true);
      let _this = this
      let asOnDate = today();
      let leaveType = this.state.leaveSet.leaveType.id;
      let employeeid = getUrlVars()["id"];

      commonApiPost("hr-leave", "eligibleleaves", "_search", {
        leaveType, tenantId, asOnDate, employeeid
      }, function (err, res) {
        if (res) {
          var _day = res && res["EligibleLeave"] && res["EligibleLeave"][0] ? res["EligibleLeave"][0].noOfDays : "";
          if (_day <= 0 || _day == "") {
            _this.setState({
              leaveSet: {
                ..._this.state.leaveSet,
                availableDays: "",
                encashable: e.target.checked
              }
            });
            return (showError("You do not have leave for this leave type."));
          }
          else {
            _this.setState({
              leaveSet: {
                ..._this.state.leaveSet,
                availableDays: _day,
                encashable: e.target.checked
              }
            });
          }
        }
      });
    } else if (name === "department") {

      this.setState({
        leaveSet: {
          ...this.state.leaveSet,
          workflowDetails: {
            ...this.state.leaveSet.workflowDetails,
            department: e.target.value
          }
        }
      })

    } else if (name === "designation") {
      this.setState({
        leaveSet: {
          ...this.state.leaveSet,
          workflowDetails: {
            ...this.state.leaveSet.workflowDetails,
            designation: e.target.value
          }
        }
      })

    } else if (name === "assignee") {
      this.setState({
        leaveSet: {
          ...this.state.leaveSet,
          workflowDetails: {
            ...this.state.leaveSet.workflowDetails,
            assignee: e.target.value
          }
        }
      })
    } else {

      this.setState({
        leaveSet: {
          ...this.state.leaveSet,
          [name]: e.target.type === 'checkbox' ? e.target.checked : e.target.value
        }
      })
    }

  }

  close() {
    // widow.close();
    open(location, '_self').close();
  }

  handleProcess(e) {
    e.preventDefault();
    var ID = e.target.id, _this = this, employee = {}, owner;
    var stateId = getUrlVars()["stateId"];
    var _this = this;

    if (_this.state.availableDays <= 0) {
      return (showError("You do not have leave for this leave type."));
    }

    if (ID === "Submit") {
      var employee;
      var today = new Date();
      var asOnDate = today();
      var departmentId = _this.state.departmentId;
      var leaveNumber = _this.state.leaveNumber;
      var tempInfo = Object.assign({}, _this.state.leaveSet),
        type = getUrlVars()["type"];
      delete tempInfo.name;
      delete tempInfo.code;

      let holidays = [];
      if (this.state.encloseHoliday) {
        for (let i = 0; i < this.state.encloseHoliday.length; i++)
          holidays.push(this.state.encloseHoliday[i].applicableOn)
      }

      tempInfo.prefixDate = this.state.perfixSuffix ? this.state.perfixSuffix.prefixFromDate : "";
      tempInfo.suffixDate = this.state.perfixSuffix ? this.state.perfixSuffix.suffixToDate : "";
      tempInfo.holidays = holidays;

      commonApiPost("hr-employee", "hod/employees", "_search", { tenantId, asOnDate, departmentId, active: true }, function (err, res2) {
        if (res2 && res2["Employee"] && res2["Employee"][0]) {
          employee = res2["Employee"][0];
          var hodname = employee.name;
          if (!tempInfo.workflowDetails) {
            tempInfo.workflowDetails = {
              action: ID,
              assignee: employee.assignments && employee.assignments[0] ? employee.assignments[0].position : ""
            };
          } else {
            tempInfo.workflowDetails.action = ID,
              tempInfo.workflowDetailsassignee = employee.assignments && employee.assignments[0] ? employee.assignments[0].position : ""
          }
          var body = {
            "RequestInfo": requestInfo,
            "LeaveApplication": tempInfo
          };

          $.ajax({
            url: baseUrl + "/hr-leave/leaveapplications/" + _this.state.leaveSet.id + "/" + "_update?tenantId=" + tenantId,
            type: 'POST',
            dataType: 'json',
            data: JSON.stringify(body),

            contentType: 'application/json',
            headers: {
              'auth-token': authToken
            },
            success: function (res) {
              window.location.href = `app/hr/leavemaster/ack-page.html?type=Submit&applicationNumber=${leaveNumber}&owner=${hodname}`;

            },
            error: function (err) {
              if (err["responseJSON"] && err["responseJSON"].message)
                showError(err["responseJSON"].message);
              else if (err["responseJSON"] && err["responseJSON"].LeaveApplication[0] && err["responseJSON"].LeaveApplication[0].errorMsg) {
                showError(err["responseJSON"].LeaveApplication[0].errorMsg)
              } else {
                showError("Something went wrong. Please contact Administrator");
              }
            }
          });
        }
        else {
          return (showError("HOD does not exists for given date range Please assign the HOD."))
        }

      });
    } else {
      var employee;
      var type;

      var asOnDate = _this.state.leaveSet.toDate;
      var departmentId = _this.state.departmentId;
      var leaveNumber = _this.state.leaveNumber;
      var tempInfo = Object.assign({}, _this.state.leaveSet);
      var _name = tempInfo.name;
      delete tempInfo.name;
      delete tempInfo.code;

      let holidays = [];
      if (this.state.encloseHoliday) {
        for (let i = 0; i < this.state.encloseHoliday.length; i++)
          holidays.push(this.state.encloseHoliday[i].applicableOn)
      }

      tempInfo.prefixDate = this.state.perfixSuffix ? this.state.perfixSuffix.prefixFromDate : "";
      tempInfo.suffixDate = this.state.perfixSuffix ? this.state.perfixSuffix.suffixToDate : "";
      tempInfo.holidays = holidays;

      if (!tempInfo.workflowDetails) {

        tempInfo.workflowDetails = { action: ID };
      } else {
        tempInfo.workflowDetails.action = ID;
      }
      var body = {
        "RequestInfo": requestInfo,
        "LeaveApplication": tempInfo
      };

      $.ajax({
        url: baseUrl + "/hr-leave/leaveapplications/" + _this.state.leaveSet.id + "/" + "_update?tenantId=" + tenantId,
        type: 'POST',
        dataType: 'json',
        data: JSON.stringify(body),

        contentType: 'application/json',
        headers: {
          'auth-token': authToken
        },
        success: function (res) {

          if (ID == "Approve") {
            tempInfo.name = _name;
            tempInfo.leaveTypeName = getNameById(_this.state.leaveList, _this.state.leaveSet.leaveType.id);
            _this.printNotice(tempInfo);
            window.location.href = `app/hr/leavemaster/ack-page.html?type=${ID}&applicationNumber=${leaveNumber}`;

          } else if (ID == "Cancel")
            window.location.href = `app/hr/leavemaster/ack-page.html?type=${ID}&applicationNumber=${leaveNumber}`;
          else {
            commonApiPost("egov-common-workflows", "process", "_search", {
              tenantId: tenantId,
              id: stateId
            }, function (err, res) {
              if (res) {
                var process = res["processInstance"];
                if (process) {
                  var positionId = process.owner.id;
                  console.log(positionId);
                  commonApiPost("hr-employee", "employees", "_search", {
                    tenantId,
                    positionId: positionId
                  }, function (err, res) {
                    if (res && res["Employee"] && res["Employee"][0]) {
                      employee = res["Employee"][0];
                      owner = employee.name;
                      window.location.href = `app/hr/leavemaster/ack-page.html?type=Submit&applicationNumber=${leaveNumber}&owner=${owner}`;
                    }
                    else {
                      return (showError("Unable to fetch Employee details after forwarding."))
                    }

                  });

                }
              }
            })

          }
        },
        error: function (err) {
          if (err["responseJSON"] && err["responseJSON"].message)
            showError(err["responseJSON"].message);
          else if (err["responseJSON"] && err["responseJSON"].LeaveApplication[0] && err["responseJSON"].LeaveApplication[0].errorMsg) {
            showError(err["responseJSON"].LeaveApplication[0].errorMsg)
          } else {
            showError("Something went wrong. Please contact Administrator");
          }
        }
      });
    }
  }


  render() {
    let { handleChange, handleChangeThreeLevel, handleProcess } = this;
    let { leaveSet, buttons } = this.state;
    let { name, code, leaveDays, availableDays, fromDate, toDate, leaveGround, reason, leaveType, encashable, totalWorkingDays, workflowDetails } = leaveSet;
    let mode = getUrlVars()["type"];
    let _this = this;
    const renderProcesedBtns = function () {
      if (buttons.length) {
        return buttons.map(function (btn, ind) {
          return (
            <span key={ind}><button key={ind} id={btn.key} type='button' className='btn btn-submit' onClick={(e) => { handleProcess(e) }}>
              {btn.name}
            </button>  &nbsp; </span>
          )
        })
      }
    }

    const renderOption = function (list) {
      if (list) {
        return list.map((item) => {
          return (<option key={item.id} value={item.id}>
            {item.name}
          </option>)
        })
      }
    }

    const renderEnclosingHolidayTr = () => {
      if (this.state.encloseHoliday) {
        if (this.state.encloseHoliday.length != 0) {
          return encloseHoliday.map((item, ind) => {
            return (
              <tr key={ind}>
                <td>{item.name}</td>
                <td>{item.applicableOn}</td>
              </tr>
            )
          })
        } else {
          return (
            <tr>
              <td colSpan="2" className="text-center">No Enclosing Holidays</td>
            </tr>
          )
        }
      }
    }

    const showEnclosingHolidayTable = () => {
      if (this.state.encloseHoliday) {
        return (
          <div>
            <div className="land-table">
              <table id="employeeTable" className="table table-bordered">
                <thead>
                  <tr>
                    <th>Holiday Name</th>
                    <th>Holiday Date </th>
                  </tr>
                </thead>
                <tbody>
                  {renderEnclosingHolidayTr()}
                </tbody>
              </table>
            </div>
          </div>
        )
      }
    }

    const showPrefix = () => {
      if (this.state.perfixSuffix && this.state.perfixSuffix.prefixFromDate && this.state.perfixSuffix.prefixToDate) {
        return (

          <div className="row">
            <div className="col-sm-6">
              <div className="row">
                <div className="col-sm-6 label-text">
                  <label htmlFor="">Prefix From Date</label>
                </div>
                <div className="col-sm-6">
                  <input type="text" id="perfixFromDate" name="perfixFromDate" value={perfixSuffix.prefixFromDate}
                    disabled />
                </div>
              </div>
            </div>
            <div className="col-sm-6">
              <div className="row">
                <div className="col-sm-6 label-text">
                  <label htmlFor="">Prefix To Date</label>
                </div>
                <div className="col-sm-6">
                  <input type="text" id="prefixToDate" name="prefixToDate" value={perfixSuffix.prefixToDate}
                    disabled />
                </div>
              </div>
            </div>
          </div>

        )
      }
    }

    const showSuffix = () => {
      if (this.state.perfixSuffix && this.state.perfixSuffix.suffixFromDate && this.state.perfixSuffix.suffixToDate) {
        return (

          <div className="row">
            <div className="col-sm-6">
              <div className="row">
                <div className="col-sm-6 label-text">
                  <label htmlFor="">Suffix From Date</label>
                </div>
                <div className="col-sm-6">
                  <input type="text" id="suffixFromDate" name="suffixFromDate" value={perfixSuffix.suffixFromDate}
                    disabled />
                </div>
              </div>
            </div>
            <div className="col-sm-6">
              <div className="row">
                <div className="col-sm-6 label-text">
                  <label htmlFor="">Suffix To Date</label>
                </div>
                <div className="col-sm-6">
                  <input type="text" id="suffixToDate" name="suffixToDate" value={perfixSuffix.suffixToDate}
                    disabled />
                </div>
              </div>
            </div>
          </div>

        )
      }
    }

    const showEncashable = () => {

      if (this.state.leaveSet.leaveType.id) {
        let encashableLeaveType = getNameById(this.state.leaveList, this.state.leaveSet.leaveType.id, "encashable");
        if (encashableLeaveType || encashableLeaveType == "TRUE" || encashableLeaveType == "true") {

          return (

            <div className="row">
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label htmlFor="">En-cashable</label>
                  </div>
                  <div className="col-sm-6">
                    <input type="checkbox" id="encashable" name="encashable" checked={encashable}
                      onChange={(e) => { handleChange(e, "encashable") }} />
                  </div>
                </div>
              </div>
            </div>

          )
        }
      }
    }

    const showDateRange = () => {

      if (!this.state.leaveSet.encashable) {

        return (

          <div>

            <div className="row">
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label htmlFor="">From Date <span>*</span></label>
                  </div>
                  <div className="col-sm-6">
                    <div className="text-no-ui">
                      <span><i className="glyphicon glyphicon-calendar"></i></span>
                      <input type="text" id="fromDate" name="fromDate" value="fromDate" value={fromDate}
                        onChange={(e) => { handleChange(e, "fromDate") }} required />
                    </div>
                  </div>
                </div>
              </div>
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label htmlFor="">To Date <span>*</span> </label>
                  </div>
                  <div className="col-sm-6">
                    <div className="text-no-ui">
                      <span><i className="glyphicon glyphicon-calendar"></i></span>
                      <input type="text" id="toDate" name="toDate" value={toDate}
                        onChange={(e) => {
                          handleChange(e, "toDate")
                        }} required />
                    </div>
                  </div>
                </div>
              </div>
            </div>


            <div className="row">
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label htmlFor="">Working Days</label>
                  </div>
                  <div className="col-sm-6">

                    <input type="number" id="leaveDays" name="leaveDays" value={leaveDays}
                      onChange={(e) => { handleChange(e, "leaveDays") }} disabled />
                  </div>
                </div>
              </div>

            </div>

          </div>

        )
      }

    }

    const renderOptionForUser = function (list) {
      if (list) {
        return list.map((item, ind) => {
          var positionId;
          item.assignments.forEach(function (item) {
            if (item.isPrimary) {
              positionId = item.position;
            }
          });

          return (<option key={ind} value={positionId}>
            {item.name}
          </option>)
        })
      }
    }

    const renderWorkflowDetails = function (status) {
      status = getNameById(_this.state.statusList, status, "code");

      if (status === "APPLIED") {
        return (
          <div>
            <br />
            <div className="form-section">
              <div className="row">
                <div className="col-md-8 col-sm-8">
                  <h3 className="categoryType">Workflow Details </h3>
                </div>
              </div>
              <div className="row">
                <div className="col-sm-6">
                  <div className="row">
                    <div className="col-sm-6 label-text">
                      <label htmlFor="">Department <span>*</span></label>
                    </div>
                    <div className="col-sm-6">
                      <div className="styled-select">
                        <select id="department" name="department" value={workflowDetails.department}
                          onChange={(e) => { handleChange(e, "department") }} required >
                          <option value="">Select Department</option>
                          {renderOption(_this.state.departmentList)}
                        </select>
                      </div>
                    </div>
                  </div>
                </div>
                <div className="col-sm-6">
                  <div className="row">
                    <div className="col-sm-6 label-text">
                      <label htmlFor="">Designation <span>*</span></label>
                    </div>
                    <div className="col-sm-6">
                      <div className="styled-select">
                        <select id="designation" name="designation" value={workflowDetails.designation}
                          onChange={(e) => { handleChange(e, "designation") }} required >
                          <option value="">Select Designation</option>
                          {renderOption(_this.state.designationList)}//TODO: get designation based on departments
                        </select>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div className="row">
                <div className="col-sm-6">
                  <div className="row">
                    <div className="col-sm-6 label-text">
                      <label htmlFor="">User Name <span>*</span></label>
                    </div>
                    <div className="col-sm-6">
                      <div className="styled-select">
                        <select id="assignee" name="assignee" value={workflowDetails.assignee}
                          onChange={(e) => { handleChange(e, "assignee") }} required>
                          <option value="">Select User</option>
                          {renderOptionForUser(_this.state.userList)}
                        </select>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        );

      }


    }


    return (
      <div>
        <form>
          <fieldset>

            <div className="row">
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label htmlFor="">Employee Name</label>
                  </div>
                  <div className="col-sm-6">
                    <input type="text" id="name" name="name" value={name}
                      onChange={(e) => { handleChange(e, "name") }} />
                  </div>
                </div>
              </div>
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label htmlFor="">Employee Code</label>
                  </div>
                  <div className="col-sm-6">
                    <input type="text" id="code" name="code" value={code}
                      onChange={(e) => { handleChange(e, "code") }} />
                  </div>
                </div>
              </div>
            </div>

            <div className="row">
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label htmlFor="leaveType">Leave Type<span>*</span></label>
                  </div>
                  <div className="col-sm-6">
                    <div className="styled-select">
                      <select id="leaveType" name="leaveType" value={leaveType.id} required="true" onChange={(e) => {
                        handleChangeThreeLevel(e, "leaveType", "id")
                      }}>
                        <option value=""> Select Leave Type</option>
                        {renderOption(this.state.leaveList)}
                      </select>
                    </div>
                  </div>
                </div>
              </div>
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label htmlFor="Reason">Reason <span>*</span></label>
                  </div>
                  <div className="col-sm-6">
                    <textarea rows="4" cols="50" id="reason" name="reason" value={reason}
                      onChange={(e) => { handleChange(e, "reason") }} required></textarea>
                  </div>
                </div>
              </div>
            </div>

            {showEncashable()}
            {showDateRange()}

            <div className="row">
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label htmlFor="">Available Leave</label>
                  </div>
                  <div className="col-sm-6">
                    <input type="number" id="availableDays" name="availableDays" value={availableDays}
                      onChange={(e) => { handleChange(e, "availableDays") }} />
                  </div>
                </div>
              </div>
            </div>

            {showPrefix()}
            {showSuffix()}

            <div className="row">
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label htmlFor="">Total Leave Days</label>
                  </div>
                  <div className="col-sm-6">

                    <input type="number" id="totalWorkingDays" name="totalWorkingDays" value={totalWorkingDays}
                      onChange={(e) => { handleChange(e, "totalWorkingDays") }} disabled />
                  </div>
                </div>
              </div>
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label htmlFor="">Leave Grounds <span>*</span></label>
                  </div>
                  <div className="col-sm-6">

                    <input type="text" id="leaveGround" name="leaveGround" value={leaveGround}
                      onChange={(e) => { handleChange(e, "leaveGround") }} required />
                  </div>
                </div>
              </div>

            </div>


            {showEnclosingHolidayTable()}

            {renderWorkflowDetails(this.state.leaveSet.status)}
            <br/>
            <div className="text-center">
              {renderProcesedBtns()}
              <button type="button" className="btn btn-close" onClick={(e) => { this.close() }}>Close</button>

            </div>
          </fieldset>
        </form>
      </div>);

  }
}


ReactDOM.render(
  <UpdateLeave />,
  document.getElementById('root')
);