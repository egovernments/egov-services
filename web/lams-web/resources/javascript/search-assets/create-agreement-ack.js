class CreateAgreement extends React.Component {
  constructor(props) {
    super(props);
    this.close = this.close.bind(this);
    this.addEditDCB = this.addEditDCB.bind(this);
  }

  close() {
    open(location, '_self').close();
  }

  addEditDCB() {
  window.open("app/dataentry/edit-demand.html?agreementNumber="+getUrlVars()["ackNo"],"_self", "fs", "fullscreen=yes");

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
      let {close,addEditDCB} = this;

      const renderAckMessage = function(){
        if(getUrlVars()["from"] === "dataEntry" && getUrlVars()["status"]==="HISTORY"){
        return  ( [<h3> Acknowledgement of History Agreement </h3>,
          <h3> <center>
          <font color="ass"> History Agreement is saved successfully in the system.
          </font> </center></h3>
       ] );

           }else{
            return ([<h3> {typeof(getUrlVars()["from"]) == "undefined" ? "Acknowledgement of Create" : "Acknowledgement of Data Entry"}  Agreement </h3>,
              <h3> <center>
              <font color="ass">
              {getUrlVars()["action"] ? (getUrlVars()["action"].toLowerCase() == "approve" ? "Agreement Approved." : getUrlVars()["action"].toLowerCase() == "modify" ? "Agreement Updated." : (getUrlVars()["action"].toLowerCase() == "forward" ? (getUrlVars()["name"] ? "Agreement forwarded to " + decodeURIComponent(getUrlVars()["name"]) : "Agreement forwarded.") : (getUrlVars()["name"] ? "Agreement rejected and forwarded to " + decodeURIComponent(getUrlVars()["name"]) : (getUrlVars()["action"] && getUrlVars()["action"].toLowerCase() == "cancel" ? "Agreement cancelled." : "Agreement rejected.")))) : (getUrlVars()["name"] ? "Agreement created and forwarded to " + decodeURIComponent(getUrlVars()["name"]) : "Successfully Created Agreement for Asset  "+decodeURIComponent(getUrlVars()["asset"]))}
              </font> </center></h3>,
              <h4> <center><font color="ass"><strong> {getUrlVars()["ackNo"] ? (getUrlVars()["from"] == "dataEntry" ? "Agreement Number: " : getUrlVars()["action"] && getUrlVars()["action"].toLowerCase() == "approve" ? "Agreement Number: " : "Acknowledgement number: ") + decodeURIComponent(getUrlVars()["ackNo"]) : ""}</strong>  </font> </center></h4>
           ]);
        }
      }

      const renderButtons = function(){
        if(getUrlVars()["from"] === "dataEntry" && getUrlVars()["status"]==="ACTIVE"){
        return (
          <div className="row">
          <div className="text-center">
              <button type="button" className="btn btn-submit" onClick={(e)=>{addEditDCB()}}>Add/Edit DCB</button>&nbsp;&nbsp;
              <button type="button" className="btn btn-close" onClick={(e)=>{close()}}>Close</button>
          </div>
          </div>
        );
      }else{
        return (
        <div className="text-center">
            <button type="button" className="btn btn-close" onClick={(e)=>{close()}}>Close</button>
        </div>);
      }
      }

      return (
        <div>
          <div className="form-section">
            <form>
             {renderAckMessage()}
             {renderButtons()}
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
