class UpdateMovement extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      movement: {
        employeeId: "",
        typeOfMovement: "TRANSFER",
        currentAssignment: "",
        transferType: "",
        promotionBasis: {
          id: ""
        },
        remarks: "",
        reason: {
          id: ""
        },
        effectiveFrom: "",
        transferedLocation: "",
        departmentAssigned: "",
        designationAssigned: "",
        positionAssigned: "",
        fundAssigned: "",
        functionAssigned: "",
        employeeAcceptance: "",
        workflowDetails: {
          assignee: "",
          department: "",
          designation: ""
        },
        tenantId: tenantId,
        checkEmployeeExists:false
      },
      employee: {
        id: "",
        name: "",
        code: "",
        departmentId: "",
        designationId: "",
        positionId: ""
      },
      positionId: "",
      positionList: [],
      departmentList: [],
      designationList: [],
      employees: [],
      promotionList: [],
      wfDesignationList: [],
      fundList: [],
      functionaryList: [],
      transferList: [],
      reasonList: [],
      pNameList: [],
      userList: [],
      buttons: [],
      owner: "",
      status: ""
    }

    this.handleChange = this.handleChange.bind(this);
    this.handleProcess = this.handleProcess.bind(this);
    this.setInitialState = this.setInitialState.bind(this);
    this.vacantPositionFun = this.vacantPositionFun.bind(this);
    this.getUsersFun = this.getUsersFun.bind(this);

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


    var type = getUrlVars()["type"],
      _this = this;
    var stateId = getUrlVars()["stateId"];
    var process;
    var transferWithPromotion;

    var _state = {},
      count = 9;
    const checkCountAndCall = function (key, res) {
      _state[key] = res;
      count--;
      if (count == 0)
        _this.setInitialState(_state);
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
    getDropdown("assignments_position", function (res) {
      checkCountAndCall("pNameList", res);
    });
    getDropdown("assignments_fund", function (res) {
      checkCountAndCall("fundList", res);
    });
    getDropdown("assignments_functionary", function (res) {
      checkCountAndCall("functionaryList", res);
    });
    getDropdown("transferReason", function (res) {
      checkCountAndCall("reasonList", res);
    });
    getDropdown("promotionBasis", function (res) {
      checkCountAndCall("promotionList", res);
    });
    getDropdown("transferType", function (res) {
      checkCountAndCall("transferList", res);
    });


    $('#effectiveFrom').datepicker({
      format: 'dd/mm/yyyy',
      startDate: new Date(),
      autoclose: true,
      defaultDate: ""
    });
    $('#effectiveFrom').val("");
    $('#effectiveFrom ').on('changeDate', function (e) {


      _this.setState({
        movement: {
          ..._this.state.movement,
          "effectiveFrom": $("#effectiveFrom").val()
        }
      });
      if (_this.state.movement.designationAssigned && _this.state.movement.departmentAssigned) {
        var _designation = _this.state.movement.designationAssigned;
        var _department = _this.state.movement.departmentAssigned;
        var _effectiveFrom = _this.state.movement.effectiveFrom;
        _this.vacantPositionFun(_department, _designation, _effectiveFrom);
      }

    });


    commonApiPost("hr-employee-movement", "movements", "_search", {
      tenantId: tenantId,
      stateId: stateId
    }, function (err, res) {
      if (res) {
        if (res.Movement[0]) {
          var Movement = res.Movement[0];
          Movement.checkEmployeeExists = false;

          let docs = Object.assign([], Movement.documents);


          if (!Movement.workflowDetails) {
            Movement.workflowDetails = {
              assignee: "",
              department: "",
              designation: ""
            };
          }
          getCommonMasterById("hr-employee", "employees", res.Movement[0].employeeId, function (err, res) {
            if (res && res.Employee) {
              var obj = res.Employee[0];
              var ind = 0;
              if (obj.length > 0) {
                obj.map((item, index) => {
                  for (var i = 0; i < item.assignments.length; i++) {
                    if ([true, "true"].indexOf(item.assignments[i].isPrimary) > -1) {
                      ind = i;
                      break;
                    }
                  }
                });
              }
              _this.setState({
                ..._this.state,
                transferWithPromotion: transferWithPromotion,
                docs,
                movement: Movement,
                employee: {
                  name: obj.name,
                  code: obj.code,
                  departmentId: obj.assignments[ind].department,
                  designationId: obj.assignments[ind].designation,
                  positionId: obj.assignments[ind].position
                }
              })
            }
          });
        }
      }
    });


    commonApiPost("egov-common-workflows", "process", "_search", {
      tenantId: tenantId,
      id: stateId
    }, function (err, res) {
      if (res) {
        process = res["processInstance"];
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
          //Disabling all fields if it is not rejected
          if (process.status != "Rejected") 
            $("input,select,textarea").prop("disabled", true);
      
          $('#effectiveFrom').prop("disabled", false);

          _this.setState({
            ..._this.state,
            buttons: _btns.length ? _btns : [],
            owner: process.owner ? process.owner.id : "",
            initiator:process.initiatorPosition,
            status: process.status
          })
        }
      }
    });

  }


  getUsersFun(departmentId, designationId) {
    var _this = this;
    var asOnDate = new Date();
    var dd = asOnDate.getDate();
    var mm = asOnDate.getMonth() + 1; //January is 0!
    var yyyy = asOnDate.getFullYear();

    if (dd < 10) {
      dd = '0' + dd
    }

    if (mm < 10) {
      mm = '0' + mm
    }

    asOnDate = dd + '/' + mm + '/' + yyyy;
    commonApiPost("hr-employee", "employees", "_search", {
      tenantId,
      departmentId,
      designationId,
      isPrimary:true,
      active: true,
      asOnDate
    }, function (err, res) {
      if (res) {
        _this.setState({
          ..._this.state,
          userList: res.Employee
        })
      }
    })
  }

  vacantPositionFun(departmentId, designationId, effectiveFrom) {
    var _this = this;
    commonApiPost("hr-masters", "vacantpositions", "_search", {
      tenantId,
      departmentId: departmentId,
      designationId: designationId,
      asOnDate: effectiveFrom,
      pageSize: 500
    }, function (err, res) {
      if (res) {
        _this.setState({
          movement: {
            ..._this.state.movement,
          },
          pNameList: res.Position
        })
      }
    });

  }

  makeAjaxUpload(file, cb) {
    if (file.constructor == File) {
      let formData = new FormData();
      formData.append("jurisdictionId", "ap.public");
      formData.append("module", "PGR");
      formData.append("file", file);
      $.ajax({
        url: baseUrl + "/filestore/v1/files?tenantId=" + tenantId,
        data: formData,
        cache: false,
        contentType: false,
        processData: false,
        type: 'POST',
        success: function (res) {
          cb(null, res);
        },
        error: function (jqXHR, exception) {
          cb(jqXHR.responseText || jqXHR.statusText);
        }
      });
    } else {
      cb(null, {
        files: [{
          fileStoreId: file
        }]
      });
    }
  }

  handleChange(e, name) {
    var _this = this;
    switch (name) {
      case "designationAssigned":
        if (this.state.movement.departmentAssigned && this.state.movement.effectiveFrom) {
          var _department = this.state.movement.departmentAssigned;
          var _date = this.state.movement.effectiveFrom;
          _this.vacantPositionFun(_department, e.target.value, _date);
        }
        break;
      case "departmentAssigned":
        if (this.state.movement.designationAssigned && this.state.movement.effectiveFrom) {
          var _designation = this.state.movement.designationAssigned;
          var _date = this.state.movement.effectiveFrom;
          _this.vacantPositionFun(e.target.value, _designation, _date);
        }
        break;
      case "department":
        _this.state.movement.workflowDetails.assignee = "";
        if (this.state.movement.workflowDetails.designation) {
          var _designation = this.state.movement.workflowDetails.designation;
          _this.getUsersFun(e.target.value, _designation);
        }
        break;
      case "designation":
        _this.state.movement.workflowDetails.assignee = "";
        if (this.state.movement.workflowDetails.department) {
          var _department = this.state.movement.workflowDetails.department;
          _this.getUsersFun(_department, e.target.value);
        }
        break;

    }

    if (name === "promotionBasis") {
      this.setState({
        movement: {
          ...this.state.movement,
          promotionBasis: {
            id: e.target.value
          }
        }
      })
    } else if (name === "department") {

      this.setState({
        movement: {
          ...this.state.movement,
          workflowDetails: {
            ...this.state.movement.workflowDetails,
            department: e.target.value
          }
        }
      })

      // getCommonMasterById("hr-employee","employees", e.target.value, function(err, res) {
      //           if(res && res.Employee && res.Employee[0] && res.Employee[0].userName) {
      //             _this.setState({
      //               revaluationSet: {
      //                 ..._this.state.revaluationSet,
      //                 reevaluatedBy: res.Employee[0].userName
      //               }
      //             })
      //           }
      //         })

    } else if (name === "designation") {
      this.setState({
        movement: {
          ...this.state.movement,
          workflowDetails: {
            ...this.state.movement.workflowDetails,
            designation: e.target.value
          }
        }
      })

    } else if (name === "assignee") {
      this.setState({
        movement: {
          ...this.state.movement,
          workflowDetails: {
            ...this.state.movement.workflowDetails,
            assignee: e.target.value
          }
        }
      })

    } else if (name === "documents") {

      var fileTypes = ["application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "application/pdf", "image/png", "image/jpeg"];

      if (e.currentTarget.files.length != 0) {
        for (var i = 0; i < e.currentTarget.files.length; i++) {
          //2097152 = 2mb
          if (e.currentTarget.files[i].size > 2097152 && fileTypes.indexOf(e.currentTarget.files[i].type) == -1) {
            $("#documents").val('');
            return showError("Maximum file size allowed is 2 MB.\n Please upload only DOC, PDF, xls, xlsx, png, jpeg file.");
          } else if (e.currentTarget.files[i].size > 2097152) {
            $("#documents").val('');
            return showError("Maximum file size allowed is 2 MB.");
          } else if (fileTypes.indexOf(e.currentTarget.files[i].type) == -1) {
            $("#documents").val('');
            return showError("Please upload only DOC, PDF, xls, xlsx, png, jpeg file.");
          }
        }

        this.setState({
          movement: {
            ...this.state.movement,
            documents: e.currentTarget.files
          }
        })
      } else {
        this.setState({
          movement: {
            ...this.state.movement,
            documents: e.currentTarget.files
          }
        })
      }

    } else {
      this.setState({
        movement: {
          ...this.state.movement,
          [name]: e.target.value
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
    
    if (e.target.id.toLowerCase() == "cancel") {
      $('#department, #designation, #assignee').prop('required', false);
    }

    if ($('#update-promotion').valid()) {
      var ID = e.target.id,
        _this = this;
      var stateId = getUrlVars()["stateId"];
      var tempInfo = Object.assign({}, _this.state.movement);
      if(tempInfo.workflowDetails)
      tempInfo.workflowDetails.action =  ID;

      if (tempInfo.documents && tempInfo.documents.constructor == FileList) {
        let counter = tempInfo.documents.length,
          breakout = 0,
          docs = [];
        for (let i = 0, len = tempInfo.documents.length; i < len; i++) {
          this.makeAjaxUpload(tempInfo.documents[i], function (err, res) {
            if (breakout == 1) {
              console.log("breakout", breakout);
              return;
            } else if (err) {
              showError("Error uploding the files. Please contact Administrator");
              breakout = 1;
            } else {
              counter--;
              docs.push(res.files[0].fileStoreId);
              console.log("docs", docs);
              if (counter == 0 && breakout == 0) {
                tempInfo.documents = docs.concat(_this.state.docs);;


                var body = {
                  "RequestInfo": requestInfo,
                  "Movement": [tempInfo]
                };

                $.ajax({
                  url: baseUrl + "/hr-employee-movement/movements/" + _this.state.movement.id + "/" + "_update?tenantId=" + tenantId,
                  type: 'POST',
                  dataType: 'json',
                  data: JSON.stringify(body),

                  contentType: 'application/json',
                  headers: {
                    'auth-token': authToken
                  },
                  success: function (res) {
                    console.log("res", res.Movement[0].workflowDetails.assignee);
                    var employee, designation;

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

                    let _positionId = (ID === "Reject") ? _this.state.initiator : res.Movement[0].workflowDetails.assignee;
                    console.log("res", _positionId );
                    commonApiPost("hr-employee", "employees", "_search", {
                      tenantId,
                      asOnDate,
                      positionId: _positionId
                    }, function (err, res2) {
                      if (res2 && res2.Employee && res2.Employee[0])
                        employee = res2.Employee[0];

                      employee.assignments.forEach(function (item) {
                        if (item.isPrimary)
                          designation = item.designation;
                      });
                      var ownerDetails = employee.name + " - " + employee.code + " - " + getNameById(_this.state.designationList, designation);

                      if (ID === "Submit")
                        window.location.href = `app/hr/movements/ack-page.html?type=PromotionSubmit&owner=${ownerDetails}`;
                      if (ID === "Approve")
                        window.location.href = `app/hr/movements/ack-page.html?type=PromotionApprove&owner=${ownerDetails}`;
                      if (ID === "Cancel")
                        window.location.href = `app/hr/movements/ack-page.html?type=PromotionCancel&owner=${ownerDetails}`;
                      if (ID === "Reject")
                        window.location.href = `app/hr/movements/ack-page.html?type=PromotionReject&owner=${ownerDetails}`;
                    });
                  },
                  error: function (err) {
                    if (err["responseJSON"].message)
                      showError(err["responseJSON"].message);
                    else if (err["responseJSON"].Movement[0] && err["responseJSON"].Movement[0].errorMsg) {
                      showError(err["responseJSON"].Movement[0].errorMsg)
                    } else {
                      showError("Something went wrong. Please contact Administrator");
                    }
                  }
                });

              }
            }
          })
        }
        // if (breakout == 1)
        //     return;
      } else {

        var body = {
          "RequestInfo": requestInfo,
          "Movement": [tempInfo]
        };

        $.ajax({
          url: baseUrl + "/hr-employee-movement/movements/" + _this.state.movement.id + "/" + "_update?tenantId=" + tenantId,
          type: 'POST',
          dataType: 'json',
          data: JSON.stringify(body),

          contentType: 'application/json',
          headers: {
            'auth-token': authToken
          },
          success: function (res) {
            console.log("res", res.Movement[0].workflowDetails.assignee);
            var employee, designation;

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
            let _positionId = (ID === "Reject") ? _this.state.initiator : res.Movement[0].workflowDetails.assignee;
            console.log("res", _positionId );
            commonApiPost("hr-employee", "employees", "_search", {
              tenantId,
              asOnDate,
              positionId: _positionId
            }, function (err, res2) {
              if (res2 && res2.Employee && res2.Employee[0])
                employee = res2.Employee[0];

              employee.assignments.forEach(function (item) {
                if (item.isPrimary)
                  designation = item.designation;
              });
              var ownerDetails = employee.name + " - " + employee.code + " - " + getNameById(_this.state.designationList, designation);

              if (ID === "Submit")
                window.location.href = `app/hr/movements/ack-page.html?type=PromotionSubmit&owner=${ownerDetails}`;
              if (ID === "Approve")
                window.location.href = `app/hr/movements/ack-page.html?type=PromotionApprove&owner=${ownerDetails}`;
              if (ID === "Cancel")
                window.location.href = `app/hr/movements/ack-page.html?type=PromotionCancel&owner=${ownerDetails}`;
              if (ID === "Reject")
                window.location.href = `app/hr/movements/ack-page.html?type=PromotionReject&owner=${ownerDetails}`;
            });
          },
          error: function (err) {
            if (err["responseJSON"].message)
              showError(err["responseJSON"].message);
            else if (err["responseJSON"].Movement[0] && err["responseJSON"].Movement[0].errorMsg) {
              showError(err["responseJSON"].Movement[0].errorMsg)
            } else {
              showError("Something went wrong. Please contact Administrator");
            }
          }
        });
      }
    } else {
      showError("Please fill all required fields");
    }

  }


  render() {

    let { handleChange, handleChangeThreeLevel, handleProcess } = this;
    let _this = this;

    let { employeeId, typeOfMovement, currentAssignment, transferType, promotionBasis, remarks, reason, effectiveFrom, transferedLocation,
      departmentAssigned, designationAssigned, positionAssigned, fundAssigned, functionAssigned, employeeAcceptance, workflowDetails, tenantId } = this.state.movement
    let { isSearchClicked, employee, transferWithPromotion, buttons } = this.state;
    let mode = getUrlVars()["type"];

    const renderProcesedBtns = function () {
      if (buttons.length) {
        return buttons.map(function (btn, ind) {
          return (<span key={ind}> <button key={ind} id={btn.key} type='button' className='btn btn-submit' onClick={(e) => { handleProcess(e) }} >
            {btn.name}
          </button> &nbsp; </span>)
        })
      }
    }

    const renderOptionForDesc = function (list) {
      if (list) {
        return list.map((item, ind) => {
          return (<option key={ind} value={typeof item == "object" ? item.id : item}>
            {typeof item == "object" ? item.description : item}
          </option>)
        })
      }
    }

    const renderOption = function (list) {
      if (list) {
        return list.map((item, ind) => {
          return (<option key={ind} value={item.id}> {item.name} </option>)
        })
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

      if (status === "Rejected") {
        return (
          <div>
            <br />
            <div className="form-section">
              <div className="row">
                <div className="col-md-8 col-sm-8">
                  <h3 className="categoryType">Approval Details </h3>
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
                          onChange={(e) => { handleChange(e, "department") }} required>
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
                          onChange={(e) => { handleChange(e, "designation") }} required>
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

    const renderFileTr = function (status) {
      var CONST_API_GET_FILE = "/filestore/v1/files/id?tenantId=" + tenantId + "&fileStoreId=";

      return _this.state.docs.map(function (file, ind) {
        return (
            <tr key={ind}>
                <td>{ind + 1}</td>
                <td>{"Document "+ (ind + 1)}</td>
                <td>
                    <a href={window.location.origin + CONST_API_GET_FILE + file} target="_blank">
                        Download
              </a>
                </td>
            </tr>
        )
    })

    }

    const renderFile = function (status) {
      if (_this.state.docs && _this.state.docs.length) {
        return (
          <table className="table table-bordered" id="fileTable">
            <thead>
              <tr>
                <th>Sr. No.</th>
                <th>Name</th>
                <th>File</th>
              </tr>
            </thead>
            <tbody>
              {renderFileTr()}
            </tbody>
          </table>
        );
      }
    }


    return (

      <div>
        <form id="update-promotion">
          <div className="form-section">
            <div className="row">
              <div className="col-md-8 col-sm-8">
                <h3 className="categoryType">Employee Details </h3>
              </div>
            </div>
            <div className="row">
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label htmlFor="code">Employee Code  </label>
                  </div>
                  <div className="col-sm-6">
                    <input type="text" name="code" id="code" value={employee.code}
                      onChange={(e) => { handleChange(e, "employee", "code") }} disabled />
                  </div>
                </div>
              </div>
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label htmlFor=""> Employee Name  </label>
                  </div>
                  <div className="col-sm-6">
                    <input type="text" name="name" id="name" value={employee.name}
                      onChange={(e) => { handleChange(e, "employee", "name") }} disabled />
                  </div>
                </div>
              </div>
            </div>
            <div className="row">
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label htmlFor="">Department  </label>
                  </div>
                  <div className="col-sm-6">
                    <div className="styled-select">
                      <select name="departmentId" value={employee.departmentId}
                        onChange={(e) => { handleChange(e, "employee", "departmentId") }} disabled>
                        <option value="">Select department</option>
                        {renderOption(this.state.departmentList)}
                      </select>
                    </div>
                  </div>
                </div>
              </div>
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label htmlFor="">Designation  </label>
                  </div>
                  <div className="col-sm-6">
                    <div className="styled-select">
                      <select name="designationId" value={employee.designationId}
                        onChange={(e) => { handleChange(e, "employee", "designationId") }} disabled>
                        <option value="">Select Designation</option>
                        {renderOption(this.state.designationList)}
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
                    <label htmlFor="">Position  </label>
                  </div>
                  <div className="col-sm-6">
                    <div className="styled-select">
                      <select name="positionId" value={employee.positionId}
                        onChange={(e) => { handleChange(e, "employeeid", "positionId") }} disabled>
                        <option value="">Select Position</option>
                        {renderOption(this.state.positionList)}
                      </select>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <br />
          <div className="form-section">
            <div className="row">
              <div className="col-md-8 col-sm-8">
                <h3 className="categoryType">Promotion Details </h3>
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
                      <select id="department" name="departmentAssigned" value={departmentAssigned}
                        onChange={(e) => { handleChange(e, "departmentAssigned") }} required>
                        <option value="">Select department</option>
                        {renderOption(this.state.departmentList)}
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
                      <select id="designation" name="designationAssigned" value={designationAssigned}
                        onChange={(e) => { handleChange(e, "designationAssigned") }} required>
                        <option value="">Select Designation</option>
                        {renderOption(this.state.designationList)}
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
                    <label htmlFor="">Promotion effective from<span>*</span></label>
                  </div>
                  <div className="col-sm-6">
                    <div className="text-no-ui">
                      <span><i className="glyphicon glyphicon-calendar"></i></span>
                      <input type="text" id="effectiveFrom" name="effectiveFrom" value="effectiveFrom" value={effectiveFrom}
                        onChange={(e) => { handleChange(e, "effectiveFrom") }} required />

                    </div>
                  </div>
                </div>
              </div>
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label htmlFor="">Position name<span>*</span></label>
                  </div>
                  <div className="col-sm-6">
                    <div className="styled-select">
                      <select id="positionAssigned" name="positionAssigned" value={positionAssigned}
                        onChange={(e) => { handleChange(e, "positionAssigned") }} required>
                        <option value="">Select Promotion Position</option>
                        {renderOption(this.state.pNameList)}
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
                    <label htmlFor="">Promotion Basis<span>*</span></label>
                  </div>
                  <div className="col-sm-6">
                    <div className="styled-select">
                      <select id="promotionBasis" name="promotionBasis" value={promotionBasis.id}
                        onChange={(e) => { handleChange(e, "promotionBasis") }} required>
                        <option value="">Select Promotion Basis</option>
                        {renderOptionForDesc(this.state.promotionList)}
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
                    <label htmlFor="">Fund</label>
                  </div>
                  <div className="col-sm-6">
                    <div className="styled-select">
                      <select id="fundAssigned" name="fundAssigned" value={fundAssigned}
                        onChange={(e) => { handleChange(e, "fundAssigned") }}>
                        <option value="">Select Fund</option>
                        {renderOption(this.state.fundList)}
                      </select>
                    </div>
                  </div>
                </div>
              </div>
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label htmlFor="">Function</label>
                  </div>
                  <div className="col-sm-6">
                    <div className="styled-select">
                      <select id="functionAssigned" name="functionAssigned" value={functionAssigned}
                        onChange={(e) => { handleChange(e, "functionAssigned") }}>
                        <option value="">Select function</option>
                        {renderOption(this.state.functionaryList)}
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
                    <label htmlFor="remark">Remark <span>*</span></label>
                  </div>
                  <div className="col-sm-6">
                    <textarea rows="4" cols="50" id="remarks" name="remarks" value={remarks}
                      onChange={(e) => { handleChange(e, "remarks") }} required></textarea>
                  </div>
                </div>
              </div>
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label htmlFor="documents">Attachments docs </label>
                  </div>
                  <div className="col-sm-6">
                    <div className="styled-file">
                      <input id="documents" name="documents" type="file"
                        onChange={(e) => { handleChange(e, "documents") }} multiple />
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div className="row">
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label htmlFor="">Promotion Accepted by Employee Y/N <span>*</span></label>
                  </div>
                  <div className="col-sm-6">
                    <div className="styled-select">
                      <select id="employeeAcceptance" name="employeeAcceptance" value={employeeAcceptance}
                        onChange={(e) => { handleChange(e, "employeeAcceptance") }} required >
                        <option value="">Select Promotion</option>
                        <option value="true">Yes</option>
                        <option value="false">No</option>
                      </select>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            {renderFile()}
          </div>

          {renderWorkflowDetails(this.state.status)}


          <br />
          <div className="text-center">
            {renderProcesedBtns()}
            <button type="button" className="btn btn-close" onClick={(e) => { this.close() }}>Close</button>
          </div>
        </form>
      </div>
    );
  }
}




ReactDOM.render(
  <UpdateMovement />,
  document.getElementById('root')
);
