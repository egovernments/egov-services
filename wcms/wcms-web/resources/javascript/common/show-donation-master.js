
class ShowDonationMaster extends React.Component {
  constructor(props) {
    super(props);
    this.state={list:[],donationSet:{
    "id": "",
    "department":"",
    "designation":"",
    "fromDate":"",
    "toDate":"",
   "tenantId":tenantId
  }}

  }


  componentDidMount()
  {
    if(window.opener && window.opener.document) {
       var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
       if(logo_ele && logo_ele[0]) {
         document.getElementsByClassName("homepage_logo")[0].src = window.location.origin + logo_ele[0].getAttribute("src");
       }
     }
    // try {
    //     var _UsageType = commonApiPost("wcms-masters","usagetype","_search",{tenantId}).responseJSON["UsageType"] || [];
    // } catch(e) {
    //     var _UsageType = [];
    // }
    // this.setState({
    //   list:_UsageType
    // });

  }


  componentDidUpdate(prevProps, prevState)
  {
      if (prevState.list.length!=this.state.list.length) {

          $('#donationMasterTable').DataTable({
            dom: 'Bfrtip',
            buttons: [
                      'excel', 'pdf', 'print'
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
    let {list}=this.state;
    let {department,designation,fromDate,toDate,id}=this.state.donationSet;
    var mode = getUrlVars()["type"];

    const renderAction=function(type,id){
      if (type==="Update") {

              return (
                      <a href={`app/create/create-donation-master.html?id=${id}&type=${type}`} className="btn btn-default btn-action"><span className="glyphicon glyphicon-pencil"></span></a>
              );

    }else {
            return (
                    <a href={`app/create/create-donation-master.html?id=${id}&type=${type}`} className="btn btn-default btn-action"><span className="glyphicon glyphicon-modal-window"></span></a>
            );
        }
}


    const renderBody=function()
    {
      return list.map((item,index)=>
      {
            return (<tr key={index}>
                    <td>{index+1}</td>
                    <td data-label="fromDate">{item.fromDate}</td>
                    <td data-label="department">{item.department}</td>
                    <td data-label="designation">{item.designation}</td>
                    <td data-label="toDate">{item.toDate}</td>
                    <td data-label="action">
                    {renderAction(getUrlVars()["type"],item.id)}
                    </td>
                </tr>
            );

      })
    }

      return (<div>
        <h3>{titleCase(getUrlVars()["type"])}  Donation Master </h3>
        <table id="donationMasterTable" className="table table-bordered">
            <thead>
                <tr>
                    <th>Sl No.</th>
                    <th>Property Type</th>
                    <th>Category</th>
                    <th>Usage Type</th>
                    <th>Max Pipe Size</th>
                    <th> Min Pipe Size </th>
                    <th> Donation Amount </th>
                    <th>From Date</th>
                    <th> To Date</th>
                    <th>Action</th>
                </tr>
            </thead>

            <tbody id="donationMasterTableResulltBody">
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
  <ShowDonationMaster />,
  document.getElementById('root')
);
