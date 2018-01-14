class Notice extends React.Component {
  constructor(props){
    super(props);
    this.state={
      searchSet:{
        tenantId
      },
      noticeType:["CREATE", "RENEWAL", "EVICTION", "CANCELLATION","OBJECTION","JUDGEMENT"]
    };
    this.handleChange=this.handleChange.bind(this);
    this.search=this.search.bind(this);
    this.mergeDownload=this.mergeDownload.bind(this);
    this.zipDownload=this.zipDownload.bind(this);
    this.closeWindow=this.closeWindow.bind(this);
    this.showTable=this.showTable.bind(this);
  }
  componentDidMount(){

    let {handleChange} = this;
    this.setState({
      assetCategories:commonApiPost("asset-services","assetCategories","_search",{tenantId}).responseJSON["AssetCategory"],
      revenueWard:commonApiPost("egov-location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", { boundaryTypeName: "WARD", hierarchyTypeName: "REVENUE", tenantId }).responseJSON["Boundary"],
      });

    let assets = commonApiPost("asset-services", "assets", "_search", { tenantId}).responseJSON.Assets;

    //autocomplete for asset
    let assetCode = [];
    let assetName = [];
    assets.map(asset=>{
      let obj={};
      obj['label']=`${asset.code} - ${asset.name}`;
      obj['value']=`${asset.code}`;
      assetName.push(asset.name);
      assetCode.push(obj);
    });

    $( "#assetCode" ).autocomplete({
      source: assetCode,
      change: function( event, ui ) {
        handleChange(ui.item.value, event.target.id)
      }
    });

    var uniqueNames = [];
    $.each(assetName, function(i, el){
        if($.inArray(el, uniqueNames) === -1) uniqueNames.push(el);
    });

    $( "#assetName" ).autocomplete({
      source: uniqueNames,
      change: function( event, ui ) {
        if(ui.item){
          handleChange(ui.item.value, event.target.id);
        }
      }
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
    let {searchSet} = this.state;
    let _this=this;
    if(!_this.state.searchSet.noticeType){
      showError("Please select notice type.")
      return false;
    }

    // if(!searchSet['revenueWard']){
    //   this.setState({
    //     ...this.error,
    //     error:{
    //       revenueWard:'Required'
    //     }
    //   })
    // }else{
    // lams-service/agreement/notice/_search
      Promise.all([
        commonApiPost("lams-services/agreement","notice","_search",searchSet)
      ]).then(function(responses){
        _this.setState({
          searchClicked:true,
          error:{},
          resultSet:responses[0].Notices
        });
      })
    // }

  }
  mergeDownload(e){
    let {searchClicked, resultSet, searchSet} = this.state;
    let self = this;
    if(searchClicked){
      //resultset not changed in the state
      this.createPDFdocForMerge(resultSet);
    }else{
         if(!searchSet.noticeType){
         showError("Please select notice type.");
         return false;
         }
      //get the latest result from the API
      Promise.all([
        commonApiPost("lams-services/agreement","notice","_search",searchSet)
      ]).then(function(responses){
        // responses[0].Notices
        self.createPDFdocForMerge(responses[0].Notices);
      })
    }
  }
  createPDFdocForMerge(res){
    var docDefinition = {
      pageSize: 'A4',
      pageMargins: [ 50, 30, 50, 30 ],
      content: this.renderPDFContent(res)
    };
    pdfMake.createPdf(docDefinition).download('Notices.pdf');
  }
  renderPDFContent(resultSet){
    let contentArray = [];
    resultSet.map((obj,index)=>{
      let contentHeader = {
           text: `${tenantId.split(".")[1]}`,
           style: 'header',
           alignment: 'center',
           margin: [ 0, 0, 0, 10 ],
           bold: true,
      };
      let content2Header = {
           text: `${tenantId.split(".")[1]} District`,
           style: 'header',
           alignment: 'center',
           margin: [ 0, 0, 0, 10 ],
           bold: true,
      };
      let content3Header = {
           text: `Asset Category Lease/Agreement Notice`,
           style: 'header',
           alignment: 'center',
           margin: [ 0, 0, 0, 0 ],
           bold: true,
      };
      let line = {
          table: {
                  widths: ['*'],
                  body: [[" "], [" "]]
          },
          layout: {
              hLineWidth: function(i, node) {
                  return (i === 0 || i === node.table.body.length) ? 0 : 2;
              },
              vLineWidth: function(i, node) {
                  return 0;
              },
          }
      };
      let first = {
        columns: [
          {
            width: '50%',
            text: `Lease Details :`,
            alignment:'left',
            bold:true,
            margin: [ 0, 0, 0, 10 ]
          },
          {
            width: '50%',
            text: `Agreement No: ${obj.agreementNumber || ''}`,
            alignment:'right',
            bold:true,
            margin: [ 0, 0, 0, 10 ]
          }
        ],
        // optional space between columns
        columnGap: 10
      };
      let second = {
        columns: [
          {
            width: '50%',
            text: `Lease Name : ${obj.allotteeName || ''}`,
            alignment:'left',
            bold:true,
            margin: [ 0, 0, 0, 10 ]
          },
          {
            width: '50%',
            text: `Asset No: ${obj.assetNo || ''}`,
            alignment:'right',
            bold:true,
            margin: [ 0, 0, 0, 10 ]
          }
        ],
        // optional space between columns
        columnGap: 10
      };
      let third = {
        text : `${obj.allotteeMobileNumber || ''}, ${obj.doorNo || ''}, ${obj.allotteeAddress || ''}, ${tenantId.split(".")[1]}`,
        bold:true
      }
      let contentObj = {
        ol: [
            { text: `The period of lease shall be ${obj.agreementPeriod*12} months commencing from ${obj.commencementDate} (dd/mm/yyyy) to ${obj.expiryDate} (dd/mm/yyyy)`, bold: true,  margin: [ 0, 10, 0, 10 ]  },
            { text: `The property leased is shop No ${obj.assetNo} and shall be leased for a sum of Rs. ${obj.rent} per month exclusive of the payment of electricity and other charges.`, bold: true,  margin: [ 0, 0, 0, 10 ]   },
            { text: `The lessee has paid a sum of Rs. ${obj.securityDeposit} as security deposit for the tenancy and the said sum is repayable or adjusted only at the end of the tenancy on the lease delivery vacant possession of the shop let out, subject to deductions, if any, lawfully and legally payable by the lessee under the terms of this lease deed and in law.`, bold: true,  margin: [ 0, 0, 0, 10 ]   },
            { text: `The rent for every month shall be payable on or before succeeding month.`, margin: [ 0, 0, 0, 10 ]},
            { text: `The lessee shall pay electricity charges to the Electricity Board every month without fail.`, margin: [ 0, 0, 0, 10 ]},
            { text: `The lessor or his agent shall have a right to inspect the shop at any hour during the day time.`, margin: [ 0, 0, 0, 10 ]},
            { text: `The Lessee shall use the shop let out duly for the business of General Merchandise and not use the same for any other purpose. (The lessee shall not enter into partnership) and conduct the business in the premises in the name of the firm. The lessee can only use the premises for his own business.`, margin: [ 0, 0, 0, 10 ]},
            { text: `The lessee shall not have any right to assign, sub-let, re-let, under-let or transfer the tenancy or any portion thereof.`, margin: [ 0, 0, 0, 10 ]},
            { text: `The lessee shall not carry out any addition or alteration to the shop without the previous consent and approval in writing of the lessor.` , margin: [ 0, 0, 0, 10 ]},
            { text: `The lessee on the expiry of the lease period of ${obj.expiryDate} months shall hand over vacant possession of the ceased shop peacefully or the lease agreement can be renewed for a further period on mutually agreed terms.`, bold: true,  margin: [ 0, 0, 0, 40 ]   },
        ],
      };
      let footer1 = {
        columns: [
          {
            width: '50%',
            text: `${obj.commissionerName || ''}`,
            alignment:'left',
            bold:true,
            margin: [ 0, 0, 0, 10 ]
          },
          {
            width: '50%',
            text: ``,
            alignment:'right',
            bold:true,
            margin: [ 0, 0, 0, 10 ]
          }
        ],
        // optional space between columns
        columnGap: 10,
      };
      let footer2 = {
        columns: [
          {
            width: '50%',
            text: `Signature`,
            alignment:'left',
            bold:true,
            margin: [ 0, 0, 0, 10 ]
          },
          {
            width: '50%',
            text: `Lessee Signature`,
            alignment:'right',
            bold:true,
            margin: [ 0, 0, 0, 10 ]
          }
        ],
        // optional space between columns
        columnGap: 10,
      };
      let footer3 = {
        columns: [
          {
            width: '50%',
            text: `${tenantId.split(".")[1]}`,
            alignment:'left',
            bold:true,
            margin: [ 0, 0, 0, 10 ]
          },
          {
            width: '50%',
            text: ``,
            alignment:'right',
            bold:true,
            margin: [ 0, 0, 0, 10 ]
          }
        ],
        // optional space between columns
        columnGap: 10,
        pageBreak: (resultSet.length-1) == index ? '' : 'after',
      };
      contentArray.push(contentHeader);
      contentArray.push(content2Header);
      contentArray.push(content3Header);
      contentArray.push(line);
      contentArray.push(first);
      contentArray.push(second);
      contentArray.push(third);
      contentArray.push(contentObj);
      contentArray.push(footer1);
      contentArray.push(footer2);
      contentArray.push(footer3);
    });
    return contentArray;
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
    let {resultSet, assetCategories, locality, electionWard} = this.state;
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
              <th>Agreement Number</th>
              <th>Asset Code</th>
              <th>Asset Category</th>
              <th>Revenue Ward</th>
              <th>Allottee Name</th>
              <th>Allottee MobileNo</th>
              <th>Allottee Address</th>
              <th>Notice Type</th>

            </tr>
          </thead>
          <tbody>
            {resultSet && resultSet.map((list, index)=>{
              return (
                <tr onClick={e=>{this.showFile(list.fileStore)}}>
                  <td>{index+1}</td>
                  <td>{list.agreementNumber}</td>
                  <td>{list.assetNo}</td>
                  <td>{list.assetCategory}</td>
                  <td>{list.ward}</td>
                  <td>{list.allotteeName}</td>
                  <td>{list.allotteeMobileNumber}</td>
                  <td>{list.allotteeAddress}</td>
                  <td>{list.noticeType}</td>
                </tr>
              )
            })}
          </tbody>
        </table>
        </div>
      </div>
    )
  }
  showFile(fileStoreId){
    let self = this;
    var fileURL = '/filestore/v1/files/id?fileStoreId='+fileStoreId+'&tenantId='+tenantId;
    var oReq = new XMLHttpRequest();
    oReq.open("GET", fileURL, true);
    oReq.responseType = "arraybuffer";
    // console.log(fileURL);
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
  zipDownload(e){
    let {searchClicked, resultSet, searchSet} = this.state;
    let self = this;
    if(searchClicked){
      //resultset not changed in the state
      this.createZIP(resultSet);
    }else{
      if(!searchSet.noticeType){
        showError("Please select notice type.")
        return false;
      }
      //get the latest result from the API
      Promise.all([
        commonApiPost("lams-services/agreement","notice","_search",searchSet)
      ]).then(function(responses){
        // responses[0].Notices
        self.createZIP(responses[0].Notices);
      })
    }
  }
  createZIP(notices){
    var zip = new JSZip();
    // zip.file("file.txt", "content");
    notices.map((notice,index)=>{
      var fileURL = '/filestore/v1/files/id?fileStoreId='+notice.fileStore+'&tenantId='+tenantId;
      var oReq = new XMLHttpRequest();
      oReq.open("GET", fileURL, true);
      oReq.responseType = "arraybuffer";
      // console.log(fileURL);
      oReq.onload = function(oEvent) {
        var blob = new Blob([oReq.response], {type: oReq.getResponseHeader('content-type')});
        var url = URL.createObjectURL(blob);
        zip.file(`notice(${index+1}).pdf`, blob, { binary: true }); // <- [2]
        if(index == notices.length-1){
          zip.generateAsync({type:"blob"})
          .then(function(content) {
              // Force down of the Zip file
              saveAs(content, "notices.zip");
          });
        }
      };
      oReq.send();
    })
  }
  componentWillUpdate(){
    $('table').DataTable().destroy();
  }
  componentDidUpdate(prevProps, prevState)
  {
    let {searchClicked} = this.state;
    if(searchClicked){
      $('table').DataTable({
        dom: 'Bfrtip',
        buttons: [
                 'excel',
                 {
                    extend: 'pdf',
                    filename: 'Base Register Report',
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
    let {noticeType, assetCategories, locality, electionWard, revenueWard, revenueZone, revenueBlock, searchClicked} = this.state;
    let {handleChange, search, mergeDownload, zipDownload, closeWindow, showTable} = this;
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
                              <label for="">Notice Type <span className="error"> *</span></label>
                          </div>
                          <div className="col-sm-6">
                              <div className="styled-select">
                                  <select id="noticeType" onChange={(e) => { handleChange(e.target.value, "noticeType") }}>
                                    <option value="">Select Notice Type</option>
                                    {renderOptions(noticeType)}
                                  </select>
                              </div>
                          </div>
                      </div>
                  </div>
                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                              <label for="">Asset Category</label>
                          </div>
                          <div className="col-sm-6">
                              <div className="styled-select">
                                  <select id="assetCategory" onChange={(e) => { handleChange(e.target.value, "assetCategory") }}>
                                    <option value="">Select Asset Category</option>
                                    {renderOptions(assetCategories)}
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
                              <label for="">Acknowledgement No</label>
                          </div>
                          <div className="col-sm-6">
                            <input type="text" id="acknowledgementNumber" onChange={(e) => { handleChange(e.target.value, "acknowledgementNumber") }}/>
                          </div>
                      </div>
                  </div>
              </div>
              <div className="row">
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
              </div>
              {/*<div className="row">
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
              <div className="row">
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
              </div>*/}
              <div className="text-center">
                <button type="button" className="btn btn-submit" onClick={(e)=>{search(e)}}>Search</button>  &nbsp;&nbsp;
                <button type="button" className="btn btn-submit" onClick={(e)=>{mergeDownload(e)}}>Merge & Download</button>  &nbsp;&nbsp;
                <button type="button" className="btn btn-submit" onClick={(e)=>{zipDownload(e)}}>Zip & Download</button>  &nbsp;&nbsp;
                <button type="button" className="btn btn-submit" onClick={(e)=>{this.closeWindow()}}>Close</button>
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
                <h4 className="modal-title">Modal Header</h4>
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
    <Notice />,
    document.getElementById('root')
);
