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
      departmentsList:[],designationList:[]}
      this.handleChange=this.handleChange.bind(this);
      this.addOrUpdate=this.addOrUpdate.bind(this);
      this.handleChangeThreeLevel=this.handleChangeThreeLevel.bind(this);
      this.handleChangeTwoLevel=this.handleChangeTwoLevel.bind(this);

}
    componentWillMount()
    {
      try {
        var assignments_designation = !localStorage.getItem("assignments_designation") || localStorage.getItem("assignments_designation") == "undefined" ? (localStorage.setItem("assignments_designation", JSON.stringify(getCommonMaster("hr-masters", "designations", "Designation").responseJSON["Designation"] || [])), JSON.parse(localStorage.getItem("assignments_designation"))) : JSON.parse(localStorage.getItem("assignments_designation"));
      } catch (e) {
          console.log(e);
           var assignments_designation = [];
      }
      try {
        var assignments_department = !localStorage.getItem("assignments_department") || localStorage.getItem("assignments_department") == "undefined" ? (localStorage.setItem("assignments_department", JSON.stringify(getCommonMaster("egov-common-masters", "departments", "Department").responseJSON["Department"] || [])), JSON.parse(localStorage.getItem("assignments_department"))) : JSON.parse(localStorage.getItem("assignments_department"));
      } catch (e) {
          console.log(e);
        var  assignments_department = [];
      }
      this.setState({
        departmentsList: assignments_department,
        designationList: assignments_designation
    })
    }


    handleChange(e,name){
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



  handleChangeTwoLevel(e,pName,name)
  {
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



    componentDidMount(){
      if(window.opener && window.opener.document) {
         var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
         if(logo_ele && logo_ele[0]) {
           document.getElementsByClassName("homepage_logo")[0].src = window.location.origin + logo_ele[0].getAttribute("src");
         }
       }
       if(getUrlVars()["type"]) $('#hp-citizen-title').text(titleCase(getUrlVars()["type"]) + " Position");

      var type=getUrlVars()["type"];
      var id=getUrlVars()["id"];

      if(getUrlVars()["type"]==="view")
      {
          $("input,select").prop("disabled", true);
        }

        if(type==="view"||type==="update")
        {
            this.setState({
              positionSet:getCommonMasterById("hr-masters","positions","Position",id).responseJSON["Position"][0]
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
            async: false,
            contentType: 'application/json',
            headers:{
              'auth-token': authToken
            },
            success: function(res) {
                    showSuccess("Position Modified successfully.");
                    window.location.href = 'app/hr/common/show-position.html?type=update';


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
            async: false,
            contentType: 'application/json',
            headers:{
              'auth-token': authToken
            },
            success: function(res) {
                    showSuccess("Position Created successfully.");
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
                      },designationList:[],departmentsList:[]})

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
