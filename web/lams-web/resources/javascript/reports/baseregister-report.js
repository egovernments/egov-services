class BaseRegister extends React.Component {
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
      revenueWard:commonApiPost("egov-location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", { boundaryTypeName: "WARD", hierarchyTypeName: "REVENUE", tenantId }).responseJSON["Boundary"],
      locality:commonApiPost("egov-location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", { boundaryTypeName: "LOCALITY", hierarchyTypeName: "LOCATION", tenantId }).responseJSON["Boundary"],
      electionWards:commonApiPost("egov-location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", { boundaryTypeName: "WARD", hierarchyTypeName: "ADMINISTRATION", tenantId }).responseJSON["Boundary"]
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
    let _this=this;
    if(!searchSet['revenueWard']){
      this.setState({
        ...this.error,
        error:{
          revenueWard:'Required'
        }
      })
    }else{
      Promise.all([
        commonApiPost("lams-services/agreements","reports","_baseregisterreport",searchSet)
      ]).then(function(responses){
        _this.setState({
          searchClicked:true,
          error:{},
          resultSet:responses[0].Agreements
        });
      })
    }
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
        <table className="table table-bordered" style={{'height': '300px', 'overflow':'scroll', 'display': 'block'}}>
          <thead >
            <tr>
              <th>S.no</th>
              <th>Asset Category</th>
              <th>Asset Name</th>
              <th>Agreement No.</th>
              <th>Name of the Allottee </th>
              <th>Mobile Number</th>
              <th>GSTIN</th>
              <th>CR Number & Date</th>
              <th>Date of Allotment</th>
              <th>Basis of Allotment</th>
              <th>Method of Renewal</th>
              <th>Category</th>
              <th>Shop No./ Survey No.</th>
              <th>Door No.</th>
              <th>Locality</th>
              <th>Revenue Ward</th>
              <th>Election Ward</th>
              <th>Area of the Asset(Sq.ft)</th>
              <th>Agreement Time Period</th>
              <th>Agreement Status </th>
              <th>Monthly Rent</th>
              <th>Total Demand</th>
              <th>Total Penalty</th>
              <th>Total Collection</th>
              <th>Total Balance</th>
            </tr>
          </thead>
          <tbody >
            {resultSet && resultSet.map((list, index)=>{
              return (
                <tr>
                  <td>{index+1}</td>
                  <td>{list.assetCategory}</td>
                  <td>{list.assetName}</td>
                  <td>{list.agreementNumber}</td>
                  <td>{list.allotteeName}</td>
                  <td>{list.mobileNumber}</td>
                  <td>{list.gstin}</td>
                  <td>{list.councilResolutionNumberDate  || "N/A"}</td>
                  <td>{list.commencementDate}</td>
                  <td>{list.basisOfAllotment}</td>
                  <td>{list.methodOfRenewal || "N/A"}</td>
                  <td>{list.reservationCategory}</td>
                  <td>{list.shopNo || "N/A"}</td>
                  <td>{list.doorno  || "N/A"}</td>
                  <td>{list.locality || "N/A"}</td>
                  <td>{list.revenueward  || "N/A"}</td>
                  <td>{list.electionward  || "N/A"}</td>
                  <td>{list.assetArea  || "N/A"}</td>
                  <td>{list.timePeriod}</td>
                  <td>{list.status}</td>
                  <td>{list.monthlyRent}</td>
                  <td>{list.demand}</td>
                  <td>{list.penalty}</td>
                  <td>{list.collection}</td>
                  <td>{list.balance}</td>
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
    let {assetCategories, revenueWard,locality, electionWards, searchClicked} = this.state;
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
              <div className="row">
                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                              <label for="">Locality </label>
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
                              <label for="">Election Ward</label>
                          </div>
                          <div className="col-sm-6">
                              <div className="styled-select">
                                  <select id="electionWard" onChange={(e) => { handleChange(e.target.value, "electionWard") }}>
                                    <option value="">Select ElectionWard</option>
                                    {renderOptions(electionWards)}
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
                              <label for="">Agreement Number</label>
                          </div>
                          <div className="col-sm-6">
                            <input type="text" id="agreementNo" onChange={(e) => { handleChange(e.target.value, "agreementNo") }}/>
                          </div>
                      </div>
                  </div>
                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                              <label for="">Old Agreement No</label>
                          </div>
                          <div className="col-sm-6">
                            <input type="text" id="oldAgreementNo" onChange={(e) => { handleChange(e.target.value, "oldAgreementNo") }}/>
                          </div>
                      </div>
                  </div>
              </div>
              <div className="row">
                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                              <label for="">CR Number</label>
                          </div>
                          <div className="col-sm-6">
                            <input type="text" id="councilResolutionNo" onChange={(e) => { handleChange(e.target.value, "councilResolutionNo") }}/>
                          </div>
                      </div>
                  </div>
                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                              <label for="">Aadhar Number</label>
                          </div>
                          <div className="col-sm-6">
                            <input type="text" id="aadharNo" onChange={(e) => { handleChange(e.target.value, "aadharNo") }}/>
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
    <BaseRegister />,
    document.getElementById('root')
);
