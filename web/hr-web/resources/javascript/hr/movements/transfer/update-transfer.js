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
          enquiryPassedDate: "",
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
          tenantId: tenantId
        },
        employee: {
          id: "",
          name: "",
          code: "",
          departmentId: "",
          designationId: "",
          positionId: ""
        },
        positionId:"",
        transferWithPromotion: false,
        positionList: [],
        departmentList: [],
        designationList: [],
        employees: [],
        promotionList: [],
        wfDesignationList: [],
        fundList: [],
        functionaryList: [],
        districtList: [],
        transferList: [],
        reasonList: [],
        pNameList: [],
        userList: [],
        buttons: [],
        owner:""
      }

      this.handleChange = this.handleChange.bind(this);
      this.handleProcess = this.handleProcess.bind(this);
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

      $("input,select,textarea").prop("disabled", true);

      var type = getUrlVars()["type"],
        _this = this;
      var stateId = getUrlVars()["stateId"];
      var process;
      var transferWithPromotion;

      var _state = {}, count = 9;
      const checkCountAndCall = function(key, res) {
        _state[key] = res;
        count--;
        if(count == 0)
          _this.setInitialState(_state);
      }

      getDropdown("assignments_designation", function(res) {
        checkCountAndCall("designationList", res);
      });
      getDropdown("assignments_department", function(res) {
        checkCountAndCall("departmentList", res);
      });
      getDropdown("assignments_position", function(res) {
        checkCountAndCall("positionList", res);
      });
      getDropdown("assignments_fund", function(res) {
        checkCountAndCall("fundList", res);
      });
      getDropdown("assignments_functionary", function(res) {
        checkCountAndCall("functionaryList", res);
      });
      getDropdown("transferReason", function(res) {
        checkCountAndCall("reasonList", res);
      });
      getDropdown("promotionBasis", function(res) {
        checkCountAndCall("promotionList", res);
      });
      getDropdown("transferType", function(res) {
        checkCountAndCall("transferList", res);
      });
      getDropdown("districtList", function(res) {
        checkCountAndCall("districtList", res);
      });


      commonApiPost("hr-employee-movement", "movements", "_search", {tenantId: tenantId,stateId: stateId}, function(err, res) {
        if (res) {
          if(res.Movement[0]){
            var Movement = res.Movement[0];

            if(Movement.typeOfMovement === "TRANSFER_CUM_PROMOTION") {
                transferWithPromotion =true;
            }
            if(!Movement.enquiryPassedDate)
              Movement.enquiryPassedDate = "";


          getCommonMasterById("hr-employee","employees", res.Movement[0].employeeId, function(err, res) {
              if(res && res.Employee) {
                var obj = res.Employee[0];
                var ind = 0;
                if(obj.length > 0) {
                  obj.map((item,index)=>{
                        for(var i=0; i<item.assignments.length; i++) {
                          if([true, "true"].indexOf(item.assignments[i].isPrimary) > -1) {
                            ind = i;
                            break;
                          }
                        }
                  });
                }
                _this.setState({
                    ..._this.state,
                    transferWithPromotion:transferWithPromotion,
                    movement: Movement,
                    employee: {
                      name: obj.name,
                      code: obj.code,
                      departmentId:obj.assignments[ind].department,
                      designationId:obj.assignments[ind].designation,
                      positionId:obj.assignments[ind].position
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
      }, function(err, res) {
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


            _this.setState({
              ..._this.state,
              buttons: _btns.length ? _btns : [],
              owner:process.owner.id
            })
          }
        }
      })

    }


    componentDidUpdate(){
      $("input,select,textarea").prop("disabled", true);
    }

    handleChange(e, name) {
      this.setState({
        leaveSet: {
          ...this.state.leaveSet,
          [name]: e.target.value
        }
      })
    }


    close() {
      // widow.close();
      open(location, '_self').close();
    }

    handleProcess(e) {
      e.preventDefault();
      var ID = e.target.id, _this = this;
      var stateId = getUrlVars()["stateId"];
      var tempInfo = Object.assign({}, _this.state.movement);
      tempInfo.workflowDetails = {"action" : ID, "assignee": _this.state.owner};

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
        success: function(res) {
          if(ID === "Approve")
          showSuccess("Successfully Approved Application");
          if(ID === "Cancel")
          showSuccess("Successfully Cancelled Application");
          if(ID === "Reject")
          showSuccess("Successfully Rejected Application");
        },
        error: function(err) {
          showError(err);

        }
      });

    }
    render() {

        let {handleChange,handleChangeThreeLevel,handleProcess}=this;
        let _this = this;

        let {employeeId, typeOfMovement, currentAssignment, transferType, promotionBasis, remarks, reason, effectiveFrom, enquiryPassedDate, transferedLocation,
              departmentAssigned, designationAssigned, positionAssigned, fundAssigned, functionAssigned, employeeAcceptance, workflowDetails, tenantId} = this.state.movement
        let {isSearchClicked,employee,transferWithPromotion, buttons}=this.state;
        let mode = getUrlVars()["type"];

        const renderProcesedBtns = function() {
          if (buttons.length) {
            return buttons.map(function(btn, ind) {
              return (<span key = {ind}> <button key = {ind} id = {btn.key} type = 'button' className = 'btn btn-submit' onClick = {(e) => {handleProcess(e)}} >
                  {btn.name}
                  </button> &nbsp; </span>)
            })
          }
        }

        const renderOptionForDesc=function(list) {
            if(list)
            {
                return list.map((item, ind)=>
                {
                    return (<option key={ind} value={typeof item == "object" ? item.id : item}>
                            {typeof item == "object" ? item.description : item}
                      </option>)
                })
            }
        }

        const renderOption = function(list) {
            if (list) {
              return list.map((item, ind) => {
                  return ( <option key={ind} value = {item.id}> {item.name} </option>)
                  })
              }
            }

            const promotionFunc=function() {
              if(transferWithPromotion=="true"||transferWithPromotion==true){
                return(<div className="row">
                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                            <label htmlFor="">Enquiry passed Date</label>
                          </div>
                          <div className="col-sm-6">
                            <div className="text-no-ui">
                              <span><i className="glyphicon glyphicon-calendar"></i></span>
                              <input type="text" id="enquiryPassedDate" name="enquiryPassedDate" value="enquiryPassedDate" value={enquiryPassedDate}
                                      onChange={(e)=>{handleChange(e,"enquiryPassedDate")}} />
                            </div>
                          </div>
                      </div>
                </div>
                <div className="col-sm-6">
                    <div className="row">
                        <div className="col-sm-6 label-text">
                          <label htmlFor="">Promotion Basis<span>*</span></label>
                        </div>
                        <div className="col-sm-6">
                          <div className="styled-select">
                            <select id="promotionBasis" name="promotionBasis" value={promotionBasis.id}
                            onChange={(e)=>{handleChange(e,"promotionBasis")}} required>
                              <option value="">Select Promotion Basis</option>
                              {renderOptionForDesc(_this.state.promotionList)}
                           </select>
                           </div>
                        </div>
                    </div>
                  </div>
              </div>);
              }
            };

            const renderOptionForUser=function(list) {
                if(list)
                {
                    return list.map((item, ind)=>
                    {
                      var positionId;
                      item.assignments.forEach(function(item) {
                                          if(item.isPrimary)
                                          {
                                            positionId = item.position;
                                          }
                                      });

                        return (<option key={ind} value={positionId}>
                                {item.name}
                          </option>)
                    })
                }
            }





    return (
      <div>
        <form>
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
                          onChange={(e)=>{handleChange(e,"employee","code")}} disabled />
                      </div>
                  </div>
                </div>
                <div className="col-sm-6">
                    <div className="row">
                        <div className="col-sm-6 label-text">
                          <label htmlFor=""> Employee Name  </label>
                        </div>
                        <div className="col-sm-6">
                            <input type="text" name="name" id="name" value= {employee.name}
                                onChange={(e)=>{handleChangeTwoLevel(e,"employee","name")}} disabled/>
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
                          onChange={(e)=>{  handleChangeTwoLevel(e,"employee","departmentId")}} disabled>
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
                              onChange={(e)=>{ handleChange(e,"employee","designationId")}} disabled>
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
                              onChange={(e)=>{ handleChange(e,"employee","positionId")}} disabled >
                            <option value="">Select Position</option>
                            {renderOption(this.state.positionList)}
                           </select>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <br/>
            <div className="form-section">
                <div className="row">
                  <div className="col-md-8 col-sm-8">
                    <h3 className="categoryType">Transfer Details </h3>
                  </div>
                </div>
            <div className="row">
              <div className="col-sm-6">
                  <div className="row">
                      <div className="col-sm-6 label-text">
                        <label htmlFor="">Transfer Type <span>*</span></label>
                      </div>
                      <div className="col-sm-6">
                        <div className="styled-select">
                          <select id="transferType" name="transferType" value={transferType}
                          onChange={(e)=>{handleChange(e,"transferType")}}required>
                            <option value="">Select Transfer Type</option>
                            {renderOption(this.state.transferList)}
                         </select>
                         </div>
                      </div>
                  </div>
                </div>
                <div className="col-sm-6">
                    <div className="row">
                        <div className="col-sm-6 label-text">
                          <label htmlFor="">Reason for Transfer <span>*</span></label>
                        </div>
                        <div className="col-sm-6">
                          <div className="styled-select">
                              <select id="reason" name="reason" value={reason.id}
                                onChange={(e)=>{  handleChange(e,"reason")}}required>
                              <option value="">Select Reason</option>
                              {renderOptionForDesc(this.state.reasonList)}
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
                            <label htmlFor="">District-ULB <span>*</span></label>
                          </div>
                          <div className="col-sm-6">
                            <div className="styled-select">
                              <select id="transferedLocation" name="transferedLocation" value={transferedLocation}
                                onChange={(e)=>{  handleChange(e,"transferedLocation") }}required>
                                <option value="">Select District-ULB</option>
                                {renderOption(this.state.districtList)}
                             </select>
                             </div>
                          </div>
                      </div>
                    </div>
                <div className="col-sm-6">
                    <div className="row">
                      <div className="col-sm-6 label-text">
                          <label htmlFor="transferWithPromotion">Transfer with Promotion</label>
                      </div>
                          <div className="col-sm-6">
                                <label className="radioUi">
                                  <input type="checkbox" name="transferWithPromotion" value="transferWithPromotion" checked={transferWithPromotion == "true" || transferWithPromotion  ==  true}
                                   onChange={(e)=>{ handleChange(e,"transferWithPromotion")}}/>
                                </label>
                          </div>
                      </div>
                    </div>
                </div>
                {promotionFunc()}
                <div className="row">
                  <div className="col-sm-6">
                    <div className="row">
                        <div className="col-sm-6 label-text">
                          <label htmlFor="">Department <span>*</span></label>
                        </div>
                        <div className="col-sm-6">
                          <div className="styled-select">
                            <select id="departmentAssigned" name="departmentAssigned" value={departmentAssigned}
                              onChange={(e)=>{  handleChange(e,"departmentAssigned")}}required>
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
                                <select id="designationAssigned" name="designationAssigned" value={designationAssigned}
                                    onChange={(e)=>{handleChange(e,"designationAssigned")}}required>
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
                            <label htmlFor="">Transfer/Promotion effective from <span>*</span></label>
                          </div>
                          <div className="col-sm-6">
                            <div className="text-no-ui">
                              <span><i className="glyphicon glyphicon-calendar"></i></span>
                              <input type="text" id="effectiveFrom" name="effectiveFrom" value="effectiveFrom" value={effectiveFrom}
                              onChange={(e)=>{handleChange(e,"effectiveFrom")}} required/>
                          </div>
                      </div>
                  </div>
                </div>
                    <div className="col-sm-6">
                        <div className="row">
                            <div className="col-sm-6 label-text">
                              <label htmlFor="">Position <span>*</span></label>
                            </div>
                            <div className="col-sm-6">
                              <div className="styled-select">
                                  <select id="positionAssigned" name="positionAssigned" value={positionAssigned}
                                    onChange={(e)=>{  handleChange(e,"positionAssigned")}}required>
                                  <option value="">Select Position</option>
                                  {renderOption(this.state.positionList)}
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
                                    onChange={(e)=>{handleChange(e,"fundAssigned")}}>
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
                                      onChange={(e)=>{  handleChange(e,"functionAssigned") }}>
                                    <option value="">Select functionary</option>
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
                              onChange={(e)=>{handleChange(e,"remarks")}} ></textarea>
                              </div>
                          </div>
                      </div>
                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                              <label htmlFor="documents">Attachments docs</label>
                          </div>
                          <div className="col-sm-6">
                              <div className="styled-file">
                              <input id="documents" name="documents" type="file" multiple/>
                             </div>
                          </div>
                      </div>
                  </div>
              </div>
              <div className="row">
                <div className="col-sm-6">
                    <div className="row">
                        <div className="col-sm-6 label-text">
                          <label htmlFor="">Transfer Accepted by Employee Y/N</label>
                        </div>
                        <div className="col-sm-6">
                          <div className="styled-select">
                              <select id="employeeAcceptance" name="employeeAcceptance" value={employeeAcceptance}
                                onChange={(e)=>{  handleChange(e,"employeeAcceptance") }}>
                              <option value="">Select Promotion</option>
                              <option value="true">Yes</option>
                              <option value="false">No</option>
                             </select>
                        </div>
                      </div>
                    </div>
                  </div>
              </div>
            </div>





          <br/>
          <div className="text-center">
            {renderProcesedBtns()}
            <button type="button" className="btn btn-close" onClick={(e)=>{this.close()}}>Close</button>
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
