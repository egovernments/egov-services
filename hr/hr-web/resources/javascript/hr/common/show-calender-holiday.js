class ShowCalenderHoliday extends React.Component {
  constructor(props) {
    super(props);
    this.state={list:[],holidaySet:
        {calendarYear:"",
        name:"",
        applicableOn:""
      },isSearchClicked:false,year:[]
  }
    this.handleChange=this.handleChange.bind(this);
    this.search=this.search.bind(this);

  }


  componentWillMount(){
    this.setState({
    year:getCommonMaster("egov-common-masters","calendaryears","CalendarYear").responseJSON["CalendarYear"] || []
  })
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
          holidaySet:{
              ...this.state.holidaySet,
              [name]:e.target.value
          }
      })

  }


  close(){
      // widow.close();
      open(location, '_self').close();
  }

search(e)
  {
    e.preventDefault();
    //call api call
     var list=commonApiPost("egov-common-masters","holidays","_search",{tenantId,year:this.state.holidaySet.calendarYear}).responseJSON["Holiday"];
      console.log(commonApiPost("egov-common-masters","holidays","_search",{tenantId,year:this.state.holidaySet.calendarYear}).responseJSON["Holiday"]);
    this.setState({
      isSearchClicked:true,
      list
    })
  }

  render() {
    let {handleChange,search}=this;
    let {list,isSearchClicked}=this.state;
    let {name,calendarYear,applicableOn,active}=this.state.holidaySet;


    const renderOption=function(list)
    {
        if(list)
        {
          return list.map((item)=>
          {
              return (<option key={item.id} value={item.name}>
                      {item.name}
                </option>)
          })
        }
    }
    const renderAction=function(type,id){
      if (type==="update") {

              return (
                      <a href={`app/hr/master/calendar-holidays-setup.html?id=${id}&type=${type}`} className="btn btn-default btn-action"><span className="glyphicon glyphicon-pencil"></span></a>
              );

    }else {
            return (
                    <a href={`app/hr/master/calendar-holidays-setup.html?id=${id}&type=${type}`} className="btn btn-default btn-action"><span className="glyphicon glyphicon-modal-window"></span></a>
            );
        }
}

const showTable=function()
    {
      if(isSearchClicked)
      {
          return (
            <table id="employeeTable" className="table table-bordered">
                <thead>
                    <tr>
                      <th>Sl NO.</th>
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

            </table>  )
      }
  }

    const renderBody=function()
    {
      return list.map((item,index)=>
      {
            return (<tr key={index}>
                    <td>{index+1}</td>
                    <td data-label="applicableOn">{item.applicableOn}</td>
                    <td data-label="name">{item.name}</td>
                    <td data-label="active">{item.active?item.active:"true"}</td>
                    <td data-label="action">
                    {renderAction(getUrlVars()["type"],item.id)}
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
                    <select id="calendarYear" name="calendarYear" required="true" value={calendarYear}
                    onChange={(e)=>{handleChange(e,"calendarYear")}}>
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
        <div className="table-cont" id="table">
              {showTable()}

          </div>
      </div>
    );
  }
}

ReactDOM.render(
  <ShowCalenderHoliday />,
  document.getElementById('root')
);
