

class LeaveSummary extends React.Component {

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
                                                                    <th>Employee Code-Name</th>
                                                                    <th>Leave Type</th>
                                                                    <th>Opening Balance</th>
                                                                    <th>Leave Elligible</th>
                                                                    <th>Total Leave Elligible</th>
                                                                    <th>Approved </th>
                                                                    <th>Balance </th>
                                                                </tr>
                                                            </thead>
                                                            <tr>
                                                                <td>89-Kumaresh </td>
                                                                <td>casual</td>
                                                                <td>1</td>
                                                                <td>3</td>
                                                                <td>10</td>
                                                                <td>5</td>
                                                                <td>5</td>
                                                            </tr>
                                                            <tr>

                                                                <td>90-bfkjjdnsk </td>
                                                                <td>casual</td>
                                                                <td>1</td>
                                                                <td>3</td>
                                                                <td>10</td>
                                                                <td>5</td>
                                                                <td>5</td>

                                                            </tr>

                                                        </table>
                                                        </div>




    );
  }
}






ReactDOM.render(
  <LeaveSummary />,
  document.getElementById('root')
);
