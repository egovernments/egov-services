

class WithoutAssignment extends React.Component {


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
                                                                     <th>Action </th>
                                                                </tr>
                                                            </thead>
                                                            <tr>
                                                                <td>89 </td>
                                                                <td>Kumaresh</td>
                                                                <td><a href="#" assign>Assign </a></td>
                                                            </tr>
                                                            <tr>
                                                                <td>90 </td>
                                                                <td>hfhjnl </td>
                                                                <td><a href="#" assign>Assign </a></td>

                                                            </tr>

                                                        </table>
                                                        </div>




    );
  }
}






ReactDOM.render(
  <WithoutAssignment />,
  document.getElementById('root')
);
