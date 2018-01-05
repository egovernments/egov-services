var flag = 0;
class AssetDepreciation extends React.Component {
  constructor(props) {
    super(props);
    this.state={
      searchSet:{
        tenantId,
      },
      error:{

      },
      isSearchClicked:false,
    }
    this.handleChange = this.handleChange.bind(this);
    this.search = this.search.bind(this);
    this.addRemoveAsset=this.addRemoveAsset.bind(this);
    this.createDepreciation=this.createDepreciation.bind(this);
    this.getName=this.getName.bind(this);
    this.setInitialState = this.setInitialState.bind(this);
    this.closeWindow = this.closeWindow.bind(this);
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

  setInitialState(initState) {
    this.setState(initState);
  }

  search(e) {
    e.preventDefault();
    let self = this;
    let searchSet = {...this.state.searchSet};

    if(!searchSet['dateOfDepreciation']){
       this.setState({
         ...this.error,
        error:{
          dateOfDepreciation:'Required'
        }
      });
    }else{
      searchSet['dateOfDepreciation'] = searchSet['dateOfDepreciation'] ? moment(searchSet['dateOfDepreciation'], "DD/MM/YYYY").valueOf() : '';
      searchSet['dateOfDepreciation'] = searchSet['assetCreatedFrom'] ? moment(searchSet['assetCreatedFrom'], "DD/MM/YYYY").valueOf() : '';
      searchSet['dateOfDepreciation'] = searchSet['assetCreatedTo'] ? moment(searchSet['assetCreatedTo'], "DD/MM/YYYY").valueOf() : '';

      commonApiPost("asset-services","assets","_search", searchSet, function(err, res) {
        console.log(res['Assets']);
        self.setState({
          resultSet:res['Assets'],
          error:{},
          isSearchClicked:true
        })
      });
    }
  }

  componentWillUpdate() {
      $('#agreementTable').dataTable().fnDestroy();
  }

  componentDidUpdate(prevProps, prevState) {
    $('#agreementTable').DataTable({
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

  componentDidMount() {

    let {handleChange} = this;
    let self = this;

    if(window.opener && window.opener.document) {
      var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
      if(logo_ele && logo_ele[0]) {
        document.getElementsByClassName("homepage_logo")[0].src = window.location.origin + logo_ele[0].getAttribute("src");
      }
    }

   $('.datePicker').datepicker({
    format: 'dd/mm/yyyy',
    endDate: '+0d',
    autoclose:true,
    defaultDate: ""
  }).on('changeDate', function (e) {
     handleChange(e.target.value, e.target.id)
  });

   let assetCode = [];
   let assetName = [];
   let uniqueNames = [];

   commonApiPost("asset-services", "assetCategories", "_search", { tenantId, assetCategoryType:'IMMOVABLE'}, function(err, res) {
     self.setState({assetCategory:res.AssetCategory})
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

  closeWindow() {
    open(location, '_self  ').close();
  }

  getName(list, val){
     if(val){
      let filteredObj = list.find(obj=>{return obj.id == val});
      return filteredObj.name || '';
    }
  }

  addRemoveAsset(boolean, assetId){
    console.log(boolean, assetId);
    if(boolean){
      //add it to the array
    }else{
      //remove it from the array
    }
  }

  createDepreciation(){

  }

  render() {
      let {handleChange, search, closeWindow, getName, handleClick, addRemoveAsset}=this;
      let {isSearchClicked, assetCategory, department, resultSet}=this.state;
      console.log(this.state);

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
              <div>
              <table id="agreementTable" className="table table-bordered">
                  <thead>
                  <tr>
                      <th><input type="checkbox" className="checkAll"/></th>
                      <th>Sr. No.</th>
                      <th>Asset Category Name</th>
                      <th>Department</th>
                      <th>Asset Code</th>
                      <th>Asset Name</th>
                      <th>Current Gross Value(Rs.)</th>
                      <th>Depreciation Rate(%)</th>
                  </tr>
                  </thead>
                  <tbody id="agreementSearchResultTableBody">
                          {
                              renderBody()
                          }
                      </tbody>

             </table>
             <button type="button" className="btn btn-submit">Create</button>
             </div>
            )
    }

    const renderBody = function() {
      if (resultSet.length>0) {
        return resultSet.map((item,index)=>
        {
              return(
                <tr key={index}>
                        <td><input type="checkbox" className="depreciationCheck" onClick={(e)=>{addRemoveAsset(e.target.checked, item.id)}} /></td>
                        <td>{index+1}</td>
                        <td>{item.assetCategory.name}</td>
                        <td>{getName(department,item.department.id)}</td>
                        <td>{item.code}</td>
                        <td>{item.name}</td>
                        <td>{item.grossValue}</td>
                        <td>{item.assetCategory.depreciationRate}</td>
                </tr>);
        })
      }
    }

    return (
      <div>

      <div className="form-section-inner">
        <form onSubmit={(e)=>{search(e)}}>
          <div className="row">
            <label className="col-sm-3 control-label text-right">Date of Depreciation <span className="error"> *</span></label>
            <div className="col-sm-3 add-margin">
              <input type="text" className="datePicker" name="dateOfDepreciation" id="dateOfDepreciation"
              onChange={(e)=>{handleChange(e.target.value,"dateOfDepreciation")}} />
              <label className="error">{this.state.error.dateOfDepreciation}</label>
            </div>
            <label className="col-sm-2 control-label text-right">Asset Category Type</label>
            <div className="col-sm-3 add-margin">
              <select className="form-control" id="assetCategoryType" onChange={(e)=>{handleChange(e.target.value,"assetCategoryType")}}>
                <option value="">Select</option>
                {renderOptions(assetCategory)}
              </select>
            </div>
          </div>
          <div className="row">
            <label className="col-sm-3 control-label text-right">Asset Category Name</label>
            <div className="col-sm-3 add-margin">
              <select className="form-control" id="assetCategory" name="" onChange={(e)=>{handleChange(e.target.value,"assetCategory")}}>
                <option value="">Select</option>
              </select>
            </div>
            <label className="col-sm-2 control-label text-right">Department</label>
            <div className="col-sm-3 add-margin">
              <select className="form-control" id="department" onChange={(e)=>{handleChange(e.target.value,"department")}}>
                <option value="">Select</option>
                {renderOptions(department)}
              </select>
            </div>
          </div>
          <div className="row">
            <label className="col-sm-3 control-label text-right"> From Date</label>
            <div className="col-sm-3 add-margin">
              <input type="text" className="datePicker" name="assetCreatedFrom" id="assetCreatedFrom"
              onChange={(e)=>{handleChange(e.target.value,"assetCreatedFrom")}} />
            </div>
            <label className="col-sm-2 control-label text-right"> To Date</label>
            <div className="col-sm-3 add-margin">
              <input type="text" className="datePicker" name="assetCreatedTo" id="assetCreatedTo"
              onChange={(e)=>{handleChange(e.target.value,"assetCreatedTo")}}/>
            </div>
          </div>
          <div className="row">
            <label className="col-sm-3 control-label text-right">Asset Code</label>
            <div className="col-sm-3 add-margin">
              <input type="text" className="" name="code" id="code"
              onChange={(e)=>{handleChange(e.target.value,"code")}} />
            </div>
            <label className="col-sm-2 control-label text-right">Asset Name</label>
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
              {isSearchClicked ?
                showTable(): ''
              }
          </div>
          </div>

    );
  }
}


ReactDOM.render(
  <AssetDepreciation />,
  document.getElementById('root')
);
