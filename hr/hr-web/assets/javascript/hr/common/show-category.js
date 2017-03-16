class ShowCategory extends React.Component {
  constructor(props) {
    super(props);
    this.state={list:[],categorySet:{
      name:"",
      description:"",
      active:""
    }
  }
}

  componentWillMount()
  {
    console.log(getUrlVars()["type"]);
}

  componentDidMount()
  {
    this.setState({
        list:getCommonMaster("egov-common-masters","categories","Category").responseJSON["Category"]
  });
  }

  componentDidUpdate(prevProps, prevState)
  {
      if (prevState.list.length!=this.state.list.length) {
          // $('#employeeTable').DataTable().draw();
          // alert(prevState.list.length);
          // alert(this.state.list.length);
          // alert('updated');
          $('#categoryTable').DataTable({
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
    console.log(this.state.categorySet);
    let {list}=this.state;
    let {name,description,active}=this.state.categorySet;

    const renderAction=function(type,id){
      if (type==="update") {

              return (
                      <a href={`../../../../app/hr/master/category.html?id=${id}&type=${type}`} className="btn btn-default btn-action"><span className="glyphicon glyphicon-pencil"></span></a>
              );

    }else {
            return (
                    <a href={`../../../../app/hr/master/category.html?id=${id}&type=${type}`} className="btn btn-default btn-action"><span className="glyphicon glyphicon-modal-window"></span></a>
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
        <table id="categoryTable" className="table table-bordered">
            <thead>
                <tr>
                    <th>Sl No.</th>
                    <th>Category Name</th>
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
  <ShowCategory />,
  document.getElementById('root')
);
