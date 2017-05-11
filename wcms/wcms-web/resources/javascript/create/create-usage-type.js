class CreateUsageType extends React.Component {
  constructor(props) {
    super(props);
    this.state={list:[],searchSet:{
    "name":"",
    "code":"",
  "description":"",
  "tenantId":tenantId,
"active":"true"}
      }
     this.handleChange=this.handleChange.bind(this);
     this.addOrUpdate=this.addOrUpdate.bind(this);

  }
  handleChange(e,name){
        if(name === "active"){
        this.setState({
          searchSet:{
              ...this.state.searchSet,
              active: !this.state.searchSet.active

          }
        })
      }else{
        this.setState({
          searchSet:{
            ...this.state.searchSet,
            [name]:e.target.value
          }
        })
      }
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
       var type=getUrlVars()["type"];
        var id=getUrlVars()["id"];

        if(getUrlVars()["type"]==="view")
        {
            $("input,select").prop("disabled", true);
          }

          if(type==="update"||type==="view")
          {
               this.setState({
                 searchSet:getCommonMasterById("wcms","usagetype","UsageType",id).responseJSON["UsageType"][0]
              })
          }

}


addOrUpdate(e,mode){
  e.preventDefault();
          var tempInfo=Object.assign({},this.state.searchSet) , type = getUrlVars()["type"];
            var body={
              "RequestInfo":requestInfo,
              "UsageType":tempInfo
            },_this=this;
            if (type == "update") {
                            $.ajax({
                   url:'http://172.16.2.174'+"/wcms/usagetype/" + this.state.searchSet.id + "/" + "_update?tenantId=" + tenantId,
                    type: 'POST',
                    dataType: 'json',
                    data:JSON.stringify(body),
                    async: false,
                    contentType: 'application/json',
                    headers:{
                      'auth-token': authToken
                    },
                    success: function(res) {
                          showSuccess("Usage Type Modified successfully.");
                          window.location.href = 'app/common/show-usage-type.html?type=update';
                    },
                    error: function(err) {
                        showError("Duplicate Usage Type are not allowed");

                    }
                });
            } else {
              $.ajax({
                    url:'http://172.16.2.174'+"/wcms/usagetype/_create?tenantId="+ tenantId ,
                    type: 'POST',
                    dataType: 'json',
                    data:JSON.stringify(body),
                    async: false,
                    contentType: 'application/json',
                    headers:{
                      'auth-token': authToken
                    },
                    success: function(res) {
                            showSuccess("Usage Type Created successfully.");
                            _this.setState({searchSet:{
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





  render() {
    let {handleChange,addOrUpdate}=this;
    let {list}=this.state;
    let {name,code,description,active}=this.state.searchSet;
    let mode=getUrlVars()["type"];

    const showActionButton=function() {
      if((!mode) ||mode==="update")
      {
        return (<button type="submit" className="btn btn-submit">{mode?"Update":"Add"}</button>);
      }
    };




    return (
    <div>
    <h3>{ getUrlVars()["type"] ? titleCase(getUrlVars()["type"]) : "Create"} Usage Type</h3>

    <form onSubmit={(e)=>{addOrUpdate(e,mode)}}>

    <div className="row">
          <div className="col-sm-6">
              <div className="row">
                  <div className="col-sm-6 label-text">
                    <label for=""> Name <span> * </span></label>
                  </div>
                  <div className="col-sm-6">
                      <input type="text" id="name" name="name" value={name}
                        onChange={(e)=>{  handleChange(e,"name")}} required/>
                  </div>
              </div>
            </div>
            <div className="col-sm-6">
                  <div className="row">
                      <div className="col-sm-6 label-text">
                        <label for=""> Description <span> * </span> </label>
                      </div>
                      <div className="col-sm-6">
              <textarea name="description" id="description" value={description}
              onChange={(e)=>{handleChange(e,"description")}} required/>

          </div>
          </div>
            </div>

              </div>

                 <div className="row">
                                            <div className="col-sm-6">
                            <div className="row">
                                <div className="col-sm-6 label-text">
                                  <label for=""> Active </label>
                                </div>
                                <div className="col-sm-6">
                                <input type="checkbox" name="active" value="true" checked={active == "true" || active  ==  true}
                             onChange={(e)=>{ handleChange(e,"active")}}/>

                                </div>
                            </div>
                          </div>
                         </div>

                         <div className="text-center">
                    {showActionButton()} &nbsp;&nbsp;
                    <button type="button" className="btn btn-close" onClick={(e)=>{this.close()}}>Close</button>
                    </div>
                    </form>



          </div>
          );
      }
}


ReactDOM.render(
  <CreateUsageType />,
  document.getElementById('root')
);
//
// <button type="submit" className="btn btn-submit">Add</button>
// &nbsp;
