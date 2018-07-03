var CONST_API_GET_FILE = "/filestore/v1/files/id?tenantId=" + tenantId + "&fileStoreId=";

const makeAjaxUpload = function (file, cb) {
  if (file.constructor == File) {
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
      success: function (res) {
        cb(null, res);
      },
      error: function (jqXHR, exception) {
        cb(jqXHR.responseText || jqXHR.statusText);
      }
    });
  } else {
    cb(null, {
      files: [{
        fileStoreId: file
      }]
    });
  }
}

const uploadFiles = function (body, cb) {
  if (body.Disposal.documents && body.Disposal.documents.length) {
    var counter = body.Disposal.documents.length;
    var breakout = 0, docs = [];
    for (let i = 0; i < body.Disposal.documents.length; i++) {
      makeAjaxUpload(body.Disposal.documents[i], function (err, res) {
        if (breakout == 1)
          return;
        else if (err) {
          cb(err);
          breakout = 1;
        } else {
          counter--;
          docs.push({fileStore:res.files[0].fileStoreId,  documentType:"disposal"});
          if (counter == 0) {
            body.Disposal.documents = docs;
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
        "buyerName": "",
        "buyerAddress": "",
        "disposalReason": "",
        "disposalDate": "",
        "panCardNumber": "",
        "aadharCardNumber": "",
        "assetCurrentValue": "",
        "saleValue": "",
        "assetSaleAccount": "",
        "auditDetails": null,
        "documents": [],
        "transactionType": ""
      },
      departments: [],
      revenueZones: [],
      revenueWards: [],
      typeToDisplay: false,
      assetAccount: [],
      readOnly: false,
      disposedFiles: []
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
    if (this.state.assetSet && this.state.assetSet.id)
      window.open(`app/asset/create-asset.html?id=${this.state.assetSet.id}&type=view`, '_blank', 'height=760, width=800, scrollbars=yes, status=yes');
  }

  setInitialState(initState) {
    this.setState(initState);
  }

  componentDidMount() {
    if (window.opener && window.opener.document) {
      var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
      if (logo_ele && logo_ele[0]) {
        document.getElementsByClassName("homepage_logo")[0].src = (logo_ele[0].getAttribute("src") && logo_ele[0].getAttribute("src").indexOf("http") > -1) ? logo_ele[0].getAttribute("src") : window.location.origin + logo_ele[0].getAttribute("src");
      }
    }

    if (getUrlVars()["type"]) $('#hpCitizenTitle').text(titleCase(getUrlVars()["type"]) + " Asset Sale Or Disposal");


    let id = getUrlVars()["id"], _this = this, count = 4, _state = {};

    if (getUrlVars()["type"] == "view") {
      _this.setState({
        readOnly: true
      })
    }

    const checkCountAndCall = function (key, res) {
      _state[key] = res;
      if (key == "assetSet") {
        _this.setState({
          disposal: {
            ..._this.state.disposal,
            assetId: res.id
          }
        })
      }
      count--;
      if (count == 0)
        _this.setInitialState(_state);
    };

    getCommonMasterById("asset-services", "assets", id, function (err, res) {
      if (res && res["Assets"] && res["Assets"][0]) {
        checkCountAndCall("assetSet", res["Assets"] && res["Assets"][0] ? res["Assets"][0] : {});
        commonApiPost("asset-services", "assets", "currentvalue/_search", { tenantId, assetIds: res["Assets"][0].id }, function (er, res) {
          if (res && res.AssetCurrentValues && res["AssetCurrentValues"][0]) {
            _this.setState({
              disposal: {
                ..._this.state.disposal,
                assetCurrentValue: res.AssetCurrentValues[0].currentAmount
              }
            })
          }
        });
      } else {
        console.log(err);
      }
    })

    if (getUrlVars()["type"] == "view") {
      setTimeout(function () {
        commonApiPost("asset-services", "assets/dispose", "_search", { assetId: id, tenantId, pageSize: 500 }, function (err, res2) {
          if (res2 && res2.Disposals && res2.Disposals.length) {
            let disposedAsset = res2.Disposals[0];

            //Changing date format
            var d = new Date(disposedAsset.disposalDate);
            disposedAsset.disposalDate = d.getDate() + "/" + (d.getMonth() + 1) + "/" + d.getFullYear();

            let profitloss = disposedAsset.assetCurrentValue - disposedAsset.saleValue;

            if (disposedAsset.documents && disposedAsset.documents.length) {
              _this.setState({
                profitloss,
                disposal: disposedAsset,
                typeToDisplay: disposedAsset.transactionType,
                disposedFiles: disposedAsset.documents.slice()
              })
            } else {
              _this.setState({
                profitloss,
                disposal: disposedAsset,
                typeToDisplay: disposedAsset.transactionType
              });
            }

            // commonApiPost("asset-services", "assets/currentvalue", "_search", { assetIds: id, tenantId }, function (err1, res3) {
            //   if (res3){
            //     _this.setState({
            //       disposal:{
            //       ..._this.state.disposal,
            //       assetCurrentValue: res3["AssetCurrentValues"]["0"]["currentAmount"]
            //     }
            //     });
            //   }
            // });

          }
        })
      }, 200);
    }

    getDropdown("assignments_department", function (res) {
      checkCountAndCall("departments", res);
    });

    getDropdown("revenueWard", function (res) {
      checkCountAndCall("revenueWards", res);
    });

    getDropdown("revenueZone", function (res) {
      checkCountAndCall("revenueZones", res);
    });

    var accounts = [];

    commonApiPost("egf-masters", "accountcodepurposes", "_search", { tenantId, name: "ASSET_PROFIT" }, function (err, res2) {
      if (res2 && res2["accountCodePurposes"][0]) {
        commonApiPost("egf-masters", "chartofaccounts", "_search", { tenantId, classification: 4, accountCodePurpose: res2["accountCodePurposes"][0].id }, function (err, resA) {
          if (resA) {
            resA["chartOfAccounts"][0].name = resA["chartOfAccounts"][0].glcode + "-" + resA["chartOfAccounts"][0].name;
            accounts.push(resA["chartOfAccounts"][0]);
            _this.setState({
              ..._this.state,
              assetAccount: accounts,
              assetAccountProfit: resA["chartOfAccounts"][0].id
            });
          }
        })
      }
    })

    commonApiPost("egf-masters", "accountcodepurposes", "_search", { tenantId, name: "ASSET_LOSS" }, function (err, res2) {
      if (res2 && res2["accountCodePurposes"][0]) {
        commonApiPost("egf-masters", "chartofaccounts", "_search", { tenantId, classification: 4, accountCodePurpose: res2["accountCodePurposes"][0].id }, function (err, resA) {
          if (resA) {
            resA["chartOfAccounts"][0].name = resA["chartOfAccounts"][0].glcode + "-" + resA["chartOfAccounts"][0].name;
            accounts.push(resA["chartOfAccounts"][0]);
            _this.setState({
              ..._this.state,
              assetAccount: accounts,
              assetAccountLoss: resA["chartOfAccounts"][0].id
            });
          }
        })
      }
    })
  }

  componentDidUpdate() {
    let _this = this;
    $("#disposalDate").datepicker({
      format: "dd/mm/yyyy",
      autoclose: true
    });

    $("#disposalDate").on("changeDate", function (e) {
      _this.setState({
        disposal: {
          ..._this.state.disposal,
          "disposalDate": e.target.value
        }
      })
    });
  }

  handleChange(e, name) {
    if (name == "transactionType") {
      if (e.target.value === "SALE") {
        return this.setState({
          typeToDisplay: e.target.value,
          disposal: {
            ...this.state.disposal,
            [name]: e.target.value,
            "aadharCardNumber": "",
            "panCardNumber": "",
            "assetSaleAccount": ""
          }
        })
      } else {
        return this.setState({
          typeToDisplay: e.target.value,
          disposal: {
            ...this.state.disposal,
            [name]: e.target.value,
            "aadharCardNumber": "",
            "panCardNumber": "",
            "assetSaleAccount": this.state.assetAccountLoss
          }
        })
      }

    } else if (name == "aadharCardNumber" && e.target.value) {
      if (/[^0-9]/.test(e.target.value) || e.target.value.length > 12) {
        var val = e.target.value.substring(0, e.target.value.length - 1);
        return this.setState({
          disposal: {
            ...this.state.disposal,
            [name]: val
          }
        });
      }
    } else if (name == "panCardNumber" && e.target.value) {
      if (!/^[a-zA-Z0-9]*$/.test(e.target.value) || e.target.value.length > 10) {
        var val = e.target.value.substring(0, e.target.value.length - 1);
        return this.setState({
          disposal: {
            ...this.state.disposal,
            [name]: val
          }
        });
      }
    } else if (name == "saleValue" && e.target.value) {
      if (this.state.disposal.assetCurrentValue && Number(this.state.disposal.assetCurrentValue) && Number(e.target.value)) {

        if (Number(this.state.disposal.assetCurrentValue) - Number(e.target.value) > 0) {
          return this.setState({
            disposal: {
              ...this.state.disposal,
              [name]: e.target.value,
              "assetSaleAccount": this.state.assetAccountLoss
            }
          });
        } else {
          return this.setState({
            disposal: {
              ...this.state.disposal,
              [name]: e.target.value,
              "assetSaleAccount": this.state.assetAccountProfit
            }
          });
        }

      }
    }

    this.setState({
      disposal: {
        ...this.state.disposal,
        [name]: name == "documents" ? e.target.files : e.target.value
      }
    })
  }

  handlePANValidation(e) {
    if (!e.target.value) {
      e.target.setCustomValidity("Please fill out this field.");
    } else if (!/^(?:[0-9]+[a-z]|[a-z]+[0-9])[a-z0-9]*$/i.test(e.target.value) || e.target.value.length < 10) {
      e.target.setCustomValidity("Please enter a valid pan.");
    } else {
      e.target.setCustomValidity("");
    }
  }

  handleAadharValidation(e) {
    if (!e.target.value) {
      e.target.setCustomValidity("Please fill out this field.");
    } else if (e.target.value.length < 12) {
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
    var tempInfo = Object.assign({}, this.state.disposal), _this = this;
    if (tempInfo.disposalDate) {
      var date = tempInfo.disposalDate.split("/");
      tempInfo.disposalDate = new Date(date[2], date[1] - 1, date[0]).getTime();
    }

    var body = {
      RequestInfo: requestInfo,
      Disposal: tempInfo
    };


    uploadFiles(body, function (err1, _body) {
      if (err1) {
        showError(err1);
      } else {
        $.ajax({
          url: baseUrl + "/asset-services/assets/dispose/_create",
          type: 'POST',
          dataType: 'json',
          data: JSON.stringify(_body),
          contentType: 'application/json',
          headers: {
            'auth-token': authToken
          },
          success: function (res) {
            window.location.href = `app/asset/create-asset-ack.html?name=${_this.state.assetSet.name}&type=&value=${(tempInfo.type == "Disposal" ? "disposed" : "sold")}&code=${_this.state.assetSet.code}`;
          },
          error: function (err) {
            console.log(err);
            var _err = err["responseJSON"].Error.message || "";
            if (err["responseJSON"].Error.fields && Object.keys(err["responseJSON"].Error.fields).length) {
              for (var key in err["responseJSON"].Error.fields) {
                _err += "\n " + key + "- " + err["responseJSON"].Error.fields[key] + " "; //HERE
              }
              showError(_err);
            } else if (_err) {
              showError(_err);
            } else {
              showError(err["statusText"]);
            }
          }
        })
      }
    })
  }

  render() {
    let { handleChange, close, createDisposal, handlePANValidation, handleAadharValidation, viewAssetDetails } = this;
    let { assetSet, departments, revenueWards, revenueZones, disposal, typeToDisplay, assetAccount, disposedFiles } = this.state;
    let self = this;
    const renderOptions = function (list) {
      if (list) {
        if (list.constructor == Array) {
          return list.map((item, ind) => {
            console.log(item);
            if (typeof item == "object") {
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
          return Object.keys(list).map((k, index) => {
            return (<option key={index} value={k}>
              {list[k]}
            </option>)
          })
        }
      }
    }

    const renderFileBody = function (fles) {
      return fles.map(function (v, ind) {
        return (
          <tr key={ind}>
            <td>{ind + 1}</td>
            <td> {"Document " + (ind + 1)}</td>
            <td>
              <a href={window.location.origin + CONST_API_GET_FILE + v.fileStore} target="_blank">
                Download
                  </a>
            </td>
          </tr>
        )

      })
    }

    function maskAadharCardNumber(aadharCardNumber){
      return(aadharCardNumber.replace(aadharCardNumber.substr(0, 8),"********"))
           }
    const showAttachedFiles = function () {
      if (disposedFiles && disposedFiles.length) {
        return (
          <table id="fileTable" className="table table-bordered">
            <thead>
              <tr>
                <th>Sr. No.</th>
                <th>Name</th>
                <th>File</th>
              </tr>
            </thead>
            <tbody id="agreementSearchResultTableBody">
              {
                renderFileBody(disposedFiles)
              }
            </tbody>

          </table>
        )
      }
    }

    const showOtherDetails = function () {
      if (typeToDisplay && typeToDisplay == "DISPOSAL") {
        return (
          <div>
            <div className="row">
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label>Disposal date <span>*</span></label>
                  </div>
                  <div className="col-sm-6" style={{ display: self.state.readOnly ? 'none' : 'block' }}>
                    <div>
                      <input id="disposalDate" type="text" value={disposal.disposalDate} className="datepicker" required />
                    </div>
                  </div>
                  <div className="col-sm-6 label-view-text" style={{ display: self.state.readOnly ? 'block' : 'none' }}>
                    <label>{disposal.disposalDate}</label>
                  </div>
                </div>
              </div>
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label>Disposal reason <span>*</span></label>
                  </div>
                  <div className="col-sm-6" style={{ display: self.state.readOnly ? 'none' : 'block' }}>
                    <div>
                      <textarea value={disposal.disposalReason} onChange={(e) => handleChange(e, "disposalReason")} required></textarea>
                    </div>
                  </div>
                  <div className="col-sm-6 label-view-text" style={{ display: self.state.readOnly ? 'block' : 'none' }}>
                    <label>{disposal.disposalReason}</label>
                  </div>
                </div>
              </div>
            </div>
            <div className="row">
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label>Current Value Of The Asset</label>
                  </div>
                  <div className="col-sm-6">
                    <div>
                      <input type="text" disabled value={disposal.assetCurrentValue} style={{ display: self.state.readOnly ? 'none' : 'block' }} />
                    </div>
                  </div>
                  <div className="col-sm-6 label-view-text" style={{ display: self.state.readOnly ? 'block' : 'none' }}>
                    <label>{disposal.assetCurrentValue === null ? (item.grossValue === null ? 0 : item.grossValue) : disposal.assetCurrentValue}</label>
                  </div>
                </div>
              </div>
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label>Asset Disposal Account Code <span>*</span></label>
                  </div>
                  <div className="col-sm-6" style={{ display: self.state.readOnly ? 'none' : 'block' }}>
                    <div>
                      <select required value={disposal.assetSaleAccount} onChange={(e) => handleChange(e, "assetSaleAccount")} disabled >
                        <option value="">Select Account Code</option>
                        {renderOptions(assetAccount)}
                      </select>
                    </div>
                  </div>
                  <div className="col-sm-6 label-view-text" style={{ display: self.state.readOnly ? 'block' : 'none' }}>
                    <label>{disposal.assetSaleAccount ? getNameById(assetAccount, disposal.assetSaleAccount) : ""}</label>
                  </div>
                </div>
              </div>

            </div>
            <div className="row" style={{ display: self.state.readOnly ? 'none' : 'block' }}>
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label>Attach Documents</label>
                  </div>
                  <div className="col-sm-6">
                    <div>
                      <input type="file" multiple onChange={(e) => handleChange(e, "documents")} />
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        )
      }
      if (typeToDisplay && typeToDisplay == "SALE") {
        return (
          <div>
            <div className="row">
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label>Sale date <span>*</span></label>
                  </div>
                  <div className="col-sm-6" style={{ display: self.state.readOnly ? 'none' : 'block' }}>
                    <div>
                      <input id="disposalDate" type="text" value={disposal.disposalDate} className="datepicker" required />
                    </div>
                  </div>
                  <div className="col-sm-6 label-view-text" style={{ display: self.state.readOnly ? 'block' : 'none' }}>
                    <label>{disposal.disposalDate}</label>
                  </div>
                </div>
              </div>
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label>Sale reason <span>*</span></label>
                  </div>
                  <div className="col-sm-6" style={{ display: self.state.readOnly ? 'none' : 'block' }}>
                    <div>
                      <textarea value={disposal.disposalReason} onChange={(e) => handleChange(e, "disposalReason")} required></textarea>
                    </div>
                  </div>
                  <div className="col-sm-6 label-view-text" style={{ display: self.state.readOnly ? 'block' : 'none' }}>
                    <label>{disposal.disposalReason}</label>
                  </div>
                </div>
              </div>
            </div>
            <div className="row">
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label>Sale party name <span>*</span> </label>
                  </div>
                  <div className="col-sm-6" style={{ display: self.state.readOnly ? 'none' : 'block' }}>
                    <div>
                      <input type="text" value={disposal.buyerName} onChange={(e) => handleChange(e, "buyerName")} required />
                    </div>
                  </div>
                  <div className="col-sm-6 label-view-text" style={{ display: self.state.readOnly ? 'block' : 'none' }}>
                    <label>{disposal.buyerName}</label>
                  </div>
                </div>
              </div>
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label>Sale party address <span> *</span> </label>
                  </div>
                  <div className="col-sm-6" style={{ display: self.state.readOnly ? 'none' : 'block' }}>
                    <div>
                      <textarea value={disposal.buyerAddress} onChange={(e) => handleChange(e, "buyerAddress")} required></textarea>
                    </div>
                  </div>
                  <div className="col-sm-6 label-view-text" style={{ display: self.state.readOnly ? 'block' : 'none' }}>
                    <label>{disposal.buyerAddress}</label>
                  </div>
                </div>
              </div>
            </div>
            <div className="row">
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label>Pan Card Number <span>*</span> </label>
                  </div>
                  <div className="col-sm-6" style={{ display: self.state.readOnly ? 'none' : 'block' }}>
                    <div>
                      <input type="text" value={disposal.panCardNumber} onChange={(e) => handleChange(e, "panCardNumber")} onInput={(e) => { handlePANValidation(e) }} onInvalid={(e) => { handlePANValidation(e) }} required />
                    </div>
                  </div>
                  <div className="col-sm-6 label-view-text" style={{ display: self.state.readOnly ? 'block' : 'none' }}>
                    <label>{disposal.panCardNumber}</label>
                  </div>
                </div>
              </div>
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label>Aadhar Card Number <span>*</span></label>
                  </div>
                  <div className="col-sm-6" style={{ display: self.state.readOnly ? 'none' : 'block' }}>
                    <div>
                      <input type="text" value={disposal.aadharCardNumber} onChange={(e) => handleChange(e, "aadharCardNumber")} onInput={(e) => { handleAadharValidation(e) }} onInvalid={(e) => { handleAadharValidation(e) }} required />
                    </div>
                  </div>
                  <div className="col-sm-6 label-view-text" style={{ display: self.state.readOnly ? 'block' : 'none' }}>
                    <label id="aadharCardNumber"> {maskAadharCardNumber(disposal.aadharCardNumber)}</label>
                  </div>
                </div>
              </div>
            </div>
            <div className="row">
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label>Current Value Of The Asset <span>*</span></label>
                  </div>
                  <div className="col-sm-6">
                    <div>
                      <input type="text" disabled value={disposal.assetCurrentValue} style={{ display: self.state.readOnly ? 'none' : 'block' }} />
                    </div>
                  </div>
                  <div className="col-sm-6 label-view-text" style={{ display: self.state.readOnly ? 'block' : 'none' }}>
                    <label>{disposal.assetCurrentValue}</label>
                  </div>
                </div>
              </div>
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label>Asset Sale Account Code <span>*</span></label>
                  </div>
                  <div className="col-sm-6" style={{ display: self.state.readOnly ? 'none' : 'block' }}>
                    <div>
                      <select required value={disposal.assetSaleAccount} onChange={(e) => handleChange(e, "assetSaleAccount")} disabled>
                        <option value="">Select Account Code</option>
                        {renderOptions(assetAccount)}
                      </select>
                    </div>
                  </div>
                  <div className="col-sm-6 label-view-text" style={{ display: self.state.readOnly ? 'block' : 'none' }}>
                    <label>{disposal.assetSaleAccount ? getNameById(assetAccount, disposal.assetSaleAccount) : ""}</label>
                  </div>
                </div>
              </div>

            </div>
            <div className="row">
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label>Sale Value <span>*</span></label>
                  </div>
                  <div className="col-sm-6" style={{ display: self.state.readOnly ? 'none' : 'block' }}>
                    <div>
                      <input type="number" value={disposal.saleValue} onChange={(e) => handleChange(e, "saleValue")} required />
                    </div>
                  </div>
                  <div className="col-sm-6 label-view-text" style={{ display: self.state.readOnly ? 'block' : 'none' }}>
                    <label>{disposal.saleValue}</label>
                  </div>
                </div>
              </div>
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label>Profit/Loss </label>
                  </div>
                  <div className="col-sm-6" style={{ display: self.state.readOnly ? 'none' : 'block' }}>
                    <div>
                      <input type="number" disabled value={disposal.assetCurrentValue && disposal.saleValue ? Math.abs(Number(disposal.assetCurrentValue) - Number(disposal.saleValue)) : ""} />
                    </div>
                  </div>
                  <div className="col-sm-6 label-view-text" style={{ display: self.state.readOnly ? 'block' : 'none' }}>
                    <label>{self.state.profitloss}</label>
                  </div>
                </div>
              </div>
            </div>
            <div className="row" style={{ display: self.state.readOnly ? 'none' : 'block' }}>
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label>Attach Documents</label>
                  </div>
                  <div className="col-sm-6">
                    <div>
                      <input type="file" multiple onChange={(e) => handleChange(e, "documents")} />
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        )
      }
    };

    return (
      <div>
        <h3 > {getUrlVars()["type"] ? titleCase(getUrlVars()["type"]) : "Create"} Asset Sale Or Disposal </h3>
        <form onSubmit={(e) => { createDisposal(e) }}>
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
                      <label>Asset Category Name </label>
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
                <h3 className="categoryType">Asset Sale or Disposal details </h3>
              </div>
              <div className="col-md-4 col-sm-4 text-right">

              </div>
              <div className="form-section-inner">
                <div className="row">
                  <div className="col-sm-6">
                    <div className="row">
                      <div className="col-sm-6 label-text">
                        <label>Type <span>*</span></label>
                      </div>
                      <div className="col-sm-6" style={{ display: this.state.readOnly ? 'none' : 'block' }}>
                        <div>
                          <select required onChange={(e) => handleChange(e, "transactionType")}>
                            <option value="">Select Type</option>
                            <option value="SALE">Sale</option>
                            <option value="DISPOSAL">Disposal</option>
                          </select>
                        </div>
                      </div>
                      <div className="col-sm-6 label-view-text" style={{ display: this.state.readOnly ? 'block' : 'none' }}>
                        <label>{disposal.transactionType}</label>
                      </div>
                    </div>
                  </div>
                </div>

                {showOtherDetails()}

                <div className="row" style={{ display: this.state.readOnly ? 'block' : 'none' }}>
                  <div className="col-sm-6">
                    <div className="row">
                      <div className="col-sm-6 label-text">
                        <label>Voucher Reference</label>
                      </div>
                      <div className="col-sm-6 label-view-text">
                        <label>{disposal.profitLossVoucherReference}</label>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <br />
          {showAttachedFiles()}
          <div className="text-center">
            {!this.state.readOnly && <button type="submit" className="btn btn-submit">Save</button>}&nbsp;&nbsp;
                <button type="button" className="btn btn-close" onClick={(e) => { close() }}>Close</button>
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
