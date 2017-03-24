class SearchAsset extends React.Component {
  constructor(props) {
    super(props);
    this.state={list:[],searchSet:{
      "tenantId": "",
      "name": "",
      "department": "",
      "assetCategory": "",
      "status": "",
      "code": ""
   },isSearchClicked:false,assetCategories:[],departments:[],statusList:{}}
    this.handleChange=this.handleChange.bind(this);
    this.search=this.search.bind(this);

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

  search(e)
  {
    e.preventDefault();
    //call api call
    var list=commonApiPost("asset-services","assets","_search",this.state.searchSet).responseJSON["Assets"];
     //console.log(commonApiPost("asset-services","assets","_search",this.state.searchSet).responseJSON["Assets"]);
    this.setState({
      isSearchClicked:true,
      list
    })

    // $('#agreementTable').DataTable().draw();
    // console.log($('#agreementTable').length);

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
  }




  componentDidMount()
  {
    //console.log(commonApiGet("asset-services","","GET_STATUS",{}).responseJSON);


     this.setState({
      assetCategories:commonApiPost("asset-services","assetCategories","_search",{}).responseJSON["AssetCategory"] ||[],
      departments:commonApiPost("egov-common-masters","departments","_search",{tenantId}).responseJSON["Department"] || [],
      statusList:commonApiGet("asset-services","","GET_STATUS",{}).responseJSON|| {}
    })
  }

  close(){
      // widow.close();
      open(location, '_self').close();
  }


  render() {
      let {handleChange,search}=this;
      let {assetCategory,name,code,department,status}=this.state.searchSet;
      let {isSearchClicked,list}=this.state;

      const renderOption=function(list)
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

      const showTable=function()
      {
        if(isSearchClicked)
        {
            return (
              <table id="agreementTable" className="table table-bordered">
                  <thead>
                  <tr>
                      <th>Code</th>
                      <th>Name</th>
                      <th>Asset Category Type</th>
                      <th>Department</th>
                      <th>Status</th>
                      <th>Asset Details</th>
                      <th>Action</th>
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

    const renderAction=function(type,id){
      if (type==="update") {

              return (
                      <a href={`../../../../app/asset/create-asset.html?id=${id}&type=${type}`} className="btn btn-default btn-action"><span className="glyphicon glyphicon-pencil"></span></a>
              );

    }else {
            return (
                    <a href={`../../../../app/asset/create-asset.html?id=${id}&type=${type}`} className="btn btn-default btn-action"><span className="glyphicon glyphicon-modal-window"></span></a>
            );
        }
}


    const renderBody=function()
    {
      return list.map((item,index)=>
      {
            return (<tr key={index}>

                                <td>{item.code}</td>
                                <td>{item.name}</td>
                                <td>{item.assetCategory.name}</td>
                                <td>{item.department.name?item.department.name:"NULL"}</td>
                                <td>{item.status}</td>
                                <td>{item.assetDetails?item.assetDetails:"NULL"}</td>

                                <td data-label="action">
                    {renderAction(getUrlVars()["type"],item.id)}
                    </td>
                            </tr>  );
      })
    }

    return (
      <div>
      <div className="form-section-inner">
        <form onSubmit={(e)=>{search(e)}}>
        <div className="row">
          <div className="col-sm-6">
            <div className="row">
              <div className="col-sm-6 label-text">
                <label for="code">  Code </label>
              </div>
              <div className="col-sm-6">
                <input id="code" name="code" value={code} type="text"
                  onChange={(e)=>{handleChange(e,"code")}}/>
              </div>
            </div>
          </div>
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label for="name">  Name </label>
                  </div>
                  <div className="col-sm-6">
                    <input id="name" name="name" value={name} type="text"
                      onChange={(e)=>{handleChange(e,"name")}}/>
                  </div>
                </div>
              </div>
            </div>
                <div className="row">
                  <div className="col-sm-6">
                    <div className="row">
                      <div className="col-sm-6 label-text">
                          <label for="assetCategory">Asset Category <span> *</span> </label>
                        </div>
                      <div className="col-sm-6">
                         <div className="styled-select">
                         <select id="assetCategory" name="assetCategory" required="true" value={assetCategory} onChange={(e)=>{
                         handleChange(e,"assetCategory")}}>
                             <option value="">Select Asset Category</option>
                             {renderOption(this.state.assetCategories)}
                           </select>
                      </div>
                    </div>
                  </div>
                </div>

              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                      <label for="department">Department</label>
                  </div>
                  <div className="col-sm-6">
                    <div className="styled-select">
                  <select id="department" name="department" value={department} onChange={(e)=>{
                          handleChange(e,"department")
                      }}>
                        <option>Select Department</option>
                        {renderOption(this.state.departments)}
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
                          <label for="status">Status  </label>
                  </div>
                  <div className="col-sm-6">
                      <div className="styled-select">
                      <select id="status" name="status" value={status} onChange={(e)=>{
                              handleChange(e,"status")
                          }}>
                            <option>Select Department</option>
                            {renderOption(this.state.statusList)}
                         </select>
                  </div>
                </div>
              </div>
            </div>
        </div>
          <br/>

              <div className="text-center">
                  <button type="submit" className="btn btn-submit">Search</button>
                  <button type="button" className="btn btn-close" onClick={(e)=>{this.close()}}>Close</button>
              </div>
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
  <SearchAsset />,
  document.getElementById('root')
);

// <div role="tabpanel" className="tab-pane" id="assignmentDetails">
// <div className="form-section" >
//         <h3 className="pull-left">Asset Search Result </h3>
// <div className="clearfix"></div>
// <div className="land-table">
//             <table className="table table-bordered">
//                 <thead>
//                     <tr>
//                         <th>Code</th>
//                         <th>Name</th>
//                         <th>Asset Category Type</th>
//                         <th>Department</th>
//                         <th>Status</th>
//                         <th>Asset Details</th>
//                         <th>Action</th>
//                     </tr>
//                 </thead>
//
//                 <tbody>
//                   <tr>
//                     <td> 001 </td>
//                     <td> Land</td>
//                   <td> Land </td>
//                   <td>  Land</td>
//                   <td> Buying</td>
//                   <td>400 sq.ft</td>
//
//                   <td> <button className="btn btn-default btn-action"><span className="glyphicon glyphicon-trash"></span></button>
//                     <button className="btn btn-default btn-action"><span className="glyphicon glyphicon-pencil"></span></button>
//                   </td>
//                   </tr>
//
//                 </tbody>
//
//             </table>
//
//         </div>
//         </div>
//         </div>
