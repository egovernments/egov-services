class AssetSearch extends React.Component {
  constructor(props) {
    super(props);
    this.state={list:[],searchSet:{
    locality:"",
    doorNo:"",
    assetCategory:"",
    name:"",
    electionWard:"",
    code:""},isSearchClicked:false,assetCategories:[],locality:[],electionwards:[]}
    this.handleChange=this.handleChange.bind(this);
    this.search=this.search.bind(this);
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

  componentWillMount()
  {


  }



  componentDidMount()
  {

     this.setState({
      assetCategories,
      locality,
      electionwards
    })
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
             ordering: false,
             bDestroy: true
          });
      }
      // else {
      //   $('#agreementTable').DataTable({
      //     dom: 'Bfrtip',
      //     buttons: [
      //              'copy', 'csv', 'excel', 'pdf', 'print'
      //      ],
      //      ordering: false
      //   });
      // }
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

  handleSelectChange(type,category)
  {
    console.log(type);
    console.log(category);
    // if (type === "view") {
    //             window.open("../../../../app/agreements/new.html", "fs", "fullscreen=yes")
    //          }
    //          else {
    //
    //             window.open("../../../../app/agreements/new.html", "fs", "fullscreen=yes")
    //         }
    window.open("app/agreements/new.html?type="+category, "fs", "fullscreen=yes")
  }


  close(){
      // widow.close();
      open(location, '_self').close();
  }



  render() {
    let {handleChange,search,updateTable,handleSelectChange}=this;
    let {isSearchClicked,list}=this.state;
    let {
    locality,
    doorNo,
    assetCategory,
    name,
    electionWard,
    code}=this.state.searchSet;

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
    const renderBody=function()
    {
      if(list.length>0)
      {
        return list.map((item,index)=>
        {
              return (<tr key={index}>
                <td>{index+1}</td>
                                  <td>{item.assetCategory.name}</td>
                                  <td>{item.name}</td>
                                  <td>{item.code}</td>
                                  <td>{item.locationDetails.electionWard}</td>
                                  <td>
                                      <div className="styled-select">
                                          <select id="myOptions" onChange={(e)=>{
                                            handleSelectChange(e.target.value,item.assetCategory.name)
                                          }}>
                                              <option value="">Select Action</option>
                                              <option value="create">Create</option>

                                          </select>
                                      </div>
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
                          Note: Any one field is mandatory other than Asset category.
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
