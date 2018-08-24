class PartialPayment extends React.Component {
    
    constructor(props) {
        super(props);
        this.state = {
            partialPayment: "",
            selectedDemands:[]
        }
        this.handleChange = this.handleChange.bind(this);
        this.getSelectedDemands = this.getSelectedDemands.bind(this);
        this.proceed = this.proceed.bind(this);

    }

    componentWillMount() {
        var data = commonApiPost("lams-services", "payment", "_partialpaymentview", {
            agreementNumber: getUrlVars()["agreementNumber"],
            //agreementNumber: "LA-18-10160487",
            tenantId
        });


        var demandDetails = data.responseJSON.legacyDemands[0].demandDetails;


        var demands = [];
        var rentDemands = [];
        var penaltyDemands = [];
        var sgstDemands = [];
        var cgstDemands = [];
        var serviceTaxDemands = [];
        var advanceTaxDemands = [];
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
            advanceTaxDemands.push(demand);
          else if (demand.taxReasonCode.toLowerCase() === "goodwill_amount")
            goodwillTaxDemands.push(demand);
          else if (demand.taxReasonCode.toLowerCase() === "adv_cgst" || demand.taxReasonCode.toLowerCase() === "adv_sgst")
            gstOnAdvanceDemands.push(demand);
          else if (demand.taxReasonCode.toLowerCase() === "gw_cgst" || demand.taxReasonCode.toLowerCase() === "gw_sgst")
            gstOnGoodWillDemands.push(demand);
  
        });
  
        var index = 0;
        if(advanceTaxDemands.length>0){
        demands.splice(index, 0, advanceTaxDemands[0]);
        index++;
         }
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

        data.responseJSON.legacyDemands[0].demandDetails = demands;


        this.setState({
          ...this.state,
          partialPayment: data.responseJSON,  
        });
  
        console.log("data", data.responseJSON);
    }

    close() {
        // widow.close();
        open(location, "_self").close();
    }

    handleChange(index, reason ,taxPeriod) {
        let length = document.getElementsByClassName("checkbox").length;

        for (let j = 0; j < length; j++) 
        document.getElementById("checkbox_"+j).checked = false;

        for (let i = 0; i <= index; i++) 
        document.getElementById("checkbox_"+i).checked = true;
        
        let demandDetails = this.state.partialPayment.legacyDemands[0].demandDetails;

        let selectedDemands = this.getSelectedDemands(demandDetails, reason ,taxPeriod);

        this.setState({
            ...this.state,
            selectedDemands  
          });
    }

    getSelectedDemands(demandDetails, reason ,taxPeriod){
        let selectedDemands = [];
        //selecting only advanced tax
        if(reason === 'advanced'){
            demandDetails.forEach((demand)=>{
                if (demand.taxReasonCode.toLowerCase() === "adv_cgst" || 
                demand.taxReasonCode.toLowerCase() === "adv_sgst" ||
                demand.taxReasonCode.toLowerCase() === "advance_tax")
                selectedDemands.push(demand);
            })
        }

        //selecting advanced tax and goodwill tax
        if(reason === 'goodwill'){
            demandDetails.forEach((demand)=>{
                if (demand.taxReasonCode.toLowerCase() === "adv_cgst" || 
                demand.taxReasonCode.toLowerCase() === "adv_sgst" ||
                demand.taxReasonCode.toLowerCase() === "advance_tax"||
                demand.taxReasonCode.toLowerCase() === "goodwill_amount" ||
                demand.taxReasonCode.toLowerCase() === "gw_cgst"||
                demand.taxReasonCode.toLowerCase() === "gw_sgst")
                selectedDemands.push(demand);
            })
        }

        //selecting advanced tax, goodwill tax and rents until taxperiod
        if(reason === 'rent'){
            let index = 0;
            for (let i = demandDetails.length -1 ; i >= 0 ; i--) {
                if(demandDetails[i].taxPeriod === taxPeriod){
                    index = i
                    break;
                }
            }

            selectedDemands = demandDetails.slice(0,index+1)
        }        

        return selectedDemands;
    }

    proceed() {

        let body =  this.state.partialPayment;
        body.legacyDemands[0].demandDetails = this.state.selectedDemands;
        

        $.ajax({
            url: baseUrl + "/lams-services/payment/_create",
            type: 'POST',
            data: JSON.stringify({
                'RequestInfo': requestInfo,
                'Agreement':body
            }),
            contentType: 'application/json',
            headers: {
                'auth-token': authToken
            },
            success: function (data) {
                jQuery("<form>.")
             .attr({
               method: "post",
               action: "/collection/receipts/receipt-newform.action",
               target: "_self"
             })
             .append(
               jQuery("<input>").attr({
                 type: "hidden",
                 id: "collectXML",
                 name: "collectXML",
                 value: data
               })
             )
             .appendTo(document.body)
             .submit();

            },
            error: function (err) {
                if (err.responseJSON && err.responseJSON.Error && err.responseJSON.Error.message)
                    showError(err.responseJSON.Error.message);
                else
                    showError("Something went wrong. Please contact Administrator");
            }

        })

        
    }

    render() {
        let demandDetails = this.state.partialPayment.legacyDemands[0].demandDetails;
        console.log("data",demandDetails);
        let objectItem = new Object();
        demandDetails.forEach((data, index) => {
      
            //filtering rents taxes
            if(data.taxReasonCode.toLowerCase() === "rent" || 
                data.taxReasonCode.toLowerCase() === "penalty" ||
                data.taxReasonCode.toLowerCase() === "state_gst" ||
                data.taxReasonCode.toLowerCase() === "central_gst"
                ){
                    var tempKey = 'rent' + data.taxPeriod
                    if (objectItem.hasOwnProperty(tempKey)) {
                        objectItem[tempKey][data.taxReasonCode] = data.taxAmount;
                    } else {
                        objectItem[tempKey] = new Object();
                        objectItem[tempKey].taxPeriod = data.taxPeriod;
                        objectItem[tempKey][data.taxReasonCode] = data.taxAmount;
                        objectItem[tempKey]['reason'] = "rent";

                        //console.log(objectItem); 
                    }
                }

                //filtering advanced taxes
                if(data.taxReasonCode.toLowerCase() === "advance_tax" || 
                data.taxReasonCode.toLowerCase() === "adv_cgst" ||
                data.taxReasonCode.toLowerCase() === "adv_sgst"
                ){
                    var tempKey = 'adv' + data.taxPeriod
                    if (objectItem.hasOwnProperty(tempKey)) {
                        objectItem[tempKey][data.taxReasonCode] = data.taxAmount;
                    } else {
                        objectItem[tempKey] = new Object();
                        objectItem[tempKey].taxPeriod = data.taxPeriod;
                        objectItem[tempKey][data.taxReasonCode] = data.taxAmount;
                        objectItem[tempKey]['reason'] = "advanced";

                        //console.log(objectItem); 
                    }
                }

                //filtering service taxes
                if(data.taxReasonCode.toLowerCase() === "goodwill_amount" || 
                data.taxReasonCode.toLowerCase() === "gw_cgst" ||
                data.taxReasonCode.toLowerCase() === "gw_sgst"
                ){
                    var tempKey = 'gw' + data.taxPeriod
                    if (objectItem.hasOwnProperty(tempKey)) {
                        objectItem[tempKey][data.taxReasonCode] = data.taxAmount;
                    } else {
                        objectItem[tempKey] = new Object();
                        objectItem[tempKey].taxPeriod = data.taxPeriod;
                        objectItem[tempKey][data.taxReasonCode] = data.taxAmount;
                        objectItem[tempKey]['reason'] = "goodwill";

                        //console.log(objectItem); 
                    }
                }

        });
        let partialPayment = Object.values(objectItem);
        console.log("objectItem" , partialPayment)
        let tableBody = (
            partialPayment.map((data, index) => {
                //console.log("data from map",data);
                if(data.reason === 'rent'){
                    return (
                        <tr>
                            <td><input type="checkbox" className="checkbox" id={"checkbox_"+index} onChange={() => this.handleChange(index,data.reason, data.taxPeriod)} /></td>
                            <td>{data.taxPeriod}</td>
                            <td>{data.RENT}</td>
                            <td>{data.penalty ? data.penalty : 0}</td>
                            <td>{data.CENTRAL_GST}</td>
                            <td>{data.STATE_GST}</td>
                            <td>{data.STATE_GST + data.CENTRAL_GST + data.RENT}</td>
                        </tr>
                    )
                }
                if(data.reason === 'goodwill'){
                    return (
                        <tr>
                            <td><input type="checkbox" className="checkbox" id={"checkbox_"+index} onChange={() => this.handleChange(index,data.reason, data.taxPeriod)} /></td>
                            <td>{"Goodwill Amount, "+data.taxPeriod.split(',')[1]}</td>
                            <td>{data["GOODWILL_AMOUNT"]}</td>
                            <td>{0}</td>
                            <td>{data['GW_CGST']}</td>
                            <td>{data['GW_SGST']}</td>
                            <td>{data['GOODWILL_AMOUNT'] + data['GW_CGST'] + data["GW_SGST"]}</td>
                        </tr>
                    )
                }
                if(data.reason === 'advanced'){
                    return (
                        <tr>
                            <td><input type="checkbox" className="checkbox" id={"checkbox_"+index} onChange={() => this.handleChange(index,data.reason, data.taxPeriod)} /></td>
                            <td>{"Advanced Tax, "+data.taxPeriod.split(',')[1]}</td>
                            <td>{data["ADVANCE_TAX"]}</td>
                            <td>{0}</td>
                            <td>{data['ADV_CGST']}</td>
                            <td>{data['ADV_SGST']}</td>
                            <td>{data['ADVANCE_TAX'] + data['ADV_CGST'] + data["ADV_SGST"]}</td>
                        </tr>
                    )
                }
            })
        )
        return (
            <div>
                <table className="table table-bordered">
                    <thead>
                        <tr>
                            <th>Select</th>
                            <th>Month & Year</th>
                            <th>Amount</th>
                            <th>Penalty</th>
                            <th>CGST</th>
                            <th>SGST</th>
                            <th>Total</th>
                        </tr>
                    </thead>
                    <tbody>
                        {tableBody}
                    </tbody>
                </table>
                <div className="text-center">
                    <button type="submit" className="btn btn-submit" onClick={this.proceed}>
                        Proceed
                    </button>
                    &nbsp;&nbsp;
                    <button
                        type="button"
                        className="btn btn-close"
                        onClick={e => {
                            this.close();
                        }}
                    >
                        Close
                    </button>
                </div>
            </div>
        )
    }
}

//ReactDom.render(<PartialPayment/>,document.getElementById('root'));
ReactDOM.render(
    <PartialPayment />,
    document.getElementById('root')
);