class DefaultersReport extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            "searchSet": {
              tenantId
            },
            "isSearchClicked": false
        };
        this.handleChange = this.handleChange.bind(this);
        this.search = this.search.bind(this);
        this.closeWindow = this.closeWindow.bind(this);
        this.showTable=this.showTable.bind(this);
    }

    componentDidMount(){
      this.setState({
        assetCategories:commonApiPost("asset-services","assetCategories","_search",{usedForLease:true,tenantId}).responseJSON["AssetCategory"],
        revenueWard:commonApiPost("egov-location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", { boundaryTypeName: "WARD", hierarchyTypeName: "REVENUE", tenantId }).responseJSON["Boundary"]
      });
    }

    handleChange(e, name) {
        this.setState({
            ...this.state,
            searchSet: {
                ...this.state.searchSet,
                [name]: e.target.value
            }
        })
    }

    closeWindow() {
        open(location, '_self').close();
    }

    search(e) {
      let {searchSet} = this.state;
      e.preventDefault();
      console.log(searchSet);
      this.setState({
        searchClicked:true,
        resultSet:[]
      });
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
                <th>Agreement No.</th>
                <th>Name of the Allottee </th>
                <th>Mobile No.</th>
                <th>Locality</th>
                <th>Address</th>
                <th>Outstanding Total Rent</th>
                <th>Current rent pending</th>
                <th>Arrears Pending</th>
                <th>Total pending</th>
                <th>Agreement created date</th>
                <th>Agreement expiry date</th>
              </tr>
            </thead>
            <tbody>
              {resultSet && resultSet.map((list, index)=>{
                return (
                  <tr>
                    <td>{index+1}</td>
                    <td>{list.agreementNumber}</td>
                    <td>{list.allotteeName}</td>
                    <td>{list.mobileNumber}</td>
                    <td>{list.locality}</td>
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
                      filename: 'Renewal Pending Report',
                      orientation: 'landscape',
                      pageSize: 'TABLOID',
                      title: `Report generated on ${moment(new Date()).format("DD/MM/YYYY")}`,
                      footer: true
                    },
                   'print'
           ],
           ordering: false
        });
      }
    }

    render() {
        let { handleChange, search, closeWindow, showTable } = this;
        let {revenueWard, assetCategories, searchClicked} = this.state;

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

        const renderOptionsForDesc = function (list) {
            if (list && list.constructor == Array) {
                return list.map((item) => {
                    return (<option key={item.id} value={item.id}>
                        {item.code}
                    </option>)
                })
            }
        }

        return (
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
                                            <label for="">Revenue ward </label>
                                        </div>
                                        <div className="col-sm-6">
                                            <div className="styled-select">
                                                <select id="revenueward" onChange={(e) => { handleChange(e, "revenueWard") }}>
                                                    <option value="">Select RevenueWard</option>
                                                    {renderOptions(revenueWard)}
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label for="">Not paid from </label>
                                        </div>
                                        <div className="col-sm-6">
                                            <div className="styled-select">
                                                <select id="noOfYears" onChange={(e) => { handleChange(e, "noOfYears") }}>
                                                    <option value="">Select Number of years</option>
                                                    <option value="1Year">1 Year</option>
                                                    <option value="2Year">2 Years</option>
                                                    <option value="3Year">3 Years</option>
                                                    <option value="4Year">4 Years</option>
                                                    <option value="5Year">5 Years</option>
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
                                            <label for="fromRent">From Rent </label>
                                        </div>
                                        <div className="col-sm-6">
                                            <input id="fromRent" type="text" onChange={(e) => { handleChange(e, "fromRent") }} />
                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label for="">To Rent </label>
                                        </div>
                                        <div className="col-sm-6">
                                            <input id="toRent" type="text" onChange={(e) => { handleChange(e, "toRent") }} />
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label for="">Top Defaulters </label>
                                        </div>
                                        <div className="col-sm-6">
                                            <div className="styled-select">
                                                <select id="topDefaulters" onChange={(e) => { handleChange(e, "topDefaulters") }}>
                                                    <option value="">Select Top Defaulters</option>
                                                    <option value="10">10</option>
                                                    <option value="50">50</option>
                                                    <option value="100">100</option>
                                                    <option value="500">500</option>
                                                    <option value="1000">1000</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label for="">Asset category </label>
                                        </div>
                                        <div className="col-sm-6">
                                            <div className="styled-select">
                                                <select id="assetCategory" onChange={(e) => { handleChange(e, "assetCategory") }}>
                                                    <option value="">Select Asset category</option>
                                                    {renderOptions(assetCategories)}
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="text-center">
                                <button type="submit" className="btn btn-submit">Search</button>&nbsp;&nbsp;
                                <button type="button" className="btn btn-close" onClick={(e) => { this.closeWindow() }}>Close</button>
                            </div>
                        </fieldset>
                    </form>
                </div>
                <br />
                {searchClicked && showTable()}
            </div>
        );
    }
}






ReactDOM.render(
    <DefaultersReport />,
    document.getElementById('root')
);
