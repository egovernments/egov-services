class CreateAgreement extends React.Component {
  constructor(props) {
    super(props);
    this.close = this.close.bind(this);
  }

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
              <h3> {typeof(getUrlVars()["from"]) == "undefined" ? "Acknowledgement of Create" : "Acknowledgement of Data Entry"}  Agreement </h3>
              <h3> <center>
                <font color="ass">
                  {getUrlVars()["action"] ? (getUrlVars()["action"].toLowerCase() == "approve" ? "Agreement Approved." : getUrlVars()["action"].toLowerCase() == "modify" ? "Agreement Updated." : (getUrlVars()["action"].toLowerCase() == "forward" ? (getUrlVars()["name"] ? "Agreement forwarded to " + decodeURIComponent(getUrlVars()["name"]) : "Agreement forwarded.") : (getUrlVars()["name"] ? "Agreement rejected and forwarded to " + decodeURIComponent(getUrlVars()["name"]) : (getUrlVars()["action"] && getUrlVars()["action"].toLowerCase() == "cancel" ? "Agreement cancelled." : "Agreement rejected.")))) : (getUrlVars()["name"] ? "Agreement created and forwarded to " + decodeURIComponent(getUrlVars()["name"]) : "Successfully Created Agreement for Asset  "+decodeURIComponent(getUrlVars()["asset"]))}
                </font> </center></h3>
              <h4> <center><font color="ass"><strong> {getUrlVars()["ackNo"] ? (getUrlVars()["from"] == "dataEntry" ? "Agreement Number: " : getUrlVars()["action"] && getUrlVars()["action"].toLowerCase() == "approve" ? "Agreement Number: " : "Acknowledgement number: ") + decodeURIComponent(getUrlVars()["ackNo"]) : ""}</strong>  </font> </center></h4>
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
  <CreateAgreement />,
  document.getElementById('root')
);
