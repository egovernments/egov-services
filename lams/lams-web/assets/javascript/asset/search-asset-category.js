




class CreateAsset extends React.Component {

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
                          <label for="assetName"> Name  <span> * </span></label>
                      </div>
                      <div className="col-sm-6">
                          <input id="assetName" name="assetName" type="text" required/>
                      </div>
                  </div>
              </div>

              <div className="col-sm-6">
                  <div className="row">
                      <div className="col-sm-6 label-text">
                          <label for="shopAreaUom">Asset Category Type <span> *</span> </label>
                      </div>
                      <div className="col-sm-6">
                          <div className="styled-select">
                              <select id="shopAreaUom" name="shopAreaUom" required>
                              <option>Land</option>
                              <option>Movable</option>
                              <option>Immovable</option>
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
              <div className="clearfix"></div>

              <div className="land-table">
                  <table className="table table-bordered">
                      <thead>
                          <tr>
                              <th>Code</th>
                              <th>Name</th>
                              <th>Asset Category Type</th>
                              <th>Parent Category</th>
                              <th>Unit Of Measurment</th>
                              <th>Action</th>
                          </tr>
                      </thead>

                      <tbody>
                        <tr>
                          <td> 001 </td>
                          <td> Land</td>
                        <td> Land </td>
                        <td>  Land</td>
                        <td> Sq. Ft</td>

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
  <CreateAsset />,
  document.getElementById('root')
);




//
