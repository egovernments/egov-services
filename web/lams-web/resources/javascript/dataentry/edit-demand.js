class EditDemand extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      demands: [],
      agreementDetail: {},
      paymentCycle: "",
      commonDemand: null,
      commonCollection: null
    }
    this.close = this.close.bind(this);
    this.addOrUpdate = this.addOrUpdate.bind(this);
    this.handleChange = this.handleChange.bind(this);
  }

  close() {
    open(location, '_self').close();
  }

  addOrUpdate(e) {

    e.preventDefault();
    var agreementDetail = this.state.agreementDetail;
    var demands = this.state.demands;
    var restOfDemands = this.state.restOfDemands;

    for (var i = 0; i < demands.length; i++) {

         if(demands[i].taxAmount <0 || demands[i].taxAmount ==""){
           demands[i].taxAmount = 0;
         }
         if(demands[i].collectionAmount <0 || demands[i].collectionAmount==""){
           demands[i].collectionAmount = 0;
         }
      if (demands[i].taxAmount < 0 || demands[i].collectionAmount < 0)
         return showError("amount can't be negative.");
      if (demands[i].collectionAmount >0
        && demands[i].taxAmount < demands[i].collectionAmount)
        return showError("advance collection is not allowed");
      if (demands[i].collectionAmount >0
        && demands[i].taxAmount > demands[i].collectionAmount)
        return showError("partial collection is not allowed");
    }
          for (var i = 0; i < demands.length; i++) {

              // good will amount gst and sgst collected or not
              if (demands[i].taxReasonCode.toLowerCase() == "goodwill_amount"
                  && demands[i].taxAmount == demands[i].collectionAmount) {

                  if (demands[i + 1].taxReasonCode.toLowerCase() == "gw_cgst"
                      && demands[i + 1].taxAmount != demands[i + 1].collectionAmount) {
                      return showError("Please Collect CGST On Good Will Amount");
                  }
                  if (demands[i + 2].taxReasonCode.toLowerCase() == "gw_sgst"
                      && demands[i + 2].taxAmount != demands[i + 2].collectionAmount) {
                      return showError("Please Collect SGST On Good Will Amount ");
                  }
              }

              if (demands[i].taxReasonCode.toLowerCase() == "gw_cgst"
                  && demands[i].taxAmount == demands[i].collectionAmount) {

                  if (demands[i - 1].taxReasonCode.toLowerCase() == "goodwill_amount"
                      && demands[i - 1].taxAmount != demands[i - 1].collectionAmount) {
                      return showError("Please Collect Good Will Amount ");
                  }
              }
              if (demands[i].taxReasonCode.toLowerCase() == "gw_sgst"
                  && demands[i].taxAmount == demands[i].collectionAmount) {
                  if (demands[i - 2].taxReasonCode.toLowerCase() == "goodwill_amount"
                      && demands[i - 2].taxAmount != demands[i - 2].collectionAmount) {
                      return showError("Please Collect Good Will Amount ");
                  }
              }

            // Advance amount gst and sgst collected or not
              if (demands[i].taxReasonCode.toLowerCase() == "advance_tax"
                  && demands[i].taxAmount == demands[i].collectionAmount) {

                  if (demands[i + 1].taxReasonCode.toLowerCase() == "adv_cgst"
                      && demands[i + 1].taxAmount != demands[i + 1].collectionAmount) {
                      return showError("Please Collect CGST On Advance Amount");
                  }
                  if (demands[i + 2].taxReasonCode.toLowerCase() == "adv_sgst"
                      && demands[i + 2].taxAmount != demands[i + 2].collectionAmount) {
                      return showError("Please Collect SGST On Advance Amount");
                  }
              }

              if (demands[i].taxReasonCode.toLowerCase() == "adv_cgst"
                  && demands[i].taxAmount == demands[i].collectionAmount) {

                  if (demands[i - 1].taxReasonCode.toLowerCase() == "advance_tax"
                      && demands[i - 1].taxAmount != demands[i - 1].collectionAmount) {
                      return showError("Please Collect Advance Amount ");
                  }
              }
              if (demands[i].taxReasonCode.toLowerCase() == "adv_sgst"
                  && demands[i].taxAmount == demands[i].collectionAmount) {
                  if (demands[i - 2].taxReasonCode.toLowerCase() == "advance_tax"
                      && demands[i - 2].taxAmount != demands[i - 2].collectionAmount) {
                      return showError("Please Collect Advance Amount ");
                  }
              }

        // condition to check service tax for advance and good will collected or not
              if (demands[i].taxReasonCode.toLowerCase() == "advance_tax"
                  && demands[i].taxAmount == demands[i].collectionAmount) {

                  if (demands[i + 1].taxReasonCode.toLowerCase() == "adv_st"
                      && demands[i + 1].taxAmount != demands[i + 1].collectionAmount) {
                      return showError("Please Collect Service Tax On Advance ");
                  }
              }

              if (demands[i].taxReasonCode.toLowerCase() == "adv_st"
                  && demands[i].taxAmount == demands[i].collectionAmount) {

                  if (demands[i - 1].taxReasonCode.toLowerCase() == "advance_tax"
                      && demands[i - 1].taxAmount != demands[i - 1].collectionAmount) {
                      return showError("Please Collect Advance Tax ");
                  }
              }

              if (demands[i].taxReasonCode.toLowerCase() == "goodwill_amount"
                  && demands[i].taxAmount == demands[i].collectionAmount ) {

                  if (demands[i + 1].taxReasonCode.toLowerCase() == "gw_st"
                      && demands[i + 1].taxAmount != demands[i + 1].collectionAmount) {
                      return showError("Please Collect Service Tax On Good Will ");
                  }
              }

              if (demands[i].taxReasonCode.toLowerCase() == "gw_st"
                  && demands[i].taxAmount == demands[i].collectionAmount) {

                  if (demands[i - 1].taxReasonCode.toLowerCase() == "goodwill_amount"
                      && demands[i - 1].taxAmount != demands[i - 1].collectionAmount) {
                      return showError("Please Collect Good Will Amount ");
                  }
              }
          }

    for (var i = 0; i < demands.length; i++) {

      if (demands[i].taxReason.toLowerCase() == "rent" &&
            demands[i].taxAmount == 0 &&
            demands[i+1].taxAmount != 0) {

              return showError("Penalty cannot be entered for the taxperiod where rent is 0");

            }

    }
     showLoading();
    var tempt = demands.concat(restOfDemands);

    agreementDetail["legacyDemands"][0]["demandDetails"] = tempt;

    // api call update method
    var response = $.ajax({
      url: baseUrl + `/lams-services/agreements/_update/${agreementDetail.agreementNumber}?tenantId=` + tenantId,
      type: 'POST',
      dataType: 'json',
      data: JSON.stringify({
        RequestInfo: requestInfo,
        Agreement: agreementDetail
      }),
      async: false,
      headers: {
        'auth-token': authToken
      },
      contentType: 'application/json'
    });
    if (response["status"] === 201) {
      hideLoading();
      showSuccess("Demand updated successfully");
      setTimeout(function () {
        window.open(location, '_self').close();
      }, 3000);

    } else {
      showError(response["statusText"]);
      hideLoading();
    }

  }

  handleChange(e, name, k) {
    var tempDemands = this.state.demands.slice();
    if(!isNaN(e.target.value)){
        tempDemands[k][name] = Math.round(e.target.value);
    }

    this.setState({
      demands: tempDemands
    })

  }
     isNumberKey(evt) {
        var charCode = (evt.which) ? evt.which : event.keyCode
        if (charCode > 47 && charCode < 58)
            return true;

        return false;
    }

  componentDidMount() {
    if (window.opener && window.opener.document) {
      var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
      if (logo_ele && logo_ele[0]) {
        document.getElementsByClassName("homepage_logo")[0].src = logo_ele[0].getAttribute("src");
      }
    }

  }

  componentWillMount() {
    var demands = [];
    var rentDemands = [];
    var penaltyDemands = [];
    var sgstDemands = [];
    var cgstDemands = [];
    var goodWillAmount = [];
    var cgstGoodWillAmount = [];
    var sgstGoodWillAmount = [];
    var depositAmount = [];
    var cgstDepositAmount = [];
    var sgstDepositAmount = [];
    var serviceTaxDemands = [];
    var restOfDemands = [];
    var serviceTaxOnGoodWill = [];
    var serviceTaxOnAdvance = [];
    var agreementDetail = {};
    var currentDate =  new Date();

    try {
      if (getUrlVars()["agreementNumber"]) {
        agreementDetail = commonApiPost("lams-services", "agreements", "demands/_prepare", {
          agreementNumber: getUrlVars()["agreementNumber"],
          tenantId
        }).responseJSON["Agreements"][0] || {};
      }

    } catch (e) {
      console.log(e);
    }

    if(agreementDetail.source!='DATA_ENTRY' || agreementDetail.action!='CREATE' || agreementDetail.status!='ACTIVE' ){
        $("#shopAssetDetailsBlock").remove();
      return showError("This is not a valid agreement number");
    }

      agreementDetail["legacyDemands"][0]["demandDetails"].forEach((demand) => {

          if (demand.taxReasonCode.toLowerCase() == "advance_tax") {
              depositAmount.push(demand);
          }
      });
      agreementDetail["legacyDemands"][0]["demandDetails"].forEach((demand) => {

          if (demand.taxReasonCode.toLowerCase() == "adv_cgst") {
              cgstDepositAmount.push(demand);
          }
      });
      agreementDetail["legacyDemands"][0]["demandDetails"].forEach((demand) => {
          if (demand.taxReasonCode.toLowerCase() == "adv_sgst") {
              sgstDepositAmount.push(demand);
          }
      });
      agreementDetail["legacyDemands"][0]["demandDetails"].forEach((demand) => {

          if (demand.taxReasonCode.toLowerCase() == "goodwill_amount") {
              goodWillAmount.push(demand);
          }
      });

      agreementDetail["legacyDemands"][0]["demandDetails"].forEach((demand) => {

          if (demand.taxReasonCode.toLowerCase() == "gw_cgst") {
              cgstGoodWillAmount.push(demand);
          }
      });
      agreementDetail["legacyDemands"][0]["demandDetails"].forEach((demand) => {

          if (demand.taxReasonCode.toLowerCase() == "gw_sgst") {
              sgstGoodWillAmount.push(demand);
          }
      });

    agreementDetail["legacyDemands"][0]["demandDetails"].forEach((demand) => {
      if (demand.taxReason.toLowerCase() === "rent") {
        var installmentDate= moment(moment(new Date(demand.periodStartDate)).format("YYYY-DD-MM"));
            installmentDate = new Date(installmentDate.format("YYYY"),installmentDate.format("MM")-1,installmentDate.format("DD"));
        if(installmentDate.getTime() <= currentDate.getTime() ){
        rentDemands.push(demand);
      }
        }
    });

    agreementDetail["legacyDemands"][0]["demandDetails"].forEach((demand) => {
      if (demand.taxReason.toLowerCase() === "penalty") {
        penaltyDemands.push(demand);
      }
    });

    agreementDetail["legacyDemands"][0]["demandDetails"].forEach((demand) => {
      if (demand.taxReason.toLowerCase() === "cgst" || demand.taxReasonCode.toLowerCase() === "central_gst") {
        cgstDemands.push(demand);
      }
    });

    agreementDetail["legacyDemands"][0]["demandDetails"].forEach((demand) => {
      if (demand.taxReason.toLowerCase() === "sgst" || demand.taxReasonCode.toLowerCase() === "state_gst") {
        sgstDemands.push(demand);
      }
    });

    agreementDetail["legacyDemands"][0]["demandDetails"].forEach((demand) => {
      if (demand.taxReason.toLowerCase() === "service tax" || demand.taxReasonCode.toLowerCase() === "service_tax") {
        serviceTaxDemands.push(demand);
      }
    });

      agreementDetail["legacyDemands"][0]["demandDetails"].forEach((demand) => {

          if (demand.taxReasonCode.toLowerCase() === "gw_st" || demand.taxReason.toLowerCase() === "ServiceTax On GoodWill") {
              serviceTaxOnGoodWill.push(demand);
          }
      });

      agreementDetail["legacyDemands"][0]["demandDetails"].forEach((demand) => {
          if (demand.taxReasonCode.toLowerCase() === "adv_st" || demand.taxReason.toLowerCase() === "ServiceTax On Advance") {
          serviceTaxOnAdvance.push(demand);
      }
      });

    var index = 0;

    for (var i = 0; i < rentDemands.length; i++) {

        depositAmount.forEach((pDemand) => {
            if (pDemand.taxPeriod === rentDemands[i].taxPeriod) {
                demands.splice(index, 0, pDemand);
                index++;
            }
        });
        serviceTaxOnAdvance.forEach((pDemand) => {
            if (pDemand.taxPeriod === rentDemands[i].taxPeriod) {
                demands.splice(index, 0, pDemand);
                index++;
            }
        });

        cgstDepositAmount.forEach((pDemand) => {
            if (pDemand.taxPeriod === rentDemands[i].taxPeriod) {
                demands.splice(index, 0, pDemand);
                index++;
            }
        });

        sgstDepositAmount.forEach((pDemand) => {
            if (pDemand.taxPeriod === rentDemands[i].taxPeriod) {
                demands.splice(index, 0, pDemand);
                index++;
            }
        });

        goodWillAmount.forEach((pDemand) => {
            if (pDemand.taxPeriod === rentDemands[i].taxPeriod) {
                demands.splice(index, 0, pDemand);
                index++;
            }
        });
        serviceTaxOnGoodWill.forEach((pDemand) => {
            if (pDemand.taxPeriod === rentDemands[i].taxPeriod) {
                demands.splice(index, 0, pDemand);
                index++;
            }
        });

        cgstGoodWillAmount.forEach((pDemand) => {
            if (pDemand.taxPeriod === rentDemands[i].taxPeriod) {
                demands.splice(index, 0, pDemand);
                index++;
            }
        });

        sgstGoodWillAmount.forEach((pDemand) => {
            if (pDemand.taxPeriod === rentDemands[i].taxPeriod) {
                demands.splice(index, 0, pDemand);
                index++;
            }
        });


        demands.splice(index, 0, rentDemands[i]);
        index++;
        penaltyDemands.forEach((pDemand) => {
        if (pDemand.taxPeriod === rentDemands[i].taxPeriod) {
          demands.splice(index, 0, pDemand);
          index++;
        }
      });

      serviceTaxDemands.forEach((pDemand) => {
        if (pDemand.taxPeriod === rentDemands[i].taxPeriod) {
          demands.splice(index, 0, pDemand);
          index++;
        }
      });

      cgstDemands.forEach((pDemand) => {
        if (pDemand.taxPeriod === rentDemands[i].taxPeriod) {
          demands.splice(index, 0, pDemand);
          index++;
        }
      });

      sgstDemands.forEach((pDemand) => {
        if (pDemand.taxPeriod === rentDemands[i].taxPeriod) {
          demands.splice(index, 0, pDemand);
          index++;
        }
      });

    }

    // console.log(demands);
    this.setState({
      demands,
      restOfDemands,
      paymentCycle: agreementDetail["paymentCycle"],
      agreementDetail
    })
  }


  render() {
    let { demands, paymentCycle, commonDemand, commonCollection } = this.state;
    let { handleCheckAll, handleChange, save } = this;

    var paymentCycleTh = paymentCycle.charAt(0).toUpperCase() + paymentCycle.slice(1).toLowerCase() + "ly Period";

    const renderBody = function () {

      return demands.map((demand, index) => {
          if(demand.taxReason.toLowerCase() === 'cgst on advance'
              || demand.taxReason.toLowerCase() === 'sgst on advance'
              || demand.taxReason.toLowerCase() === 'cgst on goodwill'
              || demand.taxReason.toLowerCase() === 'sgst on goodwill'
              || demand.taxReason.toLowerCase() === 'cgst'
              || demand.taxReason.toLowerCase() === 'sgst'){
              return (<tr key={index}>
                  <td>{demand["taxPeriod"] + "[9% " + demand["taxReason"].toLowerCase() + "]"}</td>
                  <td data-label="demand">
                      <input type="text" name={demand["taxPeriod"] + "demand"} value={demand["taxAmount"]} onkeypress="return isNumberKey(event)" onChange={(e) => {
                          handleChange(e, "taxAmount", index)
                      }} />
                  </td>
                  <td data-label="collection">
                      <input type="text" name={demand["taxPeriod"] + "collection"} value={demand["collectionAmount"]} onkeypress="return isNumberKey(event)" onChange={(e) => {
                          handleChange(e, "collectionAmount", index)
                      }} disabled={demand.isCollected} />
                  </td>
              </tr>)
          } else
        return (<tr key={index}>
          <td>{demand["taxPeriod"] + "[" + demand["taxReason"].toLowerCase() + "]"}</td>
          <td data-label="demand">
            <input type="text" name={demand["taxPeriod"] + "demand"} value={demand["taxAmount"]} onkeypress="return isNumberKey(event)" onChange={(e) => {
              handleChange(e, "taxAmount", index)
            }} />
          </td>
          <td data-label="collection">
            <input type="text" name={demand["taxPeriod"] + "collection"} value={demand["collectionAmount"]} onkeypress="return isNumberKey(event)" onChange={(e) => {
              handleChange(e, "collectionAmount", index)
            }} disabled={demand.isCollected} />
          </td>
        </tr>)
      })

    }

    return (
      <div>

        <form onSubmit={(e) => { this.addOrUpdate(e) }} >

          <div className="form-section-inner">

            <table id="editDemand" className="table table-bordered">
              <thead>
                <tr>
                  <th className="text-center">{paymentCycleTh}</th>
                  <th className="text-center">Demand</th>
                  <th>Collection</th>
                </tr>
              </thead>
              <tbody>
                {renderBody()}
              </tbody>
            </table>
            <div className="text-center">
              <button type="Submit" className="btn btn-submit">Submit</button> &nbsp;&nbsp;
              <button type="button" className="btn btn-close" onClick={(e) => { this.close() }}>Close</button>
            </div>
          </div>
        </form>
      </div>

    );
  }
}

ReactDOM.render(<EditDemand />, document.getElementById('root'));