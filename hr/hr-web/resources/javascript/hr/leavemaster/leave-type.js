class Leavetype extends React.Component{

  constructor(props) {
    super(props);
    this.state={leaveType:{
    name:"",
    payEligible:"",
    encashable:"",
    halfdayAllowed:"",
    accumulative:""
  },dataType:[]}
    this.handleChange=this.handleChange.bind(this);
    this.addOrUpdate=this.addOrUpdate.bind(this);
  }

  handleChange(e,name)
  {

      this.setState({
          leaveType:{
              ...this.state.leaveType,
              [name]:e.target.value
          }
      })

  }

  close(){
      // widow.close();
      open(location, '_self').close();
  }

addOrUpdate(e,mode)
{
  e.preventDefault();
        // console.log(zone);
        // console.log(this.state.assetCategory);
        var tempInfo = this.state.leaveType, _this = this, type = getUrlVars()["type"];
        // tempInfo["assetSet"]["assetCategory"]["id"]=parseInt(tempInfo["assetSet"]["assetCategory"]["id"])
        var body={
            "RequestInfo":requestInfo,
            "leaveType":tempInfo
          };

        var response=$.ajax({
              // url:baseUrl+"/hr-leave/leavetypes/_create?tenantId=1",
              url: baseUrl + "/hr-leave/leavetypes/" + (type == "update" ? ("_update/"+ _this.state.leaveType) : "_create") + "?tenantId=1",
              type: 'POST',
              dataType: 'json',
              data:JSON.stringify(body),
              async: false,
              contentType: 'application/json',
              headers:{
                'auth-token': authToken
              }
          });

        // console.log(response);
        if(response["statusText"]==="OK")
        {
          alert("Successfully added");
        }
        else {
          alert(response["statusText"]);
          this.setState({
            leaveType:{
            name:"",
            payEligible:"",
            encashable:"",
            halfdayAllowed:"",
            accumulative:""
          }
          })
      }
  }


componentDidMount()
{

 if (getUrlVars()["type"]==="view")
  {
  for (var variable in this.state.leaveType)
    {
      // console.log($(`#${variable}`).length);

      document.getElementById(variable).disabled= true ;

    }

  }


}
  render(){
    let {handleChange,addOrUpdate}=this;
    let{name,payEligible,encashable,halfdayAllowed,accumulative}=this.state.leaveType;
    let mode=getUrlVars()["type"];

      const showActionButton=function() {
      if((!mode)|| mode==="update")
      {
          ;return (<button type="submit" className="btn btn-submit">{mode?"Update":"Add"}</button>);
      }
    }

    return(<div>
      <form onSubmit={(e)=>{addOrUpdate(e,mode)}}>
      <fieldset>
      <div className="row">
        <div className="col-sm-6">
            <div className="row">
                <div className="col-sm-6 label-text">
                    <label for="">Leave Type Name <span> * </span></label>
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
                      <label for="">Is Half Day <span> * </span> </label>
                  </div>
                  <div className="col-sm-6">
                        <label className="radio-inline radioUi">
                          <input type="radio" name="halfdayAllowed" id="halfdayAllowed"  value="yes"
                           onChange={(e)=>{handleChange(e,"halfdayAllowed")}} /> Yes
                        </label>
                        <label className="radio-inline radioUi">
                          <input type="radio" name="halfdayAllowed" id="halfdayAllowed" value="no"
                          onChange={(e)=>{handleChange(e,"halfdayAllowed")}}/> No
                        </label>
                  </div>
              </div>
            </div>
          </div>
            <div className="row">
                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                              <label for="">Pay Eligible <span> *</span> </label>
                          </div>
                          <div className="col-sm-6">
                                <label className="radio-inline radioUi">
                                  <input type="radio" name="payEligible" id="payEligible" value="yes"
                                      onChange={(e)=>{handleChange(e,"payEligible")  }}/> Yes
                                </label>
                                <label className="radio-inline radioUi">
                                  <input type="radio" name="payEligible" id="payEligible" value="no"
                                      onChange={(e)=>{handleChange(e,"payEligible")}}/> No
                                </label>
                          </div>
                      </div>
                  </div>
                    <div className="col-sm-6">
                        <div className="row">
                            <div className="col-sm-6 label-text">
                                <label for="">accumulative <span> *</span></label>
                            </div>
                            <div className="col-sm-6">
                                  <label className="radio-inline radioUi">
                                    <input type="radio" name="accumulative" id="accumulative" value="yes"
                                        onChange={(e)=>{handleChange(e,"accumulative")}}/> Yes

                                  </label>
                                  <label className="radio-inline radioUi">
                                    <input type="radio" name="accumulative" id="accumulative" value="no"
                                        onChange={(e)=>{handleChange(e,"accumulative")}}/> No
                                  </label>
                            </div>
                        </div>
                    </div>
                  </div>

              <div className="row">
                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                              <label for="">encashable <span> *</span></label>
                          </div>
                          <div className="col-sm-6">
                                <label className="radio-inline radioUi">
                                  <input type="radio" name="encashable" id="encashable" value="yes"
                                   onChange={(e)=>{handleChange(e,"encashable")}}/> Yes
                                </label>
                                <label className="radio-inline radioUi">
                                  <input type="radio" name="encashable" id="encashable" value="no"
                                  onChange={(e)=>{handleChange(e,"encashable")}}/> No
                                </label>
                          </div>
                      </div>
                    </div>
                </div>

                <div className="text-center">
                    {showActionButton()}  &nbsp;&nbsp;
                    <button type="button" className="btn btn-close"onClick={(e)=>{this.close()}}>Close</button>
                </div>
                </fieldset>
                </form>
      </div>
    );
  }
}

ReactDOM.render(
  <Leavetype />,
  document.getElementById('root')
);
