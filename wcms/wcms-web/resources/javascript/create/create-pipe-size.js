class CreateUsageType extends React.Component {
  constructor(props) {
    super(props);
    this.state={searchSet:{
    "inputmm":"",
    "inputinc":"",
    "tenantId":tenantId,
    "active":"true"
      }
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
      }
      else {
        this.setState({
          searchSet:{
            ...this.state.searchSet,
            inputinc : (e.target.value * 0.039),
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
    var _this=this;
    $('#inputinc').prop("disabled", true);
    if(window.opener && window.opener.document) {
         var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
         if(logo_ele && logo_ele[0]) {
           document.getElementsByClassName("homepage_logo")[0].src = window.location.origin + logo_ele[0].getAttribute("src");
         }
       }


       var type=getUrlVars()["type"];
        var id=getUrlVars()["id"];

        if(getUrlVars()["type"]==="View")
        {
            $("input,select,textarea").prop("disabled", true);
          }

          if(type==="Update"||type==="View")
          {
               this.setState({
                 searchSet:getCommonMasterById("wcms-masters","usagetype","UsageType",id).responseJSON["UsageType"][0]

              })
          }

}



addOrUpdate(e,mode){
// console.log(this.state.searchSet.inputmm);

}





  render() {
    let {handleChange,addOrUpdate}=this;
    let {list}=this.state;
    let {inputmm,code,inputinc,active}=this.state.searchSet;
    let mode=getUrlVars()["type"];

    const showActionButton=function() {
      if((!mode) ||mode==="Update")
      {
        return (<button type="submit" className="btn btn-submit">{mode?"Update":"Add"}</button>);
      }
    };




    return (
    <div>
    <h3>{ getUrlVars()["type"] ? titleCase(getUrlVars()["type"]) : "Create"} Pipe Size</h3>

    <form onSubmit={(e)=>{addOrUpdate(e,mode)}}>

    <div className="row">
          <div className="col-sm-6">
              <div className="row">
                  <div className="col-sm-6 label-text">
                    <label for=""> H.S.C  Pipe Size (mm) <span> * </span></label>
                  </div>
                  <div className="col-sm-6">
                      <input type="number" id="inputmm" name="inputmm" value={inputmm}
                        onChange={(e)=>{  handleChange(e,"inputmm")}} required/>
                  </div>
              </div>
            </div>
            <div className="col-sm-6">
                  <div className="row">
                      <div className="col-sm-6 label-text">
                        <label for=""> H.S.C  Pipe Size (inches)  </label>
                      </div>
                      <div className="col-sm-6">
              <input  type="number" name="inputinc" id="inputinc"  value={inputinc}
               onChange={(e)=>{handleChange(e,"inputinc")}} readonly/>

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
