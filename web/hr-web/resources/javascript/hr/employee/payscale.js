class PayScaleMaster extends React.Component {
  constructor(props) {
    super(props);
    this.state = {

      payscaleSet: {
        "paycommission": "",
        "payscale": "",
        "amountFrom": "",
        "amountTo": "",
        "tenantId": tenantId,
        "payscaleDetails": [
          {
            "basicFrom": "",
            "basicTo": "",
            "increment": "",
            "tenantId": tenantId
          }
        ]
      }

    }
    this.handleChange = this.handleChange.bind(this);
    this.handleDetailsChange = this.handleDetailsChange.bind(this);
    this.addOrUpdate = this.addOrUpdate.bind(this);
    this.addPayScaleDetails = this.addPayScaleDetails.bind(this);
    this.deletePayscaleDetails = this.deletePayscaleDetails.bind(this);
  }

  handleDetailsChange(e, index, name) {

    let details = Object.assign([], this.state.payscaleSet.payscaleDetails);

    if(name === 'basicTo'){
      if(details[Number(index) + 1])
        details[Number(index) + 1].basicFrom = e.target.value
    }

    details[index][name] = e.target.value;
    
    this.setState({
      payscaleSet: {
        ...this.state.payscaleSet,
        payscaleDetails: details
      }
    })
  }

  handleChange(e, name) {

    if (name === 'amountFrom') {

      let details = Object.assign([], this.state.payscaleSet.payscaleDetails);

      if(details.length > 0)
      details[0].basicFrom = e.target.value

      this.setState({
        payscaleSet: {
          ...this.state.payscaleSet,
          [name]: e.target.value,
          payscaleDetails:details 
        }
      })
    } else {
      this.setState({
        payscaleSet: {
          ...this.state.payscaleSet,
          [name]: e.target.value
        }
      })
    }
  }

  deletePayscaleDetails(ind) {
    let details = Object.assign([], this.state.payscaleSet.payscaleDetails);
    if (details.length > 1) {
      details.splice(ind, 1);

      this.setState({
        payscaleSet: {
          ...this.state.payscaleSet,
          payscaleDetails: details
        }
      })
    } else {
      alert("Cant not Delete all payscale");
    }
  }

  addPayScaleDetails(index) {

    let details = Object.assign([], this.state.payscaleSet.payscaleDetails);

    let newDetails = { "basicFrom": details[Number(index) - 1].basicTo, "basicTo": "", "increment": "", "tenantId": tenantId };

    details.push(newDetails);

    this.setState({
      payscaleSet: {
        ...this.state.payscaleSet,
        payscaleDetails: details
      }
    })

  }


  componentDidMount() {
    var type = getUrlVars()["type"];
    var id = getUrlVars()["id"], _this = this;
    if (window.opener && window.opener.document) {
      var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
      if (logo_ele && logo_ele[0]) {
        document.getElementsByClassName("homepage_logo")[0].src = window.location.origin + logo_ele[0].getAttribute("src");
      }
    }

    if (getUrlVars()["type"]) $('#hp-citizen-title').text(titleCase(getUrlVars()["type"]) + " Pay Scale");




    if (type === "view" || type === "update") {
      getCommonMasterById("hr-employee", "payscale", id, function (err, res) {
        if (res) {
          var payscaleSet = res["PayscaleHeader"][0];
          if (!payscaleSet.payscaleDetails[0].increment) payscaleSet.payscaleDetails[0].increment = ""
          _this.setState({
            payscaleSet
          })
        }
      })
    }
  }

  componentDidUpdate() {

    if (getUrlVars()["type"] === "view")
    $('input,select,textarea').prop("disabled", true);

  }

  close() {
    // widow.close();
    open(location, '_self').close();
  }


  addOrUpdate(e) {
    e.preventDefault();
    var tempInfo = Object.assign({}, this.state.payscaleSet), type = getUrlVars()["type"];
    var body = {
      "RequestInfo": requestInfo,
      "PayscaleHeader": tempInfo
    }, _this = this;

    let url = "";
    getUrlVars()["type"] == "update" ? url = "/hr-employee/payscale/_update?tenantId=" : url = "/hr-employee/payscale/_create?tenantId="

    $.ajax({
      url: baseUrl + url + tenantId,
      type: 'POST',
      dataType: 'json',
      data: JSON.stringify(body),
      contentType: 'application/json',
      headers: {
        'auth-token': authToken
      },
      success: function (res) {

        if (getUrlVars()["type"] === "update") {
          showSuccess("PayScale Update successfully.");
          window.location.href = `app/hr/employee/payscale.html`;
        }
        else {
          showSuccess("PayScale Created successfully.");
          _this.setState({
            payscaleSet: {
              "paycommission": "",
              "payscale": "",
              "amountFrom": "",
              "amountTo": "",
              "tenantId": tenantId,
              "payscaleDetails": [
                {
                  "basicFrom": "",
                  "basicTo": "",
                  "increment": "",
                  "tenantId": tenantId
                }
              ]
            }
          })
        }
      },
      error: function (err) {

        let error;
        if(err.responseJSON.Error.fields){

          let values = Object.values(err.responseJSON.Error.fields);
          values.forEach(function(value){
            console.log(value);

           error =  value + "\n" 
          })
        }
        error? error = error: error = "Something went wrong. Please contact administrator"
        showError(error);
      }
    });

  }


  render() {

    let { handleChange, addOrUpdate, handleDetailsChange, deletePayscaleDetails, addPayScaleDetails } = this;
    let mode = getUrlVars()["type"];
    let { paycommission, payscale, amountFrom, amountTo, payscaleDetails } = this.state.payscaleSet;

    const showActionButton = function () {
      if ((!mode) || mode === "update") {
        return (<button type="submit" className="btn btn-submit">{mode ? "Update" : "Add"}</button>);
      }
    };


    const renderPlusMinus = function (ind) {
      if (mode !== "view")
        return (
          <td>
            <button type="button" className="btn btn-default btn-action" onClick={() => addPayScaleDetails(payscaleDetails.length)}><span className="glyphicon glyphicon-plus" aria-hidden="true"></span></button>
            &nbsp;<button type="button" className="btn btn-default btn-action" onClick={() => deletePayscaleDetails(ind)}><span className="glyphicon glyphicon-minus" aria-hidden="true"></span></button>
          </td>
        )
    }
    const renderPayScaleDetails = function () {

      if (payscaleDetails && payscaleDetails.length) {

        return payscaleDetails.map(function (payscaleDetail, ind) {
          return (

            <tr key={ind} id={ind} >
              <td>
                <input type="number" name="basicFrom" id="basicFrom" value={payscaleDetail["basicFrom"]}
                  onChange={(e) => { handleDetailsChange(e, ind, "basicFrom") }} required disabled />
              </td>
              <td>
                <input type="number" name="basicTo" id="basicTo" value={payscaleDetail["basicTo"]}
                  onChange={(e) => { handleDetailsChange(e, ind, "basicTo") }} min={payscaleDetail["basicFrom"]} max={amountTo} required />
              </td>
              <td>
                <input type="number" name="increment" id="increment" value={payscaleDetail["increment"]}
                  onChange={(e) => { handleDetailsChange(e, ind, "increment") }} max={amountTo - amountFrom} min="0" required />
              </td>

              {renderPlusMinus(ind)}

            </tr>

          );
        })
      }
    }

    const renderActionButton = function () {
      if (mode !== "view")
        return (<th>Action</th>)
    }


    return (<div>
      <h3>{getUrlVars()["type"] ? titleCase(getUrlVars()["type"]) : "Create"} Pay Scale</h3>
      <form onSubmit={(e) => { addOrUpdate(e, mode) }}>
        <fieldset>
          <div className="form-section">
            <div className="form-section-inner">

              <div className="row">
                <div className="col-sm-6">
                  <div className="row">
                    <div className="col-sm-6 label-text">
                      <label htmlFor="Grade">Pay commission <span>* </span></label>
                    </div>
                    <div className="col-sm-6">
                      <input type="text" name="paycommission" id="paycommission" value={paycommission}
                        onChange={(e) => { handleChange(e, "paycommission") }} required />
                    </div>
                  </div>
                </div>
                <div className="col-sm-6">
                  <div className="row">
                    <div className="col-sm-6 label-text">
                      <label htmlFor="">Pay scale<span> *</span> </label>
                    </div>
                    <div className="col-sm-6">
                      <input type="text" name="payscale" id="payscale" value={payscale}
                        onChange={(e) => { handleChange(e, "payscale") }} required />
                    </div>
                  </div>
                </div>
              </div>
              <div className="row">
                <div className="col-sm-6">
                  <div className="row">
                    <div className="col-sm-6 label-text">
                      <label htmlFor="">From <span>* </span></label>
                    </div>
                    <div className="col-sm-6 label-view-text">
                      <input type="number" name="amountFrom" id="amountFrom" min="1" value={amountFrom}
                        onChange={(e) => { handleChange(e, "amountFrom") }} required />
                    </div>
                  </div>
                </div>
                <div className="col-sm-6">
                  <div className="row">
                    <div className="col-sm-6 label-text">
                      <label htmlFor="">To <span>* </span></label>
                    </div>
                    <div className="col-sm-6 label-view-text">
                      <input type="number" name="amountTo" id="amountTo" min={amountFrom} value={amountTo}
                        onChange={(e) => { handleChange(e, "amountTo") }} required />
                    </div>
                  </div>
                </div>

              </div>
            </div>

          </div>
          <br />
          <div className="form-section">

            <div className="form-section-inner">

              <table id="fileTable" className="table table-bordered">
                <thead>
                  <tr>
                    <th>From Basic<span>* </span></th>
                    <th>To Basic<span>* </span></th>
                    <th>Increment<span>* </span></th>
                    {renderActionButton()}
                  </tr>
                </thead>
                <tbody id="tableBody">
                  {
                    renderPayScaleDetails()
                  }
                </tbody>
              </table>
            </div>
          </div>
          <br />
          <div className="text-center">
            {showActionButton()} &nbsp;&nbsp;
                            <button type="button" className="btn btn-close" onClick={(e) => { this.close() }}>Close</button>
          </div>
        </fieldset>
      </form>
    </div>
    );
  }
}

ReactDOM.render(
  <PayScaleMaster />,
  document.getElementById('root')
);
