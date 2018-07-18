class Nominee extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      allNomineeValue: {
        "tenantId": tenantId,
        "employee": {
          id: "",
          name: "",
          code: ""
        },
        nomineeFieldsDefination: []
      },
      nomineeSet: {
        "name": "",
        "gender": "",
        "dateOfBirth": "",
        "maritalStatus": "",
        "relationship": "",
        "bank": "",
        "bankBranch": "",
        "bankAccount": "",
        "nominated": true,
        "employed": true,
        "createdBy": "",
        "createdDate": "",
        "lastModifiedBy": "",
        "lastModifiedDate": "",
        "tenantId": tenantId
      },
      isCustomFormVisible: false,
      dataType: [],
      isEdit: false,
      index: -1,
      list: [],
      genderList: [],
      MaritalList: [],
      relationList: [],
      bankList: [],
      branchList: [],
      allBankBranchList: [],
      employList: [],
      showMsg: false,
      id: ""
    }
    this.handleChange = this.handleChange.bind(this);
    this.addOrUpdate = this.addOrUpdate.bind(this);
    this.addNewRow = this.addNewRow.bind(this);
    this.showCustomFieldForm = this.showCustomFieldForm.bind(this);
    this.setInitialState = this.setInitialState.bind(this);
    this.addNominee = this.addNominee.bind(this);
    this.handleChangeThreeLevel = this.handleChangeThreeLevel.bind(this);
  }

  setInitialState(initState) {
    this.setState(initState);
  }

  addNominee() {
    var {
      isEdit,
      index,
      list,
      nomineeSet
    } = this.state;


    var patt = new RegExp("[a-zA-Z\s]+");
    if (!patt.test(nomineeSet.name)) {
      return this.setState({
        showMsg: "name"
      })
    }


    if ((!nomineeSet.name || !nomineeSet.dateOfBirth || !nomineeSet.employed ||
      !nomineeSet.maritalStatus || !nomineeSet.gender || !nomineeSet.relationship)) {
      return this.setState({
        showMsg: true
      })
    } else {
      var _this = this;
      setTimeout(function () {
        _this.setState({
          showMsg: false
        });
      }, 300);
    }

    if (isEdit) {
      var nomineeFieldsDefination = this.state.allNomineeValue.nomineeFieldsDefination;
      nomineeFieldsDefination[index] = nomineeSet;
      this.setState({
        allNomineeValue: {
          ...this.state.allNomineeValue,
          nomineeFieldsDefination
        },
        isEdit: false,
        isCustomFormVisible: false,
        nomineeSet: {
          "name": "",
          "gender": "",
          "dateOfBirth": "",
          "maritalStatus": "",
          "relationship": "",
          "bank": "",
          "bankBranch": "",
          "bankAccount": "",
          "nominated": true,
          "employed": true,
          "createdBy": "",
          "createdDate": "",
          "lastModifiedBy": "",
          "lastModifiedDate": "",
          "tenantId": tenantId
        }
      })
      //this.setState({isEdit:false})
    } else {
      //get asset Category from state
      // customFieldData["columns"]=[];
      var temp = Object.assign([], this.state.allNomineeValue.nomineeFieldsDefination);
      temp.push(nomineeSet);
      this.setState({
        allNomineeValue: {
          ...this.state.allNomineeValue,
          nomineeFieldsDefination: temp
        },
        nomineeSet: {
          "name": "",
          "gender": "",
          "dateOfBirth": "",
          "maritalStatus": "",
          "relationship": "",
          "bank": "",
          "bankBranch": "",
          "bankAccount": "",
          "nominated": true,
          "employed": true,
          "createdBy": "",
          "createdDate": "",
          "lastModifiedBy": "",
          "lastModifiedDate": "",
          "tenantId": tenantId
        },
        isCustomFormVisible: false
      })
    }
  }


  handleChangeThreeLevel(e, pName, name, key) {

    this.setState({
      [pName]: {
        ...this.state[pName],
        [name]: {
          ...this.state[name],
          key: e.target.value
        }
      }
    })
  }

  handleChange(e, name) {
    var self = this, val = e.target.value;
    if (name === "nominated") {
      this.setState({
        nomineeSet: {
          ...this.state.nomineeSet,
          nominated: !this.state.nomineeSet.nominated
        }
      })
    } else if (name === "employed") {
      this.setState({
        nomineeSet: {
          ...this.state.nomineeSet,
          employed: !this.state.nomineeSet.employed
        }
      })
    } else if (name === "bank") {
      if (val) {
        commonApiPost("egf-masters", "bankbranches", "_search", {
          tenantId,
          "bank": val
        }, function (err, res) {
          if (res) {
            self.setState({
              branchList: res.bankBranches,
              nomineeSet: {
                ...self.state.nomineeSet,
                [name]: val,
                bankBranch: ""
              }
            })
          } else {
            self.setState({
              branchList: [],
              nomineeSet: {
                ...self.state.nomineeSet,
                [name]: val,
                bankBranch: ""
              }
            })
          }
        })
      } else {
        self.setState({
          branchList: [],
          nomineeSet: {
            ...self.state.nomineeSet,
            [name]: val,
            bankBranch: ""
          }
        })
      }
    } else {
      this.setState({
        nomineeSet: {
          ...this.state.nomineeSet,
          [name]: val
        }
      })
    }
  }

  showCustomFieldForm(isShow, index) {
    if (typeof (index) != "undefined") {
      var nomineeFieldsDefination = this.state.allNomineeValue.nomineeFieldsDefination;
      var nomineeSet = this.state.nomineeSet;
      nomineeSet = nomineeFieldsDefination[index];
      this.setState({
        nomineeSet,
        isEdit: true,
        index: index
      })
    }
    var _this = this;
    setTimeout(function () {
      $('#dateOfBirth').datepicker({
        format: 'dd/mm/yyyy',
        autoclose: true
      });
    }, 500);
    _this.setState({ isCustomFormVisible: isShow });
  }

  componentDidMount() {
    var _this = this;
    var id = getUrlVars()["id"];
    if (window.opener && window.opener.document) {
      var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
      if (logo_ele && logo_ele[0]) {
        document.getElementsByClassName("homepage_logo")[0].src = window.location.origin + logo_ele[0].getAttribute("src");
      }
    }

    if (getUrlVars()["type"]) $('#hp-citizen-title').text(titleCase(getUrlVars()["type"]) + " Nominee");

    $('body').on('changeDate', "#dateOfBirth", function (e) {
      _this.setState({
        nomineeSet: {
          ..._this.state.nomineeSet,
          "dateOfBirth": $("#dateOfBirth").val()
        }
      })
    });

    var count = 5, _state = {}, _this = this;

    const checkCountAndCall = function (key, res) {
      _state[key] = res;
      count--;
      if (count == 0)
        _this.setInitialState(_state);
    }

    getDropdown("maritalStatus", function (res) {
      checkCountAndCall("MaritalList", res);
    })

    getDropdown("bank", function (res) {
      checkCountAndCall("bankList", res);
    })

    getDropdown("gender", function (res) {
      checkCountAndCall("genderList", res);
    })

    getDropdown("relation", function (res) {
      checkCountAndCall("relationList", res);
    })

    getDropdown("bankbranches", function (res) {
      checkCountAndCall("allBankBranchList", res);
    })


    if (getUrlVars()["type"] === "update") {

      getCommonMaster("egf-masters", "bankbranches", function (err, res) {
        if (res) {
          _this.setState({
            ..._this.state,
            branchList: res.bankBranches
          })
        }
      })

    }

    getCommonMasterById("hr-employee", "employees", id, function (err, res) {
      if (res && res.Employee && res.Employee[0]) {
        var obj = res.Employee[0];
        _this.setState({
          allNomineeValue: {
            ..._this.state.allNomineeValue,
            employee: {
              id: id,
              name: obj.name,
              code: obj.code
            }
          }
        })
      }
    })

    commonApiPost("hr-employee", "nominees", "_search", { "employeeId": id, "tenantId": tenantId }, function (err, res) {
      if (res && res.Nominee) {

        res.Nominee.forEach(function (item) {
          var date = new Date(item.dateOfBirth);
          var dd = date.getDate();
          var mm = date.getMonth() + 1; //January is 0!
          var yyyy = date.getFullYear();
          if (dd < 10) { dd = '0' + dd }
          if (mm < 10) { mm = '0' + mm }
          date = dd + '/' + mm + '/' + yyyy;
          item.dateOfBirth = date;
        });

        _this.setState({
          allNomineeValue: {
            ..._this.state.allNomineeValue,
            nomineeFieldsDefination: res.Nominee
          }
        })
      }
    })
  }



  close() {
    // widow.close();
    open(location, '_self').close();
  }


  addOrUpdate(e) {
    // var finalPost={
    //   "RequestInfo":requestInfo,
    //
    // }
    e.preventDefault();

    var _this = this;
    var nomineeFieldsDefination = Object.assign([], _this.state.allNomineeValue.nomineeFieldsDefination);

    for (var k = 0; k < nomineeFieldsDefination.length; k++) {
      for (var l = 0; l < nomineeFieldsDefination.length; l++) {
        if (k!==l && nomineeFieldsDefination[k].name.toLowerCase() === nomineeFieldsDefination[l].name.toLowerCase() && nomineeFieldsDefination[k].relationship === nomineeFieldsDefination[l].relationship){
          return (showError("Dupilcate records are not accepted. Please check"));
        }
      }
    }

    var nominees = [];
    for (var i = 0; i < nomineeFieldsDefination.length; i++) {
      var _tempInfo = Object.assign({}, _this.state.allNomineeValue);
      delete _tempInfo.nomineeFieldsDefination;
      //not adding previously saved nominees in create
      console.log("TYPE", getUrlVars()["type"]);

      if (getUrlVars()["type"] === "update" || !nomineeFieldsDefination[i].id) {
        nominees.push({ ..._tempInfo, ...nomineeFieldsDefination[i] });
      }
      console.log(nominees);
    }

    var _fieldDate = Object.assign([], nominees);
    for (var i = 0; i < _fieldDate.length; i++) {
      var _date = _fieldDate[i].dateOfBirth;
      var dateParts1 = _date.split("/");
      var newDateStr = dateParts1[1] + "/" + dateParts1[0] + "/ " + dateParts1[2];
      var date1 = new Date(newDateStr);
      var _final = date1.getTime();
      _fieldDate[i].dateOfBirth = _final;
    }

    if (getUrlVars()["type"] != "update" && nominees.length === 0)
      return (showError("No new nominees to add. Please use Add Nominee button to add Nominee"));

    var body = {
      "RequestInfo": requestInfo,
      "Nominee": nominees
    };

    if (getUrlVars()["type"] === "update") {
      console.log("Inside update");
      $.ajax({
        url: baseUrl + "/hr-employee/nominees/_update?tenantId=" + tenantId,
        type: 'POST',
        dataType: 'json',
        data: JSON.stringify(body),
        contentType: 'application/json',
        headers: {
          'auth-token': authToken
        },
        success: function (res) {
          showSuccess("Nominee Updated successfully.");
          _this.setState({
            allNomineeValue: {
              "tenantId": tenantId,
              "employee": {
                id: "",
                name: "",
                code: ""
              },
              nomineeFieldsDefination: []
            },
          })
        },
        error: function (err) {
          var _err = err["responseJSON"].Error.message || "";
          if (err["responseJSON"].Error.fields && Object.keys(err["responseJSON"].Error.fields).length) {
            for (var key in err["responseJSON"].Error.fields) {
              _err += "\n " + key + "- " + err["responseJSON"].Error.fields[key] + " "; //HERE
            }
            showError(_err);
          } else if (_err) {
            showError(_err);
          } else {
            showError(err["statusText"]);
          }
        }
      })

    } else {
      $.ajax({
        url: baseUrl + "/hr-employee/nominees/_create?tenantId=" + tenantId,
        type: 'POST',
        dataType: 'json',
        data: JSON.stringify(body),
        contentType: 'application/json',
        headers: {
          'auth-token': authToken
        },
        success: function (res) {
          showSuccess("Nominee Created successfully.");
          _this.setState({
            allNomineeValue: {
              "tenantId": tenantId,
              "employee": {
                id: "",
                name: "",
                code: ""
              },
              nomineeFieldsDefination: []
            },
          })
        },
        error: function (err) {
          var _err = err["responseJSON"].Error.message || "";
          if (err["responseJSON"].Error.fields && Object.keys(err["responseJSON"].Error.fields).length) {
            for (var key in err["responseJSON"].Error.fields) {
              _err += "\n " + key + "- " + err["responseJSON"].Error.fields[key] + " "; //HERE
            }
            showError(_err);
          } else if (_err) {
            showError(_err);
          } else {
            showError(err["statusText"]);
          }
        }
      })
    }
  }
  addNewRow(e, name) {
    e.preventDefault();
    if (!this.state.newRows[name])
      this.setState({
        newRows: {
          ...this.state.newRows,
          [name]: [1]
        }
      })
    else
      this.setState({
        newRows: {
          ...this.state.newRows,
          [name]: [...this.state.newRows[name], 1]
        }
      })
  }

  render() {

    let { handleChange, addOrUpdate, addNewRow, showCustomFieldForm, addNominee, handleChangeThreeLevel } = this;
    let { MaritalList, bankList, list, branchList, allBankBranchList, genderList, relationList, isEdit, index, isCustomFormVisible, readonly, showMsg, allNomineeValue } = this.state;
    let { nomineeFieldsDefination } = this.state.allNomineeValue;
    let mode = getUrlVars()["type"];
    let { name, gender, dateOfBirth, code, maritalStatus, relationship, bank, bankBranch, bankAccount, nominated, employed } = this.state.nomineeSet;

    const renderOption = function (list) {
      if (list) {
        return list.map((item, ind) => {
          return (<option key={ind} value={typeof item == "object" ? item.id : item}>
            {typeof item == "object" ? item.name : item}
          </option>)
        })
      }
    }

    const showCustomFieldsTable = function () {
      return (
        <div className="form-section">
          <h3 className="categoryType">Nominee Details</h3>
          <div className="row" style={{ "padding-right": "18px" }}>
            {showAddNomineeBtn()}
          </div>
          <div className="land-table table-responsive">
            <table className="table table-bordered">
              <thead>
                <tr>
                  <th>Sl No.</th>
                  <th>Nominee Name</th>
                  <th>dateOfBirth</th>
                  <th>Gender</th>
                  <th>relation</th>
                  <th>Marital Status</th>
                  <th>Bank</th>
                  <th>Branch</th>
                  <th>Account Number</th>
                  <th>Is nominee</th>
                  <th>Is employed</th>
                  {renderActionHeader(getUrlVars()["type"])}
                </tr>
              </thead>
              <tbody>
                {renderBody()}
              </tbody>
            </table>
          </div>
          {showCustomFieldAddForm()}
        </div>

      )
    }

    const showCustomFieldAddForm = function () {
      if (isCustomFormVisible) {
        return (
          <div>
            <h3 className="categoryType">Add</h3>
            <div className="form-section">
              <div className="row">
                <div className="col-sm-6">
                  <div className="row">
                    <div className="col-sm-6 label-text">
                      <label htmlFor="">Name <span>*</span></label>
                    </div>
                    <div className="col-sm-6">
                      <input type="text" name="name" value={name} id="name" onChange={(e) => { handleChange(e, "name") }} required />
                    </div>
                  </div>
                </div>
                <div className="col-sm-6">
                  <div className="row">
                    <div className="col-sm-6 label-text">
                      <label for="dateOfBirth">Date Of Birth <span>*</span></label>
                    </div>
                    <div className="col-sm-6">
                      <input type="text" id="dateOfBirth" name="dateOfBirth" value={dateOfBirth}
                        onChange={(e) => { handleChange(e, "dateOfBirth") }} required />
                    </div>
                  </div>
                </div>
              </div>
              <div className="row">
                <div className="col-sm-6">
                  <div className="row">
                    <div className="col-sm-6 label-text">
                      <label for="gender"> Gender <span>*</span></label>
                    </div>
                    <div className="col-sm-6">
                      <div className="styled-select">
                        <select name="gender" value={gender}
                          onChange={(e) => { handleChange(e, "gender") }} required>
                          <option value="">Select Type</option>
                          {renderOption(genderList)}
                        </select>
                      </div>
                    </div>
                  </div>
                </div>

                <div className="col-sm-6">
                  <div className="row">
                    <div className="col-sm-6 label-text">
                      <label for="relationship"> Relation <span>*</span></label>
                    </div>
                    <div className="col-sm-6">
                      <div className="styled-select">
                        <select name="relationship" value={relationship}
                          onChange={(e) => { handleChange(e, "relationship") }} required>
                          <option value="">Select Type</option>
                          {renderOption(relationList)}
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
                      <label for="maritalStatus"> Marital Status <span>*</span></label>
                    </div>
                    <div className="col-sm-6">
                      <div className="styled-select">
                        <select id="maritalStatus" name="maritalStatus" value={maritalStatus}
                          onChange={(e) => { handleChange(e, "maritalStatus") }} required>
                          <option value="">Select Type</option>
                          {renderOption(MaritalList)}
                        </select>
                      </div>
                    </div>
                  </div>
                </div>
                <div className="col-sm-6">
                  <div className="row">
                    <div className="col-sm-6 label-text">
                      <label for="bank"> Bank</label>
                    </div>
                    <div className="col-sm-6">
                      <div className="styled-select">
                        <select id="bank" name="bank" value={bank}
                          onChange={(e) => { handleChange(e, "bank") }}>
                          <option value="">Select Type</option>
                          {renderOption(bankList)}
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
                      <label for="bankBranch"> Bank Branch </label>
                    </div>
                    <div className="col-sm-6">
                      <div className="styled-select">
                        <select id="bankBranch" name="bankBranch" value={bankBranch}
                          onChange={(e) => { handleChange(e, "bankBranch") }}>
                          <option value="">Select Type</option>
                          {renderOption(branchList)}
                        </select>
                      </div>
                    </div>
                  </div>
                </div>
                <div className="col-sm-6">
                  <div className="row">
                    <div className="col-sm-6 label-text">
                      <label htmlFor="">Bank Account</label>
                    </div>
                    <div className="col-sm-6">
                      <input type="text" name="bankAccount" value={bankAccount} onChange={(e) => { handleChange(e, "bankAccount") }} />
                    </div>
                  </div>
                </div>
              </div>
              <div className="row">
                <div className="col-sm-6">
                  <div className="row">
                    <div className="col-sm-6 label-text">
                      <label htmlFor="">Is Nominee</label>
                    </div>
                    <div className="col-sm-6">
                      <input type="checkbox" name="nominated" value={nominated} onChange={(e) => { handleChange(e, "nominated", true) }} checked={nominated === true ? true : false} />
                    </div>
                  </div>
                </div>
                <div className="col-sm-6">
                  <div className="row">
                    <div className="col-sm-6 label-text">
                      <label for="employed"> Is Employed</label>
                    </div>
                    <div className="col-sm-6">
                      <input type="checkbox" name="employed" value={employed} onChange={(e) => { handleChange(e, "employed", true) }} checked={employed === true ? true : false} />
                    </div>
                  </div>
                </div>
              </div>
              {showNoteMsg()}
              <div className="text-center">
                <button type="button" className="btn btn-primary" onClick={(e) => { addNominee() }}>Add/Edit</button>
              </div>
            </div>
          </div>
        )
      }
    }

    const showNoteMsg = function () {
      if (showMsg === "required") {
        return (<p className="text-danger">All * marked field are required.</p>)
      } else if (showMsg === "name") {
        return (<p className="text-danger">Nominee name should be Aphlanumeric characters with spaces.</p>)
      } else {
        return "";
      }
    }


    const renderBody = function () {
      if (nomineeFieldsDefination.length > 0) {
        return nomineeFieldsDefination.map((item, index) => {
          return (<tr key={index} className="text-center">
            <td>{index + 1}</td>
            <td  >
              {item.name}
            </td>
            <td  >
              {item.dateOfBirth}
            </td>
            <td  >
              {item.gender}
            </td>
            <td  >
              {item.relationship}
            </td>
            <td  >
              {item.maritalStatus}
            </td>
            <td  >
              {getNameById(bankList, item.bank)}
            </td>
            <td >
              {getNameById(allBankBranchList, item.bankBranch)}
            </td>
            <td >
              {item.bankAccount}
            </td>
            <td >
              {item.nominated === true ? "Yes" : "No"}
            </td>

            <td >
              {item.employed === true ? "Yes" : "No"}
            </td>
            {renderActionFeilds(mode, index)}
          </tr>)
        })
      }
    }


    const renderActionHeader = function (mode) {
      if (mode === "update") {
        return (<th>Action</th>);
      }
    }
    const renderActionFeilds = function (mode, index) {
      if (mode === "update") {
        return (<td data-label="Action">
          <button type="button" className="btn btn-default btn-action" onClick={(e) => { showCustomFieldForm(true, index) }}><span className="glyphicon glyphicon-pencil"></span></button>
        </td>);
      }
      console.log("leaving renderActionFeilds");
    }

    const showActionButton = function () {
      if ((!mode) || mode === "update") {
        return (<button type="submit" className="btn btn-submit">{mode ? "Update" : "Create"}</button>);
      }
    };

    const showAddNomineeBtn = function () {
      if (getUrlVars()["type"] != "view" && getUrlVars()["type"] != "update") {
        return (
          <button type="button" className="btn btn-primary  pull-right" onClick={() => { showCustomFieldForm(true) }}>Add Nominee</button>
        )
      }
    }

    return (
      <div>
        <form onSubmit={(e) => { addOrUpdate(e) }}>
          <div className="form-section-inner">

            <div className="row">
              <div className="col-md-8 col-sm-8">
                <h3 className="categoryType">Employee Details </h3>
              </div>
            </div>
            <div className="row">
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label for="name">Employee Name</label>
                  </div>
                  <div className="col-sm-6">
                    <input type="text" name="name" value={allNomineeValue.employee.name}
                      onChange={(e) => { handleChangeThreeLevel(e, "allNomineeValue", "employee", "name") }} disabled />
                  </div>
                </div>
              </div>
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label for="">Employee Code</label>
                  </div>
                  <div className="col-sm-6">
                    <input type="text" name="code" id="code" value={allNomineeValue.employee.code}
                      onChange={(e) => { handleChangeThreeLevel(e, "allNomineeValue", "employee", "code") }} disabled />
                  </div>
                </div>
              </div>
            </div>
            {showCustomFieldsTable()}
            &nbsp;&nbsp;
                  <div className="text-center">
              {showActionButton()} &nbsp;&nbsp;
                        <button type="button" className="btn btn-close" onClick={(e) => { this.close() }}>Close</button>
            </div>
          </div>
        </form>
      </div>
    );
  }
}

ReactDOM.render(
  <Nominee />,
  document.getElementById('root')
);
