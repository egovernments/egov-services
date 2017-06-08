var flag = 0;
class CommonReportComponent extends React.Component {
  constructor(props) {
    super(props);
    this.state={keys:[],list:[]};
  }
  componentWillMount(){
    let list=this.props.ReportResult;
    this.setState({
      list
    });
  }

  componentDidMount(){
    console.log($('#table').length);
    $('#table').DataTable({
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

  componentWillUpdate() {
    if(flag == 1) {
      flag = 0;
      $('#table').dataTable().fnDestroy();
    }
  }

  componentDidUpdate(prevProps, prevState) {
    console.log("hi");
    $('#table').DataTable({
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
      // if (prevState.list.length!=this.state.list.length) {
      //     $('#table').DataTable({
      //       dom: 'Bfrtip',
      //       buttons: [
      //                'copy', 'csv', 'excel', 'pdf', 'print'
      //        ],
      //        ordering: false,
      //        bDestroy: true,
      //        language: {
      //           "emptyTable": "No Records"
      //        }
      //     });
      // }
  }

  render(){
  let {list}=this.state;

  var keys = Object.keys(list[0]);

  const getData=function(item){

    return keys.map((key,index)=>{
        return (<td key={index}>{item[key]}</td>)
    })
  }

    const renderBody=function()
    {
      if (true) {
        return list.map((item,index)=>
        {
              return (<tr key={index}>{
                   getData(item)
                 }
                              </tr>  );
        })
      }
      else {
         return (
             ""
         )
     }
  }

  const  genrateRow =function(keys) {
    return keys.map((key,index)=>
      {
            return (
                      <th key={index}>{key}</th>
                           );
      })
    }
  console.log(keys);
    return(
      <div>
          <table id="table" className="table table-bordered">
              <thead>
              <tr>
                {genrateRow(keys)}
              </tr>
              </thead>
              <tbody id="agreementSearchResultTableBody">
                      {
                          renderBody()
                      }
                  </tbody>
         </table>
      </div>
    )
  }
}


class AssetRegisterReport extends React.Component {
  constructor(props) {
    super(props);
    this.state={list:[
      {
          "code": "2",
          "name":"bharath",
          "location":"bangalore",
          "description":"assets",
          "status":"done",
          "valueOfAsset":"2500",
          "assetCategory": "personal"
        },{
            "code": "3",
            "name":"murali",
            "location":"nagapur",
            "description":"assets 2",
            "status":"done",
            "valueOfAsset":"25000",
            "assetCategory": "offical"
        }
      ],searchSet:{
      "code": "",
      "name":"",
      "location":"",
      "description":"",
      "status":"",
      "valueOfAsset":"",
      "assetCategory": "",
   },isSearchClicked:false,asset_category_type:[], assetCategories: [], assignments_unitOfMeasurement: []}
    this.handleChange=this.handleChange.bind(this);
    this.search=this.search.bind(this);
    this.setInitialState = this.setInitialState.bind(this);
    this.handleClick = this.handleClick.bind(this);
  }

  search(e) {

    e.preventDefault();
    var _this = this;
       flag = 1;
    _this.setState({
      isSearchClicked: true
    // try {
    //   //call api call
    //   commonApiPost("asset-services", "assetCategories", "_search", {...this.state.searchSet ,tenantId, pageSize:500}, function(err, res) {
    //     if(res) {
    //       var list = res["AssetCategory"];
    //       flag = 1;
    //
    //     }
    //   })
    // } catch(e) {
    //
    //
    //   })


  })
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
    $('#hpCitizenTitle').text(titleCase(getUrlVars()["type"]) + "Asset Register Report");

    var count = 3, _this = this, _state = {};
    var checkCountNCall = function(key, res) {
      count--;
      _state[key] = res;
      if(count == 0)
        _this.setInitialState(_state);
    }

    getDropdown("asset_category_type", function(res) {
      checkCountNCall("asset_category_type", res);
    });

    getDropdown("assetCategories", function(res) {
      checkCountNCall("assetCategories", res);
    });

    getDropdown("assignments_unitOfMeasurement", function(res) {
      checkCountNCall("assignments_unitOfMeasurement", res);
    });
  }



  handleClick(type, id) {
    if (type === "update") {
      window.open(`app/asset/create-asset-category.html?id=${id}&type=update`, '_blank', 'location=yes, height=760, width=800, scrollbars=yes, status=yes');
    } else if(type) {
      window.open(`app/asset/create-asset-category.html?id=${id}&type=${type}`, '_blank', 'location=yes, height=760, width=800, scrollbars=yes, status=yes');
    } else {
      window.open(`app/asset/create-asset-category.html?id=${id}&type=view`, '_blank', 'location=yes, height=760, width=800, scrollbars=yes, status=yes');
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
    let {assetCategory,code,description,name,location,status,valueOfAsset}=this.state.searchSet;
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
        console.log("Inside");
          return (
            <CommonReportComponent ReportResult={list} />
          )
      }
  }

    return (
    <div>
      <h3>{titleCase(getUrlVars()["type"])} Search Asset Register Report</h3>
      <div className="form-section-inner">
        <form onSubmit={(e)=>{search(e)}}>
          <div className="row">
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label for="code"> Asset Code</label>
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
                    <label for="name"> Asset Name</label>
                  </div>
                  <div className="col-sm-6">
                    <input id="name" name="name" value={code} type="text"
                      onChange={(e)=>{handleChange(e,"name")}}/>
                  </div>
                </div>

              </div>
            </div>
            <div className="row">
                <div className="col-sm-6">
                  <div className="row">
                    <div className="col-sm-6 label-text">
                      <label for="description">Asset Description</label>
                    </div>
                    <div className="col-sm-6">
                      <input id="description" name="description" value={description} type="text"
                        onChange={(e)=>{handleChange(e,"description")}}/>
                    </div>
                  </div>
                </div>

                <div className="col-sm-6">
                    <div className="row">
                        <div className="col-sm-6 label-text">
                            <label for="assetCategory">Asset Category</label>
                        </div>
                        <div className="col-sm-6">
                            <div className="styled-select">
                            <select id="assetCategory" name="assetCategory" value={assetCategory}  onChange={(e)=>{
                            handleChange(e,"assetCategory")}}>
                                <option value="">Select Asset Category</option>
                                {renderOption(this.state.asset_category_type)}
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
                      <label for="description">Location</label>
                    </div>
                    <div className="col-sm-6">
                      <input id="location" name="location" value={location} type="text"
                        onChange={(e)=>{handleChange(e,"location")}}/>
                    </div>
                  </div>
                </div>

                <div className="col-sm-6">
                    <div className="row">
                        <div className="col-sm-6 label-text">
                            <label for="assetCategory">Status</label>
                        </div>
                        <div className="col-sm-6">
                            <div className="styled-select">
                            <select id="status" name="status" value={status}  onChange={(e)=>{
                            handleChange(e,"status")}}>
                                <option value="">Select Asset Category</option>
                                {renderOption(this.state.asset_category_type)}
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
                        <label for="description">Value of asset</label>
                      </div>
                      <div className="col-sm-6">
                        <input id="valueOfAsset" name="valueOfAsset" value={valueOfAsset} type="number"
                          onChange={(e)=>{handleChange(e,"valueOfAsset")}}/>
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
  <AssetRegisterReport />,
  document.getElementById('root')
);
