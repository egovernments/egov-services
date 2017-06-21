var flag = 0;
class EmployeeTransfer extends React.Component {
  constructor(props) {
    super(props);
    this.state={
  transferSet:{
    "employeeid": {
      id:"",
      name:"",
      code:"",
      departmentId:"",
      designationId:"",
      positionId:""
    },
    departmentId:"",
    designationId:"",
    employeeType:"",
    name:"",
    tranferType:"",
    promotionType:"",
    reason:"",
    district:"",
    positionId:"",
    fund:"",
    accepted:false,
    effectiveDate:"",
    functionary:"",
    promotionDate:"",
    remark:"",
    documents:""},
    positionList:[],departmentList:[],designationList:[],employees:[],promotionList:[],
    fundList:[],functionaryList:[],districtList:[],transferList:[],reasonList:[],pNameList:[]
  }
    this.handleChange=this.handleChange.bind(this);
    this.addOrUpdate=this.addOrUpdate.bind(this);
    this.handleChangeTwoLevel=this.handleChangeTwoLevel.bind(this);
    this.setInitialState = this.setInitialState.bind(this);
    this.vacantPositionFun = this.vacantPositionFun.bind(this);
  }

addOrUpdate(){
  console.log("created");
}

setInitialState(initState) {
  this.setState(initState);
}

componentWillUpdate() {
  if(flag == 1) {
    flag = 0;
    $('#employeeTable').dataTable().fnDestroy();
  }
}

  handleChangeTwoLevel() {
    this.setState({
        transferSet:{
            ...this.state.transferSet,
            [pName]:{
                ...this.state.transferSet[pName],
                [name]:e.target.value
            }
        }
    })
  }

  componentDidUpdate(prevProps, prevState) {
      if (prevState.employees.length!=this.state.employees.length) {
          $('#employeeTable').DataTable({
            dom: 'Bfrtip',
            buttons: [
                     'copy', 'csv', 'excel', 'pdf', 'print'
             ],
             ordering: false,
             bDestroy: true,
             language: {
               "emptyTable": "No Records"
            }
          });
      }
  }
  componentDidMount() {
    var type = getUrlVars()["type"], _this = this, id = getUrlVars()["id"];

    if(window.opener && window.opener.document) {
       var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
       if(logo_ele && logo_ele[0]) {
         document.getElementsByClassName("homepage_logo")[0].src = window.location.origin + logo_ele[0].getAttribute("src");
       }
    }
    $('#hp-citizen-title').text(titleCase(getUrlVars()["type"]) + " Employee Transfer");

    var _state = {}, _this = this, count = 5;
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


    // $('#code,#name,#departmentId,#designationId').prop("disabled", true);
    $('#effectiveDate, #promotionDate').datepicker({
        format: 'dd/mm/yyyy',
        autoclose:true,
        defaultDate: ""
    });
    $('#effectiveDate').val("");
    $('#promotionDate').val("");
    $('#effectiveDate,#promotionDate ').on('changeDate', function(e) {
          _this.setState({
                transferSet: {
                    ..._this.state.transferSet,
                    "effectiveDate":$("#effectiveDate").val(),
                    "promotionDate":$("#promotionDate").val()
                }
          });
      if(_this.state.transferSet.designationId&&_this.state.transferSet.departmentId){
        var _designation = _this.state.transferSet.designationId;
        var _department = _this.state.transferSet.departmentId;
        var _promotionDate = _this.state.transferSet.promotionDate;
          _this.vacantPositionFun(_department,_designation,_promotionDate);
      }
    });

    getCommonMasterById("hr-employee","employees", id, function(err, res) {
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
          transferSet:{
            ..._this.state.transferSet,
            employeeid: {
              name: obj.name,
              code: obj.code,
              departmentId:obj.assignments[ind].department,
              designationId:obj.assignments[ind].designation,
              positionId:obj.assignments[ind].position
            }
          }
        })
      }
    });
  }

  vacantPositionFun(departmentId,designationId,promotionDate){
    var _this = this;
    commonApiPost("hr-masters", "vacantpositions", "_search", {
        tenantId,
        departmentId: departmentId,
        designationId: designationId,
        asOnDate: promotionDate
    }, function(err, res) {
        if (res) {
          _this.setState({
              transferSet:{
                  ..._this.state.transferSet,
              },pNameList:res.Position
          })
        }
    });

  }

  handleChange(e,name){
    var _this = this;
    switch (name) {
      case "designationId":
        if(this.state.transferSet.departmentId&&this.state.transferSet.promotionDate){
          var _department = this.state.transferSet.departmentId;
          var _date = this.state.transferSet.promotionDate;
          _this.vacantPositionFun(_department,e.target.value,_date);
        }
        break;
        case "departmentId":
        if(this.state.transferSet.designationId&&this.state.transferSet.promotionDate){
          var _designation = this.state.transferSet.designationId;
          var _date = this.state.transferSet.promotionDate;
            _this.vacantPositionFun(e.target.value,_designation,_date);
        }
          break;
    }

    if(name === "accepted") {
      this.setState({
          transferSet:{
              ...this.state.transferSet,
              accepted: !this.state.transferSet.accepted
          }
      })
  } else {
    this.setState({
        transferSet:{
            ...this.state.transferSet,
            [name]:e.target.value

        }
    })
    }
  }


  close() {
      open(location, '_self').close();
  }

  render() {
    let {handleChange,addOrUpdate,handleChangeTwoLevel}=this;
    let _this = this;
    let{ code,departmentId,designationId,name,tranferType,reason,district,positionId,
        fund,functionary,promotionDate,remark ,promotionType,documents,employeeid,accepted,effectiveDate}=this.state.transferSet;
    let {isSearchClicked,employees,assignments_designation,assignments_department,assignments_position}=this.state;

    const renderOption=function(list) {
        if(list)
        {
            return list.map((item, ind)=>
            {
                return (<option key={ind} value={typeof item == "object" ? item.id : item}>
                        {typeof item == "object" ? item.name : item}
                  </option>)
            })
        }
    }
    const promotionFunc=function() {
      if(accepted=="true"||accepted==true){
        return(<div className="row">
          <div className="col-sm-6">
              <div className="row">
                  <div className="col-sm-6 label-text">
                    <label htmlFor="">Enquiry passed Date</label>
                  </div>
                  <div className="col-sm-6">
                  <div className="text-no-ui">
                  <span><i className="glyphicon glyphicon-calendar"></i></span>
                  <input type="text" id="effectiveDate" name="effectiveDate" value="effectiveDate" value={effectiveDate}
                  onChange={(e)=>{handleChange(e,"effectiveDate")}} />

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
                    <select id="promotionType" name="promotionType" value={promotionType}
                    onChange={(e)=>{handleChange(e,"promotionType")}} required>
                      <option value="">Select Promotion Basis</option>
                      {renderOption(_this.state.promotionList)}
                   </select>
                   </div>
                </div>
            </div>
          </div>
      </div>);
      }
    };


    return (
      <div>
        <form onSubmit={(e)=> {addOrUpdate(e)}}>
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
                          <input type="text" name="code" id="code" value={employeeid.code}
                          onChange={(e)=>{handleChange(e,"employeeid","code")}} disabled />
                      </div>
                  </div>
                </div>
                <div className="col-sm-6">
                    <div className="row">
                        <div className="col-sm-6 label-text">
                          <label htmlFor=""> Employee Name  </label>
                        </div>
                        <div className="col-sm-6">
                            <input type="text" name="name" id="name" value= {employeeid.name}
                                onChange={(e)=>{handleChangeTwoLevel(e,"employeeid","name")}} disabled/>
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
                        <select name="departmentId" value={employeeid.departmentId}
                          onChange={(e)=>{  handleChangeTwoLevel(e,"employeeid","departmentId")}} disabled>
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
                            <select name="designationId" value={employeeid.designationId}
                              onChange={(e)=>{ handleChange(e,"employeeid","designationId")}} disabled>
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
                            <select name="positionId" value={employeeid.positionId}
                              onChange={(e)=>{ handleChange(e,"employeeid","positionId")}} disabled >
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
                          <select id="tranferType" name="tranferType" value={tranferType}
                          onChange={(e)=>{handleChange(e,"tranferType")}}required>
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
                              <select id="reason" name="reason" value={reason}
                                onChange={(e)=>{  handleChange(e,"reason")}}required>
                              <option value="">Select Reason</option>
                              {renderOption(this.state.reasonList)}
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
                              <select id="district" name="district" value={district}
                                onChange={(e)=>{  handleChange(e,"district") }}required>
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
                          <label htmlFor="accepted">Transfer with Promotion</label>
                      </div>
                          <div className="col-sm-6">
                                <label className="radioUi">
                                  <input type="checkbox" name="accepted" value="accepted" checked={accepted == "true" || accepted  ==  true}
                                   onChange={(e)=>{ handleChange(e,"accepted")}}/>
                                </label>
                          </div>
                      </div>
                    </div>
                </div>
              {  promotionFunc()}
                <div className="row">
                  <div className="col-sm-6">
                    <div className="row">
                        <div className="col-sm-6 label-text">
                          <label htmlFor="">Department <span>*</span></label>
                        </div>
                        <div className="col-sm-6">
                          <div className="styled-select">
                            <select id="department" name="departmentId" value={departmentId}
                              onChange={(e)=>{  handleChange(e,"departmentId")}}required>
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
                                <select id="designation" name="designationId" value={designationId}
                                    onChange={(e)=>{handleChange(e,"designationId")}}required>
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
                              <input type="text" id="promotionDate" name="promotionDate" value="promotionDate" value={promotionDate}
                              onChange={(e)=>{handleChange(e,"promotionDate")}} required/>
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
                                  <select id="positionId" name="positionId" value={positionId}
                                    onChange={(e)=>{  handleChange(e,"positionId")}}required>
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
                                <select id="fund" name="fund" value={fund}
                                    onChange={(e)=>{handleChange(e,"fund")}}>
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
                                    <select id="functionary" name="functionary" value={functionary}
                                      onChange={(e)=>{  handleChange(e,"functionary") }}>
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
                              <textarea rows="4" cols="50" id="remark" name="remark" value={remark}
                              onChange={(e)=>{handleChange(e,"remark")}}required></textarea>
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
                              <input id="documents" name="documents" type="file" required multiple/>
                             </div>
                          </div>
                      </div>
                  </div>
              </div>
              {/*<div className="row">
                <div className="col-sm-6">
                    <div className="row">
                        <div className="col-sm-6 label-text">
                          <label htmlFor="">Transfer Accepted by Employee Y/N</label>
                        </div>
                        <div className="col-sm-6">
                          <div className="styled-select">
                              <select id="accepted" name="accepted" value={accepted}
                                onChange={(e)=>{  handleChange(e,"accepted") }}>
                              <option value="">Select Promotion</option>
                              <option>Yes</option>
                              <option>No</option>
                             </select>
                        </div>
                      </div>
                    </div>
                  </div>
              </div>*/}
            </div>
          <br/>
            <div className="text-center">
              <button id="sub" type="submit"  className="btn btn-submit">Create</button>&nbsp;&nbsp;
              <button type="button" className="btn btn-close" onClick={(e)=>{this.close()}}>Close</button>
            </div>
          </form>
      </div>
    );
  }
}






ReactDOM.render(
  <EmployeeTransfer />,
  document.getElementById('root')
);
