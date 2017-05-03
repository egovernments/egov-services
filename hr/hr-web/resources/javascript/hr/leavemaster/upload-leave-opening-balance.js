class UploadLeaveType extends React.Component{

  constructor(props) {
    super(props);
    this.state={LeaveType:{
        "id": "",
        "my_file_input":"",
        "tenantId": tenantId
  },temp:[],dataType:[]}
    this.handleChange=this.handleChange.bind(this);
    this.addOrUpdate=this.addOrUpdate.bind(this);
    this.filePicked=this.filePicked.bind(this);
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

  filePicked(oEvent) {
      // Get The File From The Input
      var oFile = oEvent.target.files[0];
      var sFilename = oFile.name;
      var _this = this, oJS;
      // Create A File Reader HTML5
      var reader = new FileReader();

      // Ready The Event For When A File Gets Selected
      reader.onload = function(e) {
          var data = e.target.result;

          var cfb = XLSX.read(data, {
              type: 'binary'
          });
          cfb.SheetNames.forEach(function(sheetName) {
              // Obtain The Current Row As CSV
                var sCSV = XLS.utils.make_csv(cfb.Sheets[sheetName]);
               oJS = XLS.utils.sheet_to_json(cfb.Sheets[sheetName]);
          });
          var finalObject = [];
          console.log("OJS",oJS);
          oJS.forEach(function(d){
            finalObject.push({"employee": d["Employee Code"],
                              "calendarYear":d["Calendar Year"],
                              "leaveType":  { "id": d["Leave type"]},
                              "noOfDays" : d["Number of days as on 1st Jan 2017"]
            });
          });
          console.log(JSON.stringify(finalObject));
          _this.setState({
            LeaveType:{
              ..._this.state.LeaveType
            }, temp : finalObject
          })
      };

      // Tell JS To Start Reading The File.. You could delay this if desired
      reader.readAsBinaryString(oFile);
  }

  close(){
      // widow.close();
      open(location, '_self').close();
  }



addOrUpdate(e,mode)
{

        e.preventDefault();
        console.log(JSON.stringify(this.state.temp));
        var tempInfo=Object.assign([],this.state.temp);
        console.log(JSON.stringify(tempInfo));
        var body={
            "RequestInfo":requestInfo,
            "LeaveOpeningBalance":tempInfo
          },_this=this;

          $.ajax({

                url: baseUrl + "/hr-leave/leaveopeningbalances/_create?tenantId=" + tenantId,
                type: 'POST',
                dataType: 'json',
                data:JSON.stringify(body),
                contentType: 'application/json',
                headers:{
                  'auth-token': authToken
                },
                success: function(res) {
                        showSuccess("File Uploaded successfully.");
                        _this.setState({
                          LeaveType:{
                            "id": "",
                            "my_file_input": "",
                            "tenantId": tenantId
                        }
                        })

                },
                error: function(err) {
                    showError("Only excel file can Upload");

                }
            });

  }

  render()
  {
    let {handleChange,addOrUpdate,filePicked}=this;
    let {name,payEligible,encashable,halfdayAllowed,accumulative,description,active}=this.state.LeaveType;
    let mode=getUrlVars()["type"];


    return(<div>
      <form onSubmit={(e)=>{addOrUpdate(e,mode)}}>
      <fieldset>
      <div className="row">
        <div className="col-sm-6">
            <div className="row">
                <div className="col-sm-6 label-text">
                    <label for="">Upload File <span> * </span></label>
                </div>
                <div className="col-sm-6">
                    <input type="file" id="my_file_input" name="my_file_input"
                    onChange={(e)=>{filePicked(e)}} required/>

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
