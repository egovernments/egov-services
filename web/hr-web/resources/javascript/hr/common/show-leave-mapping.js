class ShowLeaveMapping extends React.Component {
  constructor(props) {
    super(props);
    this.state={
      leaveTypeList:[],designationList:[]
    }
    this.setInitialState = this.setInitialState.bind(this);
  }

  setInitialState(initState) {
    this.setState(initState);
  }


  componentDidMount()
  {

    if(window.opener && window.opener.document) {
       var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
       if(logo_ele && logo_ele[0]) {
         document.getElementsByClassName("homepage_logo")[0].src = window.location.origin + logo_ele[0].getAttribute("src");
       }
    }
    $('#hp-citizen-title').text(titleCase(getUrlVars()["type"]) + " Leave Mapping");
    var _this = this;
    commonApiPost("hr-leave","leaveallotments","_search", { tenantId, pageSize:500 }, function(err, res) {
      if(res) {
        _this.setState({
          leaveTypeList: res.LeaveAllotment
        })
      }
    });
    var count = 1, _state = {}, _this = this;
    const checkCountAndCall = function(key, res) {
      _state[key] = res;
      count--;
      if(count == 0)
        _this.setInitialState(_state);
    }

    getDropdown("assignments_designation", function(res) {
      checkCountAndCall("designationList", res);
    });
  }

  componentDidUpdate(prevProps, prevState) {
      if (prevState.leaveTypeList.length!=this.state.leaveTypeList.length) {
          $('#LeaveTable').DataTable({
            dom: 'Bfrtip',
            buttons: [
                     'copy', 'csv', 'excel', 'pdf', 'print'
             ],
             ordering: false
          });
      }
  }

  close(){
      // widow.close();
      open(location, '_self').close();
  }


  render() {
    let {leaveTypeList,designationList}=this.state;

    const renderAction=function(type,id){
      if (type==="update") {

              return (
                      <a href={`app/hr/leavemaster/leave-mapping.html?id=${id}&type=${type}`} className="btn btn-default btn-action"><span className="glyphicon glyphicon-pencil"></span></a>
              );

        }else {
                return (
                        <a href={`app/hr/leavemaster/leave-mapping.html?id=${id}&type=${type}`} className="btn btn-default btn-action"><span className="glyphicon glyphicon-modal-window"></span></a>
                );
            }
    }


    const renderBody=function() {
      return leaveTypeList.map((item,index)=>
      {
            return (<tr key={index}>
                    <td data-label="designation">{getNameById(designationList,item.designation,"name")}</td>
                    <td data-label="leaveType">{item.leaveType.name}</td>
                    <td data-label="noOfDay">{item.noOfDays}</td>
                    <td data-label="action">
                    {renderAction(getUrlVars()["type"],item.id)}
                    </td>
                </tr>
            );

      })
    }

      return (<div>
        <h3>{titleCase(getUrlVars()["type"])} Leave Mapping</h3>
        <table id="LeaveTable" className="table table-bordered">
            <thead>
                <tr>
                    <th>Designation</th>
                    <th>Leave Type</th>
                    <th>No of day</th>
                    <th>Action</th>

                </tr>
            </thead>

            <tbody id="leavesearchResultTableBody">
                {
                    renderBody()
                }
            </tbody>

        </table>
        <div className="text-center">
            <button type="button" className="btn btn-close"onClick={(e)=>{this.close()}}>Close</button>
        </div>
      </div>
    );
  }
}


ReactDOM.render(
  <ShowLeaveMapping />,
  document.getElementById('root')
);
