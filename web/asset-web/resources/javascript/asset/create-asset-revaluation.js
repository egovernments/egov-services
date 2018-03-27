class Revaluation extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      readOnly: false,
      assetSet: {},
      functions: [],
      funds: [],
      fixedAssetAccount: [],
      "revaluationSet": {
        "tenantId": tenantId,
        "id": "",
        "assetId": "",
        "typeOfChange": "",
        "revaluationAmount": "",
        "valueAfterRevaluation": "",
        "revaluationDate": "",
        "reevaluatedBy": "",
        "reasonForRevaluation": "",
        "fixedAssetsWrittenOffAccount": "",
        "function": "",
        "fund": "",
        "comments": "",
        "status": "ACTIVE",
        "auditDetails": null,
        "revaluationOrderNo": "",
        "revaluationOrderDate": ""
      }
    };
    this.handleChange = this.handleChange.bind(this);
    this.close = this.close.bind(this);
    this.setInitialState = this.setInitialState.bind(this);
    this.createRevaluation = this.createRevaluation.bind(this);
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
    if (getUrlVars()["type"]) $('#hpCitizenTitle').text(titleCase(getUrlVars()["type"]) + " Asset Revaluation");

    let id = getUrlVars()["id"], _this = this, count = 4, _state = {};
    $("#revaluationDate").datepicker({
      format: "dd/mm/yyyy",
      autoclose: true
    });

    $("#revaluationDate").on("change", function (e) {
      _this.setState({
        revaluationSet: {
          ..._this.state.revaluationSet,
          "revaluationDate": e.target.value
        }
      })
    })

    $("#revaluationOrderDate").datepicker({
      format: "dd/mm/yyyy",
      autoclose: true
    });

    $("#revaluationOrderDate").on("change", function (e) {
      _this.setState({
        revaluationSet: {
          ..._this.state.revaluationSet,
          "revaluationOrderDate": e.target.value
        }
      })
    })

    if (getUrlVars()["type"] == "view") {
      _this.setState({
        readOnly: true
      });
    }

    const checkCountAndCall = function (key, res) {
      _state[key] = res;
      if (key == "assetSet") {
        _this.setState({
          revaluationSet: {
            ..._this.state.revaluationSet,
            assetId: res.id
          }
        })
      }
      count--;
      if (count == 0)
        _this.setInitialState(_state);
    }

    getCommonMasterById("asset-services", "assets", id, function (err, res) {
      if (res) {
        checkCountAndCall("assetSet", res["Assets"] && res["Assets"][0] ? res["Assets"][0] : {});
        commonApiPost("asset-services", "assets", "currentvalue/_search", { tenantId, assetIds: res["Assets"][0].id }, function (er, res) {
          if (res && res.AssetCurrentValues.length > 0) {
            _this.setState({
              revaluationSet: {
                ..._this.state.revaluationSet,
                currentCapitalizedValue: res.AssetCurrentValues[0].currentAmount
              }
            })
          }
        });
      } else {
        console.log(err);
      }
    });

    if (getUrlVars()["type"] == "view") {
      commonApiPost("asset-services", "assets/revaluation", "_search", { assetId: id, tenantId, pageSize: 500 }, function (err, res2) {
        if (res2 && res2.Revaluations && res2.Revaluations.length) {
          let revalAsset = res2.Revaluations[0];

          //Changing date format
          var d = new Date(revalAsset.revaluationDate);
          revalAsset.revaluationDate = d.getDate() + "/" + (d.getMonth() + 1) + "/" + d.getFullYear();

          var d1 = new Date(revalAsset.revaluationOrderDate);
          revalAsset.revaluationOrderDate = d1.getDate() + "/" + (d1.getMonth() + 1) + "/" + d1.getFullYear();

          //Getting the Name of the employee
          commonApiPost("hr-employee", "employees", "_search", {
            tenantId, id: revalAsset.reevaluatedBy, pageSize: 500
          }, function (err, Eres) {
            console.log("inside the employee call", Eres);
            if (Eres && Eres.Employee && Eres.Employee.length)
              revalAsset.reevaluatedBy = Eres.Employee[0].name;
            _this.setState({
              revaluationSet: revalAsset
            });
          });


        }
      })
    }

    getDropdown("assignments_fund", function (res) {
      checkCountAndCall("funds", res);
    });

    getDropdown("assignments_function", function (res) {
      checkCountAndCall("functions", res);
    });

    commonApiPost("egf-masters", "accountcodepurposes", "_search", { tenantId, name: "Fixed Assets Written off" }, function (err, res2) {
      if (res2) {
        getDropdown("fixedAssetAccount", function (res) {
          for (var i = 0; i < res.length; i++) {
            res[i].name = res[i].glcode + "-" + res[i].name;
          }
          checkCountAndCall("fixedAssetAccount", res);
        }, { accountCodePurpose: res2["accountCodePurposes"][0].id });
      } else {
        checkCountAndCall("fixedAssetAccount", []);
      }
    })

    commonApiPost("hr-employee", "employees", "_loggedinemployee", { tenantId }, function (err, res) {
      if (res && res.Employee && res.Employee[0] && res.Employee[0].userName) {
        _this.setState({
          revaluationSet: {
            ..._this.state.revaluationSet,
            reevaluatedBy: res.Employee[0].userName
          }
        })
      }
    })
  }

  handleChange(e, name) {
    var _this = this, val = e.target.value;
    if (name == "valueAfterRevaluation") {

      let valueAfterRevaluation = 0;
      let revaluationAmount = 0;
      let typeOfChange = "";
      if (val) {
        valueAfterRevaluation = Number(val);
        if (this.state.revaluationSet.currentCapitalizedValue) {
          revaluationAmount = Number(valueAfterRevaluation - this.state.revaluationSet.currentCapitalizedValue);
          revaluationAmount > 0 ? typeOfChange = "INCREASED" : typeOfChange = "DECREASED"
          revaluationAmount = Math.abs(revaluationAmount);
        } else if (this.state.assetSet.grossValue) {
          revaluationAmount = Number(valueAfterRevaluation - this.state.assetSet.grossValue);
          revaluationAmount > 0 ? typeOfChange = "INCREASED" : typeOfChange = "DECREASED"
          revaluationAmount = Math.abs(revaluationAmount);
        }
      }

      setTimeout(function () {
        _this.setState({
          revaluationSet: {
            ..._this.state.revaluationSet,
            "revaluationAmount": revaluationAmount,
            "typeOfChange": typeOfChange
          }
        })
      }, 200);


      // switch(name) {
      //   case 'revaluationAmount':
      //
      //     break;
      //   case 'typeOfChange':
      //     if(this.state.revaluationSet.revaluationAmount) {
      //       switch (val) {
      //         case 'INCREASED':
      //           setTimeout(function() {
      //             _this.setState({
      //               revaluationSet: {
      //                 ..._this.state.revaluationSet,
      //                 "valueAfterRevaluation": Number(_this.state.assetSet.grossValue) + Number(_this.state.revaluationSet.revaluationAmount)
      //               }
      //             })
      //           }, 200);
      //           break;
      //         case 'DECREASED':
      //           setTimeout(function() {
      //             _this.setState({
      //               revaluationSet: {
      //                 ..._this.state.revaluationSet,
      //                 "valueAfterRevaluation": Number(_this.state.assetSet.grossValue) - Number(_this.state.revaluationSet.revaluationAmount)
      //               }
      //             });
      //           }, 200);
      //           break;
      //       }
      //     }
      //     break;
      // }
    }

    this.setState({
      revaluationSet: {
        ...this.state.revaluationSet,
        [name]: val
      }
    })
  }

  close() {
    open(location, '_self').close();
  }

  createRevaluation(e) {
    e.preventDefault();
    var tempInfo = Object.assign({}, this.state.revaluationSet), _this = this;

    if (tempInfo.revaluationDate) {
      var date = tempInfo.revaluationDate.split("/");
      tempInfo.revaluationDate = new Date(date[2], date[1] - 1, date[0]).getTime();
    }

    if (tempInfo.revaluationOrderDate) {
      var date = tempInfo.revaluationOrderDate.split("/");
      tempInfo.revaluationOrderDate = new Date(date[2], date[1] - 1, date[0]).getTime();
    }

    var body = {
      RequestInfo: requestInfo,
      Revaluation: tempInfo
    };

    //return console.log(JSON.stringify(body));
    $.ajax({
      url: baseUrl + "/asset-services/assets/revaluation/_create",
      type: 'POST',
      dataType: 'json',
      data: JSON.stringify(body),
      contentType: 'application/json',
      headers: {
        'auth-token': authToken
      },
      success: function (res) {
        window.location.href = `app/asset/create-asset-ack.html?name=${_this.state.assetSet.name}&type=&value=revaluate&code=${_this.state.assetSet.code}`;
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

  render() {
    let { handleChange, close, createRevaluation, viewAssetDetails } = this;
    let { assetSet, functions, funds, revaluationSet, fixedAssetAccount } = this.state;
    const renderOptions = function (list) {
      if (list) {
        if (list.constructor == Array) {
          return list.map((item, ind) => {
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

    const renderOptionsForFund = function (list) {
      if (list) {
        if (list.constructor == Array) {
          return list.map((item, ind) => {
            if (item["active"]) {
              return (<option key={ind} value={item.id}>
                {item.name}
              </option>)
            }
          })
        }
      }
    }

    return (
      <div>
        <h3 > {getUrlVars()["type"] ? titleCase(getUrlVars()["type"]) : "Create"} Asset Revaluation </h3>
        <form onSubmit={(e) => { createRevaluation(e) }}>
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
                <h3 className="categoryType">Asset Revaluation Details </h3>
              </div>
              <div className="col-md-4 col-sm-4 text-right">

              </div>
            </div>
            <div className="form-section-inner">
              <div className="row">
                <div className="col-sm-6">
                  <div className="row">
                    <div className="col-sm-6 label-text">
                      <label>Revaluation Order No. <span>*</span></label>
                    </div>
                    <div className="col-sm-6" style={{ display: this.state.readOnly ? 'none' : 'block' }}>
                      <div>
                        <input type="text" min="0" value={revaluationSet.revaluationOrderNo} onChange={(e) => handleChange(e, "revaluationOrderNo")} required pattern="[a-zA-Z0-9./-]+" />
                      </div>
                    </div>
                    <div className="col-sm-6 label-view-text" style={{ display: this.state.readOnly ? 'block' : 'none' }}>
                      <label>{revaluationSet.revaluationOrderNo ? revaluationSet.revaluationOrderNo : ""}</label>
                    </div>
                  </div>
                </div>
                <div className="col-sm-6">
                  <div className="row">
                    <div className="col-sm-6 label-text">
                      <label>Revaluation Order Date <span>*</span></label>
                    </div>
                    <div className="col-sm-6" style={{ display: this.state.readOnly ? 'none' : 'block' }}>
                      <div>
                        <input id="revaluationOrderDate" className="datepicker" type="text" value={revaluationSet.revaluationOrderDate} onChange={(e) => handleChange(e, "revaluationOrderDate")} required />
                      </div>
                    </div>
                    <div className="col-sm-6 label-view-text" style={{ display: this.state.readOnly ? 'block' : 'none' }}>
                      <label>{revaluationSet.revaluationOrderDate ? revaluationSet.revaluationOrderDate : ""}</label>
                    </div>
                  </div>
                </div>
              </div>
              <div className="row">
                <div className="col-sm-6">
                  <div className="row">
                    <div className="col-sm-6 label-text">
                      <label>Value After Revaluation <span>*</span></label>
                    </div>
                    <div className="col-sm-6" style={{ display: this.state.readOnly ? 'none' : 'block' }}>
                      <input type="number" min="0" value={revaluationSet.valueAfterRevaluation} onChange={(e) => handleChange(e, "valueAfterRevaluation")} required />
                    </div>
                    <div className="col-sm-6 label-view-text" style={{ display: this.state.readOnly ? 'block' : 'none' }}>
                      <label>{revaluationSet.valueAfterRevaluation}</label>
                    </div>
                  </div>
                </div>
                <div className="col-sm-6">
                  <div className="row">
                    <div className="col-sm-6 label-text">
                      <label>Type Of Change <span>*</span> </label>
                    </div>
                    <div className="col-sm-6" style={{ display: this.state.readOnly ? 'none' : 'block' }}>
                      <div>
                        <select value={revaluationSet.typeOfChange} disabled>
                          <option value="">Select Type Of Change</option>
                          <option value="INCREASED">Increase</option>
                          <option value="DECREASED">Decrease</option>
                        </select>
                      </div>
                    </div>
                    <div className="col-sm-6 label-view-text" style={{ display: this.state.readOnly ? 'block' : 'none' }}>
                      <label>{revaluationSet.typeOfChange}</label>
                    </div>
                  </div>
                </div>
              </div>
              <div className="row">
                <div className="col-sm-6">
                  <div className="row">
                    <div className="col-sm-6 label-text">
                      <label>Revaluation Date <span>*</span> </label>
                    </div>
                    <div className="col-sm-6" style={{ display: this.state.readOnly ? 'none' : 'block' }}>
                      <div>
                        <input id="revaluationDate" className="datepicker" type="text" value={revaluationSet.revaluationDate} onChange={(e) => handleChange(e, "revaluationDate")} required />
                      </div>
                    </div>
                    <div className="col-sm-6 label-view-text" style={{ display: this.state.readOnly ? 'block' : 'none' }}>
                      <label>{revaluationSet.revaluationDate}</label>
                    </div>
                  </div>
                </div>
                  {getUrlVars()["type"] == "view" && <div className="row">
                  <div className="col-sm-6">
                    <div className="row">
                      <div className="col-sm-6 label-text">
                        <label>Re-evaluated By </label>
                      </div>
                      <div className="col-sm-6 label-view-text">
                        <label>{revaluationSet.reevaluatedBy}</label>
                      </div>
                    </div>
                  </div>
                </div>}
              </div>
              <div className="row">
                <div className="col-sm-6">
                  <div className="row">
                    <div className="col-sm-6 label-text">
                      <label>Reason For Revaluation </label>
                    </div>
                    <div className="col-sm-6" style={{ display: this.state.readOnly ? 'none' : 'block' }}>
                      <div>
                        <textarea value={revaluationSet.reasonForRevaluation} onChange={(e) => handleChange(e, "reasonForRevaluation")}></textarea>
                      </div>
                    </div>
                    <div className="col-sm-6 label-view-text" style={{ display: this.state.readOnly ? 'block' : 'none' }}>
                      <label>{revaluationSet.reasonForRevaluation}</label>
                    </div>
                  </div>
                </div>
                <div className="col-sm-6">
                  <div className="row">
                    <div className="col-sm-6 label-text">
                      <label>Addition/Deduction </label>
                    </div>
                    <div className="col-sm-6" style={{ display: this.state.readOnly ? 'none' : 'block' }}>
                      <div>
                        <input type="number" value={revaluationSet.revaluationAmount} disabled />
                      </div>
                    </div>
                    <div className="col-sm-6 label-view-text" style={{ display: this.state.readOnly ? 'block' : 'none' }}>
                      <label>{revaluationSet.revaluationAmount}</label>
                    </div>
                  </div>
                </div>

              </div>
              <div className="row" style={{ display: this.state.readOnly ? 'block' : 'none' }}>
                <div className="col-sm-6">
                  <div className="row">
                    <div className="col-sm-6 label-text">
                      <label>Voucher Reference </label>
                    </div>
                    <div className="col-sm-6 label-view-text">
                      <label>{revaluationSet.voucherReference}</label>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div className="form-section">
            <div className="row">
              <div className="col-md-8 col-sm-8">
                <h3 className="categoryType">Accounting Details </h3>
              </div>
              <div className="col-md-4 col-sm-4 text-right">

              </div>
            </div>
            <div className="form-section-inner">
              <div className="row">
                <div className="col-sm-6">
                  <div className="row">
                    <div className="col-sm-6 label-text">
                      <label>Asset Account Code </label>
                    </div>
                    <div className="col-sm-6 label-view-text">
                      <label>{assetSet.assetCategory && assetSet.assetCategory.assetAccount}</label>
                    </div>
                  </div>
                </div>
                <div className="col-sm-6">
                  <div className="row">
                    <div className="col-sm-6 label-text">
                      <label>Revaluation Reserve Account Code </label>
                    </div>
                    <div className="col-sm-6 display-span">
                      <span>{assetSet.assetCategory && assetSet.assetCategory.revaluationReserveAccount}</span>
                    </div>
                  </div>
                </div>
              </div>
              <div className="row">
                <div className="col-sm-6">
                  <div className="row">
                    <div className="col-sm-6 label-text">
                      <label>Fixed Assets Written Off Account Code </label>
                    </div>
                    <div className="col-sm-6" style={{ display: this.state.readOnly ? 'none' : 'block' }}>
                      <div>
                        <select value={revaluationSet.fixedAssetsWrittenOffAccount} onChange={(e) => handleChange(e, "fixedAssetsWrittenOffAccount")}>
                          <option value="">Select Account Code</option>
                          {renderOptions(fixedAssetAccount)}
                        </select>
                      </div>
                    </div>
                    <div className="col-sm-6 label-view-text" style={{ display: this.state.readOnly ? 'block' : 'none' }}>
                      <label>{revaluationSet.fixedAssetsWrittenOffAccount ? getNameById(fixedAssetAccount, revaluationSet.fixedAssetsWrittenOffAccount) : ""}</label>
                    </div>
                  </div>
                </div>
                <div className="col-sm-6">
                  <div className="row">
                    <div className="col-sm-6 label-text">
                      <label>Function <span>*</span> </label>
                    </div>
                    <div className="col-sm-6" style={{ display: this.state.readOnly ? 'none' : 'block' }}>
                      <div>
                        <select value={revaluationSet.function} onChange={(e) => handleChange(e, "function")} required>
                          <option value="">Select Function</option>
                          {renderOptions(functions)}
                        </select>
                      </div>
                    </div>
                    <div className="col-sm-6 label-view-text" style={{ display: this.state.readOnly ? 'block' : 'none' }}>
                      <label>{revaluationSet.function ? getNameById(functions, revaluationSet.function) : ""}</label>
                    </div>
                  </div>
                </div>
              </div>
              <div className="row">
                <div className="col-sm-6">
                  <div className="row">
                    <div className="col-sm-6 label-text">
                      <label>Fund <span>*</span></label>
                    </div>
                    <div className="col-sm-6" style={{ display: this.state.readOnly ? 'none' : 'block' }}>
                      <div>
                        <select value={revaluationSet.fund} onChange={(e) => handleChange(e, "fund")} required>
                          <option value="">Select Fund</option>
                          {renderOptionsForFund(funds)}
                        </select>
                      </div>
                    </div>
                    <div className="col-sm-6 label-view-text" style={{ display: this.state.readOnly ? 'block' : 'none' }}>
                      <label>{revaluationSet.fund ? getNameById(funds, revaluationSet.fund) : ""}</label>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div className="form-section">
            <div className="row">
              <div className="col-md-8 col-sm-8">
                <h3 className="categoryType">Approved Details </h3>
              </div>
              <div className="col-md-4 col-sm-4 text-right">

              </div>
            </div>
            <div className="form-section-inner">
              <div className="row">
                <div className="col-sm-6">
                  <div className="row">
                    <div className="col-sm-6 label-text">
                      <label>Comments </label>
                    </div>
                    <div className="col-sm-6" style={{ display: this.state.readOnly ? 'none' : 'block' }}>
                      <div>
                        <textarea value={revaluationSet.comments} onChange={(e) => handleChange(e, "comments")}></textarea>
                      </div>
                    </div>
                    <div className="col-sm-6 label-view-text" style={{ display: this.state.readOnly ? 'block' : 'none' }}>
                      <label>{revaluationSet.comments}</label>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
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
  <Revaluation />,
  document.getElementById('root')
);
