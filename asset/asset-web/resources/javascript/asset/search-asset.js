var flag = 0;
class SearchAsset extends React.Component {
  constructor(props) {
    super(props);
    this.state={list:[],searchSet:{
      "tenantId": tenantId,
      "name": "",
      "department": "",
      "assetCategory": "",
      "status": "",
      "code": ""
   },isSearchClicked:false,assetCategories:[],departments:[],statusList:{}}
    this.handleChange=this.handleChange.bind(this);
    this.search=this.search.bind(this);

  }

  handleChange(e,name) {

      this.setState({
          searchSet:{
              ...this.state.searchSet,
              [name]:e.target.value
          }
      })

  }

  search(e) {
    e.preventDefault();
    try {
      //call api call
      var list=commonApiPost("asset-services","assets","_search", {...this.state.searchSet, tenantId, pageSize:500}).responseJSON["Assets"];
      flag = 1;
      this.setState({
        isSearchClicked:true,
        list
      })
    } catch(e) {
      console.log(e);
    }
  }

  componentWillUpdate() {
    if(flag == 1) {
      flag = 0;
      $('#agreementTable').dataTable().fnDestroy();
    }
  }

  componentDidUpdate(prevProps, prevState) {
      if (prevState.list.length!=this.state.list.length) {
          $('#agreementTable').DataTable({
            dom: 'Bfrtip',
            buttons: [
                     'copy', 'csv', 'excel', 'pdf', 'print'
             ],
             ordering: false,
             language: {
                "emptyTable": "No Records"
             }
          });
      }
  }




  componentDidMount()
  {
    if(window.opener && window.opener.document) {
      var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
      if(logo_ele && logo_ele[0]) {
        document.getElementsByClassName("homepage_logo")[0].src = window.location.origin + logo_ele[0].getAttribute("src");
      }
    }
$('#hpCitizenTitle').text(titleCase(getUrlVars()["type"]) + " Asset");
    var assetCategories, departments, statusList;
    try { assetCategories = !localStorage.getItem("assetCategories") || localStorage.getItem("assetCategories") == "undefined" ? (localStorage.setItem("assetCategories", JSON.stringify(commonApiPost("asset-services", "assetCategories", "_search", {tenantId}).responseJSON["AssetCategory"] || [])), JSON.parse(localStorage.getItem("assetCategories"))) : JSON.parse(localStorage.getItem("assetCategories")); } catch (e) {
        console.log(e);
        assetCategories = [];
    }

    try { departments = !localStorage.getItem("assignments_department") || localStorage.getItem("assignments_department") == "undefined" ? (localStorage.setItem("assignments_department", JSON.stringify(getCommonMaster("egov-common-masters", "departments", "Department").responseJSON["Department"] || [])), JSON.parse(localStorage.getItem("assignments_department"))) : JSON.parse(localStorage.getItem("assignments_department")); } catch (e) {
        console.log(e);
        departments = [];
    }

    try { statusList = !localStorage.getItem("statusList") || localStorage.getItem("statusList") == "undefined" ? (localStorage.setItem("statusList", JSON.stringify(commonApiGet("asset-services", "", "GET_STATUS", {tenantId}).responseJSON || {})), JSON.parse(localStorage.getItem("statusList"))) : JSON.parse(localStorage.getItem("statusList")); } catch (e) {
        console.log(e);
        statusList = {};
    }

    this.setState({
      assetCategories,
      departments,
      statusList
    })
  }

  close(){
      // widow.close();
      open(location, '_self').close();
  }


  render() {
      let {handleChange,search}=this;
      let {assetCategory,name,code,department,status}=this.state.searchSet;
      let {isSearchClicked,list, departments}=this.state;

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
      if (type === "update") {
        return (
          <a href={`app/asset/create-asset.html?id=${id}&type=update`} className="btn btn-default btn-action"><span className="glyphicon glyphicon-pencil"></span></a>
        );
      } else if(type) {
        return (
          <a href={`app/asset/create-asset.html?id=${id}&type=${type}`} className="btn btn-default btn-action"><span className="glyphicon glyphicon-modal-window"></span></a>
        );
      } else {
        return (
              <a href={`app/asset/create-asset.html?id=${id}&type=view`} className="btn btn-default btn-action"><span className="glyphicon glyphicon-modal-window"></span></a>
        );
      }
}


    const renderBody=function()
    {
      if (list.length>0) {
        return list.map((item,index)=>
        {
              return (<tr key={index}>

                                  <td>{item.code}</td>
                                  <td>{item.name}</td>
                                  <td>{item.assetCategory.name}</td>
                                  <td>{getNameById(departments,item.department.id)}</td>
                                  <td>{item.status}</td>
                                  <td data-label="action">
                      {renderAction(getUrlVars()["type"],item.id)}
                      </td>
                  </tr>  );
        })

      }
    }

    return (
      <div>
      <h3>{titleCase(getUrlVars()["type"])} Asset</h3>
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
                            <option>Select Status</option>
                            {renderOption(this.state.statusList)}
                         </select>
                  </div>
                </div>
              </div>
            </div>
        </div>
          <br/>

              <div className="text-center">
                  <button type="submit" className="btn btn-submit">Search</button>&nbsp;&nbsp;
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
