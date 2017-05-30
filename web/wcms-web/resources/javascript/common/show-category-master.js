
class ShowCategoryType extends React.Component {
  constructor(props) {
    super(props);
    this.state={list:[],searchSet:{name:"",
    code:"",description:"",active:""}}

  }


  componentDidMount()
  {
    if(window.opener && window.opener.document) {
       var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
       if(logo_ele && logo_ele[0]) {
         document.getElementsByClassName("homepage_logo")[0].src = window.location.origin + logo_ele[0].getAttribute("src");
       }
     }
    try {
        var _Category = commonApiPost("wcms-masters","category","_search",{tenantId}).responseJSON["Category"] || [];
    } catch(e) {
        var _Category = [];
    }
    this.setState({
      list:_Category
    });

  }


  componentDidUpdate(prevProps, prevState)
  {
      if (prevState.list.length!=this.state.list.length) {

          $('#designationTable').DataTable({
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
    let {name,code,description,active}=this.state.searchSet;
    var mode = getUrlVars()["type"];

    const renderAction=function(type,id){
      if (type==="Update") {

              return (
                      <a href={`app/create/create-category-master.html?id=${id}&type=${type}`} className="btn btn-default btn-action"><span className="glyphicon glyphicon-pencil"></span></a>
              );

    }else {
            return (
                    <a href={`app/create/create-category-master.html?id=${id}&type=${type}`} className="btn btn-default btn-action"><span className="glyphicon glyphicon-modal-window"></span></a>
            );
        }
}


    const renderBody=function()
    {
      return list.map((item,index)=>
      {
            return (<tr key={index}>
                    <td><center>{index+1}</center></td>
                    <td data-label="code"><center>{item.code}</center></td>
                    <td data-label="name"><center>{item.name}</center></td>
                    <td data-label="active"><center>{item.active?"ACTIVE":"INACTIVE"}</center></td>
                    <td data-label="action"><center>
                    {renderAction(getUrlVars()["type"],item.id)}
                    </center></td>
                </tr>
            );

      })
    }

      return (<div>
        <h3>{titleCase(getUrlVars()["type"])} Category Type </h3>
        <table id="designationTable" className="table table-bordered">
            <thead>
                <tr>
                    <th><center>Sl No.</center></th>
                    <th><center>Code</center></th>

                    <th><center>Category Type</center></th>
                    <th><center>Status</center></th>
                    <th><center>Action</center></th>

                </tr>
            </thead>

            <tbody id="employeeSearchResultTableBody">
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
  <ShowCategoryType />,
  document.getElementById('root')
);
