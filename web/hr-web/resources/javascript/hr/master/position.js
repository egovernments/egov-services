class PositionMaster extends React.Component{
  constructor(props){
    super(props);
    this.state={list:[],positionSet:{
    "id": "",
    "name": "",
    "deptdesig": {
        "id": "",
        "department": "",
        "designation": {
                        "id": "",
                        "name": "",
                        "code": "",
                        "description": "",
                        "chartOfAccounts": null,
                        "tenantId": ""
                    }
                  },
    "isPostOutsourced": "false",
    "active": "true",
    "tenantId":tenantId
  },
      departmentsList:[],designationList:[]
}
      this.handleChange=this.handleChange.bind(this);
      this.addOrUpdate=this.addOrUpdate.bind(this);
      this.handleChangeThreeLevel=this.handleChangeThreeLevel.bind(this);
      this.handleChangeTwoLevel=this.handleChangeTwoLevel.bind(this);
      this.setInitialState = this.setInitialState.bind(this);
}

handleChange(e,name) {
    if(name === "active"){
    this.setState({
      positionSet:{
          ...this.state.positionSet,
          active: !this.state.positionSet.active

      }
    })
  }else{
    this.setState({
      positionSet:{
        ...this.state.positionSet,
        [name]:e.target.value
      }
    })
  }
}

setInitialState(initState) {
  this.setState(initState);
}

handleChangeTwoLevel(e,pName,name) {
  this.setState({
    positionSet:{
      ...this.state.positionSet,
      [pName]:{
          ...this.state.positionSet[pName],
          [name]:e.target.value
      }
    }
  })
}

handleChangeThreeLevel(e,pName,name,val)
{
  this.setState({
    positionSet:{
      ...this.state.positionSet,
      [pName]:{
          ...this.state.positionSet[pName],

          [name]:{
              ...this.state.positionSet[name],
                  [val]:e.target.value
        }
      }
    }
  })
}

close(){
    // widow.close();
    open(location, '_self').close();
}

componentDidMount() {
  var type=getUrlVars()["type"];
  var id=getUrlVars()["id"], _this=this;
  if(window.opener && window.opener.document) {
     var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
     if(logo_ele && logo_ele[0]) {
       document.getElementsByClassName("homepage_logo")[0].src = window.location.origin + logo_ele[0].getAttribute("src");
     }
   }

  if(getUrlVars()["type"]) $('#hp-citizen-title').text(titleCase(getUrlVars()["type"]) + " Position");

  if(getUrlVars()["type"]==="view") {
      $("input,select").prop("disabled", true);
  }

  var count = 2, _this = this, _state = {};
  var checkCountNCall = function(key, res) {
    count--;
    _state[key] = res;
    if(count == 0)
      _this.setInitialState(_state);
  }
  getDropdown("assignments_department", function(res) {
    checkCountNCall("departmentsList", res);
  });
  getDropdown("assignments_designation", function(res) {
    checkCountNCall("designationList", res);
  });

  if(type==="view"||type==="update") {
    getCommonMasterById("hr-masters","positions",id, function(err, res) {
      if(res) {
           var positionSet = res["Position"][0];
          _this.setState({
            positionSet
          })
        }
    })
  }
}


addOrUpdate(e){
  e.preventDefault();
  var tempInfo=Object.assign({},this.state.positionSet) , type = getUrlVars()["type"];
  var body={
      "RequestInfo":requestInfo,
      "Position":[tempInfo]
    },_this=this;
  if (type == "update") {
        $.ajax({
           url:baseUrl+"/hr-masters/positions/" + this.state.positionSet.id + "/" + "_update?tenantId=" + tenantId,
            type: 'POST',
            dataType: 'json',
            data:JSON.stringify(body),
            contentType: 'application/json',
            headers:{
              'auth-token': authToken
            },
            success: function(res) {
                    showSuccess("Position Modified successfully.");
                    window.location.href = 'app/hr/common/show-position.html?type=update';
                    localStorage.removeItem("assignments_position");


            },
            error: function(err) {
                showError("Duplicate position are not allowed");

            }
        });

    } else {
      $.ajax({
            url:baseUrl+"/hr-masters/positions/_create?tenantId=" + tenantId,
            type: 'POST',
            dataType: 'json',
            data:JSON.stringify(body),
            contentType: 'application/json',
            headers:{
              'auth-token': authToken
            },
            success: function(res) {
                    showSuccess("Position Created successfully.");
                    localStorage.removeItem("assignments_position");
                    _this.setState({positionSet:{
                        "id": "",
                        "name": "",
                        "deptdesig": {
                        "id": "",
                          "department": "",
                          "designation": {
                            "id": "",
                            "tenantId": null
                            }
                          },
                        "isPostOutsourced": "false",
                        "active": "true",
                        "tenantId": tenantId
                      }})

            },
            error: function(err) {
                showError(err);

          }
      });
  }
}


render(){
    let {handleChange,addOrUpdate,handleChangeTwoLevel,handleChangeThreeLevel}=this;
    let {department,designation,name,isPostOutsourced,deptdesig,active}=this.state.positionSet;
    let mode =getUrlVars()["type"];

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

    const showActionButton=function() {
      if((!mode) ||mode==="update")
      {
        return (<button type="submit" className="btn btn-submit">{mode?"Update":"Add"}</button>);
      }
    };


  return (<div>
        <h3>{ getUrlVars()["type"] ? titleCase(getUrlVars()["type"]) : "Create"} Position</h3>
        <form  onSubmit={(e)=>{addOrUpdate(e,mode)}}>
        <fieldset>
        <div className="row">
          <div className="col-sm-6">
              <div className="row">
                  <div className="col-sm-6 label-text">
                    <label for="">Department  </label>
                  </div>
                  <div className="col-sm-6">
                  <div className="styled-select">
                      <select id="department" name="department" value={deptdesig.department} onChange={(e)=>{
                          handleChangeTwoLevel(e,"deptdesig","department")
                      }}>
                        <option>Select Department</option>
                        {renderOption(this.state.departmentsList)}
                     </select>
                  </div>
                  </div>
              </div>
            </div>
            <div className="col-sm-6">
                <div className="row">
                    <div className="col-sm-6 label-text">
                      <label for="">Designation  </label>
                    </div>
                    <div className="col-sm-6">
                    <div className="styled-select">
                        <select id="designation" name="designation" value={deptdesig.designation.id} onChange={(e)=>{
                            handleChangeThreeLevel(e,"deptdesig","designation","id")
                        }}>
                        <option>Select Designation</option>
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
                      <label for="">Name <span>*</span></label>
                  </div>
                  <div className="col-sm-6">
                  <input type="text" name="name" value={name} id= "name" onChange={(e)=>{
                      handleChange(e,"name")}}required/>
                  </div>
              </div>
            </div>
          <div className="col-sm-6">
              <div className="row">
                  <div className="col-sm-6 label-text">
                      <label for="">Outsourced post </label>
                  </div>
                  <div className="col-sm-6">
                        <label className="radio-inline radioUi">
                          <input type="radio" name="isPostOutsourced" value="true" checked={isPostOutsourced == "true" || isPostOutsourced  ==  true }
                            onChange={(e)=>{ handleChange(e,"isPostOutsourced")}}/> Yes

                        </label>
                        <label className="radio-inline radioUi">
                          <input type="radio" name="isPostOutsourced" value="false" checked={isPostOutsourced == "false" || !isPostOutsourced}
                              onChange={(e)=>{handleChange(e,"isPostOutsourced")}}/> No
                        </label>
                  </div>
              </div>
            </div>
        </div>
          <div className="row">
            <div className="col-sm-6">
              <div className="row">
                <div className="col-sm-6 label-text">
                    <label for="">Active</label>
                </div>
                    <div className="col-sm-6">
                          <label className="radioUi">
                            <input type="checkbox" name="active" id="active" value="true" onChange={(e)=>{
                                handleChange(e,"active")}} checked={active == "true" || active  ==  true }/>
                          </label>
                    </div>
                </div>
              </div>
            </div>
            <div className="text-center">
            {showActionButton()} &nbsp;&nbsp;
            <button type="button" className="btn btn-close" onClick={(e)=>{this.close()}}>Close</button>

        </div>
        </fieldset>
        </form>
      </div>);

    }
  }
  ReactDOM.render(
    <PositionMaster/>,
    document.getElementById('root')
  );
