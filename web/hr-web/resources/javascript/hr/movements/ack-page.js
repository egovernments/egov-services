class AckPage extends React.Component {
    close() {
      // widow.close();
      open(location, '_self').close();
    }
    componentDidMount(){
      if(window.opener && window.opener.document) {
         var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
         if(logo_ele && logo_ele[0]) {
           document.getElementsByClassName("homepage_logo")[0].src = window.location.origin + logo_ele[0].getAttribute("src");
         }
       }
    }
    render() {


      const renderType=function(type) {

        switch(type) {

          case "TransferReject":  return(<h4>
                      <center><font color="black"> Transfer Application has been Rejected  and forwarded to <strong style={{'fontWeight':700}}> {getUrlVars()["owner"] ?  decodeURIComponent(getUrlVars()["owner"]) : ""} </strong> </font> </center>
                  </h4>)
          break;
          case "TransferApprove": return(<h4>
                      <center><font color="black"> Transfer Application has been Approved for <strong style={{'fontWeight':700}}> {getUrlVars()["employee"] ?  decodeURIComponent(getUrlVars()["employee"]) : ""} </strong> </font> </center>
                  </h4>)
          break;
          case "TransferApply": return(<h4>
                      <center><font color="black"> Transfer Application has been Applied successfully and forwarded to <strong style={{'fontWeight':700}}> {getUrlVars()["owner"] ?  decodeURIComponent(getUrlVars()["owner"]) : ""} </strong> </font> </center>
                  </h4>)
          break;
          case "TransferSubmit":  return(<h4>
                      <center><font color="black"> Transfer Application has been Submitted successfully and forwarded to <strong style={{'fontWeight':700}}> {getUrlVars()["owner"] ?  decodeURIComponent(getUrlVars()["owner"]) : ""} </strong> </font> </center>
                  </h4>)
          break;
          case "TransferCancel":  return(<h4>
                        <center><font color="black"> Transfer Application has been Cancelled </font> </center>
                    </h4>)
          break;

          case "PromotionReject":  return(<h4>
                        <center><font color="black"> Promotion Application has been Rejected  and forwarded to <strong style={{'fontWeight':700}}> {getUrlVars()["owner"] ?  decodeURIComponent(getUrlVars()["owner"]) : ""} </strong> </font> </center>
                    </h4>)
          break;
          case "PromotionApprove": return(<h4>
                        <center><font color="black"> Promotion Application has been Approved </font> </center>
                    </h4>)
          break;
          case "PromotionApply": return(<h4>
                        <center><font color="black"> Promotion Application has been Applied successfully and forwarded to <strong style={{'fontWeight':700}}> {getUrlVars()["owner"] ?  decodeURIComponent(getUrlVars()["owner"]) : ""} </strong> </font> </center>
                    </h4>)
          break;
          case "PromotionSubmit":  return(<h4>
                        <center><font color="black"> Promotion Application has been Submitted successfully and forwarded to <strong style={{'fontWeight':700}}> {getUrlVars()["owner"] ?  decodeURIComponent(getUrlVars()["owner"]) : ""} </strong> </font> </center>
                    </h4>)
          break;
          case "PromotionCancel":  return(<h4>
                        <center><font color="black"> Promotion Application has been Cancelled </font> </center>
                    </h4>)
          break;

          default:  return(<h4>
                      <center><font color="black">Application has been Cancelled </font> </center>
                  </h4>)

}

      };

      return (
          <div>
            {renderType(getUrlVars()["type"])}
            <div className="text-center">
                      <button type="button" className="btn btn-submit" onClick={(e)=>{this.close()}}>Close</button>
                  </div>
          </div>
      );
  }
}


ReactDOM.render(
  <AckPage />,
  document.getElementById('root')
);
