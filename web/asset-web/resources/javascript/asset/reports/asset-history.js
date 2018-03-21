var flag = 0;
class AssetHistory extends React.Component {
  constructor(props){
    super(props);
    this.state={
      isSearchClicked:false,
      searchSet:{
        tenantId
      }
    };
    this.handleChange = this.handleChange.bind(this);
    this.search = this.search.bind(this);
    this.getName=this.getName.bind(this);
    this.linkHistoryTable=this.linkHistoryTable.bind(this);
    this.showHistoryTable=this.showHistoryTable.bind(this);
    this.renderHistoryBody=this.renderHistoryBody.bind(this);
  }

  componentDidMount() {

    let {handleChange} = this;
    let self = this;

    if(window.opener && window.opener.document) {
      var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
      if(logo_ele && logo_ele[0]) {
        document.getElementsByClassName("homepage_logo")[0].src = window.location.origin + logo_ele[0].getAttribute("src");
      }
    }

   let assetCode = [];
   let assetName = [];
   let uniqueNames = [];

   commonApiPost("asset-services", "GET_ASSET_CATEGORY_TYPE", "", { tenantId}, function(err, res) {
     self.setState({assetCategory:res.AssetCategoryType})
   });

   commonApiPost("asset-services", "assetCategories", "_search", { tenantId,isChildCategory:true}, function(err, res) {
     self.setState({assetCategoryName:res.AssetCategory})
   });

   getDropdown("assignments_department", function(res) {
      self.setState({department:res})
   });

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
    $( "#code" ).autocomplete({
      source: assetCode,
      change: function( event, ui ) {
        handleChange(ui.item.value, event.target.id)
      }
    });

    $( "#name" ).autocomplete({
      source: uniqueNames,
      change: function( event, ui ) {
        if(ui.item){
          handleChange(ui.item.value, event.target.id);
        }
      }
    });

  }

  getName(list, val){
     if(val){
      let filteredObj = list.find(obj=>{return obj.id == val});
      return filteredObj.name || '';
    }
  }

  handleChange(value, property) {
      this.setState({
          isSearchClicked:false,
          searchSet:{
              ...this.state.searchSet,
              [property]:value
          }
      })
  }

  closeWindow() {
    open(location, '_self  ').close();
  }

  search(e) {
    e.preventDefault();
    let self = this;
    let searchSet = {...this.state.searchSet};

    commonApiPost("asset-services","assets","_search", searchSet, function(err, res) {
      // console.log(res['Assets']);
      self.setState({
        resultSet:res['Assets'],
        isSearchClicked:true
      });
    });

  }

  componentDidUpdate(prevProps, prevState) {
    let {isSearchClicked, isAssetDeprecated, showHistory, referredAssetCode}=this.state;
    if(isSearchClicked && flag == 0){
      flag = 1;
      $('#assetTable').DataTable({
        dom: 'Bfrtip',
        buttons: [
                 'excel',
                 {
                    extend: 'pdf',
                    filename : 'Asset',
                    orientation: 'landscape',
                    pageSize: 'TABLOID',
                    footer : true
                  },
                 'print'
         ],
         "ordering": false,
         "bDestroy": true,
         language: {
            "emptyTable": "No Records"
         }
      });
    }
    if(showHistory){
      $('#history').DataTable({
        dom: 'Bfrtip',
        buttons: [
                 'excel',
                 {
                    extend: 'pdf',
                    filename : `Asset Transaction History - ${referredAssetCode}`,
                    title : `Asset Transaction History - ${referredAssetCode}`,
                    orientation: 'landscape',
                    pageSize: 'TABLOID',
                    footer : true
                  },
                 'print'
         ],
         "ordering": false,
         "bDestroy": true,
         language: {
            "emptyTable": "No Records"
         }
      });
    }
  }

  linkHistoryTable(assetCode){
    let self = this;
    commonApiPost("asset-services", "assets", "_search", { tenantId, isTransactionHistoryRequired:true, code:assetCode}, function(err, res) {
      let result = res && res.Assets[0] && res.Assets[0].transactionHistory || []
      self.setState({
        grossValue : res && res.Assets[0] && res.Assets[0].grossValue || 0,
        historyResult:result,
        showHistory : true,
        referredAssetCode:assetCode
      });
    });
  }

  showHistoryTable(){
    return (
      <div>
      <table id="history" className="table table-bordered">
        <thead>
        <tr>
            <th>Sr. No.</th>
            <th>Transaction Date</th>
            <th>Current Gross Value(Rs.)</th>
            <th>Transaction Type</th>
            <th>Transaction Amount(Rs.)</th>
            <th>Gross Value after Transaction(Rs.)</th>
        </tr>
        </thead>
        <tbody>
          {this.renderHistoryBody()}
        </tbody>
      </table>
      <div className="text-center">
          <button type="button" className="btn btn-close" onClick={(e)=>{this.closeWindow()}}>Close</button>
      </div>
      </div>
    )
  }

  renderHistoryBody(){
    let {historyResult, grossValue} = this.state;
    return historyResult && historyResult.map((history,idx)=>{
      return(
        <tr key={idx}>
          <td>{idx+1}</td>
          <td>{moment(history.transactionDate).format('DD/MM/YYYY')}</td>
          <td>{ grossValue}</td>
          <td>{history.transactionType}</td>
          <td>{history.transactionAmount}</td>
          <td>{history.valueAfterTransaction || 0}</td>
        </tr>
      )
    })
  }

  render(){
    let {handleChange, search, closeWindow, getName, linkHistoryTable}=this;
    let {assetCategory, assetCategoryName, department, isSearchClicked, resultSet}=this.state;
    // console.log(this.state.historyResult, this.state.grossValue);
    // console.log(this.state.searchSet);

    const renderOptions = function(list)
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

      const showTable = function() {
            return (
              <table id="assetTable" className="table table-bordered">
                  <thead>
                  <tr>
                      <th>Sr. No.</th>
                      <th>Asset Category Name</th>
                      <th>Department</th>
                      <th>Asset Code</th>
                      <th>Asset Name</th>
                      <th>Current Gross Value(Rs.)</th>
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

    const renderBody = function() {
      if (resultSet.length>0) {
        return resultSet.map((item,index)=>
        {
              return(
                <tr key={index}>
                        <td>{index+1}</td>
                        <td>{item.assetCategory.name}</td>
                        <td>{getName(department,item.department.id)}</td>
                        <td><a href='javascript:;' onClick={(e)=>linkHistoryTable(item.code)}>{item.code}</a></td>
                        <td>{item.name}</td>
                        <td>{item.grossValue || 0}</td>
                </tr>);
        })
      }
    }

    if(this.state.showHistory){
      return(
        <div>
          {this.showHistoryTable()}
        </div>
      )
    }

    return(
      <div>
        <div className="form-section-inner">
          <form onSubmit={(e)=>{search(e)}}>
            <div className="row">
              <label className="col-sm-3 control-label text-right">Asset Category Type</label>
              <div className="col-sm-3 add-margin">
                <select className="form-control" id="assetCategoryType" onChange={(e)=>{handleChange(e.target.value,"assetCategoryType")}}>
                  <option value="">Select</option>
                  {renderOptions(assetCategory)}
                </select>
              </div>
              <label className="col-sm-2 control-label text-right">Asset Category Name</label>
              <div className="col-sm-3 add-margin">
                <select className="form-control" id="assetCategory" name="" onChange={(e)=>{handleChange(e.target.value,"assetCategory")}}>
                  <option value="">Select</option>
                  {renderOptions(assetCategoryName)}
                </select>
              </div>
            </div>
            <div className="row">
              <label className="col-sm-3 control-label text-right">Department</label>
              <div className="col-sm-3 add-margin">
                <select className="form-control" id="department" onChange={(e)=>{handleChange(e.target.value,"department")}}>
                  <option value="">Select</option>
                  {renderOptions(department)}
                </select>
              </div>
              <label className="col-sm-2 control-label text-right">Asset Code</label>
              <div className="col-sm-3 add-margin">
                <input type="text" className="" name="code" id="code"
                onChange={(e)=>{handleChange(e.target.value,"code")}} />
              </div>
            </div>
            <div className="row">
              <label className="col-sm-3 control-label text-right">Asset Name</label>
              <div className="col-sm-3 add-margin">
                <input type="text" className="" name="name" id="name"
                onChange={(e)=>{handleChange(e.target.value,"name")}} />
              </div>
            </div>
            <br/>
                <div className="text-center">
                    <button type="submit" className="btn btn-submit">Search</button>&nbsp;&nbsp;
                    <button type="button" className="btn btn-close" onClick={(e)=>{this.closeWindow()}}>Close</button>
                </div>
          </form>
        </div>
        <div className="table-cont" id="table">
            {isSearchClicked ? showTable(): '' }
        </div>
      </div>
    )
  }
}

ReactDOM.render(
  <AssetHistory />,
  document.getElementById('root')
);
