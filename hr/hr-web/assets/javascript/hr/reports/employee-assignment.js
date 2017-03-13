

class EmployeeAssignment extends React.Component {


  close(){
      // widow.close();
      open(location, '_self').close();
  }

  
  render() {


    return (


                                                    <div className="land-table">
                                                        <table className="table table-bordered">
                                                            <thead>
                                                                <tr>
                                                                    <th>Employee Code</th>
                                                                    <th>Employee Name</th>
                                                                    <th>Employee Designation</th>
                                                                    <th>Employee Deaprtment</th>
                                                                    <th>From Date</th>
                                                                    <th>TO Date </th>
                                                                </tr>
                                                            </thead>
                                                            <tr>
                                                                <td>89 </td>
                                                                <td>Kumaresh</td>
                                                                <td>Ui</td>
                                                                <td>EGov</td>
                                                                <td>10/12/2010</td>
                                                                <td>10/12/2016</td>
                                                            </tr>
                                                            <tr>
                                                                <td>90 </td>
                                                                <td>hfhjnl </td>
                                                                <td>Ui</td>
                                                                <td>EGov</td>
                                                                <td>10/12/2011</td>
                                                                <td>10/12/2012</td>
                                                            </tr>

                                                        </table>
                                                        </div>




    );
  }
}






ReactDOM.render(
  <EmployeeAssignment />,
  document.getElementById('root')
);
