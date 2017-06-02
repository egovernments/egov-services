class Designation extends React.Component{
  constructor(props){
    super(props);
    this.state={
      designationSet:{
        name:"",
        code:"",
        description:"",
        chartOfAccounts:null,
        active:"true",
        tenantId:tenantId
      }
    }
      this.handleChange=this.handleChange.bind(this);
      this.addOrUpdate=this.addOrUpdate.bind(this);
    }

handleChange(e,name){
  if(name === "active") {
    this.setState({
      designationSet:{
          ...this.state.designationSet,
          active: !this.state.designationSet.active

      }
    })
} else {
    this.setState({
      designationSet:{
        ...this.state.designationSet,
        [name]:e.target.value
      }
    })
  }
}
    close(){
        // widow.close();
        open(location, '_self').close();
    }


componentDidMount() {
  var type=getUrlVars()["type"], _this=this;
  var id=getUrlVars()["id"];

  if(window.opener && window.opener.document) {
     var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
     if(logo_ele && logo_ele[0]) {
       document.getElementsByClassName("homepage_logo")[0].src = window.location.origin + logo_ele[0].getAttribute("src");
     }
   }
  if(getUrlVars()["type"]) $('#hp-citizen-title').text(titleCase(getUrlVars()["type"]) + " Designation");

  if(getUrlVars()["type"]==="view") {
      $("input,select,textarea").prop("disabled", true);
    }
  if(type==="view"||type==="update") {
    getCommonMasterById("hr-masters","designations",id,function(err, res) {
      if(res) {
           var designationSet = res["Designation"][0];
          _this.setState({
            designationSet
          })
        }
    })
  }
}

addOrUpdate(e) {
    e.preventDefault();
    var tempInfo=Object.assign({},this.state.designationSet) , type = getUrlVars()["type"];
    var body={
      "RequestInfo":requestInfo,
      "Designation":tempInfo
    },_this=this;

    if (type == "update") {
        $.ajax({
           url:baseUrl+"/hr-masters/designations/" + this.state.designationSet.id + "/" + "_update?tenantId=" + tenantId,
            type: 'POST',
            dataType: 'json',
            data:JSON.stringify(body),
            contentType: 'application/json',
            headers:{
              'auth-token': authToken
            },
            success: function(res) {
                  showSuccess("Designation Modified successfully.");
                  window.location.href = 'app/hr/common/show-designation.html?type=update';
                  localStorage.removeItem("assignments_designation");
            },
            error: function(err) {
                showError("Duplicate Designation are not allowed");
            }
        });
    } else {
        $.ajax({
            url:baseUrl+"/hr-masters/designations/_create?tenantId=" + tenantId,
            type: 'POST',
            dataType: 'json',
            data:JSON.stringify(body),
            contentType: 'application/json',
            headers:{
              'auth-token': authToken
            },
            success: function(res) {
                showSuccess("Designation Created successfully.");
                localStorage.removeItem("assignments_designation");
                _this.setState({designationSet:{
                name:"",
                code:"",
                description:"",
                active:"true",
                "tenantId": tenantId},
              })
            },
            error: function(err) {
                showError(err);
            }
        });
      }
    }



render(){
    let {handleChange,addOrUpdate}=this;
    let mode=getUrlVars()["type"];
    let {name,code,description,chartOfAccounts,active}=this.state.designationSet;

    const showActionButton=function() {
      if((!mode) ||mode==="update")
      {
        return (<button type="submit" className="btn btn-submit">{mode?"Update":"Add"}</button>);
      }
    };


  return(
    <div>
      <h3>{ getUrlVars()["type"] ? titleCase(getUrlVars()["type"]) : "Create"} Designation</h3>
      <form  onSubmit={(e)=>{addOrUpdate(e,mode)}}>
      <fieldset>
      <div className="row">
        <div className="col-sm-6">
            <div className="row">
                <div className="col-sm-6 label-text">
                    <label for="">Name<span>*</span></label>
                </div>
                <div className="col-sm-6">
                  <input type="text" name="name" value={name} id="name" onChange={(e)=>{
                      handleChange(e,"name")
                  }} required/>
                </div>
            </div>
          </div>
          <div className="col-sm-6">
              <div className="row">
                  <div className="col-sm-6 label-text">
                      <label for="">Code <span>*</span></label>
                  </div>
                  <div className="col-sm-6">
                  <input type="text" name="code" value={code} id="code"
                    onChange={(e)=>{  handleChange(e,"code")}}required/>
                  </div>
              </div>
          </div>
      </div>
    <div className="row">
        <div className="col-sm-6">
            <div className="row">
                <div className="col-sm-6 label-text">
                    <label for="">Description <span>*</span></label>
                </div>
                <div className="col-sm-6">
                  <textarea name="description" id="description" value={description}
                  onChange={(e)=>{handleChange(e,"description")}} required/>

              </div>
            </div>
          </div>
          <div className="col-sm-6">
              <div className="row">
                <div className="col-sm-6 label-text">
                    <label for="">Active</label>
                </div>
                    <div className="col-sm-6">
                          <label className="radioUi">
                            <input type="checkbox" name="active" value="true" checked={active == "true" || active  ==  true}
                             onChange={(e)=>{ handleChange(e,"active")}}/>

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
    </div>
        );
    }

  }
  ReactDOM.render(
    <Designation />,
    document.getElementById('root')
  );
