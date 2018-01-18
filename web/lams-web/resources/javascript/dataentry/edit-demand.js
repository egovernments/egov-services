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
    this.handleChangeAll = this.handleChangeAll.bind(this);
  }

  close() {
    open(location, '_self').close();
  }

  validateOnSubmit(e) {
    var rent = null;
    var collection = null;
    var isValid = true;
    var demands = this.state.demands;

    demands.forEach((demand) => {
      rent = demand.taxAmount;
      collection = demand.collectionAmount;
      if (collection > rent) {
        isValid = false;
      }
    });

    return isValid;
  }

  addOrUpdate(e) {

    e.preventDefault();
    var agreementDetail = this.state.agreementDetail;
    var demands = this.state.demands;
    var tempt = [];

    demands.forEach((demand) => {
      tempt.push(demand);
    });

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

    if (e.target.value < 0) {
      e.preventDefault();
      showError("amount can't be negative.");
    }
    else {
      this.setState({
        demands: {
          ...this.state.demands,
          [k]: {
            ...this.state.demands[k],
            [name]: e.target.value
          }
        }
      })
    }
  }

  handleChangeAll(e, whichProperty) {
    var demands = this.state.demands;
    if (e.target.value < 0) {
      e.target.value = 0;
      e.preventDefault();
      showError("amount can't be negative.");
    }
    else {
      for (var variable in demands) {
        demands[variable][whichProperty] = e.target.value;
      }

      this.setState({
        demands
      })
    }
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
    var rentDemands = [];
    var penaltyDemands = [];
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
      paymentCycle: agreementDetail["paymentCycle"],
      agreementDetail
    })
  }


  render() {
    let { demands, paymentCycle, commonDemand, commonCollection } = this.state;
    let { handleCheckAll, handleChangeAll, handleChange, save } = this;

    var paymentCycleTh = paymentCycle.charAt(0).toUpperCase() + paymentCycle.slice(1).toLowerCase() + "LY Period";

    const renderBody = function () {

      return rentDemands.map((demand, index) => {

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

        <form onSubmit={(e) => {

          var valid = this.validateOnSubmit(e);
          if (valid) {
            this.addOrUpdate(e);
          } else {
            e.preventDefault();
            showError("Collection should not be greater than rent!");

          }
        }}>
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
              <button type="button" className="btn btn-default" onClick={(e) => {
                this.close()
              }}>Close</button>

              <button type="Submit" className="btn btn-submit">Submit</button>
            </div>
          </div>
        </form>
      </div>

    );
  }
}

ReactDOM.render(<EditDemand />, document.getElementById('root'));