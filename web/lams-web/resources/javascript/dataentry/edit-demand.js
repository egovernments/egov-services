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

      if (demands[i].taxAmount < 0 || demands[i].collectionAmount < 0)
         return showError("amount can't be negative.");
      if (demands[i].collectionAmount
        && demands[i].taxAmount < demands[i].collectionAmount)
        return showError("advance collection is not allowed");
      if (demands[i].collectionAmount
        && demands[i].taxAmount > demands[i].collectionAmount)
        return showError("partial collection is not allowed");

    }


    for (var i = 0; i < demands.length; i++) {

      if (demands[i].taxReason.toLowerCase() == "rent" && 
            demands[i].taxAmount === 0 && 
            demands[i+1].taxAmount != 0) {

              return showError("Penalty cannot be entered for the taxperiod where rent is 0");

            }
        
    }



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
      showSuccess("Demand updated successfully");
      setTimeout(function () {
        window.open(location, '_self').close();
      }, 5000);
    } else {
      showError(response["statusText"]);
    }

  }

  handleChange(e, name, k) {

    var tempDemands = this.state.demands.slice();
    tempDemands[k][name] = e.target.value;

    this.setState({
      demands: tempDemands
    })

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
    var restOfDemands = [];
    var agreementDetail = {};
    //api call

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

    agreementDetail["legacyDemands"][0]["demandDetails"].forEach((variable) => {
      if (variable.taxReason.toLowerCase() == "rent") {
        rentDemands.push(variable);
      }
    });

    agreementDetail["legacyDemands"][0]["demandDetails"].forEach((variable) => {
      if (variable.taxReason.toLowerCase() == "penalty") {
        penaltyDemands.push(variable);
      }
    });

    agreementDetail["legacyDemands"][0]["demandDetails"].forEach((variable) => {
      if (variable.taxReason.toLowerCase() != "rent" && variable.taxReason.toLowerCase() != "penalty") {
        restOfDemands.push(variable);
      }
    });

    var index = 0;

    for (var i = 0; i < rentDemands.length; i++) {

      demands.splice(index, 0, rentDemands[i]);
      index++;

      penaltyDemands.forEach((pDemand) => {
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

        return (<tr key={index}>
          <td>{demand["taxPeriod"] + "[" + demand["taxReason"].toLowerCase() + "]"}</td>
          <td data-label="demand">
            <input type="number" name={demand["taxPeriod"] + "demand"} value={demand["taxAmount"]} onChange={(e) => {
              handleChange(e, "taxAmount", index)
            }} />
          </td>
          <td data-label="collection">
            <input type="number" name={demand["taxPeriod"] + "collection"} value={demand["collectionAmount"]} onChange={(e) => {
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