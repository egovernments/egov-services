

class ShowGrade extends React.Component {
  constructor(props) {
    super(props);
    this.state={grades:[],gradeSet:{
        name:"",
        description:"",
        orderno:"",
        active:""
    }
  }

  }

  componentWillMount()
  {
    console.log(getUrlVars()["type"]);
    // console.log(getCommonMaster("hr-masters","grades","Grade").responseJSON["Grade"]);

    this.setState({
      grades:getCommonMaster("hr-masters","grades","Grade").responseJSON["Grade"]
    });
  }



  componentDidMount()
  {
  
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
    let {handleChange,search,updateTable}=this;
    let {isSearchClicked,grades}=this.state;
    let {name,description,orderno,action}=this.state.gradeSet;

    const renderAction=function(type,name){
      if (type==="update") {

              return (
                      <a href={`../../../../app/hr/master/grade-master.html?name=${name}&type=${type}`} className="btn btn-default btn-action"><span className="glyphicon glyphicon-pencil"></span></a>
              );

    }else {
            return (
                    <a href={`../../../../app/hr/master/grade-master.html?name=${name}&type=${type}`} className="btn btn-default btn-action"><span className="glyphicon glyphicon-modal-window"></span></a>
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
                    <td data-label="orderno">{item.orderNo}</td>
                    <td data-label="action">
                    {renderAction(getUrlVars()["type"],item.name)}
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
