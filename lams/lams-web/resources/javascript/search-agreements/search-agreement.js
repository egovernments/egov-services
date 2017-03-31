class AgreementSearch extends React.Component {
  constructor(props) {
    super(props);
    this.state={agreements:[],searchSet:{locality:"",
    agreementNumber:"",
    doorNo:"",
    assetCategory:"",
    mobileNumber:"",
    name:"",
    revenueWard:"",
    electionWard:"",
    code:"",
    tenderNumber:"",createdDate:"",endDate:""},isSearchClicked:false,assetCategories:[],locality:[],revenueWards:[],electionwards:[]}
    this.handleChange=this.handleChange.bind(this);
    this.search=this.search.bind(this);
  }

  search(e)
  {
    e.preventDefault();
    //call api call
    var agreements=commonApiPost("lams-services","agreements","_search",this.state.searchSet).responseJSON["Agreements"] ||[];
    // console.log(agreements);
    this.setState({
      isSearchClicked:true,
      agreements
    })

    // $('#agreementTable').DataTable().draw();
    // console.log($('#agreementTable').length);

  }

  componentWillMount()
  {
    // var agreements=[];
    // for(var i=1;i<=100;i++)
    // {
    //     agreements.push({
    //         code:i,name:"murali"+i,designation:'xyz',department:"yxs",userName:"avs"
    //     })
    // }
    // this.setState({
    //   isSearchClicked:true,
    //   agreements
    // })

    // console.log(commonApiGet("asset","","GET_MODE_OF_ACQUISITION",{}).responseJSON);

    // this.setState({
    //   employeeType:[{
    //           id: 1,
    //           name: "Deputation",
    //           chartOfAccounts: ""
    //       },
    //       {
    //           id: 2,
    //           name: "Permanent",
    //           chartOfAccounts: ""
    //       },
    //       {
    //           id: 3,
    //           name: "Daily Wages`",
    //           chartOfAccounts: ""
    //       },
    //       {
    //           id: 4,
    //           name: "Temporary",
    //           chartOfAccounts: ""
    //       },
    //       {
    //           id: 5,
    //           name: "Contract",
    //           chartOfAccounts: ""
    //       }],
    //   departments:[{
    //           id: 1,
    //           name: "Juniour Engineer",
    //           description: "",
    //           orderno: "1",
    //           active: true
    //       },
    //       {
    //           id: 2,
    //           name: "Assistance Engineer",
    //           description: "",
    //           orderno: "1",
    //           active: true
    //       }],
    //   designation:[{
    //           id: 1,
    //           name: "Juniour Engineer",
    //           description: "",
    //           orderno: "1",
    //           active: true
    //       },
    //       {
    //           id: 2,
    //           name: "Assistance Engineer",
    //           description: "",
    //           orderno: "1",
    //           active: true
    //       }],
    //   active:[{
    //           id: 1,
    //           name: "Juniour Engineer",
    //           description: "",
    //           orderno: "1",
    //           active: true
    //       },
    //       {
    //           id: 2,
    //           name: "Assistance Engineer",
    //           description: "",
    //           orderno: "1",
    //           active: true
    //       }],
    //   employeeStatus:[{
    //           id: 1,
    //           name: "Juniour Engineer",
    //           description: "",
    //           orderno: "1",
    //           active: true
    //       },
    //       {
    //           id: 2,
    //           name: "Assistance Engineer",
    //           description: "",
    //           orderno: "1",
    //           active: true
    //       }],
    //   function:[{
    //           id: 1,
    //           name: "Juniour Engineer",
    //           description: "",
    //           orderno: "1",
    //           active: true
    //       },
    //       {
    //           id: 2,
    //           name: "Assistance Engineer",
    //           description: "",
    //           orderno: "1",
    //           active: true
    //       }],
    //   functionary:[{
    //           id: 1,
    //           name: "Juniour Engineer",
    //           description: "",
    //           orderno: "1",
    //           active: true
    //       },
    //       {
    //           id: 2,
    //           name: "Assistance Engineer",
    //           description: "",
    //           orderno: "1",
    //           active: true
    //       }],
    //   drawingOfficer:[{
    //           id: 1,
    //           name: "Juniour Engineer",
    //           description: "",
    //           orderno: "1",
    //           active: true
    //       },
    //       {
    //           id: 2,
    //           name: "Assistance Engineer",
    //           description: "",
    //           orderno: "1",
    //           active: true
    //       }]
    // })
  }

  componentDidMount()
  {
    let _this = this; 
    this.setState({
      assetCategories:commonApiPost("asset-services","assetCategories","_search",{}).responseJSON["AssetCategory"] || [],
      locality:commonApiPost("v1/location/boundarys","boundariesByBndryTypeNameAndHierarchyTypeName","",{boundaryTypeName:"LOCALITY",hierarchyTypeName:"LOCATION"}).responseJSON["Boundary"] || [],
      electionwards:commonApiPost("v1/location/boundarys","boundariesByBndryTypeNameAndHierarchyTypeName","",{boundaryTypeName:"WARD",hierarchyTypeName:"ADMINISTRATION"}).responseJSON["Boundary"] || [],
      revenueWards:commonApiPost("v1/location/boundarys","boundariesByBndryTypeNameAndHierarchyTypeName","",{boundaryTypeName:"WARD",hierarchyTypeName:"REVENUE"}).responseJSON["Boundary"] || []
    })

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
  }

  componentDidUpdate(prevProps, prevState)
  {
      if (prevState.agreements.length!=this.state.agreements.length) {
          // $('#agreementTable').DataTable().draw();
          // alert(prevState.agreements.length);
          // alert(this.state.agreements.length);
          // alert('updated');
          $('#agreementTable').DataTable({
            dom: 'Bfrtip',
            buttons: [
                     'copy', 'csv', 'excel', 'pdf', 'print'
             ],
             ordering: false,
             bDestroy: true
          });
      }
  }

  handleChange(e,name)
  {
      this.setState({
          searchSet:{
              ...this.state.searchSet,
              [name]:e.target.value
          }
      })
  }

  handleSelectChange(type,id,number,assetCategory)
  {
    console.log(type);
    if (type === "renew") {
                window.open("../../../../app/search-agreement/view-renew-agreement.html?view=renew&type="+assetCategory+"&agreementNumber="+number+"&assetId="+id, "fs", "fullscreen=yes")
            } else {
                window.open("../../../../app/search-agreement/view-renew-agreement.html?view=new&type="+assetCategory+"&agreementNumber="+number+"&assetId="+id, "fs", "fullscreen=yes")
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
    name,
    revenueWard,
    electionWard,
    code,
    tenderNumber,createdDate,endDate,shopComplexNumber}=this.state.searchSet;

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
                        <th>Asset Number </th>
                        <th>Trade license No </th>
                        <th>Agreement Date </th>
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
        {
              return (<tr key={index}>
                <td>{index+1}</td>
                                  <td>{item.agreementNumber} </td>
                                  <td>{item.allottee.name}</td>
                                  <td>{item.allottee.mobileNumber}</td>
                                  <td>{item.asset.locationDetails.zone}</td>


                                  <td>{item.asset.assetCategory.id?getValueByName("name",item.asset.assetCategory.id):"Null"}</td>
                                  <td>{item.asset.code}</td>
                                  <td>{item.tradelicenseNumber}</td>
                                  <td>{item.agreementDate}</td>
                                  <td>
                                      <div className="styled-select">
                                          <select id="myOptions" onChange={(e)=>{
                                            handleSelectChange(e.target.value,item.asset.id,item.agreementNumber,getValueByName("name",item.asset.assetCategory.id))
                                          }}>
                                              <option value="">Select Action</option>
                                              <option value="view">View</option>
                                            { /*<option value="renew">Renew</option> */}
                                          </select>
                                      </div>
                                  </td>
                  </tr>
              );

        })
      }
      else {
          return (<tr>
              <td > </td>
          </tr>)
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
                        <a href="#" className="btn btn-default btn-action pull-right" style={{marginRight:"2%"}} data-toggle="collapse" data-target="#demo"><span className="glyphicon glyphicon-plus"></span></a>
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
                                          <input  type="text" id="name" name="name" value={name} onChange={(e)=>{
                                  handleChange(e,"name")
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
                                            <label for="from_date">Agreement Created from </label>
                                        </div>
                                        <div className="col-sm-6">
                                          <div className="text-no-ui">
                                              <span className="glyphicon glyphicon-calendar"></span>
                                            <input type="date" name="from_date" id="from_date" value={createdDate} onChange={(e)=>{
                                    handleChange(e,"createdDate")
                                }}/>
                                          </div>
                                        </div>
                                    </div>
                                </div>


                                <div className="col-sm-6">
                                  <div className="row">
                                      <div className="col-sm-6 label-text">
                                          <label for="to_date">Agreement Created To </label>
                                      </div>
                                      <div className="col-sm-6">
                                        <div className="text-no-ui">
                                            <span className="glyphicon glyphicon-calendar"></span>
                                          <input type="date" name="to_date" id="to_date" value={endDate} onChange={(e)=>{
                                  handleChange(e,"endDate")
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
                                            <label for="trade_license_number">Trade license </label>
                                        </div>
                                        <div className="col-sm-6">
                                            <input type="text" id="trade_license_number" name="trade_license_number" value={tenderNumber} onChange={(e)=>{
                                    handleChange(e,"tenderNumber")
                                }}/>
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
