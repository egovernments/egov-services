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
    this.setInitialState = this.setInitialState.bind(this);
    this.handleClick = this.handleClick.bind(this);
  }

  search(e) {
    e.preventDefault();
    var _this = this;
    try {
      //call api call
      commonApiPost("asset-services", "assetCategories", "_search", {...this.state.searchSet ,tenantId, pageSize:500}, function(err, res) {
        if(res) {
          var list = res["AssetCategory"];
          flag = 1;
          _this.setState({
            isSearchClicked: true,
            list
          })
        }
      })
    } catch(e) {
      console.log(e);
    }
  }

  close(){
      open(location, '_self').close();
  }

  setInitialState(initState) {
    this.setState(initState);
  }

  componentDidMount() {
    if(window.opener && window.opener.document) {
      var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
      if(logo_ele && logo_ele[0]) {
        document.getElementsByClassName("homepage_logo")[0].src = window.location.origin + logo_ele[0].getAttribute("src");
      }
    }
    $('#hpCitizenTitle').text(titleCase(getUrlVars()["type"]) + " Asset Category");

    var count = 2, _this = this, _state = {};
      let self = this;
    var checkCountNCall = function(key, res) {
      count--;
      _state[key] = res;
      if(count == 0)
        _this.setInitialState(_state);
    }

    getDropdown("asset_category_type", function(res) {
      checkCountNCall("asset_category_type", res);
    });

    commonApiPost("asset-services", "assetCategories", "_search", { tenantId,isParentCategory:true}, function(err, res) {
      self.setState({assetCategories:res.AssetCategory})
    });

    getDropdown("assignments_unitOfMeasurement", function(res) {
      checkCountNCall("assignments_unitOfMeasurement", res);
    });
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

  handleClick(type, id) {
    if (type === "update") {
      window.open(`app/asset/create-asset-category.html?id=${id}&type=update`, '_blank', 'height=760, width=800, scrollbars=yes, status=yes');
    } else if(type) {
      window.open(`app/asset/create-asset-category.html?id=${id}&type=${type}`, '_blank', 'height=760, width=800, scrollbars=yes, status=yes');
    } else {
      window.open(`app/asset/create-asset-category.html?id=${id}&type=view`, '_blank', 'height=760, width=800, scrollbars=yes, status=yes');
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

  render() {
    let {handleChange,search,handleClick}=this;
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
              return (<tr key={index} onClick={() => {handleClick(getUrlVars()["type"], item.id)}}>
                                  <td>{item.code}</td>
                                  <td>{item.name}</td>
                                  <td>{item.assetCategoryType}</td>
                                  <td>{getNameById(assetCategories,item.parent)}</td>
                                  <td>{getNameById(assignments_unitOfMeasurement,item.unitOfMeasurement,"narration")}</td>
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
