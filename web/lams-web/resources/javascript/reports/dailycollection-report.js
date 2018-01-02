class DailyCollection extends React.Component {
  constructor(props){
    super(props);
    this.state={
      searchSet:{
        tenantId
      },
      searchClicked:false,
      error:{

      }
    };
    this.handleChange=this.handleChange.bind(this);
    this.search=this.search.bind(this);
    this.closeWindow=this.closeWindow.bind(this);
    this.showTable=this.showTable.bind(this);
  }
  componentDidMount(){

    let {handleChange} = this;

    $('.datePicker').datepicker({
        format: 'dd/mm/yyyy',
        autoclose: true
    }).on('changeDate', function (e) {
      handleChange(e.target.value, e.target.id)
    });

    this.setState({
      revenueWard:commonApiPost("egov-location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", { boundaryTypeName: "WARD", hierarchyTypeName: "REVENUE", tenantId }).responseJSON["Boundary"]
    });

  }

  handleChange(value, property){
    this.setState({
      searchClicked : false,
      searchSet:Object.assign(this.state.searchSet || {},{[property]:value}),
    });
  }
  search(e){
    e.preventDefault();
    let {searchSet} = this.state;
    let _this=this;
    this.setState({
      searchClicked:true,
      resultSet:[]
    });
    // Promise.all([
    //   commonApiPost("lams-services/agreements","reports","_baseregisterreport",searchSet)
    // ]).then(function(responses){
    //   _this.setState({
    //     searchClicked:true,
    //     error:{},
    //     resultSet:responses[0].Agreements
    //   });
    // })
  }
  closeWindow ()
  {
      open(location, '_self').close();
  }
  showTable(){
    let {resultSet} = this.state;
    return(
      <div className="form-section" >
        <h3 className="pull-left">Search Result</h3>
        <div className="clearfix"></div>
        <div className="view-table">
        <table className="table table-bordered">
          <thead>
            <tr>
              <th>S.no</th>
              <th>Receipt Number</th>
              <th>Receipt Date</th>
              <th>Agreement Number</th>
              <th>Owner name</th>
              <th>Ward</th>
              <th>Paid at</th>
              <th>Payment mode</th>
              <th>Status</th>
              <th>Paid From</th>
              <th>Paid To</th>
              <th>Arrear amount</th>
              <th>Current amount</th>
              <th>Penalty</th>
              <th>Service tax</th>
              <th>Total amount</th>
            </tr>
          </thead>
          <tbody>
            {resultSet && resultSet.map((list, index)=>{
              return (
                <tr>
                  <td>{index+1}</td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                </tr>
              )
            })}
          </tbody>
        </table>
        </div>
      </div>
    )
  }
  componentDidUpdate(prevProps, prevState)
  {
    let {searchClicked} = this.state;
    if(searchClicked){
      $('table').DataTable({
        dom: 'Bfrtip',
        buttons: [
                 'excel',
                 {
                    extend: 'pdf',
                    filename: 'Base Register Report',
                    title: `Report generated on ${moment(new Date()).format("DD/MM/YYYY")}`
                  },
                 'print'
         ],
         ordering: false
      });
    }
  }
  render(){
    // console.log(this.state.searchSet);
    let {assetCategories, revenueWard, searchClicked} = this.state;
    let {handleChange, search, closeWindow, showTable} = this;
    const renderOptions = function(list)
    {
        if(list)
        {
            if (list.length) {
              return list.map((item)=>
              {
                  return (<option key={item.id} value={item.id}>
                          {item.name}
                    </option>)
              })

            } else {
              return Object.keys(list).map((k, index)=>
              {
                return (<option key={index} value={k}>
                        {list[k]}
                  </option>)

               })
            }

        }
    }

    return(
      <div>
        <div className="form-section">
          <h3 className="pull-left">Search Criteria</h3>
          <div className="clearfix"></div>
          <form onSubmit={(e) => { search(e) }}>
            <fieldset>
              <div className="row">
                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                              <label for="">From Date</label>
                          </div>
                          <div className="col-sm-6">
                            <input type="text" id="fromDate" className="datePicker" onChange={(e) => { handleChange(e.target.value, "fromDate") }} />
                          </div>
                      </div>
                  </div>
                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                              <label for="">To Date</label>
                          </div>
                          <div className="col-sm-6">
                            <input type="text" id="toDate" className="datePicker" onChange={(e) => { handleChange(e.target.value, "toDate") }}/>
                          </div>
                      </div>
                  </div>
              </div>
              <div className="row">
                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                              <label for="">Collection Mode</label>
                          </div>
                          <div className="col-sm-6">
                              <div className="styled-select">
                                  <select id="collectionMode" onChange={(e) => { handleChange(e.target.value, "collectionMode") }}>
                                    <option value="">Select Collection Mode</option>
                                  </select>
                              </div>
                          </div>
                      </div>
                  </div>
                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                              <label for="">Collection Operator</label>
                          </div>
                          <div className="col-sm-6">
                              <div className="styled-select">
                                  <select id="collecionOperator" onChange={(e) => { handleChange(e.target.value, "collecionOperator") }}>
                                    <option value="">Select Collection Operator</option>
                                  </select>
                              </div>
                          </div>
                      </div>
                  </div>
              </div>
              <div className="row">
                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                              <label for="">Status</label>
                          </div>
                          <div className="col-sm-6">
                              <div className="styled-select">
                                  <select id="status" onChange={(e) => { handleChange(e.target.value, "status") }}>
                                    <option value="">Select Status</option>
                                  </select>
                              </div>
                          </div>
                      </div>
                  </div>
                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                              <label for="">Revenue ward</label>
                          </div>
                          <div className="col-sm-6">
                              <div className="styled-select">
                                  <select id="revenueWard" onChange={(e) => { handleChange(e.target.value, "revenueWard") }}>
                                    <option value="">Select RevenueWard</option>
                                    <option value="all">All</option>
                                    {renderOptions(revenueWard)}
                                  </select>
                              </div>
                              <label className="error">{this.state.error.revenueWard}</label>
                          </div>
                      </div>
                  </div>
              </div>
              <div className="text-center">
                <button type="submit" className="btn btn-submit">Search</button>  &nbsp;&nbsp;
                <button type="button" className="btn btn-submit" onClick={(e)=>{this.closeWindow()}}>Close</button>
              </div>
            </fieldset>
          </form>
        </div>
        <div className="table-cont" id="table">
          {searchClicked && showTable()}
        </div>
      </div>
    )
  }
}

ReactDOM.render(
    <DailyCollection />,
    document.getElementById('root')
);
