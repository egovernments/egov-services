class DueNotice extends React.Component {
  constructor(props){
    super(props);
    this.state={
      searchSet:{
        tenantId,
        agreementNumber:null,
        assetCategory:null,
        revenueWard:null,
        locality:null
      }
    };
    this.handleChange=this.handleChange.bind(this);
    this.search=this.search.bind(this);
    this.closeWindow=this.closeWindow.bind(this);
    this.handleSelectChange = this.handleSelectChange.bind(this);
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

  search(e){
    let searchSet = this.state.searchSet;
    let _this=this;
    if(!_this.state.searchSet.assetCategory){
      showError("Asset category is required.")
      return false;
    }
    let response = commonApiPost("lams-services/agreement","notice","_duenotice",searchSet).responseJSON["Defaulters"]
    _this.setState({
      searchClicked:true,
      error:{},
      resultSet:response
    });
  }

  handleSelectChange(type,agreement){
    switch(type){
      case "dueNotice":
        let agreementNumber = agreement.agreementNumber;
        let noticeType = "DUE";
        let notice = commonApiPost("lams-services/agreement","notice/duenotice","_search",{tenantId,agreementNumber,noticeType}).responseJSON.DueNotices[0];
        if(notice && notice.fileStore){
          this.showNotice(notice.fileStore);
        }else{
          this.printNotice(agreement);
        }
    }
  }

  showNotice(fileStoreId){
      let self = this;
      var fileURL = '/filestore/v1/files/id?fileStoreId='+fileStoreId+'&tenantId='+tenantId;
      var oReq = new XMLHttpRequest();
      oReq.open("GET", fileURL, true);
      oReq.responseType = "arraybuffer";
      //console.log("fileURL",fileURL);
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


  printNotice(agreement){
    var locality = this.state.locality;
    var referenceNumber;
    var assetName = 'N/A';
    var lastPaid;
    var doc = new jsPDF();
    var date = moment(new Date()).format("DD/MM/YYYY");
    var ulbType = "MUNICIPALITY/MUNICIPAL CORPORATION";
    var cityGrade = !localStorage.getItem("city_grade") || localStorage.getItem("city_grade") == "undefined" ? (localStorage.setItem("city_grade", JSON.stringify(commonApiPost("tenant", "v1/tenant", "_search", { code: tenantId }).responseJSON["tenant"][0]["city"]["ulbGrade"] || {})), JSON.parse(localStorage.getItem("city_grade"))) : JSON.parse(localStorage.getItem("city_grade"));

    var autoTableOptions = {
      tableLineColor: [0, 0, 0],
      tableLineWidth: 0.2,
      styles: {
          lineColor: [0, 0, 0],
          lineWidth: 0.2,
      },
      headerStyles: {
          textColor: [0, 0, 0],
          fillColor: [255, 255, 255],
          overflow: 'linebreak',
          columnWidth: 'wrap'
      },
      bodyStyles: {
          fillColor: [255, 255, 255],
          textColor: [0, 0, 0],
          overflow: 'linebreak',
          columnWidth: 'wrap'
      },
      alternateRowStyles: {
          fillColor: [255, 255, 255]
      }, startY: 135
  };


  var columns = [
      { title: "Due Month-Year", dataKey: "dueYear" },
      { title: "Due Date", dataKey: "dueDate" },
      { title: "Demand/Rent", dataKey: "rent" },
      { title: "SGST", dataKey: "sgst" },
      { title: "CGST", dataKey: "cgst" },
      { title: "Penalty/Interest if any on rent", dataKey: "penalty" },
      { title: "Total", dataKey: "total" },
  ];

  var rows = [
      { "dueYear": agreement.installmentFromDate,
       "dueDate": agreement.installmentToDate,
       "rent":agreement.rent,
       "sgst":agreement.sgst,
       "cgst":agreement.cgst,
       "penalty":agreement.penalty,
       "total":agreement.totalBalance
      },
  ];

    if (cityGrade.toLowerCase() === 'corp') {
        ulbType = "MUNICIPAL CORPORATION";
    }
    var LocalityData = commonApiPost("egov-location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", { boundaryTypeName: "LOCALITY", hierarchyTypeName: "LOCATION", tenantId});
    var locality = getNameById(locality, agreement.locality);

    if(agreement.shopNumber){
      referenceNumber = agreement.shopNumber;
    }else{
      referenceNumber = "N/A";
    }
    if(agreement.assetName){
      assetName = agreement.assetName;
    }
    if(agreement.lastPaid){
      lastPaid = agreement.lastPaid;
    }else{
      lastPaid = agreement.commencementDate;
    }

    doc.setFontType("bold");
    doc.setFontSize(13)
    doc.text(105, 30, "OFFICE OF THE COMMISSIONER, " + tenantId.split(".")[1].toUpperCase(), 'center');
    doc.text(105, 37, ulbType.toUpperCase(), 'center');
    doc.text(105, 44, "NOTICE", 'center');

    doc.setFontType("normal");
    doc.setFontSize(11);
    doc.text(20,60, 'Roc.No.');
    doc.setFontType("bold");
    doc.text(35,60, noticeNumber? agreement.noticeNumber : "");
    doc.setFontType("normal");
    doc.text(165,60, 'Dt. ');
    doc.setFontType("bold");
    doc.text(171,60,noticeDate ? noticeDate : date);

    doc.fromHTML("Sub:Leases-Revenu Section-Shop No <b>" + referenceNumber + " </b> in <b>" + assetName + "</b> Complex, <b> <br>"+
    locality + "</b>- Notice for dues - Reg.",20,70);


    doc.fromHTML("Ref: 1. Lease agreement No <b>" + agreementNumber +"</b> dt <b>" + commencementDate +"</b>",20,90);
    doc.fromHTML("2. Roc No........................................dt.......................of this office",29,95);
    doc.fromHTML("3. Resolution No.................................dt.......................of Municipal Council/Standing Committee ",29,100);

    doc.text(100, 120, "><><><", 'center');

    doc.fromHTML("As per the reference 1st cited, rentals for Shop No <b>" + referenceNumber + "</b> in the <b>" + assetName + "</b>",40,130)
    doc.fromHTML(" Shopping Complex  are to be paid by 5th of succeeding month."+
    "But it is observed that rental payments <br> are pending for the said lease since <b>" +lastPaid  + "</b>",20,132);

    doc.autoTable(columns, rows, autoTableOptions);

    doc.fromHTML("You are hereby instructed to pay <b>" + totalBalance + " /- </b> within 7 days of receipt of this notice failing which" ,40,150)
    doc.fromHTML("exiting lease for the said shop will be cancelled without any further correspondence,",20,185)

    doc.setFontType("normal");
    doc.setFontSize(11)
    doc.text(165,217, "Commissioner",'center');
    doc.text(165,222, tenantId.split(".")[1].toUpperCase(),'center');
    doc.text(163,227,"(Municipality/Municipal Corporation)",'center')

    doc.setFontType("normal");
    doc.setFontSize(11)
    doc.text(22,237, "To");
    doc.text(22,242, "The Leaseholder");
    doc.text(22,247,"Copy to the concerned officials for necessary action");

    var blob = doc.output('blob');

    this.createFileStore(agreement, blob).then(this.createNotice, this.errorHandler);
  }

  errorHandler(statusCode){
    console.log("failed with status", status);
    showError('Error');
   }

   createFileStore(noticeData, blob){
     var promiseObj = new Promise(function(resolve, reject){
       let formData = new FormData();
       let fileName = "DN/"+noticeData.agreementNumber;
       formData.append("module", "LAMS");
       formData.append("file", blob,fileName);
       $.ajax({
           url: baseUrl + "/filestore/v1/files?tenantId=" + tenantId,
           data: formData,
           cache: false,
           contentType: false,
           processData: false,
           type: 'POST',
           success: function (res) {
               let obj={
                 noticeData : noticeData,
                 fileStoreId : res.files[0].fileStoreId
               }
               resolve(obj);
           },
           error: function (jqXHR, exception) {
               reject(jqXHR.status);
           }
       });
     });
     return promiseObj;
   }

   createNotice(obj){
     var CONST_API_GET_FILE = "/filestore/v1/files/id?tenantId=" + tenantId + "&fileStoreId=";
     var filestore = obj.fileStoreId;
     $.ajax({
         url: baseUrl + `/lams-services/agreement/notice/duenotice/_create?tenantId=` + tenantId,
         type: 'POST',
         dataType: 'json',
         data: JSON.stringify({
             RequestInfo: requestInfo,
             DueNotice: {
                 tenantId,
                 agreementNumber: obj.noticeData.agreementNumber,
                 fileStore:obj.fileStoreId
             }
         }),
         headers: {
             'auth-token': authToken
         },
         contentType: 'application/json',
         success:function(res){
           if(window.opener)
               window.open(window.location.origin+ CONST_API_GET_FILE+filestore, '_self');
         },
         error:function(jqXHR, exception){
           console.log('error');
           showError('Error while creating notice');
         }
     });

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
                  <td>{getName(assetCategories,item.assetCategory)}</td>
                  <td>{item.assetCode}</td>
                  <td>{item.assetName ? item.assetName : "N/A"}</td>
                  <td>{getName(locality,item.locality)}</td>
                  <td>{item.electionWard? item.electionWard : "N/A"}</td>
                  <td>
                      <select onChange={(e)=>this.handleSelectChange(e.target.value,item)}>
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
                            <input type="text" id="assetCode" onChange={(e) => { handleChange(e.target.value, "assetCode") }}/>
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
        <div id="myModal" ref="modal" className="modal fade" role="dialog">
          <div className="modal-dialog">

            <div className="modal-content">
              <div className="modal-header">
                <button type="button" className="close" data-dismiss="modal">&times;</button>
                <h4 className="modal-title">Notice</h4>
              </div>
              <div className="modal-body">
                <iframe title="Document" src={this.state.iframe_src} frameBorder="0" allowFullScreen height="500" width="100%"></iframe>
              </div>
              <div className="modal-footer">
                <button type="button" className="btn btn-default" data-dismiss="modal">Close</button>
              </div>
            </div>

          </div>
        </div>
      </div>
    )
  }
}

ReactDOM.render(
    <DueNotice />,
    document.getElementById('root')
);
