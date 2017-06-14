var flag = 0;
class EmployeePromotion extends React.Component {
  constructor(props) {
    super(props);
    this.state={
  promotionSet:{
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
    promotionType:"",
    reason:"",
    district:"",
    positionId:"",
    fund:"",
    functionary:"",
    effectiveDate:"",
    remark:"",
    documents:"",
    promotionDate:"",
    accepted:""},
    positionList:[],departmentList:[],designationList:[],employees:[],
    fundList:[],functionaryList:[],districtList:[],promotionList:[],reasonList:[]
  }
    this.handleChange=this.handleChange.bind(this);
    this.addOrUpdate=this.addOrUpdate.bind(this);
    this.handleChangeTwoLevel=this.handleChangeTwoLevel.bind(this);
    this.setInitialState = this.setInitialState.bind(this);
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
        promotionSet:{
            ...this.state.promotionSet,
            [pName]:{
                ...this.state.promotionSet[pName],
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
    $('#hp-citizen-title').text(titleCase(getUrlVars()["type"]) + " Employee");

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


    $('#code,#name,#departmentId,#designationId').prop("disabled", true);
    $('#effectiveDate, #promotionDate').datepicker({
        format: 'dd/mm/yyyy',
        autoclose:true,
        defaultDate: ""
    });
    $('#effectiveDate').val("");
    $('#promotionDate').val("");
    $('#effectiveDate,#promotionDate ').on("change", function(e) {
          _this.setState({
                promotionSet: {
                    ..._this.state.promotionSet,
                    "effectiveDate":$("#effectiveDate").val(),
                    "promotionDate":$("#promotionDate").val()
                }
          })
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
          promotionSet:{
            ..._this.state.promotionSet,
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

  handleChange(e,name) {
      this.setState({
          promotionSet:{
              ...this.state.promotionSet,
              [name]:e.target.value
          }
      })

  }

  close() {
      open(location, '_self').close();
  }

  render() {
    let {handleChange,addOrUpdate,handleChangeTwoLevel}=this;
    let{ code,departmentId,designationId,name,promotionType,reason,district,positionId,
        fund,functionary,effectiveDate,promotionDate,remark ,documents,employeeid,accepted}=this.state.promotionSet;
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
                          onChange={(e)=>{handleChange(e,"employeeid","code")}}/>
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
                                onChange={(e)=>{handleChangeTwoLevel(e,"employeeid","name")}}/>
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
                        <select id="departmentId" name="departmentId" value={employeeid.departmentId}
                          onChange={(e)=>{  handleChangeTwoLevel(e,"employeeid","departmentId")}}>
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
                            <select id="designationId" name="designationId" value={employeeid.designationId}
                              onChange={(e)=>{ handleChange(e,"employeeid","designationId")}} >
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
                            <select id="positionId" name="positionId" value={employeeid.positionId}
                              onChange={(e)=>{ handleChange(e,"employeeid","positionId")}} >
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
                    <h3 className="categoryType">Promotion Details </h3>
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
                          <select id="promotionType" name="promotionType" value={promotionType}
                          onChange={(e)=>{handleChange(e,"promotionType")}} required>
                            <option value="">Select Promotion Basis</option>
                            {renderOption(this.state.promotionList)}
                         </select>
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
                          <input type="text" name="name" id="name" value= {name}
                              onChange={(e)=>{handleChange(e,"name")}}/>
                      </div>
                    </div>
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
                                    onChange={(e)=>{handleChange(e,"designationId")}} required>
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
                                <label htmlFor="">Promotion effective from<span>*</span></label>
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
                          <label htmlFor="">Promotion Accepted by Employee Y/N</label>
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
                </div>
                <div className="row">
                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                              <label htmlFor="documents">Attachments docs<span>*</span></label>
                          </div>
                          <div className="col-sm-6">
                              <div className="styled-file">
                              <input id="documents" name="documents" type="file" required multiple/>
                             </div>
                          </div>
                      </div>
                  </div>
                </div>
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
  <EmployeePromotion />,
  document.getElementById('root')
);
