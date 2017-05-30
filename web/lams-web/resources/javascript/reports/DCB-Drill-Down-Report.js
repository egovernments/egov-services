
class DcbReport extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
        "list": [],
        "searchSet": {
            "assetCategoryType": "",
            "electionWard": "",
            "locality": "",
            tenantId
        },
        "assetCategories": [],
        "electionwards": [],
        "localityList":[],

        "isSearchClicked": false
    };
    this.handleChange = this.handleChange.bind(this);
    this.search = this.search.bind(this);
    this.closeWindow = this.closeWindow.bind(this);
  }


  componentDidMount() {
     this.setState({
         ...this.state,
        assetCategories:commonApiPost("asset-services","assetCategories","_search",{tenantId}).responseJSON["AssetCategory"],
        electionwards: commonApiPost("v1/location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", {
            boundaryTypeName: "WARD",
            hierarchyTypeName: "ADMINISTRATION",
            tenantId
        }).responseJSON["Boundary"] || [],
        localityList: commonApiPost("v1/location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", {
            boundaryTypeName: "LOCALITY",
            hierarchyTypeName: "LOCATION",
            tenantId
        }).responseJSON["Boundary"] || []

     });
  }

  componentDidUpdate(prevProps, prevState)
  {
      if (prevState.list.length!=this.state.list.length) {
          // $('#agreementTable').DataTable().draw();
          // alert(prevState.list.length);
          // alert(this.state.list.length);
          // alert('updated');
          $('#agreementTable').DataTable({
            dom: 'Bfrtip',
            buttons: [
                     'copy', 'csv', 'excel', 'pdf', 'print'
             ],
             ordering: false
          });
      }
      else {
        $('#agreementTable').DataTable({
          dom: 'Bfrtip',
          buttons: [
                   'copy', 'csv', 'excel', 'pdf', 'print'
           ],
           ordering: false
        });
      }
  }


  handleChange(e, name)
  {
      this.setState({
          ...this.state,
          searchSet:{
              ...this.state.searchSet,
              [name]:e.target.value
          }
      })
  }

  closeWindow ()
  {
      open(location, '_self').close();
  }

  search(e)
  {
    e.preventDefault();
    //call api call
    var list=commonApiPost("asset-services","assets","_search",this.state.searchSet).responseJSON["Assets"];
    this.setState({
      isSearchClicked:true,
      list
    })

    // $('#agreementTable').DataTable().draw();
    // console.log($('#agreementTable').length);

  }


  render() {
    let {handleChange, search, closeWindow} = this;
    let {assetCategories, electionwards,list,localityList,isSearchClicked} = this.state;
    let {assetCategoryType,electionWard,locality} = this.state.searchSet;

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


    const renderBody=function()
    {
      if(list)
      {
        return list.map((item,index)=>
        {
              return (<tr key={index}>
                <td>{index+1}</td>
                                  <td>{item.boundarys}</td>
                                  <td>{item.name}</td>
                                  <td>{item.code}</td>
                                  <td>{item.locationDetails.electionWard}</td>
                                  <td>
                                      ok
                                  </td>

                  </tr>
              );

        })
      }
      else {
          return (
              <tr>
                  <td colSpan="6">No records</td>
              </tr>
          )
      }


    }

    const showTable = () => {
        if(isSearchClicked) {
            return (
                <div className="form-section" >
                    <h3 className="pull-left">Employee Details </h3>
                    <div className="clearfix"></div>
                    <div className="view-table">
                    <table>
                    <thead>
                                             <tr>
                                                 <th > Boundary</th>
                                                 <th >No. Of Agreement</th>
                                                 <th >Asset Type</th>
                                                 <th colSpan="3">Demand</th>
                                                 <th >Collection</th>
                                                 <th > Balance</th>
                                              </tr>

                                             <tr>
                                                 <th></th>
                                                 <th></th>
                                                 <th></th>

                                                 <th>Arrears</th>
                                                 <th>Current</th>
                                                 <th>Total</th>

                                                 <th></th>
                                                 <th></th>

                                            </tr>
                                             </thead>

                            <tbody id="agreementSearchResultTableBody">
                                {
                                    renderBody()
                                }
                            </tbody>
                        </table>
                    </div>
                </div>
            )
        }
    }

    return (
        <div>
            <div className="form-section">
                <h3 className="pull-left">DCB Drill Down Reports </h3>
                <div className="clearfix"></div>
                <form onSubmit={(e)=>{search(e)}}>
                    <fieldset>
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label for="">Asset Category <span> * </span></label>
                                    </div>
                                    <div className="col-sm-6">
                                        <select id="assetCategoryType" value={assetCategoryType} name= "assetCategoryType" onChange={(e) => {handleChange(e, "assetCategoryType")}}>
                                            <option value=""> Select Asset Category</option>
                                            {renderOptions(assetCategories)}
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label for="">Election Ward No. <span> * </span> </label>
                                    </div>
                                    <div className="col-sm-6">
                                        <select id="electionWard" value={electionWard} name="electionWard" onChange={(e) => {handleChange(e, "electionWard")}}>
                                            <option value="">Select ElectionWard</option>
                                            {renderOptions(electionwards)}
                                        </select>
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
                                        <select id="locality" value={locality} name= "assetCategoryType" onChange={(e) => {handleChange(e, "locality")}}>
                                            <option value=""> Select Locality</option>
                                            {renderOptions(localityList)}
                                        </select>
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
                {showTable()}

            </div>
            </div>
    );
  }
}






ReactDOM.render(
  <DcbReport />,
  document.getElementById('root')
);
