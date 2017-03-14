
class ShowGrade extends React.Component {
  constructor(props) {
    super(props);
    this.state={grades:[],gradeSet:{
        name:"",
        description:"",
        orderNo:"",
        active:""
      }
  }

  }

  componentWillMount()
  {
    console.log(getUrlVars()["type"]);
    // console.log(getCommonMaster("hr-masters","grades","Grade").responseJSON["Grade"]);


  }



  componentDidMount()
  {
    this.setState({
      grades:getCommonMaster("hr-masters","grades","Grade").responseJSON["Grade"]
    });
  }

  componentDidUpdate(prevProps, prevState)
  {
      if (prevState.grades.length!=this.state.grades.length) {
          // $('#employeeTable').DataTable().draw();
          // alert(prevState.grades.length);
          // alert(this.state.grades.length);
          // alert('updated');
          $('#gradeTable').DataTable({
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
    console.log(this.state.gradeSet);
    let {isSearchClicked,grades}=this.state;
    let {name,description,orderNo,active}=this.state.gradeSet;

    const renderAction=function(type,id){
      if (type==="update") {

              return (
                      <a href={`../../../../app/hr/master/grade-master.html?id=${id}&type=${type}`} className="btn btn-default btn-action"><span className="glyphicon glyphicon-pencil"></span></a>
              );

    }else {
            return (
                    <a href={`../../../../app/hr/master/grade-master.html?id=${id}&type=${type}`} className="btn btn-default btn-action"><span className="glyphicon glyphicon-modal-window"></span></a>
            );
        }
}


    const renderBody=function()
    {
      return grades.map((item,index)=>
      {
            return (<tr key={index}>
                    <td data-label="name">{item.name}</td>
                    <td data-label="description">{item.description}</td>
                    <td data-label="orderNo">{item.orderNo}</td>
                    <td data-label="active">{item.active?"true":"false"}</td>
                    <td data-label="action">
                    {renderAction(getUrlVars()["type"],item.id)}
                    </td>
                </tr>
            );

      })
    }

      return (<div>
        <table id="gradeTable" className="table table-bordered">
            <thead>
                <tr>
                    <th>Name</th>
                    <th>Description</th>
                    <th>Order No</th>
                    <th>Active</th>
                    <th>Action</th>
                </tr>
            </thead>

            <tbody id="gradesearchResultTableBody">
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
  <ShowGrade />,
  document.getElementById('root')
);
