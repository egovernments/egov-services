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
      "location":"",
      "code": "",
      "locality":"",
      "toCapitalizedValue":"",
      "fromCapitalizedValue": "",
      "grossValue":""
   },isSearchClicked:false,assetCategories:[],departments:[],statusList:{},localityList:[],modify: false,result:[]}
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
    var post = 0;
    e.preventDefault();
      //call api call
      var _this = this;
      var locationArray = JSON.parse(localStorage.getItem("locality"));
        for(var i=0;i<locationArray.length;i++){
            if(_this.state.searchSet.location===locationArray[i].name){
              var id = locationArray[i].id;
              //////console.log("id",id);
              _this.state.searchSet.locality = id;

              break;
            }else{
              if(_this.state.searchSet.location===""){
                post=0;
              }else{
                  post=1;
              }
          }

        }


      if(post==0){
        commonApiPost("asset-services","assets","_search", {...this.state.searchSet, tenantId, pageSize:500}, function(err, res) {
          if(res) {
            var list = res["Assets"];
            list.sort(function(item1, item2) {
              return item1.code.toLowerCase() > item2.code.toLowerCase() ? 1 : item1.code.toLowerCase() < item2.code.toLowerCase() ? -1 : 0;
            })
              //////console.log(list);
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
    }else {
      showError("Invalid Location");
      _this.setState({
        list :[],
        modify: true
      });
      setTimeout(function(){
        _this.setState({
          modify: false
        });
      }, 5000);

      $('#agreementTable').dataTable().fnDestroy();
      post=0;
    }
}


  componentWillUpdate() {
    if(flag == 1) {
      flag = 0;
      $('#agreementTable').dataTable().fnDestroy();
    }
  }

  componentDidUpdate(prevProps, prevState) {
    var _this = this;
      if (this.state.modify){
          $('#agreementTable').DataTable({
            dom: 'Bfrtip',
            buttons: [ {
            text: 'Pdf',
                      action: function ( e, dt, node, config ) {
                        ////console.log(e, dt, node, config);
                          var date = new Date();
                          var localDate =  date.toLocaleString();
                          ////console.log(localDate);
                          ////console.log(_this.state);
                          var printData = _this.state.list;
                          var searchResult = _this.state.searchSet;
                          var searchString = "";
                          ////console.log("searchResult",searchResult);
                          var doc = new jsPDF();
                          doc.page=1;

                          ////console.log("Name",printData[0].assetCategory.name);
                          if(searchResult.code!=""){
                            ////console.log("searchResult.code",searchResult.code);
                            searchString = searchString+ " Asset Code - "+ searchResult.code;
                          }

                          if(searchResult.name!=""){
                            searchString = searchString + " Asset Name - "+ searchResult.name;
                          }
                          if(searchResult.status!=""){
                            searchString = searchString + " Asset Status - "+ searchResult.status;
                          }
                          if(searchResult.assetCategory!=""){
                            searchString = searchString + " Asset Category - "+ printData[0].assetCategory.name;
                          }
                          if(searchResult.location!=""){
                            searchString = searchString + " Asset Location - "+ searchResult.location ;
                          }
                          if(searchResult.grossValue!=""){
                            searchString = searchString + " Current value of asset "+ searchResult.grossValue ;
                          }

                          var locationArray = JSON.parse(localStorage.getItem("locality"));
                          ////console.log("locationArray",locationArray);


                          function footer(){
                            doc.text(190,285, 'page ' + doc.page);
                            doc.text(150,290, 'Report generated on ' + localDate);
                            doc.page ++;
                            };

                            //console.log("printData",printData);
                          var rows = [];
                          var columns = ["Sr. No", "Code", "Asset Name","Status","Asset Category", "Location","Current value of asset"];
                          for(var i=0;i<printData.length;i++){
                            for(var j =0;j<locationArray.length;j++){
                            if(printData[i].locationDetails.locality==locationArray[j].id){
                                printData[i].locationDetails.locality = locationArray[j].name;
                                break;
                            }
                          }
                          //console.log(printData[i]);
                                  if(printData[i].grossValue==null){
                                    printData[i].grossValue = " ";
                                  }
                                rows.push([i+1,printData[i].code,printData[i].name,printData[i].status,printData[i].assetCategory.name,printData[i].locationDetails.locality,printData[i].grossValue]);
                          }


                          doc.autoTable(columns, rows, {
                            styles: {overflow: 'linebreak'},
                              margin: {top: 55},
                              columnStyles: {
                                  0: {columnWidth: 10},
                                  1: {columnWidth: 15},
                                  2: {columnWidth: 45},
                                  3: {columnWidth: 30},
                                  4: {columnWidth: 30},
                                  5: {columnWidth: 30},
                                  6: {columnWidth: 25}},
                                theme: 'grid',
                                pageBreak: 'auto', // 'auto', 'avoid' or 'always'
                                tableWidth: 'auto',
                                columnWidth: 2,
                                addPageContent: function(data) {
                                  doc.rect(6, 6, doc.internal.pageSize.width - 12, doc.internal.pageSize.height - 12, 'S');
                                  doc.setFontSize(8);
                                 //print number bottom right
                                  doc.setFontType("bold");
                                  doc.setFontSize(24);
                                    doc.text(105, 20, tenantId.split(".")[1] + ' Municipal Corporation', null, null, 'center');
                                    doc.setFontSize(20);
                                    doc.text(105, 30,' Asset register report', null, null, 'center');
                                    doc.setFontType("normal");
                                    doc.setFontSize(10);

                                    if(searchString.length>90){
                                      var splitTitle = doc.splitTextToSize('List of Assets : '+searchString, 180);
                                      doc.text(15, 45, splitTitle);

                                    }else{
                                        console.log(searchString.length);
                                        doc.text(15, 45, 'List of Assets : '+searchString);
                                    }
                                    doc.setFontSize(8);
                                    doc.setFontType("normal");
                                    footer();
                                }
                            });
                          doc.setFontSize(8);
                          doc.save('Asset-register-report.pdf');
                      }
                  },{
                    text: 'Excel',
                              action: function ( e, dt, node, config ) {
                                console.log("excel");
                                var date = new Date();
                                var localDate =  date.toLocaleString();
                                var printData = _this.state.list;
                                var searchString = "";
                                var searchResult = _this.state.searchSet;
                                var locationArray = JSON.parse(localStorage.getItem("locality"));

                                if(searchResult.code!=""){
                                  ////console.log("searchResult.code",searchResult.code);
                                  searchString = searchString+ " Asset Code - "+ searchResult.code;
                                }

                                if(searchResult.name!=""){
                                  searchString = searchString + " Asset Name - "+ searchResult.name;
                                }
                                if(searchResult.status!=""){
                                  searchString = searchString + " Asset Status - "+ searchResult.status;
                                }
                                if(searchResult.assetCategory!=""){
                                  searchString = searchString + " Asset Category - "+ printData[0].assetCategory.name;
                                }
                                if(searchResult.location!=""){
                                  searchString = searchString + " Asset Location - "+ searchResult.location ;
                                }
                                if(searchResult.grossValue!=""){
                                  searchString = searchString + " Current value of asset "+ searchResult.grossValue ;
                                }

                                var finalString = tenantId.split(".")[1] +" Municipal Corporation";
                                console.log(finalString);
                                var ep1=new ExcelPlus();




                                  ep1.createFile("Success");
                                  var wscols = [
                                    {wch:6},
                                    {wch:7},
                                    {wch:10},
                                    {wch:6},
                                    {wch:7},
                                    {wch:10},
                                    {wch:20}
                                ];
                                ep1['!cols'] = wscols;
                                    ep1.write({ "sheet":"Success", "cell":"D1", "content":finalString });
                                    ep1.write({ "sheet":"Success", "cell":"D2", "content":"Asset register report" });
                                    ep1.write({ "sheet":"Success", "cell":"H1", "content":'Report generated on ' + localDate });
                                    ep1.write({ "sheet":"Success", "cell":"B4", "content":"List of Assets :"+searchString});

                                    ep1.writeCell("A1","Kurnool", {"font-weight": "Bold"});
                                  ep1.writeRow(7, ["Sr. No", "Code", "Asset Name","Status","Asset Category", "Location","Current value of asset"] );
                                  for(var i=0;i<printData.length;i++){
                                    for(var j =0;j<locationArray.length;j++){
                                    if(printData[i].locationDetails.locality==locationArray[j].id){
                                        printData[i].locationDetails.locality = locationArray[j].name;
                                        break;
                                    }
                                  }
                                  //console.log(printData[i]);
                                          if(printData[i].grossValue==null){
                                            printData[i].grossValue = " ";
                                          }
                                        ep1.writeNextRow([i+1,printData[i].code,printData[i].name,printData[i].status,printData[i].assetCategory.name,printData[i].locationDetails.locality,printData[i].grossValue])
                                  }

                                  ep1.saveAs("success.xlsx");
                              }


                    },
                     //
                    //  'copy', 'csv', 'excel', 'print'
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
    $('#hpCitizenTitle').text(titleCase(getUrlVars()["type"]) + " Asset");
    var count = 2, _this = this, _state = {},result=[];
      let self = this;
    var checkCountNCall = function(key, res) {
      count--;
      _state[key] = res;
      if(count == 0)
        _this.setInitialState(_state);
    }



    //////console.log(this.state.localityList);
    // getDropdown("assetCategories", function(res) {
    //   checkCountNCall("assetCategories", res);
    // });

    commonApiPost("asset-services", "assetCategories", "_search", { tenantId,isChildCategory:true}, function(err, res) {
      self.setState({assetCategories:res.AssetCategory})
    });

    getDropdown("locality", function(res) {
      //////console.log("location",res);
       result = res.map(function(a) {return a.name;});
       $( "#location" ).autocomplete({
         source: result,
         minLength: 3,
         change: function( event, ui ) {
           ////console.log("HERE");
           if(ui && ui.item && ui.item.value) {
           //////console.log("ui",ui);
               _this.setState({
                   searchSet:{
                       ..._this.state.searchSet,
                       location: ui.item.value
                   }
               })
             }
         }

       });
      checkCountNCall("localityList", res);
    });

    getDropdown("statusList", function(res) {
      //////console.log("statusList",res);
      checkCountNCall("statusList", res);
    });

    //////console.log(this.state.localityList);
    var location;

      //////console.log("result",result);




  }

  close() {
      open(location, '_self').close();
  }

  handleClick(type, id, status) {
    if(type == "sale" && status != "CAPITALIZED")
      return showError("Asset sale/disposal is only possible for assets with status as 'Capitalized'");
    window.open(`app/asset/create-asset.html?id=${id}&type=view`, '_blank', 'height=760, width=800, scrollbars=yes, status=yes');
  }

  render() {
    // ////console.log(this.state);
    let {handleChange,search,handleClick}=this;
    let {assetCategory,code,description,name,location,status,grossValue,fromCapitalizedValue,toCapitalizedValue}=this.state.searchSet;
    let {isSearchClicked, list, assignments_unitOfMeasurement,localityList}=this.state;

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
              <table id="agreementTable" className="table table-bordered">
                  <thead>
                  <tr>
                      <th>Sr. No.</th>
                      <th>Code</th>
                      <th>Asset Name</th>
                      <th>Status</th>
                      <th>Asset Category</th>
                      <th>Location</th>
                      <th>Current value of asset</th>

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
         ////console.log("list",list);
        return list.map((item,index)=>
        {
              return (<tr key={index} onClick={() => {handleClick(getUrlVars()["type"], item.id, item.status)}}>
                        <td>{index+1}</td>
                        <td>{item.code}</td>
                        <td>{item.name}</td>
                        <td>{item.status}</td>
                        <td>{item.assetCategory.name}</td>
                        <td>{getNameById(localityList,item.locationDetails.locality)}</td>
                        <td>{item.grossValue}</td>


                  </tr>  );
        })
      }
    }

    return (<div>
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
            <div className="row">
                <div className="col-sm-6">
                  <div className="row">
                    <div className="col-sm-6 label-text">
                      <label for="location">Location</label>
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
                            <label for="status">Status  </label>
                    </div>
                    <div className="col-sm-6">
                        <div className="styled-select">
                        <select id="status" name="status" value={status} onChange={(e)=>{
                                handleChange(e,"status")
                            }}>
                              <option value="">Select Status</option>
                              {renderOption(this.state.statusList)}
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
                          <label for="code"> Asset from value </label>
                        </div>
                        <div className="col-sm-6">

                            <input id="fromCapitalizedValue" placeholder="From Value" name="fromCapitalizedValue" value={fromCapitalizedValue} type="text"
                              onChange={(e)=>{handleChange(e,"fromCapitalizedValue")}}/>

                        </div>
                      </div>
                    </div>

                    <div className="col-sm-6">
                      <div className="row">
                        <div className="col-sm-6 label-text">
                          <label for="name">  Asset to value </label>
                        </div>
                        <div className="col-sm-6">
                          <input id="toCapitalizedValue" placeholder="To Value" name="toCapitalizedValue" value={toCapitalizedValue} type="text"
                            onChange={(e)=>{handleChange(e,"toCapitalizedValue")}}/>
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
                        <input id="grossValue" name="grossValue" value={grossValue} type="number"
                          onChange={(e)=>{handleChange(e,"grossValue")}}/>
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
  <SearchAsset />,
  document.getElementById('root')
);
