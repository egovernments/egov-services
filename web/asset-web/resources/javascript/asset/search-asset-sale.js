var flag = 0;
class SearchAsset extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      searchSet: {
        name: "",
        code: "",
        assetCategory: "",
        status: "CAPITALIZED",
        location: ""
      },
      assetCategories: [],
      locality: [],
      isSearchClicked: false,
      modify: false,
      action: "",
      assetList: []
    }

    this.search = this.search.bind(this);
    this.handleChange = this.handleChange.bind(this);
    this.close = this.close.bind(this);
    this.setInitialState = this.setInitialState.bind(this);
    this.handleAction = this.handleAction.bind(this);
    this.setAssetList = this.setAssetList.bind(this);
  }

  handleAction(e, id) {
    e.preventDefault();
    e.stopPropagation();
    window.open(`app/asset/create-asset-sale.html?id=${id}` + (getUrlVars()["type"] ? "&type=" + getUrlVars()["type"] : ""), '_blank', 'height=760, width=800, scrollbars=yes, status=yes');

    this.setState({
      action: ""
    });
  }

  setInitialState(initState) {
    this.setState(initState);
  }

  handleChange(e, name) {
      this.setState({
          searchSet:{
              ...this.state.searchSet,
              [name]:e.target.value
          }
      })
  }

  setAssetList(list) {
    flag = 1;
    var _this = this;
    _this.setState({
      isSearchClicked: true,
      assetList: list,
      modify: true
    });

    setTimeout(function(){
      _this.setState({
        modify: false
      });
    }, 1200);
  }

  search(e) {
    e.preventDefault();
    try {
      //call api call
      var _this = this, tempInfo = Object.assign({}, this.state.searchSet);

      if(tempInfo.location) {
        for(var i=0; i< _this.state.locality.length; i++) {
          if(_this.state.locality[i].name == tempInfo.location) {
            tempInfo.locality = _this.state.locality[i].id;
            delete tempInfo.location;
            break;
          }
        }
      }

      if(getUrlVars()["type"] == "view") {
        delete tempInfo.status;
      }

      commonApiPost("asset-services","assets","_search", {...tempInfo, tenantId, pageSize:500, applicableForSaleOrDisposal:true}, function(err, res) {
        if(res) {
          var assetList = res["Assets"];
          if(getUrlVars()["type"] == "view" && assetList.length) {
            var assetIds = "";
            for(var i=0; i<assetList.length; i++) {
              assetIds += (i == 0) ? assetList[i].id : "," + assetList[i].id;
            }

            commonApiPost("asset-services", "assets/dispose", "_search", {assetId: assetIds, tenantId, pageSize:500}, function(err, res2) {
              if(res2) {
                if(res2.Disposals && res2.Disposals.length) {
                  var newArray = [];
                  for(var i=0; i<res2.Disposals.length; i++) {
                    for(var j=0; j<assetList.length; j++) {
                      if(assetList[j].id == res2.Disposals[i].assetId) {
                        newArray.push(assetList[j]);
                        break;
                      }
                    }
                  }
                  _this.setAssetList(newArray);

                } else {
                  _this.setAssetList([]);
                }
              }
            })
          } else {
            assetList.sort(function(item1, item2) {
              return item1.code.toLowerCase() > item2.code.toLowerCase() ? 1 : item1.code.toLowerCase() < item2.code.toLowerCase() ? -1 : 0;
            })
            _this.setAssetList(assetList);
          }
        } else {
          _this.setAssetList([]);
        }
      })
    } catch(e) {
      console.log(e);
    }
  }

  componentWillUpdate() {
    if(flag == 1) {
      flag = 0;
      $('#assetTable').dataTable().fnDestroy();
    }
  }

  componentDidUpdate(prevProps, prevState) {
      if (this.state.modify) {
          $('#assetTable').DataTable({
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


    var count = 1, _this = this, _state = {};
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

    getDropdown("locality", function(res) {
      checkCountNCall("locality", res);
      var result = res.map(function(a) {return a.name;});
       $( "#location" ).autocomplete({
         source: result,
         minLength: 3,
         change: function( event, ui ) {
           if(ui && ui.item && ui.item.value) {
               _this.setState({
                   searchSet:{
                       ..._this.state.searchSet,
                       location: ui.item.value
                   }
               })
             }
         }
       });
    });
  }

  close() {
    open(location, '_self').close();
  }

  componentWillMount() {
    var type= getUrlVars()["type"];
    if(type==="view"){
      this.setState({
        ...this.state,
      searchSet:{
        ...this.state.searchSet,
        status:null
      }

      });
    }
  }

  render() {

      let {handleChange, search, handleClick, handleAction} = this;
      let {assetCategory, name, code, location} = this.state.searchSet;
      let {isSearchClicked, locality, assetCategories, action, assetList} = this.state;

      const renderOption = function(list) {
          if(list) {
              if (list.length) {
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
              <table id="assetTable" className="table table-bordered">
                  <thead>
                  <tr>
                      <th>Sr. No.</th>
                      <th>Asset Code</th>
                      <th>Asset Name</th>
                      <th>Asset Category</th>
                      <th>Location</th>
                      <th>Current Value Of Asset</th>
                  </tr>
                  </thead>
                  <tbody id="assetSearchResultTableBody">
                          {
                              renderBody()
                          }
                  </tbody>

             </table>
            )
        }
    }

    const renderBody = function() {
      if (assetList.length>0) {
        return assetList.map((item, index)=>
        {
          return (<tr key={index} onClick={(e) => {handleAction(e, item.id)}}>
                    <td>{index+1}</td>
                    <td>{item.code}</td>
                    <td>{item.name}</td>
                    <td>{item.assetCategory.name}</td>
                    <td>{item.locationDetails.locality ? getNameById(locality, item.locationDetails.locality) : ""}</td>
                    <td>{item.currentValue === null ? (item.grossValue === null ? 0 : item.grossValue) : item.currentValue}</td>
              </tr>  );
        })
      }
    }

    const showStatu = function () {
      var type = getUrlVars()["type"];
      if (type != "view") {
        return (

      <div className="row">
          <div className="col-sm-6">
            <div className="row">
              <div className="col-sm-6 label-text">
                <label for="status">Status  </label>
              </div>
              <div className="col-sm-6 label-view-text">
                CAPITALIZED
              </div>
          </div>
        </div>
    </div>
      )
    }

  }

    return (
      <div>
      <h3>Asset Sale/Disposal Search</h3>
      <div className="form-section-inner">
        <form onSubmit={(e)=>{search(e)}}>
        <div className="row">
          <div className="col-sm-6">
            <div className="row">
              <div className="col-sm-6 label-text">
                <label for="name">  Asset Name </label>
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
                    <label for="code">  Asset Code </label>
                  </div>
                  <div className="col-sm-6">
                    <input id="code" name="code" value={code} type="text"
                      onChange={(e)=>{handleChange(e,"code")}}/>
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
                      <label for="location">Location</label>
                  </div>
                  <div className="col-sm-6">
                    <input id="location" name="location" value={location} type="text"
                        onChange={(e)=>{handleChange(e, "location")}}/>
                </div>
              </div>
            </div>
          </div>

          {showStatu()}

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
