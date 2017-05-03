class AckPage extends React.Component {
    close() {
      // widow.close();
      open(location, '_self').close();
    }
    render() {


      const renderType=function(type) {

        switch(type) {

          case "Reject":  return(<h3>
                      <center><font color="orange"> Leave Application with Application Number: {getUrlVars()["applicationNumber"]} has been Rejected  and forwarded to   </font> </center>
                  </h3>)
          break;
          case "Approve": return(<h3>
                      <center><font color="orange"> Leave Application with Application Number:  has been Approve successfully and forwarded to   </font> </center>
                  </h3>)
          break;
          case "Apply": return(<h3>
                      <center><font color="orange"> Leave Application with Application Number:  has been Apply successfully and forwarded to   </font> </center>
                  </h3>)
          break;
          default:  "nothing"

}

      };

      return (
          <div>
            {renderType(getUrlVars()["type"])}
            <div className="text-center">
                      <button type="button" className="btn btn-close" onClick={(e)=>{this.close()}}>Close</button>
                  </div>
          </div>
      );
  }
}


ReactDOM.render(
  <AckPage />,
  document.getElementById('root')
);
