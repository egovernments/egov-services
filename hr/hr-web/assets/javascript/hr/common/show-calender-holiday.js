class ShowCalenderHoliday extends React.Component {
  constructor(props) {
    super(props);
    this.state={list:[],holiday:
        {CalendarYear:"",
        name:"",
        applicableOn:""
      },year:[]
  }
    this.handleChange=this.handleChange.bind(this);
    this.search=this.search.bind(this);

  }


  componentWillMount(){
    console.log(getUrlVars()["type"]);
    var year=[{id:"2017",name:"2017"},{id:"2016",name:"2016"},{id:"2015",name:"2015"},{id:"2014",name:"2014"}];
    this.setState({year})

  }

  componentDidMount()
  {

  }

  componentDidUpdate(prevProps, prevState)
  {
      if (prevState.list.length!=this.state.list.length) {
          // $('#employeeTable').DataTable().draw();
          // alert(prevState.list.length);
          // alert(this.state.list.length);
          // alert('updated');
          $('#employeeTable').DataTable({
            dom: 'Bfrtip',
            buttons: [
                     'copy', 'csv', 'excel', 'pdf', 'print'
             ],
             ordering: false
          });
      }
  }

  handleChange(e,name)
  {

      this.setState({
          holiday:{
              ...this.state.holiday,
              [name]:e.target.value
          }
      })

  }


  close(){
      // widow.close();
      open(location, '_self').close();
  }

search(e){
  let {name,CalendarYear,applicableOn,active}=this.state.holiday;
    e.preventDefault();
    this.setState({
      list:getCommonMaster("egov-common-masters","holidays","Holiday").responseJSON["Holiday"]
    });

}


  render() {
    console.log(this.state.holiday);
    let {handleChange,search}=this;
    let {list}=this.state;
    let {name,CalendarYear,applicableOn,active}=this.state.holiday;
    const renderOption=function(list)
    {
        if(list)
        {
            return list.map((item)=>
            {
                return (<option key={item.id} value={item.id}>
                        {item.name}
                  </option>)
            })
        }
    }

    const renderAction=function(type,name){
      if (type==="update") {

              return (
                      <a href={`../../../../app/hr/master/calendar-holidays-setup.html?name=${name}&type=${type}`} className="btn btn-default btn-action"><span className="glyphicon glyphicon-pencil"></span></a>
              );

    }else {
            return (
                    <a href={`../../../../app/hr/master/calendar-holidays-setup.html?name=${name}&type=${type}`} className="btn btn-default btn-action"><span className="glyphicon glyphicon-modal-window"></span></a>
            );
        }
}



    const renderBody=function()
    {
      return list.map((item,index)=>
      {
            return (<tr key={index}>

                    <td data-label="applicableOn">{item.applicableOn}</td>
                    <td data-label="name">{item.name}</td>
                    <td data-label="active">{item.active?"true":"false"}</td>
                    <td data-label="action">
                    {renderAction(getUrlVars()["type"],item.name)}
                    </td>
                </tr>
            );
      })
    }

      return (<div>
        <form onSubmit={(e)=>{search(e)}}>
        <fieldset>
        <div className="row">
          <div className="col-sm-6 col-sm-offset-3">
              <div className="row">
                  <div className="col-sm-6 label-text">
                    <label for="">Calendar Year <span>*</span></label>
                  </div>
                  <div className="col-sm-6">
                    <div className="styled-select">
                    <select id="CalendarYear" name="CalendarYear" value={CalendarYear}
                    onChange={(e)=>{handleChange(e,"CalendarYear")}}required>
                    <option>Select Year</option>
                    {renderOption(this.state.year)}
                   </select>
                    </div>
                  </div>
              </div>
            </div>
        </div>
        <br/>
        <br/>
        <br/>
        <div className="text-center">
            <button type="submit"  className="btn btn-submit">Search</button>
        </div>
      </fieldset>
      </form>

        <br/>
        <br/>
        <br/>
        <table id="employeeTable" className="table table-bordered">
            <thead>
                <tr>
                    <th>Holiday Date</th>
                    <th>Holiday Name</th>
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
  <ShowCalenderHoliday />,
  document.getElementById('root')
);
