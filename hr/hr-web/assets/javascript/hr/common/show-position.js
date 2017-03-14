class ShowPosition extends React.Component {
  constructor(props) {
    super(props);
    this.state={list:[],positionSet:{
    department:"",
    designation:"",
    name:"",
    isPostOutsourced:"",active:""},
    }

  }

  componentWillMount()
  {

  }



  componentDidMount()
  {
    console.log(getUrlVars()["type"]);
    this.setState({
      list:getCommonMaster("hr-masters","positions","Position").responseJSON["Position"]
    });
  }

  componentDidUpdate(prevProps, prevState)
  {
      if (prevState.list.length!=this.state.list.length) {
          $('#positionTable').DataTable({
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
    console.log(this.state.positionSet);
    let {list}=this.state;
    let {department,designation,name,isPostOutsourced,active}=this.state.positionSet;

    const renderAction=function(type,name){
      if (type==="update") {

              return (
                      <a href={`../../../../app/hr/master/position.html?name=${name}&type=${type}`} className="btn btn-default btn-action"><span className="glyphicon glyphicon-pencil"></span></a>
              );

    }else {
            return (
                    <a href={`../../../../app/hr/master/position.html?name=${name}&type=${type}`} className="btn btn-default btn-action"><span className="glyphicon glyphicon-modal-window"></span></a>
            );
        }
}


    const renderBody=function()
    {
      return list.map((item,index)=>
      {
            return (<tr key={index}>
                    <td data-label="department">{item.department}</td>
                    <td data-label="designation">{item.designation}</td>
                    <td data-label="name">{item.name}</td>
                    <td data-label="isPostOutsourced">{item.isPostOutsourced}</td>
                    <td data-label="active">{item.active?"true":"false"}</td>

                    <td data-label="action">
                    {renderAction(getUrlVars()["type"],item.name)}
                    </td>
                </tr>
            );

      })
    }

      return (<div>
        <table id="positionTable" className="table table-bordered">
            <thead>
                <tr>
                    <th>Department</th>
                    <th>Designation</th>
                    <th>Position</th>
                    <th>Outsourced post</th>
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
  <ShowPosition />,
  document.getElementById('root')
);
