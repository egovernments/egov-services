class LeaveType extends React.Component{

  constructor(props) {
    super(props);
    this.state={LeaveType:{
        "id": "",
        "name": "",
        "description": "",
        "halfdayAllowed": "false",
        "payEligible": "false",
        "accumulative": "false",
        "encashable": "false",
        "active": "true",
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
    console.log(e.target.value);
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

        var tempInfo=this.state.LeaveType , type = getUrlVars()["type"];

        var body={
            "RequestInfo":requestInfo,
            "LeaveType":[tempInfo]
          };
        var response=$.ajax({

              url:baseUrl+"/hr-leave/leavetypes/" + (type == "update" ? (this.state.LeaveType.id + "/" + "_update/") : "_create"),
              type: 'POST',
              dataType: 'json',
              data:JSON.stringify(body),
              async: false,
              contentType: 'application/json',
              headers:{
                'auth-token': authToken
              }
          });


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
      $("input,select,radio,textarea").prop("disabled", true);
      }

      if(type==="view"||type==="update")
      {

          this.setState({
            LeaveType:getCommonMasterById("hr-leave","leavetypes","LeaveType",id).responseJSON["LeaveType"][0]
          })
      }
  }
  render()
  {
    let {handleChange,addOrUpdate}=this;
    let {name,payEligible,encashable,halfdayAllowed,accumulative,description}=this.state.LeaveType;
    let mode=getUrlVars()["type"];
    const showActionButton=function() {
      if((!mode)|| mode==="update")
      {
          return (<button type="submit" className="btn btn-submit">{mode?"Update":"Add"}</button>);
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
                                  <input type="radio" name="payEligible"  value="true" checked={payEligible == "true"}
                                      onChange={(e)=>{handleChange(e,"payEligible")  }}/> Yes
                                </label>
                                <label className="radio-inline radioUi">
                                  <input type="radio" name="payEligible" value="false" checked={payEligible == "false" || !payEligible}
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
                                  <input type="radio" name="halfdayAllowed" value="true" checked={halfdayAllowed == "true"}
                                   onChange={(e)=>{handleChange(e,"halfdayAllowed")}}  /> Yes
                                </label>
                                <label className="radio-inline radioUi">
                                  <input type="radio"  name="halfdayAllowed" value="false" checked={halfdayAllowed == "false" || !halfdayAllowed}
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
                                  <input type="radio" name="encashable" value="true" checked={encashable == "true"}
                                   onChange={(e)=>{handleChange(e,"encashable")}}/> Yes
                                </label>
                                <label className="radio-inline radioUi">
                                  <input type="radio" name="encashable"  value="false" checked={encashable == "false" || !encashable}
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
                                    <input type="radio" name="accumulative"  value="true" checked={accumulative == "true"}
                                        onChange={(e)=>{handleChange(e,"accumulative")}}/> Yes

                                  </label>
                                  <label className="radio-inline radioUi">
                                    <input type="radio" name="accumulative" value="false" checked={accumulative == "false" || !accumulative}
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
                        <label for="">Active</label>
                    </div>
                        <div className="col-sm-6">
                              <label className="radioUi">
                                <input type="checkbox" name="active" id="active" value="true" onChange={(e)=>{
                                    handleChange(e,"active")}} checked/>
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
