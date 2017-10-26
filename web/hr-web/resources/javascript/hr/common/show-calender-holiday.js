var flag = 0;
class ShowCalenderHoliday extends React.Component {
  constructor(props) {
    super(props);
    this.state={list:[],holidaySet: {
              "calendarYear": {
                  "name": ""
              },
              "name": "",
              "holidayType":{
                "id":""
              },
              "applicableOn": "",
              "tenantId": tenantId
          },isSearchClicked:false,year:[]
  }
    this.handleChange=this.handleChange.bind(this);
    this.search=this.search.bind(this);

  }

  componentDidUpdate(prevProps, prevState) {
      if (prevState.list.length!=this.state.list.length) {
          $('#calenderTable').DataTable({
            dom: 'Bfrtip',
            buttons: [
                     'copy', 'csv', 'excel', 'pdf', 'print'
             ],
             ordering: false,
             bDestroy: true,
             language: {
              "emptyTable": "No Records"
            }
          });
      }
  }

  componentDidMount() {
    if(window.opener && window.opener.document) {
       var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
       if(logo_ele && logo_ele[0]) {
         document.getElementsByClassName("homepage_logo")[0].src = window.location.origin + logo_ele[0].getAttribute("src");
       }
    }
    $('#hp-citizen-title').text(titleCase(getUrlVars()["type"]) + " Holiday");

    var _this = this;

    if(getUrlVars()["type"]&& getUrlVars()["type"]==="view"){
        getDropdown("years", function(res) {
          _this.setState({
            year: res
          })
        })
    }else {
      getDropdown("futureyears", function(res) {
        _this.setState({
          year: res
        })
      })
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

  componentWillUpdate() {
    if(flag == 1) {
      flag = 0;
      $('#calenderTable').dataTable().fnDestroy();
    }
  }

  close(){
      // widow.close();
      open(location, '_self').close();
  }

  search(e) {
    e.preventDefault();
    var _this = this;
    commonApiPost("egov-common-masters","holidays","_search",{
      tenantId, year: this.state.holidaySet.calendarYear, pageSize:500
    }, function(err, res) {
      if(res) {
        flag = 1;
        _this.setState({
          isSearchClicked: true,
          list: res.Holiday
        });
      }
    });
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
            <table id="calenderTable" className="table table-bordered">
                <thead>
                    <tr>
                      <th>Sl NO.</th>
                        <th>Holiday Date</th>
                        <th>Holiday Name</th>
                        <th>Holiday Type</th>
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
                    <td data-label="name">{item.holidayType.name}</td>
                    <td data-label="action">
                    {renderAction(getUrlVars()["type"],item.id)}
                    </td>
                </tr>
            );
      })
    }

      return (<div>
        <h3>{titleCase(getUrlVars()["type"])} Calender Holiday Setup</h3>
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
                    <option value= "">Select Year</option>
                    {renderOption(this.state.year)}
                   </select>
                    </div>
                  </div>
              </div>
            </div>
        </div>
        <br/>
        <br/>
        <div className="text-center">
            <button type="submit"  className="btn btn-submit">Search</button>&nbsp;&nbsp;
            <button type="button" className="btn btn-close" onClick={(e)=>{this.close()}}>Close</button>
        </div>
      </fieldset>
      </form>


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
