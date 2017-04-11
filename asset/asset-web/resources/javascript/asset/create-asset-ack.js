class CreateAsset extends React.Component {

    render() {

    return (
                  <div>
                  <div className="form-section">
                <form>
                    <h3> Acknowledgement of Asset </h3>
                    <h4> <center><font color="ass"> Acknowledgement  Created </font> </center></h4>
                    <h3> <center><font color="ass"><strong> {getUrlVars()["ackNo"]}</strong>  </font> </center></h3>


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
  <CreateAsset />,
  document.getElementById('root')
);
