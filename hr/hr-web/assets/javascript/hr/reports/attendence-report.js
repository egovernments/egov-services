
class EmployeeAttendence extends React.Component {

  close(){
      // widow.close();
      open(location, '_self').close();
  }

  render() {


    return (
                <div>
                <div className="row">
                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                            <label for="">Number Of Days In Month </label>
                          </div>
                          <div className="col-sm-6">
                              <input type="text" id="days" name="days" />
                          </div>
                      </div>
                    </div>
                    <div className="col-sm-6">
                        <div className="row">
                            <div className="col-sm-6 label-text">
                              <label for="">Number Of Working Days</label>
                            </div>
                            <div className="col-sm-6">

                                <input  type="text" id="Workingday" name="Workingday"/>

                      </div>
                      </div>
                      </div>
                      </div>


                                                    <div className="land-table">
                                                        <table className="table table-bordered">
                                                            <thead>
                                                                <tr>
                                                                    <th>Employee Code-Name</th>
                                                                    <th>Present Days</th>
                                                                    <th>Absent Days</th>
                                                                    <th>Leave Days</th>
                                                                    <th>No of Ots</th>
                                                                </tr>
                                                            </thead>
                                                            <tr>
                                                                <td>89-Kumaresh </td>
                                                                <td>20</td>
                                                                <td>3</td>
                                                                <td>1</td>
                                                                <td>2</td>
                                                            </tr>
                                                            <tr>
                                                                <td>90-Saurabh </td>
                                                                <td>20 </td>
                                                                <td>0</td>
                                                                <td>0</td>
                                                                <td>1</td>
                                                            </tr>

                                                        </table>

                                                    </div>




      </div>
    );
  }
}






ReactDOM.render(
  <EmployeeAttendence />,
  document.getElementById('root')
);
