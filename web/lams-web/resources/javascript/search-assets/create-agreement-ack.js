class CreateAgreement extends React.Component {

  componentDidMount(){
    if(window.opener && window.opener.document) {
       var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
       if(logo_ele && logo_ele[0]) {
         document.getElementsByClassName("homepage_logo")[0].src = window.location.origin + logo_ele[0].getAttribute("src");
       }
     }
  }
    render() {
      const renderMessage = function() {
        if(getUrlVars()["name"]) {
          return (
            <font color="ass"> Successfully Forward to {getUrlVars()["name"] ? decodeURIComponent(getUrlVars()["name"]) : ""} </font>
          )
        }
      }

      return (
        <div>
          <div className="form-section">
            <form>
              <h3> {typeof(getUrlVars()["from"])=="undefined"?"Acknowledgement":"Agreement Number"} of Agreement </h3>
              <h4> <center><font color="ass"> Agreement Created </font> </center></h4>
              <h3> <center><font color="ass"><strong> {getUrlVars()["ackNo"] ? decodeURIComponent(getUrlVars()["ackNo"]) : ""}</strong>  </font> </center></h3>
              <h3><center>{renderMessage()}</center></h3>
              <div className="text-center">
                  <button type="button" className="btn btn-submit" onClick={(e)=>{this.close()}}>Close</button>
              </div>
            </form>
          </div>
        </div>
      );
  }
}

ReactDOM.render(
  <CreateAgreement />,
  document.getElementById('root')
);
