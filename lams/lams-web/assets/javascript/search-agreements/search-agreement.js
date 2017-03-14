// $(document).ready(function() {
//
//     let user = {};
//     var basUrl = "http://peaceful-headland-36194.herokuapp.com/v1/mSevaAndLA/";
//
//     $("#table").hide();
//
//       $("#search").on("click",function()
//       {
//               $("#table").show();
//       })
//
//     $.ajax({
//             method: "POST",
//             url: `${basUrl}agreements?tenant_id=kul.am`,
//             data: {
//                 "api_id": "string",
//                 "ver": "string",
//                 "ts": "2017-01-18T07:18:23.130Z",
//                 "action": "string",
//                 "did": "string",
//                 "key": "string",
//                 "msg_id": "string",
//                 "requester_id": "string",
//                 "auth_token": "aeiou"
//             }
//         })
//         .done(function(response) {
//             // alert("Data Saved: " + msg);
//             $("#searchTableBody").html("");
//             $("#searchTableBody").append(`<tr>`);
//             console.log(response.Agreements[0]);
//             console.log(response.Agreements.length);
//             var agreement=[];
//
//             // <td>${response.Agreements[i].asset.ward}</td>
//             // <td>${response.Agreements[i].asset.electionward}</td>
//
//             for (var i = 0; i < response.Agreements.length; i++) {
//                 // response.Agreements[i]
//                 $("#searchTableBody").append(`<td>${i+1}</td>
//                   <td>${response.Agreements[i].agreement_number} </td>
//                   <td>${response.Agreements[i].allottee.name}</td>
//                   <td>${response.Agreements[i].allottee.contact_no}</td>
//                   <td>${response.Agreements[i].asset.zone}</td>
//
//
//                   <td>${response.Agreements[i].asset.category}</td>
//                   <td>${response.Agreements[i].asset.code}</td>
//                   <td>${response.Agreements[i].tradelicense_number}</td>
//                   <td>${response.Agreements[i].agreement_date}</td>
//                   <td>
//                       <div className="styled-select">
//                           <select id="myOptions">
//                             <option>Select Action</option>
//                               <option value="view">View</option>
//                                 <option value="renew">Renew</option>
//
//                           </select>
//                       </div>
//                   </td>`);
//             }
//             $("#searchTableBody").append(`</tr>`);
//
//
//             $('#myOptions').change(function() {
//                 var val = $("#myOptions option:selected").text();
//                 if (val === "Renew") {
//                     window.open("./view-renew-agreement.html?type=land&view=renew&agreement_id=aeiou", "", "width=1200,height=800")
//                 } else {
//                     window.open("./view-renew-agreement.html?type=land&view=new&agreement_id=aeiou", "", "width=1200,height=800")
//                 }
//             });
//
//
//
//
//
//
//         });
//
//         $('#close').on("click",function() {
//               window.close();
//         })
//
//
//
//     $('#searchAgreement').on("click", function() {
//         $.ajax({
//                 method: "POST",
//                 url: `${basUrl}agreements?tenant_id=kul.am`,
//                 data: {
//                     "api_id": "string",
//                     "ver": "string",
//                     "ts": "2017-01-18T07:18:23.130Z",
//                     "action": "string",
//                     "did": "string",
//                     "key": "string",
//                     "msg_id": "string",
//                     "requester_id": "string",
//                     "auth_token": "aeiou"
//                 }
//             })
//             .done(function(response) {
//                 // alert("Data Saved: " + msg);
//
//             });
//     });
//
//
//     $('#myOptions').change(function() {
//         var val = $("#myOptions option:selected").text();
//         if (val === "Renew") {
//             window.open("./renew-agreement.html", "fs", "fullscreen=yes")
//         } else {
//             window.open("./view-agreement.html", "fs", "fullscreen=yes")
//         }
//         // alert(val);
//         // if(val==="View")
//         // {
//         //     window.location="./view-agreement.html"
//         // }
//         // else if (val==="Renew") {
//         //     window.location="./renew-agreement.html"
//         // }
//         // else {
//         //     window.location="./view-dcp.html"
//         // }
//
//     });
//
//     $("#shopping_complex_number").hide();
//
//
//     $("#logout").on("click", function() {
//         //clear cookies and logout
//         $("#login").hide();
//         $("#dashboard").show();
//     });
//
//     var agreement = {};
//     //Getting data for user input
//     $("input").on("keyup", function() {
//         // console.log(this.value);
//         agreement[this.id] = this.value;
//     });
//
//     //Getting data for user input
//     $("select").on("change", function() {
//         // console.log(this.value);
//         if(this.selectedIndex!=0)
//         {
//           agreement[this.id] = this.value;
//
//         }
//         if(this.value==="shop")
//         {
//           $("#shopping_complex_number").show();
//         }
//         else {
//           $("#shopping_complex_number").hide();
//         }
//     });
//
//     //file change handle for file upload
//     $("input[type=file]").on("change", function(evt) {
//         // console.log(this.value);
//         // agreement[this.id] = this.value;
//         var file = evt.currentTarget.files[0];
//
//         //call post api update and update that url in pur agrement object
//     });
//
//     var validation_rules = {};
//     var final_validatin_rules = {};
//     var commom_fields_rules = {
//         asset_category: {
//             required: true
//         }
//     }
//
//     final_validatin_rules = Object.assign({}, commom_fields_rules);
//
//     for (var key in final_validatin_rules) {
//         if (final_validatin_rules[key].required) {
//             $(`label[for=${key}]`).append(`<span> *</span>`);
//         }
//         // $(`#${key}`).attr("disabled",true);
//     };
//
//     final_validatin_rules["messages"] = {
//         asset_category: {
//             required: "Select name of category to search"
//         }
//       }
//
//     $("#searchAgreementForm").validate({
//         rules: final_validatin_rules,
//         messages: final_validatin_rules["messages"],
//         submitHandler: function(form) {
//               $("#table").show();
//             // form.submit();
//
//             // console.log(agreement);
//             // $.post(`${baseUrl}agreements?tenant_id=kul.am`, {
//             //     RequestInfo: requestInfo,
//             //     Agreement: agreement
//             // }, function(response) {
//             //     // alert("submit");
//             //     window.open("../../../../app/search-assets/create-agreement-ack.html?&agreement_id=aeiou", "", "width=1200,height=800")
//             //     console.log(response);
//             // })
//         }
//     })
//
//
// });




class AgreementSearch extends React.Component {
  constructor(props) {
    super(props);
    this.state={agreements:[],searchSet:{locality:"",
    agreementNumber:"",
    doorNo:"",
    category:"",
    mobileNumber:"",
    name:"",
    revenueWard:"",
    electionWard:"",
    code:"",
    tenderNumber:"",createdDate:"",endDate:"",shopComplexNumber:""},isSearchClicked:false,assetCategories:[],locality:[],revenueWards:[],electionwards:[]}
    this.handleChange=this.handleChange.bind(this);
    this.search=this.search.bind(this);
  }

  search(e)
  {
    e.preventDefault();
    //call api call
    var agreements=commonApiPost("agreements","","_search",this.state.searchSet).responseJSON["Agreements"];
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

    this.setState({
      employeeType:[{
              id: 1,
              name: "Deputation",
              chartOfAccounts: ""
          },
          {
              id: 2,
              name: "Permanent",
              chartOfAccounts: ""
          },
          {
              id: 3,
              name: "Daily Wages`",
              chartOfAccounts: ""
          },
          {
              id: 4,
              name: "Temporary",
              chartOfAccounts: ""
          },
          {
              id: 5,
              name: "Contract",
              chartOfAccounts: ""
          }],
      departments:[{
              id: 1,
              name: "Juniour Engineer",
              description: "",
              orderno: "1",
              active: true
          },
          {
              id: 2,
              name: "Assistance Engineer",
              description: "",
              orderno: "1",
              active: true
          }],
      designation:[{
              id: 1,
              name: "Juniour Engineer",
              description: "",
              orderno: "1",
              active: true
          },
          {
              id: 2,
              name: "Assistance Engineer",
              description: "",
              orderno: "1",
              active: true
          }],
      active:[{
              id: 1,
              name: "Juniour Engineer",
              description: "",
              orderno: "1",
              active: true
          },
          {
              id: 2,
              name: "Assistance Engineer",
              description: "",
              orderno: "1",
              active: true
          }],
      employeeStatus:[{
              id: 1,
              name: "Juniour Engineer",
              description: "",
              orderno: "1",
              active: true
          },
          {
              id: 2,
              name: "Assistance Engineer",
              description: "",
              orderno: "1",
              active: true
          }],
      function:[{
              id: 1,
              name: "Juniour Engineer",
              description: "",
              orderno: "1",
              active: true
          },
          {
              id: 2,
              name: "Assistance Engineer",
              description: "",
              orderno: "1",
              active: true
          }],
      functionary:[{
              id: 1,
              name: "Juniour Engineer",
              description: "",
              orderno: "1",
              active: true
          },
          {
              id: 2,
              name: "Assistance Engineer",
              description: "",
              orderno: "1",
              active: true
          }],
      drawingOfficer:[{
              id: 1,
              name: "Juniour Engineer",
              description: "",
              orderno: "1",
              active: true
          },
          {
              id: 2,
              name: "Assistance Engineer",
              description: "",
              orderno: "1",
              active: true
          }]
    })
  }

  componentDidMount()
  {
    // console.log(commonApiPost("asset","assetCategories","_search",{}).responseJSON["AssetCategory"]);
    // console.log(commonApiPost("v1/location/boundarys","boundariesByBndryTypeNameAndHierarchyTypeName","",{boundaryTypeName:"LOCALITY",hierarchyTypeName:"LOCATION"}).responseJSON["Boundary"]);
    // console.log(commonApiPost("v1/location/boundarys","boundariesByBndryTypeNameAndHierarchyTypeName","",{boundaryTypeName:"WARD",hierarchyTypeName:"ADMINISTRATION"}).responseJSON["Boundary"]);
    // console.log(commonApiPost("v1/location/boundarys","boundariesByBndryTypeNameAndHierarchyTypeName","",{boundaryTypeName:"WARD",hierarchyTypeName:"REVENUE"}).responseJSON["Boundary"]);

    this.setState({
      assetCategories:commonApiPost("asset","assetCategories","_search",{}).responseJSON["AssetCategory"],
      locality:commonApiPost("v1/location/boundarys","boundariesByBndryTypeNameAndHierarchyTypeName","",{boundaryTypeName:"LOCALITY",hierarchyTypeName:"LOCATION"}).responseJSON["Boundary"],
      electionwards:commonApiPost("v1/location/boundarys","boundariesByBndryTypeNameAndHierarchyTypeName","",{boundaryTypeName:"WARD",hierarchyTypeName:"ADMINISTRATION"}).responseJSON["Boundary"],
      revenueWards:commonApiPost("v1/location/boundarys","boundariesByBndryTypeNameAndHierarchyTypeName","",{boundaryTypeName:"WARD",hierarchyTypeName:"REVENUE"}).responseJSON["Boundary"]
    })
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
             ordering: false
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

  handleSelectChange(type)
  {
    console.log(type);
    if (type === "renew") {
                window.open("../../../../app/search-agreement/renew-agreement.html", "fs", "fullscreen=yes")
            } else {
                window.open("../../../../app/search-agreement/view-renew-agreement.html", "fs", "fullscreen=yes")
            }
  }


  close(){
      // widow.close();
      open(location, '_self').close();
  }



  render() {
    console.log(this.state.searchSet);
    let {handleChange,search,updateTable,handleSelectChange}=this;
    let {isSearchClicked,agreements}=this.state;
    let {locality,
    agreementNumber,
    doorNo,
    category,
    mobileNumber,
    name,
    revenueWard,
    electionWard,
    code,
    tenderNumber,createdDate,endDate,shopComplexNumber}=this.state.searchSet;

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
                        <th>Asset type </th>
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
      return agreements.map((item,index)=>
      {
            return (<tr key={index}>
              <td>{index+1}</td>
                                <td>{item.agreementNumber} </td>
                                <td>{item.allottee.name}</td>
                                <td>{item.allottee.mobileNumber}</td>
                                <td>{item.asset.locationDetails.zone}</td>


                                <td>{item.asset.category?item.asset.category:"Null"}</td>
                                <td>{item.asset.code}</td>
                                <td>{item.tradelicenseNumber}</td>
                                <td>{item.agreementDate}</td>
                                <td>
                                    <div className="styled-select">
                                        <select id="myOptions" onChange={(e)=>{
                                          handleSelectChange(e.target.value)
                                        }}>
                                            <option value="">Select Action</option>
                                            <option value="view">View</option>
                                            <option value="renew">Renew</option>
                                        </select>
                                    </div>
                                </td>
                </tr>
            );

      })
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
                                  <select id="asset_category" name="asset_category" required="true" value={category} onChange={(e)=>{
                                  handleChange(e,"category")
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
                                          <label id="name">Allottee Name </label>
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
                              <div className="col-sm-6" id="shopping_complex_number">
                                  <div className="row">
                                      <div className="col-sm-6 label-text">
                                          <label for="shopping_complex_no">Shopping Complex Number </label>
                                      </div>
                                      <div className="col-sm-6">
                                          <input  type="text" name="shopping_complex_no" id="shopping_complex_no" value={shopComplexNumber} onChange={(e)=>{
                                  handleChange(e,"shopComplexNumber")
                              }}/>
                                      </div>
                                  </div>
                              </div>
                          </div>
                          <div className="row">

                              <div className="col-sm-6">
                                  <div className="row">
                                      <div className="col-sm-6 label-text">
                                          <label for="code">Asset Number </label>
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
                          </div>
                        </div>









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
  <AgreementSearch />,
  document.getElementById('root')
);
