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
    this.close = this.close.bind(this);
    this.setInitialState = this.setInitialState.bind(this);
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
    if (getUrlVars()["type"]) $('#hpCitizenTitle').text(titleCase(getUrlVars()["type"]) + " Asset Depreciation");

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

  }

  close() {
    open(location, '_self').close();
  }

  render() {
    let { close, createRevaluation, viewAssetDetails } = this;
    let { assetSet, depreciationSet } = this.state;

    return (
      <div>
        <h3 > {getUrlVars()["type"] ? titleCase(getUrlVars()["type"]) : "Create"} Asset Depreciation </h3>
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
