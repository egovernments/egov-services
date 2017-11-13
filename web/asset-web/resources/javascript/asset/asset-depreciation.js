var flag = 0;
class AssetDepreciation extends React.Component {
  constructor(props) {
    super(props);
    this.state={list:[],searchSet:{
      "tenantId": tenantId,
      "name": "",
      "department": "",
      "assetCategory": "",
      "status": "",
      "code": ""
   },
   isSearchClicked:false,
   assetCategories:[],
   departments:[],
   statusList:{},
   modify: false
 }
    this.handleChange = this.handleChange.bind(this);
    this.search = this.search.bind(this);
    this.handleClick = this.handleClick.bind(this);
    this.setInitialState = this.setInitialState.bind(this);
    this.closeWindow = this.closeWindow.bind(this);
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


    $('#fromDate').datepicker({
     format: 'dd/mm/yyyy',
     autoclose:true,
     defaultDate: ""
 });

 $('#fromDate').on('changeDate', function(e) {
       _this.setState({
             movement: {
                 ..._this.state.movement,
                 "fromDate":$("#fromDate").val(),
             }
       });
 });

 $('#toDate').datepicker({
  format: 'dd/mm/yyyy',
  autoclose:true,
  defaultDate: ""
 });

 $('#toDate').on('changeDate', function(e) {
    _this.setState({
          movement: {
              ..._this.state.movement,
              "toDate":$("#toDate").val(),
          }
    });
 });

  }

  closeWindow() {
    open(location, '_self  ').close();
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
      let {handleChange, search, closeWindow, handleClick}=this;
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

      <div className="form-section-inner">
        <form onSubmit={(e)=>{search(e)}}>

        <div className="row">
          <label className="col-sm-3 control-label text-right"> Financial Year</label>
          <div className="col-sm-3 add-margin">
            <select className="form-control" onChange={(e)=>{handleChange(e.target.value,"financialYear")}}>
              <option value="null">Select</option>
              <option value="2016-17">2016-17</option>
              <option value="2017-18">2017-18</option>
            </select>
          </div>
          <label className="col-sm-2 control-label text-right"> From Date</label>
          <div className="col-sm-3 add-margin">
            <input type="text" className="fromDate" name="fromDate" id="fromDate"
            onChange={(e)=>{handleChange(e.target.value,"fromDate")}} />
          </div>
        </div>
        <div className="row">
          <label className="col-sm-3 control-label text-right"> To Date</label>
          <div className="col-sm-3 add-margin">
            <input type="text" className="toDate" name="toDate  " id="toDate"
            onChange={(e)=>{handleChange(e.target.value,"toDate")}}/>
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
              {showTable()}

          </div>
</div>

    );
  }
}


ReactDOM.render(
  <AssetDepreciation />,
  document.getElementById('root')
);
