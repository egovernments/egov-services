var flag = 0;
class SearchDepreciationReport extends React.Component {
  constructor(props) {
    super(props);
    this.state={
      list:[],
      searchSet:{
        "tenantId":tenantId,
        "assetCategory":"",
        "parent":"",
        "assetCategoryType":"",
        "financialYear":""

      },
      isSearchClicked:false,asset_category_type:[],assetCategories:[],departments:[],financialYears:[],result:[],modify: false}
    this.handleChange = this.handleChange.bind(this);
    this.search = this.search.bind(this);
    this.setInitialState = this.setInitialState.bind(this);
    this.reset=this.reset.bind(this);
  }

  handleChange(e, name) {
    var self = this;
      if(name === 'assetCategoryType'){
        commonApiPost("asset-services", "assetCategories", "_search", {assetCategoryType:e.target.value,tenantId,isParentCategory:true}, function(err, res) {
            let parentCategory = res["AssetCategory"].filter((obj)=>{
              return !obj.parent;//false
            })
            self.setState({parentCategories:parentCategory})
        })
      }else if(name === 'parent'){
        commonApiPost("asset-services", "assetCategories", "_search", {parent:e.target.value,tenantId,isChildCategory:true}, function(err, res) {
            // console.log(res["AssetCategory"]);
            let assetCategory = res["AssetCategory"].filter((obj)=>{
              return obj.parent;//false
            })
            self.setState({assetCategories:assetCategory});
        })
      }
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

  reset(){
    let searchSet = {...this.state.searchSet};
    for(let key in searchSet){
      if(key!='tenantId'){
        searchSet[key]=''
      }
    }
    this.setState({
      searchSet
    })
  }

  search(e) {
    e.preventDefault();
    try {
      //call api call
      var _this = this;
      var searchSet = Object.assign({}, this.state.searchSet);
      commonApiPost("asset-services","assets/depreciations","_search", {...this.state.searchSet, tenantId, pageSize:500}, function(err, res) {
        if(res) {
          var list = res["DepreciationReportCriteria"];
          // list.sort(function(item1, item2) {
          //   console.log(item1 , item2);
          //   return item1.code.toLowerCase() > item2.code.toLowerCase() ? 1 : item1.code.toLowerCase() < item2.code.toLowerCase() ? -1 : 0;
          // })
          flag = 1;
          _this.setState({
            isSearchClicked: true,
            list,
            modify: true
          })
        }
      })
    } catch(e) {
      console.log(e);
    }
  }

  componentWillUpdate() {
      if(flag == 1) {
        flag = 0;
        $('#searchDepreciationTable').dataTable().fnDestroy();
      }
    }

    componentDidUpdate(prevProps, prevState) {
        if (this.state.modify) {
            $('#searchDepreciationTable').DataTable({
              dom: 'Bfrtip',
              buttons: [
                       'excel', 'pdf', 'print'
               ],
               "ordering": false,
               "bDestroy": true,
               language: {
                  "emptyTable": "No Records"
               }
            });
        }
    }

    componentDidUpdate(prevProps, prevState) {
        if (this.state.modify) {
            $('#searchDepreciationTable').DataTable({
              dom: 'Bfrtip',
              buttons: [
                       'excel', 'pdf', 'print'
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

    let {handleChange} = this;

    let assetCode = [];
    let assetName = [];
    let uniqueNames = [];

    if(window.opener && window.opener.document) {
      var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
      if(logo_ele && logo_ele[0]) {
        document.getElementsByClassName("homepage_logo")[0].src = window.location.origin + logo_ele[0].getAttribute("src");
      }
    }
    $('#hpCitizenTitle').text(titleCase(getUrlVars()["type"]) + " Asset");
    var count =0 , _this = this, _state = {};
    var checkCountNCall = function(key, res) {
      if(key == 'financialYears'){
          let finYears = res && res.sort(function (left, right) {
              return moment.utc(right.startingDate).diff(moment.utc(left.startingDate))
          });
          _state[key] = finYears;
      }else{
          _state[key] = res;
      }
      if(count == 0)
        _this.setInitialState(_state);
    }

    getDropdown("asset_category_type", function(res) {
      checkCountNCall("asset_category_type", res);
    });

    getDropdown("assignments_department", function(res) {
      checkCountNCall("departments", res);
    });

    getDropdown("financialYears", function(res) {
      checkCountNCall("financialYears", res);
    });
     var location;

     commonApiPost("asset-services", "assets", "_search", { tenantId}, function(err, res) {
       let assets = res.Assets;

       assets.map(asset=>{
         let obj={};
         obj['label']=`${asset.code} - ${asset.name}`;
         obj['value']=`${asset.code}`;
         assetName.push(asset.name);
         assetCode.push(obj);
       });

       $.each(assetName, function(i, el){
           if($.inArray(el, uniqueNames) === -1) uniqueNames.push(el);
       });

     });

      //autocomplete for asset
      $( "#assetCode" ).autocomplete({
        source: assetCode,
        change: function( event, ui ) {
          let e = {target:{value:ui.item && ui.item.value || ''}};
          handleChange(e, event.target.id)
        }
      });

      $( "#assetName" ).autocomplete({
        source: uniqueNames,
        change: function( event, ui ) {
          if(ui.item){
            let e = {target:{value:ui.item && ui.item.value || ''}};
            handleChange(e, event.target.id);
          }
        }
      });
}

close() {
    open(location, '_self').close();
}

  render() {
    let {handleChange, search, handleClick}=this;
    let {assetCategoryType,assetCategory,department,parent,assetName,assetCode,financialYear}=this.state.searchSet;
    let {isSearchClicked,list,departments,assetCategories,financialYears,assetId}=this.state;
    console.log(this.state.searchSet);
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
              <table id="searchDepreciationTable" className="table table-bordered">
                  <thead>
                  <tr>
                      <th>Sr. No.</th>
                      <th>Asset Code</th>
                      <th>Asset Name</th>
                      <th>Asset Category Name</th>
                      <th>Department</th>
                      <th>Asset Category Type</th>
                      <th>Depreciation Rate(%)</th>
                      <th>Gross Value(Rs.)</th>
                      <th>Current Depreciation(Rs.)</th>
                      <th>Value After Depreciation(Rs.)</th>


                  </tr>
                  </thead>
                  <tbody id="depreciationSearchResultTableBody">
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
         ////console.log("list",list);
        return list.map((item,index)=>
        {
                  return (<tr key={index} onClick={() => {handleClick(getUrlVars()["type"], item.id)}}>
                        <td>{index+1}</td>
                        <td><a href={`app/asset/create-asset.html?id=${item.assetId}&type=view`}>{item.assetCode}</a></td>
                        <td>{item.assetName}</td>
                        <td>{item.assetCategoryName}</td>
                        <td>{getNameById(departments,item.department)}</td>
                        <td>{item.assetCategoryType}</td>
                        <td>{item.depreciationRate}</td>
                        <td>{item.grossValue}</td>
                        <td>{item.depreciationValue}</td>
                        <td>{item.valueAfterDepreciation}</td>
                    </tr>  );
        })
      }
    }

    return (<div>
      <h3>{titleCase(getUrlVars()["type"])} Depreciation Report</h3>
      <div className="form-section-inner">
        <form onSubmit={(e)=>{search(e)}}>
          <div className="row">
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label for="assetName"> Asset Name</label>
                  </div>
                  <div className="col-sm-6">
                    <input id="assetName" name="assetName" value={assetName} type="text"
                      onChange={(e)=>{handleChange(e,"assetName")}}/>
                  </div>
                </div>
              </div>
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label for="assetCode"> Asset Code</label>
                  </div>
                  <div className="col-sm-6">
                    <input id="assetCode" name="assetCode" value={assetCode} type="text"
                      onChange={(e)=>{handleChange(e,"assetCode")}}/>
                  </div>
                </div>
            </div>
            <div className="row">
              <div className="col-sm-6">
                <div className="row">
                    <div className="col-sm-6 label-text">
                        <label for="assetCategoryType">Asset Category Type <span> *</span></label>
                    </div>
                    <div className="col-sm-6">
                      <div className="styled-select">
                        <select id="assetCategoryType" name="assetCategoryType" value={assetCategoryType} required= "true" onChange={(e)=>{
                        handleChange(e,"assetCategoryType")}}>
                            <option value="">Select Asset Category Type</option>
                            {renderOption(this.state.asset_category_type)}
                          </select>
                      </div>
                </div>
            </div>
          </div>
          <div className="col-sm-6">
            <div className="row">
                <div className="col-sm-6 label-text">
                  <label for="financialYear">Financial Year <span> *</span></label>
                </div>
                <div className="col-sm-6">
                <div className="styled-select">
                <select id="financialYear" name="financialYear" value={financialYear} required= "true" onChange={(e)=>{
                handleChange(e,"financialYear")}}>
                    <option value="">Select Financial Years</option>
                    {this.state.financialYears.map((year)=>(
                      <option value={year.finYearRange}>{year.finYearRange}</option>
                    ))}
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
                  <label for="parent"> Parent Category  </label>
                </div>
                <div className="col-sm-6">
                  <div className="styled-select">
                    <select  name="parent" value={parent} onChange={(e)=>{
                        handleChange(e,"parent")}}>
                        <option value="">Select Parent Category</option>
                          {renderOption(this.state.parentCategories)}
                    </select>
                  </div>
                </div>
              </div>
            </div>
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
              </div>
              </div>
              <div className="text-center">
                  <button type="submit" className="btn btn-primary">Search</button>&nbsp;&nbsp;
                  <button type="button" className="btn btn-default" onClick={(e)=>{this.reset()}}>Reset</button>&nbsp;&nbsp;
                  <button type="button" className="btn btn-default" onClick={(e)=>{this.close()}}>Close</button>
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
  <SearchDepreciationReport />,
  document.getElementById('root')
);
