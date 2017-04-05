class ShowCalender extends React.Component {
  constructor(props) {
    super(props);
    this.state={list:[],calenderSet:{
        name:"",
        startDate:"",
        endDate:"",
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
            list:getCommonMaster("egov-common-masters","calendaryears","CalendarYear").responseJSON["CalendarYear"]
        });
  }

  componentDidUpdate(prevProps, prevState)
  {
      if (prevState.list.length!=this.state.list.length) {

          $('#calenderTable').DataTable({
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
    console.log(this.state.calenderSet);
    let {list}=this.state;
    let {name,startDate,endDate,active}=this.state.calenderSet;


    const renderAction=function(type,id){
      if (type==="update") {

              return (
                      <a href={`app/hr/master/calendar-setup.html?id=${id}&type=${type}`} className="btn btn-default btn-action"><span className="glyphicon glyphicon-pencil"></span></a>
              );

    }else {
            return (
                    <a href={`app/hr/master/calendar-setup.html?id=${id}&type=${type}`} className="btn btn-default btn-action"><span className="glyphicon glyphicon-modal-window"></span></a>
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
                    <td data-label="startDate">{item.startDate}</td>
                    <td data-label="endDate">{item.endDate}</td>
                    <td data-label="active">{item.active?"true":"false"}</td>
                    <td data-label="action">
                    {renderAction(getUrlVars()["type"],item.id)}
                    </td>
                </tr>
            );

      })
    }

      return (<div>
        <table id="calenderTable" className="table table-bordered">
            <thead>
                <tr>
                    <th>Sl No.</th>
                    <th>Year</th>
                    <th>From date</th>
                    <th>To date</th>
                    <th> Active </th>
                    <th>Action</th>

                </tr>
            </thead>

            <tbody id="calenderTableBody">
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
  <ShowCalender />,
  document.getElementById('root')
);
