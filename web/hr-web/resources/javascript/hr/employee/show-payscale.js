class ShowGrade extends React.Component {
  constructor(props) {
    super(props);
    this.state={
      grades:[]
  }

  }


  componentDidMount () {
    if(window.opener && window.opener.document) {
       var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
       if(logo_ele && logo_ele[0]) {
         document.getElementsByClassName("homepage_logo")[0].src = window.location.origin + logo_ele[0].getAttribute("src");
       }
     }

    $('#hp-citizen-title').text(titleCase(getUrlVars()["type"]) + " Grade");
    var _this = this;
    getDropdown("assignments_grade", function(res) {
      _this.setState({
        grades: res
      })
    })
  }


  componentDidUpdate(prevProps, prevState) {
    if (prevState.grades.length!=this.state.grades.length) {
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
    open(location, '_self').close();
  }


  render() {
    let {grades}=this.state;

    const renderAction=function(type,id){
      if (type==="update") {

              return (
                      <a href={`app/hr/master/grade-master.html?id=${id}&type=${type}`} className="btn btn-default btn-action"><span className="glyphicon glyphicon-pencil"></span></a>
              );

    }else {
            return (
                    <a href={`app/hr/master/grade-master.html?id=${id}&type=${type}`} className="btn btn-default btn-action"><span className="glyphicon glyphicon-modal-window"></span></a>
            );
        }
}


    const renderBody=function()
    {
      return grades.map((item,index)=>
      {
            return (<tr key={index}>
                    <td>{index+1}</td>
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
        <h3>{titleCase(getUrlVars()["type"])} Grade</h3>
        <table id="gradeTable" className="table table-bordered">
            <thead>
                <tr>
                    <th>Sl No.</th>
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
        <div className="text-center">
            <button type="button" className="btn btn-close"onClick={(e)=>{this.close()}}>Close</button>
        </div>
      </div>
    );
  }
}

ReactDOM.render(
  <ShowGrade />,
  document.getElementById('root')
);
