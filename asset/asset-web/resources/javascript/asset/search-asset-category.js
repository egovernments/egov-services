var flag = 0;
class SearchAssetCategory extends React.Component {
  constructor(props) {
    super(props);
    this.state={list:[],searchSet:{
      "name": "",
      "assetCategoryType": "",
   },isSearchClicked:false,asset_category_type:[], assetCategories: [], assignments_unitOfMeasurement: []}
    this.handleChange=this.handleChange.bind(this);
    this.search=this.search.bind(this);

  }

  search(e) {
    e.preventDefault();
    try {
      //call api call
      var list = commonApiPost("asset-services","assetCategories","_search",{...this.state.searchSet,tenantId,pageSize:500}).responseJSON["AssetCategory"];
      flag = 1;
      this.setState({
        isSearchClicked:true,
        list
      })
    } catch(e) {
      console.log(e);
    }
  }

  close(){
      open(location, '_self').close();
  }

  componentDidMount() {
    if(window.opener && window.opener.document) {
      var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
      if(logo_ele && logo_ele[0]) {
        document.getElementsByClassName("homepage_logo")[0].src = window.location.origin + logo_ele[0].getAttribute("src");
      }
    }
    $('#hpCitizenTitle').text(titleCase(getUrlVars()["type"]) + " Asset Category");
    
    var asset_category_type, assetCategories, assignments_unitOfMeasurement;
    try { asset_category_type = !localStorage.getItem("asset_category_type") || localStorage.getItem("asset_category_type") == "undefined" ? (localStorage.setItem("asset_category_type", JSON.stringify(commonApiGet("asset-services", "", "GET_ASSET_CATEGORY_TYPE", {tenantId}).responseJSON || {})), JSON.parse(localStorage.getItem("asset_category_type"))) : JSON.parse(localStorage.getItem("asset_category_type")); } catch (e) {
        console.log(e);
        asset_category_type = {};
    }

    try { assetCategories = !localStorage.getItem("assetCategories") || localStorage.getItem("assetCategories") == "undefined" ? (localStorage.setItem("assetCategories", JSON.stringify(commonApiPost("asset-services", "assetCategories", "_search", {tenantId}).responseJSON["AssetCategory"] || [])), JSON.parse(localStorage.getItem("assetCategories"))) : JSON.parse(localStorage.getItem("assetCategories")); } catch (e) {
        console.log(e);
        assetCategories = [];
    }

    try { assignments_unitOfMeasurement = !localStorage.getItem("assignments_unitOfMeasurement") || localStorage.getItem("assignments_unitOfMeasurement") == "undefined" ? (localStorage.setItem("assignments_unitOfMeasurement", JSON.stringify(commonApiPost("egov-common-masters", "uoms", "_search", {tenantId}).responseJSON["uoms"] || [])), JSON.parse(localStorage.getItem("assignments_unitOfMeasurement"))) : JSON.parse(localStorage.getItem("assignments_unitOfMeasurement")); } catch (e) {
        console.log(e);
        assignments_unitOfMeasurement = [];
    }

    this.setState({
      asset_category_type,
      assetCategories,
      assignments_unitOfMeasurement
    })
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
             bDestroy: true,
             language: {
                "emptyTable": "No Records"
             }
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

  render() {
    let {handleChange,search}=this;
    let {assetCategoryType,name}=this.state.searchSet;
    let {isSearchClicked, list, assignments_unitOfMeasurement, assetCategories}=this.state;

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

    const renderBody=function()
    {
      if (list.length>0) {
        return list.map((item,index)=>
        {
              return (<tr key={index}>
                                  <td>{item.code}</td>
                                  <td>{item.name}</td>
                                  <td>{item.assetCategoryType}</td>
                                  <td>{getNameById(assetCategories,item.parent)}</td>
                                  <td>{getNameById(assignments_unitOfMeasurement,item.unitOfMeasurement,"narration")}</td>

                                  <td data-label="action">
                      {renderAction(getUrlVars()["type"],item.id)}
                      </td>
                              </tr>  );
        })
      }
      else {
         return (
             ""
         )
     }
}


    const renderAction=function(type,id){
      if (type==="update") {

              return (
                      <a href={`app/asset/create-asset-category.html?id=${id}&type=${type}`} className="btn btn-default btn-action"><span className="glyphicon glyphicon-pencil"></span></a>
              );

    }else {
            return (
                    <a href={`app/asset/create-asset-category.html?id=${id}&type=${type}`} className="btn btn-default btn-action"><span className="glyphicon glyphicon-modal-window"></span></a>
            );
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
                    <th>Parent Category</th>
                    <th>Unit Of Measurment</th>
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

    return (
    <div>
      <h3>{titleCase(getUrlVars()["type"])} Asset Category</h3>
      <div className="form-section-inner">
        <form onSubmit={(e)=>{search(e)}}>
          <div className="row">
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label for="name">  Name</label>
                  </div>
                  <div className="col-sm-6">
                    <input id="name" name="name" value={name} type="text"
                      onChange={(e)=>{handleChange(e,"name")}}/>
                  </div>
                </div>
              </div>

              <div className="col-sm-6">
                  <div className="row">
                      <div className="col-sm-6 label-text">
                          <label for="assetCategoryType">Asset Category Type <span> *</span></label>
                      </div>
                      <div className="col-sm-6">
                          <div className="styled-select">
                          <select id="assetCategoryType" name="assetCategoryType" value={assetCategoryType} required= "true" onChange={(e)=>{
                          handleChange(e,"assetCategoryType")}}>
                              <option value="">Select Asset Category</option>
                              {renderOption(this.state.asset_category_type)}
                            </select>
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
            <div className="table-cont" id="table">
                {showTable()}
            </div>
        </div>

    );
  }
}


ReactDOM.render(
  <SearchAssetCategory />,
  document.getElementById('root')
);
