class UploadLeaveType extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      leaveTypeSet: {
        id: "",
        my_file_input: "",
        tenantId: tenantId
      },
      temp: [],
      dataType: [],
      employees: [],
      _leaveTypes: [],
      _years: [],
      duplicate: []
    };
    this.addOrUpdate = this.addOrUpdate.bind(this);
    this.filePicked = this.filePicked.bind(this);
    this.setInitialState = this.setInitialState.bind(this);
  }

  setInitialState(initState) {
    this.setState(initState);
  }

  componentDidMount() {
    if (window.opener && window.opener.document) {
      var logo_ele = window.opener.document.getElementsByClassName(
        "homepage_logo"
      );
      if (logo_ele && logo_ele[0]) {
        document.getElementsByClassName("homepage_logo")[0].src =
          window.location.origin + logo_ele[0].getAttribute("src");
      }
    }
    var count = 3,
      _this = this,
      _state = {};
    var checkCountNCall = function(key, res) {
      count--;
      _state[key] = res;
      if (count == 0) _this.setInitialState(_state);
    };

    getDropdown("years", function(res) {
      checkCountNCall("_years", res);
    });
    getDropdown("leaveTypes", function(res) {
      checkCountNCall("_leaveTypes", res);
    });
    getCommonMaster("hr-employee", "employees", function(err, res) {
      checkCountNCall("employees", res ? res.Employee : []);
    });
  }

  filePicked(oEvent) {
    // Get The File From The Input
    var oFile = oEvent.target.files[0];
    var sFilename = oFile.name;
    var _this = this,
      oJS;
    var key = [];
    // Create A File Reader HTML5
    var reader = new FileReader();

    // Ready The Event For When A File Gets Selected
    reader.onload = function(e) {
      var data = e.target.result;

      var cfb = XLSX.read(data, {
        type: "binary"
      });
      cfb.SheetNames.forEach(function(sheetName) {
        // Obtain The Current Row As CSV
        var sCSV = XLS.utils.make_csv(cfb.Sheets[sheetName]);

        oJS = XLS.utils.sheet_to_json(cfb.Sheets[sheetName]);

        key = Object.keys(oJS[0]);
      });

      var finalObject = [],
        duplicateObject = [],
        scannedObject = [];
      oJS.forEach(function(d) {
        var employee = d[key[0]];
        employee = employee.trim();
        var leaveType = d[key[3]];
        leaveType = leaveType.trim().toUpperCase();
        var calendarYear = d[key[4]];
        calendarYear = calendarYear.trim();
        var noOfDays = d[key[5]];
        noOfDays = noOfDays.trim();

        scannedObject.push({
          employee: employee,
          calendarYear: calendarYear,
          leaveType: { id: leaveType },
          noOfDays: noOfDays,
          duplicate: "false",
          employeeCode: employee,
          employeeName: d[key[1]],
          department: d[key[2]],
          leaveTypeName: leaveType
        });
      });

      for (var i = 0; i < scannedObject.length; i++) {
        for (var j = i + 1; j <= scannedObject.length - 1; j++)
          if (scannedObject[i].employee === scannedObject[j].employee) {
            if (scannedObject[i].leaveType.id === scannedObject[j].leaveType.id)
              scannedObject[i].duplicate = "true";
            scannedObject[j].duplicate = "true";
          }
      }
      scannedObject.forEach(function(d) {
        if (d.duplicate === "true") {
          d.errorMsg = "Duplicate row in the excel scanned";
          duplicateObject.push(d);
        } else {
          finalObject.push(d);
        }
      });
      _this.setState({
        leaveTypeSet: {
          ..._this.state.leaveTypeSet
        },
        temp: finalObject,
        duplicate: duplicateObject
      });
    };

    // Tell JS To Start Reading The File.. You could delay this if desired
    reader.readAsBinaryString(oFile);
  }

  close() {
    // widow.close();
    open(location, "_self").close();
  }

  addOrUpdate(e, mode) {
    e.preventDefault();
    var _this = this;
    var serverObject = [],
      errorObject = [],
      finalValidatedServerObject = [],
      finalSuccessObject = [];
    var tempInfo = Object.assign([], this.state.temp);
    var duplicateInfo = Object.assign([], this.state.duplicate);
    var errorLeaveOpening = [];
    var exists = false;

    duplicateInfo.forEach(function(d) {
      errorObject.push(d);
    });
    var leaveArray = [],
      calendarYearArray = [],
      employeeArray = [];
    var checkLeave = [],
      checkCalenderYear = [],
      checkEmployee = [];
    _this.state._leaveTypes.forEach(function(d) {
      checkLeave.push(d.name.trim().toUpperCase());
    });

    _this.state._leaveTypes.forEach(function(d) {
      leaveArray.push({
        name: d.name.trim().toUpperCase(),
        id: d.id
      });
    });

    _this.state._years.forEach(function(d) {
      checkCalenderYear.push(d.name.toString());
    });

    _this.state._years.forEach(function(d) {
      calendarYearArray.push({
        name: d.name.toString(),
        id: d.id
      });
    });

    _this.state.employees.forEach(function(d) {
      checkEmployee.push(d.code);
    });

    _this.state.employees.forEach(function(d) {
      employeeArray.push({
        code: d.code,
        id: d.id
      });
    });

    console.log("this.state._leaveTypes", _this.state._leaveTypes);
    console.log("this.state.employees", _this.state.employees);
    console.log("this.state._years", _this.state._years);

    var post = 0,
      error = 0,
      errorList = [],
      successList = [];
    var i = 0;
    var leaveName, calendarYearName, employeeName, calenderName, noOfDays;
    var searchName;
    var leaveId = 0,
      employeeId = 0,
      calenderId = 0;
    for (var k = 0; k < tempInfo.length; k++) {
      var d = tempInfo[k];
      console.log(d);
      noOfDays = parseInt(d.noOfDays);
      leaveName = d.leaveType.id;
      employeeName = d.employee;
      calenderName = d.calendarYear;
      var leaveValidate = checkLeave.indexOf(d.leaveType.id);
      var employeeValidate = checkEmployee.indexOf(d.employee);
      var calenderValidate = checkCalenderYear.indexOf(d.calendarYear);

      console.log("leaveValidate", leaveValidate);
      console.log("employeeValidate", employeeValidate);
      console.log("calenderValidate", calenderValidate);

      d.errorMsg = "";
      if (noOfDays < 0) {
        d.errorMsg = "Number of days is negative " + noOfDays;
        error = 1;
        post = 1;
      }

      if (leaveValidate < 0) {
        if (d.errorMsg === "") {
          d.errorMsg = " Leave type " + leaveName + " is not exist in system";
        } else {
          d.errorMsg =
            d.errorMsg + " Leave type " + leaveName + " is not exist in system";
        }
        post = 1;
        error = 1;
      } else {
        for (var j = 0; i < leaveArray.length; j++) {
          if (leaveName === leaveArray[j].name) {
            leaveId = leaveArray[j].id;
            break;
          } else {
            leaveId = 0;
          }
        }
      }

      if (employeeValidate < 0) {
        if (d.errorMsg === "") {
          d.errorMsg =
            " Employee Code " + employeeName + " is not exist in system";
        } else {
          d.errorMsg =
            d.errorMsg +
            " Employee Code " +
            employeeName +
            " is not exist in system";
        }
        error = 1;
        post = 1;
      } else {
        for (var j = 0; i < employeeArray.length; j++) {
          if (employeeName === employeeArray[j].code) {
            employeeId = employeeArray[j].id;

            break;
          } else {
            employeeId = 0;
          }
        }
      }

      if (calenderValidate < 0) {
        if (d.errorMsg === "") {
          d.errorMsg =
            " Calender Year " + calenderName + " is not exist in system";
        } else {
          d.errorMsg =
            d.errorMsg +
            " Calender Year " +
            calenderName +
            " is not exist in system";
        }
        error = 1;
        post = 1;
      } else {
        for (var j = 0; i < calendarYearArray.length; j++) {
          if (calenderName === calendarYearArray[j].name) {
            calenderId = calendarYearArray[j].name;

            break;
          } else {
            calenderId = 0;
          }
        }
      }

      if (error === 0) {
        serverObject.push({
          employee: employeeId,
          calendarYear: calenderId,
          leaveType: { id: leaveId },
          noOfDays: noOfDays,
          tenantId: tenantId,
          employeeCode: d.employeeCode,
          employeeName: d.employeeName,
          department: d.department,
          leaveTypeName: d.leaveTypeName
        });
      } else {
        errorObject.push(d);
        error = 0;
      }
    }
    console.log("serverObject", serverObject);
    if (serverObject.length > 0) {
      var calenderYearApi = serverObject[0].calendarYear;
      commonApiPost(
        "hr-leave",
        "leaveopeningbalances",
        "_search",
        {
          tenantId,
          pageSize: 500,
          year: calenderYearApi
        },
        function(err, res) {
          if (res) {
            var leaveBal = res["LeaveOpeningBalance"];
            console.log("leaveBal", leaveBal);
            for (var i = 0; i < serverObject.length; i++) {
              var calendarNumber = parseInt(serverObject[i].calendarYear);
              exists = false;
              for (var j = 0; j < leaveBal.length; j++) {
                if (calendarNumber === leaveBal[j].calendarYear) {
                  if (serverObject[i].employee === leaveBal[j].employee) {
                    if (
                      serverObject[i].leaveType["id"] ===
                      leaveBal[j].leaveType["id"]
                    ) {
                      exists = true;
                      serverObject[i].errorMsg =
                        "Leave opening balance already present in the system for this Employee";
                      errorLeaveOpening.push(serverObject[i]);
                      break;
                    }
                  }
                }
              }

              if (exists === false) {
                serverObject[i].successMessage =
                  "Employee leaves created successfully";
                finalValidatedServerObject.push(serverObject[i]);
              }
            }
            errorLeaveOpening.forEach(function(d) {
              errorObject.push(d);
            });

            finalValidatedServerObject.forEach(function(d) {
              finalSuccessObject.push({
                employee: d.employee,
                employeeName: d.employeeName,
                employeeCode: d.employeeCode,
                calendarYear: d.calendarYear,
                leaveType: { id: d.leaveType["id"] },
                noOfDays: d.noOfDays,
                departmentName: d.department,
                tenantId: d.tenantId
              });
            });
            console.log(
              "finalValidatedServerObject",
              finalValidatedServerObject
            );
            console.log("finalSuccessObject", finalSuccessObject);
            if (finalSuccessObject.length !== 0) {
              var body = {
                RequestInfo: requestInfo,
                LeaveOpeningBalance: finalSuccessObject
              };

              $.ajax({
                url:
                  baseUrl +
                  "/hr-leave/leaveopeningbalances/_create?tenantId=" +
                  tenantId +
                  "&type=upload",
                type: "POST",
                dataType: "json",
                data: JSON.stringify(body),
                contentType: "application/json",
                headers: {
                  "auth-token": authToken
                },
                success: function(res) {
                  showSuccess("File Uploaded successfully.");

                  console.log("res.ErrorList", res.ErrorList);
                  console.log("errorObject", errorObject);
                  errorList = res.ErrorList;
                  if (res.SuccessList.length !== 0) {
                    res.SuccessList.forEach(function(d) {
                      d.successMessage =
                        "Leave Opening balance created successfully";
                      successList.push(d);
                    });

                    var ep1 = new ExcelPlus();
                    var b = 0;

                    ep1.createFile("Success");
                    ep1.write({
                      content: [
                        [
                          "Employee Code",
                          "Employee Name",
                          "Department",
                          "Leave type",
                          "Calendar Year",
                          "Number of days as on 1st Jan 2017",
                          "Success Message"
                        ]
                      ]
                    });
                    for (b = 0; b < successList.length; b++) {
                      ep1.writeNextRow([
                        successList[b].employeeCode,
                        successList[b].employeeName,
                        successList[b].departmentName,
                        successList[b].leaveType["name"],
                        successList[b].calendarYear,
                        successList[b].noOfDays,
                        successList[b].successMessage
                      ]);
                    }
                    ep1.saveAs("success.xlsx");
                  }

                  if (errorList.length !== 0) {
                    for (var t = 0; t < errorList.length; t++) {
                      for (
                        var q = 0;
                        q < finalValidatedServerObject.length;
                        q++
                      ) {
                        if (
                          errorList[t].employee ===
                          finalValidatedServerObject[q].employee
                        ) {
                          finalValidatedServerObject[q].errorMsg =
                            errorList[t].errorMsg;
                          errorObject.push(finalValidatedServerObject[q]);
                          console.log("g--->", finalValidatedServerObject[q]);
                          break;
                        }
                      }
                    }
                    console.log("errorObject", errorObject);
                    var ep2 = new ExcelPlus();
                    var b = 0;

                    ep2.createFile("Error");
                    ep2.write({
                      content: [
                        [
                          "Employee Code",
                          "Employee Name",
                          "Department",
                          "Leave type",
                          "Calendar Year",
                          "Number of days as on 1st Jan 2017",
                          "Error Message"
                        ]
                      ]
                    });
                    for (b = 0; b < errorObject.length; b++) {
                      ep2.writeNextRow([
                        errorObject[b].employeeCode,
                        errorObject[b].employeeName,
                        errorObject[b].department,
                        errorObject[b].leaveTypeName,
                        errorObject[b].calendarYear,
                        errorObject[b].noOfDays,
                        errorObject[b].errorMsg
                      ]);
                    }
                    ep2.saveAs("error.xlsx");
                  }

                  _this.setState({
                    ..._this.state.leaveTypeSet,
                    leaveTypeSet: {
                      id: "",
                      my_file_input: "",
                      tenantId: tenantId
                    }
                  });
                },
                error: function(err) {
                  showError(
                    "File could not be uploaded !! Please try again later"
                  );
                }
              });
            } else {
              var ep2 = new ExcelPlus();
              var b = 0;

              ep2.createFile("Error");
              ep2.write({
                content: [
                  [
                    "Employee Code",
                    "Employee Name",
                    "Department",
                    "Leave type",
                    "Calendar Year",
                    "Number of days as on 1st Jan 2017",
                    "Error Message"
                  ]
                ]
              });
              for (b = 0; b < errorObject.length; b++) {
                ep2.writeNextRow([
                  errorObject[b].employeeCode,
                  errorObject[b].employeeName,
                  errorObject[b].department,
                  errorObject[b].leaveTypeName,
                  errorObject[b].calendarYear,
                  errorObject[b].noOfDays,
                  errorObject[b].errorMsg
                ]);
              }
              ep2.saveAs("error.xlsx");
              showError("No valid Data in the uplaod Sheet");
              showError("No vaild data in the Uploaded Excel");
            }
          }
        }
      );
    } else {
      var ep2 = new ExcelPlus();
      var b = 0;

      ep2.createFile("Error");
      ep2.write({
        content: [
          [
            "Employee Code",
            "Employee Name",
            "Department",
            "Leave type",
            "Calendar Year",
            "Number of days as on 1st Jan 2017",
            "Error Message"
          ]
        ]
      });
      for (b = 0; b < errorObject.length; b++) {
        ep2.writeNextRow([
          errorObject[b].employeeCode,
          errorObject[b].employeeName,
          errorObject[b].department,
          errorObject[b].leaveTypeName,
          errorObject[b].calendarYear,
          errorObject[b].noOfDays,
          errorObject[b].errorMsg
        ]);
      }
      ep2.saveAs("error.xlsx");
      showError("No valid Data in the uplaod Sheet");
    }
  }

  render() {
    let { addOrUpdate, filePicked } = this;
    let { my_file_input } = this.state.leaveTypeSet;
    let mode = getUrlVars()["type"];

    return (
      <div>
        <form
          onSubmit={e => {
            addOrUpdate(e, mode);
          }}
        >
          <fieldset>
            <div className="row">
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label for="">
                      Upload File <span> * </span>
                    </label>
                  </div>
                  <div className="col-sm-6">
                    <input
                      type="file"
                      id="my_file_input"
                      name="my_file_input"
                      onChange={e => {
                        filePicked(e);
                      }}
                      required
                    />
                  </div>
                </div>
              </div>
            </div>
            <div className="text-center">
              <button type="submit" className="btn btn-submit">
                Upload
              </button>{" "}
              &nbsp;&nbsp;
              <button
                type="button"
                className="btn btn-close"
                onClick={e => {
                  this.close();
                }}
              >
                Close
              </button>
            </div>
          </fieldset>
        </form>
        <a
          href="resources/sample/LeaveOpeningBalance_Upload_template.xls"
          target="_blank"
        >
          Download Sample Template
        </a>
      </div>
    );
  }
}

ReactDOM.render(<UploadLeaveType />, document.getElementById("root"));
