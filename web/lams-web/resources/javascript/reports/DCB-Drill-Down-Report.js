class DcbReport extends React.Component {
  constructor(props){
    super(props);
    this.state={
      searchSet:{
        tenantId
      },
      error:{

      }
    };
    this.handleChange=this.handleChange.bind(this);
    this.search=this.search.bind(this);
    this.closeWindow=this.closeWindow.bind(this);
    this.showTable=this.showTable.bind(this);
  }
  componentDidMount(){
    this.setState({
      assetCategories:commonApiPost("asset-services","assetCategories","_search",{usedForLease:true,tenantId}).responseJSON["AssetCategory"],
      revenueWard:commonApiPost("egov-location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", { boundaryTypeName: "WARD", hierarchyTypeName: "REVENUE", tenantId }).responseJSON["Boundary"]
    });
  }
  handleChange(value, property){
    this.setState({
      searchClicked : false,
      searchSet:Object.assign(this.state.searchSet || {},{[property]:value}),
    })
  }
  search(e){
    e.preventDefault();
    let {searchSet} = this.state;
    this.setState({
      searchClicked:true,
      resultSet:[]
    });
    // let _this=this;
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
              <th>Name of the Allottee </th>
              <th>No. of Agreements</th>
              <th>Arrears rent (Demand)</th>
              <th>Current Rent(Demand)</th>
              <th>Total Demand</th>
              <th>Arrears rent (Collections)</th>
              <th>Current Rent (Collections)</th>
              <th>Total Collections</th>
              <th>Arrears Rent (Balance)</th>
              <th>Current Rent ( Balance)</th>
              <th>Total Balance</th>
            </tr>
          </thead>
          <tbody>
            {resultSet && resultSet.map((list, index)=>{
              return (
                <tr>
                  <td>{index+1}</td>
                  <td>{list.allotteeName}</td>
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
  componentWillUpdate(){
    $('table').DataTable().destroy();
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
  componentWillUnmount(){

  }
  render(){
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
                              <label for="">Revenue ward <span className="error"> *</span></label>
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
                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                              <label for="">Asset Category</label>
                          </div>
                          <div className="col-sm-6">
                              <div className="styled-select">
                                  <select id="assetCategory" onChange={(e) => { handleChange(e.target.value, "assetCategory") }}>
                                    <option value="">Select Asset Category</option>
                                    {renderOptions(assetCategories)}
                                  </select>
                              </div>
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
    <DcbReport />,
    document.getElementById('root')
);
