

class SearchAsset extends React.Component {

  close(){
      // widow.close();
      open(location, '_self').close();
  }


  render() {
    return (

            <div className="form-section-inner">

              <div className="row">
              <div className="col-sm-6">
              <div className="row">
              <div className="col-sm-6 label-text">
                        <label for="assetCode"> Code  </label>
              </div>
              <div className="col-sm-6">
                        <input id="assetCode" name="assetCode" type="text" required/>
              </div>
              </div>
              </div>

                  <div className="col-sm-6">
                  <div className="row">
                  <div className="col-sm-6 label-text">
                          <label for="assetName"> Name  </label>
                  </div>
                  <div className="col-sm-6">
                          <input id="assetName" name="assetName" type="text"/>
                  </div>
                  </div>
                  </div>
                  </div>

                  <div className="row">
                  <div className="col-sm-6">
                  <div className="row">
                  <div className="col-sm-6 label-text">
                          <label for="shopAreaUom">Asset Category Type  </label>
                  </div>
                  <div className="col-sm-6">
                  <div className="styled-select">
                              <select id="shopAreaUom" name="shopAreaUom">
                              <option>Land</option>
                              <option>Market</option>
                              <option>Shopping Complex</option>
                          </select>
                  </div>
                  </div>
                  </div>
                  </div>

                  <div className="col-sm-6">
                  <div className="row">
                  <div className="col-sm-6 label-text">
                          <label for="shopAreaUom">Department  </label>
                  </div>
                  <div className="col-sm-6">
                  <div className="styled-select">
                              <select id="shopAreaUom" name="shopAreaUom" >
                              <option>Accounts</option>
                              <option>Education</option>
                              <option>Revenue</option>
                          </select>
                  </div>
                  </div>
                  </div>
                  </div>
                  </div>


                  <div className="row">
                  <div className="col-sm-6">
                  <div className="row">
                  <div className="col-sm-6 label-text">
                          <label for="status">Status  </label>
                  </div>
                  <div className="col-sm-6">
                  <div className="styled-select">
                              <select id="status" name="status" multiple row="5" cols="10">
                              <option>Created</option>
                              <option>CWIP</option>
                              <option>Revaluated</option>
                          </select>

                  </div>
                  </div>
                  </div>
                  </div>
                  </div>

                  <div className="text-center">
                      <button type="button" className="btn btn-submit" id="search">Search</button>
                      <button type="submit" className="btn btn-submit">close</button>
                </div>


  <div role="tabpanel" className="tab-pane" id="assignmentDetails">
  <div className="form-section" >
          <h3 className="pull-left">Asset Search Result </h3>
  <div className="clearfix"></div>
  <div className="land-table">
              <table className="table table-bordered">
                  <thead>
                      <tr>
                          <th>Code</th>
                          <th>Name</th>
                          <th>Asset Category Type</th>
                          <th>Department</th>
                          <th>Status</th>
                          <th>Asset Details</th>
                          <th>Action</th>
                      </tr>
                  </thead>

                  <tbody>
                    <tr>
                      <td> 001 </td>
                      <td> Land</td>
                    <td> Land </td>
                    <td>  Land</td>
                    <td> Buying</td>
                    <td>400 sq.ft</td>

                    <td> <button className="btn btn-default btn-action"><span className="glyphicon glyphicon-trash"></span></button>
                      <button className="btn btn-default btn-action"><span className="glyphicon glyphicon-pencil"></span></button>
                    </td>
                    </tr>

                  </tbody>

              </table>

          </div>
          </div>
          </div>
</div>

    );
  }
}






ReactDOM.render(
  <SearchAsset />,
  document.getElementById('root')
);
