class DueNotice extends React.Component {
  constructor(props){
    super(props);
    this.state={
      searchSet:{
        tenantId,
        agreementNumber:null,
        assetCategory:null,
        revenueWard:null,
        zone:null,
        fromDate:"Select Date",
        toDate:"Select Date"
      }
    };
    this.handleChange=this.handleChange.bind(this);
    this.search=this.search.bind(this);
    this.closeWindow=this.closeWindow.bind(this);
    this.showTable=this.showTable.bind(this);
  }
  componentDidMount(){

    let {handleChange} = this;
    this.setState({
      locality:commonApiPost("egov-location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", { boundaryTypeName: "LOCALITY", hierarchyTypeName: "LOCATION", tenantId }).responseJSON["Boundary"],
      revenueWard:commonApiPost("egov-location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", { boundaryTypeName: "WARD", hierarchyTypeName: "REVENUE", tenantId }).responseJSON["Boundary"],
      revenueBlock:commonApiPost("egov-location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", { boundaryTypeName: "BLOCK", hierarchyTypeName: "REVENUE", tenantId }).responseJSON["Boundary"],
      assetCategories:commonApiPost("asset-services","assetCategories","_search",{usedForLease:true,tenantId}).responseJSON["AssetCategory"]
      });
  }
  handleChange(value, property){
    this.setState({
      searchClicked : false,
      resultSet :[],
      searchSet:Object.assign(this.state.searchSet || {},{[property]:value}),
    })
  }
  handleSelectChange(type){
    console.log("type",type);
    var notice ={};
    var status = "active";
    var number = "LA-18-1016000613";
    var status = "ACTIVE";
    var id = 1324;
    var agreement = {};
    switch(type){
      // case "View":
      //   console.log("View")
      //   window.open("app/search-agreement/view-agreement-details.html?&" + (number ? "&agreementNumber=" + number : "&acknowledgementNumber=" + acknowledgementNumber) + (status ? "&status=" + status : "") + "&assetId=" + id, "fs", "fullscreen=yes");
      // break
      case "dueNotice":
        console.log("dueNotice");
        var fileStoreId = commonApiPost()
        var agreementNumber = number;
        var noticeType = "CREATE";
        var notice = commonApiPost("lams-services/agreement", "notice", "_search", { tenantId,agreementNumber,noticeType }).responseJSON.Notices[0];
        console.log("notice from case",notice);
        if(notice && notice.fileStore){
          console.log("fileStore id exist")
          this.showNotice("4a66c97d-86f7-400a-aa6d-568113e214b3");
        }else{
          console.log("No data")
        }
      break
    }
  }
  search(e){
    let {searchSet} = this.state;
    let {agreementNumber} = searchSet
    let _this=this;
    let noticeType = "ACTIVE";
    if(!_this.state.searchSet.assetCategory){
      showError("Asset category is required.")
      return false;
    }
    //console.log("searchset",searchSet);
    // Promise.all([
    // //   commonApiPost("lams-services/agreement","notice","_duenotice",{tenantId,agreementNumber})
    //     commonApiPost("lams-services","agreements","_search",{assetCategory:2012,tenantId}).responseJSON["Agreements"]
    // ]).then(function(response){
      //let response = commonApiPost("lams-services","agreements","_search",{assetCategory:2012,tenantId}).responseJSON["Agreements"]
      let response = commonApiPost("lams-services/agreement","notice","_duenotice",{tenantId,agreementNumber}).responseJSON["Defaulters"]
      _this.setState({
        searchClicked:true,
        error:{},
        resultSet:response
      });
    //})
  }

  showNotice(fileStoreId){
      let self = this;
      var fileURL = '/filestore/v1/files/id?fileStoreId='+fileStoreId+'&tenantId='+tenantId;
      var oReq = new XMLHttpRequest();
      oReq.open("GET", fileURL, true);
      oReq.responseType = "arraybuffer";
      console.log("fileURL",fileURL);
      oReq.onload = function(oEvent) {
        var blob = new Blob([oReq.response], {type: oReq.getResponseHeader('content-type')});
        var url = URL.createObjectURL(blob);
        self.setState({
          iframe_src : url,
          contentType: oReq.getResponseHeader('content-type')
        });
        $(self.refs.modal).modal('show')
      };
      oReq.send();
    }

  printNotice(){

    var doc = new jsPDF();

    var noticeNumber = "1016/AN/000202/2018-19";
    var agreementNumber = "1016/AN/000202/2018-19";
    var ShopNo = 2345;
    var agreementDate ="22/05/2018";
    var tenantId = "kurnool";
    var ulbType = "MUNICIPALITY/MUNICIPAL CORPORATION";
    var referenceNumber = "22245";
    var assetName = "RMT Shoping Complex";
    var shopName = "RMT Complex";
    var locality = "Sampath Nagar";
    var referenceNumber = "7878";
    var lastYear = 2018;
    var total = 3000;

    doc.setFontType("bold");
    doc.setFontSize(13)
    doc.text(105, 30, "OFFICE OF THE COMMISSIONER, " + tenantId.toUpperCase(), 'center');
    doc.text(105, 37, ulbType.toUpperCase(), 'center');
    doc.text(105, 44, "NOTICE", 'center');

    doc.setFontType("normal");
    doc.setFontSize(11);
    doc.text(20,60, 'Roc.No.');
    doc.setFontType("bold");
    doc.text(35,60, noticeNumber);
    doc.setFontType("normal");
    doc.text(165,60, 'Dt. ');
    doc.setFontType("bold");
    doc.text(171,60, agreementDate);

    doc.fromHTML("Sub:Leases-Revenu Section-Shop No <b>" + referenceNumber + " </b> in <b>" + assetName + "</b> Complex, <b> <br>"+
    locality + "</b> , - Notice for dues - Reg.",20,70);  
  

    doc.fromHTML("Ref: 1. Lease agreement No <b>" + agreementNumber +"</b> dt <b>" + agreementDate +"</b>",20,90);
    doc.fromHTML("2. Roc No........................................dt.......................of this office",29,95);
    doc.fromHTML("3. Roc No........................................dt.......................of Municipal Council/Standing Committee ",29,100);

    doc.text(100, 120, "><><><", 'center');

    doc.fromHTML("As per the reference 1st cited, rentals for Shop No <b>" +ShopNo + "</b> in the <b>" + shopName + "</b>",40,130)
    doc.fromHTML(" Shopping Complex  are to be paid by 5th of succeeding month."+
    "But it is observed that rental payments <br> are pending for the said lease since <b>" + lastYear + "</b>",20,132);

    doc.fromHTML("You are hereby instructed to pay " + total+ " within 7 days of receipt of this" ,40,150)
    doc.fromHTML("notice failing which exiting lease for the<br> said shop will be cancelled without any further correspondence,",20,152)

    doc.setFontType("normal");
    doc.text(165,187, "Commissioner",'center');
    doc.text(165,192, tenantId.toUpperCase(),'center');
    doc.text(163,197,"(Municipality/Municipal Corporation)",'center')

    doc.text(22,207, "To");
    doc.text(22,212, "The Leaseholder");
    doc.text(22,217,"Copy to the concerned officials for necessary action");

    doc.save("notice.pdf");
  }

  closeWindow ()
  {
      open(location, '_self').close();
  }
  getName(list, val){
    if(val){
      let filteredObj = list.find(obj=>{return obj.id == val});
      return filteredObj.name || '';
    }
  }
  showTable(){
    let {resultSet, assetCategories, locality, electionWard, revenueWard} = this.state;
    let {getName} = this;
    return(
      <div className="form-section" >
        <h3 className="pull-left">Search Result</h3>
        <div className="clearfix"></div>
        <div className="view-table">
        <table className="table table-bordered">
          <thead>
            <tr>
              <th>S.no</th>
              <th>Agreement No</th>
              <th>Asset Category</th>
              <th>Asset Code</th>
              <th>Asset Name</th>
              <th>Locality</th>
              <th>Election Ward</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            {resultSet && resultSet.map((item, index)=>{
              return (
                <tr >
                  <td>{index+1}</td>
                  <td>{item.agreementNumber}</td>
                  <td>{item.assetCategory}</td>
                  <td>{item.assetCode}</td>
                  <td>{item.assetName ? item.assetName : "N/A"}</td>
                  <td>{item.locality}</td>
                  <td>{item.electionWard? item.electionWard : "N/A"}</td>
                  <td>
                      <select onChange={(e)=>this.handleSelectChange(e.target.value,item.agreementNumber,status="ACTIVE")}>
                        <option value="">Select Action</option>
                        <option value="dueNotice">Due Notice</option>
                      </select>
                  </td>
                </tr>
              )
            })}
          </tbody>
        </table>
        </div>
      </div>
    )
  }

  componentWillUpdate(){
    $('table').DataTable().destroy();
  }
  componentDidUpdate(prevProps, prevState)
  {
    $('#fromDate, #toDate').datepicker({
      format: 'dd/mm/yyyy',
      autoclose: true
    });
    let {searchClicked} = this.state;
    if(searchClicked){
      $('table').DataTable({
        dom: 'Bfrtip',
        buttons: [
                 'excel',
                 {
                    extend: 'pdf',
                    filename: 'Due Notice Report',
                    title: `Report generated on ${moment(new Date()).format("DD/MM/YYYY")}`
                  },
                 'print'
         ],
         ordering: false
      });
    }
  }
  componentWillUnmount(){

  }
  render(){
    console.log(this.state.searchSet);
    let {assetCategories, locality, electionWard, revenueWard, revenueZone, revenueBlock, searchClicked} = this.state;
    let {handleChange, search,closeWindow, showTable} = this;
    const renderOptions = function(list)
    {
        if(list)
          {
            if (list.length) {
              return list.map((item)=>
              {
                  return (<option key={item.id} value={typeof item == "object" ? item.id : item}>
                    {typeof item == "object" ? item.name : item}
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

    return(
      <div>
        <div className="form-section">
          <h3 className="pull-left">Search Criteria</h3>
          <div className="clearfix"></div>
          <form>
            <fieldset>
              <div className="row">
                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                              <label for="" >Asset Category <span>*</span></label>
                          </div>
                          <div className="col-sm-6">
                              <div className="styled-select">
                                  <select id="assetCategory" required="true" onChange={(e) => { handleChange(e.target.value, "assetCategory")}}>
                                    <option value="">Select Asset Category</option>
                                    {renderOptions(assetCategories)}
                                  </select>
                              </div>
                          </div>
                      </div>
                  </div>
                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                              <label for="">Asset Code</label>
                          </div>
                          <div className="col-sm-6">
                            <input type="text" id="assetNo" onChange={(e) => { handleChange(e.target.value, "assetNo") }}/>
                          </div>
                      </div>
                  </div>
              </div>
              <div className="row">
                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                              <label for="">Agreement Number</label>
                          </div>
                          <div className="col-sm-6">
                            <input type="text" id="agreementNumber" onChange={(e) => { handleChange(e.target.value, "agreementNumber") }}/>
                          </div>
                      </div>
                  </div>
                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                              <label for="">Election Ward</label>
                          </div>
                          <div className="col-sm-6">
                              <div className="styled-select">
                                  <select id="electionWard" onChange={(e) => { handleChange(e.target.value, "electionWard") }}>
                                    <option value="">Select Election Ward</option>
                                    {renderOptions(electionWard)}
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
                              <label for="">Locality</label>
                          </div>
                          <div className="col-sm-6">
                              <div className="styled-select">
                                  <select id="locality" onChange={(e) => { handleChange(e.target.value, "locality") }}>
                                    <option value="">Select Locality</option>
                                    {renderOptions(locality)}
                                  </select>
                              </div>
                          </div>
                      </div>
                  </div>
                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                              <label for="">Revenue Zone</label>
                          </div>
                          <div className="col-sm-6">
                              <div className="styled-select">
                                  <select id="revenueZone" onChange={(e) => { handleChange(e.target.value, "revenueZone") }}>
                                    <option value="">Select Revenue Zone</option>
                                    {renderOptions(revenueZone)}
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
                              <label for="">Revenue Ward</label>
                          </div>
                          <div className="col-sm-6">
                              <div className="styled-select">
                                  <select id="revenueWard" onChange={(e) => { handleChange(e.target.value, "revenueWard") }}>
                                    <option value="">Select Revenue Ward</option>
                                    {renderOptions(revenueWard)}
                                  </select>
                              </div>
                          </div>
                      </div>
                  </div>
                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                              <label for="">Revenue Block</label>
                          </div>
                          <div className="col-sm-6">
                              <div className="styled-select">
                                  <select id="revenueBlock" onChange={(e) => { handleChange(e.target.value, "revenueBlock") }}>
                                    <option value="">Select Revenue Block</option>
                                    {renderOptions(revenueBlock)}
                                  </select>
                              </div>
                          </div>
                      </div>
                  </div>
              </div>
              {/* <div className="row">
                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                              <label for="">From Date</label>
                          </div>
                          <div className="col-sm-6">
                            <div className="text-no-ui">
                              <span><i className="glyphicon glyphicon-calendar"></i></span>
                              <input type="text" id="fromDate" name="fromDate" value="fromDate" value={fromDate}/>
                            </div>
                          </div>
                      </div>
                  </div>
                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                              <label for="">To Date</label>
                          </div>
                          <div className="col-sm-6">
                            <div className="text-no-ui">
                              <span><i className="glyphicon glyphicon-calendar"></i></span>
                              <input type="text" id="toDate" name="fromDate" value={toDate} 
                              />
                            </div>
                          </div>>
                      </div>
                  </div>
              </div> */}
              <div className="text-center">
                <button type="button" className="btn btn-submit" onClick={(e)=>{search(e)}}>Search</button>  &nbsp;&nbsp;
                <button type="button" className="btn btn-close" onClick={(e)=>{this.closeWindow()}}>Close</button>
              </div>
              <div className="row">
                <div className="col-sm-12" style={{padding:"10px",marginBottomg:"5px",marginTop:"5px"}}>
                    <h5 className="text-center" style={{color:"red"}}><b>NOTE:</b> All Dues shall be shown till yesterday. Any payments made today will be updated tomorrow</h5>
                </div>
              </div>
            </fieldset>
          </form>
        </div>
        <div className="table-cont" id="table">
          {searchClicked && showTable()}
        </div>

      </div>
    )
  }
}

ReactDOM.render(
    <DueNotice />,
    document.getElementById('root')
);
