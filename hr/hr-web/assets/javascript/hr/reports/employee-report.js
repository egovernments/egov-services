// var EmployeeReportCommon=require("../common/employee-search-common.js");

// function getUrlVars() {
//     var vars = [],
//         hash;
//     var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
//     for (var i = 0; i < hashes.length; i++) {
//         hash = hashes[i].split('=');
//         vars.push(hash[0]);
//         vars[hash[0]] = hash[1];
//     }
//     return vars;
// }


class EmployeeReport extends React.Component {

  render() {

    return (
                <div>

                                                <div className="form-section" >
                                                    <h3 className="pull-left">Employee Details </h3>
                                                    <div className="clearfix"></div>


                                                    <div className="land-table">
                                                        <table className="table table-bordered">
                                                            <thead>
                                                                <tr>
                                                                    <th>Employee Code</th>
                                                                    <th>Employee Name</th>
                                                                    <th>Employee Designation</th>
                                                                    <th>Employee Deaprtment</th>
                                                                    <th>Reports</th>

                                                                </tr>
                                                            </thead>
                                                            <tr>
                                                                <td>89 </td>
                                                                <td>Kumaresh</td>
                                                                <td>Ui</td>
                                                                <td>EGov</td>
                                                                <td><a href="#" Employee>Employee </a></td>
                                                            </tr>
                                                            <tr>
                                                                <td>90 </td>
                                                                <td>hfhjnl </td>
                                                                <td>Ui</td>
                                                                <td>EGov</td>
                                                                <td><a href="#" Employee>Employee </a></td>

                                                            </tr>

                                                        </table>
                                                        </div>
                                                        </div>







      </div>
    );
  }
}






ReactDOM.render(
  <EmployeeReport />,
  document.getElementById('root')
);
