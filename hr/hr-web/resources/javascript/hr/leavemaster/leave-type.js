class LeaveType extends React.Component{

  constructor(props) {
    super(props);
    this.state={LeaveType:{
        "id": "",
        "name": "",
        "description": "",
        "halfdayAllowed": "",
        "payEligible": "",
        "accumulative": "",
        "encashable": "",
        "active": "",
        "createdBy": "",
        "createdDate": "",
        "lastModifiedBy": "",
        "lastModifiedDate": "",
        "tenantId": tenantId
  },dataType:[]}
    this.handleChange=this.handleChange.bind(this);
    this.addOrUpdate=this.addOrUpdate.bind(this);
  }

  handleChange(e,name)
  {

      this.setState({
          LeaveType:{
              ...this.state.LeaveType,
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

        var tempInfo=this.state.LeaveType , type = getUrlVars()["type"];;
        // tempInfo["assetSet"]["assetCategory"]["id"]=parseInt(tempInfo["assetSet"]["assetCategory"]["id"])
        var body={
            "RequestInfo":requestInfo,
            "LeaveType":[tempInfo]
          };
        var response=$.ajax({
              //  url: baseUrl+"/hr-leave/leavetypes/_create",
              url:baseUrl+"/hr-leave/leavetypes/" + (type == "update" ? (this.state.LeaveType.id + "/" + "_update/") : "_create"),
              type: 'POST',
              dataType: 'json',
              data:JSON.stringify(body),
              async: false,
              contentType: 'application/json',
              headers:{
                'auth-token': '2f22998a-37bc-444b-88b4-3945becae279'
              }
          });

        // console.log(response);
        if(response["statusText"]==="OK")
        {
          alert("Successfully added");
          this.setState({
            LeaveType:{
              "id": "",
              "name": "",
              "description": "",
              "halfdayAllowed": "",
              "payEligible": "",
              "accumulative": "",
              "encashable": "",
              "active": "",
              "createdBy": "",
              "createdDate": "",
              "lastModifiedBy": "",
              "lastModifiedDate": "",
              "tenantId": tenantId
          }
          })
        }
        else {
          alert(response["statusText"]);
          this.setState({
            LeaveType:{
              "id": "",
              "name": "",
              "description": "",
              "halfdayAllowed": "",
              "payEligible": "",
              "accumulative": "",
              "encashable": "",
              "active": "",
              "createdBy": "",
              "createdDate": "",
              "lastModifiedBy": "",
              "lastModifiedDate": "",
              "tenantId": tenantId
          }
          })
      }
  }


  componentDidMount(){
    var type=getUrlVars()["type"];
    var id=getUrlVars()["id"];

    if(getUrlVars()["type"]==="view")
    {
      for (var variable in this.state.LeaveType)
        document.getElementById(variable).disabled = true;
      }

      if(type==="view"||type==="update")
      {
          console.log(getCommonMasterById("hr-leave","leavetypes","LeaveType",id).responseJSON["LeaveType"]);
          this.setState({
            LeaveType:getCommonMasterById("hr-leave","leavetypes","LeaveType",id).responseJSON["LeaveType"][0]
          })
      }
  }
  render(){
    let {handleChange,addOrUpdate}=this;
    let{name,payEligible,encashable,halfdayAllowed,accumulative,description}=this.state.LeaveType;
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
                      <label for="">Description <span> * </span></label>
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
                              <label for="">Pay Eligible <span> *</span> </label>
                          </div>
                          <div className="col-sm-6">
                                <label className="radio-inline radioUi">
                                  <input type="radio" name="payEligible" id="payEligible" value="true"
                                      onChange={(e)=>{handleChange(e,"payEligible")  }}/> Yes
                                </label>
                                <label className="radio-inline radioUi">
                                  <input type="radio" name="payEligible" id="payEligible" value="false"
                                      onChange={(e)=>{handleChange(e,"payEligible")}}/> No
                                </label>
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
                                  <input type="radio" name="halfdayAllowed" id="halfdayAllowed"  value="true"
                                   onChange={(e)=>{handleChange(e,"halfdayAllowed")}} /> Yes
                                </label>
                                <label className="radio-inline radioUi">
                                  <input type="radio" name="halfdayAllowed" id="halfdayAllowed" value="false"
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
                              <label for="">encashable <span> *</span></label>
                          </div>
                          <div className="col-sm-6">
                                <label className="radio-inline radioUi">
                                  <input type="radio" name="encashable" id="encashable" value="true"
                                   onChange={(e)=>{handleChange(e,"encashable")}}/> Yes
                                </label>
                                <label className="radio-inline radioUi">
                                  <input type="radio" name="encashable" id="encashable" value="false"
                                  onChange={(e)=>{handleChange(e,"encashable")}}/> No
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
                                    <input type="radio" name="accumulative" id="accumulative" value="true"
                                        onChange={(e)=>{handleChange(e,"accumulative")}}/> Yes

                                  </label>
                                  <label className="radio-inline radioUi">
                                    <input type="radio" name="accumulative" id="accumulative" value="false"
                                        onChange={(e)=>{handleChange(e,"accumulative")}}/> No
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
  <LeaveType />,
  document.getElementById('root')
);
