class ShowDesignation extends React.Component {
  constructor(props) {
    super(props);
    this.state={list:[],designationSet:{name:"",
    code:"",description:"", chartOfAccounts:"",active:""}}

  }

  componentWillMount()
  {console.log(getUrlVars()["type"]);}



  componentDidMount()
  {
    this.setState({
      list:getCommonMaster("hr-masters","designations","Designation").responseJSON["Designation"]
    });

  }

  componentDidUpdate(prevProps, prevState)
  {
      if (prevState.list.length!=this.state.list.length) {

          $('#designationTable').DataTable({
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
    console.log(this.state.designationSet);
    let {list}=this.state;
    let {name,code,description,active}=this.state.designationSet;

    const renderAction=function(type,id){
      if (type==="update") {

              return (
                      <a href={`app/hr/master/designation.html?id=${id}&type=${type}`} className="btn btn-default btn-action"><span className="glyphicon glyphicon-pencil"></span></a>
              );

    }else {
            return (
                    <a href={`app/hr/master/designation.html?id=${id}&type=${type}`} className="btn btn-default btn-action"><span className="glyphicon glyphicon-modal-window"></span></a>
            );
        }
}


    const renderBody=function()
    {
      return list.map((item,index)=>
      {
            return (<tr key={index}>
                    <td>{index+1}</td>
                    <td data-label="name">{item.name}</td>
                    <td data-label="code">{item.code}</td>
                    <td data-label="description">{item.description}</td>
                    <td data-label="active">{item.active?"true":"false"}</td>
                    <td data-label="action">
                    {renderAction(getUrlVars()["type"],item.id)}
                    </td>
                </tr>
            );

      })
    }

      return (<div>
        <table id="designationTable" className="table table-bordered">
            <thead>
                <tr>
                    <th>Sl No.</th>
                    <th>Name</th>
                    <th>Code</th>
                    <th>Description</th>
                    <th>Active</th>
                    <th>Action</th>

                </tr>
            </thead>

            <tbody id="employeeSearchResultTableBody">
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
  <ShowDesignation />,
  document.getElementById('root')
);
