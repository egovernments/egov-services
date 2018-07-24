class DueNotice extends React.Component {
  constructor(props){
    super(props);
    this.state={
      searchSet:{
        tenantId,
        agreementNumber:null,
        assetCategory:null,
        revenueWard:null,
        zone:null
      }

    };
    this.handleChange=this.handleChange.bind(this);
    this.search=this.search.bind(this);
    this.closeWindow=this.closeWindow.bind(this);
    this.showTable=this.showTable.bind(this);
  }
  componentDidMount(){

    let {handleChange} = this;
    this.setState({
      locality:commonApiPost("egov-location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", { boundaryTypeName: "LOCALITY", hierarchyTypeName: "LOCATION", tenantId }).responseJSON["Boundary"],
      revenueWard:commonApiPost("egov-location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", { boundaryTypeName: "WARD", hierarchyTypeName: "REVENUE", tenantId }).responseJSON["Boundary"],
      revenueBlock:commonApiPost("egov-location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", { boundaryTypeName: "BLOCK", hierarchyTypeName: "REVENUE", tenantId }).responseJSON["Boundary"],
      assetCategories:commonApiPost("asset-services","assetCategories","_search",{usedForLease:true,tenantId}).responseJSON["AssetCategory"]
      });
  }
  handleChange(value, property){
    this.setState({
      searchClicked : false,
      resultSet :[],
      searchSet:Object.assign(this.state.searchSet || {},{[property]:value}),
    })
  }
  search(e){
    let {searchSet} = this.state;
    let _this=this;
    // if(!_this.state.searchSet.assetCategory){
    //   showError("Asset category is required.")
    //   return false;
    // }
      Promise.all([
        commonApiPost("lams-services/agreement","notice","_duenotice",searchSet)
      ]).then(function(response){
        _this.setState({
          searchClicked:true,
          error:{},
          resultSet:response[0].Defaulters
        });
      })


  }

  closeWindow ()
  {
      open(location, '_self').close();
  }
  getName(list, val){
    if(val){
      let filteredObj = list.find(obj=>{return obj.id == val});
      return filteredObj.name || '';
    }
  }
  showTable(){
    let {resultSet, assetCategories, locality, electionWard, revenueWard} = this.state;
    let {getName} = this;
    return(
      <div className="form-section" >
        <h3 className="pull-left">Search Result</h3>
        <div className="clearfix"></div>
        <div className="view-table">
        <table className="table table-bordered">
          <thead>
            <tr>
              <th>S.no</th>
              <th>Agreement Number</th>
              <th>Agreement Type</th>
              <th>Status</th>
              <th>Asset Code</th>
              <th>Asset Category</th>
              <th>Locality</th>
              <th>Revenue Ward</th>
              <th>Allottee Name</th>
              <th>Allottee MobileNo</th>
              <th>Due Notice</th>

            </tr>
          </thead>
          <tbody>
            {resultSet && resultSet.map((list, index)=>{
              return (
                <tr >
                  <td>{index+1}</td>
                  <td>{list.agreementNumber}</td>
                  <td>{list.action}</td>
                  <td>{list.status}</td>
                  <td>{list.assetCode}</td>
                  <td>N/A</td>
                  <td>{getName(locality,list.locality)}</td>
                  <td>{getName(revenueWard,list.revenueWard)} </td>
                  <td>{list.allotteeName}</td>
                  <td>{list.mobileNumber}</td>
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
                    filename: 'Due Notice Report',
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
    console.log(this.state.searchSet);
    let { assetCategories, locality, electionWard, revenueWard, revenueZone, revenueBlock, searchClicked} = this.state;
    let {handleChange, search,closeWindow, showTable} = this;
    const renderOptions = function(list)
    {
        if(list)
          {
            if (list.length) {
              return list.map((item)=>
              {
                  return (<option key={item.id} value={typeof item == "object" ? item.id : item}>
                    {typeof item == "object" ? item.name : item}
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
          <form>
            <fieldset>
              <div className="row">
                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                              <label for="" >Asset Category <span>*</span></label>
                          </div>
                          <div className="col-sm-6">
                              <div className="styled-select">
                                  <select id="assetCategory" required="true" onChange={(e) => { handleChange(e.target.value, "assetCategory") }}>
                                    <option value="">Select Asset Category</option>
                                    {renderOptions(assetCategories)}
                                  </select>
                              </div>
                          </div>
                      </div>
                  </div>
                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                              <label for="">Asset Code</label>
                          </div>
                          <div className="col-sm-6">
                            <input type="text" id="assetNo" onChange={(e) => { handleChange(e.target.value, "assetNo") }}/>
                          </div>
                      </div>
                  </div>
              </div>
              <div className="row">
                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                              <label for="">Agreement Number</label>
                          </div>
                          <div className="col-sm-6">
                            <input type="text" id="agreementNumber" onChange={(e) => { handleChange(e.target.value, "agreementNumber") }}/>
                          </div>
                      </div>
                  </div>
                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                              <label for="">Election Ward</label>
                          </div>
                          <div className="col-sm-6">
                              <div className="styled-select">
                                  <select id="electionWard" onChange={(e) => { handleChange(e.target.value, "electionWard") }}>
                                    <option value="">Select Election Ward</option>
                                    {renderOptions(electionWard)}
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
                              <label for="">Locality</label>
                          </div>
                          <div className="col-sm-6">
                              <div className="styled-select">
                                  <select id="locality" onChange={(e) => { handleChange(e.target.value, "locality") }}>
                                    <option value="">Select Locality</option>
                                    {renderOptions(locality)}
                                  </select>
                              </div>
                          </div>
                      </div>
                  </div>
                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                              <label for="">Revenue Zone</label>
                          </div>
                          <div className="col-sm-6">
                              <div className="styled-select">
                                  <select id="revenueZone" onChange={(e) => { handleChange(e.target.value, "revenueZone") }}>
                                    <option value="">Select Revenue Zone</option>
                                    {renderOptions(revenueZone)}
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
                              <label for="">Revenue Ward</label>
                          </div>
                          <div className="col-sm-6">
                              <div className="styled-select">
                                  <select id="revenueWard" onChange={(e) => { handleChange(e.target.value, "revenueWard") }}>
                                    <option value="">Select Revenue Ward</option>
                                    {renderOptions(revenueWard)}
                                  </select>
                              </div>
                          </div>
                      </div>
                  </div>
                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                              <label for="">Revenue Block</label>
                          </div>
                          <div className="col-sm-6">
                              <div className="styled-select">
                                  <select id="revenueBlock" onChange={(e) => { handleChange(e.target.value, "revenueBlock") }}>
                                    <option value="">Select Revenue Block</option>
                                    {renderOptions(revenueBlock)}
                                  </select>
                              </div>
                          </div>
                      </div>
                  </div>
              </div>
              <div className="row">

              </div>
              <div className="text-center">
                <button type="button" className="btn btn-submit" onClick={(e)=>{search(e)}}>Search</button>  &nbsp;&nbsp;
                <button type="button" className="btn btn-close" onClick={(e)=>{this.closeWindow()}}>Close</button>
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
    <DueNotice />,
    document.getElementById('root')
);
