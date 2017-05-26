class Category extends React.Component{
  constructor(props){
    super(props);
    this.state={categorySet:{
        name:"",
        description:"",
        active:""
    }
  }
  this.handleChange=this.handleChange.bind(this);
  this.addOrUpdate=this.addOrUpdate.bind(this);
}

componentDidMount() {
  var type = getUrlVars()["type"];
  var id = getUrlVars()["id"];
  var _this = this;

  if(window.opener && window.opener.document) {
     var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
     if(logo_ele && logo_ele[0]) {
       document.getElementsByClassName("homepage_logo")[0].src = window.location.origin + logo_ele[0].getAttribute("src");
     }
   }

  if(getUrlVars()["type"]) $('#hp-citizen-title').text(titleCase(getUrlVars()["type"]) + " Category");

  if(getUrlVars()["type"]==="view") {
    for (var variable in this.state.categorySet)
      document.getElementById(variable).disabled = true;
    }

  if(type==="view"||type==="update") {

    getCommonMasterById("egov-common-masters","categories", id ,function(err, res) {
      if(res) {
           var categorySet = res["Category"][0];
          _this.setState({
            categorySet
          })
        }
    })
  }
}

handleChange(e,name) {
    this.setState({
        categorySet:{
            ...this.state.categorySet,
            [name]:e.target.value
        }
    })

}

close() {
    // widow.close();
    open(location, '_self').close();
}


addOrUpdate(e,mode) {
    console.log(this.state.categorySet);
    e.preventDefault();
    this.setState({categorySet:{
        name:"",
        description:"",
        active:""
    }
 })
}

render(){

  let {handleChange,addOrUpdate}=this;
  let {name,description,active}=this.state.categorySet;
  let mode=getUrlVars()["type"];


    const showActionButton=function() {
      if((!mode) ||mode==="update")
      {
        return (<button type="submit" className="btn btn-submit">{mode?"Update":"Add"}</button>);
      }
    };

  return (<div>

    <h3>{ getUrlVars()["type"] ? titleCase(getUrlVars()["type"]) : "Create"} Category </h3>
      <form onSubmit={(e)=>{addOrUpdate(e,mode)}}>
      <fieldset>
      <div className="row">
        <div className="col-sm-6">
            <div className="row">
                <div className="col-sm-6 label-text">
                    <label for=""> Category Name <span> *</span></label>
                </div>
                  <div className="col-sm-6">
                    <input type="text" name="name" id="name" value={name}
                        onChange={(e)=>{handleChange(e,"name")}} required/>
                  </div>
              </div>
          </div>
          <div className="col-sm-6">
              <div className="row">
                  <div className="col-sm-6 label-text">
                      <label for="">Description<span> *</span> </label>
                      </div>
                          <div className="col-sm-6">
                          <input type="text" name="description" id="description" value={description} onChange={(e)=>{
                              handleChange(e,"description")}} required/>
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
        </div>
    );
  }
}

ReactDOM.render(
  <Category />,
  document.getElementById('root')
);
