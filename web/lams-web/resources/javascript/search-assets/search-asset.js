var flag = 1;
class AssetSearch extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
        list: [],
        searchSet: {
            locality: "",
            doorNo: "",
            assetCategory: "",
            name: "",
            electionWard: "",
            code: "",
            tenantId
        },
        isSearchClicked: false,
        assetCategories: [],
        locality: [],
        electionwards: [],
        modify: false
      }
    this.handleChange = this.handleChange.bind(this);
    this.search = this.search.bind(this);
  }

  search(e) {
    e.preventDefault();
    var _this = this;
    try {
      //call api call
      var list = commonApiPost("asset-services","assets","_search",this.state.searchSet).responseJSON["Assets"] ||[];
    } catch(e) {
      var list = [];
      console.log(e);
    }
    var assetIds = '';
    for(var i=0; i<list.length; i++) {
      assetIds += (i == 0 ? '' : ',') + list[i].id;
    }

    flag = 1;
    this.setState({
      isSearchClicked: true,
      list,
      modify: true
    });

    setTimeout(function() {
      _this.setState({
        modify: false
      })
    }, 1200);
  }

  componentWillMount() {
    try {
        var locality = !localStorage.getItem("locality") || localStorage.getItem("locality") == "undefined" ? (localStorage.setItem("locality", JSON.stringify(commonApiPost("egov-location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", { boundaryTypeName: "LOCALITY", hierarchyTypeName: "LOCATION", tenantId }).responseJSON["Boundary"] || [])), JSON.parse(localStorage.getItem("locality"))) : JSON.parse(localStorage.getItem("locality"));
    } catch (e) {
        console.log(e);
        var locality = [];
    }

    try {
      var electionwards = !localStorage.getItem("ward") || localStorage.getItem("ward") == "undefined" ? (localStorage.setItem("ward", JSON.stringify(commonApiPost("egov-location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", { boundaryTypeName: "WARD", hierarchyTypeName: "ADMINISTRATION", tenantId }).responseJSON["Boundary"] || [])), JSON.parse(localStorage.getItem("ward"))) : JSON.parse(localStorage.getItem("ward"));
    } catch (e) {
        console.log(e);
      var  electionwards = [];
    }

    try {
      var assetCategories = !localStorage.getItem("assetCategories") || localStorage.getItem("assetCategories") == "undefined" ? (localStorage.setItem("assetCategories", JSON.stringify(commonApiPost("asset-services", "assetCategories", "_search", {usedForLease:true, tenantId}).responseJSON["AssetCategory"] || [])), JSON.parse(localStorage.getItem("assetCategories"))) : JSON.parse(localStorage.getItem("assetCategories"));
    } catch (e) {
        console.log(e);
      var  assetCategories = [];
    }

    this.setState({
     locality,
     electionwards,
     assetCategories,
   })

  }

  componentWillUpdate() {
    if(flag == 1) {
      flag = 0;
      $('#agreementTable').dataTable().fnDestroy();
    }
  }

  componentDidMount() {
    if(window.opener && window.opener.document) {
       var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
       if(logo_ele && logo_ele[0]) {
         document.getElementsByClassName("homepage_logo")[0].src = window.location.origin + logo_ele[0].getAttribute("src");
       }
     }
  }


  componentDidUpdate(prevProps, prevState) {
      if (this.state.modify) {
          $('#agreementTable').DataTable({
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

  handleChange(e,name) {
      this.setState({
          searchSet:{
              ...this.state.searchSet,
              [name]:e.target.value
          }
      })
  }

  handleSelectChange(type, id, category) {
    if (type === "create") {
      window.open("app/agreements/new.html?type="+category+"&assetId="+id, "fs", "fullscreen=yes");
    } else if(type === "dataEntry") {
      window.open("app/dataentry/data-entry.html?type="+category+"&assetId="+id, "fs", "fullscreen=yes");
    }
  }


  close(){
      // widow.close();
      open(location, '_self').close();
  }



  render() {
    let _this = this;
    let {handleChange,search,updateTable,handleSelectChange}=this;
    let {isSearchClicked,list, electionwards}=this.state;
    let {
    locality,
    doorNo,
    assetCategory,
    name,
    electionWard,
    code}=this.state.searchSet;

    const renderOption = function(list) {
        if(list)
        {
            return list.map((item, ind)=>
            {
                return (<option key={ind} value={item.id}>
                        {item.name}
                  </option>)
            })
        }
    }
    const showTable = function() {
      if(isSearchClicked) {
          return (
            <table id="agreementTable" className="table table-bordered">
                <thead>
                <tr>
                    <th>Sl No </th>
                    <th>Asset Category </th>
                    <th>Asset Name </th>
                    <th>Asset Code </th>
                    <th>Election Ward No </th>
                    <th>Action </th>
                  </tr>
                </thead>

                <tbody id="agreementSearchResultTableBody">
                    {
                        renderBody()
                    }
                </tbody>
            </table>
          )
      }
    }

    const showActions = function(item) {
      if( item.assetCategory.usedForLease ) {
        return (
          <div className="styled-select">
              <select id="myOptions" onChange={(e)=>{
                handleSelectChange(e.target.value, item.id, item.assetCategory.name)
              }} defaultValue="" value="">
                  <option value="">Select Action</option>
                  <option value="dataEntry"> Data Entry </option>
              </select>
          </div>
        )
      } else {
        return "";
      }
    }

    const renderBody = function() {
      if(list.length > 0) {
        return list.map((item,index)=> {
              return (
                <tr key={index}>
                  <td>{index+1}</td>
                  <td>{item.assetCategory.name}</td>
                  <td>{item.name}</td>
                  <td>{item.code}</td>
                  <td>{getNameById(electionwards, item.locationDetails.electionWard)}</td>
                  <td>
                      {showActions(item)}
                  </td>
                </tr>
              );

        })
      }


    }
    const disbaled = function(type) {
        if (type==="view") {
              return "ture";
        } else {
            return "false";
        }
    }

    return (
    <div>
    <div className="form-section">
        <div className="form-section-inner">
            <form onSubmit={(e)=>{search(e)}}>
                <div className="">
                  <div className="form-section">
                    <div className="row">
                      <div className="col-sm-3 col-sm-offset-5">
                            <label for="asset_category">Asset category<span> *</span></label>
                            <div className="styled-select">
                                <select id="asset_category" name="asset_category" required="true" value={assetCategory} onChange={(e)=>{
                                handleChange(e,"assetCategory")}}>

                                    <option value="">Select Asset Category</option>
                                    {renderOption(this.state.assetCategories)}
                                  </select>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>

                <div className="form-section">
                    <div className="row">
                      <div className="col-sm-6">
                          <div className="row">
                              <div className="col-sm-6 label-text">
                                  <label for="">Asset Code </label>
                              </div>
                              <div className="col-sm-6">
                                      <input type="text" name="code" id="code" value={code}
                                      onChange={(e)=>{handleChange(e,"code")}} />
                              </div>
                          </div>
                        </div>
                        <div className="col-sm-6">
                            <div className="row">
                                <div className="col-sm-6 label-text">
                                    <label for="">Asset Name</label>
                                </div>
                                <div className="col-sm-6">
                                    <input type="text" name="name" id="name" value={name}
                                    onChange={(e)=>{handleChange(e,"name")}}/>

                                </div>
                            </div>
                        </div>
                    </div>

                <div className="row">
                    <div className="col-sm-6">
                        <div className="row">
                            <div className="col-sm-6 label-text">
                                <label for="locality">Locality</label>
                            </div>
                            <div className="col-sm-6">
                                <div className="styled-select">
                                    <select id="locality" name="locality" value={locality}
                      onChange={(e)=>{  handleChange(e,"locality")}}>

                                      <option value="">Choose locality</option>
                                      {renderOption(this.state.locality)}

                                    </select>
                                </div>
                            </div>
                        </div>
                    </div>


                    <div className="col-sm-6">
                          <div className="row">
                              <div className="col-sm-6 label-text">
                                  <label for="door_no">Door No </label>
                              </div>
                              <div className="col-sm-6">
                                  <input type="text" name="door_no" id="door_no" value={doorNo} onChange={(e)=>{
                            handleChange(e,"doorNo")
                        }}/>
                              </div>
                          </div>
                      </div>
                  </div>

                      <div className="row">
                          <div className="col-sm-6">
                              <div className="row">
                                  <div className="col-sm-6 label-text">
                                      <label for="electionWard">Election Ward no </label>
                                  </div>
                                  <div className="col-sm-6">
                                      <div className="styled-select">
                                          <select id="electionWard" name="electionWard" value={electionWard} onChange={(e)=>{
                              handleChange(e,"electionWard")
                          }}>
                                          <option value="">Choose Election Wards</option>
                                          {renderOption(this.state.electionwards)}
                                          </select>
                                      </div>
                                  </div>
                              </div>
                          </div>
                      </div>
                  </div>


                    <div className="text-right text-danger">
                          Note: Asset category is mandatory.
                    </div>
                    <br/>
                    <div className="text-center">
                        <button type="submit" className="btn btn-submit">Search</button>
                        <button type="button" className="btn btn-close" onClick={(e)=>{this.close()}}>Close</button>
                    </div>
                    </form>
                </div>
            </div>
            <div className="table-cont" id="table">
                {showTable()}

            </div>

          </div>
          );
      }
}


ReactDOM.render(
  <AssetSearch />,
  document.getElementById('root')
);


//     <!-- Table -->
//     <div className="table-cont" id="table">
//         <table id="ast-srch" className="display responsive nowrap datatable dt-responsive" cellspacing="0" width="100%">
//             <thead>
//                 <tr>
//                     <th>Sl No </th>
//                     <th>Asset Category </th>
//                     <th>Asset Name </th>
//                     <th>Asset Code </th>
//                     <th>Election Ward No </th>
//                     <th>Action </th>
//                 </tr>
//             </thead>
//             <tbody>
//                 <tr>
//                     <td>1</td>
//                     <td>Land </td>
//                     <td>Open land in gandhi nagar extension</td>
//                     <td>033/1458/125698-12/19</td>
//                     <td>N001</td>
//                     <td>
//                         <div className="styled-select">
//                             <select id="myOptions">
//                               <option value="">Select Action</option>
//                               <option value="land">Land Agreement</option>
//                               <option value="shop">Shops in Shopping Complex Agreement</option>
//                               <option value="market">Markets Agreement</option>
//                               <option value="kalyanamandapam">Kalyana mandapams Agreement</option>
//                               <option value="parking_space">Parking Spaces Agreement</option>
//                               <option value="slaughter_house">Slaughter Houses Agreement</option>
//                               <option value="usfructs">Usfructs Agreement</option>
//                               <option value="community">Community Agreement</option>
//                               <option value="fish_tank">Fish Tank Agreement</option>
//                             </select>
//                         </div>
//                     </td>
//                 </tr>
//                 <tr>
//                     <td>2</td>
//                     <td>Pond </td>
//                     <td>Beside sukanta nagar extension</td>
//                     <td>033/1458/365998-12/19</td>
//                     <td>N02</td>
//                     <td>
//                         <div className="styled-select">
//                             <select>
//                               <option>Create Agreement</option>
//                             </select>
//                         </div>
//                     </td>
//                 </tr>
//                 <tr>
//                     <td>3</td>
//                     <td>Land </td>
//                     <td>Open land in gandhi nagar extension</td>
//                     <td>033/1458/125698-12/19</td>
//                     <td>N01</td>
//                     <td>
//                         <div className="styled-select">
//                             <select>
// <option>Create Agreement</option>
// </select>
//                         </div>
//                     </td>
//                 </tr>
//                 <tr>
//                     <td>4</td>
//                     <td>Pond </td>
//                     <td>Beside sukanta nagar extension</td>
//                     <td>033/1458/365998-12/19</td>
//                     <td>N02</td>
//                     <td>
//                         <div className="styled-select">
//                             <select>
//                               <option>Create Agreement</option>
//                               </select>
//                         </div>
//                     </td>
//                 </tr>
//
//             </tbody>
//         </table>
