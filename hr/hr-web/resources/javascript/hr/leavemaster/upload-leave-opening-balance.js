class UploadLeaveType extends React.Component{

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

addOrUpdate(e,mode)
{

        e.preventDefault();

        var tempInfo=Object.assign({},this.state.LeaveType) , type = getUrlVars()["type"];
        console.log(tempInfo);
        var body={
            "RequestInfo":requestInfo,
            "LeaveType":[tempInfo]
          },_this=this;

            if(type == "Update") {
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
                    showError("Leave Type already defined");

                }
            });

        }
  }


  componentDidMount(){
    var type=getUrlVars()["type"];
    var id=getUrlVars()["id"];

    if(getUrlVars()["type"]==="View")
    {
      $("input,select,radio,textarea").prop("disabled", true);
      }

      if(type==="View"||type==="Update")
      {
          this.setState({
            LeaveType:getCommonMasterById("hr-leave","leavetypes","LeaveType",id).responseJSON["LeaveType"][0]
          })
      }
  }
  render()
  {
    let {handleChange,addOrUpdate}=this;
    let {name,payEligible,encashable,halfdayAllowed,accumulative,description,active}=this.state.LeaveType;
    let mode=getUrlVars()["type"];


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
      </div>


                <div className="text-center">
                    <button type="submit" className="btn btn-submit">Upload</button>  &nbsp;&nbsp;
                    <button type="button" className="btn btn-close"onClick={(e)=>{this.close()}}>Close</button>
                </div>
                </fieldset>
                </form>
      </div>
    );
  }
}

ReactDOM.render(
  <UploadLeaveType />,
  document.getElementById('root')
);
