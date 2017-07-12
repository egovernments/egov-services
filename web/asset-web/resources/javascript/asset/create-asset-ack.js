class CreateAsset extends React.Component {
    close() {
      // widow.close();
      open(location, '_self').close();
    }

    componentDidMount() {
      if(window.opener && window.opener.document) {
        var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
        if(logo_ele && logo_ele[0]) {
          document.getElementsByClassName("homepage_logo")[0].src = (logo_ele[0].getAttribute("src") && logo_ele[0].getAttribute("src").indexOf("http") > -1) ? logo_ele[0].getAttribute("src") : window.location.origin + logo_ele[0].getAttribute("src");
        }
      }
    }

    render() {
      const renderCode = function () {
        if(getUrlVars()["code"]) {
          return (
            <span> and Asset Code <b style={{"font-weight": "bold"}}>{getUrlVars()["code"]}</b></span>
          )
        }
      }

      return (
          <div>
            <div className="form-section">
                <form>
                  <h3> Acknowledgement for Asset </h3>
                  <h3>
                      <center><font color="ass"><b style={{"font-weight": "bold"}}> Asset {getUrlVars()["type"] ? "Category" : ""} {getUrlVars()["value"] == "update" ? "Updated" : (getUrlVars()["value"] == "revaluate" ? "Revaluated" : (getUrlVars()["value"] == "sold" ? "Sold" : (getUrlVars()["value"] == "disposed" ? "Disposed" : "Created")))}</b> </font> </center>
                  </h3>
                  <h3>
                      <center><font color="ass"> Successfully  {getUrlVars()["value"] == "update" ? "Updated" : (getUrlVars()["value"] == "revaluate" ? "Revaluated" : (getUrlVars()["value"] == "sold" ? "Sold" : (getUrlVars()["value"] == "disposed" ? "Disposed" : "Created")))} an Asset {getUrlVars()["type"] ? "Category" : ""} with Asset {getUrlVars()["type"] ? "Category" : ""} Name <strong style={{'fontWeight':700}}>
                      { getUrlVars()["name"] ? decodeURIComponent(getUrlVars()["name"]) : ""}</strong> {renderCode()}  </font> </center>
                  </h3>
                  <div className="text-center">
                      <button type="button" className="btn btn-close" onClick={(e)=>{this.close()}}>Close</button>
                  </div>
                </form>
            </div>
          </div>
      );
  }
}


ReactDOM.render(
  <CreateAsset />,
  document.getElementById('root')
);
