class CompensetoryLeave extends React.Component {
  constructor(props) {
    super(props);
    this.state={
      employees:[],
      compensetorySet:{
      workedOn:"",
      compensatoryForDate:"",
      name:"",
      code:""}
    }
    this.handleChange=this.handleChange.bind(this);
    this.addOrUpdate=this.addOrUpdate.bind(this);
  }


  handleChange(e, name) {
    this.setState({
        compensetorySet:{
            ...this.state.compensetorySet,
            [name]:e.target.value
        }
    })
  }

  close() {
      open(location, '_self').close();
  }

addOrUpdate(e,mode) {
  e.preventDefault();
  var employee;
  var asOnDate = this.state.compensetorySet.toDate;
  var departmentId = this.state.departmentId;
  var tempInfo=Object.assign({},this.state.compensetorySet) , type = getUrlVars()["type"];
  delete  tempInfo.name;
  delete tempInfo.code;
  var body={
      "RequestInfo":requestInfo,
      "LeaveApplication":tempInfo
    },_this=this;
      if(type == "Update") {
        $.ajax({
              url:baseUrl+"/hr-leave/leaveapplications/" + this.state.compensetorySet.id + "/" + "_update?tenantId=" + tenantId,
              type: 'POST',
              dataType: 'json',
              data:JSON.stringify(body),
              contentType: 'application/json',
              headers:{
                'auth-token': authToken
              },
              success: function(res) {
                showSuccess("Leave Application Modified successfully.");
                _this.setState({
                  compensetorySet:{
                  workedOn:"",
                  compensatoryForDate:"",
                  name:"",
                  code:""}
                })
              },
              error: function(err) {
                showError(err["statusText"]);
              }
          });
      }
      else{
        $.ajax({
              url: baseUrl+"/hr-leave/leaveapplications/_create?tenantId=" + tenantId,
              type: 'POST',
              dataType: 'json',
              data:JSON.stringify(body),
              contentType: 'application/json',
              headers:{
                'auth-token': authToken
              },
              success: function(res) {
                showSuccess("Leave Application Created successfully.");
                _this.setState({
                  compensetorySet:{
                  workedOn:"",
                  compensatoryForDate:"",
                  name:"",
                  code:""}
                })
              },
              error: function(err) {
                showError(err["statusText"]);
              }
          });
      }
}


  componentDidMount() {
    if(window.opener && window.opener.document) {
       var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
       if(logo_ele && logo_ele[0]) {
         document.getElementsByClassName("homepage_logo")[0].src = (logo_ele[0].getAttribute("src") && logo_ele[0].getAttribute("src").indexOf("http") > -1) ? logo_ele[0].getAttribute("src") : window.location.origin + logo_ele[0].getAttribute("src");
     }

    var type = getUrlVars()["type"], _this = this, id = getUrlVars()["id"];
    $('#compensatoryForDate').datepicker({
        format: 'dd/mm/yyyy',
        autclose:true

    });

    $('#compensatoryForDate').on("change", function(e) {
      _this.setState({
            compensetorySet: {
                ..._this.state.compensetorySet,
                "compensatoryForDate":$("#compensatoryForDate").val()
            }
      })
    });

    if (!id) {
      commonApiPost("hr-employee","employees","_loggedinemployee",{tenantId}, function(err, res) {
        if(res) {
          var obj = res.Employee[0];
          _this.setState({
            compensetorySet:{
                ..._this.state.compensetorySet,
                name:obj.name,
                code:obj.code,
                employee:obj.id,
              }
          })
        }
      })
    } else {
      getCommonMasterById("hr-employee", "employees", id, function(err, res) {
        if(res) {
          var obj = res.Employee[0];
          _this.setState({
            compensetorySet:{
                ..._this.state.compensetorySet,
                name:obj.name,
                code:obj.code,
                employee:obj.id,
              }
          })
        }
      });
    }
  }

  render() {
      let {handleChange,addOrUpdate}=this;
      let {code,name,workedOn,compensatoryForDate}=this.state.compensetorySet;
      let mode=getUrlVars()["type"];

      const showActionButton=function() {
        if((!mode) ||mode==="create")
        {
          return (<button type="submit" className="btn btn-submit">{mode?"forward":"Apply"}</button>);
        }
      };
    return (
      <div>
          <h3>{mode} Compensetory Leave</h3>
            <form onSubmit={(e)=>{addOrUpdate(e)}}>
          <fieldset>
              <div className="row">
                <div className="col-sm-6">
                    <div className="row">
                        <div className="col-sm-6 label-text">
                          <label for="">Employee Code </label>
                        </div>
                        <div className="col-sm-6">
                            <input type="text" id="code" name="code" value={code}
                              onChange={(e)=>{  handleChange(e,"code")}}/>
                        </div>
                    </div>
                  </div>
                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                            <label for="">Employee Name</label>
                          </div>
                          <div className="col-sm-6">
                              <input  type="text" id="name" name="name" value={name}
                              onChange={(e)=>{  handleChange(e,"name")}}/>

                            </div>
                          </div>
                        </div>
                    </div>



                <div className="row">
                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                            <label for="">Worked On  </label>
                          </div>
                          <div className="col-sm-6">
                          <input  type="text" id="workedOn" name="workedOn" value={workedOn}
                          onChange={(e)=>{  handleChange(e,"workedOn")}}/>
                      </div>
                    </div>
                    </div>
                    <div className="col-sm-6">
                        <div className="row">
                            <div className="col-sm-6 label-text">
                              <label for="">Compensetory Leave On  </label>
                            </div>
                            <div className="col-sm-6">
                            <div className="text-no-ui">
                          <span><i className="glyphicon glyphicon-calendar"></i></span>
                          <input type="text" id="compensatoryForDate" name="compensatoryForDate" value={compensatoryForDate}
                          onChange={(e)=>{  handleChange(e,"compensatoryForDate")}}/>

                          </div>
                          </div>
                        </div>
                      </div>
                </div>





            <div className="text-center">
                 <button type="submit"  className="btn btn-submit">Forward</button> &nbsp;&nbsp;
                <button type="button" className="btn btn-close" onClick={(e)=>{this.close()}}>Close</button>

            </div>
          </fieldset>
          </form>
      </div>
    );
  }
}




ReactDOM.render(
  <CompensetoryLeave />,
  document.getElementById('root')
);
