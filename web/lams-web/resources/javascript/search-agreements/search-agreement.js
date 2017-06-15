var flag = 0;
class AgreementSearch extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
        agreements: [],
        searchSet: {
            locality: "",
            agreementNumber: "",
            doorNo: "",
            assetCategory: "",
            mobileNumber: "",
            allotteeName: "",
            revenueWard: "",
            electionWard: "",
            code: "",
            tenderNumber: "",
            fromDate: "",
            toDate: "",
            tenantId
        },
        isSearchClicked: false,
        assetCategories: [],
        locality: [],
        revenueWards: [],
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
      var agreements = commonApiPost("lams-services", "agreements", "_search", this.state.searchSet).responseJSON["Agreements"] ||[];
      flag = 1;
      _this.setState({
        isSearchClicked: true,
        agreements,
        modify: true
      });
      setTimeout(function() {
        _this.setState({
          modify: false
        })
      }, 1200);
    } catch(e) {
      console.log(e);
    }
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
        var electionwards = [];
    }

    try {
      var revenueWards = !localStorage.getItem("revenueWard") || localStorage.getItem("revenueWard") == "undefined" ? (localStorage.setItem("revenueWard", JSON.stringify(commonApiPost("egov-location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", { boundaryTypeName: "WARD", hierarchyTypeName: "REVENUE", tenantId }).responseJSON["Boundary"] || [])), JSON.parse(localStorage.getItem("revenueWard"))) : JSON.parse(localStorage.getItem("revenueWard"));
    } catch (e) {
        console.log(e);
        var revenueWards = [];
    }

    try {
      var assetCategories = !localStorage.getItem("assetCategories") || localStorage.getItem("assetCategories") == "undefined" ? (localStorage.setItem("assetCategories", JSON.stringify(commonApiPost("asset-services", "assetCategories", "_search", {tenantId}).responseJSON["AssetCategory"] || [])), JSON.parse(localStorage.getItem("assetCategories"))) : JSON.parse(localStorage.getItem("assetCategories"));
    } catch (e) {
        console.log(e);
        var assetCategories = [];
    }

    this.setState({
      assetCategories,
      locality,
      electionwards,
      revenueWards
    })

  }

  componentDidMount() {
    if(window.opener && window.opener.document) {
       var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
       if(logo_ele && logo_ele[0]) {
         document.getElementsByClassName("homepage_logo")[0].src = window.location.origin + logo_ele[0].getAttribute("src");
       }
     }

    let _this = this;


    //Fetch allottee name suggestions
    $( "#name" ).autocomplete({
      source: function( request, response ) {
        $.ajax({
          url: baseUrl + "/user/_search?tenantId=" + tenantId,
          type: 'POST',
          dataType: "json",
          data: JSON.stringify({
              ...requestInfo,
              name: request.term,
              fuzzyLogic: true
          }),
          contentType: 'application/json',
          success: function( data ) {
            if(data && data.user && data.user.length) {
                let users = [];
                for(let i=0;i<data.user.length;i++)
                    users.push(data.user[i].name);
                response(users);
            }
          }
        });
      },
      minLength: 3,
      change: function( event, ui ) {
        if(ui.item && ui.item.value)
            _this.setState({
                searchSet:{
                    ..._this.state.searchSet,
                    name: ui.item.value
                }
            })
      }
    });

    $(".date-picker").datepicker({
      format: "dd/mm/yyyy",
      autoclose: true
    });

    $(".date-picker").on("change", function(e) {
      _this.setState({
          searchSet: {
            ..._this.state.searchSet,
            [e.target.id]: e.target.value
          }
      })
    })
  }

  componentWillUpdate() {
    if(flag == 1) {
      flag = 0;
      $('#agreementTable').dataTable().fnDestroy();
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

  handleChange(e, name)
  {
      this.setState({
          searchSet:{
              ...this.state.searchSet,
              [name]:e.target.value
          }
      })
  }

  handleSelectChange(type, id, number, assetCategory, acknowledgementNumber) {
    switch (type) {
      case "renew":
        window.open("app/search-agreement/view-renew-agreement.html?view=renew&type=" + assetCategory + (number ? "&agreementNumber=" + number : "&acknowledgementNumber=" + acknowledgementNumber) + "&assetId=" + id, "fs", "fullscreen=yes")
        break;
      case "collTax":
        $.ajax({
          url: "/lams-services/payment/_create?tenantId=" + tenantId + "&" + (number ? "agreementNumber=" + number : "acknowledgementNumber=" + acknowledgementNumber),
          type: 'POST',
          contentType: 'application/json',
          data: JSON.stringify({
              RequestInfo: requestInfo
          }),
          success: function( data ) {
            jQuery('<form>.').attr({
                method: 'post',
                action: '/collection/receipts/receipt-newform.action',
                target: '_self'
            }).append(jQuery('<input>').attr({
                type: 'hidden',
                id: 'collectXML',
                name: 'collectXML',
                value: data
            })).appendTo( document.body ).submit();
          },
          error: function(data) {
              console.log(data);
          }
        });
        break;
      case "view":
        window.open("app/search-agreement/view-renew-agreement.html?view=new&type="+assetCategory+"&agreementNumber="+number+"&assetId="+id, "fs", "fullscreen=yes");
        break;
      case "cancel":
        window.open("app/search-agreement/view-renew-agreement.html?view=cancel&type=" + assetCategory + (number ? "&agreementNumber=" + number : "&acknowledgementNumber=" + acknowledgementNumber) + "&assetId=" + id, "fs", "fullscreen=yes");
        break;
      case "eviction":
        window.open("app/search-agreement/view-renew-agreement.html?view=eviction&type=" + assetCategory + (number ? "&agreementNumber=" + number : "&acknowledgementNumber=" + acknowledgementNumber) + "&assetId=" + id, "fs", "fullscreen=yes");
        break;
      case "addeditdemand":
          window.open("app/dataentry/edit-demand.html?"+(number ? "agreementNumber=" + number : "acknowledgementNumber=" + acknowledgementNumber)+ "&assetId=" + id, "fs", "fullscreen=yes");
        break;
    }
}


  close(){
      // widow.close();
      open(location, '_self').close();
  }



  render() {
    console.log(this.state.searchSet);
    let {handleChange,search,updateTable,handleSelectChange}=this;
    let {isSearchClicked,agreements,assetCategories}=this.state;
    let {locality,
    agreementNumber,
    doorNo,
    assetCategory,
    mobileNumber,
    allotteeName,
    revenueWard,
    electionWard,
    code,
    tenderNumber,fromDate,toDate,shopComplexNumber}=this.state.searchSet;

    const getValueByName=function(name,id)
    {
        for (var i = 0; i < assetCategories.length; i++) {
          if (assetCategories[i].id==id) {
              return assetCategories[i][name];
          }
        }
    }

    const renderOption=function(list)
    {
        if(list)
        {
            return list.map((item)=>
            {
                return (<option key={item.id} value={item.id}>
                        {item.name}
                  </option>)
            })
        }
    }
    const showTable=function()
    {
      if(isSearchClicked)
      {
          return (
            <table id="agreementTable" className="table table-bordered">
                <thead>
                    <tr>
                        <th>Sl No </th>
                        <th>Agreement Number </th>
                        <th>Allottee Name </th>
                        <th>Allottee contact No </th>
                        <th>Locality </th>
                        <th>Asset Category </th>
                        <th>Asset Code </th>
                        <th>Agreement Date </th>
                        <th>Type</th>
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
    const renderBody=function()
    {
      if (agreements.length>0) {
        return agreements.map((item,index)=>
        { var category_name = getValueByName("name",item.asset.assetCategory.id);
              return (<tr key={index}>
                <td>{index+1}</td>
                                  <td>{item.agreementNumber} </td>
                                  <td>{item.allottee.name}</td>
                                  <td>{item.allottee.mobileNumber}</td>
                                  <td>{item.asset.locationDetails.locality}</td>
                                  <td>{item.asset.assetCategory.id?category_name:"-"}</td>
                                  <td>{item.asset.code}</td>
                                  <td>{item.agreementDate}</td>
                                  <td>{item.source == "DATA_ENTRY" ? "Data Entry" : "System"}</td>
                                  <td>
                                      <div className="styled-select">
                                        {getOption((category_name == "Land" || category_name == "shop"), item)};
                                      </div>
                                  </td>
                  </tr>
              );

        })
      }

    }

    /*const showCancelNEvict = function(status) {
      if(status == "APPROVED") {
        var values = [{label: "Cancel Agreement", value: "cancel"}, {label: "Evict Agreement", value: "eviction"}];
        return values.map(function(val, ind) {
          return (
            <option value={val.value}>{val.label}</option>
          )
        })
      }  else {
        return;
      }
    }*/

    const getDemandListing = function(agreement) {
      if(agreement.source == "DATA_ENTRY") {
        return (<option value="addeditdemand">Add / Edit Demand </option>);
      }
    }

    const getOption = function(isShopOrLand, item) {
        if (isShopOrLand) {
          return(
            <select id="myOptions" onChange={(e)=>{
              handleSelectChange(e.target.value, item.asset.id, item.agreementNumber, getValueByName("name",item.asset.assetCategory.id), item.acknowledgementNumber)
            }}>
                <option value="">Select Action</option>
                <option value="view">View</option>
                <option value="renew">Renew</option>
                <option value="collTax">Collect Tax</option>
                {getDemandListing(item)}
            </select>
          )
        } else {
          return(
            <select id="myOptions" onChange={(e)=>{
              handleSelectChange(e.target.value, item.asset.id, item.agreementNumber, getValueByName("name",item.asset.assetCategory.id), item.acknowledgementNumber)
            }}>
                <option value="">Select Action</option>
                <option value="view">View</option>
                <option value="collTax">Collect Tax</option>
                {getDemandListing(item)}

            </select>
          )

        }
    }
    const disbaled=function(type) {
        if (type==="view") {
              return "ture";
        } else {
            return "false";
        }
    }
    return (
    <div>
      <div className="form-section">
          <h3>Search Agreement </h3>
          <div className="form-section-inner">
              <form onSubmit={(e)=>{search(e)}}>
                  <div className="">
                    <div className="form-section">
                      <div className="row">
                        <div className="col-sm-3 col-sm-offset-5">
                              <label for="asset_category">Asset category<span> *</span></label>
                              <div className="styled-select">
                                  <select id="asset_category" name="asset_category" required="true" value={assetCategory} onChange={(e)=>{
                                  handleChange(e,"assetCategory")
                              }}>
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
                                        <label for="agreement_number">Agreement Number </label>
                                    </div>
                                    <div className="col-sm-6">
                                        <input type="text" name="agreement_number" id="agreement_number" value={agreementNumber} onChange={(e)=>{
                                  handleChange(e,"agreementNumber")
                              }}/>
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
                        <button type="button" className="btn btn-default btn-action pull-right" style={{marginRight:"2%"}} data-toggle="collapse" data-target="#demo"><span className="glyphicon glyphicon-plus"></span></button>
                      </div>
                        <div id="demo" className="collapse">
                          <div className="row">
                            <br/>
                              <div className="col-sm-6">
                                  <div className="row">
                                      <div className="col-sm-6 label-text">
                                          <label for="contact_no">Allottee contact </label>
                                      </div>
                                      <div className="col-sm-6">
                                          <div className="text-no-ui">
                                              <span>+91</span>
                                              <input type="text" id="contact_no" name="contact_no" value={mobileNumber} onChange={(e)=>{
                                  handleChange(e,"mobileNumber")
                              }}/>
                                          </div>
                                      </div>
                                  </div>
                              </div>

                              <div className="col-sm-6">
                                  <div className="row">
                                      <div className="col-sm-6 label-text">
                                          <label for="name">Allottee Name </label>
                                      </div>
                                      <div className="col-sm-6">
                                          <input  type="text" id="name" name="name" value={allotteeName} onChange={(e)=>{
                                  handleChange(e,"allotteeName")
                              }}/>
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
                                              <select id="locality" name="locality" value={locality} onChange={(e)=>{
                                  handleChange(e,"locality")
                              }}>
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
                                          <label for="ward">Revenue Ward no</label>
                                      </div>
                                      <div className="col-sm-6">
                                          <div className="styled-select">
                                              <select id="ward" name="ward" value={revenueWard} onChange={(e)=>{
                                  handleChange(e,"revenueWard")
                              }}>
                                                  <option value="">Choose Revenue Wards</option>
                                                  {renderOption(this.state.revenueWards)}

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

                              <div className="col-sm-6">
                                  <div className="row">
                                      <div className="col-sm-6 label-text">
                                          <label for="code">Asset Code </label>
                                      </div>
                                      <div className="col-sm-6">
                                          <div className="search-ui">
                                              <input type="text" name="code" id="code" value={code} onChange={(e)=>{
                                      handleChange(e,"code")
                                  }}/>

                                          </div>
                                      </div>
                                  </div>
                              </div>
                          </div>
                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label for="fromDate">Agreement Created from </label>
                                        </div>
                                        <div className="col-sm-6">
                                          <div className="text-no-ui">
                                              <span className="glyphicon glyphicon-calendar"></span>
                                            <input className="date-picker" type="text" name="fromDate" id="fromDate" value={fromDate} onChange={(e)=>{
                                    handleChange(e,"fromDate")
                                }}/>
                                          </div>
                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                  <div className="row">
                                      <div className="col-sm-6 label-text">
                                          <label for="toDate">Agreement Created To </label>
                                      </div>
                                      <div className="col-sm-6">
                                        <div className="text-no-ui">
                                            <span className="glyphicon glyphicon-calendar"></span>
                                          <input className="date-picker" type="text" name="toDate" id="toDate" value={toDate} onChange={(e)=>{
                                  handleChange(e,"toDate")
                              }}/>
                                        </div>
                                      </div>
                                  </div>
                                </div>
                            </div>
                          </div>
                        </div>
                  <div className="text-center">
                      <button type="submit" className="btn btn-submit">Search</button>&nbsp;&nbsp;
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
  <AgreementSearch />,
  document.getElementById('root')
);


// <div className="col-sm-6" id="shopping_complex_number">
//     <div className="row">
//         <div className="col-sm-6 label-text">
//             <label for="shopping_complex_no">Shopping Complex Number </label>
//         </div>
//         <div className="col-sm-6">
//             <input  type="text" name="shopping_complex_no" id="shopping_complex_no" value={shopComplexNumber} onChange={(e)=>{
//     handleChange(e,"shopComplexNumber")
// }}/>
//         </div>
//     </div>
// </div>
