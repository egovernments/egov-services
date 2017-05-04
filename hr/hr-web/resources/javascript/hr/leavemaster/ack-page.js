class AckPage extends React.Component {
    close() {
      // widow.close();
      open(location, '_self').close();
    }
    render() {


      const renderType=function(type) {

        switch(type) {

          case "Reject":  return(<h4>
                      <center><font color="black"> Leave Application with Application Number:  <strong style={{'fontWeight':700}}> {getUrlVars()["applicationNumber"]} </strong> has been Rejected  and forwarded to <strong style={{'fontWeight':700}}> {getUrlVars()["owner"] ?  decodeURIComponent(getUrlVars()["owner"]) : ""} </strong> </font> </center>
                  </h4>)
          break;
          case "Approve": return(<h4>
                      <center><font color="black"> Leave Application with Application Number: <strong style={{'fontWeight':700}}> {getUrlVars()["applicationNumber"]}</strong> has been Approved successfully and forwarded to <strong style={{'fontWeight':700}}> {getUrlVars()["owner"] ?  decodeURIComponent(getUrlVars()["owner"]) : ""} </strong>  </font> </center>
                  </h4>)
          break;
          case "Apply": return(<h4>
                      <center><font color="black"> Leave Application with Application Number: <strong style={{'fontWeight':700}}> {getUrlVars()["applicationNumber"]} </strong> has been Applied successfully and forwarded to <strong style={{'fontWeight':700}}> {getUrlVars()["owner"] ?  decodeURIComponent(getUrlVars()["owner"]) : ""} </strong> </font> </center>
                  </h4>)
          break;
          default:  return(<h4>
                      <center><font color="black"> Leave Application with Application Number: <strong style={{'fontWeight':700}}> {getUrlVars()["applicationNumber"]} </strong> has been Submitted successfully and forwarded to <strong style={{'fontWeight':700}}> {getUrlVars()["owner"] ?  decodeURIComponent(getUrlVars()["owner"]) : ""} </strong> </font> </center>
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
