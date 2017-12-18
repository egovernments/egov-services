class LeaveMaster extends React.Component{
constructor(props){
  super(props);
  this.state={list:[],leave:{
    "id": "",
    "designation": "",
    "leaveType":{
      "id" :""
    },
    "noOfDays":"",
    "active": "",
    "createdBy": "",
    "createdDate": "",
    "lastModifiedBy": "",
    "lastModifiedDate": "",
    "tenantId": tenantId
    },
        assignments_designation:[],leaveTypeList:[]}

  this.addOrUpdate = this.addOrUpdate.bind(this);
  this.handleChange = this.handleChange.bind(this);
  this.handleChangeThreeLevel = this.handleChangeThreeLevel.bind(this);
  this.setInitialState = this.setInitialState.bind(this);
}

setInitialState(initState) {
  this.setState(initState);
}

handleChange(e,name) {
  this.setState({
      leave:{
          ...this.state.leave,
          [name]:e.target.value
      }
  })
}
close(){
  open(location, '_self').close();
}

  addOrUpdate(e){
    e.preventDefault();
    var tempInfo=Object.assign({},this.state.leave) , type = getUrlVars()["type"];
    var body={
        "RequestInfo":requestInfo,
        "LeaveAllotment":[tempInfo]
      },_this=this;
      if (type == "update") {
        delete tempInfo.leaveType.name;
        delete tempInfo.leaveType.active;
        delete tempInfo.leaveType.description;
        delete tempInfo.leaveType.createdDate;
        delete tempInfo.leaveType.lastModifiedDate;
        delete tempInfo.leaveType.tenantId;
        $.ajax({
             url:baseUrl+"/hr-leave/leaveallotments/" + this.state.leave.id + "/" + "_update?tenantId=" + tenantId,
              type: 'POST',
              dataType: 'json',
              data:JSON.stringify(body),
              contentType: 'application/json',
              headers:{
                'auth-token': authToken
              },
              success: function(res) {
                showSuccess("Leave Mapping Modified successfully.");
                window.location.href = 'app/hr/common/show-leave-mapping.html?type=update';
              },
              error: function(err) {
                  showError(err["statusText"]);
              }
          });
      } else {
        $.ajax({
              url:baseUrl+"/hr-leave/leaveallotments/_create?tenantId=" + tenantId,
              type: 'POST',
              dataType: 'json',
              data:JSON.stringify(body),
              contentType: 'application/json',
              headers:{
                'auth-token': authToken
              },
              success: function(res) {
                showSuccess("Leave Mapping Created successfully.");
                _this.setState({
                  leave:{
                    "id": "",
                    "designation": "",
                    "leaveType":{
                      "id" :""
                    },
                    "noOfDays":"",
                    "active": "",
                    "tenantId": tenantId
                    }
                })
              },
              error: function(err) {
                console.log(err);
                if(err["0"]["Error"]["description"])
                  showError(err["0"]["Error"]["description"]);
                else
                  showError(err["statusText"]);
              }
          });
      }
}

handleChangeThreeLevel(e,pName,name) {
  this.setState({
    leave:{
      ...this.state.leave,
      [pName]:{
          ...this.state.leave[pName],
          [name]:e.target.value
      }
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

    if (getUrlVars()["type"]) $('#hp-citizen-title').text(titleCase(getUrlVars()["type"]) + " Leave Mapping");

    var _this = this, count = 2, _state = {};
    const checkCountAndCall = function(key, res) {
      count--;
      _state[key] = res;
      if(count == 0)
        _this.setInitialState(_state);
    }
    getDropdown("assignments_designation", function(res) {
      checkCountAndCall("assignments_designation", res);
    });
    getDropdown("leaveTypes", function(res) {
      checkCountAndCall("leaveTypeList", res);
    });

    var type = getUrlVars()["type"];
    var id = getUrlVars()["id"];

    if (getUrlVars()["type"] === "view") {
        $("input,select,radio,textarea").prop("disabled", true);
    }

    if (type === "view" || type === "update") {
      getCommonMasterById("hr-leave", "leaveallotments", id, function(err, res) {
        if(res) {
          _this.setState({
            leave: res.LeaveAllotment[0]
          })
        }
      })
    }
}


  render(){
    let {handleChange,addOrUpdate,handleChangeThreeLevel}=this;
    let {leaveType,designation,noOfDays}=this.state.leave;
    let mode=getUrlVars()["type"];

    const renderOption=function(list)
    {
        if(list)
        {
            return list.map((item)=>
            {
                return (<option key={item.id} value={item.id}>
                        {item.name}
                  </option>)
            })
        }
    }

    const showActionButton=function(){
      if((!mode)||mode==="update"){
        return (<button type="submit" className="btn btn-submit">{mode?"Update":"Add"}</button>);
      }
    }


      return(
      <div>
        <h3>{ getUrlVars()["type"] ? titleCase(getUrlVars()["type"]) :  "Create"} Leave Mapping</h3>
      <form onSubmit={(e)=>{addOrUpdate(e,mode)}}>
      <fieldset>
        <div className="row">
          <div className="col-sm-6">
              <div className="row">
                  <div className="col-sm-6 label-text">
                    <label for="">Designation </label>
                  </div>
                  <div className="col-sm-6">
                    <div className="styled-select">
                    <select id="designation" name="designation" value={designation} onChange={(e)=>{
                        handleChange(e,"designation")
                    }}>
                    <option>Select Designation</option>
                    {renderOption(this.state.assignments_designation)}
                   </select>
                    </div>
                  </div>
              </div>
            </div>
            <div className="col-sm-6">
                <div className="row">
                    <div className="col-sm-6 label-text">
                        <label for=""> Leave Type<span>*</span></label>
                    </div>
                    <div className="col-sm-6">
                    <div className="styled-select">
                    <select id="leaveType" name="leaveType" value={leaveType.id} required="true" onChange={(e)=>{
                        handleChangeThreeLevel(e,"leaveType","id")
                    }}>
                    <option value=""> Select Leave Type</option>
                    {renderOption(this.state.leaveTypeList)}
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
                          <label for="">No of day <span>*</span></label>
                      </div>
                      <div className="col-sm-6">
                      <input type="number" name="noOfDays" value={noOfDays} id="noOfDays" min="0" max = "180" onChange={(e)=>{
                          handleChange(e,"noOfDays")}} required/>

                      </div>
                  </div>
              </div>
              {/*
                <div className="col-sm-6">
                  <div className="row">
                    <div className="col-sm-6 label-text">
                        <label for="">Active</label>
                    </div>
                        <div className="col-sm-6">
                              <label className="radioUi">
                                <input type="checkbox" name="active" id="active" value="true" onChange={(e)=>{
                                    handleChange(e,"active")}}required/>
                              </label>
                        </div>
                    </div>
                  </div>*/}
            </div>

    <div className="text-center">
        {showActionButton()} &nbsp;&nbsp;
        <button type="button" className="btn btn-close" onClick={(e)=>{this.close()}}>Close</button>

    </div>
    </fieldset>
    </form>
</div>
      );
  }
}
ReactDOM.render(
  <LeaveMaster />,
  document.getElementById('root')
);
