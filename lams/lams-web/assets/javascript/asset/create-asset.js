const defaultAssetSetState = {
    "tenantId": tenantId,
    "name": "",
    "department": {
        "id": ""
    },
    "assetCategory": {
        tenantId,
        "id": ""
    },
    "description": "",
    "assetCategoryType": "",
    "modeOfAcquisition": "",
    "status": "",
    "description": "",
    "dateOfCreation": "",
    "locationDetails": {
        "locality": "",
        "zone": "",
        "revenueWard": "",
        "block": "",
        "street": "",
        "electionWard": "",
        "doorNo": "",
        "pinCode": ""
    },
    "remarks": "",
    "length": "",
    "width": "",
    "totalArea": "",
    "properties": {},
    "grossValue": "",
    "accumulatedDepreciation": ""
};

class CreateAsset extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
        list: [],
        assetSet: Object.assign({}, defaultAssetSetState),
        assetCategories: [],
        localityList: [],
        electionwards: [],
        departments: [],
        acquisitionList: [],
        zoneList: [],
        streetList: [],
        WardList: [],
        blockList: [],
        customFields: [],
        statusList: {},
        assetCategoriesType: {},
        capitalized: false,
        error: "",
        success: ""
    }
    this.handleChange = this.handleChange.bind(this);
    this.handleChangeTwoLevel = this.handleChangeTwoLevel.bind(this);
    this.addOrUpdate = this.addOrUpdate.bind(this);

  }
  close() {
      // widow.close();
      open(location, '_self').close();
  }

  getCategory(id) {
      for (var i = 0; i < this.state.assetCategories.length; i++) {
          if (this.state.assetCategories[i].id == id) {
              return this.state.assetCategories[i]["customFields"];
          }
      }
  }

  handleChange(e, name) {
      if(name === "status") {
        this.state.capitalized = ( e.target.value === "CAPITALIZED");
      }

      this.setState({
          assetSet: {
              ...this.state.assetSet,
              [name]: e.target.value
          }
      })

  }

  addOrUpdate(e) {
      e.preventDefault();
      this.setState({
        ...this.state,
        error: "",
        success: ""
      })
      var tempInfo = this.state.assetSet;
      var body = {
          "RequestInfo": requestInfo,
          "Asset": tempInfo
      };

      var response = $.ajax({
          url: baseUrl + "/asset-services/assets/_create?tenantId=1",
          type: 'POST',
          dataType: 'json',
          data: JSON.stringify(body),
          async: false,
          contentType: 'application/json',
          headers:{
              'auth-token' :'c98e8b66-1837-4b86-b8c7-8eab985714ed'
          }
      });

      if (response["statusText"] === "OK") {
          this.setState({
            ...this.state,
            assetSet: Object.assign({}, defaultAssetSetState),
            success: "Asset successfuly added!"
          })
      } else {
           this.setState({
            ...this.state,
            error: response["statusText"]
          })
      }
  }


  handleChangeTwoLevel(e, pName, name) {
      // var id=0;
      if (pName == "assetCategory") {
          this.setState({
              customFields: this.getCategory(e.target.value),
              properties: {}
          })
          e.target.value = parseInt(e.target.value)
      }

      this.setState({
          assetSet: {
              ...this.state.assetSet,
              [pName]: {
                  ...this.state.assetSet[pName],
                  [name]: e.target.value
              }
          }
      })

  }


  componentDidMount() {
      var type = getUrlVars()["type"];
      var id = getUrlVars()["id"];

      // if(getUrlVars()["type"]==="view")
      // {
      //   for (var variable in this.state.assetSet)
      //     document.getElementById(variable).disabled = true;
      // }


      if (type === "view" || type === "update") {
          //console.log(getCommonMasterById("asset-services","assets","Assets",id).responseJSON["Assets"][0]);
          this.setState({
              assetSet: getCommonMasterById("asset-services", "assets", "Assets", id).responseJSON["Assets"][0]
          })
      }


      this.setState({
          assetCategories: commonApiPost("asset-services", "assetCategories", "_search", {}).responseJSON["AssetCategory"] || [],
          departments: commonApiPost("egov-common-masters", "departments", "_search", {
              tenantId
          }).responseJSON["Department"] || [],
          acquisitionList: commonApiGet("asset-services", "", "GET_MODE_OF_ACQUISITION", {}).responseJSON || [],
          localityList: commonApiPost("v1/location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", {
              boundaryTypeName: "LOCALITY",
              hierarchyTypeName: "LOCATION"
          }).responseJSON["Boundary"] || [],
          electionwards: commonApiPost("v1/location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", {
              boundaryTypeName: "WARD",
              hierarchyTypeName: "ADMINISTRATION"
          }).responseJSON["Boundary"] || [],
          zoneList: commonApiPost("v1/location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", {
              boundaryTypeName: "ZONE",
              hierarchyTypeName: "REVENUE"
          }).responseJSON["Boundary"] || [],
          streetList: commonApiPost("v1/location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", {
              boundaryTypeName: "STREET",
              hierarchyTypeName: "REVENUE"
          }).responseJSON["Boundary"] || [],
          WardList: commonApiPost("v1/location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", {
              boundaryTypeName: "WARD",
              hierarchyTypeName: "REVENUE"
          }).responseJSON["Boundary"] || [],
          blockList: commonApiPost("v1/location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", {
              boundaryTypeName: "BLOCK",
              hierarchyTypeName: "REVENUE"
          }).responseJSON["Boundary"] || [],
          statusList: commonApiGet("asset-services", "", "GET_STATUS", {}).responseJSON || {},
          assetCategoriesType: commonApiGet("asset-services", "", "GET_ASSET_CATEGORY_TYPE", {}).responseJSON || {}
      })
  }

  render() {
    let {handleChange, addOrUpdate, handleChangeTwoLevel}=this;
    let {isSearchClicked, list, customFields, error, success, acquisitionList}=this.state;
    let {
      assetCategory,
      locationDetails,
      assetCategoryType,
      locality,
      doorNo,
      name,
      pinCode,
      street,
      electionWard,
      dateOfCreation,
      assetAddress,
      block,
      zone,
      totalArea,
      code,
      department,
      description,
      modeOfAcquisition,
      length,
      width,
      revenueWard,
      accumulatedDepreciation,
      grossValue,
      status
  } = this.state.assetSet;
    let mode = getUrlVars()["type"];
    
    const showActionButton = function() {
        if((!mode) ||mode==="update") {
          return (<button type="submit" className="btn btn-submit">{mode?"Update":"Create"}</button>);
        }
    };

    const showAlert = function(error, success) {
        if(error) {
          return (
            <div className="alert alert-danger alert-dismissible alert-toast" role="alert">
              <button type="button" className="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
              <strong>Error!</strong> {error}
            </div>
          )
        } else if(success) {
           return (
            <div className="alert alert-success alert-dismissible alert-toast" role="alert">
              <button type="button" className="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
              <strong>Success!</strong> {success}
            </div>
          )
        }
    };

    const renderOption = function(list)
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

    const showCategorySection = function()
    {
        if(customFields.length > 0)
        {
              return (
                <div className="form-section" id="allotteeDetailsBlock">
                  <h3 className="categoryType">Category Details </h3>
                  <div className="form-section-inner">
                      <div className="row">
                          <div className="col-sm-6">
                            <div className="row">
                              <div className="col-sm-6 label-text">
                                <label for="name">Name <span>* </span></label>
                              </div>
                              <div className="col-sm-6">
                                <input id="name" name="name" value={name} type="text"
                                  onChange={(e)=>{handleChange(e, "name")}} required/>
                              </div>
                            </div>
                          </div>
                          <div className="col-sm-6">
                            <div className="row">
                              <div className="col-sm-6 label-text">
                                <label for="name">Mode Of Acquisition <span>* </span></label>
                              </div>
                              <div className="col-sm-6">
                                <select id="modeOfAcquisition" name="modeOfAcquisition" required value={modeOfAcquisition} onChange={(e)=>
                                    {handleChange(e,"modeOfAcquisition") }}>
                                    <option value="">Select Mode Of Acquisition</option>
                                    {renderOption(acquisitionList)}
                                </select>
                              </div>
                            </div>
                          </div>
                          {showFields()}
                      </div>
                  </div>
                </div>
              )
        }
    }

    const showFields = function() {
        return customFields.map((item,index)=>
        {
            return (
              <div className="col-sm-6" key={index}>
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label for={item.name}>{item.name}  {showStart(item.isActive)}</label>
                  </div>
                  <div className="col-sm-6">
                    <input id={item.name} name={item.name} value={item.values} type={item.type==="String"?"text":item.type}
                      onChange={(e)=>{handleChangeTwoLevel(e,"properties",item.name)}} required/>
                  </div>
                </div>
              </div>
            )
        })
    }

    const showStart = function(status)
    {
        if (status) {
            return(
              <span> * </span>
            )
        }
    }

    const getTodaysDate = function() {
        var now = new Date();
        var month = (now.getMonth() + 1);               
        var day = now.getDate();
        if(month < 10) 
            month = "0" + month;
        if(day < 10) 
            day = "0" + day;
        return (now.getFullYear() + '-' + month + '-' + day);
    }

    const renderIfCapitalized = function(capitalized) {
      if(capitalized) {
        return (
          <div className="row">
              <div className="col-sm-6">
                  <div className="row">
                    <div className="col-sm-6 label-text">
                        <label for="grossValue">Gross Value</label>
                    </div>
                    <div className="col-sm-6">
                        <input type="number" id="grossValue" name="grossValue" value= {grossValue}
                          onChange={(e)=>{handleChange(e,"grossValue")}} maxlength="16" step="0.01"/>
                    </div>
                  </div>
              </div>
              <div className="col-sm-6">
                  <div className="row">
                    <div className="col-sm-6 label-text">
                        <label for="accumulatedDepreciation">Gross Value</label>
                    </div>
                    <div className="col-sm-6">
                        <input type="number" id="accumulatedDepreciation" name="accumulatedDepreciation" value= {accumulatedDepreciation}
                          onChange={(e)=>{handleChange(e,"accumulatedDepreciation")}} maxlength="16" step="0.01"/>
                    </div>
                  </div>
              </div>
          </div>
        )
      }
    }

    return (
      <div>
        {showAlert(error, success)}
        <form onSubmit={(e)=>
            {addOrUpdate(e)}}>
            <div className="form-section">
              <h3 className="categoryType">Header Details </h3>
              <div className="form-section-inner">
                  <div className="row">
                    <div className="col-sm-6">
                        <div className="row">
                          <div className="col-sm-6 label-text">
                              <label for="department">Department <span> *</span></label>
                          </div>
                          <div className="col-sm-6">
                              <div className="styled-select">
                                <select id="department" name="department" required value={department.id} onChange={(e)=>
                                    {
                                    handleChangeTwoLevel(e,"department","id")
                                    }}>
                                    <option value="">Select Department</option>
                                    {renderOption(this.state.departments)}
                                </select>
                              </div>
                          </div>
                        </div>
                    </div>
                    {/*<div className="col-sm-6">
                        <div className="row">
                          <div className="col-sm-6 label-text">
                              <label for="assetCategoryType">Asset Category Type <span> *</span></label>
                          </div>
                          <div className="col-sm-6">
                              <div className="styled-select">
                                <select id="assetCategoryType" name="assetCategoryType" value={assetCategoryType} required= "true" onChange={(e)=>
                                    {
                                    handleChange(e,"assetCategoryType")}}>
                                    <option value="">Select Asset Category</option>
                                    {renderOption(this.state.assetCategoriesType)}
                                </select>
                              </div>
                          </div>
                        </div>
                    </div>*/}
                    <div className="col-sm-6">
                        <div className="row">
                          <div className="col-sm-6 label-text">
                              <label for="assetCategory">Asset Category <span> *</span> </label>
                          </div>
                          <div className="col-sm-6">
                              <div className="styled-select">
                                <select id="assetCategory" name="assetCategory" required value={assetCategory.id} onChange={(e)=>
                                    {
                                    handleChangeTwoLevel(e,"assetCategory","id")}}>
                                    <option value="">Select Asset Category</option>
                                    {renderOption(this.state.assetCategories)}
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
                              <label for="description">Date Of Creation</label>
                          </div>
                          <div className="col-sm-6">
                              <input type="date" id="dateOfCreation" name="dateOfCreation" value= {dateOfCreation}
                                onChange={(e)=>{handleChange(e,"dateOfCreation")}} max={getTodaysDate()}/>
                          </div>
                        </div>
                    </div>
                    <div className="col-sm-6">
                        <div className="row">
                          <div className="col-sm-6 label-text">
                              <label for="description">Description</label>
                          </div>
                          <div className="col-sm-6">
                              <textarea id="description" name="description" value= {description}
                                onChange={(e)=>{handleChange(e,"description")}} max="1024"></textarea>
                          </div>
                        </div>
                    </div>
                  </div>
              </div>
            </div>
            <div className="form-section" id="allotteeDetailsBlock">
              <h3 className="categoryType">Location Details </h3>
              <div className="form-section-inner">
                  <div className="row">
                    <div className="col-sm-6">
                        <div className="row">
                          <div className="col-sm-6 label-text">
                              <label for="locality"> Location <span> * </span> </label>
                          </div>
                          <div className="col-sm-6">
                              <div className="styled-select">
                                <select id="locality" name="locality" required="true" value={locationDetails.locality}
                                    onChange={(e)=>
                                    {handleChangeTwoLevel(e,"locationDetails","locality")}}>
                                    <option value="">Choose locality</option>
                                    {renderOption(this.state.localityList)}
                                </select>
                              </div>
                          </div>
                        </div>
                    </div>
                    <div className="col-sm-6">
                        <div className="row">
                          <div className="col-sm-6 label-text">
                              <label for="revenueWard"> Revenue Ward  </label>
                          </div>
                          <div className="col-sm-6">
                              <div className="styled-select">
                                <select id="revenueWard" name="revenueWard" value={locationDetails.revenueWard}
                                    onChange={(e)=>
                                    {  handleChangeTwoLevel(e,"locationDetails","revenueWard")}}>
                                    <option value="">Choose Revenue Ward</option>
                                    {renderOption(this.state.WardList)}
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
                              <label for="block"> Block Number  </label>
                          </div>
                          <div className="col-sm-6">
                              <div className="styled-select">
                                <select id="block" name="block" value={locationDetails.block}
                                    onChange={(e)=>
                                    {  handleChangeTwoLevel(e,"locationDetails","block")}}>
                                    <option value="">Choose Block</option>
                                    {renderOption(this.state.blockList)}
                                </select>
                              </div>
                          </div>
                        </div>
                    </div>
                    <div className="col-sm-6">
                        <div className="row">
                          <div className="col-sm-6 label-text">
                              <label for="street"> Street  </label>
                          </div>
                          <div className="col-sm-6">
                              <div className="styled-select">
                                <select id="street" name="street" value={locationDetails.street}
                                    onChange={(e)=>
                                    {  handleChangeTwoLevel(e,"locationDetails","street")}}>
                                    <option value="">Choose Street</option>
                                    {renderOption(this.state.streetList)}
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
                              <label for="electionWard"> Election Ward No  </label>
                          </div>
                          <div className="col-sm-6">
                              <div className="styled-select">
                                <select id="electionWard" name="electionWard" value={locationDetails.electionWard}
                                    onChange={(e)=>
                                    { handleChangeTwoLevel(e,"locationDetails","electionWard")}}>
                                    <option value="">Choose Election Wards</option>
                                    {renderOption(this.state.electionwards)}
                                </select>
                              </div>
                          </div>
                        </div>
                    </div>
                    <div className="col-sm-6">
                        <div className="row">
                          <div className="col-sm-6 label-text">
                              <label for="doorno"> Door Number  </label>
                          </div>
                          <div className="col-sm-6">
                              <input type="text" name="doorNo" id= "doorNo" value= {locationDetails.doorNo}
                                onChange={(e)=>{handleChangeTwoLevel(e,"locationDetails","doorNo")}}/>
                          </div>
                        </div>
                    </div>
                  </div>
                  <div className="row">
                    {/*<div className="col-sm-6">
                        <div className="row">
                          <div className="col-sm-6 label-text">
                              <label for="zone"> Zone Number  </label>
                          </div>
                          <div className="col-sm-6">
                              <div className="styled-select">
                                <select id="zone" name="zone" value={locationDetails.zone}
                                    onChange={(e)=>
                                    { handleChangeTwoLevel(e,"locationDetails","zone")}}>
                                    <option value="">Choose Zone Number</option>
                                    {renderOption(this.state.zoneList)}
                                </select>
                              </div>
                          </div>
                        </div>
                    </div>*/}
                    <div className="col-sm-6">
                        <div className="row">
                          <div className="col-sm-6 label-text">
                              <label for="pin">PIN Code</label>
                          </div>
                          <div className="col-sm-6">
                              <input type="text" name="pinCode" id="pinCode" value={locationDetails.pinCode}
                                onChange={(e)=>{handleChangeTwoLevel(e,"locationDetails","pinCode")}} pattern="[0-9]{6}" title="Six number pin code"/>
                          </div>
                        </div>
                    </div>
                  </div>
              </div>
            </div>
            {showCategorySection()}
            <div className="form-section" id="allotteeDetailsBlock">
              <h3 className="categoryType">Value Summary </h3>
              <div className="form-section-inner">
                  <div className="row">
                      <div className="col-sm-6">
                        <div className="row">
                          <div className="col-sm-6 label-text">
                              <label for="status"> Asset Status </label>
                          </div>
                          <div className="col-sm-6">
                              <div className="styled-select">
                                <select id="status" name="status" value={status}
                                    onChange={(e)=>
                                    { handleChange(e,"status")}}>
                                    <option value="">Choose Status</option>
                                    {renderOption(this.state.statusList)}
                                </select>
                              </div>
                          </div>
                        </div>
                    </div>
                  </div>
                  {renderIfCapitalized(this.state.capitalized)}
              </div>
            </div>
            <div className="text-center"> 
              {showActionButton()} &nbsp;&nbsp;
              <button type="button" className="btn btn-submit" onClick={(e)=>{this.close()}}>Close</button>
            </div>
        </form>
      </div>
    );
  }
}


ReactDOM.render(
  <CreateAsset />,
  document.getElementById('root')
);


// //
// //
//
//
//
// //
//
//
//
//     //
//   <div className="form-section" id="agreementDetailsBlockTemplateThree">
//   <h3 className="categoryType">Shopping Complex  Details </h3>
//   <div className="form-section-inner">
//
//         <div className="row">
//         <div className="col-sm-6">
//         <div className="row">
//         <div className="col-sm-6 label-text">
//                   <label for="acuquisition">MOde Of Acquisition <span> * </span> </label>
//          </div>
//          <div className="col-sm-6">
//           <div className="styled-select">
//                       <select name="acuquisition" id="acuquisition">
//                     <option>Purchase</option>
//                     <option>Tender</option>
//                     <option>Connstruction</option>
//                     <option value="Direct">Donation</option>
//
//                     </select>
//             </div>
//             </div>
//             </div>
//             </div>
//
//               <div className="col-sm-6">
//               <div className="row">
//               <div className="col-sm-6 label-text">
//                     <label for="name">Shopping COmplex Name.</label>
//               </div>
//               <div className="col-sm-6">
//                     <input type="text" name="name" id="name" />
//               </div>
//               </div>
//               </div>
//               </div>
//
//               <div className="row">
//               <div className="col-sm-6">
//               <div className="row">
//               <div className="col-sm-6 label-text">
//                   <label for="complexNo">Shopping Complex No  </label>
//               </div>
//               <div className="col-sm-6">
//                   <input type="text" name="complexNO" id="complexNO" />
//               </div>
//               </div>
//               </div>
//
//               <div className="col-sm-6">
//               <div className="row">
//               <div className="col-sm-6 label-text">
//                       <label for="doorNo">Door No  </label>
//               </div>
//               <div className="col-sm-6">
//                 <input type="number" name="doorNO" id="doorNO" />
//               </div>
//               </div>
//               </div>
//
//               <div className="row">
//               <div className="col-sm-6">
//               <div className="row">
//               <div className="col-sm-6 label-text">
//                     <label for="complexNo"> Number of floor  </label>
//               </div>
//               <div className="col-sm-6">
//                     <input type="text" name="floorNO" id="floorNO" />
//               </div>
//               </div>
//               </div>
//
//               <div className="col-sm-6">
//               <div className="row">
//               <div className="col-sm-6 label-text">
//                   <label for="noofShop">Total Number of Shop  </label>
//             </div>
//             <div className="col-sm-6">
//                 <input type="text" name="noofShop" id="noofShop" />
//             </div>
//             </div>
//             </div>
//             </div>
//
//             <div className="row">
//             <div className="col-sm-6">
//             <div className="row">
//             <div className="col-sm-6 label-text">
//                 <label for="floorNo">Floor No </label>
//             </div>
//             <div className="col-sm-6">
//             <div className="styled-select">
//                   <select name="floorNo" id="floorNo">
//                   <option>1</option>
//                   <option>2</option>
//                   <option>3</option>
//                   <option value="Direct">4</option>
//
//                 </select>
//             </div>
//             </div>
//             </div>
//             </div>
//             <div className="col-sm-6">
//             <div className="row">
//             <div className="col-sm-6 label-text">
//                   <label for="noShop">Number Of Shop.</label>
//             </div>
//             <div className="col-sm-6">
//                   <input type="text" name="noShop" id="noShop" />
//             </div>
//             </div>
//             </div>
//             </div>
//
//             <div className="row">
//             <div className="col-sm-6">
//             <div className="row">
//             <div className="col-sm-6 label-text">
//                 <label for="status">Status <span> * </span> </label>
//             </div>
//             <div className="col-sm-6">
//             <div className="styled-select">
//                   <select name="status" id="status">
//                   <option>1</option>
//                   <option>2</option>
//                   <option>3</option>
//                   <option value="Direct">4</option>
//
//                 </select>
//           </div>
//           </div>
//           </div>
//           </div>
//           <div className="col-sm-6">
//           <div className="row">
//           <div className="col-sm-6 label-text">
//                   <label for="value">Value</label>
//            </div>
//            <div className="col-sm-6">
//                   <input type="text" name="value" id="value" />
//           </div>
//           </div>
//           </div>
//           </div>
//
//           <div className="row">
//           <div className="col-sm-6">
//           <div className="row">
//           <div className="col-sm-6 label-text">
//               <label for="remarks">Remarks </label>
//         </div>
//         <div className="col-sm-6">
//               <textarea name="remarks" id="remarks"></textarea>
//       </div>
//       </div>
//       </div>
//       </div>
//
//       <div className="text-center">
//           <button type="button" className="btn btn-submit" >Create</button>
//           <button type="button" className="btn btn-submit">close</button>
//       </div>
//       </div>
//       </div>
