const makeAjaxUpload = function(file, cb) {
    if(file.constructor == File) {
      let formData = new FormData();
      formData.append("jurisdictionId", tenantId);
      formData.append("module", "ASSET");
      formData.append("file", file);
      $.ajax({
          url: baseUrl + "/filestore/v1/files?tenantId=" + tenantId,
          data: formData,
          cache: false,
          contentType: false,
          processData: false,
          type: 'POST',
          success: function(res) {
              cb(null, res);
          },
          error: function(jqXHR, exception) {
              cb(jqXHR.responseText || jqXHR.statusText);
          }
      });
    } else {
      cb(null, {files: [{
        fileStoreId: file
      }]});
    }
}

const uploadFiles = function(body, cb) {
    if(body.Disposal.documents && body.Disposal.documents.length) {
        var counter = body.Disposal.documents.length;
        var breakout = 0, docs = [];
        for(let i=0; i<body.Disposal.documents.length; i++) {
          makeAjaxUpload(body.Disposal.documents[i], function(err, res) {
              if (breakout == 1)
                  return;
              else if (err) {
                  cb(err);
                  breakout = 1;
              } else {
                  counter--;
                  docs.push(res.files[0].fileStoreId);
                  if(counter == 0) {
                      body.Asset.documents = docs;
                      cb(null, body);
                  }
              }
          })
        }
    } else {
        cb(null, body);
    }
}

class Sale extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
          assetSet: {},
          disposal: {
            "tenantId": tenantId,
            "id": "",
            "assetId": "",
            "disposalPartyName": "",
            "disposalPartyAddress": "",
            "disposalReason": "",
            "disposalDate": "",
            "panCardNumber": "",
            "aadharCardNumber": "",
            "currentValueOfTheAsset": "",
            "saleValue": "",
            "assetSaleAccountCode": "",
            "auditDetails": "",
            "documents": []
          },
          departments: [],
          revenueZones: [],
          revenueWards: [],
          showPANNAadhar: false,
          assetAccount: []
        };
        this.handleChange = this.handleChange.bind(this);
        this.close = this.close.bind(this);
        this.setInitialState = this.setInitialState.bind(this);
        this.createDisposal = this.createDisposal.bind(this);
        this.handlePANValidation = this.handlePANValidation.bind(this);
        this.handleAadharValidation = this.handleAadharValidation.bind(this);
        this.viewAssetDetails = this.viewAssetDetails.bind(this);
    }

    viewAssetDetails(e) {
      e.preventDefault();
      e.stopPropagation();
      if(this.state.assetSet && this.state.assetSet.id)
        window.open(`app/asset/create-asset.html?id=${this.state.assetSet.id}&type=view`, '_blank', 'location=yes, height=760, width=800, scrollbars=yes, status=yes');
    }

    setInitialState(initState) {
      this.setState(initState);
    }

    componentDidMount() {
      if(window.opener && window.opener.document) {
        var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
        if(logo_ele && logo_ele[0]) {
          document.getElementsByClassName("homepage_logo")[0].src = (logo_ele[0].getAttribute("src") && logo_ele[0].getAttribute("src").indexOf("http") > -1) ? logo_ele[0].getAttribute("src") : window.location.origin + logo_ele[0].getAttribute("src");
        }
      }

      $(".datepicker").datepicker({
        format: "dd/mm/yyyy",
        autoclose: true
      });

      let id = getUrlVars()["id"], _this = this, count = 5, _state = {};
      const checkCountAndCall = function(key, res) {
        _state[key] = res;
        count--;
        if(count == 0)
          _this.setInitialState(_state);
      };

      getCommonMasterById("asset-services", "assets", id, function(err, res) {
          if(res && res["Assets"] && res["Assets"][0]) {
            checkCountAndCall("assetSet", res["Assets"] && res["Assets"][0] ? res["Assets"][0] : {});
            commonApiPost("asset-services", "assets", "currentValue/_search", {tenantId, assetId: res["Assets"][0].id}, function(er, res) {
              if(res && res.AssetCurrentValue) {
                _this.setState({
                  disposal: {
                    ..._this.state.disposal,
                    currentValueOfTheAsset: res.AssetCurrentValue.currentAmmount
                  }
                })
              }
            });
          } else {
            console.log(err);
          }
      })

      getDropdown("assignments_department", function(res) {
        checkCountAndCall("departments", res);
      });

      getDropdown("revenueWard", function(res) {
        checkCountAndCall("revenueWards", res);
      });

      getDropdown("revenueZone", function(res) {
        checkCountAndCall("revenueZones", res);
      });

      commonApiPost("egf-masters", "accountcodepurposes", "_search", {tenantId, name:"Fixed Assets"}, function(err, res2){
        if(res2){
          getDropdown("assetAccount", function(res) {
            for(var i= 0; i<res.length; i++) {
              res[i].name = res[i].glcode + "-" + res[i].name;
            }
            checkCountAndCall("assetAccount", res);
          }, {accountCodePurpose: res2["accountCodePurposes"][0].id});
        } else {
          checkCountAndCall("assetAccount", []);
        }
      })

      $("#disposalDate").datepicker({
        format: "dd/mm/yyyy",
        autoclose: true
      });

      $("#disposalDate").on("changeDate", function(e) {
        _this.setState({
          disposal: {
            ..._this.state.disposal,
            "disposalDate": e.target.value
          }
        })
      })
    }

    handleChange(e, name) {
      if(name == "type") {
        return this.setState({
          showPANNAadhar: e.target.value && e.target.value.toLowerCase() == "sale" ? true : false,
          disposal: {
            ...this.state.disposal,
            [name]: e.target.value,
            "aadharCardNumber": "",
            "panCardNumber": ""
          }
        })
      } else if(name == "aadharCardNumber" && e.target.value) {
        if(/[^0-9]/.test(e.target.value) || e.target.value.length > 12) {
          var val = e.target.value.substring(0, e.target.value.length-1);
          return this.setState({
            disposal: {
              ...this.state.disposal,
              [name]: val
            }
          });
        }
      } else if(name == "panCardNumber" && e.target.value) {
        if(!/^[a-zA-Z0-9]*$/.test(e.target.value) || e.target.value.length > 10) {
          var val = e.target.value.substring(0, e.target.value.length-1);
          return this.setState({
            disposal: {
              ...this.state.disposal,
              [name]: val
            }
          });
        }
      } else if(name == "type" && e.target.value == "sale") {
        this.setState({
          disposal: {
            ...this.state.disposal,
            [name]: e.target.value
          },
          showPANNAadhar: true
        })        
      }

      this.setState({
        disposal: {
          ...this.state.disposal,
          [name]: name == "documents" ? e.target.files : e.target.value
        }
      })
    }
    
    handlePANValidation(e) {
      if(!e.target.value) {
        e.target.setCustomValidity("Please fill out this field.");
      } else if(!/^(?:[0-9]+[a-z]|[a-z]+[0-9])[a-z0-9]*$/i.test(e.target.value) || e.target.value.length < 10) {
        e.target.setCustomValidity("Please enter a valid pan.");
      } else {
        e.target.setCustomValidity("");
      }
    }

    handleAadharValidation(e) {
      if(!e.target.value) {
        e.target.setCustomValidity("Please fill out this field.");
      } else if(e.target.value.length < 12) {
        e.target.setCustomValidity("Please enter a valid aadhar.");
      } else {
        e.target.setCustomValidity("");
      }
    }

    close() {
      open(location, '_self').close();
  	}

    createDisposal(e) {
      e.preventDefault();
      var tempInfo = Object.assign({}, this.state.disposal);
      var body = {
        RequestInfo: requestInfo,
        Disposal: tempInfo
      };

      //return console.log(JSON.stringify(body));
       $.ajax({
            url: baseUrl + "/assets/dispose/_create",
            type: 'POST',
            dataType: 'json',
            data: JSON.stringify(body),
            contentType: 'application/json',
            headers:{
                'auth-token': authToken
            },
            success: function(res) {
              
            },
            error: function(err) {
              console.log(err);
              var _err = err["responseJSON"].Error.message || "";
              if(err["responseJSON"].Error.fields && Object.keys(err["responseJSON"].Error.fields).length) {
                for(var key in err["responseJSON"].Error.fields) {
                  _err += "\n " + key + "- " + err["responseJSON"].Error.fields[key] + " "; //HERE
                }
                showError(_err);
              } else if(_err) {
                showError(_err);
              } else {
                showError(err["statusText"]);
              }
            }
        })
    }

  	render() {
      let {handleChange, close, createDisposal, handlePANValidation, handleAadharValidation, viewAssetDetails} = this;
      let {assetSet, departments, revenueWards, revenueZones, disposal, showPANNAadhar, assetAccount} = this.state;

      const renderOptions = function(list) {
        if(list) {
          if(list.constructor == Array) {
            return list.map((item, ind)=> {
                  if(typeof item == "object") {
                    return (<option key={ind} value={item.id}>
                          {item.name}
                    </option>)
                  } else {
                    return (<option key={ind} value={item}>
                          {item}
                    </option>)
                  }
                })
          } else {
            return Object.keys(list).map((k, index)=> {
                  return (<option key={index} value={k}>
                          {list[k]}
                    </option>)
                })
          }
        }
      }

      const showOtherDetails = function() {
        if(showPANNAadhar) {
          return (
            <div className="row">
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label>Pan Card Number <span>*</span> </label>
                  </div>
                  <div className="col-sm-6">
                  <div>
                    <input type="text" value={disposal.panCardNumber} onChange={(e)=>handleChange(e, "panCardNumber")} onInput={(e) => {handlePANValidation(e)}} onInvalid={(e) => {handlePANValidation(e)}} required/>
                  </div>
                </div>
              </div>
            </div>
            <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label>Aadhar Card Number <span>*</span></label>
                  </div>
                  <div className="col-sm-6">
                  <div>
                    <input type="text" value={disposal.aadharCardNumber} onChange={(e)=>handleChange(e, "aadharCardNumber")} onInput={(e) => {handleAadharValidation(e)}} onInvalid={(e) => {handleAadharValidation(e)}} required/>
                  </div>
                </div>
              </div>
            </div>
          </div>)
        }
      };

      return (
      	<div>
      		<h3 > Create Asset Sale Or Disposal </h3>
          <form onSubmit={(e) => {createDisposal(e)}}>
              <div className="form-section">
                <div className="row">
                  <div className="col-md-8 col-sm-8">
                    <h3 className="categoryType">Asset Details </h3>
                  </div>
                  <div className="col-md-4 col-sm-4 text-right">
                      <button type="button" className="btn btn-submit" onClick={(e) => viewAssetDetails(e)}>View Details</button>
                  </div>
                </div>
                <div className="form-section-inner">
                  <div className="row">
                      <div className="col-sm-6">
                          <div className="row">
                            <div className="col-sm-6 label-text">
                              <label>Asset Code </label>
                            </div>
                            <div className="col-sm-6 label-view-text">
                              <label>{assetSet.code}</label>
                            </div>
                        </div>
                      </div>
                      <div className="col-sm-6">
                          <div className="row">
                            <div className="col-sm-6 label-text">
                              <label>Asset Name </label>
                            </div>
                            <div className="col-sm-6 label-view-text">
                              <label>{assetSet.name}</label>
                            </div>
                        </div>
                      </div>
                    </div>
                    <div className="row">
                      <div className="col-sm-6">
                          <div className="row">
                            <div className="col-sm-6 label-text">
                              <label>Description </label>
                            </div>
                            <div className="col-sm-6 label-view-text">
                              <label>{assetSet.description}</label>
                            </div>
                        </div>
                      </div>
                      <div className="col-sm-6">
                          <div className="row">
                            <div className="col-sm-6 label-text">
                              <label>Asset Category Type </label>
                            </div>
                            <div className="col-sm-6 label-view-text">
                              <label>{assetSet.assetCategory && assetSet.assetCategory.name}</label>
                            </div>
                        </div>
                      </div>
                    </div>
                </div>
            </div>
            <div className="form-section">
              <div className="row">
                  <div className="col-md-8 col-sm-8">
                    <h3 className="categoryType">Asset Disposal Details </h3>
                  </div>
                  <div className="col-md-4 col-sm-4 text-right">
                    
                </div>
                <div className="form-section-inner">
                    <div className="row">
                      <div className="col-sm-6">
                          <div className="row">
                            <div className="col-sm-6 label-text">
                              <label>Disposal Date <span>*</span></label>
                            </div>
                            <div className="col-sm-6">
                            <div>
                              <input id="disposalDate" type="text" value={disposal.disposalDate} className="datepicker" required/>
                            </div>
                          </div>
                        </div>
                      </div>
                      <div className="col-sm-6">
                          <div className="row">
                            <div className="col-sm-6 label-text">
                              <label>Disposal Party Name <span>*</span> </label>
                            </div>
                            <div className="col-sm-6">
                            <div>
                              <input type="text" value={disposal.disposalPartyName} onChange={(e)=>handleChange(e, "disposalPartyName")} required/>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>  
                    <div className="row">
                      <div className="col-sm-6">
                          <div className="row">
                            <div className="col-sm-6 label-text">
                              <label>Disposal Party Address <span> *</span> </label>
                            </div>
                            <div className="col-sm-6">
                            <div>
                              <input type="text" value={disposal.disposalPartyAddress} onChange={(e)=>handleChange(e, "disposalPartyAddress")} required/>
                            </div>
                          </div>
                        </div>
                      </div>
                      <div className="col-sm-6">
                          <div className="row">
                            <div className="col-sm-6 label-text">
                              <label>Disposal Reason <span>*</span></label>
                            </div>
                            <div className="col-sm-6">
                            <div>
                              <input type="text" value={disposal.disposalReason} onChange={(e)=>handleChange(e, "disposalReason")} required/>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>  
                    <div className="row">
                      <div className="col-sm-6">
                          <div className="row">
                            <div className="col-sm-6 label-text">
                              <label>Type <span>*</span></label>
                            </div>
                            <div className="col-sm-6">
                            <div>
                              <select required onChange={(e)=>handleChange(e, "type")}>
                                <option value="">Select Type</option>
                                <option value="Sale">Sale</option>
                                <option value="Disposal">Disposal</option>
                              </select>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>  
                    {showOtherDetails()}
                    <div className="row">
                      <div className="col-sm-6">
                          <div className="row">
                            <div className="col-sm-6 label-text">
                              <label>Current Value Of The Asset <span>*</span></label>
                            </div>
                            <div className="col-sm-6">
                            <div>
                              <input type="number" value={disposal.currentValueOfTheAsset} onChange={(e)=>handleChange(e, "currentValueOfTheAsset")} required/>
                            </div>
                          </div>
                        </div>
                      </div>
                      <div className="col-sm-6">
                        <div className="row">
                            <div className="col-sm-6 label-text">
                              <label>Sale Value <span>*</span></label>
                            </div>
                            <div className="col-sm-6">
                            <div>
                              <input type="number" value={disposal.saleValue} onChange={(e)=>handleChange(e, "saleValue")} required/>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div> 
                    <div className="row">
                      <div className="col-sm-6">
                        <div className="row">
                          <div className="col-sm-6 label-text">
                            <label>Profit/Loss </label>
                          </div>
                          <div className="col-sm-6">
                            <div>
                              <input type="text" disabled value={disposal.currentValueOfTheAsset && disposal.saleValue ? Math.abs(Number(disposal.currentValueOfTheAsset) - Number(disposal.saleValue)) : ""}/>
                            </div>
                          </div>
                        </div>
                      </div>
                      <div className="col-sm-6">
                          <div className="row">
                            <div className="col-sm-6 label-text">
                              <label>Asset Sale Account Code <span>*</span></label>
                            </div>
                            <div className="col-sm-6">
                            <div>
                              <select required value={disposal.assetSaleAccountCode} onChange={(e)=>handleChange(e, "assetSaleAccountCode")}>
                                <option value="">Select Account Code</option>
                                {renderOptions(assetAccount)}
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
                              <label>Attach Documents</label>
                            </div>
                            <div className="col-sm-6">
                              <div>
                                <input type="file" multiple onChange={(e)=>handleChange(e, "documents")}/>
                              </div>
                            </div>
                        </div>
                      </div>
                    </div>
                </div>
              </div>
              <p className="text-right text-danger">Note: Current value of the asset is not considering the depreciation and improvements done on that asset</p>
            </div>
            <div className="text-center">
                <button type="submit" className="btn btn-submit">Save</button>&nbsp;&nbsp;
                <button type="button" className="btn btn-close" onClick={(e)=>{close()}}>Close</button>
            </div>
          </form>
      	</div>
      )
    }
}

ReactDOM.render(
  <Sale />,
  document.getElementById('root')
);