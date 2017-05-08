class CreateAsset extends React.Component {
    close() {
      // widow.close();
      open(location, '_self').close();
    }

    render() {
      const renderCode = function () {
        if(getUrlVars()["code"]) {
          return (
            <span> & code <b style={{"font-weight": "bold"}}>{getUrlVars()["code"]}</b></span>
          )
        }
      }

      return (
          <div>
            <div className="form-section">
                <form>
                  <h3> Acknowledgement for Asset </h3>
                  <h4>
                      <center><font color="ass"> Asset {getUrlVars()["type"] ? decodeURIComponent(getUrlVars()["type"]) : ""} {getUrlVars()["value"] == "update" ? "Updated" : "Created"} </font> </center>
                  </h4>
                  <h3>
                      <center><font color="ass"> Asset {getUrlVars()["type"] ? decodeURIComponent(getUrlVars()["type"]) : ""} with name <strong style={{'fontWeight':700}}> {getUrlVars()["name"] ? decodeURIComponent(getUrlVars()["name"]) : ""}</strong> {renderCode()} {getUrlVars()["value"] == "update" ? "updated." : "created."}  </font> </center>
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
