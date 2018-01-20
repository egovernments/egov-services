class AckOfAgreement extends React.Component {

  

  close() {
    open(location, '_self').close();

  }

  componentDidMount(){
    if(window.opener && window.opener.document) {
       var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
       if(logo_ele && logo_ele[0]) {
         document.getElementsByClassName("homepage_logo")[0].src = (logo_ele[0].getAttribute("src") && logo_ele[0].getAttribute("src").indexOf("http") > -1) ? logo_ele[0].getAttribute("src") : window.location.origin + logo_ele[0].getAttribute("src");
       }
     }
  }

  
    render() {
      let {close} = this;

      return (
        <div>
          <div className="form-section">
          <form>
            <h3> Acknowledgement of {getUrlVars()["wftype"]} Agreement : </h3>
            <h3> <center><font color="ass"> {getUrlVars()["action"] ? (getUrlVars()["action"].toLowerCase() == "approve" ? "Application Approved and Forwarded to "+ decodeURIComponent(getUrlVars()["name"]) : (getUrlVars()["action"].toLowerCase() == "forward" ? (getUrlVars()["name"] ? "Application forwarded to " + decodeURIComponent(getUrlVars()["name"]) : "Application forwarded.") : (getUrlVars()["name"] ? "Application rejected and forwarded to " + decodeURIComponent(getUrlVars()["name"]) : (getUrlVars()["action"] && getUrlVars()["action"].toLowerCase() == "cancel" ? "Application cancelled." : "Application rejected.")))) : (getUrlVars()["name"] ? "Application created and forwarded to " + decodeURIComponent(getUrlVars()["name"]) : "Application created")} </font> </center></h3>
            <h4> <center><font color="ass"><strong> Acknowledgement Number:  {decodeURIComponent(getUrlVars()["ackNo"])}</strong>  </font> </center></h4>

            <div className="text-center">
                <button type="button" className="btn btn-submit" onClick={(e)=>{close()}}>Close</button>
              </div>
          </form>
          </div>
        </div>
      );
  }
}

ReactDOM.render(
  <AckOfAgreement />,
  document.getElementById('root')
);
