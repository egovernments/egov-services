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
   },isSearchClicked:false,assetCategories:[],departments:[],statusList:{}, modify: false}
    this.handleChange = this.handleChange.bind(this);
    this.search = this.search.bind(this);
    this.handleClick = this.handleClick.bind(this);
    this.setInitialState = this.setInitialState.bind(this);
  }

  handleChange(e, name) {
      this.setState({
          searchSet:{
              ...this.state.searchSet,
              [name]:e.target.value
          }
      })
  }

  setInitialState(initState) {
    this.setState(initState);
  }

  search(e) {
    e.preventDefault();
    try {
      //call api call
      var _this = this;
      commonApiPost("asset-services","assets","_search", {...this.state.searchSet, tenantId, pageSize:500}, function(err, res) {
        if(res) {
          var list = res["Assets"];
          list.sort(function(item1, item2) {
            return item1.code.toLowerCase() > item2.code.toLowerCase() ? 1 : item1.code.toLowerCase() < item2.code.toLowerCase() ? -1 : 0;
          })
          flag = 1;
          _this.setState({
            isSearchClicked: true,
            list,
            modify: true
          });

          setTimeout(function(){
            _this.setState({
              modify: false
            });
          }, 1200);
        }
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
      if (this.state.modify) {
          $('#agreementTable').DataTable({
            dom: 'Bfrtip',
            buttons: [
                     'copy', 'csv', 'excel', 'pdf', 'print'
             ],
             "ordering": false,
             "bDestroy": true,
             language: {
                "emptyTable": "No Records"
             }
          });
      }
  }

  componentDidMount() {
    if(window.opener && window.opener.document) {
      var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
      if(logo_ele && logo_ele[0]) {
        document.getElementsByClassName("homepage_logo")[0].src = window.location.origin + logo_ele[0].getAttribute("src");
      }
    }
    if (getUrlVars()["type"] === "update") {
      $('#hpCitizenTitle').text("Modify Asset");
    }
    else {
      $('#hpCitizenTitle').text(titleCase(getUrlVars()["type"]) + " Asset");
    }
    var count = 2, _this = this, _state = {};
      let self = this;
    var checkCountNCall = function(key, res) {
      count--;
      _state[key] = res;
      if(count == 0)
        _this.setInitialState(_state);
    }

    commonApiPost("asset-services", "assetCategories", "_search", { tenantId,isChildCategory:true}, function(err, res) {
      self.setState({assetCategories:res.AssetCategory})
    });
    getDropdown("assignments_department", function(res) {
      checkCountNCall("departments", res);
    });
    getDropdown("statusList", function(res) {
      checkCountNCall("statusList", res);
    });
  }

  close() {
      open(location, '_self').close();
  }

  handleClick(type, id, status) {
    if(type == "sale" && status != "CAPITALIZED")
      return showError("Asset sale/disposal is only possible for assets with status as 'Capitalized'");
    if(type == "revaluate")
      window.open(`app/asset/create-asset-revaluation.html?id=${id}`, '_blank', 'height=760, width=800, scrollbars=yes, status=yes');
    else if(type == "sale")
      window.open(`app/asset/create-asset-sale.html?id=${id}`, '_blank', 'height=760, width=800, scrollbars=yes, status=yes');
    else
      window.open(`app/asset/create-asset.html?id=${id}&type=${type}`, '_blank', 'height=760, width=800, scrollbars=yes, status=yes');
  }

  render() {
      let {handleChange, search, handleClick}=this;
      let {assetCategory,name,code,department,status}=this.state.searchSet;
      let {isSearchClicked,list, departments}=this.state;

      const renderOption = function(list, statusBool) {
          if(list) {
              if (list.length) {
                if(statusBool) {
                  return list.map((item, ind) => {
                    return (<option key={ind} value={item.code}>
                            {item.code}
                      </option>)
                  })
                };

                list.sort(function(item1, item2) {
                  if(item1.name && item2.name)
                    return item1.name.toLowerCase() > item2.name.toLowerCase() ? 1 : item1.name.toLowerCase() < item2.name.toLowerCase() ? -1 : 0;
                  else
                    return 0;
                });

                return list.map((item)=> {
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

      const showTable = function() {
        if(isSearchClicked)
        {
            return (
              <table id="agreementTable" className="table table-bordered">
                  <thead>
                  <tr>
                      <th>Sr. No.</th>
                      <th>Code</th>
                      <th>Name</th>
                      <th>Asset Category Type</th>
                      <th>Department</th>
                      <th>Status</th>
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

    const renderBody = function() {
      if (list.length>0) {
        return list.map((item,index)=>
        {
              return (<tr key={index} onClick={() => {handleClick(getUrlVars()["type"], item.id, item.status)}}>
                        <td>{index+1}</td>
                        <td>{item.code}</td>
                        <td>{item.name}</td>
                        <td>{item.assetCategory.name}</td>
                        <td>{getNameById(departments,item.department.id)}</td>
                        <td>{item.status}</td>
                  </tr>  );
        })
      }
    }

    return (
      <div>
      <h3>{getUrlVars()["type"]==="update"?"Modify":titleCase(getUrlVars()["type"])} Asset</h3>
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
                          <label for="assetCategory">Asset Category </label>
                        </div>
                      <div className="col-sm-6">
                         <div className="styled-select">
                         <select id="assetCategory" name="assetCategory" value={assetCategory} onChange={(e)=>{
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
                        <option value="">Select Department</option>
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
                            <option value="">Select Status</option>
                            {renderOption(this.state.statusList, true)}
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
