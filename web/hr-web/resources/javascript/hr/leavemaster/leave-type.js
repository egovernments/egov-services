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
      if(name === "active"){
        this.setState({
          LeaveType:{
              ...this.state.LeaveType,
              active: !this.state.LeaveType.active

          }
        })
      } else{
        this.setState({
            LeaveType:{
                ...this.state.LeaveType,
                [name]:e.target.value
            }
        })
      }


  }

  close(){
      // widow.close();
      open(location, '_self').close();
  }

addOrUpdate(e, mode) {
      e.preventDefault();
      var tempInfo=Object.assign({},this.state.LeaveType) , type = getUrlVars()["type"];
      var body={
          "RequestInfo":requestInfo,
          "LeaveType":[tempInfo]
        },_this=this;

      if(type == "update") {
        $.ajax({
          url:baseUrl+"/hr-leave/leavetypes/" + _this.state.LeaveType.id + "/" + "_update?tenantId=" + tenantId,
          type: 'POST',
          dataType: 'json',
          data:JSON.stringify(body),
          contentType: 'application/json',
          headers:{
            'auth-token': authToken
          },
          success: function(res) {
            showSuccess("Leave Modified successfully.");
            window.location.href = 'app/hr/common/show-leave-type.html?type=update';
          },
          error: function(err) {
              showError("Leave Type already defined");
          }
        });
      } else {
        $.ajax({
          url:baseUrl+"/hr-leave/leavetypes/_create?tenantId=" + tenantId,
          type: 'POST',
          dataType: 'json',
          data:JSON.stringify(body),
          contentType: 'application/json',
          headers:{
            'auth-token': authToken
          },
          success: function(res) {
            showSuccess("Leave Created successfully.");
            _this.setState({
              LeaveType:{
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
              }
            })
          },
          error: function(err) {
            showError("Leave Type already defined.");
          }
        });
      }
  }

  componentDidMount() {
    if(window.opener && window.opener.document) {
       var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
       if(logo_ele && logo_ele[0]) {
         document.getElementsByClassName("homepage_logo")[0].src = window.location.origin + logo_ele[0].getAttribute("src");
       }
    }
    if(getUrlVars()["type"]) $('#hp-citizen-title').text(titleCase(getUrlVars()["type"]) + " Leave Type");
    var type=getUrlVars()["type"];
    var id=getUrlVars()["id"], _this = this;

    if(getUrlVars()["type"]==="view") {
      $("input,select,radio,textarea").prop("disabled", true);
    }

    if(type==="view"||type==="update") {
      getCommonMasterById("hr-leave", "leavetypes", id, function(err, res) {
        if(res) {
          _this.setState({
            LeaveType: res.LeaveType[0]
          })
        }
      })
    }
  }

  render()
  {
    let {handleChange,addOrUpdate}=this;
    let {name,payEligible,encashable,halfdayAllowed,accumulative,description,active,encloseHoliday,includePrefixSuffix}=this.state.LeaveType;
    let mode=getUrlVars()["type"];
    const showActionButton=function() {
      if((!mode)|| mode==="update")
      {
          return (<button type="submit" className="btn btn-submit">{mode?"Update":"Add"}</button>);
      }
    }

    return(<div>
      <h3>{ getUrlVars()["type"] ? titleCase(getUrlVars()["type"]) :  "Create"} Leave Type</h3>
      <form onSubmit={(e)=>{addOrUpdate(e,mode)}}>
      <fieldset>
      <div className="row">
        <div className="col-sm-6">
            <div className="row">
                <div className="col-sm-6 label-text">
                    <label htmlFor="">Leave Type Name <span> * </span></label>
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
                      <label htmlFor="">Description <span> * </span></label>
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
                              <label htmlFor="">Pay Eligible <span> *</span> </label>
                          </div>
                          <div className="col-sm-6">
                                <label className="radio-inline radioUi">
                                  <input type="radio" name="payEligible"  value="true" checked={payEligible == "true" || payEligible  ==  true}
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
                              <label htmlFor="">Is Half Day <span> * </span> </label>
                          </div>
                          <div className="col-sm-6">
                                <label className="radio-inline radioUi">
                                  <input type="radio" name="halfdayAllowed" value="true" checked={halfdayAllowed == "true" || halfdayAllowed  ==  true }
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
                              <label htmlFor="">encashable <span> *</span></label>
                          </div>
                          <div className="col-sm-6">
                                <label className="radio-inline radioUi">
                                  <input type="radio" name="encashable" value="true" checked={encashable == "true" || encashable  ==  true}
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
                                <label htmlFor="">accumulative <span> *</span></label>
                            </div>
                            <div className="col-sm-6">
                                  <label className="radio-inline radioUi">
                                    <input type="radio" name="accumulative"  value="true" checked={accumulative == "true" || accumulative  ==  true }
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
                              <label htmlFor="">Encloses Holiday <span> *</span> </label>
                          </div>
                          <div className="col-sm-6">
                                <label className="radio-inline radioUi">
                                  <input type="radio" name="encloseHoliday"  value="true" checked={encloseHoliday == "true" || encloseHoliday  ==  true}
                                      onChange={(e)=>{handleChange(e,"encloseHoliday")  }}/> Yes
                                </label>
                                <label className="radio-inline radioUi">
                                  <input type="radio" name="encloseHoliday" value="false" checked={encloseHoliday == "false" || !encloseHoliday}
                                      onChange={(e)=>{handleChange(e,"encloseHoliday")}}/> No
                                </label>
                          </div>
                      </div>
                  </div>
                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                              <label htmlFor="">Prefix/Suffix <span> * </span> </label>
                          </div>
                          <div className="col-sm-6">
                                <label className="radio-inline radioUi">
                                  <input type="radio" name="includePrefixSuffix" value="true" checked={includePrefixSuffix == "true" || includePrefixSuffix  ==  true }
                                   onChange={(e)=>{handleChange(e,"includePrefixSuffix")}}  /> Yes
                                </label>
                                <label className="radio-inline radioUi">
                                  <input type="radio"  name="includePrefixSuffix" value="false" checked={includePrefixSuffix == "false" || !includePrefixSuffix}
                                  onChange={(e)=>{handleChange(e,"includePrefixSuffix")}}/> No
                                </label>
                          </div>
                      </div>
                    </div>
                </div>

                <div className="row">
                <div className="col-sm-6">
                  <div className="row">
                    <div className="col-sm-6 label-text">
                        <label htmlFor="">Active</label>
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
