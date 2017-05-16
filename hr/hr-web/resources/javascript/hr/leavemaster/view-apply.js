class ViewApply extends React.Component {
  constructor(props) {
    super(props);
    this.state={list:[],leaveSet:{
      "employee": "",
      "name":"",
      "code":"",
       "leaveType": {
       	"id" : ""
       },
       "fromDate" : "",
       "toDate": "",
       "availableDays": "",
       "leaveDays":"",
       "reason": "",
       "status": "",
       "stateId": "",
       "tenantId" : tenantId
     }
  }

  }


  componentDidMount() {
    if(window.opener && window.opener.document) {
       var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
       if(logo_ele && logo_ele[0]) {
         document.getElementsByClassName("homepage_logo")[0].src = window.location.origin + logo_ele[0].getAttribute("src");
       }
     }

     try{
       var _status = commonApiPost("hr-masters","hrstatuses","_search",{tenantId}).responseJSON["HRStatus"]
     }catch(e){
       var _status = [];
     }

    var employee = getUrlVars()["id"];
    var leaveApp = commonApiPost("hr-leave","leaveapplications","_search",{employee,tenantId}).responseJSON["LeaveApplication"] ;
    var empIds = [];
    for(var i=0; i<leaveApp.length; i++) {
      if(empIds.indexOf(leaveApp[i].employee) == -1)
        empIds.push(leaveApp[i].employee);
    }

    if(empIds.length > 0) {
      var employees = commonApiPost("hr-employee", "employees", "_search", {
        tenantId,
        id: empIds.join(",")
      }).responseJSON["Employee"];

      for(var i=0; i<leaveApp.length; i++) {
        employees.map(function(item, ind) {
          if(item.id == leaveApp[i].employee) {
            leaveApp[i].name = item.name;
            employees.splice(ind, 1);
          }
        })
      }
    }
    this.setState({
      list: leaveApp,
      _status
    });
  }

  componentDidUpdate(prevProps, prevState) {
      if (prevState.list.length!=this.state.list.length) {
          $('#viewleaveTable').DataTable({
            dom: 'Bfrtip',
            buttons: [
                     'copy', 'csv', 'excel', 'pdf', 'print'
             ],
             ordering: false
          });
      }
  }



  close() {
      // widow.close();
      open(location, '_self').close();
  }


  render() {
    let {list,_status}=this.state;
    let {employee,name,code,leaveType,fromDate,toDate,availableDays,leaveDays,reason}=this.state.leaveSet;


    const renderBody=function()
    {
      return list.map((item,index)=>
      {
            return (<tr key={index}>
              <td>{index+1}</td>
              <td data-label="name">{item.name}</td>
              <td data-label="fromDate">{item.fromDate}</td>
              <td data-label="toDate">{item.toDate}</td>
              <td data-label="availableDays">{item.availableDays}</td>
              <td data-label="leaveDays">{item.leaveDays}</td>
              <td data-label="status">{getNameById(_status,item.status,"code")}</td>
              <td data-label="action">
                <a href={`app/hr/leavemaster/apply-leave.html?id=${item.id}&type=view`}>View-Details</a>
              </td>
          </tr>
            );

      })
    }


      return (<div>
        <table id="viewleaveTable" className="table table-bordered">
            <thead>
                <tr>
                    <th>Sl No.</th>
                    <th>Name</th>
                    <th>From Date</th>
                    <th>To Date</th>
                    <th>Available Days</th>
                    <th>Leave Days</th>
                    <th>Status</th>
                    <th>Action</th>
                </tr>
            </thead>

            <tbody id="listsearchResultTableBody">
                {
                    renderBody()
                }
            </tbody>

        </table>
      </div>
    );
  }
}

ReactDOM.render(
  <ViewApply />,
  document.getElementById('root')
);
