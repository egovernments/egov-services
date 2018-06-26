class ViewDCB extends React.Component {
    constructor(props) {
      super(props);
      this.state = {
        agreement: {
          allottee: {
              id: "",
              name: "",
              permanentAddress: "",
              mobileNumber: "",
              aadhaarNumber: "",
              pan: "",
              emailId: "",
              userName: "",
              password: "",
              active: "",
              type: "",
              gender: "",
              tenantId: tenantId,
          },
          asset: {
              id: "",
              assetCategory: {
                  id: "",
                  name: "",
                  code: ""
              },
              name: "",
              code: "",
              locationDetails: {
                  locality: "",
                  zone: "",
                  revenueWard: "",
                  block: "",
                  street: "",
                  electionWard: "",
                  doorNo: "",
                  pinCode: ""
              }
          }
        },
        demandDetails:{}
      }

      this.setInitialState = this.setInitialState.bind(this);
      this.closeWindow=this.closeWindow.bind(this);
    }

    setInitialState(initState) {
      this.setState(initState);
    }

    closeWindow ()
    {
        open(location, '_self').close();
    }
    componentDidMount() {

      if (window.opener && window.opener.document) {
        var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
        if (logo_ele && logo_ele[0]) {
          document.getElementsByClassName("homepage_logo")[0].src = window.location.origin + logo_ele[0].getAttribute("src");
        }
      }
      var _this = this;
      var agreementNumber=getUrlVars()["agreementNumber"];
      var acknowledgementNumber = getUrlVars()["acknowledgementNumber"];
      var status = getUrlVars()["status"];
      var agreement = commonApiPost("lams-services",
        "agreements/dcb",
        "_view", {
          agreementNumber,acknowledgementNumber,status,
          tenantId
        }).responseJSON["Agreements"][0] || {};
      var demandDetails = agreement.legacyDemands[0].demandDetails;


      var demands = [];
      var rentDemands = [];
      var penaltyDemands = [];
      var sgstDemands = [];
      var cgstDemands = [];
      var serviceTaxDemands = [];
      var advanTaxDemands = [];
      var goodwillTaxDemands = [];
      var gstOnAdvanceDemands = [];
      var gstOnGoodWillDemands = [];

      demandDetails.forEach((demand) => {

        if (demand.taxReasonCode.toLowerCase() === "rent")
          rentDemands.push(demand);
        else if (demand.taxReasonCode.toLowerCase() === "penalty")
          penaltyDemands.push(demand);
        else if (demand.taxReasonCode.toLowerCase() === "state_gst")
          cgstDemands.push(demand);
        else if (demand.taxReasonCode.toLowerCase() === "central_gst")
          sgstDemands.push(demand);
        else if (demand.taxReasonCode.toLowerCase() === "service_tax")
          serviceTaxDemands.push(demand);
        else if (demand.taxReasonCode.toLowerCase() === "advance_tax")
          advanTaxDemands.push(demand);
        else if (demand.taxReasonCode.toLowerCase() === "goodwill_amount")
          goodwillTaxDemands.push(demand);
        else if (demand.taxReasonCode.toLowerCase() === "adv_cgst" || demand.taxReasonCode.toLowerCase() === "adv_sgst")
          gstOnAdvanceDemands.push(demand);
        else if (demand.taxReasonCode.toLowerCase() === "gw_cgst" || demand.taxReasonCode.toLowerCase() === "gw_sgst")
          gstOnGoodWillDemands.push(demand);

      });

      var index = 0;

      demands.splice(index, 0, advanTaxDemands[0]);
      index++;
      if(gstOnAdvanceDemands.length>0){
      demands.splice(index, 0, gstOnAdvanceDemands[0]);
      index++;
      demands.splice(index, 0, gstOnAdvanceDemands[1]);
      index++;
      }
      if(goodwillTaxDemands.length>0){
      demands.splice(index, 0, goodwillTaxDemands[0]);
      index++;
      }
      if(goodwillTaxDemands.length>0 && gstOnGoodWillDemands.length>0){
      demands.splice(index, 0, gstOnGoodWillDemands[0]);
      index++;
      demands.splice(index, 0, gstOnGoodWillDemands[1]);
      index++;
    }

      for (var i = 0; i < rentDemands.length; i++) {
        demands.splice(index, 0, rentDemands[i]);
        index++;
        penaltyDemands.forEach((pDemand) => {
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
        cgstDemands.forEach((pDemand) => {
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
      }

      this.setState({
        ...this.state,
        agreement: agreement,
        demandDetails:demands

      });
      }
    render() {
        var _this = this;
        let {agreement,demandDetails} = this.state;
        const renderAgreementDetails = function () {
              return (
                <div className="form-section" id="agreementDetailsBlock">
                  <h3>Agreement Details </h3>
                  <div className="form-section-inner">
                    <div className="row">
                      <div className="col-sm-6">
                        <div className="row">
                          <div className="col-sm-6 label-text">
                            <label htmlFor="agreementNumber"> Agreement Number :</label>
                          </div>
                          <div className="col-sm-6 label-view-text">
                            <label id="agreementNumber" name="agreementNumber">
                              {agreement.agreementNumber ? agreement.agreementNumber : "N/A"}
                            </label>
                          </div>
                        </div>
                      </div>
                      <div className="col-sm-6">
                        <div className="row">
                          <div className="col-sm-6 label-text">
                            <label htmlFor="acknowledgementNumber">Acknowledgement Number:</label>
                          </div>
                          <div className="col-sm-6 label-view-text">
                            <label id="acknowledgementNumber" name="acknowledgementNumber">
                              {agreement.acknowledgementNumber ? agreement.acknowledgementNumber : "N/A"}
                            </label>
                          </div>
                        </div>
                      </div>
                    </div>
                    <div className="row">
                      <div className="col-sm-6">
                        <div className="row">
                          <div className="col-sm-6 label-text">
                            <label htmlFor="commencementDate"> Allotment Date :</label>
                          </div>
                          <div className="col-sm-6 label-view-text">
                            <label id="commencementDate" name="commencementDate">
                              {agreement.commencementDate ? agreement.commencementDate : "N/A"}
                            </label>
                          </div>
                        </div>
                      </div>
                      <div className="col-sm-6">
                        <div className="row">
                          <div className="col-sm-6 label-text">
                            <label htmlFor="expiryDate">Expiry Date :</label>
                          </div>
                          <div className="col-sm-6 label-view-text">
                            <label id="expiryDate" name="expiryDate">
                              {agreement.expiryDate ? agreement.expiryDate : "N/A"}
                            </label>
                          </div>
                        </div>
                      </div>
                    </div>
                    <div className="row">
                      <div className="col-sm-6">
                        <div className="row">
                          <div className="col-sm-6 label-text">
                            <label htmlFor="commencementDate"> Agreement Type :</label>
                          </div>
                          <div className="col-sm-6 label-view-text">
                            <label id="action" name="action">
                              {agreement.action ? agreement.action : "N/A"}
                            </label>
                          </div>
                        </div>
                      </div>
                      <div className="col-sm-6">
                        <div className="row">
                          <div className="col-sm-6 label-text">
                            <label htmlFor="status">Current Status :</label>
                          </div>
                          <div className="col-sm-6 label-view-text">
                            <label id="expiryDate" name="expiryDate">
                              {agreement.status ? agreement.status : "N/A"}
                            </label>
                          </div>
                        </div>
                      </div>
                    </div>
                    <div className="row">
                      <div className="col-sm-6">
                        <div className="row">
                          <div className="col-sm-6 label-text">
                            <label htmlFor="assetName">Asset Name :</label>
                          </div>
                          <div className="col-sm-6 label-view-text">
                            <label id="assetName" name="assetName">
                              {agreement.asset.name ? agreement.asset.name : "N/A"}
                            </label>
                          </div>
                        </div>
                      </div>
                      <div className="col-sm-6">
                        <div className="row">
                          <div className="col-sm-6 label-text">
                            <label htmlFor="assetType">Asset Type:</label>
                          </div>
                          <div className="col-sm-6 label-view-text">
                            <label id="assetType" name="assetType">
                              {agreement.asset.assetCategory.name ? agreement.asset.assetCategory.name : "N/A"}
                            </label>
                          </div>
                        </div>
                      </div>
                    </div>
                    <div className="row">
                      <div className="col-sm-6">
                        <div className="row">
                          <div className="col-sm-6 label-text">
                            <label htmlFor="allotteeName"> Allottee Name :</label>
                          </div>
                          <div className="col-sm-6 label-view-text">
                            <label id="allotteeName" name="allotteeName">
                              {agreement.allottee.name ? agreement.allottee.name : "N/A"}
                            </label>
                          </div>
                        </div>
                      </div>
                      <div className="col-sm-6">
                        <div className="row">
                          <div className="col-sm-6 label-text">
                            <label htmlFor="mobileNumber">Mobile Number:</label>
                          </div>
                          <div className="col-sm-6 label-view-text">
                            <label id="mobileNumber" name="mobileNumber">
                              {agreement.allottee.mobileNumber ? agreement.allottee.mobileNumber : "N/A"}
                            </label>
                          </div>
                        </div>
                      </div>
                    </div>
                    <div className="row">
                      <div className="col-sm-6">
                        <div className="row">
                          <div className="col-sm-6 label-text">
                            <label htmlFor="rent">Rent :</label>
                          </div>
                          <div className="col-sm-6 label-view-text">
                            <label id="rent" name="rent">
                              {agreement.rent ? agreement.rent : "N/A"}
                            </label>
                          </div>
                        </div>
                      </div>
                      <div className="col-sm-6">
                        <div className="row">
                          <div className="col-sm-6 label-text">
                            <label htmlFor="securityDeposit">Advance Collection:</label>
                          </div>
                          <div className="col-sm-6 label-view-text">
                            <label id="securityDeposit" name="securityDeposit">
                              {agreement.securityDeposit ? agreement.securityDeposit : "N/A"}
                            </label>
                          </div>
                        </div>
                      </div>
                    </div>
                    <div className="row">
                      <div className="col-sm-6">
                        <div className="row">
                          <div className="col-sm-6 label-text">
                            <label htmlFor="paymentCycle">PaymentCycle :</label>
                          </div>
                          <div className="col-sm-6 label-view-text">
                            <label id="paymentCycle" name="paymentCycle">
                              {agreement.paymentCycle ? agreement.paymentCycle : "N/A"}
                            </label>
                          </div>
                        </div>
                      </div>
                      <div className="col-sm-6">
                        <div className="row">
                          <div className="col-sm-6 label-text">
                            <label htmlFor="natureOfAllotment">Allotment Type :</label>
                          </div>
                          <div className="col-sm-6 label-view-text">
                            <label id="natureOfAllotment" name="natureOfAllotment">
                              {agreement.natureOfAllotment ? agreement.natureOfAllotment : "N/A"}
                            </label>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              );

            }

        const showTable=function()
        {

              return (
                <div className="form-section" id="demandDetailsBlock">
                    <h3>Demand Details </h3>
                <table id="demandTable" className="table table-bordered">
                    <thead>
                    <tr>
                        <th>Installment </th>
                        <th colSpan='3'>Demand</th>
                        <th colSpan='3'>Collection</th>
                        <th colSpan='3'>Balance</th>

                    </tr>
                    </thead>
                    <tbody id="demandDetailsTableBody">
                        {
                            renderBody()
                        }
                    </tbody>
                    <tfoot>
                        {
                          renderTotalFooter()
                        }
                    </tfoot>
                </table>

                  </div>
              )

        }
        const renderBody=function()
        {
          if (demandDetails.length>0) {

            return demandDetails.map((item,index)=>
            {                   return (<tr key={index}>
                                      <td>{trim(item.taxPeriod)}[{item.taxReason}] </td>
                                      <td colSpan='3'>{item.taxAmount}</td>
                                      <td colSpan='3'>{item.collectionAmount}</td>
                                      <td colSpan='3'>{item.taxAmount-item.collectionAmount}</td>

                      </tr>
                  );

            })
          }

        }

        const renderTotalFooter=function()
        {
          var totalRent=0;
          var totalCollection=0;
          if (demandDetails.length>0) {
           demandDetails.forEach(function(item){
             totalRent += item.taxAmount;
             totalCollection+=item.collectionAmount;
           });
          return [<tr>
              <td> Total</td>
              <td colSpan='3'> {totalRent} </td>
              <td colSpan='3'> {totalCollection} </td>
              <td colSpan='3'> {totalRent-totalCollection} </td>
              </tr>,
           <tr>
           <td colSpan='6'></td>
           <td>Total Balance</td>
           <td>{totalRent-totalCollection}</td>
           </tr>
          ]
          }
        }

function trim(value){
  return(value.replace("LAMS_",""))
}
    return(
      <div>
      <h3> </h3>
      <form  >
      <fieldset>

              {renderAgreementDetails()}
               {showTable()}
              <br/>
                  <div className="text-center">
                    <button type="button" className="btn btn-submit" onClick={(e)=>{this.closeWindow()}}>Close</button>
                  </div>

      </fieldset>
      </form>
      </div>
    );
  }

}
ReactDOM.render(
  <ViewDCB />,
  document.getElementById('root')
);
