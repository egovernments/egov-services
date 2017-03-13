


class LeaveReport extends React.Component {

  close(){
      // widow.close();
      open(location, '_self').close();
  }


  render() {


    return (
                <div>

                                                    <div className="land-table">
                                                        <table className="table table-bordered">
                                                            <thead>
                                                                <tr>
                                                                    <th>Employee Code-Name</th>
                                                                    <th>Application No</th>
                                                                    <th>Leave Type</th>
                                                                    <th>No Of. Days</th>
                                                                    <th>Status</th>
                                                                    <th>Comment </th>
                                                                </tr>
                                                            </thead>
                                                            <tr>
                                                                <td>89-Kumaresh </td>
                                                                <td>123</td>
                                                                <td>casual</td>
                                                                <td>2</td>
                                                                <td>ok</td>
                                                                <td>fine</td>
                                                            </tr>
                                                            <tr>
                                                                <td>90-jbhd </td>
                                                                <td>124 </td>
                                                                <td>seck</td>
                                                                <td>1</td>
                                                                <td>checked</td>
                                                                <td>ok</td>
                                                            </tr>

                                                        </table>

                                                    </div>




      </div>
    );
  }
}






ReactDOM.render(
  <LeaveReport />,
  document.getElementById('root')
);
