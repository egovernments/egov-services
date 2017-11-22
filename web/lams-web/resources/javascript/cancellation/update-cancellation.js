class UpdateCancellation extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      agreement: {
        id: "",
        tenantId: tenantId,
        agreementNumber: "",
        acknowledgementNumber: "",
        stateId: "",
        action: "cancellation",
        agreementDate: "",
        timePeriod: "",
        allottee: {
          id: "",
          name: "",
          pemovementrmanentAddress: "",
          mobileNumber: "",
          aadhaarNumber: "",
          pan: "",
          emailId: "",
          userName: "",
          password: "",
          active: "",
          type: "",
          gender: "",
          tenantId: tenantId,
        },
        asset: {
          id: "",
          assetCategory: {
            id: "",
            name: "",
            code: ""
          },
          name: "",
          code: "",
          locationDetails: {
            locality: "",
            zone: "",
            revenueWard: "",
            block: "",
            street: "",
            electionWard: "",
            doorNo: "",
            pinCode: ""
          }
        },
        tenderNumber: "",
        tenderDate: "",
        councilNumber: "",
        councilDate: "",
        bankGuaranteeAmount: "",
        bankGuaranteeDate: "",
        securityDeposit: "",
        collectedSecurityDeposit: "",
        securityDepositDate: "",
        status: "",
        natureOfAllotment: "",
        registrationFee: "",
        caseNo: "",
        commencementDate: "",
        expiryDate: "",
        orderDetails: "",
        rent: "",
        tradelicenseNumber: "",
        paymentCycle: "",
        rentIncrementMethod: {
          id: "",
          type: "",
          assetCategory: "",
          fromDate: "",
          toDate: "",
          percentage: "",
          flatAmount: "",
          tenantId: tenantId
        },
        orderNumber: "",
        orderDate: "",
        rrReadingNo: "",
        remarks: "",
        solvencyCertificateNo: "",
        solvencyCertificateDate: "",
        tinNumber: "",
        documents: "",
        demands: [],
        workflowDetails: {
          department: "",
          designation: "",
          assignee: "",
          action: "",
          status: "",
          initiatorPosition: "",
          comments: ""
        },
        goodWillAmount: "",
        collectedGoodWillAmount: "",
        source: "",
        legacyDemands: "",
        cancellation: {
          orderNumber: "",
          orderDate: "",
          terminationDate: "",
          reasonForCancellation: "",
        },
        rdivenewal: "",
        eviction: "",
        objection: "",
        judgement: "",
        remission: "",
        createdDate: "",
        createdBy: "",
        lastmodifiedDate: "",
        lastmodifiedBy: "",
        isAdvancePaid: "",
        adjustmentStartDate: ""
      },
      cancelReasons:["Reason 1", "Reason 2", "Reason 3", "Reason 4"],
      positionList:[],
      departmentList:[],
      designationList:[],
      userList:[]

    }
    this.handleChangeTwoLevel = this.handleChangeTwoLevel.bind(this);
    this.addOrUpdate=this.addOrUpdate.bind(this);
    this.setInitialState = this.setInitialState.bind(this);
    this.getUsersFun = this.getUsersFun.bind(this);
  }

    setInitialState(initState) {
      this.setState(initState);
    }

    handleChangeTwoLevel(e,pName,name) {

      var _this = this;

      switch (name) {
          case "department":
          _this.state.agreement.workflowDetails.assignee = "";
          if(this.state.agreement.workflowDetails.designation){
            var _designation = this.state.agreement.workflowDetails.designation;
            _this.getUsersFun(e.target.value,_designation);
          }
          break;
          case "designation":
          _this.state.agreement.workflowDetails.assignee = "";
          if(this.state.agreement.workflowDetails.department){
            var _department = this.state.agreement.workflowDetails.department;
            _this.getUsersFun(_department,e.target.value);
          }
          break;

      }

      _this.setState({
        ..._this.state,
        agreement:{
          ..._this.state.agreement,
          [pName]:{
              ..._this.state.agreement[pName],
              [name]:e.target.value
          }
        }
      })
    }

    getUsersFun(departmentId,designationId){
      var _this=this;
      $.ajax({
          url:baseUrl+"/hr-employee/employees/_search?tenantId=" + tenantId + "&departmentId=" + departmentId + "&designationId="+ designationId,
          type: 'POST',
          dataType: 'json',
          data:JSON.stringify({ RequestInfo: requestInfo }),
          contentType: 'application/json',
          headers:{
            'auth-token': authToken
          },
          success: function(res) {

            _this.setState({
              ..._this.state,
              userList:res.Employee
            })

          },
          error: function(err) {
          }

      })

      }


    componentDidMount() {

      if (window.opener && window.opener.document) {
        var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
        if (logo_ele && logo_ele[0]) {
          document.getElementsByClassName("homepage_logo")[0].src = window.location.origin + logo_ele[0].getAttribute("src");
        }
      }
      $('#lams-title').text("Cancellation Of Agreement");
      var _this = this;

      try {
        var departmentList = !localStorage.getItem("assignments_department") || localStorage.getItem("assignments_department") == "undefined" ? (localStorage.setItem("assignments_department", JSON.stringify(getCommonMaster("egov-common-masters", "departments", "Department").responseJSON["Department"] || [])), JSON.parse(localStorage.getItem("assignments_department"))) : JSON.parse(localStorage.getItem("assignments_department"));
      } catch (e) {
          console.log(e);
          var department = [];
      }


      //var cityGrade = !localStorage.getItem("city_grade") || localStorage.getItem("city_grade") == "undefined" ? (localStorage.setItem("city_grade", JSON.stringify(commonApiPost("tenant", "v1/tenant", "_search", {code: tenantId}).responseJSON["tenant"][0]["city"]["ulbGrade"] || {})), JSON.parse(localStorage.getItem("city_grade"))) : JSON.parse(localStorage.getItem("city_grade"));
      var agreementType = "Create Municipality Agreement";
      // if (cityGrade.toLowerCase() === 'corp') {
      //   agreementType = "Create Corporation Agreement";
      // }

      getDesignations(null, function(designations) {
          for (let variable in designations) {
              if (!designations[variable]["id"]) {
                  var _res = commonApiPost("hr-masters", "designations", "_search", { tenantId, name: designations[variable]["name"] });
                  designations[variable]["id"] = _res && _res.responseJSON && _res.responseJSON["Designation"] && _res.responseJSON["Designation"][0] ? _res.responseJSON["Designation"][0].id : "";
              }
          }

          _this.setState({
            ..._this.state,
            designationList : designations
          });

      },agreementType);


      var agreement = commonApiPost("lams-services",
                                "agreements",
                                "_search",
                                {
                                  id: getUrlVars()["agreementNumber"],
                                  tenantId
                                }).responseJSON["Agreements"][0] || {};
      console.log(agreement);

      if(!agreement.cancellation){
        agreement.cancellation={};
      }
      if(!agreement.workflowDetails){
        agreement.workflowDetails={};
      }


      this.setState({
        ...this.state,
        agreement : agreement,
        departmentList : departmentList
      });


      $('#orderDate').datepicker({
          format: 'dd/mm/yyyy',
          autoclose:true,
          defaultDate: ""
      });

      $('#orderDate').on('changeDate', function(e) {
            _this.setState({
                  agreement: {
                      ..._this.state.agreement,
                      cancellation:{
                        ..._this.state.agreement.cancellation,
                        "orderDate":$("#orderDate").val()
                      }
                  }
            });
      });


      $('#terminationDate').datepicker({
          format: 'dd/mm/yyyy',
          autoclose:true,
          defaultDate: ""
      });

      $('#terminationDate').on('changeDate', function(e) {
            _this.setState({
                  agreement: {
                      ..._this.state.agreement,
                      cancellation:{
                        ..._this.state.agreement.cancellation,
                        "terminationDate":$("#terminationDate").val()
                      }
                  }
            });
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
          success: function(res) {
            cb(null, res);
          },
          error: function(jqXHR, exception) {
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

    close() {
      // widow.close();
      open(location, '_self').close();
    }

    handleProcess(e) {
  
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

        const renderOptionForDistrict=function(list) {
            if(list)
            {
                return list.map((item, ind)=>
                {
                    return (<option key={ind} value={typeof item == "object" ? item.id : item}>
                            {typeof item == "object" ? item.city.name : item}
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



        const renderWorkflowDetails=function(status) {

          if(status === "Rejected"){
        return(
          <div>
          <br/>
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
                               onChange={(e)=>{  handleChange(e,"department") }} required >
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
                                onChange={(e)=>{  handleChange(e,"designation") }} required >
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
                              onChange={(e)=>{  handleChange(e,"assignee") }}required>
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



    const renderFileTr=function(status) {
      var CONST_API_GET_FILE = "/filestore/v1/files/id?tenantId=" + tenantId + "&fileStoreId=";

      for(var i=0; i<_this.state.movement.documents.length; i++) {
          return(<tr>
              <td>${i+1}</td>
              <td>Document</td>
              <td>
                  <a href={window.location.origin + CONST_API_GET_FILE + _this.state.movement.documents[i]} target="_blank">
                    Download
                  </a>
              </td>
          </tr>);
      }

    }

    const renderFile=function(status) {
      if(_this.state.movement && _this.state.movement.documents) {
      return(
        <table className="table table-bordered" id="fileTable" style={{"display": "none"}}>
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
        <form id = "update-transfer">
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
                                {renderOptionForDistrict(this.state.districtList)}
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
                              onChange={(e)=>{handleChange(e,"remarks")}} required></textarea>
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
                              <input id="documents" name="documents" type="file"
                                 onChange={(e)=>{handleChange(e,"documents")}} multiple/>
                                 {renderFile()}
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


            {renderWorkflowDetails(this.state.status)}


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
  <UpdateCancellation />,
  document.getElementById('root')
);
