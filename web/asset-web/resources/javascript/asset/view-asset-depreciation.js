class Depreciation extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      readOnly: false,
      assetSet: {},
      functions: [],
      funds: [],
      fixedAssetAccount: [],
      assetAccountToDisp: "",
      revaluationReserveAccountToDisp: "",
      "depreciationSet": {
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

    let id = getUrlVars()["id"], _this = this, count = 3, _state = {};

    if (getUrlVars()["type"] == "view") {
      _this.setState({
        readOnly: true
      });
    }

    const checkCountAndCall = function (key, res) {
      _state[key] = res;
      if (key == "assetSet") {
        _this.setState({
          depreciationSet: {
            ..._this.state.depreciationSet,
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
        commonApiPost("egf-masters", "chartofaccounts", "_search", { "tenantId": tenantId, "classification": "4", "id": res["Assets"][0].assetCategory.assetAccount }, function (AccErr, AccRes) {
          if (AccRes) {
            _this.setState({
              ..._this.state,
              assetAccountToDisp: AccRes.chartOfAccounts[0].glcode + "-" + AccRes.chartOfAccounts[0].name
            })
          }
        });

        commonApiPost("egf-masters", "chartofaccounts", "_search", { "tenantId": tenantId, "classification": "4", "id": res["Assets"][0].assetCategory.revaluationReserveAccount }, function (AccErr, AccRes) {
          if (AccRes) {
            _this.setState({
              ..._this.state,
              revaluationReserveAccountToDisp: AccRes.chartOfAccounts[0].glcode + "-" + AccRes.chartOfAccounts[0].name
            })
          }
        });



        commonApiPost("asset-services", "assets", "currentvalue/_search", { tenantId, assetIds: res["Assets"][0].id }, function (er, res) {
          if (res && res.AssetCurrentValues.length > 0) {
            _this.setState({
              depreciationSet: {
                ..._this.state.depreciationSet,
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
      commonApiPost("asset-services", "assets/depreciations", "_search", { id: getUrlVars()["depreciationId"], tenantId, pageSize: 500 }, function (err, res2) {
        if (res2 && res2.DepreciationReportCriteria && res2.DepreciationReportCriteria.length) {
          let revalAsset = res2.DepreciationReportCriteria[0];

          _this.setState({
            depreciationSet: revalAsset
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

    commonApiPost("egf-masters", "accountcodepurposes", "_search", { tenantId, name: "ASSET_DEDUCTION" }, function (err, res2) {
      if (res2 && res2["accountCodePurposes"][0]) {
        commonApiPost("egf-masters", "chartofaccounts", "_search", { tenantId, classification: 4, accountCodePurpose: res2["accountCodePurposes"][0].id }, function (err, resA) {
          if (resA) {
            resA["chartOfAccounts"][0].name = resA["chartOfAccounts"][0].glcode + "-" + resA["chartOfAccounts"][0].name;
            _this.setState({
              ..._this.state,
              fixedAssetAccount: [resA["chartOfAccounts"][0]],
              fixedAssetsWrittenOffAccountPlaceHolder: resA["chartOfAccounts"][0].id
            });
          }
        })
      }
    })

    commonApiPost("hr-employee", "employees", "_loggedinemployee", { tenantId }, function (err, res) {
      if (res && res.Employee && res.Employee[0] && res.Employee[0].userName) {
        _this.setState({
          depreciationSet: {
            ..._this.state.depreciationSet,
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
      let fixedAssetsWrittenOffAccount = "";
      if (val) {
        valueAfterRevaluation = Number(val);
        if (this.state.depreciationSet.currentCapitalizedValue) {
          revaluationAmount = Number(valueAfterRevaluation - this.state.depreciationSet.currentCapitalizedValue);
          revaluationAmount > 0 ? typeOfChange = "INCREASED" : typeOfChange = "DECREASED";
          revaluationAmount > 0 ? fixedAssetsWrittenOffAccount = "" : fixedAssetsWrittenOffAccount = this.state.fixedAssetsWrittenOffAccountPlaceHolder
          revaluationAmount = Math.abs(revaluationAmount);
        } else if (this.state.assetSet.grossValue) {
          revaluationAmount = Number(valueAfterRevaluation - this.state.assetSet.grossValue);
          revaluationAmount > 0 ? typeOfChange = "INCREASED" : typeOfChange = "DECREASED";
          revaluationAmount > 0 ? fixedAssetsWrittenOffAccount = "" : fixedAssetsWrittenOffAccount = this.state.fixedAssetsWrittenOffAccountPlaceHolder
          revaluationAmount = Math.abs(revaluationAmount);
        }
      }

      setTimeout(function () {
        _this.setState({
          depreciationSet: {
            ..._this.state.depreciationSet,
            "revaluationAmount": revaluationAmount,
            "typeOfChange": typeOfChange,
            fixedAssetsWrittenOffAccount: fixedAssetsWrittenOffAccount
          }
        })
      }, 200);


      // switch(name) {
      //   case 'revaluationAmount':
      //
      //     break;
      //   case 'typeOfChange':
      //     if(this.state.depreciationSet.revaluationAmount) {
      //       switch (val) {
      //         case 'INCREASED':
      //           setTimeout(function() {
      //             _this.setState({
      //               depreciationSet: {
      //                 ..._this.state.depreciationSet,
      //                 "valueAfterRevaluation": Number(_this.state.assetSet.grossValue) + Number(_this.state.depreciationSet.revaluationAmount)
      //               }
      //             })
      //           }, 200);
      //           break;
      //         case 'DECREASED':
      //           setTimeout(function() {
      //             _this.setState({
      //               depreciationSet: {
      //                 ..._this.state.depreciationSet,
      //                 "valueAfterRevaluation": Number(_this.state.assetSet.grossValue) - Number(_this.state.depreciationSet.revaluationAmount)
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
      depreciationSet: {
        ...this.state.depreciationSet,
        [name]: val
      }
    })
  }

  close() {
    open(location, '_self').close();
  }

  createRevaluation(e) {
    e.preventDefault();
    var tempInfo = Object.assign({}, this.state.depreciationSet), _this = this;

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
    let { assetSet, functions, funds, depreciationSet, fixedAssetAccount, assetAccountToDisp, revaluationReserveAccountToDisp } = this.state;
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
                <h3 className="categoryType">Asset Depreciation Details </h3>
              </div>
              <div className="col-md-4 col-sm-4 text-right">

              </div>
            </div>
            <div className="form-section-inner">
              
              <div className="row">
                <div className="col-sm-6">
                  <div className="row">
                    <div className="col-sm-6 label-text">
                      <label>Depreciation Name</label>
                    </div>
                    <div className="col-sm-6">
                      <div>
                        <input type="text" disabled value={depreciationSet.currentCapitalizedValue} style={{ display: this.state.readOnly ? 'none' : 'block' }} />
                      </div>
                    </div>
                    <div className="col-sm-6 label-view-text" style={{ display: this.state.readOnly ? 'block' : 'none' }}>
                      <label>{depreciationSet.assetId}</label>
                    </div>
                  </div>
                </div>
                <div className="col-sm-6">
                  <div className="row">
                    <div className="col-sm-6 label-text">
                      <label>Current Depreciation(Rs.)</label>
                    </div>
                    <div className="col-sm-6">
                      <div>
                        <input type="text" disabled value={depreciationSet.currentCapitalizedValue} style={{ display: this.state.readOnly ? 'none' : 'block' }} />
                      </div>
                    </div>
                    <div className="col-sm-6 label-view-text" style={{ display: this.state.readOnly ? 'block' : 'none' }}>
                      <label>{depreciationSet.depreciationValue}</label>
                    </div>
                  </div>
                </div>
              </div>

              <div className="row">
              <div className="col-sm-6">
                  <div className="row">
                    <div className="col-sm-6 label-text">
                      <label>Value after Depreciation(Rs)</label>
                    </div>
                    <div className="col-sm-6">
                      <div>
                        <input type="text" disabled value={depreciationSet.currentCapitalizedValue} style={{ display: this.state.readOnly ? 'none' : 'block' }} />
                      </div>
                    </div>
                    <div className="col-sm-6 label-view-text" style={{ display: this.state.readOnly ? 'block' : 'none' }}>
                      <label>{depreciationSet.valueAfterDepreciation}</label>
                    </div>
                  </div>
                </div>
                <div className="col-sm-6" style={{ display: this.state.readOnly ? 'block' : 'none' }}>
                  <div className="row">
                    <div className="col-sm-6 label-text">
                      <label>Voucher Number </label>
                    </div>
                    <div className="col-sm-6 label-view-text">
                      <label>{depreciationSet.voucherReference? depreciationSet.voucherReference :"NA" }</label>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>


          <div className="text-center">
            <button type="button" className="btn btn-close" onClick={(e) => { close() }}>Close</button>
          </div>
        </form>
      </div>
    )
  }
}

ReactDOM.render(
  <Depreciation />,
  document.getElementById('root')
);
