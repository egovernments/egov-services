class PartialPayment extends React.Component{
    constructor(props){
        super(props);
        this.state={
            partialPayment:""
        }
    }
    componentDidMount(){
        var data =  commonApiPost("lams-services","payment", "_partialpaymentview",{
                    agreementNumber: getUrlVars()["agreementNumber"],
                    //agreementNumber: "LA-18-10160487",
                    tenantId
                });
        this.setState({partialPayment:data});
        console.log("data",data);
    }
    close(){
        // widow.close();
        open(location, "_self").close();
    }  
    handleChange(index,taxPeriod,Amount,penalty,cgst,sgst,total){
        let demandDetails=[];
        demandDetails.push({
            "taxPeriod":taxPeriod,
            "Amount":Amount,
            "Penality":penalty,
            "cgst":cgst,
            "sgst":sgst,
            "total":total
        })
        console.log(demandDetails);
    }
    proceed(){
        window.open(
            "app/dataentry/edit-demand.html?" +
              (number
                ? "agreementNumber=" + number
                : "acknowledgementNumber=" + acknowledgementNumber) +
              "&assetId=" +
              id,
            "fs",
            "fullscreen=yes"
          );
    }      
    render(){
        let demandDetails = this.state.partialPayment.Agreement.legacyDemands[0].demandDetails;
        //console.log("data",demandDetails);
        let objectItem = new Object();
        demandDetails.forEach((data,index)=>{
            if(objectItem.hasOwnProperty(data.taxPeriod)){
                objectItem[data.taxPeriod][data.taxReason] = data.taxAmount;
            }else{
                objectItem[data.taxPeriod] = new Object();
                objectItem[data.taxPeriod].taxPeriod = data.taxPeriod;
                objectItem[data.taxPeriod][data.taxReason] = data.taxAmount;
                //console.log(objectItem); 
            }
        });
        let partialPayment = Object.values(objectItem);
        let tableBody=(
            partialPayment.map((data,index)=>{
            //console.log("data from map",data);
            return(
                <tr>
                    <td><input type="checkbox" onChange={()=>this.handleChange(
                        index,
                       ( 
                           data.hasOwnProperty("Advance Tax") ?
                            "Advance Tax, " + data.taxPeriod.split(",")[1] :
                            data.taxPeriod
                        ),
                       (    
                           data.hasOwnProperty("Advance Tax") ? data["Advance Tax"] : data["Rent"]
                        ),
                        data.penalty,
                        data.CGST,
                        data.SGST
                        ,(data.SGST + data.CGST + (data.hasOwnProperty("Advance Tax") ? data["Advance Tax"] : data["Rent"]))
                    )}/></td>
                    {
                        data.hasOwnProperty("Advance Tax") ?
                            <td>{"Advance Tax, " + data.taxPeriod.split(",")[1]}</td> :
                            <td>{data.taxPeriod}</td>
                    }
                    <td>{data.hasOwnProperty("Advance Tax") ? data["Advance Tax"] : data["Rent"]}</td>
                    <td>{data.penalty && data.penalty? data.penalty : 0}</td>
                    <td>{data.CGST}</td>
                    <td>{data.SGST}</td>
                    <td>{data.SGST + data.CGST + (data.hasOwnProperty("Advance Tax") ? data["Advance Tax"] : data["Rent"])}</td>
                </tr>
                )
            })
        )
        return(
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
    <PartialPayment/>,
    document.getElementById('root')
);